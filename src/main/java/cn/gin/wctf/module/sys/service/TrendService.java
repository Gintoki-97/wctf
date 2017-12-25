package cn.gin.wctf.module.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gin.wctf.module.sys.dao.TrendDao;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.Trend;
import cn.gin.wctf.module.sys.entity.TrendSupport;
import cn.gin.wctf.module.sys.entity.User;

/**
 * <p>用户动态业务类。</p>
 * 
 * @author Gintoki
 * @version 2017-12-24
 */
@Service
public class TrendService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TrendDao trendDao;
	
	
	
	// ----------------------
	// Service
	// ----------------------
	
	/**
	 * <p>获取用户的用户动态接口，会根据分页数据进行懒加载。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$Integer$userId - 页面传入的需要获取的用户动态所属的用户ID
	 * @param data$Integer$index - 页面传入的当前页
	 */
	public void listTrendByUserId(ApplicationDataSupport data) {
		Integer userId = null;
		Integer index = null;
		try {
			userId = data.getParameter("userId");
			index = data.getParameter("index");
			if(userId == null || index == null || userId <= 0) {
				throw new IllegalArgumentException();
			}
		} catch(Exception e) {
			data.setStatus(281);
			data.setMsg("数据准备失败");
			data.clearParams();
			return;
		}
		data.clearParams();
		if(index <= 0)	index = 1;
		int count = countTrendByUserId(userId);
		int pages = count % 5 == 0 ? count / 5 : (count / 5 + 1);
		if(count > 0) {
			int start = (index - 1) * 5;
			List<Trend> trends = listTrendByUserId(userId, start, 5);
			for (Trend trend : trends) {
				trend = trendWrapper(trend);
			}
			data.setMsg("获取用户动态成功");
			data.setParameter("trends", trends);
		} else {
			data.setMsg("该用户最近没有什么动态信息");
		}
		data.setSuccess(true);
		data.setParameter("pages", pages);
	}
	
	public boolean saveTrend(TrendSupport trend) {
		Trend t = trend.buildTrend();
		if(t == null)
			return false;
		return trendDao.saveTrend(t) == 1;
	}
	
	public Trend getTrendById(Integer trendId) {
		return trendDao.getTrendById(trendId);
	}
	
	public List<Trend> listTrendByUserId(Integer userId) {
		return trendDao.listTrendByUserId(userId);
	}
	
	private List<Trend> listTrendByUserId(Integer userId, int start, int n) {
		return trendDao.listTrendByUserIdPaging(userId, start, n);
	}

	private int countTrendByUserId(Integer userId) {
		return trendDao.countTrendByUserId(userId);
	}

	
	// ----------------------
	// Private
	// ----------------------
	
	private Trend trendWrapper(Trend trend) {
		int userId = trend.getUserId();
		if(userId == 0)
			return null;
		User user = userService.getUserByIdOptional(userId);
		trend.setNickname(user.getNickname());
		trend.setHeader(user.getHeader());
		return trend;
	}
	
}
