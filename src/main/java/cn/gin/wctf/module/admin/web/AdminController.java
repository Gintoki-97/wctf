package cn.gin.wctf.module.admin.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.gin.wctf.common.Constants;
import cn.gin.wctf.common.util.JCodec;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.module.admin.service.AdminService;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.Role;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.service.SystemService;
import cn.gin.wctf.module.sys.service.UserService;

@Controller
@RequestMapping(Constants.Path.CTRL_ADMIN)
public class AdminController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = {Constants.Mark.EMPTY, Constants.Mark.SLASH, Constants.Path.CTRL_ADMIN_INDEX, Constants.Path.CTRL_ADMIN_DASHBOARD}, method = RequestMethod.GET)
    public String admin(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        // 1. Adjust whether the administrator is logged in.
        if (systemService.isAdminLogin()) {
            return Constants.Path.VIEW_ADMIN_INDEX;
        }

        // 2. Redirect to login page.
        resp.sendRedirect("/wctf/admin/login");
        return null;
    }

    @RequestMapping(value = Constants.Path.CTRL_ADMIN_LOGIN, method = RequestMethod.GET)
    public String loginViewRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        // 1. Adjust whether the administrator is logged in.
        if (systemService.isAdminLogin()) {
            // 1.1 Redirect to index page.
            resp.sendRedirect(Constants.Path.VIEW_WCTF_ADMIN);
            return null;
        }

        // 2. Forward to login page.
        return Constants.Path.VIEW_ADMIN_LOGIN;
    }

    @RequestMapping(value = Constants.Path.CTRL_ADMIN_LOGIN, method = RequestMethod.POST)
    public void login(HttpServletRequest req, HttpServletResponse resp, String account, String password) throws IOException {

        // 1. Adjust whether the administrator is logged in.
        if (systemService.isAdminLogin()) {
            // 1.1 Redirect to index page.
            resp.sendRedirect(Constants.Path.VIEW_WCTF_ADMIN);
        }

        // 2. Dispatch the request to service.
        boolean valid = false;
        User user = userService.getUserByAccount(account);
        if (user != null) {
            // 2.1 The user account is exists.
            String clientPwd = JCodec.md5SaltEncode(JCodec.md5Encode(password), user.getSalt());
            if (!StringUtils.isBlank(user.getPassword()) && (user.getPassword().equals(clientPwd))) {
                valid = true;
            }
        }

        // 3. Response.
        if (valid) {
            // 3.1 The login is valid.
            boolean roleValidation = false;
            user.setRoles(new HashSet<Role>(userService.getRolesByUserId(user.getId())));
            for (Role role : user.getRoles()) {
                if (Constants.System.ROLE_ADMIN.equals(role.getName())) {
                    roleValidation = true;
                    break;
                }
            }

            if (roleValidation) {
                // The role of current user is allowed.
                req.getSession().setAttribute(Constants.Attr.SESSION_USER, user);
                resp.sendRedirect(Constants.Path.VIEW_WCTF_ADMIN);
            } else {
                // The role of current user is not allowed.
                req.getSession().setAttribute("msg", "The account or password is incorrect");
                resp.sendRedirect(Constants.Path.VIEW_WCTF_ADMIN_LOGIN);
            }
        } else {
            // 3.2 The login is invalid.
            req.getSession().setAttribute("msg", "The account or password is incorrect");
            resp.sendRedirect(Constants.Path.VIEW_WCTF_ADMIN_LOGIN);
        }
    }

    @RequestMapping(value = Constants.Path.CTRL_ADMIN_USER, method = RequestMethod.GET)
    public String user(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Checked permission.
        if (!systemService.isAdminLogin()) {
            resp.sendError(404);
            return null;
        }

        return Constants.Path.VIEW_ADMIN_USER_LIST;
    }

    @RequestMapping(value = Constants.Path.CTRL_ADMIN_USER_LIST, method = RequestMethod.GET)
    public void userList(Integer limit, Integer page,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Set the pagination parameters.
        ApplicationDataSupport data = new ApplicationDataSupport();
        if (!systemService.isAdminLogin()) {
            // 2.1 Redirect to index page.
            data.setStatus(611);
            data.setMsg("The permission system denies access");
        } else {
            // 2.2 Prepared the data.
            data.setParameter("page", page);
            data.setParameter("limit", limit);

            // 3. Dispatch the request to service.
            userService.list(data);
            data.setStatus(600);
            data.setMsg("Get the user list success");
        }

        // 4. Response the JSON data.
        PrintWriter out = resp.getWriter();
        out.write(data.toJson());
        out.flush();
        out.close();
    }

    @RequestMapping(value = {"/user/{id:\\d+}"}, method = RequestMethod.GET)
    public String userEdit(@PathVariable Integer id,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Checked permission.
        if (!systemService.isAdminLogin()) {
            resp.sendError(404);
            return null;
        }

        // 2. Set the response header.
        User user = userService.getUserById(id);

        // 3. Set the pagination parameters.
        if (user != null) {
            req.setAttribute("edit", user);
            return Constants.Path.VIEW_ADMIN_USER_EDIT;
        }

        return Constants.Path.VIEW_ADMIN_USER_LIST;
    }

    @RequestMapping(value = Constants.Path.CTRL_ADMIN_USER_DEL, method = RequestMethod.GET)
    public String userDeleteViewRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Checked permission.
        if (!systemService.isAdminLogin()) {
            resp.sendError(404);
            return null;
        }

        return Constants.Path.VIEW_ADMIN_USER_DELETE;
    }

    /**
     * <p>删除用户，将用户的登录状态置为删除状态，支持多删除。</p>
     * <p>StatusCode: 610 ~ 619</p>
     *
     * @param id - 需要删除的用户 ID 列表字符串，多个 ID 用 ',' 分隔。
     */
    @RequestMapping(value = Constants.Path.CTRL_ADMIN_USER_DELETE, method = RequestMethod.POST)
    public void userDelete(String id,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Adjust whether the administrator is logged in.
        ApplicationDataSupport data = new ApplicationDataSupport();
        if (!systemService.isAdminLogin()) {
            // 2.1 Redirect to index page.
            data.setStatus(611);
            data.setMsg("The permission system denies access");
        } else {
            // 3. Validate data.
            String[] ids = id.split(",");
            if (ids == null || ids.length == 0) {
                data.setStatus(612);
                data.setMsg("The ID parameter is empty");
            } else {
                // 4. Dispatch the request to service.
                data.setParameter("ids", ids);
                userService.delete(data);
            }
        }

        // 5. Response the JSON data.
        PrintWriter out = resp.getWriter();
        out.write(data.toJson());
        out.flush();
        out.close();
    }

    /**
     * <p>查看所有已删除的用户。</p>
     * <p>StatusCode: 620 ~ 629</p>
     */
    @RequestMapping(value = Constants.Path.CTRL_ADMIN_USER_DELETE, method = RequestMethod.GET)
    public void userDelete(Integer limit, Integer page,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Set the pagination parameters.
        ApplicationDataSupport data = new ApplicationDataSupport();
        if (!systemService.isAdminLogin()) {
            // 2.1 Redirect to index page.
            data.setStatus(621);
            data.setMsg("The permission system denies access");
        } else {
            // 2.2 Prepared the data.
            data.setParameter("page", page);
            data.setParameter("limit", limit);

            // 3. Dispatch the request to service.
            userService.listDelete(data);
            data.setStatus(620);
            data.setMsg("Get the deleted user list success");
        }

        // 4. Response the JSON data.
        PrintWriter out = resp.getWriter();
        out.write(data.toJson());
        out.flush();
        out.close();
    }

    /**
     * <p>管理员修改用户的信息。</p>
     * <p>StatusCode: 630 ~ 639</p>
     */
    @RequestMapping(value = Constants.Path.CTRL_ADMIN_USER_UPDATE, method = RequestMethod.POST)
    public void userUpdate(User user,
            HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Adjust whether the administrator is logged in.
        ApplicationDataSupport data = new ApplicationDataSupport();
        if (!systemService.isAdminLogin()) {
            // 2.1 Redirect to index page.
            data.setStatus(631);
            data.setMsg("The permission system denies access");
        } else {
            // 3. Prepared data.
            data.setParameter("updated", user);

            // 4. Dispatch the request to service.
            adminService.updateUser(data);
        }

        // 5. Response the JSON data.
        PrintWriter out = resp.getWriter();
        out.write(data.toJson());
        out.flush();
        out.close();
    }

    /**
     * <p>管理员修改用户的账号状态。</p>
     * <p>StatusCode: 640 ~ 649</p>
     */
    @RequestMapping(value = Constants.Path.CTRL_ADMIN_USER_BAN, method = RequestMethod.POST)
    public void userBan(HttpServletRequest req, HttpServletResponse resp,
            User user) throws IOException {

        // 1. Set the response header.
        resp.setCharacterEncoding(Constants.System.ENCODING);
        resp.setContentType(Constants.System.HEADER_JSON);

        // 2. Adjust whether the administrator is logged in.
        ApplicationDataSupport data = new ApplicationDataSupport();
        if (!systemService.isAdminLogin()) {
            // 2.1 Redirect to index page.
            data.setStatus(641);
            data.setMsg("The permission system denies access");
        } else {
            // 3. Prepared data.
            data.setParameter("updated", user);

            // 4. Dispatch the request to service.
            adminService.banUser(data);
        }

        // 5. Response the JSON data.
        PrintWriter out = resp.getWriter();
        out.write(data.toJson());
        out.flush();
        out.close();
    }
}