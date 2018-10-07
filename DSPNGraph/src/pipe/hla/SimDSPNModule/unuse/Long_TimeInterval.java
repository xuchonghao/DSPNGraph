package pipe.hla.SimDSPNModule.unuse;

import hla.rti1516.LogicalTimeInterval;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Long_TimeInterval implements LogicalTimeInterval {
    long _interval;
    static final long _epsilon = 1;
    private boolean _serializationIsCurrent = false;
    private byte[] _serialization;

    /**
     * @param interval double
     */
    public Long_TimeInterval (long interval) {
        _interval = interval;
        _serializationIsCurrent = false;
    }

    @Override
    public boolean isZero() {
        return _interval == 0;
    }

    @Override
    public boolean isEpsilon() {
        return _interval == _epsilon;
    }

    @Override
    public LogicalTimeInterval subtract(LogicalTimeInterval subtrahend) {
        _interval -= ((Long_TimeInterval)subtrahend)._interval;
        _serializationIsCurrent = false;
        return this;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public int encodedLength() {
        updateSerialization();
        return _serialization.length;
    }

    private void updateSerialization() {
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
            e.printStackTrace();
        }
    }

    @Override
    public void encode(byte[] buffer, int offset) {
        updateSerialization();
        System.arraycopy(_serialization,0,buffer,offset,_serialization.length);
    }

    public boolean isEqualTo(LogicalTimeInterval value) {
        return _interval == ((Long_TimeInterval)value)._interval;
    }
    public boolean isGreaterThan(LogicalTimeInterval value) {
        return _interval > ((Long_TimeInterval)value)._interval;
    }

    public boolean isGreaterThanOrEqualTo(LogicalTimeInterval value) {
        return _interval >= ((Long_TimeInterval)value)._interval;
    }

    public boolean isLessThan(LogicalTimeInterval value) {
        return _interval < ((Long_TimeInterval)value)._interval;
    }

    public boolean isLessThanOrEqualTo(LogicalTimeInterval value) {
        return _interval <= ((Long_TimeInterval)value)._interval;
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
    /**
     * 对比两个对象是不是相同，这个方法在Object被存入一个hashtable的时候才使用
     * Returns a boolean that indicates whether this Object is equivalent
     * to the specified Object. This method is used when an Object is stored
     * in a hashtable.
     * @param	obj	the Object to compare with
     * @return	true if these Objects are equal; false otherwise.
     * @see		java.util.Hashtable
     */
    public boolean equals(Object obj) {
        if (obj instanceof Long_TimeInterval)
            return _interval == ((Long_TimeInterval)obj)._interval;
        else {
            return false;
        }
    }
    /**
     * Returns a String that represents the value of this object.
     */
    public String toString() {
        return "LTI<" + Long.toString(_interval) + ">";
    }
    public void setInterval (long newInterval) {
        _interval = newInterval;
        _serializationIsCurrent = false;
    }
    public long getInterval ( ) {
        return _interval;
    }
    public void setTo(LogicalTimeInterval value) {
        _interval = ((Long_TimeInterval)value)._interval;
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




}
