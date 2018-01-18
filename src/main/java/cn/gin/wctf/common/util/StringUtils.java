package cn.gin.wctf.common.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;

import cn.gin.wctf.common.config.Global;

/**
 * <p>继承自<code> commons.lang3.StringUtils </code>的增强类。</p>
 * 
 * @author Gintoki
 * @version 2017-10-03
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	@SuppressWarnings("unused")
	private static final String ENCODING = Global.getConfig("system.encoding");
	
	private static final String CHARSET_NAME = "UTF-8";
	
	// -------------------
	// Service
	// -------------------
	
	/**
	 * <p>将对象转换为字符串，如果对象为空，则使用 defaultVal 值。</p>
	 * 
	 * @param obj - 需要被转为字符串的对象
	 * @param defaultVal - 如果当前对象为空，则返回该默认值
	 */
    public static String toString(final Object obj, final String defaultVal) {
    	 return obj == null ? defaultVal : obj.toString();
    }
    
    /**
     * <p>将字符串数组拼接为字符串，每个字符串之间使用指定分隔符进行分隔。</p>
     * 
     * @param strs - 需要被转为字符串的字符串数组
     * @param limiter - 分隔符
     * @return 被转换后的字符串
     */
    public static String toString(final String[] strs, final char limiter) {
    	 if(strs == null || strs.length == 0) {
    		 return null;
    	 }
    	 String res = "";
    	 for(int i = 0; i < strs.length; i++) {
    		 res = res + strs[i];
    		 if(i != strs.length - 1) {
    			 res = res + limiter;
    		 }
    	 }
    	 return res;
    }
    
    /**
     * <p>使用系统默认编码，将字符串转换为字节数组。</p>
     * 
     * @param str - 需要被转换的字符串
     * @return 系统编码下的字节数组
     */
    public static byte[] getBytes(String str){
    	if (str != null){
    		try {
				return str.getBytes(CHARSET_NAME);
			} catch (UnsupportedEncodingException e) {
				return null;
			}
    	} else {
    		return null;
    	}
    }
    
	/**
     * <p>使用系统默认编码，将字节数组编码为字符串。</p>
     * 
     * @param bytes - 需要被转换的字节数组
     * @return 系统编码下的字符串
     */
    public static String toString(byte[] bytes){
    	try {
			return new String(bytes, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			return EMPTY;
		}
    }
    
    /**
	 * <p>替换为手机识别的HTML，去掉样式及属性，保留回车。</p>
	 * 
	 * @param txt - 需要被转换的文本信息
	 * @return 转换后的 HTML 数据
	 */
	public static String toHtml(String txt){
		if (txt == null){
			return "";
		}
		return replace(replace(StringEscapeUtils.escapeHtml4(txt), "\n", "<br/>"), "\t", "&nbsp; &nbsp; ");
	}
    
    
    /**
     * <p>获得用户远程地址。</p>
     * 
     * @param request - {@link HttpServletRequest}
     * @return 当前<code> Request </code>对应的客户端
     */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
}
