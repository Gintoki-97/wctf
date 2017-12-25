package cn.gin.wctf.module.post.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import cn.gin.wctf.common.validator.post.ReplyGroup;
import cn.gin.wctf.module.sys.entity.User;

/**
 * <p>回帖（一级）的实体类</p>
 * 
 * @author Gintoki
 * @version 2017-10-07
 */
public class Reply implements Serializable {

	private static final long serialVersionUID = -2932954730767241192L;

	/*
	 * DO
	 */
	private Integer id;			// 唯一键
	
	@Min(value = 1, groups = ReplyGroup.class, message = "{reply.userId}")
	private Integer userId;		// 回复的用户的唯一键
	
	@Min(value = 1, groups = ReplyGroup.class, message = "{reply.postId}")
	private Integer postId;		// 回复的贴子的唯一键
	
	private Date replyTime;		// 回帖时间		
	private Integer thumb;		// 赞
	
	@NotNull(groups = ReplyGroup.class, message = "{reply.content}")
	private String content;		// 回帖内容
	
	/*
	 * VO
	 */
	private Integer thumbed;	// 是否已经点赞
	private User user;			// 回帖用户
	private Post post;			// 回复的是哪个贴子
	private List<SlReply> slReplys;		// 回帖的回帖
	
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
	public Integer getPostId() {
		return postId;
	}
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	public Integer getThumb() {
		return thumb;
	}
	public void setThumb(Integer thumb) {
		this.thumb = thumb;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getThumbed() {
		return thumbed;
	}
	public void setThumbed(Integer thumbed) {
		this.thumbed = thumbed;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	public List<SlReply> getSlReplys() {
		return slReplys;
	}
	public void setSlReplys(List<SlReply> slReplys) {
		this.slReplys = slReplys;
	}
	
	@Override
	public String toString() {
		return "Reply [id=" + id + ", content=" + content + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Reply other = (Reply) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
