package cn.gin.wctf.common.web;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.net.HttpHeaders;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.util.StringUtils;

/**
 * Http 与 Servlet 相关操作工具类。
 * 
 * @author Gintoki
 * @version 2017-10-05
 */
public class Servlets {
	
	/**
     * The default root logger.
     */
	private static Logger logger = LoggerFactory.getLogger(Servlets.class);

	/* 静态文件后缀  */
	private final static String[] staticFileSuffix = StringUtils.split(Global.getConfig("web.staticFileSuffix"), ','); 
	
	/* 动态映射 URL 后缀  */
	private final static String urlSuffix = Global.getConfig("urlSuffix");
	
	
	// ---------------------
	// Service
	// ---------------------
	
	/**
	 * <p>获取本次请求对应的<code> HttpServletRequest </code>对象。</p>
	 * 
	 * <p>底层使用了 {@link RequestContextHolder}，Spring 会将每次请求对应的 Request 对象保存到该对象中。</p>
	 * 
	 * @return 本次请求对应的<code> HttpServletRequest </code>对象
	 */
	public static HttpServletRequest getRequest() {
		try{
			return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * <p>设置响应头 - 控制缓存的失效日期</p>
	 * 
	 * <p>对于设置了此响应头信息的 Http 请求，浏览器会将本次请求返回的资源设置为在指定日期后失效。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 * @param expiresSeconds - 缓存过期时间
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		// Http 1.0 header, set a fix expires date.
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header, set a time after now.
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
	}
	
	/**
	 * <p>设置响应头 - 设置浏览器缓存机制为无缓存</p>
	 * 
	 * <p>对于设置了此响应头信息的 Http 请求，浏览器会禁用客户端缓存。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 */
	public static void setNoCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// Http 1.1 header
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}
	
	/**
	 * <p>设置响应头 - Last-Modified</p>
	 * 
	 * <p>在浏览器第一次请求某一个 URL 时，服务器端的返回状态会是200，内容是客户端请求的资源。同时有一个 Last-Modified 的属性标记此文件在服务器端最后被修改的时间。
	 * 第二次请求此 URL 时，根据 HTTP 协议的规定，浏览器会向服务器传送 If-Modified-Since 报头，询问该时间之后文件是否有被修改过。如果服务器端的资源没有变化，则
	 * 自动返回 HTTP 304（Not Changed.）状态码，内容为空，这样就节省了传输数据量。</p>
	 * <p>此方法可以手动控制 Last-Modified 的时间，以达到程序的某种需求。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 * @param lastModifiedDate - 手动设置的最后一次修改时间
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModifiedDate);
	}
	
	/**
	 * <p>设置响应头 - ETag</p>
	 * 
	 * <p>ETag 是一个可以与 Web 资源关联的记号（token），一般可标示URL对象是否改变。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 * @param etag - ETag 值
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader(HttpHeaders.ETAG, etag);
	}
	
	/**
	 * <p>设置响应头 - Content-Disposition</p>
	 * 
	 * <p>Content-disposition 是 MIME 协议的扩展，MIME 协议指示 MIME 用户代理如何显示附加的文件。Content-disposition
	 * 其实可以控制用户请求所得的内容存为一个文件的时候提供一个默认的文件名，文件直接在浏览器上显示或者在访问时弹出文件下载对话框。</p>
	 * 
	 * @param response - {@link HttpServletResponse}
	 * @param filename - 默认的文件名
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			// 中文文件名支持
			String encodedFileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + encodedFileName + "\"");
		} catch (UnsupportedEncodingException e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * <p>获取所有带有指定前缀的请求参数。</p>
	 * 
	 * @param request - 本次请求对应的 {@link ServletRequest}
	 * @param prefix - 指定前缀
	 * @return 所有带有指定前缀的请求参数的集合
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Validate.notNull(request, "Request cannot be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(pre) || paramName.startsWith(pre)) {
				String unprefixed = paramName.substring(pre.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					values = new String[]{};
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	
	/**
	 * <p>判断当前请求是否是异步请求（Ajax 请求）。</p>
	 * <p>Ajax 请求会默认在请求时附加一个名为 X-Requested-With 的请求头，可以通过判断此请求头信息是否为空来验证是否是 Ajax 请求。</p>
	 * 
	 * @param request - 本次请求对应的 {@link HttpServletRequest}
	 * return 是否是异步请求
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		String accept = request.getHeader("accept");
		String xRequestedWith = request.getHeader("X-Requested-With");
		// 如果是异步请求或是手机端，则直接返回信息
		return ((accept != null && accept.indexOf("application/json") != -1 
			|| (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1)));
	}
	
	/**
     * 判断访问URI是否是静态文件请求
	 * @throws Exception 
     */
	/**
	 * <p>判断当前请求是否是静态资源（全局配置文件指定）请求。</p>
	 * <p>全局配置文件指定了所有程序中静态资源文件后缀名，可以通过判断 URL 的后缀来判断是否是静态资源请求。</p>
	 * 
	 * @param request - 本次请求对应的 {@link HttpServletRequest}
	 * return 是否是静态资源请求
	 */
    public static boolean isStaticFile(String uri){
		if (staticFileSuffix == null){
			try {
				throw new Exception("检测到“app.properties”中没有配置“web.staticFile”属性。");
			} catch (Exception e) {
				logger.info(e.getMessage());
			}
		}
		if (StringUtils.endsWithAny(uri, staticFileSuffix) && !StringUtils.endsWithAny(uri, urlSuffix)
				&& !StringUtils.endsWithAny(uri, ".jsp") && !StringUtils.endsWithAny(uri, ".java")){
			return true;
		}
		return false;
    }
	
}
