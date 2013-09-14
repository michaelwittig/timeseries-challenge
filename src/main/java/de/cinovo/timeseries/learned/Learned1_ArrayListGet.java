package de.cinovo.timeseries.learned;

import java.util.ArrayList;

/**
 * runtime: 10663 ms
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class Learned1_ArrayListGet {
	
	private static final Integer run(final ArrayList<Integer> list) {
		Integer v = null;
		for (int i = 0; i < list.size(); i++) {
			v = list.get(i);
		}
		return v;
	}
	
	public static void main(final String[] args) {
		System.out.println("Learned1_ArrayListGet");
		System.gc();
		System.out.println("start...");
		final ArrayList<Integer> list = new ArrayList<>(Learned1.ELEMENTS);
		for (int i = 0; i < Learned1.ELEMENTS; i++) {
			list.add(i);
		}
		System.out.println("test " + (Learned1_ArrayListGet.run(list) == (Learned1.ELEMENTS - 1)));
		System.out.println("warmup...");
		for (int i = 0; i < Learned1.WARMUP_RUNS; i++) {
			Learned1_ArrayListGet.run(list);
		}
		System.out.println("warmup done");
		System.gc();
		System.out.println("measure...");
		final long begin = System.nanoTime();
		for (int i = 0; i < Learned1.MEASURE_RUNS; i++) {
			Learned1_ArrayListGet.run(list);
		}
		final long runtime = System.nanoTime() - begin;
		System.out.println("run (ns): " + runtime);
		System.out.println("run (ms): " + (runtime / 1000000));
	}
}
