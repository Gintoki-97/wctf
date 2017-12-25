package cn.gin.wctf.module.post.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.gin.wctf.module.post.entity.SlReply;
import cn.gin.wctf.module.post.service.ReplyService;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.util.UserUtils;

/**
 * <p>回贴控制器</p>
 * <p>此控制器提供的所有方法接口均属于回贴模块。所有有关回贴操作的请求比如回复发贴等，它们在语义上
 * 就属于回贴模块。这些方法都由此控制器统一管理。</p>
 * 
 * @author Gintoki
 * @version 2017-10-12
 */
@Controller
@RequestMapping("/reply")
public class ReplyController {
	
	/**
	 * The default logger.
	 */
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(ReplyController.class);
	
	
	@Autowired
	private ReplyService replyService;
	
	
	// ---------------------
	// Service
	// ---------------------
	
	
	/**
	 * <p>用户回复回贴请求，用户在发贴详情页面进行对回贴进行回复请求时，会请求此控制器方法处理。</p>
	 * <p>StatusCode: 300 ~ 319</p>
	 * 
	 * @param replyId - 由页面传入的二级回贴对应的回贴 ID，用于实现 RESTFul 风格的 URL
	 * @param reply - 页面传入的二级回贴信息
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/{replyId:\\d+}", method = RequestMethod.POST)
	public void send(@PathVariable Integer replyId, HttpServletRequest req, HttpServletResponse resp,
			SlReply slReply) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据校验
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = (User) req.getSession().getAttribute("user");
		if(slReply == null || !UserUtils.isUnique(login, slReply.getUserId())) {
			data.setStatus(12);
		} else {
			// 数据准备
			data.setParameter("slReply", slReply);
			// 交由 service 层处理
			replyService.replyToReply(data);
		}
		PrintWriter out = resp.getWriter();
		out.print(data.toJson());
		out.flush();
		out.close();
	}
	
	/**
	 * <p>用户发起点赞回贴请求时，会请求此控制器方法处理。</p>
	 * <p>StatusCode: 360 ~ 369</p>
	 * 
	 * @param userId - 由页面传入的发起点赞请求的用户的 ID
	 * @param replyId - 由页面传入的回贴 ID，用于实现 RESTFul 风格的 URL
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/thumb/{replyId:\\d+}", method = RequestMethod.POST)
	public void thumb(Integer userId, @PathVariable Integer replyId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据校验
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = (User) req.getSession().getAttribute("user");
		if(userId == null || !UserUtils.isUnique(login, userId)) {
			data.setStatus(12);
		} else {
			// 数据准备
			data.setParameter("userId", userId);
			data.setParameter("replyId", replyId);
			// 交由 service 层处理
			replyService.replyThumb(data);
		}
		PrintWriter out = resp.getWriter();
		out.print(data.toJson());
		out.flush();
		out.close();
	}
	
}
