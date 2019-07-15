<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/base/menu.js"></script>
<script type="text/javascript">
	$(function() {
		menuDataGrid('${pageContext.request.contextPath}/menu/menuTreeNode.do',
				'${pageContext.request.contextPath}','${accessButtons.data}','${accessButtons.type}');
		$('#menu-btnSave').click(function() {
			menuSaveFunction('${pageContext.request.contextPath}');
		});
		$('#menuIconCombobox').combobox({
			data : iconData,
			formatter : function(v) {
				return formatString('<span class="{0}" style="display:inline-block;vertical-align:middle;width:16px;height:16px;"></span>{1}', v.value, v.value);
			}
		});
	});
</script>
<div class="easyui-layout" fit="true" id="menu-body">
<div data-options="region:'center',border:false">
	<table id="menu-data-list"></table>
</div>
<!-- Edit Win&Form -->
<div id="menu-edit-win" class="imf_pop" style="width:440px;">
		<div class="imf_pop_title">
			<strong>菜单管理：</strong><span class="imf_pop_closed" onClick="popClosed('menu-edit-win')">关闭</span>
		</div>
	<form id="menu-editForm" class="ui-form" method="post" >
		 <input type="hidden" name="menuId" id="menuId"> 
		<div class="imf_pop_con">
		<ul>
			<li>
				<strong>菜单名称：</strong> 
				<span>
				<input class="imf_intxt easyui-validatebox" type="text" id="menuName"
					name="menuName" data-options="required:true">
				</span>
			</li>
			<li>
			<strong>菜单类型 ：</strong>  
			<span>
			<select name="menuType" id="menuType" style="width:100px;">  
    			<option value="0">菜单</option>
    			<option value="9">按钮</option>
    			<option value="1">连接</option>  
   			</select>
   			</span>  
			</li>
			<li>
			<strong>菜单图标：</strong>
			<span>
				<input id="menuIconCombobox" name="iconCls"  class="imf_intxt easyui-validatebox" />
			</span>
			</li>
			<li>
				<strong>菜单URL：</strong> 
				<span>
				<input class="imf_intxt easyui-validatebox" type="text" id="menuUrl"
					name="menuUrl">
				</span>
			</li>
			<li>
				<strong>菜单actions ：</strong> 
				<span> 
				<input class="imf_intxt easyui-validatebox" type="text" id="actions"
					name="actions">
				</span>
			</li>
			<li>
				<strong>上级菜单：</strong> 
				<span>
				<input  name="parentMenuId"  id="parentMenuId"
					class="easyui-combotree" style="width:200px;"
					data-options="lines : true,required:false"
					 />
				</span>
			</li>
			<li>
			<strong>是否新开：</strong> 
			<span>
				<input name="newOpen" type="radio" value="1"  checked="checked"  id="newOpen1"/><label>是</label>
				<input name="newOpen" type="radio" value="0" id="newOpen2" /><label>否</label>
			</span>
			</li>
			<li>
				<strong>菜单顺序：</strong> 
				<span> <input id="sssmenuOrder" class="imf_intxt easyui-validatebox" name="menuOrder"> </span>
			</li>
			<li style="display:none" id="divbuttonType">
				<strong>按钮类型 ：</strong> 
				<span>
				 <input class="imf_intxt easyui-validatebox" type="text"  id="buttonType"
					name="buttonType">
				</span>
			</li>
			</ul>
		</div>
		<div class="imf_pop_btn">
				<span>
				<input type="button" name="" value="保存"
					class="imf_pop_btn_save" onclick='menuSaveFunction("${pageContext.request.contextPath}")' /> 
				</span> 
				<span>
				<input
					type="button" name="" value="关闭" class="imf_pop_btn_closed"
					onclick="popClosed('menu-edit-win')" />
				</span>
		</div>
			<div class="imf_pop_error" id="menuError">
				<p></p>
			</div>
			<div class="imf_pop_correct" id="menuInfo">
				<p></p>
			</div>
		
	</form>
</div>
</div>