<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<SCRIPT src="js/jquery-1.9.1.min.js" type="text/javascript"></SCRIPT>
<jsp:include page="/common/include.jsp"></jsp:include>
<title>请登录</title>
<script type="text/javascript">
 	$(function() {
 		pwdFocus();
 		pwdBlur();
	});
	//提交登录
	function loginCheck(){
		if($("#loginForm").form('validate')){
		  		$("#loginForm").mask("登录中...");
	  		document.formDoLogin.submit();
	  	}
	}
	//密码框获取焦点
	function pwdFocus(){
		$("#password").focus(function() {
			$("#left_hand").animate({
				left : "150",
				top : " -38"
			}, {
				step : function() {
					if (parseInt($("#left_hand").css("left")) > 140) {
						$("#left_hand").attr("class", "left_hand");
					}
				}
			}, 2000);
			$("#right_hand").animate({
				right : "-64",
				top : "-38px"
			}, {
				step : function() {
					if (parseInt($("#right_hand").css("right")) > -70) {
						$("#right_hand").attr("class", "right_hand");
					}
				}
			}, 2000);
		});
	}
	//密码框失去焦点
	function pwdBlur(){
		$("#password").blur(function() {
			$("#left_hand").attr("class", "initial_left_hand");
			$("#left_hand").attr("style", "left:100px;top:-12px;");
			$("#right_hand").attr("class", "initial_right_hand");
			$("#right_hand").attr("style", "right:-112px;top:-12px");
		});
	}
	//清空文本框
	function clearText(){
		$("#password").val("");
		$("#loginName").val("");
		pwdBlur();
	}
	//回车键提交
	$(document).keydown(function (event) {
		if (event.keyCode == 13) {
	    	$('#loginBtn').triggerHandler('click');
	    }
	});
</script>

</head>
<body>
	<form id="loginForm" action="${pageContext.request.contextPath}/login.do" name="formDoLogin" method="post">
		<div class="top_div"></div>
		<div
			style="background: rgb(255, 255, 255); margin: -100px auto auto; border: 1px solid rgb(231, 231, 231); border-image: none; width: 400px; height: 200px; text-align: center;">
			<div style="width: 165px; height: 96px; position: absolute;">
				<div class="tou"></div>
				<div class="initial_left_hand" id="left_hand"></div>
				<div class="initial_right_hand" id="right_hand"></div>
			</div>
			<p style="padding: 30px 0px 10px; position: relative;">
				<span class="u_logo"></span> <input class="ipt easyui-validatebox" data-options="required:true,missingMessage:'请输入账号'" type="text" id="loginName" name="loginName"
					placeholder="请输入账号" >
			</p>
			<p style="position: relative;">
				<span class="p_logo"></span> <input class="ipt easyui-validatebox" data-options="required:true,missingMessage:'请输入密码'" " id="password" name="loginPassword"
					type="password" placeholder="请输入密码">
			</p>
			<div style="height: 50px; line-height: 50px; margin-top: 30px; border-top-color: rgb(231, 231, 231); border-top-width: 1px; border-top-style: solid;">
				<c:if test="${!empty message}">
					<div class="errorshowtip" style="display: block">${message}</div>
				</c:if>
				<c:if test="${empty message}">
					<div class="errorshowtip" style="display: none"></div>
				</c:if>
				<div class="loginbtn">
					<input type="button" id="loginbtn" value="登录" class="accept_btn"
						onclick="loginCheck()" />
					<input type="button" name="" value="关闭" class="closed" onclick="clearText()"/>
				</div>
			</div>
		</div>
	</form>
</BODY>
</HTML>
