
package com.vission.mf.base.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysAnnoInfo;
import com.vission.mf.base.model.po.SysAnnoTypeInfo;
import com.vission.mf.base.service.SysAnnoInfoService;
import com.vission.mf.base.service.SysAnnoTypeInfoService;

/**
 * 公告类型控制器
 */
@Controller
@RequestMapping("/annoType")
public class AnnoTypeController extends BaseController {

	@Autowired
	private SysAnnoTypeInfoService sysAnnoTypeInfoService;
	
	@Autowired
	private SysAnnoInfoService sysAnnoInfoService;

	/**
	 * 跳转至annoType.jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "base/annoType";
	}
	
	/**
	 * 加载公告类型数据列表
	 */
	@RequestMapping(value = "/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request, SysAnnoTypeInfo annoType) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		DataGrid datagrid = sysAnnoTypeInfoService.dataGrid(annoType, pageNo, pageSize);
		return datagrid;
	}
	
	/**
	 * 保存公告类型
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			SysAnnoTypeInfo annoType) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		if(annoType.getTypeName().trim() == null || "".equals(annoType.getTypeName().trim())){
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("公告类型名称不能为空！");
			return ajaxResult;
		}
		try {
			if (annoType.getTypeId() == null
					|| "".equals(annoType.getTypeId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY);
			sysAnnoTypeInfoService.save(annoType, sessionInfo.getUser());
			ajaxResult.setData(annoType);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("保存成功!");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage(e.getMessage());
		}
		response.setContentType("text/html,charset=utf-8");
		return ajaxResult;
	}
	
	/**
	 * 删除公告类型
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response, SysAnnoTypeInfo annoType)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		List<SysAnnoInfo> sysAnnoInfo = sysAnnoInfoService.getByTypeId(annoType.getTypeId());
		if(sysAnnoInfo.size() > 0){
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("公告类型被使用中,不能删除！");
			return ajaxResult;
		}
		sysAnnoTypeInfoService.delete(annoType);
		ajaxResult.setData(annoType);
		ajaxResult.setSuccess(true);
		ajaxResult.setMessage("删除成功！");
		return ajaxResult;
	}
	
	
	@RequestMapping(value = "/getAnnoTypeById")
	@ResponseBody
	public AjaxResult getById(SysAnnoTypeInfo annoType) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData(sysAnnoTypeInfoService.getAnnoTypeById(annoType
				.getTypeId()));
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
	
	/**
	 * 获取所有公告类型
	 */
	@RequestMapping(value = "/getAnnoTypes", method = RequestMethod.POST)
	@ResponseBody
	public Object[] getAnnoTypes(HttpServletRequest request,
			HttpServletResponse response)
			throws ServiceException {
		
		List<SysAnnoTypeInfo> annoTypes = sysAnnoTypeInfoService.getAnnoTypes();
		Object[] result=new Object[annoTypes.size()];
		int i=0;
		for(SysAnnoTypeInfo mea : annoTypes){
			Tree tree=new Tree();
			tree.setId(mea.getTypeId());
			tree.setText(mea.getTypeName());
			result[i]=tree;
			i++;
		}
		return result;
		
	}
	

	/**
	 * 表单提交类型绑定
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

}