function helpFileDataGrid(baseUrl){
	$('#helpfile-data-list').datagrid({
		url : baseUrl + '/helpfile/findAll.do',
		fit : true,
		fitColumns : false,
		border : true,
		singleSelect : true,
		nowrap : false,
		striped : true,// 奇偶行不同颜色
		columns : [[{
			field : 'type',
			title : '文档名称',
			width : 400,
			formatter : function(value, row, index){
				value = stylePath + '/images/helpfile/' + row.fileIcon;
				return '<div><img class="item-img" align="left" src="'+ value +'" style="display:inline-block;vertical-align:middle;margin-right:5px;"/><span class="item-text">' + row.fileName + '</span></div>';
			}
		},{
			field : 'fileSize',
			title : '文档大小',
			width : 80,
			formatter : function(value, row, index){
				return row.strFileSize;
			}
		},{
			field : ' ',//此处为“”则不显示
			title : '操作',
			width : 80,
			formatter : function(value, row, index){
				href = baseUrl + '/helpfile/download.do?fileName=' + encodeURIComponent(row.fileName);
				return '<span><a href="'+ href +'">下载</a></span>';
			}
		}]]
	});
};

$(document).ready(function () {
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});