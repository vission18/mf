package ${packgeUrl};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import ${packgeInsUrl}${atfnBo.moudLowerCaseName}.db.${atfnBo.moudDbName};
import ${packgeBaseUrl}.hibernate.SimpleHibernateTemplate;
import ${packgeInsUrl}${atfnBo.moudLowerCaseName}.po.${atfnBo.moudPoName};
/**
 * 作者：lkj
 <#if atfnBo??>
 <#if atfnBo.moudExplainStr??>
 * 描述：${atfnBo.moudExplainStr}
 <#else>
 * 描述：${atfnBo.moudDaoName} 数据表模块
 </#if>
 </#if>
 * 日期：${cdate}
 * 类型：DAO文件
 */
@SuppressWarnings("serial")
@Service("${atfnBo.defVarName}Dao")
public class ${atfnBo.moudDaoName} extends
		SimpleHibernateTemplate<${atfnBo.moudPoName}, String> {

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;
	
	public ${atfnBo.moudDaoName}() {
		super(${atfnBo.moudPoName}.class);
	}

}
