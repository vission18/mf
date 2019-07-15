<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/sellms/phoneinfo/phoneInfo.js"></script>

<script type="text/javascript">
	$(function() {
		phoneInfoDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	});
</script>
<div class="easyui-layout" fit="true" id="phoneInfo-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<label>免责声明:仅限于学习与研究，用于任何商务用途者后果自负！</label>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="phoneInfo-data-list"></table>
	</div>
	<div id="phoneInfo-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title"><strong>客户信息列表</strong><span class="imf_pop_closed" onClick="popClosed('phoneInfo-edit-win')">关闭</span></div>
		<form id="phoneInfoEditForm" class="ui-form" method="post">
			<input type="hidden" name="xfId">
			<input type="hidden" name="recordDate">
			<input type="hidden" name="recordUser">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>省份：</strong>
					<span><select name="phoneName"  class="easyui-combobox" id="phoneName"  value="" style="width:200px;">  
   								<option value="1" selected="selected">四川</option>  
   					</select></span>
				</li>
				<li>
					<strong>城市：</strong>
					<span><select name="phoneName"  class="easyui-combobox" id="phoneName"  value="" style="width:200px;">  
   						<option value="1" selected="selected">成都</option>
   					</select>
   					</span>
				</li>
				<li>
					<strong>是否检查：</strong>
					<span><select name="phoneName"  class="easyui-combobox" id="phoneName"  value="" style="width:200px;">  
   						<option value="1" selected="selected">是</option>
   					</select>
   					</span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="phoneInfo-save" value="生成" class="imf_pop_btn_save" onClick="creatPhoneInfo('${pageContext.request.contextPath}/smsPhoneInfo/createPhoneList.do')"/></span>
				<span><input type="button" id="phoneInfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('phoneInfo-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="phoneInfo-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="phoneInfo-edit-info"><p></p></div>
		</form>
	</div>
</div>

