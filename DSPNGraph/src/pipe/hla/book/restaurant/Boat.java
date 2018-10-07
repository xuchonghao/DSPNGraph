 //data structure to represent Boat instance

package pipe.hla.book.restaurant;

public class Boat {
  //attribute values
  public int _privilegeToDeleteObjectState;
  /**注意：HLA对象类的 每个属性 都有两个java 实例变量 与之对应
   * 一个包含实例属性的值，另一个存储属性的状态
   * */
  public Position _position;
  public int _positionState;

  public boolean _spaceAvailable; //true iff _modelingState == EMPTY
  public int _spaceAvailableState;

  public String _cargo; //name of a Serving instance
  public int _cargoState;

  //internal
  public int _handle;
  public String _name;
  public int _modelingState;
  public int _serving; //Serving handle corresponding to _cargo
  public static final int EMPTY = 1;
  public static final int AWAITING_SERVING = 2;
  public static final int LOADED = 3;

  public static final String[] stateStrings = {
    "INVALID",
    "EMPTY",
    "AWAITING_SERVING",
    "LOADED"};
}
