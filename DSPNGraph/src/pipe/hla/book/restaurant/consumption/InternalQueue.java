
package pipe.hla.book.restaurant.consumption;
import hla.rti.*;
import java.util.*;
import se.pitch.prti.*;

public final class InternalQueue {
	private static String _newline = System.getProperty("line.separator");
  private Vector _list; //all elements are Consumption.InternalEvents

  public InternalQueue() {
    _list = new Vector(6);
  }

  public Consumption.InternalEvent dequeue() {
    if (_list.size() >= 0) {
      Consumption.InternalEvent event = (Consumption.InternalEvent)_list.elementAt(0);
      _list.removeElementAt(0);
      return event;
    }
    else return null;
  }

  public void enqueue(Consumption.InternalEvent event) {
    int size = _list.size();
    if (size == 0) {
      _list.addElement(event);
    }
    else {
      for (int index = 0; index < size; ++index) {
        if (event.getTime().isLessThan(((Consumption.InternalEvent)_list.elementAt(index)).getTime())) {
          _list.insertElementAt(event, index);
          return;
        }
      }
      _list.addElement(event);
    }
  }

  public LogicalTime getTimeAtHead()
  throws InvalidFederationTime
  {
    if (_list.size() > 0) {
      return ((Consumption.InternalEvent)_list.elementAt(0)).getTime();
    }
    else {
      LogicalTime infinity = new LogicalTimeDouble(0.0);
      infinity.setFinal();
      return infinity;
    }
  }

  public String toString() {
    StringBuffer value = new StringBuffer("Queue:" + _newline);
    int size = _list.size();
    for (int index = 0; index < size; ++index) {
      Consumption.InternalEvent event = (Consumption.InternalEvent)_list.elementAt(index);
      value.append("  " + event.getTime());
      value.append(",  diner:" + event.getDiner() + _newline);
    }
    return value.toString();
  }
} 
