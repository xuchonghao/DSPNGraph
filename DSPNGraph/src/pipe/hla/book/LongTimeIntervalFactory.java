
package pipe.hla.book;

import hla.rti.*;
import java.io.*;

public final class LongTimeIntervalFactory implements LogicalTimeIntervalFactory {

  public LogicalTimeInterval decode(byte[] buffer, int offset)
  {
    long value = 0;
    try {
      ByteArrayInputStream bytes = new ByteArrayInputStream(
        buffer,
        offset,
        buffer.length - offset);
      ObjectInputStream objectStream = new ObjectInputStream(bytes);
      value = objectStream.readLong();
    }
    catch (Exception e) {
    }
    return new LongTimeInterval(value);
  }

  public LogicalTimeInterval makeZero() {
    return new LongTimeInterval(0);
  }
}
