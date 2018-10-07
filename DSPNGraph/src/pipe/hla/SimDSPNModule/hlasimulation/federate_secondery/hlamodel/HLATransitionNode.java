package pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.hlamodel;

import hla.rti1516.*;
import pipe.hla.SimDSPNModule.hlasimulation.federate_secondery.auxiliary.Constant;
import pipe.hla.SimDSPNModule.ordinarysimulation.ordinarymodel.TransitionNode;

import java.util.ArrayList;
import java.util.Iterator;

public class HLATransitionNode extends TransitionNode {

    public HLATransitionNode(String name) {
        super(name);

        //TransitionNodeArray.add(this);

        System.out.println("TransitionNode  Ok!!!");
    }


    LogicalTimeInterval Lookahead;
    LogicalTime CurrentTime;
    boolean regulating;

    //对象实例句柄
    private ObjectInstanceHandle m_InstanceId;

    /*对象实例更新标志*/
    boolean  ms_IsEnabledUpdate;
    boolean  ms_DelayUpdate;
    boolean  ms_RateUpdate;
    boolean  ms_TypeUpdate;
    boolean ms_InstanceDeleted;

    /*对象类及属性句柄*/
    static ObjectClassHandle transitionNodeHandle;
    static AttributeHandle isEnabledHandle;
    static AttributeHandle delayHandle;
    static AttributeHandle typeHandle;
    static AttributeHandle rateHandle;


    /*所有权拥有标志*/
    Boolean   ms_OwnIsEnabled;
    Boolean   ms_OwnDelay;
    Boolean   ms_OwnRate;
    Boolean   ms_OwnType;
    Boolean ms_OwnPrivilegeToDelete;

    //
    static boolean ms_doRegistry;

    static RTIambassador rti = null;


    static void Init(RTIambassador amb_rti ){
        rti = amb_rti;
        if(rti != null){
            try {
                transitionNodeHandle = rti.getObjectClassHandle(Constant.ms_TransitionNodeTypeStr);
                System.out.println("transitionNodeHandle:"+transitionNodeHandle);

                idHandle = rti.getAttributeHandle(transitionNodeHandle, Constant.ms_IdTypeStr);
                nameHandle = rti.getAttributeHandle(transitionNodeHandle, Constant.ms_NameTypeStr);
                lastNodeNameHandle = rti.getAttributeHandle(transitionNodeHandle, Constant.ms_LastNodeNameTypeStr);
                nextNodeNameHandle = rti.getAttributeHandle(transitionNodeHandle, Constant.ms_NextNodeNameTypeStr);
                System.out.println("idHandle:"+idHandle);
                System.out.println("nameHandle:"+nameHandle);
                System.out.println("lastNodeNameHandle:"+lastNodeNameHandle);
                System.out.println("nextNodeNameHandle:"+nextNodeNameHandle);

                isEnabledHandle = rti.getAttributeHandle(transitionNodeHandle, Constant.ms_IsEnabledTypeStr);
                delayHandle = rti.getAttributeHandle(transitionNodeHandle, Constant.ms_DelayTypeStr);
                rateHandle = rti.getAttributeHandle(transitionNodeHandle, Constant.ms_RateTypeStr);
                typeHandle = rti.getAttributeHandle(transitionNodeHandle, Constant.ms_TypeTypeStr);

                System.out.println("isEnabledHandle:"+isEnabledHandle);
                System.out.println("delayHandle:"+delayHandle);
                System.out.println("rateHandle:"+rateHandle);
                System.out.println("typeHandle:"+typeHandle);

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

                attributeSet.add(isEnabledHandle);
                attributeSet.add(delayHandle);
                attributeSet.add(rateHandle);
                attributeSet.add(typeHandle);

                rti.publishObjectClassAttributes(transitionNodeHandle,attributeSet);

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
            System.out.println("*** published objectclass TransitionNode OK!");
        }
    }
    /*我感觉成员之间不需要订购Place或者Transition对象的实例
     public void Subscribing(){
         if(rti != null){
             AttributeHandleSet attributeSet = null;
             try {
                 attributeSet = rti.getAttributeHandleSetFactory().create();

                 attributeSet.add(idHandle);
                 attributeSet.add(nameHandle);
                 attributeSet.add(lastNodeNameHandle);
                 attributeSet.add(nextNodeNameHandle);

                 attributeSet.add(isEnabledHandle);
                 attributeSet.add(delayHandle);
                 attributeSet.add(rateHandle);
                 attributeSet.add(typeHandle);

                 rti.subscribeObjectClassAttributes(transitionNodeHandle,attributeSet);

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
             for(int i=0;i<TransitionNodeArray.size();i++){
                 TransitionNode transitionNode = TransitionNodeArray.get(i);
                 try {
                     transitionNode.m_InstanceId = rti.registerObjectInstance(transitionNode.transitionNodeHandle,transitionNode.getName());
                     System.out.println("transitionNode.transitionNodeHandle:"+transitionNode.getM_InstanceId() + "name:"+transitionNode.getName());
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
    public void DeleteAllInstance(ArrayList<HLATransitionNode> transitionNodes){
        for(int i=0;i<transitionNodes.size();i++){
            HLATransitionNode transitionNode = transitionNodes.get(i);
            if(transitionNode.isMs_InstanceDeleted() && transitionNode.getMs_OwnPrivilegeToDelete()){
                try {
                    if(regulating){
                        rti.deleteObjectInstance(transitionNode.getM_InstanceId(),"TransitionNode deleted".getBytes(),CurrentTime.add(Lookahead));
                    }else {
                        rti.deleteObjectInstance(transitionNode.getM_InstanceId(),"TransitionNode deleted".getBytes());
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
            transitionNode.setMs_OwnPrivilegeToDelete(false);
        }
    }
    public void TransitionNodeUpdate(HLATransitionNode transitionNode){
        AttributeHandleValueMap attributeValueMap = null;
        try {
            attributeValueMap = rti.getAttributeHandleValueMapFactory().create(8);
        } catch (FederateNotExecutionMember federateNotExecutionMember) {
            federateNotExecutionMember.printStackTrace();
        }

        if(transitionNode.isMs_IsEnabledUpdate()){
            boolean isEnabled = transitionNode.isEnabled();
            attributeValueMap.put(transitionNode.getM_InstanceId(),isEnabled);
            transitionNode.setMs_IsEnabledUpdate(false);
        }
        if(transitionNode.isMs_RateUpdate()){
            double rate = transitionNode.getRate();
            attributeValueMap.put(transitionNode.getM_InstanceId(),rate);
            transitionNode.setMs_RateUpdate(false);
        }
        if(transitionNode.isMs_TypeUpdate()){
            int type = transitionNode.getType();
            attributeValueMap.put(transitionNode.getM_InstanceId(),type);
            transitionNode.setMs_TypeUpdate(false);
        }
        if(transitionNode.isMs_DelayUpdate()){
            double delay = transitionNode.getDelay();
            attributeValueMap.put(transitionNode.getM_InstanceId(),delay);
            transitionNode.setMs_DelayUpdate(false);
        }
        if(attributeValueMap.size() > 0){

            try {
                if(regulating){
                    Lookahead = rti.queryLookahead();
                    rti.updateAttributeValues(transitionNode.getM_InstanceId(),attributeValueMap,"TransitionNodeUpdate ".getBytes(),CurrentTime.add(Lookahead));
                }else{
                    rti.updateAttributeValues(transitionNode.getM_InstanceId(),attributeValueMap,"TransitionNodeUpdate ".getBytes());
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
            System.out.println("****Update"+transitionNode.getName() +"OK!");
        }

        System.out.println("****TransitionNode 数组为空");
    }

    public void SetUpdateControl(boolean status,AttributeHandleSet theAttrHandles){
        Iterator<AttributeHandle> it = theAttrHandles.iterator();
        AttributeHandle attributeHandle = null;
        while(it.hasNext()){
            attributeHandle = it.next();

            if(isEnabledHandle == attributeHandle){
                ms_IsEnabledUpdate = status;
                System.out.println("FED_HW: Turning ms_IsEnabledUpdate Updates"+status);
            }
            if(delayHandle == attributeHandle){
                ms_DelayUpdate = status;
                System.out.println("FED_HW: Turning ms_DelayUpdate Updates"+status);
            }
            if(rateHandle == attributeHandle){
                ms_RateUpdate = status;
                System.out.println("FED_HW: Turning ms_RateUpdate Updates"+status);
            }
            if(typeHandle == attributeHandle){
                ms_TypeUpdate = status;
                System.out.println("FED_HW: Turning ms_TypeUpdate Updates"+status);
            }


        }
    }


    public HLATransitionNode getWillFiredTransiton(){

        return null;
    }
    //TODO 这个必须是原子的
    public void Fire(TokenInteraction tokenInteraction){
        //先发送一个交互
        //异步的去掉前面库所的token
        //并等待后面的返回，并判断
    }
    /**TODO 这里有些疑问？？？我这样使用可以吗，是不是不再符合HLA的规范，怎么获取实例的数据呢？
    public boolean isEnabled(HLATransitionNode transitionNode) {
        String lastNodeName = transitionNode.getLastNodeName();
        HLAPlaceNode placeNode = MyTest.FindP(lastNodeName);
        int numOfToken = placeNode.getNumOfToken();
        int outArcWeight = placeNode.getOutputArcWeight();
        if(numOfToken >= outArcWeight)
            setEnabled(true);
        else
            setEnabled(false);
        return isEnabled;
    }*/

    //get and set method
    public static boolean isMs_doRegistry() {
        return ms_doRegistry;
    }

    public static void setMs_doRegistry(boolean status) {
        HLATransitionNode.ms_doRegistry = status;
    }

    public ObjectInstanceHandle getM_InstanceId() {
        return m_InstanceId;
    }

    public void setM_InstanceId(ObjectInstanceHandle m_InstanceId) {
        this.m_InstanceId = m_InstanceId;
    }

    public boolean isMs_IsEnabledUpdate() {
        return ms_IsEnabledUpdate;
    }

    public void setMs_IsEnabledUpdate(boolean ms_IsEnabledUpdate) {
        this.ms_IsEnabledUpdate = ms_IsEnabledUpdate;
    }

    public boolean isMs_DelayUpdate() {
        return ms_DelayUpdate;
    }

    public void setMs_DelayUpdate(boolean ms_DelayUpdate) {
        this.ms_DelayUpdate = ms_DelayUpdate;
    }

    public boolean isMs_RateUpdate() {
        return ms_RateUpdate;
    }

    public void setMs_RateUpdate(boolean ms_RateUpdate) {
        this.ms_RateUpdate = ms_RateUpdate;
    }

    public boolean isMs_TypeUpdate() {
        return ms_TypeUpdate;
    }

    public void setMs_TypeUpdate(boolean ms_TypeUpdate) {
        this.ms_TypeUpdate = ms_TypeUpdate;
    }

    public boolean isMs_InstanceDeleted() {
        return ms_InstanceDeleted;
    }

    public void setMs_InstanceDeleted(boolean ms_InstanceDeleted) {
        this.ms_InstanceDeleted = ms_InstanceDeleted;
    }

    public static ObjectClassHandle getTransitionNodeHandle() {
        return transitionNodeHandle;
    }

    public static void setTransitionNodeHandle(ObjectClassHandle transitionNodeHandle) {
        HLATransitionNode.transitionNodeHandle = transitionNodeHandle;
    }

    public static AttributeHandle getIsEnabledHandle() {
        return isEnabledHandle;
    }

    public static void setIsEnabledHandle(AttributeHandle isEnabledHandle) {
        HLATransitionNode.isEnabledHandle = isEnabledHandle;
    }

    public static AttributeHandle getDelayHandle() {
        return delayHandle;
    }

    public static void setDelayHandle(AttributeHandle delayHandle) {
        HLATransitionNode.delayHandle = delayHandle;
    }

    public static AttributeHandle getTypeHandle() {
        return typeHandle;
    }

    public static void setTypeHandle(AttributeHandle typeHandle) {
        HLATransitionNode.typeHandle = typeHandle;
    }

    public static AttributeHandle getRateHandle() {
        return rateHandle;
    }

    public static void setRateHandle(AttributeHandle rateHandle) {
        HLATransitionNode.rateHandle = rateHandle;
    }

    public Boolean getMs_OwnIsEnabled() {
        return ms_OwnIsEnabled;
    }

    public void setMs_OwnIsEnabled(Boolean ms_OwnIsEnabled) {
        this.ms_OwnIsEnabled = ms_OwnIsEnabled;
    }

    public Boolean getMs_OwnDelay() {
        return ms_OwnDelay;
    }

    public void setMs_OwnDelay(Boolean ms_OwnDelay) {
        this.ms_OwnDelay = ms_OwnDelay;
    }

    public Boolean getMs_OwnRate() {
        return ms_OwnRate;
    }

    public void setMs_OwnRate(Boolean ms_OwnRate) {
        this.ms_OwnRate = ms_OwnRate;
    }

    public Boolean getMs_OwnType() {
        return ms_OwnType;
    }

    public void setMs_OwnType(Boolean ms_OwnType) {
        this.ms_OwnType = ms_OwnType;
    }

    public Boolean getMs_OwnPrivilegeToDelete() {
        return ms_OwnPrivilegeToDelete;
    }

    public void setMs_OwnPrivilegeToDelete(Boolean ms_OwnPrivilegeToDelete) {
        this.ms_OwnPrivilegeToDelete = ms_OwnPrivilegeToDelete;
    }
}

