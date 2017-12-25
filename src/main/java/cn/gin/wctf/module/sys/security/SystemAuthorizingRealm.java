package cn.gin.wctf.module.sys.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.util.CaptchaUtils;
import cn.gin.wctf.common.util.JCodec;
import cn.gin.wctf.common.util.SpringContextHolder;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.module.sys.entity.Role;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.security.authc.CaptchaAuthenticationException;
import cn.gin.wctf.module.sys.security.authc.MultipleAuthenticationException;
import cn.gin.wctf.module.sys.service.SystemService;
import cn.gin.wctf.module.sys.service.UserService;
import cn.gin.wctf.module.sys.util.UserUtils;

/**
 * 系统安全认证 realm 类
 * @author Gintoki
 * @version 2017-10-04
 */
@Component
public class SystemAuthorizingRealm extends AuthorizingRealm implements Serializable {

	private static final long serialVersionUID = 3329508315984834144L;

	private transient Logger logger = LoggerFactory.getLogger(getClass());

	private transient SystemService systemService;
	
	@Autowired
	private transient UserService userService;
	
	public SystemAuthorizingRealm() {
		this.setCachingEnabled(false);
	}
	
	/**
	 * 认证回调函数, 确定用户的身份（你是谁）
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authToken;
		int activeSessionSize = getSystemService().getSessionDao().getActiveSessions(false).size();
		if (logger.isDebugEnabled()) {
			logger.debug("login submit, active session size：{}, username：{}", activeSessionSize, token.getUsername());
		}
		// 校验登录验证码
		if (UserUtils.isCaptchaLogin(token.getUsername(), false, false)) {
			Session session = UserUtils.getSession();
			String code = (String) session.getAttribute(CaptchaUtils.IMAGE_CAPTCHA);
			session.removeAttribute(CaptchaUtils.IMAGE_CAPTCHA);
			if (token.getCaptcha() == null || !token.getCaptcha().toUpperCase().equals(code)) {
				throw new CaptchaAuthenticationException("验证码错误，session[" +  code + ", client[" + token.getCaptcha() + "]");
			}
		}
		// 校验用户名密码
		User user = userService.getUserByAccount(token.getUsername());
		if (user != null) {
			if (Global.NO.equals(user.getLoginFlag())) {
				throw new LockedAccountException();
			}
			// 自定义md5(md5(pwd), md5(salt))加盐加密认证
			String customPwd = JCodec.md5SaltEncode(String.valueOf(token.getPassword()), user.getSalt());	
			if(!StringUtils.isBlank(user.getPassword()) && (user.getPassword().equals(customPwd))) {
				token.setPassword(user.getPassword().toCharArray());	// 更新 token 中的密码
				Session session = UserUtils.getSession();	// 绑定用户信息到session
				session.setAttribute("user", user);
				return new SimpleAuthenticationInfo(new Principal(user, token.isEmailLogin()), 
						user.getPassword(), getName());
			} else {
				throw new IncorrectCredentialsException();
			}
		}
		throw new UnknownAccountException();
	}
	
	/**
	 * 授权回调函数, 确定用户权限（你可以干什么），如果缓存中存在，则直接从缓存中获取，否则就重新获取
	 */
	@Override
	protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {
		if (principals == null) {
            return null;
        }
        AuthorizationInfo info = null;
        info = (AuthorizationInfo) UserUtils.getCache(UserUtils.CACHE_AUTH_INFO);
        if (info == null) {
            info = doGetAuthorizationInfo(principals);
            if (info != null) {
            	UserUtils.putCache(UserUtils.CACHE_AUTH_INFO, info);
            }
        }

        return info;
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Principal principal = (Principal) getAvailablePrincipal(principals);
		// 获取当前已登录的用户
		if (!Global.TRUE.equals(Global.getConfig("user.multiAccountLogin"))) {
			Collection<Session> sessions = getSystemService().getSessionDao().getActiveSessions(true, principal, UserUtils.getSession());
			if (sessions.size() > 0) {
				if (UserUtils.getSubject().isAuthenticated()) {		// 如果是登录进来的，则踢出已在线用户
					for (Session session : sessions) {
						getSystemService().getSessionDao().delete(session);
					}
				} else {											// 记住我进来的，并且当前用户已登录，则退出当前用户提示信息
					UserUtils.getSubject().logout();
					throw new MultipleAuthenticationException();
				}
			}
		}
		User user = getSystemService().getUserByLoginName(principal.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 添加用户权限
			info.addStringPermission("user");
			// 添加用户角色信息
			for (Role role : user.getRoles()) {
				info.addRole(role.getName());
			}
			return info;
		} else {
			return null;
		}
	}
	
	/**
	 * 获取系统业务对象
	 */
	public SystemService getSystemService() {
		if (systemService == null){
			systemService = SpringContextHolder.getBean(SystemService.class);
		}
		return systemService;
	}
	
	@Override
	protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
        		authorizationValidate(permission);
            }
        }
		return super.isPermitted(permissions, info);
	}
	
	@Override
	protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
		if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
            	authorizationValidate(permission);
            }
        }
		return super.isPermittedAll(permissions, info);
	}
	
	@Override
	protected void checkPermission(Permission permission, AuthorizationInfo info) {
		authorizationValidate(permission);
		super.checkPermission(permission, info);
	}
	
	/**
	 * 授权验证方法
	 * @param permission
	 */
	private void authorizationValidate(Permission permission){
		// 模块授权预留接口
	}
	
	// ---------------------------
	// Support
	// ---------------------------
	
	/**
	 * 用户授权信息类
	 */
	public class Principal implements Serializable {

		private static final long serialVersionUID = 2729436746954703173L;

		private Integer id; 			// 编号
		private String account; 		// 帐号
		private boolean emailLogin; 	// 是否邮箱登录
		
		public Principal(User user, boolean emailLogin) {
			this.id = user.getId();
			this.account = user.getAccount();
			this.emailLogin = emailLogin;
		}

		public Integer getId() {
			return id;
		}

		public String getLoginName() {
			return account;
		}

		public boolean isEmailLogin() {
			return emailLogin;
		}

		/**
		 * 获取 sessionId
		 */
		public String getSessionid() {
			try{
				return (String) UserUtils.getSession().getId();
			}catch (Exception e) {
				return "";
			}
		}

		@Override
		public String toString() {
			return "Principal [id=" + id + ", account=" + account + ", emailLogin=" + emailLogin + "]";
		}
		
	}
	
}
