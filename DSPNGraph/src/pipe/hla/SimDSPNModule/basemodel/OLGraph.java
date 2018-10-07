package pipe.hla.SimDSPNModule.basemodel;



import hla.rti1516.LogicalTime;
import hla.rti1516.LogicalTimeInterval;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.*;

import java.io.*;
import java.util.*;

/**使用十字链表存储图的结构*/
public class OLGraph implements Serializable{


    String PNMLName ;
    //节点数组
    private ArrayList<VexNode> xlist ;
    private ArrayList<ArcNode> arclist;
    //存储冲突的库所节点
    private ArrayList<VexNode> conflictPlaceVexNodeList ;
    //存储冲突的变迁节点
    private ArrayList<VexNode> conflictTransitionVexNodeList ;

    //库所、变迁的数量，总顶点的数量、边的数量
    private int placenum,transitionnum,vexnum,edgenum;

    private int[] _initialMarkingVector = null;
    private int[] _currentMarkingVector = null;
    private Marking initialMarkings = null;
    private Marking currentMarkings = null;
    //发生变迁后，对下面的值进行修改
    boolean _initialMarkingVectorChanged = true;
    boolean _currentMarkingVectorChanged = true;

    //使能的瞬时变迁，每次都要进行更新，每次只发生一个
     ArrayList<VexNode> zeroQueue;
     Queue<VexNode>  priorityQueue;

    public Queue<VexNode> getPriorityQueue() {
        return priorityQueue;
    }

    public void setPriorityQueue(Queue<VexNode> priorityQueue) {
        this.priorityQueue = priorityQueue;
    }

    public OLGraph(){
        xlist = new ArrayList<>();
        arclist = new ArrayList<>();
        conflictPlaceVexNodeList = new ArrayList<>();
        conflictTransitionVexNodeList = new ArrayList<>();
        zeroQueue = new ArrayList<>();
        priorityQueue = new PriorityQueue<>();
    }

    public void initQueue(){
        zeroQueue = new ArrayList<>();
        priorityQueue = new PriorityQueue<>();
    }
    /**检测冲突嫌疑的（TODO 混惑）结构*/
    public void checkConflictStructure(){
        //获得库所后面的节点，如果个数为一，continue；若大于1，则为冲突结构；
        //把冲突的变迁和库所进行处理；也可能是混惑，在使用的时候，只要判断几个变迁的前置库所是不是同一个，不是同一个的话，就是混惑结构
        for(int i=0;i<placenum;i++){
            VexNode vexNode = xlist.get(i);
            PlaceNode placeNode = (PlaceNode) vexNode.getPlaceOrTransition();
            // System.out.println(placeNode.getName());
            HashSet<EdgeNode> outputArcNodes = getAllOutputArcNodesWithHash(vexNode);

            //与当前变迁相关系的所有变迁，包括当前变迁
            ArrayList<VexNode> conflictTransitionArray = new ArrayList<>();

            if(outputArcNodes.size() <= 1)//不是冲突
                placeNode.setConflictPlace(false);
            if(outputArcNodes.size() > 1){//是冲突
                placeNode.setConflictPlace(true);
                conflictPlaceVexNodeList.add(vexNode);//加入冲突的库所列表
                //先遍历一遍，得到所有的冲突节点
                for(EdgeNode edgeNode : outputArcNodes){
                    int index = edgeNode.getTargetIndex();
                    VexNode tNode = xlist.get(index);
                    conflictTransitionArray.add(tNode);
                }
                for(EdgeNode edgeNode : outputArcNodes){
                    int index = edgeNode.getTargetIndex();
                    VexNode tNode = xlist.get(index);
                    TransitionNode transitionNode = (TransitionNode)tNode.getPlaceOrTransition();
                    transitionNode.setConflict(true);//将变迁设置为冲突结构
                    transitionNode.setConflictTransition(conflictTransitionArray);//给变迁设置相关的冲突变迁

                }//end for
                conflictTransitionVexNodeList.addAll(conflictTransitionArray);//加入冲突的变迁列表
            }// end if
        }// end for
    }

    /**根据库所或者变迁节点的名字找到节点*/
    public VexNode getVexNodeByName(String sourceName) {
        for(int i=0;i<xlist.size();i++){
            String name = xlist.get(i).getPlaceOrTransition().getName();
            if(sourceName.equals(name)){
                return xlist.get(i);
            }
        }
        return null;
    }
    /**根据节点的AUMarking名字找到节点*/
    public VexNode getVexNodeByAUMarkingName(String sourceName) {
        for(int i=0;i<xlist.size();i++){
            String name = xlist.get(i).getAUMarking();
            if(sourceName.equals(name)){
                return xlist.get(i);
            }
        }
        return null;
    }
    /**根据库所或者变迁节点的名字找到节点*/
    public VexNode getVexNodeByAUName(String auName) {
        for(int i=0;i<xlist.size();i++){
            String name = xlist.get(i).getAUMarking();
            if(auName.equals(name)){
                return xlist.get(i);
            }
        }
        return null;
    }
    private VexNode findT(String name){
        for(int i=0;i<xlist.size();i++){
            VexNode vexNode = xlist.get(i);
            if(vexNode.getPlaceOrTransition().getName().equals(name)){
                return vexNode;
            }
        }
        return null;
    }
    double getDoubleValue(LogicalTime distance){
        String str = distance.toString();
        System.out.println(str);
        int i=0;
        for(;i<str.length();i++){
            if('<' == str.charAt(i)){
                break;
            }
        }
        String s =str.substring(i+1,str.length()-1);
        double d = Double.parseDouble(s);
        return d;
    }
    /**推进时间，对delay进行处理*/
    public void advanceTime(LogicalTime _time){
        if(priorityQueue.size() > 0){
            Iterator<VexNode> it = priorityQueue.iterator();
            while (it.hasNext()){
                TransitionNode transitionNode = (TransitionNode) it.next().getPlaceOrTransition();
                double lastAdvancedTime = transitionNode.getLastAdvancedTime();
                double now = getDoubleValue(_time);
                //时间从变迁使能开始推进的差值
                double distance = now - lastAdvancedTime;
                //delay值随着时间的推进而减少
                transitionNode.setDelay(transitionNode.getDelay() - distance);
                //上次推进的时间改为现在
                transitionNode.setLastAdvancedTime(now);
            }
        }
    }
    /**对token发生变化的库所重新判断，获得相应的使能变迁，并加入两个队列中
     * 根据库所变化对变迁进行判断
     * */
    public void setTransitionEnabledStatusAfterReceivedToken(VexNode changedPlace) throws Exception {
        //库所的输入输出弧的权重也应该有多个，因为可能有多条边
        HashSet<EdgeNode> outputArcNodes = getAllOutputArcNodesWithHash(changedPlace);

        /*
        double distance2 = getDoubleValue(distance);
        if(!priorityQueue.isEmpty()){
            Iterator<VexNode> iterator= priorityQueue.iterator();
            while (iterator.hasNext()){
                VexNode vexNode = iterator.next();
                double delay =  ((TransitionNode)vexNode.getPlaceOrTransition()).getDelay();
                if(delay >= distance2)
                    ((TransitionNode)vexNode.getPlaceOrTransition()).setDelay(delay - distance2);
                else {
                    throw new Exception("不应该发生呀！新到的时间肯定小于内部事件队列的第一个事件的时间，而该时间是当前最短的时间！");
                }
            }
        }*/

        for(EdgeNode edgeNode : outputArcNodes){
            int outputArcWeight = edgeNode.getCurrentArc().getWeight();
            String targetName = edgeNode.getCurrentArc().get_target();
            VexNode transitionNode = findT(targetName);
            TransitionNode transition = (TransitionNode) transitionNode.getPlaceOrTransition();

            //之前更新过tokennum了。  (不需要后面这个)有效token数量：变迁发生一次后剩余的token数量==》针对于同一个变迁再次增加一个token使能
            //int validTokenNum = ((PlaceNode)changedPlace.getPlaceOrTransition()).getNumOfToken() - outputArcWeight;
            boolean enabled = isEnabled(transitionNode);
            //1、若变迁之前不使能，现在使能==>添加进队列 （新增加的token数量小于输出弧的权重）
            if(!transition.isEnabled() && enabled){
                //设置为true
                transition.setEnabled(true);
                if(transition.getType() == 0){//瞬时变迁
                    if(!zeroQueue.contains(transitionNode))
                        zeroQueue.add(transitionNode);
                }else{
                    if(!priorityQueue.contains(transitionNode))
                        priorityQueue.add(transitionNode);
                    //System.out.println("它的长度为："+priorityQueue.size());
                }
            }//如果之前使能，只推进时间和增加token即可，那发生的时候也少一步判断了
//            else if(transition.isEnabled()){
//                //2、若变迁之前使能，现在又多了一个token，

//            }else {
//                //3、之前不使能，现在不使能--pass
//                //4、之前使能，若新增加的token数量小于输出弧的权重==>只改变token数，不添加使能变迁
//            }

        }
    }



    /**获得所有可能的使能变迁，并加入两个队列中，  初始化的时候*/
    public void setTransitionEnabledStatus() throws Exception {

        //对每一个变迁的前后进行判断,从库所后面一个开始遍历
        for(int i = placenum; i < vexnum; i++){
            //我们的逻辑是先放入库所，再放入变迁，所以这里取出来的肯定是变迁，不再进行判断
            VexNode transitionNode =  xlist.get(i);

            //System.out.println("………………开始的时候，判断变迁："+transitionNode.getPlaceOrTransition().getName()+"是不是使能！");
            boolean enabled = isEnabled(transitionNode);
            //System.out.println("………………使能结果为："+enabled);
            TransitionNode transition = ((TransitionNode)transitionNode.getPlaceOrTransition());

            //考虑添加进优先级队列
            if(enabled){
                transition.setEnabled(enabled);
                if(transition.getType() == 0){//瞬时变迁
                    if(!zeroQueue.contains(transitionNode))
                        zeroQueue.add(transitionNode);
                }else{
                    if(!priorityQueue.contains(transitionNode))
                        priorityQueue.add(transitionNode);
                    //System.out.println("它的长度为："+priorityQueue.size());
                }
            }
        }

    }
    /**
     * 这个操作应该在每一轮的开始进行，之后直接写一个修改队列的方法就行了==》顺序仿真*/
    public void setEnabledTransitions() throws Exception {

        if(_currentMarkingVectorChanged)
        {
            createMatrixes();
        }
        //得到使能的变迁
        try {
            setTransitionEnabledStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //变迁发生规则==》顺序仿真 TODO 目前不考虑优先级，这个之后应该修改为获得多个变迁，因为一次性也可能发生多个
    public VexNode getRandomTransition(){
        //setEnabledTransitions();//将要使能的变迁设置为使能
        //a) 存在瞬时变迁使能; 选择优先级最高的一个发生，(TODO 目前不考虑优先级)，根据随机数来决定
        if(zeroQueue.size() > 0){
            int index = new Random().nextInt(zeroQueue.size());
            VexNode transitionNode = zeroQueue.get(index);
            zeroQueue.remove(transitionNode);//fire后得删除！
            return transitionNode;
        }else{
            // b) 没有瞬时变迁，选择一个时延最短的变迁，剩余的减去发生的时延
            if(priorityQueue.isEmpty())
                return null;

            VexNode transitionNode = priorityQueue.remove();
            return transitionNode;
        }
    }

    //变迁发生规则 目前不考虑优先级，修改为获得多个变迁，因为一次性也可能发生多个==》（这里对冲突处理了）分布式仿真
    public ArrayList<VexNode> getWillFiredTransitions(){
        ArrayList<VexNode> arr = new ArrayList<>();
        //setEnabledTransitions();//将要使能的变迁设置为使能
        //a) 存在瞬时变迁使能; 选择优先级最高的一个发生，(TODO 目前不考虑优先级)，根据随机数来决定
        if(zeroQueue.size() > 0){
            System.out.println("zeroQueue.size():"+zeroQueue.size());
            if(zeroQueue.size() == 1){
                System.out.println("zeroQueue.size():"+zeroQueue.size());
                VexNode transitionNode = zeroQueue.remove(0);//fire后得删除！
                arr.add(transitionNode);
                return arr;
            }else{//有多个==》判断冲突后，把发生互不影响的加入
                //对zeroQueue中的所有变迁进行检测，如果zeroQueue包含冲突的变迁，都添加到tmp数组中，最后选择一个返回
                System.out.println("else后的第一个zeroQueue.size():"+zeroQueue.size());
                for(int i=0;i<zeroQueue.size();i++){
                    System.out.println("for循环中zeroQueue.size():"+zeroQueue.size());
                    //临时存放冲突变迁的数组，对于每一个都要新声明一个
                    ArrayList<VexNode> conflictOfZeroQueue = new ArrayList<>();
                    VexNode transitionNode = zeroQueue.get(i);
                    //获得该变迁的冲突变迁集合，这个肯定包括 transitionNode==》这个也可能是0呀！！！
                    ArrayList<VexNode> conflictArray = ((TransitionNode)transitionNode.getPlaceOrTransition()).getConflictTransition();
                    if(conflictArray.size() == 0){
                        System.out.println("该变迁不存在冲突结构,那么它会发生！");
                        zeroQueue.remove(transitionNode);
                        i--;
                        arr.add(transitionNode);
                    }else{
                        System.out.println("瞬时若存在，那么长度至少为2！"+conflictArray.size());
                        System.out.println("conflictArray应该包括transitionNode："+conflictArray.contains(transitionNode)+",这个变迁是："+(transitionNode.getPlaceOrTransition()).getName());
                        for(VexNode node : conflictArray){
                            //只要包含冲突结构的都先从
                            if(((TransitionNode)node.getPlaceOrTransition()).getType() == 0 && zeroQueue.contains(node)){
                                //因为冲突的最后只能发生一个，所以需要将其删除
                                zeroQueue.remove(node);//可以删,因为前面使用到了zeroQueue，size也随着变化
                                i--;//因为队列删除了一个，所以size减一，数字往前移动了一位，i也得往前移动一位
                                //将其设置为不使能
                                ((TransitionNode)node.getPlaceOrTransition()).setEnabled(false);
                                System.out.println("这里运行前，conflictOfZeroQueue的长度："+conflictOfZeroQueue.size());
                                conflictOfZeroQueue.add(node);
                                System.out.println("加入的元素是："+node.getPlaceOrTransition().getName());
                                System.out.println("这里运行后，conflictOfZeroQueue至少为1"+conflictOfZeroQueue.size());
                            }//如果不包含，
                        }// end for node
                        //冲突的随机加入一个 TODO  这里发生过错误 Random内部不能为0
                        int size = conflictOfZeroQueue.size();
                        System.out.println("conflictOfZeroQueue长度："+conflictOfZeroQueue.size());
                        if(size == 0){
                            System.out.println("+++++++++++++++++++++++++++++++");
                            for(int t=0;t<conflictArray.size();t++){
                                System.out.println(transitionNode.getPlaceOrTransition().getName()+"的冲突变迁包括："+conflictArray.get(i).getPlaceOrTransition().getName());
                            }
                            System.out.println(conflictOfZeroQueue);
                            System.out.println("conflictOfZeroQueue的长度："+conflictOfZeroQueue.size());
                            System.out.println("zeroQueue的长度："+zeroQueue.size());
                            System.out.println("+++++++++++++++++++++++++++++++");
                            try {
                                throw new Exception("不应该为0啊!看上面+内的内容");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(size == 1){
                            VexNode tmp = conflictOfZeroQueue.get(0);
                            arr.add(tmp);
                            ((TransitionNode)tmp.getPlaceOrTransition()).setEnabled(true);
                        }else if(size > 1){
                            int index = new Random().nextInt(size);
                            VexNode tmp = conflictOfZeroQueue.get(index);
                            //将其设置回使能
                            ((TransitionNode)tmp.getPlaceOrTransition()).setEnabled(true);
                            arr.add(tmp);
                        }
                    }
                }//end for i
                if(zeroQueue.size() > 0)
                    arr.addAll(zeroQueue);
                zeroQueue.clear();//瞬时变迁应该处理完了
                return arr;
            }//end else多个变迁

        }else{
            // b) 没有瞬时变迁，选择一个时延最短的变迁，只能选最短的，哪怕是并发的但时间不同也不成 TODO 如果没有使能的话，是不是应该阻塞，等待外部事件的到来
            if(priorityQueue.isEmpty())//如果没有使能的变迁，也就是没有变迁发生的事件，内部事件队列肯定为null，所以阻塞了
                return null;

            VexNode transitionNode = priorityQueue.remove();
            double time = ((TransitionNode)transitionNode.getPlaceOrTransition()).getDelay();
            //第一个时间最短，加入
            arr.add(transitionNode);
            //把后面时间相同的也加入
            while(!priorityQueue.isEmpty() && ((TransitionNode)(priorityQueue.peek()).getPlaceOrTransition()).getDelay()== time){
                arr.add(priorityQueue.remove());
            }
            if(arr.size() == 1)
                return arr;
            else {
                //对arr中可能的冲突变迁判断，既要从arr中删除，也要从priorityQueue中删除
                for(int i=0;i<arr.size();i++){
                    ArrayList<VexNode> conflictOfArr = new ArrayList<>();
                    TransitionNode transitionNode1 = (TransitionNode)arr.get(i).getPlaceOrTransition();
                    ArrayList<VexNode> conflicts = transitionNode1.getConflictTransition();
                    if(conflicts.size() == 0){//该节点不存在冲突的节点
                        System.out.println("该变迁不存在冲突结构,那么它会发生！");
                        continue;
                    }else {
                        System.out.println("时延 ，若存在，那么长度至少为2！"+conflicts.size());
                        //删除了所有的冲突节点（冲突节点包括自身的）
                        for(VexNode vexNode : conflicts){
                            System.out.println("是够包含该节点"+arr.contains(vexNode));
                            if(arr.contains(vexNode)){
                                arr.remove(vexNode);
                                //将其设置为不使能
                                ((TransitionNode)vexNode.getPlaceOrTransition()).setEnabled(false);
                                i--;
                                System.out.println("mark5  for i=:"+i);
                                conflictOfArr.add(vexNode);//把存在的冲突的节点临时保存
                            }
                        }
                        //然后再添加
                        int size = conflictOfArr.size();
                        System.out.println("mark 1 --conflictOfArr的长度："+conflictOfArr.size());
                        if(size == 0){
                            System.out.println("+++++++++++++++++++++++++++++++");
                            for(int t=0;t<conflicts.size();t++){
                                System.out.println(transitionNode1.getName()+"的冲突变迁包括："+conflicts.get(i).getPlaceOrTransition().getName());
                            }
                            System.out.println(conflictOfArr);
                            System.out.println("conflictOfArr的长度："+conflictOfArr.size());
                            System.out.println("arr的长度："+arr.size());
                            System.out.println("+++++++++++++++++++++++++++++++");
                            try {
                                throw new Exception("不应该为0啊!看上面+内的内容");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(size == 1){
                            System.out.println("mark2");
                            VexNode tmp = conflictOfArr.get(0);
                            arr.add(tmp);
                            ((TransitionNode)tmp.getPlaceOrTransition()).setEnabled(true);
                        }else if(size > 1){
                            System.out.println("mark3");
                            int index = new Random().nextInt(size);
                            VexNode tmp = conflictOfArr.get(index);
                            //将其设置回使能
                            ((TransitionNode)tmp.getPlaceOrTransition()).setEnabled(true);
                            arr.add(tmp);
                        }
                    }
                    System.out.println("mark4 i=:"+i);
                }
            }
            return arr;
        }
    }

    /**判断某个包含变迁的节点是不是使能*/
    private boolean isEnabled(VexNode transitionNode)   {
        if(!(transitionNode.getPlaceOrTransition() instanceof TransitionNode))
            try {
                throw new Exception("transitionNode不是变迁类型！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        boolean enabled = true;
        //获得这个变迁前边所有的库所的token数量 与weight进行比较
        HashSet<EdgeNode> inputArcNodes = getAllInputArcNodesWithHash(transitionNode);
        for (EdgeNode edgeNode : inputArcNodes){
            ArcNode arcNode = edgeNode.getCurrentArc();
            int inputWeight = arcNode.getWeight();
            PetriNetNode sourceNode = xlist.get(edgeNode.getSourceIndex()).getPlaceOrTransition();
            //System.out.println(sourceNode.getName());
            int numOfToken = ((PlaceNode)sourceNode).getNumOfToken();
            if(numOfToken < 0){
                try {
                    System.out.println("numOfToken小于0,numOfToken:"+numOfToken);
                    throw new Exception("numOfToken小于0！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            boolean isOrdinary = edgeNode.isOrdinaryArc();
            String type = edgeNode.getCurrentArc().getType();
            //还要对弧进行判断
            if(isOrdinary || "normal".equals(type)){//一般弧
                if(numOfToken < inputWeight)
                    return false;
            }else if("inhibitor".equals(type) || !isOrdinary){//抑制弧 TODO 忘了
                System.out.println("。。。。。。。。。。进入抑制弧的判断。。。。。。。。。。。");
                if(numOfToken > 0)
                    return false;
                if (numOfToken == 0)
                    enabled = true;
            }
        }
        //获得这个变迁后边所有的库所的token数量
        HashSet<EdgeNode> outputArcNodes = getAllOutputArcNodesWithHash(transitionNode);
        for (EdgeNode edgeNode : outputArcNodes){
            ArcNode arcNode = edgeNode.getCurrentArc();
            int outputWeight = arcNode.getWeight();
            int index = edgeNode.getTargetIndex();
            if(index != -1){//在同一个子网中
                PetriNetNode targetNode = xlist.get(index).getPlaceOrTransition();
                //System.out.println(targetNode.getName());
                int numOfToken = ((PlaceNode)targetNode).getNumOfToken();
                int capacity = ((PlaceNode)targetNode).getCapacity();
                if(numOfToken < 0)
                    try {
                        System.out.println("numOfToken小于0,numOfToken:"+numOfToken);
                        throw new Exception("numOfToken小于0！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                //不需要对弧进行判断  capacity==0表示没有容量的限制
                if(capacity > 0 && numOfToken + outputWeight > capacity)
                    enabled = false;
            }else{//后面的库所在另一个子网中  TODO  这个变迁后面的库所可能不在这个子网中，这里先假设库所的容量无限大好了，那这里就不用判断了

            }

        }
        return enabled;
    }
    /**通过序列化的方法实现深拷贝*/
    private VexNode deepCopy(VexNode transitionNode) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(transitionNode);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        VexNode newNode = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            newNode = (VexNode) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newNode;
    }

    /**
     * 变迁发生后，
     * 重新对两个使能变迁的队列进行修改，以供下一轮提供基础
     * （1）持续使能：如果已经包含了（即持续使能变迁），continue，时延为之前减少的那些；
     * （2）新使能：如果未包含（即新使能变迁），添加新使能的变迁到队列，delay赋初值；
     * （3）新不使能：这个情况只发生在当前变迁和与当前变迁有**冲突**的变迁，时延改不改无所谓，得把enabled改正；*/
    public double setEnabledTransitionsQueueAgain(VexNode transitionVexNode,double simClock) {
        TransitionNode transitionNode = (TransitionNode)transitionVexNode.getPlaceOrTransition();
        double delay = transitionNode.getDelay();//如果是瞬时变迁，delay = 0，不影响；
        //全局时钟前移，simClock好像只给瞬时仿真用
        simClock += delay;
        //一、首先对队列中的所有之前使能的变迁进行时间的减少操作； 瞬时变迁的话为0，减了也不影响
        /*
        for(VexNode vexNode : priorityQueue){
            TransitionNode transition = (TransitionNode)vexNode.getPlaceOrTransition();
            //TODO 在分布式仿真中收到消息后是不是也得减去呢？
            double tmp = transition.getDelay() - delay;
            transition.setDelay(tmp);
            if(tmp < 0){
                System.out.println(delay+"<----delay,tmp--->"+tmp);
                try {
                    throw new Exception("时间参数小于0了，不可能呀！！！");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }*/

        //二、对当前变迁的判断  TODO   若T2第一个没有发生，两个token，T2消耗掉一个后保留在了内部队列中了
        boolean isEnabled = true;
        try {
            //System.out.println(transitionVexNode.getPlaceOrTransition().getName());
            isEnabled = isEnabled(transitionVexNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //1。1、继续使能==》当前变迁持续使能，这是可能的
        if(isEnabled){
            transitionNode.setEnabled(true);
            if(transitionNode.getType() == 0){
                if (!zeroQueue.contains(transitionVexNode))
                    zeroQueue.add(transitionVexNode);
            }else{//非瞬时变迁
                transitionNode.resetDelay();
                if(!priorityQueue.contains(transitionVexNode))
                    priorityQueue.add(transitionVexNode);
            }
        }else{//1.2不使能了
            transitionNode.setEnabled(false);
        }
        //2、对已发生变迁 的 相冲突的所有变迁判断==>新不使能
        ArrayList<VexNode> conflictTransition = transitionNode.getConflictTransition();
        if(conflictTransition.size() == 1)
            try {                throw new Exception("冲突变迁的数量不可能为1！");            } catch (Exception e) {                e.printStackTrace();            }
        if(conflictTransition.size() > 1){//说明存在冲突变迁
            for(int i=0;i<conflictTransition.size();i++){
                VexNode vexNode = conflictTransition.get(i);
                TransitionNode transition = (TransitionNode)vexNode.getPlaceOrTransition();
                if(transition.getName().equals(transitionNode.getName()))//本身
                    continue;
                boolean enable = true;

                enable = isEnabled(vexNode);

                if(enable){//如果使能  ==》也可能持续使能 //肯定是包含的呀，不过还是判断下好了，混惑会不会出现什么问题？
                    transition.setEnabled(true);
                    if(transition.getType() == Constant.INSTANT_TRANSITION){
                        if(!zeroQueue.contains(vexNode)){//如果不包含，加入；包含，不改变
                            zeroQueue.add(vexNode);
                            System.out.println("队列中肯定得包含这个冲突变迁，否则就是出错了！");
                        }//包含的话，不影响
                    }else{
                        if(!priorityQueue.contains(vexNode)){
                            ((TransitionNode)vexNode.getPlaceOrTransition()).resetDelay();
                            priorityQueue.add(vexNode);
                            System.out.println("优先级队列中肯定得包含这个冲突变迁，否则就是出错了！");
                        }//包含的话，减去个时间即可
                    }
                }else{//如果不使能，直接在队列中删除即可！
                    transition.setEnabled(false);//将其设置为不使能，然后从使能队列中删除
                    if(transition.getType() == Constant.INSTANT_TRANSITION){
                        if(zeroQueue.contains(vexNode)){//包含的话，删除
                            zeroQueue.remove(vexNode);
                        }
                    }else{
                        if(priorityQueue.contains(vexNode)){
                            priorityQueue.remove(vexNode);
                        }
                    }
                }//end else

            }// end for
        }//end if >1

        //3、对当前变迁之后的相关变迁进行判断====>（新使能和新不使能，也可能持续使能）TODO 考虑冲撞的话，还得考虑变迁之前的库所，这里先不考虑冲撞
        //得到该变迁的所有后面的库所
        HashSet<EdgeNode> outputPArcNodes = getAllOutputArcNodesWithHash(transitionVexNode);
        for(EdgeNode edgeNode : outputPArcNodes){
            int index = edgeNode.getTargetIndex();
            if(index != -1){// 该库所和变迁在同一个子网
                VexNode pnode = xlist.get(index);//库所
                //System.out.println(pnode.getPlaceOrTransition().getName());//PlaceNode placeNode = (PlaceNode)pnode.getPlaceOrTransition();
                //得到每个库所后面的变迁，对其进行使能判断
                HashSet<EdgeNode> outputTArcNodes = getAllOutputArcNodesWithHash(pnode);
                for(EdgeNode edgeT : outputTArcNodes){
                    int index1 = edgeT.getTargetIndex();//TODO 库所后面的变迁一般都在一个子网中，但后期扩展不一定
                    VexNode tnode = xlist.get(index1);//库所后的变迁
                    TransitionNode transitionNode1 = (TransitionNode)tnode.getPlaceOrTransition();

                    boolean isEnable = true;
                    try {
                        isEnable = isEnabled(tnode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean lastEnabled = transitionNode1.isEnabled();// 看之前是否使能
                    //（1）之前使能,现在也使能，即持续使能，已经处理过--TODO 这里有问题了
                    //（2）之前不使能，现在也不使能，不用管---pass
                    //（3）之前不使能，现在使能，即新使能--重置时延
                    if(!lastEnabled && isEnable){//如果使能   之前bu使能
                        transitionNode1.resetDelay();//重置时延
                        transitionNode1.setEnabled(isEnable);//设置使能
                        //添加进队列中
                        if(transitionNode1.getType() == Constant.INSTANT_TRANSITION){
                            if(!zeroQueue.contains(tnode))
                                zeroQueue.add(tnode);
                        }else{
                            if(!priorityQueue.contains(tnode))
                                priorityQueue.add(tnode);
                        }
                    }
                    //（4）之前使能，现在不使能，即新不使能
                    if(lastEnabled && !isEnable){
                        transitionNode1.setEnabled(false);//设置不使能
                        //从进队列中删除
                        if(transitionNode1.getType() == Constant.INSTANT_TRANSITION)
                            zeroQueue.remove(tnode);
                        else
                            priorityQueue.remove(tnode);
                    }//end if

                }//end for edgeT
            }else{//TODO 应该不用判断了，前面肯定发送消息了

            }
        }//end for edgeNode
        return simClock;
    }

    /**
     * 变迁发生后，
     * 重新对两个使能变迁的队列进行修改，以供下一轮提供基础
     * （1）持续使能：如果已经包含了（即持续使能变迁），continue，时延为之前减少的那些；
     * （2）新使能：如果未包含（即新使能变迁），添加新使能的变迁到队列，delay赋初值；
     * （3）新不使能：这个情况只发生在当前变迁和与当前变迁有**冲突**的变迁，时延改不改无所谓，得把enabled改正；*/
    public double setEnabledTransitionsQueueAgainInLogical(VexNode transitionVexNode,double simClock) {
        TransitionNode transitionNode = (TransitionNode)transitionVexNode.getPlaceOrTransition();
        double delay = transitionNode.getDelay();//如果是瞬时变迁，delay = 0，不影响；
        //全局时钟前移，simClock好像只给瞬时仿真用
        simClock += delay;
        //一、首先对队列中的所有之前使能的变迁进行时间的减少操作； 瞬时变迁的话为0，减了也不影响

        for(VexNode vexNode : priorityQueue){
            TransitionNode transition = (TransitionNode)vexNode.getPlaceOrTransition();
            //TODO 在分布式仿真中收到消息后是不是也得减去呢？
            double tmp = transition.getDelay() - delay;
            transition.setDelay(tmp);
            if(tmp < 0){
                System.out.println(delay+"<----delay,tmp--->"+tmp);
                try {
                    throw new Exception("时间参数小于0了，不可能呀！！！");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        //二、对当前变迁的判断  TODO   若T2第一个没有发生，两个token，T2消耗掉一个后保留在了内部队列中了
        boolean isEnabled = true;
        try {
            //System.out.println(transitionVexNode.getPlaceOrTransition().getName());
            isEnabled = isEnabled(transitionVexNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //1。1、继续使能==》当前变迁持续使能，这是可能的
        if(isEnabled){
            transitionNode.setEnabled(true);
            if(transitionNode.getType() == 0){
                if (!zeroQueue.contains(transitionVexNode))
                    zeroQueue.add(transitionVexNode);
            }else{//非瞬时变迁
                transitionNode.resetDelay();
                if(!priorityQueue.contains(transitionVexNode))
                    priorityQueue.add(transitionVexNode);
            }
        }else{//1.2不使能了
            transitionNode.setEnabled(false);
        }
        //2、对已发生变迁 的 相冲突的所有变迁判断==>新不使能
        ArrayList<VexNode> conflictTransition = transitionNode.getConflictTransition();
        if(conflictTransition.size() == 1)
            try {                throw new Exception("冲突变迁的数量不可能为1！");            } catch (Exception e) {                e.printStackTrace();            }
        if(conflictTransition.size() > 1){//说明存在冲突变迁
            for(int i=0;i<conflictTransition.size();i++){
                VexNode vexNode = conflictTransition.get(i);
                TransitionNode transition = (TransitionNode)vexNode.getPlaceOrTransition();
                if(transition.getName().equals(transitionNode.getName()))//本身
                    continue;
                boolean enable = true;

                enable = isEnabled(vexNode);

                if(enable){//如果使能  ==》也可能持续使能 //肯定是包含的呀，不过还是判断下好了，混惑会不会出现什么问题？
                    transition.setEnabled(true);
                    if(transition.getType() == Constant.INSTANT_TRANSITION){
                        if(!zeroQueue.contains(vexNode)){//如果不包含，加入；包含，不改变
                            zeroQueue.add(vexNode);
                            System.out.println("队列中肯定得包含这个冲突变迁，否则就是出错了！");
                        }//包含的话，不影响
                    }else{
                        if(!priorityQueue.contains(vexNode)){
                            ((TransitionNode)vexNode.getPlaceOrTransition()).resetDelay();
                            priorityQueue.add(vexNode);
                            System.out.println("优先级队列中肯定得包含这个冲突变迁，否则就是出错了！");
                        }//包含的话，减去个时间即可
                    }
                }else{//如果不使能，直接在队列中删除即可！
                    transition.setEnabled(false);//将其设置为不使能，然后从使能队列中删除
                    if(transition.getType() == Constant.INSTANT_TRANSITION){
                        if(zeroQueue.contains(vexNode)){//包含的话，删除
                            zeroQueue.remove(vexNode);
                        }
                    }else{
                        if(priorityQueue.contains(vexNode)){
                            priorityQueue.remove(vexNode);
                        }
                    }
                }//end else

            }// end for
        }//end if >1

        //3、对当前变迁之后的相关变迁进行判断====>（新使能和新不使能，也可能持续使能）TODO 考虑冲撞的话，还得考虑变迁之前的库所，这里先不考虑冲撞
        //得到该变迁的所有后面的库所
        HashSet<EdgeNode> outputPArcNodes = getAllOutputArcNodesWithHash(transitionVexNode);
        for(EdgeNode edgeNode : outputPArcNodes){
            int index = edgeNode.getTargetIndex();
            if(index != -1){// 该库所和变迁在同一个子网
                VexNode pnode = xlist.get(index);//库所
                //System.out.println(pnode.getPlaceOrTransition().getName());//PlaceNode placeNode = (PlaceNode)pnode.getPlaceOrTransition();
                //得到每个库所后面的变迁，对其进行使能判断
                HashSet<EdgeNode> outputTArcNodes = getAllOutputArcNodesWithHash(pnode);
                for(EdgeNode edgeT : outputTArcNodes){
                    int index1 = edgeT.getTargetIndex();//TODO 库所后面的变迁一般都在一个子网中，但后期扩展不一定
                    VexNode tnode = xlist.get(index1);//库所后的变迁
                    TransitionNode transitionNode1 = (TransitionNode)tnode.getPlaceOrTransition();

                    boolean isEnable = true;
                    try {
                        isEnable = isEnabled(tnode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    boolean lastEnabled = transitionNode1.isEnabled();// 看之前是否使能
                    //（1）之前使能,现在也使能，即持续使能，已经处理过--TODO 这里有问题了
                    //（2）之前不使能，现在也不使能，不用管---pass
                    //（3）之前不使能，现在使能，即新使能--重置时延
                    if(!lastEnabled && isEnable){//如果使能   之前bu使能
                        transitionNode1.resetDelay();//重置时延
                        transitionNode1.setEnabled(isEnable);//设置使能
                        //添加进队列中
                        if(transitionNode1.getType() == Constant.INSTANT_TRANSITION){
                            if(!zeroQueue.contains(tnode))
                                zeroQueue.add(tnode);
                        }else{
                            if(!priorityQueue.contains(tnode))
                                priorityQueue.add(tnode);
                        }
                    }
                    //（4）之前使能，现在不使能，即新不使能
                    if(lastEnabled && !isEnable){
                        transitionNode1.setEnabled(false);//设置不使能
                        //从进队列中删除
                        if(transitionNode1.getType() == Constant.INSTANT_TRANSITION)
                            zeroQueue.remove(tnode);
                        else
                            priorityQueue.remove(tnode);
                    }//end if

                }//end for edgeT
            }else{//TODO 应该不用判断了，前面肯定发送消息了

            }
        }//end for edgeNode
        return simClock;
    }

    /**发生变迁：===>不用了
     * 1、拿到这个变迁的前面的库所，减少里面token的数量
     * 2、获得这个变迁后边所有的库所的token数量，并增加token
     * */
    public void fireTransitionOfDistributedSimulation(VexNode transitionVexNode)  {
        if(transitionVexNode != null){
            //1、拿到这个变迁的前面的库所，以及后面的库所，减少里面token的数量=》放在生成发生变迁事件的时候减了
            //2、获得这个变迁后边所有的库所的token数量，并增加token
            HashSet<EdgeNode> outputArcNodes = getAllOutputArcNodesWithHash(transitionVexNode);
            for (EdgeNode edgeNode : outputArcNodes){
                ArcNode arcNode = edgeNode.getCurrentArc();
                int outputWeight = arcNode.getWeight();
                PlaceNode targetNode = (PlaceNode) xlist.get(edgeNode.getTargetIndex()).getPlaceOrTransition();
                //System.out.println("发生变迁的targetNode："+targetNode.getName());
                int numOfToken = targetNode.getNumOfToken();
                //int capacity = targetNode.getCapacity();
                if(numOfToken < 0)
                    try {
                        System.out.println("numOfToken:"+numOfToken);
                        throw new Exception("numOfToken小于0！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                int newNumOfToken = numOfToken + outputWeight;
                //System.out.println("targetNode的变化前的数量："+targetNode.getNumOfToken());
                targetNode.setNumOfToken(newNumOfToken);
                //System.out.println("targetNode的变化后的数量："+targetNode.getNumOfToken());
            }

            //3、改变剩余变迁的时延

            //4、变迁发生后的后续处理，放到前面去了
            //setEnabledTransitionsQueueAgain(transitionVexNode);

        }
        setMatrixChanged();
    }
    /**发生变迁：===》顺序仿真
     * 1、拿到这个变迁的前面的库所，减少里面token的数量
     * 2、获得这个变迁后边所有的库所的token数量，并增加token
     * */
    public void fireTransition(VexNode transitionVexNode)  {
        if(transitionVexNode != null){
            //1、拿到这个变迁的前面的库所，以及后面的库所，减少里面token的数量
            HashSet<EdgeNode> inputArcNodes = this.getAllInputArcNodesWithHash(transitionVexNode);
            for (EdgeNode edgeNode : inputArcNodes){
                ArcNode arcNode = edgeNode.getCurrentArc();
                int inputWeight = arcNode.getWeight();
                PlaceNode sourceNode = (PlaceNode) xlist.get(edgeNode.getSourceIndex()).getPlaceOrTransition();
                //System.out.println("发生变迁的sourceNode："+sourceNode.getName());
                int numOfToken = sourceNode.getNumOfToken();
                if(numOfToken < 0)
                    try {
                        System.out.println("numOfToken:"+numOfToken);
                        throw new Exception("numOfToken小于0！不应该的情况！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                boolean isOrdinary = edgeNode.isOrdinaryArc();
                //还要对弧进行判断
                if(isOrdinary){//一般弧
                    // System.out.println("sourceNode的变化前的数量："+sourceNode.getNumOfToken());
                    int newNumOfToken = numOfToken - inputWeight;
                    sourceNode.setNumOfToken(newNumOfToken);
                    //System.out.println("sourceNode的变化后的数量："+sourceNode.getNumOfToken());
                }else{//抑制弧
                    // TODO 怎么变化呢？？？忘了
                }
            }

            //2、获得这个变迁后边所有的库所的token数量，并增加token
            HashSet<EdgeNode> outputArcNodes = getAllOutputArcNodesWithHash(transitionVexNode);
            for (EdgeNode edgeNode : outputArcNodes){
                ArcNode arcNode = edgeNode.getCurrentArc();
                int outputWeight = arcNode.getWeight();
                PlaceNode targetNode = (PlaceNode) xlist.get(edgeNode.getTargetIndex()).getPlaceOrTransition();
                //System.out.println("发生变迁的targetNode："+targetNode.getName());
                int numOfToken = targetNode.getNumOfToken();
                //int capacity = targetNode.getCapacity();
                if(numOfToken < 0)
                    try {
                        System.out.println("numOfToken:"+numOfToken);
                        throw new Exception("numOfToken小于0！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                int newNumOfToken = numOfToken + outputWeight;
                //System.out.println("targetNode的变化前的数量："+targetNode.getNumOfToken());
                targetNode.setNumOfToken(newNumOfToken);
                //System.out.println("targetNode的变化后的数量："+targetNode.getNumOfToken());
            }

            //3、改变剩余变迁的时延

            //4、变迁发生后的后续处理，放到前面去了
            //setEnabledTransitionsQueueAgain(transitionVexNode);

        }
        setMatrixChanged();
    }
    private void setMatrixChanged(){
        _initialMarkingVectorChanged = true;
        _currentMarkingVectorChanged = true;
    }
    /** 获得某个节点的所有输入弧*/
    public HashSet<EdgeNode> getAllInputArcNodesWithHash(VexNode vexNode){
        HashSet<EdgeNode> inputArcNodes = new HashSet<>();
        if(vexNode.getFirstIn() != null){
            //获得以当前节点为弧头的弧
            EdgeNode edgeNode = vexNode.getFirstIn();
            inputArcNodes.add(edgeNode);

            while(edgeNode.getSameTargetNext() != null){

                edgeNode = edgeNode.getSameTargetNext();
                inputArcNodes.add(edgeNode);

            }
            return inputArcNodes;
        }
        //若不是变迁类型，返回NULL
        return null;
    }
    /** 获得某个节点的所有输入弧*/
    public HashSet<EdgeNode> getAllOutputArcNodesWithHash(VexNode vexNode){
        HashSet<EdgeNode> outputArcNodes = new HashSet<>();
        if(vexNode.getFirstOut() != null){
            //获得以当前节点为弧头的弧
            EdgeNode edgeNode = vexNode.getFirstOut();
            outputArcNodes.add(edgeNode);
            //System.out.println(xlist.get(edgeNode.getTargetIndex()).getPlaceOrTransition().getName());
            while(edgeNode.getSameSourceNext() != null){
                edgeNode = edgeNode.getSameSourceNext();
                //System.out.println(xlist.get(edgeNode.getTargetIndex()).getPlaceOrTransition().getName());
                outputArcNodes.add(edgeNode);

            }
            return outputArcNodes;
        }
        //若没有输出的节点，那么返回null数组
        return outputArcNodes;
    }

    /**TODO 这个还一定管用*/
    public void createMatrixes() {

        createInitialMarkingVector();
        createCurrentMarkingVector();

    }
    public int[] getInitialMarkingVector()
    {
        //TODO 这个布尔的判断，还要不要
        if(_initialMarkingVectorChanged)
            createInitialMarkingVector();
        return _initialMarkingVector;
    }
    public Marking getInitialMarkings()
    {
        //TODO 这个布尔的判断，还要不要
        if(_initialMarkingVectorChanged)
            createInitialMarkings();
        return initialMarkings;
    }
    private void createInitialMarkings() {
        int placeSize = placenum;
        initialMarkings = new Marking(placeSize);
        ArrayList<PlaceToken> marking = initialMarkings.getMarking();
        for(int placeNo = 0; placeNo < placeSize; placeNo++) {
            PlaceNode placeNode = (PlaceNode)xlist.get(placeNo).getPlaceOrTransition();
            String placeName = placeNode.getName();
            int tokenNum = placeNode.getNumOfToken();
            marking.set(placeNo,new PlaceToken(placeName,tokenNum));
        }
        initialMarkings.setMarking(marking);
    }
    /**
     * Creates Initial Marking Vector from current Petri-Net
     */
    private void createInitialMarkingVector()
    {
        int placeSize = placenum;

        _initialMarkingVector = new int[placeSize];
        for(int placeNo = 0; placeNo < placeSize; placeNo++)
        {
            PlaceNode placeNode = (PlaceNode)xlist.get(placeNo).getPlaceOrTransition();
            _initialMarkingVector[placeNo] = placeNode.getNumOfToken();
        }
    }

    public int[] getCurrentMarkingVector()
    {
        if(_currentMarkingVectorChanged)
        {
            createCurrentMarkingVector();
        }

        return _currentMarkingVector;
    }

    public Marking getCurrentMarkings()
    {
        if(_currentMarkingVectorChanged)
        {
            createCurrentMarkings();
        }

        return currentMarkings;
    }

    private void createCurrentMarkings()
    {
        int placeSize = placenum;
        currentMarkings = new Marking(placeSize);
        ArrayList<PlaceToken> marking = currentMarkings.getMarking();
        for(int placeNo = 0; placeNo < placeSize; placeNo++) {
            PlaceNode placeNode = (PlaceNode)xlist.get(placeNo).getPlaceOrTransition();
            String placeName = placeNode.getName();
            int tokenNum = placeNode.getNumOfToken();
            marking.set(placeNo,new PlaceToken(placeName,tokenNum));
        }
        currentMarkings.setMarking(marking);
    }

    /**
     * Creates Current Marking Vector from current Petri-Net
     */
    private void createCurrentMarkingVector()
    {
        int placeSize = placenum;

        _currentMarkingVector = new int[placeSize];
        for(int placeNo = 0; placeNo < placeSize; placeNo++)
        {
            PlaceNode placeNode = (PlaceNode)xlist.get(placeNo).getPlaceOrTransition();
            _currentMarkingVector[placeNo] =  placeNode.getNumOfToken();
        }
    }

    public void setPNMLName(String pnmlName) {
        this.PNMLName = pnmlName;
    }

    public String getPNMLName(){
        return PNMLName;
    }

    public int getPlacenum() {
        return placenum;
    }

    public void setPlacenum(int placenum) {
        this.placenum = placenum;
    }

    public int getTransitionnum() {
        return transitionnum;
    }

    public void setTransitionnum(int transitionnum) {
        this.transitionnum = transitionnum;
    }

    public ArrayList<VexNode> getXlist() {
        return xlist;
    }

    public void setXlist(ArrayList<VexNode> xlist) {
        this.xlist = xlist;
    }



    public int getVexnum() {
        return vexnum;
    }

    public void setVexnum(int vexnum) {
        this.vexnum = vexnum;
    }

    public int getEdgenum() {
        return edgenum;
    }

    public void setEdgenum(int edgenum) {
        this.edgenum = edgenum;
    }

    public ArrayList<ArcNode> getArclist() {
        return arclist;
    }

    public void setArclist(ArrayList<ArcNode> arclist) {
        this.arclist = arclist;
    }

    /**打印Petri网的十字链表,用于测试*/
    public static void printListInSubNet(OLGraph olGraph){
        System.out.println("AdjList:\n");
        int vexnum = olGraph.getVexnum();
        ArrayList<VexNode> xlist = olGraph.getXlist();
        for(int i=0;i<vexnum;i++){
            VexNode vexNode = xlist.get(i);
            System.out.print(vexNode.getPlaceOrTransition().getName() + ":");
            //System.out.println(vexNode.firstOut);
            EdgeNode edgeNode = null;
            if(vexNode.getFirstOut() != null){//如果当前顶点的第一个指向边的指针不为空
                edgeNode = vexNode.getFirstOut();//获得以该顶点为弧尾的第一条弧节点
                while(edgeNode != null){//获得弧尾相同的下一弧节点
                    int snum = edgeNode.getSourceIndex();
                    int tnum = edgeNode.getTargetIndex();
                    System.out.print("["+snum + "(" + edgeNode.getCurrentArc().get_source() + ")-->");
                    System.out.print(tnum + "(" + edgeNode.getCurrentArc().get_target() + ")" + "]");
                    edgeNode = edgeNode.getSameSourceNext();
                }
                System.out.println();
            }else {
                System.out.println();
            }
        }

        System.out.println("------------------------------------------");
        System.out.println("InvAdjList:\n");
        for(int i=0;i<vexnum;i++){
            VexNode vexNode = xlist.get(i);
            System.out.print(vexNode.getPlaceOrTransition().getName() +":" );
            if(vexNode.getFirstIn() != null){
                EdgeNode edgeNode = vexNode.getFirstIn();
                while(edgeNode != null){
                    int snum = edgeNode.getSourceIndex();
                    int tnum = edgeNode.getTargetIndex();
                    System.out.print("["+snum + "(" + edgeNode.getCurrentArc().get_source() + ")-->");
                    System.out.print(tnum + "(" + edgeNode.getCurrentArc().get_target() + ")" + "]");
                    edgeNode = edgeNode.getSameTargetNext();
                }
                System.out.println();
            }else {
                System.out.println();
            }
        }
    }
    /**打印AU图的十字链表,用于测试*/
    public static void printAUMarkingList(OLGraph olGraph){
        System.out.println("AdjList:\n");
        int vexnum = olGraph.getVexnum();
        ArrayList<VexNode> xlist = olGraph.getXlist();
        for(int i=0;i<vexnum;i++){
            VexNode vexNode = xlist.get(i);
            System.out.print(vexNode.getAUMarking() + ":");
            //System.out.println(vexNode.firstOut);
            EdgeNode edgeNode = null;
            if(vexNode.getFirstOut() != null){//如果当前顶点的第一个指向边的指针不为空
                edgeNode = vexNode.getFirstOut();//获得以该顶点为弧尾的第一条弧节点
                while(edgeNode != null){//获得弧尾相同的下一弧节点
                    int snum = edgeNode.getSourceIndex();
                    int tnum = edgeNode.getTargetIndex();
                    System.out.print("["+snum + "(" + xlist.get(snum).getAUMarking() + ")-->");
                    System.out.print(tnum + "(" + xlist.get(tnum).getAUMarking() + ")" + "]");
                    edgeNode = edgeNode.getSameSourceNext();
                }
                System.out.println();
            }else {
                System.out.println();
            }
        }

        System.out.println("------------------------------------------");
        System.out.println("InvAdjList:\n");
        for(int i=0;i<vexnum;i++){
            VexNode vexNode = xlist.get(i);
            System.out.print(vexNode.getAUMarking() +":" );
            if(vexNode.getFirstIn() != null){
                EdgeNode edgeNode = vexNode.getFirstIn();
                while(edgeNode != null){
                    int snum = edgeNode.getSourceIndex();
                    int tnum = edgeNode.getTargetIndex();
                    System.out.print("["+snum + "(" + xlist.get(snum).getAUMarking() + ")-->");
                    System.out.print(tnum + "(" + xlist.get(tnum).getAUMarking() + ")" + "]");
                    edgeNode = edgeNode.getSameTargetNext();
                }
                System.out.println();
            }else {
                System.out.println();
            }
        }
    }
}



