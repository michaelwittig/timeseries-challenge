package de.cinovo.timeseries.impl;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.SimpleFixedTimeWindow;
import de.cinovo.timeseries.test.AFixedTimeWindowTest;

/**
 * 
 * @author mwittig
 * 
 */
public final class SimpleFixedTimeWindowTest extends AFixedTimeWindowTest {
	
	@Override
	protected IFixedTimeWindow create() {
		return new SimpleFixedTimeWindow(1000l);
	}
	
}
