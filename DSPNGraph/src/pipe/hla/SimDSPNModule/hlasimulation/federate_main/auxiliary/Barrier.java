
package pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary;

/**barrier和queue通过使用java的wait()方法让主线程休眠，直到满足这个条件
---以一种线程安全的形式封装了休眠条件并存储了与该条件相关的数据。
 barriers used to await announcement of synchronization points
 these must be allocated here lest announcement sneak up on us
 barrier和queue类用于协调主线程和回调线程！*/
public final class Barrier {
   boolean _set;
   Object[] _returnedValues;
   Object _suppliedValue;

   public Barrier() {
      _set = false;
      _suppliedValue = null;
   }

   public Barrier(Object suppliedValue) {
      _set = false;
      _suppliedValue = suppliedValue;
   }

   public synchronized void lower(Object[] returnedValues) {
      _returnedValues = returnedValues;
      _set = true;
      //awaken waiters
      notifyAll();
   }

   public synchronized Object[] await() {
      while (!_set) {//_set为false的时候，进入循环。默认为false
         try {
            wait();
         } catch (InterruptedException e) {
         }
      }
      _set = false;
      return _returnedValues;
   }

   public synchronized Object getSuppliedValue() {
    return _suppliedValue;
   }
}
