package cn.gin.wctf.module.sys.dao;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import cn.gin.wctf.module.sys.entity.Role;

@MapperScan
public interface RoleDao {

	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的角色 ID 查询对应角色的角色名</p>
	 * 
	 * @param roleId - 指定的角色 ID
	 * @return 指定 ID 对应的角色信息封装
	 */
	Role getRoleById(Integer roleId);
	
	/**
	 * <p>CRUD - R</p>
	 * <p>通过指定的用户 ID 查询对应用户的所有角色</p>
	 * 
	 * @param userId - 指定的用户 ID
	 * @return 指定 ID 对应的用户的角色列表
	 */
	List<Role> getRolesByUserIdId(Integer userId);

}