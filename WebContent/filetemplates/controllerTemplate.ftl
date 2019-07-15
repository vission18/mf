package ${packgeUrl};

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;
import ${packgeBaseUrl}.controller.BaseController;
import ${packgeBaseUrl}.enums.BaseConstants;
import ${packgeBaseUrl}.model.bo.AjaxResult;
import ${packgeBaseUrl}.model.bo.DataGrid;
import ${packgeBaseUrl}.model.bo.SessionInfo;
import ${packgeBaseUrl}.model.bo.Tree;
import ${packgeBaseUrl}.util.DateUtil;
import ${packgeBaseUrl}.util.Encrypt;
import ${packgeBaseUrl}.util.FileUtil;
import ${packgeBaseUrl}.util.RegexUtil;
import ${packgeBaseUrl}.exception.ServiceException;

import ${packgeInsUrl}${atfnBo.moudLowerCaseName}.service.${atfnBo.moudServiceName};
import ${packgeInsUrl}${atfnBo.moudLowerCaseName}.po.${atfnBo.moudPoName};

/**
 * 作者：acf
 <#if atfnBo??>
 <#if atfnBo.moudExplainStr??>
 * 描述：${atfnBo.moudExplainStr}
 <#else>
 * 描述：${atfnBo.moudControllerName} 控制层
 </#if>
 </#if>
 * 日期：${cdate}
 * 类型：CONTROLLER文件
 */
@Controller
@RequestMapping("/${atfnBo.defVarName}")
public class ${atfnBo.moudControllerName} extends BaseController {

	@Autowired
	private ${atfnBo.moudServiceName} ${atfnBo.defVarName}Service;

	/**
	 * 跳转至jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "${inJsPath}/${atfnBo.moudLowerCaseName}/${atfnBo.defVarName}List";
	}

	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/${atfnBo.defVarName}DataGrid")
	@ResponseBody
	public DataGrid ${atfnBo.defVarName}DataGrid(HttpServletRequest request, ${atfnBo.moudPoName} po) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return ${atfnBo.defVarName}Service.dataGrid(po, pageNo, pageSize);
	}


	/**
	 * 获取一条数据
	 */
	@RequestMapping(value = "/get${atfnBo.moudPoName}ById")
	@ResponseBody
	public AjaxResult get${atfnBo.moudPoName}ById(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		String pkId = request.getParameter("PK_ID");
		ajaxResult.setData(${atfnBo.defVarName}Service.get${atfnBo.moudPoName}ById(pkId));
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete${atfnBo.moudPoName}ById", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {	
			String pkId = request.getParameter("PK_ID");
			${atfnBo.defVarName}Service.delete${atfnBo.moudPoName}ById(pkId);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功！");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("删除失败！");
		}
		return ajaxResult;
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save${atfnBo.moudPoName}", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult save${atfnBo.moudPoName}(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, ${atfnBo.moudPoName} po)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		try {
			if (po.getPkId() == null || "".equals(po.getPkId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
				po.setCreateTime(DateUtil.getCurrentSystemTime());
				po.setCreateUser(sessionInfo.getUser().getUserId());
				${atfnBo.defVarName}Service.save${atfnBo.moudPoName}(po,"add");
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
				po.setLastModTime(DateUtil.getCurrentSystemTime());
				po.setLastModUser(sessionInfo.getUser().getUserId());
				${atfnBo.defVarName}Service.save${atfnBo.moudPoName}(po,"updaet");
			}
			ajaxResult.setData(po);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("保存成功！");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("保存失败！");
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
}
