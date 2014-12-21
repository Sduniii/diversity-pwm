package tools;

import gui.PWForm;

import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class HTMLParser extends SwingWorker<Void, Void> {

	private PWForm pScreen;
	private LinkedList<String> list;
	private File file;

	public HTMLParser(PWForm pScreen, File file) {
		this.pScreen = pScreen;
		list = new LinkedList<String>();
		this.file = file;
		this.addPropertyChangeListener(pScreen);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public Void doInBackground() {
		try {
			//System.out.println("hhhh");
			pScreen.getLblForText().setText("importiere HTML ...");
			pScreen.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			//pScreen.setEnabled(false);
			int progress = 0;
			setProgress(0);
			LineNumberReader lnr = new LineNumberReader(new FileReader(
					this.file));
			lnr.skip(Long.MAX_VALUE);
			int fullProgress = lnr.getLineNumber();
			lnr.close();
			// System.out.println(fullProgress);
			BufferedReader in = new BufferedReader(new FileReader(this.file));
			setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
			String str;
			boolean found = false;

			while ((str = in.readLine()) != null) {
				progress++;
				Thread.sleep(1);
				setProgress(Math
						.min((int) (progress * 100 / fullProgress), 100));
				if (str.contains("id=\"tbl-pwm\"")) {
					found = true;
				}

				if (found && str.contains("</table>")) {
					found = false;
				}
				String tS = "";
				if (str.contains("<td style=\"border-right: 1px solid black;\">")
						&& found) {
					tS = str.substring(
							str.indexOf("<td style=\"border-right: 1px solid black;\">") + 43,
							str.indexOf("</td>"));
					if (!tS.contains("<b>")) {
						list.add(str.substring(
								str.indexOf("<td style=\"border-right: 1px solid black;\">") + 43,
								str.indexOf("</td>")));
					}
				} else if (str.contains("<td>") && found) {
					tS = str.substring(str.indexOf("<td>") + 4,
							str.indexOf("</td>"));
					if (!tS.contains("<b>")) {
						list.add(str.substring(
								str.indexOf("<td>") + 4,
								str.indexOf("</td>")));
					}
				}

			}
			in.close();
			return null;
		} catch (Exception ex) {
			Log.write(ex.getMessage());
			JOptionPane.showMessageDialog(pScreen, "Fehler beim lesen der HTML", "Fehler", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done() {
		this.pScreen.getModel().getDataVector().removeAllElements();
		Iterator<String> it = list.iterator();
		try {
			while (it.hasNext()) {
				pScreen.getModel().addRow(
						new Object[] { it.next(), it.next(), it.next() });
			}
			this.pScreen.repaint();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(pScreen, "Fehler beim lesen der HTML", "Fehler", JOptionPane.ERROR_MESSAGE);
			Log.write(ex.getMessage());
		}finally{
			pScreen.setCursor(null);
			pScreen.setEnabled(true);
			setProgress(0);
			pScreen.getLblForText().setText("HTML geladen");
		}
	}
}
