package cn.gin.wctf.module.sys.entity;

import java.io.Serializable;

/**
 * 权限实体类
 * @author Gintoki
 * @version 2017-10-15
 */
public class Role implements Serializable {

	private static final long serialVersionUID = 2867819246905956443L;

	private Integer roleId;		// 权限唯一标识
	
	private String name;	// 权限名
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 47;
		int result = this.getClass().hashCode();
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		return true;
	}
	
	

}
