package pipe.gui.widgets.newwidges.view;

import org.jdom.Element;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.newwidges.bean.*;
import pipe.gui.widgets.newwidges.factory.ModelFactory2;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

//import static pipe.gui.widgets.newwidges.bean.GuideModel.swCount;

/**
 * Created by hanson on 2017/8/15.
 * 第九步
 * 建模向导第步，交换机 i
 */
public class ModelGuideDialog444 extends JDialog {
    private MultiSelectComboBox<String> comboBoxIn;
    private MultiSelectComboBox<String> comboBoxOut;
    private int firstCount ;
    int indexOfSw;
    int preDUVLNum;
    ArrayList<Element> listElementOfSwPre = null;
    ArrayList<Element> arr = null;
    ArrayList<SW> swList = null;
    Element preSwLinkEnd = null;//这个应该可以是多个吧！
    SW sw;
    public ModelGuideDialog444(Frame parent, boolean modal, GuideModel guideModel, int count, ArrayList<Element> arr, Element preSwLinkEnd) {
        super(parent, modal);
        nowModel = guideModel;

        indexOfSw = GuideModel.INDEXOFSW++;
        swList = nowModel.getSwList();
        sw = swList.get(indexOfSw);
        preDUVLNum = GuideModel.PREDUVLNUM;

        //swCount = count;//交换机的数量
        firstCount = guideModel.getNumOfSW();
        this.preSwLinkEnd = preSwLinkEnd;
        this.arr = arr;
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabelin = new JLabel("                 交换机输入端口：");
        jLabelout = new JLabel("                 交换机输出端口：");
        jSeparator1 = new JSeparator();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jPanel2 = new JPanel();
        jLabel5 = new JLabel();
        jTextField1 = new JTextField();
        jPanel3 = new JPanel();
        jLabel4 = new JLabel();
        jButton4 = new JButton();
        jPanel4 = new JPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("建模向导");

        jPanel1.setBackground(new Color(255, 255, 255));

        jLabel1.setText("第九步");

        jLabel2.setText(" 请设置第"+(indexOfSw+1) +"个AFDX交换机（SW"+(indexOfSw+1)+"）的输入和输出端口参数");


        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2))
                                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                                .addContainerGap()));
        jButton2.setText("下一步");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton3.setText("取消");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });



        //开始SW业务 TODO  把SW事先创建好，然后再得到
       // sw = new SW();

        //sw.setId("SW"+(indexOfSw+1));//这里要变成动态的
        //swList.add(sw);


        int len = nowModel.getSpmList().size();
        String[] arrInStr = new String[len+indexOfSw];//这里也得加上之前的交换机
        int l = 0;
        /**在输入多选框中添加可能的输入端*/
        for(int i=0;i<len;i++)
            arrInStr[l++] = "SPM" + (i+1);
        for(int i=0;i<indexOfSw;i++){
            arrInStr[l++] = "SW" + (i+1);
        }

        comboBoxIn =  new MultiSelectComboBox<>(arrInStr);
        comboBoxIn.setPreferredSize(new Dimension(150, 26));
        comboBoxIn.setForegroundAndToPopup(Color.BLACK);
        comboBoxIn.setPopupBackground(Color.LIGHT_GRAY);
        comboBoxIn.setBackground(Color.WHITE);

        //TODO 如果是第一个交换机,或许可以根据sw的id来判断一些东西     之后怎么处理呢，后面不就重复了吗？？？？忘了，如果是第二个交换机，这里的值是多少
        listElementOfSwPre = ModelFactory2.getListOfSpmLinkEnd();
        /**复选框*/

        comboBoxIn.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                Queue<SPM> spmList = nowModel.getSpmList();
                ArrayList<SW> swList = nowModel.getSwList();

                 /**输入端的部分*/
                /*TODO 这里指的是前面的元素是SPM的情况
                 * 可以把listElementOfSwPre拿出该类外，
                 * 或者使用别的值代替这个值，然后第一次选了哪一个就在下一次的显示中删除哪一个*/
                int spmLen = spmList.size();
                int swLen = swList.size();

                List<String> names = comboBoxIn.getSelectedItems();//选择的进入端口的名字SPM1  SW0

                ArrayList<Port> inPortList = sw.getInPortList();
                int inSpmL = sw.getInSPMCount();//都是基本数据类型
                int inSwL = sw.getInSWCount();
                for(int i=0;i<names.size();i++){//这里的names包括了sw和SPM
                    Port inport = new Port();
                    String name = names.get(i);
                    inport.setIOStr(name);
                    if("SPM".equals(name.substring(0,3))){
                        inSpmL++;
                        //找到对应的SPM
                        SPM spm = getSPM(nowModel,name);
                        Element e2 = spm.getLastEle();
                        String id = e2.getAttribute("id").getValue();
                        String sName = id.substring(0,4);
                        if(sName.equals(name)){//每个端口
                            inport.setIOEle(e2);
                                 //SPM设置在这里面    //这时候通过的VL数应该以目的地为计算单位
                            int count = getThroughVLSPM(nowModel,sName,inport);
                            inport.setThroughVLNum(count);
                        }
                        inPortList.add(inport);
                        /*for(int j=0;j<listElementOfSwPre.size();j++){
                            Element e2 = listElementOfSwPre.get(j);

                        }*/
                    }/**这里指的是前面是SW的情况，只要输入端口有SW，那么肯定不是第一个SW,后面两个条件必然成立,SW这里的preSwLinkEnd只有一个？？？  肯定不是SW1*/
                    //症结在这里---preSwLinkEnd
                    else if("SW".equals(name.substring(0,2)) && indexOfSw >= 1){
                        inSwL++;//TODO 否则,上一个SW后面的链路最后一个元素应该传过来
                        if(preSwLinkEnd!=null && (GuideModel.SW2Sw3==true)){
                            String id = preSwLinkEnd.getAttribute("id").getValue();
                            String swName = id.substring(0,3);
                            if(swName.equals(name)){
                                inport.setIOEle(preSwLinkEnd);
                                for(int q=0;q<swLen;q++){
                                    SW swLast = swList.get(q);
                                    if(swName.equals(swLast.getId()))//如果选中的选项等于sw的id
                                        inport.setComponent(swLast);//然后根据这个swLast的输出端口计算到达此处sw的VL的值,这里等解决完输出的问题在解决这里
                                }
                            }
                             inPortList.add(inport);
                        }else if(preSwLinkEnd==null && (GuideModel.SW2Sw3 == false)){//目前的sw就是sw3
                            //ArrayList<Port> sw3InportList = sw.getInPortList();
                            for(int r=0;r<swList.size();r++){
                                SW swx = swList.get(r);
                                if(name.equals(swx.getId())){
                                    ArrayList<Port> outPList = swx.getOutPortList();
                                    for(int j=0;j<outPList.size();j++){
                                        Port op = outPList.get(j);
                                        if("SW3".equals(op.getIOStr())){//sw3的输入的Ele是前面两个SW最后的Ele
                                            Port sw3Inport = new Port();
                                            sw3Inport.setIOStr(swx.getId());
                                            sw3Inport.setIOEle(op.getIOEle());
                                            sw3Inport.setComponent(swx);
                                            inPortList.add(sw3Inport);
                                        }
                                    }
                                }

                            }


                        }
                    }

                }

                //开始判断通过SW中VL的数量
                int AllThroughVLNum = 0;
                if(inSwL == 0){
                    //输入接口的SW数量为0，表明这个交换机通过的VL数量有SPM决定，明显这是SW1
                    System.out.println("应该为true："+(inSpmL==inPortList.size()));
                    for(int t=0;t<inPortList.size();t++){
                        Port inport = inPortList.get(t);
                        //System.out.println(inport.getThroughVLNum());
                        AllThroughVLNum += inport.getThroughVLNum();
                    }
                    sw.setAllThroughVLNum(AllThroughVLNum);//这个属性可以再输入端口设定也可以在输出端口设定
                }else if(inSwL == 1){//往后肯定不会是SW1   //输入接口的SW数量为1，判断输入接口是否还有其他的
                    for(int t=0;t<inPortList.size();t++){// 如果前一个outPortList.size == 1 ,等于前面的sw即可
                        Port inport = inPortList.get(t);
                        Object object = inport.getComponent();
                        if(object instanceof SW){
                            SW lastSw = (SW)object;//输入端口的上一个SW
                            ArrayList<Port> outportList = lastSw.getOutPortList();
                            int lastVLNUM = lastSw.getAllThroughVLNum();

                            if(outportList.size() == 1)  //如果上一个SW只有这一个输出，加就ok
                                 AllThroughVLNum += lastVLNUM;
                            else if(outportList.size() > 1){ //判断上一个SW除此sw,是否都是DU.是，减去通往DU的VL数量
                              if(OnlyOneSW(outportList))
                                  for(int i=0;i<outportList.size();i++){
                                    Port p = outportList.get(i);
                                    if(!((p.getIOStr().substring(0,2)).equals("SW"))){
                                        lastVLNUM -= getThroughPortNum(nowModel,outportList.get(i).getIOStr());
                                    }
                                  }

                              //不是的话，意味着还有另外的SW，此处判断不了。通过SW的输出来解决。
                              AllThroughVLNum += lastVLNUM;
                            }
                        }
                        if(object instanceof SPM){
                            ArrayList<Paratition> parList = ((SPM)object).getParList();
                            for(int p=0;p<parList.size();p++){
                                Paratition par = parList.get(p);
                                AllThroughVLNum += par.getVLCount();
                            }
                        }
                    }//总数量就是这个sw的VL数量加上SPM的VL
                    sw.setAllThroughVLNum(AllThroughVLNum);
                }
                /*else if(inSwL == 2){
                    //输入接口的SW数量为2，表明这个交换机通过的VL数量由后面的输出端口所决定
                    //那么这里就什么都不做了
                    //在输出端只要是AllThroughVLNum还为0，那么直接由输出的DU决定就好
                }*/

                sw.setInSPMCount(inSpmL);
                sw.setInSWCount(inSwL);
                sw.setInPortList(inPortList);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
//至此输入端口结束

        /**输出端的部分*/
        int duLen = nowModel.getNumOfDU();
        String[] arrOutStr = new String[nowModel.getNumOfSW()-1+duLen];
        int q=0;
        while(q<duLen){
            arrOutStr[q] = "RES" + (q+1) ;
            q++;
        }
        //System.out.println("GuideModel.swCount"+GuideModel.swCount);
        if(GuideModel.swCount > 1)
            for(int i=1;i<nowModel.getNumOfSW();i++){
                String s = "SW" + (i+1);
                if(sw.getId().equals(s)){
                    continue;
                }
                arrOutStr[q++] = s;
            }
        comboBoxOut = new MultiSelectComboBox<>(arrOutStr);//输出是其他的交换机或者是DU
        comboBoxOut.setPreferredSize(new Dimension(150, 26));
        comboBoxOut.setForegroundAndToPopup(Color.BLACK);
        comboBoxOut.setPopupBackground(Color.LIGHT_GRAY);
        comboBoxOut.setBackground(Color.WHITE);
        comboBoxOut.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                ArrayList<SW> swList = nowModel.getSwList();
                Queue<DU> duList = nowModel.getDuList();
                ArrayList<Port> outPortList = sw.getOutPortList();
                int duLen = duList.size();
                int swLen = swList.size();
                List<String> names = comboBoxOut.getSelectedItems();
                int duCount = sw.getOutDUCount();
                int swC = sw.getOutSWCount();
                for(int i=0;i<names.size();i++){//这里的names包括了sw和du
                    Port outPort = new Port();
                    String name = names.get(i);
                    outPort.setIOStr(name);
                    //当输出可能是一个DU的时候
                    if("RES".equals(name.substring(0,3))){
                        duCount++;
                        DU du = findDesDu(nowModel,name);
                        outPort.setComponent(du);
                        //通过当前端口的VL的数量，查找目的地是它的虚链路的数量
                        int throuthVLNum = getThroughPortNum(nowModel,du.getId());
                        outPort.setThroughVLNum(throuthVLNum);
                    }else if("SW".equals(name.substring(0,2))){//我们假设最多3个交换机
                        swC++;
                         for(int q=0;q<swLen;q++){
                             SW outsw = swList.get(q);
                             if(name.equals(outsw.getId()))
                                 outPort.setComponent(outsw);
                             /**如果是交换机的话，通过的VL数和Ele就不在set了*/
                         }
                    }
                    outPortList.add(outPort);
                }
                int AllThroughVLNum = 0;
                if(sw.getAllThroughVLNum() == 0){
                    //说明这在输入端还不能获得通过的VL数量，只能通过输出来确定
                    //此时的输出端口应该都是DU
                    System.out.println("zhi应该为0:"+swC);
                    for(int t=0;t<outPortList.size();t++){
                        Port outport = outPortList.get(t);
                        AllThroughVLNum += outport.getThroughVLNum();
                    }
                    sw.setAllThroughVLNum(AllThroughVLNum);
                }

                sw.setOutDUCount(duCount);
                sw.setOutSWCount(swC);
                sw.setOutPortList(outPortList);
                //sw.setOut(names);//输入只可能是前面的SPM
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });



        GroupLayout jPanel33Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel33Layout);
        GroupLayout.SequentialGroup vGroup = jPanel33Layout.createSequentialGroup();
        vGroup.addGap(10);
        vGroup.addGroup(jPanel33Layout.createParallelGroup().addComponent(jLabelin)
                .addComponent(comboBoxIn));
        vGroup.addGap(5);
        vGroup.addGroup(jPanel33Layout.createParallelGroup().addComponent(jLabelout)
                .addComponent(comboBoxOut));
        vGroup.addGap(10);
        //设置垂直组
        jPanel33Layout.setVerticalGroup(vGroup);



        GroupLayout.SequentialGroup hGroup = jPanel33Layout.createSequentialGroup();
        hGroup.addGap(5);//添加间隔
        hGroup.addGroup(jPanel33Layout.createParallelGroup().addComponent(jLabelin)
                .addComponent(jLabelout));
        hGroup.addGap(5);
        hGroup.addGroup(jPanel33Layout.createParallelGroup().addComponent(comboBoxIn)
                .addComponent(comboBoxOut));
        hGroup.addGap(5);
        jPanel33Layout.setHorizontalGroup(hGroup);

        jLabel4.setText("根据系统情况，设置AFDX交换机的输入输出端口参数");

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(42, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addGap(26, 26, 26))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jLabel4)
                                .addContainerGap(101, Short.MAX_VALUE))
        );

        jButton4.setText("上一步");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(307, Short.MAX_VALUE)
                                .addComponent(jButton4)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3)
                                .addGap(8, 8, 8))
                        .addComponent(jSeparator1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(79, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton3)
                                        .addComponent(jButton2)
                                        .addComponent(jButton4))
                                .addContainerGap())
        );

        pack();
    }

    private SPM getSPM(GuideModel nowModel, String name) {
        Queue<SPM> spmList = nowModel.getSpmList();
        int len = spmList.size();
        SPM spm = null;
        for(int i=0;i<len;i++){
            SPM spm1 = spmList.remove();
            if(spm1.getId().equals(name)){
                spm = spm1;
            }
            spmList.add(spm1);
        }
        return spm;
    }

    /**判断输出只有一个SW是否都是DU*/
    private boolean OnlyOneSW(ArrayList<Port> outportList) {
         int swCount = 0;
        for(int i=0;i<outportList.size();i++){
            Port port = outportList.get(i);
            if(port.getIOStr().substring(0,2).equals("SW"))
                swCount++;
        }
        if(swCount == 1)
            return true;
        return false;
    }

    /**获得当前name的SPM中VL的数量*/
    private int getThroughVLSPM(GuideModel nowModel, String sName,Port inport) {
        int count = 0;
        Queue<SPM> spmList = nowModel.getSpmList();
        int spmLen = spmList.size();
        for(int q=0;q<spmLen;q++){
            SPM spm = spmList.remove();
            //SPM设置在这里
            inport.setComponent(spm);
            ArrayList<VLInfo> infoList = spm.getInfoList();
            if(sName.equals(spm.getId())){
                for(int r=0;r<infoList.size();r++){
                    VLInfo info = infoList.get(r);
                    count += info.getDestination().size();
                }
            }
            spmList.add(spm);
        }
        return count;
    }

    /**通过当前端口、目的地是du的VL的数量*/
    private int getThroughPortNum(GuideModel nowModel, String duId) {
        int count = 0;
         Queue<VLInfo> infoList = nowModel.getVlList();
         int infoLen = infoList.size();
         for(int t=0;t<infoLen;t++){
             VLInfo info = infoList.remove();
             ArrayList<String> desList= info.getDestination();
             for(int j=0;j<desList.size();j++){
                 String desStr = desList.get(j);
                 String des = desStr.substring(0,4);
                 if(des.equals(duId)){
                     count++;
                 }
             }
             infoList.add(info);
         }
         return count;
    }

    private static DU findDesDu(GuideModel guideModel,String spmId){
        DU desDu = null;
        Queue<DU> duList = guideModel.getDuList();
        int duNum  = guideModel.getNumOfDU();

        for(int i=0;i<duNum;i++){
            DU du = duList.remove();//看看有没有
            String duId = du.getId().substring(0,4);
            if(duId.equals(spmId))//如果相等就是那个，然后再创建分区，确定这个分区的链路数量再创建
                desDu = du;
            duList.add(du);
        }
        return desDu;
    }
    private void jButton2ActionPerformed(ActionEvent evt) {//下一步

        List<String> arrOut = new ArrayList<>();
        ArrayList<String> outStr = new ArrayList<>();
        //String swId = sw.getId();

        ArrayList<Port> outPortList = sw.getOutPortList();
        for(int i=0;i<outPortList.size();i++){
            Port port = outPortList.get(i);
            arrOut.add(port.getIOStr());
            outStr.add(port.getIOStr());
        }
        int outNum = arrOut.size();
        Element net = arr.get(1);

        //直接在函数内将最后的节点保存到SW中
        int xVar = indexOfSw;
        int yVar = preDUVLNum;
         //System.out.println(xVar+","+yVar);
        if(GuideModel.Paralable == true){
            GuideModel.setParalable(false);
            xVar -= 1;
           // System.out.println(xVar+","+yVar);
            ModelFactory2.addSwitch(nowModel,net,1610+ xVar*1600,515+yVar*70+300 ,sw,outNum);//添加交换机S0
            GuideModel.AfterParalable = true;
        }else{
            //System.out.println(xVar+","+yVar);
            if(GuideModel.AfterParalable==true){
                 xVar--;
                 GuideModel.AfterParalable = false;
                 GuideModel.AfterAfterParalable = true;
            }
            ModelFactory2.addSwitch(nowModel,net,1610+ xVar*1600,515+yVar*70 ,sw,outNum);//添加交换机S0
        }

        /**选择的输出端口*/
        String str = null;
         for(int i=0;i<outPortList.size();i++) {
             Port oport = outPortList.get(i);
             str = oport.getIOStr();
             if("RES".equals(str.substring(0,3))){
                 Element swLinkEnd = oport.getIOEle();
                 DU desDu = findDesDu(nowModel,str);
                 int duxVar = indexOfSw;
                 if(GuideModel.AfterAfterParalable == true){
                     duxVar--;
                     ModelFactory2.addDU(nowModel,net,desDu,2650 + duxVar*1600,315+preDUVLNum*60 + i*500,swLinkEnd);
                 }else
                     ModelFactory2.addDU(nowModel,net,desDu,2650 + duxVar*1600,315+preDUVLNum*60 + i*500,swLinkEnd);//当前DU的名字必须作为参数,DU前自动添加链路
                 int sum = 0;
                 ArrayList<Paratition> paratitions = desDu.getParList();
                 int parNUm = desDu.getNumOfPar();
                 for(int t=0;t<paratitions.size();t++){
                    sum += paratitions.get(t).getVLCount();
                 }
                 preDUVLNum = sum + parNUm;
                 GuideModel.PREDUVLNUM = preDUVLNum;
             }else if("SW".equals(str.substring(0,2)) && (GuideModel.swCount--)>1){
                 Element swLinkEnd = oport.getIOEle();
                 if("SW1".equals(sw.getId()) && "SW3".equals(str)){//此时说明SW1 和 SW2 是并列的
                      GuideModel.Paralable = true;
                    doClose(RET_OK);
                    ModelGuideDialog444 guiDialog =  new ModelGuideDialog444(ApplicationSettings.getApplicationView(), true, nowModel,GuideModel.swCount,arr,null);
                    guiDialog.pack();
                    guiDialog.setLocationRelativeTo(null);
                    guiDialog.setVisible(true);
                 }else if("SW2".equals(sw.getId()) && "SW3".equals(str)){//创建SW2，且与SW1无关， 的时候
                     GuideModel.SW2Sw3 = false;

                    doClose(RET_OK);
                    ModelGuideDialog444 guiDialog =  new ModelGuideDialog444(ApplicationSettings.getApplicationView(), true, nowModel,GuideModel.swCount,arr,null);
                    guiDialog.pack();
                    guiDialog.setLocationRelativeTo(null);
                    guiDialog.setVisible(true);
                 }else{ //包括"SW2".equals(sw.getId()) && "SW3".equals(str)   等其他的 TODO 当时S3的时候，或许传null比较好
                    doClose(RET_OK);
                    ModelGuideDialog444 guiDialog =  new ModelGuideDialog444(ApplicationSettings.getApplicationView(), true, nowModel,GuideModel.swCount,arr,swLinkEnd);
                    guiDialog.pack();
                    guiDialog.setLocationRelativeTo(null);
                    guiDialog.setVisible(true);
                 }
             }
         }

        System.out.println("111"+sw.getId());
        //for结束才能让它进来   里是最后一个SW的时候才跳到Dialog5 str===SW3
        if(GuideModel.swCount == 1 && GuideModel.FLAG &&(str.equals("SW"+firstCount) || "RES".equals(str.substring(0,3)))){
            //TODO
            GuideModel.AfterAfterParalable = false;
            doClose(RET_OK);
            ModelGuideDialog5 guiDialog =  new ModelGuideDialog5(ApplicationSettings.getApplicationView(), true, nowModel,arr);
            GuideModel.FLAG = false;
            guiDialog.pack();
            guiDialog.setLocationRelativeTo(null);
            guiDialog.setVisible(true);
        }
         System.out.println("222"+sw.getId());
        if(firstCount>1)
            dispose();
        System.out.println("333"+sw.getId());
        /*else if((GuideModel.swCount--) > 1 && GuideModel.FLAG){
            //跳转另一个交换机页面，此时的输入：之前的交换机和SPM
            //输出：之后的交换机和DU
            doClose(RET_OK);
            ModelGuideDialog444 guiDialog =  new ModelGuideDialog444(ApplicationSettings.getApplicationView(), true, nowModel,GuideModel.swCount,arr,swLinkEnd2);
            guiDialog.pack();
            guiDialog.setLocationRelativeTo(null);
            guiDialog.setVisible(true);
        }*/
    }


    private void jButton3ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        doClose(RET_CANCEL);
    }
    private void jButton4ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:   上一步


        GuideModel.INDEXOFSW = 0;
        GuideModel.PREDUVLNUM = 0;
        GuideModel.SW2Sw3 = true;
        GuideModel.FLAG = true;
        GuideModel.AfterAfterParalable = false;
        GuideModel.AfterParalable = false;
        GuideModel.Paralable = false;
        sw.setInPortList(new ArrayList<>());
        sw.setOutPortList(new ArrayList<>());
        sw.setOutSWCount(0);
        sw.setOutDUCount(0);
        sw.setInSWCount(0);
        sw.setInSPMCount(0);
        doClose(RET_CANCEL);
        /**这里只能倒回到Dialog4这一步，因为涉及到了文件的写入写出和初始化工作*/
        ModelGuideDialog4 guiDialog =  new ModelGuideDialog4(ApplicationSettings.getApplicationView(), true, nowModel);
        //System.out.println(nowModel+"*********");
        guiDialog.pack();
        guiDialog.setLocationRelativeTo(null);
        guiDialog.setVisible(true);

    }
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    // Variables declaration - do not modify
    private GuideModel nowModel;
    //private ButtonGroup buttonGroup1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabelin;
    private JLabel jLabelout;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JPanel jPanel4;


    private JSeparator jSeparator1;

    private JTextField jTextField1;
    // End of variables declaration
    private int returnStatus = RET_CANCEL;


    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
}
 /*
                for(int i=0;i<listElementOfSwPre.size();i++){
                    Element e2 = listElementOfSwPre.get(i);
                    String id = e2.getAttribute("id").getValue();
                    //System.out.println("id161:"+id);
                    String sName = id.substring(0,4);

                    for(String s : names){//TODO  这么做不一定对
                        if(sName.equals(s)){// || swName.equals(s)){
                            Port inPort = new Port();
                            inPort.setIOEle(e2);
                            inPort.setIOStr(s);
                            for(int q=0;q<spmLen;q++){
                                SPM spm = spmList.remove();
                                if(sName.equals(spm.getId())){
                                    int count = 0;
                                    ArrayList<Paratition> parList = spm.getParList();
                                    for(int p=0;p<parList.size();p++){
                                        Paratition par = parList.get(p);
                                        count += par.getVLCount();
                                    }
                                    inPort.setThroughVLNum(count);
                                    inPort.setComponent(spm);

                                }
                                spmList.add(spm);
                            }
                            //swIn.add(e2);//把交换机后面的链路命名也加上了SW为前缀
                            //inStr.add(s);
                            inPortList.add(inPort);
                            break;
                        }
                    }
                }


                if(indexOfSw >= 1 && preSwLinkEnd!=null){   //TODO 否则,上一个SW后面的链路最后一个元素应该传过来
                    String id = preSwLinkEnd.getAttribute("id").getValue();
                    System.out.println("id"+id);
                    String swName = id.substring(0,3);
                    for(String s : names){//names包括了SPM和SW
                        if(swName.equals(s)){
                            Port inPort = new Port();
                            inPort.setIOEle(preSwLinkEnd);
                            inPort.setIOStr(s);
                            for(int q=0;q<swLen;q++){
                                SW swLast = swList.get(q);
                                //如果选中的选项等于sw的id
                                if(swName.equals(swLast.getId())){
                                    inPort.setComponent(swLast);
                                    //TODO 然后根据这个swLast的输出端口计算到达此处sw的VL的值,这里等解决完输出的问题在解决这里
                                    //TODO inPort.setThroughVLNum();
                                }
                                //swList.add(sw);
                            }
                            //swIn.add(preSwLinkEnd);
                            break;//应该去掉吧
                         }
                    }
                }*/


        /* for(int i=0;i<arrOut.size();i++){  //数组 *先* 添加的DU 后添加的SW String str:arrOut
            str = arrOut.get(i);
            //System.out.println("out"+str);
            if("RES".equals(str.substring(0,3))){//添加DU
                Element swLinkEnd = listOfSWLinkEnd.get(i);
                //System.out.println("i:"+i + ",indexOfSw:"+indexOfSw);
                DU desDu = findDesDu(nowModel,str);

                //添加第i个DU
                ModelFactory2.addDU(nowModel,net,desDu,2650 + indexOfSw*1600,315+preDUVLNum*60 + i*500,swLinkEnd);//当前DU的名字必须作为参数,DU前自动添加链路
                int sum = 0;
                ArrayList<Paratition> paratitions = desDu.getParList();
                int parNUm = desDu.getNumOfPar();
                for(int t=0;t<paratitions.size();t++){
                    sum += paratitions.get(t).getVLCount();
                }
                preDUVLNum = sum + parNUm;
                GuideModel.PREDUVLNUM = preDUVLNum;
            }else if("SW".equals(str.substring(0,2)) && (GuideModel.swCount--)>1){//    SW3,当这里是最后一个SW的时候才跳到5
                if("SW1".equals(sw.getId()) && "SW3".equals(str)){//此时说明SW1 和 SW2 是并列的
                    doClose(RET_OK);
                    ModelGuideDialog444 guiDialog =  new ModelGuideDialog444(ApplicationSettings.getApplicationView(), true, nowModel,GuideModel.swCount,arr,null);
                    guiDialog.pack();
                    guiDialog.setLocationRelativeTo(null);
                    guiDialog.setVisible(true);
                }else{
                     //如果是最初的数量直接结束,获取第i个交换机后面链路最后的结点
                    Element swLinkEnd = listOfSWLinkEnd.get(i);
                    //System.out.println(swLinkEnd.getAttribute("id").getValue());
                    //跳转另一个交换机页面，此时的输入：之前的交换机和SPM    输出：之后的交换机和DU
                    doClose(RET_OK);
                    ModelGuideDialog444 guiDialog =  new ModelGuideDialog444(ApplicationSettings.getApplicationView(), true, nowModel,GuideModel.swCount,arr,swLinkEnd);
                    guiDialog.pack();
                    guiDialog.setLocationRelativeTo(null);
                    guiDialog.setVisible(true);
                }

            }

        }*/