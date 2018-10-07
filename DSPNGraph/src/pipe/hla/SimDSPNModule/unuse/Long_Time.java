package pipe.hla.SimDSPNModule.unuse;

import hla.rti1516.IllegalTimeArithmetic;
import hla.rti1516.LogicalTime;
import hla.rti1516.LogicalTimeInterval;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Long_Time  implements LogicalTime {

    long _value;
    static final long _initial = 0;
    static final long _final = Long.MAX_VALUE;
    private boolean _serializationIsCurrent = false;
    private byte[] _serialization;

    /**
     * @param value long
     */
    public Long_Time (long value) {
        _value = value;
        _serializationIsCurrent = false;
    }

    public boolean isLessThan(LogicalTime value) {
        return _value < ((Long_Time)value)._value;
    }

    public boolean isLessThanOrEqualTo(LogicalTime value) {
        return _value <= ((Long_Time)value)._value;
    }

    public boolean isGreaterThan(LogicalTime value) {
        return _value > ((Long_Time)value)._value;
    }

    public boolean isGreaterThanOrEqualTo(LogicalTime value) {
        return _value >= ((Long_Time)value)._value;
    }
    public boolean isEqualTo(LogicalTime value) {
        return _value == ((Long_Time)value)._value;
    }

    @Override
    public boolean isInitial() {
        return _value == _initial;
    }

    @Override
    public boolean isFinal() {
        return _value == _final;
    }

    @Override
    public LogicalTime add(LogicalTimeInterval logicalTimeInterval) throws IllegalTimeArithmetic {

        return new Long_Time(_value + ((Long_TimeInterval)logicalTimeInterval).getInterval());

    }

    @Override
    public LogicalTime subtract(LogicalTimeInterval subtrahend) throws IllegalTimeArithmetic {

        return new Long_Time(_value - ((Long_TimeInterval)subtrahend).getInterval());
    }

    @Override//????
    public LogicalTimeInterval distance(LogicalTime logicalTime) {
        return null;
    }

    @Override
    public int compareTo(Object obj) {
        //if (obj instanceof Long_Time)
        return (int)(_value - ((Long_Time)obj)._value);
    }

    @Override
    public int encodedLength() {
        updateSerialization();
        return _serialization.length;
    }

    @Override
    public void encode(byte[] buffer, int offset) {
        updateSerialization();
        System.arraycopy(_serialization,0,buffer,offset,_serialization.length);
    }

    private void updateSerialization() {
        if (_serializationIsCurrent) return;
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(bytes);
            objectStream.writeLong(_value);
            objectStream.close();
            _serialization = bytes.toByteArray();
            _serializationIsCurrent = true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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
        _value = ((Long_Time)value)._value;
        _serializationIsCurrent = false;
    }

    public void setValue (long newValue) {
        _value = newValue;
        _serializationIsCurrent = false;
    }
    public long getValue ( ) {
        return _value;
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
        if (obj instanceof Long_Time)
            return _value == ((Long_Time)obj)._value;
        else {
            return false;
        }
    }
    public void increaseBy(LogicalTimeInterval addend)  throws IllegalTimeArithmetic
    {
        //it would be hard to exceed pos infinity
        _value += ((Long_TimeInterval)addend)._interval;
        _serializationIsCurrent = false;
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
    /**
     * Returns a String that represents the value of this object.
     */
    public String toString() {
        return "LT<" + Long.toString(_value) + ">";
    }
}
