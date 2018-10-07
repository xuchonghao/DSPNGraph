package pipe.hla.SimDSPNModule.util;

import pipe.hla.SimDSPNModule.basemodel.EdgeNode;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.basemodel.SubNet;
import pipe.hla.SimDSPNModule.basemodel.VexNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.ArcNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PetriNetNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.TransitionNode;


import java.util.ArrayList;

/**用来创建各个子网*/
public class ReConstructSubNet {

    static OLGraph subGraph;
    static ArrayList<VexNode> xlist;

    public ReConstructSubNet() {
        subGraph = new OLGraph();
        xlist = subGraph.getXlist();
    }

    public static void main(String[] args) {

        ReConstructSubNet rcs = new ReConstructSubNet();
        rcs.testFederate("p0","t0","p1","t1","P2","T3",1);
        rcs.printList();
    }

    /**创建一个子网，两个变迁和两个库所*/
    public  OLGraph testFederate(String p, String t, String pp, String tt, String otherP, String otherT, int partition){

        ArrayList<PlaceNode> _placeNodes = new ArrayList<>();
        ArrayList<TransitionNode> _transitionNodes = new ArrayList<>();
        ArrayList<ArcNode> _arcNodes = new ArrayList<>();

        PlaceNode placeNode = new PlaceNode(p);
        placeNode.setNumOfToken(1);
        placeNode.setCapacity(Integer.MAX_VALUE);
        placeNode.setPartitionNumber(partition);
        _placeNodes.add(placeNode);

        TransitionNode transitionNode = new TransitionNode(t);
        transitionNode.setType(1);
        transitionNode.setRate(2);
        //transitionNode.setPriority(transitionView.getPriority());
        transitionNode.resetDelay();
        transitionNode.setEnabled(false);
        transitionNode.setPartitionNumber(partition);
        _transitionNodes.add(transitionNode);

        ArcNode arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(p);
        arcNode.set_target(t);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);


        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(t);
        arcNode.set_target(pp);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);
        //-----------------------------------
        placeNode = new PlaceNode(pp);
        placeNode.setNumOfToken(0);
        placeNode.setCapacity(Integer.MAX_VALUE);
        placeNode.setPartitionNumber(partition);
        _placeNodes.add(placeNode);

        transitionNode = new TransitionNode(tt);
        transitionNode.setType(1);
        transitionNode.setRate(3);
        //transitionNode.setPriority(transitionView.getPriority());
        transitionNode.resetDelay();
        transitionNode.setEnabled(false);
        transitionNode.setPartitionNumber(partition);//设置它的划分区域
        _transitionNodes.add(transitionNode);

        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(pp);
        arcNode.set_target(tt);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);

        //------------
        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(tt);
        arcNode.set_target(otherP);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);

        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(otherT);
        arcNode.set_target(p);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);

        //构造图
        bulidSubGraph(_placeNodes,_transitionNodes,_arcNodes);
        //subGraph.checkConflictStructure();
        return subGraph;
    }

    /**创建一个子网，1个变迁和1个库所*/
    public  OLGraph testFederate2(String p, String t,  String otherP, String otherT, int partition){

        ArrayList<PlaceNode> _placeNodes = new ArrayList<>();
        ArrayList<TransitionNode> _transitionNodes = new ArrayList<>();
        ArrayList<ArcNode> _arcNodes = new ArrayList<>();

        PlaceNode placeNode = new PlaceNode(p);
        placeNode.setNumOfToken(1);
        placeNode.setCapacity(Integer.MAX_VALUE);
        placeNode.setPartitionNumber(partition);
        _placeNodes.add(placeNode);

        TransitionNode transitionNode = new TransitionNode(t);
        transitionNode.setType(1);
        transitionNode.setRate(2);
        //transitionNode.setPriority(transitionView.getPriority());
        transitionNode.resetDelay();
        transitionNode.setEnabled(false);
        transitionNode.setPartitionNumber(partition);
        _transitionNodes.add(transitionNode);

        ArcNode arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(p);
        arcNode.set_target(t);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);


        //------------
        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(t);
        arcNode.set_target(otherP);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);

        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(otherT);
        arcNode.set_target(p);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);

        //构造图
        bulidSubGraph(_placeNodes,_transitionNodes,_arcNodes);
        //subGraph.checkConflictStructure();
        return subGraph;
    }

    /**创建一个子网，两个变迁和两个库所*/
    public  OLGraph testFederate3(String p, String t, String pp, String tt,int partition){

        ArrayList<PlaceNode> _placeNodes = new ArrayList<>();
        ArrayList<TransitionNode> _transitionNodes = new ArrayList<>();
        ArrayList<ArcNode> _arcNodes = new ArrayList<>();

        PlaceNode placeNode = new PlaceNode(p);
        placeNode.setNumOfToken(1);
        placeNode.setCapacity(Integer.MAX_VALUE);
        placeNode.setPartitionNumber(partition);
        _placeNodes.add(placeNode);

        TransitionNode transitionNode = new TransitionNode(t);
        transitionNode.setType(1);
        transitionNode.setRate(2);
        //transitionNode.setPriority(transitionView.getPriority());
        transitionNode.resetDelay();
        transitionNode.setEnabled(false);
        transitionNode.setPartitionNumber(partition);
        _transitionNodes.add(transitionNode);

        ArcNode arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(p);
        arcNode.set_target(t);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);


        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(t);
        arcNode.set_target(pp);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);
        //-----------------------------------
        placeNode = new PlaceNode(pp);
        placeNode.setNumOfToken(0);
        placeNode.setCapacity(Integer.MAX_VALUE);
        placeNode.setPartitionNumber(partition);
        _placeNodes.add(placeNode);

        transitionNode = new TransitionNode(tt);
        transitionNode.setType(1);
        transitionNode.setRate(3);
        //transitionNode.setPriority(transitionView.getPriority());
        transitionNode.resetDelay();
        transitionNode.setEnabled(false);
        transitionNode.setPartitionNumber(partition);//设置它的划分区域
        _transitionNodes.add(transitionNode);

        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(pp);
        arcNode.set_target(tt);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);

        //------------
        arcNode = new ArcNode();
        arcNode.setType("normal");
        arcNode.set_source(tt);
        arcNode.set_target(p);
        arcNode.setWeight(1);
        _arcNodes.add(arcNode);

        //构造图
        bulidSubGraph(_placeNodes,_transitionNodes,_arcNodes);
        //subGraph.checkConflictStructure();
        return subGraph;
    }

    /**构造图
     * 先创建库所，再创建变迁*/
    public  void bulidSubGraph(ArrayList<PlaceNode> _places, ArrayList<TransitionNode> _transitions,
                                  ArrayList<ArcNode> _arcNodes){
        /*构造顶点为库所的结点*/
        int i;
        for(i=0;i<_places.size();i++){
            addVex(i,false,_places.get(i));
        }

         /*构造顶点为变迁的结点*/
        for(;i<_places.size()+_transitions.size();i++){
            //int type = _transitionViews.get(i - _placeViews.size()).getType();
            addVex(i,true,_transitions.get(i-_places.size()));
        }
        subGraph.setPlacenum(_places.size());
        subGraph.setTransitionnum(_transitions.size());
        subGraph.setVexnum(_places.size() + _transitions.size());

        /*构造普通的边，尾插法*/
        int j;
        for(j=0;j<_arcNodes.size();j++){
            ArcNode arcNode = _arcNodes.get(j);
            //TODO 之前还修改了类型！！
            boolean isOrdinary = arcNode.getType().equals("normal");
            addSubNetEdge(arcNode,isOrdinary);
        }
        subGraph.setEdgenum(_arcNodes.size());
    }

    /**构造图
     * 先创建库所，再创建变迁*/
    public  OLGraph bulidSubGraphByVexAndArc(SubNet net){
        ArrayList<PlaceNode> placeNodes = net.getPlaceNodes();
        ArrayList<TransitionNode> transitionNodes = net.getTransitionNodes();
        ArrayList<ArcNode> arcNodes = net.getArcNodes();
        bulidSubGraph(placeNodes,transitionNodes,arcNodes);
        return subGraph;
    }



    /**添加顶点*/
    private  void addVex(int sequence,boolean isTransition,PetriNetNode placeOrTransition){
        VexNode vexNode = new VexNode();//结点

        vexNode.setTransition(isTransition);
        vexNode.setPlaceOrTransition(placeOrTransition);
        vexNode.setSequence(sequence);
        placeOrTransition.setId(sequence);
        xlist.add(vexNode);
    }
    /**添加弧，注意：如果是临界的边（边界弧），它的一source或者target可能是null
     * */
    private  void addSubNetEdge(ArcNode arcNode, boolean isOrdinary){
        String sourceName = arcNode.get_source();
        String targetName = arcNode.get_target();

        VexNode snode = getVexNode(sourceName);
        VexNode tnode = getVexNode(targetName);
        //如果只有一个节点为null，则说明这个弧是边界弧
        if(snode == null && tnode != null){//输入为null，左边的节点在另一个子网中
            setPlaceWeight(arcNode,null,tnode);
            //该弧同一target的弧集合需要添加；同一source的弧就不再添加了
            EdgeNode arcBox1 = new EdgeNode();
            arcBox1.setCurrentArc(arcNode);
            arcBox1.setOrdinaryArc(isOrdinary);
            arcBox1.setTargetIndex(tnode.getSequence());
            arcBox1.setSourceIndex(-1);//-1表示这个节点不在该子网中，它的在xlist的序号设置一个不可达的-1

            //TODO 感觉这里是多余的 targetNode应该就是tnode吧
            VexNode targetNode = xlist.get(tnode.getSequence());//arcBox2的弧头（同一弧头的）节点
           // System.out.println(tnode.getPlaceOrTransition().getName().equals(targetNode.getPlaceOrTransition().getName()));
            if(targetNode.getFirstIn() == null)
                targetNode.setFirstIn(arcBox1);
            else
                linkLastHeadNext(targetNode.getFirstIn(),arcBox1);

        }else if(snode != null && tnode == null){//输出为null，右边的节点在另一个子网中
            setPlaceWeight(arcNode,snode,null);
            //与该弧同一个source的需要添加；同一target的不再添加
            EdgeNode arcBox1 = new EdgeNode();
            arcBox1.setCurrentArc(arcNode);
            arcBox1.setOrdinaryArc(isOrdinary);
            arcBox1.setSourceIndex(snode.getSequence());
            arcBox1.setTargetIndex(-1);//-1表示这个节点不在该子网中，它的在xlist的序号设置一个不可达的-1

            VexNode sourceNode = xlist.get(snode.getSequence());
            if(sourceNode.getFirstOut() == null)
                sourceNode.setFirstOut(arcBox1);
            else
                linkLastTailNext(sourceNode.getFirstOut(),arcBox1);

        }else if(snode == null && tnode == null){
            System.out.println("sourceName"+sourceName);
            System.out.println("targetName"+targetName);
            try {
//                RES1_Par1_VL0_CommunicationPort
//                        RES1_Par1_VL1_CommunicationPort
                throw new Exception("输入节点名错误，不应该出现这种情况！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{//两边的节点都在一个子网中
            //TODO 设置库所的输入和输出权重，这个函数好像不再起作用了。以后再说，Fire的时候还是有用的
            setPlaceWeight(arcNode,snode,tnode);

            EdgeNode arcBox1 = new EdgeNode();
            arcBox1.setCurrentArc(arcNode);
            arcBox1.setOrdinaryArc(isOrdinary);
            arcBox1.setTargetIndex(tnode.getSequence());
            arcBox1.setSourceIndex(snode.getSequence());

            VexNode sourceNode = xlist.get(snode.getSequence());//当前弧arcBox1的源节点(同一弧尾)
            //EdgeNode firstout = sourceNode.firstOut;//以sourceNode顶点为弧尾(源)的第一个弧节点
            if(sourceNode.getFirstOut() == null)
                sourceNode.setFirstOut(arcBox1);
            else
                linkLastTailNext(sourceNode.getFirstOut(),arcBox1);
            //System.out.println(sourceNode.firstOut);
            EdgeNode arcBox2 = new EdgeNode();
            arcBox2.setOrdinaryArc(isOrdinary);
            arcBox2.setCurrentArc(arcNode);

            arcBox2.setTargetIndex(tnode.getSequence());
            arcBox2.setSourceIndex(snode.getSequence());

            VexNode targetNode = xlist.get(tnode.getSequence());//arcBox2的弧头（同一弧头的）节点
            if(targetNode.getFirstIn() == null)
                targetNode.setFirstIn(arcBox2);
            else
                linkLastHeadNext(targetNode.getFirstIn(),arcBox2);
        }
    }
    /**在同一弧头的边链表上添加节点*/
    private  void linkLastHeadNext(EdgeNode firstnode,EdgeNode node){
        while(firstnode.getSameTargetNext() != null){
            firstnode = firstnode.getSameTargetNext();
        }
        //firstnode.sameHeadNext = node;
        firstnode.setSameTargetNext(node);
    }
    /**在同一弧尾的边链表上添加当前节点*/
    private  void linkLastTailNext(EdgeNode firstnode,EdgeNode node){
        while(firstnode.getSameSourceNext() != null){
            firstnode = firstnode.getSameSourceNext();
        }
        firstnode.setSameSourceNext(node);
    }
    private   void setPlaceWeight(ArcNode arcNode,VexNode snode, VexNode tnode) {
        PetriNetNode petriNetNode = null;
        if(snode != null)
            petriNetNode = snode.getPlaceOrTransition();
        if(petriNetNode instanceof PlaceNode){
            ((PlaceNode)petriNetNode).setOutputArcWeight(arcNode.getWeight());
        }
        if(tnode != null)
            petriNetNode = tnode.getPlaceOrTransition();
        else
            petriNetNode = null;
        if(petriNetNode instanceof PlaceNode){
            ((PlaceNode)petriNetNode).setInputArcWeight(arcNode.getWeight());
        }
    }

    /**从顶点链表返回特定的VexNode 节点*/
    private  VexNode getVexNode(String nodeName){
        for(int i=0;i<xlist.size();i++){
            if(xlist.get(i).getPlaceOrTransition().getName().equals(nodeName)){
                return xlist.get(i);
            }
        }
        return null;
    }


    /**打印十字链表,用于测试*/
    public  void printList(){
        System.out.println("AdjList:\n");
        int vexnum = subGraph.getVexnum();
        ArrayList<VexNode> xlist = subGraph.getXlist();
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
                    if(snum != -1)
                        System.out.print("["+snum + "(" + xlist.get(snum).getPlaceOrTransition().getName() + ")-->");
                    else
                        System.out.print("["+snum + "(" +edgeNode.getCurrentArc().get_source() +  ")-->");
                    if(tnum != -1)
                        System.out.print(tnum + "(" + xlist.get(tnum).getPlaceOrTransition().getName() + ")" + "]");
                    else
                        System.out.print(tnum + "(" +edgeNode.getCurrentArc().get_target() +  ")" + "]");
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
                    if(snum != -1)
                        System.out.print("["+snum + "(" + xlist.get(snum).getPlaceOrTransition().getName() + ")-->");
                    else
                        System.out.print("["+snum + "(" + edgeNode.getCurrentArc().get_source() + ")-->");
                    if(tnum != -1)
                        System.out.print(tnum + "(" + xlist.get(tnum).getPlaceOrTransition().getName() + ")" + "]");
                    else
                        System.out.print(tnum + "(" + edgeNode.getCurrentArc().get_target() + ")" + "]");
                    edgeNode = edgeNode.getSameTargetNext();
                }
                System.out.println();
            }else {
                System.out.println();
            }
        }
    }


}
