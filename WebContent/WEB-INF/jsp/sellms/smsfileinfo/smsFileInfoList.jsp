<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sellms/smsfileinfo/smsFileInfo.js">
</script>

<script type="text/javascript">
	$(function() {
		smsFileInfoDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="smsFileInfo-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="smsFileInfoSearchForm">
			<div class="searchmore">
				<label>关键字:</label>
				<input class="imf_intxt" id="keyWord" style="width:180px;" name="keyWord" type="text"/>
				<span class="imf_more"><input id="smsFileInfoBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="smsFileInfoBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="smsFileInfo-data-list"></table>
	</div>
	<div id="smsFileInfo-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>我的文件信息列表</strong><span class="imf_pop_closed" onClick="popClosed('smsFileInfo-edit-win')">关闭</span></div>
		<form id="smsFileInfoEditForm" class="ui-form" method="post">
			<div class="imf_pop_con">
			<ul>
			    <input type="hidden" name="pkId">
				<li>
					<strong>文件名称</strong>
					<span>
						<input  id="fileName" name="fileName" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>文件路径</strong>
					<span>
						<input  id="filePath" name="filePath" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>电话抬头</strong>
					<span>
						<input  id="phoneKey" name="phoneKey" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>创建人</strong>
					<span>
						<input  id="createUser" name="createUser" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>创建时间</strong>
					<span>
						<input  id="createTime" name="createTime" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="smsFileInfo-save" value="保存" class="imf_pop_btn_save" onClick="smsFileInfoSave('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="smsFileInfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('smsFileInfo-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="smsFileInfo-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="smsFileInfo-edit-info"><p></p></div>
		</form>
	</div>
</div>

