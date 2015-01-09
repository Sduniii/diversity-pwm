package de.diversity.androidpwm.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Tobias Sdun
 * @version 1.0
 */
public class SHA {
	
	public static enum TypeToGiveBack {BYTEARRAY, STRING, HEXSTRING};
	
	/** 
     * Calculate the a hash of a given String
     *
     * @param mes the String to hash
     * @param alg what algorithm should be used for hashing (SHA-512,MD5,...)
     * @param back which format should give it back?
     * 
     * @return get back a Object as hash
     */
	public static Object getHash(String mes, String alg, TypeToGiveBack back) {
		try {
			MessageDigest md = MessageDigest.getInstance(alg);
			md.update(mes.getBytes());
			if(back == TypeToGiveBack.HEXSTRING){
				return hexHash(md.digest());
			}else if(back == TypeToGiveBack.STRING){
				return new String(md.digest());
			}else if(back == TypeToGiveBack.BYTEARRAY){
				return md.digest();
			}else{
				return null;
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
