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
		// Ư�� �뼱 ������ ���� ��ġ ���ϱ�!!
		
		// ajax���� ���� parameter ��������
		String route_id = request.getParameter("route_id");

		// cmd ���� ��� ���� ����
		String result_id = "";
		
		// cmd ���๮ -> bus_location.py ����
		String command = "python C:\\Users\\-\\python\\SecondProject\\model\\bus_location.py " + route_id;
		
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
			
			// ���๮�� ���ֱ�
			line_tot = line_tot.replaceAll(System.getProperty("line.separator"), " ");
			// ���� ���ֱ�
			line_tot = line_tot.replaceAll(" ", "");
			// �ʿ��� �κи� result�� ���� -> bus_arrival.py ���� �� "& stationid1# ... stationid9# &"ó�� ��µ�. & ���̿� �ִ� ���ڿ��� �����ϱ�
			String[] arr = line_tot.split("&");
			result_id = arr[1];
		} 
		catch (Exception e) { e.printStackTrace(); }
		
		// stationid���� �����ϱ�
		String[] id = result_id.split("#");
		
		// ���������� ajax�� ������ json ����
		JSONObject result = new JSONObject();
		for (int i=0; i<id.length; i++) {
			result.put(i, id[i]);
		}	
		
		// Ŭ���̾�Ʈ���� result ����
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
				
	}

}