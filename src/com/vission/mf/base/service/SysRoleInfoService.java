package com.vission.mf.base.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.dao.SysMenuInfoDao;
import com.vission.mf.base.dao.SysRoleInfoDao;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.po.SysMenuInfo;
import com.vission.mf.base.model.po.SysRoleInfo;

@Service("roleService")
@Transactional
public class SysRoleInfoService extends BaseService {
	
	@Autowired
	private SysRoleInfoDao sysRoleInfoDao;
	
	@Autowired
	private SysMenuInfoDao sysMenuInfoDao;
	
	public void save(SysRoleInfo role, String menuIds) throws ServiceException{
		boolean isNew = false;
		if(role.getRoleId() == null || "".equals(role.getRoleId())){
			if(sysRoleInfoDao.isExistRoleName(role.getRoleName())){
				throw new ServiceException("错误：角色名称与现有角色重复，确认！");
			}
			role.setRoleId(null);
			isNew = true;
		}else{
			if(menuIds != null){
				if(sysRoleInfoDao.isExistRoleName(role.getRoleName(),role.getRoleId())){
					throw new ServiceException("错误：角色名称与现有角色重复，确认！");
				}
				List<String> ids = Arrays.asList(menuIds.split(","));
				List<SysMenuInfo> list = sysMenuInfoDao.getMenuListByIds(ids);
				role.setMenus(list);
			}
		}
		sysRoleInfoDao.save(role);
		if(isNew){
			this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_ROLE, role.getRoleName());
		}else{
			this.saveOperLogInfo(SYS_OPERLOG_INFO.UPDATE_SYS_ROLE, role.getRoleName());
		}
	}

	/**
	 * 移除权限
	 * @param role
	 * @throws ServiceException
	 */
	public void remove(SysRoleInfo role) throws ServiceException{
		try{
			sysRoleInfoDao.deleteUserIdByRoleId(role.getRoleId());
			sysRoleInfoDao.delete(role);
		}catch(Exception e){
			throw new ServiceException(e);
		}
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_ROLE, role.getRoleName());
	}

	/**
	 * 在session中存放角色ids与names 
	 */
	public void setSessionInfoRoles(SessionInfo sessionInfo) {
		List<SysRoleInfo> roles = sysRoleInfoDao.getByUserId(sessionInfo.getUser().getUserId());
		List<String> ids = new ArrayList<String>(roles.size());
		List<String> names = new ArrayList<String>(roles.size());
		for(SysRoleInfo role : roles){
			ids.add(role.getRoleId());
			names.add(role.getRoleName());
		}
		sessionInfo.setRoleIds(ids);
		sessionInfo.setRoleNames(names);
	}
	
	public List<SysRoleInfo> getByUserId(String userId) {
		return sysRoleInfoDao.getByUserId(userId);
	}

	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SysRoleInfo role, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo-1)*pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(role, filterMap);
		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("roleName", CriteriaSetup.ASC);
		return sysRoleInfoDao.findByCriteria(dataGrid, pageSize, filterMap, orderMap);
	}
	
	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SysRoleInfo role, Map<String, Object> filterMap) {
		if (role.getRoleName() != null && !role.getRoleName().trim().equals("") && !role.getRoleName().trim().equals("null") ) {
			filterMap.put(CriteriaSetup.LIKE_ALL+"roleName",role.getRoleName());
		}
		if (role.getRoleRmk() != null && !role.getRoleRmk().trim().equals("") && !role.getRoleRmk().trim().equals("null") ) {
			filterMap.put(CriteriaSetup.LIKE_ALL+"roleRmk",role.getRoleRmk());
		}
	}
	
	@Transactional(readOnly = true)
	public List<SysRoleInfo> getRoleList(){
		return sysRoleInfoDao.findAll();
	}

	/**
	 * 批量插入用户与角色关系
	 */
	public void bacthInsertUserRole(String userId, List<String> roleIds) throws ServiceException {
		sysRoleInfoDao.deleteRoleIdByUserId(userId);
		if(roleIds!=null&&roleIds.size()>0){
			sysRoleInfoDao.bacthInsertUserRole(userId, roleIds);
		}
	}
	
	/**
	 * 通过ID获得一条数据
	 */
	@Transactional(readOnly = true)
	public SysRoleInfo getRoleById(String roleId){
		return sysRoleInfoDao.get(roleId);
	}
}
