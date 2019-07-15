package com.vission.mf.base.controller;

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
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysRoleInfo;
import com.vission.mf.base.model.po.SysUserInfo;
import com.vission.mf.base.service.SysMenuInfoService;
import com.vission.mf.base.service.SysRoleInfoService;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{

	@Autowired
	private SysRoleInfoService sysRoleInfoService;
	@Autowired
	private SysMenuInfoService sysMenuService;

	/**
	 * 数据列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/userRoleDataGrid")
	@ResponseBody
	public DataGrid userRoleDataGrid(HttpServletRequest request, SysUserInfo user){
		DataGrid dataGrid = sysRoleInfoService.dataGrid(new SysRoleInfo(), 1, Integer.MAX_VALUE);
		List<SysRoleInfo> roles = sysRoleInfoService.getByUserId(user.getUserId());
		for(SysRoleInfo r : (List<SysRoleInfo>)dataGrid.getRows()){
			if(roles.contains(r)){
				r.setChecked(true);
			}else{
				r.setChecked(false);
			}
		}
		return dataGrid;
	}

	@RequestMapping("/datagrid")
	@ResponseBody
	public DataGrid datagrid(HttpServletRequest request, SysRoleInfo role) {
		int pageNo = 1;
		int pageSize = 10;
		try{
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		}catch(Exception e){
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return sysRoleInfoService.dataGrid(role, pageNo, pageSize);
	}

	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(HttpServletRequest request, HttpServletResponse response, SysRoleInfo role) throws ServiceException{
		AjaxResult j = new AjaxResult();
		try {
			String menuIds = null;
			if(role.getRoleId() == null || "".equals(role.getRoleId())){
				j.setType(BaseConstants.OPER_TYPE_INSERT);
			}else{
				j.setType(BaseConstants.OPER_TYPE_UPDATE);
				menuIds = request.getParameter("menuIds");
			}
			sysRoleInfoService.save(role, menuIds);
			j.setSuccess(true);
			j.setMessage("角色保存成功！");
			j.setData(role);
		} catch (Exception e) {
			j.setMessage(e.getMessage());
		}
		response.setContentType("text/html;charset=utf-8");
		return j;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request, HttpServletResponse response, SysRoleInfo role) throws ServiceException{
		AjaxResult j = new AjaxResult();
		try{
			sysRoleInfoService.remove(role);
			j.setData(role);
			j.setSuccess(true);
			j.setMessage("角色删除成功！");
		}catch(Exception e){
			j.setMessage("角色删除失败！");
		}
		return j;
	}

	@RequestMapping("/getRoleById")
	@ResponseBody
	public AjaxResult getRoleById(SysRoleInfo role){
		AjaxResult ajaxResult =  new AjaxResult();
		ajaxResult.setData(sysRoleInfoService.getRoleById(role.getRoleId()));
		logger.debug(sysRoleInfoService.getRoleById(role.getRoleId()).getMenuIds());
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request,HttpServletResponse response,Model model){
		this.getAccessButtons(request,model);
		return "base/role";
	}
	
	@RequestMapping(value="/getMenuTreeByRoleId")
	@ResponseBody
	public List<Tree> getMenuTreeByRoleId(String treeId, String roleId){
		return sysMenuService.getMenuTreeByRoleId(treeId, roleId);
	}
	
	@RequestMapping(value="/getAllMenuTree")
	@ResponseBody
	public List<Tree> getAllMenuTree(){
		return sysMenuService.getAllMenuTree();
	}
}
