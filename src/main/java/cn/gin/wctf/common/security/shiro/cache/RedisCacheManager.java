package cn.gin.wctf.common.security.shiro.cache;

import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Redis 使用的缓存管理器。</p>
 * 
 * @author Gintoki
 * @version 2017-11-15
 */
public class RedisCacheManager implements CacheManager {

	/**
	 * The default root logger.
	 */
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);
    
    /**
     * 缓存键的前缀，用于区分不同模块的数据
     */
    private String cacheKeyPrefix = "shiro_cache_";
    
    /**
     * 默认无参构造器。
     */
    public RedisCacheManager() {}
    
	public String getCacheKeyPrefix() {
		return cacheKeyPrefix;
	}
	
	public void setCacheKeyPrefix(String cacheKeyPrefix) {
		this.cacheKeyPrefix = cacheKeyPrefix;
	}
	 
	/**
     * 从缓存管理器中加载一个已经存在的缓存区，如果没有获取到，则创建一个新的缓存区。
     */
	public <K, V> RedisCache<K, V> getCache(String name) throws CacheException {
		if(logger.isDebugEnabled()) {
			logger.debug("Get cache named [{}{}]", cacheKeyPrefix, name);
		}
		return new RedisCache<K, V> (cacheKeyPrefix + name);
	}
}
