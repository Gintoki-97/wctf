package cn.gin.wctf.common.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>随机数生成器工具类，用于产生线程安全的随机数。</p>
 * 
 * @author Gintoki
 * @version 2017-10-01
 */
public class RandomUtils {

	/**
	 * <p>返回一个 [0, max) 范围内整型随机数。</p>
	 * 
	 * @param max - 随机数的上界，不包含该上界
	 * @return [0, max) 范围内整型随机数
	 */
	public static int nextInt(int max) {
		return ThreadLocalRandom.current().nextInt(max);
	}
	
	/**
	 * <p>返回一个 [min, max) 范围内整型随机数。</p>
	 * 
	 * @param min - 随机数的下界，包含该下界
	 * @param max - 随机数的上界，不包含该上界
	 * @return [min, max) 范围内整型随机数
	 */
	public static int nextInt(int min, int max) {
		if(min < 0 || max < 0 || max < min) {
			throw new IllegalArgumentException("The random number's min and max was wrong");
		}
		return ThreadLocalRandom.current().nextInt(max - min) + min;
	}
	
}
