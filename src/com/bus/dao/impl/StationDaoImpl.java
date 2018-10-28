package com.bus.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bus.dao.DaoFactory;
import com.bus.dao.StationDao;
import com.bus.dao.model.Station;

public class StationDaoImpl implements StationDao {

	@Override
	public List<Station> find(Station condition) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select stationCode,stationName,longitude,latitude,region,street "
				+ "from station "
				+ "where stationName like ? and region like ? and street like ?";
		List<Station> stations = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			if(condition.getStationName() !=null && condition.getStationName().length() >0) {
				preparedStatement.setString(1, "%"+condition.getStationName()+"%");
			}else {
				preparedStatement.setString(1, "%");
			}
			if(condition.getRegion() !=null && condition.getRegion().length() >0) {
				preparedStatement.setString(2, "%"+condition.getRegion()+"%");
			}else {
				preparedStatement.setString(2, "%");
			}
			if(condition.getStreet() !=null && condition.getStreet().length() >0) {
				preparedStatement.setString(3, "%"+condition.getStreet()+"%");
			}else {
				preparedStatement.setString(3, "%");
			}

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Station  station = new Station();
				station.setStationCode(resultSet.getString(1));
				station.setStationName(resultSet.getString(2));
				station.setLongitude(resultSet.getString(3));
				station.setLatitude(resultSet.getString(4));
				station.setRegion(resultSet.getString(5));
				station.setStreet(resultSet.getString(6));
				
				station.setStationName(station.getStationName()+"["+station.getStationCode()+"]");
				
				stations.add(station);
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
		return stations;
	}

	@Override
	public void updateStation(Station station) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "update station set stationName =? ,longitude= ?,latitude= ?,"
				+ "region=? ,street=?  "
				+ "where stationCode =?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, station.getStationName());
		preparedStatement.setString(2, station.getLongitude());
		preparedStatement.setString(3, station.getLatitude());
		preparedStatement.setString(4, station.getRegion());
		preparedStatement.setString(5, station.getStreet());
		preparedStatement.setString(6, station.getStationCode());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
	}

	@Override
	public void dropStation(Station station) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "delete from  station where stationCode =?";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, station.getStationCode());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
	}

	@Override
	public void saveStation(Station station) throws SQLException{
		Connection connection = DaoFactory.getConnect();
		String sql = "insert into station(stationName,longitude,latitude,region,street) "
				+ "values(?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setString(1, station.getStationName());
		preparedStatement.setString(2, station.getLongitude());
		preparedStatement.setString(3,station.getLatitude());
		preparedStatement.setString(4, station.getRegion());
		preparedStatement.setString(5,station.getStreet());
		preparedStatement.executeUpdate();
		preparedStatement.close();
		connection.close();
	}
}
