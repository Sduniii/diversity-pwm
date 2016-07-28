package models;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class MyTableModel extends DefaultTableModel {
    /**
     *
     */
    private static final long serialVersionUID = 8407048378543690551L;

    public MyTableModel(Object[][] objects, String[] strings) {
        super(objects, strings);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void setValueAt(Object value, int row, int column) {
        if (value instanceof String) {
            if (!getValueAt(row, column).equals(value)) {
                ((Vector) dataVector.get(row)).set(column, value);
                fireTableCellUpdated(row, column);
            }
        }
    }
}
