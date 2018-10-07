
package pipe.hla.book.dialog;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

public class RegionDialog extends JDialog implements Browser {
  private String currentSelection = null;
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton cancelButton = new JButton();
  JButton acceptButton = new JButton();
  JScrollPane jScrollPane1 = new JScrollPane();
  JList list = new JList();

  
  public RegionDialog(Frame frame, String title, boolean modal) {
    try  {
      jbInit();
      enableEvents(AWTEvent.WINDOW_EVENT_MASK);
      setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public RegionDialog(Frame frame) {
    this(frame, "", true);
  }

  public RegionDialog() {
    this(null, "", false);
  }

  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
      }
    });
    acceptButton.setEnabled(false);
    acceptButton.setText("Accept");
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
      public void valueChanged(ListSelectionEvent e) {
        list_valueChanged(e);
      }
    });
    acceptButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        acceptButton_actionPerformed(e);
      }
    });
    jPanel1.setLayout(flowLayout1);
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(acceptButton, null);
    jPanel1.add(cancelButton, null);
    panel1.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(list, null);
  }

  void acceptButton_actionPerformed(ActionEvent e) {
    dispose();
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    cancel();
  }

  void cancel() {
    currentSelection = null;
    dispose();
  }

  void list_valueChanged(ListSelectionEvent e) {
    if (list.isSelectionEmpty()) {
      currentSelection = null;
      acceptButton.setEnabled(false);
    }
    else {
      currentSelection = (String)list.getSelectedValue();
      acceptButton.setEnabled(true);
    }
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }

  public String getResult() {
    return currentSelection;
  }

  public void setList(String[] newList) {
    list.setListData(newList);
  }
}

                
