
package pipe.hla.book.dialog;

import javax.swing.*;

public final class StringsDialogProxy
{
  String[] _results;
  int _resultCode;


/**
* @param parent--the Frame that acts as parent to the dialog
* @param title--title for the dialog
* @param labels--array of Strings for labels on the fields
* @param defaults--Strings of initial values of TextFields. Must be "" not null.
* @param browsers--array of browser factories to select from
*/
	public int queryUser(
	  JFrame parent,
	  String title,
	  String[] labels,
	  String[] defaults,
	  BrowserFactory[] browsers)
	{
		if (labels.length != defaults.length
		  || defaults.length != browsers.length
		  || browsers.length != labels.length) throw new
		  ArrayIndexOutOfBoundsException("All array args must have same length");
		_results = defaults;
		while (true) {
  		StringsDialog sdialog = new StringsDialog(
  		  parent,
  		  title,
  		  labels,
  		  _results,
  		  browsers);
  		sdialog.show();
  		if (sdialog._resultCode == StringsDialog.CANCEL) {
  		  _resultCode = sdialog._resultCode;
  		  break;
  		}
  		else if (sdialog._resultCode == StringsDialog.BROWSE) {
  		  int whichToBrowse = sdialog._whichBrowsing;
  		  _results = sdialog.results();
  			Browser browser = browsers[whichToBrowse].create(parent);
				browser.setTitle(labels[whichToBrowse]);
  		  browser.show();
  		  //our thread blocks until dialog is closed by user
  		  _results[whichToBrowse] = browser.getResult();
  		}
  		else if (sdialog._resultCode == StringsDialog.OK) {
  		  _results = sdialog.results();
  		  _resultCode = sdialog._resultCode;
  		  break;
  		}
		}
		return _resultCode;
	}

	public String[] results() {
	  return _results;
	}
}
