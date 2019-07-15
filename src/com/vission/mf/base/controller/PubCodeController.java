package com.vission.mf.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.SysPubCodeInfo;
import com.vission.mf.base.service.SysPubCodeInfoService;

@Controller
@RequestMapping("/pubCode")
public class PubCodeController extends BaseController{
	
	@Autowired
	private SysPubCodeInfoService sysPubCodeInfoService;

	/**
	 * 跳转至pubCode.jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response,Model model){
		this.getAccessButtons(request,model);
		return "base/pubCode";
	}
	
	/**
	 * 保存
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(HttpServletRequest request, HttpServletResponse response, SysPubCodeInfo pubCode) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			if(pubCode.getCodeId()==null||"".equals(pubCode.getCodeId())){
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
			}else{
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			sysPubCodeInfoService.save(pubCode);
			ajaxResult.setData(pubCode);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("代码保存成功！");
		}catch(Exception e){
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8"); 
		return ajaxResult;
	}
	
	/**
	 * 加载数据列表
	 */
	@RequestMapping(value = "/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request, SysPubCodeInfo pubCode) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return sysPubCodeInfoService.dataGrid(pubCode, pageNo, pageSize);
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request, HttpServletResponse response, SysPubCodeInfo pubCode) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			ajaxResult.setData(pubCode);
			sysPubCodeInfoService.delete(pubCode);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功！");
		}catch(Exception e){
			throw new ServiceException(e);
		}
		return ajaxResult;
	}
	

	@RequestMapping(value="/getById")
	@ResponseBody
	public AjaxResult getById(SysPubCodeInfo pubCode){
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData(sysPubCodeInfoService.getPubcodeById(pubCode.getCodeId()));
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

}

