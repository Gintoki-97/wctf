package cn.gin.wctf.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于发送http请求的工具类
 * @author Gintoki
 * @version 2017-10-12
 */
public class HttpUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * @param urlStr 请求的url
	 * @param params 查询参数
	 * @return http响应的字符输出流
	 */
	public static String sendHttpGetRequest(String urlStr, Map<String, String> params) {
		BufferedReader reader = null;
		try {
			String queryItems = formatQueryItems(params);
			String urlNameString = urlStr + "?" + queryItems;
			URL url = new URL(urlNameString);
			// 设置连接属性
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Cache-Control", "max-age=0");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:51.0) Gecko/20100101 Firefox/51.0");
			// 建立实际的连接
            conn.connect();
            
            /* 获取响应头信息，业务暂时不需要
            // 获取所有响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }*/
            // 定义 BufferedReader输入流来读取URL的响应
            
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
            	sb.append(line);
            }
            return sb.toString();
		} catch (Exception e) {
           logger.debug("发送HTTP请求出现异常！");
        } finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 将Map中的键值对格式化为url中的查询串
	 * @param params 参数集合
	 * @return	查询串，格式为：key1=value1&key2=value2&key3=value4
	 */
	private static String formatQueryItems(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> param : params.entrySet()) {
			sb.append(param.getKey() + "=" + param.getValue());
			sb.append("&");
		}
		return sb.toString().substring(0, sb.toString().length() - 1);
	}
	
	/**
	 * 通过 {@link HttpServletRequest}，获取客户端的公网 IP 地址。屏蔽多级反向代理对真实 IP 的影响。
	 * 
	 * @param request - 客户端 Request 对象
	 * @return 客户端的 IP 地址，如果客户端的真实地址为内网地址，则会返回客户端所属网络的公网服务商 IP。
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if(ipAddress == null || ipAddress.length() == 0 || "".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ipAddress == null || ipAddress.length() == 0 || "".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {	// 本机访问
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					System.err.println("无法通过网卡获取本机IP地址");
				}
				ipAddress = inet.getHostAddress();
			}
		}
		// 多级反向代理的情况下，只获取第一个ip地址
		if(ipAddress != null && ipAddress.length() > 15) {	//ip地址长度大于15，说明有多个反向代理服务器的ip
			if(ipAddress.indexOf(',') > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(','));
			}
		}
		return ipAddress;
	}
	
}
