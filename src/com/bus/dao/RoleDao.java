package com.bus.dao;

import java.util.List;

import com.bus.dao.model.Role;

//角色
public interface RoleDao {

	List<Role> findAllRole();
}
