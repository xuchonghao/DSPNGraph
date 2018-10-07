package pipe.hla.book.restaurant;

/** The type attribute is a boolean. Defining the class ensures a uniform
* representation across all federates.
*/
public final class SpaceAvailable {

  /** Return new byte array with encoding of this instance.
  * Used to update attribute value.
  */
  public static byte[] encode(boolean spaceAvailable) {
    byte[] serialization = null;
    /*
    try {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      ObjectOutputStream objectStream =
        new ObjectOutputStream(bytes);
      objectStream.writeBoolean(spaceAvailable);
      objectStream.close();
      serialization = bytes.toByteArray();
    }
    catch (Exception e) {
    }
    */
    //the foregoing elegant canonical Java serialization has been
    //eschewed in favor of something one can type into the test federate!
    String s = spaceAvailable ? "true" : "false";
    serialization = s.getBytes();
    return serialization;
  }


  /** Construct a new value from serialized version.
  * Used when reflecting an attribute value
  */
  public static boolean decode(byte[] buffer, int offset)
  throws hla.rti.CouldNotDecode
  {
    try {
    /*
      ByteArrayInputStream bytes = new ByteArrayInputStream(
        buffer,
        offset,
        buffer.length - offset);
      ObjectInputStream objectStream = new ObjectInputStream(bytes);
      return objectStream.readBoolean();
    */
      //the foregoing elegant canonical Java serialization has been
      //eschewed in favor of something one can type into the test federate!
      String s = new String(buffer, 0, buffer.length);
      if (s.charAt(0) == 't' || s.charAt(0) == 'T') return true;
      else return false;
    }
    catch (Exception e) {
      throw new hla.rti.CouldNotDecode("SpaceAvailable.decode ("
        + new String(buffer, 0, buffer.length) + "): " + e.getMessage());
    }
  }
}
