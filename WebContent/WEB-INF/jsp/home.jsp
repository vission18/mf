<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.vission.mf.base.enums.BaseConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><%=BaseConstants.getBrowserTitle() %></title>
<%@include file="/common/include.jsp"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/skinUtil.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/home.js"></script>
<script type="text/javascript">
	jQuery.ajaxSetup({
		cache : false
	});//ajax不缓存
	var styleName = "blue";
	if ($.cookie('imfImsStyleName')) {
		styleName = $.cookie('imfImsStyleName');
	}
	switchStylestyle(styleName);//样式
	
	$(function() {
		//定义全局的baseUrl
		if(typeof homeBaseUrl === 'undefined'){
			homeBaseUrl = "${pageContext.request.contextPath}";
		}
		
		maxTabNum = <%=BaseConstants.getTabMaxNum()%>;
		//标签页菜单事件
		bindTabEvent();
		bindTabMenuEvent();
		//初始化树菜单
		initTreeMenu("${pageContext.request.contextPath}");
		$('#confirmPassword').validatebox({
			validType : 'eqPwd["#newPassword"]'
		});
		//addTab('工作台','${pageContext.request.contextPath}/portal.do', '', false, '');
	});

	//为jquery ajax设置全局error回调函数。XMLHttpRequest响应头信息若为session失效即跳转到相应的登录界面
	$.ajaxSetup({
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		complete : function(XMLHttpRequest, textStatus, errorThrown) {
			if (XMLHttpRequest.getResponseHeader("sessionstatus") == 'timeout') {
				getRootWin().location.href = "${pageContext.request.contextPath}/login.jsp";
			}
		}
	});
	function checkSession(data) {
		if (!data || data == null || data == "null") {
			getRootWin().location.href = "${pageContext.request.contextPath}/login.jsp";
		}
	}
	function getRootWin() {
		var win = window;
		while (win != win.parent) {
			win = win.parent;
		}
		return win;
	}
</script>
</head>
<body class="easyui-layout" id="mainBody">
	<div region="north" id="north-layout" title="" split="false">
		<div class="imf_header" id="bigMenuBar">
			<div class="imf_logo">
				<a href="javascript:void(0);"><img src="images/logohome.png" width="173" height="80" alt="" /></a>
			</div>
			<div class="imf_nav">
				<ul id="topMenu">
				</ul>
			</div>
		</div>

		<div class="imf_header_sub" id="smallMenuBar">
			<div class="imf_header_left_sub">
				<div class="imf_logo_sub">
					<img src="images/logo_sub.png" alt="" />
				</div>
				<div class="imf_left_arrow"></div>
			</div>
			<div class="imf_header_right_sub cl">
				<div class="imf_right_arrow"></div>
				<div class="imf_nav_sub cl">
					<ul id="topSmallMenu" class="">
		
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div region="west" id="west-layout" title="" split="true" style="width:<%=BaseConstants.getLeftWidth()%>px;">
		<div class="easyui-layout" fit="true">
			<div id="home-left-layout" region="center" border="false">
				<div id="tree-menu" class="easyui-accordion" fit="true" border="false"></div>
				<div id="treetips" class="imf_menunote" style="display:none;"></div>
			</div>
			<div region="south" style="height:26px;" border="false"></div>
		</div>
	</div>
	<div region="center">
		<div id="main-center" class="easyui-tabs" fit="true" border="false" data-options="tools:'#tab-tools'">
			<div id="indexPanel" title="首页"  style="padding:5px;">
			</div>
		</div>
	</div>
	<div region="south" id="south-layout" style="height:26px;" split="false" border="false">
		<div class="imf_footer">
			<div class="imf_user">
				<div class="imf_direction">
					<a href="javascript:void(0);" class="ditop" onclick="setTopMenuType('small');">点击隐藏头部</a>
					<a href="javascript:void(0);" class="diright" onclick="showLayoutWS();">点击显示左边</a>
					<a href="javascript:void(0);" class="didown" onclick="setTopMenuType('big');">点击显示头部</a>
					<a href="javascript:void(0);" class="dileft" onclick="hideToolBarWest();">点击隐藏左边</a>
					<a href="javascript:void(0);" class="dimid" id="fullScreen" onclick="fullScreen();">点击切换全屏</a>
				</div>
				<div class="imf_userbtn">
					<c:if test="${sessionInfo.user != null}">
						<c:if test="${sessionInfo.user.userType != 3}">
							<a href="javascript:void(0);" title="个人资料" id="open_change_user" class="imf_personal">个人资料</a>
							<a href="javascript:void(0);" id="open_change_password" title="修改密码" class="imf_modifypass">修改密码</a>
						</c:if>
					</c:if>
					 <a href="javascript:void(0);" id="open_help_download" title="帮助" class="imf_help">帮助</a>
					<a href="${pageContext.request.contextPath}/login.do" title="安全退出" class="imf_safequit">安全退出</a>
				</div>
			</div>
			<div class="imf_didesc"></div>
			<div class="imf_userinfo">
				<c:if test="${sessionInfo.user != null}">
					<strong>欢迎您：</strong>
					<span style="color:black" >${sessionInfo.user.userName}</span>
					<strong>当前时间：</strong>
					<span id="clock"></span>
				</c:if>
			</div>
			<!-- <div class="imf_skins"><a rel="blue" class="styleswitch skins_blue" title="蓝">蓝</a><a rel="red" class="styleswitch skins_red" title="红">红</a><a rel="green" class="styleswitch skins_green" title="绿">绿</a></div>
			<div class="imf_feedback"><a href="javascript:void(0);" title="我要对这个页面说两句~" onclick="openPageAdviceWin('home');">用户反馈</a></div> -->
	</div>
	</div>
	<%@include file="homeInclude.jsp"%>
</body>
</html>
