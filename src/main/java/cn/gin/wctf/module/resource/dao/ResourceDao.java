package cn.gin.wctf.module.resource.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import cn.gin.wctf.module.resource.entity.Resource;

@MapperScan
public interface ResourceDao {
	
	/**
	 * <p>CRUD - R</p>
	 * <p>统计函数，查询所有分类所有资源的数量。</p>
	 * 
	 * @return 统计应用中所有资源的数量
	 */
	int countRes();

	/**
	 * <p>CRUD - R</p>
	 * <p>统计函数，根据指定分类查询相关资源的数量。</p>
	 * 
	 * @param clsInt - 指定的分类
	 * @return 统计属于该分类的资源的数量
	 */
	int countResByClassify(int classify);
	
	/**
	 * <p>CRUD - R</p>
	 * <p>统计函数，根据匹配指定标签和指定分类的资源的数量。</p>
	 * 
	 * @param tag - 指定标签，模糊查询
	 * @param cls - 指定分类，精准匹配
	 * @return 符合查询条件的数量
	 */
	int countResByItem(Resource res);
	
	/**
	 * <p>CRUD - R</p>
	 * <p>查询指定资源 ID 对应的资源。</p>
	 * 
	 * @param resId - 指定资源 ID
	 * @return 指定资源 ID 对应的资源
	 */
	Resource getResById(Integer resId);

	/**
	 * <p>CRUD - R</p>
	 * <p>查询所有分类所有资源。</p>
	 * 
	 * @param start - 分页的起始位置
	 * @param limit - 查询的数量
	 * @return 所有分类所有资源的列表
	 */
	List<Resource> listRes(int start, int limit);

	/**
	 * <p>CRUD - R</p>
	 * <p>查询指定分类所有资源。</p>
	 * 
	 * @param cls - 指定资源分类
	 * @param start - 分页的起始位置
	 * @param limit - 查询的数量
	 * @return 指定分类所有资源的列表
	 */
	List<Resource> listResByClassify(int classify, int start, int limit);

	/**
	 * <p>CRUD - R</p>
	 * <p>查询匹配指定查询条件所有资源。</p>
	 * 
	 * @param tag - 传入的资源标签信息
	 * @param cls - 传入的资源分类信息
	 * @param order - 传入的资源排序信息
	 * @param start - 分页的起始位置
	 * @param limit - 查询的数量
	 * @return 匹配指定查询条件所有资源
	 */
	List<Resource> listResByItem(Resource res);

	/**
	 * <p>CRUD - U</p>
	 * <p>更新指定资源文件的下载信息。</p>
	 * 
	 * @param resId - 指定的资源文件 ID
	 * @return 受影响的行数
	 */
	int updateResDownloads(Integer resId);

}
