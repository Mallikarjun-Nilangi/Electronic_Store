package com.lcwd.elestronic.store.Dto;

public class RoleDto {

	private String roleId;
	
	private String roleName;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public RoleDto(String roleId, String roleName) {
		super();
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public RoleDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
