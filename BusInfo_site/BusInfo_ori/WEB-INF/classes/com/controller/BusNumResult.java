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
		// ����ڰ� �Է��� ���� �뼱 ��ȣ�� �����ϴ� ��� �뼱 ��ȣ ���ϱ�(�뼱 ��ȣ �˻� ����� ���)
		
		// ajax���� ���� parameter ��������
		String bus_num = request.getParameter("bus_num");
		
		// bus_num���� �����ϴ� ��� ���� �뼱 ����(com.model.RouteDAO�� ��� ����)
		RouteDAO dao = new RouteDAO();
		ArrayList<RouteDTO> bus_num_list = dao.route(bus_num);
		
		// ���������� ajax�� ������ json ����
		JSONObject result = new JSONObject();
		for (int i=0; i<bus_num_list.size(); i++) {
			// temp(������ JSONObject)�� �� ���� ���� �뼱 ���� ����
			JSONObject temp = new JSONObject();
			temp.put("route_nm", bus_num_list.get(i).getRoute_nm()); // �뼱 ��ȣ
			temp.put("region_name", bus_num_list.get(i).getRegion_name()); // ��� ����
			temp.put("st_sta_nm", bus_num_list.get(i).getSt_sta_nm()); // ���� ������ �̸�
			temp.put("ed_sta_nm", bus_num_list.get(i).getEd_sta_nm()); // ���� ������ �̸�
			temp.put("route_id", bus_num_list.get(i).getRoute_id()); // �뼱 id
			
			// result�� temp ����
			result.put(i, temp);
		}		
		
		// Ŭ���̾�Ʈ���� result ����
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

}
