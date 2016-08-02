package models;

import com.sun.java.swing.plaf.windows.WindowsBorders;
import tools.Colors;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class PasswordCellRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -951373305548156059L;
    private JPasswordField pwField = new JPasswordField();

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (column == 2) {
            if (isSelected) {
                this.pwField.setBackground(Colors.TABLESELECTION);
                this.pwField.setForeground(table.getSelectionForeground());
                this.pwField.setBorder(BorderFactory.createLineBorder(Colors.TABLESELECTION, 1));
                if(table.getSelectedColumn() == 2) {
                    this.pwField.setBackground(Colors.CELLSELECTION);
                    this.pwField.setForeground(table.getSelectionForeground());
                    this.pwField.setBorder(new WindowsBorders.DashedBorder(Colors.CELLSELECTIONBORDER, 1));
                } else {
                    this.pwField.setBorder(new WindowsBorders.DashedBorder(Colors.TABLESELECTION, 1));
                }
            } else {
                this.pwField.setBackground(table.getBackground());
                this.pwField.setForeground(table.getForeground());
                this.pwField.setBorder(new WindowsBorders.DashedBorder(table.getBackground(), 1));
            }

            this.pwField.setText("**********");
            return this.pwField;
        }
        super.getTableCellRendererComponent(table, value, isSelected,
                hasFocus, row, column);
        if (isSelected) {
            if(hasFocus) {
                this.setBackground(Colors.CELLSELECTION);
                this.setBorder(new WindowsBorders.DashedBorder(Colors.CELLSELECTIONBORDER, 1));
            }else{
                this.setBackground(table.getSelectionBackground());
                this.setBorder(new WindowsBorders.DashedBorder(table.getSelectionBackground(), 1));
            }
        } else {
            this.setBackground(table.getBackground());
            this.setBorder(new WindowsBorders.DashedBorder(table.getBackground(), 1));
        }
        return this;

    }
}
