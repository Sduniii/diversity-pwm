package tools;


public class MyBase64 {

	public static String encode(String mes){
		String[] b64 = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "/", "="};
		byte[] bytes = new byte[mes.length()];
		for(int i =0 ; i<mes.length();i++){
			//convert char to byte, ex: o=111 in ascii
			bytes[i] = (byte)mes.charAt(i);
		}
		boolean[][] bits = new boolean[bytes.length+bytes.length%3][8];
		for(int j = 0; j< bytes.length ; j++){
			int w = 7;
			while(w >= 0){
				if(bytes[j]%2 == 0) bits[j][w] = false;else bits[j][w] = true;
				bytes[j] = (byte) (bytes[j]/2);
				//System.out.println(bits[j][w]);
				w--;
			}
		}
		int addedZeros = 0;
		if(bytes.length % 3 != 0){
			if(bytes.length % 3 == 1){
				for(int n = 0; n<8; n++){
					bits[bytes.length+1][n] = false;
					bits[bytes.length+2][n] = false;
					addedZeros = 2;
				}
			}else if(bytes.length % 3 == 2){
				for(int n = 0; n<8; n++){
					bits[bytes.length+1][n] = false;
					addedZeros = 1;
				}
			}
		}
		String st = "";
		for(int i = 0; i<bits.length ; i++){
			for(int j = 0; j<bits[0].length; j++){
				if(bits[i][j]) st+="1"; else st+="0";
			}
		}
		//System.out.println(st);
		String out = "";
		for(int i = 0; i < st.length()/6-addedZeros-1;i++){
			int m = 0;
			for(int j = 0; j < 6; j++){
				if(st.charAt((i*6)+j) == '1'){
					m += Math.pow(2, 5-j);
					//System.out.println("m: " +((i*6)+j));
				}
			}
			//System.out.println(m);
			out += b64[m];
		}
		for(int i = 0; i < addedZeros; i++){
			out += "=";
		}
		return out;
	}
	
	public static String decode(String mes){
		String[] b64 = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "+", "/", "="};
		int[] sixBytes = new int[mes.length()];
		for(int i = 0; i<mes.length(); i++){
			for(int j = 0; j<b64.length; j++){
				if(mes.charAt(i) == b64[j].charAt(0)) sixBytes[i] = j;
			}
		}
		
		return "";
		
	}
}
