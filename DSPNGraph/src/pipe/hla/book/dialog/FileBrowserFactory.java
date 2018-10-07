
package pipe.hla.book.dialog;

import java.awt.*;

public final class FileBrowserFactory implements BrowserFactory {

public Browser create(Frame parent) {
	return new FileBrowser(parent);
}
}
