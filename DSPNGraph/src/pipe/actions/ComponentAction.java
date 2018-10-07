package pipe.actions;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.newwidges.view.ComponentDialog;
import pipe.models.PipeApplicationModel;

import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hanson on 2017/8/15.
 */
public class ComponentAction extends GuiAction
{
    //private boolean initial = false;
    public ComponentAction(String name, String tooltip, String keystroke) {
        super(name, tooltip, keystroke);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //参考另外的来区分,来生成不同的组件的逻辑，关键在于如何获得当前的文件
        PipeApplicationModel pipeApplicationView = ApplicationSettings.getApplicationModel();
        this.setSelected(true);
        if(this != pipeApplicationView.startComponentAction)
        {
            //TODO 创建一个文件
            Element pnml, net, label1, tokenclass, place, transition, arc, labelText;

            pnml = new Element("pnml");
            net = new Element("net");
            tokenclass = new Element("token");
            place = new Element("place");
            transition = new Element("transition");
            arc = new Element("arc");

            Element label = new Element("labels");
            label.setAttribute("x", "20");
            label.setAttribute("y", "20");
            label.setAttribute("width", "104");
            label.setAttribute("height", "20");
            label.setAttribute("border", "true");

            labelText = new Element("text");
            labelText.addContent("Petri Model");
            label.addContent(labelText);
            net.setAttribute("id", "Net-One1");
            net.setAttribute("type", "P/T net");

            Element tokenClass = new Element("token");
            tokenClass.setAttribute("id","Black");
            tokenClass.setAttribute("enabled","true");
            tokenClass.setAttribute("red","0");
            tokenClass.setAttribute("green","0");
            tokenClass.setAttribute("blue","0");
            net.addContent(tokenClass);

            tokenClass = new Element("token");
            tokenClass.setAttribute("id","Red");
            tokenClass.setAttribute("enabled","true");
            tokenClass.setAttribute("red","255");
            tokenClass.setAttribute("green","0");
            tokenClass.setAttribute("blue","0");
            net.addContent(tokenClass);

            tokenClass = new Element("token");
            tokenClass.setAttribute("id","Green");
            tokenClass.setAttribute("enabled","true");
            tokenClass.setAttribute("red","0");
            tokenClass.setAttribute("green","255");
            tokenClass.setAttribute("blue","0");
            net.addContent(tokenClass);
            net.addContent(label);


            pnml.addContent(net);
            org.jdom.Document doc = new org.jdom.Document(pnml);

            Format format = Format.getCompactFormat();
            format.setEncoding("iso-8859-1");           //设置xml文件的字符为iso-8859-1
            format.setIndent("    ");               //设置xml文件的缩进为4个空格

            String randomStr = "specialComponent";
            System.out.println(randomStr);

            String path = "d://"+randomStr+".xml";

            XMLOutputter XMLOut = new XMLOutputter(format);//在元素后换行，每一层元素缩排四格
            try {
                    System.out.println("@@@@@@@@@@@@@@@@@2"+path);

                    XMLOut.output(doc, new FileOutputStream(path));
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
        }
        if(this != pipeApplicationView.componentAction)
        {


            //判断是否进行了初始化，没有进行初始化提醒先进行初始化操作，然后再进行组件组合操作
            ComponentDialog guiDialog =  new ComponentDialog(ApplicationSettings.getApplicationView());
            guiDialog.pack();
            guiDialog.setLocationRelativeTo(null);
            guiDialog.setVisible(true);
        }

        //首先创建一个特殊的文件，并判断是否有无

    }
}




