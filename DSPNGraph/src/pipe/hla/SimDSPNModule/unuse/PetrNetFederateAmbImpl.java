package pipe.hla.SimDSPNModule.unuse;


import hla.rti1516.*;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.hlamodel.HLAPlaceNode;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.hlamodel.TokenInteraction;
import se.pitch.prti1516.FederateAmbassadorImpl;


public class PetrNetFederateAmbImpl extends FederateAmbassadorImpl{

    LogicalTime CurrentTime;
    boolean TimeRegulated;
    boolean TimeConstrained;
    boolean timeAdvGrant;
    boolean TimeAdvanceOutstanding;


    public void timeRegulationEnabled(LogicalTime theFederateTime) {
        CurrentTime = theFederateTime;
        System.out.println("###timeRegulationEnabled callback");
        System.out.println("###Federate currentTime is: "+CurrentTime.toString());
        TimeRegulated = true;
    }

    public void timeConstrainedEnabled(LogicalTime theFederateTime) {
        CurrentTime = theFederateTime;
        System.out.println("###timeConstrainedEnabled callback");
        System.out.println("###Federate currentTime is: "+CurrentTime.toString());
        TimeConstrained = true;
    }

    /**下一事件的时间推进请求的回调函数*/
    public void timeAdvanceGrant(LogicalTime theFederateTime) {
        System.out.println("###timeAdvanceGrant callback");
        timeAdvGrant = true;
        TimeAdvanceOutstanding = false;
        CurrentTime = theFederateTime;
        System.out.println("###currenttim:"+CurrentTime);
    }




    /**
     * 这个函数是订购？发布？注册？ 如果我只需要订购消息的话，是不是就不用这个函数了？这个函数是对谁的回调呢？*/
    public void startRegistrationForObjectClass(ObjectClassHandle theClass) {
        System.out.println("### startRegistrationForObjectClass callback");

    }
    public void stopRegistrationForObjectClass(ObjectClassHandle var1) {
    }
    public void turnInteractionsOn(InteractionClassHandle theHandle) {
        if(theHandle == TokenInteraction.getInteractionClassHandle()){
            System.out.println(" ### turnInteractionsOn callback for tokenInteraction!");
            TokenInteraction.setMs_sendInteractions(true);
        }
    }

    public void turnInteractionsOff(InteractionClassHandle theHandle) {
        if(theHandle == TokenInteraction.getInteractionClassHandle()){
            System.out.println(" ### turnInteractionsOn callback for tokenInteraction!");
            TokenInteraction.setMs_sendInteractions(false);
        }
    }

    //discoverObjectInstance  是对那个函数的回调？我是假设不需要对对象进行订购的基础上进行的
    //provideAttributeValueUpdate()这个是对谁的回调，需不需要这个呢？
    //removeObjectInstance()需要吗？








    /**对非TSO事件的接收，比如：ReturnTokenInteraction*/
    @Override
    public final void receiveInteraction(InteractionClassHandle interactionClassHandle,ParameterHandleValueMap theParameters,
                                         byte[] userSuppliedTag,OrderType sentOrdering,TransportationType theTransport){
        if(interactionClassHandle == TokenInteraction.getInteractionClassHandle()){
            //int sourceId = (int) theParameters.get(TokenInteraction.ms_SourceIdTypeStr);
            String sourceName = (String) theParameters.get(Constant.ms_SourceNameTypeStr);
            //int targetId = (int) theParameters.get(TokenInteraction.ms_TargetIdTypeStr);
            String targetName = (String) theParameters.get(Constant.ms_TargetNameTypeStr);

            HLAPlaceNode placeNode = null;//(HLAPlaceNode) MainLoopFed.FindP(targetName);
            int inputArcWeight = placeNode.getInputArcWeight();
            placeNode.setNumOfToken(placeNode.getNumOfToken() + inputArcWeight);

            //给之前的变迁返回一个值？
            //需要更新实例属性吗？？让别的成员知道，关键是它和别的成员有关系吗？所以需要一个参数来判断P是不是和其他成员有关系，即有没有其他成员订阅这个实例
        }
        System.out.println("###tokenInteraction received");
    }
    /**添加区域后对非TSO事件的接收，比如：ReturnTokenInteraction*/
    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap theParameters,
                                   byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, RegionHandleSet var6) {

    }
    /**对TSO事件的接收，比如：TokenInteraction*/
    @Override
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap theParameters,
                                   byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime var6, OrderType var7) {
        //时间需要推进
    }
    /**添加区域对TSO事件的接收，比如：TokenInteraction*/
    public void receiveInteraction(InteractionClassHandle interactionClassHandle, ParameterHandleValueMap theParameters,
                                   byte[] userSuppliedTag, OrderType sentOrdering, TransportationType theTransport, LogicalTime var6, OrderType var7, RegionHandleSet var8) {
    }


}
