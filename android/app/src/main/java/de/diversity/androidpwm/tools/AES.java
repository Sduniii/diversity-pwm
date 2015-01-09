package de.diversity.androidpwm.tools;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import de.diversity.androidpwm.tools.SHA.TypeToGiveBack;

public class AES {
	public static String encode(String pass, String mes) {
		try {
			byte[] key = (pass).getBytes("UTF-8");

			key = (byte[]) SHA.getHash(pass, "SHA-512",
					TypeToGiveBack.BYTEARRAY);

			key = Arrays.copyOf(key, 16);

			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encrypted = cipher.doFinal(mes.getBytes());

			return Base64.encodeToString(encrypted,Base64.URL_SAFE);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String decode(String pass, String geheim) {
		try {
			byte[] key;

			key = (pass).getBytes("UTF-8");

			key = (byte[]) SHA.getHash(pass, "SHA-512",
					TypeToGiveBack.BYTEARRAY);

			key = Arrays.copyOf(key, 16);

			SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
			byte[] crypted2 = geheim.getBytes();

			Cipher cipher2;

			cipher2 = Cipher.getInstance("AES/ECB/PKCS5PADDING");

			cipher2.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] cipherData2 = cipher2.doFinal(Base64.decode(crypted2,Base64.URL_SAFE));

			return new String(cipherData2);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | UnsupportedEncodingException e) {
			return null;
		}

	}
}