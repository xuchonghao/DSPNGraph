package pipe.hla.SimDSPNModule.util;



import java.io.Serializable;
import java.util.PriorityQueue;



/**产生负指数函数的随机数*/
public class RandomGenerator implements Serializable{

    public static double generator(double lamda){

        double x, z;
        //这里当产生的随机数z大于lamda的时候，返回的x值为负的。
        z = Math.random();

        x = -(1 / lamda) * Math.log(z);
        return x;
    }


    public static double generate(double lamda){
        double p,temp = 0;
        if(lamda != 0){
            temp = 1 / lamda;
        }else {
            try {
                throw new Exception("除数为0！不能产生随机数");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       // while(true){//用于产生随机的密度，保证比参数lamda小
        p =  Math.random();
//            if(p < lamda)
//                break;
//        }
        //这里当产生的随机数z大于lamda的时候，返回的x值为负的。
        double randres = -temp * Math.log(p);
        return randres;
    }

    public static void main(String[] args) {
        //Random random = new Random(System.currentTimeMillis());
        int t = 1;
        while(t < 10000){
            //System.out.println(generate(8));
            double z = Math.random();
            if (z < 0)
                System.out.println(z);
            t++;
        }

        System.out.println();
        double lamda = 2;
        int z = 2;

        System.out.println("x = "+ -(1 / lamda) * Math.log(z));

        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.offer(434);
        queue.offer(43);
        queue.offer(14);
        queue.offer(4);
        queue.offer(439);
        int len = queue.size();
        for(int i=0;i<len;i++){
            System.out.println(queue.poll());
        }


    }
}
