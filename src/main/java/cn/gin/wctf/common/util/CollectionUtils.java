package cn.gin.wctf.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * <p>集合工具类</p>
 * 
 * @author Gintoki
 * @version 2017-10-01
 */
public class CollectionUtils {

	/**
	 * <p>将目标集合中的对象的两个属性，通过调用它们的 get 方法提取到属性值并将第一个属性值作为键，第二个属性值作为值
	 * 存储到Map中。</p>
	 * 
	 * @param collection - 目标集合
	 * @param keyPropertyName - 目标集合中对象的指定属性值，会作为 Map 的键
	 * @param valuePropertyName - 目标集合中对象的指定属性值，会作为 Map 的值
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map extractToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map map = new HashMap(collection.size());

		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName),
						PropertyUtils.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}

		return map;
	}
	
	/**
	 * <p>将目标集合中的对象的指定属性对应的属性值，提取出来组合成为一个 List。</p>
	 * 
	 * @param collection - 目标集合
	 * @param propertyName - 目标集合中对象的指定属性值
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List extractToList(final Collection collection, final String propertyName) {
		List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw Reflections.convertReflectionExceptionToUnchecked(e);
		}
		return list;
	}
	
	/**
	 * <p>将目标集合中的对象的指定属性对应的属性值，提取出来组合成一个由指定分隔符分隔的字符串。</p>
	 * 
	 * @param collection - 目标集合
	 * @param propertyName - 目标集合中对象的指定属性值
	 * @param separator - 属性值之间的分隔符
	 * @return 由指定分隔符分隔的属性值集合字符串
	 */
	@SuppressWarnings("rawtypes")
	public static String extractToString(final Collection collection, String propertyName, String separator) {
		List list = extractToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}
	
	/**
	 * <p>将目标集合中的每一个对象（通常是一个字符串），追加一个前缀和后缀，并将提取后的新的对象组合为一个新的字符串。</p>
	 * 
	 * @param collection - 目标集合
	 * @param prefix - 为每个对象添加的字符串前缀
	 * @param postfix - 为每个对象添加的字符串后缀
	 * @return 提取的新的对象组成的字符串
	 */
	@SuppressWarnings("rawtypes")
	public static String convertToString(final Collection collection, final String prefix, final String postfix) {
		StringBuilder builder = new StringBuilder();
		for (Object o : collection) {
			builder.append(prefix).append(o).append(postfix);
		}
		return builder.toString();
	}
	
	/**
	 * <p>判断一个集合是否是 null 或者容量为空。</p>
	 * 
	 * @param collection - 目标容器
	 * @return 目标容器是否为空
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}

}
