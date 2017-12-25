package cn.gin.wctf.common.util;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.UUID;

import org.activiti.engine.impl.cfg.IdGenerator;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 生成唯一性 ID 算法的工具类.
 * @author Gintoki
 * @version 207-10-04
 */
@Service
@Lazy(false)
public class IdentityGenerator implements IdGenerator, SessionIdGenerator {

	private static SecureRandom random = new SecureRandom();
	
	/**
	 * JDK的uuid算法，去掉默认格式中的'-'
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
	 * 使用SecureRandom随机生成Long. 
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}
	
	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return JCodec.base62Encode(randomBytes);
	}
	
	/**
	 * Activiti ID 生成
	 */
	public String getNextId() {
		return IdentityGenerator.uuid();
	}

	/**
	 * 用于生成 shiro session id
	 */
	public Serializable generateId(Session session) {
		return IdentityGenerator.uuid();
	}
}
