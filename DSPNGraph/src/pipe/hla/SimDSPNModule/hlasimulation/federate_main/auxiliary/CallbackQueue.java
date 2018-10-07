//queue of RTI callbacks

package pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary;

import pipe.hla.SimDSPNModule.hlasimulation.federate_main.event.abstractEvent.Callback;

import java.util.Vector;

/**
 * 该数据结构必须是同步的，要保证线程间行为的协调，尤其是主线程和回调线程间的协调
 * 主线程需要等待RTI回调联邦成员
 * 所有的初始化服务和回调函数都通过回调函数队列处理*/
public final class CallbackQueue {
  private Vector _list;

  public CallbackQueue() {
    _list = new Vector(10);
  }

  public synchronized void enqueue(Callback event) {
    System.out.println(event);
    _list.addElement(event);
    notifyAll();
  }

  /**使主线程进入等待状态，直到取出一个Callback实例*/
  public synchronized Callback dequeue() {
    Callback event;
    while (_list.size() == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }
    event = (Callback)_list.elementAt(0);
    _list.removeElementAt(0);
    return event;
  }
} 
