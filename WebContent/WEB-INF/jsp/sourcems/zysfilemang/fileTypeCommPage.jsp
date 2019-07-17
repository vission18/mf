<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" 
	src="${pageContext.request.contextPath}/js/sourcems/zysfilemang/fileTypeComm.js">
</script>

<script type="text/javascript">
	$(function() {
		var baseUrl = "${pageContext.request.contextPath}";
		fileTypeDataGrid("${pageContext.request.contextPath}","${accessButtons.data}","${accessButtons.type}","${fileTypeList}");
	});
</script>
<div class="easyui-layout" fit="true" id="${fileTypeList}-mainBody">

	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="left" split="true">
		<form id="${fileTypeList}SearchForm">
			<div class="searchmore">
				<label>资源匹配:</label>
				<input class="imf_intxt" id="${fileTypeList}KeyWord" style="width:180px;" name="fileNm" type="text"/>
				<span class="imf_more"><input id="${fileTypeList}BtnSearch" type="button" value="搜索" class="imf_searchmore"/></span>						
				<span class="imf_all"><input id="${fileTypeList}BtnClean" type="button" value="显示全部" class="imf_showall"/></span>
			</div>
		</form>
	</div>
	
	<div data-options="region:'center',border:false" style="padding:5px;">
		<table id="${fileTypeList}-data-list"></table>
	</div>
	
	<div id="${fileTypeList}-download-win" class="imf_pop" style="width:500px;">
		<div class="imf_pop_title"><strong>网盘下载</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-download-win')">关闭</span></div>
		<form id="${fileTypeList}-download-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li><strong>网盘地址：</strong><span id="${fileTypeList}OpenUrl"></span></li>
				<li><strong>提取码：</strong>
					<span>
						<input  id="${fileTypeList}OpenExtCode" maxlength="100" class="imf_intxt easyui-validatebox" type="text" disabled="disabled">
					</span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-download-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-download-win')"/></span>
			</div>
			<div class="imf_pop_error" id="${fileTypeList}-download-error"><p></p></div>
			<div class="imf_pop_correct" id="${fileTypeList}-download-info"><p></p></div>
		</form>
	</div>
	
	<div id="${fileTypeList}-adminInfo-win" class="imf_pop" style="width:450px;">
		<div class="imf_pop_title"><strong>联系作者</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-adminInfo-win')">关闭</span></div>
		<form id="${fileTypeList}-adminInfo-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li><span id="${fileTypeList}-contackAuthor" style="color:red">
				</span></li>
				<li>
					<div  align="center">
						<img id="${fileTypeList}-authorVxImg" src="${pageContext.request.contextPath}/images/adminWeChat.jpg" title="微信" width="180" height="180"/>
					</div>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-adminInfo-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-adminInfo-win')"/></span>
			</div>
		</form>
	</div>
	<!-- 联系管理员 -->
	<div id="${fileTypeList}-manager-win" class="imf_pop" style="width:450px;">
		<div class="imf_pop_title"><strong>联系管理员</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-manager-win')">关闭</span></div>
		<form id="${fileTypeList}-manager-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li><span id="${fileTypeList}-contackManager" style="color:red">
					联系人：小k老师</br>
					电话：17808176853</br>
					邮箱：1084636052@qq.com</br>
				</span></li>
				<li>
					<div  align="center">
						<img id="${fileTypeList}-manager" src="${pageContext.request.contextPath}/images/adminWeChat.jpg" title="微信" width="195" height="275"/>
					</div>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-manager-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-manager-win')"/></span>
			</div>
		</form>
	</div>
	<!-- 查看详细 -->
	<div id="${fileTypeList}-zysFileMan-edit-win" class="imf_pop" style="width:720px;">
		<div class="imf_pop_title"><strong>文件详细信息</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-zysFileMan-edit-win')">关闭</span></div>
		<form id="${fileTypeList}-zysFileManEditForm" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<input type="hidden" id="${fileTypeList}-pkId" name="pkId">
			<ul>
				<li>
					<strong>文件名称:</strong>
					<span>
						<input id="${fileTypeList}-fileNm" name="fileNm"  type="text" maxlength="100" class="imf_intxt" style="width:515px;" disabled="disabled"/>
					</span>
				</li>
				<li>
					<strong>原价:</strong>
					<span>
						<input  id="${fileTypeList}-costPrice" name="costPrice" maxlength="100" class="imf_intxt easyui-validatebox" value="0" type="text" disabled="disabled" >
					</span>
					<strong>活动价:</strong>
					<span>
						<input  id="${fileTypeList}-nowPrice" name="nowPrice" maxlength="100" class="imf_intxt easyui-validatebox" value="0" type="text" disabled="disabled" >
					</span>
				</li>
				<li>
					<strong>压缩密码:</strong>
					<span>
						<input  id="${fileTypeList}-compPwd" value="******" maxlength="100" class="imf_intxt easyui-validatebox" type="text" disabled="disabled" >
					</span>
					<strong>网盘类型:</strong>
					<span>
						<select id="${fileTypeList}-skyDriveType" class="imf_intxt easyui-combobox" name="skyDriveType" editable="false" style="width:200px;" disabled="disabled">  
						    <option value="01">百度网盘</option>  
						    <option value="02">诚通网盘</option>  
						    <option value="03">360网盘</option>  
						</select>  
					</span>
				</li>
				<li>
					<strong>网盘地址:</strong>
					<span>
						<input  id="${fileTypeList}-skyDriveUrl" name="skyDriveUrl" maxlength="200" class="imf_intxt easyui-validatebox" type="text" disabled="disabled" >
					</span>
					<strong>提取码:</strong>
					<span>
						<input  id="${fileTypeList}-skyDriveExtCode" name="skyDriveExtCode" maxlength="100" class="imf_intxt easyui-validatebox" type="text" disabled="disabled" >
					</span>
				</li>
				<li>
					<strong>其他链接地址:</strong>
					<span>
						<input  id="${fileTypeList}-otherUrl" name="otherUrl" maxlength="200" class="imf_intxt easyui-validatebox" type="text" disabled="disabled" >
					</span>
				</li>
				<li>
					<strong>文件说明:</strong>
					<span> <textarea id="${fileTypeList}-fileRemark" name="fileRemark" rows="8" cols="70" class="imf_textarea" disabled="disabled"  style="width:515px;"></textarea> </span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-zysFileMan-zan" value="给个好评" class="imf_pop_btn_save" onClick="updateZan('${pageContext.request.contextPath}','${fileTypeList}')"/></span>
				<span><input type="button" id="${fileTypeList}-zysFileMan-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-zysFileMan-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="${fileTypeList}zysFileMan-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="${fileTypeList}zysFileMan-edit-info"><p></p></div>
		</form>
	</div>
	<!-- 分享说明 -->
	<div id="${fileTypeList}-share-win" class="imf_pop" style="width:550px;">
		<div class="imf_pop_title"><strong>分享说明</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-share-win')">关闭</span></div>
		<form id="${fileTypeList}-share-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li><span id="${fileTypeList}-sharedesc" style="color:red">
				1：免费注册账号，待管理员通过审批后即可使用；</br>
				&nbsp;&nbsp; &nbsp;&nbsp;注：申请后审批不超过一个工作日，申请后请联系管理员及时审批</br>
				2：用自己的账号登录，进入“我的文件”即可分享自己的资源；</br>
				3：详细使用请点击左下角 <a href="javascript:void(0);" style="height:30px;" title="帮助" class="imf_help">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a> 
					按钮，查看帮助文档</br>
				</span></li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-login-btn" value="我要注册" class="imf_pop_btn_save" onClick="openLoginWin('${fileTypeList}')"/></span>
				<span><input type="button" id="${fileTypeList}-share-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-share-win')"/></span>
			</div>
		</form>
	</div>
	<!-- 我要赚钱 -->
	<div id="${fileTypeList}-makeMonney-win" class="imf_pop" style="width:550px;">
		<div class="imf_pop_title"><strong>分享赚钱</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-makeMonney-win')">关闭</span></div>
		<form id="${fileTypeList}-makeMonney-form" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li><span id="${fileTypeList}-makeMonney" style="color:red">
				1：免费注册账号，待管理员通过审批后即可使用.</br>
				&nbsp;&nbsp; &nbsp;&nbsp;注：申请后审批不超过一个工作日，审批通过后管理员以微信的方式通知您.</br>
				2：用自己的账号登录，进入“我的文件”新增资源，可自定义价格，详细案例请参考操作手册.</br>
				3：源家是一个资源共享平台，希望大家共同维护我们的家园，营造一个和谐、健康、互助的网络环境.</br>
				</span></li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-login-makeMonney-btn" value="我要注册" class="imf_pop_btn_save" onClick="openLoginWin('${fileTypeList}')"/></span>
				<span><input type="button" id="${fileTypeList}-makeMonney-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-makeMonney-win')"/></span>
			</div>
		</form>
	</div>
	<!-- 用户注册 -->
	<div id="${fileTypeList}-user-edit-win" class="imf_pop" style="width:550px;">
		<div class="imf_pop_title"><strong>用户注册</strong><span class="imf_pop_closed" onClick="popClosed('${fileTypeList}-user-edit-win')">关闭</span></div>
		<form id="${fileTypeList}EditForm" class="ui-form" method="post" enctype="multipart/form-data">
			<div class="imf_pop_con">
			<ul>
				<li>
					<strong>登录名：</strong>
					<span><input class="imf_intxt easyui-validatebox"  data-options="required:true,missingMessage:'请输入登录名称.'" type="text" id="${fileTypeList}_login_name"
						name="loginName"></span>
				</li>
				<li>
					<strong>用户名：</strong>
					<span><input class="imf_intxt easyui-validatebox"  data-options="required:true,missingMessage:'请输入用户名称.'" type="text" id="${fileTypeList}_user_name" name="userName" data-options="required:true"></span>
				</li>
				<li>
					<strong>联系方式：</strong>
					<span>
					<input id="${fileTypeList}_user_tel" class="imf_intxt easyui-numberbox" type="text" name="userTel">
					</span>
				</li>
				<li>
					<strong>移动电话：</strong>
					<span>
					<input class="imf_intxt easyui-numberbox" type="text" name="userMobTel" id="user-mobile">
				</span>
				</li>
				<li>
					<strong>邮箱地址：</strong>
					<span>
					<input class="imf_intxt easyui-validatebox"  type="text" name="userEmail" id="${fileTypeList}_user_email" >
					</span>
				</li>
				<li>	
					<strong>二维码：</strong>
					<span><input id="${fileTypeList}_imgFile" name="file" type="file" class="imf_intxt"/></span>
				</li>
			</ul>
			</div>
			<div class="imf_pop_btn">
				<span><input type="button" id="${fileTypeList}-user-save" value="注册" class="imf_pop_btn_save" onClick="saveLogin('${pageContext.request.contextPath}','${fileTypeList}')"/></span>
				<span><input type="button" id="${fileTypeList}-user-close" value="关闭" class="imf_pop_btn_closed" onClick="popClosed('${fileTypeList}-user-edit-win')"/></span>
			</div>
			<div class="imf_pop_error" id="${fileTypeList}-user-edit-error"><p></p></div>
			<div class="imf_pop_correct" id="${fileTypeList}-user-edit-info"><p></p></div>
		</form>
	</div>
	
</div>

