package tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	public static void write(String str){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(System.getProperty("user.dir")+System.getProperty("file.separator")+"log.txt",
					true));
			bw.write(new SimpleDateFormat( "dd. MMM yyyy HH:mm:ss" ).format(new Date()) + " | " + str);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
