
package pipe.hla.book.test_federate.dialog;

import java.awt.*;

public final class InteractionBrowserFactory implements BrowserFactory {

public Browser create(Frame parent) {
	return new InteractionBrowser(parent);
}
}
