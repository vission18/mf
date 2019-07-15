package com.vission.mf.base.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.bo.UploadJson;
import com.vission.mf.base.model.po.SysAnnoAcceInfo;
import com.vission.mf.base.model.po.SysAnnoInfo;
import com.vission.mf.base.model.po.SysAnnoTypeInfo;
import com.vission.mf.base.service.SysAnnoInfoService;
import com.vission.mf.base.service.SysAnnoTypeInfoService;

/**
 * 公告控制器
 */
@Controller
@RequestMapping("/anno")
public class AnnoController extends BaseController {

	@Autowired
	private SysAnnoInfoService sysAnnoInfoService;
	@Autowired
	private SysAnnoTypeInfoService sysAnnoTypeInfoService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "base/anno";
	}

	/**
	 * 加载公告数据列表
	 */
	@RequestMapping(value = "/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request, SysAnnoInfo anno) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		DataGrid datagrid = sysAnnoInfoService.dataGrid(anno, pageNo, pageSize);
		return datagrid;
	}

	/**
	 * 保存公告
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, SysAnnoInfo anno)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		// 验证公告标题不能输入空格
		if (anno.getAnnoTitle().trim() == null
				|| "".equals(anno.getAnnoTitle().trim())) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("公告标题不能为空！");
			return ajaxResult;
		}

		// 验证begindate和enddate
		Date begindate = anno.getBeginDt();
		Date enddate = anno.getEndDt();
		if (begindate.after(enddate)) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("开始日期不能大于结束日期");
			return ajaxResult;
		}
		String typeId = request.getParameter("typeId");
		anno.setTypeId(typeId);
		try {
			if (anno.getAnnoId() == null || "".equals(anno.getAnnoId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			SessionInfo sessionInfo = (SessionInfo) request.getSession()
					.getAttribute(BaseConstants.USER_SESSION_KEY);
			sysAnnoInfoService.save(anno, sessionInfo.getUser());
			SysAnnoTypeInfo sysAnnoTypeInfo = sysAnnoTypeInfoService
					.getAnnoTypesById(anno.getTypeId());
			anno.setTypeId(sysAnnoTypeInfo.getTypeName());// 在页面typeid要显示成typename
			ajaxResult.setData(anno);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("保存成功!");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage(e.getMessage());
		}
		response.setContentType("text/html,charset=utf-8");
		return ajaxResult;
	}

	/**
	 * 删除公告
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response, SysAnnoInfo anno)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		sysAnnoInfoService.delete(anno);
		ajaxResult.setData(anno);
		ajaxResult.setSuccess(true);
		ajaxResult.setMessage("删除成功！");
		return ajaxResult;
	}

	/**
	 * 按annoid获取anno
	 */
	@RequestMapping(value = "/getAnnoById")
	@ResponseBody
	public AjaxResult getById(SysAnnoInfo anno) {
		AjaxResult ajaxResult = new AjaxResult();
		SysAnnoInfo annoInfo = sysAnnoInfoService.getAnnoById(anno.getAnnoId());
		ajaxResult.setData(annoInfo);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 按annotitle获取anno
	 */
	@RequestMapping(value = "/annoRead")
	@ResponseBody
	public AjaxResult getByAnnoTitle(SysAnnoInfo anno) {
		AjaxResult ajaxResult = new AjaxResult();
		SysAnnoInfo annoInfo = sysAnnoInfoService.getAnnoById(anno.getAnnoId());
		SysAnnoTypeInfo s = sysAnnoTypeInfoService.getAnnoTypeById(annoInfo
				.getTypeId());
		annoInfo.setTypeId(s.getTypeName());
		ajaxResult.setData(annoInfo);
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 下载文件
	 */
	@RequestMapping(value = "/download")
	@ResponseBody
	public void download(HttpServletRequest request,
			HttpServletResponse response, String fileName, String annoId)
			throws Exception {
		String result = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
		AjaxResult ajaxResult = new AjaxResult();
		try {
			SysAnnoAcceInfo sysAnnoAcceInfo = sysAnnoInfoService.getAnnoAcce(
					annoId, result);
			if (sysAnnoAcceInfo == null) {
				throw new ServiceException("不存在的文件");
			}
			ServletOutputStream out = null;
			response.reset();
			response.setContentType("application/x-msdownload;");
			result = java.net.URLEncoder.encode(result, "UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ result);
			out = response.getOutputStream();

			DataOutputStream os = new DataOutputStream(out);
			ByteArrayInputStream is = new ByteArrayInputStream(
					sysAnnoAcceInfo.getFileContent());
			byte[] buf = new byte[1024];
			while (is.read(buf) > 0) {
				os.write(buf);
			}
			is.close();
			os.flush();
			os.close();
		} catch (Exception ex) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("文件不存在");
		}
	}

	/**
	 * 上传文件 点导入时就存入数据库，点取消的话再去数据库把annoid为linshiid的acce都删掉。
	 * 点保存的话直接把所有annoid为linshiid的更新
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult upload(HttpServletRequest request,
			HttpServletResponse response, UploadJson file) throws Exception {
		AjaxResult ajaxResult = new AjaxResult();
		SysAnnoAcceInfo sysAnnoAcceInfo = new SysAnnoAcceInfo();
		try {
			if (file.getFile() == null
					|| file.getFile().getOriginalFilename() == null
					|| "".equals(file.getFile().getOriginalFilename())) {
				throw new ServiceException("不存在的文件");
			}
			String tempFileName = file.getFile().getOriginalFilename();
			tempFileName = tempFileName.replaceAll("\\\\", "/");
			String localFileName = tempFileName.substring(
					tempFileName.lastIndexOf("/") + 1, tempFileName.length());
			sysAnnoAcceInfo.setFileName(localFileName);

			InputStream stream = file.getFile().getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			stream.close();

			// 点击上传，保存附件到数据库
			sysAnnoAcceInfo.setFileContent(out.toByteArray());
			out.close();
			sysAnnoInfoService.saveAcceWithOutId(sysAnnoAcceInfo);

			sysAnnoAcceInfo.setFileContent(null);
			SysAnnoInfo sysAnnoInfo = new SysAnnoInfo();
			sysAnnoInfo.getAnnoAcceList().add(sysAnnoAcceInfo);
			ajaxResult.setData(sysAnnoInfo.getAnnoAcceList());
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("文件上传失败，请检查文件类型！");
		}
		ajaxResult.setSuccess(true);
		ajaxResult.setMessage("文件上传成功！");
		return ajaxResult;
	}

	/**
	 * 删除公告附件
	 */
	@RequestMapping(value = "/fileDel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult fileDel(HttpServletRequest request,
			HttpServletResponse response, String fileName,
			SysAnnoInfo sysAnnoInfo) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			if (sysAnnoInfo.getAnnoId() == null
					|| sysAnnoInfo.getAnnoId().trim().equals("")) {
			} else {
				sysAnnoInfoService
						.deleteAcce(sysAnnoInfo.getAnnoId(), fileName);
			}

		} catch (ServiceException ex) {
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("附件删除失败！");
		}
		ajaxResult.setData(sysAnnoInfo);
		ajaxResult.setSuccess(true);
		ajaxResult.setMessage("附件删除成功！");
		return ajaxResult;
	}

	/**
	 * 按照annoId获取附件
	 */
	@RequestMapping(value = "/getAcceInforUrl")
	@ResponseBody
	public AjaxResult getAcceInforUrl(SysAnnoInfo anno) throws IOException {
		AjaxResult ajaxResult = new AjaxResult();
		List<SysAnnoAcceInfo> sysAnnoAcceInfo = sysAnnoInfoService
				.getSysAnnoAcceInfoByAnnoId(anno.getAnnoId());
		if (sysAnnoAcceInfo.size() > 0) {
			ajaxResult.setData(sysAnnoAcceInfo);
			ajaxResult.setSuccess(true);
		} else {
			ajaxResult.setSuccess(false);
		}
		return ajaxResult;
	}

	/**
	 * 导入附件后，点击取消按钮，在去数据库中删除附件
	 */
	@RequestMapping(value = "/deletelinshiidacce", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult deletelinshiidacce(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		sysAnnoInfoService.deletelinshiidacce();
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}

	/**
	 * 表单提交类型绑定
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

}