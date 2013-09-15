package de.cinovo.timeseries.impl;

import com.google.common.base.Preconditions;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.ITimeSeries;
import de.cinovo.timeseries.ITimeSeriesPair;
import de.cinovo.timeseries.impl.RingFixedTimeWindow.ExpandStrategy;
import de.cinovo.timeseries.impl.RingFixedTimeWindow.Ring;

/**
 * The idea is that only the head and the tail of the values are heavily modified.<br>
 * So we build time buckets of values where we have only the first and the last bucket which are modified.
 * 
 * @author mwittig
 * 
 */
public final class BucketFixedTimeWindow implements IFixedTimeWindow {
	
	private final long windowLength;
	
	private final Wrapper wrapper;
	
	
	/**
	 * @param aWindowLength Window length
	 * @param aBuckets Buckets
	 * @param aBucketMaxSize Bucket max size
	 * @param aBucketExpandStrategy Bucket expand strategy
	 */
	public BucketFixedTimeWindow(final long aWindowLength, final int aBuckets, final int aBucketMaxSize, final ExpandStrategy aBucketExpandStrategy) {
		Preconditions.checkArgument(aWindowLength > 0, "window must be > 0");
		Preconditions.checkArgument(aBuckets > 0, "buckets must be > 0");
		Preconditions.checkArgument(aBucketMaxSize > 0, "bucketMaxSize must be > 0");
		Preconditions.checkNotNull(aBucketExpandStrategy, "bucketExpandStrategy");
		Preconditions.checkArgument((aWindowLength % aBuckets) == 0, "$windowLength must be an even multiple of $buckets");
		this.windowLength = aWindowLength;
		this.wrapper = new Wrapper(aWindowLength, aBuckets, aBucketMaxSize, aBucketExpandStrategy);
	}
	
	@Override
	public void add(final long time, final float value) {
		this.wrapper.add(time, value);
	}
	
	@Override
	public ITimeSeries get(final long now) {
		this.wrapper.deleteOldValues(now);
		return this.wrapper;
	}
	
	@Override
	public long windowLength() {
		return this.windowLength;
	}
	
	
	private static final class Wrapper implements ITimeSeries {
		
		private final long windowLength;
		private long lastNow = Long.MIN_VALUE;
		
		private final long bucketWindowLength;
		
		private int currentRing = -1;
		private final Ring[] rings;
		
		
		public Wrapper(final long aWindowLength, final int aBuckets, final int aBucketMaxSize, final ExpandStrategy aBucketExpandStrategy) {
			Preconditions.checkArgument(aWindowLength > 0, "window must be > 0");
			Preconditions.checkArgument(aBuckets > 0, "buckets must be > 0");
			Preconditions.checkArgument(aBucketMaxSize > 0, "bucketMaxSize must be > 0");
			Preconditions.checkNotNull(aBucketExpandStrategy, "bucketExpandStrategy");
			Preconditions.checkArgument((aWindowLength % aBuckets) == 0, "$windowLength must be an even multiple of $buckets");
			this.windowLength = aWindowLength;
			this.bucketWindowLength = (aWindowLength / aBuckets);
			this.rings = new Ring[aBuckets];
			for (int i = 0; i < aBuckets; i++) {
				this.rings[i] = new Ring(aWindowLength, aBucketMaxSize, aBucketExpandStrategy);
			}
		}
		
		private void checkTime(final long now) {
			if (now < this.lastNow) {
				throw new IllegalArgumentException("now is in the past");
			}
			this.lastNow = now;
		}
		
		private int getRingIndex(final long time) {
			return (int) ((time % this.windowLength) / this.bucketWindowLength);
		}
		
		public void add(final long time, final float value) {
			this.checkTime(time);
			final int ring = this.getRingIndex(time);
			if (this.currentRing == -1) {
				this.currentRing = ring;
			}
			this.rings[ring].add(time, value);
		}
		
		public void deleteOldValues(final long now) {
			this.checkTime(now);
			// TODO implement
		}
		
		@Override
		public ITimeSeriesPair first() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ITimeSeriesPair last() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ITimeSeriesPair minimum() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public ITimeSeriesPair maximum() {
			// TODO Auto-generated method stub
			return null;
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
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
	
}
