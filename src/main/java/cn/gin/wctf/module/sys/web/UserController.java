package cn.gin.wctf.module.sys.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.gin.wctf.common.security.shiro.session.SessionDAO;
import cn.gin.wctf.common.util.CaptchaUtils;
import cn.gin.wctf.common.util.HttpUtils;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.common.validator.user.UserRegisterGroup;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.security.SystemAuthorizingRealm.Principal;
import cn.gin.wctf.module.sys.service.SystemService;
import cn.gin.wctf.module.sys.service.TrendService;
import cn.gin.wctf.module.sys.service.UserService;
import cn.gin.wctf.module.sys.util.UserUtils;

/**
 * <p>用户控制器</p>
 * <p>此控制器提供的所有方法接口均属于用户模块。所有有关用户操作的请求比如用户注册，修改用户信息，更改密码等，它们在语义上
 * 就属于用户模块。这些方法都由此控制器统一管理。</p>
 * 
 * @author Gintoki
 * @version 2017-10-12
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	/**
	 * The default logger.
	 */
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TrendService trendService;
	
	@Autowired
	private SystemService systemService;

	
	// ---------------------
	// Service
	// ---------------------
	
	/**
	 * <p>用户请求登录时，用户请求登录页面或者在登录页面提交登录表单时，会请求此控制器方法处理。</p>
	 * <p>授权与认证机制已交由 Shiro 控制，所以用户在需要认证的时候（即未处于登录状态，或者没有 RememberMe Cookie），
	 * 会将认证交由 Shiro 执行。如果用户已处于登录状态，Shiro 则不经过认证直接放行，当前方法接口在此种情况下才会接收到请求。
	 * 所以此接口并不会包含详细的认证逻辑。</p>
	 * <p>StatusCode: 120 ~ 149</p>
	 * 
	 * @param account - 由页面传入的用户账号
	 * @param password - 由页面传入的用户密码
	 * @param model - 模型数据
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws Exception 有关 Servlet/Servlet IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
	public void login(String account, String password, Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// 请求登录视图
		if(UserUtils.isLoginViewRequest(account, password)) {
			if (logger.isDebugEnabled()){
				logger.debug("Login request , faild with [account == null || password == null]. Forward to login page");
			}
			req.getRequestDispatcher("/WEB-INF/page/user/UserLogin.jsp").forward(req, resp);
		} else {
			// 响应头信息
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("application/json;charset=utf-8");
			if (logger.isDebugEnabled()) {
				logger.debug("Login request , active session size: {}", sessionDAO.getActiveSessions(false).size());
			}
			// 数据校验
			ApplicationDataSupport data = new ApplicationDataSupport();
			Principal principal = UserUtils.getPrincipal();
			// 如果已经登录，则跳转到管理首页
			if(principal != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Login request , succeed with [account == {} || password == {}].", account, password);
				}
				data.setStatus(120);
				data.setMsg("登录成功");
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Login request , faild with [account == {} || password == {}].", account, password);
				}
				data.setStatus(121);
				data.setMsg("登录失败");
			}
			PrintWriter out = resp.getWriter();
			out.write(data.toJson());
			out.flush();
            out.close();
		}
	}
	
	/**
	 * <p>用户请求注册视图，在地址栏里（GET）访问当前 URL 时，会请求此控制器方法接口进行处理。</p>
	 * 
	 * @return 视图
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.GET)
	public String regViewRequest() {
		return "/user/UserRegister";
	}
	
	/**
	 * <p>用户请求注册，用户在注册页面进行注册表单提交时，会请求此控制器方法接口进行处理。</p>
	 * <p>StatusCode: 150 ~ 179</p>
	 * 
	 * @param client - 由页面传入的新注册用户信息
	 * @param emailCaptcha - 由页面传入的邮箱验证码
	 * @param req - {@link HttpServletRequest}
	 * @param resp - HttpServletResponse
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/reg", method = RequestMethod.POST)
	public void reg(HttpServletRequest req, HttpServletResponse resp, String emailCaptcha, 
			@Validated(UserRegisterGroup.class) User client, BindingResult userBindingResult) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据校验
		ApplicationDataSupport data = new ApplicationDataSupport();
		if(!UserUtils.validateCaptcha(emailCaptcha, (String) req.getSession().getAttribute(CaptchaUtils.EMAIL_CAPTCHA))) {
			data.setStatus(154);
			data.setMsg("验证码错误");
		} else {
			if(userBindingResult.hasErrors()) {
				if(logger.isDebugEnabled()) {
					for (ObjectError objectError  : userBindingResult.getAllErrors()) {
						logger.debug(objectError.getDefaultMessage());
					}
				}
				data.setStatus(151);
				data.setMsg("数据准备失败");
			} else {
				data.setParameter("client", client);
				// 交由 service 层处理
				userService.userRegister(data);
			}
		}
		PrintWriter out = resp.getWriter();
		out.print(data.toJson());
		out.flush();
		out.close();
	}
	
	/**
	 * <p>用户信息设置视图请求，用户在地址栏里（GET）访问当前 URL 时，会请求此控制器方法处理。</p>
	 * 
	 * @param id - 由页面传入的用户  ID，用于实现 RESTful 风格的 URL
	 * @param model - 模型数据
	 * @param req - {@link HttpServletRequest}
	 * @return 视图
	 */
	@RequestMapping(value = "/setting/{id}", method = RequestMethod.GET)
	public String settingViewRequest(@PathVariable int id, Model model, HttpServletRequest req) {
		// 数据准备
		User login = systemService.getLogin();
		if("0".equals(login.getAddress())) {
			login.setAddress(User.DEFAULT_LOCATION);
		}
		if(StringUtils.isBlank(login.getAddress()) || User.DEFAULT_LOCATION.equals(login.getAddress())) {
			// 定位
			String address = null;
			ApplicationDataSupport data = null;
			try {
				String ipAddress = HttpUtils.getIpAddress(req);
				address = HttpUtils.sendHttpGetRequest("http://114.67.139.119/location/code?ip=" + ipAddress, null);
				if(!StringUtils.isEmpty(address) && address.contains("\"success\":true")) {
					data = ApplicationDataSupport.fromJson(address);
				}
				address = data.getParameter("location");
			} catch(Exception e) {
				address = User.DEFAULT_LOCATION;
			}
			if(StringUtils.isEmpty(address) || address.length() != 6) {
				address = User.DEFAULT_LOCATION;
			}
			model.addAttribute("address", address);
		}
		// 返回用户信息更新视图
		return "/user/UserSetting";
	}
	
	/**
	 * <p>用户信息设置请求，用户在信息设置页面或管理员在信息管理页面进行信息修改提交时，会请求此控制器方法接口进行处理。</p>
	 * <p>StatusCode: 180 ~ 199</p>
	 * 
	 * @param client - 由页面传入的新的用户信息封装
	 * @param userBindingResult - 用户参数校验结果集
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/setting/{id}", method = RequestMethod.POST)
	public void setting(@PathVariable Integer id, HttpServletRequest req, HttpServletResponse resp,
			@Validated() User client, BindingResult userBindingResult) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据校验
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = systemService.getLogin();
		if(!UserUtils.isUnique(login, id)) {
			data.setStatus(12);
		} else {
			if(userBindingResult.hasErrors()) {
				List<ObjectError> allErrors = userBindingResult.getAllErrors();
				if (logger.isDebugEnabled()){
					for (ObjectError objectError : allErrors) {
						logger.debug(objectError.getDefaultMessage());
					}
				}
				data.setStatus(181);
				data.setMsg("数据准备失败");
			} else {
		    	data.setParameter("login", login);
		    	data.setParameter("client", client);
				// 交由 Service 层处理
				userService.updateUserSetting(data);
				// 更新 Session
				systemService.updateLogin(login);
			}
		}
		// 响应数据
		PrintWriter out = resp.getWriter();
		out.write(data.toJson());
		out.flush();
		out.close();
	}
	
	/**
	 * <p>用户头像上传，用户在信息设置页面或管理员在信息管理页面进行头像修改时，会请求此控制器方法处理。</p>
	 * <p>StatusCode: 260 ~ 279</p>
	 * 
	 * @param avatar - 由页面传入的头像图片文件
	 * @param id - 由页面传入的用户  ID，用于校验登录状态
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/avatar", method = RequestMethod.POST)
	public void uploadAvatar(@RequestParam("file") MultipartFile avatar, Integer id, 
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
    	resp.setContentType("application/json;charset=utf-8");
    	// 数据准备
    	ApplicationDataSupport data = new ApplicationDataSupport();
    	User login = systemService.getLogin();
    	if(!UserUtils.isUnique(login, id)) {
			data.setStatus(12);
		} else {
			data.setParameter("login", login);
			data.setParameter("avatar", avatar);
			// 交由 Service 层处理
			userService.uploadUserAvatar(data);
			// 更新 Session
			systemService.updateLogin(login);
		}
    	// 响应数据
		PrintWriter out = resp.getWriter();
		out.write(data.toJson());
		out.flush();
        out.close();
	}
	
	/**
	 * <p>用户每日签到，用户在首页点击签到按钮时，会请求此控制器方法处理。</p>
	 * <p>StatusCode: 240 ~ 259</p>
	 * 
	 * @param id - 由页面传入的用户  ID，用于实现 RESTful 风格的 URL
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/punch/{id}", method = RequestMethod.POST)
	public void punch(@PathVariable Integer id, HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据准备
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = systemService.getLogin();
		if(!UserUtils.isUnique(login, id)) {
			data.setStatus(12);
		} else {
			data.setParameter("login", login);
			// 交由 service 层处理
			userService.userPunch(data);
			// 更新 Session
			systemService.updateLogin(login);
		}
        // 响应数据
		PrintWriter out = resp.getWriter();
		out.write(data.toJson());
		out.flush();
        out.close();
	}
	
	/**
	 * <p>重置用户密码，用户在信息设置页面或管理员在信息管理页面进行密码信息的修改时，会请求此控制器方法处理。</p>
	 * <p>StatusCode: 220 ~ 239</p>
	 * 
	 * @param id - 由页面传入的用户  ID，用于实现 RESTful 风格的 URL
	 * @param nowpass - 当前 session 中登录的用户的原密码
	 * @param pass - 新密码
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
	 */
	@RequestMapping(value = "/reset/{id}", method = RequestMethod.POST)
	public void resetPassword(@PathVariable Integer id, String nowpass, String pass, 
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据准备
		ApplicationDataSupport data = new ApplicationDataSupport();
		User login = systemService.getLogin();
		if(!UserUtils.isUnique(login, id)) {
			data.setStatus(12);
		} else {
			data.setParameter("login", login);
			data.setParameter("nowpass", nowpass);
			data.setParameter("pass", pass);
			// 交由 service 层处理
			userService.resetPassword(data);
			// 更新 Session
			systemService.updateLogin(login);
		}
        // 响应数据
        PrintWriter out = resp.getWriter();
		out.write(data.toJson());
		out.flush();
        out.close();
	}
	
	
	/**
	 * <p>用户主页的视图请求，程序会判断页面传入的用户 ID 与当前登录用户是否匹配，如果不匹配则返回 404 响应码。
	 * 其实真正的原因是用户无权访问，不过业务逻辑没有为前台页面提供太多的错误细节。</p>
	 * <p>StatusCode: 280 ~ 289</p>
	 * 
	 * @param id - 由页面传入的用户  ID，用于实现 RESTful 风格的 URL，同时也是为了校验用户请求是否合法
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException - 在请求非法时给客户端错误响应码，可能会抛出该异常
	 */
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
	public String trendViewRequest(@PathVariable Integer id,
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User user = null;
		User login = systemService.getLogin();
		if(!UserUtils.isUnique(login, id)) {
			user = userService.getUserByIdOptional(id);
			if(user == null) {
				resp.sendError(404);
				return "/error/404";
			} else {
				req.setAttribute("other", true);
			}
		} else {
			user = login;
			req.setAttribute("other", false);
		}
		req.setAttribute("found", user);
		return "/user/UserPage";
	}
	
	/**
	 * <p>用户主页的数据请求，程序会根据页面传入的用户 ID 与与分页数据，返回指定数量的用户动态信息，以支持页面的流加载。</p>
	 * <p>StatusCode: 280 ~ 289</p>
	 * 
	 * @param id - 由页面传入的用户  ID，用于实现 RESTful 风格的 URL，同时也是为了校验用户请求是否合法
	 * @param page - 由页面传入的当前页信息，用于实现页面懒加载
	 * @param req - {@link HttpServletRequest}
	 * @param resp - {@link HttpServletResponse}
	 * @throws IOException - 在请求非法时给客户端错误响应码，可能会抛出该异常
	 */
	@RequestMapping(value = "/{id:\\d+}", method = RequestMethod.POST)
	public void trend(@PathVariable Integer id, Integer page, 
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// 响应头信息
		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json;charset=utf-8");
		// 数据准备
		ApplicationDataSupport data = new ApplicationDataSupport();
		data.setParameter("userId", id);
		data.setParameter("index", page == null ? 1 : page);
		trendService.listTrendByUserId(data);
		if(!data.isSuccess()) {
			data.setParameter("pages", 0);
		}
		// 响应数据
        PrintWriter out = resp.getWriter();
		out.write(data.toJson());
		out.flush();
        out.close();
	}
	
	
}
