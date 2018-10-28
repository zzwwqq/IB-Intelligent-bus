package com.bus.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bus.dao.DaoFactory;
import com.bus.dao.SchedulingDao;
import com.bus.dao.model.Scheduling;

public class SchedulingDaoImpl implements SchedulingDao {

	@Override
	public List<Scheduling> find(Scheduling condition) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select scheduling.code,line.lineCode,tcNumber,tcTime,"
				             + "scheduling.userCode,startStation,endStation,busLicense,"
					         + " startStationTab.stationName, endStationTab.stationName, "
					         + "line.lineName,sysuser.loginName "
					+ " from scheduling "
					+ "join station as startStationTab on startStation = startStationTab.stationCode"
					+ " join station as endStationTab on endStation = endStationTab.stationCode"
					+ "	join line on line.lineCode = scheduling.lineCode"
					+ " join sysuser on sysuser.code = scheduling.userCode"
					+ " where busLicense like ? and line.lineName like ? "
					                         + "and startStationTab.stationName like ? "
					                         + "and endStationTab.stationName like ?";
		List<Scheduling> schedulings = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if(condition.getBusCode()!= null && condition.getBusCode().length()>0) {
				preparedStatement.setString(1, "%"+condition.getBusCode()+"%");
			}else {
				preparedStatement.setString(1, "%");
			}
			
			if(condition.getLineName() != null && condition.getLineName().length() > 0) {
				preparedStatement.setString(2, "%"+condition.getLineName()+"%");
			}else {
				preparedStatement.setString(2, "%");
			}
			
			if(condition.getStartStation() !=null && condition.getStartStation().length() >0) {
				preparedStatement.setString(3, "%"+condition.getStartStation()+"%");
			}else {
				preparedStatement.setString(3, "%");
			}
			
			if(condition.getEndStation() != null && condition.getEndStation().length() >0) {
				preparedStatement.setString(4, "%"+condition.getEndStation()+"%");
			}else {
				preparedStatement.setString(4, "%");
			}
			
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Scheduling scheduling = new Scheduling();
				scheduling.setCode(resultSet.getString(1));
				scheduling.setLineCode(resultSet.getString(2));
				scheduling.setTcNumber(resultSet.getString(3));
				scheduling.setTcTime(resultSet.getString(4));
				scheduling.setUserCode(resultSet.getString(5));
				scheduling.setStartStation(resultSet.getString(9)+"["+resultSet.getString(6)+"]");
				scheduling.setEndStation(resultSet.getString(10)+"["+resultSet.getString(7)+"]");
				scheduling.setBusCode(resultSet.getString(8));
				scheduling.setLineName(resultSet.getString(11));
				scheduling.setUserName(resultSet.getString(12));
				schedulings.add(scheduling);
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
		return schedulings;
	}

	@Override
	public void updateScheduling(Scheduling scheduling) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "update scheduling set lineCode = ?,tcNumber = ? ,tcTime = ? ,"
				+ "userCode = ?,startStation = ?,endStation = ?,busLicense = ? "
				+ "where code = ?  ";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, scheduling.getLineCode());
		preparedStatement.setString(2,scheduling.getTcNumber());
		preparedStatement.setString(3,scheduling.getTcTime());
		preparedStatement.setString(4, scheduling.getUserCode());
		preparedStatement.setString(5, scheduling.getStartStation());
		preparedStatement.setString(6, scheduling.getEndStation());
		preparedStatement.setString(7, scheduling.getBusCode());
		preparedStatement.setString(8, scheduling.getCode());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
	}

	@Override
	public void dropScheduling(Scheduling scheduling) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "delete from  scheduling where code = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, scheduling.getCode());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
	}

	@Override
	public void saveScheduling(Scheduling scheduling) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "insert into scheduling(lineCode,tcNumber,tcTime,userCode,"
				+ "startStation,endStation,busLicense) "
				+ "values(?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, scheduling.getLineCode());
		preparedStatement.setString(2,scheduling.getTcNumber());
		preparedStatement.setString(3,scheduling.getTcTime());
		preparedStatement.setString(4, scheduling.getUserCode());
		preparedStatement.setString(5, scheduling.getStartStation());
		preparedStatement.setString(6, scheduling.getEndStation());
		preparedStatement.setString(7, scheduling.getBusCode());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();

	}
}
