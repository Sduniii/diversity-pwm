package gui;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.JXFrame;

public class SplashScreen implements PropertyChangeListener, WindowListener{

	private JXFrame frame;
	private JProgressBar progressBar;
	private Task task;

	  class Task extends SwingWorker<Void, Void> {
	    /*
	     * Main task. Executed in background thread.
	     */
	    @Override
	    public Void doInBackground() {
	      Random random = new Random();
	      int progress = 0;
	      // Initialize progress property.
	      setProgress(0);
	      while (progress < 100) {
	        // Sleep for up to one second.
	        try {
	          Thread.sleep(random.nextInt(1000));
	        } catch (InterruptedException ignore) {
	        }
	        // Make random progress.
	        progress += random.nextInt(10);
	        setProgress(Math.min(progress, 100));
	      }
	      return null;
	    }

	    /*
	     * Executed in event dispatching thread
	     */
	    @Override
	    public void done() {
	      Toolkit.getDefaultToolkit().beep();
	      frame.setCursor(null); // turn off the wait cursor
	      frame.dispose();
	      System.exit(0);
	    }
	  }

	/**
	 * Create the application.
	 */
	public SplashScreen() {
		initialize();
		frame.setVisible(true);
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setUndecorated(true);
		frame.getRootPaneExt().getContentPane().setLayout(null);
		frame.addWindowListener(this);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 275, 430, 14);
		frame.getRootPaneExt().getContentPane().add(progressBar);
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
