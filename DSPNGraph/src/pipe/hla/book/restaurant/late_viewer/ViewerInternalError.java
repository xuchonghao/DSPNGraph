 /** Used to signal internal errors in federate
 */

 package pipe.hla.book.restaurant.late_viewer;

public class ViewerInternalError extends Exception {
  public ViewerInternalError(String s) {
    super(s);
  }
} 
