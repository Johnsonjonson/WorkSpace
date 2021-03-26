<%--
  Created by IntelliJ IDEA.
  User: JohnsonZhang
  Date: 2021/2/1
  Time: 14:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>医生</title>
    <link rel="stylesheet" href="styles.css">
    <script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
</head>
<body>
<div id="container">
    <div id="header">
        <img src="img/icon.png" align="left" height="140" width="140">
        <a id="quit" href="QuitServlet">退出</a>
        <h1>医院管理系统</h1>
        <h3 style="font-size:28px;margin: 0 auto;height:30px;text-align:center;color:white;align-self: center">医生</h3>
        <ul id="menu">
            <li><a href="customersearch.jsp">客户管理</a></li>
        </ul>
    </div>
    <div id="content">
        <form action="CustomerServlet?m=search" method="post">
            <table>
                <tr>
                    <td>客户姓名</td>
                    <td><input type="text" name="customerName"/></td>
                </tr>

                <tr class="cols2">
                    <td colspan="2"><input type="submit" value="查询" /><input
                            type="reset" value="重置" /></td>
                </tr>
                <tr class="cols2">
                    <td colspan="2"><a href="CustomerServlet?m=toAdd">添加客户</a></td>
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
</body>
</html>
