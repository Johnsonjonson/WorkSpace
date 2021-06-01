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
		#modifyInfo{
			padding: 7px 10px;
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			margin: auto;
			width: 320px;
			height: 400px;
			background: #fff;
			box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
			border-radius: 7px;
			z-index: 120;
		}
		#buyGoodsInfo{
			padding: 7px 10px;
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			margin: auto;
			width: 500px;
			height: 600px;
			background: #fff;
			box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
			border-radius: 7px;
			z-index: 120;
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

<div id="buyGoodsInfo" align="center">
	<%--    <h1>物流地理信息系统</h1>--%>
	<img src="img/close.png" align="right" height="20" width="20" onclick="closebuyGoodsInfo()">
	<h2>商品信息</h2>
	<table style="width:400px;height:500px;">
		<tr class="cols2">
			<td colspan="2">
				<img src="img/pay.png">
			</td>
		</tr>
		<tr>
			<td>  ID  </td>
			<td><b id="goods_id">""</b></td>
		</tr>
		<tr>
			<td>  名称  </td>
			<td><b id="goods_name">""</b></td>
		</tr>
		<tr>
			<td>  价格  </td>
			<td><b id="goods_price">""</b></td>
		</tr>
		<tr>
			<td>  数量  </td>
			<td><b id="goods_num">1</b></td>
		</tr>
		<tr class="cols2">
			<td colspan="2">
				<input type="button" value="购买"
					   onclick="request_buy()"/>
			</td>
		</tr>
		<tr class="cols2">
			<td colspan="2" class="info"></td>
		</tr>
	</table>
</div>

<div id="modifyInfo" align="center">
	<%--    <h1>物流地理信息系统</h1>--%>
	<img src="img/close.png" align="right" height="20" width="20" onclick="closeModifyPanel()">
	<h2>修改商品</h2>
	<form action="ModifyServlet" method="post">
		<table style="width:280px;height:200px;">
			<tr>
				<td>  ID  </td>
				<td><b id="id">""</b></td>
			</tr>
			<tr>
				<td>  名称  </td>
				<td><input type="text" name="name" id="name"/></td>
			</tr>
			<tr>
				<td>  价格  </td>
				<td><input type="text" name="price" id="price"/></td>
			</tr>
			<tr>
				<td>  数量  </td>
				<td><input type="text" name="num" id="num"/></td>
			</tr>
			<tr class="cols2">
				<td colspan="2">
					<input type="button" value="确定"
						   onclick="request_modify()"/>
				</td>
			</tr>
		</table>
	</form>
</div>
	<div id="container" align="center">
		<div id="header">

			<a id="quit" href="QuitServlet">退出</a>
			<h1>超市管理系统</h1>
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
							<h2>请刷卡，快速录入商品</h2>
							<table class="sui-table table-bordered" id="stable">
							</table>
						</li>
					</ul>

				</div>
				<div class="dom">
					<ul>
						<li>
							<h2>请刷卡，购买商品</h2>
							<table class="sui-table table-bordered" id="stable1">
							</table>
<%--							<table class="sui-table table-bordered" id="stable">--%>
						</li>
					</ul>
				</div>
				<div class="dom">
					<ul>
						<li>
							<h2>商品管理</h2>
							<table class="sui-table table-bordered" id="stable2">
							</table>
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
		var modifyInfo = G("modifyInfo")
		var  buyGoodsInfo= G("buyGoodsInfo")
		modifyInfo.style.visibility = "hidden"
		buyGoodsInfo.style.visibility = "hidden"
		function closeModifyPanel() {
			G("modifyInfo").style.visibility = "hidden"
		}
		function closebuyGoodsInfo() {
			G("buyGoodsInfo").style.visibility = "hidden"
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

		// 修改信息
		function request_modify(){
			var id = G("id").innerText
			var name = G("name").value
			var price =G("price").value
			var num =G("num").value
			console.log(id,name,price,num)
			$.ajax({
				type: 'GET',
				url: 'ModifyServlet',
				data: {
					id:id,
					name:encodeURI(name),
					price:price,
					num:encodeURI(num),
				},
				success: function (result) {
					if (result.errorcode == "0"){
						modifyInfo.style.visibility = "visible"
						alert(result.modifymsg)
					}else if (result.errorcode == "1"){
						modifyInfo.style.visibility = "hidden"
						alert("修改信息成功")
					}
				},
				error: function (errorMsg) {
					alert("查询出错，请重试")
				},
				dataType: "json"
			});
			console.log("user" + id)
		}

		// 请求购买
		function request_buy(){
			var id = G("goods_id").innerText
			var name = G("goods_name").innerText
			var price = G("goods_price").innerText
			var num = G("goods_num").innerText
			console.log(id,name,price,num)
			$.ajax({
				type: 'GET',
				url: 'ModifyServlet',
				data: {
					id:id,
					name:encodeURI(name),
					// name:name,
					price:price,
					num:(num -1),
				},
				success: function (result) {
					if (result.errorcode == "0"){
						buyGoodsInfo.style.visibility = "visible"
						alert(result.modifymsg)
					}else if (result.errorcode == "1"){
						buyGoodsInfo.style.visibility = "hidden"
						alert("购买成功")
					}
				},
				error: function (errorMsg) {
					alert("查询出错，请重试")
				},
				dataType: "json"
			});
			console.log("user" + id)
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
					console.log(msg.price,msg.id,msg.name,msg.num)
					if(msg.id > 0){  //成功
						if (status ==1){
							alert("入库成功")
						}else if (status ==2){
							G("buyGoodsInfo").style.visibility = "visible"
							G("goods_id").innerText = msg.id
							G("goods_name").innerText =eval("'" + msg.name + "'")
							G("goods_price").innerText=msg.price
							G("goods_num").innerText=msg.num
							console.log("购买成功")
						}

					}else{
						// if (status ==1){
						// 	alert("入库失败")
						// }else if (status ==2){
						// 	alert("暂无该商品")
						// }
					}
				},
				error:(msg) =>{
					// alert("请求数据失败!"+msg);
				}

			});
		}
		function  updateData() {
			$.ajax({
				type: 'GET',
				url: 'DataServlet',
				data: {
				},
				success: function (result) {
					console.log(result);
					var temp = []
					for (i=0;i<result.length;i++){
						goods = result[i]
						temp[i] = []

						temp[i].push(goods.id)
						temp[i].push(goods.name)
						temp[i].push(goods.price)
						temp[i].push(goods.num)
					}
					var id = 'stable'
					if(status ==1){
						id = 'stable'
					}else if(status ==2){
						id = 'stable1'
					}else if(status ==3){
						id = 'stable2'
					}
					creat_table(id,temp);
					// alert("请求结果!"+result.temp+" "+result.humdity);
					// alert("请求数据结果：" + result[1].name)
					// temp.innerText = result.temp
					// humdity.innerText = result.humdity
					// realTimeNum.innerText = result.realTimeNum
				},
				error: function (errorMsg) {
					console.log("请求数据失败");
					// alert("请求数据失败!");
				},
				dataType: "json"
			});
		}
		updateStatus(status)
		setInterval(function (){
			updateData()
			updateStatus(status)
		},1000)

		function openEditPop(id){
			$.ajax({
				type: 'GET',
				url: 'DataServlet',
				data: {
					id:id,
					m:"querybyid"
				},
				success: function (product) {
					if(product!=null && product!=""){
						modifyInfo.style.visibility = "visible"
						G("id").innerText = product.id
						G("name").value = product.name
						G("price").value = product.price
						G("num").value = product.num
					}else{
						alert("查询出错，请重试")
					}
				},
				error: function (errorMsg) {
					alert("查询出错，请重试")
				},
				dataType: "json"
			});
			console.log("user" + id)
		}

		function creat_table(id,source_data){
			//画表格
			var tempStr = '<thead><tr><td>id</td><td>名称</td><td>价格</td><td>数量</td></thead>';
			if(status ==3){
				tempStr = '<thead><tr><td>id</td><td>名称</td><td>价格</td><td>数量</td><td>编辑</td></thead>';
			}
			for(var i=0; i<source_data.length; i++){
				var row = source_data[i]
				tempStr += '<tr>';
				for(var k=0; k<row.length; k++){
					tempStr += '<td>' +row[k]+'</td>';
				}
				if(status ==3){
					tempStr += '<td><a href="javascript:void(0)" onclick="openEditPop(+'+row[0]+')">编辑</a></td>'
				}

				tempStr += '</tr>';
			}

			$("#"+id).html(tempStr);
		}

	</script>
</body>
</html>