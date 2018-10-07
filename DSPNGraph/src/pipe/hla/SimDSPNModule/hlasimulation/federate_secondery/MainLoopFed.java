package pipe.hla.SimDSPNModule.hlasimulation.federate_secondery;

import hla.rti1516.*;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.basemodel.VexNode;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.auxiliary.*;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.IOEvent.FinishTokenReceivedEvent;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.IOEvent.FireTransitionEvent;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.IOEvent.GrantEvent;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.abstractEvent.Callback;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.abstractEvent.ExternalEvent;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.abstractEvent.InternalEvent;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceNode;
import se.pitch.prti1516.LogicalTimeIntervalDouble;
import se.pitch.prti1516.RTI;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 联邦成员
 * 当联邦成员需要某项服务时，可向RTI代理提出服务请求，
 * 然后RTI代理向RTIExec或FedExec调用其成员函数，
 * 再由FederateAmbassador进行回调函数的调用，并将服务结果返回给联邦成员*/
public class MainLoopFed {
    private static String _fileSeparator = System.getProperty("file.separator");
    private static String _userDirectory = System.getProperty("user.dir");
    public static void main(String[] args) {
        LogicalTimeInterval distance = new LogicalTimeIntervalDouble(23.3234);
        String str = distance.toString();
        System.out.println(str);
        int i=0;
        for(;i<str.length();i++){
            if('<' == str.charAt(i)){
                break;
            }
        }
        String s =str.substring(i+1,str.length()-1);
        double d = Double.parseDouble(s);
        System.out.println(d);
    }

    //RTI代理
    private RTIambassador rti;
    //联邦代理
    private MyFederateImpl _fedAmb;
    private InteractionClassHandle tokenInteractionHandle;
    private InteractionClassHandle simulationEndHandle;
    private ParameterHandle sourceNameHandle;
    private ParameterHandle targetNameHandle;
    private ParameterHandle weightHandle;

    //private FederateHandle federateHandle;
    /**内部事件队列，成员自己维护*/
    private InternalQueue _internalQueue;//存储内部事件

    //RTI每次回调联邦成员时，就会向_callbackQueue中放置一个Production.Callback实例，并进行处理
    //联邦成员的主线程在时间推进循环中会从_callbackQueue取出。。实例，并进行相应的处理（除了announce同步点）
    private CallbackQueue _callbackQueue;//允许主线程一次处理一个回调函数，同时协调主线程和回调线程！

    //we use Pitch-supplied LogicalTimeDouble
    //and LogicalTimeIntervalDouble to talk to RTI
    private LogicalTime _logicalTime;
    private LogicalTime lastLogicalTime;
    //前瞻量的选择取决于响应内部事件和外部事件的需要
    private LogicalTimeInterval _lookahead;
    LogicalTime t0 = new LogicalTimeDouble(0.0);

    //TODO 是不是应该代表子网？
    private OLGraph subGraph;
    private String federateName;
    private AtomicInteger atomicInteger;
    private int len;

    /**TODO 这几个同步点是由管理者联邦成员Manager注册的，这个之后确认manage是不是需要之后再说，不行就自己注册
     * 用于协调主线程*/
    private Barrier _readyToPopulateAnnouncementBarrier = new Barrier();
    private Barrier _readyToRunAnnouncementBarrier = new Barrier();
    private Barrier _readyToResignAnnouncementBarrier = new Barrier();

    private volatile boolean _reservationComplete;
    private volatile boolean _reservationSucceeded;
    private boolean simulationEndsReceived;
    private final Object _reservationSemaphore = new Object();

    public MainLoopFed(OLGraph olGraph,String federateName,AtomicInteger atomicInteger,int len) throws RTIinternalError, UnknownHostException, ConnectException {
        _logicalTime = new LogicalTimeDouble(0);
        //TODO  这里的Lookahead我应该设置多少比较好呢？？？
        _lookahead = new LogicalTimeIntervalDouble(Constant.lookahead);
        _fedAmb = new MyFederateImpl(this);

        Constant._simulationEnds = false;
        this.atomicInteger = atomicInteger;
        this.len = len;

        _internalQueue = new InternalQueue();
        _callbackQueue = new CallbackQueue();
        //TODO 这里需要SubGraphUtils类操作，暂时按下面的写
        this.subGraph = olGraph;
        this.federateName = federateName;
        //做冲突标识操作
        subGraph.checkConflictStructure();
        //1
        rti = RTI.getRTIambassador(Constant.HOSTNAME, Constant.CRC_PORT);
    }

    public void run() throws RTIexception, ConnectException, UnknownHostException, MalformedURLException {

        Barrier barrier;
        Object[] result;

        //2
        String urlString = System.getProperty("CONFIG","file:" + _userDirectory + _fileSeparator + "config" + _fileSeparator) + "PetriNet.xml";

        if(rti != null){
            try{
                rti.createFederationExecution(Constant.FEDERATION_NAME,new URL(urlString));
                System.out.println(Constant.FEDERATION_NAME+"联邦创建成功！");
            }catch (FederationExecutionAlreadyExists e){
                System.out.println("Federation execution " + Constant.FEDERATION_NAME + " already exists.");
            }

        }else{
            try {
                throw new Exception("rti 为null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //3
        //federateHandle =
        rti.joinFederationExecution(federateName, Constant.FEDERATION_NAME,_fedAmb,null);
        System.out.println(federateName+"加入联邦！");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        barrier = new Barrier();//创建了一个阻塞实例
        _fedAmb.setEnableTimeConstrainedBarrier(barrier);//并且该实例在FedAmbImpl中进行设置
        /**主线程等待时，它可以不断循环等待一个变量状态的变化，这个状态值可以在回调线程中设置。但是这样会浪费资源。
         * 所以在这个方法前创建了一个阻塞实例，并且该实例在FedAmbImpl中进行设置。*/
        rti.enableTimeConstrained();//回调的时候唤醒了barrier
        result = barrier.await();//主线程进行休眠状态，等待回调函数来唤醒主线程
        _logicalTime = (LogicalTime) result[0];
        System.out.println("constraint enabled at" + _logicalTime);

        //enable time regulation
        _logicalTime = new LogicalTimeDouble(Constant.initialTime);

        //barrier2
        barrier = new Barrier();
        _fedAmb.setEnableTimeRegulationBarrier(barrier);
        rti.enableTimeRegulation(_lookahead);
        result = barrier.await();

        _logicalTime = (LogicalTime) result[0];

        System.out.println("regulation enabled at" + _logicalTime);


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //4、订阅和发布对象类（属性）,交互类（参数）
        getHandles();
        subscribeAndPublishObjectClassAndInteraction();

        /**
        System.out.println("Waiting for all federates to join...");
        int _numberOfFederatesToAwait = Integer.parseInt(Constant.numberOfFederatesToAwait);
        while (_federateTable.getRowCount() < _numberOfFederatesToAwait) {
            Callback callback = _callbackQueue.dequeue();
            boolean ignore = callback.dispatch();
            _federateTable.updateFederates(_rti, _federateAttributesSet);
        }
        System.out.println("...done.");*/


        _readyToPopulateAnnouncementBarrier.await();
        barrier = new Barrier(Constant._readyToPopulate);
        System.out.println("Waiting for ReadyToPopulate...");
        _fedAmb.setFederationSynchronizedBarrier(barrier);
        rti.synchronizationPointAchieved(Constant._readyToPopulate);
        result = barrier.await();
        System.out.println("...federation synchronized.");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        _readyToRunAnnouncementBarrier.await();
        System.out.println("Waiting for ReadyToRun...");
        barrier = new Barrier(Constant._readyToRun);
        _fedAmb.setFederationSynchronizedBarrier(barrier);
        rti.synchronizationPointAchieved(Constant._readyToRun);
        result = barrier.await();
        System.out.println("...federation synchronized.");

        long start = System.currentTimeMillis();
        //5、Reserve object instance name and register object instance
        //TODO 或许可以在构建实例的时候，确定第一个要发生的变迁，这里可以把一个初始事件加入内部队列中,先人工的实验一下
        initialInstances();

        timeLoop:
        while(!Constant._simulationEnds){
            System.out.println("Adv");

            //TODO 如果内部事件队列为null，这里返回的是最大值==>这里指的是Integer类的最大值，假设时间推进不超过Integer类的最大值。之后有特殊情况再改正
            LogicalTime timeToMoveTo = _internalQueue.getTimeAtHead();
            //TODO 当这里返回最大值的时候，竟然推不动时间了，怎么回事？？？
            System.out.println("内部事件队列的第一个事件，申请时间："+timeToMoveTo.toString());
            //TODO 当这里时间最大的时候，next请求应该阻塞了，但是没有！！！！！！
            rti.nextMessageRequestAvailable(timeToMoveTo);//发出时间推进申请  这里得考虑瞬时变迁，时间戳为0，加0即可，都通用

            //等待并处理TSO事件和其他RTI的回调服务；等待直到RTI调用timeAdvanceGrant回调
            //仿真时间推进到批准时间
            //如果仿真时间大于或等于内部事件的时间，从队列中移掉该内部事件并加以处理
            boolean wasTimeAdvanceGrant;

            do{//内部循环等待并分发回调函数，包含了时间推进请求GrantEvent、接收消息回调ReceivedInteractionEvent等等
                Callback callback = _callbackQueue.dequeue();

                //只有在事件与TimeAdvanceGrant相关的时候，该布尔值才为真  接收到的交互在这里处理，回到自己的dispatch（）
                wasTimeAdvanceGrant = callback.dispatch();

                if (Constant._simulationEnds)
                    break timeLoop;

            }while (!wasTimeAdvanceGrant);//直到收到TIME ADVANCE GRANT'。wasTimeAdvanceGrant为false，进入循环
            //然后在新的批准时间，外部循环更新联邦成员的内部状态，并检查内部事件队列，之后在发出时间推进请求
            updateInternalStateAtNewTime();
        }
        System.out.println("SimulationEnds received.");

        long end = System.currentTimeMillis();
        long value = end - start;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~B运行时间是："+value);


        //当前联邦成员achieves **ReadyToResign** and waits for rest of federation,Last 第二步
        _readyToResignAnnouncementBarrier.await();
        System.out.println("Waiting for ReadyToResign...");
        barrier = new Barrier(Constant._readyToResign);
        _fedAmb.setFederationSynchronizedBarrier(barrier);
        rti.synchronizationPointAchieved(Constant._readyToResign);
        result = barrier.await();
        System.out.println("...federation synchronized.");

        rti.resignFederationExecution(ResignAction.DELETE_OBJECTS_THEN_DIVEST);
        // TODO federateHandle = -1;
        System.out.println("All done!");
        //最后一步：销毁联邦执行 TODO 是不是应该让manage来做
        //rti.destroyFederationExecution(Constant.FEDERATION_NAME);
    }

    //announceSynchronizationPoint()回调函数调用
    // federate ambassador calls this when it gets an announcement
    public void recordSynchronizationPointAnnouncement(String label) {
        if (label.equals(Constant._readyToPopulate))
            _readyToPopulateAnnouncementBarrier.lower(null);
        else if (label.equals(Constant._readyToRun))
            _readyToRunAnnouncementBarrier.lower(null);
        else if (label.equals(Constant._readyToResign))
            _readyToResignAnnouncementBarrier.lower(null);
        else
            System.out.println("INFO: unexpected sync point announced: " + label);
    }

    private void initialInstances() throws SaveInProgress, ObjectInstanceNameInUse, RestoreInProgress, ObjectClassNotPublished, ObjectClassNotDefined, FederateNotExecutionMember, RTIinternalError, ObjectInstanceNameNotReserved {
        // 在内部事件队列中放入一个事件
        try {
            subGraph.setTransitionEnabledStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<VexNode> fired = subGraph.getWillFiredTransitions();
        if(fired == null){

        }else{
            for(int i=0;i<fired.size();i++){
                //将要发生的变迁转换为内部事件,并添加进入内部事件队列中
                _internalQueue.enqueue(new FireTransitionEvent(this,fired.get(i),subGraph,atomicInteger));
            }
        }

    }


    /**TODO 每个类中的初始化问题怎么办，也是获得handle呀？*/
    private void getHandles() throws RTIexception
    {
        tokenInteractionHandle = rti.getInteractionClassHandle(Constant.ms_TokenInteractionTypeStr);
        sourceNameHandle = rti.getParameterHandle(tokenInteractionHandle, Constant.ms_SourceNameTypeStr);
        targetNameHandle = rti.getParameterHandle(tokenInteractionHandle, Constant.ms_TargetNameTypeStr);
        weightHandle = rti.getParameterHandle(tokenInteractionHandle, Constant.ms_WeightTypestr);
    }

    /**
     * TODO 这个需要修改,不急*/
    private void reserveObjectName(String noteName) {
        // Reserve object instance name and register object instance

        do {
            //获得Petri网的子网
            System.out.print("库所或变迁的名字: ");
            //TODO 这里需要一个外部文件的输入
            try {
                _reservationComplete = false;
                rti.reserveObjectInstanceName(noteName);

                synchronized (_reservationSemaphore) {
                    // Wait for response from RTI
                    while (!_reservationComplete) {
                        try {
                            _reservationSemaphore.wait();
                        } catch (InterruptedException ignored) {

                        }
                    }
                }
                if (!_reservationSucceeded) {
                    System.out.println("Name already taken, try again.");
                }
            } catch (IllegalName e) {
                System.out.println("Illegal name. Try again.");
            } catch (RTIexception e) {
                System.out.println("RTI exception when reserving name: " + e.getMessage());
                return;
            }
        } while (!_reservationSucceeded);
    }

    private void subscribeAndPublishObjectClassAndInteraction() throws NameNotFound, FederateNotExecutionMember, RTIinternalError, InvalidInteractionClassHandle, FederateServiceInvocationsAreBeingReportedViaMOM, InteractionClassNotDefined, RestoreInProgress, SaveInProgress, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined {
        //订阅和发布交互类 TODO 仿真终止交互类
        rti.publishInteractionClass(tokenInteractionHandle);
        rti.subscribeInteractionClass(tokenInteractionHandle);
    }

    /**发送消息的调用在FireTransitionEvent事件里面*/
    public void sendMessage(VexNode source,String targetName,int weight){
        //6、发送交互消息
        ParameterHandleValueMap parameters = null;
        try {
            parameters = rti.getParameterHandleValueMapFactory().create(3);
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        }

        byte[] value = InstanceName.encode(source.getPlaceOrTransition().getName());
        parameters.put(sourceNameHandle, value);

        value = new String(weight+"").getBytes();
        parameters.put(weightHandle, value);

        value = InstanceName.encode(targetName);
        parameters.put(targetNameHandle, value);


        LogicalTime sendTime = null;
        try {
            /*TODO 注意，这里加的是Lookahead，而不是变迁的时间，所以，交互类的属性可能要发生变化。
            变迁的发生的时间也不应该放在这里了。只要当那个值要比这个数小，才会发生*/
            sendTime = _logicalTime.add(_lookahead);
            //System.out.println("sendTime:"+sendTime+",_logicalTime:"+_logicalTime+",_lookahead:"+_lookahead);
            MessageRetractionReturn r = rti.sendInteraction(tokenInteractionHandle, parameters, null,sendTime);
           // System.out.println(r.retractionHandleIsValid);
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (InvalidLogicalTime invalidLogicalTime) {
            invalidLogicalTime.printStackTrace();
        } catch (InteractionClassNotPublished interactionClassNotPublished) {
            interactionClassNotPublished.printStackTrace();
        } catch (InteractionParameterNotDefined interactionParameterNotDefined) {
            interactionParameterNotDefined.printStackTrace();
        } catch (IllegalTimeArithmetic illegalTimeArithmetic) {
            illegalTimeArithmetic.printStackTrace();
        }
    }
    /**timeAdvanceGrant回调函数中调用的服务,当事件的时间是自定义的无穷大的时候，不能加入队列*/
    public void queueGrantEvent(LogicalTime time) {
        //System.out.println("---------------------");
        //System.out.println("queueGrantEvent中的时间值为："+time.toString());

        //System.out.println("time.compareTo(infinity):"+time.compareTo(new LogicalTimeDouble(Constant.InternalQueue_IS_NULL_TIME)));
        if(time.compareTo(new LogicalTimeDouble(Constant.InternalQueue_IS_NULL_TIME)) < 0){
            ExternalEvent event = new GrantEvent(subGraph,this,time);
            //System.out.println("***queueGrantEvent的_callbackQueue.enqueue");
            _callbackQueue.enqueue(event);
        }
        //System.out.println("-------------");
    }


    /**
     * 每次来自于RTI的回调线程都要向_callbackQueue加入一个CallBack实例==>仿真结束的交互*/
    public void queueReceiveInteractionCallback(InteractionClassHandle interactionClass,ParameterHandleValueMap parameterHandleValueMap)  {
        //Callback callback = new ReceiveInteractionCallback(interactionClass,parameterHandleValueMap);
        //System.out.println("***queueReceiveInteractionCallback的_callbackQueue.enqueue");
        //_callbackQueue.enqueue(callback);
    }
    /**这个是接收结束的消息
     * TODO 这里应该创建一个内部事件，而不是外部事件，因为后面马上要对内部事件进行处理了，且还会创建一个推进时间的外部事件*/
    public void queueFinishTokenReceivedEvent(InteractionClassHandle interactionClass,ParameterHandleValueMap parameterHandleValueMap,LogicalTime time)
    {
        //ExternalEvent event = new ReceiveInteractionEvent(interactionClass,parameterHandleValueMap,time);
        //System.out.println("***queueReceiveInteractionEvent的_callbackQueue.enqueue");
        //_callbackQueue.enqueue(event);

        InternalEvent event = new FinishTokenReceivedEvent(this,interactionClass,sourceNameHandle,targetNameHandle,weightHandle,parameterHandleValueMap,time,atomicInteger);
        //System.out.println("***queueFinishTokenReceivedEvent的_internalQueue.enqueue");
        _internalQueue.enqueue(event);
    }

    /**
     * 在新的批准时间，外部循环更新联邦成员的内部状态，并检查内部事件队列*/
    private void updateInternalStateAtNewTime() throws RTIexception {
        //重新检查使能的事件，是不是当前事件的发生让其不再使能，或者不能再次发生
        checkInternalQueue();
        checkEndOfSimulation();
    }

    private void checkEndOfSimulation() {
        if(atomicInteger.get() >= len){
            Constant._simulationEnds = true;
        }
    }


    /**called when we get a time advance grant
     * */
    private void checkInternalQueue()  throws RTIexception
    {
        //System.out.println("Fed1:"+_internalQueue.getTimeAtHead().toString());
        //System.out.println("Fed1:"+_logicalTime.toString());
        /**
         * 如果队列为null，那么返回的值就是Long.MAX_VALUE,肯定不满足下面的情况，也就不会处理*/
        while (_internalQueue.getTimeAtHead().compareTo(_logicalTime) <= 0) {
            //出队，并进行处理
            System.out.println("Fed1:"+_internalQueue.getTimeAtHead().toString());
            InternalEvent event = _internalQueue.dequeue();
            //TODO 如果这个事件为null，说明没有事件将要发生==》要么结束要么等待外部事件的到来？？？
            if(event != null){
                System.out.println("内部队列弹出的事件,准备发生（checkInternalQueue函数），发生时间： " + _logicalTime);

                event.dispatch();
            }else {
                //当事件为null的时候，请求时间为最大值，那么此时不创建新的事件，那么队列就是null，wait就生效了
                try {
                    throw new Exception("TODO 怎么处理事件为null？？？按理来说处理不到了呀，看上面的值会不会是-1，为什么呢？");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 从子网中查找库所，根据handle或者名字
    public VexNode FindP(Object object ){
        //System.out.println("***FindP");
        if(object instanceof String){
            for(int i=0;i<subGraph.getPlacenum();i++){
                VexNode vexNode = subGraph.getXlist().get(i);
                PlaceNode placeNode = (PlaceNode) vexNode.getPlaceOrTransition();
                //System.out.println(object+","+placeNode.getName());
                if(placeNode.getName().equals(object))
                    return vexNode;
            }
        }
        return null;
    }

    public  LogicalTime get_logicalTime() {
        return _logicalTime;
    }

    public  void set_logicalTime(LogicalTime _logicalTime) {
        this._logicalTime = _logicalTime;
    }

    public  LogicalTimeInterval get_lookahead() {
        return _lookahead;
    }

    public  void set_lookahead(LogicalTimeInterval _lookahead) {
        this._lookahead = _lookahead;
    }

    public  InternalQueue get_internalQueue() {
        return _internalQueue;
    }

    public  InteractionClassHandle getTokenInteractionHandle() {
        return tokenInteractionHandle;
    }

    public  InteractionClassHandle getSimulationEndHandle() {
        return simulationEndHandle;
    }


    public  void setSimulationEndsReceived(boolean simulationEndsReceived) {
        this.simulationEndsReceived = simulationEndsReceived;
    }

    public  OLGraph getSubGraph() {
        return subGraph;
    }
}
