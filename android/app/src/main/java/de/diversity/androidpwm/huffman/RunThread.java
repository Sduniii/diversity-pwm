package de.diversity.androidpwm.huffman;

public class RunThread extends Thread {

	boolean finished;
	int i;

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public RunThread(boolean finished) {
		this.finished = finished;
		i = 0;
	}

	public void run() {
		while (!finished) {
			if (i <= 20) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.print(".");
				i++;
			}else{
				System.out.println();
				i = 0;
			}
		}
		System.out.println();
	}

}
