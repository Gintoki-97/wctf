package cn.gin.wctf.common.validator.user;

/**
 * <p>用户注册时，使用的字段校验分组</p>
 * 
 * @author Gintoki
 * @version 2017-11-02
 */
public interface UserRegisterGroup {

	/**
	 * 用户注册时账号需要遵守的格式
	 */
	public static final String ACCOUNT = "^([\\w_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+$";
	
	/**
	 * 用户注册时密码需要遵守的格式
	 */
	public static final String PASSWORD = "^[\\w_-]{32}$";

	/**
	 * 用户注册时昵称需要遵守的格式
	 */
	public static final String NICKNAME = "^[\\S]{1,16}$";
}
