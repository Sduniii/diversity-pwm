package models;

import gui.PWForm;
import gui.SplashScreen;
import huffman.BinaryTree;
import huffman.Heap;
import huffman.HuffmanToken;
import huffman.HuffmanTree;

import java.awt.Cursor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXLabel;

import tools.AES;
import tools.Log;
import tools.SHA;
import tools.SHA.TypeToGiveBack;
import BitIO.BitInputFile;
import BitIO.BitOutputFile;

public class Core extends SwingWorker<Void, Void> {

	public enum Mode {
		LOAD, SAVE, CLOSESAVE
	}

	private File file;
	private JXLabel lblForText;
	private LinkedList<String> allDecrypt;
	private LinkedList<String> toSave;
	private String pass;
	private BufferedInputStream bis;
	private BitOutputFile bof;
	private HuffmanTree huffmanTree;
	private Mode mode;
	private SplashScreen sScreen;
	private PWForm pScreen;

	public Core(File file, JXLabel lblJxLabel, Mode mode, String pass, SplashScreen propertyChangeListener) {
		this.file = file;
		this.lblForText = lblJxLabel;
		this.mode = mode;
		this.pass = pass;
		this.allDecrypt = new LinkedList<String>();
		this.addPropertyChangeListener(propertyChangeListener);
		this.sScreen = propertyChangeListener;
	}

	public Core(File file, JXLabel lblJxLabel, Mode mode, String pass, PWForm propertyChangeListener) {
		this.file = file;
		this.lblForText = lblJxLabel;
		this.mode = mode;
		this.pass = pass;
		this.allDecrypt = new LinkedList<String>();
		this.addPropertyChangeListener(propertyChangeListener);
		this.pScreen = propertyChangeListener;
		// this.pScreen.getStatusbar().addRightComponent(this.pScreen.getProgressBar());
	}

	@Override
	public Void doInBackground() {
		// System.out.println(mode == Mode.LOAD);
		if (sScreen != null) {
			sScreen.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			sScreen.setEnabled(false);
		} else if (pScreen != null) {
			pScreen.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			pScreen.setEnabled(false);
		}
		try {
			if (mode == Mode.LOAD) {
				File tempFile;
				tempFile = new File(file.getAbsolutePath() + ".tmp");
				decompressFile(file.getAbsolutePath(), file.getAbsolutePath() + ".tmp");
				long progress = 0;
				setProgress(0);
				lblForText.setText("lese...");
				long fullProgress = tempFile.length() - 1;
				BufferedReader br = new BufferedReader(new FileReader(tempFile));
				// System.out.println(br.readLine());
				fullProgress -= br.readLine().length();
				progress++;
				setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
				lblForText.setText("lese... " + Integer.toString(getProgress()) + "%");
				int ch;
				String temp = "";
				while ((ch = br.read()) != -1) {
					// if(temp == "") System.out.println((char)ch);
					temp += (char) ch;
					progress++;
					setProgress(Math.min((int) (progress * 100 / fullProgress), 99));
					lblForText.setText("lese... " + Integer.toString(getProgress()) + "%");
					// if(progress == fullProgress)
					// System.out.println((char)ch);
				}
				br.close();
				// System.out.println(temp);
				lblForText.setText("entschlüssele...");
				progress = 0;
				fullProgress = temp.length() - 1;
				setProgress(0);
				temp = AES.decode(pass, temp);
				// System.out.println(temp.length());
				String temp2 = "";
				for (int i = 0; i < temp.length(); i++) {
					// System.out.println(i);
					if (temp.charAt(i) == '|') {
						// System.out.println(temp2);
						if (this.pScreen != null) {

						} else {
							allDecrypt.add(AES.decode(pass, temp2));
						}
						temp2 = "";
					} else {
						temp2 += temp.charAt(i);
					}
					if (i == temp.length() - 1)
						if (this.pScreen != null) {

						} else {
							allDecrypt.add(AES.decode(pass, temp2));
						}
					progress++;
					setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
					lblForText.setText("entschlüssele... " + Integer.toString(getProgress()) + "%");
				}
				// System.out.println("end");
				tempFile.delete();
				// System.out.println(allDecrypt.size());
			} else if (mode == Mode.SAVE) {
				Boolean success = false;
				while (!success) {
					int progress = 0;
					setProgress(0);
					// System.out.println(FileSystems.getDefault().getPath(file.getAbsolutePath()+".bak",""));
					Files.copy(FileSystems.getDefault().getPath(file.getAbsolutePath(), ""),
							FileSystems.getDefault().getPath(file.getAbsolutePath() + ".bak", ""),
							StandardCopyOption.REPLACE_EXISTING);
					File tempFile = new File(file.getAbsolutePath() + ".tmp");
					int fullProgress = toSave.size();
					BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile, false));
					String sss = (String) SHA.getHash(pass, "Sha-512", TypeToGiveBack.HEXSTRING);
					sss = (String) SHA.getHash(sss, "Sha-512", TypeToGiveBack.HEXSTRING);
					bw.write(new String(Base64.getUrlEncoder().encode(sss.getBytes())));
					bw.newLine();
					Iterator<String> it = toSave.iterator();
					String temp = "";
					lblForText.setText("speichere...");
					while (it.hasNext()) {
						progress++;
						setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
						lblForText.setText("speichere... " + Integer.toString(getProgress()) + "%");
						String t = it.next();
						if (it.hasNext())
							temp += AES.encode(pass, t) + "|";
						else
							temp += AES.encode(pass, t);
					}
					// System.out.println(temp.split("\\|").length);
					progress++;
					bw.write(AES.encode(pass, temp));
					bw.close();
					setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
					lblForText.setText("speichere... " + Integer.toString(getProgress()) + "%");
					startCompress(tempFile.getAbsolutePath(), file.getAbsolutePath(),
							new String(Base64.getUrlEncoder().encode(sss.getBytes())));
					tempFile.delete();
					if (!file.exists()) {
						Files.copy(FileSystems.getDefault().getPath(file.getAbsolutePath() + ".bak", ""),
								FileSystems.getDefault().getPath(file.getAbsolutePath(), ""),
								StandardCopyOption.REPLACE_EXISTING);
					} else {
						success = true;
						Files.copy(FileSystems.getDefault().getPath(file.getAbsolutePath(), ""),
								FileSystems.getDefault().getPath(file.getAbsolutePath() + ".bak", ""),
								StandardCopyOption.REPLACE_EXISTING);
					}
				}
			}

			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			Log.write(ex);
			return null;
		}
	}

	public void startCompress(String input, String output, String sss) {
		if (input.isEmpty() || output.isEmpty()) {

		} else {
			ArrayList<HuffmanToken> dict = createDictionary(input);
			HuffmanTree comTree = createHuffmanTree(dict);
			compressFile(input, output, comTree, sss);
		}

	}

	/**
	 * Read the File and create the dictionary containing the huffman token
	 * 
	 * @param sourceFile
	 *            is the input file
	 */
	private ArrayList<HuffmanToken> createDictionary(String sourceFile) {
		lblForText.setText("komprimiere: erstelle Bibliothek...");
		ArrayList<HuffmanToken> dictionary = new ArrayList<HuffmanToken>();
		int progress = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFile));
			int fullProgress = (int) (new File(sourceFile)).length();
			setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
			lblForText.setText("komprimiere: erstelle Bibliothek... " + Integer.toString(getProgress()) + "%");
			int byteRead = bis.read();
			while (byteRead > -1 && byteRead < 256) { // bytes aus Datei
														// lesen
				// solange Datei
				// vorhanden und kein
				// Byte Groesser als 255
				// ist
				// Wenn Liste leer, dann wird ein Huffman Token mit der
				// Haeufigkeit 1 eingefuegt
				if (dictionary.isEmpty()) {
					dictionary.add(new HuffmanToken(byteRead, 1));
				} else {
					// Vergleichen ob es das Byte schon gibt und die
					// Haeufigkeit
					// um 1 erhoeht werden muss, ansonsten ein neuer Huffman
					// Token
					boolean found = false;
					for (int i = 0; i < dictionary.size(); i++) {
						if (dictionary.get(i).getC() == byteRead) {
							dictionary.get(i).setFreq(dictionary.get(i).getFreq() + 1);
							found = true;
						}
					}
					if (!found) {
						dictionary.add(new HuffmanToken(byteRead, 1));
					}
				}
				progress++;
				setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
				lblForText.setText("komprimiere: erstelle Bibliothek... " + Integer.toString(getProgress()) + "%");
				byteRead = bis.read();
			}
			bis.close();
			// System.out.println("Datei erfolgreich eingelesen!");
		} catch (IOException e) {
			// System.err.println("Die Datei wurde nicht gefunden!");
			Log.write(e);
		}
		return dictionary;
	}

	/**
	 * Creates the Huffmantree from the given dictionary
	 * 
	 * @param dictionary
	 *            is the dictionary with the huffman token
	 * @return returns a Huffmantree
	 */
	private HuffmanTree createHuffmanTree(ArrayList<HuffmanToken> dictionary) {

		/** Progress **/
		lblForText.setText("komprimiere: erstelle Baum...");
		int progress = 0;
		/** Progress END **/

		int heapSize = 0;
		// einen Heap erstellen der HuffmanTrees beinhaltet
		Heap<HuffmanTree> huffmanHeap = new Heap<HuffmanTree>(new HuffmanTree(dictionary.get(0)));
		heapSize++;

		/** Progress **/
		progress++;
		int fullProgress = dictionary.size() * 2;
		setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
		lblForText.setText("komprimiere: erstelle Baum..." + Integer.toString(getProgress()) + "%");
		/** Progress END **/

		if (dictionary.size() == 1) { // der Sonderfall, dass eine Datei nur
										// aus
			// der Wiederholung eines und desselben
			// Bytes besteht
			huffmanTree = huffmanHeap.extractMin(); // dann ist der
													// Huffmanbaum
			// der Wurzelknoten des
			// Heaps
			return huffmanTree;
		}

		for (int i = 1; i < dictionary.size(); i++) { // alle HuffmanToken
														// aus
			// Liste in den Heap als
			// einelementige Baeume
			// einfuegen
			huffmanHeap.insert(new HuffmanTree(dictionary.get(i)));
			heapSize++;

			/** Progress **/
			progress++;
			setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
			lblForText.setText("komprimiere: erstelle Baum..." + Integer.toString(getProgress()) + "%");
			/** Progress END **/

		}
		int phase = 0;
		int heapsizeBefore = huffmanHeap.getSize();
		HuffmanTree finalHuffman = new HuffmanTree();
		// Huffman-Algo

		while (heapSize > 1) { // der letzte Baum im Heap ist Huffmanbaum

			HuffmanTree lowest_freq = huffmanHeap.extractMin();
			heapSize--;

			HuffmanTree lowest2_freq = huffmanHeap.extractMin();
			heapSize--;

			HuffmanTree concatHuffman = HuffmanTree.concatTwoHuffmanTrees(lowest_freq, lowest2_freq);
			phase++;
			if (phase == heapsizeBefore - 1) { // nach n-1 Phasen ist
												// Schluss
				finalHuffman = concatHuffman;
				break;
			}
			huffmanHeap.insert(concatHuffman);
			heapSize++;

			/** Progress **/
			progress++;
			setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
			lblForText.setText("komprimiere: erstelle Baum..." + Integer.toString(getProgress()) + "%");
			/** Progress END **/
		}
		Heap<HuffmanTree> Huffman = new Heap<HuffmanTree>(finalHuffman);

		if (huffmanHeap.getSize() == 1) {
			// System.out.println("HuffmanTree erfolgreich aufgebaut!");
		} else
			Log.write(new Exception("Heap hat nicht korrekte Groesse"));

		huffmanTree = Huffman.peek();

		// System.out.println("Haeufigkeit in der Wurzel des Huffmanbaums: "+
		// huffmanTree.root.data.freq);
		// System.out.println("Anzahl Zeichen: " + dictionary.size());
		return huffmanTree;
	}

	/**
	 * Compress the sourcefile by using the huffmantree
	 * 
	 * @param sourcefile
	 *            is the sourcefilename
	 * @param targetfile
	 *            is the targetfilename
	 * @param huffmanTree
	 *            is the huffmantree that is used for compression
	 * @param sss
	 */
	private void compressFile(String sourcefile, String targetfile, HuffmanTree huffmanTree, String sss) {
		/** Progress **/
		lblForText.setText("komprimiere...");
		int progress = 0;
		/** Progress END **/

		ArrayList<String> codeWords = huffmanTree.makeCodeTable();
		ArrayList<Integer> charValues = huffmanTree.chars;

		/**
		 * Routine um dafuer zu sorgen, dass das hoechste Zeichen und sein
		 * zugehoeriges Codewort als letztes in die Codetabelle geschrieben
		 * werden.
		 */
		int highestChar = Collections.max(charValues); // das zeichen mit
														// dem
		int highIndex;
		String highWay;
		for (int y = 0; y < charValues.size(); y++) {
			if (charValues.get(y).equals(highestChar)) { // finden des
															// hoechsten
															// Zeichens
				highIndex = y; // Index des hoechsten Zeichens
								// zwischenspeichern
				charValues.remove(highIndex); // hoechstes Zeichen aus Liste
												// entfernen, alle
												// Nachfolger
												// ruecken eine Position auf
				charValues.add(highestChar); // und am Ende wieder anfuegen
				highWay = codeWords.get(highIndex); // Codewort des
													// hoechsten
													// Zeichens
													// zwischenspeichern
				codeWords.remove(highIndex); // Codewort des hoechsten
												// Zeichens
												// aus Liste entfernen, alle
												// Nachfolger ruecken eine
												// Position auf
				codeWords.add(highWay); // und am Ende wieder anfuegen
			}
		}

		try {
			/** Progress **/
			progress++;
			int fullProgress = (int) (1 + charValues.size() + new File(sourcefile).length());
			setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
			lblForText.setText("komprimiere... " + Integer.toString(getProgress()) + "%");
			/** Progress END **/

			/** Signatur */

			bof = new BitOutputFile(targetfile);

			bof.writeByte(100);
			bof.writeByte(105);
			bof.writeByte(118);
			bof.writeByte(101);
			bof.writeByte(114);
			bof.writeByte(115);
			bof.writeByte(73);
			bof.writeByte(84);
			bof.writeByte(121);
			bof.writeByte(46);
			bof.writeByte(80);
			bof.writeByte(87);
			bof.writeByte(77);
			bof.writeByte(13);
			bof.writeByte(10);

			for (byte b : sss.getBytes()) {
				bof.writeByte(b);
			}
			bof.writeByte(13);
			bof.writeByte(10);

			/**
			 * Beginn der Codetabelle Schreiben der Codetabelle in die Datei:
			 * Vorgabe: "hoechstes Zeichen|Zeichen1|.."
			 */
			bof.writeByte(highestChar); // das zeichen mit dem hoechsten
			// bytewert steht am anfang der
			// codetabelle

			/** Progress **/
			progress++;
			setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
			lblForText.setText("komprimiere... " + Integer.toString(getProgress()) + "%");
			/** Progress END **/

			/**
			 * Jetzt ..|Zeichen1|Codewort Zeichen1|Zeichen2|Codewort
			 * Zeichen2|...
			 */
			for (int i = 0; i < charValues.size(); i++) {

				/** Schreiben des Bytewerts des aktuellen Zeichens */
				bof.writeByte(charValues.get(i));

				/** Bitweise Schreiben der einzelnen Codewoerter */
				bof.beginBitMode(); // Codewort beginnt
				for (int j = 0; j < codeWords.get(i).length(); j++) {
					if (codeWords.get(i).charAt(j) == '1') {
						bof.writeBit(true); // schreibt eine '1'
					} else {
						bof.writeBit(false); // schreibt eine '0'
					}
					/** Progress **/
					progress++;
					setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
					lblForText.setText("komprimiere... " + Integer.toString(getProgress()) + "%");
					/** Progress END **/
				}
				bof.endBitMode(); // Codeword ist zuende
			}
			/** Ende der Codetabelle */

			/**
			 * Jetzt werden die Codewoerter in einem Stueck in die Datei
			 * geschrieben
			 */
			bis = new BufferedInputStream(new FileInputStream(sourcefile));
			int byteRead = bis.read();
			bof.beginBitMode();
			while (byteRead > -1 && byteRead < 256) {
				int who = 0;
				for (int i = 0; i < charValues.size(); i++) {
					if (charValues.get(i) == byteRead) {
						who = i;
					}
				}
				/** Bitweise Schreiben der einzelnen Codewoerter */
				// Codewort beginnt
				for (int j = 0; j < codeWords.get(who).length(); j++) {
					if (codeWords.get(who).charAt(j) == '1') {
						bof.writeBit(true); // schreibt eine '1'
					} else {
						bof.writeBit(false); // schreibt eine '0'
					}

				}
				/** Progress **/
				progress++;
				setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
				lblForText.setText("komprimiere... " + Integer.toString(getProgress()) + "%");
				/** Progress END **/

				byteRead = bis.read();
			}
			bis.close();
			bof.close(); // datei schliessen
			/** Datei zuende geschrieben */

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.write(e);
		}
		// System.err.println("Komprimierung abgeschlossen!");
	}

	/*
	 * This Method is called while reading the begin of the compressed file,
	 * when a codeword was found.
	 */
	private void handleCodeword(String codeword, HuffmanTree decompTree) {
		decompTree.codeWords.add(codeword);
	}

	/*
	 * This Method is called while reading the begin of the compressed file,
	 * when a character was found.
	 */
	private void handleChar(int character, HuffmanTree decompTree) {
		decompTree.chars.add(character);
	}

	/*
	 * This method generates the tree, from the information of its dictionary.
	 */
	public void rebuildTree(HuffmanTree decompTree) {
		for (int i = 0; i < decompTree.codeWords.size(); i++) {
			decompTree.constructWayToLeaf(decompTree.codeWords.get(i), decompTree.chars.get(i));
		}
	}

	/** Das Einlesen der Codetabelle funktioniert jetzt ! */
	public void decompressFile(String sourceFile, String targetFile) {
		/** Progress **/
		lblForText.setText("dekomprimiere...");
		int progress = 0;
		/** Progress END **/

		HuffmanTree decompTree = new HuffmanTree();
		String codeword = "";
		int c;
		int highestChar;
		BitInputFile bif;
		try {
			bif = new BitInputFile(sourceFile);
			/** Progress **/
			progress++;
			int fullProgress = (int) new File(sourceFile).length() * 8;
			setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
			lblForText.setText("dekomprimiere... " + Integer.toString(getProgress()) + "%");
			/** Progress END **/
			int breaks = 0;
			int b = bif.readByte();
			int b2 = bif.readByte();
			while (true) {
				if (b == 13 && b2 == 10) {
					breaks++;
				}
				if (breaks == 2) {
					break;
				}
				b = b2;
				b2 = bif.readByte();
			}
			highestChar = bif.readByte(); // Einlesen des ersten Bytes,
											// welches
			c = Integer.MIN_VALUE;
			int blabla = 0;

			while (c <= highestChar) { // das hoechste zeichen kennzeichnet
										// das
				// letzte wort der codetabelle
				c = bif.readByte(); // Char-Wert des aktuellen Zeichens
									// auslesen
				// System.out.println("Zeichen: " + c);
				handleChar(c, decompTree); // zeichenwert uebergeben
				int bitsleft = bif.beginBitMode(); // Anzahl der Bits die
													// nun
				// gelesen werden koennen.
				// das muesste ja eigentlich
				// dann bis zum naechsten
				// int bzw byte in der Datei sein. also ist bitsleft die
				// laenge
				// des aktuellen codeworts.
				while (bitsleft > 0) {
					if (bif.readBit()) { // hat eine '1' gelesen
						codeword += "1";
					} else { // hat eine '0' gelesen
						codeword += "0";
					}
					bitsleft--;
					blabla++;
				}
				handleCodeword(codeword, decompTree); // codewort in die
														// liste einfuegen
				// System.out.println("Codewort: " + codeword);
				codeword = ""; // codewort-string wieder leeren
				bif.endBitMode();
				if (c == highestChar) {
					// System.out.println("--- Hier endet die Codetabelle.
					// ---");
					bif.beginBitMode();
					break;
				}
				blabla += 8;
			}
			/** Progress **/
			progress += blabla;
			setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
			lblForText.setText("dekomprimiere... " + Integer.toString(getProgress()) + "%");
			/** Progress END **/

			rebuildTree(decompTree); // Huffmanbaum rekonstruieren
			// System.out.println("Tree rebuilded: " + "\n"
			// + decompTree.toString());
			// ab jetzt die Bitfolgen lesen und im baum ablaufen bis man in
			// einem blatt gelandet ist.
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile));
			// System.ot.println("bos erstellt.");
			/** ab hier muss noch bearbeitet werden */

			BinaryTree<HuffmanToken>.BinaryTreeNode<HuffmanToken> node;
			node = decompTree.getRoot();
			// System.out.println("node gesetzt: " + node.data.getC());
			// while(das ende der Datei wurde noch nicht erreicht
			int bitsLeft = bif.bitsLeft();
			while (bitsLeft >= 0) {

				// System.out.println("bin in while-schleife");
				// bif.readBit();
				// Bits ablaufen bis man in einem Blatt gelandet ist,
				// Zeichen
				// aus dem Knoten rausgreifen, schreiben und zurueck zur
				// wurzel... bis fertig
				if (node.isLeaf()) {
					// System.out.println("blatt gefunden!");

					// Den Char aus node.data in Zieldatei schreiben
					// System.out.println("Node data: " + node.data.getC());
					// System.out.println(bif.bitsLeft());
					bos.write(node.getData().getC()); // Byte schreiben
					node = decompTree.getRoot(); // zurueck zur Wurzel

				} else if (!bif.readBit()) {
					node = node.getLeft();
					// System.out.println("gehe 'links'");
				} else {
					node = node.getRight();
					// System.out.println("gehe 'rechts'");
				}
				if (bitsLeft > 0)
					bitsLeft = bif.bitsLeft();
				else
					bitsLeft--;

				/** Progress **/
				progress++;
				setProgress(Math.min((int) (progress * 100 / fullProgress), 100));
				lblForText.setText("dekomprimiere... " + Integer.toString(getProgress()) + "%");
				/** Progress END **/

			}
			// System.out.println(bif.bitsLeft());
			bif.endBitMode();

			bif.close();
			bos.close();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "<html><font size='2'>Datei kann nicht gelesen werden!</font></html>",
					"Fehler", JOptionPane.ERROR_MESSAGE);
			Log.write(e);
		}
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
	 * @return the lblNone
	 */
	public JXLabel getLblForText() {
		return lblForText;
	}

	/**
	 * @param lblNone
	 *            the lblNone to set
	 */
	public void setLblForText(JXLabel lblNone) {
		this.lblForText = lblNone;
	}

	/**
	 * @return the allDecrypt
	 */
	public LinkedList<String> getAllDecrypt() {
		return allDecrypt;
	}

	/**
	 * @param allDecrypt
	 *            the allDecrypt to set
	 */
	public void setAllDecrypt(LinkedList<String> allDecrypt) {
		this.allDecrypt = allDecrypt;
	}

	/**
	 * @return the toSave
	 */
	public LinkedList<String> getToSave() {
		return toSave;
	}

	/**
	 * @param toSave
	 *            the toSave to set
	 */
	public void setToSave(LinkedList<String> toSave) {
		this.toSave = toSave;
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

	/*
	 * Executed in event dispatching thread
	 */
	@Override
	public void done() {
		if (this.pScreen != null && mode == Mode.SAVE) {
			setProgress(0);
			getLblForText().setText("fertig");
			pScreen.setEnabled(true);
			pScreen.setCursor(null);
		} else if (this.pScreen != null && mode == Mode.CLOSESAVE) {
			this.pScreen.dispose();
		} else if (this.sScreen != null && mode == Mode.LOAD) {
			sScreen.setCursor(null);
			sScreen.dispose();
			new PWForm(getAllDecrypt(), getPass(), getFile());
		} else if (this.pScreen != null && mode == Mode.LOAD) {
			this.pScreen.getModel().getDataVector().removeAllElements();
			Iterator<String> it = getAllDecrypt().iterator();
			try {
				while (it.hasNext()) {
					pScreen.getModel().addRow(new Object[] { it.next(), it.next(), it.next() });
				}
				this.pScreen.repaint();
			} catch (Exception ex) {
				Log.write(ex);
			}
			pScreen.setCursor(null);
			pScreen.setEnabled(true);
		}
	}

}