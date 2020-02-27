package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.model.RouteDAO;
import com.model.RouteDTO;


@WebServlet("/BusNumResult")
public class BusNumResult extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 사용자가 입력한 버스 노선 번호로 시작하는 모든 노선 번호 구하기(노선 번호 검색 결과에 사용)
		
		// ajax에서 보낸 parameter 가져오기
		String bus_num = request.getParameter("bus_num");
		
		// bus_num으로 시작하는 모든 버스 노선 저장(com.model.RouteDAO에 기능 정의)
		RouteDAO dao = new RouteDAO();
		ArrayList<RouteDTO> bus_num_list = dao.route(bus_num);
		
		// 최종적으로 ajax에 전달할 json 변수
		JSONObject result = new JSONObject();
		for (int i=0; i<bus_num_list.size(); i++) {
			// temp(임의의 JSONObject)에 한 개의 버스 노선 정보 저장
			JSONObject temp = new JSONObject();
			temp.put("route_nm", bus_num_list.get(i).getRoute_nm()); // 노선 번호
			temp.put("region_name", bus_num_list.get(i).getRegion_name()); // 담당 지역
			temp.put("st_sta_nm", bus_num_list.get(i).getSt_sta_nm()); // 기점 정류장 이름
			temp.put("ed_sta_nm", bus_num_list.get(i).getEd_sta_nm()); // 종점 정류장 이름
			temp.put("route_id", bus_num_list.get(i).getRoute_id()); // 노선 id
			
			// result에 temp 저장
			result.put(i, temp);
		}		
		
		// 클라이언트에게 result 전송
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

}
