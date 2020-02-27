package com.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

@WebServlet("/BusLocation")
public class BusLocation extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 특정 노선 버스의 현재 위치 구하기!!
		
		// ajax에서 보낸 parameter 가져오기
		String route_id = request.getParameter("route_id");

		// cmd 실행 결과 저장 변수
		String result_id = "";
		
		// cmd 실행문 -> bus_location.py 실행
		String command = "python C:\\Users\\-\\python\\SecondProject\\model\\bus_location.py " + route_id;
		
		// cmd 실행
		try {
			ProcessBuilder b = new ProcessBuilder("cmd");
			b.redirectErrorStream(true);
			Process p = b.start();

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

			writer.write(command + "\n");
			writer.flush();

			writer.write("exit" + "\n");
			writer.flush();

			BufferedReader std = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			String line = "";
			String line_tot = "";
			while ((line = std.readLine()) != null) {
				line_tot += line + "\r\n"; // cmd 실행 결과(cmd에 보이는 모든 문자열)를 모두 저장
			}

			p.waitFor();
			
			// 개행문자 없애기
			line_tot = line_tot.replaceAll(System.getProperty("line.separator"), " ");
			// 공백 없애기
			line_tot = line_tot.replaceAll(" ", "");
			// 필요한 부분만 result에 저장 -> bus_arrival.py 실행 시 "& stationid1# ... stationid9# &"처럼 출력됨. & 사이에 있는 문자열만 저장하기
			String[] arr = line_tot.split("&");
			result_id = arr[1];
		} 
		catch (Exception e) { e.printStackTrace(); }
		
		// stationid별로 저장하기
		String[] id = result_id.split("#");
		
		// 최종적으로 ajax에 전달할 json 변수
		JSONObject result = new JSONObject();
		for (int i=0; i<id.length; i++) {
			result.put(i, id[i]);
		}	
		
		// 클라이언트에게 result 전송
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
				
	}

}