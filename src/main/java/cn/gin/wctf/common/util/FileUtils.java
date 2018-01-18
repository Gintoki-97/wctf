package cn.gin.wctf.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.io.IOUtils;

/**
 * 文件相关操作工具类
 * 
 * @author Gintoki
 * @version 2017-11-01
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
	
	/**
     * The number of bytes in a kilobyte.
     */
	@SuppressWarnings("unused")
	private static final int ONE_KB = 1024;
	
	/**
     * The number of bytes in a megabyte.
     */
	private static final int ONE_MB = 1024 * 1024;

	/**
	 * <p>获取指定 URI 对应的文本文件的内容。</p>
	 * 
	 * @param uri - 文本文件的地址
	 * @return 文本文件的内容
	 */
	public static String getContent(String uri) {
		return getContent(new File(uri));
	}
	
	/**
	 * <p>获取指定文件的文本内容。</p>
	 * 
	 * @param file - 指定文件
	 * @return 文本文件的内容
	 */
	public static String getContent(File file) {
		if(!file.exists()) {
			return null;
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileChannel channel = fis.getChannel();
			String res = "";
			if(channel.size() <= FileUtils.ONE_MB * 5) {
				ByteBuffer buf = ByteBuffer.allocate((int) channel.size());
				channel.read(buf);
				if(buf.hasArray()) {
					res = new String(buf.array(), "UTF-8");
				} else {
					buf.flip();
					byte[] b = new byte[buf.remaining()];
					buf.get(b, 0, b.length);
					res = new String(b, "utf-8");
				}
			} else {
				ByteBuffer buf = ByteBuffer.allocate(FileUtils.ONE_MB);
				while(channel.read(buf) != -1) {
					buf.flip();
					byte[] b = new byte[buf.remaining()];
					buf.get(b, 0, b.length);
					res += new String(b, "utf-8");
					buf.flip();
				}
			}
			return res;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return null;
	}
	
	
	public static void transferStream(InputStream in, OutputStream out) throws IOException {
		if(in == null || out == null) {
			throw new IllegalArgumentException("The in stream or out stream cannot be null.");
		}
		byte[] buf = new byte[2048];
		int len = -1;
		while((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
	}
	
}
