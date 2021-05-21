<%@ page import="entity.User" %>
<%@ page language="java" contentType="text/html"
         pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head onload="setup()">
    <meta charset="utf-8">
    <title>地图展示</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
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
        /*#searchResultPanel{*/
        /*    padding: 7px 10px;*/
        /*    position: fixed;*/
        /*    top: 40px;*/
        /*    left: 20px;*/
        /*    width: 170px;*/
        /*    height: 800px;*/
        /*    background: #fff;*/
        /*    box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);*/
        /*    border-radius: 7px;*/
        /*    z-index: 50;*/
        /*}*/
        .tangram-suggestion-main {
            z-index: 999999;
        }

        #result{
            padding: 7px 4px;
            position: fixed;
            top: 10px;
            left: 20px;
            width: 240px;
            background: #fff;
            box-shadow: 0 2px 6px 0 rgba(27, 142, 236, 0.5);
            border-radius: 7px;
            z-index: 50;
        }
    </style>
    <style type="text/css">
        body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";font-size:14px;}
    </style>
    <script src="//api.map.baidu.com/api?type=webgl&v=1.0&ak=wbBdqTmv5pnVG5iZZ8jK9snbWQ0f2a71"></script>
    <script type="text/javascript" src="//mapopen.cdn.bcebos.com/github/BMapGLLib/DistanceTool/src/DistanceTool.min.js"></script>
    <script src="http://libs.baidu.com/jquery/1.9.0/jquery.js"></script>
    <script type="text/javascript" src="js/select.js" charset="utf-8"></script>
</head>
<body>
<%
    User user = (User)request.getAttribute("user");
%>
<div id="container"></div>
<div id='result'>
    <input id="suggestId" type="text" style="z-index: 999999"/>
    <button onclick="theLocation()">查询</button>
</div>
<div id="searchResultPanel" style="border:1px solid #C0C0C0;width:150px;height:auto; display:none;"></div>

<%--   控制面板  --%>
<div id='control_panel'>
    <img src="img/close.png" align="right" style="z-index: 100" height="20" width="20" onclick="setPanelVisible()">
<%--    <button class = "btnClose" onclick="setPanelVisible()">关闭操作面板</button>--%>
    <ul class="btn-wrap-control" style="z-index: 99;">
        <li class = "btn" id="btnExit" onclick="quit()">退出登陆</li>
        <li class = "btn" id="btnNodify" onclick = "modifyPwdFun()">修改密码</li>
        <li  class = "control_li" id="openManage"><a href="UserServlet?m=search">管理页面</a></li>
    </ul>
    <div id="tab">
        <!--选项的头部-->
        <div id="tab-header">
            <ul>
                <li class="selected" onclick = "tabClick(0)">地理元素</li>
                <li onclick = "tabClick(1)">多式联运</li>
            </ul>
        </div>
        <!--主要内容-->

        <div id="tab-content">
            <div class="dom" id="dom" style="display: block;position: absolute;width: 100px;top:80px;left: 20px;margin: 0;">
<%--                <div class="landswitch" onclick="clickSwitch()">--%>
<%--                    <input class="landswitch-checkbox" id="onoffswitch" type="checkbox">--%>
<%--                    <label class="landswitch-label" for="onoffswitch">--%>
<%--                        <span class="landswitch-inner" data-on="大陆" data-off="海洋"></span>--%>
<%--                        <span class="landswitch-switch"></span>--%>
<%--                    </label>--%>
<%--                </div>--%>
                <table id="tab-select" style="width:400px;height:200px;">
                    <tr></tr>
                    <tr><td>海洋，陆地</td></tr>
                    <tr><td><select id="s1"></select></td></tr>
                    <tr><td>洲</td></tr>
                    <tr><td><select id="s2"></select></td></tr>
                    <tr><td>国家</td></tr>
                    <tr><td><select id="s3"></select></td></tr>
                    <tr><td>省，州，直辖市</td></tr>
                    <tr><td><select id="s4"></select></td></tr>
                    <tr><td>元素</td></tr>
                    <tr><td><select id="s5"></select></td></tr>
                </table>
                <div id="draw_btn" style="margin: 10px">
                    <input class="btn" type="button" value="将选择的元素绘制到地图" style="width:180px;height:30px;left: -10px"
                           onclick="queryMarker()"/>
                    <input class="btn" type="button" value="清除地图上所有的元素" style="width:180px;height:30px;left: -10px"
                           onclick="removeOverlay()"/>
                </div>
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
</div>

<%--   地图  操作按钮--%>
<ul class="btn-wrap2" style="z-index: 30;">
    <li class = "btn" id="btnOpen" onclick = "setPanelVisible()">打开操作面板</li>
</ul>

<ul class="btn-wrap1" style="z-index: 30;">
    <li class = "btn" id="btnMeasure" onclick = "measure()">鼠标测距</li>
    <li class = "btn" id="btnClear" onclick = "removeOverlay()">清空地图</li>
</ul>

<ul class="btn-wrap" style="z-index: 30;">
    <li class = "btn" id="btn1" onclick = "queryMarkers(1)">中国机场</li>
    <li class = "btn" id="btn2" onclick = "queryMarkers(2)">中国港口</li>
    <li class = "btn" id="btn3" onclick = "queryMarkers(3)">美国机场</li>
    <li class = "btn" id="btn4" onclick = "queryMarkers(4)">美国港口</li>
    <li class = "btn" id="btn5" onclick = "queryMarkers(5)">亚洲机场</li>
    <li class = "btn" id="btn6" onclick = "queryMarkers(6)">欧洲机场</li>
    <li class = "btn" id="btn7" onclick = "queryMarkers(7)">美洲机场</li>
    <li class = "btn" id="btn8" onclick = "queryMarkers(8)">大洋洲机场</li>
    <li class = "btn" id="btn9" onclick = "queryMarkers(9)">亚洲港口</li>
    <li class = "btn" id="btn10" onclick = "queryMarkers(10)">欧洲港口</li>
    <li class = "btn" id="btn11" onclick = "queryMarkers(11)">美洲港口</li>
    <li class = "btn" id="btn12" onclick = "queryMarkers(12)">大洋洲港口</li>
    <li class = "btn" id="btn13" onclick = "queryMarkers(13)">非洲港口</li>
    <li class = "btn" id="btn14" onclick = "queryMarkers(14)">美洲海峡</li>
    <li class = "btn" id="btn15" onclick = "queryMarkers(15)">非洲海峡</li>
    <li class = "btn" id="btn16" onclick = "queryMarkers(16)">亚洲海峡</li>
    <li class = "btn" id="btn17" onclick = "queryMarkers(17)">大洋洲海峡</li>
    <li class = "btn" id="btn18" onclick = "queryMarkers(18)">欧洲海峡</li>
</ul>

<div id="modityPwd" align="center">
<%--    <h1>物流地理信息系统</h1>--%>
    <img src="img/close.png" align="right" height="20" width="20" onclick="closeModifyPanel()">
    <h2>修改密码</h2>
    <form action="ModifyServlet" method="post">
        <table style="width:240px;height:200px;">
            <tr>
                <td>用户名</td>
                <td><%=user.getName()%></td>
            </tr>
            <tr>
                <td>旧密码</td>
                <td><input type="password" name="pwd" id="pwd"/></td>
            </tr>
            <tr>
                <td>新密码</td>
                <td><input type="text" name="newpwd" id="newpwd"/></td>
            </tr>
            <tr class="cols2">
                <td colspan="2">
                    <input type="button" value="确定"
                           onclick="request_modify()"/>
                </td>
            </tr>
            <tr class="cols2">
                <td colspan="2" class="info" id="modifyinfo"></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
<script>
    // 百度地图API功能
    function G(id) {
        return document.getElementById(id);
    }
    var controlPanel = G("control_panel")
    var modifyPwd = G("modityPwd")
    controlPanel.style.visibility = "hidden"
    modifyPwd.style.visibility = "hidden"
    var map = new BMapGL.Map('container'); // 创建Map实例
    map.centerAndZoom('上海市', 11); // 初始化地图,设置中心点坐标和地图级别

    function theLocation(id){
        // map.centerAndZoom("上海市",4);      // 用城市名设置地图中心点
        var city = G("suggestId").value;
        if(city != ""){
            map.clearOverlays();
            var local = new BMapGL.LocalSearch(map, {
                renderOptions:{map: map}
            });
            local.search(city);
        }
    }
    function queryMarker(){
        var myselect = document.getElementById('s5');
        var index=myselect.selectedIndex ;
        var nameValue = myselect.options[index].value;
        var nameText = myselect.options[index].text;
        console.log(nameValue,nameText)
        $.ajax({
            type: 'GET',
            url: 'DataServlet',
            data: {
                name:encodeURI(nameText),
                m:"querybyname"
            },
            success: function (markerInfo) {
                if (markerInfo != null && markerInfo != "" && markerInfo.lng!=null && markerInfo.lng!="" ){
                    map.centerAndZoom(new BMapGL.Point(markerInfo.lng, markerInfo.lat),10);
                }else{
                    alert("未查询到数据")
                    return
                }
                removeOverlay()
                console.log(markerInfo)
                console.log(markerInfo.lng,markerInfo.lat)
                var point = new BMapGL.Point(markerInfo.lng,markerInfo.lat);
                // 创建图标
                var iconImg = "img/port.png"
                if(markerInfo.type=="港口"){
                    iconImg = "img/port.png"
                }else {
                    iconImg = "img/airport.png"
                }
                var myIcon = new BMapGL.Icon(iconImg, new BMapGL.Size(24, 24));
                // 创建Marker标注，使用图标
                var marker = new BMapGL.Marker(point, {
                    icon: myIcon
                });
                map.addOverlay(marker);              // 将标注添加到地图中
                addClickHandler(markerInfo,marker);
                function addClickHandler(content,marker){
                    marker.addEventListener("click",function(e){
                        openInfo(content,e)}
                    );
                }
                function openInfo(markerInfo,e){
                    var p = e.target;
                    var opts = {
                        width : 400,     // 信息窗口宽度
                        height: 240,     // 信息窗口高度
                        title : markerInfo.name , // 信息窗口标题
                        message:markerInfo.htmlData
                    }
                    var name =  markerInfo.name
                    var imgName =  markerInfo.imgName
                    var sContent = markerInfo.htmlData
                    if(imgName!="" && imgName!=null){
                        sContent =`<img style='float:right;margin:0 4px 22px' id='imgDemo' src='img/`+imgName +`.jpg' width='280' height='200'/>
                            <p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>
                            `+markerInfo.htmlData+`
                            </p></div>`;
                        opts = {
                            width : 500,     // 信息窗口宽度
                            height: 240,     // 信息窗口高度
                            title : markerInfo.name , // 信息窗口标题
                            message:markerInfo.htmlData
                        }
                    }else{
                        sContent = markerInfo.htmlData
                    }

                    var point = new BMapGL.Point(p.getPosition().lng, p.getPosition().lat);
                    var infoWindow = new BMapGL.InfoWindow(sContent,opts);  // 创建信息窗口对象 
                    map.openInfoWindow(infoWindow,point); //开启信息窗口
                }
            },
            error: function (errorMsg) {
                alert("查询出错，请重试")
            },
            dataType: "json"
        });
    }

    function queryMarkers(index){
        $.ajax({
            type: 'GET',
            url: 'DataServlet',
            data: {
                index:index,
                m:"querybyid"
            },
            success: function (result) {
                removeOverlay()
                if (result.length >0){
                    map.centerAndZoom(new BMapGL.Point(result[0].lng, result[0].lat),4);
                }else{
                    alert("未查询到数据")
                }
                for(i in result){
                    var markerInfo=result[i];
                    console.log(markerInfo)
                    console.log(markerInfo.lng,markerInfo.lat)
                    var point = new BMapGL.Point(markerInfo.lng,markerInfo.lat);
                    // 创建图标
                    var iconImg = "img/port.png"
                    if(markerInfo.type=="港口"){
                        iconImg = "img/port.png"
                    }else {
                        iconImg = "img/airport.png"
                    }
                    var myIcon = new BMapGL.Icon(iconImg, new BMapGL.Size(24, 24));
                    // 创建Marker标注，使用图标
                    var marker = new BMapGL.Marker(point, {
                        icon: myIcon
                    });
                    map.addOverlay(marker);              // 将标注添加到地图中
                    addClickHandler(markerInfo,marker);
                }
                function addClickHandler(content,marker){
                    marker.addEventListener("click",function(e){
                        openInfo(content,e)}
                    );
                }
                function openInfo(markerInfo,e){
                    var p = e.target;
                    var opts = {
                        width : 400,     // 信息窗口宽度
                        height: 240,     // 信息窗口高度
                        title : markerInfo.name , // 信息窗口标题
                        message:markerInfo.htmlData
                    }
                    var name =  markerInfo.name
                    var imgName =  markerInfo.imgName
                    var sContent = markerInfo.htmlData
                    if(imgName!="" && imgName!=null){
                        sContent =`<img style='float:right;margin:0 4px 22px' id='imgDemo' src='img/`+imgName +`.jpg' width='280' height='200'/>
                            <p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>
                            `+markerInfo.htmlData+`
                            </p></div>`;
                        opts = {
                            width : 500,     // 信息窗口宽度
                            height: 240,     // 信息窗口高度
                            title : markerInfo.name , // 信息窗口标题
                            message:markerInfo.htmlData
                        }
                    }else{
                        sContent = markerInfo.htmlData
                    }


                    var point = new BMapGL.Point(p.getPosition().lng, p.getPosition().lat);
                    var infoWindow = new BMapGL.InfoWindow(sContent,opts);  // 创建信息窗口对象 
                    map.openInfoWindow(infoWindow,point); //开启信息窗口
                }
            },
            error: function (errorMsg) {
                alert("查询出错，请重试")
            },
            dataType: "json"
        });
    }
    // 清除覆盖物
    function removeOverlay() {
        map.clearOverlays();
    }
    map.enableScrollWheelZoom(true); // 开启鼠标滚轮缩放

    var scaleCtrl = new BMapGL.ScaleControl();  // 添加比例尺控件
    map.addControl(scaleCtrl);
    var zoomCtrl = new BMapGL.ZoomControl();  // 添加比例尺控件
    map.addControl(zoomCtrl);

    var myDis = new BMapGLLib.DistanceTool(map);

    // 监听测距过程中的鼠标事件
    myDis.addEventListener('drawend', function(e) {
        console.log("drawend");
        console.log(e.points);
        console.log(e.overlays);
        console.log(e.distance);
    });
    myDis.addEventListener("addpoint", function(e) {
        console.log("addpoint");
        console.log(e.point);
        console.log(e.pixel);
        console.log(e.index);
        console.log(e.distance);
    });
    myDis.addEventListener("removepolyline", function(e) {
        console.log("removepolyline");
        console.log(e);
    });
    var isMeasure = false
    function measure(){
        var btnMeasure = document.getElementById("btnMeasure");
        if(isMeasure){
            btnMeasure.innerText="鼠标测距"
            myDis.close()
            isMeasure = false
        }else{
            btnMeasure.innerText="关闭测距"
            myDis.open()
            isMeasure = true
        }
    }

    // ---------------------------------------------搜索提示-----------------------------------
    // var searchResultPanel = G("searchResultPanel")
    // // searchResultPanel.style.visibility = "hidden"
    // searchResultPanel.style.visibility = "visible"
    var ac = new BMapGL.Autocomplete(    //建立一个自动完成的对象
        {"input" : "suggestId"
            ,"location" : map
        });
    ac.show()

    ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
        console.log("=================onhighlight=================")
        var str = "";
        var _value = e.fromitem.value;
        var value = "";
        if (e.fromitem.index > -1) {
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

        value = "";
        if (e.toitem.index > -1) {
            _value = e.toitem.value;
            value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        }
        str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
        G("searchResultPanel").innerHTML = str;
    });

    var myValue;
    ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
        var _value = e.item.value;
        myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
        G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

        setPlace();
    });

    function setPlace(){
        map.clearOverlays();    //清除地图上所有覆盖物
        function myFun(){
            var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
            map.centerAndZoom(pp, 18);
            map.addOverlay(new BMapGL.Marker(pp));    //添加标注
        }
        var local = new BMapGL.LocalSearch(map, { //智能搜索
            onSearchComplete: myFun
        });
        // global.search()
        local.search(myValue);
    }

    // == 值比较  === 类型比较 $(id) ---->  document.getElementById(id)
    // function a(id){
    //     return typeof id === 'string' ? document.getElementById(id):id;
    // }

    // -----------------------------------------------控制面板-----------------------------------------------
    function quit(){
        $.ajax({
            type: 'GET',
            url: 'QuitServlet',
            data: {
            },
            success: function (result) {
                window.location.href = "index.jsp";
            },
            error: function (errorMsg) {
                window.location.href = "index.jsp";
            },
            dataType: "json"
        });
    }

    // 修改密码
    function  request_modify() {
        var pwdInput = G('pwd');
        var pwdnewInput = G('newpwd');
        var pwdStr = pwdInput.value
        var newpwdStr = pwdnewInput.value
        $.ajax({
            type: 'GET',
            url: 'ModifyServlet',
            data: {
                "pwd": pwdStr,
                "newpwd": newpwdStr,
                "id":<%=user.getId()%>
            },
            success: function (result) {
                if (result.errorcode == "0"){
                    alert(result.modifymsg)
                    modifyPwd.style.visibility = "visible"
                }else if (result.errorcode == "1"){
                    alert("修改密码成功")
                    modifyPwd.style.visibility = "hidden"
                }
            },
            error: function (errorMsg) {
                alert("请求数据失败!");
            },
            dataType: "json"
        });
    }
    function setPanelVisible(){
        if (controlPanel.style.visibility=="hidden"){
            controlPanel.style.visibility = "visible"
        }else{
            controlPanel.style.visibility = "hidden"
        }

    }

    function closeModifyPanel(){
        modifyPwd.style.visibility = "hidden"
    }
    function modifyPwdFun(){
        if (modifyPwd.style.visibility=="hidden"){
            modifyPwd.style.visibility = "visible"
        }else{
            modifyPwd.style.visibility = "hidden"
        }
    }
    var openManage = G("openManage")
    var role = "<%=user.getRole()%>"
    if (role === "student"){
        openManage.style.visibility = "hidden"
    }else{
        if (controlPanel.style.visibility =="visible")
            openManage.style.visibility = "visible"
    }

    function tabClick(index){
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
    }

    // $(document).ready(function() {
        $("#onoffswitch").on('click', function () {
            clickSwitch()
        });
    //
        var clickSwitch = function () {
            if ($("#onoffswitch").is(':checked')) {
                // alert("在陆地的状态下");
            } else {
                // alert("在海洋的状态下");
            }
        };
    // });


    var s=["s1","s2","s3","s4","s5"];
    var opt0 = ["海洋，陆地","洲","国家","省，州，直辖市","元素"];
    function setup(){
        for(i=0;i<s.length-1;i++)
            document.getElementById(s[i]).onchange=new Function("change("+(i+1)+")");
        change(0);
    }
    setup()
    // function queryMarkers(){
    //     alert("===================queryMarkers===================")
    // }
    // requestModifyPwd()
</script>
