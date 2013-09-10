package de.cinovo.timeseries.impl;

/**
 * 
 * @author mwittig
 * 
 */
public final class FloatHelper {
	
	private final static float PRECISION = 0.001f;
	
	
	/**
	 * @param a Value a
	 * @param b Value b
	 * @return true if they are equal within the precision of 0.001
	 */
	public static boolean equals(final float a, final float b) {
		return a == b ? true : Math.abs(a - b) < FloatHelper.PRECISION;
	}
	
	/**
	 * @param a Value a
	 * @param b Value b
	 * @param precision Precision
	 * @return true if they are equal within the precision
	 */
	public static boolean equals(final float a, final float b, final float precision) {
		return a == b ? true : Math.abs(a - b) < precision;
	}
	
	/**
	 * @param a Value a
	 * @param b Value b
	 * @return true if a > b within the precision of 0.001
	 */
	public static boolean greaterThan(final float a, final float b) {
		return FloatHelper.greaterThan(a, b, FloatHelper.PRECISION);
	}
	
	/**
	 * @param a Value a
	 * @param b Value b
	 * @param precision Precision
	 * @return true if a > b within the precision
	 */
	public static boolean greaterThan(final float a, final float b, final float precision) {
		return (a - b) > precision;
	}
	
	/**
	 * @param a Value a
	 * @param b Value b
	 * @return true if a < b within the precision of 0.001
	 */
	public static boolean lessThan(final float a, final float b) {
		return FloatHelper.lessThan(a, b, FloatHelper.PRECISION);
	}
	
	/**
	 * @param a Value a
	 * @param b Value b
	 * @param precision Precision
	 * @return true if a < b within the precision
	 */
	public static boolean lessThan(final float a, final float b, final float precision) {
		return (b - a) > precision;
	}
	
	private FloatHelper() {
		super();
	}
	
}
