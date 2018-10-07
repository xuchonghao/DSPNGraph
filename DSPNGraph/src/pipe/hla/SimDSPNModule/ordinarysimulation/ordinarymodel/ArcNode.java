package pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel;

import java.io.Serializable;

public class ArcNode implements Serializable{
    private int AUWeightOfEdge;//该弧的权重
    private boolean isAUEdge = true;//当前的弧是不是AU图中的一部分,TODO 先默认是好了
    private String belongPartition;//当前弧所属的分区
    private boolean isBelongTwoPars;//当前弧是不是属于两个分区
    private String _source = null;
    private String _target = null;

    private String AUSource = null;
    private String AUTarget = null;
    int weight;
    String type;//

    public String get_source() {
        return _source;
    }

    public void set_source(String _source) {
        this._source = _source;
    }

    public String get_target() {
        return _target;
    }

    public void set_target(String _target) {
        this._target = _target;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAUWeightOfEdge() {
        return AUWeightOfEdge;
    }

    public void setAUWeightOfEdge(int AUWeightOfEdge) {
        this.AUWeightOfEdge = AUWeightOfEdge;
    }

    public boolean isAUEdge() {
        return isAUEdge;
    }

    public void setAUEdge(boolean AUEdge) {
        isAUEdge = AUEdge;
    }

    public String getBelongPartition() {
        return belongPartition;
    }

    public void setBelongPartition(String belongPartition) {
        this.belongPartition = belongPartition;
    }

    public boolean isBelongTwoPars() {
        return isBelongTwoPars;
    }

    public void setBelongTwoPars(boolean belongTwoPars) {
        isBelongTwoPars = belongTwoPars;
    }

    public String getAUSource() {
        return AUSource;
    }

    public void setAUSource(String AUSource) {
        this.AUSource = AUSource;
    }

    public String getAUTarget() {
        return AUTarget;
    }

    public void setAUTarget(String AUTarget) {
        this.AUTarget = AUTarget;
    }
}
