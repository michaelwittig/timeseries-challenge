package de.cinovo.timeseries.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.junit.Test;

import de.cinovo.timeseries.IFixedTimeWindow;
import de.cinovo.timeseries.impl.SimpleFixedTimeWindow;

/**
 * Test against the SimpleFixedTimeWindow implementation.
 * 
 * @author mwittig
 * 
 */
@SuppressWarnings("javadoc")
public abstract class ACompareWithSimpleFixedTimeWindowTest {
	
	private SimpleFixedTimeWindow create1() {
		return new SimpleFixedTimeWindow(1000l);
	}
	
	private IFixedTimeWindow create2() {
		return this.create2(1000l);
	}
	
	protected abstract IFixedTimeWindow create2(final long windowLength);
	
	@Test
	public void testFirst() throws Exception {
		final IFixedTimeWindow impl1 = this.create1();
		final IFixedTimeWindow impl2 = this.create2();
		final BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/cinovo/timeseries/test/test.data")));
		String line = null;
		while ((line = br.readLine()) != null) {
			final String[] s = line.split(",");
			final long time = Long.parseLong(s[0]);
			final float value = Float.parseFloat(s[1]);
			impl1.add(time, value);
			impl2.add(time, value);
			Assert.assertEquals(impl1.get(time).first().time(), impl2.get(time).first().time(), AFixedTimeWindowTest.PRECISION);
			Assert.assertEquals(impl1.get(time).first().value(), impl2.get(time).first().value());
		}
		br.close();
	}
	
	@Test
	public void testLast() throws Exception {
		final IFixedTimeWindow impl1 = this.create1();
		final IFixedTimeWindow impl2 = this.create2();
		final BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/cinovo/timeseries/test/test.data")));
		String line = null;
		while ((line = br.readLine()) != null) {
			final String[] s = line.split(",");
			final long time = Long.parseLong(s[0]);
			final float value = Float.parseFloat(s[1]);
			impl1.add(time, value);
			impl2.add(time, value);
			Assert.assertEquals(impl1.get(time).last().time(), impl2.get(time).last().time(), AFixedTimeWindowTest.PRECISION);
			Assert.assertEquals(impl1.get(time).last().value(), impl2.get(time).last().value());
		}
		br.close();
	}
	
	@Test
	public void testAverage() throws Exception {
		final IFixedTimeWindow impl1 = this.create1();
		final IFixedTimeWindow impl2 = this.create2();
		final BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/cinovo/timeseries/test/test.data")));
		String line = null;
		while ((line = br.readLine()) != null) {
			final String[] s = line.split(",");
			final long time = Long.parseLong(s[0]);
			final float value = Float.parseFloat(s[1]);
			impl1.add(time, value);
			impl2.add(time, value);
			Assert.assertEquals(impl1.get(time).avergage(), impl2.get(time).avergage(), AFixedTimeWindowTest.PRECISION);
		}
		br.close();
	}
	
	@Test
	public void testMinimum() throws Exception {
		final IFixedTimeWindow impl1 = this.create1();
		final IFixedTimeWindow impl2 = this.create2();
		final BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/cinovo/timeseries/test/test.data")));
		String line = null;
		while ((line = br.readLine()) != null) {
			final String[] s = line.split(",");
			final long time = Long.parseLong(s[0]);
			final float value = Float.parseFloat(s[1]);
			impl1.add(time, value);
			impl2.add(time, value);
			Assert.assertEquals(impl1.get(time).minimum().time(), impl2.get(time).minimum().time(), AFixedTimeWindowTest.PRECISION);
			Assert.assertEquals(impl1.get(time).minimum().value(), impl2.get(time).minimum().value());
		}
		br.close();
	}
	
	@Test
	public void testMaximum() throws Exception {
		final IFixedTimeWindow impl1 = this.create1();
		final IFixedTimeWindow impl2 = this.create2();
		final BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/cinovo/timeseries/test/test.data")));
		String line = null;
		while ((line = br.readLine()) != null) {
			final String[] s = line.split(",");
			final long time = Long.parseLong(s[0]);
			final float value = Float.parseFloat(s[1]);
			impl1.add(time, value);
			impl2.add(time, value);
			Assert.assertEquals(impl1.get(time).maximum().time(), impl2.get(time).maximum().time(), AFixedTimeWindowTest.PRECISION);
			Assert.assertEquals(impl1.get(time).maximum().value(), impl2.get(time).maximum().value());
		}
		br.close();
	}
	
	@Test
	public void testVariance() throws Exception {
		final IFixedTimeWindow impl1 = this.create1();
		final IFixedTimeWindow impl2 = this.create2();
		final BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/cinovo/timeseries/test/test.data")));
		String line = null;
		while ((line = br.readLine()) != null) {
			final String[] s = line.split(",");
			final long time = Long.parseLong(s[0]);
			final float value = Float.parseFloat(s[1]);
			impl1.add(time, value);
			impl2.add(time, value);
			Assert.assertEquals(impl1.get(time).variance(), impl2.get(time).variance(), AFixedTimeWindowTest.PRECISION);
		}
		br.close();
	}
	
	@Test
	public void testDeviation() throws Exception {
		final IFixedTimeWindow impl1 = this.create1();
		final IFixedTimeWindow impl2 = this.create2();
		final BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/cinovo/timeseries/test/test.data")));
		String line = null;
		while ((line = br.readLine()) != null) {
			final String[] s = line.split(",");
			final long time = Long.parseLong(s[0]);
			final float value = Float.parseFloat(s[1]);
			impl1.add(time, value);
			impl2.add(time, value);
			Assert.assertEquals(impl1.get(time).deviation(), impl2.get(time).deviation(), AFixedTimeWindowTest.PRECISION);
		}
		br.close();
	}
	
	@Test
	public void testMedian() throws Exception {
		final IFixedTimeWindow impl1 = this.create1();
		final IFixedTimeWindow impl2 = this.create2();
		final BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("de/cinovo/timeseries/test/test.data")));
		String line = null;
		while ((line = br.readLine()) != null) {
			final String[] s = line.split(",");
			final long time = Long.parseLong(s[0]);
			final float value = Float.parseFloat(s[1]);
			impl1.add(time, value);
			impl2.add(time, value);
			Assert.assertEquals(impl1.get(time).median(), impl2.get(time).median(), AFixedTimeWindowTest.PRECISION);
		}
		br.close();
	}
	
}
