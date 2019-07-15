package ${packgeUrl};

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
 <#if atfnBo??>
import ${packgeInsUrl}${atfnBo.moudLowerCaseName}.db.${atfnBo.moudDbName};
 </#if>

/**
 * 作者：lkj
 <#if atfnBo??>
 <#if atfnBo.moudExplainStr??>
 * 描述：${atfnBo.moudExplainStr}
 <#else>
 * 描述：${atfnBo.moudDbName} 模块Po
 </#if>
 </#if>
 * 日期：${cdate}
 * 类型：Po文件
 */
 
<#if atfnBo??>
@Entity
@Table(name = ${atfnBo.moudDbName}.TABLE_NAME)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler","fieldHandler" })
public class ${atfnBo.moudPoName} {

	private static final long serialVersionUID = 1L;
	
    <#if atfnBo.acnList?exists >
	<#list atfnBo.acnList as acn>
	//${acn.colDesc}
    private ${acn.cloType} ${acn.p_colTfNames};
    
    <#if acn.checkIsPk ==1  >
    @Id
	@GeneratedValue(generator = "uuid-gen")
	@GenericGenerator(name = "uuid-gen", strategy = "uuid")
    </#if>
	@Column(name = ${atfnBo.moudDbName}.${acn.colNameToUpcase}<#if acn.checkIsNull ==1  >,nullable = false</#if>)
	public ${acn.cloType} get${acn.colTfNames}() {
		return ${acn.p_colTfNames};
	}
	public void set${acn.colTfNames}(${acn.cloType} ${acn.p_colTfNames}) {
		this.${acn.p_colTfNames} = ${acn.p_colTfNames};
	}
	</#list>
	</#if>
}
</#if>

