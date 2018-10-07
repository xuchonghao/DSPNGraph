package pipe.hla.SimDSPNModule.basemodel;

import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.ArcNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceNode;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.TransitionNode;

import java.util.ArrayList;

public class SubNet {
    private ArrayList<PlaceNode> placeNodes = new ArrayList<>();
    private ArrayList<TransitionNode> transitionNodes = new ArrayList<>();
    private ArrayList<ArcNode> arcNodes ;


    public SubNet(ArrayList<PlaceNode> placeNodes, ArrayList<TransitionNode> transitionNodes, ArrayList<ArcNode> arcNodes) {
        this.placeNodes = placeNodes;
        this.transitionNodes = transitionNodes;
        this.arcNodes = arcNodes;
    }

    public ArrayList<PlaceNode> getPlaceNodes() {
        return placeNodes;
    }

    public void setPlaceNodes(ArrayList<PlaceNode> placeNodes) {
        this.placeNodes = placeNodes;
    }

    public ArrayList<TransitionNode> getTransitionNodes() {
        return transitionNodes;
    }

    public void setTransitionNodes(ArrayList<TransitionNode> transitionNodes) {
        this.transitionNodes = transitionNodes;
    }

    public ArrayList<ArcNode> getArcNodes() {
        return arcNodes;
    }

    public void setArcNodes(ArrayList<ArcNode> arcNodes) {
        this.arcNodes = arcNodes;
    }
}
