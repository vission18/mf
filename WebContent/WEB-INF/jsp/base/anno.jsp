<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/anno.js"></script>
<script type="text/javascript">
	$(function() {
		annoDataGrid("${pageContext.request.contextPath}",
				"${accessButtons.data}", "${accessButtons.type}");
		initAnnoType();
		$("#anno-edit-win").unload(
			closeEditWindow("${pageContext.request.contextPath}")
		);
	});
</script>

<div class="easyui-layout" fit="true" id="anno-body">
	<!-- Search panel start -->
	<div id="anno-search-div"
		data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="annoSearchForm">
			<div class="searchmore">
				<label>公告标题:</label> <input type="text" name="annoTitle" value=""
					id="annosearchbytitle" class="imf_intxt" style="width:150px;" />
				<span class="imf_more"> <input id="annoBtnSearch"
					type="button" value="搜索" class="imf_searchmore" /> </span> <span
					class="imf_all"> <input id="annoBtnClean" type="button"
					value="显示全部" class="imf_showall" /> </span>
			</div>
		</form>
	</div>
	<!--  Search panel end -->
	
	<!--公告查看开始  -->
	<div id="anno-read-win" class="imf_pop" style="width:550px;">
		<div class="imf_pop_title"><strong>公告信息</strong><span class="imf_pop_closed" onClick="closeReadWindow()">关闭</span></div>
		<div class="imf_pop_con cl">
			<ul>
				<li><strong>&nbsp;&nbsp;公告类型：</strong> <span id = "annoTypeForRead"></span></li>
				<li><strong>&nbsp;&nbsp;公告标题：</strong> <span id = "annoTitleForRead"></span></li>
				<li><strong>&nbsp;&nbsp;发布用户：</strong> <span id = "loginNameForRead"></span></li>
				<li><strong>&nbsp;&nbsp;发布日期：</strong> <span id = "createDtForRead"></span></li>
				<li><strong>&nbsp;&nbsp;有效日期：</strong> <span id = "youxiaoDateForRead"></span></li>
				<li><strong>&nbsp;&nbsp;附加下载：</strong> <span id = "annoAcceForRead"></span></li>
				<li><strong>&nbsp;&nbsp;公告内容：</strong> <span><textarea id="annoContentForRead"
								 rows="100" cols="100" class="imf_textarea" style="min-width:300px;width:300px"></textarea></span></li>
			</ul>
		</div>
		<div class="imf_pop_btn">
			<span><input type="button" value="返回"  class="imf_pop_btn_closed" onClick="closeReadWindow()"/></span>
		</div>
	</div>
	<!-- 公告查看结束 -->


	<!-- Data List -->
	<div data-options="region:'center',border:false">
		<table id="anno-data-list"></table>
	</div>
</div>

<!-- Edit Win&Form -->
<div class="imf_pop" id="anno-edit-win" style="width:550px;">
	<div class="imf_pop_title">
		<strong>公告维护</strong> <span class="imf_pop_closed"
			onclick="closeEditWindow('${pageContext.request.contextPath}')">关闭</span>
	</div>
		<form id="annoEditForm" class="ui-form" method="post">
			<input type="hidden" name="annoId"/>
			<input type="hidden" name="typeName" id="annoTypeName">
			<div class="imf_pop_con">
				<ul>
					<li><strong>公告标题：</strong> <span> <input
						class="imf_intxt easyui-validatebox" type="text" value = "111"
						name="annoTitle" id="annoTitle"
						data-options="required:true,missingMessage:'请输入公告标题.'"
						maxlength="30" /></span>
					</li>
					<li><strong>公告类型：</strong> <span> 
						<input class="easyui-combobox" name="typeId" id="annoTypeId" style="width:250px;">
						</span>
					</li>
					<li><strong>公告状态：</strong> <span> <input
							name="annoStatus" type="radio" id= "youxiao" value="1" checked="checked"/><label>有效</label>
							<input name="annoStatus" type="radio" id= "wuxiao" value="0" /><label>无效</label>
					</span></li>
					<li>
					<strong>开始日期:</strong><span>
					<input class="Wdate imf_intxt" name="beginDt" type="text" id="beginDt" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/></span>
					</li>
					<li><strong>结束日期:</strong><span>
						<input class="Wdate imf_intxt" name="endDt" type="text" id="endDt" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/></span>
					</li>
					<li><strong>公告内容：</strong> <span> <textarea id="annoContent"
								data-options="required:true,missingMessage:'请输入公告内容.'"
								name="annoContent" rows="8" cols="70" class="imf_textarea"></textarea>
					</span></li>
					<li><strong>公告附件:</strong><span id="editAnnoAcceList">
						</span>
					</li>
			</ul>
		</div>
	</form>
	<form id="uploadform" method = "post" enctype="multipart/form-data" class="ui-form">
		<div class="imf_pop_con">
			<ul>
				<li><strong>上传附件：</strong> 
					<span>
						<input id="annoAcce" name="file" type="file" class="imf_intxt"/>
						<input type = "button" value = "上传" onclick="upload('${pageContext.request.contextPath}')"/>
					</span>
				</li>
			</ul>
		</div>
	</form>

	<div class="imf_pop_btn">
		<span> <input type="button" name="" value="保存"
			class="imf_pop_btn_save"
			onclick="annoSave('${pageContext.request.contextPath}'+'/anno/save.do')" />
		</span> <span> <input type="button" name="" value="关闭" id = "close"
			class="imf_pop_btn_closed" onclick="closeEditWindow('${pageContext.request.contextPath}')" />
		</span>
	</div>

	<div class="imf_pop_error" id="annoError">
		<p></p>
	</div>
	<div class="imf_pop_correct" id="annoInfo">
		<p></p>
	</div>

</div>