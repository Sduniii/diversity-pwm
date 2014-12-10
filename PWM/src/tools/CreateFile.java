package tools;

import huffman.Heap;
import huffman.HuffmanToken;
import huffman.HuffmanTree;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;

import BitIO.BitOutputFile;

public class CreateFile {

	private static BufferedInputStream bis;
	private static BitOutputFile bof;
	private static HuffmanTree huffmanTree;

	public static void startCreate(String input, String output, String sss)
			throws IOException {
		if (input.isEmpty() || output.isEmpty()) {
			return;
		} else {
			sss = new String(Base64.getUrlEncoder().encodeToString(
					sss.getBytes()));
			File fileFC = new File(input);
			fileFC.createNewFile();
			BufferedWriter fw = new BufferedWriter(new FileWriter(fileFC));
			fw.write(sss);
			fw.newLine();
			fw.close();
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
	private static ArrayList<HuffmanToken> createDictionary(String sourceFile) {
		ArrayList<HuffmanToken> dictionary = new ArrayList<HuffmanToken>();
		try {
			bis = new BufferedInputStream(new FileInputStream(sourceFile));
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
							dictionary.get(i).setFreq(
									dictionary.get(i).getFreq() + 1);
							found = true;
						}
					}
					if (!found) {
						dictionary.add(new HuffmanToken(byteRead, 1));
					}
				}
				byteRead = bis.read();
			}
			bis.close();
			System.out.println("Datei erfolgreich eingelesen!");
		} catch (IOException e) {
			System.err.println("Die Datei wurde nicht gefunden!");
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
	private static HuffmanTree createHuffmanTree(
			ArrayList<HuffmanToken> dictionary) {

		int heapSize = 0;
		// einen Heap erstellen der HuffmanTrees beinhaltet
		Heap<HuffmanTree> huffmanHeap = new Heap<HuffmanTree>(new HuffmanTree(
				dictionary.get(0)));
		heapSize++;

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

			HuffmanTree concatHuffman = HuffmanTree.concatTwoHuffmanTrees(
					lowest_freq, lowest2_freq);
			phase++;
			if (phase == heapsizeBefore - 1) { // nach n-1 Phasen ist
												// Schluss
				finalHuffman = concatHuffman;
				break;
			}
			huffmanHeap.insert(concatHuffman);
			heapSize++;

		}
		Heap<HuffmanTree> Huffman = new Heap<HuffmanTree>(finalHuffman);

		if (huffmanHeap.getSize() == 1) {
			System.out.println("HuffmanTree erfolgreich aufgebaut!");
		} else
			System.out.println("Heap hat nicht korrekte Groesse");

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
	private static void compressFile(String sourcefile, String targetfile,
			HuffmanTree huffmanTree, String sss) {

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

				byteRead = bis.read();
			}
			bis.close();
			bof.close(); // datei schliessen
			/** Datei zuende geschrieben */

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.err.println("Komprimierung abgeschlossen!");
	}

}
