package pipe.hla.SimDSPNModule.hlasimulation.federate_secondery;

import hla.rti.ObjectClassNotKnown;
import hla.rti1516.*;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.auxiliary.Barrier;
import se.pitch.prti1516.FederateAmbassadorImpl;


public class MyFederateImpl extends FederateAmbassadorImpl {
    public MyFederateImpl(MainLoopFed _fed) {
        this._fed = _fed;
    }

    private MainLoopFed _fed;
    private Barrier _enableTimeConstrainedBarrier = null;
    private Barrier _enableTimeRegulationBarrier = null;
    private Barrier _synchronizationPointRegistrationSucceededBarrier = null;
    private Barrier _federationSynchronizedBarrier = null;
    private Barrier _timeAdvanceGrantBarrier = null;

    //we use Pitch-supplied LogicalTimeDouble
    //and LogicalTimeIntervalDouble to talk to RTI
    private LogicalTime _logicalTime;
    private LogicalTimeInterval _lookahead;


    public void setEnableTimeConstrainedBarrier(Barrier b) {
        _enableTimeConstrainedBarrier = b;
    }

    public void setEnableTimeRegulationBarrier(Barrier b) {
        _enableTimeRegulationBarrier = b;
    }

    public void setFederationSynchronizedBarrier(Barrier b) {
        _federationSynchronizedBarrier = b;
    }

    public void setSynchronizationPointRegistrationSucceededBarrier(Barrier b) {
        _synchronizationPointRegistrationSucceededBarrier = b;
    }
    public void setTimeAdvanceGrantBarrier(Barrier b) {
        _timeAdvanceGrantBarrier = b;
    }
    /**
     * 该回调函数为联邦成员提供一个仿真时间*/
    @Override
    public void timeConstrainedEnabled(LogicalTime theFederateTime) {
        //super.timeConstrainedEnabled(theFederateTime);
        //_logicalTime = theFederateTime;
        if (_enableTimeConstrainedBarrier != null) {
            Object[] returnedTime = {theFederateTime};
            _enableTimeConstrainedBarrier.lower(returnedTime);
            System.out.println("timeConstrainedEnabled_logicalTime:"+theFederateTime);
        }else
            System.out.println("timeConstrainedEnabled_theFederateTime is null");
    }


    @Override
    public void timeRegulationEnabled(LogicalTime theFederateTime) {
        //super.timeRegulationEnabled(theFederateTime);
        //_logicalTime = theFederateTime;
        if (_enableTimeRegulationBarrier != null) {
            Object[] returnedTime = {theFederateTime};
            //notifyAll
            _enableTimeRegulationBarrier.lower(returnedTime);
            System.out.println("timeConstrainedEnabled_logicalTime:"+theFederateTime);
        }else
            System.out.println("timeRegulationEnabled_theFederateTime is null");
    }

    // 6.5 TODO 目前只用到了交互，没有使用注册对象实例所以先不使用
    public void discoverObjectInstance (int theObject,int theObjectClass,String objectName) throws CouldNotDiscover, ObjectClassNotKnown, FederateInternalError{
        //_fed.queueDiscoverObjectInstance(theObject, theObjectClass, objectName);
    }

    /**调用的是这个接收函数，
     * TODO 接收到消息之后是要去创建一个内部事件，而不是外部事件*/
    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap parameterHandleValueMap, byte[] bytes, OrderType orderType, TransportationType transportationType, LogicalTime logicalTime, OrderType orderType1, MessageRetractionHandle messageRetractionHandle) {
        System.out.println("Federate的回调函数收到消息********************：时间为"+logicalTime+"接下来调用queueFinishTokenReceivedEvent，创建完成token接收内部事件，加入内部事件队列");
        //在这里计算这个差值，然后把使能队列中的变迁时延减去它
//        LogicalTimeInterval distance =  logicalTime.distance(_fed.get_logicalTime());
        _fed.queueFinishTokenReceivedEvent(interactionClassHandle,parameterHandleValueMap,logicalTime);
    }
    /*
    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap parameterHandleValueMap, byte[] bytes, OrderType orderType, TransportationType transportationType, LogicalTime logicalTime, OrderType orderType1, MessageRetractionHandle messageRetractionHandle, RegionHandleSet regionHandleSet) {
        System.out.println("********************回调函数：receiveInteraction");
        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        super.receiveInteraction(interactionClassHandle, parameterHandleValueMap, bytes, orderType, transportationType, logicalTime, orderType1, messageRetractionHandle, regionHandleSet);
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap parameterHandleValueMap, byte[] bytes, OrderType orderType, TransportationType transportationType, LogicalTime logicalTime, OrderType orderType1, RegionHandleSet regionHandleSet) {

        System.out.println("********************回调函数：receiveInteraction");
        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        super.receiveInteraction(interactionClassHandle, parameterHandleValueMap, bytes, orderType, transportationType, logicalTime, orderType1, regionHandleSet);
    }



    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap parameterHandleValueMap, byte[] bytes, OrderType orderType, TransportationType transportationType, RegionHandleSet regionHandleSet) {
        System.out.println("********************回调函数：receiveInteraction");
        System.out.println("ddddddddddddddddddddddddddddddddddd");
        super.receiveInteraction(interactionClassHandle, parameterHandleValueMap, bytes, orderType, transportationType, regionHandleSet);
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap parameterHandleValueMap, byte[] bytes, OrderType orderType, TransportationType transportationType) {
        System.out.println("********************回调函数：receiveInteraction");
        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        super.receiveInteraction(interactionClassHandle, parameterHandleValueMap, bytes, orderType, transportationType);
    }

    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap parameterHandleValueMap, byte[] bytes, OrderType orderType, TransportationType transportationType, LogicalTime logicalTime, OrderType orderType1) {
        System.out.println("********************回调函数：receiveInteraction");
        System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiifdfasfwefwe22222222222");
        super.receiveInteraction(interactionClassHandle, parameterHandleValueMap, bytes, orderType, transportationType, logicalTime, orderType1);
    }

    */
    /*** 8.13
     * 返回联邦成员申请的仿真时间
     * 批准时间推进的回调函数，它允许联邦成员推进到仿真时间值
     * RTI的回调函数批准联邦成员的推进请求
     * 仿真时间在其发出推进请求时并不增加，RTI批准该请求后增加
     *
     * 这个回调函数带有该联邦成员的新仿真时间，一旦调用，仿真时间就推进到新的仿真时间，联邦成员又进入到时间批准状态
     * 什么时候批准呢？
     *
     * 联邦成员F提出逻辑时间推进请求后，联邦执行FedExec将判断目前联邦的TSO事件中是否存在比截止时间小的时戳。
     * 如果存在，则设置联邦成员F的逻辑时间为TSO事件的最小时间戳值，然后处理RO事件以及所有与该时戳相同的TSO事件；
     * 如果不存在，则FedExec将判断联邦的LBTS是否超过了截止时间theTime，
     *         若超过了，FedExec就许可当前的逻辑时间为截止时间
     *         否则，FedExec等到上述推进条件的满足。
     */
    @Override
    public void timeAdvanceGrant(LogicalTime theTime) {
        System.out.println("****************timeAdvanceGrant回调函数授权时间 ： "+theTime.toString()+"，之后调用queueGrantEvent入队该时间授权事件");
        _fed.queueGrantEvent(theTime);
    }




    /**5.13
     * RTI对交互类 的建议，联邦成员发布一个交互类，并且至少一个其他联邦成员订阅了这个类，
     * 那么发布方将收到下面的服务*/
    @Override
    public void turnInteractionsOn(InteractionClassHandle var1) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@turnInteractionsOn");
    }
    /**当订阅方放弃了对这个交互类的订阅，发送方将收到RTI下面的服务*/
    @Override
    public void turnInteractionsOff(InteractionClassHandle var1) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@turnInteractionsOff");
    }


    // 5.10
    @Override
    public void startRegistrationForObjectClass(ObjectClassHandle theClass) {
        System.out.println("�(5.10)startRegistrationForObjectClass:" + theClass);
    }
    // 5.11
    @Override
    public void stopRegistrationForObjectClass(ObjectClassHandle theClass) {
        System.out.println("�(5.11)stopRegistrationForObjectClass:" + theClass);
    }

    /**所有联邦成员都到达同步点后，RTI使用下面的服务回调     同步集合    中的所有联邦成员*/
    @Override
    public void federationSynchronized(String synchronizationPointLabel) {
        if (_federationSynchronizedBarrier != null) {
            if (_federationSynchronizedBarrier.getSuppliedValue().equals(synchronizationPointLabel)) {
                //notifyAll
                _federationSynchronizedBarrier.lower(null);
                System.out.println("�(4.10)federationSynchronized notifyAll at:" + synchronizationPointLabel);
            }
            else {
                System.out.println("�(4.10)federationSynchronized at:" + synchronizationPointLabel);
            }
        }
        else {
            System.out.println("ERROR: federationSynchronized with no barrier set");
        }
    }

    @Override
    public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel) {
        if (_synchronizationPointRegistrationSucceededBarrier != null) {
            if (_synchronizationPointRegistrationSucceededBarrier.getSuppliedValue().equals(synchronizationPointLabel)) {
                _synchronizationPointRegistrationSucceededBarrier.lower(null);
            }
            else {
                System.out.println("�(4.7)synchronizationPointRegistrationSucceeded; label: "
                        + synchronizationPointLabel);
            }
        }
        else {
            System.out.println("ERROR: synchronizationPointRegistrationSucceeded with no barrier set");
        }
    }

    @Override
    public void synchronizationPointRegistrationFailed(String var1, SynchronizationPointFailureReason synchronizationPointLabel) {
        System.out.println("�(4.7)synchronizationPointRegistrationFailed; label: " + synchronizationPointLabel);
    }

    /**RTI使用下面的服务回调 同步集合  中的每一个联邦成员，同步点标识和用户标签  TODO 这个是对谁的回调呢？？？
     * //TODO  感觉这个也是需要进行调用的下面注释掉的那一行*/
    @Override
    public void announceSynchronizationPoint(String synchronizationPointLabel,byte[] userSuppliedTag) {
        _fed.recordSynchronizationPointAnnouncement(synchronizationPointLabel);
        System.out.println("label:"+synchronizationPointLabel+",+"+renderTag(userSuppliedTag));
    }
    private String renderTag(byte[] tag) {
        return (tag == null) ? "[null]" : new String(tag);
    }
}
