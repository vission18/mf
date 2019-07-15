<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<script type="text/javascript">
	var page;
	var pageId;
	function modifyPwd() {
		if ($('#pwdForm').form('validate')) {
			$.post("${pageContext.request.contextPath}/user/modifyPwd.do", {
				loginPassword : $('#loginPassword').val(),
				newPassword : $('#newPassword').val()
			}, function(data) {
				data = eval(data);
				if (data.success) {
					popInfo("pwdInfo", data.message);
				} else {
					popInfo("pwdError", data.message);
				}
			}, "json");
		}
	}
	
	function modifyUser() {
		if ($('#modifyUserForm').form('validate')) {
			$.post("${pageContext.request.contextPath}/user/saveMyInfo.do", {
				userId : $('#userId').val(),
				loginName : $('#loginName').val(),
				userName : $('#userName').val(),
				userEmail : $('#userEmail').val(),
				userTel : $('#userTel').val(),
				userMobTel : $('#userMobTel').val()
			}, function(data) {
				data = eval(data);
				if (data.success) {
					popInfo("userInfo", data.message);
				} else {
					popInfo("userError", data.message);
				}
			}, "json");
		}
	}
	$(function() {
		//修改密码绑定事件
		$('#open_change_password').click(function() {
			popWindow('modify-pwd-win', 'mainBody');//打开
		});
		//修改用户绑定事件
		$('#open_change_user').click(function() {
			$('#loginName').attr({
				disabled : true
			});//设置disabled,不允许修改
			popWindow('modify-user-win', 'mainBody');
		});
		//帮助绑定事件
		$('#open_help_download').click(function() {
			$('#help-download-panel').panel({
				width:560,   
			    height:300, 
		    	href:'${pageContext.request.contextPath}/helpfile/list.do'   
	  		}); 			
			popWindow('help-download-win', 'mainBody');
		});
	});
	
	//打开用户建议窗口
	function openPageAdviceWin(pageVal) {
		page = pageVal;
		$('#userAdviceForm').form('clear');
		getProjectUserAdviceByUserId(page);
		popWindow('user-advice-win', 'mainBody');
	}

	//根据用户id 页面id获取用户建议
	function getProjectUserAdviceByUserId(page) {
		if (page != null) {
			if (page === 'project') {
				pageId = $('#project-data-list').datagrid('options').url;
			} else {
				var href = $('#main-center').tabs('getSelected').panel(
						'options').href;
				if (href == null) {
					pageId = '首页';
				} else {
					pageId = href.split("&")[0];
				}
			}
			$.ajax("${pageContext.request.contextPath}"
									+ '/advice/getUserAdviceByUserIdAndPageId.do?userId=${sessionInfo.user.userId}&pageId='
									+ pageId, {
								type : 'post',
								dataType : 'json',
								success : function(result) {
									$('#userAdviceForm').form('clear');
									/* checkSession(result); */
									$('#userAdviceForm').form('load',
											result.data);
								}
							});
		}
	}

	//用户建议保存
	function AddUserAdvice() {
		try {
			if (page != null && pageId != null) {
				$.post("${pageContext.request.contextPath}"
						+ '/advice/addUserAdvice.do', {
					adviceId : $('#adviceId').val(),
					pageId : pageId,
					advice : $('#advice').val()
				}, function(data) {
					data = eval(data);
					if (data.success) {
						popInfo("adviceInfo", data.message);
					} else {
						popInfo("adviceError", data.message);
					}
				}, "json");
			}
		} catch (e) {
			popInfo('user-edit-error', '参数错误');
		}
	}
</script>

	<!-- tab右键菜单 -->
	<div id="mm" class="easyui-menu" style="width:150px;display:none;">
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">关闭全部</div>
		<div id="mm-tabcloseother">关闭其他</div>
	</div>
	
	<div id="tab-tools">
		<a href="javascript:;" class="all_del" data-options="plain:true,iconCls:'icon-remove'" onclick="closeAllTab();" title="关闭全部">关闭</a>
	</div>

	<!--  modify password start -->
	<div class="imf_pop" id="modify-pwd-win" style="width:430px;">
		<div class="imf_pop_title"><strong>修改密码</strong><span class="imf_pop_closed" onclick="popClosed('modify-pwd-win')">关闭</span></div>
		<form id="pwdForm" action="user/modifyPwd.do" class="ui-form" method="post">
			<div class="imf_pop_con">
				<ul>
					<li><strong>旧密码：</strong> <span> <input
							id="loginPassword" name="loginPassword" type="password"
							class="imf_intxt easyui-validatebox" data-options="required:true,missingMessage:'请输入旧密码.'" />
					</span>
					</li>
					<li><strong>新密码：</strong> <span> <input
							id="newPassword" name="newPassword" type="password"
							class="imf_intxt easyui-validatebox" data-options="required:true,missingMessage:'请输入新密码.'" />
					</span>
					</li>
					<li><strong>确认新密码：</strong> <span> <input
							id="confirmPassword" name="confirmPassword" type="password"
							class="imf_intxt easyui-validatebox" data-options="required:true,missingMessage:'请输入确认新密码.'" /> 
					</span>
					</li>
				</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" name="" value="保存"
					class="imf_pop_btn_save" onclick="modifyPwd()" /> </span> <span><input
					type="button" name="" value="关闭" class="imf_pop_btn_closed"
					onclick="popClosed('modify-pwd-win')" /> </span>
			</div>
			<div class="imf_pop_error" id="pwdError">
				<p></p>
			</div>
			<div class="imf_pop_correct" id="pwdInfo">
				<p></p>
			</div>
		</form>
	</div>
	<!-- modify password end  -->
	<!--  modify user start -->
	<div id="modify-user-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title">
			<strong>修改我的信息</strong><span class="imf_pop_closed" onclick="popClosed('modify-user-win')">关闭</span>
		</div>
		<form id="modifyUserForm" class="ui-form" method="post"
			action="${pageContext.request.contextPath}/user/saveMyInfo.do">
			<!-- action="user/saveMyInfo.do" -->
			<div class="imf_pop_con">
				<input type="hidden" name="userId"
					value="${sessionInfo.user.userId}" id="userId" />
				<ul>
					<li><strong> 登录名： </strong> <span> <input
							class="imf_intxt easyui-validatebox" type="text" id="loginName"
							name="loginName" value="${sessionInfo.user.loginName}"
							id="loginName" /> </span>
					</li>
					<li><strong> 用户名：</strong> <span> <input
							class="imf_intxt easyui-validatebox" type="text" id="userName"
							name="userName" value="${sessionInfo.user.userName}"
							data-options="required:true,missingMessage:'请输入用户名称.'" /> </span>
					</li>
					<li><strong> 邮箱地址：</strong> <span> <input
							class="imf_intxt easyui-validatebox" type="text" name="userEmail"
							id="userEmail" value="${sessionInfo.user.userEmail}"
							data-options="required:false,validType:'email',missingMessage:'请输入邮箱地址.',invalidMessage:'输入的邮箱格式不正确.'" />
					</span>
					</li>
					<li><strong> 固定电话：</strong> <span> <input
							class="imf_intxt easyui-validatebox" type="text" id="userTel"
							name="userTel" value="${sessionInfo.user.userTel}"
							data-options="required:false" /> </span>
					</li>
					<li><strong> 移动电话：</strong> <span> <input
							class="imf_intxt easyui-validatebox" type="text" id="userMobTel"
							name="userMobTel" value="${sessionInfo.user.userMobTel}"
							data-options="required:false" /> </span>
					</li>
				</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" name="" value="保存"
					class="imf_pop_btn_save" onclick="modifyUser()" /> </span> <span><input
					type="button" name="" value="关闭" class="imf_pop_btn_closed"
					onclick="popClosed('modify-user-win')" /> </span>
			</div>
			<div class="imf_pop_error" id="userError">
				<p></p>
			</div>
			<div class="imf_pop_correct" id="userInfo">
				<p></p>
			</div>
		</form>
	</div>
	<!--  modify user end  -->
	
	<!--  help download start -->
	<div id="help-download-win" class="imf_pop" style="width:560px;height:300px;">
		<div class="imf_pop_title">
			<strong>帮助文档</strong><span class="imf_pop_closed" onclick="popClosed('help-download-win')">关闭</span>
		</div>
		<div id="help-download-panel" class="easyui-panel">
		</div>
	</div>
	<!--  help download end  -->
	
	<!-- modify password end  -->
	<div class="imf_dap" id="pop_confirm">
		<div class="imf_dap_title"><strong>确认信息</strong><span class="imf_pop_closed" onclick="popClosed('pop_confirm')">关闭</span></div>
		<div class="imf_dap_con"><p id="confirmMessage"></p></div>
		<div class="imf_dap_btn">
			<span><input type="button" id="popConfirmYes" value="确认" class="imf_dap_btn_save" onclick="popClosed('pop_confirm');"/></span>
			<span><input type="button" name="" value="取消" class="imf_dap_btn_closed" onclick="popClosed('pop_confirm');"/></span>
		</div>
  	</div>
  	
  	<div class="imf_roll">
		<div class="imf_roll_title"><strong>提示信息</strong><span class="imf_roll_closed">关闭</span></div>
		<div class="imf_roll_con"><p id="rollMessage"></p></div>
  	</div>
	
	<div id="ims-ds-tb-right-menu" class="easyui-menu" style="width:120px;display:none;">
		<div onclick="imsSubDataSetTable('${pageContext.request.contextPath}', 'edit')" data-options="iconCls:'icon-edit'">修改数据表</div>
		<div onclick="imsSubDataSetTable('${pageContext.request.contextPath}', 'del')" data-options="iconCls:'icon-remove'">删除数据表</div>
	</div>
	<!--  user advice start -->
	<div id="user-advice-win" class="imf_pop" style="width:480px;">
		<div class="imf_pop_title">
			<strong>用户意见</strong><span class="imf_pop_closed" onclick="popClosed('user-advice-win')">关闭</span>
		</div>
		<form id="userAdviceForm" class="ui-form" method="post">
			<!-- action="advice/addUserAdvice.do" -->
			<input type="hidden" name="adviceId" id="adviceId" >
			<div class="imf_pop_con">
				<ul>
					<li><strong>您的建议：</strong> <span> <textarea id="advice"
							name="advice" rows="8" cols="70" class="imf_textarea" maxlength="200"></textarea>
							</span>
					</li>
				</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" name="" value="保存"
					class="imf_pop_btn_save" onclick="AddUserAdvice()" /> </span> <span><input
					type="button" name="" value="关闭" class="imf_pop_btn_closed"
					onclick="popClosed('user-advice-win')" /> </span>
			</div>
			<div class="imf_pop_error" id="adviceError">
				<p></p>
			</div>
			<div class="imf_pop_correct" id="adviceInfo">
				<p></p>
			</div>
		</form>
	</div>
	<!--  user advice end  -->
