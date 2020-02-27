package com.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RouteDAO {
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
	
	// bus_num���� �����ϴ� ��� �뼱��  �뼱 �̸�, ��� ����, ���� ������ �̸�, ���� ������ �̸�, �뼱 id ����
	public ArrayList<RouteDTO> route(String bus_num) {
		
		// ������ ����
		ArrayList<RouteDTO> result = new ArrayList<RouteDTO>();
		
		// jcbc ����
		getConn();
		
		// ������
		String sql = "select ROUTE_NM, REGION_NAME, ST_STA_NM, ED_STA_NM, ROUTE_ID from route where ROUTE_NM like '" + bus_num + "%'";
		
		// sql ����
		try {
			psmt = conn.prepareStatement(sql);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				String route_nm = rs.getString(1);
				String region_name = rs.getString(2);
				String st_sta_nm = rs.getString(3);
				String ed_sta_nm = rs.getString(4);
				String route_id = rs.getString(5);
				
				// result�� sql ���� ��� ����
				RouteDTO info = new RouteDTO(route_nm, region_name, st_sta_nm, ed_sta_nm, route_id);
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
