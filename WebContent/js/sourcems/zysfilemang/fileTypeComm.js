//数据列表
function fileTypeDataGrid(baseUrl, buttons, type,fileType){
	var fileTypeDataGridToolbar = [ {
		text : '我要分享',
		buttonType:'share',
		iconCls : 'icon-tip',
		handler : function() {
			popWindow(fileType+'-share-win', fileType+'-mainBody');
		}
	},'-',{
		text : '我要赚钱',
		buttonType:'edit',
		iconCls : 'icon-help',
		handler : function() {
			popWindow(fileType+'-makeMonney-win', fileType+'-mainBody');
		}
	},'-',{
		text : '联系管理员',
		buttonType:'edit',
		iconCls : 'icon-ok',
		handler : function() {
			popWindow(fileType+'-manager-win', fileType+'-mainBody');
		}
	}];
	
	var initFileTypeCommDataGrid = {
		url : baseUrl + '/zysFileMan/zysFileManDataGrid.do?fileTypeComm='+fileType,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'pkId',
		pageSize : 10,
		pageList : [30,60,90 ],
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,// 服务器端排序
		striped : true,// 奇偶行不同颜色
		nowrap : false,// 设置为true,当数据长度超出列宽时将会自动截取
		columns : [ [ {
			field : 'pkId',
			title : '复选框',
			width : 50,
			checkbox : true
		}
		, {
			field : 'fileNm',
			title : '文件名称',
			width : 280,
		}
		, {
			field : 'countOndate',
			title : '当前年月',
			width : 80,
		}
		, {
			field : 'countClick',
			title : '总点击量',
			width : 80,
		}
		, {
			field : 'countClickOndate',
			title : '当月点击量',
			width : 80,
		}
		, {
			field : 'countZan',
			title : '好评总数',
			width : 80
		}
		, {
			field : 'countZanOndate',
			title : '当月好评',
			width : 80
		}
		, {
			field : 'downloadCount',
			title : '下载总量',
			width : 80,
		}
		, {
			field : 'downloadCountOndate',
			title : '当月下载量',
			width : 80,
		}
		, {
			field : 'oper',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, row, index) {
				var str = '<span >';
				str = str+ '<a href="#" onclick="qryDetailWin('+index+',\''+fileType+'\')" style="color:blue" >查看详细</a>';
				str = str+ '&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="openSkyCommWin('+index+',\''+fileType+'\')" style="color:blue" >下载</a>';
				str = str+ '&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="openContackAuthorWin('+index+',\''+fileType+'\')" style="color:green" >联系作者</a>';
				str = str +'</span>';
				return str;
			}	
		}
		 ] ]
	};
	
	var newToolBars = getAccessButton(fileTypeDataGridToolbar,buttons,type);
	if(newToolBars.length>0){
		//initFileTypeCommDataGrid['toolbar'] = newToolBars;
	}
	initFileTypeCommDataGrid['toolbar'] = fileTypeDataGridToolbar;
	$('#'+fileType+'-data-list').datagrid(initFileTypeCommDataGrid);
	
	$('#'+fileType+'BtnSearch').click(function(){// 查询
		$('#'+fileType+'-data-list').datagrid('load', serializeObject($('#'+fileType+'SearchForm')));
	});
	$('#'+fileType+'BtnClean').click(function(){// 全部
		$('#'+fileType+'-data-list').datagrid('load', {});
		$('#'+fileType+'SearchForm').form('clear');
	});
}
//网盘窗口
function openSkyCommWin(rowIndex,fileType){
	//先清空
	$('#'+fileType+'OpenUrl').text('');
	$('#'+fileType+'OpenExtCode').text('');
	var fileRowsComm = $('#'+fileType+'-data-list').datagrid('getRows');
	var fileRowComm = fileRowsComm[rowIndex];
	//添加超链接
	var addCommA = document.createElement('a'); 
	addCommA.innerHTML = fileRowComm.skyDriveUrl;
	addCommA.href = fileRowComm.skyDriveUrl;
	addCommA.target="_blank";
	$('#'+fileType+'OpenUrl').append(addCommA);
	$('#'+fileType+'OpenExtCode').val(fileRowComm.skyDriveExtCode);
	popWindow(fileType+'-download-win', fileType+'-mainBody');
	
	var colJson = {
			'DOWNLOAD_COUNT':1,
			'DOWNLOAD_COUNT_ONDATE':1,
			'COUNT_CLICK':1,
			'COUNT_CLICK_ONDATE':1
			};
	//更新下载次数
	$.ajax(baseUrl + '/zysFileMan/updateFileCount.do?PK_ID='+fileRowComm.pkId, {
		type:'post',
		 	dataType:'json',
		 	data:colJson,
		 	success:function(result){
		 	}
	});	
	
}

// 联系作者
function openContackAuthorWin(rowIndex,fileType){
	var fileRows = $('#'+fileType+'-data-list').datagrid('getRows');
	var fileRowComm = fileRows[rowIndex];
	$('#'+fileType+'-contackAuthor').html('手机：'+fileRowComm.userMobTel+' </br>邮箱：'+fileRowComm.userEmail);
	$('#'+fileType+'-authorVxImg').attr('src',fileRowComm.vxImgPath);
	popWindow(fileType+'-adminInfo-win', fileType+'-mainBody');
	var colJson = {
			'COUNT_CLICK':1,
			'COUNT_CLICK_ONDATE':1
			};
	
	//更新下载次数
	$.ajax(baseUrl + '/zysFileMan/updateFileCount.do?PK_ID='+fileRowComm.pkId, {
		type:'post',
		 	dataType:'json',
		 	data:colJson,
		 	success:function(result){
		 		//$('#zysFileMan-data-list').datagrid('reload');
		 	}
	});
}

//查看详细
function qryDetailWin(rowIndex,fileType){
	var fileRows = $('#'+fileType+'-data-list').datagrid('getRows');
	var fileRowComm = fileRows[rowIndex];
	$('#'+fileType+'-zysFileManEditForm').form('load',fileRowComm);
	popWindow(fileType+'-zysFileMan-edit-win', fileType+'-mainBody');
	//需要更新的字段【点击量】
	var colJson = {
			'COUNT_CLICK':1,
			'COUNT_CLICK_ONDATE':1
			};
	
	//更新下载次数
	$.ajax(baseUrl + '/zysFileMan/updateFileCount.do?PK_ID='+fileRowComm.pkId, {
		type:'post',
		 	dataType:'json',
		 	data:colJson,
		 	success:function(result){
		 		//$('#zysFileMan-data-list').datagrid('reload');
		 	}
	});	
}

//查看详细
function updateZan(baseUrl,fileType){
	
	//需要更新的字段【点击量】
	var colJson = {
			'COUNT_ZAN':1,
			'COUNT_ZAN_ONDATE':1
			};
	
	$.ajax(baseUrl + '/zysFileMan/updateFileCount.do?PK_ID='+$('#'+fileType+'-pkId').val(), {
		type:'post',
		 	dataType:'json',
		 	data:colJson,
		 	success:function(result){
		 		rollDown("imf_roll",'您的认可就是我前进的动力');
		 	}
	});	
}
//打开注册窗口
function openLoginWin(fileType){
	popWindow(fileType+'-user-edit-win',fileType+'-share-win');
}

//注册保存
function saveLogin(baseUrl,fileType){
	reg = /(\.png|\.jpg|\.JPG|\.PNG)$/; 
	if($('#'+fileType+'_imgFile').val() === ''){
		popInfo(fileType+'-user-edit-error', '请上传收款二维码');
		return false;
	}
	if(!reg.test($('#'+fileType+'_imgFile').val())){
		popInfo(fileType+'-user-edit-error', '请选择jpg或png格式的二维码头像');
		return false;
	}
	if($('#'+fileType+'EditForm').form('validate')){
		$('#'+fileType+'EditForm').form('submit', {
			url : baseUrl+'/user/saveByFile.do',
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						popInfo(fileType+'-user-edit-info', r.message);
						popClosed(fileType+'-user-edit-win');
					}else{
						popInfo(fileType+'-user-edit-error', r.message);
					}
				} catch (e) {
					popInfo(fileType+'-user-edit-error', result);
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
