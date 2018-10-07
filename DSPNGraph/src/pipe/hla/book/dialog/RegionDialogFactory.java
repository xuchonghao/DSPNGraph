

package pipe.hla.book.dialog;

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