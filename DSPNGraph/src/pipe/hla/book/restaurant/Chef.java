//data structure to represent Chef instance

package pipe.hla.book.restaurant;

public class Chef {
  //attribute values
  public int _privilegeToDeleteObjectState;
  public Position _position;
  public int _positionState;
  public int _state;
  public int _stateState;
  public String _servingName;
  public int _servingNameState;

  //internal
  public int _handle;
  public String _name;
  public int _serving;  //local instance handle
  public int _boat; //handle of Boat we're trying to transfer to

  //legal values for _state
  public static final int MAKING_SUSHI = 1;
  public static final int LOOKING_FOR_BOAT = 2;
  public static final int WAITING_TO_TRANSFER = 3;

  public static final String[] stateStrings = {
    "INVALID",
    "MAKING_SUSHI",
    "LOOKING_FOR_BOAT",
    "WAITING_TO_TRANSFER"};
}
