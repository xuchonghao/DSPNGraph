package pipe.hla.SimDSPNModule.ui;

import pipe.hla.SimDSPNModule.basemodel.OLGraph;
import pipe.gui.widgets.oldwidges.FileBrowser;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Makes a filebrowser panel which is a JPanel containing the required stuff
 * @author Maxim
 */
public class PetriNetChooserPanel1 extends JPanel {

    private JCheckBox  useCurrent;
    private JLabel     label;
    private JTextField textField;
    private JButton    browse;
    private final OLGraph _defaultNetView;

    public PetriNetChooserPanel1(String title, OLGraph _defaultNetView) {
        super();

        this.setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));

        this._defaultNetView = _defaultNetView;
        if (this._defaultNetView != null) {
            useCurrent=new JCheckBox("Use current net",true);
            ActionListener useCurrentClick = new ActionListener()
            {

                public void actionPerformed(ActionEvent e)
                {
                    boolean enabled = !useCurrent.isSelected();
                    label.setEnabled(true);
                    textField.setEnabled(true);
                    browse.setEnabled(true);
                }
            };
            useCurrent.addActionListener(useCurrentClick);
            this.add(useCurrent);
            this.add(Box.createHorizontalStrut(10));
        }


        label=new JLabel("Filename:");

        this.add(label);
        this.add(Box.createHorizontalStrut(5));

        textField = new JTextField((this._defaultNetView !=null? this._defaultNetView.getPNMLName():null),15);
//    textField.setMaximumSize(new Dimension(Integer.MAX_VALUE,this.getPreferredSize().height));
//    textField.setPreferredSize(new Dimension());
        this.add(textField);
        this.add(Box.createHorizontalStrut(5));

        browse=new JButton("Browse");
        ActionListener browseButtonClick = new ActionListener()
        {

            public void actionPerformed(ActionEvent e)
            {
                File f = new FileBrowser(textField.getText()).openFile();
                if(f != null)
                {
                    textField.setText(f.getAbsolutePath());
                }
            }
        };
        browse.addActionListener(browseButtonClick);
        this.add(browse);

        this.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED),title));

        this.setMaximumSize(new Dimension(Integer.MAX_VALUE,this.getPreferredSize().height));

        if (useCurrent!=null) {
            useCurrent.getActionListeners()[0].actionPerformed(null); // update
        }
    }

    public OLGraph getDataLayer() {
        if ((useCurrent != null) && (useCurrent.isSelected())) {
            return _defaultNetView;
        } else {
            String fileName = textField.getText();
            if (fileName == null || fileName.equals("")) {
                return null;
            } else {
                JOptionPane.showMessageDialog( null, "Error loading\n" + fileName +
                                    "\nPlease check it is a valid PNML file.", "Error",
                            JOptionPane.ERROR_MESSAGE);
            }
            return null;
        }
    }

}
