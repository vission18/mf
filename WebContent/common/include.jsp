<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.vission.mf.base.enums.BaseConstants"%>
<%
	String stylePath = BaseConstants.getStylePath();//样式路径
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    
    <style type="text/css">
		@import url("<%=stylePath%>/css/para.css");
		@import url("<%=stylePath%>/js/jquery-easyui-1.3.1/themes/default/easyui.css");
		@import url("<%=stylePath%>/js/jquery-easyui-1.3.1/themes/icon.css");
		@import url("<%=stylePath%>/js/loadmask/jquery.loadmask.css");
		@import url("<%=stylePath%>/js/jquery-easyui-portal/portal.css");
        @import url("<%=stylePath%>/css/login_maotouyin.css");
        @import url("<%=stylePath%>/css/frame.css");
	</style> 
    <!-- easyui -->
     <link rel="stylesheet" type="text/css" href="<%=stylePath%>/css/style-green.css" title="blue"></link>
    <script type="text/javascript">
    	var stylePath = '<%=stylePath%>';
    	baseUrl = '${pageContext.request.contextPath}';
<%--     	if(document.all && navigator.appVersion.split(";")[1].replace(/[ ]/g,"")==="MSIE6.0"){
    		document.writeln('<script type="text/javascript" src="<%=stylePath%>/js/jquery-easyui-1.3.1/jquery-1.7.2.min.js"></script>');
    	}else{
    		document.writeln('<script type="text/javascript" src="<%=stylePath%>/js/jquery-easyui-1.3.1/jquery-1.8.3.min.js"></script>');
    	} --%>
   	</script>
   	<script type="text/javascript" src="<%=stylePath%>/js/jquery-easyui-1.3.1/jquery-1.8.3.min.js"></script>
    <!-- jquery ui drag -->
    <script type="text/javascript" src="<%=stylePath%>/js/jquery-ui.drag.min.js"></script>
    <!-- easyui -->   	
    <script type="text/javascript" src="<%=stylePath%>/js/jquery-easyui-1.3.1/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="<%=stylePath%>/js/jquery-easyui-1.3.1/locale/easyui-lang-zh_CN.js"></script>
	<!-- form -->
	<script type="text/javascript" src="<%=stylePath%>/js/jquery.form.js"></script>
    <!-- loadmask -->
    <script type="text/javascript" src="<%=stylePath%>/js/loadmask/jquery.loadmask.js"></script>
    <!-- easyui portal插件 -->
	<script type="text/javascript" src="<%=stylePath%>/js/jquery-easyui-portal/jquery.portal.js" charset="utf-8"></script>
	<!-- cookie插件 -->
	<script type="text/javascript" src="<%=stylePath%>/js/jquery.cookie.js" charset="utf-8"></script>
    <!-- other -->
    <script type="text/javascript" src="<%=stylePath%>/js/syUtil.js"></script>
    <script type="text/javascript" src="<%=stylePath%>/js/style.js"></script>
    <script type="text/javascript" src="<%=stylePath%>/js/json2.js"></script>
    <script type="text/javascript" src="<%=stylePath%>/js/autoMergeCells.js"></script>
    <script type="text/javascript" src="<%=stylePath%>/js/My97DatePickerBeta4.8b3/My97DatePicker/WdatePicker.js"></script>
  </head>
  	<!--[if IE 6]>
	<script src="<%=stylePath%>/js/ie6png.js" mce_src="<%=stylePath%>/js/ie6png.js"></script>
    <script type="text/javascript">
        DD_belatedPNG.fix('.loginlogo,.logintop,.imf_nav li,.imf_nav li a,.imf_btnarea input,.imf_trunthepage a,.imf_user,.imf_other a,.imf_userbtn a,.imf_fastquit a,.imf_main,.imf_pop_btn_save,.imf_pop_btn_closed,.imf_dap_btn input,.imf_pop_closed,.errorshow,input,.imf_feedback,.imf_logo img,.back_project,.panel-tool a,#tree-menu .tree-icon,.tabs li,.imf_logo_sub img,.imf_left_arrow,.imf_right_arrow,.imf_logo_sub img,.imf_left_arrow,.imf_right_arrow,.tabs-selected .tabs-icon,.l-btn-empty');
    </script>
	<![endif]-->
</html>
