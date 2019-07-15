function Clock() {
	var date = new Date();
	this.year = date.getFullYear();
	this.month = date.getMonth() + 1;
	this.date = date.getDate();
	this.day = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")[date.getDay()];
	this.hour = date.getHours() < 10 ? "0" + date.getHours() : date.getHours();
	this.minute = date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes();
	this.second = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

	this.toString = function() {
		return this.year + "年" + this.month + "月" + this.date + "日 " + this.hour + ":" + this.minute + ":" + this.second + " " + this.day; 
	};
	
	this.toSimpleDate = function() {
		return this.year + "-" + this.month + "-" + this.date;
	};
	
	this.toDetailDate = function() {
		return this.year + "-" + this.month + "-" + this.date + " " + this.hour + ":" + this.minute + ":" + this.second;
	};
	
	this.display = function(ele) {
		if(ele){
			var clock = new Clock();
			ele.innerHTML = clock.toString();
			window.setTimeout(function() {clock.display(ele);}, 1000);
		}
	};
}

$(document).ready(function() {
	
	var clock = new Clock();
	clock.display(document.getElementById("clock"));
	
	/**
	 * 2011-08-18 by zx for change stylesheet
	 */
	$('.styleswitch').click(function(){
		style = this.getAttribute("rel");
		expire = new Date((new Date()).getTime() + (365*24*60*60*1000));//one year
		$.cookie('imfImsStyleName', style, {
			expires : expire
		});
		switchStylestyle(style);
		return false;
	});
});

function switchStylestyle(styleName)
{
	$('link[rel=stylesheet][title]').each(function(i) 
	{
		this.disabled = true;
		if (this.getAttribute('title') == styleName) this.disabled = false;
	});
	
	$("iframe").contents().find('link[rel=stylesheet][title]').each(function(i) 
	{
		this.disabled = true;
		if (this.getAttribute('title') == styleName) this.disabled = false;
	});
}

function switchTopMenuType(type){
	if(type==='big'){
		$('#bigMenuBar').css('display', 'block');
		$('#smallMenuBar').css('display', 'none');
		resizeNorth("mainBody", 85);
		$('#main-north-layout').css('height', '85px');
	}else{
		$('#bigMenuBar').css('display', 'none');
		$('#smallMenuBar').css('display', 'block');
		resizeNorth("mainBody", 33);
		$('#main-north-layout').css('height', '33px');
	}
}

function setTopMenuType(type){
	expire = new Date((new Date()).getTime() + (365*24*60*60*1000));//one year
	$.cookie('imfImsTopMenuType', type, {
		expires : expire
	});
	switchTopMenuType(type);
}
