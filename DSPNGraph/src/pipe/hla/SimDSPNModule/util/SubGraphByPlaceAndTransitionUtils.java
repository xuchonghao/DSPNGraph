package pipe.hla.SimDSPNModule.util;


import pipe.hla.SimDSPNModule.basemodel.EdgeNode;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.basemodel.SubNet;
import pipe.hla.SimDSPNModule.basemodel.VexNode;
import pipe.hla.SimDSPNModule.basemodel.*;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.ArcNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.TransitionNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**TODO 把一个大的OLGraph划分为多个subGraph，并将其映射到多个联邦成员的工具类，步骤如下：
 * 1、构造一个AU图结构
 * 2、给边和节点赋权值
 * 3、获得符合metis函数的输入
 * 4、进行划分，并把结果赋值给每个节点和弧的分区标识
 * TODO 4.5 均衡的判断
 * 5、得到每个小分区的节点列表和弧列表
 * 6、构建k个小的Petri网===>ReConstructSubNet类中完善*/
public class SubGraphByPlaceAndTransitionUtils{
    public static OLGraph[] MainProcessOfPartition(OLGraph olGraph, int nparts){
        //1、构造一个AU图结构
        OLGraph auGraph = AUGraphHelper.buildAUGraph(olGraph);

        //2、给AU节点赋权值(边也得进行赋值---1)
        giveAUNodeAndArcWeight(olGraph, auGraph);

        //3、生成符合metis输入文件的输入
        MetisFormat metis = generateMetisInputFormat(auGraph,nparts);

        //4、使用metis中的函数进行划分,得到总的通信量和每个节点的划分区域 TODO 通信量待用
        JNICJAVA metispartition = new JNICJAVA();
        //TODO 这里需要对两节点之间的弧进行改进，权值加一
        int allComm = metispartition.METIS_PartGraphKway(metis.getNvtxs(),metis.getNcon(),metis.getXadj(),metis.getAdjncy(),nparts,metis.getPars());
        int[] partions = metis.getPars();//对应节点的划分区域值

        System.out.println(allComm);
        System.out.println(Arrays.toString(partions));
        //5、得到每个小分区的节点列表和弧列表
        SubNet[] subNets = getnPartsSubNet(olGraph, auGraph,nparts,partions);

        //6、构建k个小的Petri网===>ReConstructSubNet类中完善
        OLGraph[] petriNets = new OLGraph[nparts];
        for(int i=0;i<nparts;i++){
            SubNet net = subNets[i];
            petriNets[i] = new ReConstructSubNet().bulidSubGraphByVexAndArc(net);
            //System.out.println("------------------");
            //OLGraph.printListInSubNet(petriNets[i]);
        }

        return petriNets;
    }

    /**
     * 得到每个小分区的节点列表和弧列表 */
    private static SubNet[] getnPartsSubNet(OLGraph olGraph, OLGraph auGraph, int nparts,int[] partions) {
        //a、创建一个map：AUMarking和它所属于的分区
        HashMap<String,Integer> auPartionMap = new HashMap<>();
        ArrayList<VexNode> aulist = auGraph.getXlist();//它是按sequence的顺序进行排序的
        for(int i=0;i<aulist.size();i++){
            auPartionMap.put(aulist.get(i).getAUMarking(),partions[i]);
        }
        //b、给Petri网中每个节点的划分属性根据AUMarking赋分区的值
        ArrayList<VexNode> xlist = olGraph.getXlist();
        for(int i=0;i<xlist.size();i++){
            String auMarking = xlist.get(i).getAUMarking();
            String par = StringNameConstants.Par + auPartionMap.get(auMarking);
            //给节点VexNode设置分区号
            xlist.get(i).setBelongPartition(par);
            //在PetriNetNode层也设置分区号，为了之后使用
            xlist.get(i).getPlaceOrTransition().setPartitionNumber(auPartionMap.get(auMarking));
        }

        /* c、给弧赋划分区域的值，看属于哪个分区   共享弧，同一个弧出现在两个分区中
        弧的两端相同，在一个区域；若不同，在两个区域，划分区域为两者的组合*/
        ArrayList<ArcNode> arclist = olGraph.getArclist();
        for(int i=0;i<arclist.size();i++){
            ArcNode arcNode = arclist.get(i);
            String source = arcNode.get_source();
            System.out.println(source);
            VexNode sourceNode = olGraph.getVexNodeByName(source);
            String sourcePar = sourceNode.getBelongPartition();
            String targetPar = olGraph.getVexNodeByName(arclist.get(i).get_target()).getBelongPartition();
            if(sourcePar.equals(targetPar)){
                arcNode.setBelongPartition(sourcePar);
                arcNode.setBelongTwoPars(false);
            }
            else{
                arcNode.setBelongPartition(sourcePar + targetPar);
                arcNode.setBelongTwoPars(true);
            }
        }

        //d、生成nparts个节点列表和弧列表（其中属于两个区域的弧是重合的==》有两个）
        SubNet[] subNets = new SubNet[nparts];
        for(int i=0;i<nparts;i++){
            ArrayList<PlaceNode> placeNodes = new ArrayList<>();
            ArrayList<TransitionNode> transitionNodes = new ArrayList<>();
            ArrayList<ArcNode> arcNodes = new ArrayList<>();

            String par = StringNameConstants.Par + i;
            for(int j=0;j<xlist.size();j++){
                //如果分区相同  TODO 判断分区是不是从0开始的
                VexNode vexNode = xlist.get(j);
                if(vexNode.getBelongPartition().equals(par)){
                    //统计当前字网库所和变迁的数量
                    if(vexNode.getPlaceOrTransition() instanceof PlaceNode){
                        placeNodes.add((PlaceNode) xlist.get(j).getPlaceOrTransition());
                    }else if(vexNode.getPlaceOrTransition() instanceof TransitionNode){
                        transitionNodes.add((TransitionNode) xlist.get(j).getPlaceOrTransition());
                    }
                }
            }
            for(int j=0;j<arclist.size();j++){
                if(arclist.get(j).getBelongPartition().indexOf(par) != -1){
                    arcNodes.add(arclist.get(j));
                }
            }

            subNets[i] = new SubNet(placeNodes,transitionNodes,arcNodes);
        }
       /* for(int i=0;i<subNets.length;i++){
            System.out.println("------------------------------");
            ArrayList<PlaceNode> placeNodes = subNets[i].getPlaceNodes();
            for(PlaceNode  placeNode : placeNodes){
                System.out.println(placeNode.getName()+" , "+placeNode.getPartitionNumber());
            }
            ArrayList<TransitionNode> transitionNodes = subNets[i].getTransitionNodes();
            for(TransitionNode  transitionNode : transitionNodes){
                System.out.println(transitionNode.getName()+" , "+transitionNode.getPartitionNumber());
            }
            ArrayList<ArcNode> arcNodes = subNets[i].getArcNodes();
            for(ArcNode  arcNode : arcNodes){
                System.out.println(arcNode.getBelongPartition()+","+arcNode.get_source()+"---> "+arcNode.get_target());
            }
            System.out.println();
        }*/
        return subNets;
    }

    /**
     * 生成符合metis输入文件的输入
     * 这里是无向图*/
    private static MetisFormat generateMetisInputFormat(OLGraph auGraph,int npartsOfGraph) {
        int nvtxs = auGraph.getVexnum();
        int ncon = 1;

        int[] pars = new int[nvtxs];
        int[] xadj = new int[nvtxs+1];
        int[] adjncy = generateMetisInputFormat(auGraph,xadj);
        System.out.println(Arrays.toString(xadj));
        System.out.println(Arrays.toString(adjncy));
        return new MetisFormat(xadj, adjncy,nvtxs,ncon,pars);
    }

    /***
     * 生成adjncy
     */
    private static int[] generateMetisInputFormat(OLGraph auGraph,int[] xadj) {
        //生成adjncy==》划分按照sequence来进行
        ArrayList<VexNode> aulist = auGraph.getXlist();//它是按sequence的顺序进行排序的
        ArrayList<Integer> adjnvyList = new ArrayList<>();
        //adjncy数组的大小
        int size = 0;

        int i=0;
        for(;i<aulist.size();i++){//i 和 sequence 是同等的
            VexNode vexNode = aulist.get(i);
            System.out.println(i+" : "+vexNode.getAUMarking()+",sequence:"+vexNode.getSequence());
            HashSet<Integer> nodeSet = new HashSet<>();
            //a、获得输入节点的序号   TODO 这里报空指针错误
            HashSet<EdgeNode> inlist = auGraph.getAllInputArcNodesWithHash(vexNode);
            // 输入弧可能为null
            if(inlist != null && inlist.size() > 0){
                for(EdgeNode in : inlist){
                    nodeSet.add(in.getSourceIndex());
                }
            }

            //b、获得输出节点的序号
            HashSet<EdgeNode> outlist = auGraph.getAllOutputArcNodesWithHash(vexNode);
            //输出弧也可能为null
            if(outlist != null && outlist.size() > 0){
                for(EdgeNode out : outlist){
                    nodeSet.add(out.getTargetIndex());
                }
            }

            //c、给adjncy赋值，之后转换为数组
            for(int val : nodeSet){
                adjnvyList.add(val);
            }
            //d、给xadj赋值
            xadj[i] = size;//给xadj赋值
            size += nodeSet.size();
        }
        //最后一个
        xadj[i] = size;

        //转换为int数组
        int[] adjncy = new int[size];
        for(int j=0;j<adjnvyList.size();j++){
            adjncy[j] = adjnvyList.get(j);
        }
        return adjncy;
    }

    /**
     * 给AU节点赋权值*/
    public static void giveAUNodeAndArcWeight(OLGraph olGraph, OLGraph auGraph){
        //1、在HashMap中存储AU节点的AUMarking值和节点
        HashMap<String,VexNode> vexNodeAUMarkingMap = new HashMap<>();
        ArrayList<VexNode> aulist = auGraph.getXlist();
        for(int i=0;i<aulist.size();i++){
            vexNodeAUMarkingMap.put(aulist.get(i).getAUMarking(),aulist.get(i));
        }
        //2、遍历Petri网的图结构，根据AUMarking的数量为AU节点赋值
        ArrayList<VexNode> xlist = olGraph.getXlist();
        for(int i=0;i<xlist.size();i++){
            String petriNodeMarking = xlist.get(i).getAUMarking();
            if(vexNodeAUMarkingMap.get(petriNodeMarking) != null){
                VexNode vexNode = vexNodeAUMarkingMap.get(petriNodeMarking);
                int auweight = vexNode.getAUWeightOfVexNode();
                vexNode.setAUWeightOfVexNode(++auweight);
                vexNodeAUMarkingMap.put(petriNodeMarking,vexNode);
            }else{
                try {
                    throw new Exception("出现了AU不存在的AUMarking！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

//        for(int i=0;i<aulist.size();i++){
//           int weight = vexNodeAUMarkingMap.get(aulist.get(i).getAUMarking()).getAUWeightOfVexNode();
//            System.out.println(aulist.get(i).getAUMarking()+" : "+weight);
//        }
    }
}