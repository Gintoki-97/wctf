package cn.gin.wctf.common;

public final class Constants {

    public static final class System {

        public static final String ENCODING = "UTF-8";
        public static final String HEADER_JSON = "application/json;charset=utf-8";
        public static final String ROLE_ADMIN = "admin";
        public static final String ROLE_USER = "user";
        public static final String ROLE_GUEST = "guest";
    }

    public static final class Mark {

        public static final String EMPTY = "";
        public static final String SLASH = "/";
        public static final String BACKSLASH = "\\";
        public static final String DOT = ".";
        public static final String SPACING = " ";
        public static final String ZERO = "0";
        public static final String COLON = ":";
    }

    public static class Path {

        public static final String CTRL_USER = "/user";
        public static final String CTRL_USER_LOGIN = "/login";
        public static final String CTRL_USER_REG = "/reg";
        public static final String CTRL_USER_AVATAR = "/avatar";
        public static final String CTRL_USER_TREND = "/trend";

        public static final String CTRL_ADMIN = "/admin";
        public static final String CTRL_ADMIN_INDEX = "/index";
        public static final String CTRL_ADMIN_DASHBOARD = "/dashboard";
        public static final String CTRL_ADMIN_LOGIN = "/login";
        public static final String CTRL_ADMIN_USER = "/user";
        public static final String CTRL_ADMIN_USER_LIST = "/user/list";
        public static final String CTRL_ADMIN_USER_DEL = "/user/del";
        public static final String CTRL_ADMIN_USER_DELETE = "/user/delete";
        public static final String CTRL_ADMIN_USER_UPDATE = "/user/update";
        public static final String CTRL_ADMIN_USER_BAN = "/user/ban";

        public static final String VIEW_INDEX = "/index";
        public static final String VIEW_WCTF_INDEX = "/wctf/";
        public static final String VIEW_WCTF_ADMIN = "/wctf/admin";
        public static final String VIEW_WCTF_ADMIN_LOGIN = "/wctf/admin/login";
        public static final String VIEW_USER_LOGIN = "/user/UserLogin";
        public static final String VIEW_USER_REG = "/user/UserRegister";
        public static final String VIEW_USER_SETTING = "/user/UserSetting";
        public static final String VIEW_USER_TREND = "/user/UserTrend";
        public static final String VIEW_POST_CLASSIFY = "/post/PostTrend";
        public static final String VIEW_POST_DETAIL = "/post/PostDetail";
        public static final String VIEW_POST_SEND = "/post/PostSend";
        public static final String VIEW_RES_CLASSIFY = "/resource/ResClassify";
        public static final String VIEW_RES_DETAIL = "/resource/ResDetail";
        public static final String VIEW_RES_LIST = "/resource/ResList";
        public static final String VIEW_404 = "/error/404";

        public static final String VIEW_ADMIN_INDEX = "/admin/Dashboard";
        public static final String VIEW_ADMIN_LOGIN = "/admin/Login";
        public static final String VIEW_ADMIN_USER_LIST = "/admin/UserList";
        public static final String VIEW_ADMIN_USER_EDIT = "/admin/UserEdit";
        public static final String VIEW_ADMIN_USER_DELETE = "/admin/UserDelete";
    }

    public static final class Attr {

        public static final String PARAM_INDEX = "index";
        public static final String PARAM_LOCATION = "location";
        public static final String PARAM_LOGIN = "login";
        public static final String PARAM_CLIENT = "client";
        public static final String PARAM_NOWPASS = "nowpass";
        public static final String PARAM_PASS = "pass";
        public static final String PARAM_USER_ID= "userId";
        public static final String PARAM_AVATAR = "avatar";
        public static final String PARAM_PAGES = "pages";
        public static final String PARAM_TREND_SETTING = "trendSetting";
        public static final String PARAM_TREND_SETTING_CHARS = "trendSettingChars";
        public static final String PARAM_FOUND = "found";

        public static final String REQ_OTHER = "other";


        public static final String SESSION_USER = "user";
    }

    public static final class Msg {

        public static final String ERROR_PREPARED_DATA_FAIELD = "数据准备失败";
    }

    public static class SQL {

    }
}