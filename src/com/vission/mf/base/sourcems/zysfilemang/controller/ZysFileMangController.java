package com.vission.mf.base.sourcems.zysfilemang.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vission.mf.base.controller.BaseController;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysUserInfo;
import com.vission.mf.base.model.po.UploadExcel;
import com.vission.mf.base.sourcems.zysfilemang.db.SMS_CODE_TABLE;
import com.vission.mf.base.sourcems.zysfilemang.po.ZysFileMang;
import com.vission.mf.base.sourcems.zysfilemang.service.ZysFileMangService;
import com.vission.mf.base.util.DateUtil;
import com.vission.mf.base.util.FileUtil;
import com.vission.mf.base.util.RegexUtil;

/**
 * 作者：acf 描述：ZysFileMangController 控制层 日期：2019-7-4 15:07:15 类型：CONTROLLER文件
 */
@Controller
@RequestMapping("/zysFileMan")
public class ZysFileMangController extends BaseController {

	@Autowired
	private ZysFileMangService zysFileManService;

	/**
	 * 跳转至jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		try {
			//保存日志
			this.zysFileManService.saveOperLog("资源管理", "查看资源文件模块");
		} catch (ServiceException e) {
			//不影响主功能
			logger.error("保存日志异常："+e.getMessage(),e);
		}
		return "sourcems/zysfilemang/zysFileManList";
	}

	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/zysFileManDataGrid")
	@ResponseBody
	public DataGrid zysFileManDataGrid(HttpServletRequest request, HttpSession session,
			ZysFileMang po) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
			String fileType = request.getParameter("fileTypeComm");
			po.setFileType(fileType);
			SessionInfo sessionInfo = (SessionInfo) session
					.getAttribute(BaseConstants.USER_SESSION_KEY);
			String userId = sessionInfo.getUser().getUserId();
			if(!"admin".equals(userId)){
				po.setCreateUser(userId);
			}
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return zysFileManService.dataGrid(po, pageNo, pageSize);
	}

	/**
	 * 获取一条数据
	 */
	@RequestMapping(value = "/getZysFileMangById")
	@ResponseBody
	public AjaxResult getZysFileMangById(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		String pkId = request.getParameter("PK_ID");
		ajaxResult.setData(zysFileManService.getZysFileMangById(pkId));
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/deleteZysFileMangById", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			String pkId = request.getParameter("PK_ID");
			zysFileManService.deleteZysFileMangById(pkId);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功！");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("删除失败！");
		}
		return ajaxResult;
	}


	/**
	 * 查询码表参数
	 * 
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/queryTbCodePara", method = RequestMethod.POST)
	@ResponseBody
	public Map queryTbCodePara(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Map<String, List> paraMap = new HashMap<String, List>();
		try {
			// 文件类型
			List<Tree> fileTypeList = this.zysFileManService
					.queryCodeTableByCodeType(SMS_CODE_TABLE.FILE_TYPE, false);
			if (fileTypeList != null && fileTypeList.size() > 0) {
				paraMap.put("fileTypeList", fileTypeList);
			}

		} catch (Exception e) {
			logger.error("获取参数失败！", e);
		}
		response.setContentType("text/html;charset=utf-8");
		return paraMap;
	}

	@RequestMapping("/saveFileRec")
	@ResponseBody
	public AjaxResult saveFileRec(HttpServletRequest request,
			ZysFileMang fileInfo, HttpServletResponse response,
			HttpSession session) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(false);
		ajaxResult.setMessage("文件保存失败！");
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		try {
			if (fileInfo.getPkId() == null || "".equals(fileInfo.getPkId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);// 初始化
				this.zysFileManService.initFilePara(fileInfo);
				fileInfo.setCreateUser(sessionInfo.getUser().getUserName());
			} else {
				//获取修改前的文件
				ZysFileMang po = this.zysFileManService.getZysFileMangById(fileInfo.getPkId());
				if(po != null){
					/*fileInfo.setFilePath(po.getFilePath());
					fileInfo.setFileSize(po.getFileSize());*/
					fileInfo.setLastModTime(DateUtil.getCurrentSystemTime());
					fileInfo.setLastModUser(sessionInfo.getUser().getUserName());
					fileInfo.setCreateTime(po.getCreateTime());
					fileInfo.setCreateUser(po.getCreateUser());
					fileInfo.setDownloadCount(po.getDownloadCount());
					fileInfo.setDownloadCountOndate(po.getDownloadCountOndate());
					fileInfo.setCountClick(po.getCountClick());
					fileInfo.setCountClickOndate(po.getCountClickOndate());
					fileInfo.setCountOndate(po.getCountOndate());
					fileInfo.setCountZan(po.getCountZan());
					fileInfo.setCountZanOndate(po.getCountZanOndate());
					fileInfo.setCountOndate(po.getCountOndate());
				}
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			// 保存记录
			this.zysFileManService.saveZysFileMang(fileInfo);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("文件保存成功！");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("文件保存异常，请联系管理员！");
			throw new ServiceException("不存在的文件");
		}
		return ajaxResult;
	}

	/**
	 * 下载文件
	 * 
	 * @param request
	 * @param response
	 * @throws ServiceException
	 */
	@RequestMapping(value = "/downloadFileByPkId")
	public void downloadFileByPkId(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		try {
			ZysFileMang fileInfo = this.zysFileManService
					.getZysFileMangById(request.getParameter("PK_ID"));
			if (fileInfo != null) {
				String fileName = fileInfo.getFileNm();
				fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
				response.setHeader("Content-Disposition",
						"attachment;fileName=" + fileName);
				ServletOutputStream out = response.getOutputStream();
				String path = fileInfo.getFilePath();
				path = path.replaceAll("\\\\", "/");
				out.write(FileUtil.file2OutStream(path).toByteArray());
				out.flush();
				out.close();
				
				try {
					//保存日志
					this.zysFileManService.saveOperLog("资源管理", "下载文件【"+fileName+"】");
				} catch (ServiceException e) {
					//不影响主功能
					logger.error("保存日志异常："+e.getMessage(),e);
				}
			}
			//更新下载次数
			fileInfo.setDownloadCount(fileInfo.getDownloadCount()+1);
			this.zysFileManService.saveZysFileMang(fileInfo);
		} catch (Exception e) {
			throw new ServiceException("下载失败，服务器压力过大，建议使用网盘下载");
		}
	}
	
	@RequestMapping(value = "/updateDownloadCount")
	@ResponseBody
	public AjaxResult updateDownloadCount(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setSuccess(true);
		try {
			String pkId = request.getParameter("PK_ID");
			ZysFileMang fileInfo = zysFileManService.getZysFileMangById(pkId);
			if(fileInfo != null){
				fileInfo.setDownloadCount(fileInfo.getDownloadCount()+1);
				this.zysFileManService.saveZysFileMang(fileInfo);
				try {
					//保存日志
					this.zysFileManService.saveOperLog("资源管理", "网盘下载文件【"+fileInfo.getFileNm()+"】");
				} catch (ServiceException e) {
					//不影响主功能
					logger.error("保存日志异常："+e.getMessage(),e);
				}
			}
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
		}
		return ajaxResult;
	}
	/**
	 * 免费专区
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/freelist")
	public String freelist(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("fileTypeList", "f05");
		this.getAccessButtons(request, model);
		try {
			//保存日志
			this.zysFileManService.saveOperLog("资源管理", "免费专区模块");
		} catch (ServiceException e) {
			//不影响主功能
			logger.error("保存日志异常："+e.getMessage(),e);
		}
		return "sourcems/zysfilemang/fileTypeCommPage";
	}
	
	/**
	 * 系统工具
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/sysTollslist")
	public String sysTollslist(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("fileTypeList", "f04");
		this.getAccessButtons(request, model);
		try {
			//保存日志
			this.zysFileManService.saveOperLog("资源管理", "系统工具模块");
		} catch (ServiceException e) {
			//不影响主功能
			logger.error("保存日志异常："+e.getMessage(),e);
		}
		return "sourcems/zysfilemang/fileTypeCommPage";
	}
	
	/**
	 * 软件设计
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/softDesignlist")
	public String softDesignlist(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("fileTypeList", "f03");
		this.getAccessButtons(request, model);
		try {
			//保存日志
			this.zysFileManService.saveOperLog("资源管理", "软件设计模块");
		} catch (ServiceException e) {
			//不影响主功能
			logger.error("保存日志异常："+e.getMessage(),e);
		}
		return "sourcems/zysfilemang/fileTypeCommPage";
	}
	
	/**
	 * 影视资源
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/videoSourcelist")
	public String videoSourcelist(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("fileTypeList", "f02");
		this.getAccessButtons(request, model);
		try {
			//保存日志
			this.zysFileManService.saveOperLog("资源管理", "影视资源模块");
		} catch (ServiceException e) {
			//不影响主功能
			logger.error("保存日志异常："+e.getMessage(),e);
		}
		return "sourcems/zysfilemang/fileTypeCommPage";
	}
	
	/**
	 * 代码编程
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/codelist")
	public String codelist(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("fileTypeList", "f01");
		this.getAccessButtons(request, model);
		try {
			//保存日志
			this.zysFileManService.saveOperLog("资源管理", "代码编程模块");
		} catch (ServiceException e) {
			//不影响主功能
			logger.error("保存日志异常："+e.getMessage(),e);
		}
		return "sourcems/zysfilemang/fileTypeCommPage";
	}
}
