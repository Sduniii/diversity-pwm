package huffman;

/**
 * A priority queue is a datastructure that allows to extract the minimum (or
 * maximum) element. It supports insertion of new elements and the extraction of
 * the minimum element (which includes its removal).
 * 
 * @param <T>
 *            the type of data that can be stored in the priority queue
 * @author CoMaTeam
 */
public interface PriorityQueue<T extends Comparable<T>> {
	/**
	 * Inserts new data into the priority queue.
	 * 
	 * @param data
	 *            the new data
	 */
	void insert(T data);

	/**
	 * Returns the minimum element of the queue without removing it.
	 * 
	 * @return the minimum element of the queue
	 */
	T peek();

	/**
	 * Returns the minimum element of the queue and removes it afterwards.
	 * 
	 * @return the minimum element of the queue
	 */
	T extractMin();
}
