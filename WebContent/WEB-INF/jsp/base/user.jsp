<%@ page language="java" pageEncoding="UTF-8"%>
<!-- Js Start -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/user.js"></script>
<script type="text/javascript">
	$(function() {
		userDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="user-mainBody">
	<!-- Search panel start -->
	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="userSearchForm">
			<div class="searchmore">
				<label>登录名称:</label>
				<input type="text" name="loginName" value="" class="imf_intxt" style="width:80px;"/>
				<label>用户姓名:</label>
				<input type="text" name="userName" value="" class="imf_intxt" style="width:120px;"/>
				<label>邮件地址:</label>
				<input type="text" name="userEmail" value="" class="imf_intxt" style="width:120px;"/>
				<span class="imf_more"><input id="userBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="userBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->
	
	<!-- Data List -->
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="user-data-list"></table>
	</div>
	
	<div id="user-role-div"></div>
	
	<!-- Edit Win&Form -->
	<div id="user-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>用户维护</strong><span class="imf_pop_closed" onClick="popClosed('user-edit-win')">关闭</span></div>
		<form id="userEditForm" class="ui-form" method="post">
			<input type="hidden" name="userId">
			<input type="hidden" name="loginPassword">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>登录名：</strong>
					<span><input class="imf_intxt easyui-validatebox"  data-options="required:true,missingMessage:'请输入登录名称.'" type="text" id="login_name"
						name="loginName"></span>
				</li>
				<li>
					<strong>用户名：</strong>
					<span><input class="imf_intxt easyui-validatebox"  data-options="required:true,missingMessage:'请输入用户名称.'" type="text" id="user_name" name="userName" data-options="required:true"></span>
				</li>
				<li>
					<strong>用户状态：</strong>
					<span>
						<input name="userStatus" type="radio" value="1" checked="checked"/><label>启用</label>
						<input name="userStatus" type="radio" value="0" /><label>停用</label>
					</span>
				</li>
					<li>
					<strong>用户类型：</strong>
					<span>
						<select id="userType" class="imf_intxt easyui-combobox" name="userType" style="width:200px;">  
						    <option value="1">系统管理员</option>  
						    <option value="2">普通用户</option>  
						</select>  
					</span>
				</li>
<!-- 				<li>
					<strong>所属机构：</strong>
					<select id="userBranchTree" name="branchNo" style="width:250px;" class="imf_intxt easyui-combotree" ></select>
				</li> -->
				<li>
					<strong>邮箱地址：</strong>
					<span>
					<input class="imf_intxt easyui-validatebox"  type="text" name="userEmail" id="user_email"
						data-options="required:false,validType:'email',missingMessage:'邮箱不能为空.',invalidMessage:'邮箱格式不正确'">
					</span>
				</li>
				<li>
					<strong>固定电话：</strong>
					<span>
					<input id="user-tel" class="imf_intxt easyui-numberbox" type="text" name="userTel">
					</span>
				</li>
				<li>
					<strong>移动电话：</strong>
					<span>
					<input class="imf_intxt easyui-numberbox" type="text" name="userMobTel" id="user-mobile">
				</span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="user-save" value="保存" class="imf_pop_btn_save" onClick="userSave('${pageContext.request.contextPath}/user/save.do')"/></span>
				<span><input type="button" id="user-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('user-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="user-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="user-edit-info"><p></p></div>
		</form>
	</div>
	
	<div id="user-import-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>用户批量导入</strong><span class="imf_pop_closed" onClick="popClosed('user-import-win')">关闭</span></div>
		<form id="user-import-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
			<li><strong>模板下载：</strong><span><a id="download-btn" href="${pageContext.request.contextPath}/user/downExcel.do"></a></span></li>
			<li><strong>导入文件：</strong><span><input id="user-file" name="file" type="file" class="imf_intxt"/></span></li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="import-user-save" value="导入" class="imf_pop_btn_save" onClick="importUser('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="import-user-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('user-import-win')"/></span>
			</div>
			<div class="imf_pop_error" id="user-import-error"><p></p></div>
			<div class="imf_pop_correct" id="user-import-info"><p></p></div>
		</form>
	</div>
	
	<div id="user-role-win" class="imf_pop" style="height:350px;width:480px;">
		<div class="imf_pop_title"><strong id="user-role-title"></strong><span class="imf_pop_closed" onClick="popClosed('user-role-win')">关闭</span></div>
		<form id="userRoleForm" method="post">
			<div class="imf_pop_con">
			<table id="user-role-list" ></table>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="user-role-save" value="保存" class="imf_pop_btn_save"/></span>
				<span><input type="button" id="user-role-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('user-role-win')"/></span>
			</div>
			<div class="imf_pop_error" id="user-role-error"><p></p></div>
			<div class="imf_pop_correct" id="user-role-info"><p></p></div>
		</form>
	</div>
</div>

