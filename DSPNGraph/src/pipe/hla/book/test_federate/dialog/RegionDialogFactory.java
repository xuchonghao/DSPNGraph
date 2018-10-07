

package pipe.hla.book.test_federate.dialog;

import java.awt.*;

public class RegionDialogFactory implements BrowserFactory {
  private String[] _items;

  public RegionDialogFactory() {
  }

  public Browser create(Frame frame) {
    RegionDialog rd = new RegionDialog(frame);
    rd.setList(_items);
    return rd;
  }

  public void setList(String[] items) {
    _items = items;
  }
} 