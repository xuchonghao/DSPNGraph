package pipe.hla.SimDSPNModule.util;

import pipe.hla.SimDSPNModule.basemodel.EdgeNode;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.basemodel.VexNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.ArcNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PetriNetNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.TransitionNode;
import pipe.views.*;

import java.util.ArrayList;

/**把Pipe中的数据转换为OLGraph的存储结构*/
public class TransferatorFromPIPEHelper {
    static OLGraph olGraph;
    static ArrayList<VexNode> xlist;
    static ArrayList<ArcNode> arclist;

    /**这里肯定是不能算时间的*/
    public static OLGraph transfer(OLGraph olGraph1,PetriNetView data){
        olGraph = olGraph1;
        xlist = olGraph.getXlist();
        /**由于创建子网的时候需要弧，所以在这里把_arcNodes和arclist进行了整合*/
        arclist = olGraph.getArclist();
        ArrayList<PlaceNode> _placeNodes = new ArrayList<>();
        ArrayList<TransitionNode> _transitionNodes = new ArrayList<>();
        //ArrayList<ArcNode> _arcNodes = new ArrayList<>();

        ArrayList<PlaceView> _placeViews = data.getPlacesArrayList();
        ArrayList<TransitionView> _transitionViews = data.getTransitionsArrayList();
        ArrayList<ArcView> _arcViews = data.getArcsArrayList();
        ArrayList<InhibitorArcView> _inhibitorViews = data.getInhibitorsArrayList();


        olGraph.setPNMLName(data.getPNMLName());

        for(int i=0;i<_placeViews.size();i++){
            PlaceView placeView = _placeViews.get(i);
            PlaceNode placeNode = new PlaceNode();
            placeNode.setName(placeView.getName());
            //  这个对不对还得测试,另外，输入弧和输出弧的权重还没有赋值==>正确，已经验证2018-10-06
            placeNode.setNumOfToken(placeView.getCurrentMarkingView().get(0).getCurrentMarking());
            //System.out.println(placeNode.getName() + " : "+placeNode.getNumOfToken());
            int capacity = placeView.getCapacity();
            //System.out.println(placeNode.getName() + " : "+capacity);
            placeNode.setCapacity(capacity);//当capacity为0的时候，这时候假设palce没有capacity约束
            _placeNodes.add(placeNode);
        }

        /*构造顶点为变迁的结点*/
        for(int i=0;i<_transitionViews.size();i++){
            TransitionView transitionView = _transitionViews.get(i);
            TransitionNode transitionNode = new TransitionNode(transitionView.getName());
            //transitionNode.setName(transitionView.getName());
            transitionNode.setType(transitionView.getType());
            transitionNode.setRate(transitionView.getRate());
            //transitionNode.setPriority(transitionView.getPriority());
            transitionNode.setDelay(transitionView.getDelay());//
            //TODO  这里应该构建图的时候重新判断吧
            transitionNode.setEnabled(transitionView.isEnabled());
            _transitionNodes.add(transitionNode);
        }

         /*构造普通的边，尾插法*/
        for(int j=0;j<_arcViews.size();j++){
            ArcView normalArcView = _arcViews.get(j);
            ArcNode arcNode = new ArcNode();
            arcNode.setType(normalArcView.getType());
            arcNode.set_source(normalArcView.getSource().getName());
            arcNode.set_target(normalArcView.getTarget().getName());
            // 这个不确定  ===>正确
            arcNode.setWeight(normalArcView.getWeight().get(0).getCurrentMarking());
            //System.out.println(arcNode.get_source() + " : "+normalArcView.getWeight().get(0).getCurrentMarking());
            arclist.add(arcNode);

        }
        for(int j=0;j<_inhibitorViews.size();j++){
            InhibitorArcView inhibitorArcView = _inhibitorViews.get(j);
            ArcNode arcNode = new ArcNode();
            arcNode.setType(inhibitorArcView.getType());
            arcNode.set_source(inhibitorArcView.getSource().getName());
            arcNode.set_target(inhibitorArcView.getTarget().getName());
            //TODO 这个不确定

            arcNode.setWeight(inhibitorArcView.getSimpleWeight());
            arclist.add(arcNode);
        }
        //构造图
        bulidGraph(_placeNodes,_transitionNodes,arclist);
        //olGraph.checkConflictStructure();//检测冲突这一块先不用了
        return olGraph;
    }

    /**构造图
     * 先创建库所，再创建变迁*/
    public static void bulidGraph(ArrayList<PlaceNode> _places, ArrayList<TransitionNode> _transitions,
                           ArrayList<ArcNode> arclist){
        /*构造顶点为库所的结点*/
        int i;
        //System.out.println(_placeViews.get(0).getId());
        //System.out.println(_placeViews.get(0).getModel().get_id());
        for(i=0;i<_places.size();i++){
            addVex(i,false,_places.get(i));
        }

         /*构造顶点为变迁的结点*/
        for(;i<_places.size()+_transitions.size();i++){
            //int type = _transitionViews.get(i - _placeViews.size()).getType();
            addVex(i,true,_transitions.get(i-_places.size()));
        }
        olGraph.setPlacenum(_places.size());
        olGraph.setTransitionnum(_transitions.size());
        olGraph.setVexnum(_places.size() + _transitions.size());

        /*构造普通的边，尾插法*/
        //int j;
        for(int j=0;j<arclist.size();j++){
            ArcNode arcView = arclist.get(j);
            boolean isOrdinary = arcView.getType().equals("normal");
            addEdge(arcView,isOrdinary);
        }
        olGraph.setEdgenum(arclist.size());
    }

    /**添加顶点*/
    private static void addVex(int sequence,boolean isTransition,PetriNetNode placeOrTransition){
        VexNode vexNode = new VexNode();//结点
        //System.out.println(placeOrTransition.get_id());
        vexNode.setTransition(isTransition);
        vexNode.setPlaceOrTransition(placeOrTransition);
        vexNode.setSequence(sequence);
        placeOrTransition.setId(sequence);
        xlist.add(vexNode);
    }
    /**添加弧*/
    private static void addEdge(ArcNode arcNode, boolean isOrdinary){
        VexNode snode = getVexNode(arcNode.get_source());
        VexNode tnode = getVexNode(arcNode.get_target());
        //设置库所的输入和输出权重
        setPlaceWeight(arcNode,snode,tnode);

        EdgeNode arcBox1 = new EdgeNode();
        arcBox1.setCurrentArc(arcNode);
        arcBox1.setOrdinaryArc(isOrdinary);

        arcBox1.setTargetIndex(tnode.getSequence());
        arcBox1.setSourceIndex(snode.getSequence());

        //System.out.println(arcBox1.tailvex + "," +  arcBox1.tailstr);
        //System.out.println(arcBox1.headvex + "," +  arcBox1.headstr );
        VexNode sourceNode = xlist.get(snode.getSequence());//当前弧arcBox1的源节点(同一弧尾)
        //经验证：确实是一个对象
        //System.out.println("TransferatorFromPIPEHelper 149左右，想确认一下，这两个应该是一个节点吧！true的话可删除"+(snode == sourceNode));
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

        //System.out.println(arcBox1.tailvex + "," +  arcBox1.tailVexNode.nodeId);
        //System.out.println(arcBox1.headvex + "," +  arcBox1.headVexNode.nodeId );

        VexNode targetNode = xlist.get(tnode.getSequence());//arcBox2的弧头（同一弧头的）节点
        if(targetNode.getFirstIn() == null)
            targetNode.setFirstIn(arcBox2);
        else
            linkLastHeadNext(targetNode.getFirstIn(),arcBox2);
    }
    /**在同一弧头的边链表上添加节点*/
    private static void linkLastHeadNext(EdgeNode firstnode,EdgeNode node){
        while(firstnode.getSameTargetNext() != null){
            firstnode = firstnode.getSameTargetNext();
        }
        //firstnode.sameHeadNext = node;
        firstnode.setSameTargetNext(node);
    }
    /**在同一弧尾的边链表上添加当前节点*/
    private static void linkLastTailNext(EdgeNode firstnode,EdgeNode node){
        while(firstnode.getSameSourceNext() != null){
            firstnode = firstnode.getSameSourceNext();
        }
        firstnode.setSameSourceNext(node);
    }
    /**设置库所的输入和输出权重*/
    private  static void setPlaceWeight(ArcNode arcNode,VexNode snode, VexNode tnode) {
        PetriNetNode petriNetNode = snode.getPlaceOrTransition();
        if(petriNetNode instanceof PlaceNode){
            ((PlaceNode)petriNetNode).setOutputArcWeight(arcNode.getWeight());
        }
        petriNetNode = tnode.getPlaceOrTransition();
        if(petriNetNode instanceof PlaceNode){
            ((PlaceNode)petriNetNode).setInputArcWeight(arcNode.getWeight());
        }
    }

    /**从顶点链表返回特定的VexNode 节点*/
    private static VexNode getVexNode(String nodeName){
        for(int i=0;i<xlist.size();i++){
            //System.out.println(xlist.get(i).nodeinfo.placeOrTransition);
            //System.out.println(node);
            //System.out.println(xlist.get(i).nodeinfo.placeOrTransition == node);
            if(xlist.get(i).getPlaceOrTransition().getName().equals(nodeName)){
                return xlist.get(i);
            }
        }
        return null;
    }

    /**打印十字链表,用于测试*/
    public static void printList(){
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
                    System.out.print("["+snum + "(" + xlist.get(snum).getPlaceOrTransition().getName() + ")-->");
                    System.out.print(tnum + "(" + xlist.get(tnum).getPlaceOrTransition().getName() + ")" + "]");
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
                    System.out.print("["+snum + "(" + xlist.get(snum).getPlaceOrTransition().getName() + ")-->");
                    System.out.print(tnum + "(" + xlist.get(tnum).getPlaceOrTransition().getName() + ")" + "]");
                    edgeNode = edgeNode.getSameTargetNext();
                }
                System.out.println();
            }else {
                System.out.println();
            }
        }
    }

}
