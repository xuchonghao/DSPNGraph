package pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.hlamodel;

import hla.rti1516.*;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.auxiliary.Constant;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.PlaceNode;

import java.util.ArrayList;
import java.util.Iterator;

public class HLAPlaceNode extends PlaceNode {

    public HLAPlaceNode(String name) {
        super(name);
        //PlaceNodeArray.add(this);
        System.out.println("PlaceNode Ok!!!");
    }


    LogicalTimeInterval Lookahead;
    LogicalTime CurrentTime;
    boolean regulating;


    //对象实例句柄
    private ObjectInstanceHandle m_InstanceId;

    /*对象实例更新标志*/
    boolean  ms_NumOfTokenUpdate;
    boolean  ms_CapacityUpdate;
    boolean  ms_InputArcWeightUpdate;
    boolean  ms_OutputArcWeightUpdate;
    boolean ms_InstanceDeleted;


    /*对象类及属性句柄*/
    static ObjectClassHandle placeNodeHandle;
    static AttributeHandle numOfTokenHandle;
    static AttributeHandle capacityHandle;
    static AttributeHandle inputArcWeightHandle;
    static AttributeHandle outputArcWeightHandle;

    /*所有权拥有标志*/
    Boolean   ms_OwnNumOfToken;
    Boolean   ms_OwnCapacity;
    Boolean   ms_OwnInputArcWeight;
    Boolean   ms_OwnOutputArcWeight;
    Boolean ms_OwnPrivilegeToDelete;

    // static members data???
    static boolean ms_doRegistry;

    static RTIambassador rti = null;


    public void Init(RTIambassador amb_rti ){
        rti = amb_rti;
        if(rti != null){
            try {
                placeNodeHandle = rti.getObjectClassHandle(Constant.ms_PlaceNodeTypeStr);
                System.out.println("placeNodeHandle:"+placeNodeHandle);

                idHandle = rti.getAttributeHandle(placeNodeHandle, Constant.ms_IdTypeStr);
                nameHandle = rti.getAttributeHandle(placeNodeHandle, Constant.ms_NameTypeStr);
                lastNodeNameHandle = rti.getAttributeHandle(placeNodeHandle, Constant.ms_LastNodeNameTypeStr);
                nextNodeNameHandle = rti.getAttributeHandle(placeNodeHandle, Constant.ms_NextNodeNameTypeStr);
                System.out.println("idHandle:"+idHandle);
                System.out.println("nameHandle:"+nameHandle);
                System.out.println("lastNodeNameHandle:"+lastNodeNameHandle);
                System.out.println("nextNodeNameHandle:"+nextNodeNameHandle);

                numOfTokenHandle = rti.getAttributeHandle(placeNodeHandle, Constant.ms_NumOfTokenTypeStr);
                capacityHandle = rti.getAttributeHandle(placeNodeHandle, Constant.ms_CapacityTypeStr);
                inputArcWeightHandle = rti.getAttributeHandle(placeNodeHandle, Constant.ms_InputArcWeightTypeStr);
                outputArcWeightHandle = rti.getAttributeHandle(placeNodeHandle, Constant.ms_OutputArcWeightTypeStr);

                System.out.println("numOfTokenHandle:"+numOfTokenHandle);
                System.out.println("capacityHandle:"+capacityHandle);
                System.out.println("inputArcWeightHandle:"+inputArcWeightHandle);
                System.out.println("outputArcWeightHandle:"+outputArcWeightHandle);

                System.out.println("***Init() OK");
            } catch (NameNotFound nameNotFound) {
                nameNotFound.printStackTrace();
            } catch (FederateNotExecutionMember federateNotExecutionMember) {
                federateNotExecutionMember.printStackTrace();
            } catch (RTIinternalError rtIinternalError) {
                rtIinternalError.printStackTrace();
            } catch (InvalidObjectClassHandle invalidObjectClassHandle) {
                invalidObjectClassHandle.printStackTrace();
            }
        }

    }
    public void Publishing(){
        if(rti != null){
            AttributeHandleSet attributeSet = null;
            try {
                attributeSet = rti.getAttributeHandleSetFactory().create();
                attributeSet.add(idHandle);
                attributeSet.add(nameHandle);
                attributeSet.add(lastNodeNameHandle);
                attributeSet.add(nextNodeNameHandle);

                attributeSet.add(numOfTokenHandle);
                attributeSet.add(capacityHandle);
                attributeSet.add(inputArcWeightHandle);
                attributeSet.add(outputArcWeightHandle);

                rti.publishObjectClassAttributes(placeNodeHandle,attributeSet);


            } catch (FederateNotExecutionMember federateNotExecutionMember) {
                federateNotExecutionMember.printStackTrace();
            } catch (SaveInProgress saveInProgress) {
                saveInProgress.printStackTrace();
            } catch (AttributeNotDefined attributeNotDefined) {
                attributeNotDefined.printStackTrace();
            } catch (RestoreInProgress restoreInProgress) {
                restoreInProgress.printStackTrace();
            } catch (ObjectClassNotDefined objectClassNotDefined) {
                objectClassNotDefined.printStackTrace();
            } catch (RTIinternalError rtIinternalError) {
                rtIinternalError.printStackTrace();
            }
            System.out.println("*** published objectclass PlaceNode OK!");
        }
    }
    /*
    public void Subscribing(){
        if(rti != null){
            AttributeHandleSet attributeSet = null;
            try {
                attributeSet = rti.getAttributeHandleSetFactory().create();

                attributeSet.add(idHandle);
                attributeSet.add(nameHandle);
                attributeSet.add(lastNodeNameHandle);
                attributeSet.add(nextNodeNameHandle);

                attributeSet.add(numOfTokenHandle);
                attributeSet.add(capacityHandle);
                attributeSet.add(inputArcWeightHandle);
                attributeSet.add(outputArcWeightHandle);

                rti.subscribeObjectClassAttributes(placeNodeHandle,attributeSet);

            } catch (FederateNotExecutionMember federateNotExecutionMember) {
                federateNotExecutionMember.printStackTrace();
            } catch (SaveInProgress saveInProgress) {
                saveInProgress.printStackTrace();
            } catch (AttributeNotDefined attributeNotDefined) {
                attributeNotDefined.printStackTrace();
            } catch (RestoreInProgress restoreInProgress) {
                restoreInProgress.printStackTrace();
            } catch (ObjectClassNotDefined objectClassNotDefined) {
                objectClassNotDefined.printStackTrace();
            } catch (RTIinternalError rtIinternalError) {
                rtIinternalError.printStackTrace();
            }
            System.out.println("***Subscribing() OK");
        }

    }

    public void Register(){
        if(rti != null && ms_doRegistry){
            for(int i=0;i<PlaceNodeArray.size();i++){
                PlaceNode placeNode = PlaceNodeArray.get(i);
                try {
                    placeNode.m_InstanceId = rti.registerObjectInstance(placeNode.placeNodeHandle,placeNode.getName());
                    System.out.println("placeNode.placeNodeHandle:"+placeNode.getM_InstanceId() + "name:"+placeNode.getName());
                } catch (ObjectClassNotDefined objectClassNotDefined) {
                    objectClassNotDefined.printStackTrace();
                } catch (ObjectClassNotPublished objectClassNotPublished) {
                    objectClassNotPublished.printStackTrace();
                } catch (ObjectInstanceNameNotReserved objectInstanceNameNotReserved) {
                    objectInstanceNameNotReserved.printStackTrace();
                } catch (ObjectInstanceNameInUse objectInstanceNameInUse) {
                    objectInstanceNameInUse.printStackTrace();
                } catch (FederateNotExecutionMember federateNotExecutionMember) {
                    federateNotExecutionMember.printStackTrace();
                } catch (SaveInProgress saveInProgress) {
                    saveInProgress.printStackTrace();
                } catch (RestoreInProgress restoreInProgress) {
                    restoreInProgress.printStackTrace();
                } catch (RTIinternalError rtIinternalError) {
                    rtIinternalError.printStackTrace();
                }
            }
        }
        System.out.println("***Register() OK");
    }*/
    public void DeleteAllInstance(ArrayList<HLAPlaceNode> placeNodes){
        for(int i=0;i<placeNodes.size();i++){
            HLAPlaceNode placeNode = placeNodes.get(i);
            if(placeNode.isMs_InstanceDeleted() && placeNode.getMs_OwnPrivilegeToDelete()){
                try {
                    if(regulating){
                        rti.deleteObjectInstance(placeNode.getM_InstanceId(),"PlaceNode deleted".getBytes(),CurrentTime.add(Lookahead));
                    }else {
                        rti.deleteObjectInstance(placeNode.getM_InstanceId(),"PlaceNode deleted".getBytes());
                    }
                } catch (FederateNotExecutionMember federateNotExecutionMember) {
                    federateNotExecutionMember.printStackTrace();
                } catch (RTIinternalError rtIinternalError) {
                    rtIinternalError.printStackTrace();
                } catch (ObjectInstanceNotKnown objectInstanceNotKnown) {
                    objectInstanceNotKnown.printStackTrace();
                } catch (SaveInProgress saveInProgress) {
                    saveInProgress.printStackTrace();
                } catch (RestoreInProgress restoreInProgress) {
                    restoreInProgress.printStackTrace();
                } catch (InvalidLogicalTime invalidLogicalTime) {
                    invalidLogicalTime.printStackTrace();
                } catch (DeletePrivilegeNotHeld deletePrivilegeNotHeld) {
                    deletePrivilegeNotHeld.printStackTrace();
                } catch (IllegalTimeArithmetic illegalTimeArithmetic) {
                    illegalTimeArithmetic.printStackTrace();
                }

            }
            placeNode.setMs_OwnPrivilegeToDelete(false);
        }
    }
    public void PlaceNodeUpdate(HLAPlaceNode placeNode){
        AttributeHandleValueMap attributeValueMap = null;
        try {
            attributeValueMap = rti.getAttributeHandleValueMapFactory().create(8);
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        }
        if(placeNode.isMs_NumOfTokenUpdate()){
            int numOfToken = placeNode.getNumOfToken();
            attributeValueMap.put(placeNode.getM_InstanceId(),numOfToken);
            placeNode.setMs_NumOfTokenUpdate(false);
        }
        if(placeNode.isMs_InputArcWeightUpdate()){
            int inputArcWeight = placeNode.getInputArcWeight();
            attributeValueMap.put(placeNode.getM_InstanceId(),inputArcWeight);
            placeNode.setMs_InputArcWeightUpdate(false);
        }
        if(placeNode.isMs_OutputArcWeightUpdate()){
            int outputArcWeight = placeNode.getOutputArcWeight();
            attributeValueMap.put(placeNode.getM_InstanceId(),outputArcWeight);
            placeNode.setMs_OutputArcWeightUpdate(false);
        }
        if(placeNode.isMs_CapacityUpdate()){
            int capacity = placeNode.getCapacity();
            attributeValueMap.put(placeNode.getM_InstanceId(),capacity);
            placeNode.setMs_CapacityUpdate(false);
        }
        if(attributeValueMap.size() > 0){

            try {
                if(regulating){
                    Lookahead = rti.queryLookahead();
                    rti.updateAttributeValues(placeNode.getM_InstanceId(),attributeValueMap,"PlaceNodeUpdate ".getBytes(),CurrentTime.add(Lookahead));
                }else{
                    rti.updateAttributeValues(placeNode.getM_InstanceId(),attributeValueMap,"PlaceNodeUpdate ".getBytes());
                }
            } catch (FederateNotExecutionMember federateNotExecutionMember) {
                federateNotExecutionMember.printStackTrace();
            } catch (TimeRegulationIsNotEnabled timeRegulationIsNotEnabled) {
                timeRegulationIsNotEnabled.printStackTrace();
            } catch (AttributeNotDefined attributeNotDefined) {
                attributeNotDefined.printStackTrace();
            } catch (RTIinternalError rtIinternalError) {
                rtIinternalError.printStackTrace();
            } catch (ObjectInstanceNotKnown objectInstanceNotKnown) {
                objectInstanceNotKnown.printStackTrace();
            } catch (SaveInProgress saveInProgress) {
                saveInProgress.printStackTrace();
            } catch (RestoreInProgress restoreInProgress) {
                restoreInProgress.printStackTrace();
            } catch (IllegalTimeArithmetic illegalTimeArithmetic) {
                illegalTimeArithmetic.printStackTrace();
            } catch (AttributeNotOwned attributeNotOwned) {
                attributeNotOwned.printStackTrace();
            } catch (InvalidLogicalTime invalidLogicalTime) {
                invalidLogicalTime.printStackTrace();
            }
            System.out.println("****Update"+placeNode.getName() +"OK!");
        }

        System.out.println("****PlaceNode 数组为空");
    }

    public void SetUpdateControl(boolean status,AttributeHandleSet theAttrHandles){
        Iterator<AttributeHandle> it = theAttrHandles.iterator();
        AttributeHandle attributeHandle = null;
        while(it.hasNext()){
            attributeHandle = it.next();

            if(numOfTokenHandle == attributeHandle){
                ms_NumOfTokenUpdate = status;
                System.out.println("FED_HW: Turning ms_NumOfTokenUpdate Updates"+status);
            }
            if(capacityHandle == attributeHandle){
                ms_CapacityUpdate = status;
                System.out.println("FED_HW: Turning ms_CapacityUpdate Updates"+status);
            }
            if(inputArcWeightHandle == attributeHandle){
                ms_InputArcWeightUpdate = status;
                System.out.println("FED_HW: Turning ms_InputArcWeightUpdate Updates"+status);
            }
            if(outputArcWeightHandle == attributeHandle){
                ms_OutputArcWeightUpdate = status;
                System.out.println("FED_HW: Turning ms_OutputArcWeightUpdate Updates"+status);
            }


        }
    }


    //get and set method
    public static boolean isMs_doRegistry() {
        return ms_doRegistry;
    }

    public static void setMs_doRegistry(boolean status) {
        HLAPlaceNode.ms_doRegistry = status;
    }

    public int getNumOfToken() {
        return numOfToken;
    }

    public void setNumOfToken(int numOfToken) {
        this.numOfToken = numOfToken;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getInputArcWeight() {
        return inputArcWeight;
    }

    public void setInputArcWeight(int inputArcWeight) {
        this.inputArcWeight = inputArcWeight;
    }

    public int getOutputArcWeight() {
        return outputArcWeight;
    }

    public void setOutputArcWeight(int outputArcWeight) {
        this.outputArcWeight = outputArcWeight;
    }

    public ObjectInstanceHandle getM_InstanceId() {
        return m_InstanceId;
    }

    public void setM_InstanceId(ObjectInstanceHandle m_InstanceId) {
        this.m_InstanceId = m_InstanceId;
    }

    public boolean isMs_NumOfTokenUpdate() {
        return ms_NumOfTokenUpdate;
    }

    public void setMs_NumOfTokenUpdate(boolean status) {
        this.ms_NumOfTokenUpdate = status;
    }

    public boolean isMs_CapacityUpdate() {
        return ms_CapacityUpdate;
    }

    public void setMs_CapacityUpdate(boolean status) {
        this.ms_CapacityUpdate = status;
    }

    public boolean isMs_InputArcWeightUpdate() {
        return ms_InputArcWeightUpdate;
    }

    public void setMs_InputArcWeightUpdate(boolean status) {
        this.ms_InputArcWeightUpdate = status;
    }

    public boolean isMs_InstanceDeleted() {
        return ms_InstanceDeleted;
    }

    public void setMs_InstanceDeleted(boolean ms_InstanceDeleted) {
        this.ms_InstanceDeleted = ms_InstanceDeleted;
    }
    public boolean isMs_OutputArcWeightUpdate() {
        return ms_OutputArcWeightUpdate;
    }

    public void setMs_OutputArcWeightUpdate(boolean status) {
        this.ms_OutputArcWeightUpdate = status;
    }

    public static AttributeHandle getNumOfTokenHandle() {
        return numOfTokenHandle;
    }

    public static void setNumOfTokenHandle(AttributeHandle numOfTokenHandle) {
        HLAPlaceNode.numOfTokenHandle = numOfTokenHandle;
    }

    public static ObjectClassHandle getPlaceNodeHandle() {
        return placeNodeHandle;
    }

    public static void setPlaceNodeHandle(ObjectClassHandle placeNodeHandle) {
        HLAPlaceNode.placeNodeHandle = placeNodeHandle;
    }

    public static AttributeHandle getCapacityHandle() {
        return capacityHandle;
    }

    public static void setCapacityHandle(AttributeHandle capacityHandle) {
        HLAPlaceNode.capacityHandle = capacityHandle;
    }

    public static AttributeHandle getInputArcWeightHandle() {
        return inputArcWeightHandle;
    }

    public static void setInputArcWeightHandle(AttributeHandle inputArcWeightHandle) {
        HLAPlaceNode.inputArcWeightHandle = inputArcWeightHandle;
    }

    public static AttributeHandle getOutputArcWeightHandle() {
        return outputArcWeightHandle;
    }

    public static void setOutputArcWeightHandle(AttributeHandle outputArcWeightHandle) {
        HLAPlaceNode.outputArcWeightHandle = outputArcWeightHandle;
    }

    public Boolean getMs_OwnNumOfToken() {
        return ms_OwnNumOfToken;
    }

    public void setMs_OwnNumOfToken(Boolean ms_OwnNumOfToken) {
        this.ms_OwnNumOfToken = ms_OwnNumOfToken;
    }

    public Boolean getMs_OwnCapacity() {
        return ms_OwnCapacity;
    }

    public void setMs_OwnCapacity(Boolean ms_OwnCapacity) {
        this.ms_OwnCapacity = ms_OwnCapacity;
    }

    public Boolean getMs_OwnInputArcWeight() {
        return ms_OwnInputArcWeight;
    }

    public void setMs_OwnInputArcWeight(Boolean ms_OwnInputArcWeight) {
        this.ms_OwnInputArcWeight = ms_OwnInputArcWeight;
    }

    public Boolean getMs_OwnOutputArcWeight() {
        return ms_OwnOutputArcWeight;
    }

    public void setMs_OwnOutputArcWeight(Boolean ms_OwnOutputArcWeight) {
        this.ms_OwnOutputArcWeight = ms_OwnOutputArcWeight;
    }

    public Boolean getMs_OwnPrivilegeToDelete() {
        return ms_OwnPrivilegeToDelete;
    }

    public void setMs_OwnPrivilegeToDelete(Boolean ms_OwnPrivilegeToDelete) {
        this.ms_OwnPrivilegeToDelete = ms_OwnPrivilegeToDelete;
    }
}
