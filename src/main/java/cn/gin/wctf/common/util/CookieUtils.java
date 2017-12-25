package cn.gin.wctf.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p><code>Cookie</code> 工具类</p>
 * 
 * @author Gintoki
 * @version 2017-10-05
 */
public class CookieUtils {

	/**
	 * <code>Cookie</code> 常用过期时间，一般为一天。
	 */
	private static final int SECONDS_PER_DAY = 86400;
	
	/**
	 * <p>设置<code> Cookie </code>，使用默认的过期时间。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 * @param name - <code> Cookie </code> 的名
	 * @param value - <code> Cookie </code> 的值
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, SECONDS_PER_DAY);
	}
	
	/**
	 * <p>设置<code> Cookie </code>，为其指定路径，并使用默认的过期时间。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 * @param name - <code> Cookie </code> 的名
	 * @param value - <code> Cookie </code> 的值
	 * @param path - <code> Cookie </code> 的存储路径
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path) {
		setCookie(response, name, value, path, SECONDS_PER_DAY);
	}
	
	/**
	 * <p>设置<code> Cookie </code>，使用默认路径（“/”），并为其指定过期时间。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 * @param name - <code> Cookie </code> 的名
	 * @param value - <code> Cookie </code> 的值
	 * @param maxAge - <code> Cookie </code> 的存活时间
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
		setCookie(response, name, value, "/", maxAge);
	}
	
	/**
	 * <p>设置<code> Cookie </code>，为其指定路径，并为其指定过期时间。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 * @param name - <code> Cookie </code> 的名
	 * @param value - <code> Cookie </code> 的值
	 * @param maxAge - <code> Cookie </code> 的存活时间
	 * @param path - <code> Cookie </code> 的存储路径
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath(path);
		cookie.setMaxAge(maxAge);
		try {
			cookie.setValue(URLEncoder.encode(value, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.addCookie(cookie);
	}
	
	/**
	 * <p>获取指定名称的<code> Cookie </code></p>
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @param name - <code> Cookie </code> 的名
	 * @return 指定名称的<code> Cookie </code> 的值
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		return getCookie(request, null, name, false);
	}
	
	/**
	 * <p>获取指定名称的<code> Cookie </code>，并将其从<code> request </code>中移除。</p>
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @param response - {@link HttpServletResponse}
	 * @param name - <code> Cookie </code> 的名
	 * @return 指定名称的<code> Cookie </code> 的值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		return getCookie(request, response, name, true);
	}
	
	/**
	 * <p>获取指定名称的<code> Cookie </code>，可以根据需求将其从<code> request </code>中移除。</p>
	 * 
	 * @param request - {@link HttpServletRequest}
	 * @param response - {@link HttpServletResponse}
	 * @param name - <code> Cookie </code> 的名
	 * @param isRemove - 是否移除此<code> Cookie </code>
	 * @return 指定名称的<code> Cookie </code> 的值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemove) {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					try {
						value = URLDecoder.decode(cookie.getValue(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (isRemove) {
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
				}
			}
		}
		return value;
	}
}
