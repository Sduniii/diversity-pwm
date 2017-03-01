package gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXFrame;

public class MyDropTarget extends DropTarget {

	/**
	 * 
	 */
	private static final long serialVersionUID = 125638781L;

	private File passwordFile;
	private JPanel fileChooserPanel;
	private JXFrame frame;

	public MyDropTarget(File pF, JPanel fcp, JXFrame f) {
		super();
		this.passwordFile = pF;
		this.fileChooserPanel = fcp;
		this.frame = f;
	}

	@Override
	public synchronized void drop(DropTargetDropEvent evt) {
		try {
			evt.acceptDrop(DnDConstants.ACTION_COPY);
			@SuppressWarnings("unchecked")
			List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
			if (droppedFiles.get(0).isFile() && droppedFiles.get(0).getCanonicalPath().endsWith(".dit")) {
				passwordFile = droppedFiles.get(0);
				((JXButton) fileChooserPanel.getComponent(0)).setText(droppedFiles.get(0).getAbsolutePath());
			} else {
				JOptionPane.showMessageDialog(frame, "Falsches Dateiformat!", "Fehler", JOptionPane.ERROR_MESSAGE);
			}
			evt.getDropTargetContext().dropComplete(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
