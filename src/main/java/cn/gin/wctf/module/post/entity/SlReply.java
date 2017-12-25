package cn.gin.wctf.module.post.entity;

import java.io.Serializable;
import java.util.Date;

import cn.gin.wctf.module.sys.entity.User;

/**
 * 回帖（二级）的实体类
 * @author Gintoki
 * @version 2017-10-07
 */
public class SlReply implements Serializable {
	
	private static final long serialVersionUID = -4666938538333920083L;
	
	private Integer id;			// 唯一键
	private Integer userId;		// 对应的reply的唯一键
	private Integer replyId;	// 对应的reply的唯一键
	private Date replyTime;		// 回帖时间
	private String content;		// 回帖内容
	
	private Reply reply;		// 回复的是哪个回复
	private User user;			// 回帖用户
	
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
	public Integer getReplyId() {
		return replyId;
	}
	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Reply getReply() {
		return reply;
	}
	public void setReply(Reply reply) {
		this.reply = reply;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public String toString() {
		return "SlReply [id=" + id + ", content=" + content + "]";
	}
	
}
