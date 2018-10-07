package pipe.hla.SimDSPNModule.test.jni;

public class test {
    public static void main(String[] args) {
        int sum = 0;
        MetisC2JTest test = new MetisC2JTest();
        sum = test.DLL_ADD(9,4);
        System.out.println(sum);

        int df = test.Function2(7);
        System.out.println(df);
        System.out.println("----------------------");
         df = test.Function3(7,1);
        System.out.println(df);
        System.out.println("----------------------");

         df = test.Function4(7,1,3);
        System.out.println(df);
        System.out.println("----------------------");


        int[] xadj = {0, 3, 6, 10, 14, 17, 20, 22};

        df = test.Function5(7,1,3,xadj);
        System.out.println(df);
        System.out.println("----------------------");

        int[] adjncy = { 4, 2, 1,0, 2, 3, 4, 3, 0, 1, 1, 2, 5, 6, 0, 2, 5, 4, 3, 6, 5, 3 };
        df = test.Function6(7,1,3,xadj,adjncy);
        System.out.println(df);
        System.out.println("----------------------");


        int nvtxs = 7;
        int ncon = 1;
       // long[] xadj = {0, 3, 6, 10, 14, 17, 20, 22};

        int nparts = 3;
//        int[] part = new int[nvtxs];
        int[] part = test.METIS_PartGraphKway(nvtxs,ncon,xadj,adjncy,nparts);
        for(int i=0;i<part.length;i++){
            System.out.println(part[i]);
        }
    }
}
