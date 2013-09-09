package de.cinovo.timeseries.impl;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.DeltaFixedTimeWindow;
import de.cinovo.timeseries.test.AFixedTimeWindowTest;

/**
 * 
 * @author mwittig
 * 
 */
public final class DeltaFixedTimeWindowTest extends AFixedTimeWindowTest {
	
	@Override
	protected IFixedTimeWindow create() {
		return new DeltaFixedTimeWindow(1000l);
	}
	
}
