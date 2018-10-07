package pipe.hla.book.restaurant;

/** Representation of the position attribute. Defining the class ensures a uniform
* representation across all federates.
*/
public final class Position implements Cloneable {
  public double _angle; //degrees counter-clockwise; zero is pos x axis (east)
  public int _offset;   //one of the constants below

  public static final int INBOARD_CANAL = 1;
  public static final int ON_CANAL = 2;
  public static final int OUTBOARD_CANAL = 3;

  public Position(double angle, int offset) {
    _angle = angle;
    _offset = offset;
  }

  /** Return new byte array with encoding of this instance.
  * Used to update attribute value.
  */
  public byte[] encode() {
    byte[] serialization = null;
    String s = Double.toString(_angle) + "," + Integer.toString(_offset);
    serialization = s.getBytes();
    return serialization;
  }


  /** Construct a new instance from serialized version.
  * Used when reflecting an attribute value
  */
  public Position(byte[] buffer, int offset)
  throws hla.rti.CouldNotDecode
  {
    try {
      String s = new String(buffer, 0, buffer.length);
      int comma =  s.indexOf(',');
      if (comma == -1) throw new hla.rti.CouldNotDecode(
        "No comma in Position encoding " + s);
      String angleString = s.substring(0, comma);
      String offsetString = s.substring(comma+1);
      _angle = (new Double(angleString)).doubleValue();
      _offset = Integer.parseInt(offsetString);
    }
    catch (Exception e) {
      throw new hla.rti.CouldNotDecode("Position.decode("
        + new String(buffer, 0, buffer.length) + "): " + e.getMessage());
    }
  }

  public String toString() {
    String offset;
    switch(_offset) {
      case INBOARD_CANAL:
        offset = "INBOARD_CANAL";
        break;
      case ON_CANAL:
        offset = "ON_CANAL";
        break;
      case OUTBOARD_CANAL:
        offset = "OUTBOARD_CANAL";
        break;
      default:
        offset = "ILLEGAL";
    }
    return "(" + Double.toString(_angle) + "," + offset + ")";
  }

  public Object clone()
  throws CloneNotSupportedException
  {
    return super.clone();
  }
}
