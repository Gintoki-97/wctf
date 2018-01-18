package cn.gin.wctf.module.sys.service;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cn.gin.wctf.common.ansy.AnsyTaskManager;
import cn.gin.wctf.common.ansy.task.AnsyTaskWithNoResult;
import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.util.CacheUtils;
import cn.gin.wctf.common.util.DateUtils;
import cn.gin.wctf.common.util.JCodec;
import cn.gin.wctf.common.util.JedisUtils;
import cn.gin.wctf.common.util.RedisIndex;
import cn.gin.wctf.module.sys.dao.UserDao;
import cn.gin.wctf.module.sys.entity.ApplicationDataSupport;
import cn.gin.wctf.module.sys.entity.User;

/**
 * <p>用户业务类</p>
 * 
 * @author Gintoki
 * @version 2017-10-03
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private AnsyTaskManager ansyTaskManager;
	
	// ----------------------------
	// Service
	// ----------------------------

	/**
	 * <p>用户注册，用户在注册页面填写完基本信息并提交时，会触发对此业务方法的调用。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$User$client - 页面传人的注册信息封装，是一个瞬时态的实体对象
	 */
	public void userRegister(ApplicationDataSupport data) {
		// 数据准备
		User client = null;
		try {
			client = data.getParameter("client");
		} catch (Exception e) {
			data.setStatus(151);
			data.setMsg("数据准备失败");
			data.clearParams();
			return;
		}
		if(userDao.existsByAccount(client.getAccount()) == 0) {
			// 业务处理
			client.setEmail(client.getAccount());
			client.setGender('0');
			String salt = JCodec.md5Salt();
			String pwd = client.getPassword();
			client.setSalt(salt);
			client.setPassword(JCodec.md5SaltEncode(pwd, salt));
			client.setLoginFalg("1");
			client.setHeaderOriginal("user/avatar/default.jpg");
			client.setSign("一句话签名");
			client.setReg(new Date());
			data.clearParams();
			if(userDao.saveUser(client) == 1) {
				data.setStatus(150);
				data.setMsg("注册成功");
				data.setParameter("account", client.getAccount());
				data.setParameter("password", pwd);
			} else {
				data.setStatus(153);
				data.setMsg("数据库请求出现异常");
			}
		} else {
			data.setStatus(152);
			data.setMsg("账号已存在");
			data.clearParams();
		}
	}
	
	/**
	 * <p>用户每日签到，用户在首页点击签到按钮时，会触发对此业务方法的调用。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$User$login - 当前 Session 中登录的用户
	 */
	public void userPunch(ApplicationDataSupport data) {
		// 数据准备
		User login = null;
		try {
			login = data.getParameter("login");
		} catch (Exception e) {
			data.setStatus(241);
			data.setMsg("数据准备失败");
			data.clearParams();
			return;
		}
		// 业务处理
		String punch = userDao.getPunchByUserId(login.getId());
		if(punch == null || "0".equals(punch)) {
			punch = "0000000000000000000000000000000";
		}
		char[] punchCharArray = punch.toCharArray();
		int day = Integer.valueOf(DateUtils.getDay());
		if(punchCharArray.length >= day) {
			punchCharArray[day - 1] = '1';
		}
		if(userDao.updatePunchByUserId(new String(punchCharArray), login.getId()) == 1) {
			data.setStatus(240);
			data.setMsg("签到成功");
			login.setPunchToday(DateUtils.getDay());
		} else {
			data.setStatus(242);
			data.setMsg("数据库请求出现异常");
		}
		data.clearParams();
		return;
	}
	
	/**
	 * <p>更新用户基本信息，用户在信息设置页面或管理员在信息管理页面进行信息修改时，会触发对此业务方法的调用。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$User$login - 当前 session 中保存的用户信息（修改前）
	 * @param data$User$client - 用户输入的新的信息的封装对象，是一个不完整的实体对象
	 */
	public void updateUserSetting(ApplicationDataSupport data) {
		// 数据准备
		User login = null;
		User client = null;
		try {
			login = data.getParameter("login");
			client = data.getParameter("client");
		} catch (Exception e) {
			data.setStatus(181);
			data.setMsg("数据准备异常");
			data.clearParams();
			return;
		}
		// 业务处理，数据覆盖
		client.setId(login.getId());
		client.setAccount(null);
		client.setLoginFalg(null);
		if(userDao.updateUserSetting(client) == 1) {
			login.setNickname(client.getNickname());
			login.setGender(client.getGender());
			login.setBirthday(client.getBirthday());
			login.setAddress(client.getAddress());
			login.setLocation(client.getLocation());
			login.setSign(client.getSign());
			data.setStatus(180);
			data.setMsg("信息修改成功");
		} else {
			data.setStatus(182);
			data.setMsg("数据库请求出现异常");
		}
		data.clearParams();
		return;
	}
	
	/**
	 * <p>更新用户头像，用户在信息设置页面或管理员在信息管理页面进行头像上传时，会触发对此业务方法的调用。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$login - 需要更新头像的用户
	 * @param data$avatar - 用户上传的头像
	 */
	public void uploadUserAvatar(ApplicationDataSupport data) {
		// 数据准备
		User login = null;
		MultipartFile avatar = null;
		try {
			login = data.getParameter("login");
			avatar = data.getParameter("avatar");
		} catch (Exception e) {
			data.setStatus(261);
			data.setMsg("数据准备失败");
			data.clearParams();
			return;
		}
		// 业务处理
		String originalFilename = avatar.getOriginalFilename();
		String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));
		String prefix = DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS");
		String avatarName = prefix + login.getId() + suffix;
		String avatarPath = Global.getConfig("userfiles.basedir") + "/avatar/" + avatarName;
		File file = new File(avatarPath);
		final String before = login.getHeader();
		try {
			avatar.transferTo(file);
		} catch (Exception e) {
			data.setStatus(262);
			data.setMsg("头像保存失败");
			data.clearParams();
			return;
		}
		String imgServer = "user/avatar/" + avatarName;
		login.setHeader(imgServer);
		if(userDao.updateUserAvatar(login.getId(), imgServer) == 1) {
			data.setStatus(260);
			data.setMsg("头像上传成功");
			data.clearParams();
			data.setParameter("url", imgServer);
			/*
			 * 删除用户之前的头像文件，优先级不高可以提交到低优先级的异步任务队列线程里执行。
			 */
			ansyTaskManager.submit(new AnsyTaskWithNoResult() {
				@Override
				public void execute() {
					String s = before;
					s = s.substring(s.lastIndexOf('/') + 1);
					String abs = Global.getConfig("userfiles.basedir") + "/avatar/" + s;
					File file = new File(abs);
					if(file.exists()) {
						file.delete();
					}
				}
			});
		} else {
			data.setStatus(263);
			data.setMsg("数据库请求出现异常");
			data.clearParams();
		}
		return;
	}

	
	/**
	 * <p>重置用户密码，用户在信息设置页面或管理员在信息管理页面进行密码信息的修改时，会触发对此业务方法的调用。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$User$login - 当前 Session 中登录的用户
	 * @param data$String$nowpass - 当前 Session 中登录的用户的原密码
	 * @param data$String$pass - 更新后的新密码
	 */
	public void resetPassword(ApplicationDataSupport data) {
		// 数据准备
		User login = null;
		String nowpass = null;
		String pass = null;
		try {
			login = data.getParameter("login");
			nowpass = data.getParameter("nowpass");
			pass = data.getParameter("pass");
		} catch (Exception e) {
			data.setStatus(201);
			data.setMsg("数据准备失败");
			data.clearParams();
			return;
		}
		// 业务处理
		String nowpassEncode = JCodec.md5SaltEncode(nowpass, login.getSalt());
		if(!login.getPassword().equals(nowpassEncode)) {
			data.setStatus(202);
			data.setMsg("原密码不正确");
		} else {
			String newSalt = JCodec.md5Salt();
			String passEncode = JCodec.md5SaltEncode(pass, newSalt);
			if(userDao.updateUserPassword(login.getId(), passEncode, newSalt) == 1) {
				data.setStatus(200);
				data.setMsg("密码修改成功");
				login.setPassword(passEncode);
				login.setSalt(newSalt);
			} else {
				data.setStatus(203);
				data.setMsg("数据库请求出现异常");
			}
		}
		data.clearParams();
		return;
	}
	
	
	// ----------------------------
	// Support
	// ----------------------------
		
	/**
	 * <p>通过指定的 ID 查询对应的用户的所有信息。</p>
	 * 
	 * @param userId - 指定的用户 ID
	 * @return 指定 ID 对应的用户所有信息的封装
	 */
	public User getUserById(Integer userId) {
		return userDao.getUserById(userId);
	}
		
	/**
	 * <p>通过指定的 ID 查询对应的用户的部分可选信息（基本信息，不包含验证、月签到表、账号等）。</p>
	 * 
	 * @param userId - 指定的用户 ID
	 * @return 指定 ID 对应的用户基本信息的封装
	 */
	public User getUserByIdOptional(Integer userId) {
		return userDao.getUserByIdOptional(userId);
	}
		
	/**
	 * <p>通过指定的账号查询对应的用户。</p>
	 * 
	 * @param account - 指定的账号
	 * @return 指定账号对应的用户
	 */
	public User getUserByAccount(String account) {
		return userDao.getUserByAccount(account);
	}

	/**
	 * <p>通过指定的账号查询对应的用户是否存在。</p>
	 * 
	 * @param account - 指定的账号
	 * @return 指定账号是否已经存在
	 */
	public boolean existsByAccount(String account) {
		return userDao.existsByAccount(account) == 1;
	}

	/**
	 * <p>通过指定的用户 ID 查询对应的用户的动态设置。</p>
	 * 
	 * @param userId - 指定的用户 ID
	 * @return 对应的用户的动态设置
	 */
	public String getUserTrendSetting(Integer userId) {
		String key = "user:" + userId + ":trend:setting";
		String trendStr = (String) CacheUtils.get(CacheUtils.USER_CACHE, key);
		if(trendStr == null || !trendStr.matches("[0|1]{4}")) {
			trendStr = JedisUtils.get(key, RedisIndex.USER_CACHE);
			if(trendStr == null || !trendStr.matches("\\d{4}")) {
				trendStr = "1111";
				CacheUtils.put(CacheUtils.USER_CACHE, key, "1111");
				JedisUtils.set(key, "1111", JedisUtils.ONE_DAY, RedisIndex.USER_CACHE);
			} else {
				CacheUtils.put(CacheUtils.USER_CACHE, key, trendStr);
			}
		}
		return trendStr;
	}
	
	/**
	 * <p>更新用户的动态设置，程序会根据用户的动态设置来显示指定类型的动态信息。</p>
	 * 
	 * @param data - 应用数据流载体
	 * @param data$Integer$userId - 由页面传入的用户  ID
	 * @param data$String$trendSetting - 由页面传入的用户设置信息，一般为 0/1 字符串
	 */
	public void trendSetting(ApplicationDataSupport data) {
		Integer userId = null;
		String trendSetting = null;
		try {
			userId = data.getParameter("userId");
			trendSetting = data.getParameter("trendSetting");
		} catch(Exception e) {
			data.setStatus(291);
			data.setMsg("数据准备失败");
		}
		String key = "user:" + userId + ":trend:setting";
		CacheUtils.put(CacheUtils.USER_CACHE, key, trendSetting);
		JedisUtils.set(key, trendSetting, RedisIndex.USER_CACHE);
		data.setStatus(290);
		data.setMsg("动态设置成功");
	}
}
