package models;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class PasswordCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -951373305548156059L;
	private JPasswordField pwField = new JPasswordField();

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (column == 2) {
			if (isSelected) {
				this.pwField.setBackground(table.getSelectionBackground());
				this.pwField.setForeground(table.getSelectionForeground());
			} else {
				this.pwField.setBackground(table.getBackground());
				this.pwField.setForeground(table.getForeground());
			}
			this.pwField.setText("**********");
			this.pwField.setBorder(BorderFactory.createEmptyBorder());
			return this.pwField;
		}
		return super.getTableCellRendererComponent(table, value, isSelected,
				hasFocus, row, column);

	}
}
