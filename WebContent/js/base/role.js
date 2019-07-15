//数据列表
function roleDataGrid(baseUrl, buttons, type){
	var roleUrl = baseUrl + '/role/datagrid.do';
	var roleDeleteUrl = baseUrl + '/role/delete.do';
	
	var role_toolbar = [ {
		text : '新增',
		buttonType : 'add',
		iconCls : 'icon-add',
		handler : function() {
			createMenusTree(baseUrl + '/role/getAllMenuTree.do?', '');
			popWindow('role-edit-win', 'role-mainBody');
			$("#role-editForm").form('clear');
			
		}
	}, '-', {
		text : '修改',
		buttonType : 'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#role-data-list'));
			if (Utils.checkSelectOne(record)){
				roleSaveById(baseUrl, record[0]);
			}
		}
	}, '-', {
		text : '删除',
		buttonType : 'drop',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#role-data-list'));
			if (Utils.checkSelectOne(record)){
				popConfirm('确认删除角色['+record[0].roleName+']？','role-mainBody');
				$("#popConfirmYes").unbind('click');
				$("#popConfirmYes").click(function (){
					roleDeleteById(roleDeleteUrl,record[0]);
				});
			}
		}
	}, '-' ];
	
	var initDataGrid = {
		url : roleUrl,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'roleId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'roleName',
		sortOrder : 'asc',
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,//服务器端排序
		striped : true,//奇偶行不同颜色
		nowrap : false,//设置为true,当数据长度超出列宽时将会自动截取
		onDblClickRow:function(rowIndex, rowData) {
			roleSaveById(baseUrl,rowData);//双击进入修改
		},
		frozenColumns : [ [ {
			field : 'roleId',
			title : '角色编号',
			width : 50,
			checkbox : true
		}, {
			field : 'roleName',
			title : '角色名称',
			width : 250,
			sortable : true
		}
		] ],
		columns : [ [{
			field : 'roleRmk',
			title : '角色描述',
			width : 300
		}] ]
	};
	
	var role_newBars = getAccessButton(role_toolbar, buttons, type);
	if(role_newBars.length>0){
		initDataGrid['toolbar'] = role_newBars;
	}
	$('#role-data-list').datagrid(initDataGrid);
	
	$('#menus').combotree({
		multiple : true,
		cascadeCheck : false,
		checkbox : true,
		onCheck : function(node, checked){//checked时触发，选中的节点的父节点均被选中
			if(checked){
				getMenuIds(node.id, 'checked');
				n = $(this).tree('getParent', node.target);
				if(n != null && !n.checked){
					$(this).tree('check', n.target);
				}
			}
		},
		onBeforeCheck : function(node, checked){//unchecked时触发
			if(!checked){
				getMenuIds(node.id, 'unchecked');
			}
		}
	});
}

//通过对树的check与unchecked来级联更新隐藏的menuIds属性
function getMenuIds(id, oper){
	var menuIds = $('#menuIds').val().split(',');
	if(oper === 'checked'){//如果checked则在menuIds中添加该选中的Id
		var flag = false;
		for(var i=0; i<menuIds.length; i++){
			if(menuIds[i] === id){
				flag = true;
			}
		}
		if(!flag){
			menuIds.push(id);
		}
	}else if(oper === 'unchecked'){//如果unchecked则在menuIds中删除该Id
		for(var i=0; i<menuIds.length; i++){
			if(menuIds[i] === id){
				menuIds.splice(i, 1);
			}
		}
	}
	$('#menuIds').val(menuIds.join(','));
}

//重构MenuTree，附加属性
function createMenusTree(url, data){
	$('#menus').combotree({
		url : url + 'treeId=' + '&roleId=' + data.roleId,
		onBeforeExpand:function(node){//在展开前触发，动态加载
			if(data != ''){
				$('#menus').combotree('tree').tree('options').url = url + 'treeId=' + node.id + '&roleId=' + data.roleId;
			}
		},
		onLoadSuccess : function(node, d){//成功加载数据后触发，在修改时填充树的Text为menuNames
			if(data != ''){
				setTreeText(data.menuNames);
			}
		}
	});
}

//设置Tree的Text值
function setTreeText(text){
	$('#menus').combotree('setText', text);
}

function roleSaveById(url,data){
	$.ajax(url + '/role/getRoleById.do' ,{
		type:'post',
		 	dataType:'json',
		 	data:data,
		 	success:function(result){
		 		checkSession(result);
		 		popWindow('role-edit-win', 'role-mainBody');
				$('#role-editForm').form('load',result.data);
				$('#menuIds').val(result.data.menuIds);
				createMenusTree(url + '/role/getMenuTreeByRoleId.do?', result.data);
		 	},
		 	error:function(response, textStatus, errorThrown){
		 		
		 	},
		 	complete:function(){
		 	
		 	}
	});	
}

function roleDeleteById(url,data){
	$.post(url,data,
		function(result){
			checkSession(result);
			r = eval(result);
			if(r.success){
				index = $('#role-data-list').datagrid('getRowIndex', r.data.roleId);
				$('#role-data-list').datagrid('deleteRow', index);
			}
			rollDown("imf_roll",r.message);	
		}, "json"
	);
}

function roleSave(url){
	$('#role-editForm').form('submit', {
		url : url + '/role/save.do',
		success : function(result) {
			try {
				var r = $.parseJSON(result);
				if (r.success) {
					if(r.type === 'insert'){
						$('#role-data-list').datagrid('insertRow', {
							index : 0,
							row : r.data
						});
					}else if(r.type === 'update'){
						$('#role-data-list').datagrid('updateRow', {
							index : $('#role-data-list').datagrid('getRowIndex', r.data.roleId),
							row : r.data
						});
					}
					popInfo('role-info', r.message);
				}else{
					popInfo('role-error', r.message);
				}
			} catch (e) {
				popInfo('role-error', result);
			}
		}
	});
}

$(function(){
	$('#role-btnSearch').click(function(){
		$('#role-data-list').datagrid('load', serializeObject($('#role-searchForm')));
	});
	$('#role-btnClean').click(function(){
		$('#role-data-list').datagrid('load', {});
	});
});	

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});