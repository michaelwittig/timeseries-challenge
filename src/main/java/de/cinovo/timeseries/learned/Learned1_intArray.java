package de.cinovo.timeseries.learned;

/**
 * runtime: 5 ms
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class Learned1_intArray {
	
	private static final int run(final int[] list) {
		int v = -1;
		for (int i = 0; i < list.length; i++) {
			v = list[i];
		}
		return v;
	}
	
	public static void main(final String[] args) {
		System.out.println("Learned1_intArray");
		System.gc();
		System.out.println("start...");
		final int[] list = new int[Learned1.ELEMENTS];
		for (int i = 0; i < Learned1.ELEMENTS; i++) {
			list[i] = i;
		}
		System.out.println("test " + (Learned1_intArray.run(list) == (Learned1.ELEMENTS - 1)));
		System.out.println("warmup...");
		for (int i = 0; i < Learned1.WARMUP_RUNS; i++) {
			Learned1_intArray.run(list);
		}
		System.out.println("warmup done");
		System.gc();
		System.out.println("measure...");
		final long begin = System.nanoTime();
		for (int i = 0; i < Learned1.MEASURE_RUNS; i++) {
			Learned1_intArray.run(list);
		}
		final long runtime = System.nanoTime() - begin;
		System.out.println("run (ns): " + runtime);
		System.out.println("run (ms): " + (runtime / 1000000));
	}
}
