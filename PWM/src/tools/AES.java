package tools;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {

	public static void main(String[] args) throws Exception {
		 
	       
	      // Das Passwort bzw der Schluesseltext
	      String keyStr = "geheiadadasdfasfm";
	      // byte-Array erzeugen
	      byte[] key = (keyStr).getBytes("UTF-8");
//	      // aus dem Array einen Hash-Wert erzeugen mit MD5 oder SHA
	      MessageDigest sha = MessageDigest.getInstance("SHA-256");
	      key = sha.digest(key);
	      System.out.println(toHex(key));
//	      // nur die ersten 128 bit nutzen
	      key = Arrays.copyOf(key, 16); 
	      // der fertige Schluessel
	      SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
	       
	 
	      // der zu verschl. Text
	      String text = "Das ist der Text";
	 
	      // Verschluesseln
	      Cipher cipher = Cipher.getInstance("AES");
	      cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
	      byte[] encrypted = cipher.doFinal(text.getBytes());
	 
	      String eeee = "";
	      // Ergebnis
	      for(byte b : encrypted){
	    	  eeee += Byte.toString(b);
	      }
	      System.out.println(eeee);
	      // bytes zu Base64-String konvertieren (dient der Lesbarkeit)
	      byte[] geheim = Base64.getEncoder().encode((encrypted));
	 
	      eeee = "";
	      // Ergebnis
	      for(byte b : geheim){
	    	  eeee += Byte.toString(b);
	      }
	      System.out.println(eeee);
	       
	      // BASE64 String zu Byte-Array konvertieren
	      byte[] crypted2 = Base64.getDecoder().decode((geheim));
	 
	      // Entschluesseln
	      Cipher cipher2 = Cipher.getInstance("AES");
	      cipher2.init(Cipher.DECRYPT_MODE, secretKeySpec);
	      byte[] cipherData2 = cipher2.doFinal(crypted2);
	      String erg = new String(cipherData2);
	 
	      // Klartext
	      System.out.println(erg);
	 
	   }
	
	private static String toHex(byte[] byt){
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byt.length; i++) {
          sb.append(Integer.toString((byt[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
}
