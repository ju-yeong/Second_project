<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
<link rel="stylesheet" href="main.css?ver=1">
<script src="js/jquery-2.2.4.min.js"></script>
<script type="text/javascript" src="main.js?ver=2"></script>
<link href="https://fonts.googleapis.com/css?family=Do+Hyeon&display=swap" rel="stylesheet">
</head>
<body>
	<div class="logo_wrap" onclick="tooltip(); return false;">
		<img src = "images/logo.png" alt="logo">
	</div>
    <div class="search_container">
        <select name="sido" class="sido">
			<option value="">시/도 선택</option>
			<option value="경기도" selected>경기도</option>
		</select>
        <select name="sigungo" class="sigungo">
			<option value="">시/군/구 선택</option>
			<option value="수원시" selected>수원시</option>			
			<option value="가평군">가평군</option>
			<option value="고양시">고양시</option>
			<option value="과천시">과천시</option>
			<option value="광명시">광명시</option>
			<option value="광주시">광주시</option>
			<option value="구리시">구리시</option>
			<option value="군포시">군포시</option>
			<option value="김포시">김포시</option>
			<option value="남양주시">남양주시</option>
			<option value="동두천시">동두천시</option>
			<option value="부천시">부천시</option>
			<option value="성남시">성남시</option>
			<option value="시흥시">시흥시</option>
			<option value="안산시">안산시</option>
			<option value="안성시">안성시</option>
			<option value="안양시">안양시</option>
			<option value="양주시">양주시</option>
			<option value="양평군">양평군</option>
			<option value="여주시">여주시</option>
			<option value="연천군">연천군</option>
			<option value="오산시">오산시</option>
			<option value="용인시">용인시</option>
			<option value="의왕시">의왕시</option>
			<option value="의정부시">의정부시</option>
			<option value="이천시">이천시</option>
			<option value="파주시">파주시</option>
			<option value="평택시">평택시</option>
			<option value="포천시">포천시</option>
			<option value="하남시">하남시</option>
			<option value="화성시">화성시</option>
		</select>
        <span class="form_wrap">
            <form id="form" name="form" method="post">
				<input type="number" id="bus_num" placeholder="버스번호" name="bus_num">
				<button type="submit" id="btn_search" onclick="bus_num_func(); return false;"><img src="images/icon_search.png" class="icon_search"></button>
			</form>
        </span>
    </div>
	<div class="bus_info_container">
		<div class="up_title" onclick="up_title_func(); return false;">
			<p id="up_title">상행</p>
		</div>
		<div class="down_title" onclick="down_title_func(); return false;">
			<p id="down_title">하행</p>
		</div>
		<div class="line_container">
			<div class="line_wrap">
				<ol id="bus_stop_list">
					
				</ol>
			</div>          
		</div>
	</div>

	<div class="popup_layer">
		<div class="popup_img">
			<img src="images/bubble.png">
			<div class="popup_content">
				도착 예정 <p id="pred_time">분석 중..</p>
			</div>
		</div>
		<div>
			<span onclick="closeLayer(this)" style="cursor:pointer;font-size:1.7rem" title="닫기"><img class = "icon_close" src="images/icon_close.png" alt="" ></span>
		</div>
	</div>


</body>
</html>