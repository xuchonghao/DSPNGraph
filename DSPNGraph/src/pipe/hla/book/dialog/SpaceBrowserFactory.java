
package pipe.hla.book.dialog;

import java.awt.*;

public final class SpaceBrowserFactory implements BrowserFactory {


public Browser create(Frame parent) {
	return new SpaceBrowser(parent);
}
}
