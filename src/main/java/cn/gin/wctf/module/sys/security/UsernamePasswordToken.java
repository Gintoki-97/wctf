package cn.gin.wctf.module.sys.security;


/**
 * 用户认证信息令牌类，包含用户名，密码，验证码
 * @author Gintoki
 * @version 2017-10-05
 */
@SuppressWarnings("serial")
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

	
	private String captcha;
	private boolean emailLogin;
	
	public UsernamePasswordToken() {
		super();
	}
	
	public UsernamePasswordToken(String account, char[] password) {
		super(account, password);
	}
	
	public UsernamePasswordToken(String account, char[] password,
			boolean rememberMe, String host, String captcha, boolean emailLogin) {
		super(account, password, rememberMe, host);
		this.captcha = captcha;
		this.emailLogin = emailLogin;
	}
	
	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public boolean isEmailLogin() {
		return emailLogin;
	}
	
	public void setEmailLogin(boolean emailLogin) {
		this.emailLogin = emailLogin;
	}
	
	@Override
	public String toString() {
		return "UsernamePasswordToken[username:" + getUsername() + ", password:" + String.valueOf(getPassword());
	}

}
