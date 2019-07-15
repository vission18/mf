<%@ page language="java" pageEncoding="UTF-8"%>
<!-- Js Start -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/mms/xfType.js"></script>
<script type="text/javascript">
	$(function() {
		xfTypeDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="xfType-mainBody">
	<!-- Search panel start -->
	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="xfTypeSearchForm">
			<div class="searchmore">
				<label>类别名称:</label>
				<input type="text" name="typeName" value="" class="imf_intxt" style="width:120px;"/>
<!-- 				<label>类型状态:</label>
				<input type="text" name="typeStatus" value="" class="imf_intxt" style="width:120px;"/> -->
				<span class="imf_more"><input id="xfTypeBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="xfTypeBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->
	
	<!-- Data List -->
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="xfType-data-list"></table>
	</div>
	
	<div id="xfType-role-div"></div>
	
	<!-- Edit Win&Form -->
	<div id="xfType-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>类别信息</strong><span class="imf_pop_closed" onClick="popClosed('xfType-edit-win')">关闭</span></div>
		<form id="xfTypeEditForm" class="ui-form" method="post">
			<input type="hidden" name="typeId">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>类别名称：</strong>
					<span><input class="imf_intxt easyui-validatebox"  data-options="required:true,missingMessage:'请输入类别名称.'" type="text" id="type_name"
						name="typeName"></span>
				</li>
				<li>
					<strong>类别状态：</strong>
					<span>
						<input name="typeStatus" type="radio" value="1" checked="checked"/><label>启用</label>
						<input name="typeStatus" type="radio" value="0" /><label>停用</label>
					</span>
				</li>
				<li>
		    		<strong>备注：</strong>
		    		<span><textarea id="remark" name="remark" maxlength="512"
						 rows="100" cols="100" class="imf_textarea" style="min-width:200px;width:300px"></textarea>
					</span>
				 </li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="xfType-save" value="保存" class="imf_pop_btn_save" onClick="xfTypeSave('${pageContext.request.contextPath}/xfTypeController/save.do')"/></span>
				<span><input type="button" id="xfType-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('xfType-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="xfType-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="xfType-edit-info"><p></p></div>
		</form>
	</div>
	
	<div id="xfType-import-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>导入Excel</strong><span class="imf_pop_closed" onClick="popClosed('xfType-import-win')">关闭</span></div>
		<form id="xfType-import-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
			<li><strong>模板下载：</strong><span><a id="ModelDownload-btn" href="${pageContext.request.contextPath}/xfTypeController/downExcel.do"></a></span></li>
			<li><strong>导入文件：</strong><span><input id="xfType-file" name="file" type="file" class="imf_intxt"/></span></li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="import-xfType-save" value="导入" class="imf_pop_btn_save" onClick="importxfType('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="import-xfType-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('xfType-import-win')"/></span>
			</div>
			<div class="imf_pop_error" id="xfType-import-error"><p></p></div>
			<div class="imf_pop_correct" id="xfType-import-info"><p></p></div>
		</form>
	</div>
</div>

