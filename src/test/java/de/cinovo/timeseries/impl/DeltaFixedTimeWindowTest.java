package de.cinovo.timeseries.impl;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.test.AFixedTimeWindowTest;

/**
 * 
 * @author mwittig
 * 
 */
public final class DeltaFixedTimeWindowTest extends AFixedTimeWindowTest {
	
	@Override
	protected IFixedTimeWindow create() {
		return new DeltaFixedTimeWindow3(1000l);
	}
	
}
