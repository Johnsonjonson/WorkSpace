<%--
  Created by IntelliJ IDEA.
  User: JohnsonZhang
  Date: 2021/1/29
  Time: 12:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="entity.User" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page language="java" contentType="text/html"
         pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <style>
        table,table tr th, table tr td { border:1px solid #0094ff; }
        table { width: 200px; min-height: 25px; line-height: 25px; text-align: center; border-collapse: collapse;}
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>数据</title>
    <script src="js/echarts.min.js"></script>
    <script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
    <link rel="stylesheet" href="styles.css">
</head>
<body>

    <%
        User user = (User) request.getAttribute("user");
        String role = (String)request.getAttribute("role");
    %>
    <div id="container">
        <div id="header">
            <img src="img/icon.png" align="left" height="140" width="140">
            <a id="quit" href="QuitServlet">退出</a>
            <h1>寝室管理系统</h1>
            <h3 style="font-size:28px;margin: 0 auto;height:30px;text-align:center;color:white;align-self: center;alignment: center">寝室详情</h3>
            <ul id="menu">
                <li><a href="UserServlet?m=search" id = "customer_m" >学生管理</a></li>
            </ul>
        </div>
        <div id="content">
<%--            <div id="main" style="margin: 0 auto;height:400px;text-align:center" ></div>--%>
            <table border="1" cellspacing="0">
                <tr>
                    <td>温度</td>
                    <td id="temp"></td>
                </tr>
                <tr>
                    <td>湿度</td>
                    <td id="humdity"></td>
                </tr>
                <tr>
                    <td>实时人数</td>
                    <td id="realTimeNum"></td>
                </tr>
                <tr class="cols2">
                    <td colspan="2" class="info"><%=request.getAttribute("msg") == null ? "" : request
                            .getAttribute("msg")%></td>
                </tr>
            </table>
        </div>
        <div id="footer">
<%--            <img src="img/icon.png" align="right" height="120" width="120">--%>
        </div>
    </div>

<%--    <div id="main" style="width: 600px;height:400px;"></div>--%>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        // var myChart = echarts.init(document.getElementById('main'),'dark');
        var menu = document.getElementById('menu');
        var temp = document.getElementById('temp');
        var humdity = document.getElementById('humdity');
        var realTimeNum = document.getElementById('realTimeNum');
        var role = "<%=user.getRole()%>"
        if(role === "teacher"){
            menu.style.visibility = "visible"
        }else{
            menu.style.visibility = "hidden"
        }
        var isShowAlert = false

        function  updateData() {
            $.ajax({
                type: 'GET',
                url: 'DataServlet',
                data: {
                },
                success: function (result) {
                    // alert("请求结果!"+result.temp+" "+result.humdity);
                    // alert("请求数据结果：" + result[1].name)
                    // temp.innerText = result.temp
                    // humdity.innerText = result.humdity
                    // realTimeNum.innerText = result.realTimeNum
                },
                error: function (errorMsg) {
                    alert("请求数据失败!");
                },
                dataType: "json"
            });
        }
        updateData()
        setInterval(function (){
            updateData()
        },1000)
        // 使用刚指定的配置项和数据显示图表。
        // myChart.setOption(option);
    </script>
</body>
</html>
