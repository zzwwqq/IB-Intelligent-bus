package com.bus.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bus.dao.BusDao;
import com.bus.dao.DaoFactory;
import com.bus.dao.model.Bus;

public class BusDaoImpl implements BusDao {

	@Override
	public List<Bus> find(Bus condition) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select busCode,busLicense,busType,case busStatus "
				+ "when '1' then '启用' else '禁用' end,startTime "
				+ "from bus "
				+ "where busLicense like ? and busStatus like ?";
		List<Bus> busList = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if(condition.getBusLicense() != null && condition.getBusLicense().length()>0) {
				preparedStatement.setString(1, "%"+condition.getBusLicense()+"%");
			}else {
				preparedStatement.setString(1, "%");
			}
			if(condition.getBusStatus() != null && condition.getBusStatus().length() > 0) {
				if("2".equals(condition.getBusStatus())) {
					preparedStatement.setString(2, "%");
				}else {
					preparedStatement.setString(2, "%"+condition.getBusStatus()+"%");
				}
			}else {
				preparedStatement.setString(2, "%");
			}
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Bus bus = new Bus();
				bus.setBusCode(resultSet.getString(1));
				bus.setBusLicense(resultSet.getString(2));
				bus.setBusType(resultSet.getString(3));
				bus.setBusStatus(resultSet.getString(4));
				bus.setStartTime(resultSet.getString(5));
				busList.add(bus);
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
		return busList;
	}

	@Override
	public void updateBus(Bus bus) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "update bus set busLicense = ?,busType = ?,busStatus = ?,startTime = ? "
				+ "where busCode =?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, bus.getBusLicense());
		preparedStatement.setString(2, bus.getBusType());
		preparedStatement.setString(3,bus.getBusStatus());
		preparedStatement.setString(4, bus.getStartTime());
		preparedStatement.setString(5, bus.getBusCode());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
	}

	@Override
	public void dropBus(Bus bus) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "delete from  bus where busCode = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, bus.getBusCode());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
	}

	@Override
	public void saveBus(Bus bus) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "insert into bus(busLicense,busType,busStatus,startTime) values(?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, bus.getBusLicense());
		preparedStatement.setString(2, bus.getBusType());
		preparedStatement.setString(3,bus.getBusStatus());
		preparedStatement.setString(4, bus.getStartTime());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
	}

}
