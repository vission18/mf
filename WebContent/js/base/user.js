//数据列表
function userDataGrid(baseUrl, buttons, type){
	var user_url = baseUrl + '/user/dataGrid.do';
	var user_delete_url = baseUrl + '/user/delete.do';
	var user_rest_pwd_url = baseUrl + '/user/resetPwd.do';
	var initToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('user-edit-win', 'user-mainBody');
			$('#userEditForm').form('clear');
			$('#userEditForm').form('load',{userType:2});
			$('#login_name').removeAttr("disabled");// 去除disabled
			$('#login_name').focus();
			$(':radio[name="userStatus"]').eq(0).attr("checked",true); // 设置第一个radio为选中
			//createBranchTree(baseUrl);
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#user-data-list'));
			if (Utils.checkSelectOne(record)){
					userSaveById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'drop',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#user-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除用户['+record[0].userName+']？','user-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						userDeleteById(user_delete_url,record[0]);
					});
				}
			}
	},'-',{
		text : '用户角色',
		buttonType:'userRole',
		iconCls : 'icon-imf-role',
		handler : function() {
			var record = Utils.getCheckedRows($('#user-data-list'));
			if (Utils.checkSelectOne(record)){
					userRole(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '重置密码',
		buttonType:'resetPwd',
		iconCls : 'icon-imf-password',
		handler : function(){
			var record = Utils.getCheckedRows($('#user-data-list'));
			if (Utils.checkSelectOne(record)){
				popConfirm('确认重置['+record[0].userName+']用户密码？','user-mainBody');
				$("#popConfirmYes").unbind('click');
				$("#popConfirmYes").click(function (){
					resetPwd(user_rest_pwd_url,record[0]);
				});
			}
		}
	},'-',{
		text : '批量导入',
		buttonType : 'banch',
		iconCls : 'icon-imf-import',
		handler : function(){
			popWindow('user-import-win', 'user-mainBody');
			$('#user-import-form').form('clear');
		}
	}];
	var initDataGrid = {
		url : user_url,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'userId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'loginName',
		sortOrder : 'asc',
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,// 服务器端排序
		striped : true,// 奇偶行不同颜色
		nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
		onDblClickRow:function(rowIndex, rowData) {
				userSaveById(baseUrl,rowData);// 双击进入修改
		},
		frozenColumns : [ [ {
			field : 'userId',
			title : '用户编号',
			width : 50,
			checkbox : true
		}, {
			field : 'loginName',
			title : '登录名称',
			width : 120,
			sortable : true
		}, {
			field : 'userName',
			title : '用户姓名',
			width : 180,
			sortable : true
		} ] ],
		columns : [ [ {
			field : 'userStatus',
			title : '用户状态',
			width : 80,
			sortable : true,
			formatter : function(value, row, index) {// 标识转成汉字
				if (value) {
					return '启用';
				} else {
					return '<span style=\'color:red\'>停用</span>';// 特殊颜色标识
				}
			}			
		}, {
			field : 'userEmail',
			title : '邮件地址',
			width : 150
		}, {
			field : 'userTel',
			title : '固定电话',
			width : 150
		}, {
			field : 'userMobTel',
			title : '手机号码',
			width : 150
		} ] ]
	};
	var newBars = getAccessButton(initToolbar,buttons,type);
	if(newBars.length>0){
		initDataGrid['toolbar'] = newBars;
	}
	$('#user-data-list').datagrid(initDataGrid);
	
	$('#download-btn').linkbutton({
		iconCls: 'icon-save',
		text: '下载'
	});
	
	$('#userBtnSearch').click(function(){// 查询
		$('#user-data-list').datagrid('load', serializeObject($('#userSearchForm')));
	});
	$('#userBtnClean').click(function(){// 全部
		$('#user-data-list').datagrid('load', {});
	});
}

//生成机构树
//function createBranchTree(baseUrl){
//	$('#userBranchTree').combotree({  
//    	url: baseUrl+'/user/firstTreeNode.do',  
//    	required : true,
//	    missingMessage : '请选择机构.',
//	    onBeforeExpand:function(node) {
//        $('#userBranchTree').combotree('tree').tree('options').url = baseUrl+'/user/getChildNodeForTree.do?parentId=' + node.id;
//	  }
//	});  
//}

// 获取选中记录,弹出修改窗口
function userSaveById(baseUrl,data){
	//createBranchTree(baseUrl);
	$.ajax(baseUrl + '/user/getById.do', {
		type:'post',
		 	dataType:'json',
		 	data:data,
		 	success:function(result){
		 		checkSession(result);
		 		r1 = getBranchNameById(baseUrl, result.data);
				$('#userEditForm').form('load',result.data);
				//$('#userBranchTree').combotree('setText', r1.data);
				if(result.data.userStatus){
					$(':radio[name="userStatus"]').eq(0).attr("checked",true); // 设置第一个radio为选中
				}else{
					$(':radio[name="userStatus"]').eq(1).attr("checked",true); 
				}
				$('#login_name').attr({ disabled: true });//设置disabled,不允许修改
				popWindow('user-edit-win', 'user-mainBody');
		 	}
	});	
}

// 删除选中记录
function userDeleteById(url,data){
	$.post(url,data,
			function(result){
				checkSession(result);
				r = eval(result);
				if(r.success){
					index = $('#user-data-list').datagrid('getRowIndex', r.data.userId);
					$('#user-data-list').datagrid('deleteRow', index);
				}
				rollDown("imf_roll",r.message);
			}, 'json'
		);
}
// 新增\修改 提交
function userSave(url){
	if($('#userEditForm').form('validate')){
		$('#userEditForm').form('submit', {
			url : url,
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						if(r.type === 'insert'){
							$('#user-data-list').datagrid('insertRow', {
								index : 0,
								row : r.data
							});
						}else if(r.type === 'update'){
							var record = Utils.getCheckedRows($('#user-data-list'));
							var data ={};
							var idKey = 'userId'; // 主键名称
		 					data[idKey] = (record[0][idKey]);
		 					r.data.loginName = data.loginName;// loginName不允许修改的列
							$('#user-data-list').datagrid('updateRow', {
								index : $('#user-data-list').datagrid('getRowIndex', r.data.userId),
								row : r.data
							});
						}
					}
					popInfo('user-edit-info', r.message);
				} catch (e) {
					popInfo('user-edit-error', result);
				}
			}
		});
	}
}

//重置密码
function resetPwd(url, data){
	$.post(url,'userId=' + data.userId,
			function(result){
				checkSession(result);
				r = eval(result);
				if(r.success){
					rollDown("imf_roll",'[' + r.data.userName + ']' + r.message);
				}
			}, 'json'
	);
}

//通过id获得机构名称
function getBranchNameById(baseUrl, data)
{
	var r = null;
	    $.ajax(
	    {
	        type: 'post',
	        datatype: 'json',
	        url: baseUrl + '/user/getBranchNameById.do', 
	        data: 'branchNo=' + data.branchNo,
	        async: false, // 是否为当前的请求触发全局AJAX事件处理函数
	        beforeSend:function(XMLHttpRequest){
	             
	        },
	        success: function (result) {
	        	checkSession(result);
	        	r = eval('(' + result + ')');
	        }
	    });
	return r;
}

//批量导入用户
function importUser(BaseUrl){
	reg = /(\.xls|\.XLS|\.xlsx|\.XLSX)$/; //匹配excel文件格式
	if($('#user-file').val() === ''){
		popInfo('user-import-error', '请选择文件！');
		return false;
	}
	if(!reg.test($('#user-file').val())){
		popInfo('user-import-error', '请选择正确的文件类型！');
		return false;
	}
	$('#user-import-form').form('submit',{
		url : BaseUrl + '/user/uploadExcel.do',
		success : function(result){
			try{
				var r = $.parseJSON(result);
				if(r.success){
					$('#user-data-list').datagrid('reload');
				}else{
					popInfo('user-import-error', r.message);
					return false;
				}
				popInfo('user-import-info', r.message);
			}catch(e){
				popInfo('user-import-error', result);
			}
		}
	});
}

//加载用户角色页面
function userRole(baseUrl,data) {
	$('#user-role-title').html(data.userName + '-角色配置');
	popWindow('user-role-win', 'user-mainBody');
	userRoleDataGrid(baseUrl,data);
	$("#user-role-save").unbind('click');
	$('#user-role-save').bind('click', function(){
		userRoleSave(baseUrl + '/user/userRoleSave.do', data.userId);
	});
}

//加载用户角色数据
function userRoleDataGrid(baseUrl,data){
	$('#user-role-list').datagrid({
		url : baseUrl + '/user/userRoleDataGrid.do?userId='+data.userId,
		//fit : true,   //加上这个列表只显示一个表头,数据被遮住,不知道什么情况...
		height : 270,
		fitColumns : false,
		border : true,
		pagination : false,
		idField : 'roleId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'roleName',
		sortOrder : 'asc',
		singleSelect : false,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,//服务器端排序
		striped : true,//奇偶行不同颜色
		nowrap : false,//设置为true,当数据长度超出列宽时将会自动截取
		frozenColumns : [ [ {
			field : 'checked',
			hidden : true
		}, {
			field : 'roleId',
			title : '角色编号',
			width : 50,
			checkbox : true
		}, {
			field : 'roleName',
			title : '角色名称',
			width : 160,
			sortable : true
		} ] ],
		columns : [ [ {
			field : 'roleRmk',
			title : '角色描述',
			width : 200
		} ] ],
		onLoadSuccess : function() {
			rows = $('#user-role-list').datagrid('getRows');
			$.each(rows,function(i,data){
				if(data.checked){
					$('#user-role-list').datagrid('selectRecord',data.roleId);
				}else{
					$('#user-role-list').datagrid('unselectRow',i);
				}
			});
		}
	});
}

//保存用户角色
function userRoleSave(url,userId){
	var checkedRows = $('#user-role-list').datagrid('getChecked');
	var ids = [];
	for ( var i = 0; i < checkedRows.length; i++) {
		ids.push(checkedRows[i].roleId);
	}
	ids = ids.join(',');
	$.post(url,{userId:userId,ids:ids},
		function(result){
			checkSession(result);
			if(result.success){
				popInfo('user-role-info', result.message);
			}else{
				popInfo('user-role-error', result);
			}
		}, "json"
	);
}

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});
