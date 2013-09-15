package de.cinovo.timeseries.impl;

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
		private final int maxSize;
		private final ExpandStrategy expandStrategy;
		private int ringHead = -1;
		private int ringTail = 0;
		private int size = 0;
		private long[] times;
		private float[] values;
		private long lastNow = Long.MIN_VALUE;
		
		private static final TimeSeriesPair CLEARED = new TimeSeriesPair(Long.MIN_VALUE, Float.NaN);
		
		private TimeSeriesPair cachedMaximum = Ring.CLEARED;
		
		private TimeSeriesPair cachedMinimum = Ring.CLEARED;
		
		
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
			this.clearCache();
		}
		
		public void deleteOldValues(final long now) {
			this.checkTime(now);
			final long minTime = now - this.windowLength;
			boolean deleted = false;
			while (this.size > 0) {
				if (this.times[this.ringTail] < minTime) {
					this.delete();
					deleted = true;
				} else {
					break;
				}
			}
			if (deleted == true) {
				this.clearCache();
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
		
		private void clearCache() {
			this.cachedMaximum = Ring.CLEARED;
			this.cachedMinimum = Ring.CLEARED;
		}
		
		private void refreshCacheMinMax() {
			if (this.size > 0) {
				long minTime = 0l;
				float min = Float.MAX_VALUE;
				long maxTime = 0l;
				float max = Float.MIN_VALUE;
				
				for (int i = this.ringTail; ((i < this.maxSize) && (i <= this.ringHead)); i++) {
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
				if (this.ringHead < this.ringTail) {
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
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public float variance() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public float deviation() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public float median() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public int size() {
			return this.size;
		}
		
	}
	
}