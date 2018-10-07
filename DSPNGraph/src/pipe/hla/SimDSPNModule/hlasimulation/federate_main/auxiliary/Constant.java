package pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary;

public class Constant {
    public static final Integer INSTANT_TRANSITION = 0;
    public static final Integer EXPONENTIAL_TRANSITION = 1;
    public static final Integer DETERMINISTIC_TRANSITION = 2;
    public static final int PARTITIONNUMBER0 = 0;
    public static final int PARTITIONNUMBER1 = 1;
    public static final int PARTITIONNUMBER2 = 2;
    public static final int PARTITIONNUMBER3 = 3;
    public static final int PARTITIONNUMBER4 = 4;
    public static final int PARTITIONNUMBER5 = 5;

    public static Integer counter = 0;
    public static Boolean _simulationEnds = false;

    public static final String FEDERATION_NAME = "PetriNet";
    public static final String HOSTNAME = "192.168.206.136";
    public static final int CRC_PORT = 8989;
    //TODO  需要添加对最大时间的判断，不过一般应该也到不了这个时间吧
    public static final double MAX_TIME = Long.MAX_VALUE / 10;
    public static final double InternalQueue_IS_NULL_TIME = Integer.MAX_VALUE;

    /*PetriNet对象类及属性名称*/
    //static String ms_PetriNetNodeTypeStr = "PetriNetNode";
    public static String ms_IdTypeStr = "Id";
    public static String ms_NameTypeStr = "Name";
    public static String ms_LastNodeNameTypeStr = "LastNodeName";
    public static String ms_NextNodeNameTypeStr = "NextNodeName";

    /*变迁对象类及属性名称*/
    public static String ms_TransitionNodeTypeStr = "TransitionNode";
    public static String ms_IsEnabledTypeStr = "IsEnabled";
    public static String ms_DelayTypeStr = "Delay";
    public static String ms_RateTypeStr = "Rate";
    public static String ms_TypeTypeStr = "Type";

    /*库所对象类及属性名称*/
    public static final String ms_PlaceNodeTypeStr = "PlaceNode";
    public static final String ms_NumOfTokenTypeStr = "NumOfToken";
    public static final String ms_CapacityTypeStr = "Capacity";
    public static final String ms_InputArcWeightTypeStr = "InputArcWeight";
    public static final String ms_OutputArcWeightTypeStr = "OutputArcWeight";

    /*交互类及参数名称*/
    public static final String ms_TokenInteractionTypeStr = "TokenInteraction";
    //static String ms_SourceIdTypeStr = "SourceId";
    public static String ms_SourceNameTypeStr = "SourceName";
    //static String ms_TargetIdTypeStr = "TargetId";
    public static final String ms_TargetNameTypeStr = "TargetName";
    public static final String ms_WeightTypestr = "Weight";
    public static final String ms_TimeOfTransitionTypeStr = "TimeOfTransition";

    public static Double initialTime = 0.0;
    public static Double lookahead = 1.0;

    public static final String _readyToPopulate = "ReadyToPopulate";
    public static final String _readyToRun = "ReadyToRun";
    public static final String _readyToResign = "ReadyToResign";

    public static final String numberOfFederatesToAwait = "1";

}
