package com.vission.mf.base.service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.dao.SysBranchInfoDao;
import com.vission.mf.base.dao.SysRoleInfoDao;
import com.vission.mf.base.dao.SysUserInfoDao;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.enums.db.SYS_USER_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.SysBranchInfo;
import com.vission.mf.base.model.po.SysUserInfo;
import com.vission.mf.base.util.ClassUtil;
import com.vission.mf.base.util.DateUtil;
import com.vission.mf.base.util.Encrypt;

/**
 * 功能/模块 ：系统用户业务处理层 使用Spring的@Service/@Autowired 指定IOC设置.
 */
@Service("sysUserInfoService")
@Transactional
public class SysUserInfoService extends BaseService {

	@Autowired
	private SysUserInfoDao sysUserInfoDao;

	@Autowired
	private SysRoleInfoDao sysRoleInfoDao;

	@Autowired
	private SysBranchInfoDao sysBranchInfoDao;
	
	@Autowired
	private SysRoleInfoService sysRoleInfoService;

	/**
	 * 根据登录名获取用户(用于登录判断)
	 */
	@Transactional(readOnly = true)
	public SysUserInfo getUserByLoginName(String loginName) {
		SysUserInfo user = sysUserInfoDao.getUserByLoginName(loginName);
		return user;
	}

	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SysUserInfo user, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(user, filterMap); // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件

		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("createTime", CriteriaSetup.DESC);
		return sysUserInfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);// 空对象 页尺寸 map类型的查询条件 查询条件
	}

	/**
	 * 删除
	 */
	public void delete(SysUserInfo user) throws ServiceException {
		user = sysUserInfoDao.get(user.getUserId());
		if (user.getLoginName().equals(BaseConstants.SUPER_USER)) {
			throw new ServiceException("错误：admin不允许删除");
		}
		sysRoleInfoDao.deleteRoleIdByUserId(user.getUserId());
		sysUserInfoDao.delete(user);
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_USER,
				user.getUserName());
	}

	/**
	 * 保存
	 */
	public void save(SysUserInfo user) throws ServiceException {
		boolean isNew = false;
		if (user.getUserId() == null || "".equals(user.getUserId())) {
			if (sysUserInfoDao.isExistLoginName(user.getLoginName())) {
				throw new ServiceException("错误：登录名与现有用户重复,请确认");
			}
			/*if (sysUserInfoDao.isExistUserName(user.getLoginName())) {
				throw new ServiceException("错误：用户名与现有用户重复,请确认");
			}*/
			isNew = true;
			user.setUserId(null);
			//user.setLoginPassword(Encrypt.e(user.getLoginName()));
			user.setLoginPassword(Encrypt.e("123456"));//默认密码为123456
		} /*else {
			if (sysUserInfoDao.isExistUserName(user.getUserName(),
					user.getUserId())) {
				throw new ServiceException("错误：用户名与其他用户重复,请确认");
			}
		}*/
		sysUserInfoDao.save(user);
		if (isNew) {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_USER,
					user.getUserName());
		} else {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.UPDATE_SYS_USER,
					user.getUserName());
		}
	}
	
	/**
	 * 通过文件管理或首页注册及修改
	 * @param user
	 * @throws ServiceException
	 */
	public void saveByFileMang(SysUserInfo user) throws ServiceException {
		boolean isNew = false;
		if (user.getUserId() == null || "".equals(user.getUserId())) {
			if (sysUserInfoDao.isExistLoginName(user.getLoginName())) {
				throw new ServiceException("登录名已被使用，请重新输入!");
			}
			/*if (sysUserInfoDao.isExistUserName(user.getLoginName())) {
				throw new ServiceException("错误：用户名与现有用户重复,请确认");
			}*/
			isNew = true;
			user.setUserId(null);
			user.setUserStatus(false);//停用
			//user.setLoginPassword(Encrypt.e(user.getLoginName()));
			user.setLoginPassword(Encrypt.e("123456"));//初始密码
		}
		//读取配置文件
		ResourceBundle rb = null;
		// 读取acf_config.properties配置文件
		rb = ResourceBundle.getBundle("acf_config");
		if (null == rb) {
			logger.info("acf_config.properties文件不存在，请检查文件路径！");
			return;
		}
		String sysName = ClassUtil.chcString(rb.getString("SYS_NAME"));
		sysName = "".equals(sysName)?"/mf":sysName;
		String vxFilePath = ClassUtil.chcString(rb.getString("VX_FILE_LOCAL_PATH"));
		if("".equals(vxFilePath)){
			vxFilePath = "E:/git/localRes/mf/WebContent/images/vximg/";
		}
		if(!vxFilePath.endsWith("/")){
			vxFilePath = vxFilePath+"/";
		}
		String tempFileName = user.getFile().getOriginalFilename();
		String localFileName = tempFileName.substring(
				tempFileName.lastIndexOf("/") + 1, tempFileName.length());
		String filePath =  vxFilePath+ user.getLoginName();
		user.setVxImgName(localFileName);
		user.setVxImgPath(sysName+"/images/vximg/" + user.getLoginName() + "/" + localFileName);
		
		user.setUserType(SYS_USER_INFO.USER_TYPE_USER);
		sysUserInfoDao.save(user);
		
		try {
			//上传文件
			File path = new File(filePath); // 判断文件路径下的文件夹是否存在，不存在则创建
			if (!path.exists()) {
				path.mkdirs();
			}
			File savedFile = new File(filePath + "/" + localFileName);
			boolean isCreateSuccess = savedFile.createNewFile();// 是否创建文件成功
			if (isCreateSuccess) { // 将文件写入
				user.getFile().transferTo(savedFile);
			}
		} catch (Exception e) {
			logger.info("上传文件失败！");
			logger.error("上传文件失败:"+e.getMessage(),e);
		}
		
		if (isNew) {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.INSERT_SYS_USER,
					user.getUserName());
		} else {
			this.saveOperLogInfo(SYS_OPERLOG_INFO.UPDATE_SYS_USER,
					user.getUserName());
		}
	}

	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(SysUserInfo user, Map<String, Object> filterMap) {
		if (user.getLoginName() != null
				&& !user.getLoginName().trim().equals("")
				&& !user.getLoginName().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "loginName",
					user.getLoginName());
		}
		if (user.getUserName() != null && !user.getUserName().trim().equals("")
				&& !user.getUserName().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "userName",
					user.getUserName());
		}
		if (user.getUserEmail() != null
				&& !user.getUserEmail().trim().equals("")
				&& !user.getUserEmail().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "userEmail",
					user.getUserEmail());
		}
		
		if (user.getUserType() != null
				&& !user.getUserType().trim().equals("")
				&& !user.getUserType().trim().equals("null")) {
			filterMap.put(CriteriaSetup.EQ + "userType",
					user.getUserType());
		}
		if (user.isUserStatus()) {
			filterMap.put(CriteriaSetup.EQ + "userStatus",true);
		}else{
			filterMap.put(CriteriaSetup.EQ + "userStatus",false);
		}
	}

	/**
	 * 获取用户
	 */
	@Transactional(readOnly = true)
	public SysUserInfo getUserById(String userId) throws ServiceException{
		return sysUserInfoDao.getUserInfoByUserId(userId);
	}

	/**
	 * 重置密码
	 */
	public SysUserInfo resetPwd(String userId) throws ServiceException {
		SysUserInfo sysUserInfo = sysUserInfoDao.getOnce(userId);
		if (sysUserInfo != null) {
			sysUserInfo.setLoginPassword(Encrypt.e(sysUserInfo.getLoginName()));
			sysUserInfoDao.save(sysUserInfo);
		} else {
			throw new ServiceException("用户不存在！");
		}
		return sysUserInfo;
	}

	/**
	 * 批量导入用户
	 * 首先判断Excel中的loginName是否有重复
	 * 其次再与数据库中的用户比较，判断是否loginName是否重复
	 * 再次判断用户的机构编号是否合法
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<SysUserInfo> saveBacthUser(InputStream in)
			throws ServiceException {
		List<SysUserInfo> list0 = new ArrayList<SysUserInfo>();//存在重复用户
		List<SysUserInfo> list1 = new ArrayList<SysUserInfo>();//存储Excel中用户
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			in.close();
			HSSFSheet sheet = workbook.getSheetAt(0);
			int numberOfRow = sheet.getLastRowNum();
			HashSet<String> branchSet = getBranchSet();//获得所有机构编号
			for (int i = 1; i <= numberOfRow; i++) {
				HSSFRow row = sheet.getRow(i);
				SysUserInfo user = new SysUserInfo();
				if (row.getCell(0) == null
						|| "".equals(row.getCell(0).getStringCellValue().trim())) {
					throw new ServiceException("第" + row.getRowNum() + "行：登录名称不能为空，请检查！");
				}
				user.setLoginName(row.getCell(0).getStringCellValue());
				user.setLoginPassword(Encrypt.e(row.getCell(0)
						.getStringCellValue()));
				user.setUserStatus(true);
				if (row.getCell(1) == null
						|| "".equals(row.getCell(1).getStringCellValue().trim())) {
					throw new ServiceException("第" + row.getRowNum() + "用户名称不能为空，请检查！");
				}
				user.setUserName(row.getCell(1).getStringCellValue());
				if (row.getCell(2) != null) {
					user.setUserEmail(row.getCell(2).getStringCellValue());
				}
				if (row.getCell(3) != null) {
					user.setUserTel(row.getCell(3).getStringCellValue());
				}
				if (row.getCell(4) != null) {
					user.setUserMobTel(row.getCell(4).getStringCellValue());
				}
				if (row.getCell(5) == null
						|| "".equals(row.getCell(5).getStringCellValue())) {
					throw new ServiceException("第" + row.getRowNum() + "行：机构编号不能为空，请检查！");
				}
				if (!branchSet.contains(row.getCell(5).getStringCellValue())) {
					throw new ServiceException("第" + row.getRowNum()
							+ "行：未含有该用户使用的机构，请检查！");
				}
				user.setBranchNo(row.getCell(5).getStringCellValue());
				user.setUserType(SYS_USER_INFO.USER_TYPE_USER);//导入默认用户类型为"开发人员"
				list1.add(user);
			}
		}catch (Exception e) {
			if("Cannot get a text value from a numeric cell".equals(e.getMessage())){
				throw new ServiceException("[请将所有单元格设置成文本格式]");
			}else{
				throw new ServiceException(e.getMessage());
			}
		}
		list0 = uniqUserList(list1)[0];
		if (list0.size() <= 0) {
			sysUserInfoDao.saveBacthUser(uniqUserList(list1)[1]);
		}
		return list0;
	}

	/**
	 * 辅助方法 
	 * 获得所有机构，并将所有机构编号存入HashSet
	 */
	public HashSet<String> getBranchSet() {
		List<SysBranchInfo> branchList = sysBranchInfoDao.findAll();
		HashSet<String> branchSet = new HashSet<String>();
		if (branchList != null) {
			for (int m = 0, n = branchList.size(); m < n; m++) {
				branchSet.add(branchList.get(m).getBranchNo());
			}
		}
		return branchSet;
	}

	/**
	 * 对excel表中的用户数据进行排重 排重条件为LoginName,UserName
	 */
	@SuppressWarnings("rawtypes")
	public List[] uniqUserList(List<SysUserInfo> list) throws ServiceException {
		List[] lists = new List[2];
		Map<String, SysUserInfo> map1 = new HashMap<String, SysUserInfo>();
		/*Map<String, SysUserInfo> map2 = new HashMap<String, SysUserInfo>();*/
		List<SysUserInfo> repeatList = new ArrayList<SysUserInfo>();//重复用户的集合
		List<SysUserInfo> result = new ArrayList<SysUserInfo>();//排重后结果
		for (int i = 0, j = list.size(); i < j; i++) {
			if (map1.containsKey(list.get(i).getLoginName())) {
				repeatList.add(list.get(i));// 将重复的数据放入list中，备用！
			} else {
				map1.put(list.get(i).getLoginName(), list.get(i));
			}
		}
		for (String key : map1.keySet()) {
			if(!isReUser(map1.get(key))){
				result.add(map1.get(key));
			}else{
				repeatList.add(map1.get(key));// 将重复的数据放入list中
			}
			
			/*if (map2.containsKey(map1.get(key).getUserName())) {
				repeatList.add(map1.get(key));// 将重复的数据放入list中，备用！
			} else {
				if (!isReUser(map1.get(key))) {
					map2.put(map1.get(key).getUserName(), map1.get(key));
				} else {
					repeatList.add(map1.get(key));// 将重复的数据放入list中，备用
				}
			}*/
		}
		/*for (String key : map2.keySet()) {
			result.add(map2.get(key));
		}*/
		lists[0] = repeatList;
		lists[1] = result;
		return lists;
	}

	/**
	 * 辅助方法，判断要插入的user的LoginName，UserName是否与数据中的数据重复
	 */
	public boolean isReUser(SysUserInfo user) {
		boolean r = false;
		List<SysUserInfo> list = sysUserInfoDao.findAll();
		for (int i = 0, j = list.size(); i < j; i++) {
			if (user.getLoginName().equals(list.get(i).getLoginName())
					/*|| user.getUserName().equals(list.get(i).getUserName())*/) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	/**
	 * 根据用户名查询用户 add by syh
	 */
	public SysUserInfo getUserByUserName(String username) {
		return sysUserInfoDao.getUserByUserName(username);
	}
	
	/**
	 * 根据用户登录名查询用户模糊查询 add by syh
	 */
	public List<SysUserInfo> suggest(String word) {
		return sysUserInfoDao.suggest(word);
	}
	
	/**
	 * 新增访问用户
	 * @param user
	 * @throws ServiceException
	 */
	public void addUserView(SysUserInfo user)throws ServiceException{
		//新增访问用户
		SysUserInfo userView = new SysUserInfo();
		userView.setUserId(null);
		userView.setUserName(ClassUtil.chcString(user.getUserName())+"【访问者】");
		userView.setLoginName(ClassUtil.chcString(user.getLoginName())+"_v");
		userView.setLoginPassword(Encrypt.e("123456"));
		userView.setUserStatus(true);
		userView.setCreateTime(DateUtil.getCurrentSystemTime());
		userView.setParentUserId(user.getUserId());
		userView.setUserType(SYS_USER_INFO.USER_TYPE_VIEW);
		this.save(userView);
		this.resetInitRole(userView);
	}
	
	/**
	 * 角色权限初始化
	 * @param user
	 * @throws ServiceException
	 */
	public void resetInitRole(SysUserInfo user)throws ServiceException{
		if(user ==null || null == user.getUserId() || "".equals(user.getUserId())){
			logger.error("重置失败，未获取到userId");
			return;
		}
		List<String> listIds = new ArrayList<String>();
		if(SYS_USER_INFO.USER_TYPE_VIEW.equals(user.getUserType())){
			//重置访问用户角色权限
			listIds.add("402880ed6bc5d6c5016bc615e69b000e");
		}else{
			//重置资源用户角色权限
			listIds.add("948387ca6bfa1d9c016bfa2a3bd50002");
		}
		sysRoleInfoService.bacthInsertUserRole(user.getUserId(), listIds);
	}
}
