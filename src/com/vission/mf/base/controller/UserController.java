package com.vission.mf.base.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.enums.db.SYS_USER_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.BranchTree;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.bo.UploadJson;
import com.vission.mf.base.model.po.SysBranchInfo;
import com.vission.mf.base.model.po.SysRoleInfo;
import com.vission.mf.base.model.po.SysUserInfo;
import com.vission.mf.base.model.po.UploadExcel;
import com.vission.mf.base.service.SysBranchInfoService;
import com.vission.mf.base.service.SysRoleInfoService;
import com.vission.mf.base.service.SysUserInfoService;
import com.vission.mf.base.sourcems.zysfilemang.po.ZysFileMang;
import com.vission.mf.base.util.ClassUtil;
import com.vission.mf.base.util.ConnectToOthers;
import com.vission.mf.base.util.DateUtil;
import com.vission.mf.base.util.Encrypt;
import com.vission.mf.base.util.FileUtil;
import com.vission.mf.base.util.RegexUtil;

/**
 * 功能/模块 ：用户管理
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	@Autowired
	private SysUserInfoService sysUserInfoService;
	@Autowired
	private SysRoleInfoService sysRoleInfoService;
	@Autowired
	private SysBranchInfoService sysBranchInfoService;

	/**
	 * 跳转至user.jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "base/user";
	}

	/**
	 * 跳转至userRole.jsp
	 */
	@RequestMapping("/userRole")
	public String userRole() {
		return "base/userRole";
	}

	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request, SysUserInfo user) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return sysUserInfoService.dataGrid(user, pageNo, pageSize);
	}

	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, SysUserInfo user)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {

			if (user.getFile() == null
					|| user.getFile().getOriginalFilename() == null
					|| "".equals(user.getFile().getOriginalFilename())) {
				throw new ServiceException("不存在的文件");
			}
			if (user.getFile().getSize() > 10240000) {
				throw new ServiceException("文件过大！");
			}
			if (!RegexUtil.matching("(\\.png|\\.PNG|\\.jpg|\\.JPG)$", user
					.getFile().getOriginalFilename())) {
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("请上传jpg或png格式的图片");
				return ajaxResult;
			}
			if (user.getUserId() == null || "".equals(user.getUserId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			//读取配置文件
			ResourceBundle rb = null;
			// 读取acf_config.properties配置文件
			rb = ResourceBundle.getBundle("acf_config");
			if (null == rb) {
				logger.info("acf_config.properties文件不存在，请检查文件路径！");
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("acf_config.properties文件不存在，请检查文件路径！");
				return ajaxResult;
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

			sysUserInfoService.save(user);
			// 保存后,判断是否是session用户,如果是则更新session
			SessionInfo sessionInfo = (SessionInfo) session
					.getAttribute(BaseConstants.USER_SESSION_KEY);
			SysUserInfo sessionUser = sessionInfo.getUser();
			if (user.getUserId().equals(sessionUser.getUserId())) {
				sessionUser.setUserName(user.getUserName());
				sessionUser.setUserEmail(user.getUserEmail());
				sessionUser.setUserMobTel(user.getUserMobTel());
				sessionUser.setUserTel(user.getUserTel());
				sessionUser.setUserStatus(user.isUserStatus());
				sessionUser.setBranchNo(user.getBranchNo());
			}
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
			
			ajaxResult.setData(null);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("用户保存成功！");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

	/**
	 * 通过文件管理来保存用户
	 * @param request
	 * @param response
	 * @param session
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/saveByFile", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveByFile(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, SysUserInfo user)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {

			if (user.getFile() == null
					|| user.getFile().getOriginalFilename() == null
					|| "".equals(user.getFile().getOriginalFilename())) {
				throw new ServiceException("不存在的文件");
			}
			if (user.getFile().getSize() > 10240000) {
				throw new ServiceException("文件过大！");
			}
			if (!RegexUtil.matching("(\\.png|\\.PNG|\\.jpg|\\.JPG)$", user
					.getFile().getOriginalFilename())) {
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("请上传jpg或png格式的图片");
				return ajaxResult;
			}
			
			SessionInfo sessionInfo = (SessionInfo) session
					.getAttribute(BaseConstants.USER_SESSION_KEY);
			SysUserInfo sessionUser = sessionInfo.getUser();
			
			if (user.getUserId() == null || "".equals(user.getUserId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
				user.setCreateTime(DateUtil.getCurrentSystemTime());
				user.setCreateUser(sessionUser.getUserId());
			} else {
				user.setLastModTime(DateUtil.getCurrentSystemTime());
				user.setLastModUser(sessionUser.getUserId());
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			this.sysUserInfoService.saveByFileMang(user);
			
			ajaxResult.setData(null);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("用户注册成功！");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
	/**
	 * 修改我的信息
	 */
	@RequestMapping(value = "/saveMyInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveMyInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, SysUserInfo user)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			SessionInfo sessionInfo = (SessionInfo) session
					.getAttribute(BaseConstants.USER_SESSION_KEY);
			SysUserInfo sessionUser = sessionInfo.getUser();
			if (user.getFile().getSize() > 10240000) {
				throw new ServiceException("文件过大！");
			}
			if (!RegexUtil.matching("(\\.png|\\.PNG|\\.jpg|\\.JPG)$", user
					.getFile().getOriginalFilename())) {
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("请上传jpg或png格式的图片");
				return ajaxResult;
			}
			
			
			if (user.getUserId() == null || "".equals(user.getUserId())
					|| !sessionUser.getUserId().equals(user.getUserId())) {
				throw new ServiceException("修改失败");
			} else {
				//读取配置文件
				ResourceBundle rb = null;
				// 读取acf_config.properties配置文件
				rb = ResourceBundle.getBundle("acf_config");
				if (null == rb) {
					logger.info("acf_config.properties文件不存在，请检查文件路径！");
					ajaxResult.setSuccess(false);
					ajaxResult.setMessage("acf_config.properties文件不存在，请检查文件路径！");
					return ajaxResult;
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
				sessionUser.setVxImgName(localFileName);
				sessionUser.setVxImgPath(sysName+"/images/vximg/" + user.getLoginName() + "/" + localFileName);
				
				sessionUser.setUserName(user.getUserName());
				sessionUser.setUserEmail(user.getUserEmail());
				sessionUser.setUserMobTel(user.getUserMobTel());
				sessionUser.setUserTel(user.getUserTel());
				sessionUser.setLastModUser(user.getUserId());
				sessionUser.setLastModTime(DateUtil.getCurrentSystemTime());
				sysUserInfoService.save(sessionUser);
				
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
				ajaxResult.setData(sessionUser);
				ajaxResult.setSuccess(true);
				ajaxResult.setMessage("保存成功！");
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

	/**
	 * 保存用户-角色
	 */
	@RequestMapping(value = "/userRoleSave", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult userRoleSave(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, String userId,
			String ids) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {

			List<String> listIds = null;
			if (ids != null && !"".equals(ids)) {
				String[] arrIds = ids.split(",");
				listIds = Arrays.asList(arrIds);
			}
			sysRoleInfoService.bacthInsertUserRole(userId, listIds);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("用户角色保存成功！");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response, SysUserInfo user)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			SessionInfo sessionInfo = (SessionInfo) request.getSession()
					.getAttribute(BaseConstants.USER_SESSION_KEY);
			ajaxResult.setData(user);
			if (sessionInfo.getUser().getLoginName()
					.equals(user.getLoginName())) {
				throw new ServiceException("当前用户不能删除！");
			} else {
				sysUserInfoService.delete(user);
				ajaxResult.setSuccess(true);
				ajaxResult.setMessage("用户删除成功！");
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ajaxResult;
	}

	/**
	 * 获取一条数据
	 */
	@RequestMapping(value = "/getById")
	@ResponseBody
	public AjaxResult getById(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(true);
		try {
			ajaxResult.setData(sysUserInfoService.getUserById(request.getParameter("USER_ID")));
		} catch (ServiceException e) {
			ajaxResult.setData(null);
			ajaxResult.setSuccess(false);
		}
		return ajaxResult;
	}

	/**
	 * 修改密码
	 * 
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult modifyPwd(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, SysUserInfo user)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			SysUserInfo sessionUser = ((SessionInfo) session
					.getAttribute(BaseConstants.USER_SESSION_KEY)).getUser();
			if (Encrypt.e(user.getLoginPassword()).equals(
					sessionUser.getLoginPassword())) {
				sessionUser.setLoginPassword(Encrypt.e(user.getNewPassword()));
				sysUserInfoService.save(sessionUser);
				ajaxResult.setSuccess(true);
				ajaxResult.setMessage("密码修改成功！");
			} else {
				ajaxResult.setMessage("旧密码错误,密码修改失败！");
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ajaxResult;
	}

	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	public AjaxResult addRole() {
		AjaxResult ajaxResult = new AjaxResult();
		return ajaxResult;
	}

	@RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult resetPwd(HttpServletRequest request,
			HttpServletResponse response, String userId)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			SysUserInfo sysUserInfo = sysUserInfoService.resetPwd(userId);
			ajaxResult.setData(sysUserInfo);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("用户密码已重置！");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ajaxResult;
	}

	@RequestMapping(value = "/branchTree")
	@ResponseBody
	public AjaxResult userBranchTree(String parentNo) throws ServiceException {
		if (parentNo == null) {
			parentNo = Tree.ROOT_ID;
		}
		AjaxResult ajaxResult = new AjaxResult();
		List<SysBranchInfo> branchList = sysBranchInfoService
				.findByParentId(parentNo);
		List<Tree> treeList = new ArrayList<Tree>(branchList.size());
		for (SysBranchInfo branch : branchList) {
			Tree tree = new Tree(branch);
			tree.setState(Tree.TREE_CLOSE_STATE);
			treeList.add(tree);
		}
		ajaxResult.setSuccess(true);
		ajaxResult.setData(treeList);
		return ajaxResult;
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
	 * 通过部门Id获得部门名称
	 */
	@RequestMapping("/getBranchNameById")
	@ResponseBody
	public AjaxResult getBracnById(HttpServletRequest request,
			HttpServletResponse response, String branchNo) {
		AjaxResult ajaxResult = new AjaxResult();
		String data = "未设置";
		if (branchNo == null || "".equals(branchNo) || "null".equals(branchNo)) {
			ajaxResult.setData(data);
		} else {
			SysBranchInfo sbi = sysBranchInfoService.getById(branchNo);
			if (sbi != null) {
				if (sbi.getBranchName() != null) {
					data = sbi.getBranchName();
				}
			}
			ajaxResult.setData(data);
		}
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 下载excel模板
	 */
	@RequestMapping(value = "/downExcel")
	public void downExcel(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/vnd.ms-excel");
		try {
			String fileName = "用户批量导入模板-"
					+ DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xls";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;fileName="
					+ fileName);
			ServletOutputStream out = response.getOutputStream();
			String path = request.getSession().getServletContext()
					.getRealPath("/")
					+ "/download/userExcelModel/userModel.xls";
			out.write(FileUtil.file2OutStream(path).toByteArray());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 上传excel文件，并批量导入用户
	 * 
	 * @throws ServiceException
	 */
	@RequestMapping("/uploadExcel")
	@ResponseBody
	public AjaxResult uploadExcel(HttpServletRequest request,
			HttpServletResponse response, UploadExcel file)
			throws ServiceException {
		if (file.getFile() == null
				|| file.getFile().getOriginalFilename() == null
				|| "".equals(file.getFile().getOriginalFilename())) {
			throw new ServiceException("不存在的文件");
		}
		if (file.getFile().getSize() > 10240000) {
			throw new ServiceException("文件过大！");
		}
		AjaxResult ajaxResult = new AjaxResult();
		boolean isExcel = false;
		try {
			if (RegexUtil.matching("(\\.xls|\\.XLS|\\.xlsx|\\.XLSX)$", file
					.getFile().getOriginalFilename())) {
				isExcel = true;
			}
			if (isExcel) {
				List<SysUserInfo> list = sysUserInfoService.saveBacthUser(file
						.getFile().getInputStream());
				if (list.size() <= 0) {
					ajaxResult.setSuccess(true);
					ajaxResult.setMessage("文件上传成功");
				} else {
					ajaxResult.setSuccess(false);
					StringBuffer sb = new StringBuffer();
					sb.append("文件中出现重复用户，重复的登录名称：");
					sb.append("\r\n");
					for (int i = 0, j = list.size(); i < j; i++) {
						sb.append(list.get(i).getLoginName()).append(",")
								.append("\r\n");
					}
					ajaxResult.setMessage(sb.toString());
				}
			} else {
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("文件上传失败，请检查文件类型！");
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ajaxResult;
	}

	/**
	 * 数据列表
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/userRoleDataGrid")
	@ResponseBody
	public DataGrid userRoleDataGrid(HttpServletRequest request,
			SysUserInfo user) {
		DataGrid dataGrid = sysRoleInfoService.dataGrid(new SysRoleInfo(), 1,
				Integer.MAX_VALUE);
		List<SysRoleInfo> roles = sysRoleInfoService.getByUserId(user
				.getUserId());
		for (SysRoleInfo r : (List<SysRoleInfo>) dataGrid.getRows()) {
			if (roles.contains(r)) {
				r.setChecked(true);
			} else {
				r.setChecked(false);
			}
		}
		return dataGrid;
	}

	public String callbackAapl(HttpServletRequest request) throws Exception {
		String res = "";
		try {
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			res = sb.toString();
			System.out.println(res);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		}
		return "ok";
	}

	/**
	 * 审批用户
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/appUserSta")
	@ResponseBody
	public AjaxResult appUserSta(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(true);
		ajaxResult.setMessage("审批成功！");
		try {
			String userId = request.getParameter("USER_ID");
			SysUserInfo userInfo = sysUserInfoService.getUserById(userId);
			if(userInfo != null){
				userInfo.setLastModTime(DateUtil.getCurrentSystemTime());
				userInfo.setUserStatus(true);
				//更新用户状态
				this.sysUserInfoService.save(userInfo);
				//设置用户角色权限
				this.sysUserInfoService.resetInitRole(userInfo);
				//新增访问角色
				this.sysUserInfoService.addUserView(userInfo);
			}
		} catch (Exception e) {
			logger.error("审批失败："+e.getMessage(),e);
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("审批失败！");
		}
		return ajaxResult;
	}
	/**
	 * 重置角色权限
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/resetInitRole")
	@ResponseBody
	public AjaxResult resetInitRole(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(true);
		try {
			String userId = request.getParameter("USER_ID");
			SysUserInfo userInfo = sysUserInfoService.getUserById(userId);
			if(userInfo != null){
				//设置用户角色权限
				this.sysUserInfoService.resetInitRole(userInfo);
			}
		} catch (Exception e) {
			logger.error("审批失败："+e.getMessage(),e);
			ajaxResult.setSuccess(false);
		}
		return ajaxResult;
	}
}
