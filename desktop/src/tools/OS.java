package tools;

import javax.swing.*;

/**
 * Created by Tbaios on 28.07.2016.
 */
public class OS {

    public static void setLockAndFeel() {
        String OS = System.getProperty("os.name").toLowerCase();
        try {
            if (OS.contains("mac") || OS.contains("linux")) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } else {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            Log.write(e);
        }
    }
}
