package cn.gin.wctf.module.resource.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.gin.wctf.common.paging.Page;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.module.news.entity.News;
import cn.gin.wctf.module.news.service.NewsService;
import cn.gin.wctf.module.resource.entity.Resource;
import cn.gin.wctf.module.resource.service.ResourceService;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.service.SystemService;
import cn.gin.wctf.module.sys.util.UserUtils;

/**
 * <p>用户资源管理控制器</p>
 * <p>此控制器提供的所有方法接口均属于资源（Resource）模块。所有有关资源操作的请求比如上传文档，上传工具，下载工具等，它们在语义上
 * 就属于资源模块。这些方法都由此控制器统一管理。</p>
 * 
 * @author Gintoki
 * @version 2017-11-22
 */
@Controller
@RequestMapping("/res")
public class ResourceController {
	
	/**
	 * The default logger.
	 */
	private Logger logger = LoggerFactory.getLogger(ResourceController.class);
	
	@Autowired
	private NewsService newsService;
	
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private SystemService systemService;
	
	// ---------------------
	// Service
	// ---------------------
	
	/**
	 * <p>用户查看某项资源时，比如用户在资源列表页或者访问某一个指定资源 ID 的 URL 时，会请求此控制器方法接口进行处理。</p>
	 * 
	 * @param id - 指定的资源 ID
	 * @param model - 模型数据
	 * @return 视图
	 */
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
	public String browse(@PathVariable Integer id, Model model) {
		// 填充资源信息
		Resource res = resourceService.getResById(id);
		model.addAttribute("res", res);
		// 填充新闻数据
		Map<String, List<News>> classifyNews = newsService.prepareIndexNews();
		model.addAttribute("activityNews", classifyNews.get("activityNews"));
		model.addAttribute("bbsNews", classifyNews.get("bbsNews"));
		// 返回视图
		return "/resource/ResDetail";
	}
	
	/**
	 * <p>用户查看网站所有资源时，会请求此控制其方法进行处理</p>
	 * 
	 * @param model - 模型数据
	 * @return 视图
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String resourceViewRequest(Model model) {
		return resourceViewRequest(model, 1);
	}
	
	/**
	 * <p>用户查看网站所有资源时，会请求此控制其方法进行处理</p>
	 * 
	 * @param model - 模型数据
	 * @return 视图
	 */
	@RequestMapping(value = "/all/page/{index:\\d+}", method = RequestMethod.GET)
	public String resourceViewRequest(Model model, @PathVariable Integer index) {
		Page<Resource> paging = resourceService.listResByClassify("all", index);
		model.addAttribute("paging", paging);
		// 填充新闻数据
		Map<String, List<News>> classifyNews = newsService.prepareIndexNews();
		model.addAttribute("activityNews", classifyNews.get("activityNews"));
		model.addAttribute("bbsNews", classifyNews.get("bbsNews"));
		return "/resource/ResList";
	}
	
	/**
	 * <p>用户分类查看网站资源的视图请求。用户在首页或分类查看资源页面进行查看指定类型的资源请求时，会请求此控制器方法处理。</p>
	 * 
	 * @param req - {@link HttpServletRequest}
	 * @param model - 模型数据
	 * @return 视图
	 */
	@RequestMapping(value = {"/doc", "/code", "/tool", "/other"}, method = RequestMethod.GET)
	public String classifyBrowse(Model model, HttpServletRequest req) {
		String uri = req.getRequestURI();
		String cls = uri.substring(uri.lastIndexOf("/") + 1);
		return classifyBrowse(model, req, 1, cls);
	}
	
	/**
	 * <p>用户分类查看网站资源的视图请求，并且要求使用分页。用户在首页或分类查看资源页面进行查看指定类型的资源请求时，
	 * 会请求此控制器方法处理。</p>
	 * 
	 * @param index - 分页参数，标识当前页
	 * @param req - {@link HttpServletRequest}
	 * @param model - 模型数据
	 * @return 视图
	 */
	@RequestMapping(value = {"/doc/page/{index:\\d+}", "/code/page/{index:\\d+}", "/tool/page/{index:\\d+}", "/other/page/{index:\\d+}"}, method = RequestMethod.GET)
	public String classifyBrowse(Model model, HttpServletRequest req, @PathVariable Integer index, String classify) {
		String cls = null;
		if(classify == null) {
			String uri = req.getRequestURI();
			uri = uri.substring(0, uri.indexOf("/page"));
			cls = uri.substring(uri.lastIndexOf("/") + 1);
		} else {
			cls = classify;
		}
		Page<Resource> paging = resourceService.listResByClassify(cls, index);
		model.addAttribute("paging", paging);
		// 填充新闻数据
		Map<String, List<News>> classifyNews = newsService.prepareIndexNews();
		model.addAttribute("activityNews", classifyNews.get("activityNews"));
		model.addAttribute("bbsNews", classifyNews.get("bbsNews"));
		// 填充分类信息
		model.addAttribute("items", "0-" + resourceService.toClassify(cls) + "-0");
		return "/resource/ResClassify";
	}
	
	/**
	 * <p>用户在按照指定条件检索资源时，会请求此方法进行检索符合条件的资源文件。</p>
	 * 
	 * @param items - 页面传入的检索条件，一般格式为：Tag-Classify-Order
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 * @throws ServletException 有关 Servlet 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public void search(HttpServletRequest req, HttpServletResponse resp, String items) throws IOException, ServletException {
		search(req, resp, items, 1);
	}
	
	/**
	 * <p>用户在按照指定条件检索资源时，会请求此方法进行检索符合条件的资源文件。</p>
	 * 
	 * @param items - 页面传入的检索条件，一般格式为：Tag-Classify-Order
	 * @param index - 分页参数，标识当前页
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 * @throws ServletException 有关 Servlet 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/search/page/{index:\\d+}", method = RequestMethod.GET)
	public void search(HttpServletRequest req, HttpServletResponse resp, String items, @PathVariable Integer index) throws IOException, ServletException {
		// 数据校验
		if(StringUtils.isBlank(items) || !items.matches("^\\d{1,2}-\\d{1,2}-\\d{1,2}$")) {
			// 参数格式不正确
			resp.sendError(404);
		} else {
			String[] itemsArr = items.split("-");
			int[] arr = new int[3];
			for (int i = 0; i < itemsArr.length; i++) {
				arr[i] = NumberUtils.toInt(itemsArr[i], 0);
			}
			Page<Resource> paging = resourceService.searchResByItem(arr, index);
			paging.setQueryItems("items=" + items);
			// 响应数据
			req.setAttribute("paging", paging);
			// 填充新闻数据
			Map<String, List<News>> classifyNews = newsService.prepareIndexNews();
			req.setAttribute("activityNews", classifyNews.get("activityNews"));
			req.setAttribute("bbsNews", classifyNews.get("bbsNews"));
			// 填充分类信息
			req.setAttribute("items", items);
			req.getRequestDispatcher("/WEB-INF/page/resource/ResList.jsp").forward(req, resp);
		}
	}
	
	/**
	 * <p>用户在对指定资源 ID 的资源文件发起下载请求之前，会请求此控制器方法进行合法性检查处理。</p>
	 * 
	 * @param userId - 发起下载请求的用户 ID
	 * @param resId - 用户请求下载的资源 ID
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/down/{resId:\\d+}", method = RequestMethod.POST)
	public void downloadChecked(Integer userId, @PathVariable Integer resId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据校验		
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = systemService.getLogin();
		if(userId == null || !UserUtils.isUnique(login, userId)) {
			data.setStatus(12);
		} else {
			if(resourceService.existsResById(resId)) {
				data.setStatus(500);
				data.setMsg("请求合法");
			} else {
				data.setStatus(501);
				data.setMsg("资源不存在");
			}
		}
		PrintWriter out = resp.getWriter();
		out.print(data.toJson());
		out.flush();
		out.close();
	}
	
	/**
	 * <p>用户在对指定资源 ID 的资源文件发起下载请求时，会请求此控制器方法进行处理。</p>
	 * 
	 * @param userId - 发起下载请求的用户 ID
	 * @param resId - 用户请求下载的资源 ID
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/down/{resId:\\d+}", method = RequestMethod.GET)
	public void download(Integer userId, @PathVariable Integer resId, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 验证请求信息
		User login = systemService.getLogin();
		if(userId == null || resId == null || !UserUtils.isUnique(login, userId)) {
			resp.getWriter().print("<script>window.close();</script>");
		} else {
			ApplicationDataSupport data = new ApplicationDataSupport();
			data.setParameter("login", login);
			data.setParameter("resId", resId);
			data.setParameter("resp", resp);
			// 交由 Service 处理
			resourceService.downloadRes(data);
			// 响应客户端
			if(data.isSuccess()) {
				if(logger.isDebugEnabled()) {
					logger.debug("Download resource request, and the service returned success.");
				}
			} else {
				if(logger.isDebugEnabled()) {
					logger.debug("Download resource request, but the service returned faild.");
				}
				resp.getWriter().print("<script>window.close();</script>");
			}
		}
	}
}
