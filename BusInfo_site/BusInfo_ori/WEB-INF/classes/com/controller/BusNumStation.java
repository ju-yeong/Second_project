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
		// Ư�� �뼱 ��ȣ�� ��� ������ ���� ���ϱ�(�뼱�� �׸� �� ���)
		
		// ajax���� ���� parameter ��������
		String route_id = request.getParameter("route_id");
		
		// Ư�� �뼱 ��ȣ�� ��� ������ ���� ���ϱ�(com.model.RouteStationDAO�� ��� ����)
		RouteStationDAO dao = new RouteStationDAO();
		ArrayList<RouteStationDTO> bus_station_list = dao.routestaion(route_id);
		
		// ���������� ajax�� ������ json ����
		JSONObject result = new JSONObject();
		for (int i=0; i<bus_station_list.size(); i++) {
			// temp(������ JSONObject)�� �� ���� ������ ���� ����
			JSONObject temp = new JSONObject();
			temp.put("updown", bus_station_list.get(i).getUpdown()); // ������,������ ����
			temp.put("station_nm", bus_station_list.get(i).getStation_nm()); // ������ �̸�
			temp.put("station_id", bus_station_list.get(i).getStation_id()); // ������ id
			result.put(i, temp);
		}		
		
		// Ŭ���̾�Ʈ���� ������ ����
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
		
		
	}

}
