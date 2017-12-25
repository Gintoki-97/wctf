package cn.gin.wctf.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * <p>Java 反射相关操作的工具类，包括调用 get/set 方法、访问私有变量、调用私有方法、获取泛型的类型、获取被 AOP 增强的
 * 真实类等等。</p>
 * 
 * @author Gintoki
 * @version 2017-10-05
 */
@SuppressWarnings("rawtypes")
public class Reflections {
	
	/**
	 * The default root logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Reflections.class);
	
	/** set 方法前缀  **/
	private static final String SETTER_PREFIX = "set";
	
	/** get 方法前缀  **/
	private static final String GETTER_PREFIX = "get";
	
	/** 代理类  **/
	private static final String CGLIB_CLASS_SEPARATOR = "$$";
	
	// ------------------
	// Service
	// ------------------

	/**
	 * <p>执行目标对象中指定属性对应的 get 方法，支持多及调用。</p>
	 * 
	 * @param obj - 目标对象
	 * @param propertyName - 调用的 get 方法所属的 JavaBean 属性
	 * @return 目标对象中指定属性对应的 get 方法的返回值
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		Object object = obj;
		for (String name : StringUtils.split(propertyName, ".")){
			String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
			object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
		}
		return object;
	}

	/**
	 * <p>执行目标对象中指定属性对应的 set 方法，支持多及调用。</p>
	 * 
	 * @param obj - 目标对象
	 * @param propertyName - 调用的 set 方法所属的 JavaBean 属性
	 * @param value - 传入 set 方法的参数
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		Object object = obj;
		String[] names = StringUtils.split(propertyName, ".");
		for (int i=0; i<names.length; i++){
			if(i<names.length-1){
				String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
				object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
			}else{
				String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
				invokeMethodByName(object, setterMethodName, new Object[] { value });
			}
		}
	}

	/**
	 * <p>直接读取目标对象中指定属性的属性值，不经过 get 方法，忽略 private/protected 关键字。</p>
	 * 
	 * @param obj - 目标对象
	 * @param fieldName - 指定属性的属性名
	 * @return 指定属性的属性值
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * <p>直接设置目标对象中指定属性的属性值，不经过 set 方法，忽略 private/protected 关键字。</p>
	 * 
	 * @param obj - 目标对象
	 * @param fieldName - 指定属性的属性名
	 * @param value - 设置的属性值
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * <p>直接调用目标对象的指定方法，排除方法重载的干扰，·	忽略 private/protected 关键字。</p>
	 * 
	 * @param obj - 目标对象
	 * @param methodName - 要执行的目标方法名
	 * @param parameterTypes - 参数类型
	 * @param args - 参数列表
	 * @return 目标方法返回值
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * <p>直接调用目标对象的指定方法，忽略 private/protected 关键字。</p>
	 * 
	 * @param obj - 目标对象
	 * @param methodName - 要执行的目标方法名
	 * @param args - 参数列表
	 * @return 目标方法返回值
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}
		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * <p>从目标类型开始查找与目标属性名匹配的属性字段，如果没找到就获取目标类型的父类继续执行该操作直到 Object，并将属性的访问权限设置为 public。</p>
	 * 
	 * @param obj - 目标类型
	 * @param fieldName - 查找的目标属性字段名
	 * @return - 目标属性字段对应的 <code>Field</code> 对象
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * <p>从目标类型开始查找与目标方法名和参数类型列表均匹配的方法，如果没找到就获取目标类型的父类继续执行该操作直到 Object，并将方法的访问权限设置为 public。</p>
	 * 
	 * @param obj - 目标类型
	 * @param methodName - 查找的目标方法名
	 * @param parameterTypes - 查找的目标方法参数类型列表
	 * @return - 目标方法
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * <p>从目标类型开始查找与目标方法名匹配的方法，如果没找到就获取目标类型的父类继续执行该操作直到 Object，并将方法的访问权限设置为 public。</p>
	 * 
	 * @param obj - 目标类型
	 * @param methodName - 查找的目标方法名
	 * @return - 目标方法
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");
		
		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * <p>修改将目标方法的访问权限为 public。</p>
	 * 
	 * @param field - 需要修改的目标方法
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * <p>修改将目标字段的访问权限为 public。</p>
	 * 
	 * @param field - 需要修改的目标字段
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
				.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * <p>获取目标类超类的第一个泛型参数的类型，如果找不到，返回 Object.class。</p>
	 * 
	 * @param clazz - 需要被操作的目标类
	 * @return 目标类超类的第一个泛型参数的类型，如果找不到，返回 Object.class
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * <p>获取目标类超类的泛型参数的类型，如果找不到，返回 Object.class。</p>
	 * 
	 * @param clazz - 需要被操作的目标类
	 * @param index - 泛型参数的下标，从 0 开始，在目标类超类中有多个泛型参数的情况下，需指定获取的泛型参数的下标
	 * @return 目标类超类的泛型参数的类型，如果找不到，返回 Object.class
	 */
	public static Class getSuperClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class) params[index];
	}
	
	/**
	 * <p>通过 Cglib 增强过的代理类，获取被增强的原目标类。</p>
	 * 
	 * @param proxy - 代理类
	 * @return 被增强的原目标类
	 */
	public static Class<?> getProxyTargetSClass(Object proxy) {
		Assert.notNull(proxy, "Instance must not be null");
		Class clazz = proxy.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;
	}
	
	/**
	 * <p>将编译时异常（受检查异常）转换为运行时异常（不受检查的异常）。</p>
	 * 
	 * @param e - 编译时异常
	 * @return 运行时异常
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
}
