package pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel;

import java.io.Serializable;

public class PlaceNode extends PetriNetNode implements Serializable {

    /*对象实例自身状态*/
    protected int numOfToken;
    protected int capacity;
    //TODO 可能有多个呀，得修改
    protected int inputArcWeight;
    protected int outputArcWeight;
    protected boolean isConflictPlace;


    public PlaceNode() {

        //System.out.println("PlaceNode Ok!!!");
    }

    public PlaceNode(String name) {
        super(name);
        /*默认值的构造*/
        this.numOfToken = 0;
        capacity = 10;
        inputArcWeight = 1;
        outputArcWeight = 1;
        //PlaceNodeArray.add(this);
        //System.out.println("PlaceNode Ok!!!");
    }

    public boolean isConflictPlace() {
        return isConflictPlace;
    }

    public void setConflictPlace(boolean conflictPlace) {
        isConflictPlace = conflictPlace;
    }

    public int getNumOfToken() {
        return numOfToken;
    }

    public void setNumOfToken(int numOfToken) {
        this.numOfToken = numOfToken;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getInputArcWeight() {
        return inputArcWeight;
    }

    public void setInputArcWeight(int inputArcWeight) {
        this.inputArcWeight = inputArcWeight;
    }

    public int getOutputArcWeight() {
        return outputArcWeight;
    }

    public void setOutputArcWeight(int outputArcWeight) {
        this.outputArcWeight = outputArcWeight;
    }

}
