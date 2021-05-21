<%@ page language="java" contentType="text/html"
		 pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	session.setAttribute("flag",false);//进入index页面，首先设置session内flag值为false，表示当前未登录。
	out.print(session.getAttribute("flag"));
	if(request.getParameter("user")!=null){//判断用户的填写状态{
//		if(request.getParameter("account").equals("1")){
			session.setAttribute("flag",true);//填写信息正确，设置session中flag值为true
			response.sendRedirect("index.jsp");//重定向到登录成功状态页面
		}

	response.setHeader("Cache-Control", "no-cache");
	response.setHeader("Cache-Control", "No-store");
	response.setDateHeader("Expires", 0);  //清除浏览器的缓存，防止session中的flag值回到index界面中无法更改为false
	//  out.print(session.getAttribute("flag"));
	//   out.print(session.getAttribute("flag").equals(false));
//	if(session.getAttribute("flag").equals(false)){//这样登出后即使后退页面到登录成功状态页面，判定session中flag值已被更改为false，直接跳转回登录页面index.jsp
//		response.sendRedirect("index.jsp");
//	}
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
<div id="container" align="center">
	<div id="content">
		<h1>物流地理信息系统</h1>
		<h2>登录</h2>
		<form action="LoginServlet" method="post">
			<table style="width:240px;height:200px;">
				<tr>
					<td>用户名</td>
					<td><input type="text" name="name"/></td>
				</tr>
				<tr>
					<td>密码</td>
					<td><input type="password" name="pwd"/></td>
				</tr>
				<tr>
					<td>验证码</td>
					<td><input type="text" name="checkcode"/></td>
				</tr>
				<tr>
					<td>点击刷新</td>
					<td><input type="image" name="img-code" id="img-code"
							   alt="看不清，点击换图" src="CheckCode"
							   onclick="javascript:this.src='CheckCode?+rand=Math.random()'"></td>
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
</div>
</body>
</html>
<script language="JavaScript">

	// javascript:window.history.forward(1);

</script>