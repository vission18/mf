package com.vission.mf.base.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.dao.SysBranchInfoDao;
import com.vission.mf.base.dao.SysUserInfoDao;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.BranchTree;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysBranchInfo;

/**
 * 功能/模块 ：系统机构业务处理层 使用Spring的@Service/@Autowired 指定IOC设置.
 * 使用Spring的@Transactional指定事务管理.
 */
@Service("sysBranchInfoService")
@Transactional
public class SysBranchInfoService extends BaseService {

	@Autowired
	private SysBranchInfoDao sysBranchInfoDao;
	
	@Autowired
	private SysUserInfoDao sysUserInfoDao;

	@Transactional(readOnly = true)
	public List<SysBranchInfo> findByParentId(String parentId) {
		return sysBranchInfoDao.findByProperty("upBranchNo", parentId);

	}

	public SysBranchInfo getById(String branchNo) {
		return sysBranchInfoDao.getOnce(branchNo);
	}

	public void onlySave(SysBranchInfo branchInfo) {
		branchInfo.setLeaf(true);
		if(branchInfo.getUpBranchNo()== null||"".equals(branchInfo.getUpBranchNo())){
			branchInfo.setUpBranchNo("-1");
		}
		sysBranchInfoDao.onlySave(branchInfo);
		if (!"-1".equals(branchInfo.getUpBranchNo())) {
			SysBranchInfo upBranchInfo = this.getById(branchInfo
					.getUpBranchNo());
			if (upBranchInfo.isLeaf()) {
				upBranchInfo.setLeaf(false);
				sysBranchInfoDao.onlySave(upBranchInfo);
			}
		}
		this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_BRANCHINFO, "添加机构"
				+ branchInfo.getBranchName());
	}

	public void update(SysBranchInfo branchInfo, String oldParentNo) {
		sysBranchInfoDao.onlyUpdate(branchInfo);
		this.saveOperLogInfo(SYS_OPERLOG_INFO.UPDATE_SYS_BRANCHINFO, "修改机构"
				+ branchInfo.getBranchName());
	}

	/**
	 * 得到某一机构的级联父机构
	 * 
	 * @param upBranchNo
	 * @return
	 */
	@Transactional(readOnly = true)
	public List<String> getAllParentNo(String upBranchNo) {
		List<String> list = new ArrayList<String>();
		list.add(upBranchNo);
		if (!upBranchNo.equals(Tree.ROOT_ID)) {
			SysBranchInfo upBranchInfo = this.getById(upBranchNo);
			list.addAll(getAllParentNo(upBranchInfo.getUpBranchNo()));
		}
		return list;
	}

	public void delete(String branchNo,AjaxResult ajaxResult) {
		SysBranchInfo branchInfo = this.getById(branchNo);
		List<String> branchNos=this.getAllChildren(branchNo);
		branchNos.add(branchNo);
		if(sysUserInfoDao.existUsersByBranchNos(branchNos)){
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("机构或子机构下存在用户，不允许删除！");
		}else if(sysBranchInfoDao.isExistChildBranch(branchNo)){
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("机构下存在子机构，不允许删除！");
		}else{
			@SuppressWarnings("unused")
			SysBranchInfo upBranchInfo = this.getById(branchInfo
					.getUpBranchNo());
			//this.sysBranchInfoDao.delete(branchInfo);
			this.sysBranchInfoDao.deleteByIds(branchNos);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功！");
		}
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_BRANCHINFO, "删除机构"
				+ branchInfo.getBranchName());
	}

	/**
	 * 获得根节点树
	 */
	@Transactional(readOnly = true)
	public List<BranchTree> firstTreeNode(String parentId) {
		parentId = Tree.ROOT_ID;
		List<SysBranchInfo> firstBranchInfoList = findByParentId(parentId);
		List<BranchTree> children = new ArrayList<BranchTree>();
		for(SysBranchInfo branchInfo:firstBranchInfoList){
			BranchTree tree = new BranchTree(branchInfo);
			tree.setLoaded(false);
			children.add(tree);
		}
		return children;
	}

	/**
	 * 获得节点的子树
	 */
	@Transactional(readOnly = true)
	public List<BranchTree> getChildNodeForTree(String parentId) {
		if (parentId == null) {
			parentId = Tree.ROOT_ID;
		}
		this.debug("根据上级机构得到所有子机构，parentId：" + parentId);
		List<SysBranchInfo> firstBranchInfoList = findByParentId(parentId);
		List<BranchTree> children = new ArrayList<BranchTree>();
		for (SysBranchInfo branchInfo : firstBranchInfoList) {
			BranchTree tree = new BranchTree(branchInfo);
			tree.setLoaded(false);
			if (!branchInfo.isLeaf()) {
				tree.setState("closed");
			}
			children.add(tree);
		}
		return children;
	}
	/**
	 * 递归获得所有子机构ID
	 * @param branchNo
	 * @return
	 */
	public List<String> getAllChildren(String branchNo){
		List<String> list = new ArrayList<String>();
		List<SysBranchInfo> children=findByParentId(branchNo);
		for(SysBranchInfo branchInfo:children){
			list.add(branchInfo.getBranchNo());
			list.addAll(getAllChildren(branchInfo.getBranchNo()));
		}
		return list;
	}
	@Transactional(readOnly = true)
	public List<SysBranchInfo> findById(String branchNo) {
		return sysBranchInfoDao.findByProperty("branchNo", branchNo);

	}
}
