package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Log {
	
	public static void write(Exception ex){
		
		try {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			String s = writer.toString();
			String OS = System.getProperty("os.name").toLowerCase();
			File logFile;
			if(OS.contains("mac") || OS.contains("linux")){
				logFile = new File(System.getProperty("user.home") + "/Library/" + "de.diversity.pwm/log.txt");
			}else{
				logFile = new File(System.getProperty("user.dir")
						+ System.getProperty("file.separator") + "log.txt");
			}
			if(!logFile.exists()){
				logFile.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(logFile,
					true));
			bw.write(new SimpleDateFormat( "dd. MMM yyyy HH:mm:ss" ).format(new Date()) + " " + s);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
