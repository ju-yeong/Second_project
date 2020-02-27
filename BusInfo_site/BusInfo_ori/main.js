// 전역변수 설정
var route_id; // 선택(클릭)한 노선 id
var up_name; // 선택 노선 정방향 정류장 이름
var down_name; // 선택 노선 역방향 정류장 이름
var up_num; // 선택 노선 정방향 정류장 개수
var down_num; // 선택 노선 역방향 정류장 개수
var up_id; // 선택 노선 정방향 정류장 id
var down_id; // 선택 노선 역방향 정류장 id


$(function(){
	
	// 버스 노선 번호 검색 : 돋보기 icon 클릭 시 검색 결과 보여주기
	$(document).on("click","#btn_search",function()
	{	
		// 기존 검색 결과 지우기 
		$('.bus_num_result_li').remove(); 
		// 기존 노선도 지우기
		$('#bus_stop_list').remove(); 
		$('.line_wrap').append(`<ul id="bus_stop_list"></ul>`);
		// 정.역방향 title 원래대로
		//// 색깔 : 정방향 핑크, 역방향 회색
		$('.up_title').css({"background-color": "#ffcccc"});
		$('.down_title').css({"background-color": "gray"});
		$('.line_container').attr('id', 'up');
		//// text : 상행, 하행
		$("#up_title_txt").text("상행");
		$("#down_title_txt").text("하행");
		// 툴팁 닫기
		closeLayer();
		
		// refresh_start()를 처음으로 호출 한 뒤에 refresh_stop()을 사용할 수 있음!
		refresh_start();
		// 버스 위치 새로 고침 중지
		refresh_stop();		
		
		// 빈칸 제출 시 경고창 띄우고 다시 입력하기
		if ($("#bus_num").val().length === 0){
			alert("버스 번호를 입력해 주세요.");
			$("#bus_num").focus();
		} 
		// 버스 번호 입력 시 검색 결과 보여주기
		else {
			// 입력한 노선 번호 저장
			var formData = $('#form').serialize();
			
			// 입력한 노선 번호로 시작하는 모든 노선 가져오기
			$.ajax({
				cache: false,
				url: 'BusNumResult?',
				type: 'GET',
				data: formData,
				dataType: 'json',
				success: function(data) {
					
					// 노선 번호가 없는 경우
					if (Object.keys(data).length === 0){
						alert("노선 번호가 없습니다. 다시 확인해 주세요.");
						$("#bus_num").focus();
					} 
					// 노선 번호가 있는 경우
					else {
						// 조회 결과 저장
						for(var key in data) {
							var route_nm = data[key]['route_nm']; // 노선 번호
							var region_name = data[key]['region_name']; // 담당 지역
							var st_sta_nm = data[key]['st_sta_nm']; // 기점 정류장 이름
							var ed_sta_nm = data[key]['ed_sta_nm']; // 종점 정류장 이름
							var route_id_for_result = data[key]['route_id']; // 노선 id
							
							// ul에 태그 붙이기 (<li id="노선id"> 노선 번호, 담당지역, 기점 → 종점 </li> 
							$('.bus_num_result').append(
									`<li class="bus_num_result_li" id=${route_id_for_result}>
									<div class="bus_num_result_li_title">${route_nm} / ${region_name}</div> 
					        		<div class="bus_num_result_li_st">${st_sta_nm}</div>
					        		<div class="bus_num_result_li_arrow"> → </div>
					        		<div class="bus_num_result_li_ed">${ed_sta_nm}</div>
									</li>`
							)
						}
					}
				},
				error: function(xhr, status) {
					alert(xhr + ":" + status);
				}
			});
		}
		return false; // 제출 시(버튼 클릭 or 엔터키) 새로고침 방지!
	});
	
	
	
	
	
	// 버스 노선 검색 결과 목록 중 한 노선 클릭 시 해당 노선도 보여주기 
	$(document).on("click",".bus_num_result_li",function(){
		
		// 툴팁 닫기
		closeLayer();
		// 버스 위치 새로 고침 중지
		refresh_stop();
		
		// 정.역방향 title 수정
		var st_txt = $(this).children(".bus_num_result_li_st").text(); // 기점 정류장 이름
		var ed_txt = $(this).children(".bus_num_result_li_ed").text(); // 종점 정류장 이름
		//// 정방향 title(기점 → 종점)
		$("#up_title_txt").text(`${st_txt} → ${ed_txt}`);
		//// 역방향 title(종점 → 기점)
		$("#down_title_txt").text(`${ed_txt} → ${st_txt}`);
		//// 정.역방향 title 색깔
		$('.up_title').css({"background-color": "#ffcccc"});
		$('.down_title').css({"background-color": "gray"});
		
		// 버스 노선 리스트 중 클릭한 노선만 회색으로 바꾸기
		$(".bus_num_result_li").css({"background-color": "white"});
		$(this).css({"background-color": "#ced6e0"});
		
		// 전역변수 초기화
		up_name = new Array();
		down_name = new Array();
		up_num = 0;
		down_num = 0;
		up_id = new Array(); 
		down_id = new Array();
		
		// 클릭한 노선 id 
		route_id = $(this).attr('id');
		
		// 클릭한 노선 id의 정류장 정보 가져오기
		$.ajax({
			cache: false,
			url: 'BusNumStation?',
			type: 'GET',
			data: {"route_id": route_id},
			dataType: 'json',
			success: function(data) {

				// 정.역방향별 정류장 개수 및 이름 저장
				for(var i=0; i<Object.keys(data).length; i++){
					
					var updown = data[i]['updown']; // 정.역방향 여부
					var station_nm = data[i]['station_nm']; // 정류장 이름
					var station_id = data[i]['station_id']; // 정류장 id
					
					if (updown === "정") { 		// 정방향 일 때
						up_name[up_num] = station_nm; // 정류장 이름 저장
						up_id[up_num] = station_id; // 정류장 id 저장
						up_num++; // 정류장 개수 저장
					} else if (updown === "역") { // 역방향 일 때
						down_name[down_num] = station_nm; // 정류장 이름 저장
						down_id[down_num] = station_id; // 정류장 id 저장
						down_num++;	// 정류장 개수 저장
					}
				}
				// 정방향은 마지막 정류장이 역방향 첫번째 정류장임
				up_name[up_num] = down_name[0];
				up_id[up_num] = down_id[0];
				up_num++;
				
				// 정방향 노선도 그리기 (역방향은 역방향 클릭 시 보여줌)
				draw_line("up");
			},
			error: function(xhr, status) {
				alert(xhr + ":" + status);
			}
		});
		
		// 20초마다 버스 위치 새로 고침 
		refresh_start();
	});
	
	
	
	
	
	// 버스 노선 검색 결과 목록 중 한 노선 클릭 시 버스 위치 보여주기
	$(document).on("click",".bus_num_result_li",function(){
		bus_loca();		
	});
	
	
	
	
	
	// 정방향 클릭 시 정방향 노선도 보여주기
	$(document).on("click",".up_title",function(){
		// 툴팁 닫기
		closeLayer();
		// 정.역방향 title 색깔 수정
		$('.up_title').css({"background-color": "#ffcccc"});
		$('.down_title').css({"background-color": "gray"});
		// 반투명 박스 id 'up'으로 수정
		$('.line_container').attr('id', 'up');
		// 정방향 노선도 그리기
		draw_line("up");
		// 버스 위치 보여주기
		bus_loca();
	});
	
	
	
	
	
	// 역방향 클릭 시 정방향 노선도 보여주기
	$(document).on("click",".down_title",function(){
		// 툴팁 닫기
		closeLayer();
		// 정.역방향 title 색깔 수정
		$('.down_title').css({"background-color": "#ffcccc"});
		$('.up_title').css({"background-color": "gray"});
		// 반투명 박스 id 'down'으로 수정
		$('.line_container').attr('id', 'down');
		// 역방향 노선도 그리기
		draw_line("down");
		// 버스 위치 보여주기
		bus_loca();
	});	
	
	
	
	
	
	// 정류장 이름에 마우스 올렸을 때 전체 이름 보여줌
	$(document).on('mouseenter','.station', function (event) {
		// 마우스 올린 정류장 이름 위치 저장(위쪽,왼쪽)
		var divLeft = $(this).offset().left;
		var divTop = $(this).offset().top;
		// 마우스 올린 정류장 이름 저장
		var station_txt = $(this).text();
		// 전체 이름 div에 정류장 이름 쓰기
		$('.station_hover').text(station_txt);
		// 전체 이름 div css 수정(위치, 크기 등)
		$('.station_hover').css({
			"top": divTop,
			"left": divLeft,
			"background-color": "grey",
			"padding": "5px",
			"display" : "block"});
	// 마우스 내리면  전체 이름 div 사라짐
	}).on('mouseleave','.station',  function(){
		$('.station_hover').css({
			"display" : "none"});
	});


	
	
	
	// 정류장의 동그라미 icon 클릭 시 도착 예상 시간 툴팁 띄워주기
	$(document).on("click",".circle",function()
	{	
		// 대기 문구 설정
		$('#pred_time').text("잠시만 기다려 주세요 :)");
		
		// 클릭한 정류장 id 저장
		var station_id = $(this).parents('li').attr('id');

		// 머신러닝 모델로 분석할 7770 버스 정류장 id 
		var bus_stop_availabel_up = new Array("200000188","200000186","200000078"); // 정방향 3개
		var bus_stop_availabel_down = new Array("200000194","200000271","200000183","202000220","202000217"); // 역방향 5개
		
		// 현재 노선도의 정.역방향 여부
		var up_or_down = $(".line_container").attr("id");
		
		// 7770 버스의 머신러닝 분석 정류장 10개인 경우
		if ($(".bus_num_result_li").attr('id') === "233000031" && 
			((up_or_down === "up" && bus_stop_availabel_up.some(x => x===station_id)) ||
			(up_or_down === "down" && bus_stop_availabel_down.some(x => x===station_id)))){
				
				// 대기 문구 설정
				$('#pred_time').text("분석 중..");
				
				// 도착 예상 시간 가져오기
				$.ajax({
					cache: false,
					url: 'PredRealTime?',
					type: 'GET',
					data: {"station_id" : station_id},
					success: function(data) {
						
						// 도착 예상 시간을 숫자로 바꾼 후 소수점 이하 버리기
						var result = Math.floor(Number(data));

						// 도착 예상 시간이 숫자가 아니라면 문자 그대로 사용
						if (isNaN(result) == true){ result = data; } 
						// 도착 예상 시간이 숫자라면 소수점 이하 버리고 '분' 붙이기
						else { result = result + '분'; }
						
						// 화면에 보여주기
						$('#pred_time').text(result);

					},
					error: function(xhr, status) {
						alert(xhr + ":" + status);
					}
				});
			// 7770 버스의 10개 정류장이 아닌 모든 경우
			} else {
				
				// 도착 예상 시간 가져오기
				$.ajax({
					cache: false,
					url: 'BusArrival?',
					type: 'GET',
					data: {"station_id" : station_id, "route_id": route_id},
					success: function(data) {
						
						// 도착 예상 시간을 숫자로 바꾼 후 소수점 이하 버리기
						var result = Math.floor(Number(data));

						// 도착 예상 시간이 숫자가 아니라면 문자 그대로 사용
						if (isNaN(result) == true){ result = data; } 
						// 도착 예상 시간이 숫자라면 소수점 이하 버리고 '분' 붙이기
						else { result = result + '분'; }
						
						// 화면에 보여주기
						$('#pred_time').text(result);
						
					},
					error: function(xhr, status) {
						alert(xhr + ":" + status);
					}
				});
			}

		// 클릭한 동그라미 icon 근처에 예상시간 보여주기
		var divLeft = $(this).offset().left - 72;
		var divTop = $(this).offset().top - 100;

		$('.popup_layer').css({
			"top": divTop,
			"left": divLeft,

		}).show();		
	});
	
	
	
	
	
	// 예상시간 툴팁 닫기
	$(document).on("click",".popup_close",function()
	{
		closeLayer();	
	});
	
});




// 노선도 그리기
function draw_line(ud) {
	
	// 기존 노선도 지우기
	$('#bus_stop_list').remove();
	$('.line_wrap').append(`<ul id="bus_stop_list"></ul>`);
//	$("#up_title_txt").text();
	
	// 노선도 그리기
	if(ud==="up"){ // 정방향일 때 정류장 이름, 정류장 id, 정류장 개수 저장
		var bus_stop_name = up_name;
		var bus_stop_id = up_id;
		var bus_stop_num = up_num;
	} else if(ud==="down"){ // 역방향일 때 정류장 이름, 정류장 id, 정류장 개수 저장
		var bus_stop_name = down_name;
		var bus_stop_id = down_id;
		var bus_stop_num = down_num;
	}
	
	for (var i=1; i<bus_stop_num; i++){
		// 정류장 종류가 가로, 세로, 마지막일지 판단할 변수
		var now_num = i%18;
				
		// 버스 정류장 이름에 "경유"를 포함하는 경우(미정차역인 경우) circle의 class 이름 저장 
		if (bus_stop_name[i-1].indexOf("(경유)") !== -1){ 
			var circle_class = "circle_nonstop";
		} else {
			var circle_class = "circle";
		}
				
		// 가로노선 왼쪽 방향
		if ( now_num >=1 && now_num <=8 ) {
			$('#bus_stop_list').append(
				`<li class="bus_stop_h_right" id="${bus_stop_id[i-1]}">
				 <span class="hline"></span>
				 <span class="${circle_class}"></span>
				 <span class="station"> ${bus_stop_name[i-1]} </span>`);
		} 
		// 가로노선 오른쪽 방향
		else if ( now_num >= 10 && now_num <= 17 ) {
			$('#bus_stop_list').append(
				`<li class="bus_stop_h_left" id="${bus_stop_id[i-1]}">
				 <span class="hline"></span>
				 <span class="${circle_class}"></span>
				 <span class="station"> ${bus_stop_name[i-1]} </span>`);
		}
		// 세로노선
		else if ( now_num === 0 || now_num === 9 ) {
			$('#bus_stop_list').append(
				`<li class="bus_stop_v" id="${bus_stop_id[i-1]}">
				 <span class="vline"></span>
				 <span class="${circle_class}"></span>
				 <span class="station"> ${bus_stop_name[i-1]} </span>`);
		}	
	}
	// 마지막 역
	if ( now_num >=0 && now_num <=8 ){
		$('#bus_stop_list').append(
			`<li class="bus_stop_last_right" id="${bus_stop_id[i-1]}">
			 <span class="circle"></span>
			 <span class="station"> ${bus_stop_name[i-1]} </span>`);
	} else if ( now_num >= 9 && now_num <= 17 ) {
		$('#bus_stop_list').append(
			`<li class="bus_stop_last_left" id="${bus_stop_id[i-1]}">
			 <span class="circle"></span>
			 <span class="station"> ${bus_stop_name[i-1]} </span>`);
	}
	// 역방향일 경우 이미지 색깔 변경
	if(ud==="down"){
		$('.circle').css({"background-image": "url('images/circle_blue.png')"});
		$('.hline').css({"background-image": "url('images/hline_blue.png')"});
		$('.vline').css({"background-image": "url('images/vline_blue.png')"});
	}
	// 경유역이 경우 동그라미 icon 색깔 변경
	$('.circle_nonstop').css({"background-image": "url('images/circle_grey.png')"});
	// 노선도 띄워주기
	$('.line_wrap').css({"display": "block"});
}





// 실시간 버스 위치 보여주기
function bus_loca() {
	
	// 현재 선택(클릭)한 노선의 버스 위치(정류장 id) 가져오기
	$.ajax({
		cache: false,
		url: 'BusLocation?',
		type: 'GET',
		data: {"route_id": route_id},
		dataType: 'json',
		success: function(data) {

			// 운행 버스 대수 만큼 버스 icon 띄워주기
			for (var i=0;i<Object.keys(data).length; i++){
				
				try {
					// 정류장 li 태그에 div(버스 icon) 붙이기
					$(`#${data[i]}`).append(`<div class='icon_bus_img' id="bus${data[i]}"></div>`);
					
					// 정류장 종류 저장
					var line_type = $(`#${data[i]}`).attr('class');
					
					// 버스 icon 위치 설정
					// 가로노선 오른쪽 방향일 경우
					if (line_type === "bus_stop_h_right"){
						$(`#bus${data[i]}`).css({
							"top": "-3px",
							"left": "40px",
						}).show();
					// 가로노선 왼쪽 방향일 경우
					} else if (line_type === "bus_stop_h_left"){
						$(`#bus${data[i]}`).css({
							"top": "-3px",
							"right": "40px",
						}).show();
					// 세로노선일 경우
					} else if (line_type === "bus_stop_v"){
						$(`#bus${data[i]}`).css({
							"top": "40px",
							"left": "-3px",
						}).show();
					// 마지막 정류장 오른쪽 방향일 경우
					} else if (line_type === "bus_stop_last_right"){
						$(`#bus${data[i]}`).css({
							"display": "none"
						});
					// 마지막 정류장 왼쪽 방향일 경우
					} else if (line_type === "bus_stop_h_left"){
						$(`#bus${data[i]}`).css({
							"display": "none"
						});
					} 	
				}
				catch (e) {
					continue;
				}
			}
		},
		error: function(xhr, status) {
			alert(xhr + ":" + status);
		}
	});	
}





// 20초마다 버스 위치 새로 고침
refresh_start = function() {
	bus_loca_refresh = setInterval(function() {
		console.log("시작했냥");
		$(".icon_bus_img").remove();
		bus_loca();
	}, 20000);
};





// 버스 위치 새로 고침 중지
refresh_stop = function() {
	clearInterval(bus_loca_refresh);
	console.log("멈췄냥");
};





// 예상시간 툴팁 닫기
function closeLayer() {
	$(".popup_layer").hide();
}
