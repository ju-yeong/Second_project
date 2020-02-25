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
	
	// jdbc 연결
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
	
	// jdbc 연결 종료
	private void close() {
		try {
			if (rs != null) { rs.close(); }
			if (psmt != null) {	psmt.close(); }
			if (conn != null) {	conn.close(); }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// route_id(특정 버스 노선 id)의 정/역방향 여부, 정류장 이름, 정류장 id 리턴
	public ArrayList<RouteStationDTO> routestaion(String route_id) {
		
		// 리턴할 변수
		ArrayList<RouteStationDTO> result = new ArrayList<RouteStationDTO>();
		
		// jcbc 연결
		getConn();
		
		// 쿼리문
		String sql = "select UPDOWN, STATION_NM, STATION_ID from routestation where ROUTE_ID = ?";
		
		// sql 실행
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, route_id);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				String updown = rs.getString(1);
				String station_nm = rs.getString(2);
				String station_id = rs.getString(3);
				
				// result에 sql 실행 결과 저장
				RouteStationDTO info = new RouteStationDTO(updown, station_nm, station_id);
				result.add(info);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(); // jdbc 연결 종료
		}
		
		return result;
	}
}
