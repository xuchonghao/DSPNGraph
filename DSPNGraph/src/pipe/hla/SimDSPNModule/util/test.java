package pipe.hla.SimDSPNModule.util;

public class test {
    public static void main(String[] args) {

        JNICJAVA test = new JNICJAVA();

        int[] xadj = {0, 3, 6, 10, 14, 17, 20, 22};
        int[] adjncy = { 4, 2, 1,0, 2, 3, 4, 3, 0, 1, 1, 2, 5, 6, 0, 2, 5, 4, 3, 6, 5, 3 };
        int nvtxs = 7;
        int ncon = 1;
        int nparts = 3;

        int[] pars = new int[nvtxs];
        int comm = test.METIS_PartGraphKway(nvtxs,ncon,xadj,adjncy,nparts,pars);
        System.out.println("comm:"+comm);
        for(int i=0;i<pars.length;i++){
            System.out.println(pars[i]);
        }
    }
}
