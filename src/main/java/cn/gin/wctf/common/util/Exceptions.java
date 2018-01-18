package cn.gin.wctf.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>异常信息转换类，用于进行异常的友好信息提示。</p>
 * 
 * @author Gintoki
 * @version 2017-11-23
 */
public class Exceptions {

	/**
	 * <p>将<code> CheckedException </code>转换为<code> UncheckedException </code>。</p>
	 * 
	 * @param e - 源异常，需要被转换为运行时异常
	 * return 被转换后的运行时异常
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}
	
	/**
	 * <p>将<code> ErrorStack </code>转化为<code> String </code。</p>
	 * 
	 * @param e - 异常栈中的根异常
	 * return 指定异常所链接的异常栈字符串信息
	 */
	public static String getStackTraceAsString(Throwable e) {
		if (e == null){
			return "";
		}
		StringWriter stringWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}
	
	/**
	 * <p>判断异常是否由某些底层的异常引起。</p>
	 */
	@SafeVarargs
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}
	
	/**
	 * <p>在<code> Request </code>中获取异常类。</p>
	 * 
	 * @param request - 需要从该请求对象中读取异常信息
	 * @return 读取出的异常信息
	 */
	public static Throwable getThrowable(HttpServletRequest request){
		Throwable ex = null;
		if (request.getAttribute("exception") != null) {
			ex = (Throwable) request.getAttribute("exception");
		} else if (request.getAttribute("javax.servlet.error.exception") != null) {
			ex = (Throwable) request.getAttribute("javax.servlet.error.exception");
		}
		return ex;
	}
}
