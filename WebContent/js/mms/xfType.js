//数据列表
function xfTypeDataGrid(baseUrl, buttons, type){
	var xfType_url = baseUrl + '/xfTypeController/dataGrid.do';
	var xfType_delete_url = baseUrl + '/xfTypeController/delete.do';
	var initToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('xfType-edit-win', 'xfType-mainBody');
			$('#xfTypeEditForm').form('clear');
			$('#xfTypeEditForm').form('load',{xfTypeType:2});
			$(':radio[name="typeStatus"]').eq(0).attr("checked",true); // 设置第一个radio为选中
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#xfType-data-list'));
			if (Utils.checkSelectOne(record)){
					xfTypeSaveById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#xfType-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('主人，您确认删除我['+record[0].typeName+']吗？','xfType-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						xfTypeDeleteById(xfType_delete_url,record[0]);
					});
				}
			}
	},'-',{
		text : '导入Excel',
		buttonType : 'impExcel',
		iconCls : 'icon-undo',
		handler : function(){
			popWindow('xfType-import-win', 'xfType-mainBody');
			$('#xfType-import-form').form('clear');
		}
	},'-',{
		text : '导出Excel',
		buttonType : 'expExcel',
		iconCls : 'icon-redo',
		handler : function(){
			popConfirm('主人，您确认要导出Excel文件吗？','xfType-mainBody');
			$("#popConfirmYes").unbind('click');
			$("#popConfirmYes").click(function (){
				var url= baseUrl+'/xfTypeController/expExcel.do';	
				window.location.href=url;
			});
		}
	}];
	var initDataGrid = {
		url : xfType_url,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'typeId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'typeId',
		sortOrder : 'asc',
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,// 服务器端排序
		striped : true,// 奇偶行不同颜色
		nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
		onDblClickRow:function(rowIndex, rowData) {
				xfTypeSaveById(baseUrl,rowData);// 双击进入修改
		},
		frozenColumns : [ [ {
			field : 'typeId',
			title : '类别编号',
			width : 50,
			checkbox : true
		}, {
			field : 'typeName',
			title : '类别名称',
			width : 120,
			sortable : true
		} ] ],
		columns : [ [ {
			field : 'typeStatus',
			title : '类别状态',
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
			field : 'remark',
			title : '备注',
			width : 300
		} ] ]
	};
	var newBars = getAccessButton(initToolbar,buttons,type);
	if(newBars.length>0){
		initDataGrid['toolbar'] = newBars;
	}
	$('#xfType-data-list').datagrid(initDataGrid);
	
	$('#ModelDownload-btn').linkbutton({
		iconCls: 'icon-save',
		text: '下载'
	});
	
	$('#xfTypeBtnSearch').click(function(){// 查询
		$('#xfType-data-list').datagrid('load', serializeObject($('#xfTypeSearchForm')));
	});
	$('#xfTypeBtnClean').click(function(){// 全部
		$('#xfType-data-list').datagrid('load', {});
	});
}

// 获取选中记录,弹出修改窗口
function xfTypeSaveById(baseUrl,data){
	$.ajax(baseUrl + '/xfTypeController/getById.do', {
		type:'post',
		 	dataType:'json',
		 	data:data,
		 	success:function(result){
		 		checkSession(result);
				$('#xfTypeEditForm').form('load',result.data);
				if(result.data.typeStatus){
					$(':radio[name="typeStatus"]').eq(0).attr("checked",true); // 设置第一个radio为选中
				}else{
					$(':radio[name="typeStatus"]').eq(1).attr("checked",true); 
				}
				popWindow('xfType-edit-win', 'xfType-mainBody');
		 	}
	});	
}

// 删除选中记录
function xfTypeDeleteById(url,data){
	$.post(url,data,
			function(result){
				checkSession(result);
				r = eval(result);
				if(r.success){
					//刷新界面
					//$('#xfType-data-list').datagrid('load', {});
					index = $('#xfType-data-list').datagrid('getRowIndex', r.data.typeId);
					$('#xfType-data-list').datagrid('deleteRow', index);
				}
				rollDown("imf_roll",r.message);
			}, 'json'
		);
}
// 新增\修改 提交
function xfTypeSave(url){
	if($('#xfTypeEditForm').form('validate')){
		$('#xfTypeEditForm').form('submit', {
			url : url,
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						//$('#xfType-data-list').datagrid('load', {});
						if(r.type === 'insert'){
							$('#xfType-data-list').datagrid('insertRow', {
								index : 0,
								row : r.data
							});
						}else if(r.type === 'update'){
							var record = Utils.getCheckedRows($('#xfType-data-list'));
							var data ={};
							var idKey = 'typeId'; // 主键名称
		 					data[idKey] = (record[0][idKey]);
							$('#xfType-data-list').datagrid('updateRow', {
								index : $('#xfType-data-list').datagrid('getRowIndex', r.data.typeId),
								row : r.data
							});
						}
						popInfo('xfType-edit-info', r.message);
						popClosed('xfType-edit-win');
					}else{
						popInfo('xfType-edit-error', r.message);
					}
				} catch (e) {
					popInfo('xfType-edit-error', result);
				}
			}
		});
	}
}

//批量导入类别
function importxfType(BaseUrl){
	reg = /(\.xls|\.XLS|\.xlsx|\.XLSX|\.csv)$/; //匹配excel文件格式
	if($('#xfType-file').val() == ''){
		popInfo('xfType-import-error', '请选择文件！');
		return false;
	}
	if(!reg.test($('#xfType-file').val())){
		popInfo('xfType-import-error', '请选择Excel格式的文件类型!');
		return false;
	}
	$('#xfType-import-form').form('submit',{
		url : BaseUrl + '/xfTypeController/uploadExcel.do',
		success : function(result){
			try{
				var r = $.parseJSON(result);
				if(r.success){
					$('#xfType-data-list').datagrid('reload');
				}else{
					popInfo('xfType-import-error', r.message);
					return false;
				}
				popInfo('xfType-import-info', r.message);
			}catch(e){
				popInfo('xfType-import-error', result);
			}
		}
	});
}

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus");
	}).blur(function () {
		$(this).removeClass("input_focus");
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});
