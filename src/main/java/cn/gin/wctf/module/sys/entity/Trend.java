package cn.gin.wctf.module.sys.entity;

import cn.gin.wctf.common.util.StringUtils;

/**
 * <p>用户个人动态实体类。</p>
 * 
 * @author Gintoki
 * @version 2017-12-24 平安夜
 */
public class Trend {
	

	private Integer id;			// 动态 ID
	private Integer userId;		// 用户 ID
	private String classify;	// 动态分类
	private String title;		// 动态标题
	private Long time;			// 动态发布时间
	private String link = StringUtils.EMPTY;	// 动态地址
	private String imgLink = StringUtils.EMPTY;	// 图片地址
	
	private String nickname;	// 用户昵称
	private String header;		// 用户头像
	private String tag = StringUtils.EMPTY;		// 标签
	private String tagLink = StringUtils.EMPTY;	// 标签链接
	private String msg = StringUtils.EMPTY;		// 信息
	private String desc = StringUtils.EMPTY;	// 描述
	
	public Trend() {}
	
	public Trend(Builder builder) {
		this.id = builder.id;
		this.userId = builder.userId;
		this.classify = builder.classify;
		this.title = builder.title;
		this.time = builder.time;
		this.link = builder.link;
		this.imgLink = builder.imgLink;
		this.tag = builder.tag;
		this.tagLink = builder.tagLink;
		this.msg = builder.msg;
		this.desc = builder.desc;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getImgLink() {
		return imgLink;
	}
	public void setImgLink(String imgLink) {
		this.imgLink = imgLink;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getTagLink() {
		return tagLink;
	}
	public void setTagLink(String tagLink) {
		this.tagLink = tagLink;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public int hashCode() {
		final int prime = 47;
		int result = this.getClass().hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trend other = (Trend) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/**
	 * <p>用于创建用户动态对象的构建器。</p>
	 * 
	 * @author Gintoki
	 * @version 2017-12-24
	 */
	public static class Builder {
		
		private Integer id;			// 动态 ID
		private Integer userId;		// 用户 ID
		private String classify;	// 动态分类
		private String title;		// 动态标题
		private Long time;			// 动态发布时间
		private String link;		// 动态地址
		private String imgLink;		// 图片地址
		
		private String tag;			// 标签
		private String tagLink;		// 标签链接
		private String msg;			// 信息
		private String desc;		// 描述
		
		public Builder(Integer userId, String classify, String title, Long time, String link, String imgLink) {
			this.userId = userId;
			this.classify = classify;
			this.title = title;
			this.time = time;
			this.link = link;
			this.imgLink = imgLink;
		}
		
		public Builder id(Integer id) {
			this.id = id;
			return this;
		}
		
		public Builder tag(String tag) {
			this.tag = tag;
			return this;
		}
		
		public Builder tagLink(String tagLink) {
			this.tagLink = tagLink;
			return this;
		}
		
		public Builder msg(String msg) {
			this.msg = msg;
			return this;
		}
		
		public Builder desc(String desc) {
			this.desc = desc;
			return this;
		}
		
		public Trend build() {
			return new Trend(this);
		}
		
	}
}
