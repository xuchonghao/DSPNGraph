
package pipe.hla.book;

import hla.rti.*;
import java.io.*;
/**
 * Time implementation that uses long.
 * 
 */
public final class LongTime implements LogicalTime {
	long _value;
	static final long _initial = 0;
	static final long _final = Long.MAX_VALUE;
	private boolean _serializationIsCurrent = false;
	private byte[] _serialization;

  /**
   * @param value long
   */
  public LongTime (long value) {
    _value = value;
    _serializationIsCurrent = false;
  }

  public void decreaseBy(LogicalTimeInterval subtrahend)
  throws IllegalTimeArithmetic 
  {
    long candidate = _value - ((LongTimeInterval)subtrahend)._interval;
    if (candidate < _initial) throw new IllegalTimeArithmetic("Result cannot be less than initial value.");
    _value = candidate;
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
  public boolean equals(Object obj)
  {
    if (obj instanceof LongTime)
      return _value == ((LongTime)obj)._value;
    else {
      return false;
    }	
  }

  public long getValue ( ) {
    return _value;
  }

  /**
   * Generates a hash code for the receiver.
   * This method is supported primarily for hash tables, such as those provided in java.util.
   * @return	an integer hash code for the receiver
   * @see		java.util.Hashtable
   */
  public int hashCode() {
    return (new Long(_value)).hashCode();
  }

  public void increaseBy(LogicalTimeInterval addend)
  throws IllegalTimeArithmetic
  {
    //it would be hard to exceed pos infinity
    _value += ((LongTimeInterval)addend)._interval;
    _serializationIsCurrent = false;
  }

  public boolean isEqualTo(LogicalTime value) {
    return _value == ((LongTime)value)._value;
  }

  public boolean isFinal() {
    return _value == _final;
  }

  public boolean isGreaterThan(LogicalTime value) {
    return _value > ((LongTime)value)._value;
  }

  public boolean isGreaterThanOrEqualTo(LogicalTime value) {
    return _value >= ((LongTime)value)._value;
  }

  public boolean isInitial() {
    return _value == _initial;
  }

  public boolean isLessThan(LogicalTime value) {
    return _value < ((LongTime)value)._value;
  }

  public boolean isLessThanOrEqualTo(LogicalTime value) {
    return _value <= ((LongTime)value)._value;
  }

  public void setFinal() {
    _value = _final;
    _serializationIsCurrent = false;
  }

  public void setInitial() {
    _value = _initial;
    _serializationIsCurrent = false;
  }

  public void setTo(LogicalTime value) {
    _value = ((LongTime)value)._value;
    _serializationIsCurrent = false;
  }

  public void setValue (long newValue) {
    _value = newValue;
    _serializationIsCurrent = false;
  }

  public LogicalTimeInterval subtract(LogicalTime subtrahend) {
    return new LongTimeInterval(
      _value - ((LongTime)subtrahend)._value);
  }

  /**
   * Returns a String that represents the value of this object.
   */
  public String toString() {
    return "LT<" + Long.toString(_value) + ">";
  }

  private void updateSerialization ( ) {
    if (_serializationIsCurrent) return;
    try {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      ObjectOutputStream objectStream =
        new ObjectOutputStream(bytes);
      objectStream.writeLong(_value);
      objectStream.close();
      _serialization = bytes.toByteArray();
      _serializationIsCurrent = true;
    }
    catch (IOException e) {
    }		
  }
}
