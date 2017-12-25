package cn.gin.wctf.common.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.gin.wctf.common.util.CaptchaUtils;

/**
 * 用于生成验证码的控制器接口，可以为指定的请求生成不同的验证码.
 * 
 * @author Gintoki
 * @version 2017-10-01
 */
@WebServlet({"/common/captcha/number", "/common/captcha/letter", "/common/captcha/chars", "/common/captcha/image"})
@SuppressWarnings("serial")
public class CaptchaServlet extends HttpServlet {

	/**
	 * The default root logger.
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private int w = 70;
	private int h = 26;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 设置响应头信息
		resp.setHeader("Pragma", "no-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		
		// 处理验证码相关请求
		String uri = req.getRequestURI();
		if(uri == null) {
			logger.error("The request uri can not be null");
			return;
		}
		if(uri.endsWith("image")) {
			ServletOutputStream out = resp.getOutputStream();
			ImageIO.write(createImage(req), "JPEG", out);
			out.close();
		} else {
			PrintWriter out = resp.getWriter();
			if(uri.endsWith("number")) {
				out.write(CaptchaUtils.buildNumbers(4));
			} else if(uri.endsWith("letter")) {
				out.write(CaptchaUtils.buildLetters(4));
			} else if(uri.endsWith("chars")) {
				out.write(CaptchaUtils.buildChars(4));
			}
			out.flush();
			out.close();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
	// --------------------
	// Support
	// --------------------
	
	private BufferedImage createImage(HttpServletRequest request) throws IOException {

		/*
		 * 得到参数高，宽，都为数字时，则使用设置高宽，否则使用默认值
		 */
		String width = request.getParameter("width");
		String height = request.getParameter("height");
		if (StringUtils.isNumeric(width) && StringUtils.isNumeric(height)) {
			w = NumberUtils.toInt(width);
			h = NumberUtils.toInt(height);
		}
		
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();
		
		/*
		 * 为图像生成背景
		 */
		drawBackground(g);
		
		/*
		 * 为图像生成字符
		 */
		String s = drawCharacter(g);
		request.getSession().setAttribute(CaptchaUtils.IMAGE_CAPTCHA, s);
		
		/*
		 * 释放资源
		 */
		g.dispose();
		
		return image;
	}

	private void drawBackground(Graphics g) {
		// 填充背景
		g.setColor(randomColor(220, 250));
		g.fillRect(0, 0, w, h);
		// 加入干扰线
		for (int i = 0; i < 8; i++) {
			g.setColor(randomColor(40, 150));
			Random random = new Random();
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			int x1 = random.nextInt(w);
			int y1 = random.nextInt(h);
			g.drawLine(x, y, x1, y1);
		}
		
	}
	
	private String drawCharacter(Graphics g) {
		String[] fonts = {"Arial","Arial Black","AvantGarde Bk BT","Calibri"};
		Random random = new Random();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			String r = String.valueOf(CaptchaUtils.chars[random.nextInt(CaptchaUtils.chars.length)]);
			g.setColor(new Color(50 + random.nextInt(100), 50 + random.nextInt(100), 50 + random.nextInt(100)));
			g.setFont(new Font(fonts[random.nextInt(fonts.length)], Font.BOLD, 24)); 
			g.drawString(r, 15 * i + 5, 19 + random.nextInt(8));
			s.append(r);
		}
		return s.toString();
	}
	
	private Color randomColor(int fc, int bc) {
		if(fc > 255) 
			fc = 255;
		if(bc > 255)	
			bc = 255;
		int range = bc - fc;
		Random rd = new Random();
		return new Color(fc + rd.nextInt(range), fc + rd.nextInt(range), fc + rd.nextInt(range));
	}
}
