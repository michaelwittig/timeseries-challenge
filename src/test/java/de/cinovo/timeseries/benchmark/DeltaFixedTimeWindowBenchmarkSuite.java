package de.cinovo.timeseries.benchmark;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.DeltaFixedTimeWindow;

/**
 * 
 * @author mwittig
 * 
 */
public final class DeltaFixedTimeWindowBenchmarkSuite extends ABenchmarkSuite {
	
	/**
	 * @param args Arguments
	 * @throws Exception If something went wrong...
	 */
	public static void main(final String[] args) throws Exception {
		new DeltaFixedTimeWindowBenchmarkSuite().run();
	}
	
	@Override
	protected IFixedTimeWindow create(final long windowSize) {
		return new DeltaFixedTimeWindow(windowSize);
	}
}
