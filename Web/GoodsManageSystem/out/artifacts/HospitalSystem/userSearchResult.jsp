<%@page import="entity.User"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<base href="<%=basePath%>">
<link rel="stylesheet" href="styles.css">
	<style>
		body,
		html,
		#container{
			width: 100%;
			height: 100%;
			padding: 0;
			margin: 0;
			overflow: hidden;
		}
		#content{
			padding: 7px 10px;
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			margin: auto;
			width: 90%;
			height: 90%;
			background: #fff;
			box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
			border-radius: 7px;
			z-index: 100;
		}
		#modityPwd{
			padding: 7px 10px;
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			margin: auto;
			width: 300px;
			height: 400px;
			background: #fff;
			box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
			border-radius: 7px;
			z-index: 120;
		}
		h1 {
			margin: 0 auto;
			width: 800px;
			padding-top: 40px;
			color:  rgba(27, 142, 236, 1);
			font-size: 30px;
		}

		h2 {
			margin: 0 auto;
			width: 240px;
			padding-top: 5px;
			color: black;
			font-size: 28px;
		}
		table,table tr th, table tr td { border:1px solid #0094ff; }
		table { width: 400px; min-height: 25px; line-height: 25px; text-align: center; border-collapse: collapse;margin: 40px;}
	</style>
	<title>用户管理</title>
	<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
	<script src="js/xlsx.core.min.js"></script>
</head>
<body>
	<%
		List<User> users = (List<User>) request.getAttribute("users");
	%>

	<div id="modityPwd" align="center">
		<%--    <h1>物流地理信息系统</h1>--%>
		<img src="img/close.png" align="right" height="20" width="20" onclick="closeModifyPanel()">
		<h2>修改学生信息</h2>
		<form action="ModifyServlet" method="post">
			<table style="width:240px; align-self: center">
				<tr>
					<td>ID</td>
					<td><b id="id">""</b></td>
				</tr>
				<tr>
					<td>姓名</td>
					<td><input type="text" id="name" value=""/></td>
				</tr>
				<tr>
					<td>学号</td>
					<td><input type="text"  id="no" value=""/></td>
				</tr>
				<tr>
					<td>班级</td>
					<td><input type="text" id="class" value=""/></td>
				</tr>
				<tr>
					<td>性别</td>
					<td><input type="text" id="sex" value=""/></td>
				</tr>
				<tr class="cols2">
					<td colspan="2">
						<input type="button" value="确定"
							   onclick="request_modify()"/>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="container" align="center">
		<div id="header">
			<a id="quit" href="QuitServlet">退出</a>
		</div>

		<div id="content" align="center" style="width:90%; height:90%; overflow:auto;">
			<h1>物流地理信息系统</h1>
			<h2>用户列表</h2>
			<table style="width:700px; height:225px;">
				<thead>
					<tr>
						<td>姓名</td>
						<td>角色</td>
						<td>编辑</td>
						<td>删除</td>
					</tr>
				</thead>
				<%
					int index = 0;
					if(users!=null && users.size()>0){
						for (User user : users) {
				%>
				<tr class="result">
					<td><%=user.getName() %></td>
					<td><%=user.getNo() %></td>
					<td><a href="javascript:void(0)" onclick="openEditPop(<%=user.getId()%>)">编辑</a></td>
					<td><a href="javascript:void(0)" onclick="deleteUser(<%=user.getId()%>)">删除</a></td>
<%--					<td><a href="javascript:void(0)" href="UserServlet?m=delete&cid=<%=user.getId()%>">删除</a></td>--%>
				</tr>
				<%
							index ++;
						}
					}
				%>
				<tr class="cols2">
					<td colspan="2"><input type="file" value="导入" onchange="importf(this)" /></td>
					<td colspan="3"><input type="button" value="返回" onclick="history.back(-1);" /></td>
				</tr>
				<tr class="cols2">
					<td colspan="4" class="info"><%=request.getAttribute("msg")==null?"":request.getAttribute("msg") %></td>
				</tr>
			</table>
		</div>
		<p id="excelcontent"></p>
	</div>
</body>
</html>
<script>
	function G(id) {
		return document.getElementById(id);
	}
	var modifyInfo = G("modityPwd")
	modifyInfo.style.visibility = "hidden"
	function closeModifyPanel() {
		G("modityPwd").style.visibility = "hidden"
	}
	function openEditPop(id){
		$.ajax({
			type: 'GET',
			url: 'UserServlet',
			data: {
				id:id,
				m:"querybyid"
			},
			success: function (user) {
				if(user!=null && user!=""){
					modifyInfo.style.visibility = "visible"
					G("id").innerText = user.id
					G("name").value = user.name
					if(user.sclass!=null && user.sclass!="undefined")
						G("class").value = user.sclass
					else
						G("class").value = ""
					if(user.no!=null && user.no!="undefined")
						G("no").value = user.no
					else
						G("no").value = ""
					if(user.sex!=null && user.sex!="undefined")
						G("sex").value = user.sex
					else
						G("sex").value = ""
				}else{
					alert("查询出错，请重试")
				}
			},
			error: function (errorMsg) {
				alert("查询出错，请重试")
			},
			dataType: "json"
		});
		console.log("user" + id)
	}

	function request_modify(id){
		var id = G("id").innerText
		var name = G("name").value
		var sclass =G("class").value
		var no =G("no").value
		var sex =G("sex").value
		$.ajax({
			type: 'GET',
			url: 'UserServlet',
			data: {
				id:id,
				name:encodeURI(name),
				sclass:encodeURI(sclass),
				no:encodeURI(no),
				sex:encodeURI(sex),
				m:"modify_info"
			},
			success: function (result) {
				if (result.errorcode == "0"){
					modifyInfo.style.visibility = "visible"
					alert(result.modifymsg)
				}else if (result.errorcode == "1"){
					modifyInfo.style.visibility = "hidden"
					alert("修改信息成功")
				}
			},
			error: function (errorMsg) {
				alert("查询出错，请重试")
			},
			dataType: "json"
		});
		console.log("user" + id)
	}
			<%--?m=delete&cid=<%=user.getId()%>--%>
	function deleteUser(cid){
		$.ajax({
			type: 'GET',
			url: 'UserServlet',
			data: {
				cid:cid,
				m:"delete"
			},
			success: function (result) {
				if (result.errorcode == "0"){
					alert(result.modifymsg)
				}else if (result.errorcode == "1"){
					alert("删除用户成功")
				}
				location.reload()
			},
			error: function (errorMsg) {
				alert("删除失败，请重试")
			},
			dataType: "json"
		});
		console.log("user" + id)
	}

	var wb;//读取
	var rabs = false;
	//开始导入
	function importf(obj) {
		if(!obj.files) {
			return;
		}
		var f = obj.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			var data = e.target.result;
			if(rabs) {
				wb = XLSX.read(btoa(fixdata(data)), {//手动转化
					type: 'base64'
				});
			} else {
				wb = XLSX.read(data, {
					type: 'binary'
				});
			}
			/**
			 * wb.sheetnames[0]是获取sheets中第一个sheet的名字
			 * wb.sheets[sheet名]获取第一个sheet的数据
			 */
			var exceljson =[]
			for(const sheet in wb.Sheets) {
				console.log("====="+sheet)
				if (wb.Sheets.hasOwnProperty(sheet)) {
					console.log("====="+XLSX.utils.sheet_to_json(wb.Sheets[sheet]))
					exceljson = exceljson.concat(XLSX.utils.sheet_to_json(wb.Sheets[sheet]));
				}
			}
			console.log("=====----------"+JSON.stringify(exceljson))
			// document.getElementById("excelcontent").innerhtml= JSON.stringify(exceljson);
			insert_student(JSON.stringify(exceljson))
		};
		if(rabs) {
			reader.readAsArrayBuffer(f);
		} else {
			reader.readAsBinaryString(f);
		}
	}

	//文件流转binarystring
	function fixdata(data) {
		var o = "",
				l = 0,
				w = 10240;
		for(; l < data.byteLength / w; ++l) o += String.fromCharCode.apply(null, new Uint8Array(data.slice(l * w, l * w +

				w)));
		o += String.fromCharCode().apply(null, new Uint8Array(data.slice(l * w)));
		return o;
	}

	function insert_student(studentInfo){
		$.ajax({
			type: 'GET',
			url: 'UserServlet',
			data: {
				students:encodeURI(studentInfo),
				m:"insert_student"
			},
			success: function (result) {
				if (result.errorcode == "0"){
					// modifyInfo.style.visibility = "visible"
					alert(result.modifymsg)
				}else if (result.errorcode == "1"){
					// modifyInfo.style.visibility = "hidden"
					alert("添加学生成功")
				}
				location.reload()
			},
			error: function (errorMsg) {
				alert("查询出错，请重试")
			},
			dataType: "json"
		});
		console.log("user" + id)
	}
</script>