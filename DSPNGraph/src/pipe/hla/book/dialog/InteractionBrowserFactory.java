
package pipe.hla.book.dialog;

import java.awt.*;

public final class InteractionBrowserFactory implements BrowserFactory {

public Browser create(Frame parent) {
	return new InteractionBrowser(parent);
}
}
