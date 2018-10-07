package pipe.gui.widgets.newwidges.factory;

import com.sun.xml.internal.xsom.impl.Ref;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import pipe.gui.widgets.newwidges.bean.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 根据给定的GuideVO，来生成一个xml文件，是对应的模型，打算用JDOM
 * @author Wang-wang   这个为了赶时间 写的超级丑陋  下次重构下吧
 *
 */
public class ModelFactory2 {

    public static int index = 1;
    private static Element  pnml, net;
    public static ArrayList<Element> buildHead()
    {
        Element labelText;

        pnml = new Element("pnml");
        net = new Element("net");
        //tokenclass = new Element("token");
        //place = new Element("place");
        //transition = new Element("transition");
        //arc = new Element("arc");

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
        tokenClass.setAttribute("id","Default");
        tokenClass.setAttribute("enabled","true");
        tokenClass.setAttribute("red","0");
        tokenClass.setAttribute("green","0");
        tokenClass.setAttribute("blue","0");
        net.addContent(tokenClass);

       /* tokenClass = new Element("token");
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
        net.addContent(tokenClass);*/

        net.addContent(label);
        ArrayList<Element> arr = new ArrayList<Element>();
        arr.add(pnml);
        arr.add(net);
        return arr;
    }
    public static String buildModel(GuideModel guideModel, ArrayList<Element> arr)
    {
        Element pnml = arr.get(0);
        Element net = arr.get(1);

        pnml.addContent(net);

        Document doc = new Document(pnml);
        Format format = Format.getCompactFormat();
        format.setEncoding("iso-8859-1");           //设置xml文件的字符为iso-8859-1
        format.setIndent("    ");               //设置xml文件的缩进为4个空格

        String randomStr = "系统Petri网分析模型"+index++;
        System.out.println(randomStr);


        String path = "d://"+randomStr+".xml";
//        String path = "c://AFDX_3_PRIORITY.xml";
        //String path = "//home//xuchonghao//IntelliJ_workspace//"+randomStr+".xml";
        XMLOutputter XMLOut = new XMLOutputter(format);//在元素后换行，每一层元素缩排四格
        try {
//       XMLOut.output(doc, new FileOutputStream("C://Users//Administrator//Desktop//test1234.xml"));
            System.out.println("@@@@@@@@@@@@@@@@@2"+path);
            XMLOut.output(doc, new FileOutputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回的是生成模型的路径
        listOfPlacesInRtEnd = new LinkedList<Element>();
        listOfScheduleStartTransition = new LinkedList<Element>();
        pIndex=0;
        tIndex=0;
        return path;
    }

    private static void addArc(Element source, Element target,  Element net , int times , boolean isInhibitor,boolean curvePoint) {
        Element arc1 = new Element("arc");
        arc1.setAttribute("id", ""+source.getAttributeValue("id")+" to "+target.getAttributeValue("id"));
        arc1.setAttribute("source",source.getAttributeValue("id"));
        arc1.setAttribute("target",target.getAttributeValue("id"));
        Element graphics = new Element("graphics");
        arc1.addContent(graphics);

        Element inscription = new Element("inscription");
        Element value = new Element("value");

        String str ="Default,"+times;
        value.setText(str);
        inscription.addContent(value);
        graphics = new Element("graphics");
        inscription.addContent(graphics);
        arc1.addContent(inscription);

        Element tagged = new Element("tagged");
        value = new Element("value");
        str ="false";
        value.setText(str);
        tagged.addContent(value);
        arc1.addContent(tagged);

        List<Element> list = source.getContent();
        String sx=null,sy = null;
        for(Element e : list){
            if("graphics".equals(e.getName())){
               Element position = e.getChild("position");
               sx = position.getAttributeValue("x");
               sy = position.getAttributeValue("y");
            }
        }
        List<Element> list1 = target.getContent();
        String sx1=null,sy1 = null;
        for(Element e : list1){
            if("graphics".equals(e.getName())){
                Element position = e.getChild("position");
                sx1 = position.getAttributeValue("x");
                sy1 = position.getAttributeValue("y");
            }
        }
        //System.out.println(graph.getAttributeValue("x") + graph.getAttributeValue("y"));
        //TODO 根据弧是否curve来改变它的arcpath
        Element arcpath = new Element("arcpath");
        arcpath.setAttribute("id", "000");
        arcpath.setAttribute("x", "000");
        arcpath.setAttribute("y", "000");
        arcpath.setAttribute("curvePoint", "false");


        arc1.addContent(arcpath);

        if(curvePoint){
            arcpath = new Element("arcpath");
            arcpath.setAttribute("id", "001");
            arcpath.setAttribute("x", ""+((Integer.parseInt(sx)+Integer.parseInt(sx1))/2));
            arcpath.setAttribute("y", ""+(Integer.parseInt(sy)-21));
            arcpath.setAttribute("curvePoint", "true");
            arc1.addContent(arcpath);

            arcpath = new Element("arcpath");
            arcpath.setAttribute("id", "002");
            arcpath.setAttribute("x", "000");
            arcpath.setAttribute("y", "000");
            arcpath.setAttribute("curvePoint", "false");
            arc1.addContent(arcpath);
        }else{
            arcpath = new Element("arcpath");
            arcpath.setAttribute("id", "001");
            arcpath.setAttribute("x", "000");
            arcpath.setAttribute("y", "000");
            arcpath.setAttribute("curvePoint", "false");
            arc1.addContent(arcpath);
        }

      /*  arcpath = new Element("arcpath");
        arcpath.setAttribute("id", "001");
        arcpath.setAttribute("x", "000");
        arcpath.setAttribute("y", "000");
        arcpath.setAttribute("curvePoint", "false");
        arc1.addContent(arcpath);*/


        Element type = new Element("type");
        if(isInhibitor==false)
            type.setAttribute("value", "normal");
        else
            type.setAttribute("value", "inhibitor");
        arc1.addContent(type);
        net.addContent(arc1);

    }

    public static int getVLNUMBER() {
        return VLNUMBER;
    }

    public static void setVLNUMBER(int VLNUMBER) {
        ModelFactory2.VLNUMBER = VLNUMBER;
    }

    private static int VLNUMBER = 0;
    /*private static DU findDesDu(GuideModel guideModel,String spmId){
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
    }*/
    private static ArrayList<VLInfo> findAllParVL(GuideModel guideModel,String parId){
        Queue<VLInfo> infoList = guideModel.getVlList();
        ArrayList<VLInfo> vlInfoList = new ArrayList<>();//用于保存到达该分区的源处的VL
        int numVl = infoList.size();
        for(int j=0;j<numVl;j++){
            VLInfo info = infoList.remove();
            ArrayList<String> desList = info.getDestination();

            for(int t=0;t<desList.size();t++){
                String des = desList.get(t);
                if(des.equals(parId))//如果某条链路的目标有这个分区
                    vlInfoList.add(info);//确定了要添加源链路，数量也跟着确定了
            }
            infoList.add(info);
        }
        return vlInfoList;
    }

    private static void addDUParatition(Paratition par,Element pInqueue,int x,int y) {
        ArrayList<VLInfo> vlInfoList = par.getParititionVLInfo();
        String parId = par.getParId();
        double period = par.getLxPeriod();
        int vlNum = vlInfoList.size();
        ArrayList<Element> Pport = new ArrayList<Element>();
        ArrayList<Element> TtenchLatency = new ArrayList<Element>();
        Element pReceiption = null;
        if(vlNum > 0){
            String Receiption = parId + "_Receiption";
            pReceiption = buildPlace(net, Receiption, 0, x+110 +140+160+190+180+150, y);
        }
        //1、创建链路单元
        for(int i=0;i<vlNum;i++){
            VLInfo info = vlInfoList.get(i);
            int packageSize = info.getPackageSize();
            String infoId = info.getVlId();
            String sequenceId  = infoId.substring(12,13);
            String parVlId = parId + "_VL"+(sequenceId) + "_";

            int weight = (int)Math.ceil(packageSize/1471);
            if(weight==0)
                weight = 1;
            String transitionId0 = parVlId + "RM";
            Element transition0 = buildTransition(net,transitionId0,1,400,0,x+110,y);


            String IPStack = parVlId + "IPStack";
            Element pIPStack = buildPlace(net, IPStack, 0, x+110 +140, y);


            String TenchLatency = parVlId + "TechLatency1";
            Element trTenchLatency = buildTransition(net,TenchLatency,2,0.075,0,x+110 +140+160,y);


            String placeId0 = parVlId + "CommunicationPort";
            Element place0 = buildPlace(net, placeId0, 0, x+110 +140+160+190, y);
            Pport.add(place0);

            String transitionId1 = parVlId + "TenchLatency2";
            Element transition1 = buildTransition(net,transitionId1,2,0.075,0,x+110 +140+160+190+180,y);
            TtenchLatency.add(transition1);



            addArc(pInqueue,transition0,net,1,false,false);
            addArc(transition0,pIPStack,net,1,false,false);
            addArc(pIPStack,trTenchLatency,net,weight,false,false);
            addArc(trTenchLatency,place0,net,1,false,false);
            addArc(place0,transition1,net,1,false,false);
            addArc(transition1,pReceiption,net,1,false,false);

            y = y+50;
            x = x + 15;
        }
        x = x - vlNum*15;
        int k = 190;
        int w = 10;
        //2、创建调度单元
        if(vlNum > 0){
            String transitionId0 = parId + "_LoseResourcesT";
            Element transition0 = buildTransition(net,transitionId0,0,0,0,x+110 +140+160+190,y+w);
            String transitionId1 = parId + "_SchedulePeriod";
            Element transition1 = buildTransition(net,transitionId1,2,period,0,x+110 +140+160+190+180+150,y+w);

            String placeId0 = parId + "_LoseResourcesP";
            Element place0 = buildPlace(net, placeId0, 0, x+450, y+w);
            String placeId1 = parId + "_GetResources";
            Element place1 = buildPlace(net, placeId1, 0, x+110 +140+160+190+180, y+w);
            String placeId2 = parId + "_Wait";
            Element place2 = buildPlace(net, placeId2, 1, x+110 +140+160+190+180+250, y+w);
            addArc(transition1,place2,net,1,false,true);
            addArc(transition0,place0,net,1,false,false);
            addArc(place1,transition0,net,1,false,false);
            addArc(transition1,place1,net,1,false,false);
            addArc(place2,transition1,net,1,false,false);

            for(int j=0;j<vlNum;j++){
                Element portP =  Pport.get(j);
                addArc(portP,transition0,net,1,true,false);

                Element Ttench = TtenchLatency.get(j);
                addArc(place1,Ttench,net,1,false,false);
                addArc(Ttench,place1,net,1,false,false);
            }
        }


    }
    public static void addDU(GuideModel guideModel,Element net,DU desDu,int x,int y,Element swLinkEnd){
        //第一步：确定是哪一个DU
        //DU desDu = findDesDu(guideModel,duName);
        ArrayList<Paratition> parList = desDu.getParList();
        String duName = desDu.getId();


        //第二步、添加接收端的第一个库所
        String dus = duName + "";
        String placeId0 = dus + "_InQueue";
        Element place0 = buildPlace(net, placeId0, 0, x + 420, y);
        addArc(swLinkEnd,place0,net,1,false,false);

        //第三步：根据该DU的分区数据，建立前端的分区
        for(int i=0;i<desDu.getNumOfPar();i++){
            Paratition par = parList.get(i);
            ArrayList<VLInfo> vlInfoList = new ArrayList<>();//用于保存到达该分区的源处的VL
            String parId = par.getParId();

            //1、确定到达分区的所有链路
            vlInfoList = findAllParVL(guideModel,parId);
            /**vlInfoList这里面的链路指的是目的地为该分区的SPM中的链路*/
            par.setParititionVLInfo(vlInfoList);
            par.setVLCount(vlInfoList.size());
            //2、添加一个接收端分区
            addDUParatition(par,place0,x+420,y);

            int len = vlInfoList.size();
            y = y + len * 50 + 100;
        }
    }

    /**
     * 给某一个分区添加虚链路*/
    private static ArrayList addVLs(Paratition par, Element net,int x,int y){
        double lxPeriod = par.getLxPeriod();
        ArrayList<VLInfo> infoList = par.getParititionVLInfo();
        int numVL = par.getVLCount();

        Boolean[] isPeriodical = new Boolean[numVL];
        String[] VLId = new String[numVL];
        String[] source = new String[numVL];
        ArrayList<String>[] destination = new ArrayList[numVL];
        ArrayList<Integer> packageSize = new ArrayList<>();//包大小
        double[] bag = new double[numVL];
        double[] transCycle = new double[numVL];//传输周期
        //int cacheSize = infoList.get(0).getCacheSize();//以第一个VL的cachesize为标准

        String parId = par.getParId();

        for(int i=0;i<numVL;i++){
            VLInfo info = infoList.get(i);
            isPeriodical[i] = info.getPeriodical();
           // System.out.println(isPeriodical[i]);
            VLId[i] = info.getVlId();
            source[i] = info.getSource();
            destination[i] = info.getDestination();
            packageSize.add(info.getPackageSize());
            bag[i] = info.getBAG();
            transCycle[i] = lxPeriod;//没有发生周期
        }


        //计算weight
        int weight = 0;
        double[] jitt = new double[numVL];
        double[] jittRate = new double[numVL];

        ArrayList<Element> trEnqueue = new ArrayList<Element>();

        int bufferY = 0;
       // int queueY = 0;
        for(int i=0;i<numVL;i++){
            String vlId = VLId[i];
            //weight += Math.floor(lxPeriod/(bag[i]+1));
            weight = (int)Math.ceil(packageSize.get(i)/1471);

            if(weight==0)
                weight = 1;
            //把包大小修改为了平均帧大小
            double averageZhen = 1471;
            jitt[i] = 0.04 + (averageZhen + 47 +20) * 8 / (Math.pow(10,5));
            System.out.println("jittSPM:"+jitt[i]);
            if(jitt[i] >= 0.5){
                jitt[i] = 0.5;
            }
            jittRate[i] = 1 / (jitt[i]/1000);//单位得化成s

            String Application =  vlId + "_ApplicationPort";
            Element pApplication = buildPlace(net, Application, 1, x-64, y);
            String AFDXCommunicationPort;
            Element pAFDXCommunicationPort;
            String trSendPeriod ;
            Element traSendPeriod ;
            if(isPeriodical[i]){
                AFDXCommunicationPort = vlId + "_AfdxOrSAP_Port";
                pAFDXCommunicationPort = buildPlace(net, AFDXCommunicationPort, 0, x+315, y);
                trSendPeriod = vlId + "_"+"TransPeriod";
                // System.out.println("factory de 1017 transCycle周期:" + transCycle);
                traSendPeriod = buildTransition(net, trSendPeriod, 2, transCycle[i], 0, x+85, y);
            }else {
                AFDXCommunicationPort = vlId + "_AfdxOrSAP_Port";
                pAFDXCommunicationPort = buildPlace(net, AFDXCommunicationPort, 0, x+315, y);
                trSendPeriod = vlId + "_"+"EventM";
                traSendPeriod = buildTransition(net, trSendPeriod, 1, transCycle[i], 0, x+85, y);
            }

            String IPStack = vlId + "_IPStack";
            Element pIPStack = buildPlace(net, IPStack, 0, x+630, y);


            String placeId3 = vlId + "_Wait1";
            Element place3 = buildPlace(net, placeId3, 1, x-100, y+130);

            String placeId4 = vlId + "_Wait2";
            Element place4 = buildPlace(net, placeId4, 0, x+225, y+130);
            String placeId5 = vlId + "_Schedule";
            Element place5 = buildPlace(net, placeId5, 0, x+540, y+130);



            String trFragment = vlId + "_"+"Fragment";
            Element traFragment = buildTransition(net, trFragment,0, 0, 0, x+465, y);

             //TODO trLoss.add(transition1);

            // System.out.println("factory de 1046 bag:" + bag);
            String transtionId2 = vlId + "_"+"BAG";
            Element transition2 = buildTransition(net, transtionId2,2, bag[i], 0,x+60, y+130);
            String transtionId3 = vlId + "_"+"Jitter";
            Element transition3 = buildTransition(net, transtionId3,1 , jittRate[i], 0, x+390, y+130);

            String Enqueue = vlId + "_"+"TechLatency1";
            int y2 = y + 80;
            Element traEnqueue = buildTransition(net, Enqueue,2, 0.075, 0, x+630, y2);
            bufferY += y2;
            //queueY += (y+150);
            trEnqueue.add(traEnqueue);

            //Element source, Element target,  Element net , int times , boolean isInhibitor
            addArc(pApplication,traSendPeriod,net,1,false,false);
            addArc(traSendPeriod,pAFDXCommunicationPort,net,1,false,false);
            addArc(traSendPeriod,pApplication,net,1,false,true);
            addArc(pAFDXCommunicationPort,traFragment,net,1,false,false);
            addArc(traFragment,pIPStack,net,weight,false,false);
            addArc(pIPStack,traEnqueue,net,1,false,false);

            addArc(place3,transition2,net,1,false,false);
            addArc(transition2,place4,net,1,false,false);
            addArc(place4,transition3,net,1,false,false);
            addArc(transition3,place5,net,1,false,false);
            addArc(place5,traEnqueue,net,1,false,false);

            addArc(traEnqueue,place3,net,1,false,true);
            y = y+230;
        }
        double techLa = 0.075;
        String vlId = parId;
        String OutBuffer = vlId + "_OutBuffer";
        Element pOutBuffer = buildPlace(net, OutBuffer, 0, x+760, bufferY/numVL);

        int tenY = bufferY/numVL;
        String TechLatency = vlId + "_"+"TechLatency";
        Element traTechLatency = buildTransition(net, TechLatency,2 , techLa, 0, x+900, tenY);

        listOfTranstionVLEnd.add(traTechLatency);

        int len = trEnqueue.size();
        for(int t=0;t<len;t++){
            Element trEn = trEnqueue.get(t);
            addArc(trEn,pOutBuffer,net,1,false,false);
        }
        addArc(pOutBuffer,traTechLatency,net,1,false,false);
        return packageSize;
    }
    private static ArrayList addParatition(Paratition par, Element net,int x,int y){
        ArrayList<Integer> packageSize = addVLs(par,net,x, y);
        return packageSize;
    }

    public static void addSPM(GuideModel guideModel, ArrayList<Element> arr){
        Element net = arr.get(1);
        int x,y;
        Element linkEndElement = null;
        ArrayList<Integer> lMax = new ArrayList<>();
        int size = guideModel.getSpmList().size();
        x = 180;
        y = 45; //初始位置
        for(int i=0; i< size; i++)
        {
            int startY = y;
            int endY;

            SPM spm = guideModel.getSpmList().remove();
            ArrayList<Paratition> parList = spm.getParList();
            int numPar = spm.getParNum();
            for(int j=0;j<numPar;j++){
                Paratition par = parList.get(j);
                lMax = addParatition(par,net,x,y);
                int numOfVl = lMax.size();
                y = y + 180 * numOfVl + 90;//每两个分区高度差为700
            }
            endY = y - 90 - 50;
            y = y + 120;//每两个SPM

            int transMY = (startY+endY)/2;
            //增加最后的库所和弧
            String spmId = spm.getId()+"_";
            String placeId = spmId + "TransM";//应该位于该分区竖向的中间位置
            Element place = buildPlace(net, placeId, 0, 1200,transMY) ;

            while(!listOfTranstionVLEnd.isEmpty()){
                Element e = listOfTranstionVLEnd.remove();
                addArc(e,place,net,1,false,false);
            }

            //构建SPM后面的链路
            linkEndElement = addSPMLink(net,1200,transMY,spmId+"L"+spmId.substring(3,4),place,lMax);
            spm.setLastEle(linkEndElement);

            listOfSpmLinkEnd.add(linkEndElement);

            guideModel.getSpmList().add(spm);
        }
        arr.set(1,net);//TODO 这里还用返回吗？引用类型在此处应该就已经修改了，应该不用返回！？！？
    }

    private static Element addSPMLink( Element net,int x,int y,String spmId,Element startElement,ArrayList<Integer> lMax ){
        //TODO 下面的period需要重新填充
        int len = lMax.size();
        int packageSize = 0;
        for(int i=0;i<len;i++){
            packageSize += lMax.get(i);
        }
        double t = (1471+47)*8/(Math.pow(10,5));
        //double t = 0.12136;
        //spmId += (linkIndex++)+"_";
        spmId += "_";
        String transitionId0 = spmId + "StartTrans";
        Element transition0 = buildTransition(net,transitionId0,0,0,0,x+110,y);

        String placeId0 = spmId + "LinkIdle";
        Element place0 = buildPlace(net, placeId0, 1, x+215, y+90);

        String placeId1 = spmId + "LinkBusy";
        Element place1 = buildPlace(net, placeId1, 0, x+215, y-105);

        String transitionId1 = spmId + "EndTrans";
        Element transition1 = buildTransition(net,transitionId1,2,t,0,x+320,y-15);
        System.out.println("SPMLink EndTrans:"+ t);
        addArc(startElement,transition0,net,1,false,false);
        addArc(place0,transition0,net,1,false,false);
        addArc(transition0,place1,net,1,false,false);
        addArc(place1,transition1,net,1,false,false);
        addArc(transition1,place0,net,1,false,false);
        return transition1;
    }

    private static Element addSwLink(Element net,int x,int y,String spmId,Element startElement){
        //TODO 下面的period需要重新填充
        spmId += "_";
         //spmId += (linkIndex++)+"_";
        String transitionId0 = spmId + "StartTrans";
        Element transition0 = buildTransition(net,transitionId0,0,0,0,x+110,y);

        String placeId0 = spmId + "LinkIdle";
        Element place0 = buildPlace(net, placeId0, 1, x+215, y+105);

        String placeId1 = spmId + "LinkBusy";
        Element place1 = buildPlace(net, placeId1, 0, x+215, y-105);

        String transitionId1 = spmId + "EndTrans";
        double t = (1471+47)*8/(Math.pow(10,5));
        Element transition1 = buildTransition(net,transitionId1,2,t,0,x+320,y);

        addArc(startElement,transition0,net,1,false,false);
        addArc(place0,transition0,net,1,false,false);
        addArc(transition0,place1,net,1,false,false);
        addArc(place1,transition1,net,1,false,false);
        addArc(transition1,place0,net,1,false,false);
        return transition1;
    }

    public static void addSwitch(GuideModel nowModel,Element net,int x,int y,SW sw,int outNum){
        ArrayList<Element> listOfSpmLinkEnd = new ArrayList<>();
        String spmId = sw.getId() + "_";
        String placeIdx = spmId + "OutQueue";
        Element placex = buildPlace(net, placeIdx, 0, x+220, y);

        int AllThroughVLNum = sw.getAllThroughVLNum();

        ArrayList<Port> inPortList = sw.getInPortList();
        for(int i=0;i<inPortList.size();i++){
            Port inport = inPortList.get(i);

            Element e = inport.getIOEle();//交换机的输入端口

            String placeId = spmId + "Inport_" + i;
            Element place = buildPlace(net, placeId, 0, x, y+i*300);

            //Element e = listOfSpmLinkEnd.get(i);
            addArc(e,place,net,1,false,false);

            String transitionId = spmId + "Forward_" + i;
            Element transition = buildTransition(net,transitionId,2,0.016,0,x+110,y+i*300);
            addArc(place,transition,net,1,false,false);
            addArc(transition,placex,net,1,false,false);
        }
        Queue<VLInfo> infoList = nowModel.getVlList();
        VLInfo info = infoList.remove();
        double jitter = AllThroughVLNum * 8 *(1471 + 47 + 20) / ((Math.pow(10,5))) + 0.04;
        System.out.println("Sw 的jitter：" + jitter +","+AllThroughVLNum);
        infoList.add(info);
        if(jitter >= 0.5)
            jitter = 0.5;

        String transitionId0 = spmId + "Jitter";
        Element transition0 = buildTransition(net,transitionId0,1,1/(jitter/1000),0,x+420,y);

        String placeId0 = spmId + "Wait";
        Element place0 = buildPlace(net, placeId0, 1, x+540, y+105);

        String placeId1 = spmId + "Schedule";
        Element place1 = buildPlace(net, placeId1, 0, x+540, y-105);

        String transitionId1 = spmId + "techLatency";
        Element transition1 = buildTransition(net,transitionId1,2,0.084,0,x+660,y);

        String placeId2 = spmId + "TransM";
        Element place2 = buildPlace(net, placeId2, 0, x+870, y);
        addArc(transition1,place2,net,1,false,false);

        ArrayList<Port> outPortList = sw.getOutPortList();
        int outLen = outPortList.size();
        for(int t=0;t<outLen;t++){
            Port outport = outPortList.get(t);

            Element linkEndElement = addSwLink(net,x + 1000,y-300+t*700,spmId+"L"+spmId.substring(2,3),place2);
            //System.out.println(sw.getId() + "," + linkEndElement);
            outport.setIOEle(linkEndElement);
        }

        addArc(placex,transition0,net,1,false,false);
        addArc(place0,transition0,net,1,false,false);
        addArc(transition0,place1,net,1,false,false);
        addArc(place1,transition1,net,1,false,false);
        addArc(transition1,place0,net,1,false,false);
        //addArc(transition1,place2,net,1,false,false);
        //添加链路，交换机后肯定有链路
    }

    /**
     * 构造一个place 并且加在net上面
     * @param net 整个的网
     * @param placeName  若有名字 给名字，若为空 则用 默认的编号P1 P2等
     * @param initailMark  是否有初始的标记
     * @param x 坐标
     * @param y
     * @return place
     */
    private static Element buildPlace(Element net, String placeName ,int initailMark, int x, int y)
    {
        Element place = new Element("place");
//    place.setAttribute("id", "P"+pIndex);
        if(placeName=="")
            place.setAttribute("id", "P"+pIndex);
        else
            place.setAttribute("id", placeName);

        Element graphics = new Element("graphics");
        Element position = new Element("position");

        position.setAttribute("x", String.valueOf(x));
        position.setAttribute("y", String.valueOf(y));
        graphics.addContent(position);
        place.addContent(graphics);
        Element name = new Element("name");
        Element value = new Element("value");
        value.setText("P"+pIndex++);
        if(placeName=="")
            value.setText("P"+pIndex++);
        else
            value.setText(placeName);
        Element offset = new Element("offset");
        offset.setAttribute("x", "70.0");
        offset.setAttribute("y", "40.0");
        graphics = new Element("graphics");
        graphics.addContent(offset);
        name.addContent(value);
        name.addContent(graphics);
        place.addContent(name);
        Element initialMarking = new Element("initialMarking");
        value = new Element("value");
        String str = "Default,"+initailMark;
        value.setText(str);
        graphics = new Element("graphics");
        offset = new Element("offset");
        offset.setAttribute("x", "0");
        offset.setAttribute("y", "0");
        graphics.addContent(offset);
        initialMarking.addContent(value);
        initialMarking.addContent(graphics);
        place.addContent(initialMarking);
        Element capacity = new Element("capacity");
        value = new Element("value");
        value.setText("0");
        capacity.addContent(value);
        place.addContent(capacity);
        net.addContent(place);
        return place;
    }

    /**
     * 构造一个变迁 并且加载net上面
     * @param net
     * @param transitionName
     * @param trType
     * @param period
     * @param angle  角度
     * @param x
     * @param y
     * @return
     */
    private static Element buildTransition(Element net, String transitionName ,int trType, double period, int angle, int x, int y)
    {
        Element transition = new Element("transition");
        if(transitionName=="")
            transition.setAttribute("id", "T"+tIndex);
        else
            transition.setAttribute("id", transitionName);
        Element graphics = new Element("graphics");
        Element position = new Element("position");
        position.setAttribute("x", String.valueOf(x));
        position.setAttribute("y", String.valueOf(y));
        graphics.addContent(position);
        transition.addContent(graphics);

        Element name = new Element("name");
        Element value = new Element("value");

        if(transitionName=="")
            value.setText("T"+tIndex++);
        else
            value.setText(transitionName);

        Element offset = new Element("offset");
        offset.setAttribute("x", "70.0");
        offset.setAttribute("y", "50.0");
        graphics = new Element("graphics");

        graphics.addContent(offset);
        name.addContent(value);
        name.addContent(graphics);
        transition.addContent(name);

        Element orentation = new Element("orentation");
        value = new Element("value");
        String str ="0";
        value.setText(str);
        orentation.addContent(value);
        transition.addContent(orentation);



        Element type = new Element("type");
        value = new Element("value");
        str = "" + trType;
        value.setText(str);
        type.addContent(value);
        transition.addContent(type);


        Element rate = new Element("rate");
        value = new Element("value");
        //str ="" +period;
        //if(trType == 0)
        //str ="1";
        //TODO 这里先假设，之后再改
        if(period == 0)
            period = 1;
        str ="" +period;
        value.setText(str);
        rate.addContent(value);
        transition.addContent(rate);

        Element infiniteServer = new Element("infiniteServer");
        value = new Element("value");
        str ="false";
        value.setText(str);
        infiniteServer.addContent(value);
        transition.addContent(infiniteServer);

        Element priorty = new Element("priorty");
        value = new Element("value");
        str ="0";
        value.setText(str);
        priorty.addContent(value);
        transition.addContent(priorty);


        Element delay = new Element("delay");
        value = new Element("value");
        str ="0";
        //str =""+period;
        value.setText(str);
        delay.addContent(value);
        transition.addContent(delay);

        net.addContent(transition);

        return transition;
    }
    private static int pIndex;
    private static int tIndex;
    private static int swIndex;
    private static int duIndex;
    private static int reVlIndex;
    private static int linkIndex;

    private static List<Element> listOfPlacesInRtEnd = null;
    private static LinkedList<Element> listOfTranstionVLEnd = null;

    public static ArrayList<Element> getListOfSpmLinkEnd() {
        return listOfSpmLinkEnd;
    }

    public static void setListOfSpmLinkEnd(ArrayList<Element> listOfSpmLinkEnd) {
        ModelFactory2.listOfSpmLinkEnd = listOfSpmLinkEnd;
    }

    private static ArrayList<Element> listOfSpmLinkEnd = null;
    private static List<Element> listOfScheduleStartTransition = null;

    public ModelFactory2() {
        pIndex=0;
        tIndex=0;
        swIndex=0;
        duIndex=0;
        linkIndex=0;
        reVlIndex=0;
        pnml = null;
        net = null;

        listOfPlacesInRtEnd = new LinkedList<Element>();
        listOfTranstionVLEnd = new LinkedList<Element>();
        listOfSpmLinkEnd = new ArrayList<Element>();
        listOfScheduleStartTransition = new LinkedList<Element>();
    }

     /*
      private static ArrayList addVLs1(Paratition par, Element net,int x,int y){
        double lxPeriod = par.getLxPeriod();
        ArrayList<VLInfo> infoList = par.getParititionVLInfo();
        int numVL = par.getVLCount();
        Boolean[] isPeriodical = new Boolean[numVL];
        String[] VLId = new String[numVL];
        String[] source = new String[numVL];
        ArrayList<String>[] destination = new ArrayList[numVL];
        ArrayList<Integer> packageSize = new ArrayList<>();//包大小
        double[] bag = new double[numVL];
        double[] transCycle = new double[numVL];//传输周期
        int cacheSize = infoList.get(0).getCacheSize();//以第一个VL的cachesize为标准
        String parId = par.getParId();

        for(int i=0;i<numVL;i++){
            VLInfo info = infoList.get(i);
            isPeriodical[i] = info.getPeriodical();
            System.out.println(isPeriodical[i]);
            VLId[i] = info.getVlId();
            source[i] = info.getSource();
            destination[i] = info.getDestination();
            packageSize.add(info.getPackageSize());
            bag[i] = info.getBAG();
            transCycle[i] = info.getTransCycle();
        }


        //计算weight
        int weight = 0;
        double[] jitt = new double[numVL];
        double[] jittRate = new double[numVL];

        ArrayList<Element> trEnqueue = new ArrayList<Element>();
        ArrayList<Element> trLoss = new ArrayList<Element>();

        int bufferY = 0;
        int queueY = 0;
        for(int i=0;i<numVL;i++){
            String vlId = VLId[i];
            weight += Math.floor(lxPeriod/(bag[i]+1));
            if(weight==0)
                weight = 1;
            jitt[i] = 0.04 + (packageSize.get(i)+20) * 8 / (Math.pow(10,5));

            jittRate[i] = 1 / (jitt[i]/1000);//单位得化成s


            String placeId0 = vlId + "_Application";
            Element place0 = buildPlace(net, placeId0, 1, x, y);
            String placeId1;
            Element place1;
            String transtionId0 ;
            Element transition0 ;
            if(isPeriodical[i]){
                placeId1 = vlId + "_Sampling_port";
                place1 = buildPlace(net, placeId1, 0, x+315, y);
                transtionId0 = vlId + "_"+"Period";
                // System.out.println("factory de 1017 transCycle周期:" + transCycle);
                transition0 = buildTransition(net, transtionId0, 2, transCycle[i], 0, x+135, y);
            }else {
                placeId1 = vlId + "_Sampling_port";
                place1 = buildPlace(net, placeId1, 0, x+315, y);
                transtionId0 = vlId + "_"+"EventM";
                transition0 = buildTransition(net, transtionId0, 1, transCycle[i], 0, x+135, y);
            }

            String placeId2 = vlId + "_Loss";
            Element place2 = buildPlace(net, placeId2, 0, x+630, y);


            String placeId3 = vlId + "_Wait1";
            Element place3 = buildPlace(net, placeId3, 1, x-60, y+80);

            String placeId4 = vlId + "_Wait2";
            Element place4 = buildPlace(net, placeId4, 0, x+225, y+130);
            String placeId5 = vlId + "_Schedule";
            Element place5 = buildPlace(net, placeId5, 0, x+540, y+130);



            String transtionId1 = vlId + "_"+"TLoss";
            Element transition1 = buildTransition(net, transtionId1,0, 0, 0, x+465, y);
            bufferY += y;
            trLoss.add(transition1);

            // System.out.println("factory de 1046 bag:" + bag);
            String transtionId2 = vlId + "_"+"VL-BAG";
            Element transition2 = buildTransition(net, transtionId2,2, bag[i], 0,x+60, y+130);
            String transtionId3 = vlId + "_"+"Jitter";
            Element transition3 = buildTransition(net, transtionId3,1 , jittRate[i], 0, x+390, y+130);

            String transtionId4 = vlId + "_"+"Enqueue";
            Element transition4 = buildTransition(net, transtionId4,0 , 0, 0, x+630, y+80);
            queueY += (y+150);
            trEnqueue.add(transition4);

            //Element source, Element target,  Element net , int times , boolean isInhibitor
            addArc(place0,transition0,net,1,false,true);
            addArc(transition0,place0,net,1,false,false);
            addArc(transition0,place1,net,1,false,false);
            addArc(place1,transition1,net,1,false,false);
            addArc(place1,transition4,net,1,false,false);
            addArc(transition1,place2,net,1,false,false);

            addArc(place3,transition2,net,1,false,false);
            addArc(transition2,place4,net,1,false,false);
            addArc(place4,transition3,net,1,false,false);
            addArc(transition3,place5,net,1,false,false);
            addArc(place5,transition4,net,1,false,false);

            addArc(transition4,place3,net,1,false,false);
            y = y+230;
        }
        double techLa = 0.15;
        String vlId = parId;
        String placeId6 = vlId + "_Buffer";
        Element place6 = buildPlace(net, placeId6, cacheSize, x+735, bufferY/numVL);
        String placeId7 = vlId + "_Queue";
        Element place7 = buildPlace(net, placeId7, 0, x+735, queueY/numVL);
        int tenY = (bufferY/numVL + queueY/numVL)/2;
        String transtionId5 = vlId + "_"+"TechLatency";
        Element transition5 = buildTransition(net, transtionId5,2 , techLa, 0, x+840, tenY);

        listOfTranstionVLEnd.add(transition5);
        int len = trLoss.size();
        for(int t=0;t<len;t++){
            Element trEn = trEnqueue.get(t);
            Element trLo = trLoss.get(t);
            addArc(trEn,place7,net,1,false,false);

            addArc(place6,trEn,net,1,false,false);
            addArc(place6,trLo,net,1,true,false);
        }

        String placeId8 = vlId + "_WaitPeriod";
        Element place8 = buildPlace(net, placeId8, 0, x+500, y-30);

        String transtionId6 = vlId + "_"+"TransPeriod";
        Element transition6 = buildTransition(net, transtionId6,2 , lxPeriod, 0, x+700, y-30);

        String placeId9 = vlId + "_Send";
        Element place9 = buildPlace(net, placeId9, 0, x+840, y-30);

        addArc(place7,transition5,net,1,false,false);
        addArc(transition5,place6,net,1,false,false);

        addArc(place8,transition6,net,1,false,true);
        addArc(transition6,place9,net,weight,false,true);
        addArc(transition6,place8,net,1,false,true);
        addArc(place9,transition5,net,1,false,true);


        return packageSize;
    }
     public static void addDU(GuideModel guideModel,Element net,String duName,int x,int y,String spmId,Element swLinkEnd){
        spmId += "_";
        String placeId0 = spmId + "InQueue";
        Element place0 = buildPlace(net, placeId0, 1, x + 420, y);
        addArc(swLinkEnd,place0,net,1,false,false);

        Queue<VLInfo> vlList = guideModel.getVlList();
        int len = vlList.size();
        int k = 0;
        int d = 0;
        while(k < len){
            VLInfo info = vlList.remove();
            ArrayList<String> des = info.getDestination();
            String sourAll = info.getSource();//SPM1
            String sour = sourAll.substring(0,4);
            for(int t = 0;t<des.size();t++){
                if(des.get(t).equals(duName)){//添加该目的地的虚链路
                    //spmId +=  sour;
                    addRecVL(net,x+420,y+d*60,spmId,sour,place0);
                    d++;
                }
            }

            k++;
            vlList.add(info);
        }

    }
    private static void addRecVL( Element net,int x,int y,String spmId,String sourId,Element startElement){
        spmId += sourId + "_VL"+(reVlIndex++) + "_";

        String transitionId0 = spmId + "RM";
        Element transition0 = buildTransition(net,transitionId0,1,5,0,x+190,y);

        String placeId0 = spmId + "Rec-Port";
        Element place0 = buildPlace(net, placeId0, 1, x+380, y);

        String transitionId1 = spmId + "TenchLatency";
        Element transition1 = buildTransition(net,transitionId1,2,0.15,0,x+570,y);

        String placeId1 = spmId + "Receiption";
        Element place1 = buildPlace(net, placeId1, 1, x+760, y);

        addArc(startElement,transition0,net,1,false,false);
        addArc(transition0,place0,net,1,false,false);
        addArc(place0,transition1,net,1,false,false);
        addArc(transition1,place1,net,1,false,false);
    }
    public static void addSPM(GuideModel guideModel, ArrayList<Element> arr){
        Element net = arr.get(1);
        int x,y;
        int prelen = 1;
        Element linkEndElement = null;
         ArrayList<Integer> lMax = new ArrayList<>();
         int size = guideModel.getSpmList().size();
        for(int i=0; i< size; i++)
        {
            x = 180;
            y = 45 + i * 1800;
            SPM spm = guideModel.getSpmList().remove();

            double lxPeriod = spm.getLxPeriod();
            int p1Len = spm.getPar1VLNum();
            int p2Len = spm.getPar2VLNum();
            int p3Len = spm.getPar3VLNum();

            int len = p1Len + p2Len + p3Len;
            if(p1Len == 2){
                addParVLs2(guideModel, net,p1Len,x, y,lxPeriod);
            }else{
                addParVLs1(guideModel, net,p1Len,x, y,lxPeriod);
            }
             if(p2Len == 2){
                addParVLs2(guideModel, net,p2Len,x, y+700,lxPeriod);
            }else{
                addParVLs1(guideModel, net,p2Len,x, y+700,lxPeriod);
            }
             if(p3Len == 2){
                addParVLs2(guideModel, net,p3Len,x, y+1400,lxPeriod);
            }else{
                addParVLs1(guideModel, net,p3Len,x, y+1400,lxPeriod);
            }
            prelen = len;

            //增加最后的库所和弧
            String spmId = spm.getId();
            String placeId = spmId + "TransM";
            Element place = buildPlace(net, placeId, 1, 1200, 450+i*len*400);

            //int j = len - 1;
            while(!listOfTranstionVLEnd.isEmpty()){
                Element e = listOfTranstionVLEnd.remove();
                addArc(e,place,net,1,false,false);
            }

            Paratition p1 = spm.getPARTITION1();
            Paratition p2 = spm.getPARTITION2();
            Paratition p3 = spm.getPARTITION3();
            ArrayList<VLInfo> l1 = p1.getParititionVLInfo();
            ArrayList<VLInfo> l2 = p2.getParititionVLInfo();
            ArrayList<VLInfo> l3 = p3.getParititionVLInfo();
            int len1 = l1.size();
            int len2 = l2.size();
            int len3 = l3.size();

            for(int j=0;j<len1;j++){
                VLInfo info = l1.get(j);
                lMax.add(info.getPackageSize());
            }

            for(int j=0;j<len2;j++){
                VLInfo info = l2.get(j);
                lMax.add(info.getPackageSize());
            }
            for(int j=0;j<len3;j++){
                VLInfo info = l3.get(j);
                lMax.add(info.getPackageSize());
            }
            //构建SPM后面的链路
            linkEndElement = addSPMLink(net,1200,450+i*len*400,spmId+"L",place,lMax);
            listOfSpmLinkEnd.add(linkEndElement);

            guideModel.getSpmList().add(spm);
        }
        arr.set(1,net);//TODO 这里还用返回吗？引用类型在此处应该就已经修改了，应该不用返回！？！？
    }

    private static void addParVLs2(GuideModel guideModel, Element net,int parLen,int x,int y,double lxPeriod){
        Queue<VLInfo> list = new LinkedList<VLInfo>();
        Queue<VLInfo> vlList = guideModel.getVlList();

        for(int i=0;i<VLNUMBER; i++){//
            VLInfo info = vlList.remove();
            list.add(info);
        }
        if(parLen == 2){
            VLInfo info1 = vlList.remove();
            VLInfo info2 = vlList.remove();
            addVL2(info1,info2,net,x, y); //180   45+i*400
            list.add(info1);
            list.add(info1);

        }
        VLNUMBER += parLen;
        while(!vlList.isEmpty()){
            VLInfo info = guideModel.getVlList().remove();
            list.add(info);
        }
        guideModel.setVlList(list);
    }
*/
    //创建某个分区中的链路 ,缺少参数lx周期
/*
   private static void addParVLs1(GuideModel guideModel, Element net,int len,int x,int y,double lxPeriod){
        Queue<VLInfo> list = new LinkedList<VLInfo>();
        Queue<VLInfo> vlList = guideModel.getVlList();
        ArrayList<VLInfo> infoList = new ArrayList<VLInfo>();
       // System.out.println("VLNUMBER:"+VLNUMBER);
       // System.out.println("len:"+len);
        for(int i=0;i<VLNUMBER; i++){//
            VLInfo info = vlList.remove();
            list.add(info);
        }
        //先取出来一次用于计算,再将队列清空，再取出
       for(int i=0; i<len; i++)
        {
            VLInfo info = vlList.remove();
            //传入该分区链路的数量以便计算权重
            infoList.add(info);
            list.add(info);
        }
       while(!vlList.isEmpty()){
            VLInfo info = guideModel.getVlList().remove();
            list.add(info);
        }
        guideModel.setVlList(list);
               //第二次重新取出
        list = new LinkedList<VLInfo>();
        vlList = guideModel.getVlList();
        for(int i=0;i<VLNUMBER; i++){//
            VLInfo info = vlList.remove();
            list.add(info);
        }

        for(int i=0; i<len; i++)
        {
            VLInfo info = vlList.remove();
            //传入该分区链路的数量以便计算权重
            addVL1(info,net,x, y + i*400,infoList,lxPeriod); //180   45+i*400

            list.add(info);
        }
        VLNUMBER += len;
        while(!vlList.isEmpty()){
            VLInfo info = guideModel.getVlList().remove();
            list.add(info);
        }
        guideModel.setVlList(list);
    }

    private static void addVL2(VLInfo info1,VLInfo info2,Element net,int x,int y){
        Boolean isPeriodical = info1.getPeriodical();
        double transCycle = info1.getTransCycle();
        double bag = info1.getBAG();
        double jitt = 0.04 + (info1.getPackageSize()+20 + info2.getPackageSize()+20) * 8 / (Math.pow(10,5));
        double jittRate = 1 / jitt;
         double techLa = 0.15;
        int cacheSize1 = info1.getCacheSize();
        String vlId = info1.getVlId();


        String placeId0 = vlId + "_Application";
        Element place0 = buildPlace(net, placeId0, 1, x, y);
        String placeId1;
        Element place1;
        String transtionId0 ;
        Element transition0 ;
        if(isPeriodical)
        {
            placeId1 = vlId + "_Sampling_port";
            place1 = buildPlace(net, placeId1, 0, x+315, y);
            transtionId0 = vlId + "_"+"Period";
            // System.out.println("factory de 1017 transCycle周期:" + transCycle);
            transition0 = buildTransition(net, transtionId0, 2, transCycle, 0, x+135, y);
        }else {
            placeId1 = vlId + "_Sampling_port";
            place1 = buildPlace(net, placeId1, 0, x+315, y);
            transtionId0 = vlId + "_"+"EventM";
            transition0 = buildTransition(net, transtionId0, 1, transCycle, 0, x+135, y);
        }


        String placeId2 = vlId + "_Loss";
        Element place2 = buildPlace(net, placeId2, 0, x+630, y);
        String placeId3 = vlId + "_Wait1";
        Element place3 = buildPlace(net, placeId3, 1, x-60, y+165);

        String placeId4 = vlId + "_Wait2";
        Element place4 = buildPlace(net, placeId4, 0, x+225, y+210);
        String placeId5 = vlId + "_Schedule";
        Element place5 = buildPlace(net, placeId5, 0, x+540, y+210);

        String placeId6 = vlId + "_Buffer";
        Element place6 = buildPlace(net, placeId6, cacheSize1, x+735, y+90);

        String placeId7 = vlId + "_Queue";
        Element place7 = buildPlace(net, placeId7, 0, x+735, y+225);



        String transtionId1 = vlId + "_"+"TLoss";
        Element transition1 = buildTransition(net, transtionId1,0, 0, 0, x+465, y);

        // System.out.println("factory de 1046 bag:" + bag);
        String transtionId2 = vlId + "_"+"BAG";
        Element transition2 = buildTransition(net, transtionId2,2, bag, 0,x+60, y+210);

        String transtionId3 = vlId + "_"+"Jitter";
        Element transition3 = buildTransition(net, transtionId3,1 , jittRate, 0, x+390, y+210);

        String transtionId4 = vlId + "_"+"Enqueue";
        Element transition4 = buildTransition(net, transtionId4,0 , 0, 0, x+630, y+150);

        String transtionId5 = vlId + "_"+"TechLatency";
        Element transition5 = buildTransition(net, transtionId5,2 , techLa, 0, x+840, y+150);
        //System.out.println(transtionId5);

        String placeId8 = vlId + "_WaitPeriod";
        Element place8 = buildPlace(net, placeId8, 0, x+500, y+500);

        String transtionId6 = vlId + "_"+"TransPeriod";
        Element transition6 = buildTransition(net, transtionId6,2 , 0, 0, x+700, y+500);

        String placeId9 = vlId + "_Send";
        Element place9 = buildPlace(net, placeId9, 0, x+840, y+500);

        listOfTranstionVLEnd.add(transition5);


        addArc(place8,transition6,net,1,false,true);
        addArc(transition6,place9,net,1,false,true);
        addArc(transition6,place8,net,1,false,true);
        addArc(place9,transition5,net,1,false,true);
        //Element source, Element target,  Element net , int times , boolean isInhibitor
        addArc(place0,transition0,net,1,false,true);
        addArc(transition0,place0,net,1,false,false);
        addArc(transition0,place1,net,1,false,false);
        addArc(place1,transition1,net,1,false,false);
        addArc(place1,transition4,net,1,false,false);
        addArc(transition1,place2,net,1,false,false);

        addArc(place3,transition2,net,1,false,false);
        addArc(transition2,place4,net,1,false,false);
        addArc(place4,transition3,net,1,false,false);
        addArc(transition3,place5,net,1,false,false);
        addArc(place5,transition4,net,1,false,false);
        addArc(transition4,place7,net,1,false,false);
        addArc(place7,transition5,net,1,false,false);
        addArc(transition5,place6,net,1,false,false);
        addArc(place6,transition4,net,1,false,false);
        addArc(place6,transition1,net,1,true,false);
        addArc(transition4,place3,net,1,false,false);

        Boolean isPeriodical2 = info2.getPeriodical();
        double transCycle2 = info2.getTransCycle();
        double bag2 = info2.getBAG();
        //int cacheSize2 = info2.getCacheSize();
        String vlId2 = info2.getVlId();

        String placeId20 = vlId2 + "_Application";
        Element place20 = buildPlace(net, placeId20, 1, x, y+450);
        String placeId21;
        Element place21;
        String transtionId20 ;
        Element transition20 ;
        if(isPeriodical2)
        {
            placeId21 = vlId2 + "_Sampling_port";
            place21 = buildPlace(net, placeId21, 0, x+315, y+450);
            transtionId20 = vlId2 + "_"+"Period";
            // System.out.println("factory de 1017 transCycle周期:" + transCycle);
            transition20 = buildTransition(net, transtionId20, 2, transCycle2, 0, x+135, y+450);
        }else {
            placeId21 = vlId2 + "_Sampling_port";
            place21 = buildPlace(net, placeId21, 0, x+315, y+450);
            transtionId20 = vlId2 + "_"+"EventM";
            transition20 = buildTransition(net, transtionId20, 1, transCycle2, 0, x+135, y+450);
        }


        String placeId22 = vlId2 + "_Loss";
        Element place22 = buildPlace(net, placeId22, 0, x+630, y+450);
        String placeId23 = vlId2 + "_Wait1";
        Element place23 = buildPlace(net, placeId23, 1, x-60, y+350);

        String placeId24 = vlId2 + "_Wait2";
        Element place24 = buildPlace(net, placeId24, 0, x+225, y+300);
        String placeId25 = vlId2 + "_Schedule";
        Element place25 = buildPlace(net, placeId25, 0, x+540, y+300);

        String transtionId21 = vlId2 + "_"+"TLoss";
        Element transition21 = buildTransition(net, transtionId21,0, 0, 0, x+465, y+450);

        // System.out.println("factory de 1046 bag:" + bag);
        String transtionId22 = vlId2 + "_"+"BAG";
        Element transition22 = buildTransition(net, transtionId22,2, bag2, 0,x+60, y+300);
        String transtionId23 = vlId2 + "_"+"Jitter";
        Element transition23 = buildTransition(net, transtionId23,1 , jittRate, 0, x+390, y+300);

        String transtionId24 = vlId2 + "_"+"Enqueue";
        Element transition24 = buildTransition(net, transtionId24,0 , 0, 0, x+630, y+350);

        //System.out.println(transtionId5);

        //listOfTranstionVLEnd.add(transition5);


        //Element source, Element target,  Element net , int times , boolean isInhibitor
        addArc(place20,transition20,net,1,false,true);
        addArc(transition20,place20,net,1,false,false);
        addArc(transition20,place21,net,1,false,false);
        addArc(place21,transition21,net,1,false,false);
        addArc(place21,transition24,net,1,false,false);
        addArc(transition21,place22,net,1,false,false);

        addArc(place23,transition22,net,1,false,false);
        addArc(transition22,place24,net,1,false,false);
        addArc(place24,transition23,net,1,false,false);
        addArc(transition23,place25,net,1,false,false);
        addArc(place25,transition24,net,1,false,false);
        //addArc(transition24,place27,net,1,false,false);
        //addArc(place27,transition25,net,1,false,false);
        //addArc(transition25,place26,net,1,false,false);
        //addArc(place26,transition24,net,1,false,false);
        //addArc(place26,transition21,net,1,true,false);
        addArc(transition24,place23,net,1,false,false);


        addArc(transition24,place7,net,1,false,false);
        //addArc(place7,transition5,net,1,false,false);
        //addArc(transition5,place6,net,1,false,false);
        addArc(place6,transition21,net,1,false,false);
        addArc(place6,transition24,net,1,true,false);
    }
     private static void addVL1(VLInfo info,Element net,int x,int y,ArrayList<VLInfo> infoList,double lxPeriod){
        Boolean isPeriodical = info.getPeriodical();
        double transCycle = info.getTransCycle();
        double bag = info.getBAG();
        int cacheSize = info.getCacheSize();
        String vlId = info.getVlId();

        System.out.println("-----------------------");
         //System.out.println("transCycle:"+transCycle);
         //System.out.println("cacheSize:"+cacheSize);
        // System.out.println("lxPeriod"+lxPeriod);

         //最后一个参数用于得到该分区中每条链路的BAG
         int len = infoList.size();
         int j = 0;
         double[] baglist = new double[len];
         double[] packagelist = new double[len];
         while(j < len){
             VLInfo infoi = infoList.remove(j);
             baglist[j] = infoi.getBAG();
             packagelist[j] = infoi.getPackageSize();
             j++;
         }
         //计算weight
         int weight = 0;
         double jitt = 0;
         for(int i=0;i<len;i++){
             weight += Math.floor(lxPeriod/(baglist[i]+1));
             jitt = 0.04 + (packagelist[i]+20) * 8 / (Math.pow(10,5));
         }
         if(weight==0)
             weight = 1;
        double jittRate = 1 / (jitt/1000);//单位得化成s
         double techLa = 0.15;

        String placeId0 = vlId + "_Application";
        Element place0 = buildPlace(net, placeId0, 1, x, y);
        String placeId1;
        Element place1;
        String transtionId0 ;
        Element transition0 ;
        if(isPeriodical)
        {
           placeId1 = vlId + "_Sampling_port";
           place1 = buildPlace(net, placeId1, 0, x+315, y);
           transtionId0 = vlId + "_"+"Period";
           // System.out.println("factory de 1017 transCycle周期:" + transCycle);
           transition0 = buildTransition(net, transtionId0, 2, transCycle, 0, x+135, y);
        }else {
            placeId1 = vlId + "_Sampling_port";
            place1 = buildPlace(net, placeId1, 0, x+315, y);
            transtionId0 = vlId + "_"+"EventM";
            transition0 = buildTransition(net, transtionId0, 1, transCycle, 0, x+135, y);
        }


        String placeId2 = vlId + "_Loss";
        Element place2 = buildPlace(net, placeId2, 0, x+630, y);
        String placeId3 = vlId + "_Wait1";
        Element place3 = buildPlace(net, placeId3, 1, x-60, y+165);

        String placeId4 = vlId + "_Wait2";
        Element place4 = buildPlace(net, placeId4, 0, x+225, y+210);
        String placeId5 = vlId + "_Schedule";
        Element place5 = buildPlace(net, placeId5, 0, x+540, y+210);

        String placeId6 = vlId + "_Buffer";
        Element place6 = buildPlace(net, placeId6, cacheSize, x+735, y+90);
        String placeId7 = vlId + "_Queue";
        Element place7 = buildPlace(net, placeId7, 0, x+735, y+225);


        String transtionId1 = vlId + "_"+"TLoss";
        Element transition1 = buildTransition(net, transtionId1,0, 0, 0, x+465, y);

       // System.out.println("factory de 1046 bag:" + bag);
        String transtionId2 = vlId + "_"+"VL-BAG";
        Element transition2 = buildTransition(net, transtionId2,2, bag, 0,x+60, y+210);
        String transtionId3 = vlId + "_"+"Jitter";
        Element transition3 = buildTransition(net, transtionId3,1 , jittRate, 0, x+390, y+210);

        String transtionId4 = vlId + "_"+"Enqueue";
        Element transition4 = buildTransition(net, transtionId4,0 , 0, 0, x+630, y+150);

        String transtionId5 = vlId + "_"+"TechLatency";
        Element transition5 = buildTransition(net, transtionId5,2 , techLa, 0, x+840, y+150);
        //System.out.println(transtionId5);

        String placeId8 = vlId + "_WaitPeriod";
        Element place8 = buildPlace(net, placeId8, 0, x+500, y+280);

        String transtionId6 = vlId + "_"+"TransPeriod";
        Element transition6 = buildTransition(net, transtionId6,2 , lxPeriod, 0, x+700, y+280);

        String placeId9 = vlId + "_Send";
        Element place9 = buildPlace(net, placeId9, 0, x+840, y+280);

        listOfTranstionVLEnd.add(transition5);


        addArc(place8,transition6,net,1,false,true);
        addArc(transition6,place9,net,weight,false,true);
        addArc(transition6,place8,net,1,false,true);
        addArc(place9,transition5,net,1,false,true);
        //Element source, Element target,  Element net , int times , boolean isInhibitor
        addArc(place0,transition0,net,1,false,true);
        addArc(transition0,place0,net,1,false,false);
        addArc(transition0,place1,net,1,false,false);
        addArc(place1,transition1,net,1,false,false);
        addArc(place1,transition4,net,1,false,false);
        addArc(transition1,place2,net,1,false,false);

        addArc(place3,transition2,net,1,false,false);
        addArc(transition2,place4,net,1,false,false);
        addArc(place4,transition3,net,1,false,false);
        addArc(transition3,place5,net,1,false,false);
        addArc(place5,transition4,net,1,false,false);
        addArc(transition4,place7,net,1,false,false);
        addArc(place7,transition5,net,1,false,false);
        addArc(transition5,place6,net,1,false,false);
        addArc(place6,transition4,net,1,false,false);
        addArc(place6,transition1,net,1,true,false);
        addArc(transition4,place3,net,1,false,false);

    }
    */
    /*
    private static void addVL(VLInfo info,Element net,int x,int y){
        Boolean isPeriodical = info.getPeriodical();
        double transCycle = info.getTransCycle();
        double bag = info.getBAG();
        double jitt = 0.04 + (info.getPackageSize()+20) * 8 / (Math.pow(10,5));
        double jittRate = 1 / jitt;
        int cacheSize = info.getCacheSize();
        String vlId = info.getVlId();


        String placeId0 = vlId + "_Application";
        Element place0 = buildPlace(net, placeId0, 1, x, y);
        String placeId1;
        Element place1;
        String transtionId0 ;
        Element transition0 ;
        if(isPeriodical)
        {
           placeId1 = vlId + "_Sampling_port";
           place1 = buildPlace(net, placeId1, 0, x+315, y);
           transtionId0 = vlId + "_"+"Period";
           // System.out.println("factory de 1017 transCycle周期:" + transCycle);
           transition0 = buildTransition(net, transtionId0, 2, transCycle, 0, x+135, y);
        }else {
            placeId1 = vlId + "_Sampling_port";
            place1 = buildPlace(net, placeId1, 0, x+315, y);
            transtionId0 = vlId + "_"+"EventM";
            transition0 = buildTransition(net, transtionId0, 1, transCycle, 0, x+135, y);
        }


        String placeId2 = vlId + "_Loss";
        Element place2 = buildPlace(net, placeId2, 0, x+630, y);
        String placeId3 = vlId + "_Wait1";
        Element place3 = buildPlace(net, placeId3, 1, x-60, y+165);

        String placeId4 = vlId + "_Wait2";
        Element place4 = buildPlace(net, placeId4, 0, x+225, y+210);
        String placeId5 = vlId + "_Schedule";
        Element place5 = buildPlace(net, placeId5, 0, x+540, y+210);

        String placeId6 = vlId + "_Buffer";
        Element place6 = buildPlace(net, placeId6, cacheSize, x+735, y+90);
        String placeId7 = vlId + "_Queue";
        Element place7 = buildPlace(net, placeId7, 0, x+735, y+225);


        String transtionId1 = vlId + "_"+"TLoss";
        Element transition1 = buildTransition(net, transtionId1,0, 0, 0, x+465, y);

       // System.out.println("factory de 1046 bag:" + bag);
        String transtionId2 = vlId + "_"+"VL-BAG";
        Element transition2 = buildTransition(net, transtionId2,2, bag, 0,x+60, y+210);
        String transtionId3 = vlId + "_"+"Jitter";
        Element transition3 = buildTransition(net, transtionId3,1 , jittRate, 0, x+390, y+210);

        String transtionId4 = vlId + "_"+"Enqueue";
        Element transition4 = buildTransition(net, transtionId4,0 , 0, 0, x+630, y+150);

        String transtionId5 = vlId + "_"+"TechLatency";
        Element transition5 = buildTransition(net, transtionId5,2 , 0, 0, x+840, y+150);
        //System.out.println(transtionId5);

        String placeId8 = vlId + "_WaitPeriod";
        Element place8 = buildPlace(net, placeId8, 0, x+500, y+280);

        String transtionId6 = vlId + "_"+"TransPeriod";
        Element transition6 = buildTransition(net, transtionId6,2 , 0, 0, x+700, y+280);

        String placeId9 = vlId + "_Send";
        Element place9 = buildPlace(net, placeId9, 0, x+840, y+280);

        listOfTranstionVLEnd.add(transition5);


        addArc(place8,transition6,net,1,false,true);
        addArc(transition6,place9,net,1,false,true);
        addArc(transition6,place8,net,1,false,true);
        addArc(place9,transition5,net,1,false,true);
        //Element source, Element target,  Element net , int times , boolean isInhibitor
        addArc(place0,transition0,net,1,false,true);
        addArc(transition0,place0,net,1,false,false);
        addArc(transition0,place1,net,1,false,false);
        addArc(place1,transition1,net,1,false,false);
        addArc(place1,transition4,net,1,false,false);
        addArc(transition1,place2,net,1,false,false);

        addArc(place3,transition2,net,1,false,false);
        addArc(transition2,place4,net,1,false,false);
        addArc(place4,transition3,net,1,false,false);
        addArc(transition3,place5,net,1,false,false);
        addArc(place5,transition4,net,1,false,false);
        addArc(transition4,place7,net,1,false,false);
        addArc(place7,transition5,net,1,false,false);
        addArc(transition5,place6,net,1,false,false);
        addArc(place6,transition4,net,1,false,false);
        addArc(place6,transition1,net,1,true,false);
        addArc(transition4,place3,net,1,false,false);

    }
*/
}