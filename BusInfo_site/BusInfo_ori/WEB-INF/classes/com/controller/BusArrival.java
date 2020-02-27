package com.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BusArrival")
public class BusArrival extends HttpServlet {

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Ư�� �����忡�� Ư�� �뼱 ������ ���� ���� �ð� ���ϱ�!!(������ ������ ����)
		
		// ajax���� ���� parameter ��������
		String station_id = request.getParameter("station_id");
		String route_id = request.getParameter("route_id");

		// ���� ��� ���� ����
		String result = "";
		
		// cmd ���๮ -> bus_arrival.py ����
		String command = "python C:\\Users\\-\\python\\SecondProject\\model\\bus_arrival.py " + station_id + " " + route_id;
		
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
			
			// �ʿ��� �κи� result�� ���� -> bus_arrival.py ���� �� "& �����ð� &"ó�� ��µ�. & ���̿� �ִ� ���ڿ��� �����ϱ�
			String[] arr = line_tot.split("&");
			result = arr[1];

		} 
		catch (Exception e) { e.printStackTrace(); }

		
		// Ŭ���̾�Ʈ���� result ����
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);				
	}

}
