package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

public class ExportWriter {

    public static boolean writeHtml(LinkedList<String> list, File file) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            Iterator<String> li = list.iterator();
            int i = 0;
            String newLine = System.getProperty("line.separator");
            String html = "<html>" + newLine + "<head>" + newLine
                    + "<title>diversityPWM Export</title>" + newLine
                    + "</head>" + newLine + "<body style=\"\">" + newLine + "<table id=\"tbl-pwm\" style=\"border: 1px solid black;\">"
                    + newLine + "<tr>" + newLine;
            bw.write(html);
            while (li.hasNext()) {
                if (i == 3) {
                    bw.write("</tr>" + newLine + "<tr>" + newLine);
                    i = 0;
                }
                if (i < 2) {
                    bw.write("<td style=\"border-right: 1px solid black;\">" + li.next() + "</td>" + newLine);
                } else {
                    bw.write("<td>" + li.next() + "</td>" + newLine);
                }
                i++;

            }

            bw.write("</tr>" + newLine + "</table>" + newLine + "</body>" + newLine + "</html>");
            bw.close();
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    public static boolean writeCSV(LinkedList<LinkedList<String>> list, File file) {
        // "Sample Entry 1","tom@example.com","12345","https://keepass.info/","Notes"
        list.forEach(strings -> System.out.println(Arrays.toString(strings.toArray())));
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String newLine = System.getProperty("line.separator");
            bw.write("\"Account\",\"Login Name\",\"Password\"");
            bw.write(newLine);
            for (LinkedList<String> strings : list) {
                for (String string : strings) {
                    if (strings.getLast() == string) {
                        bw.write("\""+ string+"\"");
                    }else {
                        bw.write("\""+ string+"\",");
                    }
                }
                bw.write(newLine);
            }
            bw.close();
            return true;
        } catch (Exception ex) {
            return false;
        }

    }
}