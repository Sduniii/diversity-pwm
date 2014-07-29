package tools;

import tools.SHA.TypeToGiveBack;

public class TESTS {

	public static void main(String[] args) {
		System.out.println(MyBase64.encode("Hallo"));
		System.out.println(MyBase64.decode("SGFsbG8="));
		System.out.println(SHA.getHash("Hallo", "SHA-512", TypeToGiveBack.HEXSTRING));
		System.out.println(AES.encode("Hello Worl", "Hello WOrld"));
		System.out.println(AES.decode("Hello Worl", "/prG1sacfy9BVfaVYyeL8A=="));

	}

}
