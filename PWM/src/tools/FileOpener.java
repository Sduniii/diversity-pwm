package tools;

import java.awt.FileDialog;
import java.awt.Frame;

import exceptions.ImportFileNotFoundException;

public class FileOpener {

	public static String openFile(String[] fileExt, Frame comp) throws ImportFileNotFoundException{
		FileDialog fd = new FileDialog(comp, "Choose a file", FileDialog.LOAD);
		fd.setDirectory("D:\\");
		String extens = "";
		for(int i = 0; i < fileExt.length; i++){
			if(i>0) extens += ";";
			extens += "*." + fileExt[i];
		}
		fd.setFile(extens);
		fd.setVisible(true);
		return fd.getDirectory() + fd.getFile();
	}
}
