
function dataGrid(baseUrl,buttons,type) {
	var initToolbar=[ {
		buttonType:'drop',
		text : '批量删除',
		iconCls : 'icon-remove',
		handler : function() {
			var records = Utils.getCheckedRows($('#operlog_data_list'));
			if (Utils.checkSelect(records)){
				$("#popConfirmYes").unbind('click');
				popConfirm('确认删除所选操作日志？','operLog-mainBody');
				$("#popConfirmYes").click(function (){
					sysOperDeleteById(baseUrl,records);
				});
			}
		}
	} ];
	var initDataGrid={
			rownumbers : true,
			url : baseUrl+'/operLog/dataGrid.do',
			fit : true,
			fitColumns : false,
			collapsible : true,
			border : true,
			pagination : true,
			idField : 'userId',
			pageSize : 10,
			pageList : [ 10, 20, 30, 40, 50 ],
			sortName : 'loginName',
			sortOrder : 'asc',
			checkOnSelect : false,
			selectOnCheck : false,
			nowrap : false,
			striped : true,// 奇偶行不同颜色
			frozenColumns : [ [ {
				field : 'logId',
				title : '',
				width : 50,
				checkbox : true
			}, {
				field : 'loginName',
				title : '登录名称',
				width : 120,
				sortable : true

			}, {
				field : 'loginIp',
				title : '登录IP',
				width : 120,
				sortable : true
			}, {
				field : 'operTime',
				title : '操作时间',
				width : 160,
				sortable : true
			},{
				field : 'operTransType',
				title : '操作类型',
				width : 120
			} ] ],
			columns : [ [ {
				field : 'operContent',
				title : '操作详情',
				width : 300,
				sortable : false,
				resizable : true
			} ] ]	
	};
	
	var newBars=getAccessButton(initToolbar,buttons,type);
	if(newBars.length>0){
		initDataGrid['toolbar']=newBars;
	}
	$('#operlog_data_list').datagrid(initDataGrid);
	$('#operLog_btnSearch').click(
			function() {
				$('#operlog_data_list').datagrid('load',
						serializeObject($('#operLog_searchForm')));
			});
	$('#operLog_btnClean').click(function() {
		$('#loginName').val('');
		$('#loginIp').val('');
		$('#operType').combobox('setValue','');
		$('#beginOperTime').datetimebox('setValue', '');
		$('#endOperTime').datetimebox('setValue', '');
		$('#operlog_data_list').datagrid('load', {});
	});
	$('#operLog_btnClose').click(function() {
		$('#operlog_data_list').dialog('close');
	});
}
function sysOperDeleteById(baseUrl,records){
	var ids="";
	for(var i=0;i<records.length;i++){
		ids+="'"+records[i].logId+"',";
	}
	$.post(baseUrl+'/operLog/deleteByIds.do?ids='+ids,
		function(result){
			checkSession(result);
			r = eval(result);
			if(r.success){
				$('#operlog_data_list').datagrid('load',
				serializeObject($('#operLog_searchForm')));
			}
			rollDown("imf_roll",r.message);
		}, "json"
	);
}

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
})