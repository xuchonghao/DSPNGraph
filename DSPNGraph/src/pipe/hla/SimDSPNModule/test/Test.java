package pipe.hla.SimDSPNModule.test;

import java.util.Arrays;
import java.util.Comparator;

public class Test {
    static int fun(int a){
        a++;
        System.out.println("11:"+a);
        return a;
    }
    public static void main(String[] args) {

        //二维数组
        int[][] matrix = new int[][] {
                {8,7},{9,5},{6,4}
        };
        int[][] pairs = new int[][] {
                {8,7},{9,5},{6,4}
        };

        Arrays.sort(pairs, (a, b) -> (a[0] - b[0]));
        //排序
        Arrays.sort(matrix,new Comparator<int[]>() {
            @Override
            public int compare(int[] x, int[] y) {
                if(x[0] < y[0]){
                    return 1;
                } else if(x[0] > y[0]){
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        //打印
        for(int[] integers : matrix){
            System.out.println(Arrays.toString(integers));
        }
    }
}
