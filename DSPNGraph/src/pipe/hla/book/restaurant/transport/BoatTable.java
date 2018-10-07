//holds instance attribute state for the federate
//used as the 'model' for the UI view (JTable)

package pipe.hla.book.restaurant.transport;
import pipe.hla.book.restaurant.AttributeState;
import pipe.hla.book.restaurant.Boat;
import pipe.hla.book.restaurant.Position;

import javax.swing.table.*;
import java.util.*;

public final class BoatTable extends AbstractTableModel
{
  //columns for this table
  public final static int HANDLE = 0;
  public final static int NAME = 1;
  public final static int POSITION = 2;
  public final static int STATE = 3;
  public final static int SERVING = 4;
  public final static int COL_COUNT = 5;

  //entry: Boat
  private Vector _entries = new Vector();
  //key: handle value: serial (row in table). Both as Integers
  private Hashtable _boatsByHandle = new Hashtable();
  private String[] _columnNames = {
    "Handle",
    "Name",
    "Position",
    "State",
    "Serving"};

  public int getSerialByHandle(int handle)
  throws ArrayIndexOutOfBoundsException
  {
    Integer serial = (Integer)_boatsByHandle.get(new Integer(handle));
    if (serial == null) throw new ArrayIndexOutOfBoundsException("No Boat for handle "
      + handle);
    return serial.intValue();
  }

  public synchronized int getHandleBySerial(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(serial);
    return entry._handle;
  }

  public synchronized String getNameBySerial(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(serial);
    return entry._name;
  }

  public synchronized double getPositionBySerial(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(serial);
    return entry._position._angle;
  }

  public synchronized double getPosition(int handle)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(getSerialByHandle(handle));
    return entry._position._angle;
  }

  public synchronized int getState(int handle)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(getSerialByHandle(handle));
    return entry._modelingState;
  }

  public synchronized int getStateBySerial(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(serial);
    return entry._modelingState;
  }

  public synchronized int getServing(int handle)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(getSerialByHandle(handle));
    return entry._serving;
  }

  public synchronized int getServingBySerial(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(serial);
    return entry._serving;
  }

  //add new Boat
  public synchronized void add(
    int handle,
    String name,
    double position,
    int state,
    int serving,
    String servingName)
  {
    Boat newEntry = new Boat();
    newEntry._handle = handle;
    newEntry._name = name;
    newEntry._position = new Position(position, Position.ON_CANAL);
    newEntry._positionState = AttributeState.OWNED_INCONSISTENT;
    newEntry._modelingState = state;
    newEntry._serving = serving;
    newEntry._cargo = servingName;
    newEntry._cargoState = AttributeState.OWNED_INCONSISTENT;
    newEntry._spaceAvailable = true;
    newEntry._spaceAvailableState = AttributeState.OWNED_INCONSISTENT;
    newEntry._privilegeToDeleteObjectState = AttributeState.OWNED_INCONSISTENT;
    _entries.addElement(newEntry);
    //update serial-by-handle table
    int index = _entries.size() - 1;
    _boatsByHandle.put(new Integer(handle), new Integer(index));
    //tell TableView to update
    fireTableRowsInserted(index, index);
    fireTableRowsUpdated(index, index);
  }

  //return the Boat row that carries a given serving
  //-1 if not found
  public synchronized int getBoatForServing(int servingHandle) {
    int size = _entries.size();
    for (int i = 0; i < size; ++i) {
      Boat entry = (Boat)_entries.elementAt(i);
      if (entry._serving == servingHandle) return i;
    }
    return -1;
  }

  public synchronized void setState(int handle, int state)
  throws ArrayIndexOutOfBoundsException
  {
    int serial = getSerialByHandle(handle);
    Boat entry = (Boat)_entries.elementAt(serial);
    entry._modelingState = state;
    fireTableCellUpdated(serial, STATE);
  }

  public synchronized void setCargoBySerial(int serial, String servingName)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(serial);
    entry._cargo = servingName;
    fireTableCellUpdated(serial, SERVING);
  }

  public synchronized void setSpaceAvailableBySerial(int serial, boolean flag)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(serial);
    entry._spaceAvailable = flag;
  }

  public synchronized void setPositionBySerial(int serial, double position)
  throws ArrayIndexOutOfBoundsException
  {
    Boat entry = (Boat)_entries.elementAt(serial);
    entry._position = new Position(position, Position.ON_CANAL);
    fireTableCellUpdated(serial, POSITION);
  }

  public synchronized void setServing(int handle, int serving)
  throws ArrayIndexOutOfBoundsException
  {
    int serial = getSerialByHandle(handle);
    Boat entry = (Boat)_entries.elementAt(serial);
    entry._serving = serving;
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
    Boat entry = (Boat)(_entries.elementAt(row));
    if (col == HANDLE) return new Integer(entry._handle);
    else if (col == NAME) return entry._name;
    else if (col == POSITION) return new Double(entry._position._angle);
    else if (col == STATE) return Boat.stateStrings[entry._modelingState];
    else if (col == SERVING) return new String(entry._cargo);
    else return null;
  }

  public synchronized boolean isCellEditable(int row, int col) { return false; }

  public synchronized void setValueAt(Object value, int row, int col) {
    // our table isn't editable from UI
  }
}
