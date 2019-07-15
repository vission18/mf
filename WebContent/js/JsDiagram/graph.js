//全局画布对象
var graph;
var Images;
var Buttons;
//初始化画布
//container-容器  outline-预览小窗  toolbar-工具栏  sidebar-左侧边栏  status-状态栏  objectId-加载对象id
function initGraph(container, outline, toolbar, sidebar, status, baseUrl, objectId, ViewButtons, stylePath) {
	var popupMenu = false;
	Buttons = ViewButtons;
	Images = {
			keyImage : stylePath + "/js/JsDiagram/images/key.png",
			plusImage : stylePath + "/js/JsDiagram/images/plus.png",
			checkImage : stylePath + "/js/JsDiagram/images/check.png",
			connectorImage : stylePath + "/js/JsDiagram/images/connector.gif",
			spacerImage : stylePath + "/js/JsDiagram/images/spacer.gif",
			tableImage : stylePath + "/js/JsDiagram/images/icons48/table.png",
			nodeImage : stylePath + "/js/JsDiagram/images/properties.gif",
			undoImage : stylePath + "/js/JsDiagram/images/left.png",
			redoImage : stylePath + "/js/JsDiagram/images/right.png",
			showImage : stylePath + "/js/JsDiagram/images/toImage.png",
			printerImage : stylePath + "/js/JsDiagram/images/print.png",
			navMinusImage : stylePath + "/js/JsDiagram/images/navigate_minusz.png",
			navPlusImage : stylePath + "/js/JsDiagram/images/navigate_plusz.png",
			zoomInImage : stylePath + "/js/JsDiagram/images/zoom_in32z.png",
			zoomOutImage : stylePath + "/js/JsDiagram/images/zoom_out32z.png",
			actualSizeImage : stylePath + "/js/JsDiagram/images/view_1_132z.png",
			fitToSizeImage : stylePath + "/js/JsDiagram/images/screen.png",
			deleteImage : stylePath + "/js/JsDiagram/images/delete2.png",
			exportImage : stylePath + "/js/JsDiagram/images/save.png",
			resetImage : stylePath + "/js/JsDiagram/images/reset.png"
		}
	
	if (!mxClient.isBrowserSupported()) {
		//检查浏览器
		mxUtils.error('Browser is not supported!', 200, false);
	} else {
		mxConstants.SHADOW_OPACITY = 0.5;
		mxConstants.SHADOWCOLOR = '#C0C0C0';
		mxConstants.SHADOW_OFFSET_X = 5;
		mxConstants.SHADOW_OFFSET_Y = 6;

		mxSwimlane.prototype.imageSize = 20;
		mxSwimlane.prototype.imageDx = 16;
		mxSwimlane.prototype.imageDy = 4;

		mxSwimlaneCreateSvg = mxSwimlane.prototype.createSvg;
		mxSwimlane.prototype.createSvg = function() {
			var node = mxSwimlaneCreateSvg.apply(this, arguments);
			this.content.setAttribute('fill', '#FFFFFF');
			return node;
		};

		mxSwimlaneReconfigure = mxSwimlane.prototype.reconfigure;
		mxSwimlane.prototype.reconfigure = function(node) {
			mxSwimlaneReconfigure.apply(this, arguments);

			if (this.dialect == mxConstants.DIALECT_SVG
					&& this.shadowNode != null) {
				this.shadowNode.setAttribute('height', this.bounds.height);
			}
		};

		mxSwimlaneRedrawSvg = mxSwimlane.prototype.redrawSvg;
		mxSwimlane.prototype.redrawSvg = function() {
			mxSwimlaneRedrawSvg.apply(this, arguments);

			if (this.dialect == mxConstants.DIALECT_SVG
					&& this.shadowNode != null) {
				this.shadowNode.setAttribute('height', this.bounds.height);
			}

			if (this.imageNode != null) {
				this.imageNode.setAttribute('x', this.bounds.x + this.imageDx
						* this.scale);
				this.imageNode.setAttribute('y', this.bounds.y + this.imageDy
						* this.scale);
			}
		};

		mxSwimlaneRedrawVml = mxSwimlane.prototype.redrawVml;
		mxSwimlane.prototype.redrawVml = function() {
			mxSwimlaneRedrawVml.apply(this, arguments);

			if (this.imageNode != null) {
				this.imageNode.style.left = Math.floor(this.imageDx
						* this.scale)
						+ 'px';
				this.imageNode.style.top = Math
						.floor(this.imageDy * this.scale)
						+ 'px';
			}
		};

		mxSwimlaneCreateVml = mxSwimlane.prototype.createVml;
		mxSwimlane.prototype.createVml = function() {

			this.isShadow = false;
			var node = mxSwimlaneCreateVml.apply(this, arguments);
			this.shadowNode = document.createElement('v:rect');

			this.shadowNode.style.left = mxConstants.SHADOW_OFFSET_X + 'px';
			this.shadowNode.style.top = mxConstants.SHADOW_OFFSET_Y + 'px';
			this.shadowNode.style.width = '100%'
			this.shadowNode.style.height = '100%'

			var fillNode = document.createElement('v:fill');
			this.updateVmlFill(fillNode, mxConstants.SHADOWCOLOR, null, null,
					mxConstants.SHADOW_OPACITY * 100);
			this.shadowNode.appendChild(fillNode);

			this.shadowNode
					.setAttribute('strokecolor', mxConstants.SHADOWCOLOR);
			this.shadowNode.setAttribute('strokeweight',
					(this.strokewidth * this.scale) + 'px');

			var strokeNode = document.createElement('v:stroke');
			strokeNode.setAttribute('opacity',
					(mxConstants.SHADOW_OPACITY * 100) + '%');
			this.shadowNode.appendChild(strokeNode);

			node.insertBefore(this.shadowNode, node.firstChild);

			this.content.setAttribute('fillcolor', 'white');
			this.content.setAttribute('filled', 'true');

			if (this.opacity != null) {
				var contentFillNode = document.createElement('v:fill');
				contentFillNode.setAttribute('opacity', this.opacity + '%');
				this.content.appendChild(contentFillNode);
			}

			return node;
		};

		mxConnectionHandler.prototype.connectImage = new mxImage(Images.connectorImage, 16, 16);

		if (mxClient.IS_QUIRKS) {
			document.body.style.overflow = 'hidden';
			new mxDivResizer(container);
			new mxDivResizer(outline);
			new mxDivResizer(toolbar);
			new mxDivResizer(sidebar);
			new mxDivResizer(status);
		}

		var editor = new mxEditor();
		graph = editor.graph;
		var model = graph.model;

		graph.setConnectable(true);
		graph.setCellsDisconnectable(false);
		graph.setCellsCloneable(false);
		graph.swimlaneNesting = false;
		graph.dropEnabled = true;

		graph.setAllowDanglingEdges(false);

		graph.connectionHandler.factoryMethod = null;

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

		// 加入字段的分栏效果和关键字效果
		graph.isHtmlLabel = function(cell) {
			return !this.isSwimlane(cell) && !this.model.isEdge(cell);
		};

		// 转化表头的Object对象为字段名
		graph.convertValueToString = function(cell) {
			if (cell.value != null && cell.value.name != null) {
				return cell.value.name;
			}
			return mxGraph.prototype.convertValueToString.apply(this, arguments);
		};
		// cell.value instanceof Object
		// ====创建表格的颜色效果====================================
		graph.getLabel = function(cell) {
			if (this.isHtmlLabel(cell)) {
				var label = '';

				if (cell.value == null) {

				} else {
					if (cell.value.primaryKey) {

						label += '<img title="Primary Key" src="'
								+ Images.keyImage
								+ '" width="16" height="16" align="top">&nbsp;';
					} else {
						label += '<img src="' 
								+ Images.spacerImage
								+ '" width="16" height="1">&nbsp;';
					}

					if (cell.value.autoIncrement) {
						label += '<img title="Auto Increment" src="'
								+ Images.plusImage
								+ '" width="16" height="16" align="top">&nbsp;';
					} else if (cell.value.unique) {
						label += '<img title="Unique" src="'
								+ Images.checkImage
								+ '" width="16" height="16" align="top">&nbsp;';
					} 
					
					if(cell.value.type==null||cell.value.type==''){
						return label + mxUtils.htmlEntities(cell.value.name, false);
					}else{
						return label + mxUtils.htmlEntities(cell.value.name, false)
							+ ': '
							+ mxUtils.htmlEntities(cell.value.type, false);
					}
				}
			}
			return mxGraph.prototype.getLabel.apply(this, arguments);
		};

		// =========================创建删除CELL的方法======================================================================
		graph.addListener(mxEvent.REMOVE_CELLS, function(sender, evt) {
			var cells = evt.getProperty('cells');
			for ( var i = 0; i < cells.length; i++) {
				var cell = cells[i];
				if (this.model.isEdge(cell)) {
					var terminal = this.model.getTerminal(cell, true);
					var parent = this.model.getParent(terminal);
					this.model.remove(terminal);
				}
			}
		});

		// 关闭直接连线的功能=====================================================
		graph.isValidDropTarget = function(cell, cells, evt) {
			return this.isSwimlane(cell);
		};
		// ===========创建右键菜单======================================================
		graph.panningHandler.factoryMethod = function(menu, cell, evt) {
			if(popupMenu){
				createPopupMenu(editor, graph, menu, cell, evt);
			}
		};
		// 加载所有配置好的样式到画板中======
		var styleName = "blue";
		if (getCookie('imfImsStyleName')) {
			styleName = getCookie('imfImsStyleName');
		}
		configureStylesheet(graph,styleName);
		
		// ==============为toolbar加上按钮==========================================================================================================
		var spacer = document.createElement('div');
		spacer.style.display = 'inline';
		spacer.style.padding = '8px';
		
		// ====================为status工具栏上上按钮===============================================================================================
		addToolbarButton(editor, toolbar, 'collapseAll', '', Images.navMinusImage, true,'全部收起');
		addToolbarButton(editor, toolbar, 'expandAll', '', Images.navPlusImage, true,'全部展开');
		status.appendChild(spacer.cloneNode(true));
		addToolbarButton(editor, toolbar, 'zoomIn', '', Images.zoomInImage, true,'放大');
		addToolbarButton(editor, toolbar, 'zoomOut', '', Images.zoomOutImage, true,'缩小');
		addToolbarButton(editor, toolbar, 'actualSize', '', Images.actualSizeImage, true,'100%大小');
		
		if(Buttons.fitToSizeImage){
			addToolbarButton(editor, toolbar, 'fit', '', Images.fitToSizeImage, true,'适合大小');
		}
		// ===================================================================================================================================

		if(Buttons.deleteButton){//删除按钮
			addToolbarButton(editor, toolbar, 'delete', '', Images.deleteImage, true,'删除');
		}
		if(Buttons.undoButton){//撤销按钮
			addToolbarButton(editor, toolbar, 'undo', '', Images.undoImage, true,'撤销');
		}
		if(Buttons.redoButton){//重做按钮
			addToolbarButton(editor, toolbar, 'redo', '', Images.redoImage, true,'重做');
		}
		if(Buttons.deleteButton || Buttons.undoButton || Buttons.redoButton){//分隔
			toolbar.appendChild(spacer.cloneNode(true));
		}
		if(Buttons.imageButton){//以图片格式打开新窗口
			addToolbarButton(editor, toolbar, 'show', '', Images.showImage, true,'图片');
		}
		if(Buttons.printButton){//打印
			addToolbarButton(editor, toolbar, 'print', '', Images.printerImage, true,'打印');
		}
		if(Buttons.imageButton || Buttons.printButton){//分隔
			toolbar.appendChild(spacer.cloneNode(true));
		}
		if(Buttons.resetButton){//重置
			addToolbarButton(editor, toolbar, 'reset', '', Images.resetImage, true, '重置');
		}
		if(Buttons.saveButton){//保存
			addToolbarButton(editor, toolbar, 'save', '', Images.exportImage, true, '保存');
		}
		
		toolbar.appendChild(spacer.cloneNode(true));

		// ==============为创建的按钮加上动作============================================================================================================
		editor.addAction('save', function(editor, cell) {
			var enc = new mxCodec(mxUtils.createXmlDocument());
			var node = enc.encode(editor.graph.getModel());
			var xml = mxUtils.getPrettyXml(node);
			saveXml(baseUrl, objectId, xml);
		});
		
		editor.addAction('reset', function(editor, cell) {
			resetXml(baseUrl, objectId);
		});

		// ================================================================================
		var outln = new mxOutline(graph, outline);
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
}
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
function configureStylesheet(graph, styleName) {
	mainColor = '#0791d8';
	if(styleName==='green'){
		mainColor = '#087f79';
	}else if(styleName==='red'){
		mainColor = '#b82123';
	}
	
	var style = new Object();
	style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
	style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
	style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_LEFT;
	style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_MIDDLE;
	style[mxConstants.STYLE_FONTCOLOR] = '#000000';
	style[mxConstants.STYLE_FONTSIZE] = '12';
	style[mxConstants.STYLE_FONTSTYLE] = 0;
	style[mxConstants.STYLE_SPACING_LEFT] = '6';
	style[mxConstants.STYLE_IMAGE_WIDTH] = '48';
	style[mxConstants.STYLE_IMAGE_HEIGHT] = '48';
	graph.getStylesheet().putDefaultVertexStyle(style);
			
	style = new Object();
	style[mxConstants.STYLE_SHAPE] = mxConstants.SHAPE_RECTANGLE;
	style[mxConstants.STYLE_PERIMETER] = mxPerimeter.RectanglePerimeter;
	style[mxConstants.STYLE_ALIGN] = mxConstants.ALIGN_CENTER;
	style[mxConstants.STYLE_VERTICAL_ALIGN] = mxConstants.ALIGN_MIDDLE;
	style[mxConstants.STYLE_GRADIENTCOLOR] = mainColor;
	style[mxConstants.STYLE_FILLCOLOR] = mainColor;
	style[mxConstants.STYLE_STROKECOLOR] = '#000000';
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
	style[mxConstants.STYLE_GRADIENTCOLOR] = mainColor;
	style[mxConstants.STYLE_FILLCOLOR] = mainColor;//表背景色
	style[mxConstants.STYLE_STROKECOLOR] = '#000000';
	style[mxConstants.STYLE_FONTCOLOR] = '#ffffff';
	style[mxConstants.STYLE_STROKEWIDTH] = '1';
	style[mxConstants.STYLE_STARTSIZE] = '28';
	style[mxConstants.STYLE_VERTICAL_ALIGN] = 'middle';
	style[mxConstants.STYLE_FONTSIZE] = '14';//表名字号
	style[mxConstants.STYLE_FONTSTYLE] = 1;
	style[mxConstants.STYLE_IMAGE] = Images.tableImage;
	style[mxConstants.STYLE_SHADOW] = 0;//是否显示阴影
	graph.getStylesheet().putCellStyle('table', style);
	
	style = new Object();
	style[mxConstants.STYLE_FILLCOLOR] = '#eee';//字段背景色
	style[mxConstants.STYLE_STROKECOLOR] = '#aaa';
	style[mxConstants.STYLE_STROKEWIDTH] = '1';
	graph.getStylesheet().putCellStyle('field', style);

	style = graph.stylesheet.getDefaultEdgeStyle();
	style[mxConstants.STYLE_LABEL_BACKGROUNDCOLOR] = '#FFFFFF';
	style[mxConstants.STYLE_STROKEWIDTH] = '1';//连线宽度
	style[mxConstants.STYLE_STROKECOLOR] = '#000000';//连线颜色
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
	var dec = new mxCodec(node.ownerDocument);
	dec.decode(node, graph.getModel());
	graph.getModel().endUpdate();
}
//添加表 
//fieldConnectable-是否允许字段连接 true or false
function addTableToGraph(baseUrl, tableId, tableName, fieldConnectable){
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
					addColumn(table, data, fieldConnectable);
				});
			}
		});
	} finally {
		model.endUpdate();
	}
	graph.setSelectionCell(table);
}
//添加列
function addColumn(table, data, fieldConnectable){
	var columnObject = new Column('COLUMNNAME');
	var column = new mxCell(columnObject, new mxGeometry(0, 0, 0,26), 'field');
	column.setVertex(true);
	column.setConnectable(fieldConnectable);
	column.setId(data.fieldId);
	column.setParent(table);//无效啊。。。
	column.value.name = data.fieldCname;
	column.value.type = data.fieldType;
	column.value.primaryKey = data.keyFlag;
	column.value.autoIncrement = false;
	table.insert(column);
}

function getCookie(name){
    var cname = name + "=";
    var dc = document.cookie;
    if (dc.length > 0){
        begin = dc.indexOf(cname);
        if (begin != -1){
            begin += cname.length;
            end = dc.indexOf(";", begin);
            if (end == -1){
                end = dc.length;
            }
            return unescape(dc.substring(begin, end));
        }
    }
    return null;
}