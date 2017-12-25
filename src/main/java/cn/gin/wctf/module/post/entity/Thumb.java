package cn.gin.wctf.module.post.entity;

public class Thumb {

	private Integer userId;		// 点赞的用户
	private Integer replyId;	// 被点赞的回贴
	
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
	
	@Override
	public String toString() {
		return "Thumb [userId=" + userId + ", replyId=" + replyId + "]";
	}
}
