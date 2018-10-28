package com.bus.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bus.dao.DaoFactory;
import com.bus.dao.UserDao;
import com.bus.dao.model.User;

public class UserDaoImpl implements UserDao {

	//@Override
	/*public User findUserByName(String userName) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select code,loginName,password,name,phone,idCard,role,driving,status from sysuser where loginName =?";
		User user = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userName);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				user = new User();
				String code = resultSet.getString(0);
				String loginName = resultSet.getString(1);
				String password = resultSet.getString(2);
				String name = resultSet.getString(3);
				String phone = resultSet.getString(4);
				String idCard = resultSet.getString(5);
				String role = resultSet.getString(6);
				int driving = resultSet.getInt(7);
				String status = resultSet.getString(8);

				user.setCode(code);
				user.setLoginName(loginName);
				user.setPassword(password);
				user.setName(name);
				user.setPhone(phone);
				user.setIdCard(idCard);
				user.setRole(role);
				user.setDriving(driving);
				if("0".equals(status)) {
					user.setStatus("禁用");
				}else {
					user.setStatus("启用");
				}
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

		return user;
	}*/

	@Override
	public String findUserPasswordByName(String userName) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select password from sysuser where loginName =? and status=1";
		String password = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userName);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				password = resultSet.getString(1);
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
		return password;
	}

	@Override
	public List<String> findAuthorityListByName(String userName) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select permissionName from V_PERMISSION where loginName =?";
		List<String> AuthorityList = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, userName);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				AuthorityList.add(resultSet.getString(1));
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
		return AuthorityList;
	}

	@Override
	public List<User> find(User condition) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select code,loginName,password,name,phone,idCard,role,driving,status ,roleName "
					+ "from sysuser join role on sysuser.role = role.roleCode"
					+ " where loginName like ? and  role like ? and status like ?";
		List<User> users = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (condition.getName() != null && condition.getName().length() > 0) {
				preparedStatement.setString(1, "%" + condition.getName() + "%");
			} else {
				preparedStatement.setString(1, "%");
			}

			if (condition.getRole() != null && condition.getRole().length() > 0) {
				preparedStatement.setString(2, condition.getRole());
			} else {
				preparedStatement.setString(2, "%");
			}

			if (condition.getStatus() != null) {
				preparedStatement.setString(3, condition.getStatus());
			} else {
				preparedStatement.setString(3, "%");
			}

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				User user = new User();
				String code = resultSet.getString(1);
				String loginName = resultSet.getString(2);
				String password = resultSet.getString(3);
				String name = resultSet.getString(4);
				String phone = resultSet.getString(5);
				String idCard = resultSet.getString(6);
				String role = resultSet.getString(10)+"["+resultSet.getString(7)+"]";
				int driving = resultSet.getInt(8);
				String status = resultSet.getString(9);

				user.setCode(code);
				user.setLoginName(loginName);
				user.setPassword(password);
				user.setName(name);
				user.setPhone(phone);
				user.setIdCard(idCard);
				user.setRole(role);
				user.setDriving(driving);
				if("0".equals(status)) {
					user.setStatus("禁用");
				}else {
					user.setStatus("启用");
				}
				users.add(user);
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
		return users;
	}

	@Override
	public void changePass(String userCode, String pass) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "update sysuser set password = ? where loginName =?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, pass);
			preparedStatement.setString(2, userCode);
			preparedStatement.executeUpdate();
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
	}

	@Override
	public void save(User user) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "insert into sysuser(loginName,password,name,phone,idCard,role,driving,status)"
				+ " values(?,?,?,?,?,?,?,?)";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getLoginName());
			preparedStatement.setString(2, user.getPassword());
			preparedStatement.setString(3,user.getName());
			preparedStatement.setString(4, user.getPhone());
			preparedStatement.setString(5, user.getIdCard());
			preparedStatement.setString(6, user.getRole());
			preparedStatement.setInt(7, user.getDriving());
			preparedStatement.setString(8, user.getStatus());
			preparedStatement.executeUpdate();
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
	}

	@Override
	public void update(User user) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "update sysuser set loginName = ?, name = ?, phone = ?, "
				            + "idCard = ?, role = ?, driving = ?, status = ? "
				    + "where code = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getLoginName());
			preparedStatement.setString(2,user.getName());
			preparedStatement.setString(3, user.getPhone());
			preparedStatement.setString(4, user.getIdCard());
			preparedStatement.setString(5, user.getRole());
			preparedStatement.setInt(6, user.getDriving());
			preparedStatement.setString(7, user.getStatus());
			preparedStatement.setString(8, user.getCode());
			preparedStatement.executeUpdate();
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
	}

	@Override
	public void dropUser(User user) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "delete from  sysuser where code = ?";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, user.getCode());
			preparedStatement.executeUpdate();
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
	}
}
