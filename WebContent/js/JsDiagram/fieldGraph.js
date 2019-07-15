//全局画布对象
var graph;
//初始化画布
//container-容器  outline-预览小窗  toolbar-工具栏  sidebar-左侧边栏  status-状态栏  objectId-加载对象id
function initGraph(container, outline, toolbar, sidebar, status, baseUrl, objectId)
{
	if (!mxClient.isBrowserSupported())
	{
		mxUtils.error('Browser is not supported!', 200, false);
	}
	else
	{
		mxConnectionHandler.prototype.movePreviewAway = false;
		mxClient.NO_FO = mxClient.NO_FO || mxClient.IS_GC || mxClient.IS_SF;
		mxGraphHandler.prototype.htmlPreview = true;
		mxConnectionHandler.prototype.moveIconFront = true;
		mxConnectionHandler.prototype.connectImage = new mxImage(Images.connectorImage, 16, 16);
		var keyImage = new Image();
		keyImage.src = Images.keyImage;

		var plusImage = new Image();
		plusImage.src = Images.plusImage;

		var checkImage = new Image();
		checkImage.src = Images.checkImage;
		
		mxConnectionHandler.prototype.getTargetPerimeterPoint = function(state, me)
		{
			var y = me.getY();
			if (this.currentRowNode != null)
			{
				y = getRowY(state, this.currentRowNode);
			}
			var x = state.x;
			if (this.previous.getCenterX() > state.getCenterX())
			{
				x += state.width;
			}
			return new mxPoint(x, y); 
		};
		mxConnectionHandler.prototype.getSourcePerimeterPoint = function(state, next, me)
		{
			var y = me.getY();
			if (this.sourceRowNode != null)
			{
				y = getRowY(state, this.sourceRowNode);
			}
			var x = state.x;
			if (next.x > state.getCenterX())
			{
				x += state.width;
			}
			return new mxPoint(x, y);
		};

		// 禁止连接无效行
		mxConnectionHandler.prototype.isValidTarget = function(cell)
		{
			return this.currentRowNode != null;
		};

		graph = new mxGraph(container);
		var editor = new mxEditor();
		var model = graph.model;
		
		editor.setGraphContainer(container);
		editor.layoutSwimlanes = true;
		editor.createSwimlaneLayout = function() {
			var layout = new mxStackLayout(this.graph, false);
			layout.fill = true;
			layout.resizeParent = true;

			layout.isVertexMovable = function(cell) {
				return true;
			};

			return layout;
		};

		
		//创建删除线条的方法.响应键盘上的delete键..并且在画布上删除当前被选择的对象.但不包括已经连接的对象.
		var keyHandler = new mxKeyHandler(graph);
		keyHandler.bindKey(46, function(evt)
		{
			if (graph.isEnabled())
			{
				graph.removeCells();
			}
		});
		
		graph.stylesheet.getDefaultVertexStyle()[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_TOP;
		graph.stylesheet.getDefaultVertexStyle()[mxConstants.STYLE_PERIMETER] =mxPerimeter.EntityPerimeter;
		graph.stylesheet.getDefaultVertexStyle()[mxConstants.STYLE_SHADOW] = true;
		graph.stylesheet.getDefaultVertexStyle()[mxConstants.STYLE_FILLCOLOR] = '#ffffff';
		graph.stylesheet.getDefaultVertexStyle()[mxConstants.STYLE_STROKECOLOR] = '#ffffff';
		delete graph.stylesheet.getDefaultVertexStyle()[mxConstants.STYLE_STROKECOLOR];
		graph.stylesheet.getDefaultVertexStyle()[mxConstants.STYLE_OVERFLOW] = 'fill';
		graph.stylesheet.getDefaultEdgeStyle()[mxConstants.STYLE_EDGE] =mxEdgeStyle.EntityRelation;
		graph.stylesheet.getDefaultEdgeStyle()[mxConstants.STYLE_STROKECOLOR] = 'black';
		graph.stylesheet.getDefaultEdgeStyle()[mxConstants.STYLE_FONTCOLOR] = 'black';
		
		graph.setCellsDisconnectable(false);
		graph.setAllowDanglingEdges(false);
		graph.setCellsEditable(false);
		graph.setConnectable(true);		
		
		//==========================================各种工具条上的方法和按钮============================================		
		// ==============为toolbar加上按钮==========================================================================================================
		var spacer = document.createElement('div');
		spacer.style.display = 'inline';
		spacer.style.padding = '8px';
		
		// ====================为status工具栏上上按钮===============================================================================================
		//addToolbarButton(editor, toolbar, 'collapseAll', '', Images.navMinusImage, true,'全部收起');
		//addToolbarButton(editor, toolbar, 'expandAll', '', Images.navPlusImage, true,'全部展开');
		//status.appendChild(spacer.cloneNode(true));
		//addToolbarButton(editor, toolbar, 'zoomIn', '', Images.zoomInImage, true,'放大');
		//addToolbarButton(editor, toolbar, 'zoomOut', '', Images.zoomOutImage, true,'缩小');
		//addToolbarButton(editor, toolbar, 'actualSize', '', Images.actualSizeImage, true,'100%大小');
		//addToolbarButton(editor, toolbar, 'fit', '', Images.fitToSizeImage, true,'适合大小');
		// ===================================================================================================================================

		if(Buttons.deleteButton){//删除按钮
			addToolbarButton(editor, toolbar, 'delete', '', Images.deleteImage, true,'删除');
		}
		if(Buttons.undoButton){//撤销按钮
			//addToolbarButton(editor, toolbar, 'undo', '', Images.undoImage, true,'撤销');
		}
		if(Buttons.redoButton){//重做按钮
			//addToolbarButton(editor, toolbar, 'redo', '', Images.redoImage, true,'重做');
		}
		if(Buttons.deleteButton || Buttons.undoButton || Buttons.redoButton){//分隔
			//toolbar.appendChild(spacer.cloneNode(true));
		}
		if(Buttons.imageButton){//以图片格式打开新窗口
			//addToolbarButton(editor, toolbar, 'show', '', Images.showImage, true,'图片');
		}
		if(Buttons.printButton){//打印
			//addToolbarButton(editor, toolbar, 'print', '', Images.printerImage, true,'打印');
		}
		if(Buttons.imageButton || Buttons.printButton){//分隔
			//toolbar.appendChild(spacer.cloneNode(true));
		}
		if(Buttons.resetButton){//重置
			//addToolbarButton(editor, toolbar, 'reset', '', Images.resetImage, true, '重置');
		}
		if(Buttons.saveButton){//保存
			addToolbarButton(editor, toolbar, 'save', '', Images.exportImage, true, '保存');
		}
		
		toolbar.appendChild(spacer.cloneNode(true));

		// ==============为创建的按钮加上动作============================================================================================================
		editor.addAction('save', function(editor, cell) {
			var enc = new mxCodec(mxUtils.createXmlDocument());
			var node = enc.encode(graph.getModel());
			var xml = mxUtils.getPrettyXml(node);
			saveXml(baseUrl, objectId, xml);
		});
		
		editor.addAction('delete', function(editor, cell) {
			if (graph.isEnabled())
			{
				graph.removeCells();
			}
		});
		
		editor.addAction('reset', function(editor, cell) {
			resetXml(baseUrl, objectId);
		});
			
		//================================================================================================================					

		// 收缩table
		graph.isCellFoldable = function(cell, collapse)
		{
			return this.getModel().isVertex(cell);
		};

		// 收缩table的判断
		graph.isCellConnectable = function(cell)
		{
			return !this.isCellCollapsed(cell);
		};
		
		// 使用HTML语言作为生成表格
		graph.setHtmlLabels(true);

		//================================================================================================
		//字段连线的关键代码
		graph.connectionHandler.updateRow = function(target)
		{
			while (target != null && target.nodeName != 'TR')
			{
				target = target.parentNode;
			}

			this.currentRow = null;

			if (target != null && target.parentNode.parentNode.className == 'erd')
			{
				var rowNumber = 0;
				var current = target.parentNode.firstChild;

				while (target != current && current != null)
				{
					current = current.nextSibling;
					rowNumber++;
				}

				this.currentRow = rowNumber + 1;
			}
			else
			{
				target = null;
			}
			
			return target;
		};
		
		// 连线的坐标选择.确保为每一个div都有连接的图标
		graph.connectionHandler.updateIcons = function(state, icons, me)
		{
			var target = me.getSource();
			target = this.updateRow(target);
			
			if (target != null && this.currentRow != null)
			{
				var div = target.parentNode.parentNode.parentNode;

				icons[0].node.style.visibility = 'visible';
				icons[0].bounds.x = state.x + target.offsetLeft + Math.min(state.width,
					target.offsetWidth) - this.icons[0].bounds.width - 2;
				icons[0].bounds.y = state.y + target.offsetTop + target.offsetHeight / 2 -
					this.icons[0].bounds.height / 2 - div.scrollTop + div.offsetTop;
				icons[0].redraw();

				this.currentRowNode = target;
			}
			else
			{
				icons[0].node.style.visibility = 'hidden';
			}
		};

		// 目标连接的选择点
		var oldMouseMove = graph.connectionHandler.mouseMove;
		graph.connectionHandler.mouseMove = function(sender, me)
		{
			if (this.edgeState != null)
			{
				this.currentRowNode = this.updateRow(me.getSource());
				
				if (this.currentRow != null)
				{
					this.edgeState.cell.value.setAttribute('targetRow', this.currentRow);
				}
				else
				{
					this.edgeState.cell.value.setAttribute('targetRow', '0');
				}
				
				this.destroyIcons();
			}
			
			oldMouseMove.apply(this, arguments);
		};

		// 连线坐标
		graph.connectionHandler.createEdgeState = function(me)
		{
			var relation = mxUtils.createXmlDocument().createElement('Relation');
			relation.setAttribute('sourceRow', this.currentRow || '0');
			relation.setAttribute('targetRow', '0');
			var edge = this.createEdge(relation);
			var style = this.graph.getCellStyle(edge);
			var state = new mxCellState(this.graph.view, edge, style);
			this.sourceRowNode = this.currentRowNode;
			
			return state;
		};

		//生成table
		graph.createTable  =function(tablename,prim,filed)
		{
			//生成表头
			var tablehead ='<table width="100%" border="0" cellpadding="0" class="title"><tr><th colspan="2">'+tablename+'</th></tr></table>';
			var tablediv = '<div style="overflow:auto;cursor:default;"><table width="100%" height="100%" border="0" cellpadding="0" class="erd">';
			//生成主键
			var tableprim='';
			//生成字段
			var tablefiled='';
			//生成表尾
			var tableend='';
			for(var i=0;i<prim.length;i++)
			{
				tableprim =tableprim + '<tr><td><img align="center" src="'+Images.keyImage+'"/></td><td><u>'+prim[i]+'</u></td></tr>';
			}
			
			for(var y=0;y<filed.length;y++)
			{
				tablefiled = tablefiled +'<tr><td></td><td>'+filed[y]+'</td></tr>';
			}
			tableend='</table></div>'
			tableHtml = tablehead+tablediv+tableprim+tablefiled+tableend;
			return tableHtml;    
		}

		/* 最后:清除加载中DIV */
		var splash = document.getElementById('splash-'+objectId);
		if (splash != null) {
			try {
				mxEvent.release(splash);
				mxEffects.fadeOut(splash, 100, true);
			} catch (e) {
				splash.parentNode.removeChild(splash);
			}
		}
	}
};





//保存xml内容
function saveXml(baseUrl, objectId, xml){
	$.post(baseUrl+'/imsGraph/save.do',{objId:objectId,objXml:xml},
		function(result){
			if (!result || result == null || result == "null") {
				alert("登录超时");
			}
			if(result.success){
				alert(result.message);
			}else{
				alert(result);
			}
		}, "json"
	);
}
//重置xml内容
function resetXml(baseUrl, objectId){
	if(confirm('确定要重置关系图吗?')){
		$.post(baseUrl+'/imsGraph/reset.do',{objId:objectId},
				function(result){
					if (!result || result == null || result == "null") {
						alert("登录超时");
					}
					if(result.success){
						document.location.reload();
					}else{
						alert(result);
					}
				}, "json"
		);		
	}
}
//添加工具栏按钮
function addToolbarButton(editor, toolbar, action, label, image,
		isTransparent, altText) {
	var button = document.createElement('button');
	button.style.fontSize = '10';
	if (image != null) {
		var img = document.createElement('img');
		img.setAttribute('src', image);
		img.style.width = '32px';
		img.style.height = '32px';
		img.style.verticalAlign = 'middle';
		img.style.marginRight = '2px';
		img.setAttribute('title', altText);
		button.appendChild(img);
	}
	if (isTransparent) {
		button.style.background = 'transparent';
		button.style.color = '#FFFFFF';
		button.style.border = 'none';
	}
	mxEvent.addListener(button, 'click', function(evt) {editor.execute(action);
	});
	mxUtils.write(button, label);
	toolbar.appendChild(button);
};
//========================配置画布的颜色属性=================================================================
function configureStylesheet(graph) {
	var style = new Object();
	style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
	style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
	style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_LEFT;
	style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_MIDDLE;
	style[mxConstants.STYLE_FONTCOLOR] = '#000000';
	style[mxConstants.STYLE_FONTSIZE] = '11';
	style[mxConstants.STYLE_FONTSTYLE] = 0;
	style[mxConstants.STYLE_SPACING_LEFT] = '4';
	style[mxConstants.STYLE_IMAGE_WIDTH] = '48';
	style[mxConstants.STYLE_IMAGE_HEIGHT] = '48';
	graph.getStylesheet().putDefaultVertexStyle(style);
			
	style = new Object();
	style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
	style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
	style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
	style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_MIDDLE;
	style[mxConstants.STYLE_GRADIENTCOLOR] = '#41B9F5';
	style[mxConstants.STYLE_FILLCOLOR] = '#8CCDF5';
	style[mxConstants.STYLE_STROKECOLOR] = '#1B78C8';
	style[mxConstants.STYLE_FONTCOLOR] = '#FFFFFF';
	style[mxConstants.STYLE_ROUNDED] = true;
	style[mxConstants.STYLE_OPACITY] = '80';
	style[mxConstants.STYLE_FONTSIZE] = '12';
	style[mxConstants.STYLE_FONTSTYLE] = 0;
	style[mxConstants.STYLE_IMAGE_WIDTH] = '48';
	style[mxConstants.STYLE_IMAGE_HEIGHT] = '48';
	graph.getStylesheet().putCellStyle('ver', style);		
			
	style = new Object();
	style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_SWIMLANE;
	style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
	style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
	style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_TOP;
	style[mxConstants.STYLE_GRADIENTCOLOR] = '#41B9F5';
	style[mxConstants.STYLE_FILLCOLOR] = '#8CCDF5';
	style[mxConstants.STYLE_STROKECOLOR] = '#1B78C8';
	style[mxConstants.STYLE_FONTCOLOR] = '#000000';
	style[mxConstants.STYLE_STROKEWIDTH] = '2';
	style[mxConstants.STYLE_STARTSIZE] = '28';
	style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
	style[mxConstants.STYLE_FONTSIZE] = '12';
	style[mxConstants.STYLE_FONTSTYLE] = 1;
	style[mxConstants.STYLE_IMAGE] = Images.tableImage;
	style[mxConstants.STYLE_SHADOW] = 1;
	graph.getStylesheet().putCellStyle('table', style);

	style = graph.stylesheet.getDefaultEdgeStyle();
	style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = '#FFFFFF';
	style[mxConstants.STYLE_STROKEWIDTH] = '2';
	style[mxConstants.STYLE_ROUNDED] = true;
	style[mxConstants.STYLE_EDGE] = mxEdgeStyle.EntityRelation;
};

//=====================================================================================================		
//===================创建画图面板的右键菜单===============================================================================================		
function createPopupMenu(editor, graph, menu, cell, evt) {
	if (cell != null) {
		if (graph.isHtmlLabel(cell)) {
			menu.addItem('Properties', Images.nodeImage, function() {//属性
				
			});
		}
	}
	menu.addItem('Delete', Images.undoImage, function() {editor.execute('undo', cell);});//删除
	//加入一个横线
	//menu.addSeparator();
};		

//创建一个表对象
function Table(name) {
	this.name = name;
};
//创建一个行的属性
function Column(name) {
	this.name = name;
};
Column.prototype.clone = function() {
	return mxUtils.clone(this);
};
Table.prototype.clone = function() {
	return mxUtils.clone(this);
};
//从servlet,加载xml
function loadGraph(id, type, baseUrl){
	graph.getModel().beginUpdate();
	var xmlDoc = mxUtils.load(baseUrl+"/graph?type="+type+"&id="+id).getXml();
	var node = xmlDoc.documentElement;

	createHtmlTable(baseUrl, node);
	
	var dec = new mxCodec(node.ownerDocument);
	dec.decode(node, graph.getModel());
	graph.getModel().endUpdate();
}

function createHtmlTable(baseUrl, node){
	var arrTableObj = new Array(); 
	$.each(node.getElementsByTagName("mxCell"),function(i,cell){
		if(cell.getAttribute("source")){//线
			
		}else{//表
			id = cell.getAttribute("id");
			if(id&&id!='0'&&id!='1'){
				arrTableObj.push(addHtmlTable(baseUrl, id));
			}			
		}
	});
	
	graph.getLabel = function(cell)
	{
		if (this.getModel().isVertex(cell))
		{
			if (this.isCellCollapsed(cell))
			{
				tableName = '';
				$.each(arrTableObj,function(i, tableObj){
					if(cell.id===tableObj.id){
						tableName = tableObj.name;
					}
				});
				return '<table width="100%" border="0" cellpadding="0" class="title">' +
					'<tr><th colspan="2">'+ tableName + '</th></tr>' + '</table>';
			}
			else
			{
				retObj = null;
				$.each(arrTableObj,function(i, tableObj){
					if(cell.id===tableObj.id){
						retObj = graph.createTable(tableObj.name,tableObj.prim,tableObj.field);
						return false;
					}
				});
				return retObj;
			}
		}
		else
		{
			return '';
		}
	};	

	// 建立多条连线.但是多条连线不能重合
	mxGraphView.prototype.updateFloatingTerminalPoint = function(edge, start, end, source)
	{
		var next = this.getNextPoint(edge, end, source);
		var div = start.text.node.getElementsByTagName('div')[0];
		
		var x = start.x;
		var y = start.getCenterY();
	
		if (next.x > x + start.width / 2)
		{
			x += start.width;
		}
	
		if (div != null)
		{
			y = start.getCenterY() - div.scrollTop;
			
			if (mxUtils.isNode(edge.cell.value) && !this.graph.isCellCollapsed(start.cell))
			{
				var attr = (source) ? "sourceRow" : "targetRow";
				var row = parseInt(edge.cell.value.getAttribute(attr));
		
				var table = div.getElementsByTagName('table')[0];
				var trs = table.getElementsByTagName('tr');
				var tr = trs[Math.min(trs.length - 1, row - 1)];
				
				if (tr != null)
				{
					y = getRowY(start, tr);
				}
			}
		
			y = Math.min(start.y + start.height, Math.max(start.y + div.offsetTop, y));
			
			if (edge != null && edge.absolutePoints != null)
			{
				next.y = y;
			}
		}
	
		edge.setAbsoluteTerminalPoint(new mxPoint(x, y), source);
		
		if (source && mxUtils.isNode(edge.cell.value) && start != null && end != null)
		{
			var edges = this.graph.getEdgesBetween(start.cell, end.cell, true);
			var tmp = [];
	
			var row = edge.cell.value.getAttribute('targetRow');
			
			for (var i = 0; i < edges.length; i++)
			{
				if (mxUtils.isNode(edges[i].value) &&
					edges[i].value.getAttribute('targetRow') == row)
				{
					tmp.push(edges[i]);
				}
			}
		}
	};

	//定义一个Y点的坐标.用来取得每一个tr的位置左边.用语多条连线不至于相互重合
	var getRowY = function(state, tr)
	{
		var div = tr.parentNode.parentNode.parentNode;
		var y = state.y + tr.offsetTop + tr.offsetHeight / 2 - div.scrollTop + div.offsetTop;
		y = Math.min(state.y + state.height, Math.max(state.y + div.offsetTop, y));

		return y;
	};
	
}

function addHtmlTable(baseUrl, tableId){
	var tableObj = {id:null,name:null,prim:null,field:null};
	$.ajax(baseUrl + "/tableRelGraph/getTableAndFieldsByTableId.do?tableId="+tableId,{//获取表字段列表
		dataType : 'json',
		async:false,
		success : function(result) {
			table = result.data;
			tableObj.id = table.tableId;
			tableObj.name = table.tableCname;
			tableObj.prim = new Array();
			tableObj.field = new Array();
			for(var i=table.fields.length-1;i>-1;i--){//返回数据是倒序排列,所以这里必须再倒序取数,以变成正序
				f = table.fields[i];
				if(f.keyFlag){
					tableObj.prim.push(f.fieldCname);
				}else{
					tableObj.field.push(f.fieldCname);
				}
			}
			graph.createTable(tableObj.name,tableObj.prim,tableObj.field);
		}
	});
	return tableObj;
}


//添加表
function addTableToGraph(baseUrl, tableId, tableName){
	graph.stopEditing(false);
	
	var tableObject = new Table('TABLENAME');
	var table = new mxCell(tableObject, new mxGeometry(0, 0, 200, 28), 'table');
	var parent = graph.getDefaultParent();
	var model = graph.getModel();
	
	model.beginUpdate();
	try {
		table.value.name = tableName;
		table.setId(tableId);
		table.geometry.x = 10;
		table.geometry.y = 10;
		table.setVertex(true);
		graph.addCell(table, parent);
		table.geometry.alternateBounds = new mxRectangle(0, 0, table.geometry.width, table.geometry.height);

		$.ajax(baseUrl + "/tableRelGraph/getFieldsByTableId.do?tableId="+tableId,{//获取表字段列表
			dataType : 'json',
			async:false,
			success : function(result) {
				$.each(result.data,function(i, data) {
					addColumn(table, data);
				});
			}
		});
	} finally {
		model.endUpdate();
	}
	graph.setSelectionCell(table);
}
//添加列
function addColumn(table, data){
	var columnObject = new Column('COLUMNNAME');
	var column = new mxCell(columnObject, new mxGeometry(0, 0, 0,26), 'field');
	column.setVertex(true);
	column.setConnectable(false);
	column.setId(data.fieldId);
	column.setParent(table);//无效啊。。。
	column.value.name = data.fieldCname;
	column.value.type = data.fieldType;
	column.value.primaryKey = data.keyFlag;
	column.value.autoIncrement = false;
	table.insert(column);
}
