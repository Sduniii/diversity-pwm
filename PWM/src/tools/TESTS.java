package tools;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import tools.SHA.TypeToGiveBack;

public class TESTS {

	public static void main(String[] args) {
		System.out.println(MyBase64.encode("Hallo"));
		System.out.println(MyBase64.decode("SGFsbG8="));
		System.out.println(SHA.getHash("Hallo", "SHA-512", TypeToGiveBack.HEXSTRING));
		String en = AES.encode("aaaaa", "Hello WOrld")+ "|" + AES.encode("aaaaa", "Hello W") + "|" + AES.encode("aaaaa", "Hello");
		en = AES.encode("aaaaa", en);
		System.out.println(en);
		System.out.println();
		String de = AES.decode("aaaaa", en);
		System.out.println(de);
		String[] st = de.split("\\|");
		for(int i = 0; i < st.length; i++){
			System.out.println(AES.decode("aaaaa", st[i]));
		}
		System.out.println((char)111);
		

	}

}
