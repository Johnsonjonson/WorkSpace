<%--
  Created by IntelliJ IDEA.
  User: JohnsonZhang
  Date: 2021/1/29
  Time: 14:23
  To change this template use File | Settings | File Templates.
--%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>注册</title>
    <base href="<%=basePath%>">
    <link rel="stylesheet" href="styles.css">
</head>

<body style="text-align:center;">
<div id="container">
    <div id="header">
        <h1>传感器数据</h1>
    </div>
    <div id="content">
        <form action="RegisterServlet" method="post">
            <table style="width:400px;height:200px;">
                <tr>
                    <td>用户名：</td>
                    <td><input type="text" name="username"/></td>
                </tr>
                <tr>
                    <td>密码</td>
                    <td><input type="password" name="password"/></td>
                </tr>
                <tr class="cols2">
                    <td colspan="2">
                        <input type="submit" value="注册">
                    </td>
                </tr>
            </table>


        </form>
    </div>
    <div id="footer">
    </div>
</div>
</body>
</html>
