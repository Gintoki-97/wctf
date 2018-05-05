package cn.gin.wctf.module.post.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.Range;

import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.common.validator.post.PostSendGroup;
import cn.gin.wctf.module.sys.entity.Trend;
import cn.gin.wctf.module.sys.entity.TrendSupport;
import cn.gin.wctf.module.sys.entity.User;

/**
 * 贴子的实体类
 * @author Gintoki
 * @version 2017-10-07
 */
public class Post implements Serializable, TrendSupport {

	private static final long serialVersionUID = -5628371592572770658L;

	/*
	 * DO
	 * */
	private Integer id;			// 唯一键

	@Min(value = 1, groups = PostSendGroup.class)
	private Integer userId;		// 发送者id，用于延迟加载

//	@Pattern(regexp = PostSendGroup.TITLE, groups = PostSendGroup.class, message = "{post.send.title}")
	private String title;		// 标题
	private Date postTime;		// 发送时间
	private Integer viewed;		// 浏览量
	private Integer top;		// 热搜排序点
	private Integer close;		// 是否封贴

	@Range(min = 1, max = 5, groups = PostSendGroup.class, message = "{post.send.classify}")
	private Integer classify;	// 帖子分类

//	@Pattern(regexp = PostSendGroup.CONTENT, groups = PostSendGroup.class, message = "{post.send.content}")
	private String content;		// 内容

	/*
	 * VO
	 */
	private boolean collect;	// 是否收藏
	private String classifyStr;	// 用于显示到首页的分类
	private Integer countReply;	// 回帖数

	/*
	 * Support
	 */
	private User user;			// 发送者
	private List<Reply> replys;	// 回帖列表

	// Query
	private Integer start;
	private Integer limit;

	public Post() {}

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
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	public Integer getViewed() {
		return viewed;
	}
	public void setViewed(Integer viewed) {
		this.viewed = viewed;
	}
	public Integer getTop() {
		return top;
	}
	public void setTop(Integer top) {
		this.top = top;
	}
	public Integer getClose() {
		return close;
	}
	public void setClose(Integer close) {
		this.close = close;
	}
	public boolean isCollect() {
		return collect;
	}

	public void setCollect(boolean collect) {
		this.collect = collect;
	}

	public Integer getClassify() {
		return classify;
	}
	public void setClassify(Integer classify) {
		this.classify = classify;
		if(classify == null) {
			setClassifyStr(null);
			return;
		}
		switch(classify) {
		case 1:
			setClassifyStr("讨论");
			break;
		case 2:
			setClassifyStr("提问");
			break;
		case 3:
			setClassifyStr("建议");
			break;
		case 4:
			setClassifyStr("分享");
			break;
		case 5:
			setClassifyStr("公告");
			break;
		default:
			setClassifyStr(null);
			break;
		}
	}
	public String getClassifyStr() {
		return classifyStr;
	}

	public void setClassifyStr(String classifyStr) {
		this.classifyStr = classifyStr;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getCountReply() {
		return countReply;
	}
	public void setCountReply(Integer countReply) {
		this.countReply = countReply;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Reply> getReplys() {
		return replys;
	}
	public void setReplys(List<Reply> replys) {
		this.replys = replys;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + "]";
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
		Post other = (Post) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// ---------------------------

	@Override
	public Trend buildTrend() {
		int i = (classify <= 1 && classify <= 5) ? classify : 0;
//		TODO Test => There is updated at 2018-05-02 11:19:00.
//		String postUrl = Global.getConfig("web.root.uri") + "/post/" + id;
		String postUrl = "post/" + id;
//      TODO Test => There is updated at 2018-05-02 11:20:00.
//		String postImgUrl = Global.getConfig("web.server") + "/static/image/post_" + i + ".jpg";
		String postImgUrl = "static/image/post_" + i + ".jpg";
		String cls = StringUtils.EMPTY;
//      TODO Test => There is updated at 2018-05-02 11:20:00.
//		String tagLink = Global.getConfig("web.root.uri") + "/post/";
		String tagLink = "post/";
		switch(classify) {
		case 1:
			cls = "发起了讨论";
			tagLink += "discuss";
			break;
		case 2:
			cls = "提出了问题";
			tagLink += "question";
			break;
		case 3:
			cls = "发表了建议";
			tagLink += "suggest";
			break;
		case 4:
			cls = "分享了内容";
			tagLink += "sharing";
			break;
		case 5:
			cls = "更新了公告";
			tagLink += "notice";
			break;
		default:
			cls = "发表了新贴";
			tagLink = "index";
			break;
		}
		try {
			return new Trend.Builder(userId, cls, title, postTime.getTime(), postUrl, postImgUrl).tag(classifyStr).tagLink(tagLink).build();
		} catch(Exception e) {
			e.printStackTrace();
			try {
				return new Trend.Builder(userId, cls, title, postTime.getTime(), postUrl, postImgUrl).build();
			} catch(Exception ex) {
			    // There occurred some errors when cast the post object to trend object.
			    throw new RuntimeException(ex);
			}
		}
	}

}
