package models;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

@SuppressWarnings("serial")
public class MyJPasswordPane extends JOptionPane {
	private JPasswordField passwordField;

	public MyJPasswordPane() {
		super();
		passwordField = new JPasswordField(20);
		JPanel panel = new JPanel();
        panel.add(passwordField);
        this.setMessage(panel);
        this.setOptionType(JOptionPane.OK_CANCEL_OPTION);
	}

	public MyJPasswordPane(Object message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public MyJPasswordPane(Object message, int messageType) {
		super(message, messageType);
		// TODO Auto-generated constructor stub
	}

	public MyJPasswordPane(JPanel message, int messageType, int optionType) {
		super(message,messageType,optionType);
	}

	public MyJPasswordPane(Object message, int messageType, int optionType,
			Icon icon) {
		super(message, messageType, optionType, icon);
		// TODO Auto-generated constructor stub
	}

	public MyJPasswordPane(Object message, int messageType, int optionType,
			Icon icon, Object[] options) {
		super(message, messageType, optionType, icon, options);
		// TODO Auto-generated constructor stub
	}

	public MyJPasswordPane(Object message, int messageType, int optionType,
			Icon icon, Object[] options, Object initialValue) {
		super(message, messageType, optionType, icon, options, initialValue);
		// TODO Auto-generated constructor stub
	}
	@Override
    public void selectInitialValue() {
        passwordField.requestFocusInWindow();
    }
	
	public String getPassword(){
		return passwordField.getPassword().length == 0 ? "" : new String(passwordField.getPassword());
	}

}
