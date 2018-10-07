

package pipe.hla.book.restaurant.consumption;
import java.util.*;
import java.net.*;

import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import pipe.hla.book.manager.ManagerNames;
import hla.rti.*;
import pipe.hla.book.restaurant.*;
import se.pitch.prti.*;

public final class Consumption {
  //system properties used throughout
  private static String _fileSeparator = System.getProperty("file.separator");
  private static String _userDirectory = System.getProperty("user.dir");

  private ConsumptionFrame _userInterface;
  private int _federateHandle; // -1 when not joined
  private boolean _simulationEndsReceived;
  private FedAmbImpl _fedAmb;
  private String _fedexName;
  private Properties _properties;
  private int _servingsConsumed;
  private int _servingsToConsume; //target to end simulation

  //barriers used to await announcement of synchronization points
  //these must be allocated here lest announcement sneak up on us
  private Barrier _readyToPopulateAnnouncementBarrier = new Barrier();
  private Barrier _readyToRunAnnouncementBarrier = new Barrier();
  private Barrier _readyToResignAnnouncementBarrier = new Barrier();

  //we use Pitch-supplied LogicalTimeDouble
  //and LogicalTimeIntervalDouble to talk to RTI
  private LogicalTime _logicalTime;
  private LogicalTimeInterval _lookahead;

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
  private int _ActorClass;
  private int _DinerClass;
  private int _privilegeToDeleteObjectAttribute;
  private AttributeHandleSet _privilegeToDeleteObjectAttributeAsSet;
  private int _positionAttribute;
  private AttributeHandleSet _positionAttributeAsSet;
  private int _spaceAvailableAttribute;
  private int _cargoAttribute;
  private int _dinerStateAttribute;
  private int _servingNameAttribute;
  private AttributeHandleSet _BoatAttributes;
  private AttributeHandleSet _DinerAttributes;
  private int _SimulationEndsClass;

  private DinerTable _dinerTable;     //table of our notional chefs
  private Hashtable _servings;      //key: instance handle; value: Serving
  private Hashtable _knownBoats;    //key: instance handle; value: Boat
  private InternalQueue _internalQueue;
  private CallbackQueue _callbackQueue;
  private double _dinersReach;
  private LogicalTimeInterval[] _consumptionTimes;

  public Consumption(Properties props) {
    _federateHandle = -1;  //not joined
    _servings = new Hashtable();
    _knownBoats = new Hashtable();
    _dinerTable = new DinerTable();
    _internalQueue = new InternalQueue();
    _callbackQueue = new CallbackQueue();
    _simulationEndsReceived = false;
    _servingsConsumed = 0;

		_userInterface = new ConsumptionFrame();
    _userInterface.finishConstruction(this, _dinerTable);
		_userInterface.show();
    _userInterface.lastAdjustments();

    _properties = props;
    //System.out.println("host: " + props.get("RTI_HOST"));
    //System.out.println("port: " + props.get("RTI_PORT"));
    //System.out.println("config: " + props.get("CONFIG"));
		//create RTI implementation
  	try {
		 // _rti = RTI.getRTIambassador(        (String)_properties.get("RTI_HOST"),        Integer.parseInt((String)_properties.get("RTI_PORT")));
      _rti = RTI.getRTIambassador(Constant.HOSTNAME,8989);
		  _userInterface.post("RTIambassador created");
			_fedAmb = new FedAmbImpl(this, _userInterface);

      //do other implementation-specific things
      _suppliedParametersFactory = RTI.suppliedParametersFactory();
      _suppliedAttributesFactory = RTI.suppliedAttributesFactory();
      _attributeHandleSetFactory = RTI.attributeHandleSetFactory();
      _federateHandleSetFactory = RTI.federateHandleSetFactory();
      System.out.println("Consumption started.");
		}
		catch (Exception e) {
		  _userInterface.post("Consumption: constructor failed: " + e);
		  _userInterface.post("You may as well exit.");
		}
  }

  public static void main(String[] args) {
	  Properties props = parseArgs(args);
    loadProperties(props);
    Consumption consumption = new Consumption(props);
    consumption.mainThread();
  }

  //called when we get a time advance grant
  private void checkInternalQueue()
  throws RTIexception
  {
    while (_internalQueue.getTimeAtHead().isLessThanOrEqualTo(_logicalTime)) {
      InternalEvent event = _internalQueue.dequeue();
      /*
      _userInterface.post("Dequeued internal event at " + event.getTime()
        + ", diner: " + event.getDiner());
        */
      event.dispatch();
    }
  }

  public void checkEndOfSimulation()
  throws RTIexception
  {
     if (_servingsConsumed >= _servingsToConsume) {
      SuppliedParameters sp = _suppliedParametersFactory.create(0);
      _rti.sendInteraction(_SimulationEndsClass, sp, null);
      _simulationEndsReceived = true;
     }
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
        (String)_properties.get("CONFIG")  + _fedexName + ".fed";
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
      _federateHandle = _rti.joinFederationExecution(ConsumptionNames._federateType,_fedexName,_fedAmb);
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
      _logicalTime = new LogicalTimeDouble(        (new Double((getProperty("Federation.initialTime")))).doubleValue());
      _lookahead = new LogicalTimeIntervalDouble(        (new Double((getProperty("Consumption.lookahead")))).doubleValue());
      _servingsToConsume = Integer.parseInt(getProperty("Federation.servingsToConsume"));
      _userInterface.post("Enabling time regulation...");
      barrier = new Barrier();
      _fedAmb.setEnableTimeRegulationBarrier(barrier);
      _rti.enableTimeRegulation(_logicalTime, _lookahead);
      result = barrier.await();
      _logicalTime.setTo((LogicalTime)result[0]);
      _userInterface.post("...regulation enabled at " + _logicalTime);
      _userInterface.setLogicalTime(((LogicalTimeDouble)_logicalTime).getValue());
      _userInterface.setTimeStateGranted();

      getHandles();
      publish();
      subscribe();

      //Consumption achieves ReadyToPopulate and waits for rest of federation
      _readyToPopulateAnnouncementBarrier.await();
      _userInterface.post("Waiting for ReadyToPopulate...");
      barrier = new Barrier(ManagerNames._readyToPopulate);
      _fedAmb.setFederationSynchronizedBarrier(barrier);
      _rti.synchronizationPointAchieved(ManagerNames._readyToPopulate);
      result = barrier.await();
      _userInterface.post("...federation synchronized.");

      makeInitialInstances();

      //Consumption achieves ReadyToRun and waits for rest of federation
      _readyToRunAnnouncementBarrier.await();
      _userInterface.post("Waiting for ReadyToRun...");
      barrier = new Barrier(ManagerNames._readyToRun);
      _fedAmb.setFederationSynchronizedBarrier(barrier);
      _rti.synchronizationPointAchieved(ManagerNames._readyToRun);
      result = barrier.await();
      _userInterface.post("...federation synchronized.");

      _rti.enableAsynchronousDelivery();

      //advance time until ending criterion satisfied
    timeLoop:
      while (!_simulationEndsReceived) {
        _userInterface.setTimeStateAdvancing();
        LogicalTime timeToMoveTo = _internalQueue.getTimeAtHead();
        //_userInterface.post("NER to " + timeToMoveTo);
        _rti.nextEventRequest(timeToMoveTo);
        //process all the events & callbacks we receive from the RTI
        boolean wasTimeAdvanceGrant;
        do {
          Callback callback = _callbackQueue.dequeue();
          wasTimeAdvanceGrant = callback.dispatch();
          //_userInterface.post("After dispatch " + _logicalTime);
        } while (!wasTimeAdvanceGrant);
        updateInternalStateAtNewTime();
        if (_simulationEndsReceived) break timeLoop;
        //process callbacks not requiring advance while in granted state
        while (!_dinerTable.isTimeAdvanceRequired()) {
          Callback callback = _callbackQueue.dequeue();
          callback.dispatch();
        }
      }
      _userInterface.post("Ending criterion satisfied.");

      //Consumption achieves ReadyToResign and waits for rest of federation
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
    _ActorClass = _rti.getObjectClassHandle(RestaurantNames._ActorClassName);
    _DinerClass = _rti.getObjectClassHandle(RestaurantNames._DinerClassName);
    _privilegeToDeleteObjectAttribute = _rti.getAttributeHandle(
      RestaurantNames._privilegeToDeleteObjectAttributeName,
      _RestaurantClass);
    _positionAttribute = _rti.getAttributeHandle(
      RestaurantNames._positionAttributeName,
      _RestaurantClass);
    _spaceAvailableAttribute = _rti.getAttributeHandle(
      RestaurantNames._spaceAvailableAttributeName,
      _BoatClass);
    _cargoAttribute = _rti.getAttributeHandle(
      RestaurantNames._cargoAttributeName,
      _BoatClass);
    _servingNameAttribute = _rti.getAttributeHandle(
      RestaurantNames._servingNameAttributeName,
      _ActorClass);
    _dinerStateAttribute = _rti.getAttributeHandle(
      RestaurantNames._dinerStateAttributeName,
      _DinerClass);

    _privilegeToDeleteObjectAttributeAsSet = _attributeHandleSetFactory.create();
    _privilegeToDeleteObjectAttributeAsSet.add(_privilegeToDeleteObjectAttribute);

    _positionAttributeAsSet = _attributeHandleSetFactory.create();
    _positionAttributeAsSet.add(_positionAttribute);

    _BoatAttributes = _attributeHandleSetFactory.create();
    _BoatAttributes.add(_positionAttribute);
    _BoatAttributes.add(_spaceAvailableAttribute);
    _BoatAttributes.add(_cargoAttribute);

    _DinerAttributes = _attributeHandleSetFactory.create();
    _DinerAttributes.add(_positionAttribute);
    _DinerAttributes.add(_dinerStateAttribute);
    _DinerAttributes.add(_servingNameAttribute);

    _SimulationEndsClass =
      _rti.getInteractionClassHandle(ManagerNames._SimulationEndsClassName);
  }

  private void publish()
  throws RTIexception
  {
    //publish Serving position because we'll update it
    _rti.publishObjectClass(_ServingClass, _positionAttributeAsSet);
    //publish Diner class because we register them
    _rti.publishObjectClass(_DinerClass, _DinerAttributes);
    //publish the interaction because it's our job to send it
    _rti.publishInteractionClass(_SimulationEndsClass);
  }

  private void subscribe()
  throws RTIexception
  {
    //subscribe to Boat (less privToDelete)
    _rti.subscribeObjectClassAttributes(_BoatClass, _BoatAttributes);
    _rti.subscribeObjectClassAttributes(_ServingClass, _positionAttributeAsSet);
  }

  private void makeInitialInstances()
  throws RTIexception
  {
    _dinersReach = (new Double(
      (getProperty("Consumption.diner.reach")))).doubleValue();
    int numberOfDiners = Integer.parseInt(
      getProperty("Consumption.numberOfDiners"));
    _consumptionTimes = new LogicalTimeInterval[numberOfDiners];
    for (int serial = 0; serial < numberOfDiners; ++serial) {
      //register Diner instance
      String dinerName = "D_" + _federateHandle + "_" + serial;
      int dinerHandle = _rti.registerObjectInstance(_DinerClass, dinerName);
      //add to local table
      String prop = "Consumption.diner.position." + serial;
      double position =
        (new Double((getProperty(prop)))).doubleValue();
      _dinerTable.add(
        dinerHandle,
        dinerName,
        position,
        Diner.LOOKING_FOR_FOOD,
        "",
        0);
      //update Diner attribute values
      SuppliedAttributes sa = _suppliedAttributesFactory.create(3);
      sa.add(_positionAttribute, _dinerTable.getFullPosition(serial).encode());
      sa.add(_dinerStateAttribute, IntegerAttribute.encode(_dinerTable.getState(serial)));
      sa.add(_servingNameAttribute, InstanceName.encode(_dinerTable.getServingName(serial)));
      LogicalTime sendTime = new LogicalTimeDouble(0.0);
      sendTime.setTo(_logicalTime);
      sendTime.increaseBy(_lookahead);
      EventRetractionHandle erh =
        _rti.updateAttributeValues(dinerHandle, sa, null, sendTime);
      //collect its consumption time
      prop = "Consumption.diner.meanConsumptionTime." + serial;
      double time = (new Double((getProperty(prop)))).doubleValue();
      _consumptionTimes[serial] = new LogicalTimeIntervalDouble(time);
    }
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

  private void getConfigurationData() {
  }

  private void updateInternalStateAtNewTime()
  throws RTIexception
  {
    checkInternalQueue();
    updateDiners();
    updateServings();
    checkEndOfSimulation();
  }

  private void updateDiners()
  throws RTIexception
  {
    LogicalTime sendTime = new LogicalTimeDouble(0.0);
    sendTime.setTo(_logicalTime);
    sendTime.increaseBy(_lookahead);
    _dinerTable.updateDiners(
      sendTime,
      _suppliedAttributesFactory,
      _positionAttribute,
      _servingNameAttribute,
      _dinerStateAttribute,
      _rti);
  }

  private void updateServings()
  throws RTIexception
  {
    LogicalTime sendTime = new LogicalTimeDouble(0.0);
    sendTime.setTo(_logicalTime);
    sendTime.increaseBy(_lookahead);
    Enumeration e = _servings.elements();
    while (e.hasMoreElements()) {
      Serving serving = (Serving)e.nextElement();
      if (serving._privilegeToDeleteObjectState == AttributeState.OWNED_INCONSISTENT) {
        EventRetractionHandle erh =
          _rti.deleteObjectInstance(serving._handle, null, sendTime);
        _servings.remove(new Integer(serving._handle));
        _userInterface.post("Deleted serving " + serving._handle);
        //update count to end simulation
        _servingsConsumed++;
      }
      else if (serving._positionState == AttributeState.OWNED_INCONSISTENT) {
        SuppliedAttributes sa = _suppliedAttributesFactory.create(1);
        sa.add(_positionAttribute, serving._position.encode());
        EventRetractionHandle erh =
          _rti.updateAttributeValues(serving._handle, sa, null, sendTime);
        serving._positionState = AttributeState.OWNED_CONSISTENT;
      }
    }
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
     String cmdHost = "192.168.206.136";;
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
      System.out.println("Consumption.parseArgs: default arguments failed: " + e);
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
      System.out.println("Consumption failed to load properties: " + e);
      System.exit(1);
    }
  }

  //represents one event on the internal queue
  //This class is defined within Consumption so the dispatch routines
  //of its subclasses have the context of Consumption
  public abstract class InternalEvent {
    protected LogicalTime _time;
    protected int _diner;  //serial, not instance handle

    public LogicalTime getTime() { return _time; }
    public int getDiner() { return _diner; }
    public abstract void dispatch()
    throws RTIexception;
  }

  public final class FinishEatingSushiEvent extends InternalEvent {
    public FinishEatingSushiEvent(LogicalTime time, int diner) {
      _time = time;
      _diner = diner;
    }

    public void dispatch()
    throws RTIexception
    {
      SuppliedAttributes sa;
      EventRetractionHandle erh;
      LogicalTime sendTime;
      //diner has finished eating; prepare to delete Serving
      //update diner's state
      _dinerTable.setState(_diner, Diner.PREPARING_TO_DELETE_SERVING);
      //begin transfer of privilegeToDeleteObject for Serving
      _rti.attributeOwnershipAcquisition(
        _dinerTable.getServing(_diner),
        _privilegeToDeleteObjectAttributeAsSet,
        null);
    }
  } //end FinishEatingSushiEvent

  //represents one callback coming from RTI
  //This class is defined within Production so the dispatch routines
  //of its subclasses have the context of Consumption
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
    int object,
    AttributeHandleSet attributes) {
    Callback callback = new AOANcallback(
      object,
      attributes);
    _callbackQueue.enqueue(callback);
  }

  public final class AOANcallback extends Callback {
    private int _object;
    private AttributeHandleSet _attributes;

    public AOANcallback(
      int object,
      AttributeHandleSet attributes) {
      _object = object;
      _attributes = attributes;
    }

    public boolean dispatch() {
      try {
        //is the object a Serving?
        int objectClass = _rti.getObjectClass(_object);
        if (objectClass != _ServingClass) throw new ObjectNotKnown(
          "Object not Serving class, class: " + objectClass);
        if (_attributes.equals(_positionAttributeAsSet)) {
          transferServingToDiner();
        }
        else if (_attributes.equals(_privilegeToDeleteObjectAttributeAsSet)) {
          finishDestroyingServing();
        }
        else throw new AttributeNotKnown("unexpected attribute set: " + _attributes);
      }
      catch (ConsumptionInternalError e) {
        _userInterface.post("ERROR AOAN: " + e.getMessage());
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR AOAN: " + e);
      }
      return false;
    }

    private void transferServingToDiner()
    throws RTIexception, ConsumptionInternalError
    {
      SuppliedAttributes sa;
      EventRetractionHandle erh;
      LogicalTime sendTime;
      int dinerSerial = _dinerTable.getDinerForServing(_object);
      if (dinerSerial < 0) throw new ConsumptionInternalError("acquisition notification for"
        + " serving " + _object + " not wanted by any diner");
      if (_dinerTable.getState(dinerSerial) != Diner.ACQUIRING) throw new
        ConsumptionInternalError("acquisition notification for serving " + _object
        + " wanted by diner serial " + dinerSerial + "which was in state "
        + _dinerTable.getState(dinerSerial));
      _userInterface.post("Transferring serving " + _dinerTable.getServingName(dinerSerial)
        + " to diner " + _dinerTable.getName(dinerSerial));
      //change state of diner
      _dinerTable.setState(dinerSerial, Diner.EATING);
      _dinerTable.setBoatHandle(dinerSerial, 0);
      //record transfer of ownership
      Serving serving = (Serving)(_servings.get(new Integer(_object)));
      if (serving == null) throw new ConsumptionInternalError(
        "Serving " + _object + " not known");
      serving._privilegeToDeleteObjectState = AttributeState.NOT_REFLECTED;
      serving._typeState = AttributeState.NOT_REFLECTED;
      serving._positionState = AttributeState.OWNED_INCONSISTENT;
      serving._position = new Position(
        _dinerTable.getPosition(dinerSerial),
        Position.OUTBOARD_CANAL);
      //schedule consumption of the serving
      LogicalTime eventTime = new LogicalTimeDouble(0.0);
      eventTime.setTo(_logicalTime);
      eventTime.increaseBy(_consumptionTimes[dinerSerial]);
      _internalQueue.enqueue(new FinishEatingSushiEvent(eventTime, dinerSerial));
      //_userInterface.post("Internal queue: " + _internalQueue);
    }

    private void finishDestroyingServing()
    throws RTIexception, ConsumptionInternalError
    {
      int dinerSerial = _dinerTable.getDinerForServing(_object);
      if (dinerSerial < 0) throw new ConsumptionInternalError("acquisition notification for"
        + " serving " + _object + " not being chewed by any diner");
      //_userInterface.post("Marking serving " + _object + " for deletion");
      if (_dinerTable.getState(dinerSerial) != Diner.PREPARING_TO_DELETE_SERVING) throw new
        ConsumptionInternalError("acquisition notification for serving " + _object
        + " wanted by diner serial " + dinerSerial + "which was in state "
        + _dinerTable.getState(dinerSerial));
      //change state of diner
      _dinerTable.setState(dinerSerial, Diner.LOOKING_FOR_FOOD);
      _dinerTable.setServing(dinerSerial, 0);
      _dinerTable.setServingName(dinerSerial, "");
      //mark Serving for deletion after time grant
      Integer servingKey = new Integer(_object);
      Serving serving = (Serving)(_servings.get(servingKey));
      if (serving == null) throw new ConsumptionInternalError(
        "Serving " + _object + " not known");
      serving._privilegeToDeleteObjectState = AttributeState.OWNED_INCONSISTENT;
    }
  } //end AttributeOwnershipAcquisitionNotificationCallback

  public void queueAttributeOwnershipAcquisitionUnavailableCallback(
    int object,
    AttributeHandleSet attributes) {
    Callback callback = new AOAUcallback(
      object,
      attributes);
    _callbackQueue.enqueue(callback);
  }

  public final class AOAUcallback extends Callback {
    private int _object;
    private AttributeHandleSet _attributes;

    public AOAUcallback(
      int object,
      AttributeHandleSet attributes) {
      _object = object;
      _attributes = attributes;
    }

    public boolean dispatch() {
      try {
        //is the object a Serving?
        int objectClass = _rti.getObjectClass(_object);
        if (objectClass != _ServingClass) throw new ObjectNotKnown(
          "Object not Serving class, class: " + objectClass);
        if (_attributes.equals(_positionAttributeAsSet)) {
          returnDejectedlyToLooking();
        }
        else throw new AttributeNotKnown("unexpected attribute set: " + _attributes);
      }
      catch (ConsumptionInternalError e) {
        _userInterface.post("ERROR attrOwnUnav: " + e.getMessage());
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR attrOwnUnav: " + e);
      }
      return false;
    }

    private void returnDejectedlyToLooking()
    throws RTIexception, ConsumptionInternalError
    {
      SuppliedAttributes sa;
      EventRetractionHandle erh;
      LogicalTime sendTime;
      int dinerSerial = _dinerTable.getDinerForServing(_object);
      if (dinerSerial < 0) throw new ConsumptionInternalError("attr own unav for"
        + " serving " + _object + " not wanted by any diner");
      if (_dinerTable.getState(dinerSerial) != Diner.ACQUIRING) throw new
        ConsumptionInternalError("attr own unav for serving " + _object
        + " wanted by diner serial " + dinerSerial + "which was in state "
        + _dinerTable.getState(dinerSerial));
      _userInterface.post("Failed to get serving " + _dinerTable.getServingName(dinerSerial)
        + " to diner " + _dinerTable.getName(dinerSerial));
      //change state of diner
      _dinerTable.setState(dinerSerial, Diner.LOOKING_FOR_FOOD);
      _dinerTable.setServing(dinerSerial, 0);
      _dinerTable.setServingName(dinerSerial, "");
      _dinerTable.setBoatHandle(dinerSerial, 0);
    }
  } //end AttributeOwnershipAcquisitionUnavailableCallback

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
        if (_objectClass == _BoatClass) {
          Boat newBoat = new Boat();
          newBoat._handle = _instanceHandle;
          newBoat._name = _instanceName;
          newBoat._privilegeToDeleteObjectState = AttributeState.DISCOVERED;
          newBoat._positionState = AttributeState.DISCOVERED;
          newBoat._spaceAvailableState = AttributeState.DISCOVERED;
          newBoat._cargoState = AttributeState.DISCOVERED;
          _knownBoats.put(new Integer(_instanceHandle), newBoat);
          _userInterface.post("Discovered Boat " + _instanceHandle + "(" + _instanceName + ")");
        } else if (_objectClass == _ServingClass) {
          Serving newServing = new Serving();
          newServing._handle = _instanceHandle;
          newServing._name = _instanceName;
          newServing._privilegeToDeleteObjectState = AttributeState.DISCOVERED;
          newServing._positionState = AttributeState.DISCOVERED;
          newServing._typeState = AttributeState.DISCOVERED;
          _servings.put(new Integer(_instanceHandle), newServing);
          _userInterface.post("Discovered Serving " + _instanceHandle + "(" + _instanceName + ")");
        } else {
          throw new ConsumptionInternalError(
            "instance " + _instanceHandle + "(" + _instanceName + ") of class " + _objectClass
            + " not a Boat or Serving!");
        }
      }
      catch (ConsumptionInternalError e) {
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
      //we ignore attribute set in provide callback and update all we have
      try {
        int classHandle = _rti.getObjectClass(_object);
        if (classHandle == _ServingClass) {
          _userInterface.post("ProvAttrUpd for Serving " + _object);
          Integer key = new Integer(_object);
          Serving entry = (Serving)(_servings.get(key));
          if (entry != null) {
            if (entry._positionState == AttributeState.OWNED_CONSISTENT)
              entry._positionState = AttributeState.OWNED_INCONSISTENT;
          }
          else throw new ObjectNotKnown("Serving " + _object + " not known.");
        }
        else if (classHandle == _DinerClass) {
          _userInterface.post("ProvAttrUpd for Diner " + _object);
          _dinerTable.markForUpdate(_object);
        }
        else throw new ObjectNotKnown("Object " + _object +
          " is of unknown class " + classHandle);
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR provideAttributeValueUpdate: " + e);
      }
      return false;
    }
  } //end ProvideAttributeValueUpdateCallback

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
        if (oclass == _BoatClass) reflectBoat();
        else if (oclass == _ServingClass) reflectServing();
        else throw new ConsumptionInternalError (
          "unexpected class " + oclass + " instance " + _object);
      }
      catch (ConsumptionInternalError e) {
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

    private void reflectBoat()
    throws ConsumptionInternalError, RTIexception
    {
      //find the Boat in our collection
      Boat boat = (Boat)_knownBoats.get(new Integer(_object));
      if (boat == null) throw new ConsumptionInternalError ("unknown Boat " + _object);
      //decode and store new attribute values
      int attrCount = _attributes.size();
      for (int i = 0; i < attrCount; ++i) {
        int handle = _attributes.getAttributeHandle(i);
        if (handle == _positionAttribute) {
          boat._position =
            new Position(_attributes.getValueReference(i), 0);
          boat._positionState = AttributeState.REFLECTED; //we have data
        }
        else if (handle == _spaceAvailableAttribute) {
          boat._spaceAvailable =
            SpaceAvailable.decode(_attributes.getValueReference(i), 0);
          boat._spaceAvailableState = AttributeState.REFLECTED; //we have data
        }
        else if (handle == _cargoAttribute) {
          boat._cargo =
            InstanceName.decode(_attributes.getValueReference(i), 0);
          boat._cargoState = AttributeState.REFLECTED; //we have data
        }
        else throw new ConsumptionInternalError("unknown attribute " + handle);
      }
      if (!boat._spaceAvailable) {
        boat._serving = _rti.getObjectInstanceHandle(boat._cargo);
      }
      /*
      _userInterface.post("Reflected boat " + _rti.getObjectInstanceName(_object)
        + " position " + boat._position + " at " + _time);
        */
      //if the boat is loaded, see if a diner wants to take its Serving it
      if (!boat._spaceAvailable) doWeWantToEmptyThisBoat(boat);
    }

    private void reflectServing()
    throws ConsumptionInternalError, RTIexception
    {
      //find the Serving in our collection
      Serving serving = (Serving)_servings.get(new Integer(_object));
      if (serving == null) throw new ConsumptionInternalError ("unknown Serving " + _object);
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

    private void doWeWantToEmptyThisBoat(Boat boat)
    throws RTIexception, ArrayIndexOutOfBounds, ConsumptionInternalError
    {
      int dinerCount = _dinerTable.getRowCount();
      for (int diner = 0; diner < dinerCount; ++diner) {
        if (_dinerTable.getState(diner) == Diner.LOOKING_FOR_FOOD
          && _dinerTable.isInReach(diner, boat._position._angle, _dinersReach)) {
          String boatName = _rti.getObjectInstanceName(_object);
          /*
          _userInterface.post("Diner " + diner + " attempting to take from boat "
            + boatName);
            */
          //change state of diner
          _dinerTable.setState(diner, Diner.ACQUIRING);
          _dinerTable.setServing(diner, boat._serving);
          _dinerTable.setServingName(diner, boat._cargo);
          _dinerTable.setBoatHandle(diner, boat._handle);
          //remember Serving
          boat._serving = _rti.getObjectInstanceHandle(boat._cargo);
          //begin acquisition of Serving
          _rti.attributeOwnershipAcquisitionIfAvailable(
            boat._serving,
            _positionAttributeAsSet);
        }
      }
    }
  } //end ReflectAttributeValuesEvent
}
