package pipe.hla.book.restaurant.production;
import hla.rti.*;
import java.util.*;
import se.pitch.prti.*;

public final class InternalQueue {
	private static String _newline = System.getProperty("line.separator");
  private Vector _list; //all elements are Production.InternalEvents

  public InternalQueue() {
    _list = new Vector(6);
  }

  public Production.InternalEvent dequeue() {
    if (_list.size() >= 0) {
      Production.InternalEvent event = (Production.InternalEvent)_list.elementAt(0);
      _list.removeElementAt(0);
      return event;
    }
    else return null;
  }

  public void enqueue(Production.InternalEvent event) {
    int size = _list.size();
    if (size == 0) {
      _list.addElement(event);
    }
    else {
      for (int index = 0; index < size; ++index) {
        if (event.getTime().isLessThan(((Production.InternalEvent)_list.elementAt(index)).getTime())) {
          _list.insertElementAt(event, index);
          return;
        }
      }
      _list.addElement(event);
    }
  }

  public LogicalTime getTimeAtHead()  throws InvalidFederationTime
  {
    if (_list.size() > 0) {
      return ((Production.InternalEvent)_list.elementAt(0)).getTime();
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
      Production.InternalEvent event = (Production.InternalEvent)_list.elementAt(index);
      value.append("  " + event.getTime());
      value.append(",  chef:" + event.getChef() + _newline);
    }
    return value.toString();
  }
} 