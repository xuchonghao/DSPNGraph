package pipe.gui.widgets.newwidges.bean;

import java.util.ArrayList;

/**这里使用DU来普遍代表接收端，根据惯性，不再使用RES*/
public class DU {
    private String id;
    private int numOfPar;

    private ArrayList<Paratition> parList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumOfPar() {
        return numOfPar;
    }

    public void setNumOfPar(int numOfPar) {
        this.numOfPar = numOfPar;
    }
     public ArrayList<Paratition> getParList() {
        return parList;
    }

    public void setParList(ArrayList<Paratition> parList) {
        this.parList = parList;
    }
}
