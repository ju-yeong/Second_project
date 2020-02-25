package com.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PredRealTime")
public class PredRealTime extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 머신러닝 모델로 7770 노선의 10개 정류장에 버스 도착 예상 시간 분석하기(툴팁에 보여줄 내용)
		
		// ajax에서 보낸 parameter 가져오기
		String station_id = request.getParameter("station_id");
		
		// 최종 결과 저장 변수
		String result = "";
		
		// cmd 실행문 -> pred_real_time.py 실행
		String command = "python C:\\Users\\-\\python\\SecondProject\\model\\pred_real_time.py " + station_id;

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
			
			// 필요한 부분만 result에 저장 -> bus_arrival.py 실행 시 "& [도착시간] &"처럼 출력됨. & 사이에 있는 문자열만 저장하고 '[', ']' 삭제하기
			String[] arr = line_tot.split("&");
			result = arr[1];
			result = result.replace("[","");
			result = result.replace("]","");
		} 
		catch (Exception e) { e.printStackTrace(); }
		
		// 클라이언트에게 result 전송
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

}
