package de.cinovo.timeseries.benchmark;

import de.cinovo.timeseries.ITimeSeries;

/**
 * 
 * @author mwittig
 * 
 */
public final class FirstBenchmark extends AFixedWindowBenchmark {
	
	/**
	 * @param aBenchmarkSuite Benchmark suite
	 * @param aDataset Dataset
	 * @param aWindowSize Window size
	 */
	public FirstBenchmark(final ABenchmarkSuite aBenchmarkSuite, final Dataset aDataset, final long aWindowSize) {
		super(aBenchmarkSuite, aDataset, aWindowSize);
	}
	
	@Override
	protected void call(final ITimeSeries series) {
		series.first();
	}
	
}
