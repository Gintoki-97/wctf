package cn.gin.wctf.common.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * 测试 CaptchaUtils 工具类
 * 
 * @author Gintoki
 * @see CaptchaUtils
 * @version 2017-10-03
 */
public class CaptchaUtilsTest {

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
	
	/*
	 * 测试数字验证码 - 正常
	 * 
	 * 测试用例：digit = 4
	 * 预期测试结果：正常
	 * 实际测试结果：正常
	 */
	@Test
	public void testBuildNumbers1() {
		for (int i = 0; i < 50; i++) {
			this.runAs(CaptchaUtils.buildNumbers(4));
		}
	}

	/*
	 * 测试数字验证码 - 边界测试 - 下边界
	 * 
	 * 测试用例：digit = 0
	 * 预期测试结果：异常
	 * 实际测试结果：异常
	 */
	@Test
	public void testBuildNumbers2() {
		this.runAs(CaptchaUtils.buildNumbers(0));
	}
	
	/*
	 * 测试数字验证码 - 边界测试 - 上边界
	 * 
	 * 测试用例：digit = 7
	 * 预期测试结果：异常
	 * 实际测试结果：异常
	 */
	@Test
	public void testBuildNumbers3() {
		this.runAs(CaptchaUtils.buildNumbers(7));
	}
	
	/*
	 * 测试字母验证码 - 正常
	 * 
	 * 测试用例：digit = 4
	 * 预期测试结果：正常
	 * 实际测试结果：正常
	 */
	@Test
	public void testBuildLetters1() {
		for (int i = 0; i < 50; i++) {
			this.runAs(CaptchaUtils.buildLetters(4));
		}
	}
	
	/*
	 * 测试字母验证码 - 边界测试 - 下边界
	 * 
	 * 测试用例：digit = 0
	 * 预期测试结果：异常
	 * 实际测试结果：异常
	 */
	@Test
	public void testBuildLetters2() {
		this.runAs(CaptchaUtils.buildLetters(0));
	}
	
	/*
	 * 测试字母验证码 - 边界测试 - 上边界
	 * 
	 * 测试用例：digit = 7
	 * 预期测试结果：异常
	 * 实际测试结果：异常
	 */
	@Test
	public void testBuildLetters3() {
		this.runAs(CaptchaUtils.buildLetters(7));
	}
	
	/*
	 * 测试字符验证码 - 正常
	 * 
	 * 测试用例：digit = 4
	 * 预期测试结果：正常
	 * 实际测试结果：正常
	 */
	@Test
	public void testBuildChars1() {
		for (int i = 0; i < 50; i++) {
			this.runAs(CaptchaUtils.buildChars(4));
		}
	}
	
	/*
	 * 测试字符验证码 - 边界测试 - 下边界
	 * 
	 * 测试用例：digit = 0
	 * 预期测试结果：异常
	 * 实际测试结果：异常
	 */
	@Test
	public void testBuildChars2() {
		this.runAs(CaptchaUtils.buildChars(0));
	}
	
	/*
	 * 测试字符验证码 - 边界测试 - 上边界
	 * 
	 * 测试用例：digit = 7
	 * 预期测试结果：异常
	 * 实际测试结果：异常
	 */
	@Test
	public void testBuildChars3() {
		this.runAs(CaptchaUtils.buildChars(7));
	}
}
