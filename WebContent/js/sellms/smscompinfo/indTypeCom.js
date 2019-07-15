//数据列表
function indTypeComDataGrid(baseUrl, buttons, type,industryType){
	var indTypeComToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow(industryType+'-edit-win', industryType+'-mainBody');
			$('#'+industryType+'EditForm').form('clear');
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#'+industryType+'-data-list'));
			if (Utils.checkSelectOne(record)){
				smscompinfoUpdateById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#'+industryType+'-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除这条数据吗?',industryType+'-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						
					});
				}
			}
	},'-',{
		text : '批量导入',
		buttonType:'import',
		iconCls : 'icon-import',
		handler : function() {
			popWindow(industryType+'-import-win', industryType+'-mainBody');
			$('#'+industryType+'-import-form').form('clear');
		}
	}];
	var indTypeComDataGrid = {
		url :  baseUrl + '/smscompinfo/indTypeComDataGrid.do?industryType='+industryType,
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
			smscompinfoUpdateById(baseUrl,rowData);
		},
		frozenColumns : [ [ {
			field : 'pkId',
			title : '复选框',
			width : 50,
			checkbox : true
		} ] ],
		columns : [ [ {
			field : 'compName',
			title : '公司名称',
			width : 220
		}, {
			field : 'compLegal',
			title : '公司法人',
			width : 102
		}
		, {
			field : 'compMoney',
			title : '注册资金',
			width : 200,
			hidden:true
		}
		, {
			field : 'compDate',
			title : '成立日期',
			width : 102,
			hidden:true
		}
		, {
			field : 'telNumber',
			title : '联系电话',
			width : 102,
			hidden:true
		}
		, {
			field : 'mobile',
			title : '联系方式',
			width : 102
		}
		, {
			field : 'compAdd',
			title : '公司地址',
			width : 300,
			formatter : function(value, row, index) {
				if(value.length > 21){
					return '<span title="'+value+'">' + value.substr(0,21) + '......</span>';
				}else{
					return value;
				}
			}
		}
		, {
			field : 'compWeb',
			title : '公司官网',
			width : 102,
			hidden:true
		}
		, {
			field : 'busiScope',
			title : '业务范围',
			width : 350,
			formatter : function(value, row, index) {
				if(value.length > 26){
					return '<span title="'+value+'">' + value.substr(0,26) + '......</span>';
				}else{
					return value;
				}
			}
		}
		 ] ]
	};
	var newBars = getAccessButton(indTypeComToolbar,buttons,type);
	if(newBars.length>0){
		indTypeComDataGrid['toolbar'] = indTypeComToolbar;
	}
	$('#'+industryType+'-data-list').datagrid(indTypeComDataGrid);
	
	$('#'+industryType+'BtnSearch').click(function(){//执行
		$('#'+industryType+'-data-list').datagrid('load', serializeObject($('#'+industryType+'SearchForm')));
	});
	
	$('#'+industryType+'BtnClear').click(function(){// 重置
		$('#'+industryType+'SearchForm').form('clear');
		$('#'+industryType+'-data-list').datagrid('load', {});
	});
}
// 获取选中记录,弹出修改窗口
function industryTypeUpdateById(baseUrl,row){
	$.ajax(baseUrl + '/smscompinfo/getSmsCompInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		checkSession(result);
				$('#'+industryType+'EditForm').form('load',result.data);
				popWindow(industryType+'-edit-win', industryType+'-mainBody');
		 	}
	});	
}

// 删除选中记录
function industryTypeDeleteById(row){
	$.ajax(baseUrl + '/smscompinfo/deleteSmsCompInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	data:data,
		 	success:function(result){
		 		r = eval(result);
		 		if(r.success){
					//刷新界面
		 			$('#'+industryType+'-data-list').datagrid('load', {});
				}else{
					rollDown("imf_roll",r.message);
				}
		 	}
	});
}
// 新增\修改 提交
function industryTypeSave(url){
	if($('#'+industryType+'EditForm').form('validate')){
		$('#'+industryType+'EditForm').form('submit', {
			url : baseUrl + '/smscompinfo/saveSmsCompInfo.do',
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						$('#'+industryType+'-data-list').datagrid('load', {});
						popInfo(industryType+'-edit-info', r.message);
						popClosed(industryType+'-edit-win');
					}else{
						popInfo(industryType+'-edit-error', r.message);
					}
				} catch (e) {
					popInfo(industryType+'-edit-error', result);
				}
			}
		});
	}
}

//批量导入
/*function importsmscompinfo(baseUrl){
	reg = /(\.xls|\.XLS|\.xlsx|\.XLSX|\.csv)$/; //匹配excel文件格式
	if($('#'+industryType+'-file').val() == ''){
		popInfo(industryType+'-import-error', '请选择文件！');
		return false;
	}
	if(!reg.test($('#'+industryType+'-file').val())){
		popInfo(industryType+'-import-error', '请选择Excel格式的文件类型!');
		return false;
	}
	//$("#smscompinfo-mainBody").mask("文件上传中，请稍后...");
	$('#'+industryType+'-import-form').form('submit',{
		url : baseUrl + '/smscompinfo/uploadCompExcel.do',
		success : function(result){
			try{
				var r = $.parseJSON(result);
				$("#smscompinfo-mainBody").unmask();
				if(r.success){
					$('#'+industryType+'-data-list').datagrid('reload');
				}else{
					popInfo(industryType+'-import-error', r.message);
					return false;
				}
				popInfo(industryType+'-import-info', r.message);
			}catch(e){
				$("#smscompinfo-mainBody").unmask();
				popInfo(industryType+'-import-error', result);
			}
		}
	});
}*/

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus");
	}).blur(function () {
		$(this).removeClass("input_focus");
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});
