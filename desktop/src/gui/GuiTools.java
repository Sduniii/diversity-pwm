package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import tools.AES;
import tools.HardwareIDs;
import tools.Log;

public class GuiTools {

	public static HashMap<String, Object> readOptionFile(File optionFile) {
		HashMap<String, Object> map = new HashMap<>();
		if (optionFile.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(optionFile));
				String li = br.readLine();
				while (li != null) {
					if (li.contains("F||")) {
						map.put("file", new File(li.replace("F||", "")));
					} else if (li.contains("P||")) {
						map.put("password", AES.decode(HardwareIDs.getSerial(), li.replace("P||", "")));
					}
					li = br.readLine();
				}
				br.close();
				return map;
			} catch (IOException e) {
				Log.write(e);
				return map;
			}
		} else {
			return map;
		}
	}
}
