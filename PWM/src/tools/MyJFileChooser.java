package tools;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

@SuppressWarnings("serial")
public class MyJFileChooser extends JFileChooser {

	public MyJFileChooser() {
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        SwingUtilities.updateComponentTreeUI(this);
	    } catch (Exception ex) {
	    }
	}

	public MyJFileChooser(String currentDirectoryPath) {
		super(currentDirectoryPath);
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        SwingUtilities.updateComponentTreeUI(this);
	    } catch (Exception ex) {
	    }
	}

	public MyJFileChooser(File currentDirectory) {
		super(currentDirectory);
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        SwingUtilities.updateComponentTreeUI(this);
	    } catch (Exception ex) {
	    }
	}

	public MyJFileChooser(FileSystemView fsv) {
		super(fsv);
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        SwingUtilities.updateComponentTreeUI(this);
	    } catch (Exception ex) {
	    }
	}

	public MyJFileChooser(File currentDirectory, FileSystemView fsv) {
		super(currentDirectory, fsv);
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        SwingUtilities.updateComponentTreeUI(this);
	    } catch (Exception ex) {
	    }
	}

	public MyJFileChooser(String currentDirectoryPath, FileSystemView fsv) {
		super(currentDirectoryPath, fsv);
		try {
	        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	        SwingUtilities.updateComponentTreeUI(this);
	    } catch (Exception ex) {
	    }
	}

}
