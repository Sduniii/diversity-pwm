package huffman;

import java.util.Random;

public class HeapSort {

	public static void heapSort(int[] array) {

		Heap<Integer> heap = new Heap<Integer>(array[0]);
		for (int j = 1; j < array.length; j++) {
			heap.insert(array[j]);
		}
		System.out.println(heap.toString());

		int[] sortArray = new int[array.length];

		for (int i = 0; i < array.length; i++) {
			sortArray[i] = heap.extractMin();
			System.out.print(sortArray[i]);
		}

	}

	// private String

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] array;
		Random rnd = new Random();
		array = new int[10];
		for(int i = 0; i < array.length; i++) {
			array[i] = rnd.nextInt(50); 
		}
		heapSort(array);
	}

}
