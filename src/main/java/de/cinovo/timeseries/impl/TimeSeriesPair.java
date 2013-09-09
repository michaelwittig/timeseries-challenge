package de.cinovo.timeseries.impl;

import de.cinovo.timeseries.ITimeSeriesPair;

/**
 * 
 * @author mwittig
 * 
 */
public final class TimeSeriesPair implements ITimeSeriesPair {
	
	private final long time;
	
	private final float value;
	
	
	/**
	 * @param time Time
	 * @param value value
	 */
	public TimeSeriesPair(long time, float value) {
		super();
		this.time = time;
		this.value = value;
	}
	
	@Override
	public long time() {
		return this.time;
	}
	
	@Override
	public float value() {
		return this.value;
	}
	
}
