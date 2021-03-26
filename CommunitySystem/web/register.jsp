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
        <img src="img/icon.png" align="left" height="140" width="140">
        <h1>社区管理系统</h1>
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
                <tr>
                    <td>年龄</td>
                    <td><input type="text" name="age"/></td>
                </tr>
                <tr>
                    <td>性别</td>
                    <td><input type="text" name="sex"/></td>
                </tr>
                <tr>
                    <td>联系电话</td>
                    <td><input type="text" name="tel"/></td>
                </tr>
                <tr>
                    <td>子女联系电话</td>
                    <td><input type="text" name="childTel"/></td>
                </tr>
                <tr>
                    <td>基本病症</td>
                    <td><input type="text" name="baseInfo"/></td>
                </tr>
<%--                <tr><!--单选按钮-->--%>
<%--                    <td><input type="radio" name="role" value="student" checked="checked">学生</td>--%>
<%--                    <td><input type="radio" name="role" value="dormitory">宿管</td>--%>
<%--                </tr>--%>
                <tr class="cols2">
                    <td colspan="2">
                        <input type="submit" value="注册">
                    </td>
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
