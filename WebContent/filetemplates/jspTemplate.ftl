<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${baseUrl}/js/${inJsPath}/${atfnBo.moudLowerCaseName}/${atfnBo.defVarName}.js">
</script>

<script type="text/javascript">
	$(function() {
		${atfnBo.defVarName}DataGrid("${baseUrl}","${buttonsData}","${accessButtons}");
	});
</script>
<div class="easyui-layout" fit="true" id="${atfnBo.defVarName}-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="${atfnBo.defVarName}SearchForm">
			<div class="searchmore">
				<label>关键字:</label>
				<input class="imf_intxt" id="keyWord" style="width:180px;" name="keyWord" type="text"/>
				<span class="imf_more"><input id="${atfnBo.defVarName}BtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="${atfnBo.defVarName}BtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="${atfnBo.defVarName}-data-list"></table>
	</div>
	<div id="${atfnBo.defVarName}-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>${modName}信息列表</strong><span class="imf_pop_closed" onClick="popClosed('${atfnBo.defVarName}-edit-win')">关闭</span></div>
		<form id="${atfnBo.defVarName}EditForm" class="ui-form" method="post">
			<div class="imf_pop_con">
			<ul>
			 	<#if atfnBo.acnList?exists >
				<#list atfnBo.acnList as acn>
				<#if acn.checkIsPk ==1  >
			    <input type="hidden" name="${acn.p_colTfNames}">
			    <#else>
				<li>
					<strong>${acn.colChaName}</strong>
					<span>
						<input  id="${acn.p_colTfNames}" name="${acn.p_colTfNames}" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				</#if>
				</#list>
				</#if>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${atfnBo.defVarName}-save" value="保存" class="imf_pop_btn_save" onClick="${atfnBo.defVarName}Save('${baseUrl}')"/></span>
				<span><input type="button" id="${atfnBo.defVarName}-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${atfnBo.defVarName}-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="${atfnBo.defVarName}-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="${atfnBo.defVarName}-edit-info"><p></p></div>
		</form>
	</div>
</div>

