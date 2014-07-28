package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXTable;

import tools.HTMLParser;
import components.JStatusBar;

public class PWForm {

	private JXFrame frmPwm;
	private DefaultTableModel model;
	private JStatusBar statusBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PWForm window = new PWForm();
					window.frmPwm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PWForm() {
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
			e.printStackTrace();
		}
		JXFrame.setDefaultLookAndFeelDecorated(true);
		frmPwm = new JXFrame();
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
		frmPwm.setBounds(100, 100, 450, 351);
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
				LinkedList<String> list = HTMLParser.parse("C:/Users/Tbaios/SkyDrive/Documents/ssss.html");
				Iterator<String> it = list.iterator();
				while(it.hasNext()){
					model.addRow(new Object[]{it.next(),it.next(),it.next()});
				}
			}
		});
		mnImport.add(mntmHtml);
		mnDatei.add(mntmbeenden);
		
		frmPwm.getRootPaneExt().getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		frmPwm.getRootPaneExt().getContentPane().add(panel, BorderLayout.CENTER);
		
		JXButton btnSpeichern = new JXButton();
		btnSpeichern.setBounds(10, 239, 79, 23);
		panel.add(btnSpeichern);
		btnSpeichern.setText("Speichern");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 414, 217);
		panel.add(scrollPane);
		JXTable table = new JXTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		model = new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Location", "User", "Password"
				}
			);
		table.setModel(model);
		scrollPane.setViewportView(table);
		
		JXButton btnNeu = new JXButton();
		btnNeu.setText("Neu");
		btnNeu.setBounds(99, 239, 51, 23);
		panel.add(btnNeu);
		
		statusBar = new JStatusBar();
		statusBar.setLeftComponent(new JXLabel("Ready"));
		frmPwm.getRootPaneExt().getContentPane().add(statusBar, BorderLayout.SOUTH);
	}
}
