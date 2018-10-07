package pipe.hla.SimDSPNModule.hlasimulation.federate_main.hlamodel;

import hla.rti1516.*;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.auxiliary.Constant;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PetriNetNode;
import se.pitch.prti1516.Encoder;
import se.pitch.prti1516.LogicalTimeIntervalDouble;

public class TokenInteraction {
    /*交互传递的参数*/
    //private int sourceId;
    private String sourceName;
    //private int targetId;
    private String targetName;
    private long timeOfTransition;


    //交互类及参数句柄
    private static InteractionClassHandle interactionClassHandle;
    //private static ParameterHandle sourceIdHandle;
    private static ParameterHandle sourceNameHandle;
    //private static ParameterHandle targetIdHandle;
    private static ParameterHandle targetNameHandle;
    private static ParameterHandle timeOfTransitionHandle;
    static RTIambassador rti = null;

    LogicalTimeInterval Lookahead;
    LogicalTime CurrentTime;
    boolean regulating;

    static boolean ms_sendInteractions;

    static void Init(RTIambassador amb_rti ){
        rti = amb_rti;
        if(rti != null){
            try {
                interactionClassHandle = rti.getInteractionClassHandle(Constant.ms_TokenInteractionTypeStr);
                System.out.println("ms_TokenInteractionTypeStr:"+interactionClassHandle);

                //sourceIdHandle = rti.getParameterHandle(interactionClassHandle,ms_SourceIdTypeStr);
                sourceNameHandle = rti.getParameterHandle(interactionClassHandle, Constant.ms_SourceNameTypeStr);
                //targetIdHandle = rti.getParameterHandle(interactionClassHandle,ms_TargetIdTypeStr);
                targetNameHandle = rti.getParameterHandle(interactionClassHandle, Constant.ms_TargetNameTypeStr);
                timeOfTransitionHandle = rti.getParameterHandle(interactionClassHandle, Constant.ms_TimeOfTransitionTypeStr);
                //System.out.println("sourceIdHandle:"+sourceIdHandle);
                System.out.println("sourceNameHandle:"+sourceNameHandle);
                //System.out.println("targetIdHandle:"+targetIdHandle);
                System.out.println("targetNameHandle:"+targetNameHandle);


                System.out.println("***Init() OK");
            } catch (InvalidInteractionClassHandle invalidInteractionClassHandle) {
                invalidInteractionClassHandle.printStackTrace();
            } catch (NameNotFound nameNotFound) {
                nameNotFound.printStackTrace();
            } catch (FederateNotExecutionMember federateNotExecutionMember) {
                federateNotExecutionMember.printStackTrace();
            } catch (RTIinternalError rtIinternalError) {
                rtIinternalError.printStackTrace();
            }
        }

    }
    public void Publishing(){
        if(rti != null){
            try {
                rti.publishInteractionClass(interactionClassHandle);
            } catch (SaveInProgress saveInProgress) {
                saveInProgress.printStackTrace();
            } catch (RestoreInProgress restoreInProgress) {
                restoreInProgress.printStackTrace();
            } catch (FederateNotExecutionMember federateNotExecutionMember) {
                federateNotExecutionMember.printStackTrace();
            } catch (RTIinternalError rtIinternalError) {
                rtIinternalError.printStackTrace();
            } catch (InteractionClassNotDefined interactionClassNotDefined) {
                interactionClassNotDefined.printStackTrace();
            }
            System.out.println("*** published objectclass PlaceNode OK!");
        }
    }
    public void Subscribing(){
        if(rti != null){

            try {

                rti.subscribeInteractionClass(interactionClassHandle);
            } catch (SaveInProgress saveInProgress) {
                saveInProgress.printStackTrace();
            } catch (FederateServiceInvocationsAreBeingReportedViaMOM federateServiceInvocationsAreBeingReportedViaMOM) {
                federateServiceInvocationsAreBeingReportedViaMOM.printStackTrace();
            } catch (RestoreInProgress restoreInProgress) {
                restoreInProgress.printStackTrace();
            } catch (FederateNotExecutionMember federateNotExecutionMember) {
                federateNotExecutionMember.printStackTrace();
            } catch (RTIinternalError rtIinternalError) {
                rtIinternalError.printStackTrace();
            } catch (InteractionClassNotDefined interactionClassNotDefined) {
                interactionClassNotDefined.printStackTrace();
            }
            System.out.println("***Subscribing() OK");
        }

    }

    //???fire()方法该给谁呢？  target也可能是多个吧？换个存储方式？   发送交互应该带上时间戳消息
    public void sendTokenInteraction(PetriNetNode source, PetriNetNode target){
        try{
            ParameterHandleValueMap parameters = rti.getParameterHandleValueMapFactory().create(4);

            //parameters.put(sourceIdHandle, Encoder.makeHLAinteger32BE(source.getId()));
            parameters.put(sourceNameHandle, Encoder.makeHLAunicodeString(source.getName()));
            //parameters.put(targetIdHandle, Encoder.makeHLAinteger32BE(target.getId()));
            parameters.put(targetNameHandle, Encoder.makeHLAunicodeString(target.getName()));
            parameters.put(timeOfTransitionHandle,Encoder.makeHLAlogicalTimeInterval(new LogicalTimeIntervalDouble(((HLATransitionNode)source).getDelay())));

            rti.sendInteraction(interactionClassHandle,parameters,null,CurrentTime);
            //rti.sendInteraction(interactionClassHandle, parameters, null);
            System.out.println("sendTokenInteraction,from "+source.getName()+" to " + target.getName());
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (InteractionParameterNotDefined interactionParameterNotDefined) {
            interactionParameterNotDefined.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (InteractionClassNotPublished interactionClassNotPublished) {
            interactionClassNotPublished.printStackTrace();
        } catch (InteractionClassNotDefined interactionClassNotDefined) {
            interactionClassNotDefined.printStackTrace();
        } catch (InvalidLogicalTime invalidLogicalTime) {
            invalidLogicalTime.printStackTrace();
        }

    }


    //get and set method

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public long getTimeOfTransition() {
        return timeOfTransition;
    }

    public void setTimeOfTransition(long timeOfTransition) {
        this.timeOfTransition = timeOfTransition;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public static InteractionClassHandle getInteractionClassHandle() {
        return interactionClassHandle;
    }

    public static void setInteractionClassHandle(InteractionClassHandle interactionClassHandle) {
        TokenInteraction.interactionClassHandle = interactionClassHandle;
    }



    public static ParameterHandle getSourceNameHandle() {
        return sourceNameHandle;
    }

    public static void setSourceNameHandle(ParameterHandle sourceNameHandle) {
        TokenInteraction.sourceNameHandle = sourceNameHandle;
    }



    public static ParameterHandle getTargetNameHandle() {
        return targetNameHandle;
    }

    public static void setTargetNameHandle(ParameterHandle targetNameHandle) {
        TokenInteraction.targetNameHandle = targetNameHandle;
    }

    public static boolean isMs_sendInteractions() {
        return ms_sendInteractions;
    }

    public static void setMs_sendInteractions(boolean sendInteractions) {
        ms_sendInteractions = sendInteractions;
    }
    /*
    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }
    public static ParameterHandle getTargetIdHandle() {
        return targetIdHandle;
    }

    public static void setTargetIdHandle(ParameterHandle targetIdHandle) {
        TokenInteraction.targetIdHandle = targetIdHandle;
    }
    public static ParameterHandle getSourceIdHandle() {
        return sourceIdHandle;
    }

    public static void setSourceIdHandle(ParameterHandle sourceIdHandle) {
        TokenInteraction.sourceIdHandle = sourceIdHandle;
    }
    */
}

