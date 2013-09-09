package de.cinovo.timeseries;

/**
 * 
 * Time series.
 * 
 * @author mwittig
 * 
 */
public interface ITimeSeries {
	
	/**
	 * @return First value in series (null if no value was added)
	 */
	ITimeSeriesPair first();
	
	/**
	 * @return Last value in series (null if no value was added)
	 */
	ITimeSeriesPair last();
	
	/**
	 * @return Minimum value in series (null if no value was added)
	 */
	ITimeSeriesPair minimum();
	
	/**
	 * @return Maximum value in series (null if no value was added)
	 */
	ITimeSeriesPair maximum();
	
	/**
	 * @return Average of series (Float.NaN if no value was added)
	 */
	float avergage();
	
	/**
	 * @return Variance of series (Float.NaN if no value was added)
	 */
	float variance();
	
	/**
	 * @return Standard deviation of series (Float.NaN if no value was added)
	 */
	float deviation();
	
	/**
	 * @return Median of series (Float.NaN if no value was added)
	 */
	float median();
	
	/**
	 * @return Number of values
	 */
	int size();
	
}
