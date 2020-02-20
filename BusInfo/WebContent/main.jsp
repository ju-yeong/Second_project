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
			<option value="">��/�� ����</option>
			<option value="��⵵" selected>��⵵</option>
		</select>
        <select name="sigungo" class="sigungo">
			<option value="">��/��/�� ����</option>
			<option value="������" selected>������</option>			
			<option value="����">����</option>
			<option value="����">����</option>
			<option value="��õ��">��õ��</option>
			<option value="�����">�����</option>
			<option value="���ֽ�">���ֽ�</option>
			<option value="������">������</option>
			<option value="������">������</option>
			<option value="������">������</option>
			<option value="�����ֽ�">�����ֽ�</option>
			<option value="����õ��">����õ��</option>
			<option value="��õ��">��õ��</option>
			<option value="������">������</option>
			<option value="�����">�����</option>
			<option value="�Ȼ��">�Ȼ��</option>
			<option value="�ȼ���">�ȼ���</option>
			<option value="�Ⱦ��">�Ⱦ��</option>
			<option value="���ֽ�">���ֽ�</option>
			<option value="����">����</option>
			<option value="���ֽ�">���ֽ�</option>
			<option value="��õ��">��õ��</option>
			<option value="�����">�����</option>
			<option value="���ν�">���ν�</option>
			<option value="�ǿս�">�ǿս�</option>
			<option value="�����ν�">�����ν�</option>
			<option value="��õ��">��õ��</option>
			<option value="���ֽ�">���ֽ�</option>
			<option value="���ý�">���ý�</option>
			<option value="��õ��">��õ��</option>
			<option value="�ϳ���">�ϳ���</option>
			<option value="ȭ����">ȭ����</option>
		</select>
        <span class="form_wrap">
            <form id="form" name="form" method="post">
				<input type="number" id="bus_num" placeholder="������ȣ" name="bus_num">
				<button type="submit" id="btn_search" onclick="bus_num_func(); return false;"><img src="images/icon_search.png" class="icon_search"></button>
			</form>
        </span>
    </div>
	<div class="bus_info_container">
		<div class="up_title" onclick="up_title_func(); return false;">
			<p id="up_title">����</p>
		</div>
		<div class="down_title" onclick="down_title_func(); return false;">
			<p id="down_title">����</p>
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
				���� ���� <p id="pred_time">�м� ��..</p>
			</div>
		</div>
		<div>
			<span onclick="closeLayer(this)" style="cursor:pointer;font-size:1.7rem" title="�ݱ�"><img class = "icon_close" src="images/icon_close.png" alt="" ></span>
		</div>
	</div>


</body>
</html>