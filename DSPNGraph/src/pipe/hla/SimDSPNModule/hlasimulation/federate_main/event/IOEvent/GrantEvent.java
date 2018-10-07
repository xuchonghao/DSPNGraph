package pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.IOEvent;

import hla.rti1516.LogicalTime;

import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.MainLoopFed;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.abstractEvent.ExternalEvent;

public final class GrantEvent extends ExternalEvent {

    MainLoopFed mainLoopFed;
    OLGraph subGraph;
    public GrantEvent( OLGraph subGraph,MainLoopFed mainLoopFed,LogicalTime time) {
        this.mainLoopFed = mainLoopFed;
        this._time = time;
        this.subGraph = subGraph;
    }

    //this dispatch doesn't do as much as the others because actions upon
    //grant are handled differently.
    /**对每类回调函数的处理的方法，返回Boolean值，当事件与TIME ADVANCE GRANT' 相关的时候返回真*/
    public boolean dispatch() {
        //System.out.println("GrantEvent的dispatch中的时间值："+_time.toString()+"，当前值："+ mainLoopFed.get_logicalTime().toString()+"如果是从外界接收到的消息，后面的时间应该小");
        mainLoopFed.set_logicalTime(_time);
        subGraph.advanceTime(_time);
        return true;
    }
} //end GrantEvent
