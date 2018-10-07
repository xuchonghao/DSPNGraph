package pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel;


import hla.rti1516.AttributeHandle;

import java.io.Serializable;

public class PetriNetNode implements Serializable{

    protected int id;
    protected String name;//库所或者变迁的名字
    private int partitionNumber;


    /*HLA对象使用,对象类属性句柄*/
    protected static AttributeHandle idHandle;
    protected static AttributeHandle nameHandle;
    protected static AttributeHandle lastNodeNameHandle;
    protected static AttributeHandle nextNodeNameHandle;
    protected String lastNodeName;
    protected String nextNodeName;

    public PetriNetNode() {
    }
    public PetriNetNode(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getPartitionNumber() {
        return partitionNumber;
    }

    public void setPartitionNumber(int partitionNumber) {
        this.partitionNumber = partitionNumber;
    }
}
