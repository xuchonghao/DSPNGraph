package pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.auxiliary;

import hla.rti1516.LogicalTime;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.event.abstractEvent.InternalEvent;

import java.util.Vector;

public final class InternalQueue {
  private static String _newline = System.getProperty("line.separator");
  private Vector _list; //all elements are Production.InternalEvents

  public InternalQueue() {
    _list = new Vector(6);
  }

  public InternalEvent dequeue(){
    if (_list.size() > 0){//TODO 这个等于0对吗？
      InternalEvent event = (InternalEvent)_list.elementAt(0);
      _list.removeElementAt(0);
      return event;
    }else
      return null;
  }

  public InternalEvent peek() {
    if (_list.size() >= 0){
      InternalEvent event = (InternalEvent)_list.elementAt(0);
      //_list.removeElementAt(0);
      return event;
    }else
      return null;
  }


  public void enqueue(InternalEvent event) {
    int size = _list.size();
    if (size == 0) {
      _list.addElement(event);
    }
    else {
      for (int index = 0; index < size; ++index) {
        //事件的发生时间
        LogicalTime newEventTime = event.getTime();
        System.out.println(newEventTime.toString());
        //队列中存在一个事件，得到它的发生时间
        LogicalTime interListTime = ((InternalEvent)_list.elementAt(index)).getTime();
        System.out.println(interListTime.toString());
        //如果当前事件的发生时间小于队列中的那个事件的时间，将其插入该位置
        if(newEventTime.compareTo(interListTime) < 0){//如果新的事件比队列中的事件时间小
          System.out.println("InternalQueue插入了新的事件");
          _list.insertElementAt(event, index);
          return;
        }
      }
      _list.addElement(event);
    }
  }


  /**
   * 如果队列不为null，那么就返回队列中的队首元素
   * 若队列为null，那么返回最大值*/
  public LogicalTime getTimeAtHead()
  {
    if (_list.size() > 0) {
      return ((InternalEvent)_list.elementAt(0)).getTime();
    }
    else {
      LogicalTime infinity = new LogicalTimeDouble(Constant.InternalQueue_IS_NULL_TIME);

      return infinity;
    }
  }

  public String toString() {
    StringBuffer value = new StringBuffer("Queue:" + _newline);
    int size = _list.size();
    for (int index = 0; index < size; ++index) {
      InternalEvent event = (InternalEvent)_list.elementAt(index);
      value.append("  " + event.getTime());
      //value.append(",  chef:" + event.getChef() + _newline);
    }
    return value.toString();
  }
} 