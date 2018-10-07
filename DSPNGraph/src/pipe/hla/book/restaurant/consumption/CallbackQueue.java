//queue of RTI callbacks

package pipe.hla.book.restaurant.consumption;
import java.util.*;

public final class CallbackQueue {
  private Vector _list;

  public CallbackQueue() {
    _list = new Vector(10);
  }

  public synchronized void enqueue(Consumption.Callback callback) {
    _list.addElement(callback);
    notifyAll();
  }

  public synchronized Consumption.Callback dequeue() {
    Consumption.Callback callback;
    while (_list.size() == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
    callback = (Consumption.Callback)_list.elementAt(0);
    _list.removeElementAt(0);
    return callback;
  }

  public synchronized int size() { return _list.size(); }
} 
