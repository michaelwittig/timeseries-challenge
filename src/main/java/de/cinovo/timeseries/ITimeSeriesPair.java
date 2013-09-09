package de.cinovo.timeseries;

/**
 * 
 * @author mwittig
 * 
 */
public interface ITimeSeriesPair {
	
	/**
	 * @return Time
	 */
	long time();
	
	/**
	 * @return value
	 */
	float value();
	
}
