
package pipe.hla.SimDSPNModule.hlasimulation.manage;

import hla.rti.AttributeHandleSet;
import hla.rti.ObjectNotKnown;
import hla.rti.RTIambassador;
import hla.rti.RTIexception;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public final class FederateTable extends AbstractTableModel
{
  private final class FederateEntry {
    int _MOMhandle;
    String _instanceName;
    String _federateHandle;
    int _federateHandleState;
    String _federateType;
    int _federateTypeState;
    String _federateHost;
    int _federateHostState;
  }

  public final class BarrierAlreadySet extends Exception {
  }

  public final static int OWNED = 1;
  public final static int DISCOVERED = 2;      //discovered but no data available
  public final static int REFLECTED = 3;       //data available
  public final static int NOT_REFLECTED = 4;   //not owned, not subscribed

  //columns for this table
  public final static int   FEDERATE_HANDLE = 0;
  public final static int   FEDERATE_TYPE = 1;
  public final static int   FEDERATE_HOST = 2;
  public final static int COL_COUNT = 3;

  private Vector _entries = new Vector();
  private String[] _columnNames = {
    "Federate Handle",
    "Federate Type",
    "Federate Host"};

  //add new FederateEntry
  public synchronized void add(int momHandle, String instanceName)
  {
    FederateEntry newEntry = new FederateEntry();
    newEntry._MOMhandle = momHandle;
    newEntry._instanceName = instanceName;
    newEntry._federateHandleState = DISCOVERED;
    newEntry._federateTypeState = DISCOVERED;
    newEntry._federateHostState = DISCOVERED;
    _entries.addElement(newEntry);
    int index = _entries.size() - 1;
    //tell TableView to update
    fireTableRowsInserted(index, index);
    fireTableRowsUpdated(index, index);
  }

  public synchronized void remove(int momHandle) {
    //gotta go thru this rigmarole because entries shift upon deletion
    int index = -1;
    for (int i = 0; i < _entries.size(); ++i) {
      FederateEntry entry = (FederateEntry)_entries.elementAt(i);
      if (entry._MOMhandle == momHandle) {
        index = i;
        break;
      }
    }
    if (index < 0) return;
    _entries.removeElementAt(index);
    fireTableDataChanged();
  }

  public synchronized String getColumnName(int col) {
    return _columnNames[col];
  }

  public synchronized Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  public synchronized int getRowCount() {
    return _entries.size();
  }

  public synchronized int getColumnCount() {
    return COL_COUNT;
  }

  public synchronized Object getValueAt(int row, int col) {
    FederateEntry entry = (FederateEntry)(_entries.elementAt(row));
    if (col == FEDERATE_HANDLE)
      return (entry._federateHandleState == REFLECTED) ? entry._federateHandle : "";
    else if (col == FEDERATE_TYPE)
      return (entry._federateTypeState == REFLECTED) ? entry._federateType : "";
    else if (col == FEDERATE_HOST)
      return (entry._federateHostState == REFLECTED) ? entry._federateHost : "";
    else return null;
  }

  public synchronized boolean isCellEditable(int row, int col) { return false; }

  public synchronized void setFederateHandle(int momHandle, String federateHandle)
  throws ObjectNotKnown
  {
    //find entry
    int index = -1;
    FederateEntry entry = null;
    for (int i = 0; i < _entries.size(); ++i) {
      entry = (FederateEntry)(_entries.elementAt(i));
      if (entry._MOMhandle == momHandle) {
        index = i;
        break;
      }
    }
    if (index < 0) throw new ObjectNotKnown("object " + momHandle + " not known");
    entry._federateHandle = federateHandle;
    entry._federateHandleState = REFLECTED;
    fireTableCellUpdated(index, FEDERATE_HANDLE);
  }

  public synchronized void setFederateHost(int momHandle, String federateHost)
  throws ObjectNotKnown
  {
    //find entry
    int index = -1;
    FederateEntry entry = null;
    for (int i = 0; i < _entries.size(); ++i) {
      entry = (FederateEntry)(_entries.elementAt(i));
      if (entry._MOMhandle == momHandle) {
        index = i;
        break;
      }
    }
    if (index < 0) throw new ObjectNotKnown("object " + momHandle + " not known");
    entry._federateHost = federateHost;
    entry._federateHostState = REFLECTED;
    fireTableCellUpdated(index, FEDERATE_HOST);
  }

  public synchronized void setFederateType(int momHandle, String federateType)
  throws ObjectNotKnown
  {
    //find entry
    int index = -1;
    FederateEntry entry = null;
    for (int i = 0; i < _entries.size(); ++i) {
      entry = (FederateEntry)(_entries.elementAt(i));
      if (entry._MOMhandle == momHandle) {
        index = i;
        break;
      }
    }
    if (index < 0) throw new ObjectNotKnown("object " + momHandle + " not known");
    entry._federateType = federateType;
    entry._federateTypeState = REFLECTED;
    fireTableCellUpdated(index, FEDERATE_TYPE);
  }

  public synchronized void setValueAt(Object value, int row, int col) {
    // our table isn't editable from UI
  }

  public synchronized void updateFederates(
    RTIambassador rti,
    AttributeHandleSet federateAttributesSet)
  {
    try {
      int count = _entries.size();
      for (int index = 0; index < count; ++index) {
        FederateEntry entry = (FederateEntry)_entries.elementAt(index);
        if (entry._federateHandleState == DISCOVERED
          || entry._federateTypeState == DISCOVERED
          || entry._federateHostState == DISCOVERED) {
          rti.requestObjectAttributeValueUpdate(entry._MOMhandle, federateAttributesSet);
        }
      }
    }
    catch (RTIexception e) {
    }
  }
}
