package cn.gin.wctf.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCrypt;

import org.apache.commons.codec.DecoderException;

/**
 * 封装各种格式的编码解码工具类.
 * 1.commons-codec 的 hex/base64 编码
 * 2.编码实现的base62编解码
 * 3.commons-lang 的 xml/html escape
 * 4.JDK 提供的 URLEncoder
 * @author Gintoki
 * @version 2017-10-03
 */
public class JCodec {
	
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * 十六进制编码，普通字符串 => 十六进制字符串
	 * @param data 需要编码的字节数组
	 */
	public static String hexEncode(byte[] data) {
		return new String(Hex.encodeHex(data));
	}
	
	/**
	 * 十六进制编码，普通字符串 => 十六进制字符串
	 * @param data 需要编码的普通字符串
	 * @param charset 
	 * @return 编码后的十六进制字符串
	 */
	public static String hexEncode(String data, Charset charset) {
		return new String(Hex.encodeHex(data.getBytes(charset)));
	}
	
	/**
	 * 十六进制解码
	 * @param data 编码后的十六进制字符串
	 * @return 解码后的普通字符串
	 */
	public static byte[] hexDecode(String data) {
		try {
			return Hex.decodeHex(data.toCharArray());
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Base64编码
	 * @param data 需要编码的普通字符串对应的字节数组
	 */
	public static String base64Encode(byte[] data) {
		return new String(Base64.encodeBase64(data));
	}
	
	/**
	 * Base64编码，将字符串按照 utf-8 的编码方式转为字节数组
	 * @param data 需要编码的普通字符串
	 */
	public static String base64Encode(String data) {
		try {
			return new String(Base64.encodeBase64(data.getBytes(DEFAULT_URL_ENCODING)));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Base64解码
	 * @param data 需要解码的Base64字符串
	 */
	public static byte[] base64Decode(String data) {
		return Base64.decodeBase64(data.getBytes());
	}
	
	/**
	 * Base64解码
	 * @param data 需要解码的Base64字符串
	 */
	public static String base64DecodeToStr(String data) {
		try {
			return new String(Base64.decodeBase64(data.getBytes()), DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Base62编码
	 */
	public static String base62Encode(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}
	
	/**
	 * URL 编码，默认为 u8 字符集
	 */
	public static String urlEncode(String data) {
		try {
			return URLEncoder.encode(data, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * URL 解码，默认为 u8 字符集
	 */
	public static String urlDecode(String data) {
		try {
			return URLDecoder.decode(data, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * md5 加密
	 */
	public static String md5Encode(String data) {
		try {
			//1. 获取消息摘要
			MessageDigest digest = MessageDigest.getInstance("MD5");
			//2. 加密的十进制字节数组 
			byte[] md5BytesValue = digest.digest(data.getBytes());
			//3. 转为十六进制
			return new BigInteger(1, md5BytesValue).toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * md5 加盐加密
	 */
	public static String md5SaltEncode(String data, String salt) {
		return md5Encode(data + salt);
	}
	
	/**
	 * sha1加密
	 */
	public static String sha1Encode(String str){
	    if (null == str || 0 == str.length()){
	        return null;
	    }
	    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	            'a', 'b', 'c', 'd', 'e', 'f'};
	    try {
	        MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
	        mdTemp.update(str.getBytes("UTF-8"));
	         
	        byte[] md = mdTemp.digest();
	        int j = md.length;
	        char[] buf = new char[j * 2];
	        int k = 0;
	        for (int i = 0; i < j; i++) {
	            byte byte0 = md[i];
	            buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
	            buf[k++] = hexDigits[byte0 & 0xf];
	        }
	        return new String(buf);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 * sha1 加盐加密
	 */
	public static String sha1SaltEncode(String data, String salt) {
		return sha1Encode(data + salt);
	}
	
	/**
	 * 随机生成salt
	 * @return
	 */
	public static String salt() {
		return BCrypt.gensalt();
	}
	
	/**
	 * 随机生成md5加密的salt
	 * @return
	 */
	public static String md5Salt() {
		return md5Encode(BCrypt.gensalt());
	}
	
	public static void main(String[] args) {
		String pwd = "729844";
		String md5Pwd = md5Encode(pwd);
		String salt = salt();
		String md5Salt = md5Encode(salt);
		String md5SaltPwd = sha1SaltEncode(md5Pwd, md5Salt);
		System.out.println("【salt】" + salt);
		System.out.println("【md5(pwd)】" + md5Pwd);
		System.out.println("【md5(salt)】" + md5Salt);
		System.out.println("【md5(saltPwd)1】" + md5SaltPwd);
		System.out.println("【md5(saltPwd)2】" + md5SaltEncode("$1$4IPLWlpQ$bUlaj50FcNOyYXLWbaS1.0", "$1$xQErFR86$.l5VXfchIZxR5mwdFLurb/"));
	}
}
