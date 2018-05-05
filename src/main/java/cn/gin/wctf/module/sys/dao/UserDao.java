package cn.gin.wctf.module.sys.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import cn.gin.wctf.module.sys.entity.Role;
import cn.gin.wctf.module.sys.entity.User;

@MapperScan
public interface UserDao {

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的 ID 查询对应的用户的所有信息</p>
	 *
	 * @param userId - 指定的用户 ID
	 * @return 指定 ID 对应的用户
	 */
	User getUserById(Integer userId);

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的 ID 查询对应的用户的部分可选信息（基本信息，不包含验证、月签到表、账号等）</p>
	 *
	 * @param userId - 指定的用户 ID
	 * @return 指定 ID 对应的用户
	 */
	User getUserByIdOptional(Integer userId);

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的账号查询对应的用户</p>
	 *
	 * @param account - 指定的账号
	 * @return 指定账号对应的用户
	 */
	User getUserByAccount(String account);

	/**
     * <p>CRUD - R</p>
     * <p>通过指定的账号和密码查询对应的用户</p>
     *
     * @param account - 指定的账号
     * @param password - 密码
     * @return 指定账号对应的用户
     */
    User getUserByAccountAndPassword(String account, String password);

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的账号查询对应的用户是否存在</p>
	 *
	 * @param account - 指定的账号
	 * @return 指定账号对应的用户数量（大于等于一即为已存在）
	 */
	int existsByAccount(String account);

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的 ID 查询对应用户的所有角色信息（授权）</p>
     *
	 * @param userId - 指定的用户 ID
	 * @return 指定用户 ID 对应的用户权限列表
	 */
	List<Role> listRoleByUserId(Integer userId);

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的 ID 查询对应用户的月签到表</p>
	 *
	 * @param userId - 指定的用户 ID
	 * @return 指定用户 ID 对应的用户月签到表
	 */
	String getPunchByUserId(Integer userId);

	/**
     * <p>CRUD - R</p>
     * <p>查询符合条件的用户的数量</p>
     *
     * @param user - 包含需要查询的条件
     * @return
     */
    int count(User user);

    /**
     * <p>CRUD - R</p>
     * <p>查询符合条件的已删除用户的数量</p>
     *
     * @param user - 包含需要查询的条件
     * @return
     */
    int countDelete(User user);

    /**
     * <p>CRUD - R</p>
     * <p>查询符合条件的用户列表</p>
     *
     * @param user - 包含需要查询的条件
     * @return
     */
    List<User> list(User user);

    /**
     * <p>CRUD - R</p>
     * <p>查询符合条件的已删除用户的用户列表</p>
     *
     * @param user - 包含需要查询的条件
     * @return
     */
    List<User> listDelete(User user);

	/**
	 * <p>CRUD - C</p>
	 * <p>插入一条新的用户信息</p>
	 *
	 * @param user - 新的用户信息
	 * @return 受 DML 影响的行数
	 */
	int saveUser(User user);

	/**
	 * <p>CRUD - U</p>
	 * <p>更新用户的基本信息</p>
	 *
	 * @param client - 用户输入的基本信息
	 * @return 受 DML 影响的行数
	 */
	int updateUserSetting(User client);

	/**
	 * <p>CRUD - U</p>
	 * <p>更新指定 ID 对应的用户的头像信息</p>
	 *
	 * @param userId - 指定的用户 ID
	 * @param avatar - 更新后的用户头像信息
	 * @return 受 DML 影响的行数
	 */
	int updateUserAvatar(Integer userId, String avatar);

	/**
	 * <p>CRUD - U</p>
	 * <p>更新指定 ID 对应的用户的月签到表</p>
	 *
	 * @param punch - 更新后的月签到表
	 * @param userId - 指定用户
	 * @return 受 DML 影响的行数
	 */
	int updatePunchByUserId(String punch, Integer userId);

	/**
	 * <p>CRUD - U</p>
	 * <p>更新指定 ID 对应的用户的密码信息</p>
	 *
	 * @param userId - 指定的用户 ID
	 * @param password - 更新后的密码
	 * @param salt - 更新后的盐
	 * @return 受 DML 影响的行数
	 */
	int updateUserPassword(Integer userId, String password, String salt);

	/**
	 * <p>CRUD - U</p>
     * <p>删除指定 ID 对应的用户，逻辑删除，只是更新用户的状态。</p>
     *
	 * @param id - 需要删除的用户 ID
	 * @return 受 DML 影响的行数
	 */
    int deleteById(Integer id);
}