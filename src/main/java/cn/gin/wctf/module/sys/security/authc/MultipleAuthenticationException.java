package cn.gin.wctf.module.sys.security.authc;

import org.apache.shiro.authc.AuthenticationException;

/**
 * <p>当 Shiro 进行对当前用户登录验证通过，但发现对于该账号有多个活动 Session ，并且全局配置文件中声明了不允许重复登录
 * （user.multiAccountLogin != true）时，会抛出此异常。</p>
 * <p>此异常非 Shiro 官方提供，是作为应用登录模块的扩展功能实现的。可以为数据状态提供更为详细的信息描述。</p>
 * 
 * @author Gintoki
 * @version 2017-10-22
 */
@SuppressWarnings("serial")
public class MultipleAuthenticationException extends AuthenticationException {
	
	/**
     * 创建一个新的 MultipleAuthenticationException。
     */
    public MultipleAuthenticationException() {
        super();
    }

    /**
     * 创建一个新的 MultipleAuthenticationException。
     *
     * @param message 异常发生的原因
     */
    public MultipleAuthenticationException(String message) {
        super(message);
    }

    /**
     * 创建一个新的 MultipleAuthenticationException。
     *
     * @param 导致该异常被抛出的潜在可抛出问题
     */
    public MultipleAuthenticationException(Throwable cause) {
        super(cause);
    }

    /**
     * 创建一个新的 MultipleAuthenticationException。
     *
     * @param message 异常发生的原因
     * @param cause   导致该异常被抛出的潜在可抛出问题
     */
    public MultipleAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
