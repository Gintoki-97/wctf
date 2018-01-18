package cn.gin.wctf.module.resource.entity;

import java.io.Serializable;
import java.util.Date;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.module.sys.entity.Trend;
import cn.gin.wctf.module.sys.entity.TrendSupport;
import cn.gin.wctf.module.sys.entity.User;

/**
 * <p>网站中用户上传下载的资源的实体类。</p>
 * 
 * @author Gintoki
 * @version 2017-11-22
 */
public class Resource implements Serializable, TrendSupport {

	private static final long serialVersionUID = 7333742041147204151L;
	
	private Integer id;			// 唯一键
	
	private Integer userId;		// 上传者唯一键
		
	private String name;		// 资源名
	private Integer classify;	// 资源分类
	private String img;			// 图片路径
	private String path;		// 文件路径
	private Date uploadTime;	// 上传时间
	private String tagStr;		// 标签
	private Integer downloads;	// 下载量
	private Integer size;		// 文件大小
	private Integer weight;		// 悬赏斤
	private String desc;		// 描述
	
	private String[] tags;		// 资源标签
	private Integer offer;		// 悬赏斤
	private User user;			// 上传者
	
	// 查询使用
	
	private transient String itemTag;
	private transient Integer itemCls;
	private transient Integer itemOrder;
	private transient Integer itemStart;
	private transient Integer itemLimit;
	

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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getClassify() {
		return classify;
	}
	public void setClassify(Integer classify) {
		this.classify = classify;
		setImg(Global.getConfig("web.server") + "/static/image/res_" + classify + ".jpg");
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public String getTagStr() {
		return tagStr;
	}
	public void setTagStr(String tagStr) {
		this.tagStr = tagStr;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
		setTagStr(StringUtils.toString(tags, '\20'));
	}
	public Integer getDownloads() {
		return downloads;
	}
	public void setDownloads(Integer downloads) {
		this.downloads = downloads;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public Integer getOffer() {
		return offer;
	}
	public void setOffer(Integer offer) {
		this.offer = offer;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getItemTag() {
		return itemTag;
	}
	public void setItemTag(String itemTag) {
		this.itemTag = itemTag;
	}
	public Integer getItemCls() {
		return itemCls;
	}
	public void setItemCls(Integer itemCls) {
		this.itemCls = itemCls;
	}
	public Integer getItemOrder() {
		return itemOrder;
	}
	public void setItemOrder(Integer itemOrder) {
		this.itemOrder = itemOrder;
	}
	public Integer getItemStart() {
		return itemStart;
	}
	public void setItemStart(Integer itemStart) {
		this.itemStart = itemStart;
	}
	public Integer getItemLimit() {
		return itemLimit;
	}
	public void setItemLimit(Integer itemLimit) {
		this.itemLimit = itemLimit;
	}
	public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
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
		Resource other = (Resource) obj;
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
		String cls = StringUtils.EMPTY;
		switch(classify) {
		case 1:
			cls = "上传了文档";
			break;
		case 2:
			cls = "上传了代码";
			break;
		case 3:
			cls = "上传了工具";
			break;
		default:
			cls = "上传了文件";
			break;
		}
		String resUrl = "res/" + id;
		String resImgUrl = img;
		String t = tagStr == null ? StringUtils.toString(tags, '\20') : tagStr;
		try {
			return new Trend.Builder(userId, cls, name, uploadTime.getTime(), resUrl, resImgUrl).tag(t).build();
		} catch(Exception e) {
			e.printStackTrace();
			try {
				return new Trend.Builder(userId, cls, name, uploadTime.getTime(), resUrl, resImgUrl).build();
			} catch(Exception ex) {ex.printStackTrace();}
		}
		return null;
	}
}
