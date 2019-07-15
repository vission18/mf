<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/acf/modinfo/modInfo.js"></script>
<script type="text/javascript">
 	$(function(){
 		var baseUrl = "${pageContext.request.contextPath}";
 		colsDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}");
	}); 
</script>
<div class="easyui-layout imf_table_wrap" fit="true" id="table-mainBody">
	<div data-options="region:'center',border:true">
		<div class="imf_table_wrap">
			<div class="tabtab">
				<ul id="table-subtab-ul">
					<li id="modLi"><a href="javascript:void(0);" class="cur" onclick="showIndTab('mod')">模块信息</a></li>
					<li id="colsLi"><a href="javascript:void(0);" onclick="showIndTab('cols')" >字段对应关系</a></li>
				</ul>
			</div>
		</div>
		<div class="tablescon">
			<!-- 模块页面 -->
			<div id="mod-tabcon1" class="tabcon1">
				<div class="tabconwrap">
					<div class="imf_tablecon1">
						<form id="modForm" class="ui-form" method="post">
				  			<input type="hidden" id="modId" name="modId">
				  			<input type="hidden" id="saveFleg" name="saveFleg">
				  			<input type="hidden" id="colList" name="colList" />
				  			<ul>
				    			<li><strong> 表中文名：</strong> 
				    				<span> <input name="modChaName" style="width:250px;" class="imf_intxt easyui-validatebox" type="text" /> </span>
								</li>
								<li><strong> 表英文名：</strong> 
				    				<span> <input name="modEngName" style="width:250px;" class="imf_intxt easyui-validatebox" type="text" /> </span>
								</li>
								<li><strong> 文件包名前缀：</strong> 
				    				<span> <input name="packageUrlIns" style="width:250px;" class="imf_intxt easyui-validatebox" type="text" /> </span>
								</li>
								<li><strong> 模块说明：</strong>
									<span> <textarea name="remark" rows="8" cols="70" class="imf_textarea"></textarea> </span>
								</li>
								<li><input type="button" class="imf_button" value="保存" onclick="saveMod('${pageContext.request.contextPath}')"></li>
				  			</ul>
				  			<div class="imf_pop_error" id="modError"><p></p></div>
							<div class="imf_pop_correct" id="modInfo"><p></p></div>
						</form>
					</div>
				</div>
			</div>
			<!-- 字段页面 -->
			<div id="cols-tabcon1" class="tabcon1">
				<div class="tabconwrap">
					<div class="imf_tablecon1">
						<div class="easyui-layout" fit="true" id="colsInfo-mainBody">
							<div data-options="region:'center',border:false" style="padding:5px;">
								<table id="colsInfo-data-list"></table>
							</div>
							<div id="colsInfo-edit-win" class="imf_pop" style="width:500px;height:400px;">
								<div class="imf_pop_title">新增字段<span class="imf_pop_closed" onClick="popClosed('colsInfo-edit-win')">关闭</span></div>
								<form id="colsInfoEditForm" class="ui-form" method="post">
									<div class="imf_pop_con">
									<ul>
										<li>
											<span>
												<strong>字段英文名：</strong>
												<input  id="colEngName" name="colEngName" style="width:200px;" maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
											</span>
										</li>
										<li>	
											<strong>字段中文名：</strong>
											<span>
												<input  id="colChaName" name="colChaName" style="width:200px;"  maxlength="100" class="imf_intxt easyui-validatebox" type="text" >
											</span>
										</li>
										<li>
											<strong>字段类型：</strong>
								    		<span>
												<select name="colType" class="easyui-combobox" style="width:200px;"  id="colType" editable="false">  
						    						<option value="CHAR">CHAR</option>
						    						<option value="CLOB">CLOB</option>
						    						<option value="DATE">DATE</option>
						    						<option value="DECIMAL">DECIMAL</option>
						    						<option value="INTEGER">INTEGER</option>
						    						<option value="VARCHAR" selected="selected">VARCHAR</option>   
						    						<option value="TIMESTAMP">TIMESTAMP</option>  
						   						</select>
											</span>
										</li>
										<li>
											<strong>默认值：</strong>
											<span>
												<input class="imf_intxt"  id="defaultVal" style="width:200px;" name="defaultVal" type="text" />
											</span>
										</li>
										<li>
											<strong>是否主键：</strong>
								    		<span>
												<input name="isPk" type="radio" value="1" id="isPk1"/><label>是</label>
												<input name="isPk" type="radio" value="0" checked="checked" id="isPk0"/><label>否</label>
											</span>
										</li>
										<li>
											<strong>允许为空：</strong>
								    		<span>
												<input name="isNull" type="radio" value="1" checked="checked" id="isNull1"/><label>是</label>
												<input name="isNull" type="radio" value="0" id="isNull0"/><label>否</label>
											</span>
										 </li>
										  <li>
											<strong>字段描述：</strong>
								    		<span><textarea id="colDesc" name="colDesc" maxlength="512"
												 rows="15" cols="60" class="imf_textarea" style="width:200px;height:40px;"></textarea>
											</span>
										 </li>
									</ul>
									</div>
									<div class="imf_pop_btn">
										<span><input type="button" id="colsInfo-save" value="保存" class="imf_pop_btn_save" onClick="saveColInfo('${pageContext.request.contextPath}')"/></span>
										<span><input type="button" id="colsInfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('colsInfo-edit-win')"/></span>
									</div>
									<div class="imf_pop_error" id="colsInfo-edit-error"><p></p></div>
									<div class="imf_pop_correct" id="colsInfo-edit-info"><p></p></div>
								</form>
							</div>
						</div>


					</div>
				</div>
			</div>
		</div>
	</div>
</div>

