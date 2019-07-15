package com.vission.mf.base.sellms.smscompinfo.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vission.mf.base.controller.BaseController;
import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.AjaxResult;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.po.UploadExcel;
import com.vission.mf.base.sellms.smscompinfo.db.SMS_COMP_INFO;
import com.vission.mf.base.sellms.smscompinfo.po.SmsCompInfo;
import com.vission.mf.base.sellms.smscompinfo.service.SmsCompInfoService;
import com.vission.mf.base.util.DateUtil;
import com.vission.mf.base.util.FileUtil;
import com.vission.mf.base.util.RegexUtil;

/**
 * 作者：acf
 * 描述：SmsCompInfoController 控制层
 * 日期：2019-6-21 9:29:04
 * 类型：CONTROLLER文件
 */
@Controller
@RequestMapping("/smscompinfo")
public class SmsCompInfoController extends BaseController {

	@Autowired
	private SmsCompInfoService smscompinfoService;

	/**
	 * 跳转至jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "sellms/smscompinfo/SmsCompInfoList";
	}

	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/smscompinfoDataGrid")
	@ResponseBody
	public DataGrid smscompinfoDataGrid(HttpServletRequest request, SmsCompInfo po) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return smscompinfoService.dataGrid(po, pageNo, pageSize,request);
	}


	/**
	 * 获取一条数据
	 */
	@RequestMapping(value = "/getSmsCompInfoById")
	@ResponseBody
	public AjaxResult getSmsCompInfoById(HttpServletRequest request,
			HttpServletResponse response) {
		AjaxResult ajaxResult = new AjaxResult();
		String pkId = request.getParameter("PK_ID");
		ajaxResult.setData(smscompinfoService.getSmsCompInfoById(pkId));
		ajaxResult.setSuccess(true);
		return ajaxResult;
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/deleteSmsCompInfoById", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {	
			String pkId = request.getParameter("PK_ID");
			smscompinfoService.deleteSmsCompInfoById(pkId);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("删除成功！");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("删除失败！");
		}
		return ajaxResult;
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/saveSmsCompInfo", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult saveSmsCompInfo(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, SmsCompInfo po)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		try {
			if (po.getPkId() == null || "".equals(po.getPkId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
				po.setCreateTime(DateUtil.getCurrentSystemTime());
				po.setCreateUser(sessionInfo.getUser().getUserId());
				smscompinfoService.saveSmsCompInfo(po,"add");
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
				po.setLastModTime(DateUtil.getCurrentSystemTime());
				po.setLastModUser(sessionInfo.getUser().getUserId());
				smscompinfoService.saveSmsCompInfo(po,"update");
			}
			ajaxResult.setData(po);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("保存成功！");
		} catch (Exception e) {
			ajaxResult.setSuccess(false);
			ajaxResult.setMessage("保存失败！");
			throw new ServiceException(e);
		}
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
	
	/**
	 * 下载excel模板
	 */
	@RequestMapping(value = "/downloadCompExcel")
	public void downloadCompExcel(HttpServletRequest request,
			HttpServletResponse response) throws ServiceException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/vnd.ms-excel");
		try {
			String fileName = "批量导入公司信息模板-"
					+ DateUtil.format(new Date(), "yyyyMMddHHmmss")
					+ ".xlsx";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;fileName="
					+ fileName);
			ServletOutputStream out = response.getOutputStream();
			String path = request.getSession().getServletContext()
					.getRealPath("/")
					+ "/download/sellms/companyModel.xlsx";
			out.write(FileUtil.file2OutStream(path).toByteArray());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//批量上传文件
	@RequestMapping(value="/uploadCompExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult uploadCompExcel(HttpServletRequest request,
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
				List<SmsCompInfo> list = smscompinfoService.saveBacthCompInfo(file
						.getFile().getInputStream(),request);
				if (list.size() <= 0) {
					ajaxResult.setSuccess(true);
					ajaxResult.setMessage("文件上传成功");
				} else {
					ajaxResult.setSuccess(false);
					StringBuffer sb = new StringBuffer();
					sb.append("文件中出现重复的公司名称：");
					sb.append("\r\n");
					for (int i = 0, j = list.size(); i < j; i++) {
						sb.append(list.get(i).getCompName()).append(",").append("\r\n");
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
		response.setContentType("text/html;charset=utf-8");
		return ajaxResult;
	}
	
	/**
	 * 行业分类
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/fcList")
	public String fcList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("industryType", SMS_COMP_INFO.INDUSTRY_TYPE_FC);
		this.getAccessButtons(request, model);
		return "sellms/smscompinfo/indTypeComList";
	}
	@RequestMapping("/bxList")
	public String bxList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("industryType", SMS_COMP_INFO.INDUSTRY_TYPE_BX);
		this.getAccessButtons(request, model);
		return "sellms/smscompinfo/indTypeComList";
	}
	@RequestMapping("/jrList")
	public String jrList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("industryType", SMS_COMP_INFO.INDUSTRY_TYPE_JR);
		this.getAccessButtons(request, model);
		return "sellms/smscompinfo/indTypeComList";
	}
	@RequestMapping("/qcList")
	public String qcList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("industryType", SMS_COMP_INFO.INDUSTRY_TYPE_QC);
		this.getAccessButtons(request, model);
		return "sellms/smscompinfo/indTypeComList";
	}
	@RequestMapping("/zxList")
	public String zxList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		model.addAttribute("industryType", SMS_COMP_INFO.INDUSTRY_TYPE_ZX);
		this.getAccessButtons(request, model);
		return "sellms/smscompinfo/indTypeComList";
	}
	
	@RequestMapping(value = "/indTypeComDataGrid")
	@ResponseBody
	public DataGrid indTypeComDataGrid(HttpServletRequest request, SmsCompInfo po) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return smscompinfoService.indTypeComDataGrid(po, pageNo, pageSize,request);
	}
}
