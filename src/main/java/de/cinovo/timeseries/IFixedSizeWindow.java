package de.cinovo.timeseries;

/**
 * 
 * A fixed size window contains between 0 and $windowSize values. If the window is full the oldest value is dropped.
 * 
 * @author mwittig
 * 
 */
public interface IFixedSizeWindow extends IWindow {
	
	/**
	 * @return Time series
	 */
	ITimeSeries get();
	
	/**
	 * @return Maximum number of values for this window
	 */
	int windowSize();
	
}
