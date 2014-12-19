package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;

import javax.swing.SwingWorker;

public class HTMLParser extends SwingWorker<Void, Void>{

	File file;
	LinkedList<String> list;
	
	public HTMLParser(File file, LinkedList<String> list) {
		this.file = file;
		this.list = list;
	}
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public Void doInBackground() {
		try {

			BufferedReader in = new BufferedReader(new FileReader(file));
			String str;
			boolean found = false;

			while ((str = in.readLine()) != null) {
				if (str.contains("id=\"tbl-pwm\"")) {
					found = true;
				}

				if (found && str.contains("</table>")) {
					found = false;
				}

				if (str.contains("<td>") && found) {
					String tS = str.substring(str.indexOf("<td>") + 4,
							str.indexOf("</td>"));
					if (!tS.contains("<b>")) {
						list.add(str.substring(str.indexOf("<td>") + 4,
								str.indexOf("</td>")));
					}
				}

			}
			in.close();
			return null;
		}catch(Exception ex){
			return null;
		}
	}

	public LinkedList<String> parse(File urlS) {
		LinkedList<String> list = new LinkedList<String>();
		try {

			BufferedReader in = new BufferedReader(new FileReader(urlS));
			String str;
			boolean found = false;

			while ((str = in.readLine()) != null) {
				if (str.contains("id=\"tbl-pwm\"")) {
					found = true;
				}

				if (found && str.contains("</table>")) {
					found = false;
				}

				if (str.contains("<td>") && found) {
					String tS = str.substring(str.indexOf("<td>") + 4,
							str.indexOf("</td>"));
					if (!tS.contains("<b>")) {
						list.add(str.substring(str.indexOf("<td>") + 4,
								str.indexOf("</td>")));
					}
				}

			}
			in.close();
			return list;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return list;
		}
	}
}
