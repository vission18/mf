//数据列表
function custInfoDataGrid(baseUrl, buttons, type){
	var custInfo_url = baseUrl + '/smsCustInfo/dataGrid.do';
	var custInfo_delete_url = baseUrl + '/smsCustInfo/delete.do';
	var initCustInfoToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('custInfo-edit-win', 'custInfo-mainBody');
			$('#custInfoEditForm').form('clear');
			$('#custInfoEditForm').form('load',{custInfoType:2});
			//$(':radio[name="typeStatus"]').eq(0).attr("checked",true); // 设置第一个radio为选中
		}
	},'-',{
		text : '修改',
		buttonType:'update',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#custInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					custInfoSaveById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#custInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除消费记录：['+record[0].xfGoods+']？','custInfo-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						custInfoDeleteById(custInfo_delete_url,record[0]);
					});
				}
			}
	},'-',{
		text : '检查',
		buttonType:'check',
		iconCls : 'icon-reload',
		handler : function() {
			var record = Utils.getCheckedRows($('#custInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					
				}
			}
	},'-',{
		text : '发送短信',
		buttonType:'send',
		iconCls : 'icon-print',
		handler : function() {
			var record = Utils.getCheckedRows($('#custInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					
				}
			}
	},'-',{
		text : '批量导出',
		buttonType : 'expExcel',
		iconCls : 'icon-redo',
		handler : function(){
			popConfirm('确认导出Excel文件？','custInfo-mainBody');
			$("#popConfirmYes").unbind('click');
			$("#popConfirmYes").click(function (){
				var url= baseUrl+'/smsCustInfo/expExcel.do';	
				window.location.href=url;
			});
		}
	}];
	var initCustInfoDataGrid = {
		url : custInfo_url,
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
				custInfoSaveById(baseUrl,rowData);// 双击进入修改
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
	var newBars = getAccessButton(initCustInfoToolbar,buttons,type);
	if(newBars.length>0){
		initCustInfoDataGrid['toolbar'] = newBars;
	}
	$('#custInfo-data-list').datagrid(initCustInfoDataGrid);
	
	$('#custInfoBtnSearch').click(function(){// 查询
		$('#custInfo-data-list').datagrid('load', serializeObject($('#custInfoSearchForm')));
	});
	$('#custInfoBtnClean').click(function(){// 全部
		$('#custInfo-data-list').datagrid('load', {});
		$('#custInfoSearchForm').form('clear');
	});
}

// 获取选中记录,弹出修改窗口
function custInfoSaveById(baseUrl,data){
	$.ajax(baseUrl + '/smsCustInfo/getById.do', {
		type:'post',
		 	dataType:'json',
		 	data:data,
		 	success:function(result){
		 		checkSession(result);
				$('#custInfoEditForm').form('load',result.data);
				popWindow('custInfo-edit-win', 'custInfo-mainBody');
		 	}
	});	
}

// 删除选中记录
function custInfoDeleteById(url,data){
	$.post(url,data,
			function(result){
				checkSession(result);
				r = eval(result);
				if(r.success){
					//刷新界面
					$('#custInfo-data-list').datagrid('load', {});
				}
				rollDown("imf_roll",r.message);
			}, 'json'
		);
}
// 新增\修改 提交
function custInfoSave(url){
	if($('#custInfoEditForm').form('validate')){
		$('#custInfoEditForm').form('submit', {
			url : url,
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						$('#custInfo-data-list').datagrid('load', {});
						popInfo('custInfo-edit-info', r.message);
						popClosed('custInfo-edit-win');
					}else{
						popInfo('custInfo-edit-error', r.message);
					}
				} catch (e) {
					popInfo('custInfo-edit-error', result);
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
