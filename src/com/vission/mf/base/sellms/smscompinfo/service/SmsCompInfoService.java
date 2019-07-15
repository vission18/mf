package com.vission.mf.base.sellms.smscompinfo.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.sellms.smscompinfo.dao.SmsCompInfoDao;
import com.vission.mf.base.sellms.smscompinfo.db.SMS_COMP_INFO;
import com.vission.mf.base.sellms.smscompinfo.po.SmsCompInfo;
import com.vission.mf.base.service.BaseService;
import com.vission.mf.base.util.POIUtil;

/**
 * 作者：acf
 * 描述：SmsCompInfoService 业务逻辑处理
 * 日期：2019-6-21 9:29:04
 * 类型：SERVICE文件
 */
@Service("smscompinfoService")
@Transactional
public class SmsCompInfoService extends BaseService {

	@Autowired
	private SmsCompInfoDao smscompinfoDao;


	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(SmsCompInfo po, int pageNo, int pageSize,HttpServletRequest request) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		 // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件
		if(!setupFilterMap(request, filterMap)){
			return dataGrid;
		}
		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("busiType", CriteriaSetup.ASC);
		dataGrid = smscompinfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);
		return dataGrid;// 空对象 页尺寸 map类型的查询条件 查询条件
	}
	
	@Transactional(readOnly = true)
	public DataGrid indTypeComDataGrid(SmsCompInfo po, int pageNo, int pageSize,HttpServletRequest request) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		String industryType = request.getParameter("industryType");
		if( SMS_COMP_INFO.INDUSTRY_TYPE_FC.equals(industryType)){
			filterMap.put(CriteriaSetup.LIKE_ALL + "compName",
					"房产");
		}else if( SMS_COMP_INFO.INDUSTRY_TYPE_BX.equals(industryType)){
			filterMap.put(CriteriaSetup.LIKE_ALL + "compName",
					"保险");
		}else if( SMS_COMP_INFO.INDUSTRY_TYPE_QC.equals(industryType)){
			filterMap.put(CriteriaSetup.LIKE_ALL + "compName",
					"汽车");
		}else if( SMS_COMP_INFO.INDUSTRY_TYPE_JR.equals(industryType)){
			filterMap.put(CriteriaSetup.LIKE_ALL + "compName",
					"金融");
		}else if( SMS_COMP_INFO.INDUSTRY_TYPE_ZX.equals(industryType)){
			filterMap.put(CriteriaSetup.LIKE_ALL + "compName",
					"装修");
		}
		 // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件
		setupFilterMap(request, filterMap);
		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("busiType", CriteriaSetup.ASC);
		dataGrid = smscompinfoDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);
		return dataGrid;// 空对象 页尺寸 map类型的查询条件 查询条件
	}


	/**
	 * 新增查询条件
	 */
	private boolean setupFilterMap(HttpServletRequest request, Map<String, Object> filterMap) {
		boolean isSearch = false;
		String queryProvice = request.getParameter("queryProvice");
		String queryCity = request.getParameter("queryCity");
		String queryBuss = request.getParameter("queryBuss");
		String queryBussScope = request.getParameter("queryBussScope");
		String queryCompName = request.getParameter("queryCompName");
		
		if (queryProvice != null && !"".equals(queryProvice.trim()) && !"null".equals(queryProvice.trim())) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "compAdd",
					queryProvice);
			isSearch = true;
		}
		
		if (queryCity != null && !"".equals(queryCity.trim()) && !"null".equals(queryCity.trim())) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "compAdd",
					queryCity);
			isSearch = true;
		}
		
		if (queryBuss != null && !"".equals(queryBuss.trim()) && !"null".equals(queryBuss.trim())) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "compName",
					queryBuss);
			isSearch = true;
		}
		
		if (queryBussScope != null && !"".equals(queryBussScope.trim()) && !"null".equals(queryBussScope.trim())) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "busiScope",
					queryBussScope);
			isSearch = true;
		}
		
		if (queryCompName != null && !"".equals(queryCompName.trim()) && !"null".equals(queryCompName.trim())) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "compName",
					queryCompName);
			isSearch = true;
		}
		
		
		return isSearch;
	}

	@Transactional(readOnly = true)
	public SmsCompInfo getSmsCompInfoById(String pkId) {
		return smscompinfoDao.get(pkId);
	}
	
	/**
	 * 删除
	 */
	public void deleteSmsCompInfoById(String pkId) throws ServiceException {
		this.smscompinfoDao.delete(this.getSmsCompInfoById(pkId));
	}
	
	/**
	 * 保存
	 */
	public void saveSmsCompInfo(SmsCompInfo po,String operType) throws ServiceException {
		po.setBusiType("xt");
		if("update".equals(operType)){
			this.smscompinfoDao.onlyUpdate(po);
		}else{
			this.smscompinfoDao.onlySave(po);
		}
	}
	
	/**
	 * 批量上传
	 * @param in
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public List<SmsCompInfo> saveBacthCompInfo(InputStream in,HttpServletRequest request)
			throws ServiceException {
		List<SmsCompInfo> list0 = new ArrayList<SmsCompInfo>();//存在重复的公司名称
		List<SmsCompInfo> list1 = new ArrayList<SmsCompInfo>();//存储Excel的公司名称
		try {
			List<List<String>> result = new ArrayList<List<String>>();
			result = POIUtil.readExcel07(in, 0, 1, 0, 10);
			in.close();
			for (int i = 0; i < result.size(); i++) {
				List<String> row = result.get(i);
				
				SmsCompInfo compInfo = new SmsCompInfo();
				compInfo.setBusiType("99");
			/*	if (row.get(0) != null) {
					compInfo.setBusiType(row.get(0));
				}*/
				
				if (row.get(0) != null) {
					compInfo.setCompName(row.get(0));
				}
				
				if (row.get(1) != null) {
					compInfo.setCompLegal(row.get(1));
				}
				if (row.get(2) != null) {
					compInfo.setCompMoney(row.get(2));
				}
				if (row.get(3) != null) {
					compInfo.setCompDate(row.get(3));
				}
				if (row.get(4) != null) {
					String[] phoneArr = row.get(4).split(";");
					if(phoneArr.length>1){
						for(int p=0;p<phoneArr.length;p++){
							String phoneStr = phoneArr[p].trim();
							phoneStr = phoneStr.replaceAll(" ", "");
							phoneStr = phoneStr.replaceAll(";", "");
							if(phoneArr[p].startsWith("0")){
								compInfo.setTelNumber(phoneArr[p].trim());
								break;
							}
						}
					}else{
						compInfo.setTelNumber(row.get(4));
					}
				}
				if (row.get(5) != null) {
					String[] phoneArr = row.get(5).split(";");
					if(phoneArr.length>1){
						for(int p=0;p<phoneArr.length;p++){
							String phoneStr = phoneArr[p].trim();
							phoneStr = phoneStr.replaceAll(" ", "");
							phoneStr = phoneStr.replaceAll(";", "");
							if(phoneArr[p].startsWith("1")){
								compInfo.setMobile(phoneStr);
								break;
							}
						}
					}else{
						compInfo.setMobile(row.get(5));
					}
				}
				if (row.get(6) != null) {
					compInfo.setCompAdd(row.get(6).trim());
				}
				if (row.get(7) != null) {
					String[] webArr = row.get(7).split(";");
					if(webArr.length>1){
						compInfo.setCompWeb(webArr[0].trim());
					}else{
						compInfo.setCompWeb(row.get(7));
					}
				}
				if (row.get(8) != null) {
					String[] emailArr = row.get(8).trim().split(";");
					if(emailArr.length>1){
						compInfo.setCompEmail(emailArr[0].trim());
					}else{
						compInfo.setCompEmail(row.get(8));
					}
				}
				if (row.get(9) != null) {
					compInfo.setBusiScope(row.get(9).trim());
				}
				if(compInfo.getMobile() == null || !compInfo.getMobile().startsWith("1")){
					continue;
				}
				list1.add(compInfo);
			}
		}catch (Exception e) {
			if("Cannot get a text value from a numeric cell".equals(e.getMessage())){
				throw new ServiceException("[请将所有单元格设置成文本格式]");
			}else{
				throw new ServiceException(e.getMessage());
			}
		}
		
		list0 = uniqCompInfoList(list1)[0];
		if (list0.size() <= 0) {
			//批量保存
			List<SmsCompInfo> list = uniqCompInfoList(list1)[1];
			for (int j = 0; j  < list.size(); j++) {
				System.out.println("-------------------------"+list.get(j).getCompName());
				this.smscompinfoDao.onlySave(list.get(j));
				if (j % 50 == 0) {
					this.smscompinfoDao.getSession().flush();
					this.smscompinfoDao.getSession().clear();
				}
			}
		}
		return list0;
	}
	/**
	 * 批量保存辅助
	 * @param list
	 * @return
	 * @throws ServiceException
	 */
	public List[] uniqCompInfoList(List<SmsCompInfo> list) throws ServiceException {
		List[] lists = new List[2];
		Map<String, SmsCompInfo> map1 = new HashMap<String, SmsCompInfo>();
		List<SmsCompInfo> repeatList = new ArrayList<SmsCompInfo>();//重复用户的集合
		List<SmsCompInfo> result = new ArrayList<SmsCompInfo>();//排重后结果
		
		for (int i = 0, j = list.size(); i < j; i++) {
			SmsCompInfo compInfo = list.get(i);
			//先判断是该指标是否已存在
			if(map1.containsKey(compInfo.getCompName())){
				repeatList.add(compInfo);
			}else{
				//自动生成ID
				map1.put(compInfo.getCompName(), compInfo);
				result.add(compInfo);
			}
		}
		lists[0] = repeatList;
		lists[1] = result;
		return lists;
	}
}
