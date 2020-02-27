<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
<link rel="stylesheet" href="main.css?ver=1">
<script src="js/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="main.js?ver=1"></script>
<link href="https://fonts.googleapis.com/css?family=Do+Hyeon&display=swap" rel="stylesheet">
</head>
<body>

	<!-- ==================== 1. �ΰ� ==================== -->
	<div class="logo_wrap">
	</div>
	
	<!-- ==================== 2. �뼱 �˻� ��� & 3. �뼱�� ==================== -->
	<div class="bus_total_container">
		<!-- ========== 2. �뼱 �˻� ��� ========== -->
	    <div class="search_container">
	    	<!-- ===== 2-1) �õ� ����(��ǻ� �ʿ� ����. ���� ����) ===== -->
	        <select id="sido" class="sido">
				<option value="">��/�� ����</option>
				<option value="��⵵" selected>��⵵</option>
			</select>
			<!-- ===== 2-2) �뼱 ��ȣ �Է� �� ���� ===== -->
	        <div class="form_wrap">
	            <form id="form" name="form" method="post"> 
	            	<!-- �Է� -->
					<input type="number" id="bus_num" placeholder="�뼱 ��ȣ" name="bus_num">
					<!-- ���� -->
					<button type="submit" id="btn_search"><img src="images/icon_search.png" class="icon_search"></button>
				</form>
	        </div>
	        <!-- ===== 2-3) �˻� ��� ===== -->
	        <div class="bus_num_result_wrap">
	        	<!-- �˻� ��� ����Ʈ -->
	        	<ul class="bus_num_result">

	        	</ul>
	        </div>
	    </div>
	    <!-- ========== 3. �뼱�� ========== -->
		<div class="bus_info_container">
			<!-- ===== 3-1) ������ title ===== -->
			<span class="up_title">
				<p id="up_title_txt">����</p>
			</span>
			<!-- ===== 3-2) ������ title ===== -->
			<span class="down_title">
				<p id="down_title_txt">����</p>
			</span>
			<!-- ===== 3-3) �뼱�� ===== -->
			<div class="line_container" id="up">
				<div class="line_wrap">
					<ul id="bus_stop_list">
						<li>�׽�Ʈ��</li>>
					</ul>
				</div>          
			</div>
		</div>
	</div>
	
	<!-- ������ �̸��� mouse over �� ������ ��ü �̸� -->
	<div class="station_hover"></div>
	
	<!-- ���� : �����庰 ���׶�� icon Ŭ�� �� ���� �� ���� ���� ���� �ð� -->
	<div class="popup_layer">
		<!-- ��ǳ�� -->
		<div class="popup_img">
			<img src="images/bubble.png">
			<div class="popup_content">
				���� ���� <p id="pred_time">��ø� ��ٷ� �ּ��� :)</p>
			</div>
		</div>
		<!-- �����ڽ� -->
		<div class="popup_close">
		</div>
	</div>

</body>
</html>