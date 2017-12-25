package cn.gin.wctf.module.sys.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.security.shiro.session.SessionDAO;
import cn.gin.wctf.common.web.Servlets;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.util.UserUtils;

/**
 * <p>系统管理，安全相关实体的管理类。</p>
 * 
 * @author Gintoki
 * @version 2017-10-03
 */
@Service
public class SystemService implements InitializingBean {

	@Autowired
	private transient SessionDAO sessionDao;
	
	public SessionDAO getSessionDao() {
		return sessionDao;
	}
	
	public void afterPropertiesSet() throws Exception {
		
	}
	
	public static boolean loadProperties() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n======================================================================\r\n");
		sb.append("\r\n====================       欢迎使用 " + Global.getConfig("productName") + "\t=======================\r\n");
		sb.append("\r\n======================================================================\r\n");
		System.out.println(sb.toString());
		return true;
	}

	public User getLogin() {
		HttpServletRequest req = Servlets.getRequest();
		if(req != null) {
			// 获取当前在线用户
			User login = (User) req.getSession().getAttribute("user");
			return login;
		}
		return null;
	}
	
	public boolean updateLogin(User user) {
		HttpServletRequest req = Servlets.getRequest();
		if(user != null && req != null) {
			req.getSession().setAttribute("user", user);
			return true;
		}
		return false;
	}
	
	public User getUserByLoginName(String account) {
		return UserUtils.getByLoginName(account);
	}
	
	
}
