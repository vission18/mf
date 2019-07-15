//数据列表
function annoTypeDataGrid(baseUrl, buttons, type) {
	annoTypeUrl = baseUrl + '/annoType/dataGrid.do';
	annoTypeSaveUrl = baseUrl + '/annoType/getAnnoTypeById.do';
	annoTypeDeleteUrl = baseUrl + '/annoType/delete.do';
	var initToolbarAnnoType = [ {
		text : '新增',
		iconCls : 'icon-add',
		buttonType : 'add',
		handler : function(){
			popWindow('annoType-edit-win', 'annoType-body');
			$("#annoTypeEditForm").form('clear');
		}
	}, {
		text : '修改',
		iconCls : 'icon-edit',
		buttonType : 'edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#annoType-data-list'));
			if (Utils.checkSelectOne(record)) {
				var data = {};
				var idKey = 'typeId';
				data[idKey] = (record[0][idKey]);
				annoTypeSaveById(annoTypeSaveUrl, data);
			}
		}
	}, {
		text : '删除',
		iconCls : 'icon-remove',
		buttonType : 'drop',
		handler : function() {
			var record = Utils.getCheckedRows($('#annoType-data-list'));
			if (Utils.checkSelectOne(record)) {
				popConfirm('确认删除公告类型[' + record[0].typeName + ']？', 'annoType-body');
				$("#popConfirmYes").unbind('click');
				$("#popConfirmYes").click(function() {
					annoTypeDeleteById(annoTypeDeleteUrl, record[0]);
				});
			}
		}
	} ];
	var initdatagrid = {
		url : annoTypeUrl,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'typeId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'typeName',
		sortOrder : 'asc',
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,
		striped : true,
		nowrap : false,
		onDblClickRow : function(rowIndex, rowData) {
			annoTypeSaveById(annoTypeSaveUrl, rowData);
		},
		columns : [ [ {
			field : 'typeId',
			title : '类型Id',
			checkbox : true
		}, {
			field : 'typeName',
			title : '类型名称',
			width : 200,
			sortable : true
		}, {
			field : 'typeRmk',
			title : '类型描述',
			width : 250,
			sortable : true
		}] ]
	};
	/*var newBarsAnnoType = getAccessButton(initToolbarAnnoType, buttons, type);
	if (newBarsAnnoType.length > 0) {
		initdatagrid['toolbar'] = newBarsAnnoType;
	}*/
	initdatagrid['toolbar'] = initToolbarAnnoType;
	$('#annoType-data-list').datagrid(initdatagrid);
	$('#typeName').validatebox({
		required : true,
		missingMessage : '请输入公告类型名称.'
	});
}

function annoTypeDeleteById(url, data) {
	$.post(url, data, function(result) {
		checkSession(result);
		r = eval(result);
		if (r.success) {
			index = $('#annoType-data-list').datagrid('getRowIndex',
					r.data.typeId);
			$('#annoType-data-list').datagrid('deleteRow', index);
		}
		rollDown("imf_roll", r.message);
	}, "json");
}

function annoTypeSave(url){
	$('#annoTypeEditForm').form('submit',{
		url : url,
		success : function(result) {
			try {
				var r = $.parseJSON(result);
				if (r.success) {
					popInfo("annoTypeInfo", r.message);
					if (r.type == 'insert') {
						$('#annoType-data-list').datagrid('insertRow', {
							index : 0,
							row : r.data
						});
					} else if (r.type == 'update') {
						$('#annoType-data-list').datagrid('updateRow', {
							index : $('#annoType-data-list').datagrid('getRowIndex',r.data.typeId),
							row : r.data
						});
					}
					popInfo('annoTypeInfo', r.message);
				} else {
					popInfo("annoTypeError", r.message);
				}
			} catch (e) {
				$.messager.alert('提示', result);
			}
		}
	});
}

$(function() {
	$('#annoTypeBtnSearch').click(function() {
		$('#annoType-data-list').datagrid('load',
			serializeObject($('#annoTypeSearchForm')));
		});
	$('#annoTypeBtnClean').click(function() {
		$('#annoType-data-list').datagrid('load', {});
	});
});

function annoTypeSaveById(url, data) {
	$.ajax(url, {
		type : 'get',
		dataType : 'json',
		data : data,
		success : function(result) {
			checkSession(result);
			$('#annoTypeEditForm').form('load', result.data);
			$("#typeId").attr({
				disabled : true
			});
			popWindow('annoType-edit-win', 'annoType-body');
		}
	});
}

$(document).ready(function() {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});
