package gui;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;

import tools.AES;
import tools.SHA;
import tools.SHA.TypeToGiveBack;

public class SplashScreen implements PropertyChangeListener, WindowListener {

	private JXFrame frame;
	private JProgressBar progressBar;
	private Task task;
	private String pass;
	private File file;
	private LinkedList<String> allDecrypt;
	private JXLabel label;
	private JXLabel lblNone;
	private LinkedList<String> toSave;
	private boolean save = false;
	private boolean onlySave = false;

	class Task extends SwingWorker<Void, Void> {
		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {
			try {
				if (!save) {
					long progress = 0;
					setProgress(0);
					lblNone.setText("lese...");
					long fullProgress = file.length()-1;
					BufferedReader br = new BufferedReader(new FileReader(file));
					//System.out.println(br.readLine());
					fullProgress -= br.readLine().length();
					progress++;
					setProgress(Math.min((int) (progress * 100 / fullProgress),
							100));
					label.setText((progress * 100 / fullProgress) + "%");
					int ch;
					String temp = "";
					while ((ch = br.read()) != -1) {
						//if(temp == "") System.out.println((char)ch);
						temp += (char)ch;
						progress++;
						setProgress(Math.min(
								(int) (progress * 100 / fullProgress), 99));
						label.setText((progress * 100 / fullProgress) + "%");
						//if(progress == fullProgress) System.out.println((char)ch);
					}
					br.close();
					//System.out.println(temp);
					lblNone.setText("entschlüssele...");
					progress = 0;
					fullProgress = temp.length()-1;
					setProgress(0);
					temp = AES.decode(pass, temp);
					//System.out.println(temp);
					String temp2 = "";
					for(int i = 0; i< temp.length(); i++){
						if(temp.charAt(i) == '|'){
							allDecrypt.add(AES.decode(pass, temp2));
							temp2 = "";
						}else{
							temp2 += temp.charAt(i);
						}
						if(i == temp.length()-1)allDecrypt.add(AES.decode(pass, temp2));
						progress++;
						setProgress(Math.min(
								(int) (progress * 100 / fullProgress), 100));
						label.setText((progress * 100 / fullProgress) + "%");
					}
					//System.out.println(allDecrypt.size());
				} else{
					int progress = 0;
					setProgress(0);
					int fullProgress = toSave.size();
					BufferedWriter bw = new BufferedWriter(new FileWriter(file,
							false));
					String sss = (String)SHA.getHash(pass, "Sha-512", TypeToGiveBack.HEXSTRING);
					sss = (String)SHA.getHash(sss, "Sha-512", TypeToGiveBack.HEXSTRING);
					bw.write(new String(Base64.getUrlEncoder().encode(sss.getBytes())));
					bw.newLine();
					Iterator<String> it = toSave.iterator();
					String temp = "";
					lblNone.setText("speichere...");
					while (it.hasNext()) {
						progress++;
						setProgress(Math.min(
								(int) (progress * 100 / fullProgress), 100));
						label.setText((progress * 100 / fullProgress) + "%");
						String t = it.next();
						if(it.hasNext()) temp += AES.encode(pass, t) + "|";
						else temp += AES.encode(pass, t);
					}
					//System.out.println(temp.split("\\|").length);
					progress++;
					bw.write(AES.encode(pass, temp));
					bw.close();
					setProgress(Math.min(
							(int) (progress * 100 / fullProgress), 100));
					label.setText((progress * 100 / fullProgress) + "%");
				}
				return null;
			} catch (IOException ex) {
				return null;
			}
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			if(save && onlySave) {
				Toolkit.getDefaultToolkit().beep();
				frame.setCursor(null); // turn off the wait cursor
				frame.dispose();
				System.exit(0);
			}else if(save){
				Toolkit.getDefaultToolkit().beep();
				frame.setCursor(null); // turn off the wait cursor
				frame.dispose();
				new SplashScreen(pass, file);
			}else{
				Toolkit.getDefaultToolkit().beep();
				frame.setCursor(null); // turn off the wait cursor
				frame.dispose();
				new PWForm(allDecrypt,pass,file);
			}
		}
	}

	/**
	 * Create the application.
	 */
	public SplashScreen() {
		initialize();
		frame.setVisible(true);
	}

	public SplashScreen(String pass, File file) {
		this.pass = pass;
		this.file = file;
		this.allDecrypt = new LinkedList<String>();
		initialize();
		lblNone.setText("lade...");
		frame.setVisible(true);
	}

	public SplashScreen(String pass, File file, LinkedList<String> toSave) {
		this.toSave = toSave;
		//System.out.println(toSave.size());
		this.save = true;
		this.pass = pass;
		this.file = file;
		initialize();
		lblNone.setText("speichern...");
		frame.setVisible(true);
	}

	public SplashScreen(String pass, File file, LinkedList<String> tr,
			boolean b) {
		this.toSave = tr;
		//System.out.println(toSave.size());
		this.save = true;
		this.pass = pass;
		this.file = file;
		this.onlySave = b;
		initialize();
		lblNone.setText("speichern...");
		frame.setVisible(true);
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
		frame.setTitle("");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.getRootPaneExt().getContentPane().setLayout(null);
		frame.addWindowListener(this);

		progressBar = new JProgressBar();
		progressBar.setBounds(10, 275, 430, 14);
		frame.getRootPaneExt().getContentPane().add(progressBar);

		label = new JXLabel();
		label.setText("0 %");
		label.setBounds(83, 260, 26, 14);
		frame.getRootPaneExt().getContentPane().add(label);
		
		lblNone = new JXLabel();
		lblNone.setText("speichern...");
		lblNone.setBounds(10, 260, 63, 14);
		frame.getRootPaneExt().getContentPane().add(lblNone);
		
		JXLabel lblSasad = new JXLabel();
		lblSasad.setIcon(new ImageIcon(SplashScreen.class.getResource("/images/s!logo.png")));
		lblSasad.setBounds(10, 11, 430, 238);
		frame.getRootPaneExt().getContentPane().add(lblSasad);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}

	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		// Instances of javax.swing.SwingWorker are not reusuable, so
		// we create new instances as needed.
		task = new Task();
		task.addPropertyChangeListener(this);
		task.execute();
	}
}
