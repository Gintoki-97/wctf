package cn.gin.wctf.module.news.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import cn.gin.wctf.module.news.entity.News;

@MapperScan
public interface NewsDao {

	/**
	 * 获取指定分类的最新新闻，数量限制为5条。
	 * 
	 * @param classify 获取的新闻的所属分类
	 * @return 列表固定大小为5
	 * @see {@link cn.gin.wctf.module.news.entity.NewsClassify}
	 */
	List<News> listLastedNews(String classify);
	
}
