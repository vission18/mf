//数据列表
function xfHistoryDataGrid(baseUrl, buttons, type){
	var xfHistory_url = baseUrl + '/xfRecordController/dataGridHistory.do';
	var xfHistory_delete_url = baseUrl + '/xfRecordController/deleteHistory.do';
	var getAllTypeToHistory=baseUrl + '/xfRecordController/getAllType.do';
	$('#historyType').combobox( {
		url : getAllTypeToHistory,
		valueField : "typeId",
		textField : "typeName",
	});
	var initToolbar = [ {
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var History = Utils.getCheckedRows($('#xfHistory-data-list'));
			if (Utils.checkSelectOne(History)){
					popConfirm('确认删除消费记录：['+History[0].xfGoods+']？','xfHistory-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						xfHistoryDeleteById(xfHistory_delete_url,History[0]);
					});
				}
			}
	},'-',{
		text : '导出Excel',
		buttonType : 'expExcel',
		iconCls : 'icon-redo',
		handler : function(){
			popConfirm('确认导出Excel文件？','xfHistory-mainBody');
			$("#popConfirmYes").unbind('click');
			$("#popConfirmYes").click(function (){
				var url= baseUrl+'/xfRecordController/expExcel.do';	
				window.location.href=url;
			});
		}
	}];
	var initDataGrid = {
		url : xfHistory_url,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'xfId',
		pageSize : 10,
		sortName : 'recordDate',
		sortOrder : 'desc',
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,// 服务器端排序
		striped : true,// 奇偶行不同颜色
		nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
		frozenColumns : [ [ {
			field : 'xfId',
			title : '类别编号',
			width : 50,
			checkbox : true
		}, {
			field : 'xfType',
			title : '消费类别',
			width : 80,
			sortable : true
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
			width : 200,
		}, {
			field : 'xfDetail',
			title : '消费详情',
			width : 265
		} ] ]
	};
	var newBars = getAccessButton(initToolbar,buttons,type);
	if(newBars.length>0){
		initDataGrid['toolbar'] = newBars;
	}
	$('#xfHistory-data-list').datagrid(initDataGrid);
	$('#xfHistoryBtnSearch').click(function(){// 查询
		$('#xfHistory-data-list').datagrid('load', serializeObject($('#xfHistorySearchForm')));
	});
	$('#xfHistoryBtnClean').click(function(){// 全部
		$('#xfHistory-data-list').datagrid('load', {});
		$('#xfHistorySearchForm').form('clear');
	});
}

// 删除选中记录
function xfHistoryDeleteById(url,data){
	$.post(url,data,
			function(result){
				checkSession(result);
				r = eval(result);
				if(r.success){
					index = $('#xfHistory-data-list').datagrid('getRowIndex', r.data.xfId);
					$('#xfHistory-data-list').datagrid('deleteRow', index);
				}
				rollDown("imf_roll",r.message);
			}, 'json'
		);
}

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus");
	}).blur(function () {
		$(this).removeClass("input_focus");
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});
