package de.cinovo.timeseries.impl;

import java.util.ArrayDeque;

import com.google.common.base.Preconditions;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.ITimeSeries;
import de.cinovo.timeseries.ITimeSeriesPair;
import de.cinovo.timeseries.test.AFixedTimeWindowTest;

/**
 * 
 * Optimization: Using a fixed array for delting elements.
 * 
 * DeltaFixedTimeWindow1.FirstBenchmark (window: 1000, runs: 10000; calls per run: 27088)<br>
 * nanos micros millis<br>
 * Avg 2951313,00 2951,31 2,95<br>
 * Min 2640000,00 2640,00 2,64<br>
 * Max 18514000,00 18514,00 18,51<br>
 * 
 * @author mwittig
 * 
 */
public final class DeltaFixedTimeWindow1 implements IFixedTimeWindow {
	
	private final long window;
	
	private final int expectedMaxSize;
	
	private long lastTime = Long.MIN_VALUE;
	
	private final ArrayDeque<TimeSeriesPair> values;
	
	private final TimeSeriesPair[] deletes;
	
	private static final TimeSeriesPair STOP_ELEMENT = new TimeSeriesPair(0l, 0f);
	
	private final Wrapper wrapper;
	
	
	/**
	 * @param window Window size (e. g. if you use milliseconds as time than window size is in milliseconds)
	 * @param expectedMaxSize Expected maximum number of values
	 */
	public DeltaFixedTimeWindow1(final long window, final int expectedMaxSize) {
		Preconditions.checkArgument(window > 0, "window must be > 0");
		Preconditions.checkArgument(expectedMaxSize > 0, "expectedMaxSize must be > 0");
		this.window = window;
		this.expectedMaxSize = expectedMaxSize;
		this.values = new ArrayDeque<TimeSeriesPair>(expectedMaxSize);
		this.wrapper = new Wrapper(this.values);
		this.deletes = new TimeSeriesPair[this.expectedMaxSize];
	}
	
	/**
	 * @param window Window size (e. g. if you use milliseconds as time than window size is in milliseconds)
	 */
	public DeltaFixedTimeWindow1(final long window) {
		this(window, 1000);
	}
	
	@Override
	public ITimeSeries get(final long now) {
		this.checkTime(now);
		this.cleanUp(now);
		this.wrapper.delete(this.deletes);
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
	
	/**
	 * @param now Time
	 */
	private void cleanUp(final long now) {
		int i = 0;
		while (i < this.deletes.length) {
			final TimeSeriesPair pair = this.values.pollFirst();
			if (pair == null) {
				this.deletes[i] = DeltaFixedTimeWindow1.STOP_ELEMENT;
				return;
			}
			if (pair.time() >= (now - this.window)) { // check if the value is not too old
				this.values.addFirst(pair); // reinsert the value at the beginning
				this.deletes[i] = DeltaFixedTimeWindow1.STOP_ELEMENT;
				return;
			}
			this.deletes[i] = pair;
			i += 1;
		}
		throw new RuntimeException("more elements to delete than expcted");
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
		
		public void delete(final TimeSeriesPair[] pairs) {
			for (int i = 0; i < pairs.length; i++) {
				final TimeSeriesPair pair = pairs[i];
				if (pair == DeltaFixedTimeWindow1.STOP_ELEMENT) {
					break;
				}
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
