//数据列表
function zysFileManDataGrid(baseUrl, buttons, type){
	var zysFileMan_url = baseUrl + '/zysFileMan/zysFileManDataGrid.do';
	var initZysFileMangToolbar = [ {
		text : '上传',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			$('#zysFileManEditForm').form('clear');
			popWindow('zysFileMan-edit-win', 'zysFileMan-mainBody');
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#zysFileMan-data-list'));
			if (Utils.checkSelectOne(record)){
					zysFileManUpdateById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#zysFileMan-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除这条数据吗?','zysFileMan-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						zysFileManDeleteById(record[0]);
					});
				}
			}
	}];
	var initZysFileMangDataGrid = {
		url : zysFileMan_url,
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
			zysFileManUpdateById(baseUrl,rowData);
		},
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
			width : 80,
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
			formatter : function(value, row, index) {
				var str = '<span >';
			/*	if(parseInt(row.fileSize) > parseInt('103809024')){
					str = str+ '<a href="#" onclick="openSkyWin('+index+')" style="color:blue" >网盘下载</a>';
				}else{
					href = baseUrl + '/zysFileMan/downloadFileByPkId.do?PK_ID='+row.pkId;
					str = str+ '<a href="'+ href +'">下载</a>';
				}*/
				str = str+ '<a href="#" onclick="openSkyWin('+index+')" style="color:blue" >网盘下载</a>';
				str = str+ '&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="openContackAuthorWin('+index+')" style="color:green" >联系作者</a>';
				str = str +'</span>';
				return str;
			}	
		}
		 ] ]
	};
	var newBars = getAccessButton(initZysFileMangToolbar,buttons,type);
	if(newBars.length>0){
		initZysFileMangDataGrid['toolbar'] = newBars;
	}
	$('#zysFileMan-data-list').datagrid(initZysFileMangDataGrid);
	
	$('#zysFileManBtnSearch').click(function(){// 查询
		$('#zysFileMan-data-list').datagrid('load', serializeObject($('#zysFileManSearchForm')));
	});
	$('#zysFileManBtnClean').click(function(){// 全部
		$('#zysFileMan-data-list').datagrid('load', {});
		$('#zysFileManSearchForm').form('clear');
	});
	queryTableCodePara(baseUrl);
}
//查询码表参数
function queryTableCodePara(baseUrl){
	$.ajax(baseUrl + '/zysFileMan/queryTbCodePara.do', {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		checkSession(result);
				$('#fileType').combobox({
					data:result.fileTypeList,
					valueField:'id',
					textField:'text'
				});
		 	}
	});	
}

//保存file记录
function saveFileRec(baseUrl){
	$('#filePath').val($('#upload-file').val());
	$('#zysFileManEditForm').form('submit',{
		url : baseUrl + '/zysFileMan/saveFileRec.do',
		success : function(result){
			try{
				var r = $.parseJSON(result);
				if(r.success){
					$('#zysFileMan-data-list').datagrid('reload');
					popClosed('zysFileMan-edit-win');
				}else{
					popInfo('zysFileMan-edit-error', r.message);
					return false;
				}
				popInfo('zysFileMan-edit-info', r.message);
			}catch(e){
				popInfo('zysFileMan-edit-error', result);
			}
		}
	});
	
}

// 获取选中记录,弹出修改窗口
function zysFileManUpdateById(baseUrl,row){
	$.ajax(baseUrl + '/zysFileMan/getZysFileMangById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		checkSession(result);
				$('#zysFileManEditForm').form('load',result.data);
				popWindow('zysFileMan-edit-win', 'zysFileMan-mainBody');
		 	}
	});	
}
//网盘窗口
function openSkyWin(rowIndex){
	//先清空
	$('#openSkyDriveUrl').text('');
	$('#openSkyDriveExtCode').text('');
	var fileRows = $('#zysFileMan-data-list').datagrid('getRows');
	var fileRow = fileRows[rowIndex];
	//添加超链接
	var addA = document.createElement('a'); 
	addA.innerHTML = fileRow.skyDriveUrl;
	addA.href = fileRow.skyDriveUrl;
	addA.target="_blank";
	$('#openSkyDriveUrl').append(addA);
	$('#openSkyDriveExtCode').val(fileRow.skyDriveExtCode);
	popWindow('skyDrive-download-win', 'zysFileMan-mainBody');
	
	var colJson = {
			'DOWNLOAD_COUNT':1,
			'DOWNLOAD_COUNT_ONDATE':1,
			'COUNT_CLICK':1,
			'COUNT_CLICK_ONDATE':1
			};
	//更新下载次数
	$.ajax(baseUrl + '/zysFileMan/updateFileCount.do?PK_ID='+fileRow.pkId, {
		type:'post',
		 	dataType:'json',
		 	data:colJson,
		 	success:function(result){
		 	}
	});	
	
}

// 删除选中记录
function zysFileManDeleteById(row){
	$.ajax(baseUrl + '/zysFileMan/deleteZysFileMangById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		console.info(result);
		 		r = eval(result);
		 		console.info(r);
		 		if(r.success){
					//刷新界面
		 			$('#zysFileMan-data-list').datagrid('reload');
				}else{
					rollDown("imf_roll",r.message);
				}
		 	}
	});
}
/**
 * 联系作者
 */
function openContackAuthorWin(rowIndex){
	var fileRows = $('#zysFileMan-data-list').datagrid('getRows');
	var fileRow = fileRows[rowIndex];
	$('#contackAuthor').html('手机：'+fileRow.userMobTel+' </br>邮箱：'+fileRow.userEmail);
	console.info(fileRow.vxImgPath);
	$('#authorVxImg').attr('src',fileRow.vxImgPath);
	popWindow('open-adminInfo-win', 'zysFileMan-mainBody');
}

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus");
	}).blur(function () {
		$(this).removeClass("input_focus");
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});
