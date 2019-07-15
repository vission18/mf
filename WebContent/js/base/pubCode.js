//数据列表
function pubCodeDataGrid(baseUrl,buttons,type) {
	url = baseUrl + '/pubCode/dataGrid.do';
	saveUrl = baseUrl + '/pubCode/getById.do';
	deleteUrl = baseUrl + '/pubCode/delete.do';
	var initToolbarPubCode=[ {
		text : '新增',
		iconCls : 'icon-add',
		buttonType:'add',
		handler : function() {
			$("#codeName").attr({ disabled: false });//去除disabled
			popWindow('pubcode-edit-win','pub-code-body');
			$("#pubCodeEditForm").form('clear');
		}
	},
	{
		text : '修改',
		iconCls : 'icon-edit',
		buttonType:'edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#pubcode-data-list'));
			if (Utils.checkSelectOne(record)) {
				var data = {};
				var idKey = 'codeId'; // 主键名称
				data[idKey] = (record[0][idKey]);
				pubCodeSaveById(saveUrl, data);
			}
		}
	}, {
		text : '删除',
		iconCls : 'icon-remove',
		buttonType:'drop',
		handler : function() {
			var record = Utils.getCheckedRows($('#pubcode-data-list'));
			if (Utils.checkSelectOne(record)){
				popConfirm('确认删除代码['+record[0].codeName+']？','pub-code-body');
				$("#popConfirmYes").click(function (){
					pubCodeDeleteById(deleteUrl,record[0]);
				});
			}
		}
	} ];
	var initdatagrid={
			url : url,
			fit : true, 
			fitColumns : false,
			border : true,
			pagination : true,
			idField : 'codeId',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'codeName',
			sortOrder : 'asc',
			singleSelect : true,
			checkOnSelect : true,
			selectOnCheck : false,
			remoteSort : false,// 服务器端排序
			striped : true,// 奇偶行不同颜色
			nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
			onDblClickRow : function(rowIndex, rowData) {
				pubCodeSaveById(saveUrl, rowData);// 双击进入修改
			},
			columns : [ [ {
				field : 'codeId',
				title : '代码ID',
				checkbox : true
			}, {
				field : 'codeName',
				title : '代码名称',
				width : 200,
				sortable : true
			}, {
				field : 'codeValue',
				title : '代码值',
				width : 300
			}, {
				field : 'codeRmk',
				title : '代码描述',
				width : 300
			} ] ]
		};
	var newBarsPubCode=getAccessButton(initToolbarPubCode,buttons,type);
	if(newBarsPubCode.length>0){
		initdatagrid['toolbar']=newBarsPubCode;
		}
	$('#pubcode-data-list').datagrid(initdatagrid);
	$('#codeName').validatebox({
		required : true ,
		missingMessage : '请输入代码名称.'
	});
	$('#codeValue').validatebox({
		required : true ,
		missingMessage : '请输入代码值.'
	});
}
function pubCodeSaveById(url, data) {
	$.ajax(url, {
		type : 'get',
		dataType : 'json',
		data : data,
		success : function(result) {
			checkSession(result);
			$('#pubCodeEditForm').form('load', result.data);
			$("#codeName").attr({ disabled: true });//设置disabled,不允许修改
			popWindow('pubcode-edit-win','pub-code-body');
		},
		error : function(response, textStatus, errorThrown) {

		},
		complete : function() {

		}
	});
}

function pubCodeDeleteById(url, data) {
	$.post(url, data, function(result) {
		r = eval(result);
		if (r.success) {
			checkSession(result);
			index = $('#pubcode-data-list').datagrid('getRowIndex',
					r.data.codeId);
			$('#pubcode-data-list').datagrid('deleteRow', index);
		}
		rollDown("imf_roll",r.message);
	}, "json");
}

function pubCodeSave(url) {
	$('#pubCodeEditForm').form('submit', {
		url : url,
		success : function(result) {
			try {
				var r = $.parseJSON(result);
				if (r.success) {
					popInfo("pubCodeInfo", r.message);
					if (r.type == 'insert') {
						$('#pubcode-data-list').datagrid('insertRow', {
							index : 0,
							row : r.data
						});
					} else if (r.type == 'update') {
						var record = Utils.getCheckedRows($('#pubcode-data-list'));
						var data ={};
						var idKey = 'codeId'; //主键名称
	 					data[idKey] = (record[0][idKey]);
	 					r.data.codeName = data.codeName;//codeName不允许修改的列
						$('#pubcode-data-list').datagrid('updateRow', {
								index : $('#pubcode-data-list').datagrid('getRowIndex',r.data.codeId),
								row : r.data
							});
					}
				}else {
					popInfo("pubCodeError", data.message);
				}
				} catch (e) {
				$.messager.alert('提示', result);
			}
		}
	});
}

$(function() {
	$('#pubcodebtnSearch').click(function() {
		$('#pubcode-data-list').datagrid('load', serializeObject($('#pubCodeSearchForm')));
	});
	$('#pubCodeBtnClean').click(function() {
		$('#pubcode-data-list').datagrid('load', {});
	});
	$('#pubCodeBtnClose').click(function() {
		$('#pubcode-edit-win').dialog('close');
	});
});

$(document).ready(function () {
	$(".pop_con input,table input").focus(function () {
		$(this).css({
			"border":"1px solid #087de4",
			"color":"#087de4",
			"box-shadow":"1px 1px 2px #56bff3"
			});
	}).blur(function () {
		$(this).css({
			"border":"1px solid #e5e5e5",
			"color":"#555",
			"box-shadow":"none"
			});
	})
})

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
})