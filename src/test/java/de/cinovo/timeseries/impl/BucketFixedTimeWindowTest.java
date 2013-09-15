package de.cinovo.timeseries.impl;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.RingFixedTimeWindow.ExpandStrategy;
import de.cinovo.timeseries.test.AFixedTimeWindowTest;

/**
 * 
 * @author mwittig
 * 
 */
public final class BucketFixedTimeWindowTest extends AFixedTimeWindowTest {
	
	@Override
	protected IFixedTimeWindow create() {
		return new BucketFixedTimeWindow(1000l, 100, 10000, ExpandStrategy.throwException);
	}
	
}
