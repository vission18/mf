package ${packgeUrl};

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ${packgeBaseUrl}.hibernate.CriteriaSetup;
import ${packgeBaseUrl}.model.bo.DataGrid;
import ${packgeBaseUrl}.service.BaseService;
import ${packgeInsUrl}${atfnBo.moudLowerCaseName}.po.${atfnBo.moudPoName};
import ${packgeInsUrl}${atfnBo.moudLowerCaseName}.dao.${atfnBo.moudDaoName};
import ${packgeBaseUrl}.exception.ServiceException;

/**
 * 作者：acf
 <#if atfnBo??>
 <#if atfnBo.moudExplainStr??>
 * 描述：${atfnBo.moudExplainStr}
 <#else>
 * 描述：${atfnBo.moudServiceName} 业务逻辑处理
 </#if>
 </#if>
 * 日期：${cdate}
 * 类型：SERVICE文件
 */
@Service("${atfnBo.defVarName}Service")
@Transactional
public class ${atfnBo.moudServiceName} extends BaseService {

	@Autowired
	private ${atfnBo.moudDaoName} ${atfnBo.defVarName}Dao;


	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(${atfnBo.moudPoName} po, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(po, filterMap); // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件

		Map<String, String> orderMap = new HashMap<String, String>(1);
		//orderMap.put("loginName", CriteriaSetup.ASC);
		dataGrid = ${atfnBo.defVarName}Dao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);
		return dataGrid;// 空对象 页尺寸 map类型的查询条件 查询条件
	}


	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(${atfnBo.moudPoName} po, Map<String, Object> filterMap) {
		
	    <#if atfnBo.acnList?exists >
		<#list atfnBo.acnList as acn>

		if (po.get${acn.colTfNames}() != null && !po.get${acn.colTfNames}().trim().equals("")
			&& !po.get${acn.colTfNames}().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "${acn.p_colTfNames}",
					po.get${acn.colTfNames}());
		}
		</#list>
		</#if>
	}

	@Transactional(readOnly = true)
	public ${atfnBo.moudPoName} get${atfnBo.moudPoName}ById(String pkId) {
		return ${atfnBo.defVarName}Dao.get(pkId);
	}
	
	/**
	 * 删除
	 */
	public void delete${atfnBo.moudPoName}ById(String pkId) throws ServiceException {
		this.${atfnBo.defVarName}Dao.delete(this.get${atfnBo.moudPoName}ById(pkId));
	}
	
	/**
	 * 保存
	 */
	public void save${atfnBo.moudPoName}(${atfnBo.moudPoName} po,String operType) throws ServiceException {
		if("update".equals(operType)){
			this.${atfnBo.defVarName}Dao.onlyUpdate(po);
		}else{
			this.${atfnBo.defVarName}Dao.onlySave(po);
		}
	}
}
