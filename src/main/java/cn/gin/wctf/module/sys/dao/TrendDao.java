package cn.gin.wctf.module.sys.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import cn.gin.wctf.module.sys.entity.Trend;

@MapperScan
public interface TrendDao {

	/**
	 * <p>CRUD - R</p>
	 * <p>统计函数，通过指定的 ID 查询对应的动态的数量</p>
	 * 
	 * @param userId - 指定的动态 ID
	 * @return 对应的动态的数量
	 */
	int countTrendByUserId(Integer userId);

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的 ID 查询对应的动态的所有信息</p>
	 * 
	 * @param trendId - 指定的动态 ID
	 * @return 对应的动态的所有信息
	 */
	Trend getTrendById(Integer trendId);
	
	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的用户 ID 查询对应的用户的所有的动态信息</p>
	 * 
	 * @param userId - 指定的用户 ID 
	 * @return 对应的用户的所有动态的信息
	 */
	List<Trend> listTrendByUserId(Integer userId);
	
	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的用户 ID 查询对应的用户的部分动态信息，查询多少则根据分页数据来决定。</p>
	 * 
	 * @param userId - 指定的用户 ID 
	 * @param start - 分页的起始位置
	 * @param n - 获取的动态信息的数目
	 * @return 对应的用户的所有动态的信息
	 */
	List<Trend> listTrendByUserIdPaging(Integer userId, int start, int n);

	/**
	 * <p>CRUD - C</p>
	 * <p>将一条用户动态保存到数据库中。</p>
	 * 
	 * @param trend - 用户在进行默写操作时程序生成的用户动态信息
	 */
	int saveTrend(Trend trend);

}
