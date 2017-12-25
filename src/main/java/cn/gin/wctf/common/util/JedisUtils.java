package cn.gin.wctf.common.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

/**
 * <p>使用 Jedis 操作 Redis 数据库的工具类。适用于集群生产环境下的缓存支持类，因为 Shiro 的自定义 Session 模块需要一个 Session 持久化方案，而
 * 工作与 JVM 之内的 EhCache 是很难完成不同 JVM 之间的缓存同步的。Redis 作为一个独立的内存缓存与数据持久化系统，很容易实现这个功能</p>
 * <p>单机生产环境下可以参考 {@link CacheUtils}，使用 EhCache 实现的缓存支持类。</p>
 * 
 * @author Gintoki
 * @version 2017-10-15
 */
public class JedisUtils {

	/**
	 * The default root logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);
 
	/**
	 * 常用缓存时间
	 */
	public static final int ONE_DAY = 86400;
	
	/**
	 * jedis 连接池
	 **/
	private static JedisPool jedisPool = SpringContextHolder.getBean(JedisPool.class);

	/**
	 * 获取String缓存，在默认的redis 默认索引库中
	 * @param key 缓存键
	 * @return
	 */
	public static String get(String key) {
		return get(key, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 获取String缓存，在指定的redis数据库中
	 * @param key 缓存键
	 * @param index 数据库的索引下标
	 * @return 
	 */
	public static String get(String key, RedisIndex index) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {} from redis", key, value);
			}
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis); // 归还资源
		}
		return value;
	}
	
	/**
	 * 获取List缓存
	 * @param key 缓存键
	 * @return 值
	 */
	public static List<String> getList(String key) {
		return getList(key, RedisIndex.SYSTEM_CACHE);
	}

	/**
	 * 获取List缓存
	 * @param key 缓存键
	 * @param index 数据库的索引下标
	 * @return 值
	 */
	public static List<String> getList(String key, RedisIndex index) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(key)) {
				value = jedis.lrange(key, 0, -1);
				logger.debug("getList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getList {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取Set缓存
	 * @param key 缓存键
	 * @return 值
	 */
	public static Set<String> getSet(String key) {
		return getSet(key, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 获取Set缓存
	 * @param key 缓存键
	 * @param index 数据库的索引下标
	 * @return 值
	 */
	public static Set<String> getSet(String key, RedisIndex index) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(key)) {
				value = jedis.smembers(key);
				logger.debug("getSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getSet {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取Map缓存
	 * @param key 缓存键
	 * @return 值
	 */
	public static Map<String, String> getMap(String key) {
		return getMap(key, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 获取Map缓存
	 * @param key 缓存键
	 * @param index 数据库的索引下标
	 * @return 值
	 */
	public static Map<String, String> getMap(String key, RedisIndex index) {
		Map<String, String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(key)) {
				value = jedis.hgetAll(key);
				logger.debug("getMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getMap {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取Object缓存
	 * @param key 缓存键
	 * @return 值
	 */
	public static Object getObject(String key) {
		return getObject(key, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 获取Object缓存
	 * @param key 缓存键
	 * @param index 数据库的索引下标
	 * @return 值
	 */
	public static Object getObject(String key, RedisIndex index) {
		Object value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(getBytesKey(key))) {
				value = toObject(jedis.get(getBytesKey(key)));
				logger.debug("getObject {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObject {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取Object List缓存
	 * @param key 缓存键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key) {
		return getObjectList(key, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 获取Object List缓存
	 * @param key 缓存键
	 * @param index 数据库的索引下标
	 * @return 值
	 */
	public static List<Object> getObjectList(String key, RedisIndex index) {
		List<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(getBytesKey(key))) {
				List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectList {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 获取Object Set缓存
	 * @param key 缓存键
	 * @return 值
	 */
	public static Set<Object> getObjectSet(String key) {
		return getObjectSet(key, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 获取Object Set缓存
	 * @param key 缓存键
	 * @param index 数据库的索引下标
	 * @return 值
	 */
	public static Set<Object> getObjectSet(String key, RedisIndex index) {
		Set<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(getBytesKey(key))) {
				value = Sets.newHashSet();
				Set<byte[]> set = jedis.smembers(getBytesKey(key));
				for (byte[] bs : set) {
					value.add(toObject(bs));
				}
				logger.debug("getObjectSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectSet {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取Object Map缓存
	 * @param key 缓存键
	 * @return 值
	 */
	public static Map<String, Object> getObjectMap(String key) {
		return getObjectMap(key, RedisIndex.SYSTEM_CACHE);
	}

	/**
	 * 获取Object Map缓存
	 * @param key 缓存键
	 * @param index 数据库的索引下标
	 * @return 值
	 */
	public static Map<String, Object> getObjectMap(String key, RedisIndex index) {
		Map<String, Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(getBytesKey(key))) {
				value = Maps.newHashMap();
				Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
				for (Map.Entry<byte[], byte[]> e : map.entrySet()) {
					value.put(StringUtils.toString(e.getKey()), toObject(e.getValue()));
				}
				logger.debug("getObjectMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectMap {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 设置缓存数据
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @return 是否设置成功，redis格式
	 */
	public static String set(String key, String value, int timeout) {
		return set(key, value, timeout, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 设置缓存数据
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @param index 数据库的索引下标
	 * @return 是否设置成功，redis格式
	 */
	public static String set(String key, String value, int timeout, RedisIndex index) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			result = jedis.set(key, value);
			if (timeout != 0) {
				jedis.expire(key, timeout);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("set {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置List缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @return
	 */
	public static long setList(String key, List<String> value, int timeout) {
		return setList(key, value, timeout, RedisIndex.SYSTEM_CACHE);
	}

	/**
	 * 设置List缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @param index 数据库的索引下标
	 * @return
	 */
	public static long setList(String key, List<String> value, int timeout, RedisIndex index) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.rpush(key, (String[]) value.toArray());
			if (timeout != 0) {
				jedis.expire(key, timeout);
			}
			logger.debug("setList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setList {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Set缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int timeout) {
		return setSet(key, value, timeout, RedisIndex.SYSTEM_CACHE);
	}

	/**
	 * 设置Set缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @param index 数据库的索引下标
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int timeout, RedisIndex index) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.sadd(key, (String[]) value.toArray());
			if (timeout != 0) {
				jedis.expire(key, timeout);
			}
			logger.debug("setSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSet {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Map缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @return
	 */
	public static String setMap (String key, Map<String, String> value, int timeout) {
		return setMap(key, value, timeout, RedisIndex.SYSTEM_CACHE);
	}

	/**
	 * 设置Map缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @param index 数据库的索引下标
	 * @return
	 */
	public static String setMap(String key, Map<String, String> value, int timeout, RedisIndex index) {
		if(value.size() > 0) {
			String result = null;
			Jedis jedis = null;
			try {
				jedis = getResource();
				jedis.select(index.getIndex());
				if (jedis.exists(key)) {
					jedis.del(key);
				}
				result = jedis.hmset(key, value);
				if (timeout != 0) {
					jedis.expire(key, timeout);
				}
				logger.debug("setMap {} = {}", key, value);
			} catch (Exception e) {
				logger.warn("setMap {} = {}", key, value, e);
			} finally {
				jedisPool.returnResource(jedis);
			}
			return result;
		}
		return StringUtils.EMPTY;
	}

	/**
	 * 设置对象缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @return
	 */
	public static String setObject(String key, Object value, int timeout) {
		return setObject(key, value, timeout, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 设置对象缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 数据的超时时间，0为不超时
	 * @param index 数据库的索引下标
	 * @return
	 */
	public static String setObject(String key, Object value, int timeout, RedisIndex index) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			result = jedis.set(getBytesKey(key), toBytes(value));
			if (timeout != 0) {
				jedis.expire(key, timeout);
			}
			logger.debug("setObject {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObject {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置Object List缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout timeout 数据的超时时间，0为不超时
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value, int timeout) {
		return setObjectList(key, value, timeout, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 设置Object List缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout timeout 数据的超时时间，0为不超时
	 * @param index 数据库的索引下标
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value, int timeout, RedisIndex index) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value) {
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][]) list.toArray());
			if (timeout != 0) {
				jedis.expire(key, timeout);
			}
			logger.debug("setObjectList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectList {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Set缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectSet(String key, Set<Object> value, int timeout) {
		return setObjectSet(key, value, timeout, RedisIndex.SYSTEM_CACHE);
	}

	/**
	 * 设置Set缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 超时时间，0为不超时
	 * @param index 数据库的索引下标
	 * @return
	 */
	public static long setObjectSet(String key, Set<Object> value, int timeout, RedisIndex index) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value) {
				set.add(toBytes(o));
			}
			result = jedis.sadd(getBytesKey(key), (byte[][]) set.toArray());
			if (timeout != 0) {
				jedis.expire(key, timeout);
			}
			logger.debug("setObjectSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectSet {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 设置Map缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 超时时间，0为不超时
	 * @return
	 */
	public static String setObjectMap(String key, Map<String, Object> value, int timeout) {
		return setObjectMap(key, value, timeout, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 设置Map缓存
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeout 超时时间，0为不超时
	 * @param index 数据库的索引下标
	 * @return
	 */
	public static String setObjectMap(String key, Map<String, Object> value, int timeout, RedisIndex index) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()) {
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>) map);
			if (timeout != 0) {
				jedis.expire(key, timeout);
			}
			logger.debug("setObjectMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectMap {} = {}", key, value, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 删除默认缓存区中的指定缓存
	 * @param key 缓存键
	 * @return
	 */
	public static long del(String key) {
		return del(key, RedisIndex.SYSTEM_CACHE);
	}
	
	/**
	 * 删除默认缓存区中的指定缓存
	 * @param key 缓存键
	 * @return
	 */
	public static long del(String key, RedisIndex index) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.select(index.getIndex());
			if (jedis.exists(key)){
				result = jedis.del(key);
				logger.debug("del {}", key);
			}else{
				logger.debug("del {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("del {}", key, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 移除 Map 缓存中的值
	 * @param key - Map对应的键
	 * @param mapKey - 要删除Map中缓存的键
	 * @return
	 */
	public static long mapRemove(String key, String mapKey) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hdel(key, mapKey);
			logger.debug("mapRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapRemove {}  {}", key, mapKey, e);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
	

	/**
	 * 获取redis资源
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (JedisException e) {
			logger.warn("getResource()", e);
			jedisPool.returnBrokenResource(jedis);
			throw e;
		}
		return jedis;
	}

	/**
	 * 获取byte[]类型Key
	 * @param key
	 * @return
	 */
	public static byte[] getBytesKey(Object object) {
		if (object instanceof String) {
			return StringUtils.getBytes((String) object);
		} else {
			return ObjectUtils.serialize(object);
		}
	}

	/**
	 * 获取byte[]类型Key
	 * @param key
	 * @return
	 */
	public static Object getObjectKey(byte[] key) {
		try {
			return StringUtils.toString(key);
		} catch (UnsupportedOperationException e) {
			try {
				return JedisUtils.toObject(key);
			} catch (UnsupportedOperationException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Object转换byte[]类型
	 * @param key
	 * @return
	 */
	public static byte[] toBytes(Object object) {
		return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * @param key
	 * @return
	 */
	public static Object toObject(byte[] bytes) {
		return ObjectUtils.unserialize(bytes);
	}
	
	public static void returnResource(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}

}
