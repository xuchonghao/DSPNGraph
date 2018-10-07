//queue of RTI callbacks

package pipe.hla.book.restaurant.transport;
import java.util.*;

public final class CallbackQueue {
  private Vector _list;

  public CallbackQueue() {
    _list = new Vector(10);
  }

  public synchronized void enqueue(Transport.Callback callback) {
    _list.addElement(callback);
    notifyAll();
  }

  public synchronized Transport.Callback dequeue() {
    Transport.Callback callback;
    while (_list.size() <= 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
    callback = (Transport.Callback)_list.elementAt(0);
    _list.removeElementAt(0);
    return callback;
  }
} 
