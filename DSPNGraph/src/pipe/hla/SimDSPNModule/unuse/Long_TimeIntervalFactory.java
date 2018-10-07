
package pipe.hla.SimDSPNModule.unuse;


import hla.rti1516.CouldNotDecode;
import hla.rti1516.LogicalTimeInterval;
import hla.rti1516.LogicalTimeIntervalFactory;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public final class Long_TimeIntervalFactory implements LogicalTimeIntervalFactory {

  @Override
  public LogicalTimeInterval decode(byte[] buffer, int offset) throws CouldNotDecode {
    long value = 0;
    try {
      ByteArrayInputStream bytes = new ByteArrayInputStream(buffer,offset,buffer.length - offset);
      ObjectInputStream objectStream = new ObjectInputStream(bytes);
      value = objectStream.readLong();
    }catch (Exception e) {
      e.printStackTrace();
    }
    return new Long_TimeInterval(value);
  }

  @Override
  public LogicalTimeInterval makeZero() {
    return new Long_TimeInterval(0);
  }

  @Override
  public LogicalTimeInterval makeEpsilon() {
    return null;
  }

}
