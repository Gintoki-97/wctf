package cn.gin.wctf.module.post.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import cn.gin.wctf.module.post.entity.SlReply;

@MapperScan
public interface SlReplyDao {


	/**
	 * <p>通过回贴 ID 查询所有二级回贴</p>
	 * @param id
	 * @return
	 */
	List<SlReply> listSlReplyByReplyId(Integer id);

	/**
	 * <p>CRUD - C</p>
	 * <p>将一个新的二级回贴插入到数据库</p>
	 * 
	 * @param slReply - 新的二级回贴
	 * @return 受 DML 影响的行数
	 */
	int saveSlReply(SlReply slReply);

}
