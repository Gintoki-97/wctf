package cn.gin.wctf.common.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 * 测试 JCodec 工具类
 * @author Gintoki
 * @see cn.gin.wctf.common.util.JCodec
 * @version 2017-10-03
 */
public class JCodecTest {
	
	private static AtomicInteger count = new AtomicInteger(1);
	
	@Rule
	public TestName name = new TestName();
	
	private void runAs(Object result) {
		String mName = name.getMethodName();
		mName = mName.substring(4, mName.length());
		System.out.println("Test " + count + " : " +  mName + "\r\n\t==>\t The result:" + result.toString());
		count.getAndIncrement();
	}

	@Test
	public void testHexEncoder() {
		this.runAs(JCodec.hexEncode("Gintoki".getBytes()));
	}
	
	@Test
	public void testHexDecode() {
		String hexEncode = JCodec.hexEncode("Gintoki".getBytes());
		this.runAs(new String(JCodec.hexDecode(hexEncode)));
	}
	
	@Test
	public void testBase64EncodeWithBytes() {
		this.runAs(JCodec.base64Encode("http://www.gintoki.cn/wctf?legal=false&user=gin".getBytes()));
	}
	
	@Test
	public void testBase64EncodeWithStr() {
		this.runAs(JCodec.base64Encode("http://www.gintoki.cn/wctf?legal=false&user=汤姆"));
	}
	
	@Test
	public void testBase64DecodeToBytes() {
		String data = JCodec.base64Encode("http://www.gintoki.cn/wctf?legal=false&user=gin".getBytes());
		this.runAs(new String(JCodec.base64Decode(data)));
	}
	
	@Test
	public void testBase64EncodeToStr() {
		String base64Encode = JCodec.base64Encode("http://www.gintoki.cn/wctf?legal=false&user=汤姆");
		this.runAs(JCodec.base64DecodeToStr(base64Encode));
	}
	
	@Test
	public void testUrlEncode() {
		this.runAs(JCodec.urlEncode("http://www.gintoki.cn/wctf?legal=false&user=gin"));
	}
	
	@Test
	public void testUrlDecode() {
		String urlEncode = JCodec.urlEncode("http://www.gintoki.cn/wctf?legal=false&user=汤姆");
		this.runAs(JCodec.urlDecode(urlEncode));
	}
	
	@Test
	public void testMd5Encode() {
		String md5Encode = "729844";
		this.runAs(JCodec.md5Encode(md5Encode));
	}
	
	@Test
	public void testSalt() {
		this.runAs(JCodec.salt());
	}
	
	@Test
	public void testMd5SaltEncode() {
		String pwd = "729844";
		String salt = JCodec.salt();
		this.runAs(JCodec.md5SaltEncode(JCodec.md5Encode(pwd), JCodec.md5Encode(salt)));
	}
	
	@Test
	public void testMd5SaltEncode2() {
		String pwd = "729844";
		String md5Pwd = JCodec.md5Encode(pwd);
		String salt = JCodec.salt();
		String md5Salt = JCodec.md5Encode(salt);
		String md5SaltPwd = JCodec.sha1SaltEncode(md5Pwd, md5Salt);
		System.out.println("Test " + count + " : \r\n\t"
				+ "==>\t The 【pwd】:" + pwd + "\r\n\t"
				+ "==>\t The 【salt】:" + salt + "\r\n\t"
				+ "==>\t The 【md5(pwd)】:" + md5Pwd + "\r\n\t"
				+ "==>\t The 【md5(salt)】:" + md5Salt + "\r\n\t"
				+ "==>\t The 【md5(md5(pwd),md5(salt))】:" + md5SaltPwd + "\r\n");
	}
	
	@Test
	public void testSha1Encode() {
		this.runAs(JCodec.sha1Encode("729844"));
	}
	
}
