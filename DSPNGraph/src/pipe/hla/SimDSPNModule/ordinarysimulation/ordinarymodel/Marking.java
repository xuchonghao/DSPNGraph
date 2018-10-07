package pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel;

import java.util.ArrayList;

public class Marking{
    private int placeLen;
    private ArrayList<PlaceToken> marking ;
    //当前标识维持的时间
    private double exitedTime;
    public Marking(int len){
        this.placeLen = len;
        marking = new ArrayList<PlaceToken>(len);
    }

    public ArrayList<PlaceToken> getMarking() {
        return marking;
    }

    public void setMarking(ArrayList<PlaceToken> marking) {
        this.marking = marking;
    }

    public double getExitedTime() {
        return exitedTime;
    }

    public void setExitedTime(double exitedTime) {
        this.exitedTime = exitedTime;
    }
}
