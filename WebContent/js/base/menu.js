function menuDataGrid(url, baseUrl,buttons,type) {
	var initToolbarMenu=[{
		text : '新增',
		iconCls : 'icon-add',
		buttonType:'add',
		handler : function() {
			popWindow('menu-edit-win','menu-body');
			$("#menu-edit-win").form('clear');
			$(':radio[name="newOpen"]').eq(1).attr("checked",true);
			menuGetMaxOrder(baseUrl + '/menu/getMaxOrder.do');//获取最大排序序号
			createMenuTree(baseUrl);
		}
	}, {
		text : '修改',
		iconCls : 'icon-edit',
		buttonType : 'edit',
		handler : function() {
			var record = $('#menu-data-list').treegrid('getSelected');
			if (record != null) {
				var data = {
					id : null
				};
				data.id = record.id;
				menuSaveById(baseUrl + '/menu/getTreeById.do', data);
				createMenuTree(baseUrl);
			} else {
				$.messager.alert('提示',"请选择一条记录.");
			}
		}
	}, {
		text : '删除',
		iconCls : 'icon-remove',
		buttonType : 'drop',
		handler : function() {
			var record = $('#menu-data-list').treegrid('getSelected');
			var data = {
				id : null
			};
			if (record != null) {
				data.id = record.id;
				popConfirm('确认删除菜单[' + record.text + ']？', 'menu-body');
				$("#popConfirmYes").click(function() {
					menuDeleteById(baseUrl + '/menu/delete.do', data);
				});
			} else {
				$.messager.alert('提示',"请选择一条记录.");
			}
		}
	} ];
	var initTreeGridMenu={
		fit : true,
		iconCls : 'icon-save',
		nowrap : false,
		rownumbers : true,
		collapsible : true,
		url : url,
		idField : 'id',
		treeField : 'text',
		onDblClickRow : function(row) {
			var data={id:null};
				data.id=row.id;
			menuSaveById(baseUrl + '/menu/getTreeById.do',data);// 双击进入修改
			createMenuTree(baseUrl);
		},
		columns : [ [ {
			field : 'checkboxId',
			title : '',
			width : 50,
			checkbox : true
		}, {
			field : 'text',
			title : '菜单名称',
			width : 300
		}] ],
		onContextMenu : function(e, row) {// 树节点右键单击时触发该函数
			e.preventDefault();
			$(this).treegrid('unselectAll');
			$(this).treegrid('select', row.id);
		},
		onBeforeExpand: function(row){
			
		}
	};
	var newBarsMenu = getAccessButton(initToolbarMenu,buttons,type);
	if(newBarsMenu.length>0){
		initTreeGridMenu['toolbar']=newBarsMenu;
	}
	$('#menu-data-list').treegrid(initTreeGridMenu);

	$('#menuName').validatebox({
		required : true ,
		missingMessage : '请输入菜单名称.'
	});
	$('#sssmenuOrder').validatebox({
		required : true ,
		missingMessage : '请定义菜单顺序.'
	});
}

function createMenuTree(baseUrl){
	$('#parentMenuId').combotree({ 
		url : baseUrl + '/menu/getAllMenuTreeNoleaf.do'
	});
}


formatString = function(str) {
	var iconCls="";
	for ( var i = 0; i < arguments.length - 1; i++) {
		if(arguments[i + 1]=="images/package_settings.png"){
			iconCls="icon-root";
		}
		if(arguments[i + 1]=="images/print_class.png"){
			iconCls="icon-reaf";
		}
	   if(arguments[i + 1]=="images/kdmconfig.png"){
		   iconCls="icon-oneself";
	   }
		str = str.replace("{" + i + "}", iconCls);
	}
	return str;
};

// 新增\修改 提交
function menuSaveFunction(url) {
	$('#menu-editForm').form('submit', {
		url : url + '/menu/save.do',
		success : function(result) {
			try {
				var r = $.parseJSON(result);
				if (r.success) {
					popInfo("menuInfo", r.message);
					if (r.type == 'insert') {
						$('#menu-data-list').treegrid('append', {
							parent : r.data.parentId,
							data : [ r.data ]
						});
					} else if (r.type == 'update') {
						$('#menu-data-list').treegrid('update', {
							id : r.data.id,
							row : r.data
						});
						$('#menu-data-list').treegrid('reload');
					}
				} else {
					popInfo("menuError", data.message);
				}
			} catch (e) {
				$.messager.alert('提示', e);
			}
		}
	});
}
function menuDeleteById(url, data) {
	$.post(url, data, function(result) {
		checkSession(result);
		r = eval(result);
		if (r.success) {
			$('#menu-data-list').treegrid('remove', data.id);
		}
		rollDown("imf_roll",r.message);
	}, "json");
}

formatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

function menuSaveById(url, data) {
	$.ajax(url + '?id=' + data.id, {
		type : 'get',
		dataType : 'json',
		data : {},
		success : function(result) {
			checkSession(result);
			$('#menu-editForm').form('load', result.data);
			if(result.data.newOpen){
				$(':radio[name="newOpen"]').eq(0).attr("checked",true); //设置第一个radio为选中
			}else{
				$(':radio[name="newOpen"]').eq(1).attr("checked",true); 
			}
			if(result.data.parentMenuId==-1){
				 $("#parentMenuId").combotree('disable');
			}else{ $("#parentMenuId").combotree('enable');}
			popWindow('menu-edit-win','menu-body');
		},
		error : function(response, textStatus, errorThrown) {

		},
		complete : function() {

		}
	});
}

function menuGetMaxOrder(url) {
	$.ajax(url, {
		type : 'get',
		dataType : 'json',
		data : null,
		success : function(result) {
			checkSession(result);
			var maxOrder = eval(result);
			var spinValue=new Number(maxOrder.data+1);
			var spinValueString=spinValue.toString();
			var spinStyle = {required:true,  
				     increment:1,
				     value:spinValueString,
			         min:1,
			         max:100,
			         onSpinUp:function(down){
			        	if(spinValue<=100){
			        		spinValue=spinValue+1;
			        		spinValueString=spinValue.toString();
			        		$('#sssmenuOrder').spinner("setValue",spinValueString);
			        	}
			        },
			         onSpinDown:function(down){
			        	 if(spinValue>0){
			        		spinValue=spinValue-1;
			        		spinValueString=spinValue.toString();
			        		$('#sssmenuOrder').spinner("setValue",spinValueString);
			        	}
			         }
			         };
		    $('#sssmenuOrder').spinner(spinStyle);

			},
		error : function(response, textStatus, errorThrown) {

		},
		complete : function() {

		}
	});
}

$(function() {
	function saveById(url, data) {
		$.ajax(url, {
			type : 'get',
			dataType : 'json',
			data : data,
			success : function(result) {
				checkSession(result);
				$('#editForm').form('load', result.data);
				$('#edit-win').dialog('open');
			},
			error : function(response, textStatus, errorThrown) {

			},
			complete : function() {

			}
		});
	}
	$('#menu-btnClose').click(function() {//关闭新增\修改窗口menu-btnClose
		$('#menu-edit-win').dialog('close');
	});
	$('#menuType').combobox({  
        onChange:function(newValue, oldValue){ 
        	if(newValue==0){
        		$("#divbuttonType").hide();
        	}else{
        		$("#divbuttonType").show();
        	}
        }
	}) ;
});

var iconData = [ {
	value : '',
	text : '默认'
}, {
	value : 'icon-add',
	text : 'icon-add'
}, {
	value : 'icon-edit',
	text : 'icon-edit'
}, {
	value : 'icon-remove',
	text : 'icon-remove'
}, {
	value : 'icon-save',
	text : 'icon-save'
}, {
	value : 'icon-cut',
	text : 'icon-cut'
}, {
	value : 'icon-ok',
	text : 'icon-ok'
}, {
	value : 'icon-no',
	text : 'icon-no'
}, {
	value : 'icon-cancel',
	text : 'icon-cancel'
}, {
	value : 'icon-reload',
	text : 'icon-reload'
}, {
	value : 'icon-search',
	text : 'icon-search'
}, {
	value : 'icon-print',
	text : 'icon-print'
}, {
	value : 'icon-help',
	text : 'icon-help'
}, {
	value : 'icon-undo',
	text : 'icon-undo'
}, {
	value : 'icon-redo',
	text : 'icon-redo'
}, {
	value : 'icon-back',
	text : 'icon-back'
}, {
	value : 'icon-sum',
	text : 'icon-sum'
}, {
	value : 'icon-tip',
	text : 'icon-tip'
}, {
	value : 'icon-acf-add',
	text : 'icon-acf-add'
}, {
	value : 'icon-acf-edit',
	text : 'icon-acf-edit'
}, {
	value : 'icon-acf-delete',
	text : 'icon-acf-delete'
/*}, {
	value : 'icon-acf-save',
	text : 'icon-acf-save'
}, {
	value : 'icon-acf-cut',
	text : 'icon-acf-cut'
}, {
	value : 'icon-acf-ok',
	text : 'icon-acf-ok'
}, {
	value : 'icon-acf-no',
	text : 'icon-acf-no'
}, {
	value : 'icon-acf-cancel',
	text : 'icon-acf-cancel'
}, {
	value : 'icon-acf-reload',
	text : 'icon-acf-reload'
}, {
	value : 'icon-acf-search',
	text : 'icon-acf-search'
}, {
	value : 'icon-acf-print',
	text : 'icon-acf-print'
}, {
	value : 'icon-acf-help',
	text : 'icon-acf-help'
}, {
	value : 'icon-acf-undo',
	text : 'icon-acf-undo'
}, {
	value : 'icon-acf-redo',
	text : 'icon-acf-redo'
}, {
	value : 'icon-acf-back',
	text : 'icon-acf-back'
}, {
	value : 'icon-acf-sum',
	text : 'icon-acf-sum'
}, {
	value : 'icon-acf-tip',
	text : 'icon-acf-tip'*/
} ];

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
})