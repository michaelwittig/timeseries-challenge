package de.cinovo.timeseries.benchmark;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.BucketFixedTimeWindow;
import de.cinovo.timeseries.impl.RingFixedTimeWindow.ExpandStrategy;

/**
 * 
 * @author mwittig
 * 
 */
public final class BucketFixedTimeWindowBenchmarkSuite extends ABenchmarkSuite {
	
	/**
	 * @param args Arguments
	 */
	public static void main(final String[] args) {
		new BucketFixedTimeWindowBenchmarkSuite().run();
	}
	
	@Override
	protected IFixedTimeWindow create(final long windowSize) {
		return new BucketFixedTimeWindow(windowSize, 100, 10000, ExpandStrategy.throwException);
	}
}
