package de.cinovo.timeseries.benchmark;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.SimpleFixedTimeWindow;

/**
 * 
 * @author mwittig
 * 
 */
public final class SimpleFixedTimeWindowBenchmarkSuite extends ABenchmarkSuite {
	
	/**
	 * @param args Arguments
	 * @throws Exception If something went wrong...
	 */
	public static void main(final String[] args) throws Exception {
		new SimpleFixedTimeWindowBenchmarkSuite().run();
	}
	
	@Override
	protected IFixedTimeWindow create(final long windowSize) {
		return new SimpleFixedTimeWindow(windowSize);
	}
	
}
