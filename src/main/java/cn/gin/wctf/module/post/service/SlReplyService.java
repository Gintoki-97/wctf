package cn.gin.wctf.module.post.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gin.wctf.module.post.dao.SlReplyDao;
import cn.gin.wctf.module.post.entity.SlReply;

/**
 * <p>二级回贴业务类</p>
 * 
 * @author Gintoki
 * @version 2017-10-03
 */
@Service
public class SlReplyService {
	
	@Autowired
	private SlReplyDao slReplyDao;

	// ---------------------
	// Support
	// ---------------------
	
	/**
	 * <p>通过指定回贴的 ID 查询该回贴下的二级回复列表</p>
	 * 
	 * @param replyId 过指定回贴的 ID
	 * @return 该回贴下的二级回复列表
	 */
	public List<SlReply> listSlReplyByReplyId(Integer replyId) {
		return slReplyDao.listSlReplyByReplyId(replyId);
	}
	
	/**
	 * <p>回复指定的回贴</p>
	 * 
	 * @param slReply - 页面传入的二级回贴的信息
	 * @return 是否回复成功
	 */
	public boolean saveSlReply(SlReply slReply) {
		if(slReply.getReplyTime() == null) {
			slReply.setReplyTime(new Date());
		}
		return slReplyDao.saveSlReply(slReply) == 1;
	}
	
}
