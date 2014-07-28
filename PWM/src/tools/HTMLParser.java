package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class HTMLParser {
	
	public static LinkedList<String> parse(String urlS){
		LinkedList<String> list = new LinkedList<String>();
		try {
		    // Create a URL for the desired page
		    URL url = new URL("file:///" + urlS);
		    
		    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		    String str;

		    while ((str = in.readLine()) != null) {
		    	if(str.contains("<td>")){
		    		String tS = str.substring(str.indexOf("<td>")+4, str.indexOf("</td>"));
		    		if(!tS.contains("<b>")){
		    			list.add(str.substring(str.indexOf("<td>")+4, str.indexOf("</td>")));
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
