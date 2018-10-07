//queue of RTI callbacks

package pipe.hla.book.restaurant.viewer;
import java.util.*;

public final class CallbackQueue {
  private Vector _list;

  public CallbackQueue() {
    _list = new Vector(10);
  }

  public synchronized void enqueue(Viewer.Callback callback) {
    _list.addElement(callback);
    notifyAll();
  }

  public synchronized Viewer.Callback dequeue() {
    Viewer.Callback callback;
    while (_list.size() == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
    callback = (Viewer.Callback)_list.elementAt(0);
    _list.removeElementAt(0);
    return callback;
  }
} 
