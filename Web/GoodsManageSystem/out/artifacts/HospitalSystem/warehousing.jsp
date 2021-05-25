<%@ page language="java" contentType="text/html; charset=UTF-8"
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
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<base href="<%=basePath%>">
<link rel="stylesheet" href="map.css">
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
		#header {
			position: relative;
			height: 140px;
			/*background-color: #FF0000;*/
			/*opacity: 35%;*/
			background:url("img/head.jpg")
		}
		#header h1 {
			margin: 0 auto;
			width: 240px;
			padding-top: 40px;
			color: white;
			font-size: 36px;
			text-align:center
		}
		#quit {
			float: right;
			padding-top: 5px;
			padding-right: 10px;
		}

		#menu {
			position: absolute;
			bottom: 5px;
		}

		#menu li {
			float: left;
			margin-left: 10px;
			list-style: none;
		}
		/*h1 {*/
		/*	margin: 0 auto;*/
		/*	width: 300px;*/
		/*	padding-top: 10px;*/
		/*	color:  rgba(27, 142, 236, 1);*/
		/*	font-size: 30px;*/
		/*}*/

		h2 {
			margin: 0 auto;
			width: 240px;
			padding-top: 5px;
			color: black;
			font-size: 28px;
		}
	</style>
<title>快速入库</title>
</head>
<body>
	<div id="container" align="center">
		<div id="header">

			<a id="quit" href="QuitServlet">退出</a>
			<h1 align="center">商品管理系统</h1>
<%--			<ul id="menu">--%>
<%--				<li><a href="warehousing.jsp">快速入库</a></li>--%>
<%--				<li><a href="transaction.jsp">交易处理</a></li>--%>
<%--				<li><a href="manage.jsp">超市管理</a></li>--%>
<%--			</ul>--%>


		</div>
		<div id="content">
			<!--选项的头部-->
			<div id="tab-header">
				<ul>
					<li class="selected" onclick = "tabClick(0)">快速入库</li>
					<li onclick = "tabClick(1)">交易处理</li>
					<li onclick = "tabClick(2)">超市管理</li>
				</ul>
			</div>
			<!--主要内容-->

			<div id="tab-content">
				<div class="dom" id="dom" style="display: block;position: absolute;width: 400px;top:80px;left: 20px;margin: 0;">
					
					请刷卡，快速录入商品

				</div>
				<div class="dom">
					<ul>
						<li>
							<a href="#" style="align-self: center">暂无内容，敬请期待</a>
						</li>
					</ul>
				</div>
				<div class="dom">
					<ul>
						<li>
							<a href="#" style="align-self: center">暂无内容，敬请期待</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
<%--		<div id="content">--%>
<%--			快速入库--%>
<%--		</div>--%>
		<div id="footer"></div>
	</div>

	<script type="text/javascript">
		function G(id) {
			return document.getElementById(id);
		}
		function tabClick(index){
			// 拿到所有的标题(li标签) 和 标题对应的内容(div)
			var titles = G('tab-header').getElementsByTagName('li');
			var divs = G('tab-content').getElementsByClassName('dom');
			// 判断
			if(titles.length != divs.length) return;
			// 遍历
			for(var j=0; j<titles.length; j++){
				titles[j].className = '';
				divs[j].style.display = 'none';
			}
			titles[index].className = 'selected';
			divs[index].style.display = 'block';
		}
	</script>
</body>
</html>