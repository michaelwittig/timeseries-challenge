package de.cinovo.timeseries.benchmark;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.RingFixedTimeWindow;
import de.cinovo.timeseries.impl.RingFixedTimeWindow.ExpandStrategy;

/**
 * 
 * @author mwittig
 * 
 */
public final class RingFixedTimeWindowBenchmarkSuite extends ABenchmarkSuite {
	
	/**
	 * @param args Arguments
	 */
	public static void main(final String[] args) {
		new RingFixedTimeWindowBenchmarkSuite().run();
	}
	
	@Override
	protected IFixedTimeWindow create(final long windowSize) {
		return new RingFixedTimeWindow(windowSize, 10000, ExpandStrategy.throwException);
	}
}
