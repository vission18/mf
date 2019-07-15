//数据列表
function smsSignInfoDataGrid(baseUrl, buttons, type){
	var smsSignInfo_url = baseUrl + '/smsSignInfo/smsSignInfoDataGrid.do';
	var initSmsSignInfoToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('smsSignInfo-edit-win', 'smsSignInfo-mainBody');
			$('#smsSignInfoEditForm').form('clear');
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#smsSignInfo-data-list'));
			if (Utils.checkSelectOne(record)){
				smsSignInfoUpdateById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#smsSignInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除这条数据吗?','smsSignInfo-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						smsSignInfoDeleteById(record[0]);
					});
				}
			}
	}];
	var initSmsSignInfoDataGrid = {
		url : smsSignInfo_url,
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
			smsSignInfoUpdateById(baseUrl,rowData);
		},
		frozenColumns : [ [ {
			field : 'pkId',
			title : '复选框',
			width : 50,
			checkbox : true
		} ] ],
		columns : [ [ {
			field : 'signName',
			title : '签名',
			width : 400
		}
		, {
			field : 'signDesc',
			title : '描述',
			width : 400,
		}
		 ] ]
	};
	var newBars = getAccessButton(initSmsSignInfoToolbar,buttons,type);
	if(newBars.length>0){
		initSmsSignInfoDataGrid['toolbar'] = newBars;
	}
	$('#smsSignInfo-data-list').datagrid(initSmsSignInfoDataGrid);
	
	$('#smsSignInfoBtnSearch').click(function(){// 查询
		$('#smsSignInfo-data-list').datagrid('load', serializeObject($('#smsSignInfoSearchForm')));
	});
	$('#smsSignInfoBtnClean').click(function(){// 全部
		$('#smsSignInfo-data-list').datagrid('load', {});
		$('#smsSignInfoSearchForm').form('clear');
	});
}

// 获取选中记录,弹出修改窗口
function smsSignInfoUpdateById(baseUrl,row){
	$.ajax(baseUrl + '/smsSignInfo/getSmsSignInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		checkSession(result);
				$('#smsSignInfoEditForm').form('load',result.data);
				popWindow('smsSignInfo-edit-win', 'smsSignInfo-mainBody');
		 	}
	});	
}

// 删除选中记录
function smsSignInfoDeleteById(row){
	console.info(row);
	$.ajax(baseUrl + '/smsSignInfo/deleteSmsSignInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		try{
		 			r = eval(result);
			 		if(r.success){
						//刷新界面
						$('#smsSignInfo-data-list').datagrid('load', {});
					}
			 		rollDown("imf_roll",r.message);
		 		}catch(e){
		 			rollDown("imf_roll",r.message);
		 		}
		 	}
	});
}
// 新增\修改 提交
function smsSignInfoSave(url){
	if($('#smsSignInfoEditForm').form('validate')){
		$('#smsSignInfoEditForm').form('submit', {
			url : baseUrl + '/smsSignInfo/saveSmsSignInfo.do',
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						$('#smsSignInfo-data-list').datagrid('load', {});
						popInfo('smsSignInfo-edit-info', r.message);
						popClosed('smsSignInfo-edit-win');
					}else{
						popInfo('smsSignInfo-edit-error', r.message);
					}
				} catch (e) {
					popInfo('smsSignInfo-edit-error', result);
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
