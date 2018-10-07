package pipe.hla.SimDSPNModule.test.jni;

public class Java2Cpp {
    static {
        System.loadLibrary("javaCallcpp");
    }
    public native int DLL_ADD(int a,int b);//加法
    public native int DDL_SUB(int a,int b);
    public native int DDL_MUL(int a,int b);
    public native int DDL_DIV(int a,int b);

    public static void main(String[] args) {
        int sum = 0,ans1 = 1,ans2 = 0,ans3 = 0;
        Java2Cpp test = new Java2Cpp();
        sum = test.DLL_ADD(2,4);
        //ans1 = test.DDL_SUB(7,2);
        //ans2 = test.DDL_MUL(4,5);
        //ans3 = test.DDL_DIV(9,3);
        System.out.println(sum+","+ans1+","+ans2+","+ans3);
    }
}
