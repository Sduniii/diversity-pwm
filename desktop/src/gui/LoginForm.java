package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
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
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLoginPane;

import tools.AES;
import tools.CreateFile;
import tools.HardwareIDs;
import tools.Log;
import tools.MyJFileChooser;
import tools.OS;
import tools.SHA;
import tools.SHA.TypeToGiveBack;

public class LoginForm extends JXFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * global variable
	 */

	private JXLoginPane loginPane;
	private MyJFileChooser fileChooser;
	private File passwordFile;
	private JPanel fileChooserPanel;
	private JPasswordField txtFieldpassword;
	private JCheckBox chckbxDateipfadMerken, chckbxPasswortSpeichern;
	private File optionFile;
	private JXHyperlink hprlnkNeuerBenutzer;
	private JXButton btnOk, fileChooserButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				LoginForm window = new LoginForm(null);
				window.setVisible(true);
				window.getTxtFieldpassword().requestFocus();
				window.getTxtFieldpassword().selectAll();
			} catch (Exception e) {
				Log.write(e);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginForm(File file) {
		initialize(file);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(File file) {
		// Look and Feel
		OS.setLockAndFeel();

		// Frame
		initFrame();

		// LoginPane
		initLoginPane();

		// Filechooser
		initFileChooser();

		// Option File
		initOptionFile();

		// Password setzen
		initPassword(file);

		// Ok Button
		initOkButton();

		// neuen Benutzer erstellen Link
		initNewUserLink();

		// Cekcboxen
		initCheckboxes();
	}

	/**
	 * Create
	 */
	@SuppressWarnings("resource")
	private void createActions() {
		try {
			// System.out.println(System.getProperty("user.dir")+System.getProperty("file.separator")
			// + loginPane.getUserName() + ".dit");
			if (loginPane.getBannerText().trim().equals("Anmeldung")) {
				if (passwordFile != null) {
					if (passwordFile.exists()) {
						BufferedReader fr = new BufferedReader(new FileReader(passwordFile));
						fr.readLine();
						String pa = fr.readLine();
						String sss = (String) SHA.getHash(new String(loginPane.getPassword()), "Sha-512",
								TypeToGiveBack.HEXSTRING);
						sss = (String) SHA.getHash(sss, "Sha-512", TypeToGiveBack.HEXSTRING);
						if (new String(Base64.getUrlDecoder().decode(pa)).equals(sss)) {
							boolean delete = true;
							BufferedWriter bw = new BufferedWriter(new FileWriter(optionFile));
							if (chckbxDateipfadMerken.isSelected()) {
								bw.write("F||" + passwordFile.getAbsolutePath());
								bw.newLine();
								delete &= false;
							}
							if (chckbxPasswortSpeichern.isSelected()) {
								bw.write("P||"
										+ AES.encode(HardwareIDs.getSerial(), new String(loginPane.getPassword())));
								delete &= false;
							}
							bw.close();

							if (delete)
								optionFile.delete();

							dispose();
							System.gc();
							new SplashScreen(new String(loginPane.getPassword()), passwordFile);
							System.gc();

						} else {
							JOptionPane.showMessageDialog(this, "falsches Passwort!", "Fehler",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(this, "Datei existiert nicht!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(this, "keine Datei ausgew\u00E4hlt!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				if (passwordFile == null) {
					JOptionPane.showMessageDialog(this, "Datei existiert nicht!", "Fehler", JOptionPane.ERROR_MESSAGE);
				} else if (passwordFile.exists()) {
					JOptionPane.showMessageDialog(this, "Datei existiert bereits!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (passwordFile != null) {
						fileCreated();
					} else {
						if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
							passwordFile = fileChooser.getSelectedFile();
							((JXButton) fileChooserPanel.getComponent(0)).setText(passwordFile.getAbsolutePath());
							fileCreated();
						}
					}
				}
			}
		} catch (Exception e) {
			Log.write(e);
		}
	}

	private void fileCreated() {
		try {
			if (loginPane.getPassword().length >= 6) {

				String sss = (String) SHA.getHash(new String(loginPane.getPassword()), "Sha-512",
						TypeToGiveBack.HEXSTRING);
				sss = (String) SHA.getHash(sss, "Sha-512", TypeToGiveBack.HEXSTRING);
				CreateFile.startCreate(passwordFile.getAbsolutePath() + "tmp", passwordFile.getAbsolutePath(), sss);
				JOptionPane.showMessageDialog(this, "Datei erstellt!", "OK", JOptionPane.INFORMATION_MESSAGE);
				swapToLogin();
			} else {
				JOptionPane.showMessageDialog(this, "Passwort zu klein!", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void swapToLogin() {
		loginPane.setBannerText("Anmeldung");
		hprlnkNeuerBenutzer.setText("Neue Datei");
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.addChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
		fileChooser.setDialogTitle("Passwort Datei ausw\u00E4hlen");
		btnOk.setText("OK");
		hprlnkNeuerBenutzer.setUnclickedColor(Color.GREEN);
		hprlnkNeuerBenutzer.setClickedColor(Color.GREEN);
	}

	// INITS
	private void initFrame() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(LoginForm.class.getResource("/images/s!logo.png")));
		setTitle("diversityPWM");
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		setBounds(screenWidth / 2 - 220, screenHeight / 2 - 133, 440, 266);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getRootPaneExt().getContentPane().setLayout(null);
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					createActions();
				}
			}
		});
		JXFrame.setDefaultLookAndFeelDecorated(true);
	}

	private void initLoginPane() {
		loginPane = new JXLoginPane();
		loginPane.setBounds(10, 11, 414, 164);
		getRootPaneExt().getContentPane().add(loginPane);
	}

	private void initFileChooser() {
		fileChooser = new MyJFileChooser(System.getProperty("user.dir"));
		fileChooserPanel = (JPanel) ((JPanel) ((JPanel) ((JPanel) loginPane.getComponent(1)).getComponent(0))
				.getComponent(1)).getComponent(1);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("diversITy Dateien (*.dit)", "dit");
		fileChooser.setFileFilter(filter);
		fileChooser.setDialogTitle("Passwort Datei ausw\u00E4hlen");

		fileChooserButton = new JXButton();
		fileChooserButton.setDropTarget(new MyDropTarget(passwordFile, fileChooserPanel, this));
		fileChooserButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					createActions();
				}
			}
		});

		fileChooserButton.addActionListener(e -> fileChooserAction());
		((JLabel) fileChooserPanel.getParent().getComponent(0)).setText("Datei");
		fileChooserPanel.remove(fileChooserPanel.getComponent(0));
		fileChooserPanel.add(fileChooserButton, 0);
	}

	private void initOptionFile() {
		try {
			String oS = System.getProperty("os.name").toLowerCase();
			if (oS.contains("mac") || oS.contains("linux")) {
				optionFile = new File(
						System.getProperty("user.home") + "/Library/Application Support/de.diversity.pwm/opt.ini");
			} else {
				optionFile = new File(
						System.getProperty("user.dir") + System.getProperty("file.separator") + "opt.ini");
			}
			String canonicalPath = optionFile.getCanonicalPath();
			String path = canonicalPath.substring(0, canonicalPath.lastIndexOf(File.separator));
			if (!new File(path).exists()) {
				new File(path).mkdirs();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void initPassword(File file) {
		HashMap<String, Object> map = GuiTools.readOptionFile(optionFile);
		if (map.get("password") == null)
			loginPane.setPassword("".toCharArray());
		else {
			loginPane.setPassword(((String) map.get("password")).toCharArray());
		}
		if (map.get("file") != null) {
			// Wenn Passwortfile Übergeben, setze dieses File
			if (file == null)
				passwordFile = ((File) map.get("file"));
			else
				passwordFile = file;
			fileChooserButton.setText(passwordFile.getAbsolutePath());
		}

		txtFieldpassword = (JPasswordField) ((JPanel) ((JPanel) ((JPanel) ((JPanel) loginPane.getComponent(1))
				.getComponent(0)).getComponent(1)).getComponent(1)).getComponent(1);
		txtFieldpassword.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					createActions();
				}
			}
		});
	}

	private void initOkButton() {
		btnOk = new JXButton();
		btnOk.addActionListener(e -> createActions());
		btnOk.setText("OK");
		btnOk.setBounds(334, 208, 90, 23);

		getRootPaneExt().getContentPane().add(btnOk);
	}

	private void initNewUserLink() {
		hprlnkNeuerBenutzer = new JXHyperlink();
		hprlnkNeuerBenutzer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (loginPane.getBannerText().trim().equals("Anmeldung")) {
					loginPane.setBannerText("Neue Datei");
					fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
					fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
					fileChooser.setDialogTitle("Datei erstellen...");
					hprlnkNeuerBenutzer.setText("Abbrechen");
					hprlnkNeuerBenutzer.setUnclickedColor(Color.RED);
					hprlnkNeuerBenutzer.setClickedColor(Color.RED);
					btnOk.setText("Erstellen");
				} else {
					swapToLogin();
				}
			}
		});
		hprlnkNeuerBenutzer.setToolTipText("erstelle einen neue Datei");
		hprlnkNeuerBenutzer.setFont(new Font("Tahoma", Font.PLAIN, 11));
		hprlnkNeuerBenutzer.setText("Neue Datei");
		hprlnkNeuerBenutzer.setBounds(268, 210, 76, 19);
		hprlnkNeuerBenutzer.setUnclickedColor(Color.GREEN);
		hprlnkNeuerBenutzer.setClickedColor(Color.GREEN);

		getRootPaneExt().getContentPane().add(hprlnkNeuerBenutzer);
	}

	private void initCheckboxes() {
		chckbxPasswortSpeichern = new JCheckBox("Passwort merken?");
		chckbxPasswortSpeichern.setBounds(10, 208, 123, 23);
		if (loginPane.getPassword() != null && !loginPane.getPassword().equals(""))
			chckbxPasswortSpeichern.setSelected(true);
		getRootPaneExt().getContentPane().add(chckbxPasswortSpeichern);

		chckbxDateipfadMerken = new JCheckBox("Dateipfad merken?");
		chckbxDateipfadMerken.setSelected(true);
		chckbxDateipfadMerken.setBounds(10, 182, 123, 23);

		getRootPaneExt().getContentPane().add(chckbxDateipfadMerken);
	}

	private void fileChooserAction() {
		int returnVal;
		if (loginPane.getBannerText().trim().equals("Anmeldung")) {
			returnVal = fileChooser.showOpenDialog(this);
		} else {
			returnVal = fileChooser.showSaveDialog(this);
		}
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			passwordFile = fileChooser.getSelectedFile();
			if (passwordFile != null && !passwordFile.getName().endsWith(".dit")
					&& !fileChooser.getFileFilter().accept(passwordFile)) {
				if (fileChooser.getDialogType() == JFileChooser.SAVE_DIALOG) {
					passwordFile = new File(passwordFile.getAbsolutePath() + ".dit");
				}
			}

			if (passwordFile.exists() || fileChooser.getDialogType() != JFileChooser.OPEN_DIALOG) {
				fileChooserButton.setText(passwordFile.getAbsolutePath());
			}
		}

		if (passwordFile == null) {
			fileChooser.setCurrentDirectory(optionFile);
		} else {
			fileChooser.setCurrentDirectory(passwordFile);
		}
	}

	public JPasswordField getTxtFieldpassword() {
		return txtFieldpassword;
	}

	public JXButton getBtnOk() {
		return btnOk;
	}
}
