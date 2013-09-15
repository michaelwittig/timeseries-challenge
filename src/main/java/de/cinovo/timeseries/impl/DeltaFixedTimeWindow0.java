package de.cinovo.timeseries.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;

import com.google.common.base.Preconditions;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.ITimeSeries;
import de.cinovo.timeseries.ITimeSeriesPair;
import de.cinovo.timeseries.test.AFixedTimeWindowTest;

/**
 * 
 * Optimization: reuse deletes ArrayList
 * 
 * DeltaFixedTimeWindow0.FirstBenchmark (window: 1000, runs: 10000; calls per run: 27088)<br>
 * nanos micros millis<br>
 * Avg 3208084,00 3208,08 3,21<br>
 * Min 2955000,00 2955,00 2,96<br>
 * Max 13633000,00 13633,00 13,63<br>
 * 
 * @author mwittig
 * 
 */
public final class DeltaFixedTimeWindow0 implements IFixedTimeWindow {
	
	private final long window;
	
	private long lastTime = Long.MIN_VALUE;
	
	private final ArrayDeque<TimeSeriesPair> values;
	
	private final Wrapper wrapper;
	
	
	/**
	 * @param window Window size (e. g. if you use milliseconds as time than window size is in milliseconds)
	 * @param expectedMaxSize Expected maximum number of values
	 */
	public DeltaFixedTimeWindow0(final long window, final int expectedMaxSize) {
		Preconditions.checkArgument(window > 0, "window must be > 0");
		Preconditions.checkArgument(expectedMaxSize > 0, "expectedMaxSize must be > 0");
		this.window = window;
		this.values = new ArrayDeque<TimeSeriesPair>(expectedMaxSize);
		this.wrapper = new Wrapper(this.values);
		this.deletes = new ArrayList<>(Math.max(10, expectedMaxSize / 10));
	}
	
	/**
	 * @param window Window size (e. g. if you use milliseconds as time than window size is in milliseconds)
	 */
	public DeltaFixedTimeWindow0(final long window) {
		this(window, 10000);
	}
	
	@Override
	public ITimeSeries get(final long now) {
		this.checkTime(now);
		this.cleanUp(now);
		if (this.deletes.size() > 0) {
			this.wrapper.delete(this.deletes);
		}
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
	
	
	private final ArrayList<TimeSeriesPair> deletes;
	
	
	/**
	 * @param now Time
	 */
	private void cleanUp(final long now) {
		this.deletes.clear();
		while (true) {
			final TimeSeriesPair pair = this.values.pollFirst();
			if (pair == null) {
				break;
			}
			if (pair.time() >= (now - this.window)) { // check if the value is not too old
				this.values.addFirst(pair); // reinsert the value at the beginning
				break;
			}
			this.deletes.add(pair);
		}
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
		
		private double sum = 0.0d;
		
		
		public Wrapper(final ArrayDeque<TimeSeriesPair> values) {
			super();
			this.values = values;
		}
		
		public void add(final TimeSeriesPair pair) {
			final float value = pair.value();
			final long time = pair.time();
			this.sum += value;
			this.cachedAvergage = Float.POSITIVE_INFINITY;
			
			if ((this.cachedMaximum == null) || ((this.cachedMaximum != Wrapper.CLEARED) && FloatHelper.greaterThan(value, this.cachedMaximum.value(), AFixedTimeWindowTest.PRECISION))) {
				this.cachedMaximum = new TimeSeriesPair(time, value); // we have a new maximum
			}
			if ((this.cachedMinimum == null) || ((this.cachedMinimum != Wrapper.CLEARED) && FloatHelper.lessThan(value, this.cachedMinimum.value(), AFixedTimeWindowTest.PRECISION))) {
				this.cachedMinimum = new TimeSeriesPair(time, value); // we have a new minimum
			}
		}
		
		public void delete(final ArrayList<TimeSeriesPair> pairs) {
			for (final TimeSeriesPair pair : pairs) {
				final float value = pair.value();
				this.sum -= value;
				if ((this.cachedMaximum != null) && (this.cachedMaximum != Wrapper.CLEARED) && FloatHelper.equals(value, this.cachedMaximum.value(), AFixedTimeWindowTest.PRECISION)) {
					this.cachedMaximum = Wrapper.CLEARED; // we lost the maximum. we have to check all values to find the new minimum.
				}
				if ((this.cachedMinimum != null) && (this.cachedMinimum != Wrapper.CLEARED) && FloatHelper.equals(value, this.cachedMinimum.value(), AFixedTimeWindowTest.PRECISION)) {
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
