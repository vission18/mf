package com.vission.mf.base.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.enums.db.SYS_MENU_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysMenuInfo;

/**
* 功能/模块 ：登录模块-登录成功后跳到home.jsp
 */
@Controller
@RequestMapping(value="/home.do")
public class HomeController {
	
	/**
	 * 跳转到home页
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String home(HttpServletRequest request, Model model, String projectId) {
		SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY);
		model.addAttribute("sessionInfo", sessionInfo);
		return "home";
	}
	
	/**
	 * 初始化角色,菜单
	 * 用户角色\角色菜单权限有变化时,刷新home即可
	 */
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public AjaxResult menus(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try{
			SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY);
			Map<String,List<String>> buttonsMap = new HashMap<String,List<String>>();//菜单对应的按钮
			List<String> urls = new ArrayList<String>();
			List<Tree> treeList = new ArrayList<Tree>();
			Map<String,List<Tree>> treeMap = new HashMap<String,List<Tree>>();
			List<SysMenuInfo> buttonsList = new ArrayList<SysMenuInfo>();
			//循环生成所有父级(含有子菜单)菜单的map
			for(SysMenuInfo menu : sessionInfo.getMenus()){
				if(menu.getMenuUrl()!=null&&!"".equals(menu.getMenuUrl())){
					urls.add(menu.getMenuUrl());
					//添加menu对应actionUrl
					if(menu.getActions()!=null&&!"".equals(menu.getActions())){
						setAccessUrls(menu.getMenuUrl(),menu.getActions(),urls);
					}
				}
				//得到button菜单
				if(SYS_MENU_INFO.MENU_TYPE_BUTTON.equals(menu.getMenuType().trim())){//按钮
					buttonsList.add(menu);
				} else if(SYS_MENU_INFO.MENU_TYPE_LINK.equals(menu.getMenuType().trim())) {//连接
					
				} else{
					Tree tree = new Tree(menu);
					treeList.add(tree);
					if(!treeMap.containsKey(menu.getParentMenuId())){
						treeMap.put(menu.getParentMenuId(), new ArrayList<Tree>());
					}
					treeMap.get(menu.getParentMenuId()).add(tree);
				}
			}
			List<Tree> firstTreeList = new ArrayList<Tree>();	
			//生成一级菜单和设置所有菜单的children
			for(Tree tree : treeList){
				List<Tree> treeList1=treeMap.get(tree.getId());
				if(treeList1!=null&&treeList1.size()>0){
					Collections.sort(treeList1);
					tree.setChildren(treeList1);
				}
				if(tree.isFirstNode()){
					firstTreeList.add(tree);
				}
			}
			//得到有权限的button链接
			for(SysMenuInfo sysMenuInfo:buttonsList){
				for(SysMenuInfo menu : sessionInfo.getMenus()){
					if(sysMenuInfo.getParentMenuId().equals(menu.getMenuId())){
						setAccessUrls(menu.getMenuUrl(),sysMenuInfo.getActions(),urls);
						if(!buttonsMap.containsKey(menu.getMenuId())){
							List<String> sysList=new LinkedList<String>();
							buttonsMap.put(menu.getMenuId(),sysList);
						}
						buttonsMap.get(menu.getMenuId()).add(sysMenuInfo.getButtonType()+"_"+sysMenuInfo.getIconCls());
//						buttonsMap.get(menu.getMenuId()).add(sysMenuInfo.getButtonType());
					}
				}
			}
			sessionInfo.setMenuUrls(urls);//有权限的连接
			sessionInfo.setButtonsMap(buttonsMap);//有权限的buttton按钮
			Collections.sort(firstTreeList);
			ajaxResult.setData(firstTreeList);//第一级菜单
			ajaxResult.setSuccess(true);
		}catch(Exception e){
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8"); 
		return ajaxResult;
	}
	
	public void setAccessUrls(String menuUrl,String actionUrls,List<String> accessUrls){
		if(menuUrl == null || actionUrls == null || accessUrls == null ){
			return;
		}
		String[] actions =actionUrls.split( "\\|");
		String menuUri = StringUtils.substringBeforeLast(menuUrl, "/");
		for(String action : actions){
			action = StringUtils.trim(action);
			if(action.startsWith("/")){
				accessUrls.add(action);
			}else{
				accessUrls.add(menuUri+"/"+action);
			}
		}
	}
}
