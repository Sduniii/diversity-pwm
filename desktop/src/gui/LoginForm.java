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
import tools.CreateFile;
import tools.Log;
import tools.MyJFileChooser;
import tools.SHA;
import tools.SHA.TypeToGiveBack;

public class LoginForm {
	
	/**
	 * global variable
	 */
	private JXFrame frame;
	private JXLoginPane loginPane;
	private MyJFileChooser fc = new MyJFileChooser();
	private File fileFC = null;
	private JPanel ttt;
	private JPasswordField ssss;
	private JCheckBox chckbxDateipfadMerken, chckbxPasswortSpeichern;
	private final File optionFile = new File(System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "opt.ini");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
					window.getFrame().setVisible(true);
					window.getPswdField().requestFocus();
					window.getPswdField().selectAll();
				} catch (Exception e) {
					Log.write(e);
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
			Log.write(e);
		}
		JXFrame.setDefaultLookAndFeelDecorated(true);
		String pfad = "...";
		String sPa = "";
		if (getOptionFile().exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(
						getOptionFile()));
				String li = br.readLine();
				while (li != null) {
					if (li.contains("F||")) {
						pfad = li.replace("F||", "");
						setFile(new File(pfad));
					} else if (li.contains("P||")) {
						sPa = li.replace("P||", "");
						sPa = AES.decode("5CMFs4pd=~:@Iva8MH*}~S!K[B/&bK", sPa);
					}
					li = br.readLine();
				}
				br.close();
			} catch (IOException e) {
				Log.write(e);
			}
		}
		setFrame(new JXFrame());
		getFrame().setResizable(false);
		getFrame().setIconImage(Toolkit.getDefaultToolkit().getImage(
				LoginForm.class.getResource("/images/s!logo.png")));
		getFrame().setTitle("diversityPWM");
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		getFrame().setBounds(screenWidth / 2 - 220, screenHeight / 2 - 133, 440, 266);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getRootPaneExt().getContentPane().setLayout(null);
		getFrame().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					createActions();
				}
			}
		});

		setLoginPane(new JXLoginPane());
		getLoginPane().setBounds(10, 11, 414, 164);
		getLoginPane().setPassword(sPa.toCharArray());
		setPswdField((JPasswordField) ((JPanel) ((JPanel) ((JPanel) ((JPanel) loginPane
				.getComponent(1)).getComponent(0)).getComponent(1))
				.getComponent(1)).getComponent(1));
		getPswdField().addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					createActions();
				}
			}
		});
		setFilechooserPanel((JPanel) ((JPanel) ((JPanel) ((JPanel) getLoginPane().getComponent(1))
				.getComponent(0)).getComponent(1)).getComponent(1));
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"diversITy Dateien (*.dit)", "dit");
		getFilechooser().setFileFilter(filter);
		getFilechooser().setDialogTitle("Passwort Datei auswählen");
		JXButton btnFC = new JXButton();
		btnFC.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					createActions();
				}
			}
		});
		btnFC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int returnVal;
				if (getLoginPane().getBannerText().trim().equals("Anmeldung")) {
					returnVal = getFilechooser().showOpenDialog(frame);
				} else {
					returnVal = getFilechooser().showSaveDialog(frame);
				}

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					setFile(getFilechooser().getSelectedFile());
					if (!getFile().getName().endsWith(".dit")
							&& !getFilechooser().getFileFilter().accept(getFile())) {
						if (getFilechooser().getDialogType() == JFileChooser.SAVE_DIALOG) {
							setFile(new File(getFile().getAbsolutePath() + ".dit"));
						}
					}
					if (!getFile().exists()
							&& getFilechooser().getDialogType() == JFileChooser.OPEN_DIALOG) {
						setFile(null);
					} else {
						((JXButton) getFilechooserPanel().getComponent(0)).setText(getFile()
								.getAbsolutePath());
					}
				}
			}
		});
		btnFC.setText(pfad);
		((JLabel) getFilechooserPanel().getParent().getComponent(0)).setText("Datei");
		getFilechooserPanel().remove(getFilechooserPanel().getComponent(0));
		getFilechooserPanel().add(btnFC, 0);
		getFrame().getRootPaneExt().getContentPane().add(getLoginPane());

		JXButton btnOk = new JXButton();
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				createActions();
			}
		});
		btnOk.setText("OK");
		btnOk.setBounds(334, 208, 90, 23);
		getFrame().getRootPaneExt().getContentPane().add(btnOk);

		JXHyperlink hprlnkNeuerBenutzer = new JXHyperlink();
		hprlnkNeuerBenutzer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (getLoginPane().getBannerText().trim().equals("Anmeldung")) {
					getLoginPane().setBannerText("Neue Datei");
					getFilechooser().setDialogType(JFileChooser.SAVE_DIALOG);
					getFilechooser().removeChoosableFileFilter(fc.getAcceptAllFileFilter());
					getFilechooser().setDialogTitle("Datei erstellen...");
					hprlnkNeuerBenutzer.setText("Laden");
					btnOk.setText("Erstellen");
				} else {
					getLoginPane().setBannerText("Anmeldung");
					hprlnkNeuerBenutzer.setText("Neue Datei");
					getFilechooser().setDialogType(JFileChooser.OPEN_DIALOG);
					getFilechooser().addChoosableFileFilter(fc.getAcceptAllFileFilter());
					getFilechooser().setDialogTitle("Passwort Datei auswählen");
					btnOk.setText("OK");
				}
			}
		});
		hprlnkNeuerBenutzer.setToolTipText("erstelle einen neue Datei");
		hprlnkNeuerBenutzer.setFont(new Font("Tahoma", Font.PLAIN, 11));
		hprlnkNeuerBenutzer.setText("Neue Datei");
		hprlnkNeuerBenutzer.setBounds(268, 210, 76, 19);
		getFrame().getRootPaneExt().getContentPane().add(hprlnkNeuerBenutzer);

		setChckbxPasswortSpeichern(new JCheckBox("Passwort merken?"));
		getChckbxPasswortSpeichern().setBounds(10, 208, 123, 23);
		if (sPa != "")
			getChckbxPasswortSpeichern().setSelected(true);
		getFrame().getRootPaneExt().getContentPane().add(getChckbxPasswortSpeichern());

		setChckbxDateipfadMerken(new JCheckBox("Dateipfad merken?"));
		getChckbxDateipfadMerken().setSelected(true);
		getChckbxDateipfadMerken().setBounds(10, 182, 123, 23);
		getFrame().getRootPaneExt().getContentPane().add(getChckbxDateipfadMerken());
	}

	
	
	/**
	 * Create
	 */
	@SuppressWarnings("resource")
	private void createActions() {
		try {
			// System.out.println(System.getProperty("user.dir")+System.getProperty("file.separator")
			// + loginPane.getUserName() + ".ts");
			if (getLoginPane().getBannerText().trim().equals("Anmeldung")) {
				if (getFile() != null) {
					if (getFile() != null && getFile().exists()) {
						BufferedReader fr = new BufferedReader(new FileReader(
								getFile()));
						String pa = fr.readLine();
						pa = fr.readLine();
						String sss = (String) SHA.getHash(
								new String(getLoginPane().getPassword()), "Sha-512",
								TypeToGiveBack.HEXSTRING);
						sss = (String) SHA.getHash(sss, "Sha-512",
								TypeToGiveBack.HEXSTRING);
						if (new String(Base64.getUrlDecoder().decode(pa))
								.equals(sss)) {
							if (getChckbxDateipfadMerken().isSelected()
									&& !getChckbxPasswortSpeichern().isSelected()) {
								try {
									BufferedWriter bw = new BufferedWriter(
											new FileWriter(getOptionFile()));
									bw.write("F||" + getFile().getAbsolutePath());
									bw.close();
								} catch (Exception ex) {
									Log.write(ex);
								}
							} else if (getChckbxPasswortSpeichern().isSelected()
									&& !getChckbxDateipfadMerken().isSelected()) {
								try {
									BufferedWriter bw = new BufferedWriter(
											new FileWriter(getOptionFile()));
									bw.write("P||"
											+ new String(getLoginPane()
													.getPassword()));
									bw.close();
								} catch (Exception ex) {
									Log.write(ex);
								}
							} else if (getChckbxDateipfadMerken().isSelected()
									&& getChckbxPasswortSpeichern().isSelected()) {
								try {
									BufferedWriter bw = new BufferedWriter(
											new FileWriter(getOptionFile()));
									bw.write("F||" + getFile().getAbsolutePath());
									bw.newLine();
									bw.write("P||"
											+ AES.encode(
													"5CMFs4pd=~:@Iva8MH*}~S!K[B/&bK",
													new String(getLoginPane()
															.getPassword())));
									bw.close();
								} catch (Exception ex) {
									Log.write(ex);
								}
							} else {
								getOptionFile().delete();
							}
							getFrame().dispose();

							new SplashScreen(
									new String(getLoginPane().getPassword()), getFile());

						} else {
							JOptionPane.showMessageDialog(getFrame(),
									"falsches Passwort!", "Fehler",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(getFrame(),
								"Datei existiert nicht!", "Fehler",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(getFrame(),
							"keine Datei ausgewählt!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				if (getFile() == null){
					JOptionPane.showMessageDialog(getFrame(),
							"Datei existiert nicht!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}else if(getFile().exists()) {
					JOptionPane.showMessageDialog(getFrame(),
							"Datei existiert bereits!", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (getFile() != null) {
						if (getLoginPane().getPassword().length >= 6) {

							String sss = (String) SHA.getHash(new String(
									getLoginPane().getPassword()), "Sha-512",
									TypeToGiveBack.HEXSTRING);
							sss = (String) SHA.getHash(sss, "Sha-512",
									TypeToGiveBack.HEXSTRING);
							CreateFile.startCreate(getFile().getAbsolutePath()
									+ "tmp", getFile().getAbsolutePath(), sss);
							JOptionPane.showMessageDialog(getFrame(),
									"Datei erstellt!", "OK",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(getFrame(),
									"Passwort zu klein!", "Fehler",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						if (getFilechooser().showSaveDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
							setFile(getFilechooser().getSelectedFile());
							((JXButton) getFilechooserPanel().getComponent(0)).setText(getFile()
									.getAbsolutePath());
							if (getLoginPane().getPassword().length >= 6) {

								String sss = (String) SHA.getHash(new String(
										getLoginPane().getPassword()), "Sha-512",
										TypeToGiveBack.HEXSTRING);
								sss = (String) SHA.getHash(sss, "Sha-512",
										TypeToGiveBack.HEXSTRING);
								CreateFile.startCreate(getFile().getAbsolutePath()
										+ "tmp", getFile().getAbsolutePath(), sss);
								JOptionPane.showMessageDialog(getFrame(),
										"Datei erstellt!", "OK",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(getFrame(),
										"Passwort zu klein!", "Fehler",
										JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Log.write(e);
		}
	}
	
	
	
	
	/**
	 * 
	 * 
	 *	/~_ _ _|__|_ _  _   _  _  _|  (~ _ _|__|_ _  _
	 *	\_/(/_ |  | (/_|   (_|| |(_|  _)(/_ |  | (/_| 
	 * 
	 * 
	 */
	
	
	
	/**
	 * @return the frame
	 */
	public JXFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame the frame to set
	 */
	public void setFrame(JXFrame frame) {
		this.frame = frame;
	}

	/**
	 * @return the loginPane
	 */
	public JXLoginPane getLoginPane() {
		return loginPane;
	}

	/**
	 * @param loginPane the loginPane to set
	 */
	public void setLoginPane(JXLoginPane loginPane) {
		this.loginPane = loginPane;
	}

	/**
	 * @return the fc
	 */
	public MyJFileChooser getFilechooser() {
		return fc;
	}

	/**
	 * @param fc the fc to set
	 */
	public void setFilechooser(MyJFileChooser fc) {
		this.fc = fc;
	}

	/**
	 * @return the fileFC
	 */
	public File getFile() {
		return fileFC;
	}

	/**
	 * @param fileFC the fileFC to set
	 */
	public void setFile(File fileFC) {
		this.fileFC = fileFC;
	}

	/**
	 * @return the ttt
	 */
	public JPanel getFilechooserPanel() {
		return ttt;
	}

	/**
	 * @param ttt the ttt to set
	 */
	public void setFilechooserPanel(JPanel ttt) {
		this.ttt = ttt;
	}

	/**
	 * @return the ssss
	 */
	public JPasswordField getPswdField() {
		return ssss;
	}

	/**
	 * @param ssss the ssss to set
	 */
	public void setPswdField(JPasswordField ssss) {
		this.ssss = ssss;
	}

	/**
	 * @return the chckbxDateipfadMerken
	 */
	public JCheckBox getChckbxDateipfadMerken() {
		return chckbxDateipfadMerken;
	}

	/**
	 * @param chckbxDateipfadMerken the chckbxDateipfadMerken to set
	 */
	public void setChckbxDateipfadMerken(JCheckBox chckbxDateipfadMerken) {
		this.chckbxDateipfadMerken = chckbxDateipfadMerken;
	}

	/**
	 * @return the chckbxPasswortSpeichern
	 */
	public JCheckBox getChckbxPasswortSpeichern() {
		return chckbxPasswortSpeichern;
	}

	/**
	 * @param chckbxPasswortSpeichern the chckbxPasswortSpeichern to set
	 */
	public void setChckbxPasswortSpeichern(JCheckBox chckbxPasswortSpeichern) {
		this.chckbxPasswortSpeichern = chckbxPasswortSpeichern;
	}

	/**
	 * @return the optionFile
	 */
	public File getOptionFile() {
		return optionFile;
	}
}
