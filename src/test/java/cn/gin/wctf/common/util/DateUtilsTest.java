package cn.gin.wctf.common.util;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * 测试 DateUtils 工具类
 * 
 * @author Gintoki
 * @see DateUtils
 * @version 2017-10-03
 */
public class DateUtilsTest {

	private static AtomicInteger count = new AtomicInteger(1);
	
	@Rule
	public TestName name = new TestName();
	
	private void runAs(Object result) {
		String mName = name.getMethodName();
		mName = mName.substring(4, mName.length());
		System.out.println("Test " + count + " : " +  mName + "\r\n\t==>\t The result:" + result.toString());
		count.getAndIncrement();
	}
	
	// -----------------------
	// Test
	// -----------------------
	
	@Test
	public void testGetDate() {
		this.runAs(DateUtils.getDate());
	}
	
	@Test
	public void testGetDateWithParams() {
		this.runAs(DateUtils.getDate(new Date(), "yyyy%MM%dd HH/mm/ss"));
	}
	
	@Test
	public void testGetYear() {
		this.runAs(DateUtils.getYear());
	}
	
	@Test
	public void testGetMonth() {
		this.runAs(DateUtils.getMonth());
	}
	
	@Test
	public void testGetDay() {
		this.runAs(DateUtils.getDay());
	}
	
	@Test
	public void testGetHours() {
		this.runAs(DateUtils.getHours());
	}
	
	@Test
	public void testGetMinutes() {
		this.runAs(DateUtils.getMinutes());
	}
	
	@Test
	public void testGetSeconds() {
		this.runAs(DateUtils.getSeconds());
	}
	
	@Test
	public void testPastDays() {
		this.runAs(DateUtils.pastDays(new Date()));
	}
	
	@Test
	public void testPastHours() {
		this.runAs(DateUtils.pastDays(new Date()));
	}
	
	@Test
	public void testPastMinutes() {
		this.runAs(DateUtils.pastDays(new Date()));
	}
	
	@Test
	public void testPastSeconds() {
		this.runAs(DateUtils.pastDays(new Date()));
	}
	
	@Test
	public void testParseDate() {
		this.runAs(DateUtils.parseDate("2007/08/09", "yyyy/MM/dd"));
	}
}
