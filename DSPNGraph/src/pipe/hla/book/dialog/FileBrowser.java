
package pipe.hla.book.dialog;

import java.awt.*;

public final class FileBrowser implements Browser {
	private FileDialog _dialog;
	private String _result;

public FileBrowser (Frame parent) {
	_dialog = new FileDialog(parent);
	_dialog.setMode(FileDialog.LOAD);
	_result = null;
}

public void dispose ( ) {
	_dialog.dispose();
}

public String getResult() {
	return _result;
}

public void setTitle(String title) {
	_dialog.setTitle(title);
}

public void show ( ) {
	//we block here until dialog returns
	_dialog.show();
	//collect result
	String dir = _dialog.getDirectory();
	_result = dir != null ? dir : "";
	String file = _dialog.getFile(); 
	if (file != null) _result += file;
}
}
