<%--
  Created by IntelliJ IDEA.
  User: JohnsonZhang
  Date: 2021/1/29
  Time: 12:15
  To change this template use File | Settings | File Templates.
--%>
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
	<title>车流量详情</title>
	<script src="js/echarts.min.js"></script>
	<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
<%--	<link rel="stylesheet" href="styles.css">--%>
	<style>
		body,
		html,
		h1 {
			margin: 0 auto;
			width: auto;
			padding-top: 40px;
			color: black;
			font-size: 36px;
			text-align:center
		}
	</style>
	<%--    <script src="js/jquery-1.8.3.min.js"></script>--%>
</head>
<body>

<%
	String role = (String)request.getAttribute("role");
%>
<div id="container">
	<div id="header">
<%--		<h1>景区今日车流量情况</h1>--%>
	</div>
	<div id="content">
		<h1>景区今日车流量情况</h1>
		<div id="main" style="margin: 0 auto;height:400px;text-align:center" ></div>
		<span id="total" style="font-size: medium; color: black; margin: 30px;">车流量流动总数:</span>
		<span id="enter" style="font-size: medium; color: black; margin: 30px;">进入景区车辆数:</span>
		<span id="exit" style="font-size: medium; color: black; margin: 30px;">离开景区车辆数:</span>
		<span id="park" style="font-size: medium; color: black; margin: 30px;">预计景区车位数:</span>
	</div>
</div>

<%--    <div id="main" style="width: 600px;height:400px;"></div>--%>
<script type="text/javascript">
	// 基于准备好的dom，初始化echarts实例
	var myChart = echarts.init(document.getElementById('main'),'dark');
	var total =  document.getElementById('total')
	var enter =  document.getElementById('enter')
	var exit =  document.getElementById('exit')
	var park =  document.getElementById('park')
	var timeData = new Array()
	var totalData = new Array()
	function  updateData() {
		$.ajax({
			type: 'GET',
			url: 'http://129.204.232.210:8538/get',
			// data:,
			success: function (result) {
				// alert("无该品类实时成交数据!"+result);
				// alert("请求数据结果：" + result[1].name)

				var i = 0
				resultData = result
				var d = new Date();
				var time = d.getTime();
				var hour =  d.getHours(); //获取当前小时数(0-23)
				var m = d.getMinutes(); //获取当前分钟数(0-59)
				var s = d.getSeconds(); //获取当前秒数(0-59)

				if (timeData.length >20){
					timeData.shift()
					totalData.shift()
				}
				timeData.push(hour+":"+m+":"+s)
				totalData.push(result.total)
				total.innerText="车流量流动总数: "+result.total
				enter.innerText="进入景区车辆数: "+result.enter
				exit.innerText="离开景区车辆数: "+result.exit
				park.innerText="预计景区车位数: "+result.park
				// 指定图表的配置项和数据
				var option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['车流量']
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
						name: "车流量",
						data: totalData,
						type: 'line'
					},]
				};
				myChart.setOption(option);
				setTimeout(function () {
					window.onresize = function () {
						myChart.resize();
					}
				}, 200)
			},
			error: function (errorMsg) {
				alert("请求数据失败!"+errorMsg);
			},
			dataType: "json"
		});
	}
	updateData()
	setInterval(function (){
		updateData()
	},1000)
</script>
</body>
</html>
