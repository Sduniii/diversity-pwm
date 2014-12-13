package gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;

import models.MyJPasswordPane;
import models.MyTableModel;
import models.PasswordCellRenderer;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;

import tools.HTMLParser;
import tools.MyJFileChooser;

public class PWForm {

	private JXFrame frmPwm;
	private MyTableModel model;
	private String pass;
	private File file;
	private JXButton btnSpeichern, btnNeu, btnLschen;
	private JXTable table;
	private JScrollPane scrollPane;
	private JCheckBox chckbxPasswrterAnzeigen, chckbxStopEdit;
	MouseAdapter tableMouseListener;

	// /**
	// * Create the application.
	// */
	// public PWForm() {
	// initialize();
	// frmPwm.setVisible(true);
	// }

	/**
	 * Create the application.
	 */
	public PWForm(LinkedList<String> list, String pass, File file) {
		this.file = file;
		this.pass = pass;
		// System.out.println(pass);
		initialize();
		Iterator<String> it = list.iterator();
		try {
			while (it.hasNext()) {
				model.addRow(new Object[] { it.next(), it.next(), it.next() });
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frmPwm,
					"gespeicherte Datei ist evtl. defekt!", "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
		model.addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				btnSpeichern.setEnabled(true);
			}
		});
		frmPwm.setVisible(true);
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
			e.printStackTrace();
		}
		JXFrame.setDefaultLookAndFeelDecorated(true);
		frmPwm = new JXFrame();
		frmPwm.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				scrollPane.setBounds(10, 11, e.getComponent().getWidth() - 36,
						e.getComponent().getHeight() - 111);
				scrollPane.repaint();
				btnLschen.setBounds(160, e.getComponent().getHeight() - 89, 71,
						23);
				btnLschen.repaint();
				btnNeu.setBounds(99, e.getComponent().getHeight() - 89, 51, 23);
				btnNeu.repaint();
				btnSpeichern.setBounds(10, e.getComponent().getHeight() - 89,
						79, 23);
				btnSpeichern.repaint();
				chckbxPasswrterAnzeigen.setBounds(237, e.getComponent()
						.getHeight() - 89, 131, 23);
				chckbxPasswrterAnzeigen.repaint();
				chckbxStopEdit.setBounds(370, e.getComponent()
						.getHeight() - 89, 131, 23);
				chckbxStopEdit.repaint();
			}
		});
		frmPwm.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmPwm.setIconImage(Toolkit.getDefaultToolkit().getImage(
				LoginForm.class.getResource("/images/s!logo.png")));
		frmPwm.setTitle("PWM");
		frmPwm.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				if (table.isEditing()) {
					table.getCellEditor().stopCellEditing();
				}
				if (btnSpeichern.isEnabled()) {
					if (JOptionPane.showConfirmDialog(frmPwm,
							"Änderung speichern?", "Speichern",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
						onlySave();
					} else {
						frmPwm.dispose();
						System.exit(0);
					}
				} else {
					frmPwm.dispose();
					System.exit(0);
				}

			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		int progWidth = screenWidth / 2;
		int progHeight = screenHeight / 2;
		frmPwm.setBounds(progWidth - (progWidth / 2), progHeight
				- (progHeight / 2), progWidth, progHeight);

		JMenuBar menuBar = new JMenuBar();
		frmPwm.setJMenuBar(menuBar);

		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		JMenuItem mntmbeenden = new JMenuItem("Beenden");
		mntmbeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (btnSpeichern.isEnabled()) {
					if (JOptionPane.showConfirmDialog(frmPwm,
							"Änderung speichern?", "Speichern",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
						onlySave();
					} else {
						frmPwm.dispose();
						System.exit(0);
					}
				} else {
					frmPwm.dispose();
					System.exit(0);
				}
			}
		});
		
		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter");
		mntmSpeichernUnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MyJFileChooser fc = new MyJFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"diversITy Dateien (*.dit)", "dit");
				fc.setFileFilter(filter);
				fc.setDialogTitle("Speichern unter");
				int returnVal = fc.showSaveDialog(frmPwm);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					if (!file.getName().endsWith(".dit")
							&& !fc.getFileFilter().accept(file)) {
						if (fc.getDialogType() == JFileChooser.SAVE_DIALOG) {
							file = new File(file.getAbsolutePath() + ".dit");
						}
					}
					saveButtonClicked();
				}
			}
		});
		mnDatei.add(mntmSpeichernUnter);

		JMenu mnImport = new JMenu("Import");
		mnDatei.add(mnImport);

		JMenuItem mntmHtml = new JMenuItem("HTML");
		mntmHtml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					MyJFileChooser fc = new MyJFileChooser();
					FileFilter filter = new FileNameExtensionFilter("HTML Dateien (*.html,*htm)", "htm", "html");
					fc.setFileFilter(filter);
					if (fc.showOpenDialog(frmPwm)==JFileChooser.APPROVE_OPTION) {
						LinkedList<String> list = HTMLParser.parse(fc.getSelectedFile().getAbsolutePath());
						Iterator<String> it = list.iterator();
						while (it.hasNext()) {
							model.addRow(new Object[] { it.next(), it.next(),
									it.next() });
						}
						btnSpeichern.setEnabled(true);
					}
				} catch (Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});

		mnImport.add(mntmHtml);

		JMenuItem mntmPasswortndern = new JMenuItem("Passwort \u00E4ndern");
		mntmPasswortndern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = (String) JOptionPane.showInputDialog(frmPwm,
						"neues Passwort", "Passwort ändern",
						JOptionPane.PLAIN_MESSAGE, null, null, "");
				if (s != null) {
					if (s.length() >= 6) {
						pass = s;
						JOptionPane.showMessageDialog(frmPwm,
								"Passwort erfolgreich geändert",
								"Passwort geändert",
								JOptionPane.INFORMATION_MESSAGE);
						saveButtonClicked();
					} else {
						JOptionPane.showMessageDialog(frmPwm,
								"Passwort zu klein", "Passwort Fehler",
								JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		mnDatei.add(mntmPasswortndern);
		mnDatei.add(mntmbeenden);

		JPopupMenu pMenu = new JPopupMenu();
		JMenuItem mntmCopy = new JMenuItem("Kopieren");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow() != -1
						&& table.getSelectedColumn() != -1) {
					Toolkit.getDefaultToolkit()
							.getSystemClipboard()
							.setContents(
									new StringSelection(table.getStringAt(
											table.getSelectedRow(),
											table.getSelectedColumn())), null);
				}
			}
		});
		pMenu.add(mntmCopy);

		frmPwm.getRootPaneExt().getContentPane()
				.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		panel.setLayout(null);
		frmPwm.getRootPaneExt().getContentPane()
				.add(panel, BorderLayout.CENTER);

		btnSpeichern = new JXButton();
		btnSpeichern.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveButtonClicked();
			}
		});
		btnSpeichern.setBounds(10, frmPwm.getHeight() - 89, 79, 23);
		panel.add(btnSpeichern);
		btnSpeichern.setText("Speichern");
		btnSpeichern.setEnabled(false);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, frmPwm.getHeight() - 36,
				frmPwm.getWidth() - 111);
		panel.add(scrollPane);
		table = new JXTable();
		tableMouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == 3) {
					table.changeSelection(
							table.rowAtPoint(new Point(e.getX(), e.getY())),
							table.columnAtPoint(new Point(e.getX(), e.getY())),
							false, false);
					// System.out.println(Arrays.toString(pMenu.getKeyListeners()));
					pMenu.show(table, e.getX(), e.getY());
				}
			}
		};
		table.addMouseListener(tableMouseListener);
		table.putClientProperty("terminateEditOnFocusLost", true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		model = new MyTableModel(new Object[][] {}, new String[] { "Location",
				"User", "Password" });
		table.setModel(model);
		table.setDefaultRenderer(Object.class, new PasswordCellRenderer());
		scrollPane.setViewportView(table);

		btnNeu = new JXButton();
		btnNeu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.addRow(new Object[] { "", "", "" });
				table.changeSelection(table.getRowSorter().convertRowIndexToView(model.getRowCount()-1), 0, false, false);
				table.requestFocus();
			}
		});
		btnNeu.setText("Neu");
		btnNeu.setBounds(99, frmPwm.getHeight() - 89, 51, 23);
		panel.add(btnNeu);

		btnLschen = new JXButton();
		btnLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow() != -1)
					if (JOptionPane.showConfirmDialog(frmPwm,
							"Passwort l\u00F6schen?", "Löschen",
							JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)

						model.removeRow(table.convertRowIndexToModel(table
								.getSelectedRow()));
			}
		});
		btnLschen.setText("L\u00F6schen");
		btnLschen.setBounds(160, frmPwm.getHeight() - 89, 71, 23);
		panel.add(btnLschen);

		chckbxPasswrterAnzeigen = new JCheckBox("Passw\u00F6rter anzeigen?");

		chckbxPasswrterAnzeigen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (chckbxPasswrterAnzeigen.isSelected()) {
					MyJPasswordPane pane = new MyJPasswordPane();
					pane.createDialog(frmPwm, "Passwort:").setVisible(true);
					String ss = pane.getPassword();
					if ((ss).equals(pass)) {
						table.setDefaultRenderer(Object.class,
								new DefaultTableCellRenderer());
						scrollPane.repaint();
					}else{
						chckbxPasswrterAnzeigen.setSelected(false);
					}
				} else {
					table.setDefaultRenderer(Object.class,
							new PasswordCellRenderer());
					scrollPane.repaint();
				}
			}
		});
		chckbxPasswrterAnzeigen
				.setBounds(237, frmPwm.getHeight() - 89, 131, 23);
		panel.add(chckbxPasswrterAnzeigen);
		
		chckbxStopEdit = new JCheckBox("Editieren sperren!");

		chckbxStopEdit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!chckbxStopEdit.isSelected()) {
					MyJPasswordPane pane = new MyJPasswordPane();
					pane.createDialog(frmPwm, "Passwort:").setVisible(true);
					String ss = pane.getPassword();
					if ((ss).equals(pass)) {
						table.setEditable(true);
						table.addMouseListener(tableMouseListener);
						scrollPane.repaint();
					}else{
						chckbxStopEdit.setSelected(true);
					}
				} else {
					table.setEditable(false);
					table.removeMouseListener(tableMouseListener);
					scrollPane.repaint();
				}
			}
		});
		chckbxStopEdit
				.setBounds(370, frmPwm.getHeight() - 89, 131, 23);
		panel.add(chckbxStopEdit);
	}

	protected void saveButtonClicked() {
		LinkedList<String> tr = new LinkedList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < model.getColumnCount(); j++) {
				tr.add(model.getValueAt(i, j).toString());
				// System.out.println(tr.getLast());
			}
		}
		// System.out.println(tr.size());
		frmPwm.dispose();
		new SplashScreen(pass, file, tr);
	}

	protected void onlySave() {
		LinkedList<String> tr = new LinkedList<>();
		for (int i = 0; i < model.getRowCount(); i++) {
			for (int j = 0; j < model.getColumnCount(); j++) {
				tr.add(model.getValueAt(i, j).toString());
				// System.out.println(tr.getLast());
			}
		}
		// System.out.println(tr.size());
		frmPwm.dispose();
		new SplashScreen(pass, file, tr, true);
	}
}
