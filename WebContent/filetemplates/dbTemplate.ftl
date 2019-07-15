package ${packgeUrl};

/**
 * 作者：lkj
 <#if atfnBo??>
 <#if atfnBo.moudExplainStr??>
 * 描述：${atfnBo.moudExplainStr}
 <#else>
 * 描述：${atfnBo.moudDbName} 数据表模块
 </#if>
 </#if>
 * 日期：${cdate}
 * 类型：DB文件
 */
<#if atfnBo??>
public class ${atfnBo.moudDbName} {
	// 库表信息
	public static final String TABLE_NAME = "${atfnBo.moudDbName}";
    <#if atfnBo.acnList?exists >
	<#list atfnBo.acnList as acn>
	public static final String ${acn.colNameToUpcase} = "${acn.colNameToUpcase}";
	</#list>
	</#if>
}
</#if>
