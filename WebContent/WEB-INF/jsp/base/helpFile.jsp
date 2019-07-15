<!-- Js Start -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/helpFile.js"></script>

<script type="text/javascript">
<!--
	$(function(){
		helpFileDataGrid("${pageContext.request.contextPath}");
	});
-->
</script>
<div class="easyui-layout" fit="true">
	<!-- Data List -->
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="helpfile-data-list"></table>
	</div>
</div>