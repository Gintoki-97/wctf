package cn.gin.wctf.common.util;

import java.util.Collection;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;

/**
 * <p>使用 EhCache 实现的缓存工具类。适用于集群生产环境下的缓存支持类。因为 EhCache 工作于线程之中，没有 Redis 的网络 IO 操作，运行速度相对较快。但在集群环境下，
 * EhCache 无法解决不同 JVM 之间缓存同步的问题，所以缓存模块选用何种技术要根据项目需求来决定。</p>
 * <p>集群生产环境下可以参考 {@link JedisUtils}，使用 Redis 实现的缓存支持类。</p>
 * 
 * @author Gintoki
 * @version 2017-10-09
 */
public class CacheUtils {

	/**
	 * The default root logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);
	
	/**
	 * 系统缓存，定义在ehcache配置文件中
	 */
	public static final String SYS_CACHE = "sysCache";
	
	/**
	 * 用户缓存，定义在ehcache配置文件中
	 */
	public static final String USER_CACHE = "userCache";
	
	/**
	 * 贴子缓存，定义在ehcache配置文件中
	 */
	public static final String POST_CACHE = "postCache";
	
	/**
	 * 新闻缓存，定义在ehcache配置文件中
	 */
	public static final String NEWS_CACHE = "newsCache";
	
	/**
	 * 简单页面缓存，定义在ehcache配置文件中
	 */
	public static final String PAGE_CACHING_FILTER = "pageCachingFilter";
	
	/**
	 * 缓存管理器，实现类为ehcache，配置在spring核心配置文件中
	 */
	private static CacheManager cacheManager = SpringContextHolder.getBean(EhCacheCacheManager.class);
	
	
	// ---------------------
	// Service
	// ---------------------
	
	/**
	 * <p>从 EhCache 的系统缓存区中获取指定缓存</p>
	 * 
	 * @param key - 获取的缓存的缓存键
	 * @return 系统缓存区中指定缓存键对应的缓存
	 */
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}
	
	/**
	 * <p>从 EhCache 的指定缓存区中获取指定缓存。</p>
	 * 
	 * @param cacheName - EhCache 中指定缓存区名
	 * @param key - 获取的缓存的缓存键
	 * @return 指定缓存区中指定缓存键对应的缓存
	 */
	public static Object get(String cacheName, String key) {
		if(logger.isDebugEnabled()) {
			logger.debug("Cache：find cache {} in {} with no default value", key, cacheName);
		}
		ValueWrapper valueWrapper = getCache(cacheName).get(key);
		if(valueWrapper == null) {return null;}
		Object res = valueWrapper.get();
		if(logger.isDebugEnabled()) {
			if(res != null)
				logger.debug("Cache：hit the cache {} in {} value {}", key, cacheName, res);
		}
		return res;
	}
	
	/**
	 * <p>从 EhCache 的指定缓存区中获取指定缓存，如果没有则返回默认值。</p>
	 * 
	 * @param cacheName - EhCache 中指定缓存区名
	 * @param key - 获取的缓存的缓存键
	 * @param defaultValue - 如果没有返回的默认值
	 * @return 指定缓存区中获取指定缓存，如果没有则返回默认值
	 */
	public static Object get(String cacheName, String key, Object defaultValue) {
		if(logger.isDebugEnabled()) {
			logger.debug("Cache：find cache {} in {} with default value {}", key, cacheName, defaultValue);
		}
		Object value = get(cacheName, key);
		return value != null ? value : defaultValue;
	}
	
	/**
	 * <p>将缓存放入 EhCache 的系统缓存区中。</p>
	 * 
	 * @param key - 缓存键
	 * @param value - 缓存值
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}
	
	/**
	 * <p>将缓存放入 EhCache 的指定缓存区中。</p>
	 * 
	 * @param cacheName - EhCache 中指定缓存区名
	 * @param key - 缓存键
	 * @param value - 缓存值
	 */
	public static void put(String cacheName, String key, Object value) {
		getCache(cacheName).put(key, value);
	}
	
	/**
	 * <p>删除 EhCache 的指定缓存区中的指定缓存。</p>
	 * 
	 * @param cacheName - EhCache 中指定缓存区名
	 * @param key - 指定的缓存键
	 */
	public static void remove(String cacheName, String key) {
		getCache(cacheName).evict(key);
	}
	
	/**
	 * <p>清空 EhCache 的指定缓存区中的所有缓存。</p>
	 * 
	 * @param cacheName - EhCache 中指定缓存区名
	 */
	public static void removeAll(String cacheName) {
		Cache cache = getCache(cacheName);
		Collection<String> keys = cacheManager.getCacheNames();
		for (Iterator<String> it = keys.iterator(); it.hasNext();){
			cache.evict(it.next());
		}
		logger.info("清理缓存： {} => {}", cacheName, keys);
	}
	
	// ---------------------
	// Support
	// ---------------------
	
	/**
	 * <p>获取 EhCache 的指定缓存区。</p>
	 * 
	 * @param cacheName - EhCache 中指定缓存区名
	 * @return 指定缓存区
	 */
	private static Cache getCache(String cacheName){
		Cache cache = CacheUtils.cacheManager.getCache(cacheName);
		if (cache == null){
			throw new RuntimeException("当前系统中没有定义当前缓存：" + cacheName);
		}
		return cache;
	}
	
}
