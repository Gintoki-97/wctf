package cn.gin.wctf.common.paging;

import java.io.Serializable;
import java.util.List;

/**
 * <p>实现应用所有分页相关功能的根据类。</p>
 * 
 * @param <T> 分页类存储的实体对象类型
 * @author Gintoki
 * @version 2017-10-05
 */
public class Page<T> implements Serializable {
	
	private static final long serialVersionUID = -4879815869242845239L;

	/** 当前页  **/
	private int index;
	
	/** 数据总数 */
	private int count;
	
	/** 总页数 **/
	private int totalPage;
	
	/** 每页显示的数据条数 */
	private int limit;
	
	/** 是否允许客户端动态选择每页显示的数据条数 */
	private boolean limits;
	
	/** 连续出现的页码个数 */
	private int groups;
	
	/** 每组页码项需要的查询条件*/
	private String queryItems;
	
	/** 每页对应的实体数据 */
	private List<T> list;
	
	/**
	 * 使用默认数据
	 */
	public Page() {
		this.index = 1;		// 当前页，默认为第一页
		this.limit = 10;	// 每页显示的数据条数
		this.limits = true;	// 允许客户端修改每页显示的数据条数
		this.groups = 5;	// 连续出现页码项的个数
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isLimits() {
		return limits;
	}

	public void setLimits(boolean limits) {
		this.limits = limits;
	}

	public int getGroups() {
		return groups;
	}

	public void setGroups(int groups) {
		this.groups = groups;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	public String getQueryItems() {
		return queryItems;
	}

	public void setQueryItems(String queryItems) {
		this.queryItems = queryItems;
	}

	@Override
	public String toString() {
		return "Page [count=" + count + ", limit=" + limit + ", limits=" + limits + ", groups=" + groups + "]";
	}

	/**
	 * <p>根据分页对象的总记录数和每页显示条数，计算分页总数。</p>
	 */
	public void calculate() {
		if(limit != 0) {
			if(count % limit == 0) {
				totalPage = count / limit;
			} else {
				totalPage = count / limit + 1;
			}
		}
	}

}
