package tools;

public class Base64 {
	public static void main(String[] args){
		System.out.println(encode("Hallo"));
	}

	public static String encode(String mes){
		String[] b64 = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "/", "="};
		byte[] bytes = new byte[mes.length()];
		for(int i =0 ; i<mes.length();i++){
			//convert char to byte, ex: o=111 in ascii
			bytes[i] = (byte)mes.charAt(i);
		}
		String str = "";
		for(int j = 0; j< bytes.length ; j++){
			boolean isR = false;
			while(!isR){
				if(bytes[j]%2 == 0) str+="0";
				else str+="1";
				bytes[j] = (byte) (bytes[j]/2);
				if(bytes[j] == 0) isR = true;
			}
		}
		String st = "";
		for(int m = 0; m<str.length();m++){
			if(m%6==0 && m!=0) st += "|";
			st += str.charAt(m);
		}
		return st;
	}
	
	private static boolean isSet(byte value, int bit){
		   return (value&(1<<bit))!=0;
	} 
}
