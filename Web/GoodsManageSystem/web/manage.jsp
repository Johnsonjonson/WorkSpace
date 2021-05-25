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
<link rel="stylesheet" href="styles.css">
<title>快速入库</title>
</head>
<body>
	<div id="container">
		<div id="header">

			<a id="quit" href="QuitServlet">退出</a>
			<h1>商品管理系统</h1>
			<ul id="menu">
				<li><a href="warehousing.jsp">快速入库</a></li>
				<li><a href="transaction.jsp">交易处理</a></li>
				<li><a href="manage.jsp">超市管理</a></li>
			</ul>
		</div>
		<div id="content">
			商品管理
		</div>
		<div id="footer"></div>
	</div>
</body>
</html>