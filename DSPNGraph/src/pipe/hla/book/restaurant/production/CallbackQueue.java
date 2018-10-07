//queue of RTI callbacks

package pipe.hla.book.restaurant.production;

import java.util.*;

public final class CallbackQueue {
  private Vector _list;

  public CallbackQueue() {
    _list = new Vector(10);
  }

  public synchronized void enqueue(Production.Callback event) {
    _list.addElement(event);
    notifyAll();
  }

  /**使主线程进入等待状态，知道取出一个Production.Callback实例*/
  public synchronized Production.Callback dequeue() {
    Production.Callback event;
    while (_list.size() == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
    event = (Production.Callback)_list.elementAt(0);
    _list.removeElementAt(0);
    return event;
  }
} 
