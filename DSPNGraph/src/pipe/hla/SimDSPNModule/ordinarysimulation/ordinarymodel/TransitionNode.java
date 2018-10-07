package pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel;

import pipe.hla.SimDSPNModule.basemodel.VexNode;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import pipe.hla.SimDSPNModule.util.RandomGenerator;

import java.io.Serializable;
import java.util.ArrayList;


public class TransitionNode extends PetriNetNode implements Comparable<TransitionNode>,Serializable {

    /*对象实例自身状态*/
    protected boolean isEnabled;
    protected double delay;
    protected int type;
    protected double rate;
    protected boolean isConflict;
    protected ArrayList<VexNode> conflictTransition;
    protected double lastAdvancedTime;


    public TransitionNode() {
        System.out.println("尽量不要使用这个构造函数！");
        this.isEnabled = false;
        delay = 0;
        rate = 1;
        type = Constant.INSTANT_TRANSITION;
        conflictTransition = new ArrayList<>();
    }
    public TransitionNode(String name) {//id知道VexNode的序号
        super(name);
        /*默认值的构造*/
        this.isEnabled = false;
        delay = 0;
        rate = 1;
        type = Constant.INSTANT_TRANSITION;
        conflictTransition = new ArrayList<>();
        //TransitionNodeArray.add(this);

        //System.out.println("TransitionNode  Ok!!!");
    }



    public boolean isConflict() {
        return isConflict;
    }

    public void setConflict(boolean conflict) {
        isConflict = conflict;
    }

    public ArrayList<VexNode> getConflictTransition() {
        return conflictTransition;
    }

    public void setConflictTransition(ArrayList<VexNode> conflictTransition) {
        //conflictTransition包含所有的，这里去掉自身
//        int len = conflictTransition.size();
//        for(int i=0;i<len;i++){
//            TransitionNode transitionNode = (TransitionNode)conflictTransition.get(i).getPlaceOrTransition();
//            if(transitionNode == this){
//                conflictTransition.remove(i);
//                break;
//            }
//        }
        this.conflictTransition = conflictTransition;
    }

    public boolean isEnabled(){
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }


    public double getDelay() {
        return delay;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getLastAdvancedTime() {
        return lastAdvancedTime;
    }

    public void setLastAdvancedTime(double lastAdvancedTime) {
        this.lastAdvancedTime = lastAdvancedTime;
    }

    @Override
    public int compareTo(TransitionNode o) {
        return Double.compare(delay,o.delay);
    }

    //TODO
    public void resetDelay() {
        if(this.getType() == 0){
            this.setDelay(0);
        }else if(this.getType() == 1){
            //ms
            double delay = RandomGenerator.generate(rate) * 1000;
            this.setDelay(delay);
        }else{
            this.setDelay(this.rate);
        }
    }
}

