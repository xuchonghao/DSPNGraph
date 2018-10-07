
package pipe.hla.book.restaurant.production;
import java.util.*;
import java.net.*;

import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import pipe.hla.book.manager.ManagerNames;
import hla.rti.*;
import pipe.hla.book.restaurant.*;
import se.pitch.prti.*;

public final class Production {
  /*
  * */
  //system properties used throughout
  private static String _fileSeparator = System.getProperty("file.separator");
  private static String _userDirectory = System.getProperty("user.dir");

  private ProductionFrame _userInterface;

  private int _federateHandle; // -1 when not joined
  private boolean _simulationEndsReceived;

  private FedAmbImpl _fedAmb;
  private String _fedexName;
  private Properties _properties;

  //barriers used to await announcement of synchronization points
  //these must be allocated here lest announcement sneak up on us
  //barrier和queue类用于协调主线程和回调线程！
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
  private int _ChefClass;
  private int _privilegeToDeleteObjectAttribute;
  private AttributeHandleSet _privilegeToDeleteObjectAttributeAsSet;
  private int _positionAttribute;
  private AttributeHandleSet _positionAttributeAsSet;
  private int _typeAttribute;
  private int _spaceAvailableAttribute;
  private int _cargoAttribute;
  private int _chefStateAttribute;
  private int _servingNameAttribute;
  private AttributeHandleSet _ServingAttributes;
  private AttributeHandleSet _BoatAttributes;
  private AttributeHandleSet _ChefAttributes;
  private int _SimulationEndsClass;
  private int _TransferAcceptedClass;
  private int _servingNameParameter;

  private int _nextServingSerial;   //serial to assign to next Serving registered
  //存储了厨师的状态信息，还用于用户界面厨师列表的更新
  private ChefTable _chefTable;     //table of our notional chefs
  //存储联邦成员产生的Serving实例的状态信息
  private Hashtable _servings;      //key: instance handle; value: Serving
  private Random _random;           //generator to determine sushi types
  private int _numberOfSushiTypes;
  private LogicalTimeIntervalDouble[] _manufactureTimes; //time to make sushi by type
  private Hashtable _knownBoats;    //key: instance handle; value: Boat
  private InternalQueue _internalQueue;//存储内部事件，由厨师产生的只有生产联邦成员感兴趣的事件

  //RTI每次回调联邦成员时，就会向_callbackQueue中放置一个Production.Callback实例，并进行处理
  //联邦成员的主线程在时间推进循环中会从_callbackQueue取出。。实例，并进行相应的处理（除了announce同步点）
  private CallbackQueue _callbackQueue;//允许主线程一次处理一个回调函数，同时协调主线程和回调线程！

  private double _chefsReach;

  public Production(Properties props) {
    _federateHandle = -1;  //not joined
    _nextServingSerial = 0;
    _servings = new Hashtable();
    _knownBoats = new Hashtable();
    _chefTable = new ChefTable();
    _internalQueue = new InternalQueue();
    _callbackQueue = new CallbackQueue();
    _random = new Random();
    _simulationEndsReceived = false;

    _userInterface = new ProductionFrame();
    _userInterface.finishConstruction(this, _chefTable);
    _userInterface.show();
    _userInterface.lastAdjustments();

    _properties = props;
    System.out.println("host: " + props.get("RTI_HOST"));
    System.out.println("port: " + props.get("RTI_PORT"));
    System.out.println("config: " + props.get("CONFIG"));

    String host = (String)_properties.get("RTI_HOST");
    int port = Integer.parseInt((String)_properties.get("RTI_PORT"));
    System.out.println("host_port : "+host +" , "+port);
    //create RTI implementation
    try {
      //_rti = RTI.getRTIambassador(host,port);
      _rti = RTI.getRTIambassador(Constant.HOSTNAME,8989);
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (ConnectException e) {
      e.printStackTrace();
    } catch (RTIinternalError rtIinternalError) {
      rtIinternalError.printStackTrace();
    }catch (Exception e) {
      _userInterface.post("Production: constructor failed: " + e);
      _userInterface.post("You may as well exit.");
    }
    System.out.println("RTIambassador created");
    _userInterface.post("RTIambassador created");
    _fedAmb = new FedAmbImpl(this, _userInterface);

    //do other implementation-specific things
    _suppliedParametersFactory = RTI.suppliedParametersFactory();
    _suppliedAttributesFactory = RTI.suppliedAttributesFactory();
    _attributeHandleSetFactory = RTI.attributeHandleSetFactory();
    _federateHandleSetFactory = RTI.federateHandleSetFactory();
    System.out.println("Production started.");


  }

  public static void main(String[] args) {
    Properties props = parseArgs(args);
    loadProperties(props);
    Production production = new Production(props);
    production.mainThread();
  }

  //called when we get a time advance grant
  private void checkInternalQueue()
  throws RTIexception
  {
    while (_internalQueue.getTimeAtHead().isLessThanOrEqualTo(_logicalTime)) {
      InternalEvent event = _internalQueue.dequeue();

      event.dispatch();
    }
  }

  //the main thread 这里是主线程，实现联邦成员的生命周期和时间推进循环
  private void mainThread() {
    Barrier barrier;
    Object[] result;

    try {
      //先获取配置数据
      getConfigurationData();

      //create federation execution (if necessary) and join
      _fedexName = (String)_properties.get("FEDEX");
      URL fedURL;
      String urlString = (String)_properties.get("CONFIG") + _fedexName + ".fed";
      //System.out.println(urlString);
      fedURL = new URL(urlString);

      //the federation execution may already exist
      try {
        _rti.createFederationExecution(_fedexName, fedURL);
        _userInterface.post("Federation execution " + _fedexName + " created.");
      }
      catch (FederationExecutionAlreadyExists e) {
        _userInterface.post("Federation execution " + _fedexName + " already exists.");
      }

      //join federation execution
      _fedexName = (String)_properties.get("FEDEX");
      _federateHandle = _rti.joinFederationExecution(ProductionNames._federateType,_fedexName,_fedAmb);
      _userInterface.post("Joined as federate " + _federateHandle);

      //enable time constraint
      _userInterface.post("Enabling time constraint...");
      barrier = new Barrier();//创建了一个阻塞实例
      _fedAmb.setEnableTimeConstrainedBarrier(barrier);//并且该实例在FedAmbImpl中进行设置
      /**主线程等待时，它可以不断循环等待一个变量状态的变化，这个状态值可以在回调线程中设置。但是这样会浪费资源。
       * 所以在这个方法前创建了一个阻塞实例，并且该实例在FedAmbImpl中进行设置。*/
      _rti.enableTimeConstrained();

      result = barrier.await();//主线程进行休眠状态

      _logicalTime = (LogicalTime)result[0];
      _userInterface.post("...constraint enabled at " + _logicalTime);

      //enable time regulation
      _logicalTime = new LogicalTimeDouble((new Double((getProperty("Federation.initialTime")))).doubleValue());
      _lookahead = new LogicalTimeIntervalDouble((new Double((getProperty("Production.lookahead")))).doubleValue());
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

      //Production achieves ReadyToPopulate and waits for rest of federation
      _readyToPopulateAnnouncementBarrier.await();
      _userInterface.post("Waiting for ReadyToPopulate...");
      barrier = new Barrier(ManagerNames._readyToPopulate);
      _fedAmb.setFederationSynchronizedBarrier(barrier);
      _rti.synchronizationPointAchieved(ManagerNames._readyToPopulate);
      result = barrier.await();
      _userInterface.post("...federation synchronized.");

      //Production federate makes initial instances as it makes chefs
      makeInitialInstances();

      //Production achieves ReadyToRun and waits for rest of federation
      _readyToRunAnnouncementBarrier.await();
      _userInterface.post("Waiting for ReadyToRun...");
      barrier = new Barrier(ManagerNames._readyToRun);
      _fedAmb.setFederationSynchronizedBarrier(barrier);
      _rti.synchronizationPointAchieved(ManagerNames._readyToRun);
      result = barrier.await();
      _userInterface.post("...federation synchronized.");

      //使得RTI能向处于时间推进状态或时间批准状态的联邦成员分发RO顺序的回调
      _rti.enableAsynchronousDelivery();

      //advance time until SimulationEnds received
    timeLoop:
      while (!_simulationEndsReceived) {
        _userInterface.setTimeStateAdvancing();
        LogicalTime timeToMoveTo = _internalQueue.getTimeAtHead();
        //_userInterface.post("NER to " + timeToMoveTo);
        _rti.nextEventRequest(timeToMoveTo);//发出时间推进申请
        boolean wasTimeAdvanceGrant;
        do {//内部循环等待并分发回调函数
          Callback callback = _callbackQueue.dequeue();
          wasTimeAdvanceGrant = callback.dispatch();
          if (_simulationEndsReceived) break timeLoop;
        } while (!wasTimeAdvanceGrant);//直到收到TIME ADVANCE GRANT'
        //然后在新的批准时间，外部循环更新联邦成员的内部状态，并检查内部事件队列，之后在发出时间推进请求
        updateInternalStateAtNewTime();
      }

      _userInterface.post("SimulationEnds received.");

      //Production achieves ReadyToResign and waits for rest of federation
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

  private void getHandles()  throws RTIexception
  {
    _RestaurantClass = _rti.getObjectClassHandle(RestaurantNames._RestaurantClassName);
    _ServingClass = _rti.getObjectClassHandle(RestaurantNames._ServingClassName);
    _BoatClass = _rti.getObjectClassHandle(RestaurantNames._BoatClassName);
    _ActorClass = _rti.getObjectClassHandle(RestaurantNames._ActorClassName);
    _ChefClass = _rti.getObjectClassHandle(RestaurantNames._ChefClassName);
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
    _servingNameAttribute = _rti.getAttributeHandle(
      RestaurantNames._servingNameAttributeName,
      _ActorClass);
    _chefStateAttribute = _rti.getAttributeHandle(
      RestaurantNames._chefStateAttributeName,
      _ChefClass);

    _privilegeToDeleteObjectAttributeAsSet = _attributeHandleSetFactory.create();
    _privilegeToDeleteObjectAttributeAsSet.add(_privilegeToDeleteObjectAttribute);

    _positionAttributeAsSet = _attributeHandleSetFactory.create();
    _positionAttributeAsSet.add(_positionAttribute);

    _ServingAttributes = _attributeHandleSetFactory.create();
    _ServingAttributes.add(_privilegeToDeleteObjectAttribute);
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

    _SimulationEndsClass =
      _rti.getInteractionClassHandle(ManagerNames._SimulationEndsClassName);
    _TransferAcceptedClass =
      _rti.getInteractionClassHandle(RestaurantNames._TransferAcceptedClassName);

    _servingNameParameter = _rti.getParameterHandle(
      RestaurantNames._servingNameParameterName,
      _TransferAcceptedClass);
  }

  private void getConfigurationData()
  throws InvalidLookahead
  {
    //get manfacture times for sushi types
    _numberOfSushiTypes = Integer.parseInt(getProperty("Federation.Sushi.numberOfTypes"));
    _manufactureTimes = new LogicalTimeIntervalDouble[_numberOfSushiTypes];
    for (int i = 0; i < _numberOfSushiTypes; ++i) {
      String timeString = getProperty("Federation.Sushi.meanManufactureTime."+i);
      double time = (new Double((timeString))).doubleValue();
      _manufactureTimes[i] = new LogicalTimeIntervalDouble(time);
    }
  }

  private void publish()  throws RTIexception
  {
    //publish Serving class because we register them
    _rti.publishObjectClass(_ServingClass, _ServingAttributes);
    //publish Chef class because we register them
    _rti.publishObjectClass(_ChefClass, _ChefAttributes);
  }

  private void subscribe()  throws RTIexception
  {
    //subscribe to Boat (less privToDelete)
    _rti.subscribeObjectClassAttributes(_BoatClass, _BoatAttributes);

    _rti.subscribeInteractionClass(_SimulationEndsClass);
    _rti.subscribeInteractionClass(_TransferAcceptedClass);
  }

  private void makeInitialInstances()  throws RTIexception
  {
    _chefsReach = (new Double((getProperty("Production.chef.reach")))).doubleValue();
    int numberOfChefs = Integer.parseInt(getProperty("Production.numberOfChefs"));
    for (int serial = 0; serial < numberOfChefs; ++serial) {
      //register Chef instance
      String chefName = "C_" + _federateHandle + "_" + serial;
      int chefHandle = _rti.registerObjectInstance(_ChefClass, chefName);
      //add to local table
      String prop = "Production.chef.position." + serial;
      double position = (new Double((getProperty(prop)))).doubleValue();

      _chefTable.add(chefHandle,chefName,position, Chef.MAKING_SUSHI,"",0); //serving created later

      //update Chef attribute values
      SuppliedAttributes sa = _suppliedAttributesFactory.create(3);
      sa.add(_positionAttribute, _chefTable.getFullPosition(serial).encode());
      sa.add(_chefStateAttribute, IntegerAttribute.encode(_chefTable.getState(serial)));
      sa.add(_servingNameAttribute, InstanceName.encode(_chefTable.getServingName(serial)));
      LogicalTime sendTime = new LogicalTimeDouble(0.0);
      sendTime.setTo(_logicalTime);
      sendTime.increaseBy(_lookahead);
      EventRetractionHandle erh = _rti.updateAttributeValues(chefHandle, sa, null, sendTime);
      //what kind of sushi?
      int type = Math.abs(_random.nextInt()) % _numberOfSushiTypes;
      //put event on internal queue
      LogicalTime eventTime = new LogicalTimeDouble(0.0);
      eventTime.setTo(_logicalTime);
      eventTime.increaseBy(_manufactureTimes[type]);

      //把一个FinishMakingSushiEvent事件放入内部事件队列中
      _internalQueue.enqueue(new FinishMakingSushiEvent(eventTime, serial, type));
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

  private void updateChefs()
  throws RTIexception
  {
    LogicalTime sendTime = new LogicalTimeDouble(0.0);
    sendTime.setTo(_logicalTime);
    sendTime.increaseBy(_lookahead);
    _chefTable.updateChefs(sendTime,_suppliedAttributesFactory,_positionAttribute,
        _servingNameAttribute,      _chefStateAttribute,      _rti);
  }

  private void updateInternalStateAtNewTime()
  throws RTIexception
  {
    checkInternalQueue();
    updateChefs();
    updateServings();
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
      SuppliedAttributes sa = _suppliedAttributesFactory.create(2);
      boolean needToUpdate = false;
      if (serving._positionState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(_positionAttribute, serving._position.encode());
        needToUpdate = true;
        serving._positionState = AttributeState.OWNED_CONSISTENT;
      }
      if (serving._typeState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(_typeAttribute, IntegerAttribute.encode(serving._type));
        needToUpdate = true;
        serving._typeState = AttributeState.OWNED_CONSISTENT;
      }
      if (needToUpdate) {
        try {
          EventRetractionHandle erh =
            _rti.updateAttributeValues(serving._handle, sa, null, sendTime);
        }
        catch (AttributeNotOwned ex) {
          //might lose ownership before the fact is recorded here
          _userInterface.post("Late update: " + ex);
        }
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
      defaultRTIhost = "192.168.206.134";
      String cmdHost = System.getProperty("RTI_HOST");
      System.out.println("Cmd line had " + defaultRTIhost + "," + cmdHost);
      if (cmdHost == null)
        props.put("RTI_HOST", defaultRTIhost);
      else
        props.put("RTI_HOST", cmdHost);

      //port number for Central RTI component
      String defaultRTIport = "8989";
      String cmdPort = System.getProperty("RTI_PORT");
      //System.out.println("Cmd line had " + cmdPort);
      if (cmdPort == null)
        props.put("RTI_PORT", defaultRTIport);
      else
        props.put("RTI_PORT", cmdPort);

      //form URL
      String urlString = System.getProperty("CONFIG",
        "file:" + _userDirectory + _fileSeparator + "config" + _fileSeparator);
      props.put("CONFIG", urlString);
      System.out.println("Config URL: " + props.get("CONFIG"));

      //federation execution name
      String defaultFedExName = "restaurant_1";
      String cmdFedExName = System.getProperty("FEDEX");
      if (cmdFedExName == null) props.put("FEDEX", defaultFedExName);
      else props.put("FEDEX", cmdFedExName);
    }
    catch (UnknownHostException e) {
      System.out.println("Production.parseArgs: default arguments failed: " + e);
      System.exit(1);
    }
    return props;
  }

  //load other properties from URL
  private static void loadProperties(Properties props) {
    try {
      //form URL for properties
      String urlString =        (String)props.get("CONFIG")        + (String)props.get("FEDEX")        + ".props";
      URL propsURL = new URL(urlString);
      props.load(propsURL.openStream());
    }
    catch (Exception e) {
      System.out.println("Production failed to load properties: " + e);
      System.exit(1);
    }
  }

  //represents one event on the internal queue
  //This class is defined within Production so the dispatch routines
  //of its subclasses have the context of Production
  public abstract class InternalEvent {
    protected LogicalTime _time;
    protected int _chef;  //serial, not instance handle
    protected int _sushiType; //type of sushi we're making

    public LogicalTime getTime() { return _time; }
    public int getChef() { return _chef; }
    public int getType() { return _sushiType; }
    public abstract void dispatch()
    throws RTIexception;
  }

  /**内部事件，内部事件只有在联邦成员的仿真时间已经推进到该事件的时间才能进行处理
   * 与外部事件必须分开处理*/
  public final class FinishMakingSushiEvent extends InternalEvent {
    public FinishMakingSushiEvent(LogicalTime time, int chef, int type) {
      _time = time;
      _chef = chef;
      _sushiType = type;
    }

    public void dispatch()
    throws RTIexception
    {
      //chef has finished making; register new Serving
      String servingName = "S_" + _federateHandle + "_" + _nextServingSerial++;
      int servingHandle = _rti.registerObjectInstance(
        _ServingClass,
        servingName);
      //add new instance to our collection
      Serving serving = new Serving();
      serving._privilegeToDeleteObjectState = AttributeState.OWNED_CONSISTENT;
      serving._handle = servingHandle;
      serving._name = servingName;
      Position pos = new Position(
        _chefTable.getPosition(_chef), //Serving starts where chef is
        Position.INBOARD_CANAL);
      serving._position = pos;
      serving._positionState = AttributeState.OWNED_INCONSISTENT;
      serving._type = _sushiType;
      serving._typeState = AttributeState.OWNED_INCONSISTENT;
      serving._privilegeToDeleteObjectState = AttributeState.OWNED_CONSISTENT;
      _servings.put(new Integer(servingHandle), serving);
      //update chef's state and serving
      _chefTable.setState(_chef, Chef.LOOKING_FOR_BOAT);
      _chefTable.setServing(_chef, servingHandle);
      _chefTable.setServingName(_chef, servingName);
    }
  } //end FinishMakingSushiEvent

  //represents one callback coming from RTI
  //This class is defined within Production so the dispatch routines
  //of its subclasses have the context of Production
  public abstract class Callback {
    //returns true if event was a grant
    public abstract boolean dispatch()    throws RTIexception;
  }

  //represents one callback that is an event (carries a time)
  public abstract class ExternalEvent extends Callback {
    protected LogicalTime _time;

    public LogicalTime getTime() { return _time; }
    //returns true if event was a grant
    public abstract boolean dispatch()    throws RTIexception;
  }

  public void queueAttributeOwnershipDivestitureNotification(int object,AttributeHandleSet attributes) {
    Callback callback = new AODNcallback(object, attributes);
    _callbackQueue.enqueue(callback);
  }

  public final class AODNcallback extends Callback {
    private int _object;
    private AttributeHandleSet _attributes;

    public AODNcallback(int object,AttributeHandleSet attributes) {
      _object = object;
      _attributes = attributes;
    }

    public boolean dispatch() {
      SuppliedAttributes sa;
      EventRetractionHandle erh;
      LogicalTime sendTime;
      try {
        //record transfer of ownership
        Serving serving = (Serving)(_servings.get(new Integer(_object)));
        if (serving == null) throw new ProductionInternalError(
          "Serving " + _object + " not known");
        serving._positionState = AttributeState.NOT_REFLECTED; //we're not subscribed
        _userInterface.post("AODN Serving " + serving._name);
      }
      catch (ProductionInternalError e) {
        _userInterface.post("ERROR AODN: " + e.getMessage());
      }
      return false;
    }
  } //end AttributeOwnershipDivestitureNotificationCallback

  public void queueDiscoverObjectInstance(int handle,int objectClass,String name) {
    Callback callback = new DiscoverObjectInstanceCallback(handle,objectClass,name);
    _callbackQueue.enqueue(callback);
  }

  public final class DiscoverObjectInstanceCallback extends Callback
  {
    private int _instanceHandle;
    private int _objectClass;
    private String _instanceName;

    public DiscoverObjectInstanceCallback(int handle,int objectClass,String name) {
      _instanceHandle = handle;
      _objectClass = objectClass;
      _instanceName = name;
    }

    public boolean dispatch() {
      try {
        if (_objectClass != _BoatClass) throw new ProductionInternalError(
          "instance " + _instanceHandle + "(" + _instanceName + ") of class "
          + _objectClass + " not a Boat!");
        Boat newBoat = new Boat();
        newBoat._handle = _instanceHandle;
        newBoat._name = _instanceName;
        newBoat._privilegeToDeleteObjectState = AttributeState.NOT_REFLECTED;
        newBoat._positionState = AttributeState.DISCOVERED;
        newBoat._spaceAvailableState = AttributeState.DISCOVERED;
        newBoat._cargoState = AttributeState.DISCOVERED;
        _knownBoats.put(new Integer(_instanceHandle), newBoat);
        _userInterface.post("Discovered Boat " + _instanceHandle + "(" + _instanceName + ")");
      }
      catch (ProductionInternalError e) {
        _userInterface.post("ERROR discoverObjectInstance: " + e.getMessage());
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
    /**对每类回调函数的处理的方法，返回Boolean值，当事件与TIME ADVANCE GRANT' 相关的时候返回真*/
    public boolean dispatch() {
      _logicalTime.setTo(_time);
      //_userInterface.post("...granted to " + _logicalTime);
      _userInterface.setLogicalTime(((LogicalTimeDouble)_logicalTime).getValue());
      _userInterface.setTimeStateGranted();
      return true;
    }
  } //end GrantEvent

  public void queueProvideAttributeValueUpdateCallback(int objectHandle,AttributeHandleSet theAttributes)
  {
    Callback callback = new ProvideAttributeValueUpdateCallback(objectHandle,theAttributes);
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

    public boolean dispatch()    throws RTIexception
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
            if (entry._typeState == AttributeState.OWNED_CONSISTENT)
              entry._typeState = AttributeState.OWNED_INCONSISTENT;
          }
          else throw new ObjectNotKnown("Serving " + _object + " not known.");
        }
        else if (classHandle == _ChefClass) {
          _userInterface.post("ProvAttrUpd for Chef " + _object);
          _chefTable.markForUpdate(_object);
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

  public void queueReceiveInteractionCallback(int interactionClass,ReceivedInteraction theInteraction)  {
    Callback callback = new ReceiveInteractionCallback(      interactionClass,      theInteraction);
    _callbackQueue.enqueue(callback);
  }

  public final class ReceiveInteractionCallback extends Callback {
    int _class;
    ReceivedInteraction _interaction;

    public ReceiveInteractionCallback(      int interactionClass,      ReceivedInteraction theInteraction)
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

  public void queueReceiveInteractionEvent(  int interactionClass, ReceivedInteraction theInteraction,  LogicalTime time)
  {
    ExternalEvent event = new ReceiveInteractionEvent(interactionClass,theInteraction,time);
    _callbackQueue.enqueue(event);
  }

  public final class ReceiveInteractionEvent extends ExternalEvent {
    int _class;
    ReceivedInteraction _interaction;

    public ReceiveInteractionEvent(      int interactionClass,      ReceivedInteraction theInteraction,      LogicalTime time)
    {
      _time = time;
      _class = interactionClass;
      _interaction = theInteraction;
    }

    public boolean dispatch() throws RTIexception {
      try {
        if (_class != _TransferAcceptedClass)
          throw new ProductionInternalError("interaction of unknown class " + _class);
        //retrieve serving name from parameter
        String servingName = InstanceName.decode(_interaction.getValueReference(0), 0);

        int object = _rti.getObjectInstanceHandle(servingName);
        int chefSerial = _chefTable.getChefForServing(object);
        if (chefSerial < 0) throw new ProductionInternalError("TransferAccepted for"
          + " serving " + servingName + " not held by any chef");
        if (_chefTable.getState(chefSerial) != Chef.WAITING_TO_TRANSFER) throw new
          ProductionInternalError("TransferAccepted for serving " + servingName
          + " held by chef serial " + chefSerial + "which was in state "
          + _chefTable.getState(chefSerial));
        _userInterface.post("TransferAccepted: " + servingName + " from chef serial "
          + chefSerial + " " + _time);
        //change state of chef
        _chefTable.setState(chefSerial, Chef.MAKING_SUSHI);
        _chefTable.setServing(chefSerial, 0);
        _chefTable.setServingName(chefSerial, "");
        _chefTable.setBoatHandle(chefSerial, 0);
        //schedule production of another serving
        //what kind of sushi?
        int type = Math.abs(_random.nextInt()) % _numberOfSushiTypes;
        //put event on internal queue
        LogicalTime eventTime = new LogicalTimeDouble(0.0);
        eventTime.setTo(_logicalTime);
        eventTime.increaseBy(_manufactureTimes[type]);
        _internalQueue.enqueue(new FinishMakingSushiEvent(eventTime, chefSerial, type));
      }
      catch (ProductionInternalError e) {
        _userInterface.post("ERROR receiveInteraction dispatch: " + e.getMessage());
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR receiveInteraction dispatch: " + e);
      }
      return false;
    }
  } //end ReceiveInteractionEvent

  public void queueReflectAttributeValuesEvent(    LogicalTime time,    int objectHandle,    ReflectedAttributes attributes,    byte[] tag)
  {
    ExternalEvent event = new ReflectAttributeValuesEvent(      time,      objectHandle,      attributes,      tag);
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
        //this had better be a Boat; that's all we're subscribed to
        int oclass = _rti.getObjectClass(_object);
        if (oclass != _BoatClass) throw new ProductionInternalError (
          "unexpected class " + oclass + " instance " + _object);
        //find the Boat in our collection
        Boat boat = (Boat)_knownBoats.get(new Integer(_object));
        if (boat == null) throw new ProductionInternalError ("unknown Boat " + _object);
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
          else throw new ProductionInternalError("unknown attribute " + handle);
        }
        //_userInterface.post("Reflect " + boat._name + " at " + _time);
        //if the boat is empty, see if a chef wants to put something on it
        if (boat._spaceAvailable) doWeWantToLoadThisBoat(boat);
        //should we cancel attempt to load this boat?
        checkLoadingCancellation(boat);
      }
      catch (ProductionInternalError e) {
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

    private void doWeWantToLoadThisBoat(Boat boat) throws RTIexception
    {
      int chefCount = _chefTable.getRowCount();
      for (int chef = 0; chef < chefCount; ++chef) {
        if (_chefTable.getState(chef) == Chef.LOOKING_FOR_BOAT
          && _chefTable.isInReach(chef, boat._position._angle, _chefsReach)) {
          String boatName = _rti.getObjectInstanceName(_object);
          _userInterface.post("Chef " + chef + " attempting to load boat "
            + boatName);
          //change state of chef
          _chefTable.setState(chef, Chef.WAITING_TO_TRANSFER);
          _chefTable.setBoatHandle(chef, boat._handle);
          //begin negotiated divestiture of Serving position attribute
          //user-supplied tag contains Boat's instance name
          _rti.negotiatedAttributeOwnershipDivestiture(
            _chefTable.getServing(chef),
            _positionAttributeAsSet,
            boatName.getBytes());
          //break;
        }
      }
    }

    private void checkLoadingCancellation(Boat boat) throws RTIexception, ArrayIndexOutOfBounds
    {
      int chefCount = _chefTable.getRowCount();
      for (int chef = 0; chef < chefCount; ++chef) {
        try {
          if (_chefTable.getState(chef) == Chef.WAITING_TO_TRANSFER
            && boat._handle == _chefTable.getBoatHandle(chef)
            && !_chefTable.isInReach(chef, boat._position._angle, _chefsReach)) {
            String boatName = _rti.getObjectInstanceName(_object);
            _userInterface.post("Chef " + chef + " cancelling attempt to load boat "
              + boatName);
            //cancel the negotiated transfer of Serving position attribute
            _rti.cancelNegotiatedAttributeOwnershipDivestiture(
              _chefTable.getServing(chef),
              _positionAttributeAsSet);
            //change state of Chef
            _chefTable.setState(chef, Chef.LOOKING_FOR_BOAT);
            _chefTable.setBoatHandle(chef, -1);
          }
        }
        catch (AttributeNotOwned e) {
          //this might occur if the RTI has already transferred ownership of
          //the attribute but we haven't yet acted on the
          //attributeOwnershipDivestitureNotification, in which case we merely skip
          //the change of state and assume the AODN will catch up with us
          _userInterface.post("Attempted to cancel divestiture of Serving position"
            + " attribute when RTI had already transferred it, chef: "
            + chef + " serving: " + _chefTable.getServing(chef));
        }
      }
    }
  } //end ReflectAttributeValuesEvent

  public void queueRemoveObjectInstanceEvent(int objectHandle)
  {
    ExternalEvent event = new RemoveObjectInstanceEvent(objectHandle);
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
      Integer servingKey = new Integer(_object);
      Serving serving = (Serving)_servings.get(servingKey);
      if (serving == null) throw new ObjectNotKnown("Object " + _object + " not known");
      //_userInterface.post("Serving " + serving._name + " being removed");
      //Hashtables are synchronized
      _servings.remove(servingKey);
      return false;
    }
  } //end RemoveObjectInstanceEvent

  public void queueRequestAttributeOwnershipReleaseCallback(
    int objectHandle,
    AttributeHandleSet attributes,
    byte[] tag)
  {
    Callback callback = new RAORcallback(
      objectHandle,
      attributes,
      tag);
    _callbackQueue.enqueue(callback);
  }

  public final class RAORcallback extends Callback {
    int _object;
    AttributeHandleSet _attributes;
    byte[] _tag;

    public RAORcallback(
      int objectHandle,
      AttributeHandleSet attributes,
      byte[] tag)
    {
      _object = objectHandle;
      _attributes = attributes;
      _tag = tag;
    }

    public boolean dispatch()
    throws RTIexception
    {
      try {

        if (_rti.getObjectClass(_object) != _ServingClass) throw new
          ProductionInternalError("Instance " + _object + "is of class "
            + _rti.getObjectClass(_object));
        Integer servingKey = new Integer(_object);
        Serving serving = (Serving)_servings.get(servingKey);
        if (serving == null) throw new ObjectNotKnown("instance " + _object
          + " not known.");
        if (_attributes.equals(_privilegeToDeleteObjectAttributeAsSet)) {
          if (serving._privilegeToDeleteObjectState != AttributeState.OWNED_CONSISTENT)
            throw new AttributeNotOwned(
            "privilegeToDeleteObject of instance " + _object + " not owned.");
          AttributeHandleSet released = _rti.attributeOwnershipReleaseResponse(
            _object,
            _privilegeToDeleteObjectAttributeAsSet);
          if (!released.equals(_privilegeToDeleteObjectAttributeAsSet))
            throw new ProductionInternalError("attributes " + released
              + " were released, not " + _privilegeToDeleteObjectAttributeAsSet);
          serving._privilegeToDeleteObjectState = AttributeState.DISCOVERED;
        }
        else {
          throw new AttributeNotKnown("attributes " + _attributes
            + " of instance " + _object + " not known.");
        }
      }
      catch (ProductionInternalError e) {
        _userInterface.post("ERROR requestAttributeOwnershipRelease: "
          + e.getMessage());
      }
      catch (RTIexception e) {
        _userInterface.post("ERROR requestAttributeOwnershipRelease: " + e);
      }
      return false;
    }
  } //end RequestAttributeOwnershipReleaseCallback

}
