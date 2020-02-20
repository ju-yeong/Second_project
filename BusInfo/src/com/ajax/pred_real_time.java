package com.ajax;

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

@WebServlet("/pred_real_time")
public class pred_real_time extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String bus_stop_name_now = request.getParameter("bus_stop_name_now");
		String test =  URLDecoder.decode(bus_stop_name_now, "UTF-8");
		String result = "";

		String[] commands = new String[] {
				"python C:\\Users\\-\\Downloads\\pred_real_time.py " + bus_stop_name_now
			};
	
		try {
			ProcessBuilder b = new ProcessBuilder("cmd");
			b.redirectErrorStream(true);
			Process p = b.start();

			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

			for (String cmd : commands) {
				writer.write(cmd + "\n");
				writer.flush();
			}

			writer.write("exit" + "\n");
			writer.flush();

			BufferedReader std = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			String line_tot = "";
			while ((line = std.readLine()) != null) {
				line_tot += line + "\r\n";
			}

			p.waitFor();
			
			String[] arr = line_tot.split("&");
			result = arr[1];
			result = result.replace("[","");
			result = result.replace("]","");
			
		} 
		catch (Exception e) { e.printStackTrace(); }
		
		// 클라이언트에게 데이터 전송
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(result);
	}

}
