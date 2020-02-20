

var bus_num = ""; // 버스 노선 번호
var up_title = ""; // 상행 이름
var down_title = ""; // 하행 이름
var bus_stop_num_up = ""; // 버스 정류장 수 (상행)
var bus_stop_num_down = ""; // 버스 정류장 수 (하행)
var bus_stop_name_up = ""; // 버스 정류장 이름 (상행)
var bus_stop_name_down = ""; // 버스 정류장 이름 (하행)


// 버스 번호 확인 후 노선도 그리기
function draw_line(up_down) {
	if(up_down === "up"){
		$('li').remove();
		var bus_stop_num = bus_stop_num_up;
		var bus_stop_name = bus_stop_name_up;
	} else if(up_down === "down"){
		$('li').remove();
		var bus_stop_num = bus_stop_num_down;
		var bus_stop_name = bus_stop_name_down;
	}
	
	
	if (bus_num === '7770') {
		
		document.getElementById("up_title").innerText = up_title;
		document.getElementById("down_title").innerText = down_title;
		
		for (var i=1; i<bus_stop_num; i++){
			var now_num = i%12;
			
			// 가로선 왼쪽 방향
			if ( now_num >=1 && now_num <=5 ) {
				$('#bus_stop_list').append(
					`<li type="none" class="bus_stop_h_right id=bus_stop${i}">
					 <span class="hline"></span>
					 <span class="circle"></span>
					 <span class="station" id="bus_stop_name_now"> ${bus_stop_name[i-1]} </span>`);
			} 
			// 가로선 오른쪽 방향
			else if ( now_num >= 7 && now_num <= 11 ) {
				$('#bus_stop_list').append(
					`<li type="none" class="bus_stop_h_left id=bus_stop${i}">
					 <span class="hline"></span>
					 <span class="circle"></span>
					 <span class="station" id="bus_stop_name_now"> ${bus_stop_name[i-1]} </span>`);
			}
			// 세로선
			else if ( now_num === 0 || now_num === 6 ) {
				$('#bus_stop_list').append(
					`<li type="none" class="bus_stop_v id=bus_stop${i}">
					 <span class="vline"></span>
					 <span class="circle"></span>
					 <span class="station" id="bus_stop_name_now"> ${bus_stop_name[i-1]} </span>`);
			}
			
		}
		// 마지막 역
		if ( now_num >=1 && now_num <=5 ){
			$('#bus_stop_list').append(
				`<li type="none" class="bus_stop_last_right id=bus_stop${i}">
				 <span class="circle"></span>
				 <span class="station" id="bus_stop_name_now"> ${bus_stop_name[i-1]} </span>`);
		} else if ( now_num >= 7 && now_num <= 11 ) {
			$('#bus_stop_list').append(
				`<li type="none" class="bus_stop_last_left id=bus_stop${i}">
				 <span class="circle"></span>
				 <span class="station" id="bus_stop_name_now"> ${bus_stop_name[i-1]} </span>`);
		}
	}
	else {
		document.getElementById("up_title").innerText = "상행";
		document.getElementById("down_title").innerText = "하행";
		$('li').remove();
	}
}

// ajax로 노선도 그리기
function bus_num_func() {
	var formData = $('#form').serialize();
	
	$.ajax({
		cache: false,
		url: 'bus_num_check?',
		type: 'GET',
		data: formData,
		dataType: 'json',
		success: function(data) {
			bus_num = data["basic_info"]["bus_num"]; // 버스 노선 번호
			up_title = data["basic_info"]["up_title"]; // 상행 이름
			down_title = data["basic_info"]["down_title"]; // 하행 이름
			bus_stop_num_up = data["basic_info"]["bus_stop_num_up"]; // 버스 정류장 수 (상행)
			bus_stop_num_down = data["basic_info"]["bus_stop_num_down"]; // 버스 정류장 수 (하행)
			bus_stop_name_up = data["name_info_up"]; // 버스 정류장 이름 (상행)
			bus_stop_name_down = data["name_info_down"]; // 버스 정류장 이름 (하행)			
			
			$('.line_container').css({
				"background-color": "#f8c291"
			})
			draw_line("up");

		},
		error: function(xhr, status) {
			alert(xhr + ":" + status);
		}
	});

	$(".bus_info_container").css({
		"display": "block"
	});
}


// 상행박스 클릭 시 상행 노선 그리기
function up_title_func() {
	$('.line_container').css({
		"background-color": "#f8c291"
	})
	draw_line("up");
}


// 하행박스 클릭 시 하행 노선 그리기
function down_title_func() {
	$('.line_container').css({
		"background-color": "#74b9ff"
	})
	draw_line("down");
	$('.circle').css({"background-image": "url('images/circle_blue.png')"})
	$('.hline').css({"background-image": "url('images/hline_blue.png')"})
	$('.vline').css({"background-image": "url('images/vline_blue.png')"})
}


// 예상시간 툴팁 닫기
function closeLayer( obj ) {
	$(obj).parent().parent().hide();
}


// 클릭 시 툴팁 보여주기
$(function(){
	$(document).on("click",".circle",function()
	{
		
		$('#pred_time').text("분석 중..");
		var bus_stop_name_now = $(this).next().text();
		console.log(bus_stop_name_now);
		
		$.ajax({
			cache: false,
			url: 'pred_real_time?',
			type: 'GET',
			data: {"bus_stop_name_now" : bus_stop_name_now},

			success: function(data) {				
				var result = Math.floor(Number(data));
				if (isNaN(result) == true){
					result = data;
				} else {
					result = result + '분';
				}
				
				$('#pred_time').text(result);

			},
			error: function(xhr, status) {
				alert(xhr + ":" + status);
			}
		});
		
		var sWidth = window.innerWidth;
		var sHeight = window.innerHeight;

		var oWidth = $('.popup_layer').width();
		var oHeight = $('.popup_layer').height();

		// 클릭한 태그 근처에 레이어 보여주기
		var divLeft = $(this).offset().left - 72;
		var divTop = $(this).offset().top - 100;

		// 레이어가 화면 크기를 벗어나면 위치를 바꾸어 배치한다.
		if( divLeft + oWidth > sWidth ) divLeft -= oWidth;
		if( divTop + oHeight > sHeight ) divTop -= oHeight;

		// 레이어 위치를 바꾸었더니 상단기준점(0,0) 밖으로 벗어난다면 상단기준점(0,0)에 배치하자.
		if( divLeft < 0 ) divLeft = 0;
		if( divTop < 0 ) divTop = 0;

		$('.popup_layer').css({
			"top": divTop,
			"left": divLeft,
            "font-size": "1.7rem",
	        "font-family": 'Do Hyeon'
		}).show();
		
	});
	
});
