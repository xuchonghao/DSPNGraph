
//Title:        Late Viewer Federate for Book
//Version:      
//Copyright:    Copyright (c) 1998
//Author:       Frederick Kuhl
//Company:      The MITRE corporation
//Description:  Viewer federate modified to join federate "late"


package pipe.hla.book.restaurant.late_viewer;

import java.util.*;
import java.net.*;

import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import pipe.hla.book.manager.ManagerNames;
import hla.rti.*;
import pipe.hla.book.restaurant.*;
import se.pitch.prti.*;

public final class LateViewer {
  //system properties used throughout
  private static String _fileSeparator = System.getProperty("file.separator");
  private static String _userDirectory = System.getProperty("user.dir");

  private LateViewerFrame _userInterface;
  private int _federateHandle; // -1 when not joined
  private boolean _simulationEndsReceived;
  private boolean _resignRequestReceived;
  private FedAmbImpl _fedAmb;
  private String _fedexName;
  private Properties _properties;

  //barriers used to await announcement of synchronization points
  //The 'late' federate awaits no sync points.

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
  private int _ActorClass;
  private int _ChefClass;
  private int _DinerClass;
  private int _positionAttribute;
  private int _typeAttribute;
  private int _spaceAvailableAttribute;
  private int _cargoAttribute;
  private int _chefStateAttribute;
  private int _dinerStateAttribute;
  private int _servingNameAttribute;
  private AttributeHandleSet _ServingAttributes;
  private AttributeHandleSet _BoatAttributes;
  private AttributeHandleSet _ChefAttributes;
  private AttributeHandleSet _DinerAttributes;
  private int _SimulationEndsClass;

  private int _nextServingSerial;   //serial to assign to next Serving registered
  private Hashtable _chefs;         //key: instance handle; value: Chef
  private Hashtable _servings;      //key: instance handle; value: Serving
  private Hashtable _boats;         //key: instance handle; value: Boat
  private Hashtable _diners;        //key: instance handle; value: Diners
  private int _numberOfSushiTypes;
  private CallbackQueue _callbackQueue;

  public LateViewer(Properties props) {
    _federateHandle = -1;  //not joined
    _properties = props;
    _servings = new Hashtable();
    _boats = new Hashtable();
    _chefs = new Hashtable();
    _diners = new Hashtable();
    _callbackQueue = new CallbackQueue();
    _simulationEndsReceived = false;
    _resignRequestReceived = false;

		_userInterface = new LateViewerFrame();
    _userInterface.finishConstruction(
      this,
      _chefs,
      _servings,
      _boats,
      _diners);
		_userInterface.show();
    _userInterface.lastAdjustments();

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
      System.out.println("LateViewer started.");
		}
		catch (Exception e) {
		  _userInterface.post("LateViewer: constructor failed: " + e);
		  _userInterface.post("You may as well exit.");
		}
  }

  public static void main(String[] args) {
	  Properties props = parseArgs(args);
    loadProperties(props);
    LateViewer production = new LateViewer(props);
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
        (String)_properties.get("CONFIG")
        + _fedexName
        + ".fed";
      fedURL = new URL(urlString);
      //join federation execution
      _fedexName = (String)_properties.get("FEDEX");
      _federateHandle = _rti.joinFederationExecution(
        ViewerNames._federateType,
        _fedexName,
        _fedAmb);
      _userInterface.post("Joined as federate " + _federateHandle);

      //do everything possible beforehand to minimize time between enabling
      //time regulation and beginning to advance time
      getHandles();
      subscribe();
      _rti.enableAsynchronousDelivery();

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
        (new Double((getProperty("Viewer.lookahead")))).doubleValue());
      _advanceInterval = new LogicalTimeIntervalDouble(
        (new Double((getProperty("Viewer.advanceInterval")))).doubleValue());
      _userInterface.post("Enabling time regulation...");
      barrier = new Barrier();
      _fedAmb.setEnableTimeRegulationBarrier(barrier);
      _rti.enableTimeRegulation(_logicalTime, _lookahead);
      result = barrier.await();
      _logicalTime = (LogicalTime)result[0];
      _userInterface.post("...regulation enabled at " + _logicalTime);

      //advance time in steps until SimulationEnds received
      _targetTime = new LogicalTimeDouble(0.0);
      _targetTime.setTo(_logicalTime);
    timeLoop:
      while (!_simulationEndsReceived && !_resignRequestReceived) {
        //advance by a step
        _targetTime.increaseBy(_advanceInterval);
        _rti.timeAdvanceRequest(_targetTime);
        //chew through all the events we receive from the RTI
        boolean wasTimeAdvanceGrant;
        do {
          Callback callback = _callbackQueue.dequeue();
          wasTimeAdvanceGrant = callback.dispatch();
          if (_simulationEndsReceived) break timeLoop;
        } while (!wasTimeAdvanceGrant);
      }
      if (_simulationEndsReceived) _userInterface.post("SimulationEnds received.");
      if (_resignRequestReceived) _userInterface.post("Resign request received.");

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
    _ChefClass = _rti.getObjectClassHandle(RestaurantNames._ChefClassName);
    _DinerClass = _rti.getObjectClassHandle(RestaurantNames._DinerClassName);
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
    _servingNameAttribute = _rti.getAttributeHandle(
      RestaurantNames._servingNameAttributeName,
      _ActorClass);
    _chefStateAttribute = _rti.getAttributeHandle(
      RestaurantNames._chefStateAttributeName,
      _ChefClass);
    _dinerStateAttribute = _rti.getAttributeHandle(
      RestaurantNames._dinerStateAttributeName,
      _DinerClass);

    _ServingAttributes = _attributeHandleSetFactory.create();
    _ServingAttributes.add(_positionAttribute);
    _ServingAttributes.add(_typeAttribute);

    _BoatAttributes = _attributeHandleSetFactory.create();
    _BoatAttributes.add(_positionAttribute);
    _BoatAttributes.add(_spaceAvailableAttribute);
    _BoatAttributes.add(_cargoAttribute);

    _ChefAttributes = _attributeHandleSetFactory.create();
    _ChefAttributes.add(_positionAttribute);
    _ChefAttributes.add(_chefStateAttribute);
    _ChefAttributes.add(_servingNameAttribute);

    _DinerAttributes = _attributeHandleSetFactory.create();
    _DinerAttributes.add(_positionAttribute);
    _DinerAttributes.add(_dinerStateAttribute);
    _DinerAttributes.add(_servingNameAttribute);

    _SimulationEndsClass =
      _rti.getInteractionClassHandle(ManagerNames._SimulationEndsClassName);
  }

  public void requestResignation() {
    _resignRequestReceived = true;
  }

  private void subscribe()
  throws RTIexception
  {
    _rti.subscribeObjectClassAttributesPassively(_BoatClass, _BoatAttributes);
    //changing these subscriptions to active causes the Serving.type and state
    //attributes to be updated
    _rti.subscribeObjectClassAttributes(_ServingClass, _ServingAttributes);
    _rti.subscribeObjectClassAttributes(_ChefClass, _ChefAttributes);
    _rti.subscribeObjectClassAttributes(_DinerClass, _DinerAttributes);
    //_rti.subscribeObjectClassAttributesPassively(_ServingClass, _ServingAttributes);
    //_rti.subscribeObjectClassAttributesPassively(_ChefClass, _ChefAttributes);
    //_rti.subscribeObjectClassAttributesPassively(_DinerClass, _DinerAttributes);

    _rti.subscribeInteractionClass(_SimulationEndsClass);
  }

  private void getConfigurationData()
  {
  }

  //federate ambassador calls this when it gets an announcement
  public void recordSynchronizationPointAnnouncement(String label) {
    _userInterface.post("INFO: sync pt announcement for " + label);
  }

  //defined so that a missing property doesn't cause a crash later
  public String getProperty(String name) {
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
      System.out.println("LateViewer.parseArgs: default arguments failed: " + e);
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
      System.out.println("LateViewer failed to load properties: " + e);
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

  public void queueDiscoverObjectInstance(
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
      if (_objectClass == _BoatClass) {
        Boat newBoat = new Boat();
        newBoat._handle = _instanceHandle;
        newBoat._name = _instanceName;
        newBoat._privilegeToDeleteObjectState = AttributeState.NOT_REFLECTED;
        newBoat._positionState = AttributeState.DISCOVERED;
        newBoat._spaceAvailableState = AttributeState.DISCOVERED;
        newBoat._cargoState = AttributeState.DISCOVERED;
        _boats.put(new Integer(_instanceHandle), newBoat);
        //_userInterface.post("Discovered Boat " + _instanceHandle + "(" + _instanceName + ")");
        try {
          _rti.requestObjectAttributeValueUpdate(_instanceHandle, _BoatAttributes);
        }
        catch (RTIexception e) {
          _userInterface.post("ReqAttrVal Boat failed: " + e);
        }
      }
      else if (_objectClass == _ServingClass) {
        Serving newServing = new Serving();
        newServing._handle = _instanceHandle;
        newServing._name = _instanceName;
        newServing._privilegeToDeleteObjectState = AttributeState.NOT_REFLECTED;
        newServing._positionState = AttributeState.DISCOVERED;
        newServing._typeState = AttributeState.DISCOVERED;
        _servings.put(new Integer(_instanceHandle), newServing);
        //_userInterface.post("Discovered Serving " + _instanceHandle + "(" + _instanceName + ")");
        try {
          _rti.requestObjectAttributeValueUpdate(_instanceHandle, _ServingAttributes);
        }
        catch (RTIexception e) {
          _userInterface.post("ReqAttrVal Serving failed: " + e);
        }
      }
      else if (_objectClass == _ChefClass) {
        Chef newChef = new Chef();
        newChef._handle = _instanceHandle;
        newChef._name = _instanceName;
        newChef._privilegeToDeleteObjectState = AttributeState.NOT_REFLECTED;
        newChef._positionState = AttributeState.DISCOVERED;
        newChef._stateState = AttributeState.DISCOVERED;
        newChef._servingNameState = AttributeState.DISCOVERED;
        _chefs.put(new Integer(_instanceHandle), newChef);
        //_userInterface.post("Discovered Chef " + _instanceHandle + "(" + _instanceName + ")");
        try {
          _rti.requestObjectAttributeValueUpdate(_instanceHandle, _ChefAttributes);
        }
        catch (RTIexception e) {
          _userInterface.post("ReqAttrVal Chef failed: " + e);
        }
      }
      else if (_objectClass == _DinerClass) {
        Diner newDiner = new Diner();
        newDiner._handle = _instanceHandle;
        newDiner._name = _instanceName;
        newDiner._privilegeToDeleteObjectState = AttributeState.NOT_REFLECTED;
        newDiner._positionState = AttributeState.DISCOVERED;
        newDiner._stateState = AttributeState.DISCOVERED;
        newDiner._servingNameState = AttributeState.DISCOVERED;
        _diners.put(new Integer(_instanceHandle), newDiner);
        //_userInterface.post("Discovered Diner " + _instanceHandle + "(" + _instanceName + ")");
        try {
          _rti.requestObjectAttributeValueUpdate(_instanceHandle, _DinerAttributes);
        }
        catch (RTIexception e) {
          _userInterface.post("ReqAttrVal Diner failed: " + e);
        }
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
      _userInterface.updateView();
      return true;
    }
  } //end GrantEvent

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
        if (oclass == _BoatClass) updateBoat();
        else if (oclass == _ServingClass) updateServing();
        else if (oclass == _ChefClass) updateChef();
        else if (oclass == _DinerClass) updateDiner();
        else throw new ViewerInternalError (
          "unexpected class " + oclass + " instance " + _object);
      }
      catch (ViewerInternalError e) {
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

    private void updateBoat()
    throws ViewerInternalError, CouldNotDecode, ArrayIndexOutOfBounds
    {
      //find the Boat in our collection
      Boat boat = (Boat)_boats.get(new Integer(_object));
      if (boat == null) throw new ViewerInternalError ("unknown Boat " + _object);
      //decode and store new attribute values
      int attrCount = _attributes.size();
      for (int i = 0; i < attrCount; ++i) {
        int handle = _attributes.getAttributeHandle(i);
        if (handle == _positionAttribute) {
          boat._position =
            new Position(_attributes.getValueReference(i), 0);
          boat._positionState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " boat " + _object + " posn " + boat._position);
        }
        else if (handle == _spaceAvailableAttribute) {
          boat._spaceAvailable =
            SpaceAvailable.decode(_attributes.getValueReference(i), 0);
          boat._spaceAvailableState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " boat " + _object + " sp avail " + boat._spaceAvailable);
        }
        else if (handle == _cargoAttribute) {
          boat._cargo =
            InstanceName.decode(_attributes.getValueReference(i), 0);
          boat._cargoState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " boat " + _object + " cargo " + boat._cargo);
        }
        else throw new ViewerInternalError("unknown attribute " + handle);
      }
    }

    private void updateServing()
    throws ViewerInternalError, CouldNotDecode, ArrayIndexOutOfBounds
    {
      //find the Serving in our collection
      Serving serving = (Serving)_servings.get(new Integer(_object));
      if (serving == null) throw new ViewerInternalError ("unknown Serving " + _object);
      //decode and store new attribute values
      int attrCount = _attributes.size();
      for (int i = 0; i < attrCount; ++i) {
        int handle = _attributes.getAttributeHandle(i);
        if (handle == _positionAttribute) {
          serving._position =
            new Position(_attributes.getValueReference(i), 0);
          serving._positionState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " Svg " + _object + " posn " + serving._position);
        }
        else if (handle == _typeAttribute) {
          serving._type =
            IntegerAttribute.decode(_attributes.getValueReference(i), 0);
          serving._typeState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " Svg " + _object + " type " + serving._type);
        }
        else throw new ViewerInternalError("unknown attribute " + handle);
      }
    }

    private void updateChef()
    throws ViewerInternalError, CouldNotDecode, ArrayIndexOutOfBounds
    {
      //find the Chef in our collection
      Chef chef = (Chef)_chefs.get(new Integer(_object));
      if (chef == null) throw new ViewerInternalError ("unknown Chef " + _object);
      //decode and store new attribute values
      int attrCount = _attributes.size();
      for (int i = 0; i < attrCount; ++i) {
        int handle = _attributes.getAttributeHandle(i);
        if (handle == _positionAttribute) {
          chef._position =
            new Position(_attributes.getValueReference(i), 0);
          chef._positionState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " Chef " + _object + " posn " + chef._position);
        }
        else if (handle == _chefStateAttribute) {
          chef._state =
            IntegerAttribute.decode(_attributes.getValueReference(i), 0);
          chef._stateState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " Chef " + _object + " state " + chef._state);
        }
        else if (handle == _servingNameAttribute) {
          chef._servingName =
            InstanceName.decode(_attributes.getValueReference(i), 0);
          chef._servingNameState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " Chef " + _object + " svg name " + chef._servingName);
        }
        else throw new ViewerInternalError("unknown attribute " + handle);
      }
    }

    private void updateDiner()
    throws ViewerInternalError, CouldNotDecode, ArrayIndexOutOfBounds
    {
      //find the Diner in our collection
      Diner diner = (Diner)_diners.get(new Integer(_object));
      if (diner == null) throw new ViewerInternalError ("unknown Diner " + _object);
      //decode and store new attribute values
      int attrCount = _attributes.size();
      for (int i = 0; i < attrCount; ++i) {
        int handle = _attributes.getAttributeHandle(i);
        if (handle == _positionAttribute) {
          diner._position =
            new Position(_attributes.getValueReference(i), 0);
          diner._positionState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " Diner " + _object + " posn " + diner._position);
           }
        else if (handle == _dinerStateAttribute) {
          diner._state =
            IntegerAttribute.decode(_attributes.getValueReference(i), 0);
          diner._stateState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " Diner " + _object + " state " + diner._state);
        }
        else if (handle == _servingNameAttribute) {
          diner._servingName =
            InstanceName.decode(_attributes.getValueReference(i), 0);
          diner._servingNameState = AttributeState.REFLECTED; //we have data
          //_userInterface.post("Refl " + _time + " Diner " + _object + " svg name " + diner._servingName);
        }
        else throw new ViewerInternalError("unknown attribute " + handle);
      }
    }
  } //end ReflectAttributeValuesEvent

  public void queueRemoveObjectInstanceEvent(
    LogicalTime time,
    int objectHandle)
  {
    ExternalEvent event = new RemoveObjectInstanceEvent(
      time,
      objectHandle);
    _callbackQueue.enqueue(event);
  }

  public final class RemoveObjectInstanceEvent extends ExternalEvent {
    LogicalTime _time;
    int _object;

    public RemoveObjectInstanceEvent(
      LogicalTime time,
      int objectHandle)
    {
      _time = time;
      _object = objectHandle;
    }

    public boolean dispatch()
    throws RTIexception
    {
      Integer key = new Integer(_object);
      Object entry;
      if ((entry = _servings.get(key)) != null) {
        _servings.remove(key);
        _userInterface.post("Serving " + ((Serving)entry)._name + " being removed at " + _time);
      }
      else if ((entry = _boats.get(key)) != null) {
        _boats.remove(key);
        _userInterface.post("Boat " + ((Boat)entry)._name + " being removed at " + _time);
      }
      else if ((entry = _chefs.get(key)) != null) {
        _chefs.remove(key);
        _userInterface.post("Chef " + ((Chef)entry)._name + " being removed at " + _time);
      }
      else if ((entry = _diners.get(key)) != null) {
        _diners.remove(key);
        _userInterface.post("Diner " + ((Diner)entry)._name + " being removed at " + _time);
      }
      else {
        throw new ObjectNotKnown("Object " + _object + " not known");
      }
      return false;
    }
  } //end RemoveObjectInstanceEvent
}
