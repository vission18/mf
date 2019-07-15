//数据列表
function smscompinfoDataGrid(baseUrl, buttons, type){
	var smscompinfo_url = baseUrl + '/smscompinfo/smscompinfoDataGrid.do';
	var initSmsCompInfoToolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('smscompinfo-edit-win', 'smscompinfo-mainBody');
			$('#smscompinfoEditForm').form('clear');
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#smscompinfo-data-list'));
			if (Utils.checkSelectOne(record)){
				smscompinfoUpdateById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#smscompinfo-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除这条数据吗?','smscompinfo-mainBody');
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
			popWindow('smscompinfo-import-win', 'smscompinfo-mainBody');
			$('#smscompinfo-import-form').form('clear');
		}
	}];
	var initSmsCompInfoDataGrid = {
		url : smscompinfo_url,
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
			field : 'busiType',
			title : '行业分类',
			width : 102,
			hidden:true
		}, {
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
	var newBars = getAccessButton(initSmsCompInfoToolbar,buttons,type);
	if(newBars.length>0){
		initSmsCompInfoDataGrid['toolbar'] = initSmsCompInfoToolbar;
	}
	$('#smscompinfo-data-list').datagrid(initSmsCompInfoDataGrid);
	
	$('#smscompinfoBtnSearch').click(function(){//执行
		if($("#queryProvice").val() == '' && $("#queryCity").val() == '' && $("#queryBuss").val() == '' && 
				$("#queryBussScope").val() == '' ){
			$.messager.alert('提示','请输入执行条件！');
			return;
		}
		$("#smscompinfo-mainBody").mask("采集中，请稍等...");
		var readNum = (Math.round(Math.random()*10)+9)*789;
		setTimeout("queryData()", readNum);
	});
	
	$('#smscompinfoBtnClear').click(function(){// 重置
		$('#smscompinfoSearchForm').form('clear');
		$('#smscompinfo-data-list').datagrid('load', {});
	});
	
	$('#smscompinfo-modelDownload-btn').linkbutton({
		iconCls: 'icon-save',
		text: '下载'
	});
}
//执行
function queryData(){
	$("#smscompinfo-mainBody").unmask();
	$('#smscompinfo-data-list').datagrid('load', serializeObject($('#smscompinfoSearchForm')));
}
// 获取选中记录,弹出修改窗口
function smscompinfoUpdateById(baseUrl,row){
	$.ajax(baseUrl + '/smscompinfo/getSmsCompInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		checkSession(result);
				$('#smscompinfoEditForm').form('load',result.data);
				popWindow('smscompinfo-edit-win', 'smscompinfo-mainBody');
		 	}
	});	
}

// 删除选中记录
function smscompinfoDeleteById(row){
	$.ajax(baseUrl + '/smscompinfo/deleteSmsCompInfoById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	data:data,
		 	success:function(result){
		 		r = eval(result);
		 		if(r.success){
					//刷新界面
					$('#smscompinfo-data-list').datagrid('load', {});
				}else{
					rollDown("imf_roll",r.message);
				}
		 	}
	});
}
// 新增\修改 提交
function smscompinfoSave(url){
	if($('#smscompinfoEditForm').form('validate')){
		$('#smscompinfoEditForm').form('submit', {
			url : baseUrl + '/smscompinfo/saveSmsCompInfo.do',
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						$('#smscompinfo-data-list').datagrid('load', {});
						popInfo('smscompinfo-edit-info', r.message);
						popClosed('smscompinfo-edit-win');
					}else{
						popInfo('smscompinfo-edit-error', r.message);
					}
				} catch (e) {
					popInfo('smscompinfo-edit-error', result);
				}
			}
		});
	}
}

//批量导入
function importsmscompinfo(baseUrl){
	reg = /(\.xls|\.XLS|\.xlsx|\.XLSX|\.csv)$/; //匹配excel文件格式
	if($('#smscompinfo-file').val() == ''){
		popInfo('smscompinfo-import-error', '请选择文件！');
		return false;
	}
	if(!reg.test($('#smscompinfo-file').val())){
		popInfo('smscompinfo-import-error', '请选择Excel格式的文件类型!');
		return false;
	}
	//$("#smscompinfo-mainBody").mask("文件上传中，请稍后...");
	$('#smscompinfo-import-form').form('submit',{
		url : baseUrl + '/smscompinfo/uploadCompExcel.do',
		success : function(result){
			try{
				var r = $.parseJSON(result);
				$("#smscompinfo-mainBody").unmask();
				if(r.success){
					$('#smscompinfo-data-list').datagrid('reload');
				}else{
					popInfo('smscompinfo-import-error', r.message);
					return false;
				}
				popInfo('smscompinfo-import-info', r.message);
			}catch(e){
				$("#smscompinfo-mainBody").unmask();
				popInfo('smscompinfo-import-error', result);
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
