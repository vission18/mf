<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.vission.mf.base.enums.BaseConstants"%>
<%
	String stylePath = BaseConstants.getStylePath();//样式路径
%>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/base/branch.js"></script>
	<script type="text/javascript">
		$(function(){
		var baseUrl='${pageContext.request.contextPath}';
			dataGrid('${pageContext.request.contextPath}','${accessButtons.data}','${accessButtons.type}');
		});	
		$('#branchNo').validatebox({
			validType:'checkEngAndNum'
		});
	</script>
	<div class="easyui-layout" id="branch-layout" fit="true">
     <div data-options="region:'center',border:false">
		<table id="branch-data-list"></table>
	 </div>
     <!-- Edit Win&Form -->
     <div id="branch-edit-win" class="imf_pop" style="width:440px;">
	     <div  class="imf_pop_title"><strong>机构维护</strong><span class="imf_pop_closed" onclick="popClosed('branch-edit-win')">关闭</span></div>
	     	<form id="branch-editForm" class="ui-form" method="post">  
	     		<input id="upBranchNo" name="upBranchNo" type="hidden"/>
		        <input  name="leaf" type="hidden"/>
	     		<div class="imf_pop_con">
	     			<ul>
						<li><strong>机构代码：</strong>
							<span>
								 <input type="text" id="branchNo-Text" disabled="disabled" class="imf_intxt">
		             			 <input type="hidden" name="branchNo">
							</span>
						</li>
						<li><strong>机构名称：</strong>
							<span>
								<input class="imf_intxt easyui-validatebox" id="branchName-edit" type="text" name="branchName" data-options="required:true,missingMessage:'请输入机构名称。'">
							</span>
						</li>
						<li><strong>机构简称：</strong>
							<span>
								<input class="imf_intxt easyui-validatebox" type="text" id="branchShortName-edit" name="branchShortName" data-options="required:true,missingMessage:'请输入机构简称。'">
							</span>
						</li>
						<li><strong>上级机构：</strong>
							<span>
								<input id="upbranchNo-tree" name="upBranchName" style="width:250px;" class="easyui-combotree"/>
							</span>
							<a href="javascript:;" class="imf_li_a" onclick="cleanTreeValue('upbranchNo-tree','','upBranchNo','-1');" style="display:inline-block;vertical-align:middle;"><img src="<%=stylePath%>/images/icons/clean.gif"  title="清空"/></a>
						</li>
					</ul>
		         </div>
		         <div class="imf_pop_btn">
					<span><input type="button" name="" value="保存" class="imf_pop_btn_save" onclick="saveFunction('${pageContext.request.contextPath}')"/></span>
					<span><input type="button" name="" value="关闭" class="imf_pop_btn_closed" onclick="popClosed('branch-edit-win')"/></span>
				</div>
				<div class="imf_pop_error" id="branchEditError"><p></p></div>
				<div class="imf_pop_correct" id="branchEditInfo"><p></p></div>
	     	</form>
  	 </div>
  	 <div id="branch-add-win" class="imf_pop" style="width:440px;">
  	  <div  class="imf_pop_title"><strong>机构新增</strong><span class="imf_pop_closed" onclick="addWinClosed('${pageContext.request.contextPath}','branch-add-win')">关闭</span></div>
     	<form id="branch-addForm" class="ui-form" method="post">  
     		<input name="leaf" id="add-leaf" type="hidden"/>
     		<div class="imf_pop_con">
     			<ul>
     				<li><strong>机构代码：</strong>
						<span>
							<input id="branchNo" name="branchNo" class="imf_intxt easyui-validatebox" data-options="required:true,missingMessage:'请输入机构代码。'"/>
						</span>
					</li>
					<li><strong>机构名称：</strong>
						<span>
							<input id="branchName-add" name="branchName" class="imf_intxt easyui-validatebox" data-options="required:true,missingMessage:'请输入机构名称。'"/>
						</span>
					</li>
					<li><strong>机构简称：</strong>
						<span>
							<input id="branchShortName-add" name="branchShortName" class="imf_intxt easyui-validatebox" data-options="required:true,missingMessage:'请输入机构简称。'"/>
						</span>
					</li>
					<li><strong>上级机构：</strong>
							<span>
								<input id="upbranchNo-add-tree" name="upBranchNo" style="width:250px;" class="easyui-combotree"/>
							</span>
							<a href="javascript:;" class="imf_li_a" onclick="cleanTreeValue('upbranchNo-add-tree','','','');" style="display:inline-block;vertical-align:middle;"><img src="<%=stylePath%>/images/icons/clean.gif" title="清空"/></a>
					</li>
     			</ul>
	         </div>
         	 <div class="imf_pop_btn">
				<span><input type="button" name="" value="保存" class="imf_pop_btn_save" onclick="insertFunction('${pageContext.request.contextPath}')"/></span>
				<span><input type="button" name="" value="关闭" class="imf_pop_btn_closed" onclick="addWinClosed('${pageContext.request.contextPath}','branch-add-win')"/></span>
			</div>
			<div class="imf_pop_error" id="branchAddError"><p></p></div>
			<div class="imf_pop_correct" id="branchAddInfo"><p></p></div>
     	</form>
  	 </div> 
</div>