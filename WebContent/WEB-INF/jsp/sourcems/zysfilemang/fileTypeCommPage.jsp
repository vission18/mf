<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sourcems/zysfilemang/fileTypeComm.js">
</script>

<script type="text/javascript">
	$(function() {
		var baseUrl = "${pageContext.request.contextPath}";
		fileTypeDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}","${fileTypeList}");
	});
</script>
<div class="easyui-layout" fit="true" id="${fileTypeList}-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="${fileTypeList}SearchForm">
			<div class="searchmore">
				<label>资源匹配:</label>
				<input class="imf_intxt" id="${fileTypeList}KeyWord" style="width:180px;" name="fileNm" type="text"/>
				<span class="imf_more"><input id="${fileTypeList}BtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="${fileTypeList}BtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="${fileTypeList}-data-list"></table>
	</div>
	
	<div id="${fileTypeList}-download-win" class="imf_pop" style="width:500px;">
		<div class="imf_pop_title"><strong>网盘下载</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-download-win')">关闭</span></div>
		<form id="${fileTypeList}-download-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li><strong>网盘地址：</strong><span id="${fileTypeList}OpenUrl"></span></li>
				<li><strong>提取码：</strong>
					<span>
						<input  id="${fileTypeList}OpenExtCode" maxlength="100" class="imf_intxt easyui-validatebox" type="text" disabled="disabled">
					</span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-download-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-download-win')"/></span>
			</div>
			<div class="imf_pop_error" id="${fileTypeList}-download-error"><p></p></div>
			<div class="imf_pop_correct" id="${fileTypeList}-download-info"><p></p></div>
		</form>
	</div>
	
	<div id="${fileTypeList}-adminInfo-win" class="imf_pop" style="width:450px;">
		<div class="imf_pop_title"><strong>联系管理员</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-adminInfo-win')">关闭</span></div>
		<form id="${fileTypeList}-adminInfo-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li><span style="color:red">
					1:通过管理员微信后，请说明要下载的资源名称.</br>
					2:由于访问人数较多，服务器压力过大，大文件建议使用网盘下载.</br>
					3:资源系统中若有侵权行为，请立即联系管理员删除资源.</br>
				</span></li>
				<li>
					<div align="center">
						<img src="${pageContext.request.contextPath}/images/adminWeChat.jpg" title="微信" width="195" height="275"/>
					</div>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-adminInfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-adminInfo-win')"/></span>
			</div>
		</form>
	</div>
</div>

