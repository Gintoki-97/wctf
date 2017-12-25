package cn.gin.wctf.common.util;

/**
 * Redis 索引库枚举类，每个索引库枚举都对应着 Redis 中相应数据库的下标。
 * 
 * @author Gintoki
 * @version 2017-11-04
 */
public enum RedisIndex {

	/**
	 * 	系统缓存
	 */
	SYSTEM_CACHE(0),
	
	/**
	 * session 缓存
	 */
	SESSION_CACHE(1),
	
	/**
	 * 用户缓存
	 */
	USER_CACHE(2),
	
	/**
	 * 发贴缓存
	 */
	POST_CACHE(3),
	
	/**
	 * 新闻缓存
	 */
	NEWS_CACHE(4);
	
	// -----------------
	// Support
	// -----------------
	
	/**
	 * 用于支持 Jedis 选择指定下标
	 */
	private int index;
	
	private RedisIndex(int index) {
		this.index = index;
	}
	
	// -----------------
	// Service
	// -----------------
	
	/**
	 * <p>获取当前索引库对应的下标，每个索引库枚举都对应了一个唯一的索引库下标。</p>
	 * 
	 * @return 当前索引库对应的下标
	 */
	public int getIndex() {
		return index;
	}
}
