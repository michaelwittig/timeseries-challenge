package de.cinovo.timeseries.test;

import org.junit.Assert;
import org.junit.Test;

import de.cinovo.timeseries.IFixedTimeWindow;

/**
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public abstract class AFixedTimeWindowTest {
	
	public static final float PRECISION = 0.001f;
	
	
	protected abstract IFixedTimeWindow create();
	
	@Test(expected = RuntimeException.class)
	public void testAddOutOfTimeOrder() {
		final IFixedTimeWindow ts = this.create();
		ts.add(100l, 1.0f);
		ts.add(99l, 2.0f);
	}
	
	@Test(expected = RuntimeException.class)
	public void testNowOutOfTimeOrder() {
		final IFixedTimeWindow ts = this.create();
		ts.get(100l);
		ts.get(99l);
	}
	
	/**
	 * size()
	 */
	
	@Test
	public void testSizeOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertEquals(0, ts.get(1l).size());
	}
	
	@Test
	public void testSizeAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(1, ts.get(1l).size());
	}
	
	@Test
	public void testSizeAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(2, ts.get(1l).size());
	}
	
	@Test
	public void testSizeAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(2, ts.get(1001l).size());
	}
	
	@Test
	public void testSizeAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(1, ts.get(1002l).size());
	}
	
	@Test
	public void testSizeAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(0, ts.get(1002l).size());
	}
	
	/**
	 * first()
	 */
	
	@Test
	public void testFirstOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertNull(ts.get(1l).first());
	}
	
	@Test
	public void testFirstAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(1l, ts.get(1l).first().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testFirstAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(1l, ts.get(1l).first().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1l).first().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testFirstAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(1l, ts.get(1001l).first().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1001l).first().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testFirstAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(1002l, ts.get(1002l).first().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1002l).first().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testFirstAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertNull(ts.get(1002l).first());
	}
	
	/**
	 * last()
	 */
	
	@Test
	public void testLastOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertNull(ts.get(1l).last());
	}
	
	@Test
	public void testLastAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(1l, ts.get(1l).last().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testLastAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(1l, ts.get(1l).last().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1l).last().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testLastAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(1001l, ts.get(1001l).last().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1001l).last().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testLastAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(1002l, ts.get(1002l).last().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1002l).last().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testLastAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertNull(ts.get(1002l).last());
	}
	
	/**
	 * maximum()
	 */
	
	@Test
	public void testMaximumOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertNull(ts.get(1l).maximum());
	}
	
	@Test
	public void testMaximumAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(1l, ts.get(1l).maximum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1l).maximum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMaximumAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(1l, ts.get(1l).maximum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1l).maximum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMaximumAfterAdd2InWindowSameTime2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1l, 1.0f);
		Assert.assertEquals(1l, ts.get(1l).maximum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1l).maximum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMaximumAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(1001l, ts.get(1001l).maximum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1001l).maximum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMaximumAfterAdd2InWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1001l, 1.0f);
		Assert.assertEquals(1l, ts.get(1001l).maximum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1001l).maximum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMaximumAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(1002l, ts.get(1002l).maximum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1002l).maximum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMaximumAfterAdd1OutOfFirstWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1002l, 1.0f);
		Assert.assertEquals(1002l, ts.get(1002l).maximum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1002l).maximum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMaximumAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertNull(ts.get(1002l).maximum());
	}
	
	/**
	 * minimum()
	 */
	
	@Test
	public void testMinimumOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertNull(ts.get(1l).minimum());
	}
	
	@Test
	public void testMinimumAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(1l, ts.get(1l).minimum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1l).minimum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMinimumAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(1l, ts.get(1l).minimum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1l).minimum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMinimumAfterAdd2InWindowSameTime2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1l, 1.0f);
		Assert.assertEquals(1l, ts.get(1l).minimum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1l).minimum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMinimumAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(1l, ts.get(1001l).minimum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1001l).minimum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMinimumAfterAdd2InWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1001l, 1.0f);
		Assert.assertEquals(1001l, ts.get(1001l).minimum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1001l).minimum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMinimumAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(1002l, ts.get(1002l).minimum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(2.0f, ts.get(1002l).minimum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMinimumAfterAdd1OutOfFirstWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1002l, 1.0f);
		Assert.assertEquals(1002l, ts.get(1002l).minimum().time(), AFixedTimeWindowTest.PRECISION);
		Assert.assertEquals(1.0f, ts.get(1002l).minimum().value(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMinimumAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertNull(ts.get(1002l).minimum());
	}
	
	/**
	 * average()
	 */
	
	@Test
	public void testAvergaeOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertTrue(Float.isNaN(ts.get(1l).avergage()));
	}
	
	@Test
	public void testAverageAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(1.0f, ts.get(1l).avergage(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testAverageAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(1.5f, ts.get(1l).avergage(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testAverageAfterAdd2InWindowSameTime2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1l, 1.0f);
		Assert.assertEquals(1.5f, ts.get(1l).avergage(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testAverageAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(1.5f, ts.get(1001l).avergage(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testAverageAfterAdd2InWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1001l, 1.0f);
		Assert.assertEquals(1.5f, ts.get(1001l).avergage(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testAverageAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(2.0f, ts.get(1002l).avergage(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testAverageAfterAdd1OutOfFirstWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1002l, 1.0f);
		Assert.assertEquals(1.0f, ts.get(1002l).avergage(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testAverageAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertTrue(Float.isNaN(ts.get(1002l).avergage()));
	}
	
	/**
	 * variance()
	 */
	
	@Test
	public void testVarianceOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertTrue(Float.isNaN(ts.get(1l).variance()));
	}
	
	@Test
	public void testVarianceAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(0.0f, ts.get(1l).variance(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testVarianceAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(0.25f, ts.get(1l).variance(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testVarianceAfterAdd2InWindowSameTime2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1l, 1.0f);
		Assert.assertEquals(0.25f, ts.get(1l).variance(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testVarianceAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(0.25f, ts.get(1001l).variance(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testVarianceAfterAdd2InWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1001l, 1.0f);
		Assert.assertEquals(0.25f, ts.get(1001l).variance(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testVarianceAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(0.0f, ts.get(1002l).variance(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testVarianceAfterAdd1OutOfFirstWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1002l, 1.0f);
		Assert.assertEquals(0.0f, ts.get(1002l).variance(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testVarianceAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertTrue(Float.isNaN(ts.get(1002l).variance()));
	}
	
	/**
	 * deviation()
	 */
	
	@Test
	public void testDeviationOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertTrue(Float.isNaN(ts.get(1l).deviation()));
	}
	
	@Test
	public void testDeviationAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(0.0f, ts.get(1l).deviation(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testDeviationAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(0.5f, ts.get(1l).deviation(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testDeviationAfterAdd2InWindowSameTime2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1l, 1.0f);
		Assert.assertEquals(0.5f, ts.get(1l).deviation(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testDeviationAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(0.5f, ts.get(1001l).deviation(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testDeviationAfterAdd2InWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1001l, 1.0f);
		Assert.assertEquals(0.5f, ts.get(1001l).deviation(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testDeviationAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(0.0f, ts.get(1002l).deviation(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testDeviationAfterAdd1OutOfFirstWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1002l, 1.0f);
		Assert.assertEquals(0.0f, ts.get(1002l).deviation(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testDeviationAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertTrue(Float.isNaN(ts.get(1002l).deviation()));
	}
	
	/**
	 * median()
	 */
	
	@Test
	public void testMedianOfEmptySeries() {
		final IFixedTimeWindow ts = this.create();
		Assert.assertTrue(Float.isNaN(ts.get(1l).median()));
	}
	
	@Test
	public void testMedianAfterAdd1InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(1.0f, ts.get(1l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianAfterAdd2InWindowSameTime() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(1.5f, ts.get(1l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianAfterAdd2InWindowSameTime2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1l, 1.0f);
		Assert.assertEquals(1.5f, ts.get(1l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianAfterAdd2InWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1001l, 2.0f);
		Assert.assertEquals(1.5f, ts.get(1001l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianAfterAdd2InWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1001l, 1.0f);
		Assert.assertEquals(1.5f, ts.get(1001l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianAfterAdd1OutOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1002l, 2.0f);
		Assert.assertEquals(2.0f, ts.get(1002l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianAfterAdd1OutOfFirstWindow2() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 2.0f);
		ts.add(1002l, 1.0f);
		Assert.assertEquals(1.0f, ts.get(1002l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianAfterFullCleanOfFirstWindow() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertTrue(Float.isNaN(ts.get(1002l).median()));
	}
	
	@Test
	public void testMedianCalculationWith1Value() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		Assert.assertEquals(1.0f, ts.get(1l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianCalculationWith2Values() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(1.5f, ts.get(1l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianCalculationWith3Values() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 3.0f);
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		Assert.assertEquals(2.0f, ts.get(1l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianCalculationWith4Values() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 1.0f);
		ts.add(1l, 2.0f);
		ts.add(1l, 4.0f);
		ts.add(1l, 3.0f);
		Assert.assertEquals(2.5f, ts.get(1l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
	@Test
	public void testMedianCalculationWith5Values() {
		final IFixedTimeWindow ts = this.create();
		ts.add(1l, 5.0f);
		ts.add(1l, 1.0f);
		ts.add(1l, 4.0f);
		ts.add(1l, 2.0f);
		ts.add(1l, 3.0f);
		Assert.assertEquals(3.0f, ts.get(1l).median(), AFixedTimeWindowTest.PRECISION);
	}
	
}
