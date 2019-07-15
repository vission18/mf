<%@ page language="java" pageEncoding="UTF-8"%>
<!-- Js Start -->
<script type="text/javascript">
	var getAllType=baseUrl + '/xfRecordController/getAllType.do';
	$('#sumType').combobox( {
		url : getAllType,
		valueField : "typeId",
		textField : "typeName",
	});
	$('#xfMoneySumBtnCalculate').click(function(){// 计算
		$.ajax('${pageContext.request.contextPath}/xfRecordController/calculateMoney.do', {
			type:'post',
		 	dataType:'json',
		 	data:serializeObject($('#xfMoneySumSearchForm')),
		 	success:function(result){
		 	if (result.success) {
				$('#caculateMoney').val(result.message);
			}else{
				$('#caculateMoney').val('');
				$.messager.alert("提示",result.message);
			}
			}
		});	
	});
</script>
<div class="easyui-layout" fit="true" id="xfMoneySum-mainBody">
	<!-- Search panel start -->
	<div data-options="region:'north',title:'',border:false"
		style="overflow:hidden;padding:10px;" align="center" split="true">
		<form id="xfMoneySumSearchForm">
			<div class="searchmore">
				<label>消费类别:</label>
				<select  class="imf_intxt easyui-combobox"  id="sumType" name="xfType" style="width:100px"></select>
				<label>查询日期:</label>
				<input class="Wdate imf_intxt"  style="width:180px;" name="xfDate" type="text" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})"/>
				<label>--</label>
				<input class="Wdate imf_intxt"  style="width:180px;" name="recordDate" type="text" onClick="WdatePicker({dateFmt:'yyyy/MM/dd'})"/>
				&nbsp;
				<label>合计:</label>
				 <input type="text" id="caculateMoney" value="" class="imf_intxt"	style="width: 150px;" />
				<label>元</label>
				&nbsp;
				<span class="imf_more"><input id="xfMoneySumBtnCalculate" type="button" value="计算" class="imf_searchmore"/></span>
			</div>
		</form>
	</div>
</div>

