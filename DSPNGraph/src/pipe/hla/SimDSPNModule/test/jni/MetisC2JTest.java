package pipe.hla.SimDSPNModule.test.jni;

public class MetisC2JTest {
    static {
        System.loadLibrary("METISCJAVA");
    }
    public native int DLL_ADD(int a,int b);//加法
    //returnValue is objval  communitionNumber
    public native int[] METIS_PartGraphKway(int nvtxs, int ncon, int[] xadj, int[] adjncy,int nparts);
    public native int Function();
    public native int Function2(int nvtxs);
    public native int Function3(int nvtxs,int ncon);
    public native int Function4(int nvtxs,int ncon,int nparts);
    public native int Function5(int nvtxs,int ncon,int nparts,int[] xadj);
    public native int Function6(int nvtxs,int ncon,int nparts,int[] xadj,int[] adjncy);
}
