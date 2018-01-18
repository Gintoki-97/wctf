package cn.gin.wctf.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.google.common.collect.Maps;

import cn.gin.wctf.common.util.StringUtils;

/**
 * <p>应用全局配置相关的工具类。在应用启动时，会触发当前类加载全局配置文件，并且在应用各处都可以通过此类获取配置文件中的指定项。
 * 底层使用 Spring 的配置文件加载器。</p>
 * 
 * @author Gintoki
 * @version 2017-10-04
 */
public class Global {
	
	/**
	 * The default root logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Global.class);
	
	/** 应用通用转义字符 */
	public static final String YES = "1";
	/** 应用通用转义字符 */
	public static final String NO = "0";
	/** 应用通用转义字符 */
	public static final String TRUE = "true";
	/** 应用通用转义字符 */
	public static final String FALSE = "false";
	
	/**
	 * 全局配置文件
	 */
	private static HashMap<String, String> map = Maps.newHashMap();
	
	/**
	 * 全局配置文件加载器
	 */
	private static PropertiesLoader loader = new PropertiesLoader("wctf.properties");
	
	/**
	 * 单例
	 */
	private static Global global = new Global();
	
	private Global() {}
	
	/**
	 * 获取当前对象实例
	 */
	public static Global getInstance() {
		return global;
	}
	
	/**
	 * 获取指定属性键对应的值
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = loader.getProperty(key);
			map.put(key, value);
		}
		return value;
	}
	
	/**
	 * 获取指定属性键对应的值
	 */
	public static void putConfig(String key, String value) {
		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			throw new IllegalArgumentException("Connot put the null key or value.");
		}
		map.put(key, value);
	}
	
	/**
	 * Properties 配置文件加载器
	 * 
	 * @author Gintoki
	 * @version 2017-10-04
	 */
	public static class PropertiesLoader {
		
		// 使用 Spring 的资源文件加载器
		private static ResourceLoader resourceLoader = new DefaultResourceLoader();
		
		/**
		 * 全局 Properties 配置文件
		 */
		private final Properties properties;
		
		/**
		 * 获取全局 Properties 配置文件
		 */
		public Properties getProperties() {
			return properties;
		}
		
		/**
		 * 批量加载 Properties 配置文件
		 * 
		 * @param resourcesPaths - Properties 配置文件的路径
		 */
		public PropertiesLoader(String... resourcesPaths) {
			properties = loadProperties(resourcesPaths);
		}
		
		/**
		 * 取出 key 对应的 property，如果没有返回空字符串
		 */
		public String getProperty(String key) {
			String property = getProperty0(key);
			return property == null ? "" : property;
		}
		
		/**
		 * 取出 key 对应的 property，如果没有返回默认值
		 */
		public String getProperty(String key, String defaultValue) {
			String property = getProperty0(key);
			return property == null ? defaultValue : property;
		}
		
		/**
		 * 取出 key 对应的 property，如果没有则抛出异常
		 */
		public String getPropertyRequired(String key) {
			String property = getProperty0(key);
			if(property == null) {
				throw new NoSuchElementException("Cannot find this property with key:" + key);
			}
			return property;
		}
		
		// --------------------
		// Support
		// --------------------
		
		/*
		 * 载入多个配置文件
		 */
		private Properties loadProperties(String... resourcesPaths) {
			Properties props = new Properties();
			for (String location : resourcesPaths) {
				logger.debug("Loading properties file from:" + location);
				InputStream is = null;
				try {
					Resource res = resourceLoader.getResource(location);
					is = res.getInputStream();
					props.load(is);
				} catch(IOException e) {
					logger.info("Could not load properties from location:" + location + ", " + e.getMessage());
				} finally {
					IOUtils.closeQuietly(is);	//关闭资源流
				}
			}
			return props;
		}
		
		/*
		 * 获取指定属性键对应的值，如果没有返回 null。
		 * 优先从 System 和  Properties 中取出  key 对应的属性值 ，如果没有返回 null
		 */
		private String getProperty0(String key) {
			String property = System.getProperty(key);
			if(property != null) {
				return property;
			}
			property =  properties.getProperty(key);
			return property;
		}
	}
}
