package tools;

import java.util.Random;

	
public class TESTS {

	public static void main(String[] args) {
		
		float t = 15f;
		float x1 = t*100/24;
		System.out.println(x1);
		
//		int[] tn = new int[24];
//		for(int i = 0; i < tn.length; i++){
//			tn[i] = (int) (Math.random() * (100 - 1) + 1);
//		}
		int schnitt = 20;
//		for(int i : tn){
//			schnitt+=i;
//		}
		//schnitt/=24;
		float sP =  100/schnitt;
		float y = (sP*x1)/100;
		System.out.println(y);
		System.out.println(14f*100f/(24f*30f));

	}

}
