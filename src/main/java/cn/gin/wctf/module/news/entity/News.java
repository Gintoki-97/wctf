package cn.gin.wctf.module.news.entity;

import java.io.Serializable;
import java.util.Date;

import cn.gin.wctf.module.post.entity.Post;

/**
 * 网站的新闻实体类
 * @author Gintoki
 * @version 2017-10-21
 */
public class News implements Serializable {
	
	private static final long serialVersionUID = -6829712974748687160L;

	private Integer id;			// 唯一键
	private String classify;	// 新闻分类，参考 NewsClassify
	private Integer postId;		// 对应的发贴唯一键
	private String desc;		// 新闻描述，图片或者摘要
	
	private Date newsTime;		// 新闻日期
	
	private Post post;			// 新闻对应的发贴分类
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public void setClassify(NewsClassify classify) {
		this.classify = classify.getValue();
	}
	public Integer getPostId() {
		return postId;
	}
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getNewsTime() {
		return newsTime;
	}
	public void setNewsTime(Date newsTime) {
		this.newsTime = newsTime;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	
	@Override
	public String toString() {
		return "News [id=" + id + ", classify=" + classify + ", postId=" + postId + "]";
	}
	
}
