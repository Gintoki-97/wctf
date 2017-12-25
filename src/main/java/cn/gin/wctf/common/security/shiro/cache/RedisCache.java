package cn.gin.wctf.common.security.shiro.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.web.Servlets;
import redis.clients.jedis.Jedis;

/**
 * <p>Redis 使用的缓存表示类，用于支持 Shiro 中的缓存模块。</p>
 * 
 * @author Gintoki
 * @param <K> - 缓存键
 * @param <V> - 缓存值
 * @version 2017-11-15
 */
public class RedisCache<K, V> implements Cache<K, V> {

	/**
	 * The default root logger.
	 */
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
    
    /**
     * The wrapped.
     */
    private String cacheKeyName = null;
    
    /**
     * <p>通过给定的 RedisCache 返回一个 Shiro cache 的包装类实例。</p>
     * 
     * @param cache - 被封装的 RedisCache
     */
    public RedisCache(String cacheKeyName) {
    	if (cacheKeyName == null) {
    		throw new IllegalArgumentException("Cache argument cannot be null.");
    	}
    	this.cacheKeyName = cacheKeyName;
    }
    
    /**
     * <p>通过给定的缓存键获取对应的值。</p>
     *
     * @param key - 缓存键
     * @return 缓存中缓存键对应的值，如果没找到或者已过时则返回 null。
     */
	@SuppressWarnings("unchecked")
	public V get(K key) throws CacheException {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Getting object from cache [{}] for key [{}]", cacheKeyName, key);
	        }
            if (key == null) {
                return null;
            } else {
            	HttpServletRequest request = Servlets.getRequest();
            	V v = null;
            	if (request != null){
    				v = (V) request.getAttribute(cacheKeyName);
    				if (v != null){
    					return v;
    				} else {
    					v = null;
    				}
    			}
            	Jedis jedis = null;
            	try {
    				jedis = JedisUtils.getResource();
    				v = (V) JedisUtils.toObject(jedis.hget(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key)));
    				if (logger.isDebugEnabled()) {
    					logger.debug("Get from the cache [{}], by key [{}], request uri [{}]", cacheKeyName, key, request != null ? request.getRequestURI() : "");
    		        }
    			} catch (Exception e) {
    				logger.error("Get from the cache [{}], request uri [{}], exception [{}]", cacheKeyName, key, request != null ? request.getRequestURI() : "", e);
    			} finally {
    				JedisUtils.returnResource(jedis);
    			}
            	if (request != null && v != null){
    				request.setAttribute(cacheKeyName, v);
    			}
            	return v;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
	}

	/**
     * <p>将一个对象放入到缓存中。</p>
     * 
     * @param key -  缓存键
     * @param value - 缓存值
     */
	public V put(K key, V value) throws CacheException {
		if(key == null) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Putting object in cache [{}], for key [{}]", cacheKeyName, key);
        }
		Jedis jedis = null;
        try {
        	jedis = JedisUtils.getResource();
			jedis.hset(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key), JedisUtils.toBytes(value));
			if (logger.isDebugEnabled()) {
				logger.debug("Putting object in cache [{}], for key-value [{},{}]", cacheKeyName, key, value);
	        }
        } catch (Exception e) {
        	if(logger.isErrorEnabled()) {
        		logger.error("Putting object in cache [{}], for key-value [{},{}], exception {}", cacheKeyName, key, value, e  );
        	}
        } finally {
			JedisUtils.returnResource(jedis);
		}
        return value;
	}

	/**
     * <p>移除与给定缓存键匹配的对应缓存，如果没有匹配的缓存，程序不会移除任何元素，并且不会抛出异常。</p>
     * 
     * @param key - 需要被移除的缓存的缓存键
     */
	@SuppressWarnings("unchecked")
	public V remove(K key) throws CacheException {
		V value = null;
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			value = (V) JedisUtils.toObject(jedis.hget(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key)));
			jedis.hdel(JedisUtils.getBytesKey(cacheKeyName), JedisUtils.getBytesKey(key));
			if (logger.isDebugEnabled()) {
				logger.debug("Removing object in cache [{}], for key-value [{},{}]", cacheKeyName, key, value);
	        }
		} catch (Exception e) {
			if (logger.isWarnEnabled()) {
				logger.warn("Removing object in cache [{}], for key-value [{},{}], exception {}", cacheKeyName, key, value, e);
	        }
		} finally {
			JedisUtils.returnResource(jedis);
		}
		return value;
	}

	/**
     * <p>移除缓存中所有缓存。</p>
     */
	public void clear() throws CacheException {
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			jedis.hdel(JedisUtils.getBytesKey(cacheKeyName));
			if (logger.isDebugEnabled()) {
				logger.debug("Clearing the cache [{}]", cacheKeyName);
	        }
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Clearing the cache [{}], exception {}", cacheKeyName, e);
	        }
		} finally {
			JedisUtils.returnResource(jedis);
		}
	}

	/**
	 * <p>获取指定缓存中所有的键的集合。</p>
	 */
	@SuppressWarnings("unchecked")
	public Set<K> keys() {
		Set<K> keys = Sets.newHashSet();
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			Set<byte[]> set = jedis.hkeys(JedisUtils.getBytesKey(cacheKeyName));
			for(byte[] key : set){
				Object obj = (K) JedisUtils.getObjectKey(key);
				if (obj != null){
					keys.add((K) obj);
				}
        	}
			if (logger.isErrorEnabled()) {
				logger.debug("Keys the cache [{}], keys [{}] ", cacheKeyName, keys);
			}
			return keys;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Keys the cache [{}], exception {}", cacheKeyName, e);
			}
		} finally {
			JedisUtils.returnResource(jedis);
		}
		return keys;
	}

	/**
	 * <p>获取指定缓存中所有的值的集合</p>
	 */
	@SuppressWarnings("unchecked")
	public Collection<V> values() {
		Collection<V> vals = Collections.emptyList();
		Jedis jedis = null;
		try {
			jedis = JedisUtils.getResource();
			Collection<byte[]> col = jedis.hvals(JedisUtils.getBytesKey(cacheKeyName));
			for(byte[] val : col){
				Object obj = JedisUtils.toObject(val);
				if (obj != null){
					vals.add((V) obj);
				}
        	}
			if (logger.isDebugEnabled()) {
				logger.debug("Values the cache [{}], keys [{}] ", cacheKeyName, vals);
			}
			return vals;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Values the cache [{}], exception {}", cacheKeyName, e);
			}
		} finally {
			JedisUtils.returnResource(jedis);
		}
		return vals;
	}

	/**
	 * <p>获取指定缓存区</p>
	 */
	public int size() {
		Jedis jedis = null;
		int size = 0;
		try {
			jedis = JedisUtils.getResource();
			size = jedis.hlen(JedisUtils.getBytesKey(cacheKeyName)).intValue();
			if (logger.isDebugEnabled()) {
				logger.debug("Size the cache [{}], keys [{}] ", cacheKeyName, size);
			}
			return size;
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Size the cache [{}], exception {}", cacheKeyName, e);
			}
		} finally {
			JedisUtils.returnResource(jedis);
		}
		return size;
	}

}
