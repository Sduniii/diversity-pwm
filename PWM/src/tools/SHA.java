package tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Tobias Sdun
 * @version 1.0
 */
public class SHA {
	
	/** 
     * Calculate the a hash of a given String
     *
     * @param mes the String to hash
     * @param alg what algorithm should be used for hashing (SHA-512,MD5,...)
     * @param hexHash should give it back a hex string?
     * 
     * @return get back a String as hash
     */
	public static String getHash(String mes, String alg, boolean hexHash) {
		try {
			MessageDigest md = MessageDigest.getInstance(alg);
			md.update(mes.getBytes());
			if(hexHash){
				return hexHash(md.digest());
			}else{
				return new String(md.digest());
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String hexHash(byte[] mb) {
		String out = "";
		for (int i = 0; i < mb.length; i++) {
			byte temp = mb[i];
			String s = Integer.toHexString(new Byte(temp));
			while (s.length() < 2) {
				s = "0" + s;
			}
			s = s.substring(s.length() - 2);
			out += s;
		}
		return out;
	}
}
