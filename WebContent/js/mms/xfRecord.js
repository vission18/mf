//数据列表
function xfRecordDataGrid(baseUrl, buttons, type){
	var xfRecord_url = baseUrl + '/xfRecordController/dataGrid.do';
	var xfRecord_delete_url = baseUrl + '/xfRecordController/delete.do';
	var getAllType=baseUrl + '/xfRecordController/getAllType.do';
	$('#typeList').combobox( {
		url : getAllType,
		valueField : "typeId",
		textField : "typeName",
	});
	$('#xfType').combobox( {
		url : getAllType,
		valueField : "typeId",
		textField : "typeName",
	});
	var initToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('xfRecord-edit-win', 'xfRecord-mainBody');
			$('#xfRecordEditForm').form('clear');
			$('#xfRecordEditForm').form('load',{xfRecordType:2});
			$(':radio[name="typeStatus"]').eq(0).attr("checked",true); // 设置第一个radio为选中
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#xfRecord-data-list'));
			if (Utils.checkSelectOne(record)){
					xfRecordSaveById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#xfRecord-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除消费记录：['+record[0].xfGoods+']？','xfRecord-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						xfRecordDeleteById(xfRecord_delete_url,record[0]);
					});
				}
			}
	},'-',{
		text : '导出Excel',
		buttonType : 'expExcel',
		iconCls : 'icon-redo',
		handler : function(){
			popConfirm('确认导出Excel文件？','xfRecord-mainBody');
			$("#popConfirmYes").unbind('click');
			$("#popConfirmYes").click(function (){
				var url= baseUrl+'/xfRecordController/expExcel.do';	
				window.location.href=url;
			});
		}
	}];
	var initDataGrid = {
		url : xfRecord_url,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'xfId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'recordDate',
		sortOrder : 'desc',
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,// 服务器端排序
		striped : true,// 奇偶行不同颜色
		nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
		onDblClickRow:function(rowIndex, rowData) {
				xfRecordSaveById(baseUrl,rowData);// 双击进入修改
		},
		frozenColumns : [ [ {
			field : 'xfId',
			title : '类别编号',
			width : 50,
			checkbox : true
		}, {
			field : 'xfType',
			title : '消费类别',
			width : 80
		} ] ],
		columns : [ [ {
			field : 'xfGoods',
			title : '消费物品',
			width : 120,
		}, {
			field : 'xfPrice',
			title : '消费价格',
			width : 120,
		}, {
			field : 'xfDate',
			title : '消费日期',
			width : 120,
		}, {
			field : 'recordDate',
			title : '记录日期',
			width : 180,
		}, {
			field : 'recordUser',
			title : '记录人',
			width : 120,
		}, {
			field : 'xfDetail',
			title : '消费详情',
			width : 345
		} ] ]
	};
	var newBars = getAccessButton(initToolbar,buttons,type);
	if(newBars.length>0){
		initDataGrid['toolbar'] = newBars;
	}
	$('#xfRecord-data-list').datagrid(initDataGrid);
	$('#xfRecordBtnSearch').click(function(){// 查询
		$('#xfRecord-data-list').datagrid('load', serializeObject($('#xfRecordSearchForm')));
	});
	$('#xfRecordBtnClean').click(function(){// 全部
		$('#xfRecord-data-list').datagrid('load', {});
		$('#xfRecordSearchForm').form('clear');
	});
}

// 获取选中记录,弹出修改窗口
function xfRecordSaveById(baseUrl,data){
	$.ajax(baseUrl + '/xfRecordController/getById.do', {
		type:'post',
		 	dataType:'json',
		 	data:data,
		 	success:function(result){
		 		checkSession(result);
				$('#xfRecordEditForm').form('load',result.data);
				popWindow('xfRecord-edit-win', 'xfRecord-mainBody');
		 	}
	});	
}

// 删除选中记录
function xfRecordDeleteById(url,data){
	$.post(url,data,
			function(result){
				checkSession(result);
				r = eval(result);
				if(r.success){
					//刷新界面
					$('#xfRecord-data-list').datagrid('load', {});
				}
				rollDown("imf_roll",r.message);
			}, 'json'
		);
}
// 新增\修改 提交
function xfRecordSave(url){
	if($('#xfRecordEditForm').form('validate')){
		$('#xfRecordEditForm').form('submit', {
			url : url,
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						$('#xfRecord-data-list').datagrid('load', {});
						popInfo('xfRecord-edit-info', r.message);
						popClosed('xfRecord-edit-win');
					}else{
						popInfo('xfRecord-edit-error', r.message);
					}
				} catch (e) {
					popInfo('xfRecord-edit-error', result);
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
