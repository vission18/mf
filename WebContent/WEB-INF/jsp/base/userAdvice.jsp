<%@ page language="java" pageEncoding="UTF-8"%>
<!-- Js Start -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/userAdvice.js"></script>
<script type="text/javascript">
	$(function(){
		var path = '${pageContext.request.contextPath}';
		userAdviceDataGrid(path,"${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="userAdvice-mainBody">
	<!-- Search panel start -->
	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="userAdvice-searchForm">
			<div class="searchmore">
				<label>项目ID:</label>
				<input type="text" name="projectId" value="" class="imf_intxt"/>
				<label>页面名称:</label>
				<input type="text" name="pageId" value="" class="imf_intxt"/>
				<span class="imf_more"><input id="userAdvice-btnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="userAdvice-btnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->

	<!-- Data List -->
	<div data-options="region:'center',border:false">
		<table id="userAdvice-data-list"></table>
	</div>
</div>