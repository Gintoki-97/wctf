package cn.gin.wctf.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

import org.apache.commons.io.IOUtils;

/**
 * <p>操作 {@code Object} 的工具类。</p>
 * 
 * @author Gintoki
 * @version 2017-10-15
 */
public class ObjectUtils extends org.apache.commons.lang3.ObjectUtils {

	/**
	 * 注解到对象复制，只复制能匹配上的方法。
	 * 
	 * @param annotation
	 * @param object
	 */
	public static void annotationToObject(Object annotation, Object object) {
		if (annotation != null) {
			Class<?> annotationClass = annotation.getClass();
			if (null == object) {
				return;
			}
			Class<?> objectClass = object.getClass();
			for (Method m : objectClass.getMethods()) {
				if (StringUtils.startsWith(m.getName(), "set")) {
					try {
						String s = StringUtils.uncapitalize(StringUtils.substring(m.getName(), 3));
						Object obj = annotationClass.getMethod(s).invoke(annotation);
						if (obj != null && !"".equals(obj.toString())) {
							m.invoke(object, obj);
						}
					} catch (Exception e) {
						// 忽略所有设置失败方法
					}
				}
			}
		}
	}

	/**
	 * 将 {@code Object} 序列化为字节数组。
	 * 
	 * @param object - 被序列化的 Object
	 * @return 对象序列化后的字节数组
	 */
	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		try {
			if (object != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				return baos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字节数组反序列化为 {@code Object}。
	 * 
	 * @param bytes - 被反序列化的字节数组
	 * @return 字节数组反序列化后的对象
	 */
	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		LocalObjectInputStream ois = null;
		try {
			if (bytes != null && bytes.length > 0) {
				bais = new ByteArrayInputStream(bytes);
				ois = new LocalObjectInputStream(bais);
				Object obj = ois.readObject();
				return obj;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ois);
		}
		return null;
	}

}
