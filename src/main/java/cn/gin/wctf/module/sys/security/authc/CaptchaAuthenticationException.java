package cn.gin.wctf.module.sys.security.authc;

import org.apache.shiro.authc.AuthenticationException;

/**
 * <p>当 Shiro 进行验证码校验并且出错的时候，会抛出此异常。
 * <p>此异常非 Shiro 官方提供，是作为应用登录模块的扩展功能实现的。可以为数据状态提供更为详细的信息描述。</p>
 * 
 * @author Gintoki
 * @version 2017-10-22
 */
@SuppressWarnings("serial")
public class CaptchaAuthenticationException extends AuthenticationException {
	
	/**
     * 创建一个新的 CaptchaAuthenticationCaptcha。
     */
    public CaptchaAuthenticationException() {
        super();
    }

    /**
     * 创建一个新的 CaptchaAuthenticationCaptcha。
     *
     * @param message 异常发生的原因
     */
    public CaptchaAuthenticationException(String message) {
        super(message);
    }

    /**
     * 创建一个新的 CaptchaAuthenticationCaptcha。
     *
     * @param 导致该异常被抛出的潜在可抛出问题
     */
    public CaptchaAuthenticationException(Throwable cause) {
        super(cause);
    }

    /**
     * 创建一个新的 CaptchaAuthenticationCaptcha。
     *
     * @param message 异常发生的原因
     * @param cause   导致该异常被抛出的潜在可抛出问题
     */
    public CaptchaAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
