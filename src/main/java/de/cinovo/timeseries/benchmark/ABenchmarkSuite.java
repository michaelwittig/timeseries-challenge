package de.cinovo.timeseries.benchmark;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.benchmark.AFixedWindowBenchmark.Dataset;

/**
 * 
 * @author mwittig
 * 
 */
public abstract class ABenchmarkSuite {
	
	protected abstract IFixedTimeWindow create(final long windowSize);
	
	/**
	 * @param dataset Dataset
	 */
	public void run(final Dataset dataset) {
		this.run(1000l, dataset); // 1 sec
		this.run(10000l, dataset); // 10 sec
		this.run(30000l, dataset); // 30 sec
		this.run(60000l, dataset); // 1min
		this.run(600000l, dataset); // 10 min
	}
	
	/** */
	public void run() {
		this.run(Dataset.benchmark);
	}
	
	private void run(final long windowSize, final Dataset dataset) {
		new FirstBenchmark(this, dataset, windowSize).run();
		System.out.println("");
		new LastBenchmark(this, dataset, windowSize).run();
		System.out.println("");
		new AverageBenchmark(this, dataset, windowSize).run();
		System.out.println("");
		new MinimumBenchmark(this, dataset, windowSize).run();
		System.out.println("");
		new MaximumBenchmark(this, dataset, windowSize).run();
		System.out.println("");
		new VarianceBenchmark(this, dataset, windowSize).run();
		System.out.println("");
		new DeviationBenchmark(this, dataset, windowSize).run();
		System.out.println("");
		new MedianBenchmark(this, dataset, windowSize).run();
		System.out.println("");
	}
}
