package de.cinovo.timeseries.learned;

/**
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class Learned2 {
	
	/** Elements in the list. */
	public static int ELEMENTS = 100000;
	
	/** Runs to warmup JVM. */
	public static int WARMUP_RUNS = 1000000;
	
	/** Runs to measure. */
	public static int MEASURE_RUNS = 100000;
	
	
	public final static void main(final String[] args) {
		Learned2_IntegerArray.main(args);
		Learned2_intArray.main(args);
		System.out.println("all done");
	}
	
}
