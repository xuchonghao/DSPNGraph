package pipe.gui.widgets.newwidges.bean;

import java.util.ArrayList;

/**
 * Created by hanson on 2017/8/22.
 */
public class Paratition {
        private String parId;
        private double lxPeriod;
        private int VLCount;//链路数量
        private ArrayList<VLInfo> parititionVLInfo;

        public double getLxPeriod() {
        return lxPeriod;
    }

        public void setLxPeriod(double lxPeriod) {
        this.lxPeriod = lxPeriod;
    }

        public int getVLCount() {
        return VLCount;
    }

        public void setVLCount(int VLCount) {
        this.VLCount = VLCount;
    }

        public Paratition() {
            parititionVLInfo = new ArrayList<VLInfo>();
        }
        //public Paratition(int VLCount,String parId) {
        public Paratition(String parId) {
            this.parId = parId;
            parititionVLInfo = new ArrayList<VLInfo>();
        }

        public ArrayList<VLInfo> getParititionVLInfo() {
        return parititionVLInfo;
    }

        public void setParititionVLInfo(ArrayList<VLInfo> parititionVLInfo) {
        this.parititionVLInfo = parititionVLInfo;
    }

        public String getParId() {

            return parId;
        }

        public void setParId(String parId) {
        this.parId = parId;
    }

}
