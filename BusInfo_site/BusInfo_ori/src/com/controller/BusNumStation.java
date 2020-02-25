package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.model.RouteDAO;
import com.model.RouteStationDAO;
import com.model.RouteStationDTO;


@WebServlet("/BusNumStation")
public class BusNumStation extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 특정 노선 번호의 모든 정류장 정보 구하기(노선도 그릴 때 사용)
		
		// ajax에서 보낸 parameter 가져오기
		String route_id = request.getParameter("route_id");
		
		// 특정 노선 번호의 모든 정류장 정보 구하기(com.model.RouteStationDAO에 기능 정의)
		RouteStationDAO dao = new RouteStationDAO();
		ArrayList<RouteStationDTO> bus_station_list = dao.routestaion(route_id);
		
		// 최종적으로 ajax에 전달할 json 변수
		JSONObject result = new JSONObject();
		for (int i=0; i<bus_station_list.size(); i++) {
			// temp(임의의 JSONObject)에 한 개의 정류장 정보 저장
			JSONObject temp = new JSONObject();
			temp.put("updown", bus_station_list.get(i).getUpdown()); // 정방향,역방향 여부
			temp.put("station_nm", bus_station_list.get(i).getStation_nm()); // 정류장 이름
			temp.put("station_id", bus_station_list.get(i).getStation_id()); // 정류장 id
			result.put(i, temp);
		}		
		
		// 클라이언트에게 데이터 전송
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		
		
	}

}
