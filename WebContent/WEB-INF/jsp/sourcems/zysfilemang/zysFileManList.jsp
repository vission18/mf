<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sourcems/zysfilemang/zysFileMan.js">
</script>

<script type="text/javascript">
	$(function() {
		var baseUrl = "${pageContext.request.contextPath}";
		zysFileManDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="zysFileMan-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="zysFileManSearchForm">
			<div class="searchmore">
				<label>资源匹配:</label>
				<input class="imf_intxt" id="keyWord" style="width:180px;" name="fileNm" type="text"/>
				<span class="imf_more"><input id="zysFileManBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="zysFileManBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="zysFileMan-data-list"></table>
	</div>
	<div id="zysFileMan-edit-win" class="imf_pop" style="width:720px;">
		<div class="imf_pop_title"><strong>文件管理信息列表</strong><span class="imf_pop_closed" onClick="popClosed('zysFileMan-edit-win')">关闭</span></div>
		<form id="zysFileManEditForm" class="ui-form" method="post" enctype="multipart/form-data">
			<input type="hidden" id="pkId" name="pkId">
			<input type="hidden" id="filePath" name="filePath">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>文件类型:</strong>
					<span>
						<input  id="fileType" name="fileType" maxlength="100" class="imf_intxt easyui-combobox" 
						style="width:200px;" editable="false" type="text" >
					</span>
					<strong>文件名称:</strong>
					<span>
						<input id="fileNm" name="fileNm"  type="text" maxlength="100" class="imf_intxt"/>
					</span>
				</li>
				<li>
					<strong>原价:</strong>
					<span>
						<input  id="costPrice" name="costPrice" maxlength="100" class="imf_intxt easyui-validatebox" value="0" type="text" >
					</span>
					<strong>活动价:</strong>
					<span>
						<input  id="nowPrice" name="nowPrice" maxlength="100" class="imf_intxt easyui-validatebox" value="0" type="text" >
					</span>
				</li>
				<li>
					<strong>压缩密码:</strong>
					<span>
						<input  id="compPwd" name="compPwd" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>网盘类型:</strong>
					<span>
						<select id="skyDriveType" class="imf_intxt easyui-combobox" name="skyDriveType" editable="false" style="width:200px;">  
						    <option value="01">百度网盘</option>  
						    <option value="02">诚通网盘</option>  
						    <option value="03">360网盘</option>  
						</select>  
					</span>
				</li>
				<li>
					<strong>网盘地址:</strong>
					<span>
						<input  id="skyDriveUrl" name="skyDriveUrl" maxlength="200" class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>提取码:</strong>
					<span>
						<input  id="skyDriveExtCode" name="skyDriveExtCode" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>其他链接地址:</strong>
					<span>
						<input  id="otherUrl" name="otherUrl" maxlength="200" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>文件说明:</strong>
					<span> <textarea id="fileRemark" name="fileRemark" rows="8" cols="70" class="imf_textarea"></textarea> </span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="zysFileMan-save" value="保存" class="imf_pop_btn_save" onClick="saveFileRec('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="zysFileMan-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('zysFileMan-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="zysFileMan-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="zysFileMan-edit-info"><p></p></div>
		</form>
	</div>
	
	<div id="skyDrive-download-win" class="imf_pop" style="width:500px;">
		<div class="imf_pop_title"><strong>网盘下载</strong><span class="imf_pop_closed" onClick="popClosed('skyDrive-download-win')">关闭</span></div>
		<form id="skyDrive-download-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li><strong>网盘地址：</strong><span id="openSkyDriveUrl"></span></li>
				<li><strong>提取码：</strong>
					<span>
						<input  id="openSkyDriveExtCode" maxlength="100" class="imf_intxt easyui-validatebox" type="text" disabled="disabled">
					</span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="skyDrive-download-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('skyDrive-download-win')"/></span>
			</div>
			<div class="imf_pop_error" id="skyDrive-download-error"><p></p></div>
			<div class="imf_pop_correct" id="skyDrive-download-info"><p></p></div>
		</form>
	</div>
	
	<div id="open-adminInfo-win" class="imf_pop" style="width:450px;">
		<div class="imf_pop_title"><strong>联系管理员</strong><span class="imf_pop_closed" onClick="popClosed('open-adminInfo-win')">关闭</span></div>
		<form id="open-adminInfo-form" class="ui-form" method="post" enctype="multipart/form-data">
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
				<span><input type="button" id="open-adminInfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('open-adminInfo-win')"/></span>
			</div>
			<div class="imf_pop_error" id="skyDrive-download-error"><p></p></div>
			<div class="imf_pop_correct" id="skyDrive-download-info"><p></p></div>
		</form>
	</div>
</div>

