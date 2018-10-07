package pipe.hla.SimDSPNModule.hlasimulation.federate_secondery;

import hla.rti1516.InteractionClassHandle;

/**从另一个库所返回capacity来判断是否可以使能时使用，也算是一个交互类吧，这个扩展待用*/
public class ReturnTokenInteraction {
    /*交互类及参数名称*/
    static String ms_TokenInteractionTypeStr = "ReturnTokenInteraction";
    //交互类及参数句柄
    private static InteractionClassHandle interactionClassHandle;
}
