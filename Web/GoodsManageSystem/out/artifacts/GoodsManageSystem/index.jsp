<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%--<base href="<%=basePath%>">--%>
<script src="js/echarts.min.js"></script>
<link rel="stylesheet" href="map.css">
	<style>
		body,
		html,
		#container{
			width: 100%;
			height: 100%;
			padding: 0;
			margin: 0;
			overflow: hidden;
		}
		#header {
			position: relative;
			height: 140px;
			/*background-color: #FF0000;*/
			/*opacity: 35%;*/
			background:url("img/head.jpg")
		}
		#header h1 {
			align-self: center;
			margin: 0 auto;
			width: 240px;
			padding-top: 40px;
			color: white;
			font-size: 36px;
			text-align:center
		}
		#quit {
			float: right;
			padding-top: 5px;
			padding-right: 10px;
		}

		#menu {
			position: absolute;
			bottom: 5px;
		}

		#menu li {
			float: left;
			margin-left: 10px;
			list-style: none;
		}
		/*h1 {*/
		/*	margin: 0 auto;*/
		/*	width: 300px;*/
		/*	padding-top: 10px;*/
		/*	color:  rgba(27, 142, 236, 1);*/
		/*	font-size: 30px;*/
		/*}*/

		/*h2 {*/
		/*	margin: 0 auto;*/
		/*	width: 240px;*/
		/*	padding-top: 5px;*/
		/*	color: black;*/
		/*	font-size: 28px;*/
		/*}*/
		table,table tr th, table tr td { border:1px solid #0094ff; }
		table { width: 400px; min-height: 25px; line-height: 25px; text-align: center; border-collapse: collapse;margin: 40px;}
	</style>
	<script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
<title>快速入库</title>
</head>
<body>
	<div id="container" align="center">
		<div id="header">

			<a id="quit" href="QuitServlet">退出</a>
			<h1>商品管理系统</h1>
		</div>
		<div id="content">
			<!--选项的头部-->
			<div id="tab-header">
				<ul>
					<li class="selected" onclick = "tabClick(0)">快速入库</li>
					<li onclick = "tabClick(1)">交易处理</li>
					<li onclick = "tabClick(2)">超市管理</li>
				</ul>
			</div>
			<!--主要内容-->

			<div id="tab-content">
<%--				<div class="dom" id="dom" style="display: block;position: absolute;width: 400px;top:80px;left: 20px;margin: 0;">--%>
				<div class="dom" style="display: block;">
					<ul>
						<li>
							<a  href="#" style="align-self: center">请刷卡，快速录入商品</a>
							<table class="sui-table table-bordered" id="stable">
							</table>
						</li>
					</ul>

				</div>
				<div class="dom">
					<ul>
						<li>
							<a href="#" style="align-self: center">暂无内容，敬请期待</a>
<%--							<table class="sui-table table-bordered" id="stable">--%>
						</li>
					</ul>
				</div>
				<div class="dom">
					<ul>
						<li>
							<a href="#" style="align-self: center">暂无内容，敬请期待</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
<%--		<div id="content">--%>
<%--			快速入库--%>
<%--		</div>--%>
		<div id="footer"></div>
	</div>

	<script type="text/javascript">
		// http://129.204.232.210:8555/status?status=1
		var status = 1
		function G(id) {
			return document.getElementById(id);
		}
		function tabClick(index){
			status = 1 +index
			// 拿到所有的标题(li标签) 和 标题对应的内容(div)
			var titles = G('tab-header').getElementsByTagName('li');
			var divs = G('tab-content').getElementsByClassName('dom');
			// 判断
			if(titles.length != divs.length) return;
			// 遍历
			for(var j=0; j<titles.length; j++){
				titles[j].className = '';
				divs[j].style.display = 'none';
			}
			titles[index].className = 'selected';
			divs[index].style.display = 'block';
			updateStatus(status)
		}

		function  updateStatus(status) {
			// console.log("网页状态"+status)
			$.ajax({
				type: "get",
				url: 'http://129.204.232.210:8555/status?',
				data:{
					status:status+"",
				},
				dataType:'jsonp',//服务器返回json格式数据
				crossDomain: true,
				jsonp: 'callback',
				jsonpCallback: "callback",
				success: (msg) => {
					//执行方法
					// alert("请求数据chbeng!"+msg);
					// this.handMarker(msg.data);
					// console.log(msg.price,msg.id,msg.name,msg.num)
					if(msg.id > 0){  //成功
						if (status ==1){
							console.log("入库成功")
						}

						else if (status ==2){
							console.log("购买成功")
						}

					}else{

					}
				},
				error:(msg) =>{
					alert("请求数据失败!"+msg);
				}

			});
		}
		updateStatus(status)
		setInterval(function (){
			updateStatus(status)
			$(function(){
				creat_table('stable',source_data);
			});
		},1000)
		var source_data = [
			['1001','篮球','150','1'],
			['1002','足球','120','1'],
			['1003','棒球','50','1'],
		];
		function creat_table(id,source_data){
			//画表格
			var tempStr = '<thead><tr><td>id</td><td>名称</td><td>价格</td><td>数量</td></thead>';
			for(var i=0; i<source_data.length; i++){
				var row = source_data[i]
				tempStr += '<tr>';
				for(var k=0; k<row.length; k++){
					tempStr += '<td>' +row[k]+'</td>';
				}
				tempStr += '</tr>';
			}
			$("#"+id).html(tempStr);
		}

	</script>
</body>
</html>