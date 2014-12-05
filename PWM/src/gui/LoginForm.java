package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLoginPane;

import tools.SHA;
import tools.SHA.TypeToGiveBack;

public class LoginForm {
	private JXFrame frame;
	JXLoginPane loginPane;
	private JFileChooser fc = new JFileChooser();
	File fileFC = null;

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
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JXFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JXFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(LoginForm.class.getResource("/images/s!logo.png")));
		frame.setTitle("Anmelden");
		frame.setBounds(100, 100, 450, 255);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getRootPaneExt().getContentPane().setLayout(null);

		loginPane = new JXLoginPane();
		loginPane.setBounds(10, 11, 414, 164);
		JPanel ttt = ((JPanel)((JPanel)((JPanel)((JPanel)loginPane.getComponent(1)).getComponent(0)).getComponent(1)).getComponent(1));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "*.ts", "ts");
		fc.setFileFilter(filter);
		fc.setDialogTitle("Passwort Datei ausw�hlen");
		JXButton btnFC = new JXButton();
		btnFC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			        int returnVal = fc.showOpenDialog(frame);

			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            fileFC = fc.getSelectedFile();
			            ((JXButton)ttt.getComponent(0)).setText(fileFC.getAbsolutePath());
			            //This is where a real application would open the file.
			        } 
			}
		});
		btnFC.setText("...");
		((JLabel)ttt.getParent().getComponent(0)).setText("Datei");;
		ttt.remove(ttt.getComponent(0));
		ttt.add(btnFC,0);
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
				if (loginPane.getBannerText().trim().equals("Anmeldung")) {
					loginPane.setBannerText("Neuer Benutzer");
					hprlnkNeuerBenutzer.setText("Login");
				} else {
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
		try {
			//System.out.println(System.getProperty("user.dir")+System.getProperty("file.separator") + loginPane.getUserName() + ".ts");
			if(fileFC != null){
			if (loginPane.getBannerText().trim().equals("Anmeldung")) {
				if (fileFC.exists()) {
					@SuppressWarnings("resource")
					BufferedReader fr = new BufferedReader(new FileReader(fileFC));
					String pa = fr.readLine();
					String sss = (String)SHA.getHash(new String(loginPane.getPassword()), "Sha-512", TypeToGiveBack.HEXSTRING);
					sss = (String)SHA.getHash(sss, "Sha-512", TypeToGiveBack.HEXSTRING);
					if(new String(Base64.getUrlDecoder().decode(pa)).equals(sss)){
						frame.dispose();
						new SplashScreen(new String(loginPane.getPassword()),fileFC);
					}else{
						JOptionPane.showMessageDialog(frame,
								"falsches Passwort!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frame,
							"Benutzer existiert nicht!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				if (fileFC.exists()) {
					JOptionPane.showMessageDialog(frame,
							"Benutzer existiert bereits!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (loginPane.getUserName().length() > 0 && loginPane.getPassword().length > 4) {

						fileFC.createNewFile();
						BufferedWriter fw = new BufferedWriter(new FileWriter(fileFC));
						String sss = (String)SHA.getHash(new String(loginPane.getPassword()), "Sha-512", TypeToGiveBack.HEXSTRING);
						sss = (String)SHA.getHash(sss, "Sha-512", TypeToGiveBack.HEXSTRING);
						fw.write(new String(Base64.getUrlEncoder().encodeToString(sss.getBytes())));
						fw.newLine();
						fw.close();
						JOptionPane.showMessageDialog(frame,
								"Benutzer erstellt!", "OK",
								JOptionPane.INFORMATION_MESSAGE);
					}else{
						JOptionPane.showMessageDialog(frame,
								"Benutzername oder Passwort zu klein!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
					}

				}
			}
			}else{
				JOptionPane.showMessageDialog(null, "Datei existiert nicht!", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException e) {
			
		}
	}

}
