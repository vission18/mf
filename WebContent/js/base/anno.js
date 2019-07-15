//数据列表
function annoDataGrid(baseUrl, buttons, type) {
	annoUrl = baseUrl + '/anno/dataGrid.do';
	annoSaveUrl = baseUrl + '/anno/getAnnoById.do';
	annoDeleteUrl = baseUrl + '/anno/delete.do';
	getAnnoTypesUrl = baseUrl + '/annoType/getAnnoTypes.do';
	var initToolbarAnno = [{
		text : '新增',
		iconCls : 'icon-add',
		buttonType : 'add',
		handler : function(){
			$("#annoEditForm").form('clear');
			$("#uploadform").show();
			$(':radio[name="annoStatus"]').eq(0).attr("checked",true); // 设置第一个radio为选中
			popWindow('anno-edit-win', 'anno-body');
		}
	}, {
		text : '修改',
		iconCls : 'icon-edit',
		buttonType : 'edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#anno-data-list'));
			if (Utils.checkSelectOne(record)) {
				var data = {};
				var idKey = 'annoId';
				data[idKey] = (record[0][idKey]);
				$("#uploadform").show();
				getInfor(annoSaveUrl, record[0]);
				getAcceInforForEdit(baseUrl, record[0]);
				popWindow('anno-edit-win', 'anno-body');
			}
		}
	}, {
		text : '删除',
		iconCls : 'icon-remove',
		buttonType : 'drop',
		handler : function() {
			var record = Utils.getCheckedRows($('#anno-data-list'));
			if (Utils.checkSelectOne(record)) {
				popConfirm('确认删除公告[' + record[0].annoTitle + ']？', 'anno-body');
				$("#popConfirmYes").unbind('click');
				$("#popConfirmYes").click(function() {
					annoDeleteById(annoDeleteUrl, record[0]);
				});
			}
		}
	}];
	var initdatagrid = {
		url : annoUrl,
		fit : true,
		fitColumns : false,
		border : true,
		pagination : true,
		idField : 'annoId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'annoTitle',
		sortOrder : 'asc',
		singleSelect : true,
		checkOnSelect : true,
		selectOnCheck : false,
		remoteSort : false,
		striped : true,
		nowrap : false,
		onDblClickRow : function(rowIndex, rowData) {
			$("#uploadform").show();
			getInfor(annoSaveUrl, rowData);
			getAcceInforForEdit(baseUrl, rowData);
			popWindow('anno-edit-win', 'anno-body');
			
		},
		columns : [ [ {
			field : 'annoId',
			title : '公告Id',
			checkbox : true
		}, {
			field : 'annoTitle',
			title : '公告标题',
			width : 250,
			sortable : true,
			formatter : function(value, row, index) {// 新增连接
				return '<a href=\'javascript:void(0);\' onclick = annoRead("' + baseUrl + '/anno/annoRead.do?annoId='
					+ row.annoId +'","'+baseUrl+'")>'+value+'</a>';
			}
		}, {
			field : 'loginName',
			title : '发布用户',
			width : 120,
			sortable : true
		}, {
			field : 'createDt',
			title : '发布时间',
			width : 150,
			sortable : true
		}, {
			field : 'typeId',
			title : '公告类型',
			width : 150,
			sortable : true
		}, {
			field : 'annoStatus',
			title : '公告状态',
			width : 160,
			sortable : true,
			formatter : function(value, row, index) {// 标识转成汉字
				if (value) {
					return '有效';
				} else {
					return '<span style=\'color:red\'>无效</span>';// 特殊颜色标识
				}
			}
		} ] ]
	};
	/*var newBarsAnno = getAccessButton(initToolbarAnno, buttons, type);
	if (newBarsAnno.length > 0) {
		initdatagrid['toolbar'] = newBarsAnno;
	}*/
	initdatagrid['toolbar'] = initToolbarAnno;
	$('#anno-data-list').datagrid(initdatagrid);
	$('#annoTitle').validatebox({
		required : true,
		missingMessage : '请输入公告标题.'
	});
	$('#annoContent').validatebox({
		required : true,
		missingMessage : '请输入公告内容.'
	});
	$('#beginDt').validatebox({
		required : true,
		missingMessage : '请选择开始日期.'
	});
	$('#endDt').validatebox({
		required : true,
		missingMessage : '请选择结束日期.'
	});
	
}

function initAnnoType(){
	$('#annoTypeId').combobox({
		url: homeBaseUrl + '/annoType/getAnnoTypes.do',
		valueField:'id',
		textField:'text',
		multiple:false,
		required:true,
		missingMessage:'请输选择公告类型.',
		onSelect : function(re){
			$('#annoTypeName').val(re.text);
		}
	});
}

function annoRead(url,baseUrl,type){
	if(!type){
		type = '';
	}
	$.post(url, function(result) {
		checkSession(result);
		r = eval(result);
		if (r.success) {
			document.getElementById("annoTypeForRead"+type).innerHTML = r.data.typeId;
			document.getElementById("annoTitleForRead"+type).innerHTML = r.data.annoTitle;
			document.getElementById("loginNameForRead"+type).innerHTML = r.data.loginName;
			document.getElementById("createDtForRead"+type).innerHTML = r.data.createDt;
			document.getElementById("youxiaoDateForRead"+type).innerHTML = r.data.beginDt + "     -     " + r.data.endDt;
			document.getElementById("annoTypeForRead"+type).innerHTML = r.data.typeId;
			document.getElementById("annoContentForRead"+type).innerHTML = r.data.annoContent;
			$("#annoContentForRead"+type).attr({
				disabled : true
			});
			getAcceInfor(baseUrl,r.data, type);
			if(type){
				popWindow('anno-read-win-exp', 'homeDesktop-body');
			}else{
				popWindow('anno-read-win', 'anno-body');
			}
		}
	}, "json");
}

function annoDeleteById(url, data) {
	$.post(url, data, function(result) {
		checkSession(result);
		r = eval(result);
		if (r.success) {
			index = $('#anno-data-list').datagrid('getRowIndex',
					r.data.annoId);
			$('#anno-data-list').datagrid('deleteRow', index);
		}
		rollDown("imf_roll", r.message);
	}, "json");
}

function annoSave(url){
	$('#annoEditForm').form('submit',{
		url : url,
		success : function(result) {
			try {
				var r = $.parseJSON(result);
				if (r.success) {
					popInfo("annoInfo", r.message);
					if (r.type == 'insert') {
						$('#anno-data-list').datagrid('insertRow', {
							index : 0,
							row : r.data
						});
					} else if (r.type == 'update') {
						$('#anno-data-list').datagrid('updateRow', {
							index : $('#anno-data-list').datagrid('getRowIndex',r.data.annoId),
							row : r.data
						});
					}
					popInfo('annoInfo', r.message);
				} else {
					popInfo("annoError", r.message);
				}
			} catch (e) {
				$.messager.alert('提示', result);
			}
		}
	});
}

$(function() {
	$('#annoBtnSearch').click(function() {
		$('#anno-data-list').datagrid('load',
			serializeObject($('#annoSearchForm')));
		});
	$('#annoBtnClean').click(function() {
		$('#anno-data-list').datagrid('load', {});
	});
});

$(document).ready(function() {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});

var linshiid = "LIN_SHI_ID";

function getInfor(url,data){
	$.ajax(url, {
		type : 'get',
		dataType : 'json',
		data : data,
		success : function(result) {
			checkSession(result);
			$('#annoEditForm').form('load', result.data);
			$("#annoId").attr({
				disabled : true
			});
			if(result.data.annoStatus == 0){
				$(':radio[name="annoStatus"]').eq(1).attr("checked",true);
			}else if(result.data.annoStatus == 1){
				$(':radio[name="annoStatus"]').eq(0).attr("checked",true);
			}
		}
	});
}

function getAcceInfor(baseUrl,data, type){
	html1 = '';
	$.ajax(baseUrl+'/anno/getAcceInforUrl.do', {
		type : 'get',
		dataType : 'json',
		data : data,
		success : function(result) {
			r = eval(result);
			if(r.success){
				var jword = $(result.data);
	 			jword.each(function(){
	 				var temp = "";
	 				var str = this.fileName.split(".");
	 				for(var i = 0 ; i < str.length ; i++){
	 					temp += str[i];
	 				}
	 				html1 += '<span id="aaa'+temp+'"><input type = "hidden" id = "' + this.fileName + '" name = "' + this.fileName + '">'
	 						+ this.fileName+'<a href="' + baseUrl + '/anno/download.do?fileName=' 
	 						+ this.fileName +'&annoId='+data.annoId+'">' + "【下载】" + '</a></span>';
	 			});
	 			$("#annoAcceForRead" + type).html(html1);
			}
		}
	});
}
//点击修改按钮调用函数
function getAcceInforForEdit(baseUrl,data){
	html2 = '';
	$.ajax(baseUrl+'/anno/getAcceInforUrl.do', {
		type : 'get',
		dataType : 'json',
		data : data,
		success : function(result) {
			r = eval(result);
			if (r.success){
				var jword = $(result.data);
	 			jword.each(function(){
	 				var temp = "";
	 				var str = this.fileName.split(".");
	 				for(i = 0;i<str.length;i++){
	 					temp += str[i];
	 				}//id中弱有点，按id删除时删除不成功
	 				html2 += '<span id="aaa'+temp+'"><input type = "hidden" id = "' + this.fileName + '" name = "' + this.fileName + '">'
	 						+this.fileName+'<a href=\'javascript:;\' onclick = fileDel("' + baseUrl + '/anno/fileDel.do","'
	 						+ this.fileName + '","'+data.annoId+'","'+temp+'")>' + "【删除】" + '</a></span>';
	 			});
	 			$("#editAnnoAcceList").html(html2);
			}
		}
	});
}

function fileDel(url,fileName,annoId,temp){
	$.post(url,{fileName:fileName,annoId:annoId}, function(result) {
		checkSession(result);
		r = eval(result);
		if (r.success) {
			$("#aaa"+ temp).remove();
		}
	}, "json");
}

function closeEditWindow(baseUrl){
	$.post(baseUrl + "/anno/deletelinshiidacce.do", function(result) {
	}, "json");
	popClosed('anno-edit-win');
	$("li[id]").remove();
}
function closeReadWindow(type){
	if(type){
		popClosed('anno-read-win-exp');
	}else{
		popClosed('anno-read-win');
	}
}

function upload(baseUrl){
	if($('#annoAcce').val() === ''){
		popInfo('imf_pop_error', '请选择文件！');
		return false;
	}
	html = $("#editAnnoAcceList").html();
	$('#uploadform').form('submit',{
		url : baseUrl + '/anno/upload.do',
		success : function(result) {
			try {
				var r = $.parseJSON(result);
				if (r.success) {
					var jword = $(r.data);
         			jword.each(function(){
         				//去掉文件名中所有的点,否则带点的名不能做参数传递
         				var temp = "";
         				var str = this.fileName.split(".");
         				for(i = 0;i<str.length;i++){
         					temp += str[i];
         				}
         				html += '<span id="aaa'+temp+'"><input type = "hidden" id = "' + this.fileName + '" name = "' + this.fileName + '">'
         						+ this.fileName + '<a href=\'javascript:;\' onclick = fileDel("' + baseUrl + '/anno/fileDel.do","'+this.fileName
         						+'","'+linshiid+'","' + temp + '")>' + "【删除】" + '</a></span>';
         			});
         			$("#editAnnoAcceList").html(html);
				} else {
					popInfo("annoError", r.message);
				}
			} catch (e) {
				$.messager.alert('提示', result);
			}
		}
	});
}

function homeAnnoDataGrid(baseUrl) {
	annoUrl = baseUrl + "/anno/dataGrid.do?";
	var initdatagrid = {
		url : annoUrl,
		fit : true,
		fitColumns : true,
		border : true,
		pagination : true,
		idField : 'annoId',
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		sortName : 'annoTitle',
		sortOrder : 'asc',
		singleSelect : true,
		remoteSort : false,
		striped : true,
		nowrap : false,
		columns : [ [ {
			field : 'annoId',
			width : 400,
			title : '最新公告',
			sortable : false,
			formatter : function(value, row, index) {// 增加连接
				annoType = '';
				if(row.typeId===''){
				}else{
					annoType = '【'+row.typeName+'】';
				}
				return '<a href=\'javascript:void(0);\' onclick = annoRead("' + baseUrl + '/anno/annoRead.do?annoId='
					+ value +'","'+baseUrl+'","exp")>'+annoType+row.annoTitle+'</a>';		
			}
		} ] ]
	};
	$('#admin-home-anno-data-list').datagrid(initdatagrid);
}