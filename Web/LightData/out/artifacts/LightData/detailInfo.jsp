<%--
  Created by IntelliJ IDEA.
  User: JohnsonZhang
  Date: 2021/1/29
  Time: 12:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="entity.User" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="dao.DataDao" %>
<%@ page import="entity.InfoData" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="java.io.File" %>
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
        .slide-btn{
            width:100px;
            display:inline-block;
            border:1px solid;
            border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
            border-radius:5px;
            position: relative;
            overflow: hidden;
            cursor: pointer;
        }
        .slide-btn .inner-on,.slide-btn .inner-off{
            width:151px;
            box-sizing:border-box;
            display: inline-block;
            position: relative;
            left:0;
            cursor: pointer;
            transition:left 0.5s;
        }
        .inner-on .left,.inner-off .left{
            width: 50px;
            color: #fff;
            text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
            background:#58b058;
            text-align:center;
            display: inline-block;
            padding: 4px 0;
        }
        .inner-on .space,.inner-off .space{
            width: 51px;
            display: inline-block;
            box-sizing:border-box;
            padding: 4px 0;
            color: #fff;
            background-color: #f5f5f5;
            border-left: 1px solid #cccccc;
            border-right: 1px solid #cccccc;
            text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
            background-image: linear-gradient(to bottom, #ffffff, #e6e6e6);
            border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
        }
        .inner-on .right,.inner-off .right{
            width: 50px;
            color: #fff;
            text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
            background:#f9a123;
            text-align:center;
            display: inline-block;
            padding: 4px 0;
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
            <a id="quit" href="QuitServlet">退出</a>
            <h1>光照数据</h1>
            <h3 style="font-size:28px;margin: 0 auto;height:30px;text-align:center;color:white;align-self: center;alignment: center">数据详情</h3>
        </div>
        <div id="content">
<%--            <div id="main" style="margin: 0 auto;height:400px;text-align:center" ></div>--%>
            <table border="1" cellspacing="0">
                <tr>
                    <td>光照数据</td>
                    <td id="light">1</td>
                </tr>
                <tr>
                    <td>时间</td>
                    <td id="time"></td>
                </tr>
                <tr>
                    <td>灯开关</td>
                    <td id="switch">
                        开<div class="slide-btn">
                        <div class="inner-on" id="inner">
                            <input style="display:none;" type="checkbox" checked>
                            <span class="left">ON</span><span class="space">&nbsp;</span><span class="right">OFF</span>
                        </div>
                    </div>关
                    </td>
                </tr>
                <tr class="cols2">
                    <td colspan="2" class="info"><%=request.getAttribute("msg") == null ? "" : request
                            .getAttribute("msg")%></td>
                </tr>
            </table>

        </div>
        <div id="footer">
        </div>
    </div>

<%--    <div id="main" style="width: 600px;height:400px;"></div>--%>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        // var myChart = echarts.init(document.getElementById('main'),'dark');

        function  updateSwitch(switchStatus) {
            console.log("开关状态"+switchStatus)
            $.ajax({
                type: "get",
                url: 'http://129.204.232.210:8552/switch?',
                data:{
                    switchStatus:switchStatus+"",
                },
                dataType: "jsonp",
                jsonpCallback: "success_jsonpCallback",
                success: (msg) => {
                    //执行方法
                    // this.handMarker(msg.data);
                    alert(msg)
                },

            });
        }

        document.getElementById("inner").onclick = function() {
            if (this.className == "inner-on") {
                this.style.left = -51 + "px";
                this.childNodes[1].checked = false;
                this.className = "inner-off";
                updateSwitch(1)
            }else{
                this.style.left = 0;
                this.childNodes[1].checked = true;
                this.className = "inner-on";
                updateSwitch(0)
            }
        }

        var light = document.getElementById('light');
        var time = document.getElementById('time');

        function  updateData() {
            $.ajax({
                type: 'GET',
                url: 'DataServlet',
                data: {
                },
                success: function (result) {
                    // alert("请求结果!"+result.temp+" "+result.humdity);
                    // alert("请求数据结果：" + result[1].name)
                    light.innerText = result.light
                    time.innerText = result.time
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
