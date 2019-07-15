<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/pubCode.js"></script>
<script type="text/javascript">
	$(function() {             
		pubCodeDataGrid('${pageContext.request.contextPath}','${accessButtons.data}','${accessButtons.type}');
		$('#pubCodeBtnSave').click(function() {
			pubCodeSave('${pageContext.request.contextPath}/pubCode/save.do');
		});
	});
</script>

<div class="easyui-layout" fit="true" id="pub-code-body">

<!-- Search panel start -->
<div id="pubcode-search-div" data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
	<form id="pubCodeSearchForm">
		<div class="searchmore">
			<label>代码名:</label>
			<input type="text" name="codeName" value="" class="imf_intxt" style="width:80px;"/>
			<label>代码值:</label>
			<input type="text" name="codeValue" value="" class="imf_intxt" style="width:120px;"/>	
			<span class="imf_more"><input id="pubcodebtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
			<span class="imf_all"><input id="pubCodeBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
		</div>
	</form>
</div>
<!--  Search panel end -->

<!-- Data List -->
<div data-options="region:'center',border:false">
	<table id="pubcode-data-list"></table>
</div>

	<!-- Edit Win&Form -->
	<!--  modify password start -->
	<div class="imf_pop" id="pubcode-edit-win" style="width:480px;">
		<div class="imf_pop_title"><strong>代码维护</strong><span class="imf_pop_closed" onClick="popClosed('pubcode-edit-win')">关闭</span></div>
		<form id="pubCodeEditForm" class="ui-form" method="post">
			<input type="hidden" name="codeId"/>
			<div class="imf_pop_con">
					<ul>
						<li><strong>代码名：</strong>
							<span>
								<input class="imf_intxt easyui-validatebox" type="text" id="codeName"
									name="codeName" data-options="required:true">
							</span>
						</li>
						<li><strong>代码值：</strong>
							<span>
								<input class="imf_intxt easyui-validatebox" type="text" name="codeValue" id="codeValue"
									data-options="required:true">
							</span>
						</li>
						<li><strong>代码描述：</strong>
							<span>
 								<textarea name="codeRmk" class="imf_textarea easyui-validatebox" style="height:45px;"  validType="length[0,128]"></textarea>
							</span>
						</li>
					</ul>
			</div>
			<div class="imf_pop_btn">
					<span><input type="button" name="" value="保存" class="imf_pop_btn_save" onclick="pubCodeSave('${pageContext.request.contextPath}'+'/pubCode/save.do')"/></span>
					<span><input type="button" name="" value="关闭" class="imf_pop_btn_closed" onclick="popClosed('pubcode-edit-win')"/></span>
			</div>

			<div class="imf_pop_error" id="pubCodeError"><p></p></div>
			<div class="imf_pop_correct" id="pubCodeInfo"><p></p></div>

		</form>
 	</div>

</div>
