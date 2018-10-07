
package pipe.hla.book.restaurant.transport;
import java.util.*;
import java.net.*;

import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import pipe.hla.book.manager.ManagerNames;
import hla.rti.*;
import pipe.hla.book.restaurant.*;
import se.pitch.prti.*;

public final class Transport {
  //system properties used throughout
  private static String _fileSeparator = System.getProperty("file.separator");
  private static String _userDirectory = System.getProperty("user.dir");

  private TransportFrame _userInterface;
  private int _federateHandle; // -1 when not joined
  private boolean _simulationEndsReceived;
  private FedAmbImpl _fedAmb;
  private String _fedexName;
  private Properties _properties;

  //barriers used to await announcement of synchronization points
  //these must be allocated here lest announcement sneak up on us
  private Barrier _readyToPopulateAnnouncementBarrier = new Barrier();
  private Barrier _readyToRunAnnouncementBarrier = new Barrier();
  private Barrier _readyToResignAnnouncementBarrier = new Barrier();

  //we use Pitch-supplied LogicalTimeDouble
  //and LogicalTimeIntervalDouble to talk to RTI
  private LogicalTime _logicalTime;
  private LogicalTimeInterval _lookahead;
  private LogicalTimeInterval _advanceInterval; //how far to move on each time step
  private LogicalTime _targetTime;              //where we're moving to

  //things dependent on RTI implementation in use
  private RTIambassador _rti;
  public SuppliedParametersFactory _suppliedParametersFactory;
  public SuppliedAttributesFactory _suppliedAttributesFactory;
  public AttributeHandleSetFactory _attributeHandleSetFactory;
  public FederateHandleSetFactory _federateHandleSetFactory;

  //handles and handle sets
  private int _RestaurantClass;
  private int _BoatClass;
  private int _ServingClass;
  private int _privilegeToDeleteObjectAttribute;
  private int _positionAttribute;
  private AttributeHandleSet _positionAttributeAsSet;
  private int _typeAttribute;
  private int _spaceAvailableAttribute;
  private int _cargoAttribute;
  private AttributeHandleSet _BoatAttributes;
  private int _SimulationEndsClass;
  private int _TransferAcceptedClass;
  private int _servingNameParameter;

  private double _boatRate;
  private BoatTable _boatTable;     //table of our Boats
  private CallbackQueue _callbackQueue;
  private Hashtable _servings; //key: instance handle; value: Serving

  public Transport(Properties props) {
    _federateHandle = -1;  //not joined
    _boatTable = new BoatTable();
    _servings = new Hashtable();
    _callbackQueue = new CallbackQueue();
    _simulationEndsReceived = false;

		_userInterface = new TransportFrame();
    _userInterface.finishConstruction(this, _boatTable);
		_userInterface.show();
    _userInterface.lastAdjustments();

    _properties = props;
    //System.out.println("host: " + props.get("RTI_HOST"));
    //System.out.println("port: " + props.get("RTI_PORT"));
    //System.out.println("config: " + props.get("CONFIG"));
		//create RTI implementation
  	try {
		  //_rti = RTI.getRTIambassador(        (String)_properties.get("RTI_HOST"),        Integer.parseInt((String)_properties.get("RTI_PORT")));
      _rti = RTI.getRTIambassador(Constant.HOSTNAME,8989);
		   _userInterface.post("RTIambassador created");
			_fedAmb = new FedAmbImpl(this, _userInterface);

      //do other implementation-specific things
      _suppliedParametersFactory = RTI.suppliedParametersFactory();
      _suppliedAttributesFactory = RTI.suppliedAttributesFactory();
      _attributeHandleSetFactory = RTI.attributeHandleSetFactory();
      _federateHandleSetFactory = RTI.federateHandleSetFactory();
      System.out.println("Transport started.");
		}
		catch (Exception e) {
		  _userInterface.post("Transport: constructor failed: " + e);
		  _userInterface.post("You may as well exit.");
		}
  }

  public static void main(String[] args) {
	  Properties props = parseArgs(args);
    loadProperties(props);
    Transport production = new Transport(props);
    production.mainThread();
  }

  //the main thread
  private void mainThread() {
    Barrier barrier;
    Object[] result;

    try {
      getConfigurationData();

      //create federation execution (if necessary) and join
      _fedexName = (String)_properties.get("FEDEX");
      URL fedURL;
      String urlString =
        (String)_properties.get("CONFIG")        + _fedexName        + ".fed";
      fedURL = new URL(urlString);
      //the federation execution may already exist
      try {
        _rti.createFederationExecution(_fedexName, fedURL);
        _userInterface.post("Federation execution " + _fedexName + " created.");
      }
      catch (FederationExecutionAlreadyExists e) {
        _userInterface.post("Federation execution " + _fedexName
          + " already exists.");
      }
      //join federation execution
      _fedexName = (String)_properties.get("FEDEX");
      _federateHandle = _rti.joinFederationExecution(
        TransportNames._federateType,
        _fedexName,
        _fedAmb);
      _userInterface.post("Joined as federate " + _federateHandle);

      //enable time constraint
      _userInterface.post("Enabling time constraint...");
      barrier = new Barrier();
      _fedAmb.setEnableTimeConstrainedBarrier(barrier);
      _rti.enableTimeConstrained();
      result = barrier.await();
      _logicalTime = (LogicalTime)result[0];
      _userInterface.post("...constraint enabled at " + _logicalTime);

      //enable time regulation
      _logicalTime = new LogicalTimeDouble(
        (new Double((getProperty("Federation.initialTime")))).doubleValue());
      _lookahead = new LogicalTimeIntervalDouble(
        (new Double((getProperty("Transport.lookahead")))).doubleValue());
      _advanceInterval = new LogicalTimeIntervalDouble(
        (new Double((getProperty("Transport.advanceInterval")))).doubleValue());
      _userInterface.post("Enabling time regulation...");
      barrier = new Barrier();
      _fedAmb.setEnableTimeRegulationBarrier(barrier);
      _rti.enableTimeRegulation(_logicalTime, _lookahead);
      result = barrier.await();
      _logicalTime = (LogicalTime)result[0];
      _userInterface.post("...regulation enabled at " + _logicalTime);
      _userInterface.setLogicalTime(((LogicalTimeDouble)_logicalTime).getValue());
      _userInterface.setTimeStateGranted();

      getHandles();
      publish();
      subscribe();

      //Transport achieves ReadyToPopulate and waits for rest of federation
      _readyToPopulateAnnouncementBarrier.await();
      _userInterface.post("Waiting for ReadyToPopulate...");
      barrier = new Barrier(ManagerNames._readyToPopulate);
      _fedAmb.setFederationSynchronizedBarrier(barrier);
      _rti.synchronizationPointAchieved(ManagerNames._readyToPopulate);
      result = barrier.await();
      _userInterface.post("...federation synchronized.");

      makeInitialInstances();

      //Transport achieves ReadyToRun and waits for rest of federation
      _readyToRunAnnouncementBarrier.await();
      _userInterface.post("Waiting for ReadyToRun...");
      barrier = new Barrier(ManagerNames._readyToRun);
      _fedAmb.setFederationSynchronizedBarrier(barrier);
      _rti.synchronizationPointAchieved(ManagerNames._readyToRun);
      result = barrier.await();
      _userInterface.post("...federation synchronized.");

      _rti.enableAsynchronousDelivery();

      //advance time in steps until SimulationEnds received
      _targetTime = new LogicalTimeDouble(0.0);
      _targetTime.setTo(_logicalTime);
    timeLoop:
      while (!_simulationEndsReceived) {
        //advance by a step
        _targetTime.increaseBy(_advanceInterval);
        _userInterface.setTimeStateAdvancing();
        _rti.timeAdvanceRequest(_targetTime);
        //chew through all the events we receive from the RTI
        boolean wasTimeAdvanceGrant;
        do {
          Callback callback = _callbackQueue.dequeue();
          wasTimeAdvanceGrant = callback.dispatch();
          if (_simulationEndsReceived) break timeLoop;
        } while (!wasTimeAdvanceGrant);
        updateInternalStateAtNewTime();
      }
      _userInterface.post("SimulationEnds received.");

      //Transport achieves ReadyToResign and waits for rest of federation
      _readyToResignAnnouncementBarrier.await();
      _userInterface.post("Waiting for ReadyToResign...");
      barrier = new Barrier(ManagerNames._readyToResign);
      _fedAmb.setFederationSynchronizedBarrier(barrier);
      _rti.synchronizationPointAchieved(ManagerNames._readyToResign);
      result = barrier.await();
      _userInterface.post("...federation synchronized.");
      _rti.resignFederationExecution(ResignAction.DELETE_OBJECTS_AND_RELEASE_ATTRIBUTES);
      _federateHandle = -1;
      _userInterface.post("All done.");

    }
    catch (Exception e) {
      _userInterface.post("Exception in main thread: " + e);
      e.printStackTrace();
    }
  }

  private void getHandles()
  throws RTIexception
  {
    _RestaurantClass = _rti.getObjectClassHandle(RestaurantNames._RestaurantClassName);
    _ServingClass = _rti.getObjectClassHandle(RestaurantNames._ServingClassName);
    _BoatClass = _rti.getObjectClassHandle(RestaurantNames._BoatClassName);
    _privilegeToDeleteObjectAttribute = _rti.getAttributeHandle(
      RestaurantNames._privilegeToDeleteObjectAttributeName,
      _RestaurantClass);
    _positionAttribute = _rti.getAttributeHandle(
      RestaurantNames._positionAttributeName,
      _RestaurantClass);
    _typeAttribute = _rti.getAttributeHandle(
      RestaurantNames._typeAttributeName,
      _ServingClass);
    _spaceAvailableAttribute = _rti.getAttributeHandle(
      RestaurantNames._spaceAvailableAttributeName,
      _BoatClass);
    _cargoAttribute = _rti.getAttributeHandle(
      RestaurantNames._cargoAttributeName,
      _BoatClass);

    _positionAttributeAsSet = _attributeHandleSetFactory.create();
    _positionAttributeAsSet.add(_positionAttribute);

    _BoatAttributes = _attributeHandleSetFactory.create();
    _BoatAttributes.add(_privilegeToDeleteObjectAttribute);
    _BoatAttributes.add(_positionAttribute);
    _BoatAttributes.add(_spaceAvailableAttribute);
    _BoatAttributes.add(_cargoAttribute);

    _SimulationEndsClass =
      _rti.getInteractionClassHandle(ManagerNames._SimulationEndsClassName);
    _TransferAcceptedClass =
      _rti.getInteractionClassHandle(RestaurantNames._TransferAcceptedClassName);

    _servingNameParameter = _rti.getParameterHandle(
      RestaurantNames._servingNameParameterName,
      _TransferAcceptedClass);
  }

  private void publish()
  throws RTIexception
  {
    //publish Boat (including privToDelete)
    _rti.publishObjectClass(_BoatClass, _BoatAttributes);
    //publish Serving class position attribute because we update it
    _rti.publishObjectClass(_ServingClass, _positionAttributeAsSet);
    //publish interaction because we send it
    _rti.publishInteractionClass(_TransferAcceptedClass);
  }

  private void subscribe()
  throws RTIexception
  {
    _rti.subscribeInteractionClass(_SimulationEndsClass);
    _rti.subscribeObjectClassAttributes(_ServingClass, _positionAttributeAsSet);
  }

  private void makeInitialInstances()
  throws RTIexception
  {
    _boatRate = (new Double((getProperty("Transport.Boats.rate")))).doubleValue();
    int numberOfBoats = Integer.parseInt(getProperty("Transport.numberOfBoats"));
    for (int serial = 0; serial < numberOfBoats; ++serial) {
      String prop = "Transport.Boat.position." + serial;
      double position = (new Double((getProperty(prop)))).doubleValue();
      String instanceName = "B_" + _federateHandle + "_" + serial;
      int handle = _rti.registerObjectInstance(_BoatClass, instanceName);
      _boatTable.add(
        handle,
        instanceName,
        position,
        Boat.EMPTY,
        0,
        "");
      updateBoatBySerial(serial);
    }
  }

  private void moveBoats()
  throws RTIexception
  {
    try {
      int boatCount = _boatTable.getRowCount();
      for (int serial = 0; serial < boatCount; ++serial) {
        //update table
        double positionAsDouble = _boatTable.getPositionBySerial(serial);
        positionAsDouble += _boatRate
          * ((LogicalTimeIntervalDouble)_advanceInterval).getValue();
        if (positionAsDouble >= 360.0) positionAsDouble -= 360.0;
        _boatTable.setPositionBySerial(serial, positionAsDouble);
        //update Boat attributes
        updateBoatBySerial(serial);
        //if necessary, update value for Serving carried on Boat
        if (_boatTable.getStateBySerial(serial) == Boat.LOADED) {
          int servingHandle = _boatTable.getServingBySerial(serial);
          updateServing(servingHandle, positionAsDouble);
        }
      }
    }
    catch (TransportInternalError e) {
      _userInterface.post("moveBoats: " + e.getMessage());
    }
  }

  private void updateServing(int servingHandle, double positionAsDouble)
  throws RTIexception, TransportInternalError
  {
    try {
      Serving serving = (Serving)_servings.get(new Integer(servingHandle));
      if (serving == null) throw new TransportInternalError("Serving "
        + servingHandle + " not owned.");
      Position position = new Position(positionAsDouble, Position.ON_CANAL);
      SuppliedAttributes sa = _suppliedAttributesFactory.create(1);
      sa.add(_positionAttribute, position.encode());
      LogicalTime sendTime = new LogicalTimeDouble(0.0);
      sendTime.setTo(_logicalTime);
      sendTime.increaseBy(_lookahead);
      EventRetractionHandle erh =
        _rti.updateAttributeValues(servingHandle, sa, null, sendTime);
    }
    catch (AttributeNotOwned e) {
      //the RTI may transfer ownership away before before we can adjust state
      //write informative message and expect us to catch up later
      _userInterface.post("Lost ownership of serving " + servingHandle
        + " before AODN arrived");
    }
  }

  private void updateBoatBySerial(int serial)
  throws RTIexception
  {
    updateBoat(_boatTable.getHandleBySerial(serial));
  }

  private void updateBoat(int handle)
  throws RTIexception
  {
    //update attribute values for Boat
    int serial = _boatTable.getSerialByHandle(handle);
    Position position = new Position(
      _boatTable.getPositionBySerial(serial),
      Position.ON_CANAL);
    boolean spaceAvailable;
    String cargo;
    int servingHandle = 0;
    if (_boatTable.getStateBySerial(serial) == Boat.LOADED) {
      spaceAvailable = false;
      servingHandle = _boatTable.getServingBySerial(serial);
      cargo = _rti.getObjectInstanceName(servingHandle);
    }
    else {
      spaceAvailable = true;
      cargo = "";
    }
    SuppliedAttributes sa = _suppliedAttributesFactory.create(3);
    sa.add(_positionAttribute, position.encode());
    sa.add(_spaceAvailableAttribute, SpaceAvailable.encode(spaceAvailable));
    sa.add(_cargoAttribute, InstanceName.encode(cargo));
    LogicalTime sendTime = new LogicalTimeDouble(0.0);
    sendTime.setTo(_logicalTime);
    sendTime.increaseBy(_lookahead);
    EventRetractionHandle erh = _rti.updateAttributeValues(
      _boatTable.getHandleBySerial(serial),
      sa,
      null,
      sendTime);
      /*
    _userInterface.post("Updating boat " + _boatTable.getNameBySerial(serial)
      + " at time " + sendTime);
      */
  }

  //federate ambassador calls this when it gets an announcement
  public void recordSynchronizationPointAnnouncement(String label) {
    if (label.equals(ManagerNames._readyToPopulate))
      _readyToPopulateAnnouncementBarrier.lower(null);
    else if (label.equals(ManagerNames._readyToRun))
      _readyToRunAnnouncementBarrier.lower(null);
    else if (label.equals(ManagerNames._readyToResign))
      _readyToResignAnnouncementBarrier.lower(null);
    else
      _userInterface.post("INFO: unexpected sync point announced: " + label);
  }

  private void updateInternalStateAtNewTime()
  throws RTIexception
  {
    //we're at the new logical time: update the Boats
    moveBoats();
  }

  private void getConfigurationData() {
  }

  //defined so that a missing property doesn't cause a crash later
  private String getProperty(String name) {
    String value = (String)_properties.get(name);
    if (value == null) {
      System.err.println("Property " + name + " not defined; exiting.");
      System.exit(1);
    }
    return value;
  }

  //used by UI
  public boolean isJoined() { return _federateHandle >= 0; }

  private static Properties parseArgs(String args[]) {
    Properties props = new Properties();
    //default values
    try {
      //host name for Central RTI Component
      String defaultRTIhost = InetAddress.getLocalHost().getHostName();
      String cmdHost = System.getProperty("RTI_HOST");
      //System.out.println("Cmd line had " + cmdHost);
      if (cmdHost == null) props.put("RTI_HOST", defaultRTIhost);
      else props.put("RTI_HOST", cmdHost);

      //port number for Central RTI component
      String defaultRTIport = "8989";
      String cmdPort = System.getProperty("RTI_PORT");
      //System.out.println("Cmd line had " + cmdPort);
      if (cmdPort == null) props.put("RTI_PORT", defaultRTIport);
      else props.put("RTI_PORT", cmdPort);

      //form URL
      String urlString = System.getProperty("CONFIG",
        "file:" + _userDirectory + _fileSeparator + "config" + _fileSeparator);
      props.put("CONFIG", urlString);
      //System.out.println("Config URL: " + props.get("CONFIG"));

      //federation execution name
      String defaultFedExName = "restaurant_1";
      String cmdFedExName = System.getProperty("FEDEX");
      if (cmdFedExName == null) props.put("FEDEX", defaultFedExName);
      else props.put("FEDEX", cmdFedExName);
    }
    catch (UnknownHostException e) {
      System.out.println("Transport.parseArgs: default arguments failed: " + e);
      System.exit(1);
    }
    return props;
  }

  //load other properties from URL
  private static void loadProperties(Properties props) {
    try {
      //form URL for properties
      String urlString =
        (String)props.get("CONFIG")
        + (String)props.get("FEDEX")
        + ".props";
      URL propsURL = new URL(urlString);
      props.load(propsURL.openStream());
    }
    catch (Exception e) {
      System.out.println("Transport failed to load properties: " + e);
      System.exit(1);
    }
  }

  //represents one callback coming from RTI
  //This class is defined within Production so the dispatch routines
  //of its subclasses have the context of Production
  public abstract class Callback {
    //returns true if event was a grant
    public abstract boolean dispatch()
    throws RTIexception;
  }

  //represents one callback that is an event (carries a time)
  public abstract class ExternalEvent extends Callback {
    protected LogicalTime _time;

    public LogicalTime getTime() { return _time; }
    //returns true if event was a grant
    public abstract boolean dispatch()
    throws RTIexception;
  }

  public void queueAttributeOwnershipAcquisitionNotificationCallback(
    int theObject,
    AttributeHandleSet theAttributes)
  {
    Callback callback = new AOANcallback(
      theObject,
      theAttributes);
    _callbackQueue.enqueue(callback);
  }

  public final class AOANcallback extends Callback {
    private int _object;
    private AttributeHandleSet _attributes;

    public AOANcallback(
    int theObject,
    AttributeHandleSet theAttributes)
    {
      _object = theObject;
      _attributes = theAttributes;
    }

    public boolean dispatch() {
      try {
        //is the object a Serving?
        int objectClass = _rti.getObjectClass(_object);
        if (objectClass != _ServingClass) throw new
          AttributeAcquisitionWasNotRequested("not Serving class: " + objectClass);
        //is it the position attribute?
        if (!_attributes.equals(_positionAttributeAsSet)) throw new
          AttributeAcquisitionWasNotRequested("not position: " + _attributes);
        int boatSerial = _boatTable.getBoatForServing(_object);
        int boatHandle = _boatTable.getHandleBySerial(boatSerial);
        if (boatSerial < 0) throw new TransportInternalError(
          "No boat was waiting for Serving " + _object);
        String servingName = _rti.getObjectInstanceName(_object);
        _userInterface.post("Acquired serving " + servingName);
        //change Serving state
        /*
        Serving serving = new Serving();
        serving._handle = _object;
        serving._name = servingName;
        Position newServingPosition = new Position(
          _boatTable.getPositionBySerial(boatSerial),
          Position.ON_CANAL);
        serving._position = newServingPosition;
        serving._positionState = AttributeState.OWNED_INCONSISTENT;
        _servings.put(new Integer(_object), serving);
        */
        //we should have discovered this Serving
        Serving serving = (Serving)(_servings.get(new Integer(_object)));
        if (serving == null) throw new TransportInternalError(
          "Serving " + _object + " not previously discovered.");
        serving._positionState = AttributeState.OWNED_INCONSISTENT;
        Position newServingPosition = new Position(
          _boatTable.getPositionBySerial(boatSerial),
          Position.ON_CANAL);
        serving._position = newServingPosition;
        //change Boat state
        _boatTable.setState(boatHandle, Boat.LOADED);
        _boatTable.setCargoBySerial(boatSerial, servingName);
        _boatTable.setServing(boatHandle, _object);
        _boatTable.setSpaceAvailableBySerial(boatSerial, false);
        //update Boat attributes
        SuppliedAttributes sa = _suppliedAttributesFactory.create(2);
        sa.add(_spaceAvailableAttribute, SpaceAvailable.encode(false));
        sa.add(_cargoAttribute, InstanceName.encode(servingName));
        LogicalTime sendTime = new LogicalTimeDouble(0.0);
        sendTime.setTo(_logicalTime);
        sendTime.increaseBy(_lookahead);
        EventRetractionHandle erh = _rti.updateAttributeValues(
          boatHandle, sa, null, sendTime);
        //update Serving position attribute
        sa = _suppliedAttributesFactory.create(1);
        sa.add(_positionAttribute, newServingPosition.encode());
        erh = _rti.updateAttributeValues(_object, sa, null, sendTime);
        //make Serving position available for acquisition by Consumption
        _rti.negotiatedAttributeOwnershipDivestiture(_object, _attributes, null);
      }
      catch (TransportInternalError e) {
        _userInterface.post("ERROR AttrOwnAcqNotif: " + e.getMessage());
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR AttrOwnAcqNotif: " + e);
      }
      return false;
    }
  } //end AttributeOwnershipAcquisitionNotificationCallback

  public void queueAttributeOwnershipDivestitureNotificationCallback(
    int theObject,
    AttributeHandleSet theAttributes)
  {
    Callback callback = new AODNcallback(
      theObject,
      theAttributes);
    _callbackQueue.enqueue(callback);
  }

  public final class AODNcallback extends Callback {
    private int _object;
    private AttributeHandleSet _attributes;

    public AODNcallback(
    int theObject,
    AttributeHandleSet theAttributes)
    {
      _object = theObject;
      _attributes = theAttributes;
    }

    public boolean dispatch() {
      try {
        //is the object a Serving?
        int objectClass = _rti.getObjectClass(_object);
        if (objectClass != _ServingClass) throw new
          AttributeAcquisitionWasNotRequested("not Serving class: " + objectClass);
        //is it the position attribute?
        if (!_attributes.equals(_positionAttributeAsSet)) throw new
          AttributeAcquisitionWasNotRequested("not position: " + _attributes);
        int boatSerial = _boatTable.getBoatForServing(_object);
        int boatHandle = _boatTable.getHandleBySerial(boatSerial);
        if (boatSerial < 0) throw new TransportInternalError(
          "No boat was carrying Serving " + _object);
        //was the Boat in the right state?
        if (_boatTable.getStateBySerial(boatSerial) != Boat.LOADED) throw new
          TransportInternalError("Boat " + boatHandle + " not loaded");
        String servingName = _rti.getObjectInstanceName(_object);
        _userInterface.post("Divesting serving " + servingName);
        //change Serving state
        Integer servingKey = new Integer(_object);
        Serving serving = (Serving)(_servings.get(servingKey));
        if (serving == null) throw new ObjectNotKnown("Serving " + _object
          + " not known to federate");
        serving._positionState = AttributeState.REFLECTED;
        //change Boat state
        _boatTable.setState(boatHandle, Boat.EMPTY);
        _boatTable.setCargoBySerial(boatSerial, "");
        _boatTable.setSpaceAvailableBySerial(boatSerial, true);
        _boatTable.setServing(boatHandle, 0);
        //update Boat attributes
        SuppliedAttributes sa = _suppliedAttributesFactory.create(2);
        sa.add(_spaceAvailableAttribute, SpaceAvailable.encode(true));
        sa.add(_cargoAttribute, InstanceName.encode(""));
        LogicalTime sendTime = new LogicalTimeDouble(0.0);
        sendTime.setTo(_logicalTime);
        sendTime.increaseBy(_lookahead);
        EventRetractionHandle erh = _rti.updateAttributeValues(
          boatHandle, sa, null, sendTime);
      }
      catch (TransportInternalError e) {
        _userInterface.post("ERROR AttrOwnDivNotif: " + e.getMessage());
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR AttrOwnDivNotif: " + e);
      }
      return false;
    }
  } //end AttributeOwnershipDivestitureNotificationCallback

  public void queueAttributeOwnershipUnavailableCallback(
    int theObject,
    AttributeHandleSet theAttributes)
  {
    Callback callback = new AttributeOwnershipUnavailableCallback(
      theObject,
      theAttributes);
    _callbackQueue.enqueue(callback);
  }

  public final class AttributeOwnershipUnavailableCallback extends Callback {
    private int _object;
    private AttributeHandleSet _attributes;

    public AttributeOwnershipUnavailableCallback(
    int theObject,
    AttributeHandleSet theAttributes)
    {
      _object = theObject;
      _attributes = theAttributes;
    }

    public boolean dispatch() {
      try {
        //we'll assume the object is a Serving and the attribute is position
        int boatSerial = _boatTable.getBoatForServing(_object);
        int boatHandle = _boatTable.getHandleBySerial(boatSerial);
        if (boatSerial < 0) throw new TransportInternalError(
          "No boat was waiting for Serving " + _object);
        String servingName = _rti.getObjectInstanceName(_object);
        _userInterface.post("Serving " + servingName + " was unvailable");
        //change Boat state
        _boatTable.setState(boatHandle, Boat.EMPTY);
        _boatTable.setCargoBySerial(boatSerial, "");
        _boatTable.setServing(boatHandle, 0);
        _boatTable.setSpaceAvailableBySerial(boatSerial, true);
      }
      catch (TransportInternalError e) {
        _userInterface.post("ERROR AttrOwnUnav: " + e.getMessage());
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR AttrOwnUnav: " + e);
      }
      return false;
    }
  } //end AttributeOwnershipUnavailableCallback

  public void queueDiscoverObjectInstanceCallback(
    int handle,
    int objectClass,
    String name) {
    Callback callback = new DiscoverObjectInstanceCallback(
      handle,
      objectClass,
      name);
    _callbackQueue.enqueue(callback);
  }

  public final class DiscoverObjectInstanceCallback
    extends Callback
  {
    private int _instanceHandle;
    private int _objectClass;
    private String _instanceName;

    public DiscoverObjectInstanceCallback(
      int handle,
      int objectClass,
      String name) {
      _instanceHandle = handle;
      _objectClass = objectClass;
      _instanceName = name;
    }

    public boolean dispatch() {
      try {
        if (_objectClass != _ServingClass) throw new TransportInternalError(
          "instance " + _instanceHandle + "(" + _instanceName + ") of class " + _objectClass
          + " not a Serving!");
        Serving newServing = new Serving();
        newServing._handle = _instanceHandle;
        newServing._name = _instanceName;
        newServing._privilegeToDeleteObjectState = AttributeState.DISCOVERED;
        newServing._positionState = AttributeState.DISCOVERED;
        newServing._typeState = AttributeState.DISCOVERED;
        _servings.put(new Integer(_instanceHandle), newServing);
        _userInterface.post("Discovered Serving " + _instanceHandle + "(" + _instanceName + ")");
      }
      catch (TransportInternalError e) {
        _userInterface.post("discoverObjectInstance: " + e.getMessage());
      }
      return false;
    }
  } //end DiscoverObjectInstanceCallback

  public void queueGrantEvent(LogicalTime time) {
    ExternalEvent event = new GrantEvent(time);
    _callbackQueue.enqueue(event);
  }

  public final class GrantEvent extends ExternalEvent {

    public GrantEvent(LogicalTime time) {
      _time = time;
    }

    //this dispatch doesn't do as much as the others because actions upon
    //grant are handled differently.
    public boolean dispatch() {
      _logicalTime.setTo(_time);
      //_userInterface.post("...granted to " + _logicalTime);
      _userInterface.setLogicalTime(((LogicalTimeDouble)_logicalTime).getValue());
      _userInterface.setTimeStateGranted();
      return true;
    }
  } //end GrantEvent

  public void queueProvideAttributeValueUpdateCallback(
    int objectHandle,
    AttributeHandleSet theAttributes)
  {
    Callback callback = new ProvideAttributeValueUpdateCallback(
      objectHandle,
      theAttributes);
    _callbackQueue.enqueue(callback);
  }

  public final class ProvideAttributeValueUpdateCallback extends Callback {
    int _object;
    AttributeHandleSet _attributes;

    public ProvideAttributeValueUpdateCallback(
      int objectHandle,
      AttributeHandleSet attributes)
    {
      _object = objectHandle;
      _attributes = attributes;
    }

    public boolean dispatch()
    throws RTIexception
    {
      try {
        int classHandle = _rti.getObjectClass(_object);
        if (classHandle == _ServingClass) {
          _userInterface.post("ProvAttrUpd for Serving " + _object);
          Serving serving = (Serving)_servings.get(new Integer(_object));
          if (serving == null) throw new ObjectNotKnown("Serving " + _object
            + " not known");
          //it's overkill checking the requested attributes, but serves as example
          HandleIterator iter = _attributes.handles();
          SuppliedAttributes sa = _suppliedAttributesFactory.create(1);
          for (int h = iter.first(); iter.isValid(); h = iter.next()) {
            if (h == _positionAttribute) {
              if (serving._positionState != AttributeState.OWNED_INCONSISTENT
                && serving._positionState != AttributeState.OWNED_CONSISTENT)
                throw new AttributeNotOwned(
                "Position attribute not owned for instance " + _object);
              sa.add(_positionAttribute, serving._position.encode());
              serving._positionState = AttributeState.OWNED_CONSISTENT;
            }
            else throw new AttributeNotKnown("Attribute " + h
              + " not known for instance " + _object);
          }
          //we can send update now because Transport is time-stepped
          LogicalTime sendTime = new LogicalTimeDouble(0.0);
          sendTime.setTo(_logicalTime);
          sendTime.increaseBy(_lookahead);
          EventRetractionHandle erh =
            _rti.updateAttributeValues(serving._handle, sa, null, sendTime);
        }
        else if (classHandle == _BoatClass) {
          _userInterface.post("ProvAttrUpd for Boat " + _object);
          //this will throw ArrayIndexOutOfBoundsException if not found
          int serial = _boatTable.getSerialByHandle(_object);
          //we'll update all attributes of Boat: it's easy & respects I/F spec
          updateBoat(_object);
        }
        else throw new ObjectNotKnown("Object " + _object +
          " is of unknown class " + classHandle);
      }
      catch (ArrayIndexOutOfBoundsException e) {
        throw new ObjectNotKnown("Instance " + _object + " not known.");
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR provideAttributeValueUpdate: " + e);
      }
      return false;
    }
  } //end ProvideAttributeValueUpdateCallback

  public void queueReceiveInteractionCallback(
    int interactionClass,
    ReceivedInteraction theInteraction)
  {
    Callback callback = new ReceiveInteractionCallback(
      interactionClass,
      theInteraction);
    _callbackQueue.enqueue(callback);
  }

  public final class ReceiveInteractionCallback extends Callback {
    int _class;
    ReceivedInteraction _interaction;

    public ReceiveInteractionCallback(
      int interactionClass,
      ReceivedInteraction theInteraction)
    {
      _class = interactionClass;
      _interaction = theInteraction;
    }

    public boolean dispatch()
    throws RTIexception
    {
      if (_class != _SimulationEndsClass) {
        _userInterface.post("ERROR unexpected interaction class " + _class);
        throw new InteractionClassNotKnown("class not " + _SimulationEndsClass);
      }
      //updates of booleans are atomic: not synch problem
      _simulationEndsReceived = true;
      return false;
    }
  } //end ReceiveInteractionCallback

  public void queueReflectAttributeValuesEvent(
    LogicalTime time,
    int objectHandle,
    ReflectedAttributes attributes,
    byte[] tag)
  {
    ExternalEvent event = new ReflectAttributeValuesEvent(
      time,
      objectHandle,
      attributes,
      tag);
    _callbackQueue.enqueue(event);
  }

  public final class ReflectAttributeValuesEvent extends ExternalEvent {
    int _object;
    ReflectedAttributes _attributes;
    byte[] _tag;

    public ReflectAttributeValuesEvent(
      LogicalTime time,
      int objectHandle,
      ReflectedAttributes attributes,
      byte[] tag)
    {
      _time = time;
      _object = objectHandle;
      _attributes = attributes;
      _tag = tag;
    }

    public boolean dispatch()
    throws RTIexception
    {
      try {
        int oclass = _rti.getObjectClass(_object);
        if (oclass == _ServingClass) reflectServing();
        else throw new TransportInternalError (
          "unexpected class " + oclass + " instance " + _object);
      }
      catch (TransportInternalError e) {
        _userInterface.post("ERROR: reflectAttributeValues: " + e.getMessage());
      }
      catch (CouldNotDecode e) {
        _userInterface.post("ERROR: reflectAttributeValues: " + e);
      }
      catch (ArrayIndexOutOfBounds e) {
        _userInterface.post("ERROR: reflectAttributeValues: " + e);
      }
      return false;
    }

    private void reflectServing()
    throws TransportInternalError, RTIexception
    {
      //find the Serving in our collection
      Serving serving = (Serving)_servings.get(new Integer(_object));
      if (serving == null) throw new TransportInternalError ("unknown Serving " + _object);
      /*
      //decode and store new attribute values
      int attrCount = _attributes.size();
      for (int i = 0; i < attrCount; ++i) {
        int handle = _attributes.getAttributeHandle(i);
        if (handle == _positionAttribute) {
          serving._position =
            new Position(_attributes.getValueReference(i), 0);
          serving._positionState = AttributeState.REFLECTED; //we have data
        }
        else throw new ConsumptionInternalError("unknown attribute " + handle);
      }
      _userInterface.post("Reflected serving " + _rti.getObjectInstanceName(_object));
      */
    }
  } //end ReflectAttributeValuesEvent

  public void queueRemoveObjectInstanceEvent(
    int objectHandle)
  {
    ExternalEvent event = new RemoveObjectInstanceEvent(
      objectHandle);
    _callbackQueue.enqueue(event);
  }

  public final class RemoveObjectInstanceEvent extends ExternalEvent {
    int _object;

    public RemoveObjectInstanceEvent(
      int objectHandle)
    {
      _object = objectHandle;
    }

    public boolean dispatch()
    throws RTIexception
    {
      try {
        Integer servingKey = new Integer(_object);
        Serving serving = (Serving)_servings.get(servingKey);
        if (serving == null) throw new TransportInternalError(
          "Object " + _object + " not known");
        _userInterface.post("Serving " + serving._name + " being removed");
        //Hashtables are synchronized
        _servings.remove(servingKey);
      }
      catch (TransportInternalError e) {
        _userInterface.post("ERROR: removeObjectInstance: " + e.getMessage());
      }
      return false;
    }
  } //end RemoveObjectInstanceCallback

  public void queueRequestAttributeOwnershipAssumptionCallback(
    int theObject,
    AttributeHandleSet theAttributes,
    byte[] userSuppliedTag)
  {
    Callback callback = new RAOAcallback(
      theObject,
      theAttributes,
      userSuppliedTag);
    _callbackQueue.enqueue(callback);
  }

  public final class RAOAcallback extends Callback {
    private int _object;
    private AttributeHandleSet _attributes;
    private byte[] _tag;

    public RAOAcallback(
      int theObject,
      AttributeHandleSet theAttributes,
      byte[] userSuppliedTag)
    {
      _object = theObject;
      _attributes = theAttributes;
      _tag = userSuppliedTag;
    }

    public boolean dispatch() {
      String tagString = new String(_tag);
      try {
        //the tag should contain the name of a Boat instance
        int boatHandle = _rti.getObjectInstanceHandle(tagString);
        int boatSerial = _boatTable.getSerialByHandle(boatHandle);
        //we're going to assume that the object is a Serving and the
        //attribute set contains position
        String objectName = _rti.getObjectInstanceName(_object);
        if (_boatTable.getStateBySerial(boatSerial) == Boat.EMPTY) {
          _userInterface.post("Boat " + boatHandle + " agrees to acquire serving"
            + objectName);
          //we'll accept this offer: send interaction
          SuppliedParameters sp = _suppliedParametersFactory.create(1);
          sp.add(_servingNameParameter, InstanceName.encode(objectName));
          LogicalTime sendTime = new LogicalTimeDouble(0.0);
          sendTime.setTo(_logicalTime);
          sendTime.increaseBy(_lookahead);
          EventRetractionHandle erh = _rti.sendInteraction(_TransferAcceptedClass,sp,null,sendTime);
          //now ask for ownership of the attribute
          _rti.attributeOwnershipAcquisitionIfAvailable(_object,_positionAttributeAsSet);
          //update internal state of Boat
          _boatTable.setState(boatHandle, Boat.AWAITING_SERVING);
          //record the Serving so we know later which Boat it goes with
          _boatTable.setServing(boatHandle, _object);
        }
        else {
          //otherwise, we don't reply to request to assume
          _userInterface.post("Boat " + boatHandle + " spurns offer of serving "
            + _rti.getObjectInstanceName(_object));
        }
      }
      catch (ArrayIndexOutOfBoundsException e) {
        _userInterface.post("ERROR ReqAttrOwnAssump: tag " + tagString
          + " corresponds to no known Boat");
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR ReqAttrOwnAssump: " + e);
      }
      return false;
    }
  } //end RequestAttributeOwnershipAssumptionCallback
}
