//holds instance attribute state for the federate
//used as the 'model' for the UI view (JTable)

package pipe.hla.book.restaurant.consumption;
import hla.rti.*;
import pipe.hla.book.restaurant.*;

import javax.swing.table.*;
import java.util.*;

public final class DinerTable extends AbstractTableModel
{

  //columns for this table
  public final static int POSITION = 0;
  public final static int STATE = 1;
  public final static int SERVING = 2;
  public final static int BOAT = 3;
  public final static int COL_COUNT = 4;

  //entry: Diner
  private Vector _entries = new Vector();
  private String[] _columnNames = {
    "Position",
    "State",
    "Serving",
    "Boat to Xfer from"};

  public synchronized double getBoatHandle(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    return entry._boat;
  }

  public synchronized int getHandle(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    return entry._handle;
  }

  public synchronized String getName(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    return entry._name;
  }

  public synchronized double getPosition(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    return entry._position._angle;
  }

  //return the diner serial that is looking for a given serving
  //-1 if not found
  public synchronized int getDinerForServing(int servingHandle) {
    int size = _entries.size();
    for (int i = 0; i < size; ++i) {
      Diner entry = (Diner)_entries.elementAt(i);
      if (entry._serving == servingHandle) return i;
    }
    return -1;
  }

  public synchronized Position getFullPosition(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    Position posn = null;
    try {
      posn = (Position)(entry._position.clone());
    }
    catch (CloneNotSupportedException e) { }
    return posn;
  }

  public synchronized int getState(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    return entry._state;
  }

  public synchronized int getServing(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    return entry._serving;
  }

  public synchronized String getServingName(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    return entry._servingName;
  }

  public synchronized boolean isInReach(
    int serial,
    double angle,
    double reach)
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    double DinerAngle = entry._position._angle;
    double difference = DinerAngle - angle;
    if (difference < 0.0) difference = -difference;
    if (difference > 180.0) difference = 360.0 -difference;
    return difference <= reach;
  }

  //are any diners in a state where a time advance is required?
  public synchronized boolean isTimeAdvanceRequired() {
    int count = _entries.size();
    for (int i = 0; i < count; ++i) {
      Diner diner = (Diner)_entries.elementAt(i);
      if (diner._state == Diner.EATING || diner._state == Diner.LOOKING_FOR_FOOD)
        return true;
    }
    return false;
  }

  //add new Diner
  public synchronized void add(
    int handle,
    String name,
    double position,
    int state,
    String servingName,
    int serving)
  {
    Diner newEntry = new Diner();
    newEntry._handle = handle;
    newEntry._name = name;
    newEntry._position = new Position(position, Position.OUTBOARD_CANAL);
    newEntry._positionState = AttributeState.OWNED_INCONSISTENT;
    newEntry._state = state;
    newEntry._stateState = AttributeState.OWNED_INCONSISTENT;
    newEntry._servingName = servingName;
    newEntry._servingNameState = AttributeState.OWNED_INCONSISTENT;
    newEntry._serving = serving;
    newEntry._privilegeToDeleteObjectState = AttributeState.OWNED_INCONSISTENT;
    _entries.addElement(newEntry);
    int index = _entries.size() - 1;
    //tell TableView to update
    fireTableRowsInserted(index, index);
    fireTableRowsUpdated(index, index);
  }

  //used in response to provideAttributeValueUpdate
  public synchronized void markForUpdate(int dinerHandle)
  throws ObjectNotKnown
  {
    int length = _entries.size();
    int serial = -1;
    Diner entry = null;
    for (int i = 0; i < length; ++i) {
      entry = (Diner)_entries.elementAt(i);
      if (entry != null && entry._handle == dinerHandle) {
        serial = i;
        break;
      }
    }
    if (serial < 0) throw new ObjectNotKnown("Diner handle " + dinerHandle
      + " not found.");
    entry._positionState = AttributeState.OWNED_INCONSISTENT;
    entry._servingNameState = AttributeState.OWNED_INCONSISTENT;
    entry._stateState = AttributeState.OWNED_INCONSISTENT;
  }

  public synchronized void setPositionConsistent(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    entry._positionState = AttributeState.OWNED_CONSISTENT;
  }

  public synchronized void setState(int serial, int state)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    entry._state = state;
    //state becomes inconsistent only on transition visible outside federate
    if (state == Diner.EATING || state == Diner.LOOKING_FOR_FOOD)
      entry._stateState = AttributeState.OWNED_INCONSISTENT;
    fireTableRowsUpdated(serial, serial);
  }

  public synchronized void setStateConsistent(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    entry._stateState = AttributeState.OWNED_CONSISTENT;
  }

  public synchronized void setBoatHandle(int serial, int boat)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    entry._boat = boat;
    fireTableRowsUpdated(serial, serial);
  }

  public synchronized void setServing(int serial, int serving)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    entry._serving = serving;
  }

  public synchronized void setServingName(int serial, String servingName)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    entry._servingName = servingName;
    entry._servingNameState = AttributeState.OWNED_INCONSISTENT;
    fireTableRowsUpdated(serial, serial);
  }

  public synchronized void setServingNameConsistent(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Diner entry = (Diner)_entries.elementAt(serial);
    entry._servingNameState = AttributeState.OWNED_CONSISTENT;
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
    Diner entry = (Diner)(_entries.elementAt(row));
    if (col == POSITION) return new Double(entry._position._angle);
    else if (col == STATE) return Diner.stateStrings[entry._state];
    else if (col == SERVING) {
      if (entry._state == Diner.EATING || entry._state == Diner.PREPARING_TO_DELETE_SERVING)
        return Integer.toString(entry._serving);
      else return "";
    }
    else if (col == BOAT) {
      if (entry._state == Diner.ACQUIRING) return Integer.toString(entry._boat);
      else return "";
    }
    else return null;
  }

  public synchronized boolean isCellEditable(int row, int col) { return false; }

  public synchronized void setValueAt(Object value, int row, int col) {
    // our table isn't editable from UI
  }

  public synchronized void updateDiners(
    LogicalTime sendTime,
    SuppliedAttributesFactory suppliedAttributesFactory,
    int positionAttribute,
    int servingNameAttribute,
    int stateAttribute,
    RTIambassador rti)
  throws RTIexception
  {
    int count = _entries.size();
    for (int serial = 0; serial < count; ++serial) {
      Diner diner = (Diner)_entries.elementAt(serial);
      boolean needToUpdate = false;
      SuppliedAttributes sa = suppliedAttributesFactory.create(2);
      if (diner._positionState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(positionAttribute, diner._position.encode());
        needToUpdate = true;
        diner._positionState = AttributeState.OWNED_CONSISTENT;
      }
      if (diner._servingNameState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(servingNameAttribute, InstanceName.encode(diner._servingName));
        needToUpdate = true;
        diner._servingNameState = AttributeState.OWNED_CONSISTENT;
      }
      if (diner._stateState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(stateAttribute, IntegerAttribute.encode(diner._state));
        needToUpdate = true;
        diner._stateState = AttributeState.OWNED_CONSISTENT;
      }
      if (needToUpdate) {
        EventRetractionHandle erh =
          rti.updateAttributeValues(diner._handle, sa, null, sendTime);
      }
    }
  }
}

