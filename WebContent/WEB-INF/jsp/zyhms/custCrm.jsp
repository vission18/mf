<%@ page language="java" pageEncoding="UTF-8"%>
<!-- Js Start -->
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/zhyms/custCrm.js"></script>
<script type="text/javascript">
	$(function() {
		custCrmDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="custCrm-mainBody">
	<!-- Search panel start -->
	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="custCrmSearchForm">
			<div class="searchmore">
				<label>消费类别:</label>
				<select  class="imf_intxt easyui-combobox"  id="typeList" name="xfType" style="width:100px"></select>
				<label>消费日期:</label>
				<input class="Wdate imf_intxt"  style="width:180px;" name="xfDate" type="text" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})"/>
				<label>记录日期:</label>
				<input class="Wdate imf_intxt"  style="width:180px;" name="recordDate" type="text" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})"/>
				<label>记录人:</label>
				<input type="text" name="recordUser" value="" class="imf_intxt"	style="width: 120px;" />
				<span class="imf_more"><input id="custCrmBtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="custCrmBtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->
	
	<!-- Data List -->
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="custCrm-data-list"></table>
	</div>
	
	<div id="custCrm-role-div"></div>
	
	<!-- Edit Win&Form -->
	<div id="custCrm-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>消费记录</strong><span class="imf_pop_closed" onClick="popClosed('custCrm-edit-win')">关闭</span></div>
		<form id="custCrmEditForm" class="ui-form" method="post">
			<input type="hidden" name="xfId">
			<input type="hidden" name="recordDate">
			<input type="hidden" name="recordUser">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>消费类别：</strong>
					<span><select name="xfType"  class="easyui-combobox" id="xfType"  value="" style="width:200px;">  
   					</select></span>
				</li>
				<li>
					<strong>消费物品：</strong>
					<span><input maxlength="100" class="imf_intxt easyui-validatebox" type="text" id="xfGoods" name="xfGoods" data-options="required:true,missingMessage:'请输入消费物品.'"></span>
				</li>
				<li>
					<strong>消费价格：</strong>
					<span><input class="imf_intxt easyui-numberbox"  id="xfPrice" type="text" name="xfPrice" data-options="required:true,missingMessage:'请输入消费价格'"></span>
				</li>
				<li>
					<strong>消费日期：</strong>
					<span>
						<input class="Wdate imf_intxt" style="width:200px;" name="xfDate" type="text" id="xfDate" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})"/>
					</span>
				</li>
				<li>
		    		<strong>消费详情：</strong>
		    		<span><textarea id="xfDetail" name="xfDetail" maxlength="512"
						 rows="100" cols="100" class="imf_textarea" style="min-width:200px;width:300px"></textarea>
					</span>
				 </li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="custCrm-save" value="保存" class="imf_pop_btn_save" onClick="custCrmSave('${pageContext.request.contextPath}/custCrmController/save.do')"/></span>
				<span><input type="button" id="custCrm-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('custCrm-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="custCrm-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="custCrm-edit-info"><p></p></div>
		</form>
	</div>
</div>

