package cn.gin.wctf.module.resource.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import cn.gin.wctf.common.ansy.AnsyTaskManager;
import cn.gin.wctf.common.ansy.task.AnsyTaskWithNoResult;
import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.paging.Page;
import cn.gin.wctf.common.util.CacheUtils;
import cn.gin.wctf.common.util.FileUtils;
import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.util.RedisIndex;
import cn.gin.wctf.common.web.Servlets;
import cn.gin.wctf.module.resource.dao.ResourceDao;
import cn.gin.wctf.module.resource.entity.Resource;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.User;
import cn.gin.wctf.module.sys.service.TrendService;
import cn.gin.wctf.module.sys.service.UserService;

/**
 * <p>资源业务类</p>
 * 
 * @author Gintoki
 * @version 2017-11-22
 */
@Service
public class ResourceService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private TrendService trendService;
	
	@Autowired
	private ResourceDao resourceDao;
	
	@Autowired
	private AnsyTaskManager ansyTaskManager;
	
	private static final Map<Integer, String> resClassify = Maps.newHashMapWithExpectedSize(8);
	
	/*
	 * Prepared resource classify.
	 */
	static {
		resClassify.put(0, "全部");
		resClassify.put(1, "规范");
		resClassify.put(2, "开发");
		resClassify.put(3, "黑客");
		resClassify.put(4, "安全");
	}
	
	
	// -----------------
	// Service
	// -----------------
	
	public Resource getResById(Integer resId) {
		if(resId != null && resId > 0) {
			Resource res = resourceDao.getResById(resId);
			// 更新用户信息
			res.setUser(userService.getUserByIdOptional(res.getUserId()));
			// 返回数据
			return res;
		}
		return null;
	}
	
	/**
	 * <p>获取指定分类的所有资源，用户在首页或分类查看资源页面进行查看指定类型的资源请求时，会请求此业务方法处理。</p>
	 * 
	 * @param cls - 指定分类
	 * @param index - 分页对象，如果为 null 表示不需要分页
	 * @return 指定分类的所有资源
	 */
	@SuppressWarnings("unchecked")
	public Page<Resource> listResByClassify(String cls, Integer index) {
		Page<Resource> paging = null;
		// 查询缓存
		if("all".equalsIgnoreCase(cls) && index == 1) {
			paging = (Page<Resource>) CacheUtils.get("indexResource");
			if(paging == null || paging.getList() == null || paging.getList().isEmpty()) {
				paging = (Page<Resource>) JedisUtils.getObject("indexResource", RedisIndex.SYSTEM_CACHE);
				if(paging != null && paging.getList() == null && !paging.getList().isEmpty()) {
					CacheUtils.put("indexResource", paging);
				}
			}
			if(paging != null && paging.getList() == null && !paging.getList().isEmpty()) {
				return paging;
			}
		}
		// 查询数据库
		paging = new Page<Resource>();
		if(index == null || index <= 1) {
			paging.setIndex(1);
		} else {
			paging.setIndex(index);
		}
		int clsInt = toClassify(cls);
		if(clsInt == 0) {
			paging.setCount(resourceDao.countRes());
		} else {	
			paging.setCount(resourceDao.countResByClassify(clsInt));
		}
		paging.calculate();
		// 根据分页对象从数据库中获取
		List<Resource> resList = null;
		if(clsInt == 0) {
			resList = resourceDao.listRes((paging.getIndex() - 1) * paging.getLimit(), paging.getLimit());
		} else {	
			resList = resourceDao.listResByClassify(clsInt, (paging.getIndex() - 1) * paging.getLimit(), paging.getLimit());
		}
		for (Resource res : resList) {
			res.setUser(userService.getUserByIdOptional(res.getUserId()));
		}
		paging.setList(resList);
		// 更新缓存
		if("all".equalsIgnoreCase(cls) && index == 1) {
			CacheUtils.put("indexResource", paging);
			JedisUtils.setObject("indexResource", paging, null, RedisIndex.SYSTEM_CACHE);
		}
		return paging;
	}
	
	/**
	 * 
	 * @param items
	 * @return
	 */
	public Page<Resource> searchResByItem(int[] items, Integer index) {
		Page<Resource> paging = new Page<Resource>();
		if(index == null || index <= 1) {
			paging.setIndex(1);
		} else {
			paging.setIndex(index);
		}
		// 标签信息
		if(items[0] < 0 || items[0] > 4) {items[0] = 0;}
		String tag = items[0] == 0 ? null : resClassify.get(items[0]);
		// 分类信息
		Integer cls = formatClassify(items[1]);
		cls = cls == 0 ? null : cls;
		// 排序信息
		if(items[2] < 0 || items[2] > 1) {items[2] = 0;}
		Integer order = items[2] == 0 ? null : 1;
		Resource res = new Resource();
		res.setItemTag(tag == null ? null : ("%" + tag + "%"));
		res.setItemCls(cls);
		// 统计数量
		paging.setCount(resourceDao.countResByItem(res));
		paging.calculate();
		// 根据分页对象从数据库中获取
		res.setItemOrder(order);
		res.setItemStart((paging.getIndex() - 1) * paging.getLimit());
		res.setItemLimit(paging.getLimit());
		List<Resource> resList = resourceDao.listResByItem(res);
		// 填充用户信息
		for (Resource r : resList) {
			r.setUser(userService.getUserByIdOptional(r.getUserId()));
		}
		paging.setList(resList);
		// 返回数据
		return paging;
	}
	
	/**
	 * <p>判断当前资源 ID 对应的资源是否存在。</p>
	 * 
	 * @param resId - 资源 ID
	 * return 资源是否存在
	 */
	public boolean existsResById(Integer resId) {
		if(resId == null || resId <= 0)
			return false;
		return getResById(resId) != null;
	}
	
	/**
	 * <p>资源文件的下载请求，会请求此业务方法进行处理。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$Integer$resId - 需要下载的资源文件的 ID
	 * @param data$User$login - 发起下载请求的用户
	 */
	public void downloadRes(ApplicationDataSupport data) {
		Integer resId = null;
		User login = null;
		HttpServletResponse resp = null;
		try {
			resId = data.getParameter("resId");
			login = data.getParameter("login");
			resp = data.getParameter("resp");
		} catch(Throwable t) {
			data.setStatus(12);
		}
		Resource res = getResById(resId);
		if(res == null) {
			data.setStatus(511);
			data.setMsg("请求的资源文件不存在");
		} else {
			String prefix = Global.getConfig("userfiles.basedir");
			String suffix = res.getPath();
			String extension = suffix.substring(suffix.lastIndexOf("."));
			FileInputStream fis = null;
			try {
				fis = FileUtils.openInputStream(new File(prefix + suffix));
				if(fis != null) {
					// 填充响应头
					Servlets.setFileDownloadHeader(resp, res.getName() + extension);
					FileUtils.transferStream(fis, resp.getOutputStream());
				} else {
					throw new IOException();
				}
				// TDOO Add user download infos
				final Resource r = res;
				ansyTaskManager.submit(new AnsyTaskWithNoResult() {
					@Override
					public void execute() {
						// 更新文件下载信息
						updateResDownloads(r.getId());
						// 保存用户下载动态
						trendService.saveTrend(r);
					}
				});
				login.toString();
			} catch(Throwable t) {
				data.setStatus(512);
				data.setMsg("Download resource request, but its occured error in stream transfer.");
			} finally {
				IOUtils.closeQuietly(fis);
			}
		}
	}
	
	// -----------------
	// Private
	// -----------------
	
	/**
	 * <p>将资源分类转换为对应的字符串。</p>
	 * 
	 * @param classify - 需要被转换的资源分类，是一个整型表示
	 * @return 指定资源分类对应的<code> View </code>表示
	 */
	public String fromClassify(Integer classify) {
		if(classify == null) {
			return "all";
		}
		switch (classify) {
			case 1:
				return "doc";
			case 2:
				return "code";
			case 3:
				return "tool";
			case 4:
				return "other";
			default:
				return "all";
		}
	}
	
	/**
	 * <p>将资源分类字符串转换为对应的资源分类。</p>
	 * 
	 * @param classify - 需要被转换的资源分类，是一个字符串表示
	 * @return 指定资源分类对应的<code> MODEL </code>表示
	 */
	public int toClassify(String classify) {
		if(classify == null) {
			return 0;
		}
		classify = classify.toLowerCase();
		switch (classify) {
			case "doc":
				return 1;
			case "code":
				return 2;
			case "tool":
				return 3;
			case "other":
				return 4;
			default:
				return 0;
		}
	}
	
	/**
	 * <p>将资源分类格式化为标准的的资源分类。</p>
	 * 
	 * @param classify - 需要被格式化的资源分类，防止错误数据
	 * @return 指定资源分类对应的<code> MODEL </code>表示
	 */
	public int formatClassify(Integer classify) {
		if(classify == null) {
			return 0;
		}
		switch (classify) {
			case 1:
				return 1;
			case 2:
				return 2;
			case 3:
				return 3;
			case 4:
				return 4;
			default:
				return 0;
		}
	}
	
	/**
	 * <p>在对指定资源文件进行下载请求时，更新资源文件的下载信息。</p>
	 * 
	 * @param resId - 指定的资源文件 ID
	 */
	private void updateResDownloads(Integer resId) {
		resourceDao.updateResDownloads(resId);
	}

}
