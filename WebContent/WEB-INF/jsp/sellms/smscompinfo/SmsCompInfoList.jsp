<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sellms/smscompinfo/SmsCompInfo.js">
</script>

<script type="text/javascript">
	$(function() {
		smscompinfoDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="smscompinfo-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="smscompinfoSearchForm">
			<div class="searchmore">
				<div>
					<label>省份:</label>
					<input class="imf_intxt" id="queryProvice" style="width:180px;" name="queryProvice" type="text"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label>所在城市:</label>
					<input class="imf_intxt" id="queryCity" style="width:180px;" name="queryCity" type="text"/>
					<span class="imf_more"><input id="smscompinfoBtnSearch" type="button" value="采集" class="imf_searchmore"/></span>
				</div>
				<div>
					<label>行业:</label>
					<input class="imf_intxt" id="queryBuss" style="width:180px;" name="queryBuss" type="text"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label>经营范围:</label>
					<input class="imf_intxt" id="queryBussScope" style="width:180px;" name="queryBussScope" type="text"/>
					<span class="imf_more"><input id="smscompinfoBtnClear" type="button" value="清空" class="imf_searchmore"/></span>
				</div>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="smscompinfo-data-list"></table>
	</div>
	<div id="smscompinfo-edit-win" class="imf_pop" style="width:720px;">
		<div class="imf_pop_title"><strong>公司信息表信息列表</strong><span class="imf_pop_closed" onClick="popClosed('smscompinfo-edit-win')">关闭</span></div>
		<form id="smscompinfoEditForm" class="ui-form" method="post">
			<input type="hidden" name="pkId">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>公司名称：</strong>
					<span>
						<input  id="compName" name="compName" maxlength="100"
						data-options="required:true,missingMessage:'请输入公司名称.'"  class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>公司法人：</strong>
					<span>
						<input  id="compLegal" name="compLegal" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>注册资金：</strong>
					<span>
						<input  id="compMoney" name="compMoney" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>成立日期：</strong>
					<span>
						<input  id="compDate" name="compDate" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>联系电话：</strong>
					<span>
						<input  id="telNumber" name="telNumber" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>手机号码：</strong>
					<span>
						<input  id="mobile" name="mobile" maxlength="100" 
						data-options="required:true,missingMessage:'请输入手机号码.'" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>公司地址：</strong>
					<span>
						<input  id="compAdd" name="compAdd" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>公司官网：</strong>
					<span>
						<input  id="compWeb" name="compWeb" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>公司邮箱：</strong>
					<span>
						<input  id="compEmail" name="compEmail" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>经营范围：</strong>
					<span> <textarea name="busiScope" rows="4" cols="150" class="imf_textarea" style="width:500px;height:100px;"></textarea> </span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="smscompinfo-save" value="保存" class="imf_pop_btn_save" onClick="smscompinfoSave('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="smscompinfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('smscompinfo-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="smscompinfo-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="smscompinfo-edit-info"><p></p></div>
		</form>
	</div>
		<div id="smscompinfo-import-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>批量导入数据窗口</strong><span class="imf_pop_closed" onClick="popClosed('smscompinfo-import-win')">关闭</span></div>
		<form id="smscompinfo-import-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
			<li><strong>模板下载：</strong><span><a id="smscompinfo-modelDownload-btn" href="${pageContext.request.contextPath}/smscompinfo/downloadCompExcel.do"></a></span></li>
			<li><strong>导入文件：</strong><span><input id="smscompinfo-file" name="file" type="file" class="imf_intxt"/></span></li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="import-smscompinfo-save" value="导入" class="imf_pop_btn_save" onClick="importsmscompinfo('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="import-smscompinfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('smscompinfo-import-win')"/></span>
			</div>
			<div class="imf_pop_error" id="smscompinfo-import-error"><p></p></div>
			<div class="imf_pop_correct" id="smscompinfo-import-info"><p></p></div>
		</form>
	</div>
</div>

