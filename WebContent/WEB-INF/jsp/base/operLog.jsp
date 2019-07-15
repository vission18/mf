<%@ page language="java" pageEncoding="UTF-8"%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/base/operLog.js"></script>
	<script type="text/javascript">
		$(function(){
			dataGrid('${pageContext.request.contextPath}','${accessButtons.data}','${accessButtons.type}');
		});	
	</script>
	<div class="easyui-layout" fit="true" id="operLog-mainBody">
	
	<!-- Search panel start -->
	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="operLog_searchForm">
			<div class="searchmore">
				<label>登录名称:</label>
				<input type="text" name="loginName" value="" class="imf_intxt" style="width:80px;"/>
				<label>登录IP:</label>
				<input type="text" name="loginIp" value="" class="imf_intxt" style="width:80px;"/>
				<label>操作类型:</label>
				<input class="easyui-combobox" name="operType" 
					style="width:120px;" data-options="
								url:'${pageContext.request.contextPath}/operLog/getAllOperType.do',
								valueField:'id',
								textField:'text',
								multiple:true
						">
				<br/>
				<label>操作时间:</label>
				<input class="Wdate imf_intxt" style="width:200px;" name="beginOperTime" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>&nbsp;&nbsp;-
				<input class="Wdate imf_intxt" style="width:200px;" name="endOperTime" type="text" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
				<span class="imf_more"><input id="operLog_btnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="operLog_btnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->
	
    <!-- Data List -->
    <div data-options="region:'center',border:false" style="padding:5px;">
		<table id="operlog_data_list"></table>
	</div>
</div>
