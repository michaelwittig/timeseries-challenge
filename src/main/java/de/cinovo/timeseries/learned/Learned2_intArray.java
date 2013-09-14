package de.cinovo.timeseries.learned;

/**
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class Learned2_intArray {
	
	private static final int run(final int delta, final int[] list) {
		int v = delta;
		for (int i = 0; i < list.length; i++) {
			if (list[i] != -1) {
				v += 1;
			}
		}
		return v;
	}
	
	public static void main(final String[] args) {
		System.out.println("Learned2_intArray");
		System.gc();
		System.out.println("start...");
		final int[] list = new int[Learned2.ELEMENTS];
		for (int i = 0; i < Learned2.ELEMENTS; i++) {
			list[i] = i;
		}
		System.out.println("test " + (Learned2_intArray.run(0, list) == (Learned2.ELEMENTS - 1)));
		System.out.println("warmup...");
		for (int i = 0; i < Learned2.WARMUP_RUNS; i++) {
			Learned2_intArray.run(i, list);
		}
		System.out.println("warmup done");
		System.gc();
		System.out.println("measure...");
		final long begin = System.nanoTime();
		for (int i = 0; i < Learned2.MEASURE_RUNS; i++) {
			Learned2_intArray.run(i, list);
		}
		final long runtime = System.nanoTime() - begin;
		System.out.println("run (ns): " + runtime);
		System.out.println("run (ms): " + (runtime / 1000000));
	}
}
