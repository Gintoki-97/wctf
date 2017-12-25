package cn.gin.wctf.module.post.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gin.wctf.module.post.dao.ReplyDao;
import cn.gin.wctf.module.post.entity.Reply;
import cn.gin.wctf.module.post.entity.SlReply;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;

/**
 * <p>回贴业务类</p>
 * 
 * @author Gintoki
 * @version 2017-10-03
 */
@Service
public class ReplyService {

	@Autowired
	private SlReplyService slReplyService;
	
	@Autowired
	private ReplyDao replyDao;
	

	// ----------------------------
	// Service
	// ----------------------------	

	/**
	 * <p>用户的回贴请求，用户在发贴详情页面提交回贴表单，会触发对此业务方法的调用。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$Reply$reply - 用户传入的回贴信息
	 */
	public void saveReply(ApplicationDataSupport data) {
		Reply reply = null;
		try {
			reply = data.getParameter("reply");
			data.clearParams();
		} catch(Exception e) {
			data.setStatus(321);
			data.setMsg("数据准备失败");
			return;
		}
		reply.setThumb(0);
		if(replyDao.saveReply(reply) == 1) {
			data.setStatus(320);
			data.setMsg("回贴成功");
		} else {
			data.setStatus(322);
			data.setMsg("数据库请求失败");
		}
	}
	
	// ----------------------------
	// Support
	// ----------------------------
	
	/**
	 * <p>通过发贴的 ID 获取指定发贴的回贴列表数。</p>
	 * 
	 * @param id - 指定发贴的 ID
	 * @return 指定的发贴的回贴数
	 */
	public Integer countReplyByPostId(Integer id) {
		return id == 0 ? 0 : replyDao.countReplyByPostId(id);
	}

	/**
	 * <p>通过指定发贴 ID 查询出所有的回贴列表。</p>
	 * 
	 * @param postId - 指定的发贴的 ID
	 * @return 指定的发贴的回贴列表
	 */
	public List<Reply> listReplyByPostId(Integer postId) {
		List<Reply> replys = replyDao.listReplyByPostId(postId);
		for (Reply reply : replys) {
			List<SlReply> slReplys = slReplyService.listSlReplyByReplyId(reply.getId());
			reply.setSlReplys(slReplys);
			reply.setThumb(replyDao.countThumb(reply.getId()));
		}
		return replys;
	}

	/**
	 * <p>回复指定发贴 ID 对应的发贴。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$SLReply$slReply - 用户传入的回复回贴的信息
	 */
	public void replyToReply(ApplicationDataSupport data) {
		SlReply slReply = null;
		try {
			slReply = data.getParameter("slReply");
		} catch(Exception e) {
			data.setStatus(341);
			data.setMsg("数据准别失败");
			data.clearParams();
			return;
		}
		if(slReplyService.saveSlReply(slReply)) {
			data.setStatus(340);
			data.setMsg("回复成功");
		} else {
			data.setStatus(341);
			data.setMsg("数据库请求出现异常");
		}
		data.clearParams();
	}

	/**
	 * <p>点赞指定回贴 ID 对应的回贴。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$Integer$userId - 用户传入的发起点赞的用户 ID，即当前进行操作的用户
	 * @param data$Integer$replyId - 用户传入的被点赞的回贴 ID
	 */
	public void replyThumb(ApplicationDataSupport data) {
		int userId = 0;
		int replyId = 0;
		try {
			userId = data.getParameter("userId");
			replyId = data.getParameter("replyId");
			if(userId == 0 || replyId == 0) {
				throw new IllegalArgumentException();
			}
		} catch(Exception e) {
			data.setStatus(361);
			data.setMsg("数据准备失败");
			data.clearParams();
			return;
		}
		List<Integer> users = listThumb(replyId);
		if(users.contains(userId)) {
			data.setStatus(363);
			data.setMsg("已经赞过了~");
		} else {
			if(replyDao.saveThumb(userId, replyId) == 1) {
				data.setStatus(360);
				data.setMsg("点赞成功");
			} else {
				data.setStatus(362);
				data.setMsg("数据库请求出现异常");
			}
		}
		data.clearParams();
	}

	/**
	 * <p>获取指定回贴对应的点赞列表</p>
	 * 
	 * @param replyId - 指定的回贴 ID
	 * @return 点赞列表
	 */
	public List<Integer> listThumb(Integer replyId) {
		return replyDao.listThumb(replyId);
	}

}
