package com.vission.mf.base.mms;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.vission.mf.base.util.DateUtil;
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
import com.vission.mf.base.model.po.MmsXfType;
import com.vission.mf.base.model.po.ShowOprMessage;
import com.vission.mf.base.model.po.UploadExcel;
import com.vission.mf.base.util.FileUtil;
import com.vission.mf.base.util.RegexUtil;

/**
 * 功能/模块 ：消费类别
 */
@Controller
@RequestMapping("/xfTypeController")
public class XfTypeController extends BaseController {

	@Autowired
	private XfTypeService xfTypeService;

	/**
	 * 跳转至xfType.jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "mms/xfType";
	}

	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request, MmsXfType xfType) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return xfTypeService.dataGrid(xfType, pageNo, pageSize);
	}

	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, MmsXfType xfType)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			if (xfType.getTypeId() == null || "".equals(xfType.getTypeId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			ShowOprMessage oprMsg=xfTypeService.save(xfType);
			ajaxResult.setData(xfType);
			ajaxResult.setMessage(oprMsg.getResMsg());
			if(oprMsg.isOperFlag()){
				ajaxResult.setSuccess(true);
			}else{
				ajaxResult.setSuccess(false);
			}
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
			HttpServletResponse response, MmsXfType xfType)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			ajaxResult.setData(xfType);
			xfTypeService.delete(xfType);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("我走了，主人再见！");
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
	public AjaxResult getById(MmsXfType xfType) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData(xfTypeService.getXfTypeById(xfType.getTypeId()));
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
			String fileName = "消费类别导入模板-"
					+ DateUtil.format(new Date(), "yyyyMMddHHmmss")
					+ ".xls";
			fileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;fileName="
					+ fileName);
			ServletOutputStream out = response.getOutputStream();
			String path = request.getSession().getServletContext()
					.getRealPath("/")
					+ "/download/xfTypeExcelModel/xfTypeModel.xls";
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
				List<MmsXfType> list = xfTypeService.saveMoreXfType(file
						.getFile().getInputStream());
				if (list.size() <= 0) {
					ajaxResult.setSuccess(true);
					ajaxResult.setMessage("主人，好厉害，文件已上传成功");
				} else {
					ajaxResult.setSuccess(false);
					StringBuffer sb = new StringBuffer();
					sb.append("主人，文件中出现重复数据:  ");
					sb.append("\r\n");
					for (int i = 0, j = list.size(); i < j; i++) {
						sb.append(list.get(i).getTypeName()).append(",")
								.append("\r\n");
					}
					ajaxResult.setMessage(sb.toString());
				}
			} else {
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("主人，很遗憾，文件上传失败，请检查文件类型！");
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ajaxResult;
	}
	/**
	 * 导出Excel文件
	 */
	@RequestMapping(value = "/expExcel")
	@ResponseBody
	public void downloadByTabNo(HttpServletRequest request,HttpServletResponse response) {
       		try {
       			OutputStream os = response.getOutputStream();
       			response.reset();
       			response.setContentType("application/msexcel");
       			WritableWorkbook wbook = Workbook.createWorkbook(os);
       			WritableSheet wsheet = wbook.createSheet("消费类别", 0);
       			int charNormal = 11;
       			WritableFont oneFont = new WritableFont(
       					WritableFont.createFont("宋体"), charNormal);
       			jxl.write.WritableCellFormat normalFormat = new jxl.write.WritableCellFormat(
       					oneFont);
       			normalFormat.setAlignment(Alignment.CENTRE);
       			normalFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
       			normalFormat.setWrap(true); // 是否换行
       			WritableFont wfont = new WritableFont(WritableFont.ARIAL, 12,
       					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
       					Colour.BLACK);
       			WritableCellFormat wcfFC = new WritableCellFormat(wfont);
       			wcfFC.setBackground(Colour.AQUA);
       			wfont = new jxl.write.WritableFont(WritableFont.ARIAL, 11,
       					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
       					Colour.BLACK);
       			wcfFC = new WritableCellFormat(wfont);
       			wcfFC.setAlignment(Alignment.CENTRE);
       			wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
       			//根据对象属性个数获取列
       			List<MmsXfType> xfTypes=xfTypeService.getAll();
       			wsheet.setColumnView(0, 40);
       			wsheet.setColumnView(1, 10);
       			wsheet.setColumnView(2, 10);
       			wsheet.setColumnView(3, 30);
       			// 开始生成主体内容
       			wsheet.mergeCells(0, 0, 3, 0);
       			Label tit00 = new Label(0, 0, "消费类别数据", wcfFC);
       			wsheet.addCell(tit00);
       			// 1行0列
       			wsheet.addCell(new Label(0, 1, "类别编号", wcfFC));
       			wsheet.addCell(new Label(1, 1, "类别名称", wcfFC));
       			wsheet.addCell(new Label(2, 1, "类别状态 ", wcfFC));
       			wsheet.addCell(new Label(3, 1, "备注", wcfFC));
       			for (int i = 0; i < xfTypes.size(); i++) {
       				MmsXfType xt = xfTypes.get(i);
       				wsheet.addCell(new Label(0, i + 2,xt.getTypeId()));
       				wsheet.addCell(new Label(1, i + 2,xt.getTypeName()));
       				if(xt.isTypeStatus()){
       					wsheet.addCell(new Label(2, i + 2,"启用"));
       				}else{
       					wsheet.addCell(new Label(2, i + 2,"停用"));
       				}
       				wsheet.addCell(new Label(3, i + 2,xt.getRemark()));
       			}
       			String filename = "ExcelTest"+DateUtil.format(new Date(), "yyyyMMddHHmmss")+".xls";
       			filename = new String(filename.getBytes("GBK"), "ISO8859-1");
       			response.setHeader("Content-disposition", "attachment; filename="+ filename);
       			wbook.write();
       			wbook.close();
       			os.close();
       		} catch (Exception ex) {
       			ex.printStackTrace();
       		}
	}
}
