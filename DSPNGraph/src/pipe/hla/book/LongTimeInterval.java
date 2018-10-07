
package pipe.hla.book;

import hla.rti.*;
import java.io.*;
/**
 * Example implementation of LogicalTimeInterval
 * 
 */
public final class LongTimeInterval implements LogicalTimeInterval {
	long _interval;
   static final long _epsilon = 1;
	private boolean _serializationIsCurrent = false;
	private byte[] _serialization;

  /**
   * @param interval double
   */
  public LongTimeInterval (long interval) {
    _interval = interval;
    _serializationIsCurrent = false;
  }

  public void encode(byte[] buffer, int offset) {
    updateSerialization();
    System.arraycopy(
      _serialization, 
      0, 
      buffer, 
      offset,
      _serialization.length);
  }

  public int encodedLength() {
    updateSerialization();
    return _serialization.length;
  }
  /**
   * Compares two Objects for equality.
   * Returns a boolean that indicates whether this Object is equivalent 
   * to the specified Object. This method is used when an Object is stored
   * in a hashtable.
   * @param	obj	the Object to compare with
   * @return	true if these Objects are equal; false otherwise.
   * @see		java.util.Hashtable
   */
  public boolean equals(Object obj) {
    if (obj instanceof LongTimeInterval)
      return _interval == ((LongTimeInterval)obj)._interval;
    else {
      return false;
    }	
  }

  public long getInterval ( ) {
    return _interval;
  }
  /**
   * Generates a hash code for the receiver.
   * This method is supported primarily for hash tables, such as those provided in java.util.
   * @return	an integer hash code for the receiver
   * @see		java.util.Hashtable
   */
  public int hashCode() {
    return (new Long(_interval)).hashCode();
  }

  public boolean isEqualTo(LogicalTimeInterval value) {
    return _interval == ((LongTimeInterval)value)._interval;
  }

  public boolean isGreaterThan(LogicalTimeInterval value) {
    return _interval > ((LongTimeInterval)value)._interval;
  }

  public boolean isGreaterThanOrEqualTo(LogicalTimeInterval value) {
    return _interval >= ((LongTimeInterval)value)._interval;
  }

  public boolean isLessThan(LogicalTimeInterval value) {
    return _interval < ((LongTimeInterval)value)._interval;
  }

  public boolean isLessThanOrEqualTo(LogicalTimeInterval value) {
    return _interval <= ((LongTimeInterval)value)._interval;
  }

  public boolean isEpsilon() {
    return _interval == _epsilon;
  }

  public boolean isZero() {
    return _interval == 0;
  }

  public void setInterval (long newInterval) {
    _interval = newInterval;
    _serializationIsCurrent = false;
  }

  public void setTo(LogicalTimeInterval value) {
    _interval = ((LongTimeInterval)value)._interval;
    _serializationIsCurrent = false;
  }

  public void setEpsilon() {
    _interval = _epsilon;
    _serializationIsCurrent = false;
  }

  public void setZero() {
    _interval = 0;
    _serializationIsCurrent = false;
  }

  public LogicalTimeInterval subtract(LogicalTimeInterval subtrahend) {
    _interval -= ((LongTimeInterval)subtrahend)._interval;
    _serializationIsCurrent = false;
    return this;
  }

  /**
   * Returns a String that represents the value of this object.
   */
  public String toString() {
    return "LTI<" + Long.toString(_interval) + ">";
  }

  private void updateSerialization ( ) {
    if (_serializationIsCurrent) return;
    try {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      ObjectOutputStream objectStream =
        new ObjectOutputStream(bytes);
      objectStream.writeLong(_interval);
      objectStream.close();
      _serialization = bytes.toByteArray();
      _serializationIsCurrent = true;
    }
    catch (IOException e) {
    }		
  }
}
