package de.cinovo.timeseries.impl;

import org.junit.Assert;
import org.junit.Test;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.RingFixedTimeWindow.ExpandStrategy;
import de.cinovo.timeseries.test.AFixedTimeWindowTest;

/**
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public final class RingFixedTimeWindowTest extends AFixedTimeWindowTest {
	
	@Override
	protected IFixedTimeWindow create() {
		return new RingFixedTimeWindow(1000l, 10000, ExpandStrategy.throwException);
	}
	
	@Test
	public void testDeleteNothing() {
		final RingFixedTimeWindow w = new RingFixedTimeWindow(2, 3, ExpandStrategy.throwException);
		Assert.assertEquals(0, w.get(0l).size());
		
		w.add(1l, 1.0f);
		Assert.assertEquals(1, w.get(1l).size());
		w.add(1l, 1.0f);
		Assert.assertEquals(2, w.get(1l).size());
		w.add(1l, 1.0f);
		Assert.assertEquals(3, w.get(1l).size());
		
		w.get(3l);
		Assert.assertEquals(3, w.get(3l).size());
	}
	
	@Test
	public void testDelete1of3() {
		final RingFixedTimeWindow w = new RingFixedTimeWindow(2, 3, ExpandStrategy.throwException);
		Assert.assertEquals(0, w.get(0l).size());
		
		w.add(1l, 1.0f);
		Assert.assertEquals(1, w.get(1l).size());
		w.add(2l, 1.0f);
		Assert.assertEquals(2, w.get(2l).size());
		w.add(2l, 1.0f);
		Assert.assertEquals(3, w.get(2l).size());
		
		Assert.assertEquals(2, w.get(4l).size());
	}
	
	@Test
	public void testDelete2of3() {
		final RingFixedTimeWindow w = new RingFixedTimeWindow(2, 3, ExpandStrategy.throwException);
		Assert.assertEquals(0, w.get(0l).size());
		
		w.add(1l, 1.0f);
		Assert.assertEquals(1, w.get(1l).size());
		w.add(1l, 1.0f);
		Assert.assertEquals(2, w.get(1l).size());
		w.add(2l, 1.0f);
		Assert.assertEquals(3, w.get(2l).size());
		
		Assert.assertEquals(1, w.get(4l).size());
	}
	
	@Test
	public void testDelete3of3() {
		final RingFixedTimeWindow w = new RingFixedTimeWindow(2, 3, ExpandStrategy.throwException);
		Assert.assertEquals(0, w.get(0l).size());
		
		w.add(1l, 1.0f);
		Assert.assertEquals(1, w.get(1l).size());
		w.add(1l, 1.0f);
		Assert.assertEquals(2, w.get(1l).size());
		w.add(1l, 1.0f);
		Assert.assertEquals(3, w.get(1l).size());
		
		Assert.assertEquals(0, w.get(4l).size());
	}
	
	@Test
	public void testOverride() {
		final RingFixedTimeWindow w = new RingFixedTimeWindow(3, 3, ExpandStrategy.override);
		Assert.assertEquals(0, w.get(0l).size());
		
		w.add(1l, 1.0f);
		Assert.assertEquals(1, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 2.0f);
		Assert.assertEquals(2, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 3.0f);
		Assert.assertEquals(3, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(3.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 4.0f);
		Assert.assertEquals(3, w.get(1l).size());
		Assert.assertEquals(2.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(4.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 5.0f);
		Assert.assertEquals(3, w.get(1l).size());
		Assert.assertEquals(3.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(5.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 6.0f);
		Assert.assertEquals(3, w.get(1l).size());
		Assert.assertEquals(4.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(6.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 7.0f);
		Assert.assertEquals(3, w.get(1l).size());
		Assert.assertEquals(5.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(7.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testExpand() {
		final RingFixedTimeWindow w = new RingFixedTimeWindow(3, 3, ExpandStrategy.doubleCapacity);
		Assert.assertEquals(0, w.get(0l).size());
		
		w.add(1l, 1.0f);
		Assert.assertEquals(1, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 2.0f);
		Assert.assertEquals(2, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 3.0f);
		Assert.assertEquals(3, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(3.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 4.0f);
		Assert.assertEquals(4, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(4.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 5.0f);
		Assert.assertEquals(5, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(5.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 6.0f);
		Assert.assertEquals(6, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(6.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		w.add(1l, 7.0f);
		Assert.assertEquals(7, w.get(1l).size());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(7.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testBounds() {
		final RingFixedTimeWindow w = new RingFixedTimeWindow(2, 3, ExpandStrategy.throwException);
		Assert.assertEquals(0, w.get(0l).size());
		Assert.assertNull(w.get(0l).first());
		Assert.assertNull(w.get(0l).last());
		
		Assert.assertEquals(0, w.get(1l).size());
		w.add(1l, 1.0f);
		Assert.assertEquals(1, w.get(1l).size());
		Assert.assertEquals(1l, w.get(1l).first().time());
		Assert.assertEquals(1l, w.get(1l).last().time());
		Assert.assertEquals(1.0f, w.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, w.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		Assert.assertEquals(1, w.get(2l).size());
		w.add(2l, 2.0f);
		Assert.assertEquals(2, w.get(2l).size());
		Assert.assertEquals(1l, w.get(2l).first().time());
		Assert.assertEquals(2l, w.get(2l).last().time());
		Assert.assertEquals(1.0f, w.get(2l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, w.get(2l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		Assert.assertEquals(2, w.get(3l).size());
		w.add(3l, 3.0f);
		Assert.assertEquals(3, w.get(3l).size());
		Assert.assertEquals(1l, w.get(3l).first().time());
		Assert.assertEquals(3l, w.get(3l).last().time());
		Assert.assertEquals(1.0f, w.get(3l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(3.0f, w.get(3l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		Assert.assertEquals(2, w.get(4l).size());
		w.add(4l, 4.0f);
		Assert.assertEquals(3, w.get(4l).size());
		Assert.assertEquals(2l, w.get(4l).first().time());
		Assert.assertEquals(4l, w.get(4l).last().time());
		Assert.assertEquals(2.0f, w.get(4l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(4.0f, w.get(4l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		Assert.assertEquals(2, w.get(5l).size());
		w.add(5l, 5.0f);
		Assert.assertEquals(3, w.get(5l).size());
		Assert.assertEquals(3l, w.get(5l).first().time());
		Assert.assertEquals(5l, w.get(5l).last().time());
		Assert.assertEquals(3.0f, w.get(5l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(5.0f, w.get(5l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		Assert.assertEquals(2, w.get(6l).size());
		w.add(6l, 6.0f);
		Assert.assertEquals(3, w.get(6l).size());
		Assert.assertEquals(4l, w.get(6l).first().time());
		Assert.assertEquals(6l, w.get(6l).last().time());
		Assert.assertEquals(4.0f, w.get(6l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(6.0f, w.get(6l).last().value(), AFixedTimeWindowTest.PRECISION);
		
		Assert.assertEquals(2, w.get(7l).size());
		w.add(7l, 7.0f);
		Assert.assertEquals(3, w.get(7l).size());
		Assert.assertEquals(5l, w.get(7l).first().time());
		Assert.assertEquals(7l, w.get(7l).last().time());
		Assert.assertEquals(5.0f, w.get(7l).first().value(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(7.0f, w.get(7l).last().value(), AFixedTimeWindowTest.PRECISION);
		
	}
}
