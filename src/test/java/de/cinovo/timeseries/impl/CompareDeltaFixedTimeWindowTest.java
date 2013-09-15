package de.cinovo.timeseries.impl;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.test.ACompareWithSimpleFixedTimeWindowTest;

/**
 * 
 * @author mwittig
 * 
 */
public final class CompareDeltaFixedTimeWindowTest extends ACompareWithSimpleFixedTimeWindowTest {
	
	@Override
	protected IFixedTimeWindow create2(long windowLength) {
		return new DeltaFixedTimeWindow3(windowLength);
	}
	
}
