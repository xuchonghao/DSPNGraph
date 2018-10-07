/**
 * Holds data related to one Region for local bookkeeping.
 */

package pipe.hla.book.test_federate;
import hla.rti.*;

public class RegionRecord {
  public Region _region;
  public String _localName; //redundant with key but useful
  public String _spaceName;
  public int _spaceHandle;
  public String _dimensionNameA;
  public int _dimensionHandleA;
  public String _dimensionNameB;
  public int _dimensionHandleB;
  public String _dimensionNameC;
  public int _dimensionHandleC;

  public RegionRecord() {
  }
} 