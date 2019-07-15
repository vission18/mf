<%@ page language="java" pageEncoding="UTF-8"%>
<!-- Js Start -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/role.js"></script>
<script type="text/javascript">
	$(function(){
		var path = '${pageContext.request.contextPath}';
		roleDataGrid(path,"${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="role-mainBody">
	<!-- Search panel start -->
	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="role-searchForm">
			<div class="searchmore">
				<label>角色名称:</label>
				<input type="text" name="roleName" value="" class="imf_intxt" style="width:120px;"/>
				<label>角色描述:</label>
				<input type="text" name="roleRmk" value="" class="imf_intxt" style="width:200px;"/>
				<span class="imf_more"><input id="role-btnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="role-btnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->

	<!-- Data List -->
	<div data-options="region:'center',border:false">
		<table id="role-data-list"></table>
	</div>

	<!-- Edit Win&Form -->
	<div id="role-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>角色维护</strong><span class="imf_pop_closed" onClick="popClosed('role-edit-win')">关闭</span></div>
		<form id="role-editForm" class="ui-form" method="post">
			<input type="hidden" name="roleId">
			<input type="hidden" name="menuIds" id="menuIds">
			<div class="imf_pop_con">
				<ul>
					<li><strong>角色名称：</strong><span><input class="imf_intxt easyui-validatebox" type="text" data-options="required:true, missingMessage:'请填写角色名称.'"
						id="roleName" name="roleName"></span>
					</li>
					<li><strong>角色描述：</strong><span><input class="imf_intxt easyui-validatebox" type="text" 
						id="roleRmk" name="roleRmk"></span>
					</li>
					<li><strong>角色权限：</strong><span><select id="menus" name="menus" style="width:250px;" class="imf_intxt easyui-combotree" ></select></span>
					</li>
				</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="role-save" value="保存" class="imf_pop_btn_save" onClick="roleSave('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="role-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('role-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="role-error"><p></p></div>
			<div class="imf_pop_correct" id="role-info"><p></p></div>
		</form>
	</div>
</div>
