package cn.gin.wctf.common.util;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * <p>用于保存 <code>Spring ApplicationContext</code> 的工具类。实现了 {@link ApplicationContextAware}
 * 接口，在 Spring 初始化时就将 Spring 上下文注入到当前对象中。在程序需要使用 <code>ApplicationContext</code> 的时候
 * 可以直接过本类获取。</p>
 * 
 * @author Gintoki
 * @date 2017-10-05
 */
@Service
@Lazy(false)
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {
	
	/**
	 * The default root logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	/**
	 * 当前应用的 Spring ApplicationContext 实例 
	 */
	private static ApplicationContext applicationContext = null;
	
	// -------------------------
	// Override
	// -------------------------
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.applicationContext = applicationContext;
	}
	
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}
	
	// -------------------------
	// Service
	// -------------------------
	
	/**
	 * <p>获取存储在静态变量中的 <code>ApplicationContext</code>。</p>
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}
	
	/**
	 * <p>从 <code>ApplicationContext</code> 中获取指定 name 的 Bean。</p>
	 * 
	 * @param name - 获取的 Bean 的 name 属性
	 * @return 指定的 Bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * <p>从 <code>ApplicationContext</code> 中获取指定类型的 Bean。</p>
	 * 
	 * @param requiredType - 获取的 Bean 的类型
	 * @return 指定的 Bean
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}
	
	/**
	 * <p>清除当前类持有的 <code>ApplicationContext</code> 实例。
	 */
	public static void clearHolder() {
		if (logger.isDebugEnabled()){
			logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		}
		applicationContext = null;
	}
	
	// -------------------------
	// Support
	// -------------------------
	
	/**
	 * 在使用 <code>ApplicationContext</code> 之前进行非空校验，如果为空则抛出异常。
	 */
	private static void assertContextInjected() {
		Validate.validState(applicationContext != null, "applicaitonContext属性未注入, 请在spring配置文件中中定义SpringContextHolder.");
	}

}
