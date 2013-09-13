package de.cinovo.timeseries.benchmark;

import de.cinovo.timeseries.IFixedTimeWindow;

/**
 * 
 * @author mwittig
 * 
 */
public abstract class ABenchmarkSuite {
	
	protected abstract IFixedTimeWindow create(final long windowSize);
	
	/** */
	public void run() {
		this.run(1000l); // 1 sec
		this.run(10000l); // 10 sec
		this.run(30000l); // 30 sec
		this.run(60000l); // 1min
		this.run(600000l); // 10 min
	}
	
	private void run(final long windowSize) {
		new FirstBenchmark(this, windowSize).run();
		System.out.println("");
		new LastBenchmark(this, windowSize).run();
		System.out.println("");
		new AverageBenchmark(this, windowSize).run();
		System.out.println("");
		new MinimumBenchmark(this, windowSize).run();
		System.out.println("");
		new MaximumBenchmark(this, windowSize).run();
		System.out.println("");
		new VarianceBenchmark(this, windowSize).run();
		System.out.println("");
		new DeviationBenchmark(this, windowSize).run();
		System.out.println("");
		new MedianBenchmark(this, windowSize).run();
		System.out.println("");
	}
}
