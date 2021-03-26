<%@ page import="entity.User" %>
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
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<base href="<%=basePath%>">
	<link rel="stylesheet" href="styles.css">
	<style>
		body,
		html,
		#content{
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
			z-index: 100;
		}
		h1 {
			margin: 0 auto;
			width: 300px;
			padding-top: 10px;
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
	</style>
	<title>首页</title>
</head>
<body>
<%
	User user = (User)request.getAttribute("user");
%>
<div id="container" align="center">
	<div id="content">
		<h1>物流地理信息系统</h1>
		<h2>修改密码</h2>
		<form action="LoginServlet" method="post">
			<table style="width:240px;height:200px;">
				<tr>
					<td>用户ID</td>
					<td><input type="text" name="name"/>"<%user.getId();%>"</td>
				</tr>
				<tr>
					<td>密码</td>
					<td><input type="password" name="pwd"/></td>
				</tr>
				<tr>
					<td>新密码</td>
					<td><input type="password" name="pwd"/></td>
				</tr>
				<tr class="cols2">
					<td colspan="2">
						<input type="submit" value="登录" />
					</td>
				</tr>
				<tr class="cols2">
					<td colspan="2" class="info"><%=request.getAttribute("msg")==null?"":request.getAttribute("msg") %></td>
				</tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>
<script language="JavaScript">

	// javascript:window.history.forward(1);

</script>