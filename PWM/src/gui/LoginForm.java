package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLoginPane;

import tools.AES;
import tools.SHA;
import tools.SHA.TypeToGiveBack;

public class LoginForm {
	private JXFrame frame;
	JXLoginPane loginPane;
	private JFileChooser fc = new JFileChooser();
	File fileFC = null;
	JPanel ttt;
	JPasswordField ssss;
	JCheckBox chckbxDateipfadMerken, chckbxPasswortSpeichern;
	private final File optionFile = new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"opt.ini");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
					window.frame.setVisible(true);
					window.ssss.requestFocus();
					window.ssss.selectAll();
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
		String pfad = "...";
		String sPa = "";
		if(optionFile.exists()){
			try {
				BufferedReader br = new BufferedReader(new FileReader(
						optionFile));
				String li = br.readLine();
				while(li != null){
					if(li.contains("F||")){
						pfad = li.replace("F||", "");
						fileFC = new File(pfad);
					}else if(li.contains("P||")){
						sPa = li.replace("P||", "");
						sPa = AES.decode("d!dPzEdD#14bTS", sPa);
					}
					li = br.readLine();
				}
				br.close();
			} catch (IOException e) {
			}
		}
		frame = new JXFrame();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				LoginForm.class.getResource("/images/s!logo.png")));
		frame.setTitle("Anmelden");
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		frame.setBounds(screenWidth/2-220, screenHeight/2-133, 440, 266);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getRootPaneExt().getContentPane().setLayout(null);
		frame.addKeyListener
	      (new KeyAdapter() {
	          public void keyPressed(KeyEvent e) {
	            int key = e.getKeyCode();
	            if (key == KeyEvent.VK_ENTER) {
	               createActions();
	               }
	            }
	          }
	       );
		

		loginPane = new JXLoginPane();
		loginPane.setBounds(10, 11, 414, 164);
		loginPane.setPassword(sPa.toCharArray());
		ssss = (JPasswordField) ((JPanel) ((JPanel) ((JPanel) ((JPanel) loginPane.getComponent(1))
				.getComponent(0)).getComponent(1)).getComponent(1)).getComponent(1);
		ssss.addKeyListener
	      (new KeyAdapter() {
	          public void keyPressed(KeyEvent e) {
	            int key = e.getKeyCode();
	            if (key == KeyEvent.VK_ENTER) {
	               createActions();
	               }
	            }
	          }
	       );
		ttt = (JPanel) ((JPanel) ((JPanel) ((JPanel) loginPane.getComponent(1))
				.getComponent(0)).getComponent(1)).getComponent(1);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.ts",
				"ts");
		fc.setFileFilter(filter);
		fc.setDialogTitle("Passwort Datei auswählen");
		JXButton btnFC = new JXButton();
		btnFC.addKeyListener
	      (new KeyAdapter() {
	          public void keyPressed(KeyEvent e) {
	            int key = e.getKeyCode();
	            if (key == KeyEvent.VK_ENTER) {
	            	createActions();
	               }
	            }
	          }
	       );
		btnFC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal;
				if (loginPane.getBannerText().trim().equals("Anmeldung")) {
					returnVal = fc.showOpenDialog(frame);
				} else {
					returnVal = fc.showSaveDialog(frame);
				}

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileFC = fc.getSelectedFile();
					((JXButton) ttt.getComponent(0)).setText(fileFC
							.getAbsolutePath());
					// This is where a real application would open the file.
				}
			}
		});
		btnFC.setText(pfad);
		((JLabel) ttt.getParent().getComponent(0)).setText("Datei");
		;
		ttt.remove(ttt.getComponent(0));
		ttt.add(btnFC, 0);
		frame.getRootPaneExt().getContentPane().add(loginPane);

		JXButton btnOk = new JXButton();
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createActions();
			}
		});
		btnOk.setText("OK");
		btnOk.setBounds(334, 208, 90, 23);
		frame.getRootPaneExt().getContentPane().add(btnOk);

		JXHyperlink hprlnkNeuerBenutzer = new JXHyperlink();
		hprlnkNeuerBenutzer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (loginPane.getBannerText().trim().equals("Anmeldung")) {
					loginPane.setBannerText("Neue Datei");
					fc.setDialogType(JFileChooser.SAVE_DIALOG);
					fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
					fc.setDialogTitle("Datei erstellen...");
					hprlnkNeuerBenutzer.setText("Login");
					btnOk.setText("Erstellen");
				} else {
					loginPane.setBannerText("Anmeldung");
					hprlnkNeuerBenutzer.setText("Neue Datei");
					fc.setDialogType(JFileChooser.OPEN_DIALOG);
					fc.addChoosableFileFilter(fc.getAcceptAllFileFilter());
					fc.setDialogTitle("Passwort Datei auswählen");
					btnOk.setText("OK");
				}
			}
		});
		hprlnkNeuerBenutzer.setToolTipText("erstelle einen neue Datei");
		hprlnkNeuerBenutzer.setFont(new Font("Tahoma", Font.PLAIN, 11));
		hprlnkNeuerBenutzer.setText("Neue Datei");
		hprlnkNeuerBenutzer.setBounds(268, 210, 76, 19);
		frame.getRootPaneExt().getContentPane().add(hprlnkNeuerBenutzer);
		
		chckbxPasswortSpeichern = new JCheckBox("Passwort merken?");
		chckbxPasswortSpeichern.setBounds(10, 208, 123, 23);
		if(sPa != "") chckbxPasswortSpeichern.setSelected(true);
		frame.getRootPaneExt().getContentPane().add(chckbxPasswortSpeichern);
		
		chckbxDateipfadMerken = new JCheckBox("Dateipfad merken?");
		chckbxDateipfadMerken.setSelected(true);
		chckbxDateipfadMerken.setBounds(10, 182, 123, 23);
		frame.getRootPaneExt().getContentPane().add(chckbxDateipfadMerken);
	}

	
	
	
	
	
	
	
	
	@SuppressWarnings("resource")
	private void createActions() {
		try {
			// System.out.println(System.getProperty("user.dir")+System.getProperty("file.separator")
			// + loginPane.getUserName() + ".ts");
			if (loginPane.getBannerText().trim().equals("Anmeldung")) {
				if (fileFC != null) {
					if (fileFC != null && fileFC.exists()) {
						BufferedReader fr = new BufferedReader(new FileReader(
								fileFC));
						String pa = fr.readLine();
						String sss = (String) SHA.getHash(
								new String(loginPane.getPassword()), "Sha-512",
								TypeToGiveBack.HEXSTRING);
						sss = (String) SHA.getHash(sss, "Sha-512",
								TypeToGiveBack.HEXSTRING);
						if (new String(Base64.getUrlDecoder().decode(pa))
								.equals(sss)) {
							if(chckbxDateipfadMerken.isSelected()&& !chckbxPasswortSpeichern.isSelected()){
								try{
									BufferedWriter bw = new BufferedWriter(
											new FileWriter(optionFile));
									bw.write("F||"+fileFC.getAbsolutePath());
									bw.close();
								}catch(Exception ex){
									
								}
							}else if(chckbxPasswortSpeichern.isSelected() && !chckbxDateipfadMerken.isSelected()) {
								try{
									BufferedWriter bw = new BufferedWriter(
											new FileWriter(optionFile));
									bw.write("P||"+new String(loginPane.getPassword()));
									bw.close();
								}catch(Exception ex){
									
								}
							}else if(chckbxDateipfadMerken.isSelected() && chckbxPasswortSpeichern.isSelected()){
								try{
									BufferedWriter bw = new BufferedWriter(
											new FileWriter(optionFile));
									bw.write("F||"+fileFC.getAbsolutePath());
									bw.newLine();
									bw.write("P||"+AES.encode("d!dPzEdD#14bTS", new String(loginPane.getPassword())));
									bw.close();
								}catch(Exception ex){
									
								}
							}else{
								optionFile.delete();
							}
							frame.dispose();
							new SplashScreen(
									new String(loginPane.getPassword()), fileFC);
						} else {
							JOptionPane.showMessageDialog(frame,
									"falsches Passwort!", "Fehler",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(frame,
								"Datei existiert nicht!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(frame,
							"keine Datei ausgewählt!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				if (fileFC == null || fileFC.exists()) {
					JOptionPane.showMessageDialog(frame,
							"Datei existiert bereits!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (fileFC != null) {
						if (loginPane.getPassword().length >= 6) {

							fileFC.createNewFile();
							BufferedWriter fw = new BufferedWriter(
									new FileWriter(fileFC));
							String sss = (String) SHA.getHash(new String(
									loginPane.getPassword()), "Sha-512",
									TypeToGiveBack.HEXSTRING);
							sss = (String) SHA.getHash(sss, "Sha-512",
									TypeToGiveBack.HEXSTRING);
							fw.write(new String(Base64.getUrlEncoder()
									.encodeToString(sss.getBytes())));
							fw.newLine();
							fw.close();
							JOptionPane.showMessageDialog(frame,
									"Datei erstellt!", "OK",
									JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
							fileFC = fc.getSelectedFile();
							((JXButton) ttt.getComponent(0)).setText(fileFC
									.getAbsolutePath());
							if (loginPane.getPassword().length >= 6) {

								fileFC.createNewFile();
								BufferedWriter fw = new BufferedWriter(
										new FileWriter(fileFC));
								String sss = (String) SHA.getHash(new String(
										loginPane.getPassword()), "Sha-512",
										TypeToGiveBack.HEXSTRING);
								sss = (String) SHA.getHash(sss, "Sha-512",
										TypeToGiveBack.HEXSTRING);
								fw.write(new String(Base64.getUrlEncoder()
										.encodeToString(sss.getBytes())));
								fw.newLine();
								fw.close();
								JOptionPane.showMessageDialog(frame,
										"Datei erstellt!", "OK",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(frame,
										"Passwort zu klein!", "Fehler",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			}
		} catch (IOException e) {

		}
	}
}
