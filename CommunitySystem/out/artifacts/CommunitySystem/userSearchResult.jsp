<%@page import="entity.User"%>
<%@page import="java.util.List"%>
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
<title>用户管理</title>
</head>
<body>
	<div id="container">
		<div id="header">
			<img src="img/icon.png" align="left" height="140" width="140">
			<a id="quit" href="QuitServlet">退出</a>
			<h1>社区管理系统</h1>
			<h3 style="font-size:28px;margin: 0 auto;height:30px;text-align:center;color:white;align-self: center">用户列表</h3>
		</div>
		<div id="content">
			<table>
				<thead>
					<tr>
						<td>用户</td>
						<td>角色</td>
						<td>操作</td>
					</tr>
				</thead>
				<%
					List<User> users = (List<User>) request.getAttribute("users");
					if(users!=null && users.size()>0){
						for (User user : users) {
				%>
				<tr class="result">
					<td><%=user.getName() %></td>
					<td><%=user.getChildTel() %></td>
					<td><a href="UserServlet?m=delete&cid=<%=user.getId()%>">删除</a></td>
				</tr>
				<%
						}
					}
				%>



				<tr class="cols2">
					<td colspan="3"><input type="button" value="返回" onclick="history.back(-1);" /></td>
				</tr>
				<tr class="cols2">
					<td colspan="3" class="info"><%=request.getAttribute("msg")==null?"":request.getAttribute("msg") %></td>
				</tr>
			</table>
		</div>
		<div id="footer">
			<img src="img/icon.png" align="right" height="120" width="120">
		</div>
	</div>
</body>
</html>