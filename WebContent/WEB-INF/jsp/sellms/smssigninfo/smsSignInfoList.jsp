<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sellms/smssigninfo/smsSignInfo.js">
</script>

<script type="text/javascript">
	$(function() {
		smsSignInfoDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="smsSignInfo-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="smsSignInfoSearchForm">
			<div class="searchmore">
				<label>关键字:</label>
				<input class="imf_intxt" id="keyWord" style="width:180px;" name="keyWord" type="text"/>
				<span class="imf_more"><input id="smsSignInfoBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="smsSignInfoBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="smsSignInfo-data-list"></table>
	</div>
	<div id="smsSignInfo-edit-win" class="imf_pop" style="width:500px;">
		<div class="imf_pop_title"><strong>短信签名信息列表</strong><span class="imf_pop_closed" onClick="popClosed('smsSignInfo-edit-win')">关闭</span></div>
		<form id="smsSignInfoEditForm" class="ui-form" method="post">
			<input type="hidden" name="pkId">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>签名</strong>
					<span>
						<input  id="signName" name="signName" maxlength="50" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>描述</strong>
					<span> <textarea id ="signDesc" name="signDesc" rows="8" cols="70" class="imf_textarea"></textarea> </span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="smsSignInfo-save" value="保存" class="imf_pop_btn_save" onClick="smsSignInfoSave('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="smsSignInfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('smsSignInfo-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="smsSignInfo-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="smsSignInfo-edit-info"><p></p></div>
		</form>
	</div>
</div>

