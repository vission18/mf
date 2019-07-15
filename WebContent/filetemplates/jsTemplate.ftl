//数据列表
function ${atfnBo.defVarName}DataGrid(baseUrl, buttons, type){
	var ${atfnBo.defVarName}_url = baseUrl + '/${atfnBo.defVarName}/${atfnBo.defVarName}DataGrid.do';
	var init${atfnBo.moudPoName}Toolbar = [ {
		text : '新增',
		buttonType:'add',
		iconCls : 'icon-add',
		handler : function() {
			popWindow('${atfnBo.defVarName}-edit-win', '${atfnBo.defVarName}-mainBody');
			$('#${atfnBo.defVarName}EditForm').form('clear');
		}
	},'-',{
		text : '修改',
		buttonType:'edit',
		iconCls : 'icon-edit',
		handler : function() {
			var record = Utils.getCheckedRows($('#${atfnBo.defVarName}-data-list'));
			if (Utils.checkSelectOne(record)){
					${atfnBo.defVarName}UpdateById(baseUrl,record[0]);
			}
		}
	},'-',{
		text : '删除',
		buttonType:'delete',
		iconCls : 'icon-remove',
		handler : function() {
			var record = Utils.getCheckedRows($('#${atfnBo.defVarName}-data-list'));
			if (Utils.checkSelectOne(record)){
					popConfirm('确认删除这条数据吗?','${atfnBo.defVarName}-mainBody');
					$("#popConfirmYes").unbind('click');
					$("#popConfirmYes").click(function (){
						${atfnBo.defVarName}DeleteById(record[0]);
					});
				}
			}
	}];
	var init${atfnBo.moudPoName}DataGrid = {
		url : ${atfnBo.defVarName}_url,
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
			${atfnBo.defVarName}UpdateById(baseUrl,rowData);
		},
		frozenColumns : [ [ {
			field : 'pkId',
			title : '复选框',
			width : 50,
			checkbox : true
		}
	    <#if atfnBo.acnList?exists >
		<#list atfnBo.acnList as acn>
		<#if acn.checkIsPk ==0  >
		, {
			field : '${acn.p_colTfNames}',
			title : '${acn.colDesc}',
			width : ${colWidth},
		}
		</#if>
		</#list>
		</#if>
		 ] ]
	};
	var newBars = getAccessButton(init${atfnBo.moudPoName}Toolbar,buttons,type);
	if(newBars.length>0){
		init${atfnBo.moudPoName}DataGrid['toolbar'] = newBars;
	}
	$('#${atfnBo.defVarName}-data-list').datagrid(init${atfnBo.moudPoName}DataGrid);
	
	$('#${atfnBo.defVarName}BtnSearch').click(function(){// 查询
		$('#${atfnBo.defVarName}-data-list').datagrid('load', serializeObject($('#${atfnBo.defVarName}SearchForm')));
	});
	$('#${atfnBo.defVarName}BtnClean').click(function(){// 全部
		$('#${atfnBo.defVarName}-data-list').datagrid('load', {});
		$('#${atfnBo.defVarName}SearchForm').form('clear');
	});
}

// 获取选中记录,弹出修改窗口
function ${atfnBo.defVarName}UpdateById(baseUrl,row){
	$.ajax(baseUrl + '/${atfnBo.defVarName}/get${atfnBo.moudPoName}ById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		checkSession(result);
				$('#${atfnBo.defVarName}EditForm').form('load',result.data);
				popWindow('${atfnBo.defVarName}-edit-win', '${atfnBo.defVarName}-mainBody');
		 	}
	});	
}

// 删除选中记录
function ${atfnBo.defVarName}DeleteById(row){
	$.ajax(baseUrl + '/${atfnBo.defVarName}/delete${atfnBo.moudPoName}ById.do?PK_ID='+row.pkId, {
		type:'post',
		 	dataType:'json',
		 	success:function(result){
		 		try{
		 			r = eval(result);
			 		if(r.success){
						//刷新界面
						$('#${atfnBo.defVarName}-data-list').datagrid('load', {});
					}
			 		rollDown("imf_roll",r.message);
		 		}catch(e){
		 			rollDown("imf_roll",r.message);
		 		}
		 	}
	});
}
// 新增\修改 提交
function ${atfnBo.defVarName}Save(url){
	if($('#${atfnBo.defVarName}EditForm').form('validate')){
		$('#${atfnBo.defVarName}EditForm').form('submit', {
			url : baseUrl + '/${atfnBo.defVarName}/save${atfnBo.moudPoName}.do',
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						//刷新界面
						$('#${atfnBo.defVarName}-data-list').datagrid('load', {});
						popInfo('${atfnBo.defVarName}-edit-info', r.message);
						popClosed('${atfnBo.defVarName}-edit-win');
					}else{
						popInfo('${atfnBo.defVarName}-edit-error', r.message);
					}
				} catch (e) {
					popInfo('${atfnBo.defVarName}-edit-error', result);
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
