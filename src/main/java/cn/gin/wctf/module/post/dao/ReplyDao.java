package cn.gin.wctf.module.post.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import cn.gin.wctf.module.post.entity.Reply;

@MapperScan
public interface ReplyDao {

	/**
	 * <p>CRUD - R</p>
	 * <p>统计指定发贴 ID 对应的发贴的回贴数</p>
	 * 
	 * @return 指定发贴 ID 对应的发贴的回贴数
	 */
	Integer countReplyByPostId(Integer id);

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定发贴 ID 获取对应的发贴的所有回贴列表</p>
	 * 
	 * @param id - 指定的发贴 ID
	 * @return 指定发贴 ID 获取对应的发贴的所有回贴列表
	 */
	List<Reply> listReplyByPostId(Integer id);

	/**
	 * <p>CRUD - C</p>
	 * <p>保存发贴信息</p>
	 * 
	 * @param reply - 用户传入的发贴信息
	 * @return 受 DML 影响的行数
	 */
	int saveReply(Reply reply);

	/**
	 * <p>CRUD - C</p>
	 * <p>保存点赞回贴信息到 THUMB 表</p>
	 * 
	 * @param userId - 发起点赞的用户 ID
	 * @param replyId - 被点赞的回贴 ID
	 * @return 受 DML 影响的行数
	 */
	int saveThumb(int userId, int replyId);

	/**
	 * <p>CRUD - R</p>
	 * <>通过指定用户 ID 和指定回贴 ID 查询数据库中已存在的记录，用于验证该用户是否已经点赞过该回贴。</p>
	 * 
	 * @param replyId - 被点赞的回贴 ID
	 * @return 与条件匹配的行数
	 */
	List<Integer> listThumb(int replyId);

	/**
	 * <p>CRUD - R</p>
	 * <p>统计指定回贴 ID 的点赞数</p>
	 * 
	 * @param replyId - 指定回贴 ID
	 * @return 指定回贴 ID 的点赞数
	 */
	int countThumb(Integer replyId);
}
