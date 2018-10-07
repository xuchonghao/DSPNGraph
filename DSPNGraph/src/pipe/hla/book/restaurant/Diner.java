//data structure to represent Diner instance

package pipe.hla.book.restaurant;

public class Diner {
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
  public int _serving; //handle for _servingName
  public int _boat; //handle of Boat we're trying to transfer from
  //legal values for _state
  public static final int LOOKING_FOR_FOOD = 1;
  public static final int ACQUIRING = 2;
  public static final int EATING = 3;
  public static final int PREPARING_TO_DELETE_SERVING = 4;

  public static final String[] stateStrings = {
    "INVALID",
    "LOOKING_FOR_FOOD",
    "ACQUIRING",
    "EATING",
    "PREPARING_TO_DELETE_SERVING"};
}
