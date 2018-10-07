

package pipe.hla.book.manager;

public class ManagerNames {
  //labels of the synchronization points used in lifecycle protocol
  public static final String _readyToPopulate = "ReadyToPopulate";
  public static final String _readyToRun = "ReadyToRun";
  public static final String _readyToResign = "ReadyToResign";

  //names of object and interaction classes
  public static final String _FederateClassName = "ObjectRoot.Manager.Federate";
  public static final String _SimulationEndsClassName
    = "InteractionRoot.Manager.SimulationEnds";

  //attributes
  public static final String _FederateHandleAttributeName = "FederateHandle";
  public static final String _FederateTypeAttributeName = "FederateType";
  public static final String _FederateHostAttributeName = "FederateHost";

  //federate type
  public static final String _federateType = "Manager";
}
