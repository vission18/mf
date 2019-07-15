//信息提示
function popInfo(infoId, message) {
	delayTime = 1000;//默认显示1秒
	var infoObj = $("#"+infoId);
	if(infoObj.hasClass("imf_pop_error")){//如果是错误提示则显示3秒
		delayTime = 3000;
	}
	infoObj.html("<p>"+message+"</p>");
	if (infoObj.is(":hidden")) {
		infoObj.show()
		.stop()
		.show()
		.animate({"top":"0"},400)
		.delay(delayTime)
		.animate({"top":"-200px"},800)
		.fadeOut(200)
		.animate({"top":"35px"})
	}	
}

//信息提示
function popConfirm(message,parentId) {
	$("#confirmMessage").html(message);
	popCenter("pop_confirm");
	$("#pop_confirm").fadeIn(times);
	$("#"+parentId).append($backShade);
	$backShade.fadeIn(times);		
}

var $backShade=$(
		"<div style='background-color:#333;width:"
			+backWidth+"px;height:"
			+backHeight+"px;position:absolute;left:0;top:0;z-index:7999' class='opacity'></div>"
		).css({"opacity":"0.5","filter":"Alpha(opacity=50)"});

//创建背景层
var backWidth=$(window).width();
var backHeight=$(window).height();

//弹出窗口
var times=500;
function popWindow(winId,parentId) {
	height = "5px";
	if(parentId==='mainBody'){
		height = "100px";
	}
	$("#"+winId).fadeIn(times).css({"left":"50%","top":height});
	popWinTopCenter(winId);
	
	html = "<div id='"+winId+"_background' style='opacity:0.5;filter:Alpha(opacity=50);background-color:#333;width:"
		+backWidth+"px;height:" +backHeight+"px;position:absolute;left:0;top:0;z-index:7999' class='opacity'></div>";

	if($("#"+parentId).html() && $("#"+parentId).html().indexOf(winId+"_background")<0){
		$("#"+parentId).append(html);
	}
	$("#"+winId+"_background").fadeIn(times);
}

function popFullWindow(winId,parentId) {
	$("#"+winId).fadeIn(times).css({"left":"0","top":"0","position":"absolute","width":"100%","height":"100%"});
}

function popClosed(winId) {
	$("#"+winId).fadeOut(times);
	$("#"+winId+"_background").fadeOut(times);
}
//窗口位置
function popCenter(winId) {
	var $popArea=$("#"+winId);
	var popwidth=$popArea.width()/2;
	var popheight=$popArea.height()/2;
	$popArea.css({marginLeft:-popwidth,marginTop:-popheight});
}

function popWinTopCenter(winId) {
	var $popArea=$("#"+winId);
	var popwidth=$popArea.width()/2;
	//var popheight=$popArea.height()/2;
	$popArea.css({marginLeft:-popwidth,marginTop:-5});
}

function rollDown(popname,message) {
	$("#rollMessage").html(message);
	var $popArea=$("."+popname);
	$popArea.css({"display":"block"}).stop().animate({
		"bottom":80
	},300).animate({
		"bottom":30
	},"fast").delay(2000).slideUp(700);
}

function rollUp(popname) {
	var $popArea=$("."+popname);
	$popArea.stop().fadeOut(700);

}
//登录语言选择
function languageList(choose) {
	nextall=choose.nextAll()
	if (nextall.is(':hidden')) {
			nextall.click(function () {
			var ddVal=choose.find('em').text($(this).text());
			$(this).parent().find('dd').hide();
		});
		nextall.show()
	}	else {
		nextall.hide()
	}
	choose.parent().mouseleave(function () {
		$(this).find('dd').hide();
	})
}

// 搜索框类型选择
function selectList(choose) {
	nextall = choose.nextAll()
	if (nextall.is(':hidden')) {
		nextall.click(function() {
			var ddVal = choose.text($(this).text());
			$(this).parent().find('dd').hide();
		});
		nextall.show()
	} else {
		nextall.hide()
	}
	choose.parent().mouseleave(function() {
		$(this).find('dd').hide();
	})
}

//demo 临时加载
function changeOpen(area,idname) {
	var tdArea=area.parent()
	var tdIndex=tdArea.index()+1;
	var tdWidth=tdArea.width()-1;
	var tdPostion=Math.ceil((tdArea.position()).left);
	var $tdShow=$("#"+idname+" .selectablist"+tdIndex);
	$tdShow.css({"position":"absolute","left":tdPostion-1});
	$tdShow.width(tdWidth)
	if ($tdShow.is(":hidden")) {
		$tdShow.show();
	}else {
		$tdShow.hide();
	}
}

function directionHover(directext) {
	var diText=directext.text();
	$('.imf_didesc').show().text(diText);
	
}

$(function () {
	$('.imf_direction a').hover(function () {
		directionHover($(this));
	},function () {
		$('.imf_didesc').hide();
	})
	$('.search_default_type dt').click(function () {
		selectList($(this));
	})
	$(".open").click(function () {
		var thisTag=$(this);
		var thisName=$(this).parents("table").next("div").attr("id");
		changeOpen(thisTag,thisName);
	})
	$(".selectlistarea li").click(function () {
		$(this).parent().hide();
	})
	$("table tr:odd").css({"backgroundColor":"#f5f5f5"});
	$("table tr:even").css({"backgroundColor":"#eee"});
	$("table tr td").not("thead tr td").hover(function () {
		$(this).addClass("trhover").siblings().addClass("trhover");
	},function () {
		$(this).removeClass("trhover").siblings().removeClass("trhover");
	})

	$(".selectpanel dd,.selectlist dd").hide();
	$(".tablescon").children().hide()
	$(".tablescon").children(":first").show();
	$(".tabtab li a").click(function () {
		$(this).addClass("cur").parent().siblings().find("a").removeClass("cur");
	})
		
	$(".tabtab li a").click(function () {
		var listIndex=$(this).parent().index();
		$(".tablescon").children("div:eq("+listIndex+")").show().siblings().hide();
	})
	$(".searchtitle em").toggle(function () {
		$(this).text("展开").parent().next().slideUp(500);
	},function () {
		$(this).text("收缩").parent().next().slideDown(200);
	})
	$(".searchtitle strong").toggle(function () {
		$(this).parent().next().slideUp(500);
	},function () {
		$(this).parent().next().slideDown(200);
	})
	$(".selectpanel dt,.selectlist dt").toggle(function () {
		$(this).nextAll().show();
	},function () {
		$(this).nextAll().hide();
	})
	$(".selectpanel dd,.selectlist dd").click(function () {
		$(".selectpanel dd,.selectlist dd").hide();
	})
	$(".selectlist").focus(function () {
		$(this).find("dt").css({"z-index":"999"})
	}).blur(function () {
		$(this).find("dt").css({"z-index":"888"})
	})
	$(".imf_intxt,textarea,.selectlist").focus(function () {
		$(this).addClass("input_focus");
	}).blur(function () {
		$(this).removeClass("input_focus");
	});
	$(".imf_tablecon tr").hover(function () {
		$(this).children(":first").find("a").css({"color":"#e82f2f"});
	},function () {
		$(this).children(":first").find("a").css({"color":"#555"});
	})
	
	$('.loginlanguage dt').click(function () {
		languageList($(this));
	})
})

function browser(){
	var bro = $.browser;
	if(bro.msie) {
		return "IE "+bro.version;
	}else if(bro.mozilla) {
		return "Firefox "+bro.version;
	}else if(bro.safari) {
		return "Safari "+bro.version;
	}else if(bro.opera) {
		return "Opera "+bro.version;
	}
}

function changeSkin(color){
	if(browser()==='IE 6.0'){
		document.location.reload();
	}
}