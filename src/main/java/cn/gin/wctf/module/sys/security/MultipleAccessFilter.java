package cn.gin.wctf.module.sys.security;

import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.util.RedisIndex;
import cn.gin.wctf.common.util.StringUtils;
import redis.clients.jedis.Jedis;

public class MultipleAccessFilter extends AccessControlFilter {

	/**
	 * Session 缓存的前缀
	 */
	private String sessionKeyPrefix;
	
	/**
	 * 登录 URL
	 */
	private String loginUrl;
	
	
	public String getSessionKeyPrefix() {
		return sessionKeyPrefix;
	}

	public void setSessionKeyPrefix(String sessionKeyPrefix) {
		this.sessionKeyPrefix = sessionKeyPrefix;
	}
	
	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	
	// ----------------
	// Service
	// ----------------

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		Subject subject = getSubject(request, response);  
	    if(!subject.isAuthenticated() && !subject.isRemembered()) {  
	        // 如果用户未登录，直接放行
	        return true;
	    }
	    String account = (String) request.getAttribute("account");
	    if(StringUtils.isEmpty(account)) {
	    	return false;
	    } else {
	    	account = account.intern();
	    }
	    synchronized (account) {
	    	// 使用 Jedis 更新 Session
	    	Jedis jedis = JedisUtils.getResource();
	    	jedis.select(RedisIndex.SESSION_CACHE.getIndex());
	    	try {
	    		Map<String, String> sessionMap = jedis.hgetAll(sessionKeyPrefix);
	    		for(Entry<String, String> e : sessionMap.entrySet()) {
	    			if(e.getValue().startsWith(account)) {
	    				// 已经有相同用户名的 Session 存在
	    				JedisUtils.mapRemove(sessionKeyPrefix, e.getKey());
	    				jedis.expire(JedisUtils.getBytesKey(sessionKeyPrefix + e.getKey()), 0);
	    			}
	    		}
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
		}
		return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		// 退出
		Subject subject = getSubject(request, response);
		subject.logout();
		WebUtils.getSavedRequest(request);
		// 重定向
		WebUtils.issueRedirect(request, response, loginUrl);
		return false;
	}

}
