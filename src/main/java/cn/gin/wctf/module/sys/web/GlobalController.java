package cn.gin.wctf.module.sys.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.gin.wctf.common.Constants;
import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.paging.Page;
import cn.gin.wctf.common.util.CaptchaUtils;
import cn.gin.wctf.common.util.EmailUtils;
import cn.gin.wctf.common.util.FileUtils;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.common.validator.user.UserRegisterGroup;
import cn.gin.wctf.module.news.entity.News;
import cn.gin.wctf.module.news.service.NewsService;
import cn.gin.wctf.module.post.entity.Post;
import cn.gin.wctf.module.post.service.PostService;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.service.UserService;

/**
 * <p>全局控制器</p>
 * <p>此控制器提供的所有方法接口均不属于任一模块。比如访问首页请求，邮件相关请求等，它们在语义上不属于
 * 任何一个模块，而是属于公共的方法接口。这些方法都由此控制器统一管理。</p>
 *
 * @author Gintoki
 * @version 2017-10-12
 */
@Controller
@RequestMapping
public class GlobalController {

    /**
     * The default root logger.
     */
    private Logger logger = LoggerFactory.getLogger(GlobalController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private EmailUtils emailUtils;


    // ---------------------
    // Service
    // ---------------------

    /**
     * <p>用户请求首页时，会请求此控制器方法接口进行处理。</p>
     *
     * @param stamp - 用于标识是否需要初始化首页数据
     * @param model - 模型数据
     * @param req - {@link HttpServletRequest}
     * @param resp - {@link HttpServletResponse}
     * @return 视图
     * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
     */
    @RequestMapping(value = {Constants.Mark.EMPTY, Constants.Mark.SLASH, Constants.Path.VIEW_INDEX})
    public String index(Model model, String stamp, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if(StringUtils.isEmpty(stamp)) {
            // 初始化发贴模块数据
            Map<String, List<Post>> classifyPosts = postService.prepareIndexPost(null);
            model.addAttribute("tops", classifyPosts.get("tops"));
            model.addAttribute("posts", classifyPosts.get("posts"));
            // 初始化新闻模块数据
            Map<String, List<News>> classifyNews = newsService.prepareIndexNews();
            model.addAttribute("bannerNews", classifyNews.get("bannerNews"));
            model.addAttribute("activityNews", classifyNews.get("activityNews"));
            model.addAttribute("bbsNews", classifyNews.get("bbsNews"));
            return Constants.Path.VIEW_INDEX;
        } else {
            resp.sendRedirect("/wctf/index");
            return null;
        }
    }

    /**
     * <p>用户请求首页并且带有分页要求时，会请求此控制器方法接口进行处理。</p>
     *
     * @param model - 模型数据
     * @param index - 分页参数，标识当前页
     * @return 视图
     */
    @RequestMapping(value = {"/index/page/{index}"}, method = RequestMethod.GET)
    public String indexWithPaging(Model model, @PathVariable int index) {
        // 初始化分页模块数据
        Page<Post> paging = new Page<Post> ();
        if(index > 1) {
            paging.setIndex(index);
        }
        // 初始化发贴模块数据
        Map<String, List<Post>> classifyPosts = postService.prepareIndexPost(paging);
        model.addAttribute("tops", classifyPosts.get("tops"));
        model.addAttribute("posts", classifyPosts.get("posts"));
        model.addAttribute("paging", paging);
        // 初始化新闻模块数据
        Map<String, List<News>> classifyNews = newsService.prepareIndexNews();
        model.addAttribute("bannerNews", classifyNews.get("bannerNews"));
        model.addAttribute("activityNews", classifyNews.get("activityNews"));
        model.addAttribute("bbsNews", classifyNews.get("bbsNews"));
        return Constants.Path.VIEW_INDEX;
    }

    /**
     * <p>邮件发送请求，用户在注册页面注册新的账号或更改邮箱时，点击发送邮件按钮，会请求此控制器方法接口进行处理。</p>
     * <p>StatusCode: 30 ~ 39</p>
     *
     * @param account - 由页面传入的邮箱账号
     * @param req - {@link HttpServletRequest}
     * @param resp - {@link HttpServletResponse}
     * @throws IOException 有关 Socket IO 的错误情况可能会造成此异常抛出
     */
    @RequestMapping(value = {"/common/email"}, method = RequestMethod.POST)
    public void emailValidate(HttpServletRequest req, HttpServletResponse resp,
            String account) throws IOException {
        // 设置响应头信息
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=utf-8");
        // 数据校验
        ApplicationDataSupport data = new ApplicationDataSupport();
        if(account == null || !account.matches(UserRegisterGroup.ACCOUNT)) {
            data.setStatus(31);
            data.setMsg("数据格式异常");
        } else {
            if(userService.existsByAccount(account)) {
                data.setStatus(33);
                data.setMsg("邮箱账号已存在");
            } else {
                // 生成邮箱验证码
                String captcha = CaptchaUtils.buildNumbers(4);
                String templetUri = Global.getConfig("web.root") + "WEB-INF\\page\\templet\\Email.htm";
                String content = FileUtils.getContent(templetUri);
                content = content.replace("&#0000;", Global.getConfig("web.root.uri"));
                content = content.replace("&#0010;", Global.getConfig("productName"));
                content = content.replace("&#0100;", captcha);
                try {
                    emailUtils.addTo(account);
                    emailUtils.sendHtmlEmail("邮箱注册验证", content);
                    req.getSession().setAttribute(CaptchaUtils.EMAIL_CAPTCHA, captcha);
                    data.setStatus(30);
                    data.setMsg("邮件发送成功");
                } catch (EmailException e) {
                    data.setStatus(32);
                    data.setMsg("邮件发送程序执行异常");
                    if(logger.isErrorEnabled()) {
                        logger.error(e.getMessage());
                    }
                }
            }
        }
        PrintWriter out = resp.getWriter();
        out.print(data.toJson());
        out.flush();
        out.close();
    }
}