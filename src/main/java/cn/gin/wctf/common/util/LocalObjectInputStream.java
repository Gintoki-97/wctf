package cn.gin.wctf.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;

/**
 * <p>用于支持序列化及反序列化相关操作的的 ObjectInputStream。Tomcat 默认使用的类加载器是
 * <code> org.apache.catalina.loader.WebAppClassLoader </code>， 而 JVM 
 * 默认使用<code> sun.misc.VM </code>。</p>
 * <p>当前类继承自 {@link ObjectInputStream} 并覆盖了resolveClass()，会统一使用当前
 * 线程的类加载器来进行序列化相关类的加载。以解决 <code>ClassNotFoundException</code>。</p>
 * 
 * @author Gintoki
 * @version 2017-11-10
 */
public class LocalObjectInputStream extends ObjectInputStream {

	/**
	 * 基本数据类型的初始化映射表。
	 */
	private static final HashMap<String, Class<?>> primClasses = new HashMap<String, Class<?>>(8, 1.0F);
	
	/**
	 * 序列化相关操作使用的默认类加载器。
	 */
	private ClassLoader loader = this.getClass().getClassLoader();
	
	/*
	 * 初始化基本数据类型映射表。
	 */
	static {
		primClasses.put("boolean", boolean.class);
		primClasses.put("byte", byte.class);
		primClasses.put("char", char.class);
		primClasses.put("short", short.class);
		primClasses.put("int", int.class);
		primClasses.put("long", long.class);
		primClasses.put("float", float.class);
		primClasses.put("double", double.class);
		primClasses.put("void", void.class);
    }
	
	public LocalObjectInputStream(InputStream in) throws IOException {
		super(in);
	}
	
	// ---------------------
	// Override
	// ---------------------
	
	protected LocalObjectInputStream() throws IOException, SecurityException {
		super();
	}
	
	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		String name = desc.getName();
        try {
            return Class.forName(name, false, loader);
        } catch (ClassNotFoundException ex) {
            Class<?> cl = primClasses.get(name);
            if (cl != null) {
                return cl;
            } else {
                throw ex;
            }
        }
	}
}
