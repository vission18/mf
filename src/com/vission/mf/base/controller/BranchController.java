package com.vission.mf.base.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.BranchTree;
import com.vission.mf.base.model.bo.TSysBranchInfo;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysBranchInfo;
import com.vission.mf.base.service.SysBranchInfoService;

@Controller
@RequestMapping("/branch")
public class BranchController extends BaseController {

	@Autowired
	private SysBranchInfoService sysBranchInfoService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response,Model model) {
		this.getAccessButtons(request,model);
		return "base/branch";
	}

	/**
	 * 数据列表得到第一个节点
	 */
	@RequestMapping(value = "/firstTreeNode")
	@ResponseBody
	public List<BranchTree> firstTreeNode(HttpServletRequest request,
			HttpServletResponse response, String parentId) {
		return sysBranchInfoService.firstTreeNode(parentId);
	}

	/**
	 * 数据列表得到父节点的所有子节点
	 */
	@RequestMapping(value = "/getChildNodeForTree")
	@ResponseBody
	public List<BranchTree> getChildNodeForTree(HttpServletRequest request,
			HttpServletResponse response, String parentId) {
		return sysBranchInfoService.getChildNodeForTree(parentId);
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			SysBranchInfo branchInfo) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
		List<SysBranchInfo> list = sysBranchInfoService.findById(branchInfo.getBranchNo());
		if(list!=null&&list.size()>0){
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("机构代码重复，请重新输入！");
		}else{
			if(branchInfo.getUpBranchNo()==null||"".equals(branchInfo.getUpBranchNo())){
				branchInfo.setUpBranchNo(Tree.ROOT_ID);
			}
			sysBranchInfoService.onlySave(branchInfo);
			ajaxResult.setData(branchInfo);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("机构保存成功！");
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult update(HttpServletRequest request,
			HttpServletResponse response, HttpSession session,
			SysBranchInfo branchInfo) throws ServiceException {
		String oldParentId = request.getParameter("oldparentId");
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
		List<String> list = sysBranchInfoService
				.getAllParentNo(branchInfo.getUpBranchNo());
		if (list.contains(branchInfo.getBranchNo())) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("所属机构不能为自己的子机构，请重新选择！");

		} else {
			sysBranchInfoService.update(branchInfo, oldParentId);
			ajaxResult.setData(branchInfo);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("机构保存成功！");
		}
		this.debug("机构修改 机构名称：" + branchInfo.getBranchName() + "，oldParentId："
				+ oldParentId + "    newParentId:" + branchInfo.getUpBranchNo());
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

	/**
	 * 获取一条数据
	 */
	@RequestMapping(value = "/getById", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult getById(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		SysBranchInfo branchInfo = sysBranchInfoService.getById(request
				.getParameter("id"));
		if (!branchInfo.getUpBranchNo().equals(Tree.ROOT_ID)) {
			SysBranchInfo upBranchInfo = sysBranchInfoService
					.getById(branchInfo.getUpBranchNo());
			branchInfo.setUpSysBranchInfo(upBranchInfo);
		}
		TSysBranchInfo tBranchInfo = new TSysBranchInfo();
		BeanUtils.copyProperties(branchInfo, tBranchInfo);
		ajaxResult.setData(tBranchInfo);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	@RequestMapping("/getBracnNameById")
	@ResponseBody
	public AjaxResult getBracnById(HttpServletRequest request,
			HttpServletResponse response, String branchNo) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData(sysBranchInfoService.getById(branchNo)
				.getBranchName());
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/deleteById", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		String id = request.getParameter("id");
		sysBranchInfoService.delete(id,ajaxResult);
		this.debug("机构删除，机构ID：" + id);
		return ajaxResult;
	}
	
	/**
	 * 判断是否存在branchNo
	 */
	@RequestMapping(value="/existBranchNo",method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult existBranchNo(HttpServletRequest request, HttpServletResponse response){
		AjaxResult ajaxResult = new AjaxResult();
		List<SysBranchInfo> list = sysBranchInfoService.findById(request.getParameter("branchNo"));
		if(list!=null&&list.size()>0){
			ajaxResult.setData(true);
		}else{
			ajaxResult.setData(false);
		}
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
}
