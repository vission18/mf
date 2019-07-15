//数据列表
function smsFileInfoDataGrid(baseUrl, buttons, type){
	var smsFileInfo_url = baseUrl + '/smsFileInfo/smsFileInfoDataGrid.do';
	var initSmsFileInfoToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('smsFileInfo-edit-win', 'smsFileInfo-mainBody');
			$('#smsFileInfoEditForm').form('clear');
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#smsFileInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					smsFileInfoUpdateById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#smsFileInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除这条数据吗?','smsFileInfo-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						smsFileInfoDeleteById(record[0]);
					});
				}
			}
	}];
	var initSmsFileInfoDataGrid = {
		url : smsFileInfo_url,
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
			smsFileInfoUpdateById(baseUrl,rowData);
		},
		frozenColumns : [ [ {
			field : 'pkId',
			title : '复选框',
			width : 50,
			checkbox : true
		}
		, {
			field : 'fileName',
			title : '文件名称',
			width : 204,
		}
		, {
			field : 'filePath',
			title : '文件路径',
			width : 204,
		}
		, {
			field : 'phoneKey',
			title : '电话抬头',
			width : 204,
		}
		, {
			field : 'createUser',
			title : '创建人',
			width : 204,
		}
		, {
			field : 'createTime',
			title : '创建时间',
			width : 204,
		}
		 ] ]
	};
	var newBars = getAccessButton(initSmsFileInfoToolbar,buttons,type);
	if(newBars.length>0){
		initSmsFileInfoDataGrid['toolbar'] = newBars;
	}
	$('#smsFileInfo-data-list').datagrid(initSmsFileInfoDataGrid);
	
	$('#smsFileInfoBtnSearch').click(function(){// 查询
		$('#smsFileInfo-data-list').datagrid('load', serializeObject($('#smsFileInfoSearchForm')));
	});
	$('#smsFileInfoBtnClean').click(function(){// 全部
		$('#smsFileInfo-data-list').datagrid('load', {});
		$('#smsFileInfoSearchForm').form('clear');
	});
}

// 获取选中记录,弹出修改窗口
function smsFileInfoUpdateById(baseUrl,row){
	$.ajax(baseUrl + '/smsFileInfo/getSmsFileInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		checkSession(result);
				$('#smsFileInfoEditForm').form('load',result.data);
				popWindow('smsFileInfo-edit-win', 'smsFileInfo-mainBody');
		 	}
	});	
}

// 删除选中记录
function smsFileInfoDeleteById(row){
	$.ajax(baseUrl + '/smsFileInfo/deleteSmsFileInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		try{
		 			r = eval(result);
			 		if(r.success){
						//刷新界面
						$('#smsFileInfo-data-list').datagrid('load', {});
					}
			 		rollDown("imf_roll",r.message);
		 		}catch(e){
		 			rollDown("imf_roll",r.message);
		 		}
		 	}
	});
}
// 新增\修改 提交
function smsFileInfoSave(url){
	if($('#smsFileInfoEditForm').form('validate')){
		$('#smsFileInfoEditForm').form('submit', {
			url : baseUrl + '/smsFileInfo/saveSmsFileInfo.do',
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						$('#smsFileInfo-data-list').datagrid('load', {});
						popInfo('smsFileInfo-edit-info', r.message);
						popClosed('smsFileInfo-edit-win');
					}else{
						popInfo('smsFileInfo-edit-error', r.message);
					}
				} catch (e) {
					popInfo('smsFileInfo-edit-error', result);
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
