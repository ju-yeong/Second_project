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

	<!-- ==================== 1. 로고 ==================== -->
	<div class="logo_wrap">
	</div>
	
	<!-- ==================== 2. 노선 검색 결과 & 3. 노선도 ==================== -->
	<div class="bus_total_container">
		<!-- ========== 2. 노선 검색 결과 ========== -->
	    <div class="search_container">
	    	<!-- ===== 2-1) 시도 선택(사실상 필요 없음. 추후 삭제) ===== -->
	        <select id="sido" class="sido">
				<option value="">시/도 선택</option>
				<option value="경기도" selected>경기도</option>
			</select>
			<!-- ===== 2-2) 노선 번호 입력 및 제출 ===== -->
	        <div class="form_wrap">
	            <form id="form" name="form" method="post"> 
	            	<!-- 입력 -->
					<input type="number" id="bus_num" placeholder="노선 번호" name="bus_num">
					<!-- 제출 -->
					<button type="submit" id="btn_search"><img src="images/icon_search.png" class="icon_search"></button>
				</form>
	        </div>
	        <!-- ===== 2-3) 검색 결과 ===== -->
	        <div class="bus_num_result_wrap">
	        	<!-- 검색 결과 리스트 -->
	        	<ul class="bus_num_result">

	        	</ul>
	        </div>
	    </div>
	    <!-- ========== 3. 노선도 ========== -->
		<div class="bus_info_container">
			<!-- ===== 3-1) 정방향 title ===== -->
			<span class="up_title">
				<p id="up_title_txt">상행</p>
			</span>
			<!-- ===== 3-2) 역방향 title ===== -->
			<span class="down_title">
				<p id="down_title_txt">하행</p>
			</span>
			<!-- ===== 3-3) 노선도 ===== -->
			<div class="line_container" id="up">
				<div class="line_wrap">
					<ul id="bus_stop_list">
						<li>테스트다</li>>
					</ul>
				</div>          
			</div>
		</div>
	</div>
	
	<!-- 정류장 이름에 mouse over 시 보여줄 전체 이름 -->
	<div class="station_hover"></div>
	
	<!-- 툴팁 : 정류장별 동그라미 icon 클릭 시 보여 줄 버스 도착 예상 시간 -->
	<div class="popup_layer">
		<!-- 말풍선 -->
		<div class="popup_img">
			<img src="images/bubble.png">
			<div class="popup_content">
				도착 예정 <p id="pred_time">잠시만 기다려 주세요 :)</p>
			</div>
		</div>
		<!-- 엑스박스 -->
		<div class="popup_close">
		</div>
	</div>

</body>
</html>