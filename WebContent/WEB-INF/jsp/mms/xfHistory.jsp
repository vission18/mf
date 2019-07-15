<%@ page language="java" pageEncoding="UTF-8"%>
<!-- Js Start -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/mms/xfHistory.js"></script>
<script type="text/javascript">
	$(function() {
		xfHistoryDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="xfHistory-mainBody">
	<!-- Search panel start -->
	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="xfHistorySearchForm">
			<div class="searchmore">
				<label>消费类别:</label>
				<select  class="imf_intxt easyui-combobox"  id="historyType" name="xfType" style="width:100px"></select>
				<label>消费日期:</label>
				<input class="Wdate imf_intxt"  style="width:180px;" name="xfDate" type="text" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})"/>
				<label>记录日期:</label>
				<input class="Wdate imf_intxt"  style="width:180px;" name="recordDate" type="text" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})"/>
				<label>记录人:</label>
				<input type="text" name="recordUser" value="" class="imf_intxt"	style="width: 120px;" />
				<span class="imf_more"><input id="xfHistoryBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="xfHistoryBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->
	
	<!-- Data List -->
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="xfHistory-data-list"></table>
	</div>
	
	<div id="xfHistory-role-div"></div>
</div>

