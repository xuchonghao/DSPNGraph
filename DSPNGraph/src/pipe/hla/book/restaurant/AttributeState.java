

package pipe.hla.book.restaurant;

/**定义了属性的状态，使用它们的目的是用于区分。。。*/
public class AttributeState {
  public final static int OWNED_CONSISTENT = 1;   //value unchanged since update
  public final static int OWNED_INCONSISTENT = 2; //value changes since last update
  public final static int DISCOVERED = 3;        //discovered but no data available
  public final static int REFLECTED = 4;         //data available
  public final static int NOT_REFLECTED = 5;     //not owned, not subscribed
}
