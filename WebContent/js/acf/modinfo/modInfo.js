function saveMod(baseUrl){
	if($('#modForm').form('validate')){
		$('#modForm').ajaxSubmit ({
			url : baseUrl + '/acfModInfo/saveModInfo.do',
			success : function(result){
				checkSession(result);
				var r = $.parseJSON(result);
				if(r.success){
					var row = r.data;
					popInfo('modInfo', '保存成功');
					$('#modId').val(row.modId);
					$('#colsInfo-data-list').datagrid({url:baseUrl + '/acfModInfo/colDataGridByModId.do?modId='+$('#modId').val()});
					showIndTab('cols');
				}else{
					popInfo('modError', r.message);
				}
			}
		});
		}
}

//tab页面切换
function showIndTab(showType) {
	if (showType == 'mod') {
		$("#modLi").find("a").addClass("cur");
		$("#colsLi").find("a").removeClass("cur");

		$('#mod-tabcon1').show();
		$('#cols-tabcon1').hide();

	} else if (showType == 'cols') {
		if($('#modId').val() == '' || $('#modId').val() == 'undefined' ){
			$.messager.alert('提示','请先保存模块表信息！');
			return ;
		}
		$("#colsLi").find("a").addClass("cur");
		$("#modLi").find("a").removeClass("cur");

		$('#cols-tabcon1').show();
		$('#mod-tabcon1').hide();

	} 
}

function colsDataGrid(baseUrl, buttons, type){
	var colsInfo_url = baseUrl + '/acfModInfo/colDataGridByModId.do?modId='+$('#modId').val();
	var initColInfoToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('colsInfo-edit-win', 'colsInfo-mainBody');
			$('#colsInfoEditForm').form('clear');
			$('#colType').combobox('setValue','VARCHAR');
			$(':radio[name="isPk"]').eq(1).attr("checked",true); // 设置第一个radio为选中
			$(':radio[name="isNull"]').eq(0).attr("checked",true); // 设置第一个radio为选中
		}
	},'-',{
		text : '修改',
		buttonType:'update',
		iconCls : 'icon-edit',
		handler : function() {
		/*	var record = Utils.getCheckedRows($('#colsInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					colsInfoSaveById(baseUrl,record[0]);
			}*/
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			/*var record = Utils.getCheckedRows($('#colsInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除消费记录：['+record[0].xfGoods+']？','colsInfo-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						colsInfoDeleteById(colsInfo_delete_url,record[0]);
					});
				}*/
			}
	},'-',{
		text : '生成代码',
		buttonType:'autocreate',
		iconCls : 'icon-reload',
		handler : function() {
			var colRows = $('#colsInfo-data-list').datagrid('getRows');
			if(colRows.length > 0){
				 $("#colList").val(JSON.stringify(colRows)); 
				 autoCreateCode(baseUrl);
			}
		}
	}];
	var initColInfoDataGrid = {
		url : colsInfo_url,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'colId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,// 服务器端排序
		striped : true,// 奇偶行不同颜色
		nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
		onDblClickRow:function(rowIndex, rowData) {
				colsInfoSaveById(baseUrl,rowData);// 双击进入修改
		},
		frozenColumns : [ [ {
			field : 'colEngName',
			title : '字段英文名',
			width : 50,
			checkbox : true
		}, {
			field : 'colChaName',
			title : '字段中文名',
			width : 80
		} ] ],
		columns : [ [ {
			field : 'colType',
			title : '字段类型',
			width : 120,
		}, {
			field : 'defaultVal',
			title : '默认值',
			width : 120,
		}, {
			field : 'isPk',
			title : '是否主键',
			width : 120,
		}, {
			field : 'isNull',
			title : '是否为空',
			width : 180,
		}, {
			field : 'colDesc',
			title : '字段描述',
			width : 345
		} ] ]
	};
	initColInfoDataGrid['toolbar'] = initColInfoToolbar;
	var newBars = getAccessButton(initColInfoToolbar,buttons,type);
	if(newBars.length>0){
		//initColInfoDataGrid['toolbar'] = newBars;
	}
	$('#colsInfo-data-list').datagrid(initColInfoDataGrid);
	
}
//保存列
function saveColInfo(baseUrl){
	if ($('#colsInfoEditForm').form('validate')) {
		$('#colsInfoEditForm').ajaxSubmit({
			url : baseUrl + '/acfModInfo/saveColInfo.do?modId='+$('#modId').val(),
			success : function(result) {
				var r = $.parseJSON(result);
				if (r.success) {
					popInfo("colsInfo-edit-info", '保存成功');
					// 刷新界面
					$('#colsInfo-data-list').datagrid({url:baseUrl + '/acfModInfo/colDataGridByModId.do?modId='+$('#modId').val()});
					//$('#colsInfo-data-list').datagrid('reload');
				} else {
					popInfo("colsInfo-edit-error", r.message);
				}
			}
		});
	}
}

function autoCreateCode(baseUrl){
	$("#table-mainBody").mask('代码生成中，请稍等......')
	$('#modForm').ajaxSubmit({
		url : baseUrl + '/acfModInfo/autoCreateCode.do',
		success : function(result) {
			try {
				var r = $.parseJSON(result);
				if (r.success) {
					$.messager.alert('提示','代码生成成功，请后台刷新查看！');
				} else {
					$.messager.alert('错误',r.message);
					return false;
				}
			} catch (e) {
				$.messager.alert('错误','代码生成异常！');
			}
			$("#table-mainBody").unmask();
		}
	});
}