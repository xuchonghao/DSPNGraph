package pipe.hla.SimDSPNModule.ordinarysimulation;

import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.basemodel.VexNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.Marking;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceToken;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.TransitionNode;
import pipe.gui.widgets.oldwidges.ResultsHTMLPane;

import java.text.DecimalFormat;
import java.util.*;


public class SimulationLogical {
    double simClock;
    //int[] marking ;
    Marking markings;
    HashMap<ArrayList<PlaceToken>,Double> hashMap = new HashMap<>();
    double averageTokens[];
    int totalTokens[] ;
    double avgResult[];
    double errorResult[] ;
    double overallAverages[][] ;

    //初始化结果数组以及初始marking TODO 还得重新定义
    private void initArray(OLGraph olGraph, int cycles) {
        // 1、 获得初始状态
        //marking = olGraph.getInitialMarkingVector();


        int length = olGraph.getPlacenum();
        averageTokens = new double[length];
        totalTokens = new int[length];
        avgResult = new double[length];
        errorResult = new double[length];
        overallAverages = new double[cycles][length];
        // Initialise array，每一轮都要更新
        initArrayLittle(length,olGraph);

        //Initialise matrices TODO 如果太大的话，这里也浪费不少资源，应该可以省略掉吧==>应该可以
        for(int i = 0; i < cycles; i++){
            for(int j = 0; j < length; j++)
                overallAverages[i][j] = 0;
        }
    }



    /*** 每一轮都要更新 */
    private void initArrayLittle(int length,OLGraph olGraph) {
        for(int i = 0; i < length; i++){
            averageTokens[i] = 0;
            totalTokens[i] = 0;
            avgResult[i] = 0;
            errorResult[i] = 0;
        }
        //初始化队列，把上一循环中的队列元素清0
        olGraph.initQueue();
    }

    public static void main(String[] args) throws Exception {

        int[] arr = {1,2,3,4,5,6,7,8};
//        try{
//            if(arr.length == 8){
//                throw new Exception("shishishi");
//            }
//            System.out.println("dfsfsdfs");
//        }catch (Exception e){
//            System.out.println(e.toString());
//        }

        System.out.println(Arrays.toString(arr));
    }
    public int kernelSimulation(OLGraph olGraph, int firings,int transCount){
        for(int j = 0; j < firings; j++){
            System.out.println("Firing " + j + " now");

            //3、获得要发生的变迁（TODO 之后要变成多个=》数组） a random transition
            VexNode fired = olGraph.getRandomTransition();

            System.out.println("获得要发生的变迁:"+fired);
            if(fired == null){
                System.out.println("已经不存在使能的变迁了！");
                break;
            }else{
                //data.createCurrentMarkingVector();
                System.out.println("***fire的变迁是："+fired.getPlaceOrTransition().getName()+","+"发生的时间： "+simClock);

                //4、发生变迁，并返回此时的全局时钟，内部包括发生变迁，改变状态
                try {
                    olGraph.fireTransition(fired); //NOU-PERE
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //TODO 每个变迁与一个时延相关联，那这里究竟是使能延续的时间还是fire延续的时间==》这个应该和标识捆绑在一起
                double delay = ((TransitionNode)fired.getPlaceOrTransition()).getDelay();
                System.out.println("***该变迁的时延："+delay);
                //4、变迁发生后的后续处理
                simClock = olGraph.setEnabledTransitionsQueueAgain(fired,simClock);
                //输出fire变迁的名字
                System.out.println("***发生后的时间： "+simClock);

                //4.5、Get the new marking from the _dataLayer object
                markings = olGraph.getCurrentMarkings();
                markings.setExitedTime(delay);
                if(hashMap.containsKey(markings.getMarking())){
                    hashMap.put(markings.getMarking(),markings.getExitedTime() + hashMap.get(markings.getMarking()));
                }else
                    hashMap.put(markings.getMarking(),markings.getExitedTime());
                //System.out.println("marking: " + Collections.toString(markings.getMarking()));

                //4.6、Add to the totalTokens array TODO  这里统计的时候可以想办法去掉一些偏差
                //if(j > firings * 0.1){
                addTotal(markings, totalTokens);
                //Increment the transition count
                transCount++;
                //}
            }
        }//判断是否达到firing的次数，如果达到则结束！
        return transCount;
    }

    /**
     * （1）olgraph获得初始的状态（给初始变迁做判断，确认谁是使能的）
     * （2）获得要发生的变迁
     * （3）发生变迁，改变状态
     * */
    public String simulate(OLGraph olGraph, int cycles, int firings){

        // 1、 初始化结果数组，olgraph获得初始的状态
        initArray(olGraph,cycles);

        //仿真流程开始
        for(int i = 0; i < cycles; i++){
            System.out.println("Cycle  " + i);

            simClock = 0.0;//每一轮都需要初始化全局时钟
            //Need to initialise the transition count again
            int transCount = 0;
            //1.5、每一轮循环都要 Get initial marking,是为了计算最后的平均队列长度。 TODO 感觉这里会有问题，上一轮的变化之后，数据保存到哪里了呢？？？会不会影响到现在的
            markings = olGraph.getInitialMarkings();
            //if(ApplicationSettings.getApplicationView() != null) data.restorePreviousMarking();
            //System.out.println(Arrays.toString(markings.getMarking()));

            //1.6、Initialise matrices for each new cycle    每一cycles对数组进行初始化
            initArrayLittle(markings.getMarking().size(),olGraph);

            //1.7、Add initial marking to the total
            addTotal(markings, totalTokens);//初始的，每一轮仅这一次

            //2、第一次填充使能变迁的队列，将要使能的变迁设置为使能（给初始变迁做判断，确认谁是使能的） 正式开始
            try {
                olGraph.setEnabledTransitions();
                //然后把合适的变迁加入到队列中
            } catch (Exception e) {
                e.printStackTrace();
            }

            //3和4、核心仿真逻辑 kenelSimulate()
            transCount = kernelSimulation(olGraph,firings,transCount);

            System.out.println("transCount : "+transCount);

            //5、Evaluate averages
            for(int j = 0; j < markings.getMarking().size(); j++){
                //Divide by transCount + 1 as total number of markings
                //considered includes the original marking which is outside
                //the loop which counts the number of randomly fired transitions.
                averageTokens[j] = (totalTokens[j] / (transCount + 1.0));

                //add appropriate to appropriate row of overall averages for each cycle
                overallAverages[i][j] = averageTokens[j];
            }
        }//end cycles

        double noneTime = 0;
        double AllTime = 0;
        for(HashMap.Entry<ArrayList<PlaceToken>,Double> entry : hashMap.entrySet()){
            ArrayList<PlaceToken> placeTokens = entry.getKey();
            Double time = entry.getValue();
            AllTime += time;
            int num1 = -1,num2 = -1,num3 = -1,num4 = -1,num5 = -1;
            for(int i=0;i<placeTokens.size();i++){
                String name = placeTokens.get(i).getPlaceName();
                int num = placeTokens.get(i).getTokenNum();
                if("P5".equals(name)){
                    num1 = num;
                    System.out.println("num1:"+num1);
                }
                if("P6".equals(name)){
                    num2 = num;
                    System.out.println("num2:"+num2);
                }
                if("P7".equals(name)){
                    num3 = num;
                    System.out.println("num3:"+num3);
                }
                if("P8".equals(name)){
                    num4 = num;
                    System.out.println("num4:"+num4);
                }
                if("P9".equals(name)){
                    num5 = num;
                    System.out.println("num5:"+num5);
                }
            }
            if(num1 ==0 && num2 ==0 && num3 ==0 && num4 ==0 && num5 ==0){
                noneTime += time;
            }
        }

        System.out.println("noneTime:"+noneTime);
        System.out.println("AllTime:"+AllTime);
        System.out.println("none:"+noneTime/AllTime);

        //Add up averages for each cycle and divide by number of cycles
        //Perform evaluation on the overallAverages matrix.
        //for each column
        for(int i = 0; i < markings.getMarking().size(); i++){
            //for each row
            for(int j = 0; j < cycles; j++){
                avgResult[i] = avgResult[i] + overallAverages[j][i];
            }
            avgResult[i] = (avgResult[i] / cycles);
        }


        //Generate the 95% confidence interval for the table of results

        //Find standard deviation and mulitply by 1.95996 assuming approx
        //to gaussian distribution

        //For each column in result array
        for(int i = 0; i < markings.getMarking().size(); i++){
            //Find variance
            for(int j = 0; j < cycles; j++){
                //Sum of squares
                errorResult[i] = errorResult[i] +
                        ((overallAverages[j][i] - avgResult[i]) *
                                (overallAverages[j][i] - avgResult[i]));
            }

            //Divide by number of cycles
            //Find standard deviation by taking square root
            //Multiply by 1.95996 to give 95% confidence interval
            errorResult[i] = 1.95996 * Math.sqrt(errorResult[i] / cycles);
        }

        ArrayList results = new ArrayList();
        DecimalFormat f = new DecimalFormat();
        f.setMaximumFractionDigits(5);

        if(averageTokens != null && errorResult != null && averageTokens.length > 0 && errorResult.length > 0){
            // Write table of results
            results.add("Place");
            results.add("AverageQueueLength");
            results.add("95% confidence interval (+/-)");
            for(int i = 0; i < averageTokens.length; i++){
                results.add(olGraph.getXlist().get(i).getPlaceOrTransition().getName());
                results.add(f.format(averageTokens[i]));
                results.add(f.format(errorResult[i]));
            }
        }
        //if(ApplicationSettings.getApplicationView() != null) data.restorePreviousMarking();
        return ResultsHTMLPane.makeTable(results.toArray(), 3, false, true, true, true);
    }
    private void addTotal(Marking markings, int dest[])
    {
        if(markings.getMarking().size() == dest.length)
        {
            for(int i = 0; i < dest.length; i++)
            {
                dest[i] += markings.getMarking().get(i).getTokenNum();
            }
        }
    }
}
