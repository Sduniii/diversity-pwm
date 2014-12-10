package huffman;

/** 
 * A Huffman token
 * @author co2-128
 *
 */
public class HuffmanToken {
	
	/** Data field for the char c that will be saved in the HuffmanToken. */
	protected int c;

	/** Data field for the frequency of a char. c */
	protected int freq;
	
	/** Constructs a Huffmantoken with given data.
	 * @param c is the current char
	 * @param freq is the frequency of the current char
	 */
	public HuffmanToken(int c, int freq){
		
		this.c = c;
		this.freq = freq;
	}
	
	public HuffmanToken(int cha) {
		this.c = cha;
		this.freq = 0;
	}
	
	public HuffmanToken() {
		this.c = -1;
		this.freq = 0;
	}
	
	public int getFreq(){
		return this.freq;
	}
	
	public void setFreq(int freq){
		this.freq = freq;
	}
	/**
	 * @return the c
	 */
	public int getC() {
		return c;
	}
	public void setC(int cha){
		this.c = cha;
	}
}
