package pipe.gui.widgets.newwidges.view;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import pipe.DSPNModules.AverageQueueLengthModule;
import pipe.DSPNModules.ThroughputModule;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.newwidges.bean.GuideModel;
import pipe.gui.widgets.newwidges.factory.ModelFactory2;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Created by hanson on 2017/8/15.
 */

/**
 * 为了保证重用和扩展*/
public class ComponentDialog extends JDialog {
    // Variables declaration - do not modify
    private ButtonGroup buttonGroup1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JButton jButton6;
    private JButton jButton7;
    private JButton jButton8;
    private JButton jButton9;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabeljLabelIntroduction;//右侧的 关于各个不同总线的介绍
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JPanel jPanel3;
    private JButton t1;//AFDX
    private JSeparator jSeparator1;
    // End of variables declaration
    private int returnStatus = RET_CANCEL;

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;
    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;


    public ComponentDialog(Frame parent) {
        super(parent);
        //this.model = guideModel;
        initComponents();
    }
    @SuppressWarnings("unchecked")
    private void initComponents() {
        buttonGroup1 = new ButtonGroup();
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jSeparator1 = new JSeparator();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jButton4 = new JButton();
        jButton5 = new JButton();
        jButton6 = new JButton();
        jButton7 = new JButton();
        jButton8 = new JButton();
        jButton9 = new JButton();
        jPanel2 = new JPanel();
        //jRadioButtonAFDX = new JRadioButton();
        jPanel3 = new JPanel();
        jLabeljLabelIntroduction = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("建模向导");

        jPanel1.setBackground(new Color(255, 255, 255));

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 290, Short.MAX_VALUE)
                                .addComponent(jLabel3)
                                .addContainerGap())
        );
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
                                .addContainerGap())
        );

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
        jButton4.setText("虚链路组件");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jButton5.setText("虚链路组件2");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jButton6.setText("虚链路组件3");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jButton7.setText("虚链路组件4");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

      //  buttonGroup1.add(jRadioButtonAFDX);

        GroupLayout jPanel33Layout = new GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel33Layout);
        GroupLayout.SequentialGroup vGroup = jPanel33Layout.createSequentialGroup();
        vGroup.addGap(10);
        vGroup.addGroup(jPanel33Layout.createParallelGroup().addComponent(jButton4)
                .addComponent(jButton5));
        vGroup.addGap(5);
        vGroup.addGroup(jPanel33Layout.createParallelGroup().addComponent(jButton6)
                .addComponent(jButton7));
        vGroup.addGap(10);
        //设置垂直组
        jPanel33Layout.setVerticalGroup(vGroup);



        GroupLayout.SequentialGroup hGroup = jPanel33Layout.createSequentialGroup();
        hGroup.addGap(5);//添加间隔
        hGroup.addGroup(jPanel33Layout.createParallelGroup().addComponent(jButton4)
                .addComponent(jButton6));
        hGroup.addGap(5);
        hGroup.addGroup(jPanel33Layout.createParallelGroup().addComponent(jButton5)
                .addComponent(jButton7));
        hGroup.addGap(5);
        jPanel33Layout.setHorizontalGroup(hGroup);

        GroupLayout jPanel3Layout = new GroupLayout(jPanel3);

        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jLabeljLabelIntroduction, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(29, 29, 29)
                                .addComponent(jLabeljLabelIntroduction, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(59, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(495, Short.MAX_VALUE)
                                .addComponent(jButton2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton3)
                                .addGap(8, 8, 8))
                        .addComponent(jSeparator1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(126, 126, 126)
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(35, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton3)
                                        .addComponent(jButton2))
                                .addContainerGap())
        );
        pack();
    }// </editor-fold>



    private void jButton2ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        doClose(RET_OK);//下一步 button
        //在这里清除之前的数据,否则会导致第二次建模失败
        ModelFactory2.setVLNUMBER(0);
        GuideModel.INDEXOFSW = 0;
        AverageQueueLengthModule.setGuideModel(null);
        ThroughputModule.setGuideModel(null);
        AverageQueueLengthModule.setGuideModel(null);

    }


    private void jButton3ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
        doClose(RET_CANCEL);
    }
    private void jButton4ActionPerformed(ActionEvent evt) {
        // TODO 读取那个文件
        ComponentDialogVL guiDialog =  new ComponentDialogVL(ApplicationSettings.getApplicationView());
        guiDialog.pack();
        guiDialog.setLocationRelativeTo(null);
        guiDialog.setVisible(true);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder db = dbf.newDocumentBuilder();

                String str = "d://specialComponent.xml";
                File file = new File(str);

                Document document = db.parse(file);

                NodeList list = document.getElementsByTagName("net");
                Node node = list.item(0);
                Element e1 = (Element)node;
                //Element e = ModelFactory3.buildPlace(e1,"",1,12,12);
                System.out.println(node);
                Element element = document.getElementById("net");

                //System.out.println(element);
                System.out.println(document.getDocumentURI());

                } catch (SAXException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                 catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                 }
    }
    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
}
