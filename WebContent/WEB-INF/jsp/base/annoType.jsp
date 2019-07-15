<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/annoType.js"></script>
<script type="text/javascript">
	$(function() {
		annoTypeDataGrid("${pageContext.request.contextPath}",
				"${accessButtons.data}", "${accessButtons.type}");
	});
</script>

<div class="easyui-layout" fit="true" id="annoType-body">
	<!-- Search panel start -->
	<div id="annoType-search-div"
		data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="annoTypeSearchForm">
			<div class="searchmore">
				<label>类型名称:</label> <input type="text" name="typeName" value=""
					id="annoTypesearchbytitle" class="imf_intxt" style="width:150px;" />
				<span class="imf_more"> <input id="annoTypeBtnSearch"
					type="button" value="搜索" class="imf_searchmore" /> </span>
				 <span class="imf_all"> <input id="annoTypeBtnClean" type="button"
					value="显示全部" class="imf_showall" /> </span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->

	<!-- Data List -->
	<div data-options="region:'center',border:false">
		<table id="annoType-data-list"></table>
	</div>
</div>
	
<!-- Edit Win&Form -->
	<div class="imf_pop" id="annoType-edit-win" style="width:480px;">
		<div class="imf_pop_title">
			<strong>公告类型维护</strong> <span class="imf_pop_closed"
				onClick="popClosed('annoType-edit-win')">关闭</span>
		</div>
		<form id="annoTypeEditForm" class="ui-form" method="post">
			<input type="hidden" name="typeId"/>
			<div class="imf_pop_con" >
				<ul>
					<li><strong>类型名称：</strong> <span> <input
							class="imf_intxt easyui-validatebox" type="text" name="typeName" 
							id="typeName" data-options="required:true,missingMessage:'请输入公告类型名称.'" maxlength="20"/> </span></li>
					<li><strong>类型描述：</strong> <span> 
							<textarea name="typeRmk" rows="8" cols="70" maxlength="120" class="imf_textarea"></textarea>
						</span>
					</li>
				</ul>
			</div>
			<div class="imf_pop_btn">
				<span> <input type="button" name="" value="保存"
					class="imf_pop_btn_save"
					onclick="annoTypeSave('${pageContext.request.contextPath}'+'/annoType/save.do')" />
				</span> <span> <input type="button" name="" value="关闭"
					class="imf_pop_btn_closed"
					onclick="popClosed('annoType-edit-win')" /> </span>
			</div>

			<div class="imf_pop_error" id="annoTypeError">
				<p></p>
			</div>
			<div class="imf_pop_correct" id="annoTypeInfo">
				<p></p>
			</div>
		</form>
	</div>
