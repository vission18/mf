var oldParentId = '';
function dataGrid(baseUrl,buttons,type) {
	var initToolbar=[ {
						text : '新增',
						buttonType:'add',
						iconCls : 'icon-add',
						handler : function() {
							$('#upbranchNo-add-tree').combotree({
								url : baseUrl + '/branch/firstTreeNode.do',
								onBeforeExpand : function(node) {
									$('#upbranchNo-add-tree').combotree("tree").tree(
											"options").url = baseUrl
											+ '/branch/getChildNodeForTree.do?parentId='
											+ node.id;
								}
							});
							popWindow('branch-add-win','branch-layout');//打开
							$('#branch-addForm').form('clear');
							$('#add-leaf').val(1);
						}
					},
					{
						text : '修改',
						buttonType:'edit',
						iconCls : 'icon-edit',
						handler : function() {
							var records = $('#branch-data-list').treegrid('getSelected');
							if (records != null) {
								getBranchById(baseUrl,records);
								oldParentId=records.parentId;
								popWindow('branch-edit-win','branch-layout');//打开
							} else {
								$.messager.alert('提示','请选择一条记录.');
							}
						}

					}, {
						text : '删除',
						buttonType:'drop',
						iconCls : 'icon-remove',
						handler : function() {
							var records = $('#branch-data-list').treegrid('getSelected');
							if (records != null) {
								$("#popConfirmYes").unbind('click');
								popConfirm('确认删除机构['+records.text+']？','branch-layout');
								$("#popConfirmYes").click(function (){
									branchDeleteById(baseUrl+'/branch/deleteById.do',records);
								});
							} else {
								$.messager.alert('提示','请选择一条记录.');
							}
						}
					} ];
	var initTreeGrid={
			fit : true,
			fitColumns : false,
			iconCls : 'icon-save',
			nowrap : false,
			rownumbers : true,
			collapsible : true,
			url : baseUrl+'/branch/firstTreeNode.do',
			idField : 'id',
			treeField : 'text',
			autoRowHeight:false,
			columns : [ [ {
				field : 'checkboxId',
				title : '',
				checkbox : true
			}, {
				field : 'id',
				title : '机构代码',
				width : 100
			}, {
				field : 'text',
				title : '机构名称',
				width : 300
			}, {
				field : 'branchShortName',
				title : '机构简称',
				width : 300
			}] ],
			onBeforeExpand : function(row) {
				if (!row.loaded) {
					$('#branch-data-list').treegrid("options").url = baseUrl+ '/branch/getChildNodeForTree.do?parentId='+ row.id;
					row.loaded = true;
				}
			},
			onContextMenu : function(e, row) {
				e.preventDefault();
				$(this).treegrid('unselectAll');
				$(this).treegrid('select', row.id);
				$('#branch-data-list').menu('show', {
					left : e.pageX,
					top : e.pageY
				});
			},
			onDblClickRow : function(row) {
				getBranchById(baseUrl,row);
				oldParentId=row.parentId;
				//$('#upbranchNo-tree').combotree('reload',baseUrl + '/branch/firstTreeNode.do');
				popWindow('branch-edit-win','branch-layout');//打开
			}
	};
	var newBars=getAccessButton(initToolbar,buttons,type);
	if(newBars.length>0){
		initTreeGrid['toolbar']=newBars;
	};
	$('#branch-data-list').treegrid(initTreeGrid);
}

function getBranchById(baseUrl, data) {
	$('#upbranchNo-tree').combotree({
		url : baseUrl + '/branch/firstTreeNode.do',
		onBeforeExpand : function(node) {
			$('#upbranchNo-tree').combotree("tree").tree(
					"options").url = baseUrl
					+ '/branch/getChildNodeForTree.do?parentId='
					+ node.id;
		}
	});
	
	$.ajax(baseUrl+'/branch/getById.do', {
		type : 'get',
		dataType : 'json',
		data : data,
		success : function(result) {
			checkSession(result);
			$('#branch-editForm').form('load', result.data);
			$('#branchNo-Text').val(result.data.branchNo);
		}
	});
}

// 修改提交
function saveFunction(baseUrl) {
	var selectedNode = $('#upbranchNo-tree').combotree('tree').tree(
			'getSelected');
	if(selectedNode!=null){
		$('#upBranchNo').val(selectedNode.id);
	}
	 $('#branch-editForm').form('submit',{
			url : baseUrl+'/branch/update.do' + '?oldparentId=' + oldParentId,
			success : function(result) {
				try {
					var r = $.parseJSON(result);
					if (r.success) {
						var parentId = r.data.upBranchNo;
						if(parentId!=oldParentId){
							if(parentId!='-1'){
								//父节点改变，找到父节点,append，未找到：不处理
								var parentNode = $('#branch-data-list').treegrid(
										'find', parentId);
								//原有节点删除
								$('#branch-data-list').treegrid('remove',r.data.branchNo);
								if (parentNode != null && (parentNode.loaded||parentNode.state=='open')) {
									$('#branch-data-list').treegrid('append', {
										parent : parentId, 
										data : [ {
											id : r.data.branchNo,
											text : r.data.branchName,
											branchNo: r.data.branchNo,
											state:'closed',
											branchShortName:r.data.branchShortName,
											parentId : parentId,
											branchType:r.data.branchType,
											upBranchNo:r.data.upBranchNo,
											branchType:r.data.branchType
											
										} ]
									});
									parentNode.leaf = false;
								}
							}else{
								//父节点改变，找到父节点,append，未找到：不处理
								var parentNode = $('#branch-data-list').treegrid(
										'find', parentId);
								//原有节点删除
								$('#branch-data-list').treegrid('remove',r.data.branchNo);
								$('#branch-data-list').treegrid('append', {
									data : [ {
										id : r.data.branchNo,
										text : r.data.branchName,
										branchNo: r.data.branchNo,
										state:'closed',
										branchShortName:r.data.branchShortName,
										parentId : parentId,
										branchType:r.data.branchType,
										upBranchNo:r.data.upBranchNo,
										branchType:r.data.branchType
										
									} ]
								});
							}
						}else{
								var node=$('#branch-data-list').treegrid('find',r.data.branchNo);
								node.text=r.data.branchName;
								node.branchShortName=r.data.branchShortName;
								$('#branch-data-list').treegrid('refresh',r.data.branchNo);
						}
						popInfo("branchEditInfo",r.message);
					}else{
						popInfo("branchEditError",r.message);
					}
				} catch (e) {
					popInfo("branchEditError",result);
				}
			}
		});
	 }

//新增提交
function insertFunction(baseUrl) {
	$('#branch-addForm').form('submit',{
		url : baseUrl + '/branch/save.do',
		success : function(result) {
			try {
				var r = $.parseJSON(result);
				if (r.success) {
					var parentId = r.data.upBranchNo;
					var parentNode = $('#branch-data-list').treegrid(
							'find', parentId);
					if (parentNode != null) {
						if(parentNode.state==='open'||parentNode.loaded){
							$('#branch-data-list').treegrid('append', {
								parent : parentId, 
								data : [ {
									id : r.data.branchNo,
									text : r.data.branchName,
									branchNo: r.data.branchNo,
									state:'closed',
									branchShortName:r.data.branchShortName,
									parentId : parentId,
									branchType:r.data.branchType,
									upBranchNo:r.data.upBranchNo,
									branchType:r.data.branchType
								} ]
							});
						}
						parentNode.leaf = false;
					}else if(parentId=='-1'){
						$('#branch-data-list').treegrid('append', {
							data : [ {
								id : r.data.branchNo,
								text : r.data.branchName,
								branchNo: r.data.branchNo,
								state:'closed',
								branchShortName:r.data.branchShortName,
								parentId : parentId,
								branchType:r.data.branchType,
								upBranchNo:r.data.upBranchNo,
								branchType:r.data.branchType
							} ]
						});
					}
					popInfo("branchAddInfo",r.message);
				}else{
					popInfo("branchAddError",r.message);
				}
			} catch (e) {
				popInfo("branchAddError",e);
			}
		}
	});
}

//删除选中记录
function branchDeleteById(url,data){
	$.post(url,data,
			function(result){
				checkSession(result);
				if(result.success){
					$('#branch-data-list').treegrid('remove', data.id);
					rollDown("imf_roll",result.message);
				}else{
					$.messager.alert('提示', result.message);
				}
			}, "json"
		);
}
//添加面板，关闭时删除信息
function addWinClosed(baseUrl,winId){
	$('#upbranchNo-add-tree').combotree('reload',baseUrl + '/branch/firstTreeNode.do');
	popClosed(winId);
}

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});