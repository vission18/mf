<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sellms/smscompinfo/indTypeCom.js">
</script>

<script type="text/javascript">
	$(function() {
		//var industryType = "${industryType}";
		indTypeComDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}","${industryType}");
	});
</script>
<div class="easyui-layout" fit="true" id="${industryType}-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="${industryType}SearchForm">
			<div class="searchmore">
				<div>
					<label>省份:</label>
					<input class="imf_intxt" id="${industryType}queryProvice" style="width:180px;" name="queryProvice" type="text"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label>所在城市:</label>
					<input class="imf_intxt" id="${industryType}queryCity" style="width:180px;" name="queryCity" type="text"/>
					<span class="imf_more"><input id="${industryType}BtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>
				</div>
				<div>
					<label>公司:</label>
					<input class="imf_intxt" id="${industryType}queryCompName" style="width:180px;" name="queryCompName" type="text"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label>经营范围:</label>
					<input class="imf_intxt" id="${industryType}queryBussScope" style="width:180px;" name="queryBussScope" type="text"/>
					<span class="imf_more"><input id="${industryType}BtnClear" type="button" value="清空" class="imf_searchmore"/></span>
				</div>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="${industryType}-data-list"></table>
	</div>
	<div id="${industryType}-edit-win" class="imf_pop" style="width:720px;">
		<div class="imf_pop_title"><strong>数据列表</strong><span class="imf_pop_closed" onClick="popClosed('${industryType}-edit-win')">关闭</span></div>
		<form id="${industryType}EditForm" class="ui-form" method="post">
			<input type="hidden" id="${industryType}pkId" name="pkId">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>公司名称：</strong>
					<span>
						<input  id="${industryType}compName" name="compName" maxlength="100"
						data-options="required:true,missingMessage:'请输入公司名称.'"  class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>公司法人：</strong>
					<span>
						<input  id="${industryType}compLegal" name="compLegal" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>注册资金：</strong>
					<span>
						<input  id="${industryType}compMoney" name="compMoney" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>成立日期：</strong>
					<span>
						<input  id="${industryType}compDate" name="compDate" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>联系电话：</strong>
					<span>
						<input  id="${industryType}telNumber" name="telNumber" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>手机号码：</strong>
					<span>
						<input  id="${industryType}mobile" name="mobile" maxlength="100" 
						data-options="required:true,missingMessage:'请输入手机号码.'" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>公司地址：</strong>
					<span>
						<input  id="${industryType}compAdd" name="compAdd" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
					<strong>公司官网：</strong>
					<span>
						<input  id="${industryType}compWeb" name="compWeb" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>公司邮箱：</strong>
					<span>
						<input  id="${industryType}compEmail" name="compEmail" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
					</span>
				</li>
				<li>
					<strong>经营范围：</strong>
					<span> <textarea id ="${industryType}busiScope" name="busiScope" rows="4" cols="150" class="imf_textarea" style="width:500px;height:100px;"></textarea> </span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${industryType}-save" value="保存" class="imf_pop_btn_save" onClick="industryTypeSave('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" id="${industryType}-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${industryType}-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="${industryType}-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="${industryType}-edit-info"><p></p></div>
		</form>
	</div>
</div>

