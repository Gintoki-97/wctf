package cn.gin.wctf.module.sys.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.util.UserUtils;

/**
 * 表单验证（包含验证码）过滤类
 * @author Gintoki
 * @version 2017-10-09
 */
@Component
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public static final String DEFAULT_USERNAME_PARAM = "account";
	public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
	public static final String DEFAULT_EMAIL_PARAM = "emailLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";
	
	private String usernameParam = DEFAULT_USERNAME_PARAM;
	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	private String emailLoginParam = DEFAULT_EMAIL_PARAM;
	private String messageParam = DEFAULT_MESSAGE_PARAM;
	
	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String account = getUsername(request);
		String password = getPassword(request);
		password = password == null ? "" : password;
		boolean rememberMe = isRememberMe(request);
		String host = StringUtils.getRemoteAddr((HttpServletRequest) request);
		String captcha = getCaptcha(request);
		boolean emailLogin = isEmailLogin(request);
		return new UsernamePasswordToken(account, password.toCharArray(), rememberMe, host, captcha, emailLogin);
	}
	
	@Override
	public String getUsernameParam() {
		return this.usernameParam;
	}

	@Override
	protected String getUsername(ServletRequest request) {
		String account = WebUtils.getCleanParam(request, getUsernameParam());
		if(StringUtils.isBlank(account)) {
			account = StringUtils.toString(request.getAttribute(getUsernameParam()), StringUtils.EMPTY);
		}
		return account;
	}
	
	@Override
	protected String getPassword(ServletRequest request) {
		String password = super.getPassword(request);
		if(StringUtils.isBlank(password)) {
			password = StringUtils.toString(request.getAttribute(getPasswordParam()), StringUtils.EMPTY);
		}
		return password;
	}
	
	/**
	 * 获取记住我
	 */
	@Override
	protected boolean isRememberMe(ServletRequest request) {
		String rememberMe = WebUtils.getCleanParam(request, getRememberMeParam());
		if (StringUtils.isBlank(rememberMe)){
			rememberMe = StringUtils.toString(request.getAttribute(getRememberMeParam()), StringUtils.EMPTY);
		}
		return StringUtils.toBoolean(rememberMe);
	}
	
	/**
	 * 登录成功调用事件
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (!"XMLHttpRequest".equalsIgnoreCase(req.getHeader("X-Requested-With"))) {	// 不是ajax请求
            issueSuccessRedirect(request, response);
        } else {
        	// 设置响应头
        	resp.setCharacterEncoding("UTF-8");
        	resp.setContentType("application/json;charset=utf-8");
        	// 准备数据
        	ApplicationDataSupport data = new ApplicationDataSupport(true, 10, "登录成功");
        	try {
        		data.setParameter("referer", WebUtils.getSavedRequest(request).getRequestUrl());
        	} catch(Exception e) {
        		// Catching
        	}
        	// 响应
            PrintWriter out = resp.getWriter();
            out.println(data.toJson());
            out.flush();
            out.close();
        }
        return true;
	}
	
	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		if (!"XMLHttpRequest".equalsIgnoreCase(req.getHeader("X-Requested-With"))) {	// 不是ajax请求
            setFailureAttribute(req, e);
            return true;
        }
		try {
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json;charset=utf-8");
	    	PrintWriter out = resp.getWriter();
	    	String message = e.getClass().getSimpleName();
	    	ApplicationDataSupport data = preparedDataSupport(token);
	    	data.setParameter("validate", UserUtils.isCaptchaLogin((String) data.getParameter("account"), true, false));
	    	if ("IncorrectCredentialsException".equals(message)) {
	    		data.setStatus(121);
	    		data.setMsg("密码错了奥");
	    	} else if ("UnknownAccountException".equals(message)) {
	    		data.setStatus(122);
	    		data.setMsg("账号不存在");
	    	} else if ("LockedAccountException".equals(message)) {
	    		data.setStatus(123);
	    		data.setMsg("账号被锁定");
	    	} else if ("CaptchaAuthenticationException".equals(message)) {
	    		data.setStatus(124);
	    		data.setMsg("验证码错误，请重试");
	    	} else if ("MultipleAuthenticationException".equals(message)) {
	    		data.setStatus(13);
	    		data.setMsg("当前用户已在别处登录，请重新登录");
	    	} else {
	    		data.setStatus(125);
	    		data.setMsg("未知错误");
	    		e.printStackTrace();
	    	}
	    	out.write(data.toJson());
	        out.flush();
	        out.close();
		} catch (IOException ex) {
	    	ex.printStackTrace();
	    }
        return false;
    }
	
	/**
	 * 访问拒绝时调用事件
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if(this.isLoginRequest(request, response)) {
            if(this.isLoginSubmission(request, response)) {
                if(logger.isDebugEnabled()) {
                	logger.debug("Login submission detected.  Attempting to execute login.");
                }
                return this.executeLogin(request, response);
            } else {
                if(logger.isDebugEnabled()) {
                	logger.debug("Login page view.");
                }
                return true;
            }
        } else {
            if(logger.isDebugEnabled()) {
            	logger.debug("Attempting to access a path which requires authentication.  Forwarding to the Authentication url [" + this.getLoginUrl() + "]");
            }
            this.saveRequestAndRedirectToLogin(request, response);
            return false;
        }
	}
	
	/**
	 * 预处理返回的json对象
	 * @param request
	 * @return
	 */
	private ApplicationDataSupport preparedDataSupport(AuthenticationToken authToken) {
		UsernamePasswordToken token = (UsernamePasswordToken) authToken;
		ApplicationDataSupport data = new ApplicationDataSupport();
		data.setParameter("account", token.getUsername());
		return data;
	}
	

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}
	
	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

	protected boolean isEmailLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getEmailLoginParam());
    }
	
	public String getEmailLoginParam() {
		return emailLoginParam;
	}

	public void setemailLoginParam(String emailLoginParam) {
		this.emailLoginParam = emailLoginParam;
	}

	public String getMessageParam() {
		return messageParam;
	}

	public void setMessageParam(String messageParam) {
		this.messageParam = messageParam;
	}

	@Override
	public void setUsernameParam(String usernameParam) {
		this.usernameParam = usernameParam;
	}
	
}
