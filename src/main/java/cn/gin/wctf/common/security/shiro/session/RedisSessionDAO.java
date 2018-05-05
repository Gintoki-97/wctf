package cn.gin.wctf.common.security.shiro.session;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.util.DateUtils;
import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.util.ObjectUtils;
import cn.gin.wctf.common.util.RedisIndex;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.common.web.Servlets;
import redis.clients.jedis.Jedis;

/**
 * <p>系统 Session 管理器，用于进行 Session - CRUD 相关操作的类。</p>
 * <p>Shiro 提供了相关接口让程序可以扩展 Session 的管理方式。本类使用 Redis 作为 Session 的持久化方式，
 * 以便于程序可以在集群环境正常工作而下不会出现 Session 无法共享的问题。</p>
 *
 * @author Gintoki
 * @version 2017-11-15
 */
public class RedisSessionDAO extends AbstractSessionDAO implements SessionDAO {

    /**
     * The default root logger.
     */
    private final Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    /**
     * Session 缓存的前缀
     */
    private String sessionKeyPrefix;

    public String getSessionKeyPrefix() {
        return sessionKeyPrefix;
    }

    public void setSessionKeyPrefix(String sessionKeyPrefix) {
        this.sessionKeyPrefix = sessionKeyPrefix;
    }

    // --------------------
    // Service
    // --------------------

    /**
     * <p><b>CRUD - C</b></p>
     * <p>用于创建 Session，包括登入且当前用户没有可用 Session 等操作，会触发对此方法的调用。</p>
     * <p>Shiro 回为用户的 Web 资源请求创建一个 Session 对象，但是不包含 SessionId，对于已经通过 Shiro 权限校验的用户
     * 只需要为其 Session 生成 SessionId 并持久化到 Redis 中即可。</p>
     *
     * @param session - 用户当前 Session，并且此 Session 没有 Id
     * @return 为当前 Session 生成的 Id
     */
    @Override
    protected Serializable doCreate(Session session) {
        HttpServletRequest request = Servlets.getRequest();
        if (request != null){
            String uri = request.getServletPath();
            // 静态资源不创建 Session
            if (Servlets.isStaticFile(uri)){
                return null;
            }
        }
        // 生成 SessioId
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        this.update(session);
        return sessionId;
    }

    /**
     * <p><b>CRUD - R</b></p>
     * <p>用于获取 Session，更新 Session 的一系列操作，都会触发对此方法的调用。</p>
     *
     * @param sessionId - 需要从 Redis 中读取的 Session 的 Id
     * @return Redis 中指定 Id 对应的 Session 对象
     */
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        try{
            return super.readSession(sessionId);
        }catch (UnknownSessionException e) {
            return null;
        }
    }

    /**
     * <p><b>CRUD - R</b></p>
     * <p>用于获取 Session，父类的 readSession() 最终会调用当前方法去执行实际的  Session 操作。</p>
     *
     * @param sessionId - 需要从 Redis 中读取的 Session 的 Id
     * @return Redis 中指定 Id 对应的 Session 对象
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session s = null;
        HttpServletRequest request = Servlets.getRequest();
        if (request != null) {
            String uri = request.getServletPath();
            // 静态文件不获取 Session
            if (Servlets.isStaticFile(uri)) {
                return null;
            }
            s = (Session) request.getAttribute("session_" + sessionId);
        }
        if (s != null){
            return s;
        }
        Session session = null;
        Jedis jedis = JedisUtils.getResource();
        jedis.select(RedisIndex.SESSION_CACHE.getIndex());
        try {
            // 从 Redis 中获取 Session 对象
            session = (Session) ObjectUtils.unserialize(jedis.get(JedisUtils.getBytesKey(sessionKeyPrefix + sessionId)));

            if (logger.isDebugEnabled()) {
                logger.debug("Do read session [{}], request uri [{}]", sessionId, request == null ? StringUtils.EMPTY : request.getRequestURI());
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Do read session error, [{}], request uri [{}], exception {e}", sessionId, request == null ? StringUtils.EMPTY : request.getRequestURI(), e);
            }
        } finally {
            JedisUtils.returnResource(jedis);
        }
        // Request 范围内缓存 Session
        if (request != null && session != null){
            request.setAttribute("session_" + sessionId, session);
        }
        return session;
    }

    /**
     * <p><b>CRUD - U</b></p>
     * <p>用于 更新 Session，包括修改最后访问时间，设置属性，登入登出等操作，都会触发对此方法的调用。</p>
     *
     * @param session - 用户当前 Session，需要更新到 Redis 中
     */
    @Override
	public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }
        HttpServletRequest request = Servlets.getRequest();
        if (request != null) {
            String uri = request.getServletPath();
            // 静态资源请求，不更新 Session
            if (Servlets.isStaticFile(uri)) {
                return;
            }
            // 视图请求不更新 Session
            if (StringUtils.startsWith(uri, Global.getConfig("web.view.prefix")) &&
                    StringUtils.endsWith(uri, Global.getConfig("web.view.suffix"))) {
                return;
            }
            // Request 请求控制不更新 Session
            if (Global.NO.equals(request.getParameter("updateSession"))) {
                return;
            }
        }
        // 使用 Jedis 更新 Session
        Jedis jedis = JedisUtils.getResource();
        jedis.select(RedisIndex.SESSION_CACHE.getIndex());
        try {
            // 获取登录者编号信息，存储到 Session 表中
            PrincipalCollection pc = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            String principalId = pc == null ? StringUtils.EMPTY : pc.getPrimaryPrincipal().toString();

            // 操作 Redis 中的  Session
                // 存储 Session 表
            jedis.hset(sessionKeyPrefix, session.getId().toString(), principalId + "|" + session.getTimeout() + "|" + session.getLastAccessTime().getTime());
                // 存储 Session 对象
            jedis.set(JedisUtils.getBytesKey(sessionKeyPrefix + session.getId()), JedisUtils.toBytes(session));
                // 设置超时时间
            int timeoutSeconds = (int) (session.getTimeout() / 1000);
            jedis.expire(sessionKeyPrefix, timeoutSeconds);
            jedis.expire((sessionKeyPrefix + session.getId()), timeoutSeconds);

            if (logger.isDebugEnabled()) {
                logger.debug("Update session which [{}], requets uri [{}] ", session.getId(), request == null ? StringUtils.EMPTY : request.getRequestURI());
            }
        } catch(Exception e) {
            if (logger.isErrorEnabled()) {
                logger.debug("Update session error, which [{}], requets uri [{}], exception {} ", session.getId(), request == null ? StringUtils.EMPTY : request.getRequestURI(), e);
            }
        } finally {
            JedisUtils.returnResource(jedis);
        }
    }

    /**
     * <p><b>CRUD - D</b></p>
     * <p>用于删除 Session，手动登出，多人登录同一账号等操作，都会触发对此方法的调用。</p>
     *
     * @param session - 用户当前 Session
     */
    @Override
	public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        Jedis jedis = JedisUtils.getResource();
        jedis.select(RedisIndex.SESSION_CACHE.getIndex());
        try {
            // 操作 Redis 中的 Session
                // 删除 Session 表
            jedis.hdel(JedisUtils.getBytesKey(sessionKeyPrefix), JedisUtils.getBytesKey(session.getId().toString()));
                // 删除 Session 对象
            jedis.del(JedisUtils.getBytesKey(sessionKeyPrefix + session.getId()));

            if (logger.isDebugEnabled()) {
                logger.debug("Delete session which [{}]", session.getId());
            }
        } catch(Exception e) {
            if (logger.isErrorEnabled()) {
                logger.debug("Delete session error, which [{}], exception {}", session.getId(), e);
            }
        } finally {
            JedisUtils.returnResource(jedis);
        }
    }

    /**
     * <p>获取系统中所有的活动 Session，程序会定时不断的进行 Session 校验，以清除无效会话。
     * 在进行 Session 校验时，需要从 Redis 中获取所有的 Session。会触发对此方法的调用。</p>
     * <p>此方法会默认获取所有的会话，包括已经离线的 Session。</p>
     *
     * @return 程序中所有 Session 的集合
     */
    @Override
	public Collection<Session> getActiveSessions() {
        return getActiveSessions(true);
    }

    /**
     * <p>获取系统中所有的活动 Session。</p>
     *
     * @param includeLeave - 是否包括离线（最后访问时间大于3分钟为离线会话）的 Session
     * @return 程序中所有活动 Session 的集合
     */
    @Override
	public Collection<Session> getActiveSessions(boolean includeLeave) {
        return getActiveSessions(includeLeave, null, null);
    }

    /**
     * <p>获取系统中所有的 Session。</p>
     *
     * @param includeLeave - 是否包括离线（最后访问时间大于3分钟为离线会话）
     * @param principal - 根据登录者对象获取活动会话
     * @param filterSession - 如果这个参数不为空，则在结果中会过滤掉这个会话
     * @return 程序中所有 Session 的集合
     */
    @Override
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession) {
        Set<Session> sessions = Sets.newHashSet();
        Jedis jedis = JedisUtils.getResource();
        jedis.select(RedisIndex.SESSION_CACHE.getIndex());
        try {
            // 获取 Session 表
            Map<String, String> map = jedis.hgetAll(sessionKeyPrefix);
            for (Map.Entry<String, String> s : map.entrySet()) {
                if (StringUtils.isNoneBlank(s.getKey()) && StringUtils.isNoneBlank(s.getValue())) {
                    String[] valueInfos = StringUtils.split(s.getValue(), "|");
                    // 只获取有登录信息的 Session
                    if (valueInfos != null && valueInfos.length == 3) {
                        SimpleSession session = new SimpleSession();
                        session.setId(s.getKey());
                        session.setAttribute("principalId", valueInfos[0]);
                        session.setTimeout(Long.valueOf(valueInfos[1]));
                        session.setLastAccessTime(new Date(Long.valueOf(valueInfos[2])));
                        try {
                            session.validate();
                            boolean legal = false;
                            // 不包括离线并符合最后访问时间小于等于3分钟条件
                            if (includeLeave || DateUtils.pastMinutes(session.getLastAccessTime()) <= 3) {
                                legal = true;
                            }
                            // 符合登陆者条件
                            if (principal != null) {
                                PrincipalCollection pc = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                                if (principal.toString().equals(pc != null ? pc.getPrimaryPrincipal().toString() : StringUtils.EMPTY)){
                                    legal = true;
                                }
                            }
                            // 过滤掉的 Session
                            if (filterSession != null && filterSession.getId().equals(session.getId())) {
                                legal = false;
                            }
                            // 此会话符合条件，添加到集合中
                            if (legal) {
                                sessions.add(session);
                            }
                        } catch(Exception e) {
                            jedis.hdel(sessionKeyPrefix, s.getKey());    // Session 验证失败，删除
                        }
                    } else {
                        jedis.hdel(sessionKeyPrefix, s.getKey());        // Session 不符合规则，删除
                    }
                } else if (StringUtils.isNotBlank(s.getKey())) {
                    jedis.hdel(sessionKeyPrefix, s.getKey());            // Session 值为空，删除
                }
            }
            if (logger.isInfoEnabled()) {
                logger.info("Get active sessions size [{}].", sessions.size());
            }
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Get active sessions, but exceptions [{}]", e);
            }
        } finally {
            JedisUtils.returnResource(jedis);
        }
        return sessions;
    }

}
