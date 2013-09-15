package de.cinovo.timeseries.impl;

import java.util.Arrays;

import com.google.common.base.Preconditions;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.ITimeSeries;
import de.cinovo.timeseries.ITimeSeriesPair;
import de.cinovo.timeseries.test.AFixedTimeWindowTest;

/**
 * 
 * @author mwittig
 * 
 */
public final class RingFixedTimeWindow implements IFixedTimeWindow {
	
	private final long windowLength;
	
	private final Ring ring;
	
	
	/**
	 * 
	 * @author mwittig
	 * 
	 */
	public static enum ExpandStrategy {
		/** double capacity. */
		doubleCapacity,
		
		/** override values. */
		override,
		
		/** throw exception. */
		throwException
	}
	
	
	/**
	 * @param windowLength Window length
	 * @param maxSize Max size
	 * @param expandStrategy Expand strategy
	 */
	public RingFixedTimeWindow(final long windowLength, final int maxSize, final ExpandStrategy expandStrategy) {
		Preconditions.checkArgument(windowLength > 0, "window must be > 0");
		Preconditions.checkArgument(maxSize > 0, "maxSize must be > 0");
		Preconditions.checkNotNull(expandStrategy, "expandStrategy");
		this.windowLength = windowLength;
		this.ring = new Ring(windowLength, maxSize, expandStrategy);
	}
	
	@Override
	public void add(long time, float value) {
		this.ring.add(time, value);
		
	}
	
	@Override
	public ITimeSeries get(final long now) {
		this.ring.deleteOldValues(now);
		return this.ring;
	}
	
	@Override
	public long windowLength() {
		return this.windowLength;
	}
	
	
	private static final class Ring implements ITimeSeries {
		
		private final long windowLength;
		private long lastNow = Long.MIN_VALUE;
		
		// ring
		private final int maxSize;
		private final ExpandStrategy expandStrategy;
		private int ringHead = -1;
		private int ringTail = 0;
		private int size = 0;
		private long[] times;
		private float[] values;
		
		private double sum;
		
		// Caches
		private static final TimeSeriesPair CLEARED = new TimeSeriesPair(Long.MIN_VALUE, Float.NaN);
		private float cachedAvergage = Float.POSITIVE_INFINITY;
		private TimeSeriesPair cachedMaximum = Ring.CLEARED;
		private TimeSeriesPair cachedMinimum = Ring.CLEARED;
		private float cachedVariance = Float.POSITIVE_INFINITY;
		private float cachedDeviation = Float.POSITIVE_INFINITY;
		private float cachedMedian = Float.POSITIVE_INFINITY;
		
		
		public Ring(final long windowLength, final int maxSize, final ExpandStrategy expandStrategy) {
			Preconditions.checkArgument(windowLength > 0, "window must be > 0");
			Preconditions.checkArgument(maxSize > 0, "maxSize must be > 0");
			Preconditions.checkNotNull(expandStrategy, "expandStrategy");
			this.windowLength = windowLength;
			this.maxSize = maxSize;
			this.expandStrategy = expandStrategy;
			this.times = new long[maxSize];
			this.values = new float[maxSize];
		}
		
		public void checkTime(final long now) {
			if (now < this.lastNow) {
				throw new IllegalArgumentException("now is in the past");
			}
			this.lastNow = now;
		}
		
		public void add(long time, float value) {
			this.checkTime(time);
			final int nextHead = this.ringHead + 1;
			if (((nextHead < this.maxSize) && (nextHead != this.ringTail)) || (this.ringHead == -1)) {
				// somewhere in the middle of the array or the first add
				this.ringHead = nextHead;
				this.times[nextHead] = time;
				this.values[nextHead] = value;
				this.size += 1;
			} else if ((nextHead == this.maxSize) && (0 != this.ringTail)) {
				// reached the end of the array so we have to start from 0 again
				this.ringHead = 0;
				this.times[0] = time;
				this.values[0] = value;
				this.size += 1;
			} else {
				if (this.expandStrategy == ExpandStrategy.override) {
					// the ring was full so we override values
					{
						// delete the last element
						final int nextTail = this.ringTail + 1;
						if ((nextTail < this.maxSize)) {
							this.ringTail = nextTail;
						} else {
							this.ringTail = 0;
						}
					}
					{
						// add the first element
						if (nextHead < this.maxSize) {
							// somewhere in the middle of the array
							this.ringHead = nextHead;
							this.times[nextHead] = time;
							this.values[nextHead] = value;
						} else {
							// reached the end of the array so we have to start from 0 again
							this.ringHead = 0;
							this.times[0] = time;
							this.values[0] = value;
						}
					}
				} else if (this.expandStrategy == ExpandStrategy.doubleCapacity) {
					throw new UnsupportedOperationException("not yet implemented");
				} else {
					throw new IndexOutOfBoundsException("maxSize reached");
				}
			}
			
			this.sum += value;
			
			this.cachedAvergage = Float.POSITIVE_INFINITY;
			this.cachedVariance = Float.POSITIVE_INFINITY;
			this.cachedDeviation = Float.POSITIVE_INFINITY;
			this.cachedMedian = Float.POSITIVE_INFINITY;
			if ((this.cachedMaximum == null) || ((this.cachedMaximum != Ring.CLEARED) && FloatHelper.greaterThan(value, this.cachedMaximum.value()))) {
				this.cachedMaximum = new TimeSeriesPair(time, value); // we have a new maximum
			}
			if ((this.cachedMinimum == null) || ((this.cachedMinimum != Ring.CLEARED) && FloatHelper.lessThan(value, this.cachedMinimum.value()))) {
				this.cachedMinimum = new TimeSeriesPair(time, value); // we have a new minimum
			}
		}
		
		public void deleteOldValues(final long now) {
			this.checkTime(now);
			final long minTime = now - this.windowLength;
			boolean deleted = false;
			while (this.size > 0) {
				if (this.times[this.ringTail] < minTime) {
					final float value = this.values[this.ringTail];
					this.sum -= value;
					if ((this.cachedMaximum != null) && (this.cachedMaximum != Ring.CLEARED) && FloatHelper.equals(value, this.cachedMaximum.value())) {
						this.cachedMaximum = Ring.CLEARED; // we lost the maximum. we have to check all values to find the new minimum.
					}
					if ((this.cachedMinimum != null) && (this.cachedMinimum != Ring.CLEARED) && FloatHelper.equals(value, this.cachedMinimum.value())) {
						this.cachedMinimum = Ring.CLEARED; // we lost the minimum. we have to check all values to find the new minimum.
					}
					this.delete();
					deleted = true;
				} else {
					break;
				}
			}
			if (deleted == true) {
				this.cachedAvergage = Float.POSITIVE_INFINITY;
				this.cachedVariance = Float.POSITIVE_INFINITY;
				this.cachedDeviation = Float.POSITIVE_INFINITY;
				this.cachedMedian = Float.POSITIVE_INFINITY;
			}
		}
		
		private void delete() {
			final int nextTail = this.ringTail + 1;
			if (this.size == 1) {
				this.ringHead = -1;
				this.ringTail = 0;
				this.size = 0;
			} else if (nextTail == this.ringHead) {
				this.ringTail = nextTail;
				this.size -= 1;
			} else if ((nextTail < this.maxSize) && (nextTail != this.ringHead)) {
				this.ringTail = nextTail;
				this.size -= 1;
			} else if ((nextTail == this.maxSize) && (0 != this.ringHead)) {
				this.ringTail = 0;
				this.size -= 1;
			} else {
				throw new IndexOutOfBoundsException("nextTail " + nextTail);
			}
		}
		
		private void refreshCacheAvg() {
			if (this.size > 0) {
				this.cachedAvergage = (float) (this.sum / this.size);
			} else {
				this.cachedAvergage = Float.NaN;
			}
		}
		
		private void refreshCacheVarDev() {
			if (this.size > 0) {
				if (Float.isInfinite(this.cachedAvergage)) {
					this.refreshCacheAvg();
				}
				final float avg = this.cachedAvergage;
				double sumAbweichungImQuadrat = 0.0d;
				if (this.ringHead >= this.ringTail) {
					for (int i = this.ringTail; i <= this.ringHead; i++) {
						final float abweichung = this.values[i] - avg;
						sumAbweichungImQuadrat += abweichung * abweichung;
					}
				} else {
					for (int i = this.ringTail; i < this.maxSize; i++) {
						final float abweichung = this.values[i] - avg;
						sumAbweichungImQuadrat += abweichung * abweichung;
					}
					for (int i = 0; i <= this.ringHead; i++) {
						final float abweichung = this.values[i] - avg;
						sumAbweichungImQuadrat += abweichung * abweichung;
					}
				}
				this.cachedVariance = (float) (sumAbweichungImQuadrat / this.size);
				this.cachedDeviation = (float) Math.sqrt(this.cachedVariance);
			} else {
				this.cachedVariance = Float.NaN;
				this.cachedDeviation = Float.NaN;
			}
		}
		
		private void refreshCacheMinMax() {
			if (this.size > 0) {
				long minTime = -1l;
				float min = Float.POSITIVE_INFINITY;
				long maxTime = -1l;
				float max = Float.NEGATIVE_INFINITY;
				if (this.ringHead >= this.ringTail) {
					for (int i = this.ringTail; i <= this.ringHead; i++) {
						final long time = this.times[i];
						final float value = this.values[i];
						if (FloatHelper.greaterThan(value, max, AFixedTimeWindowTest.PRECISION)) {
							maxTime = time;
							max = value;
						}
						if (FloatHelper.lessThan(value, min, AFixedTimeWindowTest.PRECISION)) {
							minTime = time;
							min = value;
						}
					}
				} else {
					for (int i = this.ringTail; i < this.maxSize; i++) {
						final long time = this.times[i];
						final float value = this.values[i];
						if (FloatHelper.greaterThan(value, max, AFixedTimeWindowTest.PRECISION)) {
							maxTime = time;
							max = value;
						}
						if (FloatHelper.lessThan(value, min, AFixedTimeWindowTest.PRECISION)) {
							minTime = time;
							min = value;
						}
					}
					for (int i = 0; i <= this.ringHead; i++) {
						final long time = this.times[i];
						final float value = this.values[i];
						if (FloatHelper.greaterThan(value, max, AFixedTimeWindowTest.PRECISION)) {
							maxTime = time;
							max = value;
						}
						if (FloatHelper.lessThan(value, min, AFixedTimeWindowTest.PRECISION)) {
							minTime = time;
							min = value;
						}
					}
				}
				this.cachedMinimum = new TimeSeriesPair(minTime, min);
				this.cachedMaximum = new TimeSeriesPair(maxTime, max);
			} else {
				this.cachedMinimum = null;
				this.cachedMaximum = null;
			}
		}
		
		private void refreshCacheMed() {
			if (this.size > 0) {
				final float[] v = new float[this.size];
				
				if (this.ringHead >= this.ringTail) {
					System.arraycopy(this.values, this.ringTail, v, 0, this.size);
				} else {
					final int firstLength = (this.maxSize - this.ringTail);
					System.arraycopy(this.values, this.ringTail, v, 0, firstLength);
					System.arraycopy(this.values, 0, v, firstLength, this.ringHead + 1);
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
		
		@Override
		public ITimeSeriesPair first() {
			if (this.size > 0) {
				return new TimeSeriesPair(this.times[this.ringTail], this.values[this.ringTail]);
			}
			return null;
		}
		
		@Override
		public ITimeSeriesPair last() {
			if (this.size > 0) {
				return new TimeSeriesPair(this.times[this.ringHead], this.values[this.ringHead]);
			}
			return null;
		}
		
		@Override
		public ITimeSeriesPair minimum() {
			if (this.cachedMinimum == Ring.CLEARED) {
				this.refreshCacheMinMax();
			}
			return this.cachedMinimum;
		}
		
		@Override
		public ITimeSeriesPair maximum() {
			if (this.cachedMaximum == Ring.CLEARED) {
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
				this.refreshCacheVarDev();
			}
			return this.cachedVariance;
		}
		
		@Override
		public float deviation() {
			if (Float.isInfinite(this.cachedDeviation)) {
				this.refreshCacheVarDev();
			}
			return this.cachedDeviation;
		}
		
		@Override
		public float median() {
			if (Float.isInfinite(this.cachedMedian)) {
				this.refreshCacheMed();
			}
			return this.cachedMedian;
		}
		
		@Override
		public int size() {
			return this.size;
		}
		
	}
	
}
