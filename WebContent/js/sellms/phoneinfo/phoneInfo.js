//数据列表
function phoneInfoDataGrid(baseUrl, buttons, type){
	var phoneInfo_url = baseUrl + '/smsPhoneInfo/dataGrid.do';
	var initPhoneInfoToolbar = [ {
		text : '生成号码',
		buttonType:'create',
		iconCls : 'icon-tip',
		handler : function() {
			openCreatePhoneWin();
		}
	},'-',{
		text : '信号检测',
		buttonType:'check',
		iconCls : 'icon-reload',
		handler : function() {
			var record = Utils.getCheckedRows($('#phoneInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					
				}
			}
	},'-',{
		text : '群发短信',
		buttonType:'send',
		iconCls : 'icon-print',
		handler : function() {
			var record = Utils.getCheckedRows($('#phoneInfo-data-list'));
			if (Utils.checkSelectOne(record)){
					
				}
			}
	},'-',{
		text : '全部导出',
		buttonType : 'export',
		iconCls : 'icon-redo',
		handler : function(){
			popConfirm('确认导出文件？','phoneInfo-mainBody');
			$("#popConfirmYes").unbind('click');
			$("#popConfirmYes").click(function (){
				var url= baseUrl+'/smsPhoneInfo/expExcel.do';	
				window.location.href=url;
			});
		}
	}];
	var initPhoneInfoDataGrid = {
		//url : phoneInfo_url,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'idNum',
		pageSize : 10000,
		pageList : [ 10000 ],
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,// 服务器端排序
		striped : true,// 奇偶行不同颜色
		nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
		frozenColumns : [ [ {
			field : 'idNum',
			title : '主键',
			width : 50,
			checkbox : true
		}, {
			field : 'phoneNum',
			title : '电话号码',
			width : 200
		} ] ],
		columns : [ [ {
			field : 'province',
			title : '所属省份',
			width : 200,
		}, {
			field : 'city',
			title : '所属城市',
			width : 200,
		}, {
			field : 'phoneStatus',
			title : '号码状态',
			width : 200,
			formatter : function(value, row, index) {// 标识转成汉字
				if (value == '1') {
					return '<span style=\'color:green\'>正常</span>';
				}else if (value == '2') {
					return '<span style=\'color:red\'>停机</span>';
				}else if (value == '3') {
					return '<span style=\'color:orange\'>空号</span>';
				}
			}
		} ] ]
	};
	var newBars = getAccessButton(initPhoneInfoToolbar,buttons,type);
	if(newBars.length>0){
		initPhoneInfoDataGrid['toolbar'] = newBars;
	}
	$('#phoneInfo-data-list').datagrid(initPhoneInfoDataGrid);
}

function openCreatePhoneWin(){
	popWindow('phoneInfo-edit-win', 'phoneInfo-mainBody');
	$('#phoneInfoEditForm').form('clear');
}

function creatPhoneInfo(url){
	if($('#phoneInfoEditForm').form('validate')){
		$('#phoneInfoEditForm').form('submit', {
			url : url,
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					console.info(r);
					if (r.success) {
						//刷新界面
						$('#phoneInfo-data-list').datagrid('loadData',r.data);
						popInfo('phoneInfo-edit-info', r.message);
						popClosed('phoneInfo-edit-win');
					}else{
						popInfo('phoneInfo-edit-error', r.message);
					}
				} catch (e) {
					popInfo('phoneInfo-edit-error', result);
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
