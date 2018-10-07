package pipe.hla.SimDSPNModule.util;

import pipe.hla.SimDSPNModule.basemodel.EdgeNode;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.basemodel.VexNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.ArcNode;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * 1、为每一个节点和弧添加AU变量、权重变量、分区变量
 * 2、从Petri网的图中抽象出节点列表、弧列表即可
 * 3、构造一个AU图结构*/
public class AUGraphHelper {
    /**
     * 从Petri网的图中抽象出节点列表、弧列表,然后构造一个AU图结构
     * 对每个变迁：
     * 1、一个变迁和一个库所成环的特殊结构
     * 2、变迁前面的弧，isAUEdge设为false；变迁后面的弧，isAUEdge设为true；
     * 3、创建两个数组，容纳新的AU节点和新的AU弧
     * 4、创建AU图*/
    public static OLGraph buildAUGraph(OLGraph olGraph){
        OLGraph auGraph = new OLGraph();
        //一、生成节点列表
        ArrayList<VexNode> AUNodeList = buildVexNodeList(olGraph,auGraph);
//        for(VexNode vexNode : AUNodeList){
//            System.out.println("vexNode:"+vexNode.getAUMarking());
//        }
        //二、生成弧的列表
        ArrayList<ArcNode> AUArcList = buildArcNodeList(olGraph,auGraph);
//        for(ArcNode arcNode : AUArcList){
//            System.out.println(arcNode.getAUSource()+"-->"+arcNode.getAUTarget());
//        }

        //三、利用节点列表和弧的列表创建AU图
        buildAUGraphByAUList(AUNodeList,AUArcList,auGraph);
        //OLGraph.printAUMarkingList(auGraph);
        return auGraph;
    }

    /**利用节点列表和弧的列表创建AU图
     * VexNode已经创建好了，还差EdgeNode*/
    private static void buildAUGraphByAUList(ArrayList<VexNode> auNodeList, ArrayList<ArcNode> auArcList, OLGraph auGraph) {
        //1、VexNode已经创建好了
        //2、创建EdgeNode
        for(int i=0;i<auArcList.size();i++){
            ArcNode arcNode = auArcList.get(i);
            //boolean type = arcNode.getType().equals("AU");
            //System.out.println("type:"+type);
            //将ArcNode转换为EdgeNode
            transferEdge(auGraph,arcNode);
        }
        //3、设置边的suze、au节点的size
        auGraph.setEdgenum(auArcList.size());
        auGraph.setVexnum(auNodeList.size());
    }

    /**将ArcNode转换为EdgeNode*/
    private static void transferEdge(OLGraph auGraph,ArcNode arcNode) {
        //1、获得该弧的源节点和目的节点      此时已经修改了arcNode所连接节点的名字
        VexNode snode = auGraph.getVexNodeByAUName(arcNode.getAUSource());
        VexNode tnode = auGraph.getVexNodeByAUName(arcNode.getAUTarget());

        //2、创建节点的输出链表（同尾部）
        EdgeNode arcBox1 = new EdgeNode();
        arcBox1.setCurrentArc(arcNode);
        //arcBox1.setOrdinaryArc(type);//设不设置无所谓吧，这个在token数量变化的时候有用
        //序号在节点中已经设置了，所以可以直接用
        arcBox1.setTargetIndex(tnode.getSequence());
        arcBox1.setSourceIndex(snode.getSequence());

        //TODO 如果这里是true的话，可以删除
        //VexNode sourceNode = auGraph.getXlist().get(snode.getSequence());
       // System.out.println("如果这里是true的话，可以删除！不一样的话就出错了，回来修改："+(sourceNode == snode));

        if(snode.getFirstOut() == null)
            snode.setFirstOut(arcBox1);
        else
            linkLastTailNext(snode.getFirstOut(),arcBox1);

        //3、创建节点的输入链表（同头部的）
        EdgeNode arcBox2 = new EdgeNode();
        //arcBox2.setOrdinaryArc(type);
        arcBox2.setCurrentArc(arcNode);
        arcBox2.setTargetIndex(tnode.getSequence());
        arcBox2.setSourceIndex(snode.getSequence());

        if(tnode.getFirstIn() == null)
            tnode.setFirstIn(arcBox2);
        else
            linkLastHeadNext(tnode.getFirstIn(),arcBox2);
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


    /**
     * 生成弧的列表*/
    private static ArrayList<ArcNode> buildArcNodeList(OLGraph olGraph,OLGraph auGraph) {
        ArrayList<ArcNode> AUArcs = auGraph.getArclist();
        ArrayList<ArcNode> allArcs = olGraph.getArclist();
        for(ArcNode arc : allArcs){
            //如果这个弧是AU中的弧
            if(arc.isAUEdge()) {
                //获得弧的源节点和目的节点的名字
                System.out.println(arc.get_source() + ":" + arc.get_target());
                String sourceAUMarking = olGraph.getVexNodeByName(arc.get_source()).getAUMarking();

                String targetName = arc.get_target();
                VexNode targetPlace =  olGraph.getVexNodeByName(targetName);
                String targetAUMarking = targetPlace.getAUMarking();

                // 修改arc的源节点和目的节点的String名字为 AU的序号  添加了两个AU的属性
                arc.setAUSource(sourceAUMarking);
                arc.setAUTarget(targetAUMarking);
                System.out.println(arc.getAUSource() + ":" + arc.getAUTarget());

                //TODO 可以在这里给AU权值赋值,暂时都赋值为1，之后有需要可以修改
                arc.setAUWeightOfEdge(1);

                //添加到弧列表中
                AUArcs.add(arc);
            }
        }
        return AUArcs;
    }

    /**
     * 生成节点列表*/
    private static ArrayList<VexNode> buildVexNodeList(OLGraph olGraph,OLGraph auGraph) {
        ArrayList<VexNode> AUNodes = auGraph.getXlist();//AU 图的节点链表
        ArrayList<VexNode> xlist = olGraph.getXlist();

        int placeNum = olGraph.getPlacenum();
        for(int i=placeNum;i<xlist.size();i++){//对每一个变迁做处理
            VexNode vexNode = xlist.get(i);
            if(vexNode.isTransition()){//当前节点是变迁
                //System.out.println(vexNode.getPlaceOrTransition().getName());
                VexNode[] placeNode = new VexNode[1];
                //1、特殊环结构判断  TODO 这里我们假设只能存在一种这样的结构，多个循环的话不处理
                boolean isSpecial = dealSpecialCycleStructure(olGraph,vexNode,placeNode,AUNodes);
                if(isSpecial)
                    continue;
                //2、对变迁前面的弧处理，并设置当前的节点的AU序号值         得到当前变迁的所有输入弧，改变状态，然后得到所有输入库所  设置AU序号
                HashSet<EdgeNode> inset = olGraph.getAllInputArcNodesWithHash(vexNode);
                String AUMarking = StringNameConstants.AU+StringNameConstants.getAUSequence();
                //System.out.println(AUMarking);
                for(EdgeNode in : inset){
                    in.getCurrentArc().setAUEdge(false);//内部弧，设置为false
                    int sourceIndex = in.getSourceIndex();
                    VexNode inPlace = xlist.get(sourceIndex);
                    //System.out.println(inPlace.getPlaceOrTransition().getName());
                    //如果输入库所已经有了AU编号，那么目前所有相关的节点都重新设置为这个编号；否则设置新的编号
                    if(null != inPlace.getAUMarking() && !AUMarking.equals(inPlace.getAUMarking())){ //TODO 当两个AU冲突的时候，进行合并=》都设置为之前的AU
                        AUMarking = inPlace.getAUMarking();//修改AU，后面设置变迁的AU编号也为这个
                        //之前已有了AU编号，现在将当前变迁所有的输入库所设置为已有的AU
                        reSetVexNodeAUMarking(inset,AUMarking,olGraph);
                        vexNode.setAUMarking(AUMarking);//设置变迁节点的AU编号
                        break;//跳出这个循环
                    }else{//如果还没有设置，或者设置了，和当前的一样
                        inPlace.setAUMarking(AUMarking);
                    }
                }//end for in
                vexNode.setAUMarking(AUMarking);//最后设置变迁节点的AU编号

                //3、对变迁后面的弧处理   TODO 我们假设后面的库所不会是单个的，这里的处理可能是多余的
                HashSet<EdgeNode> outset = olGraph.getAllOutputArcNodesWithHash(vexNode);
                if(outset.size() > 0){
                    for(EdgeNode out : outset)//输出弧都是AU之间的弧
                        out.getCurrentArc().setAUEdge(true);
                }

                //4、创建一个新的AU节点并添加到AUNodes中 TODO 权重没赋值
                AUNodes.add(new VexNode(AUMarking));
            }else {//下面else中的这一段没用，除非OLGraph设置节点的时候出错了
                try {
                    throw new Exception("这里不可能不是变迁！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }// end for i  到这里，为每个变迁以及前面的库所都设置了AU，弧都设置了 是否为内部弧的标识

        //5 给节点的sequence属性设值==》这里必须从0开始，且得连续
        for(int sequence=0;sequence<AUNodes.size();sequence++)
            AUNodes.get(sequence).setSequence(sequence);

        return AUNodes;
    }

    /**
     * 处理一开始的特殊结构（库所和变迁之间的环），输入是某个变迁节点*/
    private static boolean dealSpecialCycleStructure(OLGraph olGraph,VexNode vexNode ,VexNode[] placeNodes,ArrayList<VexNode> AUNodes){
        boolean res = false;
        HashSet<EdgeNode> inset = olGraph.getAllInputArcNodesWithHash(vexNode);
        // boolean res = false;
        for(EdgeNode in : inset){//变迁的输入弧
            //获得该弧的输入库所,判断输入库所后面的弧有没有该变迁
            int sourceIndex = in.getSourceIndex();
            VexNode placeOfSource = olGraph.getXlist().get(sourceIndex);//输入弧的源==》库所

            HashSet<EdgeNode> outset = olGraph.getAllOutputArcNodesWithHash(vexNode);
            //如果某个变迁没有输出弧，那么不用设置,那么肯定不是特殊的结构
            if(outset.size() == 0)
                return false;

            for(EdgeNode out : outset){
                int targetIndex = out.getTargetIndex();
                VexNode placeOfTarget = olGraph.getXlist().get(targetIndex);
                if(placeOfTarget.equals(placeOfSource)){//如果一个弧的输入库所和输出库所是同一个
                    //res = true;
                    System.out.println(placeOfSource.getPlaceOrTransition().getName());
                    System.out.println(placeOfTarget.getPlaceOrTransition().getName());
                    System.out.println(placeOfTarget+"是特殊结构");
                    placeNodes[0] = placeOfSource;
                    //如果是特殊结构的话对弧进行处理，两条弧的isAUEdge都设为false；
                    in.getCurrentArc().setAUEdge(false);
                    out.getCurrentArc().setAUEdge(false);
                    //设置AU标识,TODO 这里运行一次就结束了=》是的
                    String AUMarking = StringNameConstants.AU+StringNameConstants.getAUSequence();
                    //TODO 这里设置AUMarking的时候也要进行检查，是否进行了设置
                    //placeOfSource.setAUMarking(AUMarking);
                    //如果输入库所已经有了AU编号，那么目前所有相关的节点都重新设置为这个编号；否则设置新的编号
                    if(null != placeOfSource.getAUMarking() && !AUMarking.equals(placeOfSource.getAUMarking())){ //TODO 当两个AU冲突的时候，进行合并=》都设置为之前的AU
                        AUMarking = placeOfSource.getAUMarking();//修改AU，后面设置变迁的AU编号也为这个
                        //之前已有了AU编号，现在将当前变迁所有的输入库所设置为已有的AU
                        reSetVexNodeAUMarking(inset,AUMarking,olGraph);
                        vexNode.setAUMarking(AUMarking);
                        //添加这个AU节点到AUNodes中，如果之前已经有了AU编号，此时就不能再创建一个同样AU编号的节点了
                        //AUNodes.add(new VexNode(AUMarking));
                        res = true;
                        continue;//跳出这个循环
                    }else{//如果还没有设置，或者设置了，和当前的一样
                        placeOfSource.setAUMarking(AUMarking);
                    }
                    //当前节点也设置为已有的AUMarking
                    vexNode.setAUMarking(AUMarking);

                    //添加这个AU节点到AUNodes中
                    AUNodes.add(new VexNode(AUMarking));
                    res = true;
                }else if(!placeOfTarget.equals(placeOfSource)){
                    //把别的不是这种结构的弧设置为AU图中的弧
                    System.out.println(placeOfSource.getPlaceOrTransition().getName());
                    System.out.println(placeOfTarget.getPlaceOrTransition().getName());
                    //前当前是特殊结构，并且这条边之前被设置为false（之前设置过了这个输出弧为false，防止再被改变）
                    if((res == true) && (out.getCurrentArc().isAUEdge() == false)){

                    }else //如果不是特殊结构的内部双向的那个输出弧，设置为true
                        out.getCurrentArc().setAUEdge(true);//TODO 后面还有一次设置，不知能不能保证
                }
            }
        }
        return res;
    }


    /**重新设置变迁输入库所的AUMarking*/
    private static void reSetVexNodeAUMarking(HashSet<EdgeNode> inset, String auMarking,OLGraph olGraph) {
        for(EdgeNode in : inset){
            in.getCurrentArc().setAUEdge(false);//此处的弧也设置为false
            int sourceIndex = in.getSourceIndex();
            VexNode inPlace = olGraph.getXlist().get(sourceIndex);
            inPlace.setAUMarking(auMarking);
        }
    }
}
