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
<%@ page import="utils.ExcelHelper" %>
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
            <h1>传感器数据</h1>
            <h3 style="font-size:28px;margin: 0 auto;height:30px;text-align:center;color:white;align-self: center;alignment: center">数据详情</h3>
        </div>
        <div id="content">
<%--            <div id="main" style="margin: 0 auto;height:400px;text-align:center" ></div>--%>
            <table border="1" cellspacing="0">
                <tr>
                    <td>id</td>
                    <td id="id">1</td>
                </tr>
                <tr>
                    <td>x</td>
                    <td id="xdata"></td>
                </tr>
                <tr>
                    <td>y</td>
                    <td id="ydata"></td>
                </tr>
                <tr>
                    <td>z</td>
                    <td id="zdata"></td>
                </tr>
                <tr>
                    <td>震动数据</td>
                    <td id="zhendongdata"></td>
                </tr>
                <tr>
                    <td>time</td>
                    <td id="time"></td>
                </tr>
                <tr>
                    <td>使用时间</td>
                    <td id="use_time"></td>
                </tr>
                <tr>
                    <td>加速度传感器</td>
                    <td>正常</td>
                </tr>
                <tr>
                    <td>震动传感器</td>
                    <td>正常</td>
                </tr>

                <tr class="cols2">
                    <td colspan="2" class="info"><%=request.getAttribute("msg") == null ? "" : request
                            .getAttribute("msg")%></td>
                </tr>
            </table>

<%--    <%--%>
<%--        DataDao sku = new DataDao();--%>
<%--        List<InfoData> skuList = sku.getAllData();--%>
<%--        ExcelHelper.createExcel(skuList); //查询的结果，便于插入Excel填充数据--%>
<%--    %>--%>
<%--    <form action="ExcelServlet" method="post">--%>
<%--        <select name="file">--%>
<%--            <option value="/Users/zhangqiangsheng/apache-tomcat-7.0.107/temp/info.xls">  //从服务器指定位置下载历史数据--%>
<%--            </option>--%>
<%--        </select>--%>
<%--        <input type="submit" value="下载">--%>
<%--    </form>--%>
        </div>
        <div id="footer">
<%--            <img src="img/icon.png" align="right" height="120" width="120">--%>
        </div>
    </div>

<%--    <div id="main" style="width: 600px;height:400px;"></div>--%>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        // var myChart = echarts.init(document.getElementById('main'),'dark');
        var xdata = document.getElementById('xdata');
        var ydata = document.getElementById('ydata');
        var zdata = document.getElementById('zdata');
        var zhendongdata = document.getElementById('zhendongdata');
        var time = document.getElementById('time');
        var useTime = document.getElementById('use_time');

        var timejs= '2021-05-16 00:00:00';

        function getTimer(timejs) {
            console.log(timejs)
            var date2 = new Date();    //结束时间
            var date3 = date2.getTime() - new Date(timejs).getTime();   //时间差的毫秒数
            var days = Math.floor(date3 / (24 * 3600 * 1000))
            console.log(date3)
            var leave1 = date3 % (24 * 3600 * 1000)    //计算天数后剩余的毫秒数
            var hours = Math.floor(leave1 / (3600 * 1000))
            //计算相差分钟数
            var leave2 = leave1 % (3600 * 1000)        //计算小时数后剩余的毫秒数
            var minutes = Math.floor(leave2 / (60 * 1000))
            //计算相差秒数
            var leave3 = leave2 % (60 * 1000)      //计算分钟数后剩余的毫秒数
            var seconds = Math.round(leave3 / 1000)


            var not0 = !!date3,
                d = days,
                h = hours,
                m = minutes,
                s = seconds;
            var absVal = Math.abs(days);
            return d + "天" + h + "小时" + m + "分" + s + "秒";
        }

        // var timeDif = getTimer(timejs);

        function  updateData() {
            var timeDif = getTimer(timejs);
            $.ajax({
                type: 'GET',
                url: 'DataServlet',
                data: {
                },
                success: function (result) {
                    // alert("请求结果!"+result.temp+" "+result.humdity);
                    // alert("请求数据结果：" + result[1].name)
                    xdata.innerText = result.x
                    ydata.innerText = result.y
                    zdata.innerText = result.z
                    zhendongdata.innerText = result.d
                    time.innerText = result.time
                    useTime.innerText = timeDif
                    if (result.d > 60){
                        alert("警告!\r\n震动数据过高，大于60")
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
