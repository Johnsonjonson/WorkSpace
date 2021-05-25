<%--
  Created by IntelliJ IDEA.
  User: JohnsonZhang
  Date: 2021/1/29
  Time: 12:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="entity.User" %>
<%@ page import="entity.UserData" %>
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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>病人详情</title>
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

            <a id="quit" href="QuitServlet">退出</a>
            <h1>商品管理系统</h1>
            <h3 style="font-size:28px;margin: 0 auto;height:30px;text-align:center;color:white;align-self: center;alignment: center">病人详情</h3>
            <ul id="menu">
                <li><a href="customersearch.jsp" id = "customer_m">超市管理</a></li>
            </ul>
        </div>
        <div id="content">
            <div id="main" style="margin: 0 auto;height:400px;text-align:center" ></div>
            <table>
                <tr>
                    <td>客户姓名</td>
                    <td><input type="text" name="name" disabled="disabled"
                               value="<%=user.getName()%>" /></td>
                </tr>
                <tr>
                    <td>联系电话</td>
                    <td><input type="text" name="tel" disabled="disabled"
                               value="<%=user.getTel()%>" /></td>
                </tr>
                <tr>
                    <td>家庭地址</td>
                    <td><input type="text" name="address" disabled="disabled"
                               value="<%=user.getAddress()%>" /></td>
                </tr>
                <tr class="cols2">
                    <td colspan="2" class="info"><%=request.getAttribute("msg") == null ? "" : request
                            .getAttribute("msg")%></td>
                </tr>
            </table>
        </div>
        <div id="footer"></div>
    </div>

<%--    <div id="main" style="width: 600px;height:400px;"></div>--%>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'),'dark');
        var menu = document.getElementById('menu');

        if("<%=role%>" === "admin"){
            menu.style.visibility = "visible"
        }else{
            menu.style.visibility = "hidden"
        }


        var isShowAlert = false
        function  updateData() {
            $.ajax({
                type: 'GET',
                url: 'PatientServlet',
                data: {
                    username:"<%=user.getName()%>",
                    id:"<%=user.getId()%>"
                },
                success: function (result) {
                    // alert("无该品类实时成交数据!"+result);
                    // alert("请求数据结果：" + result[1].name)
                    var timeData = new Array()
                    var bloodData = new Array()
                    var heartData = new Array()
                    var i = 0
                    resultData = result
                    if (result.length <= 0 && !isShowAlert ) {
                        alert("暂无数据!");
                        isShowAlert = true
                    }
                    result.forEach(function (value) {
                        // alert("请求数据结果：" + value.name + " " + value.num)
                        // nameData.join(value.name)
                        // valueData.join(value.num)
                        timeData[i] = value.time
                        bloodData[i] = value.blood
                        heartData[i] = value.heart
                        i++
                    })

                    // 指定图表的配置项和数据
                    var option = {
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {
                            data: ['血压', '心率']
                        },
                        xAxis: {
                            type: 'category',
                            data: timeData,
                            // boundaryGap: false,
                        },
                        yAxis: {
                            type: 'value'
                        },
                        series: [{
                            name: "血压",
                            data: bloodData,
                            type: 'line'
                        },
                            {
                                name: '心率',
                                type: 'line',
                                data: heartData
                            },
                        ]
                    };
                    myChart.setOption(option);
                    setTimeout(function () {
                        window.onresize = function () {
                            myChart.resize();
                        }
                    }, 200)
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
