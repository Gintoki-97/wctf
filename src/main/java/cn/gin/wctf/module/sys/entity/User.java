package cn.gin.wctf.module.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.validation.constraints.Pattern;

import com.google.common.collect.Sets;

import cn.gin.wctf.common.config.Global;
import cn.gin.wctf.common.util.CollectionUtils;
import cn.gin.wctf.common.util.DateUtils;
import cn.gin.wctf.common.util.StringUtils;
import cn.gin.wctf.common.validator.user.UserRegisterGroup;

/**
 * 用户的实体类
 * @author Gintoki
 * @version 2017-10-07
 */
public class User implements Serializable {
	
	private static final long serialVersionUID = -3926869456422226138L;
	
	private Integer id;			// 用户唯一标识
	
	@Pattern(regexp = UserRegisterGroup.ACCOUNT, message = "{user.reg.account}", groups = {UserRegisterGroup.class})
	private String account;		// 登录帐号
	
	@Pattern(regexp = UserRegisterGroup.NICKNAME, message = "{user.reg.account}", groups = {UserRegisterGroup.class})
	private String nickname;	// 昵称
	private String email;		// 邮箱，目前业务中等同帐号
	private Character gender;	// 性别
	
	@Pattern(regexp = UserRegisterGroup.PASSWORD, message = "{user.reg.password}", groups = {UserRegisterGroup.class})
	private String password;	// 加盐密码
	
	private transient String salt;		// 盐
	private String loginFlag;	// 可登录标识，账号是否可用
	private String header;		// 头像url
	private String sign;		// 简介
	private Date birthday;		// 生日
	private String punch;		// 月签到表
	private String address;		// 省市区地址
	private String location;	// 详细的街道地址
	private String auth;		// 认证信息
	private Date reg;			// 注册时间
	private Integer weight;		// 重量
	
	private String punchToday;	// 今天是否已签到
	private Role role;			//根据角色查询用户条件
	private Set<Role> roles = Sets.newHashSet(); 	//用户拥有的角色列表
	
	//
	public static transient final String DEFAULT_LOCATION = "000000";
	
	public User() {
		this.loginFlag = Global.YES;
	}
	
	public User(Integer id){
		this.id = id;
	}
	
	public User(String account, String password){
		this.account = account;
		this.password = password;
	}
	
	public User(Integer id, String loginAccount){
		this.id = id;
		this.account = loginAccount;
	}
	
	public User(Role role){
		this.role = role;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Character getGender() {
		return gender;
	}

	public void setGender(Character gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}
	
	public void setHeaderOriginal(String header) {
		this.header = header;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getPunch() {
		return punch;
	}
	
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public void setPunch(String punch) {
		if(StringUtils.isBlank(punch) || "0".equals(punch)) {
			punch = "0000000000000000000000000000000";
		}
		this.punch = punch;
		int day = Integer.valueOf(DateUtils.getDay());
		if(punch.charAt(day - 1) == '1') {
			setPunchToday(DateUtils.getDay());
		} else {
			setPunchToday("-1");
		}
	}

	public String getPunchToday() {
		return punchToday;
	}

	public void setPunchToday(String punchToday) {
		this.punchToday = punchToday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		address = (address == null || "0".equals(address)) ? "000000" : address;
		this.address = address;
	}
	
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public Date getReg() {
		return reg;
	}

	public void setReg(Date reg) {
		this.reg = reg;
	}
	
	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFalg(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}
	
	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
	public String getRoleNames() {
		return CollectionUtils.extractToString(roles, "name", ",");
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", account=" + account + "]";
	}

}
