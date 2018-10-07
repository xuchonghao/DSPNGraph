package pipe.hla.SimDSPNModule.basemodel;

import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.ArcNode;

import java.io.Serializable;

public class EdgeNode implements Serializable{
    private int sourceIndex;//弧尾顶点位置     TODO 直接改为弧尾节点的id--String
    private int targetIndex;//弧头顶点位置

    private ArcNode currentArc;//当前弧的信息  //TODO 对于AU图来说，当isAUEdge为true的时候，它保存的是AU的名字


    //private VexNode sourceVexNode;//该弧的源节点     感觉不需要，只需要序号就行了
    //private VexNode targetVexNode;//该弧的目标节点

    private EdgeNode sameSourceNext;//弧尾相同的下一弧的位置（输出弧  O--->）
    private EdgeNode sameTargetNext;//弧头相同的下一弧的位置（--->O   输入弧）

    private boolean isOrdinaryArc;//判断是不是普通弧


    public boolean isOrdinaryArc() {

        return isOrdinaryArc;
    }

    public void setOrdinaryArc(boolean ordinaryArc) {
        isOrdinaryArc = ordinaryArc;
    }

    public ArcNode getCurrentArc() {
        return currentArc;
    }

    public void setCurrentArc(ArcNode currentArc) {
        this.currentArc = currentArc;
    }
    public int getSourceIndex() {
        return sourceIndex;
    }

    public void setSourceIndex(int sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public void setTargetIndex(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    public EdgeNode getSameSourceNext() {
        return sameSourceNext;
    }

    public void setSameSourceNext(EdgeNode sameSourceNext) {
        this.sameSourceNext = sameSourceNext;
    }

    public EdgeNode getSameTargetNext() {
        return sameTargetNext;
    }

    public void setSameTargetNext(EdgeNode sameTargetNext) {
        this.sameTargetNext = sameTargetNext;
    }


}
