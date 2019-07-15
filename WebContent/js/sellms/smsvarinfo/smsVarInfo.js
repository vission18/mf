//数据列表
function smsVarInfoDataGrid(baseUrl, buttons, type){
	var smsVarInfo_url = baseUrl + '/smsVarInfo/smsVarInfoDataGrid.do';
	var initSmsVarInfoToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('smsVarInfo-edit-win', 'smsVarInfo-mainBody');
			$('#smsVarInfoEditForm').form('clear');
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#smsVarInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					smsVarInfoUpdateById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#smsVarInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除这条数据吗?','smsVarInfo-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						smsVarInfoDeleteById(record[0]);
					});
				}
			}
	}];
	var initSmsVarInfoDataGrid = {
		url : smsVarInfo_url,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'pkId',
		pageSize : 10,
		pageList : [10,20,30,40,50 ],
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,// 服务器端排序
		striped : true,// 奇偶行不同颜色
		nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
		onDblClickRow:function(rowIndex, rowData) {
			smsVarInfoUpdateById(baseUrl,rowData);
		},
		frozenColumns : [ [ {
			field : 'pkId',
			title : '复选框',
			width : 50,
			checkbox : true
		}
		, {
			field : 'varName',
			title : '变量名称',
			width : 170,
		}
		, {
			field : 'varDesc',
			title : '变量说明',
			width : 170,
		}
		, {
			field : 'createUser',
			title : '创建人',
			width : 170,
		}
		, {
			field : 'createTime',
			title : '创建时间',
			width : 170,
		}
		, {
			field : 'lastModUser',
			title : '修改人',
			width : 170,
		}
		, {
			field : 'lastModTime',
			title : '修改时间',
			width : 170,
		}
		 ] ]
	};
	var newBars = getAccessButton(initSmsVarInfoToolbar,buttons,type);
	if(newBars.length>0){
		initSmsVarInfoDataGrid['toolbar'] = newBars;
	}
	$('#smsVarInfo-data-list').datagrid(initSmsVarInfoDataGrid);
	
	$('#smsVarInfoBtnSearch').click(function(){// 查询
		$('#smsVarInfo-data-list').datagrid('load', serializeObject($('#smsVarInfoSearchForm')));
	});
	$('#smsVarInfoBtnClean').click(function(){// 全部
		$('#smsVarInfo-data-list').datagrid('load', {});
		$('#smsVarInfoSearchForm').form('clear');
	});
}

// 获取选中记录,弹出修改窗口
function smsVarInfoUpdateById(baseUrl,row){
	$.ajax(baseUrl + '/smsVarInfo/getSmsVarInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		checkSession(result);
				$('#smsVarInfoEditForm').form('load',result.data);
				popWindow('smsVarInfo-edit-win', 'smsVarInfo-mainBody');
		 	}
	});	
}

// 删除选中记录
function smsVarInfoDeleteById(row){
	$.ajax(baseUrl + '/smsVarInfo/deleteSmsVarInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		try{
		 			r = eval(result);
			 		if(r.success){
						//刷新界面
						$('#smsVarInfo-data-list').datagrid('load', {});
					}
			 		rollDown("imf_roll",r.message);
		 		}catch(e){
		 			rollDown("imf_roll",r.message);
		 		}
		 	}
	});
}
// 新增\修改 提交
function smsVarInfoSave(url){
	if($('#smsVarInfoEditForm').form('validate')){
		$('#smsVarInfoEditForm').form('submit', {
			url : baseUrl + '/smsVarInfo/saveSmsVarInfo.do',
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						$('#smsVarInfo-data-list').datagrid('load', {});
						popInfo('smsVarInfo-edit-info', r.message);
						popClosed('smsVarInfo-edit-win');
					}else{
						popInfo('smsVarInfo-edit-error', r.message);
					}
				} catch (e) {
					popInfo('smsVarInfo-edit-error', result);
				}
			}
		});
	}
}

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus");
	}).blur(function () {
		$(this).removeClass("input_focus");
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});
