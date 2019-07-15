var globalTreeId;
var globalTreeNode;
var maxTabNum = 5;//允许同时打开的tab个数
var arrayTab = new Array();
function addTab(title, href, iconCls, frameFlag, parentId) {// title 标题,href 连接地址,
	// 使用iFrame
	var tt = $('#main-center');
	if (tt.tabs('exists', title)) {
		tt.tabs('close', title);//存在相同标题的tab时,关闭前面打开的
	} 
	
	if(arrayTab.length>=maxTabNum){
		tt.tabs('close', arrayTab[0]);
	}
	arrayTab[arrayTab.length] = title;
	
	var content;
	if (frameFlag) {// 加一个外部tab标签,通常是一个全路径的外部url
		if (href) {
			$('#main-center').mask("数据加载中,请稍等...");
			content = '<iframe onLoad="$(\'#main-center\').unmask()" scrolling="auto" frameborder="0"  src="'
					+ href + '" style="width:100%;height:100%;"></iframe>';
		} else {
			content = '未实现';
		}
		tt.tabs('add', {
			title : title,
			closable : true,
			closed : true,
			content : content
		});
	} else {
		tt.tabs('add', {
			title : title,
			closable : true,
			id : parentId,
			href : href,
			iconCls : iconCls
		});
	}	
}

//更新tab title
function updateTab(title, tabId){
	try{
		$('#main-center').tabs('update', {
			tab: $('#'+tabId),
			options:{
				title:title
			}
		});
	}catch(e){}
}

function updateCurrTabTitle(title){
	try{
		var objTab = $('#main-center').tabs('getSelected');
		$('#main-center').tabs('update', {
			tab: objTab,
			options:{
				title:title
			}
		});
	}catch(e){}
}

function updateTabHref(href){
	try{
		var objTab = $('#main-center').tabs('getSelected');
		$('#main-center').tabs('update', {
			tab: objTab,
			options:{
				href:href
			}
		});
		objTab.panel('refresh');
	}catch(e){}
}


function getCurTabTitle(){
	return $('#main-center').tabs('getSelected').panel('options').title;
}

function bindTabEvent() {
	$(".tabs-inner").live('dblclick', function() {// 双击事件--关闭tab
		var subtitle = $(this).children("span").text();
		var t = $('#main-center').tabs('getTab', subtitle);
		if (t.panel('options').closable) {
			$('#main-center').tabs('close', subtitle);
		}
	});
	$(".tabs-inner").live('contextmenu', function(e) {// 右键事件--弹出菜单
		$('#mm').menu('show', {
			left : e.pageX,
			top : e.pageY
		});
		var subtitle = $(this).children("span").text();
		$('#mm').data("currtab", subtitle);
		return false;
	});
}
function closeAllTab(){
	var allTabs = $('#main-center').tabs('tabs');
	var closeTabsTitle = [];
	$.each(allTabs, function() {
		title = $(this).panel('options').title;
		if ($(this).panel('options').closable) {
			closeTabsTitle.push(title);
		}
	});
	for ( var i = 0; i < closeTabsTitle.length; i++) {
		$('#main-center').tabs('close', closeTabsTitle[i]);
	}
}
function bindTabMenuEvent() {
	// 关闭当前
	$('#mm-tabclose').click(function() {
		var curTabTitle = $('#mm').data("currtab");
		var t = $('#main-center').tabs('getTab', curTabTitle);
		if (t.panel('options').closable) {
			$('#main-center').tabs('close', curTabTitle);
		}
	});
	// 全部关闭
	$('#mm-tabcloseall').click(function() {
		closeAllTab();
	});
	// 关闭其他
	$('#mm-tabcloseother').click(function() {
		var allTabs = $('#main-center').tabs('tabs');
		var closeTabsTitle = [];
		var curTabTitle = $('#mm').data("currtab");
		$.each(allTabs, function() {
			title = $(this).panel('options').title;
			if ($(this).panel('options').closable && title != curTabTitle) {
				closeTabsTitle.push(title);
			}
		});
		for ( var i = 0; i < closeTabsTitle.length; i++) {
			$('#main-center').tabs('close', closeTabsTitle[i]);
		}
	});
}

function Clearnav() {
	$.each(titleArray, function(i, title) {
		$('#tree-menu').accordion('remove', title);
	});
	titleArray = new Array();
}

var titleArray = new Array();

// 二级菜单
function addTree(baseUrl, index, menuId) {
	Clearnav();
	if (arrTreeUrl[index]  && arrTreeUrl[index] != baseUrl && arrTreeUrl[index] != null && arrTreeUrl[index] != ''){
		targetUrl = arrTreeUrl[index];
		if(targetUrl.indexOf('/runMenu/menu.do')>-1){
			targetUrl += '&menuId=' + menuId;
		}
		$('#home-left-layout').panel({   
			href : targetUrl
		});
		$('#home-left-layout').panel("refresh");
	} else if (arrTreeMenu[index] && arrTreeMenu[index] != null) {
		$.each(arrTreeMenu[index],function(i, data) {
			var treeJson = '<ul id="tt_'+ index+ i+ '" class="easyui-tree" style="padding:5px;"></ul>';
			$('#tree-menu').accordion('add', {
				title : data.text,
				content : treeJson,
				selected : false,
				id : 'pp_' + index + i
			});
			$('#tt_' + index + i).tree({
				data : data.children,
				fit : true,
				onClick : function(node) {
					mouseLeftClick(baseUrl, node, index, i);
				}
			});
			titleArray[i] = data.text;
		});
		var pp = $('#tree-menu').accordion('panels');
		var t = pp[0].panel('options').title;
		$('#tree-menu').accordion('select', t);
	}
}

function mouseLeftClick(baseUrl, node, index, i){
	addTab(node.text,baseUrl+ node.attributes.url+ '?menuId='+ node.id+ '&menuType='+ node.type, node.iconCls, false, node.id);
}


// 点击一级菜单触发事件
function addTreeMenu(obj, baseUrl, index, menuId) {
	$("#topMenu").find("a").each(function() {
		$(this).removeClass("cur");
	});
	$("#topSmallMenu").find("a").each(function() {
		$(this).removeClass("cur");
	});
	$(obj).addClass("cur");
	addTree(baseUrl, index, menuId);
}
// 初始化菜单树
var arrTreeMenu = new Array();
var arrTreeUrl = new Array();
function initTreeMenu(baseUrl) {
	$.ajax(baseUrl + "/home.do",{
		type : 'post',
		dataType : 'json',
		data : '{}',
		success : function(result) {
			html = '';
			smallHtml = '';
			$.each(result.data,function(i, data) {
				classStyle = "";
				if (i == 0) {
					classStyle = "cur";
				}
				if(data.children){
					arrTreeMenu[i] = data.children;	
				}
				if(data.attributes.url && data.attributes.url != 'null'){
					arrTreeUrl[i] = baseUrl + data.attributes.url;
				}
				img = data.iconCls + ".png";
				html += '<li style="background:url('+stylePath+'/images/home/'+ img+ ') no-repeat 18px 10px;"><a href="javascript:void(0);" onclick="addTreeMenu(this,\''
						+ baseUrl + '\',' + i + ',\'' + data.id + '\')" class="'
						+ classStyle
						+ '">'
						+ data.text
						+ '</a></li>';
				smallHtml += '<li><a href="javascript:void(0);" onclick="addTreeMenu(this,\''
						+ baseUrl + '\',' + i + ',\'' + data.id + '\')" class="'
						+ classStyle
						+ '">'
						+ data.text
						+ '</a></li>';
			});
			$('#topMenu').html(html);
			$('#topSmallMenu').html(smallHtml);
			
			var topMenuType = "big";
			if ($.cookie('imfImsTopMenuType')) {
				topMenuType = $.cookie('imfImsTopMenuType');
			}
			switchTopMenuType(topMenuType);
			
			addTree(baseUrl, 0);
			$('#indexPanel').panel({
				href : baseUrl + '/public/desktop.do'
			});
		}
	});
}

$(document).ready(function() {
	$(".imf_roll_closed").click(function() {
		rollUp("imf_roll");
	});
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus")
	}).blur(function () {
		$(this).removeClass("input_focus")
	});
	//tab事件
	$('#main-center').tabs({
		onClose: function(title,index){
			j = 0;
			tempTab = new Array();
			for(i=0;i<arrayTab.length;i++){
				if(arrayTab[i]!=title){
					tempTab[j] = arrayTab[i];
					j++;
				}
			}
			arrayTab = tempTab;
		}
	});
	$(".imf_pop" ).uidraggable({cancel:"input,textarea,button,select,option,.datagrid,.tree"});
});

var arrTreeMenuTips = new Array();
function addAltToTree(){
	$("#tree-menu .tree-node").each(
		function(i, node){
			nodeId = $(this).attr("node-id");
			tips = $("#tree-menu").tree("find",nodeId).attributes.tipText;
			arrTreeMenuTips[i] = tips;
			if(tips&&tips!=''){
				$(this).bind("mouseover", function(){
					seashowtip(i,1,150);
				});
				$(this).bind("mouseout", function(){
					seashowtip(i,0,150);
				});
			}
		}
	);
}
function seashowtip(i,flag,iwidth){
	tips = arrTreeMenuTips[i];
	var my_tips = $("#treetips");
	if(flag&&tips&&tips!=''){
		my_tips.css("display","");
		my_tips.css("width",iwidth);
		my_tips.html(tips);
	    my_tips.css("left",event.clientX-20);
	    my_tips.css("top",event.clientY-50);
	}else{
		my_tips.css("display","none");
	}
}

//刷新树
function reloadTree(){
    if (globalTreeNode) {
        $('#'+globalTreeId).tree('reload', globalTreeNode.target);
    } else {
        $('#'+globalTreeId).tree('reload');
    }
}

var northFlag = true;
var westFlag = true;
var booterFlag = true;
function showLayout(region){
	$("#mainBody").layout("expand",region);
	if(region==='north') northFlag = true;
	if(region==='west') westFlag = true;
	if(region==='south') booterFlag = true;
	if(northFlag&&westFlag)changerTxt("fullScreen","点击切换全屏");
}
function hideLayout(region){
	$("#mainBody").layout("collapse",region);
	if(region==='north') northFlag = false;
	if(region==='west') westFlag = false;
	if(region==='south') booterFlag = false;
	if(!northFlag&&!westFlag)changerTxt("fullScreen","点击取消全屏");
}
var fullScreenFlag = false;
function fullScreen(){
	if(northFlag&&westFlag){
		hideLayout("north");
		hideLayout("west");
		hideLayout("south");
		fullScreenFlag = true;
		changerTxt("fullScreen","点击取消全屏");
	}else if(!northFlag&&!westFlag){
		showLayout("west");
		showLayout("north");
		showLayout("south");
		fullScreenFlag = false;
		changerTxt("fullScreen","点击切换全屏");
	}else{
		if(fullScreenFlag){
			showLayout("west");
			showLayout("north");
			showLayout("south");
			changerTxt("fullScreen","点击切换全屏");
		}else{
			hideLayout("north");
			hideLayout("west");
			hideLayout("south");
			changerTxt("fullScreen","点击取消全屏");
		}
		fullScreenFlag = !fullScreenFlag;
	}
}
//隐藏下面的工具栏
function hideToolBarWest(){
	hideLayout("south");
	hideLayout("west");
	booterFlag = false;
	westFlag = false;
}
function showLayoutWS(){
	showLayout("south");
	showLayout("west");
	booterFlag = true;
	westFlag = true;
}



function changerTxt(id,txt){
	try{
		$("#"+id)[0].textContent=txt;
	}catch(e){
			$("#"+id)[0].innerText  = txt;
	}
}
function resizeNorth(layoutId, h){
	$('#'+layoutId).layout('panel', 'north').panel('resize',{height:h});
	$('#'+layoutId).layout('resize');
}

$(document).ready(function () {
	$('#west-layout').panel({   
		onOpen:function(){ 
			westFlag = true;
			if(!booterFlag)
			showLayout("south");
			if(westFlag&&northFlag)changerTxt("fullScreen","点击切换全屏");
	    },
	    onClose:function(){
	    	westFlag = false;
	    	if(booterFlag)
	    		hideLayout("south");
	    }
	});
	$('#south-layout').panel({   
		onOpen:function(){ 
			booterFlag = true;
			if(!westFlag)
				showLayout("west");
			if(westFlag&&northFlag)changerTxt("fullScreen","点击切换全屏");
				
	    } ,
	    onClose:function(){
	    	booterFlag = false;
	    	if(westFlag)
	    		hideLayout("west");
	    }  
	});  
	$('#north-layout').panel({   
		onOpen:function(){   
			if(!northFlag){
				northFlag = true;
				if(westFlag&&northFlag)changerTxt("fullScreen","点击切换全屏");
			}
	    }   
	}); 
})
