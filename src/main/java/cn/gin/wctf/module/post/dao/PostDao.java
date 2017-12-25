package cn.gin.wctf.module.post.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import cn.gin.wctf.module.post.entity.Post;

@MapperScan
public interface PostDao {

	/**
	 * 懒加载（不查询发贴内容），根据分页对象，查询所有发贴
	 * @param start 起始位置，下标
	 * @param count	获取帖子的数量
	 * @return
	 */
	List<Post> listPostLazing(int start, int count);
	
	/**
	 * 懒加载（不查询发贴内容），查询指定数量的置顶贴
	 * @param count 查询的置顶帖数量，客户端默认设为3，管理员端任意
	 * @return
	 */
	List<Post> listTopLazing(int count);
	
	/**
	 * 懒加载（不查询发贴内容），查询指定数量和分类的发贴，不包括置顶贴
	 * @param classify	发贴分类
	 * @param start	数据库中开始查询的位置
	 * @param limit	查询的记录数
	 * @return
	 */
	List<Post> listPostByClassifyLazing(int classify, int start, int limit);
	
	/**
	 * <p>CRUD - R</p>
	 * <p>获取指定发贴的收藏者列表</p>
	 * 
	 * @param postId - 指定的发帖 ID
	 * @return 收藏者列表
	 */
	List<Integer> listCollect(Integer postId);

	/**
	 * 发贴，将post保存到数据库
	 * @param post
	 */
	int savePost(Post post);
	
	/**
	 * 查询普通的发贴总数
	 * @return
	 */
	int countPost();
	
	/**
	 * 置顶的发贴总数
	 * @return
	 */
	int countTop();

	/**
	 * 指定分类的发贴总数
	 * @param classify
	 * @return
	 */
	int countPostByClassify(Integer classify);

	/**
	 * 根据唯一键查询指定发贴的所有信息
	 * @param id 唯一键
	 * @return
	 */
	Post getPostById(Integer id);

	/**
	 * 更新发贴浏览量
	 * @param id - 被更新的发贴id
	 * @return 更新影响的数据条数
	 */
	int updateViewedByPostId(Integer id);

	/**
	 * <p>保存收藏发贴请求</p>
	 * 
	 * @param userId - 即当前进行操作的用户 ID
	 * @param postDao - 被收藏的发贴 ID
	 * @return 受影响的数据条数
	 */
	int saveCollect(int userId, Integer postId);

}
