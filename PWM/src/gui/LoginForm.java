package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLoginPane;


public class LoginForm {
	private JXFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginForm() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JXFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JXFrame();
		frame.setBounds(100, 100, 450, 255);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getRootPaneExt().getContentPane().setLayout(null);
		
		JXLoginPane loginPane = new JXLoginPane();
		loginPane.setBounds(10, 11, 414, 164);
		frame.getRootPaneExt().getContentPane().add(loginPane);
		
		JXButton btnOk = new JXButton();
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createActions();
			}
		});
		btnOk.setText("OK");
		btnOk.setBounds(355, 186, 69, 23);
		frame.getRootPaneExt().getContentPane().add(btnOk);
		
		JXHyperlink hprlnkNeuerBenutzer = new JXHyperlink();
		hprlnkNeuerBenutzer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(loginPane.getBannerText().trim().equals("Anmeldung")){
					loginPane.setBannerText("Neuer Benutzer");
					hprlnkNeuerBenutzer.setText("Login");
				}else{
					loginPane.setBannerText("Anmeldung");
					hprlnkNeuerBenutzer.setText("Neuer Benutzer");
				}
			}
		});
		hprlnkNeuerBenutzer.setToolTipText("erstelle einen neuen Benutzer");
		hprlnkNeuerBenutzer.setFont(new Font("Tahoma", Font.PLAIN, 11));
		hprlnkNeuerBenutzer.setText("Neuer Benutzer");
		hprlnkNeuerBenutzer.setBounds(269, 188, 76, 19);
		frame.getRootPaneExt().getContentPane().add(hprlnkNeuerBenutzer);
	}
	private void createActions() {
		frame.dispose();
		new SplashScreen();
	}
}
