package gui;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.*;

import models.Core;

import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXLabel;

import tools.Log;
import tools.OS;

public class SplashScreen extends JXFrame implements PropertyChangeListener, WindowListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private File file;
	private JXLabel lblForText;
	private String pass;
	private JProgressBar progressbar;

	/**
	 * Create the application.
	 */
	public SplashScreen() {
		super();
		initialize();
		this.setVisible(true);
	}

	public SplashScreen(String pass, File file) {
		super();
		setPass(pass);
		setFile(file);
		initialize();
		getLblForText().setText("lade...");
		this.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
        OS.setLockAndFeel();
        JXFrame.setDefaultLookAndFeelDecorated(true);
        this.setIconImage(
				Toolkit.getDefaultToolkit().getImage(
						LoginForm.class.getResource("/images/s!logo.png")));
		this.setTitle("");
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int screenWidth = gd.getDisplayMode().getWidth();
		int screenHeight = gd.getDisplayMode().getHeight();
		this.setBounds(screenWidth / 2 - 225, screenHeight / 2 - 150,
				450, 300);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setUndecorated(true);
        this.getRootPaneExt().getContentPane().setLayout(null);

		setProgressbar(new JProgressBar());
		getProgressbar().setBounds(10, 275, 430, 14);
		this.getRootPaneExt().getContentPane().add(getProgressbar());
		this.addWindowListener(this);

		setLblForText(new JXLabel());
		getLblForText().setText("speichern...");
		getLblForText().setBounds(10, 260, 400, 14);
		this.getRootPaneExt().getContentPane().add(getLblForText());

		JXLabel lblSasad = new JXLabel();
		lblSasad.setIcon(new ImageIcon(SplashScreen.class
				.getResource("/images/s!logo.png")));
		lblSasad.setBounds(10, 11, 430, 238);
		this.getRootPaneExt().getContentPane().add(lblSasad);
        System.gc();
    }

	@Override
	public void windowActivated(WindowEvent arg0) {
        System.gc();
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
		Core cr = new Core(getFile(), getLblForText(), Core.Mode.LOAD, getPass(), this);
		cr.execute();
        System.gc();

//		Task tsk = new Task(getFile(), getLblForText(), Task.Mode.LOAD,
//				getPass());
//		tsk.addPropertyChangeListener(this);
//		while(!tsk.isDone()){
//			
//		}
		//this.setCursor(null);
		//this.dispose();
		//new PWForm(tsk.getAllDecrypt(), getPass(), getFile());

	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the lblForText
	 */
	public JXLabel getLblForText() {
		return lblForText;
	}

	/**
	 * @param lblForText
	 *            the lblForText to set
	 */
	public void setLblForText(JXLabel lblForText) {
		this.lblForText = lblForText;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass
	 *            the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the progressbar
	 */
	public JProgressBar getProgressbar() {
		return progressbar;
	}

	/**
	 * @param progressbar
	 *            the progressbar to set
	 */
	public void setProgressbar(JProgressBar progressbar) {
		this.progressbar = progressbar;
	}
	

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            int progress = (Integer) evt.getNewValue();
            getProgressbar().setValue(progress);
		}

	}
}
