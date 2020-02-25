package com.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RouteStationDAO {
	private Connection conn;
	private PreparedStatement psmt;
	private ResultSet rs;
	
	// jdbc ����
	private void getConn() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String db_url = "jdbc:oracle:thin:@localhost:1521:xe";
			String db_id = "hr";
			String db_pw = "1234";
			conn = DriverManager.getConnection(db_url, db_id, db_pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// jdbc ���� ����
	private void close() {
		try {
			if (rs != null) { rs.close(); }
			if (psmt != null) {	psmt.close(); }
			if (conn != null) {	conn.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// route_id(Ư�� ���� �뼱 id)�� ��/������ ����, ������ �̸�, ������ id ����
	public ArrayList<RouteStationDTO> routestaion(String route_id) {
		
		// ������ ����
		ArrayList<RouteStationDTO> result = new ArrayList<RouteStationDTO>();
		
		// jcbc ����
		getConn();
		
		// ������
		String sql = "select UPDOWN, STATION_NM, STATION_ID from routestation where ROUTE_ID = ?";
		
		// sql ����
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, route_id);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				String updown = rs.getString(1);
				String station_nm = rs.getString(2);
				String station_id = rs.getString(3);
				
				// result�� sql ���� ��� ����
				RouteStationDTO info = new RouteStationDTO(updown, station_nm, station_id);
				result.add(info);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // jdbc ���� ����
		}
		
		return result;
	}
}
