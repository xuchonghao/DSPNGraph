package pipe.hla.SimDSPNModule.unuse;

import hla.rti1516.AttributeHandle;

public class HLAPetriNetNode {
    private int id;
    private String name;
    private String lastNodeName;
    private String nextNodeName;

    /*对象实例更新标志*/
    boolean  ms_IdUpdate;
    boolean  ms_NameUpdate;
    boolean  ms_LastNodeNameUpdate;
    boolean  ms_NextNodeNameUpdate;

    /*对象类属性句柄*/
    static AttributeHandle idHandle;
    static AttributeHandle nameHandle;
    static AttributeHandle lastNodeNameHandle;
    static AttributeHandle nextNodeNameHandle;



    /*所有权拥有标志*/
    Boolean   ms_OwnId;
    Boolean   ms_OwnName;
    Boolean   ms_OwnLastNodeName;
    Boolean   ms_OwnNextNodeName;
    Boolean ms_OwnPrivilegeToDelete;

    public HLAPetriNetNode(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isMs_IdUpdate() {
        return ms_IdUpdate;
    }

    public void setMs_IdUpdate(boolean ms_IdUpdate) {
        this.ms_IdUpdate = ms_IdUpdate;
    }

    public boolean isMs_NameUpdate() {
        return ms_NameUpdate;
    }

    public void setMs_NameUpdate(boolean ms_NameUpdate) {
        this.ms_NameUpdate = ms_NameUpdate;
    }

    public boolean isMs_LastNodeNameUpdate() {
        return ms_LastNodeNameUpdate;
    }

    public void setMs_LastNodeNameUpdate(boolean ms_LastNodeNameUpdate) {
        this.ms_LastNodeNameUpdate = ms_LastNodeNameUpdate;
    }

    public boolean isMs_NextNodeNameUpdate() {
        return ms_NextNodeNameUpdate;
    }

    public void setMs_NextNodeNameUpdate(boolean ms_NextNodeNameUpdate) {
        this.ms_NextNodeNameUpdate = ms_NextNodeNameUpdate;
    }

    public static AttributeHandle getIdHandle() {
        return idHandle;
    }

    public static void setIdHandle(AttributeHandle idHandle) {
        HLAPetriNetNode.idHandle = idHandle;
    }

    public static AttributeHandle getNameHandle() {
        return nameHandle;
    }

    public static void setNameHandle(AttributeHandle nameHandle) {
        HLAPetriNetNode.nameHandle = nameHandle;
    }

    public static AttributeHandle getLastNodeNameHandle() {
        return lastNodeNameHandle;
    }

    public static void setLastNodeNameHandle(AttributeHandle lastNodeNameHandle) {
        HLAPetriNetNode.lastNodeNameHandle = lastNodeNameHandle;
    }

    public static AttributeHandle getNextNodeNameHandle() {
        return nextNodeNameHandle;
    }

    public static void setNextNodeNameHandle(AttributeHandle nextNodeNameHandle) {
        HLAPetriNetNode.nextNodeNameHandle = nextNodeNameHandle;
    }

    public Boolean getMs_OwnId() {
        return ms_OwnId;
    }

    public void setMs_OwnId(Boolean ms_OwnId) {
        this.ms_OwnId = ms_OwnId;
    }

    public Boolean getMs_OwnName() {
        return ms_OwnName;
    }

    public void setMs_OwnName(Boolean ms_OwnName) {
        this.ms_OwnName = ms_OwnName;
    }

    public Boolean getMs_OwnLastNodeName() {
        return ms_OwnLastNodeName;
    }

    public void setMs_OwnLastNodeName(Boolean ms_OwnLastNodeName) {
        this.ms_OwnLastNodeName = ms_OwnLastNodeName;
    }

    public Boolean getMs_OwnNextNodeName() {
        return ms_OwnNextNodeName;
    }

    public void setMs_OwnNextNodeName(Boolean ms_OwnNextNodeName) {
        this.ms_OwnNextNodeName = ms_OwnNextNodeName;
    }

    public Boolean getMs_OwnPrivilegeToDelete() {
        return ms_OwnPrivilegeToDelete;
    }

    public void setMs_OwnPrivilegeToDelete(Boolean ms_OwnPrivilegeToDelete) {
        this.ms_OwnPrivilegeToDelete = ms_OwnPrivilegeToDelete;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastNodeName() {
        return lastNodeName;
    }

    public void setLastNodeName(String lastNodeName) {
        this.lastNodeName = lastNodeName;
    }

    public String getNextNodeName() {
        return nextNodeName;
    }

    public void setNextNodeName(String nextNodeName) {
        this.nextNodeName = nextNodeName;
    }
}
