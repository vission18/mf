<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sellms/smsvarinfo/smsVarInfo.js">
</script>

<script type="text/javascript">
	$(function() {
		smsVarInfoDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="smsVarInfo-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="smsVarInfoSearchForm">
			<div class="searchmore">
				<label>关键字:</label>
				<input class="imf_intxt" id="keyWord" style="width:180px;" name="keyWord" type="text"/>
				<span class="imf_more"><input id="smsVarInfoBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="smsVarInfoBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="smsVarInfo-data-list"></table>
	</div>
	<div id="smsVarInfo-edit-win" class="imf_pop" style="width:500px;">
		<div class="imf_pop_title"><strong>短信变量信息列表</strong><span class="imf_pop_closed" onClick="popClosed('smsVarInfo-edit-win')">关闭</span></div>
		<form id="smsVarInfoEditForm" class="ui-form" method="post">
			<div class="imf_pop_con">
			<ul>
			    <input type="hidden" name="pkId">
				<li>
					<strong>变量名称</strong>
					<span>
						<input  id="varName" name="varName" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>变量说明</strong>
					<span> <textarea name="varDesc" rows="4" cols="90" class="imf_textarea" ></textarea> </span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="smsVarInfo-save" value="保存" class="imf_pop_btn_save" onClick="smsVarInfoSave('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="smsVarInfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('smsVarInfo-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="smsVarInfo-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="smsVarInfo-edit-info"><p></p></div>
		</form>
	</div>
</div>

