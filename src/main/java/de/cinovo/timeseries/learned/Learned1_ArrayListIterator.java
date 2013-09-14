package de.cinovo.timeseries.learned;

import java.util.ArrayList;

/**
 * runtime: 9827 ms
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class Learned1_ArrayListIterator {
	
	private static final int run(final int delta, final ArrayList<Integer> list) {
		int v = delta;
		for (final Integer l : list) {
			v += l; // autoboxing happens here
		}
		return v;
	}
	
	public static void main(final String[] args) {
		System.out.println("Learned1_ArrayListIterator");
		System.gc();
		System.out.println("start...");
		final ArrayList<Integer> list = new ArrayList<>(Learned1.ELEMENTS);
		for (int i = 0; i < Learned1.ELEMENTS; i++) {
			list.add(i);
		}
		System.out.println("test " + (Learned1_ArrayListIterator.run(0, list) == Learned1.SUM_ELEMENTS));
		System.out.println("warmup...");
		for (int i = 0; i < Learned1.WARMUP_RUNS; i++) {
			Learned1_ArrayListIterator.run(i, list);
		}
		System.out.println("warmup done");
		System.gc();
		System.out.println("measure...");
		final long begin = System.nanoTime();
		for (int i = 0; i < Learned1.MEASURE_RUNS; i++) {
			Learned1_ArrayListIterator.run(i, list);
		}
		final long runtime = System.nanoTime() - begin;
		System.out.println("run (ns): " + runtime);
		System.out.println("run (ms): " + (runtime / 1000000));
	}
}
