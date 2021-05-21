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
        table { width: 200px; min-height: 30px; line-height: 30px; text-align: center; border-collapse: collapse;}
        h1 {
            margin: 0 auto;
            width: 240px;
            padding-top: 40px;
            color: yellow;
            font-size: 36px;
            text-align:center
        }

        h2 {
        	margin: 0 auto;
        	width: 240px;
        	padding-top: 10px;
        	color: white;
        	font-size: 30px;
            text-align:center

        }
    </style>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>数据</title>
    <script src="js/echarts.min.js"></script>
    <script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
    <link rel="stylesheet" href="styles.css">
<%--    <script src="js/jquery-1.8.3.min.js"></script>--%>
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
            <h1>社区管理系统</h1>
            <h2>用户详情</h2>
<%--            <h3 style="font-size:28px;margin: 0 auto;height:30px;text-align:center;color:white;align-self: center;alignment: center">用户详情</h3>--%>
            <ul id="menu">
                <li><a href="UserServlet?m=search" id = "customer_m" >用户管理</a></li>
            </ul>
        </div>
        <div id="content">
<%--            <div id="main" style="margin: 0 auto;height:400px;text-align:center" ></div>--%>
            <table border="1" cellspacing="0" style="width: 500px">
                <tr>
                    <td>姓名</td>
                    <td id="name1"></td>
                </tr>
                <tr>
                    <td>性别</td>
                    <td id="sex"></td>
                </tr>
                <tr>
                    <td>年龄</td>
                    <td id="age"></td>
                </tr>
                <tr>
                    <td>联系方式</td>
                    <td id="tel"></td>
                </tr>
                <tr>
                    <td>子女联系方式</td>
                    <td id="childtel"></td>
                </tr>
                <tr>
                    <td>基本病症</td>
                    <td id="baseinfo"></td>
                </tr>
                <tr>
                    <td>红外数据</td>
                    <td id="havePeople"></td>
                </tr>
                <tr>
                    <td>温度</td>
                    <td id="temp"></td>
                </tr>
                <tr>
                    <td>血氧浓度</td>
                    <td id="blood"></td>
                </tr>
                <tr>
                    <td>心率</td>
                    <td id="heart"></td>
                </tr>
                <tr class="cols2">
                    <td colspan="2" class="info"><%=request.getAttribute("msg") == null ? "" : request
                            .getAttribute("msg")%></td>
                </tr>
            </table>
        </div>
        <div id="footer">
            <img src="img/icon.png" align="right" height="120" width="120">
        </div>
    </div>

<%--    <div id="main" style="width: 600px;height:400px;"></div>--%>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        // var myChart = echarts.init(document.getElementById('main'),'dark');
        var menu = document.getElementById('menu');
        var name1 = document.getElementById('name1');
        var sex = document.getElementById('sex');
        var age = document.getElementById('age');
        var tel = document.getElementById('tel');
        var childtel = document.getElementById('childtel');
        var baseinfo = document.getElementById('baseinfo');
        var temp = document.getElementById('temp');
        var blood = document.getElementById('blood');
        var heart = document.getElementById('heart');
        var havePeople = document.getElementById('havePeople');
        <%--var role = "<%=user.getTel()%>"--%>
        <%--if(role === "dormitory"){--%>
        menu.style.visibility = "hidden"
        <%--}else{--%>
        <%--    menu.style.visibility = "hidden"--%>
        <%--}--%>
        var isShowAlert = false
        var isShowAlarm = false

        function  updateData() {
            $.ajax({
                type: 'GET',
                url: 'DataServlet',
                data: {
                },
                success: function (result) {
                    // alert("请求结果!"+result.temp+" "+result.humdity);
                    // alert("请求数据结果：" + result[1].name)这里！！！！！！！！！！InfoData
                    console.log(" 测试 " + result.isAlarm)
                    name1.innerText = "<%=user.getName()%>"
                    sex.innerText = "<%=user.getSex()%>"
                    age.innerText = "<%=user.getAge()%>"
                    tel.innerText = "<%=user.getTel()%>"
                    childtel.innerText = "<%=user.getChildTel()%>"
                    temp.innerText = result.temp
                    blood.innerText = result.blood
                    heart.innerText = result.heart
                    baseinfo.innerText = "<%=user.getBaseInfo()%>"
                    havePeople.innerText = result.havePeople
                    if(result.isAlarm == "1"){
                        alert("老人一键报警");
                    }else{
                        if(result.temp > 27){
                            alert("老人温度过高,"+"温度:"+ result.temp+"℃");
                        }
                    }

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
