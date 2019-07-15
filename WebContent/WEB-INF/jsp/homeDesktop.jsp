<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/base/anno.js"></script>
	<script type="text/javascript">
		$(function(){
			homeAnnoDataGrid("${pageContext.request.contextPath}");
		});
	</script>
	<div class="easyui-layout" fit="true" id="homeDesktop-body">
    	<div data-options="region:'center',border:false,title:''">
			<table id="admin-home-anno-data-list"></table>
		</div>
	</div>

	<!--公告查看开始  -->
	<div id="anno-read-win-exp" class="imf_pop" style="width:550px;">
		<div class="imf_pop_title"><strong>公告信息</strong><span class="imf_pop_closed" onClick="closeReadWindow('exp')">关闭</span></div>
		<div class="imf_pop_con cl">
			<ul>
				<li><strong>&nbsp;&nbsp;公告类型：</strong> <span id = "annoTypeForReadexp"></span></li>
				<li><strong>&nbsp;&nbsp;公告标题：</strong> <span id = "annoTitleForReadexp"></span></li>
				<li><strong>&nbsp;&nbsp;发布用户：</strong> <span id = "loginNameForReadexp"></span></li>
				<li><strong>&nbsp;&nbsp;发布日期：</strong> <span id = "createDtForReadexp"></span></li>
				<li><strong>&nbsp;&nbsp;有效日期：</strong> <span id = "youxiaoDateForReadexp"></span></li>
				<li><strong>&nbsp;&nbsp;附加下载：</strong> <span id = "annoAcceForReadexp"></span></li>
				<li><strong>&nbsp;&nbsp;公告内容：</strong> <span><textarea id="annoContentForReadexp"
								 rows="100" cols="100" class="imf_textarea" style="min-width:300px;width:400px"></textarea></span></li>
			</ul>
		</div>
		<div class="imf_pop_btn">
			<span><input type="button" value="返回"  class="imf_pop_btn_closed" onClick="closeReadWindow('exp')"/></span>
		</div>
	</div>
	<!-- 公告查看结束 -->