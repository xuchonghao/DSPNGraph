package pipe.hla.SimDSPNModule.hlasimulation;


import org.apache.hadoop.dfs.SecondaryNameNode;
import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.ui.PetriNetChooserPanel1;
import pipe.hla.SimDSPNModule.util.ReConstructSubNet;
import pipe.hla.SimDSPNModule.util.SubGraphByPlaceAndTransitionUtils;
import pipe.hla.SimDSPNModule.util.TransferatorFromPIPEHelper;
import pipe.gui.ApplicationSettings;
import pipe.gui.widgets.newwidges.bean.GuideModel;
import pipe.gui.widgets.oldwidges.ButtonBar;
import pipe.gui.widgets.oldwidges.EscapableDialog;
import pipe.gui.widgets.oldwidges.ResultsHTMLPane;
import pipe.modules.interfaces.IModule;
import pipe.views.PetriNetView;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class ParallelSimulation  extends SwingWorker implements IModule{

    private static final String MODULE_NAME = "ParallelSimulation";

    public static HashMap<String,OLGraph> map = new HashMap();
    private PetriNetChooserPanel1 sourceFilePanel;
    private ResultsHTMLPane results;
    private OLGraph olGraph;

    private JTextField jtfFirings, jtfCycles;

    public static GuideModel getGuideModel() {
        return guideModel;
    }

    public static void setGuideModel(GuideModel guideModel) {
        ParallelSimulation.guideModel = guideModel;
    }

    private static GuideModel guideModel;

    public void start()
    {
        PetriNetView pnmlData = ApplicationSettings.getApplicationView().getCurrentPetriNetView();

        EscapableDialog guiDialog = new EscapableDialog(ApplicationSettings.getApplicationView(), MODULE_NAME, true);

        // 1 Set layout
        Container contentPane = guiDialog.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        olGraph = new OLGraph();
        //1.5 创建OLGraph
        olGraph = TransferatorFromPIPEHelper.transfer(olGraph,pnmlData);

        // 2 Add file browser
        sourceFilePanel = new PetriNetChooserPanel1("Source net", olGraph);
        contentPane.add(sourceFilePanel);

        // 2.5 Add edit boxes
        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.LINE_AXIS));
        settings.add(new JLabel("Firings:"));
        settings.add(Box.createHorizontalStrut(5));
        settings.add(jtfFirings = new JTextField("1000", 5));
        settings.add(Box.createHorizontalStrut(10));
        settings.add(new JLabel("Replications:"));
        settings.add(Box.createHorizontalStrut(5));
        settings.add(jtfCycles = new JTextField("5", 5));
        settings.setBorder(new TitledBorder(new EtchedBorder(),
                "Simulation parameters"));
        settings.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                settings.getPreferredSize().height));
        contentPane.add(settings);

        // 3 Add results pane
        results = new ResultsHTMLPane(pnmlData.getPNMLName());
        contentPane.add(results);

        // 4 Add button
        contentPane.add(new ButtonBar("SimulateCompute", simulateButtonClick,
                guiDialog.getRootPane()));

        // 5 Make window fit contents' preferred size
        guiDialog.pack();

        // 6 Move window to the middle of the screen
        guiDialog.setLocationRelativeTo(null);

        guiDialog.setVisible(true);
    }


    public String getName()
    {
        return MODULE_NAME;
    }

    /**
     * Simulate button click handler
     */
    private final ActionListener simulateButtonClick = new ActionListener()
    {

        public void actionPerformed(ActionEvent arg0)
        {
            OLGraph sourceDataLayer = sourceFilePanel.getDataLayer();

            String s = "<h2>Petri net simulation results</h2>";
            if(sourceDataLayer == null)
            {
                JOptionPane.showMessageDialog(null, "Please, choose a source net",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(sourceDataLayer.getVexnum() <= 0){
                s += "No Petri net objects defined!";
            }else{
                int firings = Integer.parseInt(jtfFirings.getText());
                //int cycles = Integer.parseInt(jtfCycles.getText());

                partitionMain(firings);

                results.setEnabled(true);
            }
            results.setText(s);
        }
    };

    public void partitionProcess(){
        int nparts = 3;
        int len = 1000;
        long start = System.currentTimeMillis();
        //1、划分为nparts个子网
        OLGraph[] subPNs = SubGraphByPlaceAndTransitionUtils.MainProcessOfPartition(olGraph,nparts);

        System.out.println("~~~~~~~~"+(System.currentTimeMillis()-start));
        System.out.println("划分完毕！");
        map.put("0",subPNs[0]);
        map.put("1",subPNs[1]);
        map.put("2",subPNs[2]);
        //2、将他们分配到多个机器上进行仿真
        try {
            CyclicBarrier barrier = new CyclicBarrier(nparts);
            //创建一个服务器端的Socket，即ServerSocket,绑定需要监听的端口
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket socket = null;

            //记录连接过服务器的客户端数量
            int count = 0;
            System.out.println("***服务器即将启动，等待客户端的连接***");
            while(true){//循环侦听新的客户端的连接
                //调用accept（）方法侦听，等待客户端的连接以获取Socket实例
                socket = serverSocket.accept();
                //创建新线程
                Thread thread = new Thread(new ServerThread(socket,barrier));
                thread.start();

                count++;
                System.out.println("服务器端被连接过的次数："+count);
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP为："+address.getHostAddress());
                if(count == 122){
                    serverSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected Object doInBackground() throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void partitionMain(int firings){
        int nparts = 3;
        int len = firings;

        //1、划分为nparts个子网
        OLGraph[] subPNs = SubGraphByPlaceAndTransitionUtils.MainProcessOfPartition(olGraph,nparts);

        CyclicBarrier barrier = new CyclicBarrier(nparts);
        AtomicInteger atomicInteger = new AtomicInteger(0);

        new Thread(new MainThread(subPNs[0],"subnet"+0,barrier,atomicInteger,len)).start();
        for(int i=1;i<nparts;i++){
            new Thread(new SenonderyThread(subPNs[i],"subnet"+i,barrier,atomicInteger,len)).start();
        }
    }

    /**在线程内运行*/
    public static void functionTest(){
        OLGraph subGraph1 = new ReConstructSubNet().testFederate("p0","t0","p1","t1","p2","t3",1);
        OLGraph subGraph2 = new ReConstructSubNet().testFederate("p2","t2","p3","t3","p0","t1",2);
        CyclicBarrier barrier = new CyclicBarrier(2);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int len = 1000;
        new Thread(new MainThread(subGraph1,"subnet1",barrier,atomicInteger,len)).start();
        new Thread(new SenonderyThread(subGraph2,"subnet2",barrier,atomicInteger,len)).start();
    }

    public static void main(String[] args) {
        //partitionProcess();
    }
    /**多个进程运行*/
    public static void functionTest1(){
        OLGraph subGraph1 = new ReConstructSubNet().testFederate("p0","t0","p1","t1","p2","t3",1);
        OLGraph subGraph2 = new ReConstructSubNet().testFederate("p2","t2","p3","t3","p0","t1",2);

        map.put("0",subGraph1);
        map.put("1",subGraph2);
        //2、将他们分配到多个机器上进行仿真
        try {
            CyclicBarrier barrier = new CyclicBarrier(2);
            //创建一个服务器端的Socket，即ServerSocket,绑定需要监听的端口
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket socket = null;

            //记录连接过服务器的客户端数量
            int count = 0;
            System.out.println("***服务器即将启动，等待客户端的连接***");
            while(true){//循环侦听新的客户端的连接
                //调用accept（）方法侦听，等待客户端的连接以获取Socket实例
                socket = serverSocket.accept();
                //创建新线程
                Thread thread = new Thread(new ServerThread(socket,barrier));
                thread.start();

                count++;
                System.out.println("服务器端被连接过的次数："+count);
                InetAddress address = socket.getInetAddress();
                System.out.println("当前客户端的IP为："+address.getHostAddress());
                if(count == 122){
                    serverSocket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
