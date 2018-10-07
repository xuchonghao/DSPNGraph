package pipe.hla.SimDSPNModule.unuse;


import hla.rti1516.*;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.hlamodel.HLAPlaceNode;
import pipe.hla.SimDSPNModule.hlasimulation.federate_main.hlamodel.HLATransitionNode;
import se.pitch.prti1516.RTI;

import java.io.File;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * 变迁是不是应该有发生时间和延迟
 * 应该考虑抑制弧的存在，考虑在库所后面添加表示抑制弧的属性*/
public class MainLoop {
    public static final String FEDERATION_NAME = "PetriNet";
    public static final String HOSTNAME = "192.168.206.134";
    private static final int CRC_PORT = 8989;
    private static RTIambassador rti = null;



    private ObjectClassHandle placeNodeHandle;
    private ObjectClassHandle transitionNodeHandle;

    private AttributeHandle numOfTokenHandle;
    private AttributeHandle capacityHandle;
    private AttributeHandle inputArcWeightHandle;
    private AttributeHandle outputArcWeightHandle;

    private AttributeHandle isEnabledHandle;
    private AttributeHandle delayHandle;
    private AttributeHandle rateHandle;
    private AttributeHandle typeHandle;


    //private ParameterHandle sourceNameHandle;
    //private ParameterHandle targetNameHandle;
    /**用于保存当前子网中的库所和变迁*/
    public static ArrayList<HLAPlaceNode> HLAPlaceNodeArray = new ArrayList<HLAPlaceNode>();
    public static ArrayList<HLATransitionNode> HLATransitionNodeArray = new ArrayList<HLATransitionNode>();


    private  FederateHandle federateHandle;
    PetrNetFederateAmbImpl petrNetFederateAmb;

    LogicalTimeInterval Lookahead;
    LogicalTime CurrentTime;

    public void run(){
        //1
        try {
            rti = RTI.getRTIambassador(HOSTNAME,CRC_PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        }

        //2
        File fddFile = new File("PetriNet.xml");
        try {
            System.out.println("***Creating Federation Execution");
            rti.createFederationExecution(FEDERATION_NAME,fddFile.toURL());
            System.out.println("***Successful Creating Federation Execution");
        } catch (FederationExecutionAlreadyExists federationExecutionAlreadyExists) {
            System.out.println("***Note: Federation execution already exists.");
            federationExecutionAlreadyExists.printStackTrace();
        } catch (CouldNotOpenFDD couldNotOpenFDD) {
            couldNotOpenFDD.printStackTrace();
        } catch (ErrorReadingFDD errorReadingFDD) {
            errorReadingFDD.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //3
        try {
            System.out.println("***JOINING FEDERATION EXECUTION: ");
            federateHandle = rti.joinFederationExecution("subnet1", FEDERATION_NAME, petrNetFederateAmb, null);
        } catch (FederateAlreadyExecutionMember federateAlreadyExecutionMember) {
            federateAlreadyExecutionMember.printStackTrace();
        } catch (FederationExecutionDoesNotExist federationExecutionDoesNotExist) {
            federationExecutionDoesNotExist.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        }

        /**感觉不需要！*/
        try {
            System.out.println(" 开启联邦成员Class是否存在异地定购恰当性建议");
            rti.enableObjectClassRelevanceAdvisorySwitch();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (ObjectClassRelevanceAdvisorySwitchIsOn objectClassRelevanceAdvisorySwitchIsOn) {
            objectClassRelevanceAdvisorySwitchIsOn.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        }

        try {
            System.out.println("开启联邦成员Interaction是否存在异地定购恰当性建议");
            rti.enableInteractionRelevanceAdvisorySwitch();
        } catch (InteractionRelevanceAdvisorySwitchIsOn interactionRelevanceAdvisorySwitchIsOn) {
            interactionRelevanceAdvisorySwitchIsOn.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        }

        try {
            rti.enableTimeConstrained();
        } catch (TimeConstrainedAlreadyEnabled timeConstrainedAlreadyEnabled) {
            timeConstrainedAlreadyEnabled.printStackTrace();
        } catch (InTimeAdvancingState inTimeAdvancingState) {
            inTimeAdvancingState.printStackTrace();
        } catch (RequestForTimeConstrainedPending requestForTimeConstrainedPending) {
            requestForTimeConstrainedPending.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        }
        try {
            rti.enableTimeRegulation(Lookahead);
        } catch (TimeRegulationAlreadyEnabled timeRegulationAlreadyEnabled) {
            timeRegulationAlreadyEnabled.printStackTrace();
        } catch (InvalidLookahead invalidLookahead) {
            invalidLookahead.printStackTrace();
        } catch (InTimeAdvancingState inTimeAdvancingState) {
            inTimeAdvancingState.printStackTrace();
        } catch (RequestForTimeRegulationPending requestForTimeRegulationPending) {
            requestForTimeRegulationPending.printStackTrace();
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        } catch (SaveInProgress saveInProgress) {
            saveInProgress.printStackTrace();
        } catch (RestoreInProgress restoreInProgress) {
            restoreInProgress.printStackTrace();
        } catch (RTIinternalError rtIinternalError) {
            rtIinternalError.printStackTrace();
        }

        /**初始化成员数据*/
        HLAPlaceNode p1 = new HLAPlaceNode("P1");
        p1.setNumOfToken(1);
        HLATransitionNode t1 = new HLATransitionNode("T1");

        p1.Init(rti);
        p1.Publishing();

        //rti.nextMessageRequest();



       // rti.tick();

    }
    public static void main(String[] args) {




    }
}
