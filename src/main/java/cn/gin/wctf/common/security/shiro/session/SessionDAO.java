package cn.gin.wctf.common.security.shiro.session;

import java.util.Collection;

import org.apache.shiro.session.Session;

/**
 * <p>扩展 {@link org.apache.shiro.session.mgt.eis.SessionDAO} 接口，
 * 加入了获取所有处于活动状态的会话的功能。</p>
 * 
 * @author Gintoki
 * @version 2017-10-05
 */
public interface SessionDAO extends org.apache.shiro.session.mgt.eis.SessionDAO {

	/**
	 * 获取活动会话
	 * 
	 * @param includeLeave
	 *            是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @return
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave);

	/**
	 * 获取活动会话
	 * 
	 * @param includeLeave
	 *            是否包括离线（最后访问时间大于3分钟为离线会话）
	 * @param principal
	 *            根据登录者对象获取活动会话
	 * @param filterSession
	 *            不为空，则过滤掉（不包含）这个会话。
	 * @return
	 */
	public Collection<Session> getActiveSessions(boolean includeLeave, Object principal, Session filterSession);
}
