package de.cinovo.timeseries.impl;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.RingFixedTimeWindow.ExpandStrategy;
import de.cinovo.timeseries.test.ACompareWithSimpleFixedTimeWindowTest;

/**
 * 
 * @author mwittig
 * 
 */
public final class CompareBucketFixedTimeWindowTest extends ACompareWithSimpleFixedTimeWindowTest {
	
	@Override
	protected IFixedTimeWindow create2(long windowLength) {
		return new BucketFixedTimeWindow(windowLength, 100, 10000, ExpandStrategy.throwException);
	}
	
}
