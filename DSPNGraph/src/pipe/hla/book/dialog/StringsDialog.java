
package pipe.hla.book.dialog;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StringsDialog extends JDialog {
  JPanel contentsPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton cancelButton = new JButton();
  JButton okButton = new JButton();

	JButton[] _browserButtons;
	int _fieldCount;
	JLabel[] _labels;
	int _resultCode;
	JTextField[] _textFields;
	int _whichBrowsing;
	public final static int OK = 1;
	public final static int BROWSE = 2;
	public final static int CANCEL = 3;
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  class BrowserButtonActionListener implements java.awt.event.ActionListener {
    private int _index;

    BrowserButtonActionListener(int index) {
      _index = index;
    }

    public void actionPerformed(ActionEvent e) {
      _whichBrowsing = _index;
      _resultCode = BROWSE;
      dispose();
    }
  }

  public StringsDialog(
    JFrame frame,
    String title, 
	  String[] labels,
	  String[] defaults,
	  BrowserFactory[] browsers)
  {
  	this(frame, title, true);
    _fieldCount = labels.length;
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    _labels = new JLabel[_fieldCount];
    _textFields = new JTextField[_fieldCount];
    _browserButtons = new JButton[_fieldCount];
    for (int i = 0; i < _fieldCount; ++i) {
      _labels[i] = new JLabel(labels[i], SwingConstants.RIGHT);
      //set which row
      gbc.gridy = i;
      //second-to-last in row
      gbc.gridwidth = GridBagConstraints.RELATIVE;
      mainPanel.add(_labels[i], gbc);
      JPanel temp = new JPanel();
      temp.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));
      //last in row
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      mainPanel.add(temp, gbc);
      _textFields[i] = new JTextField(20);
      _textFields[i].setText(defaults[i]);
      temp.add(_textFields[i]);
      if (browsers[i] != null) {
        _browserButtons[i] = new JButton("Browse...");
        _browserButtons[i].setActionCommand("button");
        //_browserButtons[i].setLabel("Browse...");
        temp.add(_browserButtons[i]);
      }
      else _browserButtons[i] = null;
    }
    for (int i = 0; i < _fieldCount; ++i) {
      if (_browserButtons[i] != null) {
        _browserButtons[i].addActionListener(
          new BrowserButtonActionListener(i));
      }
    }
    pack();
    //Center the window
    Dimension parentSize = frame.getSize();
    Point parentLoc = frame.getLocation();
    Dimension frameSize = this.getSize();
    this.setLocation(
      Math.max(0, parentLoc.x + (parentSize.width- frameSize.width) / 2),
      Math.max(0, parentLoc.y + (parentSize.height - frameSize.height) /2));
    //force layout to run again (we're working around a Swing 1.1. bug)
    Dimension currentSize = getSize();
    currentSize.width += 3;
    currentSize.height += 3;
    setSize(currentSize);
  }

  
  public StringsDialog(JFrame frame, String title, boolean modal) {
    super(frame, title, modal);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try  {
      jbInit();
      getContentPane().add(contentsPanel);
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  
  public StringsDialog(JFrame frame) {
    this(frame, "", false);
  }

  
  public StringsDialog(JFrame frame, boolean modal) {
    this(frame, "", modal);
  }

  
  public StringsDialog(JFrame frame, String title) {
    this(frame, title, false);
  }

  void jbInit() throws Exception {
    contentsPanel.setLayout(borderLayout1);
    buttonPanel.setLayout(flowLayout1);
    mainPanel.setLayout(gridBagLayout1);
    cancelButton.setText("Cancel");
    cancelButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cancelButton_actionPerformed(e);
      }
    });
    okButton.setText("OK");
    okButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        okButton_actionPerformed(e);
      }
    });
    contentsPanel.add(mainPanel, BorderLayout.CENTER);
    contentsPanel.add(buttonPanel, BorderLayout.SOUTH);
    buttonPanel.add(okButton, null);
    buttonPanel.add(cancelButton, null);
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }

  void cancel() {
    _resultCode = CANCEL;
    dispose();
  }

  void okButton_actionPerformed(ActionEvent e) {
    _resultCode = OK;
    dispose();
  }

  void cancelButton_actionPerformed(ActionEvent e) {
    _resultCode = CANCEL;
    dispose();
  }

	String[] results() {
	  String[] theResults = new String[_fieldCount];
	  for (int i = 0; i < _fieldCount; ++i) {
	    theResults[i] = _textFields[i].getText();
	  }
	  return theResults;
	}
}

             
