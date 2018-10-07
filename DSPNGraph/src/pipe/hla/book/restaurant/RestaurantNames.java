
package pipe.hla.book.restaurant;

/**此类用于为FED中出现的HLA对象类和属性的名字定义常量
HLA对象类的  每个属性  都有  两个java实例变量 与之对应
其中一个包含实例属性的值， 另一个存储属性的状态 */
public class RestaurantNames {

  //object class names
  public static final String _RestaurantClassName = "ObjectRoot.Restaurant";
  public static final String _ServingClassName = "ObjectRoot.Restaurant.Serving";
  public static final String _BoatClassName = "ObjectRoot.Restaurant.Boat";
  public static final String _ActorClassName = "ObjectRoot.Restaurant.Actor";
  public static final String _ChefClassName = "ObjectRoot.Restaurant.Actor.Chef";
  public static final String _DinerClassName = "ObjectRoot.Restaurant.Actor.Diner";

  //attribute names
  public static final String _privilegeToDeleteObjectAttributeName = "privilegeToDeleteObject";
  public static final String _positionAttributeName = "position";
  public static final String _typeAttributeName = "type";
  public static final String _spaceAvailableAttributeName = "spaceAvailable";
  public static final String _cargoAttributeName = "cargo";
  public static final String _chefStateAttributeName = "chefState";
  public static final String _dinerStateAttributeName = "dinerState";
  public static final String _servingNameAttributeName = "servingName";

  //interaction names
  public static final String _TransferAcceptedClassName = "InteractionRoot.TransferAccepted";

  //parameter names
  public static final String _servingNameParameterName = "servingName";
}
