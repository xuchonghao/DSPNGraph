package pipe.hla.SimDSPNModule.basemodel;

import java.util.ArrayList;

public class Temp {

    private ArrayList<VexNode> xlist = new ArrayList<>();
    /**获得某个节点（变迁类型）的所有输出节点*/
    public ArrayList<VexNode> getAllOutputVexNodes(VexNode vexNode){
        ArrayList<VexNode> outPutVexNodes = new ArrayList<>();
        if(vexNode.getFirstOut()!=null){
            EdgeNode edgeNode = vexNode.getFirstOut();
            int tnum = edgeNode.getTargetIndex();
            outPutVexNodes.add(xlist.get(tnum));
            while (edgeNode.getSameSourceNext() != null){
                edgeNode = edgeNode.getSameSourceNext();
                tnum = edgeNode.getTargetIndex();
                outPutVexNodes.add(xlist.get(tnum));
            }
            return outPutVexNodes;
        }
        return null;
    }

    /**获得某个节点（变迁类型）的所有输出节点
    public HashSet<TransitionView> getAllOutputVexNodesWithHash(VexNode vexNode){
        HashSet<TransitionView> outPutVexNodes = new HashSet<>();
        if(vexNode.firstOut!=null){
            EdgeNode edgeNode = vexNode.firstOut;
            outPutVexNodes.add((TransitionView)edgeNode.getHeadVexNode().getNodeinfo().getPlaceOrTransition());
            while (edgeNode.getSameTailNext() != null){
                edgeNode = edgeNode.getSameTailNext();
                outPutVexNodes.add((TransitionView)edgeNode.getHeadVexNode().getNodeinfo().getPlaceOrTransition());
            }
            return outPutVexNodes;
        }
        return null;
    }*/

    /** 获得某个节点的所有输入节点
    public ArrayList<VexNode> getAllInputVexNodes(VexNode vexNode){
        ArrayList<VexNode> inputVexNodes = new ArrayList<>();
        //若当前节点是一个变迁,返回它的输入节点
        if(vexNode.firstIn != null){
            //获得以当前节点为弧头的弧
            EdgeNode edgeNode = vexNode.firstIn;
            inputVexNodes.add(edgeNode.getTailVexNode());
            //System.out.println(edgeNode.tailVexNode.nodeId);
            while(edgeNode.getSameHeadNext() != null){
                //获得该弧的输入就是其中的一个输入库所
                edgeNode = edgeNode.getSameHeadNext();
                inputVexNodes.add(edgeNode.getTailVexNode());
                //System.out.println(edgeNode.tailVexNode.nodeId);
            }
            return inputVexNodes;
        }
        //若不是变迁类型，返回NULL
        return null;
    }*/
    /**根据变迁返回OLGraph中的顶点
    public VexNode getVexNodeBasedTransitionView(TransitionView transitionView) {
        int len = xlist.size();
        int i = 0;
        while (i < len){
            if(xlist.get(i).getNodeinfo().getPlaceOrTransition().getId().equals(transitionView.getId())){
                return  xlist.get(i);
            }
            i++;
        }
        return null;
    }*/

        /*
    public ArrayList<VexNode> getAllVexNodesById(ArrayList<String> names) {
        ArrayList<VexNode> arr = new ArrayList<>();
        for(int i=0;i<names.size();i++){
            String str = names.get(i);
            arr.add(getVexNodesById(str));
        }
        return arr;
    }

    private VexNode getVexNodesById(String str) {
        for(int i=0;i<xlist.size();i++){
            if(xlist.get(i).getNodeId().equals(str))
                return xlist.get(i);
        }
        return null;
    }*/
}
