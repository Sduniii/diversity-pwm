package models;

import ca.odell.glazedlists.GlazedLists;
import ca.odell.glazedlists.swing.AutoCompleteSupport;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;

@SuppressWarnings("serial")
public class MyCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JComboBox<String> field;
    private AutoCompleteSupport<Object> acs;

    public MyCellEditor() {
        field = new JComboBox<>();
        field.addFocusListener(new FocusHandler());
        field.getEditor().addActionListener(e -> fireEditingStopped());
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        if (e instanceof MouseEvent) {
            return ((MouseEvent) e).getClickCount() >= 2;
        }
        return super.isCellEditable(e);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        //JXComboBox field = (JXComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        Object[] suggestions = this.getSuggestions(table, column, value);
        if (acs == null || !acs.isInstalled()) {
            acs = AutoCompleteSupport.install(field, GlazedLists.eventListOf(suggestions));
        } else {
            acs.uninstall();
            acs = AutoCompleteSupport.install(field, GlazedLists.eventListOf(suggestions));
        }

        field.setEditable(true);
        field.setSelectedItem(value);
        return field;
    }

    private Object[] getSuggestions(JTable table, int column, Object value) {
        ArrayList<String> list = new ArrayList<>();
        if (value != null) {
            for (int i = table.getRowCount() - 1; i > -1; i--) {
                //System.out.println(table.getValueAt(i, column));
                if (!value.toString().equals(table.getValueAt(i, column)) && !list.contains(table.getValueAt(i, column)))
                    list.add(table.getValueAt(i, column).toString());
            }
        }
        return list.toArray();
    }

    @Override
    public Object getCellEditorValue() {
        if (field.getEditor().getEditorComponent() instanceof JTextField)
            return ((JTextField) field.getEditor().getEditorComponent()).getText();
        else
            return "";
    }

    public static class FocusHandler extends FocusAdapter {
        public void focusGained(FocusEvent evt) {
            JComboBox<String> cb = (JComboBox) evt.getSource();
            if (cb.isEditable()) {
                Component editor = cb.getEditor().getEditorComponent();
                if (editor != null) {
                    editor.requestFocus();
                }
            }
        }
    }

    public JTextField getEditorField() {
        if (field.getEditor() != null && field.getEditor().getEditorComponent() instanceof JTextField) {
            return (JTextField) field.getEditor().getEditorComponent();
        } else {
            return null;
        }
    }
}