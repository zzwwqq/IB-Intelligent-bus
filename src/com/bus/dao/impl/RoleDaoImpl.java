package com.bus.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bus.dao.DaoFactory;
import com.bus.dao.RoleDao;
import com.bus.dao.model.Role;

public class RoleDaoImpl implements RoleDao{

	@Override
	public List<Role> findAllRole() {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select roleCode,roleName from role ";
		List<Role> roles = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Role role = new Role();
				role.setRoleCode(resultSet.getString(1));
				role.setRoleName(resultSet.getString(2));
				roles.add(role);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return roles;
	}
	

}
