package pipe.gui.widgets.newwidges.bean;


import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanson on 2017/8/29.
 */
public class SW {
     public SW(String id) {
        inPortList = new ArrayList<>();
        outPortList = new ArrayList<>();
        this.id = id;


        AllThroughVLNum = 0;
        inSPMCount = 0;
        inSWCount = 0;
        outDUCount = 0;
        outSWCount = 0;
    }
    public SW() {
        inPortList = new ArrayList<>();
        outPortList = new ArrayList<>();
        AllThroughVLNum = 0;
        inSPMCount = 0;
        inSWCount = 0;
        outDUCount = 0;
        outSWCount = 0;
       // in = new ArrayList<Element>();
        //inStr = new ArrayList<String>();
        //out = new ArrayList<String>();
        //outStr = new ArrayList<String>();
    }

    private String id;
     /**通过当前交换机的VL数量*/
    private int AllThroughVLNum;
    //TODO 打算添加SW最后一个Element 的list
    private ArrayList<Element> lastElementList;

    public ArrayList<Element> getLastElementList() {
        return lastElementList;
    }

    public void setLastElementList(ArrayList<Element> lastElementList) {
        this.lastElementList = lastElementList;
    }

    /**输入和输出端口的中各端系统的数量*/
    private int inSPMCount;
    private int inSWCount;
    private int outDUCount;
    private int outSWCount;

    public int getInSPMCount() {
        return inSPMCount;
    }

    public void setInSPMCount(int inSPMCount) {
        this.inSPMCount = inSPMCount;
    }

    public int getInSWCount() {
        return inSWCount;
    }

    public void setInSWCount(int inSWCount) {
        this.inSWCount = inSWCount;
    }

    public int getOutDUCount() {
        return outDUCount;
    }

    public void setOutDUCount(int outDUCount) {
        this.outDUCount = outDUCount;
    }

    public int getOutSWCount() {
        return outSWCount;
    }

    public void setOutSWCount(int outSWCount) {
        this.outSWCount = outSWCount;
    }

    /**SW的输入端口和输出端口*/
    private ArrayList<Port> inPortList;
    private ArrayList<Port> outPortList;
    /**考虑吧Element的in out和 String的 inStr outStr给整合到Port中去*/
   // private ArrayList<Element> in;
   // private ArrayList<String> inStr;

   // private List<String> out;
    //private ArrayList<String> outStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAllThroughVLNum() {
        return AllThroughVLNum;
    }

    public void setAllThroughVLNum(int allThroughVLNum) {
        AllThroughVLNum = allThroughVLNum;
    }

    public ArrayList<Port> getInPortList() {
        return inPortList;
    }

    public void setInPortList(ArrayList<Port> inPortList) {
        this.inPortList = inPortList;
    }

    public ArrayList<Port> getOutPortList() {
        return outPortList;
    }

    public void setOutPortList(ArrayList<Port> outPortList) {
        this.outPortList = outPortList;
    }

    /*
    public ArrayList<Element> getIn() {
        return in;
    }

    public void setIn(ArrayList<Element> in) {
        this.in = in;
    }

    public ArrayList<String> getInStr() {
        return inStr;
    }

    public void setInStr(ArrayList<String> inStr) {
        this.inStr = inStr;
    }
     public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out = out;
    }
     public ArrayList<String> getOutStr() {
        return outStr;
    }

    public void setOutStr(ArrayList<String> outStr) {
        this.outStr = outStr;
    }
    */
}
