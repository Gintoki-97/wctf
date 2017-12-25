package cn.gin.wctf.module.sys.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.util.RedisIndex;
import cn.gin.wctf.common.util.SpringContextHolder;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.module.post.entity.Post;
import cn.gin.wctf.module.sys.dao.UserDao;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.security.SystemAuthorizingRealm.Principal;

/**
 * 用户操作的工具类，根据cache判断是否需要验证码，根据用户名获取用户等等
 * @author Gintoki
 * @version 2017-10-10
 */
public class UserUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(UserUtils.class);

	private static UserDao userDao = SpringContextHolder.getBean(UserDao.class);
//	private static RoleDao roleDao = SpringContextHolder.getBean(RoleDao.class);
	
	public static final String USER_CACHE = "userCache";
	public static final String USER_CACHE_ID_ = "id_";
	public static final String USER_CACHE_LOGIN_NAME_ = "ln_";
	public static final String USER_CACHE_LIST_BY_OFFICE_ID_ = "oid_";
	
	public static final String CACHE_AUTH_INFO = "auths";
	public static final String CACHE_ROLE_LIST = "roles";
	
	
	/**
	 * 根据登录名获取用户
	 * @param loginName
	 * @return 取不到返回null
	 */
	public static User getByLoginName(String loginAccount) {
		User user = (User) JedisUtils.getObject(USER_CACHE_LOGIN_NAME_ + loginAccount, RedisIndex.USER_CACHE);
//		User user = (User) CacheUtils.get(USER_CACHE, USER_CACHE_LOGIN_NAME_ + loginAccount);
		if (user == null) {
			user = userDao.getUserByAccount(loginAccount);
			if (user == null) {
				return null;
			}
			user.getRoles().addAll(userDao.listRoleByUserId(user.getId()));
			JedisUtils.setObject(USER_CACHE_ID_ + user.getId(), user, JedisUtils.ONE_DAY, RedisIndex.USER_CACHE);
			JedisUtils.setObject(USER_CACHE_LOGIN_NAME_ + user.getAccount(), user, JedisUtils.ONE_DAY, RedisIndex.USER_CACHE);
//			CacheUtils.put(USER_CACHE, USER_CACHE_ID_ + user.getId(), user);
//			CacheUtils.put(USER_CACHE, USER_CACHE_LOGIN_NAME_ + user.getAccount(), user);
		}
		return user;
	}
	
	/**
	 * 获取授权主要对象
	 */
	public static Subject getSubject(){
		return SecurityUtils.getSubject();
	}
	
	/**
	 * 获取当前登录者对象
	 */
	public static Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Principal principal = (Principal) subject.getPrincipal();
			if (principal != null){
				return principal;
			}
		} catch (UnavailableSecurityManagerException e) {
			} catch (InvalidSessionException e){
		}
		return null;
	}
	
	/**
	 * 获取当前登录者对象的session
	 */
	public static Session getSession(){
		try{
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession(false);
			if (session == null){
				session = subject.getSession();
			}
			if (session != null){
				return session;
			}
		}catch (InvalidSessionException e){
			}
		return null;
	}
	
	/**
	 * 是否是验证码登录
	 * @param useruame 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	public static boolean isCaptchaLogin(String useruame, boolean isFail, boolean clean) {
		Map<String, String> loginFailedMap = JedisUtils.getMap("loginFailedMap", RedisIndex.SYSTEM_CACHE);
		if (loginFailedMap == null) {
			loginFailedMap = Maps.newHashMap();
		}
		Integer loginFailedNum = Integer.valueOf(loginFailedMap.get(useruame) == null ? "0" : loginFailedMap.get(useruame));
		if (isFail) {
			loginFailedNum++;
			loginFailedMap.put(useruame, loginFailedNum.toString());
		}
		if (clean) {
			loginFailedMap.remove(useruame);
		}
		JedisUtils.setMap("loginFailedMap", loginFailedMap, 30 * 60);
		return loginFailedNum >= 3;	//超过三次使用验证码
	}
	
	/**
	 * 是否是发帖的视图请求或控制器请求
	 * @param post 根据post中的参数是否为空来判断是否是视图请求或控制器请求
	 * @return
	 */
	public static boolean isPostSendViewRequest(Post post) {
		if(post == null)
			return true;
		if(StringUtils.isBlank(post.getTitle()) || StringUtils.isBlank(post.getContent()) || post.getClassify() == null)
			return true;	
		return false;
	}
	
	public static boolean isLoginViewRequest(String account, String password) {
		if(StringUtils.isBlank(account) || StringUtils.isBlank(password))
			return true;
		return false;
	}
	
	
	
	/**
	 * 是否是用户注册的视图请求或控制器请求
	 * @param user 根据用户对象中的数据判断是否是控制器请求或视图请求
	 * @return
	 */
	public static boolean isUserRegViewRequest(User user) {
		if(user == null)
			return true;
		if(StringUtils.isBlank(user.getAccount()) || StringUtils.isBlank(user.getPassword()) || StringUtils.isBlank(user.getNickname()))
			return true;	
		return false;
	}
	
	/**
	 * <p>判断客户端用户与服务器端用户是否有歧义性，所有更新操作都必须保证两端操作的用户实体是唯一的。</p>
	 * 
	 * @param user - 服务器端用户
	 * @param id - 客户端用户
	 * @return 两者是否是同一用户
	 */
	public static boolean isUnique(User user, int id) {
		if(id == 0 || user == null || user.getId() == null) {
			return false;
		}
		return user.getId().equals(id);
	}
	
	/**
	 * 根据头像（图片）文件名获取头像
	 * @param avatarName
	 * @return
	 */
	public static InputStream getAvatar(String avatarName) {
		if(avatarName == null) {
			return null;
		}
		String rootPath = Global.getConfig("userfiles.basedir");
		File file = new File(rootPath + "/avatar/" + avatarName);
		if(!file.exists()) {
			logger.debug("对应的头像不存在，图片名：" + avatarName);
			return null;
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
		} catch(IOException ex) {
			logger.debug("对应的头像不存在，图片名：" + avatarName);
		}
		return fis;
	}
	
	/**
	 * 验证客户端输入的验证码是否正确
	 * @param clientCaptcha - 客户端传来的验证码
	 * @param serverCaptcha	- 服务器端保存的验证码
	 * @return
	 */
	public static boolean validateCaptcha(String clientCaptcha, String serverCaptcha) {
		if(!StringUtils.isBlank(clientCaptcha) && !StringUtils.isBlank(serverCaptcha)) {
			return clientCaptcha.equalsIgnoreCase(serverCaptcha);
		}
		return false;
	}
	
	// ========= User Cache ==========
	
	public static Object getCache(String key) {
		return getCache(key, null);
	}
	
	public static Object getCache(String key, Object defaultValue) {
		Object obj = getSession().getAttribute(key);
		return obj == null ? defaultValue : obj;
	}

	public static void putCache(String key, Object value) {
		getSession().setAttribute(key, value);
	}

	public static void removeCache(String key) {
		getSession().removeAttribute(key);
	}

}
