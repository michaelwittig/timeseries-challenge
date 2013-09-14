package de.cinovo.timeseries.learned;

import java.util.ArrayDeque;

/**
 * runtime: 19732 ms
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class Learned1_ArrayDequeIterator {
	
	private static final Integer run(final int delta, final ArrayDeque<Integer> list) {
		Integer v = delta;
		for (final Integer l : list) {
			v += l;
		}
		return v;
	}
	
	public static void main(final String[] args) {
		System.out.println("Learned1_ArrayDequeIterator");
		System.gc();
		System.out.println("start...");
		final ArrayDeque<Integer> list = new ArrayDeque<>(Learned1.ELEMENTS);
		for (int i = 0; i < Learned1.ELEMENTS; i++) {
			list.add(i);
		}
		System.out.println("test " + (Learned1_ArrayDequeIterator.run(0, list) == Learned1.SUM_ELEMENTS));
		System.out.println("warmup...");
		for (int i = 0; i < Learned1.WARMUP_RUNS; i++) {
			Learned1_ArrayDequeIterator.run(i, list);
		}
		System.out.println("warmup done");
		System.gc();
		System.out.println("measure...");
		final long begin = System.nanoTime();
		for (int i = 0; i < Learned1.MEASURE_RUNS; i++) {
			Learned1_ArrayDequeIterator.run(i, list);
		}
		final long runtime = System.nanoTime() - begin;
		System.out.println("run (ns): " + runtime);
		System.out.println("run (ms): " + (runtime / 1000000));
	}
}
