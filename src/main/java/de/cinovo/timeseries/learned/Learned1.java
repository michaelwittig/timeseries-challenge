package de.cinovo.timeseries.learned;

/**
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class Learned1 {
	
	/** Elements in the list. */
	public static int ELEMENTS = 100;
	
	/** Runs to warmup JVM. */
	public static int WARMUP_RUNS = 1000000;
	
	/** Runs to measure. */
	public static int MEASURE_RUNS = 100000;
	
	
	public static void main(final String[] args) {
		Learned1_ArrayListIterator.main(args);
		Learned1_ArrayListGet.main(args);
		Learned1_ArrayDequeIterator.main(args);
		Learned1_IntegerArray.main(args);
		Learned1_intArray.main(args);
		System.out.println("all done");
	}
	
}
