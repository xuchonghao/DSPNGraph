//queue of RTI callbacks

package pipe.hla.book.restaurant.late_viewer;
import java.util.*;

public final class CallbackQueue {
  private Vector _list;

  public CallbackQueue() {
    _list = new Vector(10);
  }

  public synchronized void enqueue(LateViewer.Callback callback) {
    _list.addElement(callback);
    notifyAll();
  }

  public synchronized LateViewer.Callback dequeue() {
    LateViewer.Callback callback;
    while (_list.size() == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
    callback = (LateViewer.Callback)_list.elementAt(0);
    _list.removeElementAt(0);
    return callback;
  }
} 
