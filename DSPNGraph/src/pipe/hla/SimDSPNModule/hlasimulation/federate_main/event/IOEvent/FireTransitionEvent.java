package pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.IOEvent;


import hla.rti1516.IllegalTimeArithmetic;
import hla.rti1516.LogicalTime;
import hla.rti1516.LogicalTimeInterval;
import hla.rti1516.RTIexception;
import pipe.hla.SimDSPNModule.basemodel.EdgeNode;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.basemodel.VexNode;

import pipe.hla.SimDSPNModule.hlasimulation.federate_main.MainLoopFed;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.abstractEvent.InternalEvent;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.ArcNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.TransitionNode;
import se.pitch.prti1516.LogicalTimeIntervalDouble;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

/**变迁发生的时间，应该属于发送交互吧*/
public final class FireTransitionEvent extends InternalEvent {
    VexNode transition;
    OLGraph subGraph;
    LogicalTimeInterval fireTimeInterval;
    MainLoopFed mainLoopFed;
    private AtomicInteger atomicInteger;

    public FireTransitionEvent(MainLoopFed mainLoopFed,VexNode vexNode, OLGraph subGraph, AtomicInteger atomicInteger) {
        this.transition = vexNode;
        this.subGraph = subGraph;
        this.mainLoopFed = mainLoopFed;
        this.atomicInteger = atomicInteger;

        double time = ((TransitionNode)vexNode.getPlaceOrTransition()).getDelay();
        try {
            //TODO 时间这一块还有待修改,在上一个变迁发生的时候，持续使能的变迁会减去发生的时间，这里会加上全局时间。
            fireTimeInterval = new LogicalTimeIntervalDouble(time);
            LogicalTime t = mainLoopFed.get_logicalTime();
            //System.out.println("^^^^^^"+t.toString());
            //表示事件的发生时间
            _time = t.add(fireTimeInterval);

            System.out.println("FireTransitionEvent中，该事件的发生间隔fireTimeInterval："+fireTimeInterval.toString()+",该事件的发生时间:"+_time.toString());
        } catch (IllegalTimeArithmetic illegalTimeArithmetic) {
            illegalTimeArithmetic.printStackTrace();
        }

          /*此时要对变迁前面的库所做减token的操作*/
        //1、拿到这个变迁的前面的库所，以及后面的库所，减少里面token的数量
        HashSet<EdgeNode> inputArcNodes = subGraph.getAllInputArcNodesWithHash(transition);
        for (EdgeNode edgeNode : inputArcNodes){
            ArcNode arcNode = edgeNode.getCurrentArc();
            int inputWeight = arcNode.getWeight();
            PlaceNode sourceNode = (PlaceNode) subGraph.getXlist().get(edgeNode.getSourceIndex()).getPlaceOrTransition();
            //System.out.println("发生变迁的sourceNode："+sourceNode.getName());
            int numOfToken = sourceNode.getNumOfToken();
            if(numOfToken < 0)
                try {
                    System.out.println("numOfToken:"+numOfToken);
                    throw new Exception("numOfToken小于0！不应该的情况！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            boolean isOrdinary = edgeNode.isOrdinaryArc();
            //还要对弧进行判断
            if(isOrdinary){//一般弧
                //System.out.println("sourceNode的变化前的数量："+sourceNode.getNumOfToken());
                int newNumOfToken = numOfToken - inputWeight;
                sourceNode.setNumOfToken(newNumOfToken);
                //System.out.println("sourceNode的变化后的数量："+sourceNode.getNumOfToken());
            }else{//抑制弧
                // TODO 怎么变化呢？？？忘了
            }
        }

    }

    public void dispatch() throws RTIexception
    {
        //变迁发生，如果库所在这个子图中，正常发生；如果不在，
        //System.out.println(transition.getPlaceOrTransition().getName());
        HashSet<EdgeNode> setOut = subGraph.getAllOutputArcNodesWithHash(transition);
        //HashSet<EdgeNode> setIn = olGraph.getAllInputArcNodesWithHash(transition);
        //ArrayList<VexNode> list = olGraph.getXlist();
        for(EdgeNode  edgeNode : setOut){
            int index = edgeNode.getTargetIndex();
            String targetName = edgeNode.getCurrentArc().get_target();
            int weight = edgeNode.getCurrentArc().getWeight();
            System.out.println("~~~~~~~~~~~~~~~~~~~~fireTransition或者sendMessage中发生的变迁是："+transition.getPlaceOrTransition().getName());
            if(index != -1){//说明该变迁后面 的库所在同一个划分中
                //olGraph.fireTransitionOfDistributedSimulation(transition);
                ArcNode arcNode = edgeNode.getCurrentArc();
                int outputWeight = arcNode.getWeight();
                PlaceNode targetNode = (PlaceNode) subGraph.getXlist().get(edgeNode.getTargetIndex()).getPlaceOrTransition();
                //System.out.println("发生变迁的targetNode："+targetNode.getName());
                int numOfToken = targetNode.getNumOfToken();
                //int capacity = targetNode.getCapacity();
                if(numOfToken < 0)
                    try {
                        System.out.println("numOfToken:"+numOfToken);
                        throw new Exception("numOfToken小于0！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                int newNumOfToken = numOfToken + outputWeight;
                //System.out.println("targetNode的变化前的数量："+targetNode.getNumOfToken());
                targetNode.setNumOfToken(newNumOfToken);

                //subGraph.fireTransitionOfDistributedSimulation(transition);

            }else{//说明该变迁后面 的库所在另一个划分中

                mainLoopFed.sendMessage(transition,targetName,weight);

                // 更新属性值:减少库所中的token数，减少变迁时间和队列中的事件
//                for(EdgeNode node : setIn){
//                    int idx =node.getSourceIndex();
//                    VexNode source = list.get(idx);
//                    PlaceNode placeNode = (PlaceNode)source.getPlaceOrTransition();
//                    placeNode.setNumOfToken(placeNode.getNumOfToken() - node.getCurrentArc().getWeight());
//                }
            }
            atomicInteger.incrementAndGet();

        }
        //TODO 好像已经推进时间了吧，这里没必要再搞了吧
        //MainLoopFed.set_logicalTime(MainLoopFed.get_logicalTime().add(new LogicalTimeIntervalDouble(((TransitionNode)transition.getPlaceOrTransition()).getDelay())));
        //把发生变迁放入队列中，构成闭口的循环
        subGraph.setEnabledTransitionsQueueAgain(transition,0);
        //System.out.println("simClock"+simClock);
        //重新在内部事件队列中加入事件
        ArrayList<VexNode> fired = subGraph.getWillFiredTransitions();
        if(fired != null){
            for(int i=0;i<fired.size();i++){
                //将要发生的变迁转换为内部事件,并添加进入内部事件队列中
                mainLoopFed.get_internalQueue().enqueue(new FireTransitionEvent(mainLoopFed,fired.get(i),subGraph,atomicInteger));
            }
        }else{//TODO 当fired为null的时候要进行阻塞！！！？

        }

    }

    public VexNode getTransition() {
        return transition;
    }

    public LogicalTimeInterval getFireTimeInterval() {
        return fireTimeInterval;
    }
} //end FinishTokenSendEvent