package cn.gin.wctf.module.news.entity;

/**
 * 新闻分类的常量枚举类
 * @author Gintoki
 * @version 2017-10-21
 */
public enum NewsClassify {

	/**
	 * 社区新闻分类
	 */
	BBS("bbs"), 
	
	/**
	 * 活动新闻分类
	 */
	ACTIVITY("activity"),
	
	/**
	 * banner区新闻分类
	 */
	BANNER("banner");
	
	private String value;
	
	private NewsClassify(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
