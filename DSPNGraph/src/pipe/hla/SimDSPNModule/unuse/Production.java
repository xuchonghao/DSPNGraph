
package pipe.hla.SimDSPNModule.unuse;


public final class Production {
  /*

  private double _chefsReach;



  //the main thread 这里是主线程，实现联邦成员的生命周期和时间推进循环
  private void mainThread() {
      //先获取配置数据
      getConfigurationData();
      //Production federate makes initial instances as it makes chefs
      makeInitialInstances();
  }


  private void getConfigurationData()  throws InvalidLookahead
  {
    //get manfacture times for sushi types
       _manufactureTimes = new LogicalTimeIntervalDouble[_numberOfSushiTypes];
    for (int i = 0; i < _numberOfSushiTypes; ++i) {
      String timeString = getProperty("Federation.Sushi.meanManufactureTime."+i);
      double time = (new Double((timeString))).doubleValue();
      _manufactureTimes[i] = new LogicalTimeIntervalDouble(time);
    }
  }

  private void makeInitialInstances()  throws RTIexception
  {
    for (int serial = 0; serial < numberOfChefs; ++serial) {

      //update Chef attribute values

      LogicalTime sendTime = new LogicalTimeDouble(0.0);
      sendTime.setTo(_logicalTime);
      sendTime.increaseBy(_lookahead);

      EventRetractionHandle erh = rti.updateAttributeValues(chefHandle, suppliedAttributes, null, sendTime);

      //put event on internal queue
      LogicalTime eventTime = new LogicalTimeDouble(0.0);
      eventTime.setTo(_logicalTime);
      eventTime.increaseBy(_manufactureTimes[type]);

      //把一个FinishMakingSushiEvent事件放入内部事件队列中
      _internalQueue.enqueue(new FinishMakingSushiEvent(eventTime, serial, type));
    }
  }


  private void updateChefs() throws RTIexception
  {
    LogicalTime sendTime = new LogicalTimeDouble(0.0);
    sendTime.setTo(_logicalTime);
    sendTime.increaseBy(_lookahead);
  }


  private void updateServings()  throws RTIexception
  {
    LogicalTime sendTime = new LogicalTimeDouble(0.0);
    sendTime.setTo(_logicalTime);
    sendTime.increaseBy(_lookahead);
    Enumeration e = _servings.elements();
    while (e.hasMoreElements()) {
      Serving serving = (Serving)e.nextElement();
      SuppliedAttributes sa = _suppliedAttributesFactory.create(2);
      boolean needToUpdate = false;
      if (serving._positionState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(_positionAttribute, serving._position.encode());
        needToUpdate = true;
        serving._positionState = AttributeState.OWNED_CONSISTENT;
      }
      if (serving._typeState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(_typeAttribute, IntegerAttribute.encode(serving._type));
        needToUpdate = true;
        serving._typeState = AttributeState.OWNED_CONSISTENT;
      }
      if (needToUpdate) {
        try {
          EventRetractionHandle erh =
            _rti.updateAttributeValues(serving._handle, sa, null, sendTime);
        }
        catch (AttributeNotOwned ex) {
          //might lose ownership before the fact is recorded here
          _userInterface.post("Late update: " + ex);
        }
      }
    }
  }
*/
}
