package de.cinovo.timeseries;

/**
 * 
 * A fixed time window contains values between now and the length of the period you are interested in. The size (number of values) is not
 * constant and can heavily change during time.<br>
 * <br>
 * E. g. your window can be 1 minute (60000 ms) in length. With every call to now(time) you provide the current time to only consider values
 * with time >= (now - $windowLength). Older values are dropped.<br>
 * <br>
 * Make sure that $now and $windowLength use the same unit (e. g. milliseconds).
 * 
 * @author mwittig
 * 
 */
public interface IFixedTimeWindow extends IWindow {
	
	/**
	 * @param now Current time
	 * @return Time series
	 */
	ITimeSeries get(long now);
	
	/**
	 * @return Window length (e. g. if you use milliseconds as time than $windowLength is in milliseconds)
	 */
	long windowLength();
	
}
