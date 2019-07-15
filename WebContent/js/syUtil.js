/**
 * 
 * @requires jQuery,EasyUI
 * 
 * panel关闭时回收内存，主要用于layout使用iframe嵌入网页时的内存泄漏问题
 */
$.fn.panel.defaults.onBeforeDestroy = function() {
	var frame = $('iframe', this);
	try {
		if (frame.length > 0) {
			for ( var i = 0; i < frame.length; i++) {
				frame[i].contentWindow.document.write('');
				frame[i].contentWindow.close();
			}
			frame.remove();
			if ($.browser.msie) {
				CollectGarbage();
			}
		}
	} catch (e) {
	}
};

/**
 * 使panel和datagrid在加载时提示
 * 
 * 
 * 
 * @requires jQuery,EasyUI
 * 
 */
$.fn.panel.defaults.loadingMessage = '加载中...';
$.fn.datagrid.defaults.loadMsg = '加载中...';

/**
 * 
 * 
 * @requires jQuery,EasyUI
 * 
 * 通用错误提示
 * 
 * 用于datagrid/treegrid/tree/combogrid/combobox/form加载数据出错时的操作
 */
var easyuiErrorFunction = function(XMLHttpRequest) {
	$.messager.progress('close');
	$.messager.alert('错误', XMLHttpRequest.responseText);
};
$.fn.datagrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.treegrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.tree.defaults.onLoadError = easyuiErrorFunction;
$.fn.combogrid.defaults.onLoadError = easyuiErrorFunction;
$.fn.combobox.defaults.onLoadError = easyuiErrorFunction;
$.fn.form.defaults.onLoadError = easyuiErrorFunction;

/**
 * 
 * 
 * @requires jQuery,EasyUI
 * 
 * 为datagrid、treegrid新增表头菜单，用于显示或隐藏列，注意：冻结列不在此菜单中
 */
var createGridHeaderContextMenu = function(e, field) {
	e.preventDefault();
	var grid = $(this);/* grid本身 */
	var headerContextMenu = this.headerContextMenu;/* grid上的列头菜单对象 */
	if (!headerContextMenu) {
		var tmenu = $('<div style="width:200px;"></div>').appendTo('body');
		var fields = grid.datagrid('getColumnFields');
		for ( var i = 0; i < fields.length; i++) {
			var fildOption = grid.datagrid('getColumnOption', fields[i]);
			if (!fildOption.hidden) {
				$('<div iconCls="icon-ok" field="' + fields[i] + '"/>').html(
						fildOption.title).appendTo(tmenu);
			} else {
				$('<div iconCls="icon-empty" field="' + fields[i] + '"/>')
						.html(fildOption.title).appendTo(tmenu);
			}
		}
		headerContextMenu = this.headerContextMenu = tmenu.menu({
			onClick : function(item) {
				var field = $(item.target).attr('field');
				if (item.iconCls == 'icon-ok') {
					grid.datagrid('hideColumn', field);
					$(this).menu('setIcon', {
						target : item.target,
						iconCls : 'icon-empty'
					});
				} else {
					grid.datagrid('showColumn', field);
					$(this).menu('setIcon', {
						target : item.target,
						iconCls : 'icon-ok'
					});
				}
			}
		});
	}
	headerContextMenu.menu('show', {
		left : e.pageX,
		top : e.pageY
	});
};
$.fn.datagrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;
$.fn.treegrid.defaults.onHeaderContextMenu = createGridHeaderContextMenu;

/**
 * 
 * 
 * @requires jQuery,EasyUI
 * 
 * 扩展validatebox，添加验证两次密码功能
 */
$.extend($.fn.validatebox.defaults.rules, {
	eqPwd : {
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '两次输入的新密码不一致!'
	},
	checkEngAndNum : {
		validator : function(value) {
			var patrn=/^\w+$/; 
			if (!patrn.exec(value)){
				return false ;
			}else{
				return true; 
			}
		},
		message : '机构代码必须为英文或是数字组合!'
	}
});

/**
 * @author 夏悸
 * 
 * @requires jQuery,EasyUI
 * 
 * 扩展tree，使其支持平滑数据格式
 */
$.fn.tree.defaults.loadFilter = function(data, parent) {
	var opt = $(this).data().tree.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		var i, l, treeData = [], tmpMap = [];
		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}
		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]]
					&& data[i][idFiled] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

/**
 * 
 * 
 * @requires jQuery,EasyUI
 * 
 * 扩展treegrid，使其支持平滑数据格式
 */
$.fn.treegrid.defaults.loadFilter = function(data, parentId) {
	var opt = $(this).data().treegrid.options;
	var idFiled, textFiled, parentField;
	if (opt.parentField) {
		idFiled = opt.idFiled || 'id';
		textFiled = opt.textFiled || 'text';
		parentField = opt.parentField;
		var i, l, treeData = [], tmpMap = [];
		for (i = 0, l = data.length; i < l; i++) {
			tmpMap[data[i][idFiled]] = data[i];
		}
		for (i = 0, l = data.length; i < l; i++) {
			if (tmpMap[data[i][parentField]]
					&& data[i][idFiled] != data[i][parentField]) {
				if (!tmpMap[data[i][parentField]]['children'])
					tmpMap[data[i][parentField]]['children'] = [];
				data[i]['text'] = data[i][textFiled];
				tmpMap[data[i][parentField]]['children'].push(data[i]);
			} else {
				data[i]['text'] = data[i][textFiled];
				treeData.push(data[i]);
			}
		}
		return treeData;
	}
	return data;
};

/**
 * 
 * 
 * @requires jQuery,EasyUI
 * 
 * 扩展combotree，使其支持平滑数据格式
 */
$.fn.combotree.defaults.loadFilter = $.fn.tree.defaults.loadFilter;

/**
 * 
 * 
 * @requires jQuery,EasyUI
 * 
 * 防止panel/window/dialog组件超出浏览器边界
 * @param left
 * @param top
 */
var easyuiPanelOnMove = function(left, top) {
	var l = left;
	var t = top;
	if (l < 1) {
		l = 1;
	}
	if (t < 1) {
		t = 1;
	}
	var width = parseInt($(this).parent().css('width')) + 14;
	var height = parseInt($(this).parent().css('height')) + 14;
	var right = l + width;
	var buttom = t + height;
	var browserWidth = $(window).width();
	var browserHeight = $(window).height();
	if (right > browserWidth) {
		l = browserWidth - width;
	}
	if (buttom > browserHeight) {
		t = browserHeight - height;
	}
	$(this).parent().css({/* 修正面板位置 */
		left : l,
		top : t
	});
};
$.fn.dialog.defaults.onMove = easyuiPanelOnMove;
$.fn.window.defaults.onMove = easyuiPanelOnMove;
$.fn.panel.defaults.onMove = easyuiPanelOnMove;

/**
 * 
 * 
 * @requires jQuery,EasyUI,jQuery cookie plugin
 * 
 * 更换EasyUI主题的方法
 * 
 * @param themeName
 *            主题名称
 */
changeTheme = function(themeName) {
	var $easyuiTheme = $('#easyuiTheme');
	var url = $easyuiTheme.attr('href');
	var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName
			+ '/easyui.css';
	$easyuiTheme.attr('href', href);

	var $iframe = $('iframe');
	if ($iframe.length > 0) {
		for ( var i = 0; i < $iframe.length; i++) {
			var ifr = $iframe[i];
			$(ifr).contents().find('#easyuiTheme').attr('href', href);
		}
	}

	$.cookie('easyuiThemeName', themeName, {
		expires : 7
	});
};

/**
 * 
 * 
 * @requires jQuery
 * 
 * 将form表单元素的值序列化成对象
 * 
 * @returns object
 */
serializeObject = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};

/**
 * 
 * 
 * 新增formatString功能
 * 
 * 使用方法：formatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * 
 * @returns 格式化后的字符串
 */
formatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

/**
 * 
 * 
 * 接收一个以逗号分割的字符串，返回List，list里每一项都是一个字符串
 * 
 * @returns list
 */
stringToList = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for ( var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};
/**
 * 
 * 
 * @requires jQuery
 * 
 * 改变jQuery的AJAX默认属性和方法
 */
$.ajaxSetup({
	type : 'POST',
	error : function(XMLHttpRequest, textStatus, errorThrown) {
		$.messager.progress('close');
		$.messager.alert('错误', XMLHttpRequest.responseText);
	}
});

var Utils = {
	getCheckedRows : function(dataGrid) {
		return dataGrid.datagrid('getChecked');
	},
	checkSelect : function(rows) {// 检查grid是否有勾选的行, 有返回 true,没有返回true
		var records = rows;
		if (records && records.length > 0) {
			return true;
		}
		$.messager.alert('提示','请选择一条记录.');
		return false;

	},
	checkSelectOne : function(rows) {// 检查grid是否只勾选了一行,是返回 true,否返回true
		var records = rows;
		if (!Utils.checkSelect(records)) {
			return false;
		}
		if (records.length == 1) {
			return true;
		}
		$.messager.alert('提示','只能选择一条记录.');
		return false;
	}
};
var getAccessButton = function(initToolbar, buttons, type) {
	var newBars = [];
	var buttonStr = (buttons.substring(1, buttons.length - 1));
	for ( var i = 0; i < initToolbar.length; i++) {
		var bar = initToolbar[i];
		var buttonArray=buttonStr.split(',');
		for(var j=0;j<buttonArray.length;j++){
			var ibuttonArray=buttonArray[j].split('_');
			var type=ibuttonArray[0].replace(/\s+/g,"");
			if (bar.buttonType==type) {
				bar.iconCls=ibuttonArray[1];
				newBars.push(bar);
				newBars.push('-');
				break;
			}
		}
/*		
		if (type == 'super_user') {
			newBars.push(bar);
		} else {
			if ($.inArray(bar.buttonType, buttonStr.split(', ')) >= 0) {
				newBars.push(bar);
				newBars.push('-');
			}
		}*/
	}
	return newBars;
};

function closeWindow() {
	window.opener = null;
	window.open('', '_self');
	window.close();
	// FireFox需要设置allow_scripts_to_close_windows=true才能关闭,about:config
}

/* 
 * 用来遍历指定对象所有的属性名称和值 
 * obj 需要遍历的对象 
 * author: Jet Mah 
 * website: http://www.javatang.com/archives/2006/09/13/442864.html 
 */ 
function allPrpos(obj) { 
    // 用来保存所有的属性名称和值 
    var props = ""; 
    // 开始遍历 
    for(var p in obj){  
        // 方法 
        if(typeof(obj[p])=="function"){  
            obj[p](); 
        }else{  
            // p 为属性名称，obj[p]为对应属性的值 
            props+= p + "=" + obj[p] + "\t"; 
        }  
    }  
    // 最后显示所有的属性 
    $.messager.alert('提示',props); 
}
//显示或隐藏输入提示
function showTellDesc(obj, flag){
	if(flag){
		obj.find(".telldesc").css("display","");
	}else{
		obj.find(".telldesc").css("display","none");
	}
}
//显示或隐藏输入提示
function showTellDesc2(obj, flag){
	if(flag){
		obj.find(".telldesc2").css("display","");
	}else{
		obj.find(".telldesc2").css("display","none");
	}
}
/*	将数值四舍五入后格式化.	
@param num 数值(Number或者String)	
@param cent 要保留的小数位(Number)	
@param isThousand 是否需要千分位 0:不需要,1:需要(数值类型);	
@return 格式的字符串,如'1,234,567.45'	
@type String	
*/	
function formatPercent(num,cent,isThousand){
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))//检查传入数值为数值类型.		 
		num = "0";	
	num = parseFloat(num)*100;
	return formatNumber(num,cent,isThousand)+'%';
}
/*	将数值四舍五入后格式化.	
	@param num 数值(Number或者String)	
	@param cent 要保留的小数位(Number)	
	@param isThousand 是否需要千分位 0:不需要,1:需要(数值类型);	
	@return 格式的字符串,如'1,234,567.45'	
	@type String	
*/	
function formatNumber(num,cent,isThousand){
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))//检查传入数值为数值类型.		 
		num = "0";		
	if(isNaN(cent))//确保传入小数位为数值型数值.		
		cent = 0;		
	cent = parseInt(cent);		
	cent = Math.abs(cent);//求出小数位数,确保为正整数.		
	if(isNaN(isThousand))//确保传入是否需要千分位为数值类型.		
		isThousand = 0;		
	isThousand = parseInt(isThousand);		
	if(isThousand < 0)		
		isThousand = 0;		
	if(isThousand >=1) //确保传入的数值只为0或1		
		isThousand = 1;		
	sign = (num == (num = Math.abs(num)));//获取符号(正/负数)		
	//Math.floor:返回小于等于其数值参数的最大整数		
	num = Math.floor(num*Math.pow(10,cent)+0.50000000001);//把指定的小数位先转换成整数.多余的小数位四舍五入.		
	cents = num%Math.pow(10,cent); //求出小数位数值.		
	num = Math.floor(num/Math.pow(10,cent)).toString();//求出整数位数值.		
	cents = cents.toString();//把小数位转换成字符串,以便求小数位长度.		
	while(cents.length<cent){//补足小数位到指定的位数.		
		cents = "0" + cents;		
	}		
	if(isThousand == 0) //不需要千分位符.		
//		return (((sign)?'':'-') + num + '.' + cents);		//对整数部分进行千分位格式化.	
		return (((sign)?'':'-') + num + ((cent===0)?'':'.' +cents)  );  //对保留0位小数处理
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)		
		num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));		
//	return (((sign)?'':'-') + num + '.' + cents);	
	return (((sign)?'':'-') + num + ((cent===0)?'':'.' +cents)  );  //对保留0位小数处理
}

String.prototype.endWith=function(s){
	if(s==null||s==""||this.length==0||s.length>this.length)
		return false;
	if(this.substring(this.length-s.length)==s)
		return true;
	else
		return false;
	return true;
}

String.prototype.startWith=function(s){
	if(s==null||s==""||this.length==0||s.length>this.length)
		return false;
	if(this.substr(0,s.length)==s)
		return true;
	else
		return false;
	return true;
}
/**
 * 清空树组件的数据，若树的组件有对应隐藏域，同时刷新隐藏域的值
 * id:需要修改的树ID
 * defaultValue：默认的值
 * hiddenId:与树对应的隐藏域ID
 * hiddenValue：隐藏域赋值
 */
function cleanTreeValue(id,defaultValue,hiddenId,hiddenValue){
	$('#'+id).combotree('setValue',defaultValue);
	var selectedNode = $('#'+id).combotree('tree').tree(
	'getSelected');
	if(hiddenId!=''){
		$('#'+hiddenId).val(hiddenValue);
	}
}

function cleanComboboxValue(id){
	$('#'+id).combobox('clear');
}

//数组排序  arr.sort(sortBy("proName"));
var sortBy = function(name){
	return function(o, p){
		debugger;
				var a, b;
				if (typeof o === "object" && typeof p === "object" && o && p){
					a = o[name];
					b = p[name];
					if (a === b) {return 0;}
					if (typeof a === typeof b) { return a < b ? -1 : 1;}
					return typeof a < typeof b ? -1 : 1;
				}
				else {
					throw ("error"); 
				}
			}
}

/**
 * 动态修改文本时，textContent在ie8下不兼容，在ie8时用innerText
 * cl:要修改的文本的class
 * txt：要修改的文本
 * newText:修改后的文本
 */
function changerAuthTxt(cl,txt,newText){
	$(cl).each(function(i,data){
		try{
			if($(cl)[i].textContent.indexOf(txt) >= 0){
				$(cl)[i].textContent = '['+newText+']-'+txt;
			}
		}catch(Exception){
			if($(cl)[i].innerText.indexOf(txt) >= 0){
				$(cl)[i].innerText = '['+newText+']-'+txt;
			}
		}
	});
}

//iFrame加载前的提示
function iFrameLoading(iFrameParentId, iFrameId, promptStr){
	$('#'+iFrameParentId).mask(promptStr);
	var oFrm = document.getElementById(iFrameId);
	oFrm.onload = oFrm.onreadystatechange = function() {
	     if (this.readyState && this.readyState != 'complete') return;
	     else {
	    	 $('#'+iFrameParentId).unmask();
	     }
	}
}

//checkbox tree 级联子节点
function updateTreeChildren(node,checked,treeId) {
	var treeObj = $('#'+treeId);
	var children = treeObj.tree('getChildren', node.target);
	for(var i=0; i<children.length; i++){
		if(checked){
			treeObj.tree('check', children[i].target);
		}else{
			treeObj.tree('uncheck', children[i].target);
		}
	}
}
//checkbox tree 级联父节点
function updateTreeParent(node,checked,treeId) { 
	var treeObj = $('#'+treeId);
	var parentNode = treeObj.tree('getParent', node.target);
	if(parentNode){
		var children = treeObj.tree('getChildren', parentNode.target);
		var hasChecked = false; 
		for(var i=0; i<children.length; i++){
			if(children[i].checked){
				hasChecked = true;
			}
		}
		if(hasChecked){
			treeObj.tree('check', parentNode.target);
		}else{
			treeObj.tree('uncheck', parentNode.target);
		}
		updateTreeParent(parentNode, checked, treeId);
	}
}