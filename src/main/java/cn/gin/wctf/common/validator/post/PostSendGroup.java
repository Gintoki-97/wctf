package cn.gin.wctf.common.validator.post;

/**
 * <p>用户发贴时，使用的字段校验分组</p>
 *
 * @author Gintoki
 * @version 2017-11-02
 */
public interface PostSendGroup {

	/**
	 * 用户发贴标题需要遵守的格式
	 */
	public static final String TITLE = "[\\S]{1,50}";

	/**
	 * 用户发贴标内容需要遵守的格式
	 */
	public static final String CONTENT = "[\\S]{1,50000}";

}
