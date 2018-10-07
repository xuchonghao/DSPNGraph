package pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.IOEvent;


import hla.rti1516.LogicalTime;
import hla.rti1516.RTIexception;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.MainLoopFed;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.abstractEvent.InternalEvent;

public final class EndEvent extends InternalEvent {
    MainLoopFed mainLoopFed;
    public EndEvent(LogicalTime time,MainLoopFed mainLoopFed) {
        _time = time;
        this.mainLoopFed = mainLoopFed;
    }
    public void dispatch() throws RTIexception
    {

    }
} //end FinishTokenSendEvent