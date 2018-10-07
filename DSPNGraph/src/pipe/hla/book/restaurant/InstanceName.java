package pipe.hla.book.restaurant;

/** The attributes cargo and servingName are
* all names of object instances, so they are all represented as Strings.
*/
public final class InstanceName {

  /** Return new byte array with encoding of this instance.
  * Used to update attribute value or send a parameter.
  */
  public static byte[] encode(String instanceName) {
    return instanceName.getBytes();
  }


  /** Construct a new value from serialized version.
  * Used when reflecting an attribute value or receiving a parameter
  */
  public static String decode(byte[] buffer, int offset) throws hla.rti.CouldNotDecode
  {
    try {
      return new String(buffer, 0, buffer.length);
    }
    catch (Exception e) {
      throw new hla.rti.CouldNotDecode("InstanceName.decode: " + e.getMessage());
    }
  }
}
