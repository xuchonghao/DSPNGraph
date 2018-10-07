package pipe.hla.SimDSPNModule.basemodel;

import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PetriNetNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.TransitionNode;

import java.io.Serializable;

public class VexNode implements Comparable<VexNode>,Serializable{
    public VexNode(){}
    public VexNode(String AUMarking){
        this.AUMarking = AUMarking;
    }

    private int AUWeightOfVexNode;//该节点的权重
    private String AUMarking;//当前节点的AU标识,其实也就是当前AU节点的名称
    private String belongPartition;//当前节点所属的分区

    private int sequence;//该节点在图中对应的序号，添加节点的时候进行赋值

    /**这三个是Petri网的节点特有的属性*/
    private boolean isTransition;//当前节点是不是变迁
    private double fireTime;//当前变迁的发生时间是多少，为了分布式仿真中出现两个相同变迁使能的条件，对其进行区分
    private PetriNetNode placeOrTransition;//当前节点的信息


    /**以该顶点为弧尾的第一个  输出弧  节点*/
    private EdgeNode firstOut =null;

    /**以该顶点为弧头的第一个  输入弧  节点*/
    private EdgeNode firstIn = null;

    public EdgeNode getFirstIn() {
        return firstIn;
    }

    public void setFirstIn(EdgeNode firstIn) {
        this.firstIn = firstIn;
    }

    public EdgeNode getFirstOut() {
        return firstOut;
    }

    public void setFirstOut(EdgeNode firstOut) {
        this.firstOut = firstOut;
    }
    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isTransition() {
        return isTransition;
    }

    public void setTransition(boolean transition) {
        isTransition = transition;
    }

    public PetriNetNode getPlaceOrTransition() {
        return placeOrTransition;
    }

    public void setPlaceOrTransition(PetriNetNode placeOrTransition) {
        this.placeOrTransition = placeOrTransition;
    }


    /**获得当前节点的id*/

    public int getNodeId() {
        return placeOrTransition.getId();
    }

    public double getFireTime() {
        return fireTime;
    }

    public void setFireTime(double fireTime) {
        this.fireTime = fireTime;
    }

    public int getAUWeightOfVexNode() {
        return AUWeightOfVexNode;
    }

    public void setAUWeightOfVexNode(int AUWeightOfVexNode) {
        this.AUWeightOfVexNode = AUWeightOfVexNode;
    }

    public String getAUMarking() {
        return AUMarking;
    }

    public void setAUMarking(String AUMarking) {
        this.AUMarking = AUMarking;
    }

    public String getBelongPartition() {
        return belongPartition;
    }

    public void setBelongPartition(String belongPartition) {
        this.belongPartition = belongPartition;
    }

    @Override
    public int compareTo(VexNode o) {
        if(this.getPlaceOrTransition() instanceof TransitionNode && o.getPlaceOrTransition() instanceof TransitionNode){
            return ((TransitionNode)this.placeOrTransition).compareTo((TransitionNode)o.getPlaceOrTransition());
        }
        return 0;
    }
}

