<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sellms/smsSignInf/smsSignInf.js">
</script>

<script type="text/javascript">
	$(function() {
		smsSignInfDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="smsSignInf-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="smsSignInfSearchForm">
			<div class="searchmore">
				<label>关键字:</label>
				<input class="imf_intxt" id="keyWord" style="width:180px;" name="keyWord" type="text"/>
				<span class="imf_more"><input id="smsSignInfBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="smsSignInfBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="smsSignInf-data-list"></table>
	</div>
	<div id="smsSignInf-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>短信签名信息列表</strong><span class="imf_pop_closed" onClick="popClosed('smsSignInf-edit-win')">关闭</span></div>
		<form id="smsSignInfEditForm" class="ui-form" method="post">
			<input type="hidden" name="xfId">
			<input type="hidden" name="recordDate">
			<input type="hidden" name="recordUser">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>主键</strong>
					<span>
						<input  id="PK_ID" name="PK_ID" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>签名描述</strong>
					<span>
						<input  id="SIGN_NAME" name="SIGN_NAME" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="smsSignInf-save" value="保存" class="imf_pop_btn_save" onClick="smsSignInfSave('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="smsSignInf-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('smsSignInf-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="smsSignInf-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="smsSignInf-edit-info"><p></p></div>
		</form>
	</div>
</div>

