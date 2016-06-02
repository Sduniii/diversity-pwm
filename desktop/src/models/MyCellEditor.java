package models;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

@SuppressWarnings("serial")
public class MyCellEditor extends DefaultCellEditor {


	public MyCellEditor() {
		super(new JXComboBox());
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		//JXComboBox field = (JXComboBox) super.getTableCellEditorComponent(table, value, isSelected, row, column);

		List<String> suggestions = this.getSuggestions(table, column, value);
		JXComboBox field = new JXComboBox(suggestions.toArray());
		field.setEditable(true);

		if (column == 0 || column == 1) {
			AutoCompleteDecorator.decorate(field);
		}

		return field;
	}

	private List<String> getSuggestions(JTable table, int column, Object value) {
		ArrayList<String> list = new ArrayList<String>();
		for(int i = table.getRowCount()-1; i > -1; i--){
			System.out.println(table.getValueAt(i, column));
			if(!value.toString().equals(table.getValueAt(i, column)) && !list.contains(table.getValueAt(i, column)))
					list.add(table.getValueAt(i, column).toString());
		}
		return list;
	}
}