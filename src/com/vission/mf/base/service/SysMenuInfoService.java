package com.vission.mf.base.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.dao.SysMenuInfoDao;
import com.vission.mf.base.dao.SysRoleInfoDao;
import com.vission.mf.base.enums.db.SYS_MENU_INFO;
import com.vission.mf.base.enums.db.SYS_MENU_ROLE_REL;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysMenuInfo;

/**
 * 功能/模块 ：系统菜单业务处理层 使用Spring的@Service/@Autowired 指定IOC设置.
 * 使用Spring的@Transactional指定事务管理.
 */
@Service("sysMenuInfoService")
@Transactional
public class SysMenuInfoService extends BaseService {

	@Autowired
	private SysMenuInfoDao sysMenuInfoDao;
	@Autowired
	private SysRoleInfoDao sysRoleInfoDao;

	/**
	 * 删除
	 */
	public void delete(String id) throws ServiceException {
		SysMenuInfo menu = sysMenuInfoDao.get(id);
		String sql = "delete from "+SYS_MENU_ROLE_REL.TABLE_NAME+" where MENU_ID=?";
		sysMenuInfoDao.delete(menu);
		sysMenuInfoDao.getSession().createSQLQuery(sql).setString(0, id)
				.executeUpdate();// 将菜单与角色的关系删除
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_MENU,
				menu.getMenuName());
	}

	@Transactional(readOnly = true)
	public SysMenuInfo getMenuById(String menuId) {
		return sysMenuInfoDao.get(menuId);
	}

	public void save(SysMenuInfo menu) throws ServiceException {
		boolean isNew = false;
		if (menu.getMenuId() == null || "".equals(menu.getMenuId())) {
			isNew = true;
			menu.setMenuId(null);
		}
		sysMenuInfoDao.save(menu);
		if (isNew) {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_MENU,
					menu.getMenuName());
		} else {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.UPDATE_SYS_MENU,
					menu.getMenuName());
		}
	}

	/**
	 * 获取有权限的菜单
	 */
	@Transactional(readOnly = true)
	public List<SysMenuInfo> getMenuListByRoleIds(List<String> roleIds) {
		if (roleIds == null || roleIds.size() == 0) {
			return new ArrayList<SysMenuInfo>(0);
		}
		return sysMenuInfoDao.getMenuListByRoleIds(roleIds);
	}

	/**
	 * 通过parent_id获得菜单集合
	 */
	@Transactional(readOnly = true)
	public List<SysMenuInfo> getMenuListByParentId(String parentId) {
		return sysMenuInfoDao.findByProperty("parentMenuId", parentId);
	}
	
	/**
	 * 通过menu_url获得菜单集合
	 */
	@Transactional(readOnly = true)
	public List<SysMenuInfo> getMenuListByMenuUrl(String menuUrl) {
		return sysMenuInfoDao.findByProperty("menuUrl", menuUrl);
	}

	@Transactional(readOnly = true)
	public List<Tree> getAllMenuTree() {
		List<SysMenuInfo> menus = sysMenuInfoDao.findAll();
		List<Tree> treeList = new ArrayList<Tree>();
		Map<String, List<Tree>> treeMap = new HashMap<String, List<Tree>>();
		// 循环生成所有父级(含有子菜单)菜单的map
		for (SysMenuInfo menu : menus) {
			Tree tree = new Tree(menu);
			treeList.add(tree);
			if (!treeMap.containsKey(menu.getParentMenuId())) {
				treeMap.put(menu.getParentMenuId(), new ArrayList<Tree>());
			}
			treeMap.get(menu.getParentMenuId()).add(tree);
		}
		// 设置所有菜单的children
		for (Tree tree : treeList) {
			List<Tree> treeList1 = treeMap.get(tree.getId());
			if (treeList1 != null && treeList1.size() > 0) {
				Collections.sort(treeList1);
				tree.setChildren(treeList1);
			}
		}
		// 生成一级菜单
		List<Tree> firstTreeList = new ArrayList<Tree>();
		for (Tree tree : treeList) {
			if (tree.isFirstNode()) {
				List<Tree> treeList1 = treeMap.get(tree.getId());
				if(treeList1!=null&&treeList1.size()>0)
					tree.setState(Tree.TREE_CLOSE_STATE);
				else
					tree.setState(Tree.TREE_OPEN_STATE);
				firstTreeList.add(tree);
			}
		}
		return firstTreeList;
	}

	@Transactional(readOnly = true)
	public List<Tree> getAllMenuTreeNoleaf() {
		List<SysMenuInfo> menus = sysMenuInfoDao.findByProperty("menuType", "0");
		List<Tree> treeList = new ArrayList<Tree>();
		Map<String, List<Tree>> treeMap = new HashMap<String, List<Tree>>();
		// 循环生成所有父级(含有子菜单)菜单的map
		for (SysMenuInfo menu : menus) {
				Tree tree = new Tree(menu);
				treeList.add(tree);
				if (!treeMap.containsKey(menu.getParentMenuId())) {
					treeMap.put(menu.getParentMenuId(), new ArrayList<Tree>());
				}
				treeMap.get(menu.getParentMenuId()).add(tree);
			}
		// 设置所有菜单的children
		for (Tree tree : treeList) {
			List<Tree> treeList1 = treeMap.get(tree.getId());
			if (treeList1 != null && treeList1.size() > 0) {
				Collections.sort(treeList1);
				tree.setChildren(treeList1);
			}
		}
		// 生成一级菜单
		List<Tree> firstTreeList = new ArrayList<Tree>();
		for (Tree tree : treeList) {
			if (tree.isFirstNode()) {
				firstTreeList.add(tree);
			}
		}
		return firstTreeList;
	}

	/**
	 * 通过角色ID获得对应的菜单树
	 */
	public List<Tree> getMenuTreeByRoleId(String roleId) {
		List<Tree> tree = getAllMenuTree();
		List<SysMenuInfo> smis = sysRoleInfoDao.get(roleId).getMenus();
		return treeCheck(tree, smis);
	}
	
	/**
	 * 动态加载角色的菜单树
	 */
	public List<Tree> getMenuTreeByRoleId(String treeId, String roleId){
		if(treeId == null || "".equals(treeId)){
			treeId = Tree.ROOT_ID;
		}
		List<SysMenuInfo> smisOfRole = sysRoleInfoDao.get(roleId).getMenus();
		List<SysMenuInfo> smis = getMenuListByParentId(treeId);
		List<Tree> trees = new ArrayList<Tree>();
		for(SysMenuInfo s : smis){
			Tree tree = new Tree(s);
			for(SysMenuInfo sor : smisOfRole){
				if(tree.getId().equals(sor.getMenuId())){
					tree.setChecked(true);
				}
			}
			if(!"9".equals(s.getMenuType())){
				tree.setState("closed");
			}
			trees.add(tree);
		}
		return trees;
	}

	/**
	 * 辅助方法 遍历树
	 */
	public List<Tree> treeCheck(List<Tree> tree, List<SysMenuInfo> smis) {
		for (Tree t : tree) {
			for (SysMenuInfo s : smis) {
				if (s.getMenuId().equals(t.getId())) {
					t.setChecked(true);
				}
			}
			if (t.getChildren() != null && t.getChildren().size() > 0) {
				treeCheck(t.getChildren(), smis);
			}
		}
		return tree;
	}

	public String getMaxOrder() {
		String sql = "select max(MENU_ORDER) from "+SYS_MENU_INFO.TABLE_NAME;
		String maxOrder = sysMenuInfoDao.getSession().createSQLQuery(sql).uniqueResult().toString();
		return maxOrder;
	}

	public List<Tree> getMenuTreeByParentId(String menuId) {
		List<SysMenuInfo> menus = sysMenuInfoDao.findAll();
		List<Tree> treeList = new ArrayList<Tree>();
		Map<String, List<Tree>> treeMap = new HashMap<String, List<Tree>>();
		// 循环生成所有父级(含有子菜单)菜单的map
		for (SysMenuInfo menu : menus) {
			Tree tree = new Tree(menu);
			treeList.add(tree);
			if (!treeMap.containsKey(menu.getParentMenuId())) {
				treeMap.put(menu.getParentMenuId(), new ArrayList<Tree>());
			}
			treeMap.get(menu.getParentMenuId()).add(tree);
		}
		// 设置所有菜单的children
		for (Tree tree : treeList) {
			List<Tree> treeList1 = treeMap.get(tree.getId());
			if (treeList1 != null && treeList1.size() > 0) {
				Collections.sort(treeList1);
				tree.setChildren(treeList1);
			}
		}
		// 生成一级菜单
		List<Tree> firstTreeList = new ArrayList<Tree>();
		for (Tree tree : treeList) {
			if (tree.getParentId().equals(menuId)) {
				List<Tree> treeList1 = treeMap.get(tree.getId());
				if(treeList1!=null&&treeList1.size()>0)
					tree.setState(Tree.TREE_CLOSE_STATE);
				else
					tree.setState(Tree.TREE_OPEN_STATE);
				firstTreeList.add(tree);
			}
		}
		return firstTreeList;
	}

}
