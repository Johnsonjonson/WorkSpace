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
	<title>首页</title>
</head>
<body>
<div id="container">
	<div id="header">
<%--		<img src="img/icon.png" align="left" height="140" width="140">--%>
		<h1>寝室管理系统</h1>
	</div>
	<div id="content">
		<form action="LoginServlet" method="post">
			<table style="width:400px;height:200px;">
				<tr>
					<td>用户名</td>
					<td><input type="text" name="name"/></td>
				</tr>
				<tr>
					<td>密码</td>
					<td><input type="password" name="pwd"/></td>
				</tr>
				<tr class="cols2">
					<td colspan="2">
						<input type="submit" value="登录" />
						<input type="reset" value="重置" />
						<input type="button" value="注册"
							   onclick="javascript:window.parent.location.href='${pageContext.request.contextPath }/register.jsp'"/>
					</td>
				</tr>
				<tr class="cols2">
					<td colspan="2" class="info"><%=request.getAttribute("msg")==null?"":request.getAttribute("msg") %></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="footer">
		<img src="img/icon.png" align="right" height="120" width="120">
	</div>
</div>
</body>
</html>