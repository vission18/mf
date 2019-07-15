package com.vission.mf.base.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.PageParameters;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysOperLogInfo;

/**
 * 
 * 功能/模块 :操作日志控制器
 */
@Controller
@RequestMapping("/operLog")
public class OperLogController extends BaseController {

	/**
	 * 跳转至sysOperLogInfo.jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response,Model model){
		this.getAccessButtons(request,model);
		return "base/operLog";
	}
	
	/**
	 * 数据列表
	 */
	@RequestMapping(value="/dataGrid")
	@ResponseBody
	public DataGrid dataGrid( HttpServletRequest request,HttpServletResponse response,SysOperLogInfo sysOperLogInfo,PageParameters par){
		return this.getSysOperLogInfoService().dataGrid(sysOperLogInfo,par);
	}

	/**
	 * 删除
	 */
	@RequestMapping(value="/deleteByIds", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		String ids=request.getParameter("ids");
		ids=ids.substring(0,ids.length()-1);
		try{
			this.getSysOperLogInfoService().batchDelete(ids);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("批量删除成功！");
		}catch(Exception e){
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("批量删除失败！");
			throw new ServiceException(e);
		}
		return ajaxResult;
	}
	
	/**
	 * 得到所有的操作类型，方便页面查询
	 * @return
	 */
	@RequestMapping(value="/getAllOperType")
	@ResponseBody
	public Object[] getAllOperType(){
		Map<String,String> map=SYS_OPERLOG_INFO.operTypeMap;
		Object[] result=new Object[map.size()];
		int i=0;
		for(String key:map.keySet()){
			Tree tree=new Tree();
			tree.setId(key);
			tree.setText(map.get(key));
			result[i]=tree;
			i++;
		}
		return result;
	}
}
