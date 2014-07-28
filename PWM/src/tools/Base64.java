package tools;

public class Base64 {
	public static void main(String[] args){
		System.out.println(encode("Tobias Sdun"));
	}

	public static String encode(String mes){
		String[] b64 = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "/", "="};
		
		return null;
	}
	
	private static boolean isSet(byte value, int bit){
		   return (value&(1<<bit))!=0;
	} 
}
