package cn.gin.wctf.common.security.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.util.DateUtils;
import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.util.RedisIndex;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.common.web.Servlets;

/**
 * EhCache 使用的缓存管理器
 * @author Gintoki
 * @version 2017-10-04
 */
public class CacheSessionDAO extends EnterpriseCacheSessionDAO implements SessionDAO {

	/**
	 * The default root logger.
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public CacheSessionDAO() {
        super();
    }
	
	@Override
	protected Serializable doCreate(Session session) {
		HttpServletRequest request = Servlets.getRequest();
		if (request != null){
			String uri = request.getServletPath();
			if (Servlets.isStaticFile(uri)){	// 如果是静态文件，则不创建session
		        return null;
			}
		}
		super.doCreate(session);
		if(logger.isDebugEnabled()) {
			logger.debug("doCreate {} {}", session, request != null ? request.getRequestURI() : "");
		}
		// Session 持久化
		// 30分钟 Session 失效, 存储在 index = 1 redis 数据库
		JedisUtils.setObject(session.getId().toString(), session, 30 * 60, RedisIndex.SYSTEM_CACHE);
		if(logger.isDebugEnabled()) {
			logger.debug("Serializable session to redis: {}", session.getId());
		}
    	return session.getId();
	}
	
	@Override
	protected void doUpdate(Session session) {
		if(session == null || session.getId() == null) {
			return;
		}
		HttpServletRequest request = Servlets.getRequest();
		if (request != null){
			String uri = request.getServletPath();
			if (Servlets.isStaticFile(uri)){	// 如果是静态文件，则不更新session
				return;
			}
			if (StringUtils.startsWith(uri, Global.getConfig("web.view.prefix"))
					&& StringUtils.endsWith(uri, Global.getConfig("web.view.suffix"))){	// 如果是视图文件，则不更新session
				return;
			}
			String updateSession = request.getParameter("updateSession");	// 手动控制不更新session
			if (Global.FALSE.equals(updateSession) || Global.NO.equals(updateSession)){
				return;
			}
		}
		super.doUpdate(session);
		if(logger.isDebugEnabled()) {
			logger.debug("update {} {}", session.getId(), request != null ? request.getRequestURI() : "");
		}
	}
	
	@Override
	protected void doDelete(Session session) {
		if (session == null || session.getId() == null) { 
            return;
        }
    	super.doDelete(session);
    	if(logger.isDebugEnabled()) {
			logger.debug("delete {} ", session.getId());
		}
	}
	
	@Override
	protected Session doReadSession(Serializable sessionId) {
		Session session = super.doReadSession(sessionId);
		if(logger.isDebugEnabled()) {
			logger.debug("Get session from cache: {}, result: ", sessionId, session == null);
		}
		if(session == null) {
			session = (Session) JedisUtils.getObject(sessionId.toString());
			if(logger.isDebugEnabled()) {
				logger.debug("Get session from redis: {}, result: ", sessionId, session == null);
			}
		}
		return session;
	}
	
	@Override
	public Session readSession(Serializable sessionId) throws UnknownSessionException {
		try{
    		Session s = null;
    		HttpServletRequest request = Servlets.getRequest();
    		if (request != null){
    			String uri = request.getServletPath();
    			if (Servlets.isStaticFile(uri)){	// 如果是静态文件，则不获取 session
    				return null;
    			}
    			s = (Session) request.getAttribute("session_" + sessionId);
    		}
    		if (s != null){
    			return s;
    		}
    		Session session = super.readSession(sessionId);
    		if(logger.isDebugEnabled()) {
    			logger.debug("readSession {} {}", sessionId, request != null ? request.getRequestURI() : "");
    		}
    		if (request != null && session != null){
    			request.setAttribute("session_" + sessionId, session);
    		}
    		return session;
    	}catch (UnknownSessionException e) {
    		if(logger.isInfoEnabled()) {
    			logger.info(e.getMessage());
    		}
			return null;
		}
	}
	
	
	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave) {
		return getActiveSessions(includeLeave, null, null);
	}

	/**
	 * 获取活动会话
	 * @param includeLeave 是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param principal 根据登录者对象获取活动会话
	 * @param filterSession 不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
		if (includeLeave && principal == null){	// 如果包括离线，并且没有登录者条件
			return getActiveSessions();
		}
		Set<Session> sessions = Sets.newHashSet();
		for (Session session : getActiveSessions()){
			boolean isActiveSession = false;
			// 不包括离线并符合最后访问时间小于等于3分钟条件
			if (includeLeave || DateUtils.pastMinutes(session.getLastAccessTime()) <= 3){
				isActiveSession = true;
			}
			// 符合登陆者条件
			if (principal != null){
				PrincipalCollection pc = (PrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if (principal.toString().equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)){
					isActiveSession = true;
				}
			}
			// 过滤掉的SESSION
			if (filterSession != null && filterSession.getId().equals(session.getId())){
				isActiveSession = false;
			}
			if (isActiveSession){
				sessions.add(session);
			}
		}
		return sessions;
	}

}
