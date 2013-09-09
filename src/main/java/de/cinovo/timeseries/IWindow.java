package de.cinovo.timeseries;

/**
 * A window stores values in a timed order. It it possible to add multiple values for the same time.<br>
 * <br>
 * A RuntimeException is thrown if you try to add values out of timed order.
 * 
 * @author mwittig
 * 
 */
public interface IWindow {
	
	/**
	 * @param time Time
	 * @param value Value
	 */
	void add(long time, float value);
	
}
