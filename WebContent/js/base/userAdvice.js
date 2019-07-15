//数据列表
function userAdviceDataGrid(baseUrl, buttons, type){
	var userAdviceUrl = baseUrl + '/advice/dataGrid.do';
	var userAdviceDeleteUrl = baseUrl + '/advice/delete.do';
	
	var userAdvice_toolbar = [ {
		text : '删除',
		buttonType : 'drop',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#userAdvice-data-list'));
			if (Utils.checkSelectOne(record)){
				popConfirm('确认删除建议['+record[0].pageId+']？','userAdvice-mainBody');
				$("#popConfirmYes").unbind('click');
				$("#popConfirmYes").click(function (){
					userAdviceDeleteById(userAdviceDeleteUrl,record[0]);
				});
			}
		}
	}, '-' ];
	
	var initDataGrid = {
		url : userAdviceUrl,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'adviceId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'projectId',
		sortOrder : 'asc',
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,//服务器端排序
		striped : true,//奇偶行不同颜色
		nowrap : false,//设置为true,当数据长度超出列宽时将会自动截取
		frozenColumns : [ [ {
			field : 'userAdviceId',
			title : '建议编号',
			width : 150,
			checkbox : true
		}, {
			field : 'pageId',
			title : '页面名称',
			width : 80,
			sortable : true
		}
		] ],
		columns : [ [{
			field : 'advice',
			title : '用户建议',
			width : 90
		}] ]
	};
	
	var userAdvice_newBars = getAccessButton(userAdvice_toolbar, buttons, type);
	if(userAdvice_newBars.length>0){
		initDataGrid['toolbar'] = userAdvice_newBars;
	}
	$('#userAdvice-data-list').datagrid(initDataGrid);
}

function userAdviceDeleteById(url,data){
	$.post(url,data,
		function(result){
			checkSession(result);
			r = eval(result);
			if(r.success){
				index = $('#userAdvice-data-list').datagrid('getRowIndex', r.data.adviceId);
				$('#userAdvice-data-list').datagrid('deleteRow', index);
			}
			rollDown("imf_roll",r.message);	
		}, "json"
	);
}

$(function(){
	$('#userAdvice-btnSearch').click(function(){
		$('#userAdvice-data-list').datagrid('load', serializeObject($('#userAdvice-searchForm')));
	});
	$('#userAdvice-btnClean').click(function(){
		$('#userAdvice-data-list').datagrid('load', {});
	});
});	

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});