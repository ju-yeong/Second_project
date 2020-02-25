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
		// �ӽŷ��� �𵨷� 7770 �뼱�� 10�� �����忡 ���� ���� ���� �ð� �м��ϱ�(������ ������ ����)
		
		// ajax���� ���� parameter ��������
		String station_id = request.getParameter("station_id");
		
		// ���� ��� ���� ����
		String result = "";
		
		// cmd ���๮ -> pred_real_time.py ����
		String command = "python C:\\Users\\-\\python\\SecondProject\\model\\pred_real_time.py " + station_id;

		// cmd ����
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
				line_tot += line + "\r\n"; // cmd ���� ���(cmd�� ���̴� ��� ���ڿ�)�� ��� ����
			}

			p.waitFor();
			
			// �ʿ��� �κи� result�� ���� -> bus_arrival.py ���� �� "& [�����ð�] &"ó�� ��µ�. & ���̿� �ִ� ���ڿ��� �����ϰ� '[', ']' �����ϱ�
			String[] arr = line_tot.split("&");
			result = arr[1];
			result = result.replace("[","");
			result = result.replace("]","");
		} 
		catch (Exception e) { e.printStackTrace(); }
		
		// Ŭ���̾�Ʈ���� result ����
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

}
