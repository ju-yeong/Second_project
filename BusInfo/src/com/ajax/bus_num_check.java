package com.ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/bus_num_check")
public class bus_num_check extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 버스 노선 번호
		String bus_num = request.getParameter("bus_num");
		// 상하행 이름
		String up_title = "수원 → 사당";
		String down_title = "사당 → 수원";
		// 버스 정류장 이름
		//// 상행
		String[] bus_stop_name_up = {
				"수원역", "고등동구터미널","고등동사거리","수원여자고교","화서시장","글로벌청소년드림센터","화서문",
				"장안공원","장안문","KT수원지사","수원종합운동장","수원KT위즈파크","한일타운","수일여중",
				"수일중학교","한국가스안전공사","경기도보건환경연구원","경기도인재개발원","의왕톨게이트","관문사거리부대앞",
				"남태령역(중)","사당역(중)"};
		////하행
		String[] bus_stop_name_down = {
				"사당역(중)","사당역4번출구","관문사거리부대앞","의왕톨게이트","경기도인재개발원","국세청삼거리","경기도보건환경연구원",
				"한국가스안전공사","수일여중","경기일보","장안지하차도","수원KT위즈파크","수원종합운동장","동성아울렛",
				"장안공원","한국은행","화서문로터리","글로벌청소년드림센터","병무청입구","수원여자고교","고등동사거리","농천교회",
				"수원역"};
		
		// 버스 정류장 수
		//// 상행
		String bus_stop_num_up = Integer.toString(bus_stop_name_up.length);
		//// 하행
		String bus_stop_num_down = Integer.toString(bus_stop_name_down.length);
		
		// json 만들기
		// 노선번호, 정류장 수, 종점을 담을 json
		JSONObject basic_info = new JSONObject();  
		basic_info.put("bus_num", bus_num);
		basic_info.put("bus_stop_num_up", bus_stop_num_up);
		basic_info.put("bus_stop_num_down", bus_stop_num_down);
		basic_info.put("up_title", up_title);
		basic_info.put("down_title", down_title);
		// 정류장 이름을 담을 json
		//// 상행
		JSONObject name_info_up = new JSONObject(); 
		for (int i=0; i<bus_stop_name_up.length; i++) {
			name_info_up.put(i, bus_stop_name_up[i]);
		}
		//// 하행
		JSONObject name_info_down = new JSONObject(); 
		for (int i=0; i<bus_stop_name_down.length; i++) {
			name_info_down.put(i, bus_stop_name_down[i]);
		}
		// 최종  json
		JSONObject bus_info = new JSONObject(); 
		bus_info.put("basic_info", basic_info);
		bus_info.put("name_info_up", name_info_up);
		bus_info.put("name_info_down", name_info_down);
		
		// 클라이언트에게 데이터 전송
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(bus_info);
	}

}
