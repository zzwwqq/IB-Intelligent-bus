package com.bus.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.bus.dao.DaoFactory;
import com.bus.dao.LineDao;
import com.bus.dao.model.Line;

public class LineDaoImpl implements LineDao{

	@Override
	public List<Line> find(Line condition) {
		Connection connection = DaoFactory.getConnect();
		PreparedStatement preparedStatement = null;
		String sql = "select line.lineCode,lineName, case status "
				+ "when '0' then '禁用' else '启用' end,startLineTime,"+ "case direction "
				+ "when '0' then '下行' else '上行' end,stationName,line_station_ref.stationCode "
				+ "from line "
				+ " join line_station_ref  on line.lineCode = line_station_ref.lineCode"
				+ " join  station on station.stationCode = line_station_ref.stationCode"
				+ " where lineName like ? and  direction like ? and status like ?  "
				+ "order by line.lineCode, stationOrder";
		List<Line> lines = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(sql);
			
			if (condition.getLineName() != null) {
				preparedStatement.setString(1, "%" + condition.getLineName() + "%");
			} else {
				preparedStatement.setString(1, "%");
			}
			if (condition.getDirection() != null) {
				if("2".equals(condition.getDirection())) {
					preparedStatement.setString(2, "%");
				}else {
					preparedStatement.setString(2, "%" + condition.getDirection() + "%");
				}
			} else {
				preparedStatement.setString(2, "%");
			}
			if (condition.getStatus() != null) {
				if("2".equals(condition.getStatus())) {
					preparedStatement.setString(3, "%");
				}else {
					preparedStatement.setString(3, "%" + condition.getStatus() + "%");
				}
			} else {
				preparedStatement.setString(3, "%");
			}

			ResultSet resultSet = preparedStatement.executeQuery();
			String currentLineCode="";
			Line line= new Line();
			while (resultSet.next()) {
				String linecode = resultSet.getString(1);
				String stationInfo = resultSet.getString(6)+"["+resultSet.getString(7)+"]";
				if(!currentLineCode.equals(linecode)) {
					line = new Line();
					line.setLineCode(resultSet.getString(1));
					line.setLineName(resultSet.getString(2));
					line.setStatus(resultSet.getString(3));
					line.setStartLineTime(resultSet.getString(4));
					line.setDirection(resultSet.getString(5));
					line.setAllStation(stationInfo);
					lines.add(line);
					currentLineCode = linecode;
				}else {
					line.setAllStation(line.getAllStation()+","+stationInfo);
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
		return lines;
	}

	@Override
	public void updateLine(Line line) throws SQLException{
		// 获取数据库连接
		Connection connection = DaoFactory.getConnect();
		// 更新线路sql
		String sqlUpdateLine = "update line set lineName=?, status=?, startLineTime=?, direction=? "
				               + "where lineCode = ?";
		// 删除线路站点关联sql
		String sqlDelRef = "delete from line_station_ref "
				             + "where lineCode = ?";
		// 添加线路站点关联sql
		String sqlSaveRef = "insert into line_station_ref(lineCode, stationCode, stationOrder)"
				+ " values (?, ?, ?)";
		PreparedStatement psUpdateLine = connection.prepareStatement(sqlUpdateLine);
		PreparedStatement psDelRef = connection.prepareStatement(sqlDelRef);
			
		// 更新线路
		psUpdateLine.setString(1, line.getLineName());
		psUpdateLine.setString(2, line.getStatus());
		psUpdateLine.setString(3, line.getStartLineTime());
		psUpdateLine.setString(4, line.getDirection());
		psUpdateLine.setString(5, line.getLineCode());
		psUpdateLine.executeUpdate();
		
		// 删除关联关系
		psDelRef.setString(1, line.getLineCode());
		psDelRef.executeUpdate();
		
		// 重建关联关系
		String[] stationCodes = line.getAllStation().split(",");
		for(int i=0; i<stationCodes.length; i++) {
			PreparedStatement psSaveRef = connection.prepareStatement(sqlSaveRef);
			psSaveRef.setString(1, line.getLineCode());
			psSaveRef.setString(2, stationCodes[i]);
			psSaveRef.setInt(3, i);
			psSaveRef.executeUpdate();
			psSaveRef.close();
		}
		psDelRef.close();
		psUpdateLine.close();
		connection.close();
	}

	@Override
	public int dropLine(Line line) throws SQLException {
		Connection connection = DaoFactory.getConnect();
		String sql = "{CALL PRO_DEL_LINE(?,?)}"; //调用存储过程 
		CallableStatement cstm = connection.prepareCall(sql);
		//实例化对象cstm 
		cstm.setString(1, line.getLineCode()); 
		cstm.registerOutParameter(2, Types.VARCHAR); // 设置返回值类型 
		cstm.execute(); // 执行存储过程 
		int result = cstm.getInt(2);
		connection.close();
		return result; 
	}

	@Override
	public void saveLine(Line line) throws SQLException{
		// 获取数据库连接
		Connection connection = DaoFactory.getConnect();
		// 更新线路sql
		String sqlUpdateLine = "insert into line(lineName, status, startLineTime, direction)"
				                 + " values (?, ?, ?, ?)";
		// 查询刚刚插入的线路code sql
		String sqlSelMaxCode = "select max(lineCode) from line";
		// 添加线路站点关联sql
		String sqlSaveRef = "insert into line_station_ref(lineCode, stationCode, stationOrder)"
				+ " values (?, ?, ?)";
		PreparedStatement psUpdateLine = connection.prepareStatement(sqlUpdateLine);
		PreparedStatement psSelMaxCode = connection.prepareStatement(sqlSelMaxCode);
			
		// 插入线路
		psUpdateLine.setString(1, line.getLineName());
		psUpdateLine.setString(2, line.getStatus());
		psUpdateLine.setString(3, line.getStartLineTime());
		psUpdateLine.setString(4, line.getDirection());
		psUpdateLine.executeUpdate();
		
		// 查询刚刚插入的线路code
		String lineCode = null;
		ResultSet rs = psSelMaxCode.executeQuery();
		if(rs.next()) {
			lineCode = rs.getString(1);
		}
		rs.close();
		
		// 创建关联关系
		String[] stationCodes = line.getAllStation().split(",");
		for(int i=0; i<stationCodes.length; i++) {
			PreparedStatement psSaveRef = connection.prepareStatement(sqlSaveRef);
			psSaveRef.setString(1, lineCode);
			psSaveRef.setString(2, stationCodes[i]);
			psSaveRef.setInt(3, i);
			psSaveRef.executeUpdate();
			psSaveRef.close();
		}
		psSelMaxCode.close();
		psUpdateLine.close();
		connection.close();
	}
	

}
