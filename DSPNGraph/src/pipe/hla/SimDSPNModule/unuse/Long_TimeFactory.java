
package pipe.hla.SimDSPNModule.unuse;

import hla.rti1516.CouldNotDecode;
import hla.rti1516.LogicalTime;
import hla.rti1516.LogicalTimeFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public final class Long_TimeFactory implements LogicalTimeFactory {


  @Override
  public LogicalTime decode(byte[] buffer, int offset) throws CouldNotDecode {
    try {
      ByteArrayInputStream bytes = new ByteArrayInputStream(buffer,offset,buffer.length - offset);
      ObjectInputStream objectStream = new ObjectInputStream(bytes);
      long value = objectStream.readLong();
      return new Long_Time(value);
    }
    catch (Exception e) {
      throw new CouldNotDecode(e.getMessage());
    }
  }

  @Override
  public LogicalTime makeInitial() {
    return new Long_Time(Long_Time._initial);
  }

  @Override
  public LogicalTime makeFinal() {
    return new Long_Time(Long_Time._final);
  }
}
