package pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.abstractEvent;

import hla.rti1516.LogicalTime;
import hla.rti1516.RTIexception;

/**
 * represents one event on the internal queue
 * This class is defined within this federate so the dispatch routines
 * of its subclasses have the context of this federate
 * 内部事件，内部事件只有在联邦成员的仿真时间已经推进到该事件的时间才能进行处理
 * 与外部事件必须分开处理
 *
 * 内部事件只有在联邦成员的仿真时间已经推进到该事件的时间才进行处理*/
public abstract class InternalEvent {
    protected LogicalTime _time;
    public LogicalTime getTime() { return _time; }
    public abstract void dispatch() throws RTIexception;
}
