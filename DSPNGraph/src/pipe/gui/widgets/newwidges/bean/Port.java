package pipe.gui.widgets.newwidges.bean;

import org.jdom.Element;

/**交换机SW的端口类*/
public class Port {
    /**经过该端口的VL数目*/
    private int throughVLNum;
    /**该端口连接到组件SPM SW  DU */
    private Object component;

    /**该端口输入的关键Element，或者输出的Element  Element指得是SPM或者SW后面链路的最后一个元素*/
    private Element IOEle;
    /**输入或输出的组件名称*/
    private String IOStr;
    public Port() {
        throughVLNum = 0;
        component = 0;
        IOEle = null;
        IOStr = null;
    }

    public int getThroughVLNum() {
        return throughVLNum;
    }

    public void setThroughVLNum(int throughVLNum) {
        this.throughVLNum = throughVLNum;
    }

    public Object getComponent() {
        return component;
    }

    public void setComponent(Object component) {
        this.component = component;
    }

    public Element getIOEle() {
        return IOEle;
    }

    public void setIOEle(Element IOEle) {
        this.IOEle = IOEle;
    }

    public String getIOStr() {
        return IOStr;
    }

    public void setIOStr(String IOStr) {
        this.IOStr = IOStr;
    }
}
