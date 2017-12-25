package cn.gin.wctf.common.util;

/**
 * <p>用于支持验证码生成的工具类，提供了数字验证码，字符验证码，图片验证码，邮箱验证码等。</p>
 * 
 * @author Gintoki
 * @version 2017-10-01
 */
public class CaptchaUtils {
	
	/*
	 * 系统中的所有支持的验证码类型
	 */
	
	public static final String NUMBER_CAPTCHA = "NUMBER_CAPTCHA";
	public static final String LETTER_CAPTCHA = "LETTER_CAPTCHA";
	public static final String CHARS_CAPTCHA = "CHARS_CAPTCHA";
	public static final String IMAGE_CAPTCHA = "IMAGE_CAPTCHA";
	public static final String EMAIL_CAPTCHA = "EMAIL_CAPTCHA";
	
	/**
	 * 验证码字符，去除了容易引起混淆的字符。
	 */
	public static final char[] chars = 
			{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M',
			'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W','X', 'Y', 'Z', 
			'2', '3', '4', '5', '6', '7', '8', '9' };
	
	
	
	// -------------------------
	// Service
	// -------------------------
	
	
	/**
	 * <p>生成指定位数的数字验证码，只包含数字。</p>
	 * 
	 * @param digit - 验证码的位数
	 * @return 指定位数的数字验证码
	 */
	public synchronized static String buildNumbers(int digit) {
		if(digit <= 0 || digit >= 7) {
			throw new IllegalArgumentException("The captcha digit could only be [1, 6]");
		}
		String res = "";
		for (int i = 0; i < digit; i++) {
			res += String.valueOf(RandomUtils.nextInt(10));
		}
		return res;
	}
	
	/**
	 * <p>生成指定位数的字母验证码，只包含字母，不包含数字。</p>
	 * 
	 * @param digit - 验证码的位数
	 * @return 指定位数的字母验证码
	 */
	public synchronized static String buildLetters(int digit) {
		if(digit <= 0 || digit >= 7) {
			throw new IllegalArgumentException("The captcha digit could only be [1, 6]");
		}
		String res = "";
		for (int i = 0; i < digit; i++) {
			res += String.valueOf(chars[RandomUtils.nextInt(0, 23)]);
		}
		return res;
	}
	
	/**
	 * <p>生成指定位数的字符验证码，可能会包含字母和数字。</p>
	 * 
	 * @param digit - 验证码的位数
	 * @return 指定位数的字符验证码
	 */
	public synchronized static String buildChars(int digit) {
		if(digit <= 0 || digit >= 7) {
			throw new IllegalArgumentException("The captcha digit could only be [1, 6]");
		}
		String res = "";
		for (int i = 0; i < digit; i++) {
			res += String.valueOf(chars[RandomUtils.nextInt(chars.length)]);
		}
		return res;
	}
	
}
