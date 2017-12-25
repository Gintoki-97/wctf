package cn.gin.wctf.module.news.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cn.gin.wctf.common.util.CacheUtils;
import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.util.RedisIndex;
import cn.gin.wctf.module.news.dao.NewsDao;
import cn.gin.wctf.module.news.entity.News;
import cn.gin.wctf.module.post.service.PostService;

/**
 * <p>新闻业务类</p>
 * 
 * @author Gintoki
 * @version 2017-10-04
 */
@Service
public class NewsService {

	@Autowired
	private PostService postService;
	
	@Autowired
	private NewsDao newsDao;
	
	
	// ------------------------
	// Service
	// ------------------------	
	
	/**
	 * <p>预处理首页新闻列表数据，程序会先从一级缓存中获取，如果为空再从二级缓存中获取，如果都没获取到，
	 * 则从数据库中获取。</p>
	 * 
	 * @return 首页新闻列表
	 */
	public Map<String, List<News>> prepareIndexNews() {
		Map<String, List<News>> newsCache = new HashMap<String, List<News>>(4);
		List<News> bannerNews = listLastedNewsLazing("bannerNews", "banner");
		List<News> activityNews =  listLastedNewsLazing("activityNews", "activity");
		List<News> bbsNews =  listLastedNewsLazing("bbsNews", "bbs");
		newsCache.put("bannerNews", newsWrapper(bannerNews));
		newsCache.put("activityNews", newsWrapper(activityNews));
		newsCache.put("bbsNews", newsWrapper(bbsNews));
		return newsCache;
	}
	
	// -----------------------
	// Support
	// -----------------------
	
	/**
	 * 将新闻的相关信息比如发送详情，发贴人等信息都填充到其中
	 */
	private List<News> newsWrapper(List<News> newsList) {
		for (News news : newsList) {
			news.setPost(postService.getPostByIdLazing(news.getPostId()));
		}
		return newsList;
	}
	
	/**
	 * 查询最新的新闻，并放入到 Redis 缓存中。使用懒加载，只会加载 Post 字段中的日期属性。
	 */
	private Map<String, List<News>> listLastedNewsLazing() {
		Map<String, List<News>> newsCache = new HashMap<String, List<News>> (4);
		List<News> bannerNews = newsDao.listLastedNews("banner");
		List<News> activityNews = newsDao.listLastedNews("activity");
		List<News> bbsNews = newsDao.listLastedNews("bbs");
		newsCache.put("bannerNews", bannerNews);
		newsCache.put("activityNews", activityNews);
		newsCache.put("bbsNews", bbsNews);
		return newsCache;
	}
	
	/**
	 * <p>查询缓存中指定缓存键的新闻数据，一般都是首页上每天定时更新的新闻信息。并将获取到的新闻同步到一级缓存和
	 * 二级缓存中。</p>
	 * 
	 * @param cacheClassify - 新闻数据对应的指定缓存键
	 * @param classify - 新闻的类型
	 */
	@SuppressWarnings("unchecked")
	private List<News> listLastedNewsLazing(String cacheClassify, String classify) {
		List<News> news =  (List<News>) CacheUtils.get(CacheUtils.NEWS_CACHE, cacheClassify);
		if(news == null || news.size() != 5) {
			news =  (List<News>) JedisUtils.getObject(cacheClassify, RedisIndex.NEWS_CACHE);
			if(news == null || news.size() != 5) {
				news = newsDao.listLastedNews(classify);
				CacheUtils.put(CacheUtils.NEWS_CACHE, cacheClassify, news);
				JedisUtils.setObject(cacheClassify, news, 86400, RedisIndex.NEWS_CACHE);
			} else {
				CacheUtils.put(CacheUtils.NEWS_CACHE, cacheClassify, news);
			}
		}
		return news;
	}
	
	/**
	 * <p>使用 Spring Task 定时更新 Redis 中缓存的网站新闻数据。一般来说，网站的新闻数据每天会变化一次变化，所以
	 * 将每天的凌晨两点钟设置为新闻更新时间点。以保证受影响的用户较少。</p>
	 */
	@Scheduled(cron = "0 0 2 * * ?")
	public void scheduUpdateNewsCache() {
		Map<String, List<News>> newsCache = listLastedNewsLazing();
		JedisUtils.setObject("newsCache", newsCache, 24 * 3600);
	}
	
}
