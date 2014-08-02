package gui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXTable;

import tools.FileOpener;
import tools.HTMLParser;
import exceptions.ImportFileNotFoundException;

public class PWForm {

	private JXFrame frmPwm;
	private DefaultTableModel model;
	private String pass;
	private File file;
	private JXButton btnSpeichern;
	private JXTable table;

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
		frmPwm.setIconImage(Toolkit.getDefaultToolkit().getImage(
				LoginForm.class.getResource("/images/s!logo.png")));
		frmPwm.setTitle("PWM");
		frmPwm.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {

			}
		});
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
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		});
		frmPwm.setBounds(100, 100, 450, 328);
		frmPwm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmPwm.setJMenuBar(menuBar);

		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		JMenuItem mntmbeenden = new JMenuItem("Beenden");
		mntmbeenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmPwm.dispose();
				System.exit(0);
			}
		});

		JMenu mnImport = new JMenu("Import");
		mnDatei.add(mnImport);

		JMenuItem mntmHtml = new JMenuItem("HTML");
		mntmHtml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String[] s = { "htm", "html" };
					String file = FileOpener.openFile(s, frmPwm);
					if (file != null) {
						LinkedList<String> list = HTMLParser.parse(file);
						Iterator<String> it = list.iterator();
						while (it.hasNext()) {
							model.addRow(new Object[] { it.next(), it.next(),
									it.next() });
						}
						btnSpeichern.setEnabled(true);
					}
				} catch (ImportFileNotFoundException ex) {
					System.out.println(ex.getMessage());
				}
			}
		});
		mnImport.add(mntmHtml);
		mnDatei.add(mntmbeenden);

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
		btnSpeichern.setBounds(10, 239, 79, 23);
		panel.add(btnSpeichern);
		btnSpeichern.setText("Speichern");
		btnSpeichern.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 414, 217);
		panel.add(scrollPane);
		table = new JXTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		model = new DefaultTableModel(new Object[][] {}, new String[] {
				"Location", "User", "Password" });
		table.setModel(model);
		scrollPane.setViewportView(table);

		JXButton btnNeu = new JXButton();
		btnNeu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				model.addRow(new Object[] { "", "", "" });
			}
		});
		btnNeu.setText("Neu");
		btnNeu.setBounds(99, 239, 51, 23);
		panel.add(btnNeu);

		JXButton btnLschen = new JXButton();
		btnLschen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow() != -1)
					if (JOptionPane.showConfirmDialog(frmPwm, "Löschen",
							"Passwort löschen?", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION)
						model.removeRow(table.getSelectedRow());
			}
		});
		btnLschen.setText("L\u00F6schen");
		btnLschen.setBounds(160, 239, 71, 23);
		panel.add(btnLschen);
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
}
