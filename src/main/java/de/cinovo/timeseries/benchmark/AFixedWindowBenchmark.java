package de.cinovo.timeseries.benchmark;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.ITimeSeries;
import de.cinovo.timeseries.ITimeSeriesPair;
import de.cinovo.timeseries.impl.TimeSeriesPair;

/**
 * 
 * @author mwittig
 * 
 */
public abstract class AFixedWindowBenchmark {
	
	private static final int WARM_UP_CYCLES = 100000;
	
	private static final int MEASURE_CYCLES = 10000;
	
	private static final List<ITimeSeriesPair> pairs;
	
	private final String comment;
	
	private final ABenchmarkSuite benchmarkSuite;
	
	private final long windowSize;
	
	static {
		try (final BufferedReader br = new BufferedReader(new InputStreamReader(AFixedWindowBenchmark.class.getClassLoader().getResourceAsStream("de/cinovo/timeseries/benchmark/benchmark.data")))) {
			String line;
			final ArrayList<ITimeSeriesPair> aPairs = new ArrayList<ITimeSeriesPair>();
			while ((line = br.readLine()) != null) {
				final String[] s = line.split(",");
				final long time = Long.parseLong(s[0]);
				final float value = Float.parseFloat(s[1]);
				aPairs.add(new TimeSeriesPair(time, value));
			}
			pairs = aPairs;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Create random benchmark data.
	 */
	public static void createRandomBenchmarkData() {
		long time = 1378591663329l;
		float value = 123.45f;
		for (int i = 0; i < 100000; i++) {
			System.out.println(time + "," + value);
			time += (long) (Math.random() * 1000l) + 1l;
			if (Math.random() < 0.5d) {
				value += 0.01f;
			} else {
				value -= 0.01f;
			}
			value = Math.round(value * 100.0f) / 100.0f;
		}
	}
	
	/**
	 * @param aBenchmarkSuite Benchmark suite
	 * @param aWindowSize Window size
	 * @param aComment Comment
	 * @throws Exception If something went wrong...
	 */
	protected AFixedWindowBenchmark(final ABenchmarkSuite aBenchmarkSuite, final long aWindowSize, final String aComment) {
		this.comment = aComment;
		this.benchmarkSuite = aBenchmarkSuite;
		this.windowSize = aWindowSize;
	}
	
	/**
	 * @param aBenchmarkSuite Benchmark suite
	 * @param aWindowSize Window size
	 * @throws Exception If something went wrong...
	 */
	protected AFixedWindowBenchmark(final ABenchmarkSuite aBenchmarkSuite, final long aWindowSize) {
		this(aBenchmarkSuite, aWindowSize, "window: " + aWindowSize);
	}
	
	private void warmUp() {
		System.gc();
		for (int i = 0; i < AFixedWindowBenchmark.WARM_UP_CYCLES; i++) {
			final IFixedTimeWindow impl = this.create();
			for (final ITimeSeriesPair pair : AFixedWindowBenchmark.pairs) {
				this.call(impl, pair);
			}
		}
		System.gc();
	}
	
	private void call(final IFixedTimeWindow impl, final ITimeSeriesPair pair) {
		impl.add(pair.time(), pair.value());
		this.call(impl.get(pair.time()));
		this.call(impl.get(pair.time()));
		this.call(impl.get(pair.time()));
		this.call(impl.get(pair.time()));
		this.call(impl.get(pair.time()));
	}
	
	private long[] measure() {
		final long[] runtimes = new long[AFixedWindowBenchmark.MEASURE_CYCLES];
		for (int i = 0; i < AFixedWindowBenchmark.MEASURE_CYCLES; i++) {
			final IFixedTimeWindow impl = this.create();
			final long begin = System.nanoTime();
			for (final ITimeSeriesPair pair : AFixedWindowBenchmark.pairs) {
				this.call(impl, pair);
			}
			final long end = System.nanoTime();
			runtimes[i] = end - begin;
		}
		return runtimes;
	}
	
	private static void print(final String name, final long aNanos) {
		final double nanos = aNanos;
		final double micros = nanos / 1000;
		final double millis = micros / 1000;
		System.out.printf("  %s %20.2f %20.2f %20.2f%n", name, nanos, micros, millis);
	}
	
	private void report(final long[] runtimes) {
		final IFixedTimeWindow impl = this.create();
		System.out.println(impl.getClass().getSimpleName() + "." + this.getClass().getSimpleName() + " (" + this.comment + ", runs: " + runtimes.length + "; calls per run: " + AFixedWindowBenchmark.pairs.size() + ")");
		System.out.println("                     nanos               micros               millis");
		long runtimeSum = 0l;
		long runtimeMax = Long.MIN_VALUE;
		long runtimeMin = Long.MAX_VALUE;
		for (final long runtime : runtimes) {
			runtimeSum += runtime;
			if (runtime < runtimeMin) {
				runtimeMin = runtime;
			}
			if (runtime > runtimeMax) {
				runtimeMax = runtime;
			}
		}
		final long runtimeAvg = runtimeSum / runtimes.length;
		AFixedWindowBenchmark.print("Avg", runtimeAvg);
		AFixedWindowBenchmark.print("Min", runtimeMin);
		AFixedWindowBenchmark.print("Max", runtimeMax);
	}
	
	/**
	 * Run benchmark.
	 */
	public void run() {
		this.warmUp();
		final long[] runtimes = this.measure();
		this.report(runtimes);
	}
	
	protected final IFixedTimeWindow create() {
		return this.benchmarkSuite.create(this.windowSize);
	}
	
	protected abstract void call(final ITimeSeries series);
	
}
