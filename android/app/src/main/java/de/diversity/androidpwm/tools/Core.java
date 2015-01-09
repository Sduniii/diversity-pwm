package de.diversity.androidpwm.tools;

        import android.database.Cursor;
        import android.os.AsyncTask;
        import android.util.Base64;
        import android.widget.TextView;

        import java.io.BufferedInputStream;
        import java.io.BufferedOutputStream;
        import java.io.BufferedReader;
        import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.FileReader;
        import java.io.FileWriter;
        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Iterator;
        import java.util.LinkedList;
        import java.util.Objects;

        import de.diversity.androidpwm.bitio.BitInputFile;
        import de.diversity.androidpwm.bitio.BitOutputFile;
        import de.diversity.androidpwm.huffman.BinaryTree;
        import de.diversity.androidpwm.huffman.Heap;
        import de.diversity.androidpwm.huffman.HuffmanToken;
        import de.diversity.androidpwm.huffman.HuffmanTree;
        import de.diversity.androidpwm.models.Mode;

public class Core{

    private File file;
    private LinkedList<String> allDecrypt;
    private LinkedList<String> toSave;
    private String pass;
    private BufferedInputStream bis;
    private BitOutputFile bof;
    private HuffmanTree huffmanTree;
    private Mode mode;
    private TextView txtV;

    public Core(String file, String pass, Mode mode, TextView txtV) {
        this.file = new File(file);
        this.mode = mode;
        this.pass = pass;
        this.allDecrypt = new LinkedList<String>();
        this.txtV = txtV;
    }

    public void doInBackground() {
        // System.out.println(mode == Mode.LOAD);
        try {
            if (mode == Mode.LOAD) {
                boolean withCompress = true;
                File tempFile;
                //txtV.setText("entpacke Datei ...");
                if (withCompress) {
                    tempFile = new File(file.getAbsolutePath() + "tmp");
                    decompressFile(file.getAbsolutePath(),
                            file.getAbsolutePath() + "tmp");
                } else {
                    tempFile = file;
                }
                BufferedReader br = new BufferedReader(new FileReader(tempFile));
                br.readLine();
                //txtV.setText("lese Datei ...");
                int ch;
                String temp = "";
                while ((ch = br.read()) != -1) {
                    // if(temp == "") System.out.println((char)ch);
                    temp += (char) ch;
                }
                br.close();
                //System.out.println(temp);
                //txtV.setText("dekodiere Datei ...");
                temp = AES.decode(pass, temp);
                String temp2 = "";
                for (int i = 0; i < temp.length(); i++) {
                    // System.out.println(i);
                    if (temp.charAt(i) == '|') {
                        allDecrypt.add(AES.decode(pass, temp2));
                        //System.out.println(new String(new String(allDecrypt.getLast().getBytes(),"ASCII").getBytes("UTF-8")));
                        temp2 = "";
                    } else {
                        temp2 += temp.charAt(i);
                    }
                    if (i == temp.length() - 1) {
                        allDecrypt.add(AES.decode(pass, temp2));
                    }
                }
                // System.out.println("end");
                if (withCompress)
                    tempFile.delete();

            } else {
                File tempFile = new File(file.getAbsolutePath() + "tmp");
                int fullProgress = toSave.size();
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile,
                        false));
                String sss = (String) SHA.getHash(pass, "Sha-512",
                        SHA.TypeToGiveBack.HEXSTRING);
                sss = (String) SHA.getHash(sss, "Sha-512",
                        SHA.TypeToGiveBack.HEXSTRING);
                bw.write(new String(Base64.encode(
                        sss.getBytes(),Base64.URL_SAFE)));
                bw.newLine();
                Iterator<String> it = toSave.iterator();
                String temp = "";
                while (it.hasNext()) {
                    String t = it.next();
                    if (it.hasNext())
                        temp += AES.encode(pass, t) + "|";
                    else
                        temp += AES.encode(pass, t);
                }
                // System.out.println(temp.split("\\|").length);
                bw.write(AES.encode(pass, temp));
                bw.close();
                startCompress(tempFile.getAbsolutePath(),
                        file.getAbsolutePath(), new String(Base64
                                .encode(sss.getBytes(), Base64.URL_SAFE)));
                tempFile.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            return;
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
            //System.out.println("Datei erfolgreich eingelesen!");
        } catch (Exception e) {
            //System.err.println("Die Datei wurde nicht gefunden!");
            e.printStackTrace();
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
            //System.out.println("HuffmanTree erfolgreich aufgebaut!");
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
    private void compressFile(String sourcefile, String targetfile,
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

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
            decompTree.constructWayToLeaf(decompTree.codeWords.get(i),
                    decompTree.chars.get(i));
        }
    }

    /** Das Einlesen der Codetabelle funktioniert jetzt ! */
    public void decompressFile(String sourceFile, String targetFile) {

        HuffmanTree decompTree = new HuffmanTree();
        String codeword = "";
        int c;
        int highestChar;
        BitInputFile bif;
        try {
            bif = new BitInputFile(sourceFile);

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
                    // System.out.println("--- Hier endet die Codetabelle. ---");
                    bif.beginBitMode();
                    break;
                }
                blabla += 8;
            }

            rebuildTree(decompTree); // Huffmanbaum rekonstruieren
            // System.out.println("Tree rebuilded: " + "\n"
            // + decompTree.toString());
            // ab jetzt die Bitfolgen lesen und im baum ablaufen bis man in
            // einem blatt gelandet ist.
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(targetFile));
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

            }
            // System.out.println(bif.bitsLeft());
            bif.endBitMode();

            bif.close();
            bos.close();

        } catch (Exception e) {
            e.printStackTrace();
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
}