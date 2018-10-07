//holds instance attribute state for the federate
//used as the 'model' for the UI view (JTable)

package pipe.hla.book.restaurant.production;
import hla.rti.*;
import pipe.hla.book.restaurant.*;

import javax.swing.table.*;
import java.util.*;

/**所有方法都是用了sychronized，以确保同时只能有一个方法调用一个实例，
从而保证了内部数据的一致性*/
public final class ChefTable extends AbstractTableModel
{

  //columns for this table
  public final static int POSITION = 0;
  public final static int STATE = 1;
  public final static int SERVING = 2;
  public final static int BOAT = 3;
  public final static int COL_COUNT = 4;

  private Vector _entries = new Vector();
  private String[] _columnNames = {
    "Position",
    "State",
    "Serving",
    "Boat to Xfer to"};


  public synchronized double getBoatHandle(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    return entry._boat;
  }

  public synchronized int getHandle(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    return entry._handle;
  }

  public synchronized double getPosition(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    return entry._position._angle;
  }

  public synchronized Position getFullPosition(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
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
    Chef entry = (Chef)_entries.elementAt(serial);
    return entry._state;
  }

  public synchronized int getServing(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    return entry._serving;
  }

  public synchronized String getServingName(int serial)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    return entry._servingName;
  }

  public synchronized boolean isInReach(
    int serial,
    double angle,
    double reach)
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    double chefAngle = entry._position._angle;
    double difference = chefAngle - angle;
    if (difference < 0.0) difference = -difference;
    if (difference > 180.0) difference = 360.0 -difference;
    return difference <= reach;
  }

  //add new Chef
  public synchronized void add(
    int handle,
    String name,
    double position,
    int state,
    String servingName,
    int serving)
  {
    Chef newEntry = new Chef();
    newEntry._handle = handle;
    newEntry._name = name;
    newEntry._position = new Position(position, Position.INBOARD_CANAL);
    newEntry._positionState = AttributeState.OWNED_INCONSISTENT;
    newEntry._state = state;
    newEntry._stateState = AttributeState.OWNED_INCONSISTENT;
    newEntry._servingName = servingName;
    newEntry._servingNameState = AttributeState.OWNED_INCONSISTENT;
    newEntry._serving = serving;
    newEntry._boat = -1;
    newEntry._privilegeToDeleteObjectState = AttributeState.OWNED_INCONSISTENT;
    _entries.addElement(newEntry);
    int index = _entries.size() - 1;
    //tell TableView to update
    fireTableRowsInserted(index, index);
    fireTableRowsUpdated(index, index);
  }

  //return the chef serial that made a given serving
  //-1 if not found
  public synchronized int getChefForServing(int servingHandle) {
    int size = _entries.size();
    for (int i = 0; i < size; ++i) {
      Chef entry = (Chef)_entries.elementAt(i);
      if (entry._serving == servingHandle) return i;
    }
    return -1;
  }

  public synchronized void setState(int serial, int state)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    entry._state = state;
    entry._stateState = AttributeState.OWNED_INCONSISTENT;
    fireTableRowsUpdated(serial, serial);
  }

  public synchronized void setBoatHandle(int serial, int boat)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    entry._boat = boat;
    fireTableRowsUpdated(serial, serial);
  }

  public synchronized void setServing(int serial, int serving)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    entry._serving = serving;
  }

  public synchronized void setServingName(int serial, String servingName)
  throws ArrayIndexOutOfBoundsException
  {
    Chef entry = (Chef)_entries.elementAt(serial);
    entry._servingName = servingName;
    entry._servingNameState = AttributeState.OWNED_INCONSISTENT;
    fireTableRowsUpdated(serial, serial);
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
    Chef entry = (Chef)(_entries.elementAt(row));
    if (col == POSITION) return new Double(entry._position._angle);
    else if (col == STATE) return Chef.stateStrings[entry._state];
    else if (col == SERVING) {
      if (entry._state == Chef.MAKING_SUSHI) return "";
      else return Integer.toString(entry._serving);
    }
    else if (col == BOAT) {
      if (entry._state == Chef.WAITING_TO_TRANSFER) return Integer.toString(entry._boat);
      else return "";
    }
    else return null;
  }

  public synchronized boolean isCellEditable(int row, int col) { return false; }

  public synchronized void setValueAt(Object value, int row, int col) {
    // our table isn't editable from UI
  }

  //used in response to provideAttributeValueUpdate
  public synchronized void markForUpdate(int chefHandle)
  throws ObjectNotKnown
  {
    int length = _entries.size();
    int serial = -1;
    Chef entry = null;
    for (int i = 0; i < length; ++i) {
      entry = (Chef)_entries.elementAt(i);
      if (entry != null && entry._handle == chefHandle) {
        serial = i;
        break;
      }
    }
    if (serial < 0) throw new ObjectNotKnown("Chef handle " + chefHandle
      + " not found.");
    entry._positionState = AttributeState.OWNED_INCONSISTENT;
    entry._servingNameState = AttributeState.OWNED_INCONSISTENT;
    entry._stateState = AttributeState.OWNED_INCONSISTENT;
  }

  public synchronized void updateChefs(
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
      Chef chef = (Chef)_entries.elementAt(serial);
      boolean needToUpdate = false;
      SuppliedAttributes sa = suppliedAttributesFactory.create(3);
      if (chef._positionState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(positionAttribute, chef._position.encode());
        needToUpdate = true;
        chef._positionState = AttributeState.OWNED_CONSISTENT;
      }
      if (chef._servingNameState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(servingNameAttribute, InstanceName.encode(chef._servingName));
        needToUpdate = true;
        chef._servingNameState = AttributeState.OWNED_CONSISTENT;
      }
      if (chef._stateState == AttributeState.OWNED_INCONSISTENT) {
        sa.add(stateAttribute, IntegerAttribute.encode(chef._state));
        needToUpdate = true;
        chef._stateState = AttributeState.OWNED_CONSISTENT;
      }
      if (needToUpdate) {
        EventRetractionHandle erh =
          rti.updateAttributeValues(chef._handle, sa, null, sendTime);
      }
    }
  }
}
