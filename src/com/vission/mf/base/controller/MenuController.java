package com.vission.mf.base.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysMenuInfo;
import com.vission.mf.base.service.SysMenuInfoService;

/**
 * 功能：菜单控制器
 *
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController{
	@Autowired
	private SysMenuInfoService sysMenuService;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response,Model model){
		this.getAccessButtons(request,model);
		return "base/menu";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			String id = request.getParameter("id");
			sysMenuService.delete(id);
			ajaxResult.setData(id);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功！");
		}catch(Exception e){
			throw new ServiceException(e);
		}
		return ajaxResult;
	}
	
	/**
	 * 得到没有叶子节点的树
	 */
	@RequestMapping(value = "/getAllMenuTreeNoleaf")
	@ResponseBody
	public List<Tree> getAllMenuTreeNoleaf(HttpServletRequest request,
			HttpServletResponse response) {
		List<Tree> tree = sysMenuService.getAllMenuTreeNoleaf();
		return tree;
	}
	
	/**
	 * 菜单树
	 */
	@RequestMapping(value = "/menuTreeNode")
	@ResponseBody
	public List<Tree> menuTreeNode(HttpServletRequest request,
			HttpServletResponse response) {
		List<Tree> tree = sysMenuService.getAllMenuTree();
		return tree;
	}
	
	@RequestMapping(value = "/childMenuTreeNode")
	@ResponseBody
	public List<Tree> childMenuTreeNode(HttpServletRequest request,
			HttpServletResponse response, String menuId) {
		List<Tree> tree = sysMenuService.getMenuTreeByParentId(menuId);
		return tree;
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public AjaxResult save(HttpServletRequest request, HttpServletResponse response, SysMenuInfo menu) throws ServiceException {
		AjaxResult j = new AjaxResult();
		Tree tree = new Tree();
		try {
			if (menu.getMenuId() == null || "".equals(menu.getMenuId())) {
				if(menu.getParentMenuId()==null||"".equals(menu.getParentMenuId())){
					menu.setParentMenuId(Tree.ROOT_ID);
				}
				j.setType(BaseConstants.OPER_TYPE_INSERT);
				List<String> menuNameList = new ArrayList<String>();
				String parentMenuId = menu.getParentMenuId();
				String menuName = menu.getMenuName();
				List<SysMenuInfo>  sysMenuInfoList= sysMenuService.getMenuListByParentId(parentMenuId);
				for(SysMenuInfo menuname:sysMenuInfoList){
					menuNameList.add(menuname.getMenuName());
				}
				if(menuNameList.contains(menuName)){
					throw new ServiceException("同一级别下菜单名不能重复！");
				}
			} else {
				j.setType(BaseConstants.OPER_TYPE_UPDATE);	
				if(menu.getParentMenuId()==null){
					menu.setParentMenuId("-1");
				}
			}
			sysMenuService.save(menu);
			tree.setId(menu.getMenuId());
			tree.setParentId(menu.getParentMenuId());
			tree.setText(menu.getMenuName());
			tree.setUrl(menu.getMenuUrl());
			tree.setOrder(menu.getMenuOrder());
			tree.setIconCls(menu.getIconCls());
			j.setSuccess(true);
			j.setData(tree);
			j.setMessage("菜单保存成功!");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8"); 
		return j;
	}

	@RequestMapping(value="/getTreeById" , method=RequestMethod.GET)
	@ResponseBody
	public AjaxResult getTreeById(HttpServletRequest request, HttpServletResponse response){
		String menuId = request.getParameter("id");
		AjaxResult ajaxResult = new AjaxResult();
		SysMenuInfo rmenu = sysMenuService.getMenuById(menuId);
		ajaxResult.setData(rmenu);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
	
	@RequestMapping(value="/getMaxOrder" , method=RequestMethod.GET)
	@ResponseBody
	public AjaxResult getMaxOrder(HttpServletRequest request, HttpServletResponse response){
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData(sysMenuService.getMaxOrder());
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
}
