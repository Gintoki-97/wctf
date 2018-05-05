package cn.gin.wctf.module.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gin.wctf.common.Constants;
import cn.gin.wctf.module.sys.dao.UserDao;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.User;

@Service
public class AdminService {

    @Autowired
    private UserDao userDao;

    /**
     * <p>管理员修改用户的信息。</p>
     * <p>StatusCode: 630 ~ 639</p>
     *
     * @param data - 应用数据流载体
     * @param data$User$updated - 被更新的用户
     */
    public void updateUser(ApplicationDataSupport data) {

        User user = null;
        try {
            user = data.getParameter("updated");

            if (user.getId() == null) {
                throw new IllegalArgumentException("The parameter of updated user object is invalid");
            }
        } catch(Throwable throwable) {
            data.setStatus(632);
            data.setMsg("The parameter is invalid");
            return;
        }

        if (userDao.updateUserSetting(user) == 1) {
            data.setStatus(630);
            data.setMsg("修改成功");
        } else {
            data.setStatus(633);
            data.setMsg("修改失败，请稍后再试");
        }
    }

    /**
     * <p>管理员修改用户的账号状态。</p>
     * <p>StatusCode: 640 ~ 649</p>
     *
     * @param data - 应用数据流载体
     * @param data$User$updated - 被更新的用户
     */
    public void banUser(ApplicationDataSupport data) {

        User user = null;
        try {
            user = data.getParameter("updated");

            if (user.getId() == null) {
                throw new IllegalArgumentException("The parameter of updated user object is invalid");
            }
        } catch(Throwable throwable) {
            data.setStatus(642);
            data.setMsg("The parameter is invalid");
            return;
        }

        String loginFlag = user.getLoginFlagVo();
        if ("2".equals(loginFlag)) {
            loginFlag = loginFlag + Constants.Mark.COLON + user.getLoginFlagStart() + Constants.Mark.COLON + user.getLoginFlagEnd() + Constants.Mark.COLON + user.getLoginFlagMsg();
        } else if ("3".equals(loginFlag)) {
            loginFlag = loginFlag + Constants.Mark.COLON + user.getLoginFlagMsg();
        }
        user.setLoginFalg(loginFlag);

        if (userDao.updateUserSetting(user) == 1) {
            data.setStatus(640);
            data.setMsg("修改成功");
        } else {
            data.setStatus(643);
            data.setMsg("修改失败，请稍后再试");
        }
    }
}