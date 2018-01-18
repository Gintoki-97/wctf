package cn.gin.wctf.module.post.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gin.wctf.common.ansy.AnsyTaskManager;
import cn.gin.wctf.common.ansy.task.AnsyTaskWithNoResult;
import cn.gin.wctf.common.paging.Page;
import cn.gin.wctf.common.util.CacheUtils;
import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.util.RedisIndex;
import cn.gin.wctf.module.post.dao.PostDao;
import cn.gin.wctf.module.post.entity.Post;
import cn.gin.wctf.module.post.entity.Reply;
import cn.gin.wctf.module.post.entity.SlReply;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.service.SystemService;
import cn.gin.wctf.module.sys.service.TrendService;
import cn.gin.wctf.module.sys.service.UserService;

/**
 * <p>发贴业务类</p>
 * 
 * @author Gintoki
 * @version 2017-10-03
 */
@Service
public class PostService {
	
	@Autowired
	private SystemService systemService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private TrendService trendService;
	
	@Autowired
	private ReplyService replyService;
	
	@Autowired
	private SlReplyService slReplyService;
	
	@Autowired
	private PostDao postDao;
	
	@Autowired
	private AnsyTaskManager ansyTaskManager;
	
	// ----------------------------
	// Service
	// ----------------------------	
	
	/**
	 * <p>预处理首页发贴数据列表。从 EhCache 中获取，如果缓存中没有，从 Redis 中获取；如果还是没有就从数据库中获取。
	 * 如果客户端需要进行分页，会直接从数据库中查询，因为缓存中只存储首页默认的数据。</p>
	 * 
	 * @param paging - 分页对象，如果为 null 表示不需要分页
	 * @return 首页发贴数据列表
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Post>> prepareIndexPost(Page<Post> paging) {
		Map<String, List<Post>> indexData = null;
		List<Post> tops = null;
		List<Post> posts = null;
		if(paging == null) {	// 当前是首页，不需要分页
			// 从一级缓存（Ehcache）中获取
			tops = (List<Post>) CacheUtils.get(CacheUtils.POST_CACHE, "indexTops");
			if(tops == null || tops.size() != 3) {
				// 从二级缓存（Redis）中获取
				tops = (List<Post>) JedisUtils.getObject("indexTops", RedisIndex.POST_CACHE);
				if(tops == null || tops.size() != 3) {
					// 从数据库中获取，并更新一级缓存和二级缓存
					tops = postsWrapper(postDao.listTopLazing(3));
					CacheUtils.put(CacheUtils.POST_CACHE, "indexTops", tops);
					JedisUtils.setObject("indexTops", tops, 60 * 10, RedisIndex.POST_CACHE);
				} else {
					// 二级缓存中有，只更新一级缓存
					CacheUtils.put(CacheUtils.POST_CACHE, "indexTops", tops);
				}
			}
			posts = (List<Post>) CacheUtils.get(CacheUtils.POST_CACHE, "indexPosts");
			if(posts == null || posts.size() != 10) {
				// 从二级缓存（Redis）中获取
				posts = (List<Post>) JedisUtils.getObject("indexPosts", RedisIndex.POST_CACHE);
				if(posts == null || posts.size() != 10) {
					// 从数据库中获取，并更新一级缓存和二级缓存
					posts = postsWrapper(postDao.listPostLazing(0, 10));
					CacheUtils.put(CacheUtils.POST_CACHE, "indexPosts", posts);
					JedisUtils.setObject("indexPosts", posts, 60 * 10, RedisIndex.POST_CACHE);
				} else {
					// 二级缓存中有，只更新一级缓存
					CacheUtils.put(CacheUtils.POST_CACHE, "indexPosts", posts);
				}
			}
			indexData = new HashMap<String, List<Post>> (2);
			indexData.put("tops", tops);
			indexData.put("posts", posts);
		} else {
			// 设置分页对象的数据
			paging.setCount(postDao.countPost());
			paging.calculate();
			// 根据分页对象从数据库中获取
			tops = postDao.listTopLazing(3);
			posts = postDao.listPostLazing((paging.getIndex() - 1) * paging.getLimit(), paging.getLimit());
			indexData = new HashMap<String, List<Post>> (2);
			indexData.put("tops", postsWrapper(tops));
			indexData.put("posts", postsWrapper(posts));
		}
		return indexData;
	}
	
	/**
	 * <p>用户发贴，用户在发贴页面提交发帖表单，会触发对此业务方法的调用。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$User$login - 当前 Session 中登录的用户
	 * @param data$Post$post - 页面传入的发贴信息的封装
	 */
	public void sendPost(ApplicationDataSupport data) {
		User login = null;
		Post post = null;
		try {
			login = data.getParameter("login");
			post = data.getParameter("post");
		} catch (Exception e) {
			data.setStatus(301);
			data.setMsg("数据准备异常");
			data.clearParams();
			return;
		} 
		post.setTop(0);
		post.setViewed(0);
		post.setClose(0);
		post.setUserId(login.getId());
		post.setPostTime(new Date());
		if(postDao.savePost(post) == 1) {
			data.setStatus(300);
			data.setMsg("发贴成功");
			// 强制缓存失效
			JedisUtils.del("indexPosts", RedisIndex.POST_CACHE);
			// 保存用户动态
			final Post p = post;
			ansyTaskManager.submit(new AnsyTaskWithNoResult() {
				@Override
				public void execute() {
					// 保存用户发贴动态
					trendService.saveTrend(p);
				}
			});
		} else {
			data.setStatus(302);
			data.setMsg("数据库请求出现异常");
		}
		data.clearParams();
		return;
	}

	/**
	 * <p>获取指定分类的所有发贴，用户在首页或分类查看发贴页面进行查看指定类型的发贴请求时，会请求此控制器方法处理。</p>
	 * 
	 * @param classify - 指定分类
	 * @param index - 分页对象，如果为 null 表示不需要分页
	 * @return 指定分类的所有发贴
	 */
	public Page<Post> listPostByClassify(String classify, Integer index) {
		Page<Post> paging = new Page<Post>();
		if(index == null || index <= 1) {
			paging.setIndex(1);
		} else {
			paging.setIndex(index);
		}
		Post query = new Post();
		Integer clsInt = getClassifyByString(classify);
		query.setClassify(clsInt == 0 ? null : clsInt);
		paging.setCount(postDao.countPostByClassify(query));
		paging.calculate();
		// 根据分页对象从数据库中获取
		query.setStart((paging.getIndex() - 1) * paging.getLimit());
		query.setLimit(paging.getLimit());
		List<Post> posts = postDao.listPostByClassifyLazing(query);
		// 更新用户和浏览量
		for (Post post : posts) {
			post.setUser(userService.getUserById(post.getUserId()));
			post.setCountReply(replyService.countReplyByPostId(post.getId()));
		}
		paging.setList(posts);
		return paging;
	}
	
	/**
	 * <p>收藏指定发贴 ID 对应的发贴，会触发对此业务方法的调用。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$Integer$userId - 用户传入的发起收藏请求的用户 ID，即当前进行操作的用户
	 * @param data$Integer$postId - 用户传入的被收藏的发贴 ID
	 */
	public void saveCollect(ApplicationDataSupport data) {
		int userId = 0;
		int postId = 0;
		try {
			userId = data.getParameter("userId");
			postId = data.getParameter("postId");
			if(userId == 0 || postId == 0) {
				throw new IllegalArgumentException();
			}
		} catch(Exception e) {
			data.setStatus(371);
			data.setMsg("数据准备失败");
			data.clearParams();
			return;
		}
		List<Integer> users = postDao.listCollect(postId);
		if(users.contains(userId)) {
			data.setStatus(373);
			data.setMsg("已经收藏过了~");
		} else {
			if(postDao.saveCollect(userId, postId) == 1) {
				data.setStatus(370);
				data.setMsg("收藏成功");
			} else {
				data.setStatus(372);
				data.setMsg("数据库请求出现异常");
			}
		}
		data.clearParams();
	}
	
	// ------------------------
	// Support
	// ------------------------
	
	/**
	 * <p>格式化发贴分类信息，用于页面和数据库之间的数据格式转换。</p>
	 * 
	 * @param classify - 页面传入的发贴分类分类字符串
	 * @return 指定发贴分类对应整型（数据库中存储格式为 TINYINT）
	 */
	private int getClassifyByString(String classify) {
		classify = classify.toUpperCase();
		switch (classify) {
			case "DISCUSS":
				return 1;
			case "QUESTION":
				return 2;
			case "SUGGEST":
				return 3;
			case "SHARING":
				return 4;
			case "NOTICE":
				return 5;
			default:
				return 0;
		}
	}

	/**
	 * <p>根据发贴 ID 加载指定发贴的所有信息，同时会级联查询所有回贴及二级回贴信息。</p>
	 * 
	 * @param postId - 发贴 ID
	 * @return 指定发贴的所有信息封装
	 */
	public Post getPostById(Integer postId) {
		if(postId != null && postId > 0) {
			Post post = postDao.getPostById(postId);
			User login = systemService.getLogin();
			Integer userId = login == null ? 0 : login.getId() == null ? 0 : login.getId();
			// 更新用户信息
			post.setUser(userService.getUserById(post.getUserId()));
			post.setCountReply(replyService.countReplyByPostId(post.getId()));
			// 查询回贴及二级回贴
			List<Reply> replys = replyService.listReplyByPostId(postId);
			for (Reply reply : replys) {
				reply.setUser(userService.getUserByIdOptional(reply.getUserId()));
				List<SlReply> slRplys = slReplyService.listSlReplyByReplyId(reply.getId());
				for (SlReply slReply : slRplys) {
					slReply.setUser(userService.getUserByIdOptional(slReply.getUserId()));
				}
				List<Integer> users = replyService.listThumb(reply.getId());
				// 设置
				if(users.contains(userId)) {
					reply.setThumbed(1);
				} else {
					reply.setThumbed(0);
				}
				reply.setSlReplys(slRplys);
			}
			// 设置收藏
			List<Integer> users = postDao.listCollect(postId);
			if(users.contains(userId)) {
				post.setCollect(true);
			} else {
				post.setCollect(false);
			}
			post.setReplys(replys);
			return post;
		}
		return null;
	}
	
	/**
	 * <p>根据发贴 ID 懒加载指定发贴的所有信息和发贴用户信息，对于回贴信息不会进行关联查询。</p>
	 * 
	 * @param postId - 发贴 ID
	 * @return 指定发贴的所有信息封装，不包括关联查询
	 */
	public Post getPostByIdLazing(Integer postId) {
		if(postId != null && postId > 0) {
			Post post = postDao.getPostById(postId);
			// 更新用户信息
			post.setUser(userService.getUserById(post.getUserId()));
			post.setCountReply(replyService.countReplyByPostId(post.getId()));
			return post;
		}
		return null;
	}

	/**
	 * <p>浏览指定发贴时，自动更新该发贴的浏览量</p>
	 * 
	 * @param id - 需要更新的发贴的 ID
	 * @return 是否更新成功
	 */
	public boolean updateViewed(Integer id) {
		return postDao.updateViewedByPostId(id) == 1;
	}
	
	
	// ----------------------------
	// Private
	// ----------------------------
	
	/**
	 * <p>批量为发帖对象填充依赖信息</p>
	 * 
	 * @param posts - 需要被填充的发贴列表
	 * @return 填充后的发贴列表
	 */
	private List<Post> postsWrapper(List<Post> posts) {
		for (Post post : posts) {
			post.setUser(userService.getUserByIdOptional(post.getUserId()));
			post.setCountReply(replyService.countReplyByPostId(post.getId()));
		}
		return posts;
	}
	
}
