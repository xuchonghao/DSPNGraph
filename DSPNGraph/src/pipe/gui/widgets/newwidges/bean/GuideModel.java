package pipe.gui.widgets.newwidges.bean;

import pipe.gui.widgets.newwidges.bean.SPM;
import pipe.gui.widgets.newwidges.bean.SW;
import pipe.gui.widgets.newwidges.bean.VLInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by hanson on 2017/8/22.
 * 会用于存储一些其他类中的全局变量，以及整体的各模块信息
 */
public class GuideModel {
    public GuideModel() {

        vlList = new LinkedList<VLInfo>();
        spmList = new LinkedList<SPM>();
        swList = new ArrayList<SW>();
        swList.add(new SW("SW1"));
        swList.add(new SW("SW2"));
        swList.add(new SW("SW3"));

        duList = new LinkedList<DU>();
        INDEXOFSW = 0;
        PREDUVLNUM = 0;

        SW2Sw3 = true;
        FLAG = true;
        Paralable = false;
    }
    private Integer typeOfBus = 0;//类型为AFDX，为了代码重用和以后的扩展
    private Integer numOfSPM = 1;//SPM终端的数量--假设有一个先
    private Integer numOfDU = 1;//DU终端的数量--假设有一个先
    private Integer numOfSW = 1;//目前使用SW的数量--假设有一个先
    private  int duParNum = 1;

    public static boolean isParalable() {
        return Paralable;
    }

    public static void setParalable(boolean paralable) {
        Paralable = paralable;
    }

    public static int INDEXOFSW = 0;
    public static int PREDUVLNUM = 0;


    private Queue<DU> duList;

    /**指发送端的虚链路信息*/
    private Queue<VLInfo> vlList;
    private Queue<SPM> spmList;
    private ArrayList<SW> swList;

    public final static int THE_AFDX = 101;
    public final static int EVENT_MESSAGE = 301;
    public final static int PERIOD_MESSAGE = 302;
    public static boolean FLAG = true;
    public static boolean SW2Sw3 = true;
    public static boolean Paralable = false;
    public static boolean AfterParalable = false;
    public static boolean AfterAfterParalable = false;
    public static boolean isAfterParalable() {
        return AfterParalable;
    }

    public static void setAfterParalable(boolean afterParalable) {
        AfterParalable = afterParalable;
    }

    public static int swCount = 1;


    public ArrayList<SW> getSwList() {
        return swList;
    }

    public void setSwList(ArrayList<SW> swList) {
        this.swList = swList;
    }

    public int getDuParNum() {
        return duParNum;
    }

    public void setDuParNum(int duParNum) {
        this.duParNum = duParNum;
    }

    public Integer getNumOfSW() {
        return numOfSW;
    }

    public void setNumOfSW(Integer numOfSW) {
        this.numOfSW = numOfSW;
        swCount = numOfSW;
    }

    public Integer getNumOfDU() {
        return numOfDU;
    }

    public void setNumOfDU(Integer numOfDU) {
        this.numOfDU = numOfDU;
    }

    public Queue<DU> getDuList() {
        return duList;
    }

    public void setDuList(Queue<DU> duList) {
        this.duList = duList;
    }

    public Queue<VLInfo> getVlList() {
        return vlList;
    }

    public void setVlList(Queue<VLInfo> vlList) {
        this.vlList = vlList;
    }

    public Queue<SPM> getSpmList() {
        return spmList;
    }

    public void setSpmList(Queue<SPM> spmList) {
        this.spmList = spmList;
    }

    public Integer getNumOfSPM() {
        return numOfSPM;
    }

    public void setNumOfSPM(Integer numOfSPM) {
        this.numOfSPM = numOfSPM;
    }

    public Integer getTypeOfBus() {
        return typeOfBus;
    }

    public void setTypeOfBus(Integer typeOfBus) {
        this.typeOfBus = typeOfBus;
    }
}
