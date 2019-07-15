package com.vission.mf.base.mms;

import java.io.OutputStream;
import java.util.ArrayList;
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

import net.sf.json.JSONArray;

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
import com.vission.mf.base.model.po.MmsXfRecord;
import com.vission.mf.base.model.po.MmsXfType;
import com.vission.mf.base.model.po.ShowOprMessage;
import com.vission.mf.base.model.po.UploadExcel;
import com.vission.mf.base.util.FileUtil;
import com.vission.mf.base.util.RegexUtil;

/**
 * 功能/模块 ：消费记录
 */
@Controller
@RequestMapping("/xfRecordController")
public class XfRecordController extends BaseController {

	@Autowired
	private XfRecordService xfRecordService;

	/**
	 * 跳转至xfRecord.jsp
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "mms/xfRecord";
	}

	/**
	 * 数据列表
	 */
	@RequestMapping(value = "/dataGrid")
	@ResponseBody
	public DataGrid dataGrid(HttpServletRequest request, MmsXfRecord xfRecord) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return xfRecordService.dataGrid(xfRecord, pageNo, pageSize);
	}

	/**
	 * 保存用户
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult save(HttpServletRequest request,
			HttpServletResponse response, HttpSession session, MmsXfRecord xfRecord)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			if (xfRecord.getXfId() == null || "".equals(xfRecord.getXfId())) {
				ajaxResult.setType(BaseConstants.OPER_TYPE_INSERT);
			} else {
				ajaxResult.setType(BaseConstants.OPER_TYPE_UPDATE);
			}
			SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY);
			//刷新记录日期(当前系统时间)
			xfRecord.setRecordDate(DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss"));
			xfRecord.setRecordUser(sessionInfo.getUser().getUserName());
			xfRecord.setDelFlag("0");
			ShowOprMessage oprMsg=xfRecordService.save(xfRecord);
			ajaxResult.setData(xfRecord);
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
	 * 做假删除，可在历史记录中查询
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult delete(HttpServletRequest request,
			HttpServletResponse response, MmsXfRecord xfRecord)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			if(xfRecord!=null){
				SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(BaseConstants.USER_SESSION_KEY);
				//刷新记录日期(当前系统时间)
				xfRecord.setRecordDate(DateUtil.format(new Date(), "yyyy/MM/dd HH:mm:ss"));
				xfRecord.setRecordUser(sessionInfo.getUser().getUserName()+"  <历史创建人："+xfRecord.getRecordUser()+">");
				xfRecord.setDelFlag("1");
				//将消费类别中文转为id
				List<MmsXfType> dList = new ArrayList<MmsXfType>();
				dList = xfRecordService.getAllType();
				for(int i=0;i<dList.size();i++){
					if(xfRecord.getXfType().equals(dList.get(i).getTypeName())){
						xfRecord.setXfType(dList.get(i).getTypeId());
					}
				}
				ShowOprMessage oprMsg=xfRecordService.save(xfRecord);
				ajaxResult.setData(xfRecord);
				ajaxResult.setMessage(oprMsg.getResMsg());
				if(oprMsg.isOperFlag()){
					ajaxResult.setSuccess(true);
				}else{
					ajaxResult.setSuccess(false);
				}
			}else{
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("主人，系统出现异常了...");
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
	public AjaxResult getById(MmsXfRecord xfRecord) {
		AjaxResult ajaxResult = new AjaxResult();
		ajaxResult.setData(xfRecordService.getXfRecordById(xfRecord.getXfId()));
		ajaxResult.setSuccess(true);
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
       			WritableSheet wsheet = wbook.createSheet("消费记录表", 0);
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
       			List<MmsXfRecord> xfRecord=xfRecordService.getAll();
       			List<MmsXfType> xts=xfRecordService.getAllType();
       			wsheet.setColumnView(0, 10);
       			wsheet.setColumnView(1, 10);
       			wsheet.setColumnView(2, 20);
    			wsheet.setColumnView(3, 20);
       			wsheet.setColumnView(4, 25);
    			wsheet.setColumnView(5, 15);
       			wsheet.setColumnView(6, 50);
       			// 开始生成主体内容
       			wsheet.mergeCells(0, 0, 6, 0);
       			Label tit00 = new Label(0, 0, "消费记录表", wcfFC);
       			wsheet.addCell(tit00);
       			// 1行0列
       			wsheet.addCell(new Label(0, 1, "消费类别", wcfFC));
       			wsheet.addCell(new Label(1, 1, "消费物品", wcfFC));
       			wsheet.addCell(new Label(2, 1, "价格", wcfFC));
       			wsheet.addCell(new Label(3, 1, "消费日期 ", wcfFC));
       			wsheet.addCell(new Label(4, 1, "记录日期", wcfFC));
       			wsheet.addCell(new Label(5, 1, "记录人 ", wcfFC));
       			wsheet.addCell(new Label(6, 1, "消费详情", wcfFC));
       			for (int i = 0; i < xfRecord.size(); i++) {
       				MmsXfRecord xr = xfRecord.get(i);
       				//将类型id转中文
       				for(int j=0;j<xts.size();j++){
       					if(xr.getXfType().equals(xts.get(j).getTypeId())){
       						wsheet.addCell(new Label(0, i + 2,xts.get(j).getTypeName()));
       					}
       				}
       				wsheet.addCell(new Label(1, i + 2,xr.getXfGoods()));
       				wsheet.addCell(new Label(2, i + 2,xr.getXfPrice()+""));
       				wsheet.addCell(new Label(3, i + 2,xr.getXfDate()));
       				wsheet.addCell(new Label(4, i + 2,xr.getRecordDate()));
       				wsheet.addCell(new Label(5, i + 2,xr.getRecordUser()));
       				wsheet.addCell(new Label(6, i + 2,xr.getXfDetail()));
       			}
       			String filename = "家庭消费记录表"+DateUtil.format(new Date(), "yyyyMMddHHmmss")+".xls";
       			filename = new String(filename.getBytes("GBK"), "ISO8859-1");
       			response.setHeader("Content-disposition", "attachment; filename="+ filename);
       			wbook.write();
       			wbook.close();
       			os.close();
       		} catch (Exception ex) {
       			ex.printStackTrace();
       			System.out.println("**********下载失败");
       		}
	}
	/**
	 * 获取类别信息
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/getAllType")
	@ResponseBody
	public JSONArray getAllType(HttpServletRequest request) {
		JSONArray ja = new JSONArray();
		List<MmsXfType> dList = new ArrayList<MmsXfType>();
		dList = xfRecordService.getAllType();
		ja = ja.fromObject(dList);
		return ja;
	}
	//以下是消费金额合计业务
	/**
	 * 跳转至xfMoneySum.jsp
	 */
	@RequestMapping("/moneySum")
	public String moneySum(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "mms/xfMoneySum";
	}
	/**
	 * 计算选中日期间的消费金额总和
	 */
	@RequestMapping(value = "/calculateMoney", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult calculateMoney(HttpServletRequest request,MmsXfRecord xfRecord) {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			ShowOprMessage sm=xfRecordService.calculateMoney(xfRecord);
			if(sm!=null){
				ajaxResult.setData(sm);
				ajaxResult.setSuccess(sm.isOperFlag());
				ajaxResult.setMessage(sm.getResMsg());
			}else{
				ajaxResult.setSuccess(false);
				ajaxResult.setMessage("主人，计算出错啦!去找李大叔");
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		return ajaxResult;
	}
	/**
	 * 跳转至xfRecord.jsp(历史记录)
	 */
	@RequestMapping("/hList")
	public String hList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		this.getAccessButtons(request, model);
		return "mms/xfHistory";
	}
	/**
	 * 数据列表(历史记录)
	 */
	@RequestMapping(value = "/dataGridHistory")
	@ResponseBody
	public DataGrid dataGridHistory(HttpServletRequest request, MmsXfRecord xfRecord) {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.parseInt(request.getParameter("page"));
			pageSize = Integer.parseInt(request.getParameter("rows"));
		} catch (Exception e) {
			pageNo = 1;
			pageSize = BaseConstants.MAX_PAGE_SIZE;
		}
		return xfRecordService.dataGridHistory(xfRecord, pageNo, pageSize);
	}
	/**
	 * 删除历史记录
	 */
	@RequestMapping(value = "/deleteHistory", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult deleteHistory(HttpServletRequest request,
			HttpServletResponse response, MmsXfRecord xfRecord)
			throws ServiceException {
		AjaxResult ajaxResult = new AjaxResult();
		try {
			xfRecordService.delete(xfRecord);
			ajaxResult.setData(xfRecord);
			ajaxResult.setSuccess(true);
			ajaxResult.setMessage("这下我真的走了，主人，我会想你的....");
		} catch (Exception e) {
			throw new ServiceException(e);
		}
		return ajaxResult;
	}
}
