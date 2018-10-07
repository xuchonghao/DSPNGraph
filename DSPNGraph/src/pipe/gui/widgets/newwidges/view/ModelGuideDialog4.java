package pipe.gui.widgets.newwidges.view;

import org.jdom.Element;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.newwidges.bean.*;
import pipe.gui.widgets.newwidges.factory.ModelFactory2;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by hanson on 2017/8/22.
 * 第七步，补全链路的信息
 */
public class ModelGuideDialog4 extends JDialog {
    /**用来暂时存储所有的链路*/
    private ArrayList<VLInfo> vlList = new ArrayList<VLInfo>();

    private GuideModel nowModel;

    private String[] id = new String[100];

    private int[] typeOfMessage = new int[100];

    private String[] remarkOfRT = new String[100];

    private Set<String> idCheckSet = new HashSet<String>();

    //private int[] delay = new int[100];

    private int[] bag = new int[100];

    private int[] cache = new int[100];

    private ArrayList<String>[] destin = new ArrayList[100];

    private int[] packageSize = new int[100];

    private int num;

    private ArrayList<Element> arr = null;

    /** Creates new form ModelGuideDialog1 */
    public ModelGuideDialog4(Frame parent, boolean modal, GuideModel guideModel) {
        super(parent, modal);
        nowModel = guideModel;
        this.arr = arr;
        for(int i=0; i<typeOfMessage.length; i++)
        {
            typeOfMessage[i] = GuideModel.PERIOD_MESSAGE;
        }
        initComponents();
    }
     /**获得虚链路的数量*/
    private int getVLNum(){
        num = 0;
        int NUMBERVL = 0;
        Queue<SPM> spmList = nowModel.getSpmList();
        int size = spmList.size();
        for(int i=0;i<size;i++){
            SPM spm = spmList.remove();
            ArrayList<Paratition> parList = spm.getParList();
            int parNum = spm.getParNum();
            for(int j=0;j<parNum;j++){
                Paratition par = parList.get(j);
                int vlNum = par.getVLCount();//分区内vl的数量
                num += vlNum;
                //分区初始化的时候就new了，这样也可以
                ArrayList<VLInfo> pvlList = par.getParititionVLInfo();
                String parId = par.getParId();
                for(int t=0;t<vlNum;t++){
                    VLInfo info = new VLInfo();
                    //对虚链路的ID进行修改 不再是，每个分区的t，而是所有统一VL的编号
                    info.setVlId(parId +"_VL" + (NUMBERVL++));
                    pvlList.add(info);
                    vlList.add(info);
                }
            }
            spmList.add(spm);
        }
        return num;
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        num = getVLNum();
        setResizable(false);

        for(int i=0; i<num; i++)
        {
            jLabelArray1[i] = new javax.swing.JLabel();
            jTextFieldArray2[i] = new javax.swing.JTextField();
            jComboBoxArray3[i] = new javax.swing.JComboBox();
            //jTextFieldArray4[i] = new javax.swing.JTextField();
            jTextFieldArray5[i] = new javax.swing.JTextField();
            jTextFieldArray6[i] = new javax.swing.JTextField();
            //jTextFieldArray211[i] = new javax.swing.JTextField();
            jTextFieldArray7[i] = new javax.swing.JTextField();
            //jTextFieldArray8[i] = new javax.swing.JTextField();
            //jComboBoxArray4[i] = new javax.swing.JComboBox();
            comboBox[i] = new MultiSelectComboBox<String>();
            //jButtonArray5[i] = new javax.swing.JButton();
        }


        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton6 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButtonNextStep = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();


        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        //jLabel9 = new javax.swing.JLabel();
        jLabe20 = new javax.swing.JLabel();
        jLabe21 = new javax.swing.JLabel();
        //jLabe211 = new javax.swing.JLabel();
        jLabe22 = new javax.swing.JLabel();
        jLabe23 = new javax.swing.JLabel();

        jButton6.setText("添加备注");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("建模向导");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("第七步");

        jLabel2.setText("  补全分区消息参数(如分区消息传输类型、生成周期、包大小)、虚链路参数信息（如虚链路ID、BAG）、分区缓存大小及接收端系统");

        //jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icon_guide.png"))); // NOI18N
        //jLabel3.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
//            jPanel1Layout.setAutoCreateGaps(true);
//            jPanel1Layout.setAutoCreateContainerGaps(true);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 770, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 217, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2))
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                                .addContainerGap())
        );

        jButtonNextStep.setText("下一步");
        jButtonNextStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextStepActionPerformed(evt);
            }
        });

        jButton3.setText("取消");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("上一步");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevStepActionPerformed(evt);
            }
        });

        jLabel5.setText("编号");
        jLabel7.setText("VL_ID");
        jLabel8.setText("航电消息类型");
        //jLabel9.setText("分区传输周期(ms)");
        jLabe20.setText(" BAG(ms)");
        jLabe21.setText("包大小(Byte)");
        //jLabe211.setText("SPM输出缓存帧的数量(个)");
        jLabe22.setText("源端系统分区");
        jLabe23.setText("目的端系统分区");
        //这块一下把十个都设置好吧  然后 这里4-10的响应函数 没调整呢 注意


        for(int i=0; i<num; i++)
        {
            jLabelArray1[i].setText("   "+(i+1));
            // 	jTextFieldArray2[i].setActionCommand("jTextFieldArray2_"+(i));
            String vlId = vlList.get(i).getVlId();
            jTextFieldArray2[i].setText(vlId);
            jTextFieldArray2[i].addFocusListener(new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    jTextField2_1FocusLost(evt);
                }
            });
            jComboBoxArray3[i].setModel(new javax.swing.DefaultComboBoxModel(new String[] { "周期型", "事件型" }));
            jComboBoxArray3[i].setActionCommand("jComboBoxArray_"+(i));
            jComboBoxArray3[i].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jComboBox3_1ActionPerformed(evt);
                }
            });


            int duNum = nowModel.getNumOfDU();
            Queue<DU> duList = nowModel.getDuList();
            int count = 0;
            ArrayList<String> arr = new ArrayList<>();
            for(int n=0;n<duNum;n++){
                DU du = duList.remove();
                int duParNum = du.getNumOfPar();
                ArrayList<Paratition> parList = du.getParList();
                for(int m=0;m<duParNum;m++){
                    Paratition par = parList.get(m);
                    String parId = par.getParId();
                    arr.add(parId);
                    count++;
                }
                duList.add(du);
            }


            int num = count;
            String[] str = new String[num];
            //str[0] = "";
            for(int t=0;t<num;t++){
                str[t] = arr.get(t);
            }
            final MultiSelectComboBox<String> comboBox1 = comboBox[i];
            comboBox1.setModel(new javax.swing.DefaultComboBoxModel(str));
            comboBox1.setForegroundAndToPopup(Color.BLACK);
            comboBox1.setActionCommand("jComboBoxArray_"+(i));
            comboBox1.addPopupMenuListener(new PopupMenuListener() {

                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                    //System.out.println("222选择的值："+comboBoxOut.getSelectedItemsString());
                    //List<Integer> indexs = comboBoxIn.getSelectedSortedIndexs();
                    // jComboBox4_1ActionPerformed(e);
                    List<String> names = comboBox1.getSelectedItems();
                    //System.out.println("out"+names);
                    String command = comboBox1.getActionCommand();
                    // String command = e.getActionCommand();
                    //System.out.println("command"+command);
                    int comboNumber = Integer.parseInt(command.substring(command.length()-1));
                    //System.out.println("comboNumber" + comboNumber);
                    int len = names.size();
                    //System.out.println("len:"+len);
                    destin[comboNumber] = new ArrayList<>();

                    for(int j=0;j<len;j++){
                        System.out.println(names.get(j));
                        destin[comboNumber].add(names.get(j));
                        //destin[comboNumber][j] = names.get(j);
                    }

                /*String command = comboBox1.getActionCommand();
                // String command = e.getActionCommand();
                System.out.println("command"+command);
                int comboNumber = Integer.parseInt(command.substring(command.length()-1));
                int num = nowModel.getNumOfDU();

                System.out.println("jComboBoxArray4[comboNumber].getSelectedIndex()"+comboNumber+"" +
                        ","+comboBox1.getSelectedIndex());
                if(comboBox[comboNumber].getSelectedIndex()<=num && comboBox[comboNumber].getSelectedIndex()>=1)
                {
                    for(int j=0;j<len;j++){
                     destin[comboNumber][j] = "RES" + comboBox[comboNumber].getSelectedIndex();
                    }
              // System.out.println(comboNumber +"," +destin[comboNumber]);
                 }*/
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                }
            });
            /*comboBox[i].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {

                }
            });*/
            //jTextFieldArray4[i].setToolTipText("周期：单位ms");
            /*jButtonArray5[i].setText("添加备注");
            jButtonArray5[i].setActionCommand("jButtonArray_"+(i));
            jButtonArray5[i].addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton5_1ActionPerformed(evt);
                }
            });*/
        }



        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2Layout.setAutoCreateGaps(true);
        jPanel2Layout.setAutoCreateContainerGaps(true);
        jPanel2.setLayout(jPanel2Layout);
        GroupLayout.ParallelGroup gp1 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        gp1.addComponent(jLabel5);
        for(int i=0; i<num; i++)
        {
            gp1.addComponent(jLabelArray1[i]);
        }

        GroupLayout.ParallelGroup gp2 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        gp2.addComponent(jLabel7);
        for(int i=0; i<num; i++)
        {
            gp2.addComponent(jTextFieldArray2[i]);
        }

        GroupLayout.ParallelGroup gp3 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        gp3.addComponent(jLabel8);
        for(int i=0; i<num; i++)
        {
            gp3.addComponent(jComboBoxArray3[i]);
        }
//            gp3.addComponent(jComboBox3_1);
//            gp3.addComponent(jComboBox3_2);
//            gp3.addComponent(jComboBox3_3);

       /* GroupLayout.ParallelGroup gp4 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        gp4.addComponent(jLabel9);
        for(int i=0; i<num; i++)
        {
            jTextFieldArray4[i].setText("100");
            gp4.addComponent(jTextFieldArray4[i]);
        }*/
//            gp4.addComponent(jTextField4_1,javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE);
//            gp4.addComponent(jTextField4_2,javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE);
//            gp4.addComponent(jTextField4_3,javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE);

        GroupLayout.ParallelGroup gpp = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        gpp.addComponent(jLabe20);
        for(int i=0; i<num; i++)
        {
            jTextFieldArray5[i].setText("4");
            gpp.addComponent(jTextFieldArray5[i]);
        }

        GroupLayout.ParallelGroup gppp = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        gppp.addComponent(jLabe21);
        for(int i=0; i<num; i++)
        {
            jTextFieldArray6[i].setText("64");
            gppp.addComponent(jTextFieldArray6[i]);
        }

        /*GroupLayout.ParallelGroup g211 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        g211.addComponent(jLabe211);
        for(int i=0; i<num; i++)
        {
            jTextFieldArray211[i].setText("1");
            g211.addComponent(jTextFieldArray211[i]);
        }*/
        GroupLayout.ParallelGroup g4p = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        g4p.addComponent(jLabe22);
        for(int i=0; i<num; i++)
        {
            String vlId = vlList.get(i).getVlId();
            jTextFieldArray7[i].setText(vlId.substring(0,9));

            g4p.addComponent(jTextFieldArray7[i]);
        }
        GroupLayout.ParallelGroup g5p = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        g5p.addComponent(jLabe23);
        for(int i=0; i<num; i++)
        {
            g5p.addComponent(comboBox[i]);
        }

      /*  GroupLayout.ParallelGroup gp5 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for(int i=0; i<num; i++)
        {
            gp5.addComponent(jButtonArray5[i]);
        }*/
//            gp5.addComponent(jButton5_1);
//            gp5.addComponent(jButton5_2);
//            gp5.addComponent(jButton5_3);

        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(gp1).addGap(30).addGroup(gp2).addGap(30).addGroup(gp3).addGap(30).addGroup(gppp).addGap(30)./*addGroup(gp4).addGap(30).*/addGroup(gpp).addGap(30)./*addGroup(g211).addGap(30).*/addGroup(g4p).addGap(30).addGroup(g5p));//.addGap(30).addGroup(gp5));


        GroupLayout.ParallelGroup gp6 = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        gp6.addComponent(jLabel5);
        gp6.addComponent(jLabel7);
        gp6.addComponent(jLabel8);
        //gp6.addComponent(jLabel9);
        gp6.addComponent(jLabe20);
        gp6.addComponent(jLabe21);
        //gp6.addComponent(jLabe211);
        gp6.addComponent(jLabe22);
        gp6.addComponent(jLabe23);


        GroupLayout.SequentialGroup group = jPanel2Layout.createSequentialGroup().addGroup(gp6);

        for(int i=0; i<num; i++)
        {
            GroupLayout.ParallelGroup gpTemp = jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            gpTemp.addComponent(jLabelArray1[i]);
            gpTemp.addComponent(jTextFieldArray2[i]);
            gpTemp.addComponent(jComboBoxArray3[i]);
           // gpTemp.addComponent(jTextFieldArray4[i]);
            gpTemp.addComponent(jTextFieldArray5[i]);
            //gpTemp.addComponent(jButtonArray5[i]);
            gpTemp.addComponent(jTextFieldArray6[i]);
            //gpTemp.addComponent(jTextFieldArray211[i]);
            gpTemp.addComponent(jTextFieldArray7[i]);
            gpTemp.addComponent(comboBox[i]);
            group.addGroup(gpTemp);
        }

        jPanel2Layout.setVerticalGroup(group);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(318, Short.MAX_VALUE)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonNextStep)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3)
                                .addGap(8, 8, 8))
                        .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton3)
                                        .addComponent(jButtonNextStep)
                                        .addComponent(jButton4)).addContainerGap())
        );

        pack();

    }// </editor-fold>


    private void jTextField2_1FocusLost(java.awt.event.FocusEvent evt) {
        // TODO add your handling code here:
        //    	System.out.println(command+"||||||||||||||||||||");//这里原本想 失去焦点 然后判断是否重复  但是因为不知道这个事件是哪个 textField发出来的 所以作罢

    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        doClose(RET_CANCEL);
    }

    private void jButtonPrevStepActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        //上一步
        Queue<VLInfo> queue = new LinkedList<VLInfo>();
        int n = 0;
        Queue<SPM> spmList = nowModel.getSpmList();
        int size = spmList.size();
        for(int i=0;i<size;i++){
            SPM spm = spmList.remove();
            spm.setParList(new ArrayList<>());
            ArrayList<Paratition> parList = spm.getParList();
            int parNum = spm.getParNum();
            for(int j=0;j<parNum;j++){
                Paratition par = parList.get(j);
                //分区初始化的时候就new了，这样也可以
                //ArrayList<VLInfo> pvlList = par.getParititionVLInfo();
                ArrayList<VLInfo> pvlList = new ArrayList<>();
                par.setParititionVLInfo(pvlList);
            }
            spmList.add(spm);
        }
        this.nowModel.setVlList(queue);
        doClose(RET_CANCEL);
        ModelGuideDialog32 guiDialog1 =  new ModelGuideDialog32(ApplicationSettings.getApplicationView(), true, this.nowModel);
        guiDialog1.pack();
        guiDialog1.setLocationRelativeTo(null);
        guiDialog1.setVisible(true);
    }

    private void jComboBox3_1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        String command = evt.getActionCommand();
        //System.out.println("command"+command);
        int comboNumber = Integer.parseInt(command.substring(command.length()-1));
        //System.out.println("comboNumber" + comboNumber);
        if(jComboBoxArray3[comboNumber].getSelectedIndex()==1)
        {
            //System.out.println("jComboBoxArray3[comboNumber].getSelectedIndex():"+jComboBoxArray3[comboNumber].getSelectedIndex());
            typeOfMessage[comboNumber] = GuideModel.EVENT_MESSAGE;
        }
        else if(jComboBoxArray3[comboNumber].getSelectedIndex()==0)
        {
            //System.out.println("11111jComboBoxArray3[comboNumber].getSelectedIndex():"+jComboBoxArray3[comboNumber].getSelectedIndex());
            typeOfMessage[comboNumber] = GuideModel.PERIOD_MESSAGE;
            //jTextFieldArray4[comboNumber].setEditable(true);
        }
        if(jComboBoxArray3[comboNumber].getSelectedIndex()==1)
        {
            //System.out.println("22222222222jComboBoxArray3[comboNumber].getSelectedIndex():"+jComboBoxArray3[comboNumber].getSelectedIndex());
            //jTextFieldArray4[comboNumber].setText("");
            //jTextFieldArray4[comboNumber].setEditable(false);
        }

    }



    private void jButton5_1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        System.out.println("弹出一个小框，然后填写备注");

        String command = evt.getActionCommand();
        int buttonNumber = Integer.parseInt(command.substring(command.length()-1));

        String  remark = JOptionPane.showInputDialog("请输入备注");
        if(remark!=null)
            remarkOfRT[buttonNumber] = remark;
        System.out.println(remark+buttonNumber);

    }

    private void jButtonNextStepActionPerformed(java.awt.event.ActionEvent evt) {//下一步
        // TODO add your handling code here:
        //int num = nowVo.getNumOfRTs();

        for(int i=0; i<num; i++)
        {
            id[i] = jTextFieldArray2[i].getText().trim();
            if(id[i].equals(""))
            {
                JOptionPane.showMessageDialog(this, "ID不能为空", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(this.idCheckSet.contains(id[i]))
            {
                JOptionPane.showMessageDialog(this, "ID不能重复", "Warning",
                        JOptionPane.WARNING_MESSAGE);

                this.idCheckSet.clear();
                return;
            }
            this.idCheckSet.add(id[i]);
        }
        this.idCheckSet.clear();


        /*for(int i=0; i<num; i++)
        {
            if(!jTextFieldArray4[i].isEditable())//这个说明是 事件消息
                delay[i]=0;
            else
            {
                if(jTextFieldArray4[i].getText().trim().equals(""))
                {
                    JOptionPane.showMessageDialog(this, "周期参数不能为空", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                delay[i] = Integer.parseInt(jTextFieldArray4[i].getText());

            }
        }*/

        for(int i=0; i<num; i++)
        {
            if(jTextFieldArray5[i].getText().trim().equals(""))
            {
                JOptionPane.showMessageDialog(this, "BAG参数不能为空", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            //if(jTextFieldArray211[i].getText().trim().equals(""))
              //  cache[i]=1;
           // else
               // cache[i] = Integer.parseInt(jTextFieldArray211[i].getText());
               /* Pattern pattern = Pattern.compile("[1|2|3|4|5|6][0-100]*");
                if(!pattern.matcher(jTextFieldArray5[i].getText()).matches())
                {
                    JOptionPane.showMessageDialog(this, "BAG参数格式有误，请重新确认", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }*/
            this.bag[i] = Integer.parseInt(jTextFieldArray5[i].getText());
        }

        for(int i=0; i<num; i++)
        {
            if(jTextFieldArray6[i].getText().trim().equals(""))
            {
                JOptionPane.showMessageDialog(this, "帧大小不能为空", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
               /* Pattern pattern = Pattern.compile("[1|2|3|4|5|6][0-100]*");
                if(!pattern.matcher(jTextFieldArray5[i].getText()).matches())
                {
                    JOptionPane.showMessageDialog(this, "BAG参数格式有误，请重新确认", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }*/
            this.packageSize[i] = Integer.parseInt(jTextFieldArray6[i].getText());
        }


        Queue<VLInfo> queue = new LinkedList<VLInfo>();
        //System.out.println("num:"+num);
      /*  for(int i=0; i<num; i++)
        {
            VLInfo info = new VLInfo();//备注暂时先没加
            info.setVlId(vlList.get(i).getVlId());
            info.setBAG(bag[i]);
            info.setCacheSize(cache[i]);
            info.setTransCycle(delay[i]);
            info.setSource(vlList.get(i).getVlId());
            info.setDestination(destin[i]);


            info.setPackageSize(packageSize[i]);
            if(typeOfMessage[i] == GuideModel.EVENT_MESSAGE)
                info.setPeriodical(false);
            else if(typeOfMessage[i] == GuideModel.PERIOD_MESSAGE)
                info.setPeriodical(true);
            queue.add(info);
        }*/
        int n = 0;
        Queue<SPM> spmList = nowModel.getSpmList();
        int size = spmList.size();
        for(int i=0;i<size;i++){
            SPM spm = spmList.remove();
            ArrayList<VLInfo> vlInfoList = spm.getInfoList();
            ArrayList<Paratition> parList = spm.getParList();
            int parNum = spm.getParNum();
            for(int j=0;j<parNum;j++){
                Paratition par = parList.get(j);
                int vlNum = par.getVLCount();//分区内vl的数量
                //分区初始化的时候就new了，这样也可以
                //ArrayList<VLInfo> pvlList = par.getParititionVLInfo();
                ArrayList<VLInfo> pvlList = new ArrayList<>();
                String parId = par.getParId();
                for(int t=0;t<vlNum;t++){
                    VLInfo info = new VLInfo();
                    info.setVlId(vlList.get(n).getVlId());
                    info.setBAG(bag[n]);
                    info.setCacheSize(cache[n]);
                    //info.setTransCycle(delay[n]);
                    info.setSource(vlList.get(n).getVlId());
                    info.setDestination(destin[n]);

                    info.setPackageSize(packageSize[n]);
                    if(typeOfMessage[n] == GuideModel.EVENT_MESSAGE)
                        info.setPeriodical(false);
                    else if(typeOfMessage[n] == GuideModel.PERIOD_MESSAGE)
                        info.setPeriodical(true);
                    vlInfoList.add(info);
                    pvlList.add(info);
                    queue.add(info);
                    n++;
                }
                par.setParititionVLInfo(pvlList);
            }
            spmList.add(spm);
        }
        //System.out.println(n +""+num);
        this.nowModel.setVlList(queue);
        //System.out.println("4de  556" + nowModel.getVlList().size());
        doClose(RET_OK);
//    	ModelGuideDialog4 guiDialog =  new ModelGuideDialog4(CreateGui.getApp(), true, this.nowVo);  System.out.println(nowVo);

        //初始化
        GuideModel.FLAG = true;

        new ModelFactory2();
        ArrayList<Element> arr =  ModelFactory2.buildHead();
        ModelFactory2.addSPM(nowModel,arr);
        //TODO 验证此时arr的内容是不是变了

        ModelGuideDialog44 guiDialog =  new ModelGuideDialog44(ApplicationSettings.getApplicationView(), true, this.nowModel,arr);
        guiDialog.pack();
        guiDialog.setLocationRelativeTo(null);
        guiDialog.setVisible(true);
    }
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ModelGuideDialog1 dialog = new ModelGuideDialog1(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify
    private javax.swing.ButtonGroup buttonGroup1;
    //添加备注button
    //private javax.swing.JButton jButtonArray5[] = new javax.swing.JButton[100];

    private javax.swing.JButton jButtonNextStep;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;

    private javax.swing.JButton jButton6;

    private javax.swing.JComboBox jComboBox10;

    //终端消息类型
    private javax.swing.JComboBox[] jComboBoxArray3 = new javax.swing.JComboBox[100];

    //接收端 目的 String
    // private javax.swing.JComboBox[] jComboBoxArray4 = new javax.swing.JComboBox[100];
    private MultiSelectComboBox<String>[] comboBox = new MultiSelectComboBox[100];

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;

    //第一列的编号
    private javax.swing.JLabel[] jLabelArray1 = new javax.swing.JLabel[100];

    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    //private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabe20;
    private javax.swing.JLabel jLabe21;
    //private javax.swing.JLabel jLabe211;
    private javax.swing.JLabel jLabe22;
    private javax.swing.JLabel jLabe23;
    // private javax.swing.JLabel jLabe24;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;

    //第二列 ID
    private javax.swing.JTextField[] jTextFieldArray2 = new javax.swing.JTextField[100];

    //    第四列参数 发生周期
   // private javax.swing.JTextField[] jTextFieldArray4 = new javax.swing.JTextField[100];
    //    第五列参数bag
    private javax.swing.JTextField[] jTextFieldArray5 = new javax.swing.JTextField[100];

    //    第6列参数 包大小
    private javax.swing.JTextField[] jTextFieldArray6 = new javax.swing.JTextField[100];
    //    第6.5列参数 缓存
   // private javax.swing.JTextField[] jTextFieldArray211 = new javax.swing.JTextField[100];
    //    第7列参数 源
    private javax.swing.JTextField[] jTextFieldArray7 = new javax.swing.JTextField[100];

    //private MultiSelectComboBox<String> comboBox;


    // End of variables declaration
    private int returnStatus = RET_CANCEL;

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;
}
