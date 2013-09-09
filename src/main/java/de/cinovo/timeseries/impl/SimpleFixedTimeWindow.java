package de.cinovo.timeseries.impl;

import java.util.ArrayDeque;
import java.util.Arrays;

import com.google.common.base.Preconditions;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.ITimeSeries;
import de.cinovo.timeseries.ITimeSeriesPair;

/**
 * 
 * Simple implementation of a fixed time window.
 * 
 * @author mwittig
 * 
 */
public final class SimpleFixedTimeWindow implements IFixedTimeWindow {
	
	private final long window;
	
	private long lastTime = Long.MIN_VALUE;
	
	private final ArrayDeque<TimeSeriesPair> values;
	
	private final Wrapper wrapper;
	
	
	/**
	 * @param window Window size (e. g. if you use milliseconds as time than window size is in milliseconds)
	 * @param expectedMaxSize Expected maximum number of values
	 */
	public SimpleFixedTimeWindow(final long window, final int expectedMaxSize) {
		Preconditions.checkArgument(window > 0, "window must be > 0");
		Preconditions.checkArgument(expectedMaxSize > 0, "expectedMaxSize must be > 0");
		this.window = window;
		this.values = new ArrayDeque<TimeSeriesPair>(expectedMaxSize);
		this.wrapper = new Wrapper(this.values);
	}
	
	/**
	 * @param window Window size (e. g. if you use milliseconds as time than window size is in milliseconds)
	 */
	public SimpleFixedTimeWindow(final long window) {
		this(window, 10000);
	}
	
	public ITimeSeries get(final long now) {
		this.checkTime(now);
		if (this.cleanUp(now) == true) {
			this.wrapper.clearCache();
		}
		return this.wrapper;
	}
	
	public void add(final long time, final float value) {
		this.checkTime(time);
		this.values.add(new TimeSeriesPair(time, value));
		this.wrapper.clearCache();
	}
	
	public long windowLength() {
		return this.window;
	}
	
	private void checkTime(final long now) {
		if (now < this.lastTime) {
			throw new IllegalArgumentException("now is in the past");
		}
		this.lastTime = now;
	}
	
	/**
	 * @param now Time
	 * @return true if something was cleaned up
	 */
	private boolean cleanUp(final long now) {
		boolean cleanedUp = false;
		while (true) {
			final TimeSeriesPair pair = this.values.pollFirst();
			if (pair == null) {
				break;
			}
			if (pair.time() >= (now - this.window)) { // check if the value is not too old
				this.values.addFirst(pair); // reinsert the value at the beginning
				break;
			}
			cleanedUp = true;
		}
		return cleanedUp;
	}
	
	
	private static final class Wrapper implements ITimeSeries {
		
		private final ArrayDeque<TimeSeriesPair> values;
		
		private static final TimeSeriesPair CLEARED = new TimeSeriesPair(Long.MIN_VALUE, Float.NaN);
		
		private TimeSeriesPair cachedMaximum = Wrapper.CLEARED;
		
		private TimeSeriesPair cachedMinimum = Wrapper.CLEARED;
		
		private float cachedAvergage = Float.POSITIVE_INFINITY;
		
		private float cachedVariance = Float.POSITIVE_INFINITY;
		
		private float cachedDeviation = Float.POSITIVE_INFINITY;
		
		private float cachedMedian = Float.POSITIVE_INFINITY;
		
		
		public Wrapper(final ArrayDeque<TimeSeriesPair> values) {
			super();
			this.values = values;
		}
		
		private void clearCache() {
			this.cachedMaximum = Wrapper.CLEARED;
			this.cachedMinimum = Wrapper.CLEARED;
			this.cachedAvergage = Float.POSITIVE_INFINITY;
			this.cachedVariance = Float.POSITIVE_INFINITY;
			this.cachedDeviation = Float.POSITIVE_INFINITY;
			this.cachedMedian = Float.POSITIVE_INFINITY;
		}
		
		private void refreshCacheMinMaxAvg() {
			final ITimeSeriesPair first = this.first();
			if (first != null) {
				long minTime = first.time();
				float min = first.value();
				long maxTime = first.time();
				float max = first.value();
				double sum = 0.0d;
				for (final TimeSeriesPair pair : this.values) {
					if (pair.value() < min) {
						minTime = pair.time();
						min = pair.value();
					}
					if (pair.value() > max) {
						maxTime = pair.time();
						max = pair.value();
					}
					sum += pair.value();
				}
				this.cachedMinimum = new TimeSeriesPair(minTime, min);
				this.cachedMaximum = new TimeSeriesPair(maxTime, max);
				this.cachedAvergage = (float) (sum / this.values.size());
			} else {
				this.cachedMinimum = null;
				this.cachedMaximum = null;
				this.cachedAvergage = Float.NaN;
			}
		}
		
		private void refreshCacheVarDev() {
			if (Float.isInfinite(this.cachedAvergage)) {
				this.refreshCacheMinMaxAvg();
			}
			if (this.values.size() > 0) {
				double sumAbweichungImQuadrat = 0.0d;
				for (final TimeSeriesPair pair : this.values) {
					final float abweichung = pair.value() - this.cachedAvergage;
					sumAbweichungImQuadrat += abweichung * abweichung;
				}
				this.cachedVariance = (float) (sumAbweichungImQuadrat / this.values.size());
				this.cachedDeviation = (float) Math.sqrt(this.cachedVariance);
			} else {
				this.cachedVariance = Float.NaN;
				this.cachedDeviation = Float.NaN;
			}
		}
		
		private void refreshCacheMed() {
			if (this.values.size() > 0) {
				final float[] v = new float[this.values.size()];
				int i = 0;
				for (final TimeSeriesPair pair : this.values) {
					v[i] = pair.value();
					i++;
				}
				Arrays.sort(v);
				final float med;
				if ((v.length % 2) == 0) {
					med = (v[(v.length / 2) - 1] + v[v.length / 2]) / 2.0f;
				} else {
					med = v[v.length / 2];
				}
				this.cachedMedian = med;
			} else {
				this.cachedMedian = Float.NaN;
			}
		}
		
		public ITimeSeriesPair first() {
			return this.values.peekFirst();
		}
		
		public ITimeSeriesPair last() {
			return this.values.peekLast();
		}
		
		public ITimeSeriesPair minimum() {
			if (this.cachedMinimum == Wrapper.CLEARED) {
				this.refreshCacheMinMaxAvg();
			}
			return this.cachedMinimum;
		}
		
		public ITimeSeriesPair maximum() {
			if (this.cachedMaximum == Wrapper.CLEARED) {
				this.refreshCacheMinMaxAvg();
			}
			return this.cachedMaximum;
		}
		
		public float avergage() {
			if (Float.isInfinite(this.cachedAvergage)) {
				this.refreshCacheMinMaxAvg();
			}
			return this.cachedAvergage;
		}
		
		public float variance() {
			if (Float.isInfinite(this.cachedVariance)) {
				this.refreshCacheVarDev();
			}
			return this.cachedVariance;
		}
		
		public float deviation() {
			if (Float.isInfinite(this.cachedDeviation)) {
				this.refreshCacheVarDev();
			}
			return this.cachedDeviation;
		}
		
		public float median() {
			if (Float.isInfinite(this.cachedMedian)) {
				this.refreshCacheMed();
			}
			return this.cachedMedian;
		}
		
		public int size() {
			return this.values.size();
		}
		
	}
	
}
