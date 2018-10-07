package pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.IOEvent;

import hla.rti1516.*;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.basemodel.VexNode;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.MainLoopFed;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.PetriNetInternalError;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.auxiliary.InstanceName;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.abstractEvent.InternalEvent;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceNode;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**内部事件，内部事件只有在联邦成员的仿真时间已经推进到该事件的时间才能进行处理
 * 与外部事件必须分开处理*/
public final class FinishTokenReceivedEvent extends InternalEvent {
    InteractionClassHandle interactionClassHandle;
    ParameterHandle sourceNameHandle;
    ParameterHandle targetNameHandle;
    ParameterHandle weightHandle;
    ParameterHandleValueMap _parameterHandleValueMap;
    MainLoopFed mainLoopFed;
    AtomicInteger atomicInteger;
//    LogicalTimeInterval distance;
    public FinishTokenReceivedEvent(MainLoopFed mainLoopFed, InteractionClassHandle interactionClass,
                                    ParameterHandle sourceNameHandle, ParameterHandle targetNameHandle, ParameterHandle weightHandle, ParameterHandleValueMap parameterHandleValueMap,
                                    LogicalTime time,AtomicInteger atomicInteger) {
        _time = time;
        //System.out.println("接收事件："+_time.toString());
        interactionClassHandle = interactionClass;
        this.sourceNameHandle = sourceNameHandle;
        this.targetNameHandle = targetNameHandle;
        this.weightHandle = weightHandle;
        this._parameterHandleValueMap = parameterHandleValueMap;
        this.mainLoopFed = mainLoopFed;
        this.atomicInteger = atomicInteger;
//        this.distance = distance;
    }

    public void dispatch() throws RTIexception{
        /**
         * 接收交互：
         * （一）如果是token的交互
         * 1、获得参数String：source和target
         * 2、从当前的子网中找到target这个库所
         * 3、库所汇总token的数量加权重值
         * （二）终结者的交互
         * */

        if (interactionClassHandle.equals(mainLoopFed.getTokenInteractionHandle())){
            //retrieve  parameter
            String sourceName = null;
            String targetName = null;
            String weightStr = null;
            try {
                sourceName = InstanceName.decode((byte[]) _parameterHandleValueMap.get(sourceNameHandle),0);

                targetName = InstanceName.decode((byte[]) _parameterHandleValueMap.get(targetNameHandle),0);

                weightStr = InstanceName.decode((byte[]) _parameterHandleValueMap.get(weightHandle),0);

            } catch (hla.rti.CouldNotDecode couldNotDecode) {
                couldNotDecode.printStackTrace();
            }

            int inputArcWeight = Integer.parseInt(weightStr);

            /*TODO 获得这个变迁发生的时间，如果这个时间是合理的，那么就修改联邦成员的时间,好像不用发送这个属性*/
            //LogicalTime time = (LogicalTime) _parameterHandleValueMap.get(Constant.ms_TimeOfTransitionTypeStr);

            LogicalTime now = mainLoopFed.get_logicalTime();
            //System.out.println("FinishTokenReceivedEvent的now："+now + ",成员当前时间："+_time);
            //接收的交互的时间是不是要大于当前的时间
            if(_time.compareTo(now) >= 0){//这个应该是必须的
                //ParameterHandle sourceNameHandle = rti.getParameterHandle(interactionClassHandle,sourceName);
                if(sourceName == null || targetName == null){
                    try {
                        throw new PetriNetInternalError("参数不正确");
                    } catch (PetriNetInternalError petriNetInternalError) {
                        petriNetInternalError.printStackTrace();
                    }
                }
                //System.out.println(targetName);
                //TODO 这里vexNode怎么回事null呢？
                //change state of place
                VexNode vexNode  = mainLoopFed.FindP(targetName);
                if(vexNode == null){
                    System.out.println("接收到的消息的target库所不在该子网中！");
                }else {
                    PlaceNode placeNode = (PlaceNode) vexNode.getPlaceOrTransition();
                    //int inputArcWeight = placeNode.getInputArcWeight();
                    int numOfToken = placeNode.getNumOfToken();
                    placeNode.setNumOfToken(inputArcWeight + numOfToken);

                    //TODO 下面这个要吗===》为了以后添加capacity，给上一个子网回复消息扩展使用
                    //MainLoopFed.get_internalQueue().enqueue(new FinishTokenReceivedEvent(_time));

                    //TODO 接收外界消息完成后，应该再次判断是否有新的使能变迁发生! 然后将其加入到内部队列
                    // 因为时间是相同的，所以这个使能事件也同时会被处理。  增加一个token，所以需要重新判断使能
                    OLGraph subGraph = mainLoopFed.getSubGraph();
                    try {
                        //TODO token数量增加了，使能的变迁数量肯定变了，需要添加
                        subGraph.setTransitionEnabledStatusAfterReceivedToken(vexNode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ArrayList<VexNode> fired = subGraph.getWillFiredTransitions();
                    if(fired == null){
                        //如果为null，说明这时候没有增加新的使能变迁，就是有变迁在使能，跳回一开始，继续申请之前的时间
                    }else {
                        for(int i=0;i<fired.size();i++){
                            //将要发生的变迁转换为内部事件,并添加进入内部事件队列中
                            mainLoopFed.get_internalQueue().enqueue(new FireTransitionEvent(mainLoopFed,fired.get(i),subGraph,atomicInteger));
                        }
                    }
                }
                //put event on internal queue TODO 接收到消息后，这个时间应该设为多少呢？？？每个子网一个时钟吗？
                //TODO 如果这个时间比要请求的时间小，那么就将其设置为这个时间，或者直接将其设置为这个时间就好
                mainLoopFed.set_logicalTime(_time);//多余

            }else {
                try {
                    throw new Exception("时延不可能小于当前时延吧，因为RTI就已经排除了这种情况了！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
} //end FinishTokenReceivedEvent