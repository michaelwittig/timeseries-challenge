package de.cinovo.timeseries.learned;

/**
 * runtime: 4 ms
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class Learned1_IntegerArray {
	
	private static final Integer run(final Integer[] list) {
		Integer v = null;
		for (int i = 0; i < list.length; i++) {
			v = list[i];
		}
		return v;
	}
	
	public static void main(final String[] args) {
		System.out.println("Learned1_IntegerArray");
		System.gc();
		System.out.println("start...");
		final Integer[] list = new Integer[Learned1.ELEMENTS];
		for (int i = 0; i < Learned1.ELEMENTS; i++) {
			list[i] = i;
		}
		System.out.println("test " + (Learned1_IntegerArray.run(list) == (Learned1.ELEMENTS - 1)));
		System.out.println("warmup...");
		for (int i = 0; i < Learned1.WARMUP_RUNS; i++) {
			Learned1_IntegerArray.run(list);
		}
		System.out.println("warmup done");
		System.gc();
		System.out.println("measure...");
		final long begin = System.nanoTime();
		for (int i = 0; i < Learned1.MEASURE_RUNS; i++) {
			Learned1_IntegerArray.run(list);
		}
		final long runtime = System.nanoTime() - begin;
		System.out.println("run (ns): " + runtime);
		System.out.println("run (ms): " + (runtime / 1000000));
	}
}
