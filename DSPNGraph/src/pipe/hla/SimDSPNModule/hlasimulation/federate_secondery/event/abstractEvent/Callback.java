package pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.abstractEvent;

import hla.rti1516.RTIexception;

//represents one callback coming from RTI
//This class is defined within Production so the dispatch routines
//of its subclasses have the context of Production

/**不带仿真时间的RTI回调函数*/
public abstract class Callback {
    //returns true if event was a grant
    public abstract boolean dispatch() throws RTIexception;
}