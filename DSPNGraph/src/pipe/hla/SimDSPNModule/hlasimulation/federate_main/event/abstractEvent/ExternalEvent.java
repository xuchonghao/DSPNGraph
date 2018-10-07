package pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.abstractEvent;

import hla.rti1516.LogicalTime;
import hla.rti1516.RTIexception;

//represents one callback that is an event (carries a time)

/**带仿真时间的RTI回调函数*/
public abstract class ExternalEvent extends Callback {
    protected LogicalTime _time;

    public LogicalTime getTime() { return _time; }
    //returns true if event was a grant
    public abstract boolean dispatch() throws RTIexception;
}