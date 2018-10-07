

package pipe.hla.book.restaurant.late_viewer;
import pipe.hla.book.restaurant.*;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class ViewerPanel extends JPanel {
  private static double degToRad = Math.PI / 180.0;
  private static final int _canalWidth = 60; //pixels
  private Hashtable _chefs;
  private Hashtable _servings;
  private Hashtable _boats;
  private Hashtable _diners;
  private Image[] _sushiIcons;
  private Image _chefIcon;
  private Image _boatIcon;
  private Image _dinerIcon;

  public class Transform {
    private int _xCenter;
    private int _yCenter;
    private int _radius;

    Transform(int x, int y, int radius) {
      _xCenter = x;
      _yCenter = y;
      _radius = radius;
    }

    Point transform(Position position) {
      double s = Math.sin(position._angle * degToRad);
      double c = Math.cos(position._angle * degToRad);
      double radius;
      if (position._offset == Position.INBOARD_CANAL)
        radius = _radius  - _canalWidth;
      else if (position._offset == Position.ON_CANAL) radius = _radius;
      else radius = _radius + _canalWidth;
      Point pt = new Point();
      pt.x = (int)(c * radius) + _xCenter;
      pt.y = _yCenter - (int)(s * radius);
      return pt;
    }
  }

  public ViewerPanel() {
  }

  public void setSimulationData(
    Hashtable chefs,
    Hashtable servings,
    Hashtable boats,
    Hashtable diners)
  {
    _chefs = chefs;
    _servings = servings;
    _boats = boats;
    _diners = diners;
  }

  public void setConfigurationData(
    Image[] sushiIcons,
    Image chefIcon,
    Image boatIcon,
    Image dinerIcon)
  {
    _sushiIcons = sushiIcons;
    _chefIcon = chefIcon;
    _boatIcon = boatIcon;
    _dinerIcon = dinerIcon;
  }


  public Dimension getMaximumSize() {
    return new Dimension(300,300);
  }

  public Dimension getMinimumSize() {
    return new Dimension(300,300);
  }

  public Dimension getPreferredSize() {
    return new Dimension(300,300);
  }

  public void paintComponent(Graphics g) {
    Insets insets = getInsets();
    int currentWidth = getWidth() - insets.left - insets.right;
    int currentHeight = getHeight() - insets.top - insets.bottom;
    int lesserDimension = Math.min(currentWidth, currentHeight);
    int xCenter = lesserDimension / 2 + insets.left;
    int yCenter = lesserDimension / 2 + insets.top;
    int radius = lesserDimension / 2 - (int)(_canalWidth * 1.5);
    Transform xform = new Transform(xCenter, yCenter, radius);

    super.paintComponent(g); //paint background
    g.setColor(Color.blue);
    g.fillOval(
      insets.left + _canalWidth,
      insets.top + _canalWidth,
      lesserDimension - _canalWidth * 2,
      lesserDimension - _canalWidth * 2);
    g.setColor(Color.lightGray);
    g.fillOval(
      insets.left + _canalWidth * 2,
      insets.top + _canalWidth * 2,
      lesserDimension - _canalWidth * 4,
      lesserDimension - _canalWidth * 4);
    g.setColor(Color.black);
    synchronized (_chefs) {
      Enumeration e = _chefs.elements();
      while (e.hasMoreElements()) {
        Chef chef = (Chef)(e.nextElement());
        if (chef._positionState == AttributeState.REFLECTED) {
          Point pt = xform.transform(chef._position);
          renderIcon(g, _chefIcon, pt.x, pt.y);
          renderString(g, Chef.stateStrings[chef._state], pt.x, pt.y);
        }
      }
    }
    synchronized (_diners) {
      Enumeration e = _diners.elements();
      while (e.hasMoreElements()) {
        Diner diner = (Diner)(e.nextElement());
        if (diner._positionState == AttributeState.REFLECTED) {
          Point pt = xform.transform(diner._position);
          renderIcon(g, _dinerIcon, pt.x, pt.y);
          renderString(g, Diner.stateStrings[diner._state], pt.x, pt.y);
        }
      }
    }
    synchronized (_boats) {
      Enumeration e = _boats.elements();
      while (e.hasMoreElements()) {
        Boat boat = (Boat)(e.nextElement());
        if (boat._positionState == AttributeState.REFLECTED) {
          Point pt = xform.transform(boat._position);
          renderIcon(g, _boatIcon, pt.x, pt.y);
        }
      }
    }
    synchronized (_servings) {
      Enumeration e = _servings.elements();
      while (e.hasMoreElements()) {
        Serving serving = (Serving)(e.nextElement());
        if (serving._positionState == AttributeState.REFLECTED) {
          Point pt = xform.transform(serving._position);
          renderIcon(g, _sushiIcons[serving._type], pt.x, pt.y);
        }
      }
    }
  }

  private void renderIcon(Graphics g, Image icon, int x, int y) {
    g.drawImage(
      icon,
      x - icon.getWidth(this) / 2,
      y - icon.getHeight(this) / 2,
      this);
  }

  private void renderString(Graphics g, String s, int x, int y) {
    FontMetrics metrics = g.getFontMetrics();
    int height = metrics.getHeight();
    int width = metrics.stringWidth(s);
    g.drawString(
      s,
      x - width / 2,
      y - height / 2);
  }
}
