package cn.gin.wctf.module.post.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.gin.wctf.common.paging.Page;
import cn.gin.wctf.common.util.CaptchaUtils;
import cn.gin.wctf.common.validator.post.PostSendGroup;
import cn.gin.wctf.common.validator.post.ReplyGroup;
import cn.gin.wctf.module.news.entity.News;
import cn.gin.wctf.module.news.service.NewsService;
import cn.gin.wctf.module.post.entity.Post;
import cn.gin.wctf.module.post.entity.Reply;
import cn.gin.wctf.module.post.service.PostService;
import cn.gin.wctf.module.post.service.ReplyService;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.service.SystemService;
import cn.gin.wctf.module.sys.util.UserUtils;

/**
 * <p>发贴控制器</p>
 * <p>此控制器提供的所有方法接口均属于发贴模块。所有有关发贴操作的请求比如查看法帖，发贴，回复发贴等，它们在语义上
 * 就属于发贴模块。这些方法都由此控制器统一管理。</p>
 * 
 * @author Gintoki
 * @version 2017-10-12
 */
@Controller
@RequestMapping("/post")
public class PostController {
	
	/**
	 * The default logger.
	 */
	private Logger logger = LoggerFactory.getLogger(PostController.class);
	
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private ReplyService replyService;
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private SystemService systemService;
	
	// ---------------------
	// Service
	// ---------------------
	
	/**
	 * <p>用户查看发贴时，用户在发贴列表页或者访问某一个指定发贴 ID 的 URL 时，会请求此控制器方法接口进行处理。</p>
	 * 
	 * @param id - 指定的发贴 ID
	 * @param model - 模型数据
	 * @return 视图
	 */
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
	public String browse(@PathVariable Integer id, Model model) {
		// 填充发贴数据
		Post post = postService.getPostById(id);
		postService.updateViewed(id);
		model.addAttribute("post", post);
		// 填充新闻数据
		Map<String, List<News>> classifyNews = newsService.prepareIndexNews();
		model.addAttribute("activityNews", classifyNews.get("activityNews"));
		model.addAttribute("bbsNews", classifyNews.get("bbsNews"));
		// 返回视图
		return "/post/PostDetail";
	}
	
	/**
	 * <p>用户发贴视图请求，用户在地址栏里（GET）访问当前 URL 时，会请求此控制器方法处理。</p>
	 * 
	 * @return 视图
	 */
	@RequestMapping(value = "/send", method = RequestMethod.GET)
	public String sendViewRequest() {
		if (logger.isDebugEnabled()){
			logger.debug("PostSend view request is accepted");
		}
		return "/post/PostSend";
	}
	
	/**
	 * <p>用户发贴请求，用户在发贴页面进行发贴请求提交时，会请求此控制器方法处理。</p>
	 * <p>StatusCode: 300 ~ 319</p>
	 * 
	 * @param post - 由页面传入的发贴信息
	 * @param imageCaptcha - 客户端输入的图片验证码
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public void send(String imageCaptcha, HttpServletRequest req, HttpServletResponse resp, 
			@Validated(PostSendGroup.class) Post post, BindingResult postBindingResult) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据校验
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = systemService.getLogin();
		if(post == null || !UserUtils.isUnique(login, post.getUserId())) {
			data.setStatus(12);
		} else {
			if(postBindingResult.hasErrors()) {
				if(logger.isDebugEnabled()) {
					for (ObjectError objectError  : postBindingResult.getAllErrors()) {
						logger.debug(objectError.getDefaultMessage());
					}
				}
				data.setStatus(301);
				data.setMsg("数据准备失败");
			} else {
				if(!UserUtils.validateCaptcha(imageCaptcha, (String) req.getSession().getAttribute(CaptchaUtils.IMAGE_CAPTCHA))) {
					data.setStatus(303);
					data.setMsg("验证码错误");
				} else {
					// 数据准备
					data.setParameter("login", login);
					data.setParameter("post", post);
					// 交由 service 层处理
					postService.sendPost(data);
				}
			}
		}
		PrintWriter out = resp.getWriter();
		out.print(data.toJson());
		out.flush();
		out.close();
	}
	
	/**
	 * <p>用户分类查看发贴视图请求，用户在首页或分类查看发贴页面进行查看指定类型的发贴请求时，会请求此控制器方法处理。</p>
	 * 
	 * @param model - 模型数据
	 * @param req - 用于获取请求路径中指定的发贴分类
	 * @return 视图
	 */
	@RequestMapping(value = {"/discuss", "/question", "/suggest", "/sharing", "/notice", "/activity"}, method = RequestMethod.GET)
	public String classifyBrowse(Model model, HttpServletRequest req) {
		String uri = req.getRequestURI();
		String classify = uri.substring(uri.lastIndexOf("/") + 1);
		Page<Post> paging = postService.listPostByClassify(classify, 1);
		model.addAttribute("paging", paging);
		return "/post/PostClassify";
	}
	
	/**
	 * <p>用户分类查看发贴视图请求，并且要求使用分页。用户在首页或分类查看发贴页面进行查看指定类型的发贴请求时，
	 * 会请求此控制器方法处理。</p>
	 * 
	 * @param index - 分页参数，标识当前页
	 * @param req - 用于获取请求路径中指定的发贴分类
	 * @param model - 模型数据
	 * @return 视图
	 */
	@RequestMapping(value = {"/discuss/page/{index:\\d+}", "/question/page/{index:\\d+}", "/suggest/page/{index:\\d+}", "/sharing/page/{index:\\d+}", "/notice/page/{index:\\d+}", "/activity/page/{index:\\d+}"}, method = RequestMethod.GET)
	public String classifyBrowse(Model model, HttpServletRequest req, @PathVariable Integer index) {
		String uri = req.getRequestURI();
		String classify = uri.substring(uri.lastIndexOf("/") + 1);
		Page<Post> paging = postService.listPostByClassify(classify, index);
		model.addAttribute("paging", paging);
		return "/post/PostClassify";
	}
	
	/**
	 * <p>用户的回贴请求，用户在发贴详情页面提交回贴表单，会请求此控制器接口进行处理。</p>
	 * <p>StatusCode: 320 ~ 339</p>
	 * 
	 * @param reply - 用户传入的回贴信息
	 * @param replyBindingResult - 用户参数校验结果集
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = {"/reply"}, method = RequestMethod.POST)
	public void reply(HttpServletRequest req, HttpServletResponse resp,
			@Validated(ReplyGroup.class) Reply reply, BindingResult replyBindingResult) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据校验
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = systemService.getLogin();
		if(!login.getId().equals(reply.getUserId())) {
			data.setStatus(12);
		} else {
			if(replyBindingResult.hasErrors()) {
				if(logger.isDebugEnabled()) {
					for (ObjectError objectError  : replyBindingResult.getAllErrors()) {
						logger.debug(objectError.getDefaultMessage());
					}
				}
				data.setStatus(321);
				data.setMsg("数据准备失败");
			} else {
				data.setParameter("reply", reply);
				// 交由 Service 层处理
				replyService.saveReply(data);
			}
		}
		PrintWriter out = resp.getWriter();
		out.print(data.toJson());
		out.flush();
		out.close();
	}
	
	/**
	 * <p>用户发起收藏发贴请求时，会请求此控制器方法处理。</p>
	 * <p>StatusCode: 370 ~ 379</p>
	 * 
	 * @param userId - 由页面传入的发起点赞请求的用户的 ID
	 * @param postId - 由页面传入的发贴 ID，用于实现 RESTFul 风格的 URL
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/coll/{postId:\\d+}", method = RequestMethod.POST)
	public void collect(Integer userId, @PathVariable Integer postId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据校验
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = systemService.getLogin();
		if(userId == null || !UserUtils.isUnique(login, userId)) {
			data.setStatus(12);
		} else {
			// 数据准备
			data.setParameter("userId", userId);
			data.setParameter("postId", postId);
			// 交由 service 层处理
			postService.saveCollect(data);
		}
		PrintWriter out = resp.getWriter();
		out.print(data.toJson());
		out.flush();
		out.close();
	}
	
}
