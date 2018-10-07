
package pipe.hla.book.dialog;

import java.awt.*;

public final class ObjectBrowserFactory implements BrowserFactory {


public Browser create(Frame parent) {
	return new ObjectBrowser(parent);
}
}
