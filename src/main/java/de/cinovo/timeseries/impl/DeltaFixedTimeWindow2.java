package de.cinovo.timeseries.impl;

import java.util.ArrayDeque;

import com.google.common.base.Preconditions;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.ITimeSeries;
import de.cinovo.timeseries.ITimeSeriesPair;

/**
 * 
 * Optimization: Using a fixed primitive array for delting elements.
 * 
 * @author mwittig
 * 
 */
public final class DeltaFixedTimeWindow2 implements IFixedTimeWindow {
	
	private final long window;
	
	private final int expectedMaxSize;
	
	private long lastTime = Long.MIN_VALUE;
	
	private final ArrayDeque<TimeSeriesPair> values;
	
	private final Wrapper wrapper;
	
	
	/**
	 * @param window Window size (e. g. if you use milliseconds as time than window size is in milliseconds)
	 * @param expectedMaxSize Expected maximum number of values
	 */
	public DeltaFixedTimeWindow2(final long window, final int expectedMaxSize) {
		Preconditions.checkArgument(window > 0, "window must be > 0");
		Preconditions.checkArgument(expectedMaxSize > 0, "expectedMaxSize must be > 0");
		this.window = window;
		this.expectedMaxSize = expectedMaxSize;
		this.values = new ArrayDeque<TimeSeriesPair>(expectedMaxSize);
		this.wrapper = new Wrapper(this.values, this.deletes);
	}
	
	/**
	 * @param window Window size (e. g. if you use milliseconds as time than window size is in milliseconds)
	 */
	public DeltaFixedTimeWindow2(final long window) {
		this(window, 1000);
	}
	
	@Override
	public ITimeSeries get(final long now) {
		this.checkTime(now);
		this.cleanUp(now);
		this.wrapper.delete();
		return this.wrapper;
	}
	
	@Override
	public void add(final long time, final float value) {
		this.checkTime(time);
		final TimeSeriesPair pair = new TimeSeriesPair(time, value);
		this.values.add(pair);
		this.wrapper.add(pair);
	}
	
	@Override
	public long windowLength() {
		return this.window;
	}
	
	private void checkTime(final long now) {
		if (now < this.lastTime) {
			throw new IllegalArgumentException("now is in the past");
		}
		this.lastTime = now;
	}
	
	
	private final float[] deletes = new float[this.expectedMaxSize];
	private static final float STOP_ELEMENT = Float.MIN_VALUE;
	
	
	/**
	 * @param now Time
	 */
	private void cleanUp(final long now) {
		int i = 0;
		while (i < this.deletes.length) {
			final TimeSeriesPair pair = this.values.pollFirst();
			if (pair == null) {
				this.deletes[i] = DeltaFixedTimeWindow2.STOP_ELEMENT;
				break;
			}
			if (pair.time() >= (now - this.window)) { // check if the value is not too old
				this.values.addFirst(pair); // reinsert the value at the beginning
				this.deletes[i] = DeltaFixedTimeWindow2.STOP_ELEMENT;
				break;
			}
			this.deletes[i] = pair.value();
			i += 1;
		}
	}
	
	
	private static final class Wrapper implements ITimeSeries {
		
		private final ArrayDeque<TimeSeriesPair> values;
		
		private final float[] deletes;
		
		private static final TimeSeriesPair CLEARED = new TimeSeriesPair(Long.MIN_VALUE, Float.NaN);
		
		private TimeSeriesPair cachedMaximum = Wrapper.CLEARED;
		
		private TimeSeriesPair cachedMinimum = Wrapper.CLEARED;
		
		private float cachedAvergage = Float.POSITIVE_INFINITY;
		
		private float cachedVariance = Float.POSITIVE_INFINITY;
		
		private float cachedDeviation = Float.POSITIVE_INFINITY;
		
		private float cachedMedian = Float.POSITIVE_INFINITY;
		
		private double sum = 0.0d;
		
		
		public Wrapper(final ArrayDeque<TimeSeriesPair> values, final float[] deletes) {
			super();
			this.values = values;
			this.deletes = deletes;
		}
		
		public void add(final TimeSeriesPair pair) {
			final float value = pair.value();
			final long time = pair.time();
			this.sum += value;
			this.cachedAvergage = Float.POSITIVE_INFINITY;
			
			if ((this.cachedMaximum == null) || ((this.cachedMaximum != Wrapper.CLEARED) && FloatHelper.greaterThan(value, this.cachedMaximum.value()))) {
				this.cachedMaximum = new TimeSeriesPair(time, value); // we have a new maximum
			}
			if ((this.cachedMinimum == null) || ((this.cachedMinimum != Wrapper.CLEARED) && FloatHelper.lessThan(value, this.cachedMinimum.value()))) {
				this.cachedMinimum = new TimeSeriesPair(time, value); // we have a new minimum
			}
		}
		
		public void delete() {
			for (int i = 0; i < this.deletes.length; i++) {
				final float value = this.deletes[i];
				if (value == DeltaFixedTimeWindow2.STOP_ELEMENT) {
					break;
				}
				this.sum -= value;
				if ((this.cachedMaximum != null) && (this.cachedMaximum != Wrapper.CLEARED) && FloatHelper.equals(value, this.cachedMaximum.value())) {
					this.cachedMaximum = Wrapper.CLEARED; // we lost the maximum. we have to check all values to find the new minimum.
				}
				if ((this.cachedMinimum != null) && (this.cachedMinimum != Wrapper.CLEARED) && FloatHelper.equals(value, this.cachedMinimum.value())) {
					this.cachedMinimum = Wrapper.CLEARED; // we lost the minimum. we have to check all values to find the new minimum.
				}
			}
			this.cachedAvergage = Float.POSITIVE_INFINITY;
		}
		
		private void refreshCacheAvg() {
			final int size = this.values.size();
			if (this.values.size() > 0) {
				this.cachedAvergage = (float) (this.sum / size);
			} else {
				this.cachedAvergage = Float.NaN;
			}
		}
		
		private void refreshCacheMinMax() {
			final ITimeSeriesPair first = this.first();
			if (first != null) {
				long minTime = first.time();
				float min = first.value();
				long maxTime = first.time();
				float max = first.value();
				for (final TimeSeriesPair pair : this.values) {
					if (pair.value() < min) {
						minTime = pair.time();
						min = pair.value();
					}
					if (pair.value() > max) {
						maxTime = pair.time();
						max = pair.value();
					}
				}
				this.cachedMinimum = new TimeSeriesPair(minTime, min);
				this.cachedMaximum = new TimeSeriesPair(maxTime, max);
			} else {
				this.cachedMinimum = null;
				this.cachedMaximum = null;
			}
		}
		
		@Override
		public ITimeSeriesPair first() {
			return this.values.peekFirst();
		}
		
		@Override
		public ITimeSeriesPair last() {
			return this.values.peekLast();
		}
		
		@Override
		public ITimeSeriesPair minimum() {
			if (this.cachedMinimum == Wrapper.CLEARED) {
				this.refreshCacheMinMax();
			}
			return this.cachedMinimum;
		}
		
		@Override
		public ITimeSeriesPair maximum() {
			if (this.cachedMaximum == Wrapper.CLEARED) {
				this.refreshCacheMinMax();
			}
			return this.cachedMaximum;
		}
		
		@Override
		public float avergage() {
			if (Float.isInfinite(this.cachedAvergage)) {
				this.refreshCacheAvg();
			}
			return this.cachedAvergage;
		}
		
		@Override
		public float variance() {
			if (Float.isInfinite(this.cachedVariance)) {
				// TODO implement
			}
			return this.cachedVariance;
		}
		
		@Override
		public float deviation() {
			if (Float.isInfinite(this.cachedDeviation)) {
				// TODO implement
			}
			return this.cachedDeviation;
		}
		
		@Override
		public float median() {
			if (Float.isInfinite(this.cachedMedian)) {
				// TODO implement
			}
			return this.cachedMedian;
		}
		
		@Override
		public int size() {
			return this.values.size();
		}
		
	}
	
}
