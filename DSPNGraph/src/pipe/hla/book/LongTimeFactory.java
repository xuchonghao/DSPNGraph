
package pipe.hla.book;

import hla.rti.*;
import java.io.*;

public final class LongTimeFactory implements LogicalTimeFactory {

  public LogicalTime decode(byte[] buffer, int offset)
  throws CouldNotDecode
  {
    try {
      ByteArrayInputStream bytes = new ByteArrayInputStream(
        buffer,
        offset,
        buffer.length - offset);
      ObjectInputStream objectStream = new ObjectInputStream(bytes);
      long value = objectStream.readLong();
      return new LongTime(value);
    }
    catch (Exception e) {
      throw new CouldNotDecode(e.getMessage());
    }
  }

  public LogicalTime makeInitial() {
    return new LongTime(LongTime._initial);
  }
}
