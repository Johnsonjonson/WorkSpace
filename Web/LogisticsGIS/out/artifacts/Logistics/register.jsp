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
</head>

<body>
<div id="container" align="center">
<%--    <div id="header">--%>
<%--        <img src="img/icon.png" align="left" height="140" width="140">--%>
<%--        <h1>物流地理信息系统</h1>--%>
<%--    </div>--%>
    <div id="content">
        <h1>物流地理信息系统</h1>
        <h2>注册</h2>
        <form action="RegisterServlet" method="post">
            <table style="width:300px;height:200px;">
                <tr>
                    <td>用户名：</td>
                    <td><input type="text" name="username"/></td>
                </tr>
                <tr>
                    <td>密码</td>
                    <td><input type="password" name="password"/></td>
                </tr>
                <tr><!--单选按钮-->
                    <td colspan="2">
                        <input type="radio" name="role" value="student" checked="checked">学生</input>
                        <input type="radio" name="role" value="teacher">老师</input>
                    </td>
                </tr>
                <tr class="cols2">
                    <td colspan="2">
                        <input type="submit" value="注册">
                    </td>
                </tr>
            </table>


        </form>
    </div>
<%--    <div id="footer">--%>
<%--        <img src="img/icon.png" align="right" height="120" width="120">--%>
<%--    </div>--%>
</div>
</body>
</html>
