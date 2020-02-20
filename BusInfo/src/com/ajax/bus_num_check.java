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
		// ���� �뼱 ��ȣ
		String bus_num = request.getParameter("bus_num");
		// ������ �̸�
		String up_title = "���� �� ���";
		String down_title = "��� �� ����";
		// ���� ������ �̸�
		//// ����
		String[] bus_stop_name_up = {
				"������", "�����͹̳�","����Ÿ�","�������ڰ�","ȭ������","�۷ι�û�ҳ�帲����","ȭ����",
				"��Ȱ���","��ȹ�","KT��������","�������տ��","����KT������ũ","����Ÿ��","���Ͽ���",
				"�������б�","�ѱ�������������","��⵵����ȯ�濬����","��⵵���簳�߿�","�ǿ������Ʈ","������Ÿ��δ��",
				"���·ɿ�(��)","��翪(��)"};
		////����
		String[] bus_stop_name_down = {
				"��翪(��)","��翪4���ⱸ","������Ÿ��δ��","�ǿ������Ʈ","��⵵���簳�߿�","����û��Ÿ�","��⵵����ȯ�濬����",
				"�ѱ�������������","���Ͽ���","����Ϻ�","�����������","����KT������ũ","�������տ��","�����ƿ﷿",
				"��Ȱ���","�ѱ�����","ȭ�������͸�","�۷ι�û�ҳ�帲����","����û�Ա�","�������ڰ�","����Ÿ�","��õ��ȸ",
				"������"};
		
		// ���� ������ ��
		//// ����
		String bus_stop_num_up = Integer.toString(bus_stop_name_up.length);
		//// ����
		String bus_stop_num_down = Integer.toString(bus_stop_name_down.length);
		
		// json �����
		// �뼱��ȣ, ������ ��, ������ ���� json
		JSONObject basic_info = new JSONObject();  
		basic_info.put("bus_num", bus_num);
		basic_info.put("bus_stop_num_up", bus_stop_num_up);
		basic_info.put("bus_stop_num_down", bus_stop_num_down);
		basic_info.put("up_title", up_title);
		basic_info.put("down_title", down_title);
		// ������ �̸��� ���� json
		//// ����
		JSONObject name_info_up = new JSONObject(); 
		for (int i=0; i<bus_stop_name_up.length; i++) {
			name_info_up.put(i, bus_stop_name_up[i]);
		}
		//// ����
		JSONObject name_info_down = new JSONObject(); 
		for (int i=0; i<bus_stop_name_down.length; i++) {
			name_info_down.put(i, bus_stop_name_down[i]);
		}
		// ����  json
		JSONObject bus_info = new JSONObject(); 
		bus_info.put("basic_info", basic_info);
		bus_info.put("name_info_up", name_info_up);
		bus_info.put("name_info_down", name_info_down);
		
		// Ŭ���̾�Ʈ���� ������ ����
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(bus_info);
	}

}
