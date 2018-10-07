package pipe.hla.SimDSPNModule.basemodel;

/**
 *metis的文件格式
 *int[] xadj = {0, 3, 6, 10, 14, 17, 20, 22};
 int[] adjncy = { 4, 2, 1,0, 2, 3, 4, 3, 0, 1, 1, 2, 5, 6, 0, 2, 5, 4, 3, 6, 5, 3 };
 int nvtxs = 7;
 int ncon = 1;
 int nparts = 3;*/
public class MetisFormat {
    public MetisFormat(int[] xadj, int[] adjncy, int nvtxs, int ncon, int[] pars) {
        this.xadj = xadj;
        this.adjncy = adjncy;
        this.nvtxs = nvtxs;
        this.ncon = ncon;
        //this.nparts = nparts;
        this.pars = pars;
    }

    private int[] xadj;
    private int[] adjncy;
    private int nvtxs;
    private int ncon;
    //private int nparts;//todo 这个是外界的输入函数
    private int[] pars;

    public int[] getXadj() {
        return xadj;
    }

    public void setXadj(int[] xadj) {
        this.xadj = xadj;
    }

    public int[] getAdjncy() {
        return adjncy;
    }

    public void setAdjncy(int[] adjncy) {
        this.adjncy = adjncy;
    }

    public int getNvtxs() {
        return nvtxs;
    }

    public void setNvtxs(int nvtxs) {
        this.nvtxs = nvtxs;
    }

    public int getNcon() {
        return ncon;
    }

    public void setNcon(int ncon) {
        this.ncon = ncon;
    }


    public int[] getPars() {
        return pars;
    }

    public void setPars(int[] pars) {
        this.pars = pars;
    }
}
