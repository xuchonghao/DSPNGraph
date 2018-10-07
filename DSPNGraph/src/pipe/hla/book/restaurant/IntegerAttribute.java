package pipe.hla.book.restaurant;

/** Attributes type and state are integers. Ensure uniform
* representation across all federates.
*/
public final class IntegerAttribute {

  /** Return new byte array with encoding of this instance.
  * Used to update attribute value.
  */
  public static byte[] encode(int attr) {
    byte[] serialization = null;
    String s = Integer.toString(attr);
    serialization = s.getBytes();
    return serialization;
  }


  /** Construct a new value from serialized version.
  * Used when reflecting an attribute value
  */
  public static int decode(byte[] buffer, int offset)
  throws hla.rti.CouldNotDecode
  {
    try {
      String s = new String(buffer, 0, buffer.length);
      return Integer.parseInt(s);
    }
    catch (Exception e) {
      throw new hla.rti.CouldNotDecode("IntegerAttribute.decode ("
        + new String(buffer, 0, buffer.length) + "): " + e.getMessage());
    }
  }
}
