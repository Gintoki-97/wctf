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

import cn.gin.wctf.common.Constants;
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
@RequestMapping(Constants.Path.CTRL_USER)
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
    @RequestMapping(value = {Constants.Path.CTRL_USER_LOGIN}, method = {RequestMethod.GET, RequestMethod.POST})
    public void login(String account, String password, Model model, HttpServletRequest req, HttpServletResponse resp) throws Exception {

        // 1. Adjust whether is a view request.
        if (UserUtils.isLoginViewRequest(account, password)) {
            // 1.1 The request is a view request.
            if (logger.isDebugEnabled()){
                logger.debug("Login request , faild with [account == null || password == null]. Forward to login page");
            }
            req.getRequestDispatcher("/WEB-INF/page/user/UserLogin.jsp").forward(req, resp);
        } else {
            // 1.2 The request is a handler request.
            // 2. Set the response header.
            resp.setCharacterEncoding(Constants.System.ENCODING);
            resp.setContentType(Constants.System.HEADER_JSON);
            if (logger.isDebugEnabled()) {
                logger.debug("Login request , active session size: {}", sessionDAO.getActiveSessions(false).size());
            }

            // 3. Data validation.
            ApplicationDataSupport data = new ApplicationDataSupport();
            Principal principal = UserUtils.getPrincipal();

            // 4. Adjust the login status.
            if (principal != null) {
                // 4.1 Already logined.
                if (logger.isDebugEnabled()) {
                    logger.debug("Login request , succeed with [account == {} || password == {}].", account, password);
                }
                data.setStatus(120);
                data.setMsg("登录成功");
            } else {
                // 4.2 Login failed.
                if (logger.isDebugEnabled()) {
                    logger.debug("Login request , faild with [account == {} || password == {}].", account, password);
                }
                data.setStatus(121);
                data.setMsg("登录失败");
            }

            // 5. Response the JSON data.
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
    @RequestMapping(value = Constants.Path.CTRL_USER_REG, method = RequestMethod.GET)
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
    @RequestMapping(value = Constants.Path.CTRL_USER_REG, method = RequestMethod.POST)
    public void reg(HttpServletRequest req, HttpServletResponse resp, String emailCaptcha,
            @Validated(UserRegisterGroup.class) User client, BindingResult userBindingResult) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Data validation.
        ApplicationDataSupport data = new ApplicationDataSupport();
        if (!UserUtils.validateCaptcha(emailCaptcha, (String) req.getSession().getAttribute(CaptchaUtils.EMAIL_CAPTCHA))) {
            // 2.1 The captcha is invalid.
            data.setStatus(154);
            data.setMsg("验证码错误");
        } else {
            // 2.2 The account or password is invalid.
            if (userBindingResult.hasErrors()) {
                if (logger.isDebugEnabled()) {
                    for (ObjectError objectError  : userBindingResult.getAllErrors()) {
                        logger.debug(objectError.getDefaultMessage());
                    }
                }
                data.setStatus(151);
                data.setMsg(Constants.Msg.ERROR_PREPARED_DATA_FAIELD);
            } else {
                // 2.3 All the data format is valid.
                // 3. Dispatch the request to service.
                data.setParameter(Constants.Attr.PARAM_CLIENT, client);
                userService.userRegister(data);
            }
        }

        // 4. Response the JSON data.
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

        // 1. Prepared data.
        User login = systemService.getLogin();

        // 2. Set the default location.
        if (Constants.Mark.ZERO.equals(login.getAddress())) {
            login.setAddress(User.DEFAULT_LOCATION);
        }

        // 3. Request location API.
        if (StringUtils.isBlank(login.getAddress()) || User.DEFAULT_LOCATION.equals(login.getAddress())) {

            // 3.1 Prepared data.
            String address = null;
            ApplicationDataSupport data = null;

            try {
                // 3.2 Send the request.
                // Get the IP address.
                String ipAddress = HttpUtils.getIpAddress(req);

                // Send the location HTTP request, use the IP as parameter.
                address = HttpUtils.sendHttpGetRequest("http://114.67.139.119/location/code?ip=" + ipAddress, null);

                // Handler the return data.
                if (!StringUtils.isEmpty(address) && address.contains("\"success\":true")) {
                    data = ApplicationDataSupport.fromJson(address);
                }
                address = data.getParameter(Constants.Attr.PARAM_LOCATION);
            } catch(Exception e) {
                address = User.DEFAULT_LOCATION;
            }

            if (StringUtils.isEmpty(address) || address.length() != 6) {
                address = User.DEFAULT_LOCATION;
            }
            model.addAttribute("address", address);
        }

        // 4. Return the user setting view.
        return Constants.Path.VIEW_USER_SETTING;
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

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Data validation.
        ApplicationDataSupport data = new ApplicationDataSupport();
        User login = systemService.getLogin();
        if (!UserUtils.isUnique(login, id)) {
            // 2.1 The operation is not allowed.
            data.setStatus(12);
        } else {
            // 2.2 The operation is valid.
            if (userBindingResult.hasErrors()) {
                List<ObjectError> allErrors = userBindingResult.getAllErrors();
                if (logger.isDebugEnabled()){
                    for (ObjectError objectError : allErrors) {
                        logger.debug(objectError.getDefaultMessage());
                    }
                }
                data.setStatus(181);
                data.setMsg(Constants.Msg.ERROR_PREPARED_DATA_FAIELD);
            } else {
                // 3. Dispatch the request to service.
                data.setParameter(Constants.Attr.PARAM_LOGIN, login);
                data.setParameter(Constants.Attr.PARAM_CLIENT, client);
                userService.updateUserSetting(data);

                // 4. Update the session attribute.
                systemService.updateLogin(login);
            }
        }

        // 5. Response the JSON data.
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
    @RequestMapping(value = Constants.Path.CTRL_USER_AVATAR, method = RequestMethod.POST)
    public void uploadAvatar(@RequestParam("file") MultipartFile avatar, Integer id,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Prepared data.
        ApplicationDataSupport data = new ApplicationDataSupport();
        User login = systemService.getLogin();

        // 3. Data validation.
        if (!UserUtils.isUnique(login, id)) {
            // 3.1 The operation is not allowed.
            data.setStatus(12);
        } else {
            // 3.2 The operation is valid.
            // 4. Dispatch the request to service.
            data.setParameter(Constants.Attr.PARAM_LOGIN, login);
            data.setParameter(Constants.Attr.PARAM_AVATAR, avatar);
            userService.uploadUserAvatar(data);

            // 5. Update the session attribute.
            systemService.updateLogin(login);
        }

        // 6. Response the JSON data.
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

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Prepared data.
        ApplicationDataSupport data = new ApplicationDataSupport();
        User login = systemService.getLogin();

        // 3. Data validation.
        if (!UserUtils.isUnique(login, id)) {
            // 3.1 The operation is not allowed.
            data.setStatus(12);
        } else {
            // 3.2 The operation is valid.
            // 4. Dispatch the request to service.
            data.setParameter(Constants.Attr.PARAM_LOGIN, login);
            userService.userPunch(data);

            // 5. Update the session attribute.
            systemService.updateLogin(login);
        }

        // 6. Response the JSON data.
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

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Prepared data.
        ApplicationDataSupport data = new ApplicationDataSupport();
        User login = systemService.getLogin();

        // 3. Data validation.
        if (!UserUtils.isUnique(login, id)) {
            // 3.1 The operation is not allowed.
            data.setStatus(12);
        } else {
            data.setParameter(Constants.Attr.PARAM_LOGIN, login);
            data.setParameter(Constants.Attr.PARAM_NOWPASS, nowpass);
            data.setParameter(Constants.Attr.PARAM_PASS, pass);
            // 3.2 The operation is valid.
            // 4. Dispatch the request to service.
            userService.resetPassword(data);

            // 5. Update the session attribute.
            systemService.updateLogin(login);
        }

        // 6. Response the JSON data.
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
     * @throws IOException 在请求非法时给客户端错误响应码，可能会抛出该异常
     */
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.GET)
    public String trendViewRequest(@PathVariable Integer id,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = null;
        User login = systemService.getLogin();
        if (!UserUtils.isUnique(login, id)) {
            user = userService.getUserByIdOptional(id);
            if (user == null) {
                resp.sendError(404);
                return Constants.Path.VIEW_404;
            } else {
                req.setAttribute(Constants.Attr.REQ_OTHER, true);
            }
        } else {
            user = login;
            req.setAttribute(Constants.Attr.REQ_OTHER, false);
        }
        String trendSetting = userService.getUserTrendSetting(id);
        req.setAttribute(Constants.Attr.PARAM_TREND_SETTING, trendSetting);
        req.setAttribute(Constants.Attr.PARAM_TREND_SETTING_CHARS, trendSetting.toCharArray());
        req.setAttribute(Constants.Attr.PARAM_FOUND, user);
        return Constants.Path.VIEW_USER_TREND;
    }

    /**
     * <p>用户主页的数据请求，程序会根据页面传入的用户 ID 与与分页数据，返回指定数量的用户动态信息，以支持页面的流加载。</p>
     * <p>StatusCode: 280 ~ 289</p>
     *
     * @param id - 由页面传入的用户  ID，用于实现 RESTful 风格的 URL，同时也是为了校验用户请求是否合法
     * @param page - 由页面传入的当前页信息，用于实现页面懒加载
     * @param req - {@link HttpServletRequest}
     * @param resp - {@link HttpServletResponse}
     * @throws IOException 在请求非法时给客户端错误响应码，可能会抛出该异常
     */
    @RequestMapping(value = "/{id:\\d+}", method = RequestMethod.POST)
    public void trend(@PathVariable Integer id, Integer page,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Prepared data.
        ApplicationDataSupport data = new ApplicationDataSupport();
        data.setParameter(Constants.Attr.PARAM_USER_ID, id);
        data.setParameter(Constants.Attr.PARAM_INDEX, page == null ? 1 : page);

        // 3. Dispatch the request to service.
        trendService.listTrendByUserId(data);
        if (!data.isSuccess()) {
            data.setParameter(Constants.Attr.PARAM_PAGES, 0);
        }

        // 4. Response the JSON data.
        PrintWriter out = resp.getWriter();
        out.write(data.toJson());
        out.flush();
        out.close();
    }

    /**
     * <p>用户主页更新动态设置的请求，程序会根据用户的动态设置自动为用户推送不同的动态或屏蔽某些动态。</p>
     * <p>StatusCode: 290 ~ 299</p>
     *
     * @param id - 由页面传入的用户  ID，用于实现 RESTful 风格的 URL，同时也是为了校验用户请求是否合法
     * @param trendSetting - 由页面传入的用户设置信息，一般为 0/1 字符串
     * @param req - {@link HttpServletRequest}
     * @param resp - {@link HttpServletResponse}
     * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
     */
    @RequestMapping(value = Constants.Path.CTRL_USER_TREND, method = RequestMethod.POST)
    public void trendSetting(Integer userId, String trendSetting,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Prepared data.
        ApplicationDataSupport data = new ApplicationDataSupport();
        User login = systemService.getLogin();

        // 3. Data validation.
        if (!UserUtils.isUnique(login, userId)) {
            // 3.1 The operation is not allowed.
            data.setStatus(12);
        } else {
            // 3.2 The operation is allowed.
            // 4. Dispatch the request to service.
            data.setParameter(Constants.Attr.PARAM_USER_ID, userId);
            data.setParameter(Constants.Attr.PARAM_TREND_SETTING, trendSetting);
            userService.trendSetting(data);
        }

        // 4. Response the JSON data.
        PrintWriter out = resp.getWriter();
        out.write(data.toJson());
        out.flush();
        out.close();
    }
}