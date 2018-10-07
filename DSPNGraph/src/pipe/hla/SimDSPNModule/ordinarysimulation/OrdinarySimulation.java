/**
 * Simulation IModule
 * @author James D Bloom (UI)
 * @author Clare Clark (Maths)
 * @author Maxim (replacement UI and cleanup)
 *
 * @author Davd Patterson (handle null return from fireRandomTransition)
 *
 */
package pipe.hla.SimDSPNModule.ordinarysimulation;

import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.hla.SimDSPNModule.ui.PetriNetChooserPanel1;
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



public class OrdinarySimulation extends SwingWorker implements IModule
{

    private static final String MODULE_NAME = "OrdinarySimulation";

    private PetriNetChooserPanel1 sourceFilePanel;
    private ResultsHTMLPane results;
    private OLGraph olGraph;

    private JTextField jtfFirings, jtfCycles;

    public static GuideModel getGuideModel() {
        return guideModel;
    }

    public static void setGuideModel(GuideModel guideModel) {
        OrdinarySimulation.guideModel = guideModel;
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

        TransferatorFromPIPEHelper.printList();
        // 2 Add file browser
        sourceFilePanel = new PetriNetChooserPanel1("Source net", olGraph);
        contentPane.add(sourceFilePanel);

        // 2.5 Add edit boxes
        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.LINE_AXIS));
        settings.add(new JLabel("Firings:"));
        settings.add(Box.createHorizontalStrut(5));
        settings.add(jtfFirings = new JTextField("10", 5));
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
    //if (!sourceDataLayer.getPetriNetObjects().hasNext()) {

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
            if(sourceDataLayer.getVexnum() <= 0)
            {
                s += "No Petri net objects defined!";
            }else
            {
                try{
                    //PNMLWriter.saveTemporaryFile(sourceDataLayer, this.getClass().getName());
                    int firings = Integer.parseInt(jtfFirings.getText());
                    int cycles = 1;//Integer.parseInt(jtfCycles.getText());
                    long start = System.currentTimeMillis();
                    s += new SimulationLogical().simulate(olGraph, cycles, firings);
                    long end = System.currentTimeMillis();
                    long value = end - start;
                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~运行时间是："+value);

                    results.setEnabled(true);
                }
                catch(NumberFormatException e){
                    s += "Invalid parameter!";
                }
                catch(OutOfMemoryError oome){
                    System.gc();
                    results.setText("");
                    s = "Memory error: " + oome.getMessage();

                    s += "<br>Not enough memory. Please use a larger heap size."
                            + "<br>"
                            + "<br>Note:"
                            + "<br>The Java heap size can be specified with the -Xmx option."
                            + "<br>E.g., to use 512MB as heap size, the command line looks like this:"
                            + "<br>java -Xmx512m -classpath ...\n";
                    results.setText(s);
                    return;
                }
                catch(Exception e){
                    e.printStackTrace();
                    s = "<br>Error" + e.getMessage();
                    results.setText(s);
                    return;
                }
            }
            results.setText(s);
        }
    };

    @Override
    protected Object doInBackground() throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
