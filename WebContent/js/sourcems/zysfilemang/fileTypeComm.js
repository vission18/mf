//数据列表
function fileTypeDataGrid(baseUrl, buttons, type,fileType){
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
			width : 240,
		}
		, {
			field : 'fileSize',
			title : '文件大小',
			width : 80,
			align : 'center',
			formatter : function(value, row, index) {
				if(parseInt(value) >0 && parseInt(value) <= parseInt('1048576‬') ){
					//KB展示
					var num = (value/1024).toFixed(2);
					return   num + "KB";
				}else if(parseInt(value) >parseInt('1048576‬')){
					//M展示
					var num = (value/1024/1024).toFixed(2);
					return  num + "M";
				}else {
					return value;
				}
			}
		}
		, {
			field : 'fileRemark',
			title : '文件说明',
			width : 300,
			align : 'left',
			formatter : function(value, row, index) {
				if(value.length > 26){
					return '<span title="'+value+'">' + value.substr(0,26) + '......</span>';
				}else{
					return value;
				}
			}
		}
		, {
			field : 'costPrice',
			title : '售价',
			width : 80,
			align : 'center',
			formatter : function(value, row, index) {
				return '<span style=\'color:red\'>'+value+' 元</span>';// 特殊颜色标识
			}
		}
		, {
			field : 'nowPrice',
			title : '秒杀价',
			width : 80,
			align : 'center',
			formatter : function(value, row, index) {
				return '<span style=\'color:red\'>'+value+' 元</span>';// 特殊颜色标识
			}
		}
		, {
			field : 'downloadCount',
			title : '下载次数',
			width : 80,
			align : 'center'
		}
		, {
			field : 'oper',
			title : '操作',
			width : 200,
			align : 'center',
			formatter : function(value, row, index) {
				var str = '<span >';
				if(parseInt(row.fileSize) >parseInt('103809024')){
					str = str+ '<a href="#" onclick="openSkyCommWin('+index+',\''+fileType+'\')" style="color:blue" >网盘下载</a>';
				}else{
					href = baseUrl + '/zysFileMan/downloadFileByPkId.do?PK_ID='+row.pkId;
					str = str+ '<a href="'+ href +'">下载</a>';
				}
				
				
				str = str+ '&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" onclick="openAdminCommWin(\''+fileType+'\')" style="color:green" >联系管理员</a>';
				str = str +'</span>';
				return str;
			}	
		}
		 ] ]
	};
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
	console.info(rowIndex);
	console.info(fileType);
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
	
	//更新下载次数
	$.ajax(baseUrl + '/zysFileMan/updateDownloadCount.do?PK_ID='+fileRowComm.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		//$('#zysFileMan-data-list').datagrid('reload');
		 	}
	});	
	
}

// 联系管理员
function openAdminCommWin(fileType){
	popWindow(fileType+'-adminInfo-win', fileType+'-mainBody');
}

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus");
	}).blur(function () {
		$(this).removeClass("input_focus");
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});
