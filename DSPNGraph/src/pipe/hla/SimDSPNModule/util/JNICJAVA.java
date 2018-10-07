package pipe.hla.SimDSPNModule.util;

public class JNICJAVA {
    static {
        System.loadLibrary("METISCJAVA");
    }
    public native int DLL_ADD(int a,int b);//加法
    //returnValue is communitionNumber
    public native int METIS_PartGraphKway(int nvtxs, int ncon, int[] xadj, int[] adjncy,int nparts,int[] pars);
}
