package cn.gin.wctf.common.util;

import java.util.Arrays;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 电子邮件工具类
 * 
 * @author Gintoki
 * @version 2017-10-03
 */
public class EmailUtils {
	
	/**
	 * The default root logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(EmailUtils.class);
	
	// -------------
	// Required
	// -------------
	
	private String from;
	private String smtp;
	private String account;
	private String password;
	
	// -------------
	// Optional
	// -------------
	
	private String conpany;
	private String charset = "UTF-8";
	private String[] receivers;
	
	
	public String getConpany() {
		return conpany;
	}

	public void setConpany(String conpany) {
		this.conpany = conpany;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	public String[] getReceivers() {
		return receivers;
	}
	
	/**
	 * <p>邮件工具类的唯一构造函数，接收四个必须参数。</p>
	 * 
	 * @param form - 发件人
	 * @param smtp - 邮件发送方 SMTP 服务器地址
	 * @param account - 邮件发送者账号
	 * @param password - 邮件发送者密码
	 */
	public EmailUtils(String form, String smtp, String account, String password) {
		this.from = form;
		this.smtp = smtp;
		this.account = account;
		this.password = password;
	}

	// ------------------------
	// Service
	// ------------------------
	
	/**
	 * 添加收件人列表，每次添加收件人都会情空之前的收件人，所以需要一次添加完毕。
	 * 
	 * @param to 收件人列表
	 */
	public void addTo(String... to) {
		this.receivers = to;
	}

	/**
	 * 发送一封 HTML 模板的 Email，在发送之前请先调用 addTo 方法，设置收件人列表。
	 * 
	 * @param subject - 邮件主题
	 * @param content - 邮件内容
	 * @throws EmailException 任何异常情况（邮箱服务器错误，收件人无效等等）都会导致邮件发送异常抛出
	 */
	public void sendHtmlEmail(String subject, String content) throws EmailException {
		if(receivers == null || receivers.length == 0) {
			logger.debug("The program canceled the send request, becauseof there is no receiver.");
			return;
		}
		long start = System.currentTimeMillis();
		HtmlEmail email = new HtmlEmail();
		email.setCharset(charset);
		email.setHostName(smtp);
		email.setAuthentication(account, password);
		email.setSSLOnConnect(true);
		email.addTo(receivers);
		email.setFrom(from, conpany);
		email.setSubject(subject);
		email.setMsg(content);
		email.send();
		long end = System.currentTimeMillis();
		if(logger.isDebugEnabled()) {
			logger.debug("Send HTML Email success[To: {1}, waste time: {2}]", Arrays.toString(receivers), end - start);
		}
	}
}