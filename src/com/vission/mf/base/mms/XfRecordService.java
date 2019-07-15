package com.vission.mf.base.mms;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.MmsXfRecord;
import com.vission.mf.base.model.po.MmsXfType;
import com.vission.mf.base.model.po.ShowOprMessage;
import com.vission.mf.base.service.BaseService;
import com.vission.mf.base.util.Util;

/**
 * 
 * 功能/模块 ：消费记录业务处理层 使用Spring的@Service/@Autowired 指定IOC设置.
 */
@Service("xfRecordService")
@Transactional
public class XfRecordService extends BaseService {

	@Autowired
	private XfRecordDao xfRecordDao;

	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(MmsXfRecord xfRecord, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		StringBuffer hql = new StringBuffer();
		hql.append(" from MmsXfRecord where delFlag='0' ");
		String sql=searchCondition(xfRecord,hql);
		dataGrid = xfRecordDao
				.findDataPage(dataGrid, pageSize, sql);
		@SuppressWarnings("unchecked")
		List<MmsXfRecord> xfRecords=dataGrid.getRows();
		//在此将类别id转换为类别名称
		if(xfRecords.size()>0 && xfRecords != null){
			List<MmsXfType> xfTypes=getAllType();
			for(int i=0;i<xfRecords.size();i++){
				for(int j=0;j<xfTypes.size();j++){
					if((xfRecords.get(i).getXfType()).equals((xfTypes.get(j).getTypeId()))){
						xfRecords.get(i).setXfType(xfTypes.get(j).getTypeName());
					}
				}
			}
		}
		return dataGrid;
	}

/**
	 * 做假删除，可在历史记录中查询
	 */
	public void delete(MmsXfRecord xfRecord) throws ServiceException {
		xfRecordDao.delete(xfRecord);
	}

	/**
	 * 保存
	 */
	public ShowOprMessage save(MmsXfRecord xfRecord) throws ServiceException {
		ShowOprMessage oprMsg=new ShowOprMessage();
		try {
			if (xfRecord.getXfId() == null || "".equals(xfRecord.getXfId())) {
				xfRecordDao.onlySave(xfRecord);
			}else{
				xfRecordDao.onlyUpdate(xfRecord);
			}
		} catch (Exception e) {
			oprMsg.setOperFlag(false);
			oprMsg.setResMsg("主人，操作数据出现异常！");
			return oprMsg;
		}
		oprMsg.setOperFlag(true);
		//删除标志
		if(xfRecord.getDelFlag().equals("1")){
			oprMsg.setResMsg("主人，我先走了，想我就去历史记录看我...");
		}else{
			oprMsg.setResMsg("主人，保存成功了！");
		}
		return oprMsg;
	}

	/**
	 * 新增查询条件
	 */
	private String searchCondition(MmsXfRecord xfRecord, StringBuffer hql) {
		String xfType=Util.nullToEmpty(xfRecord.getXfType());
		String xfDate=Util.nullToEmpty(xfRecord.getXfDate());
		String rDate=Util.nullToEmpty(xfRecord.getRecordDate());
		String rUser=Util.nullToEmpty(xfRecord.getRecordUser());
		if (!"".equals(xfType)) {
			hql.append(" and xfType like '%" + xfType + "%'");
		}
		if (!"".equals(xfDate)) {
			hql.append(" and xfDate like '%" + xfDate + "%'");
		}
		if (!"".equals(rDate)) {
			hql.append(" and recordDate like '%" + rDate + "%'");
		}
		if (!"".equals(rUser)) {
			hql.append(" and recordUser like '%" + rUser + "%'");
		}
		return hql.toString();
	}

	/**
	 * 根据id查找记录
	 */
	@Transactional(readOnly = true)
	public MmsXfRecord getXfRecordById(String xfId) {
		return xfRecordDao.get(xfId);
	}
	
	/**
	 * 根据用户登录名查询用户模糊查询 add by syh
	 */
	public List<MmsXfRecord> getAll() {
		return xfRecordDao.findAll();
	}
	/**
	 * 根据用户登录名查询用户模糊查询 add by syh
	 */
	/**
	 * 获取消费类别集合 
	 */
	@SuppressWarnings("unchecked")
	public List<MmsXfType> getAllType() {

		StringBuffer hql = new StringBuffer();
		hql.append("from MmsXfType where typeStatus='1'");
		List<MmsXfType> list = xfRecordDao.find(hql.toString());
		return list;
	}
	public ShowOprMessage calculateMoney(MmsXfRecord xfRecord) throws ServiceException {
		ShowOprMessage sm=new ShowOprMessage();
		if(xfRecord!=null){
			String queryString="select SUM(xfPrice) from MmsXfRecord ";
			if(xfRecord.getXfType()!=null && !"".equals(xfRecord.getXfType())){
				if(xfRecord.getXfDate()!=null && !"".equals(xfRecord.getXfDate())){
					if(xfRecord.getRecordDate()!=null && !"".equals(xfRecord.getRecordDate())){
						queryString=queryString+" where xfType='"+xfRecord.getXfType()+"' and xfDate >= '"+xfRecord.getXfDate()+"' and xfDate <= '"+xfRecord.getRecordDate()+"'";
					}else{
						queryString=queryString+" where xfType='"+xfRecord.getXfType()+"' and xfDate >= '"+xfRecord.getXfDate()+"'";	
					}
				}else{
					if(xfRecord.getRecordDate()!=null && !"".equals(xfRecord.getRecordDate())){
						queryString=queryString+" where xfType='"+xfRecord.getXfType()+"' and xfDate <= '"+xfRecord.getRecordDate()+"'";
					}else{
						queryString=queryString+" where xfType='"+xfRecord.getXfType()+"'";
					}
				}
			}else{
				if(xfRecord.getXfDate()!=null && !"".equals(xfRecord.getXfDate())){
					if(xfRecord.getRecordDate()!=null && !"".equals(xfRecord.getRecordDate())){
						queryString=queryString+" where xfDate >= '"+xfRecord.getXfDate()+"' and xfDate <= '"+xfRecord.getRecordDate()+"'";
					}else{
						queryString=queryString+" where xfDate >= '"+xfRecord.getXfDate()+"'";	
					}
				}else{
					if(xfRecord.getRecordDate()!=null && !"".equals(xfRecord.getRecordDate())){
						queryString=queryString+" where xfDate <= '"+xfRecord.getRecordDate()+"'";
					}
				}
			}
			Query q=xfRecordDao.createQuery(queryString);
			if(q.list().get(0)!=null){
				sm.setOperFlag(true);
				sm.setResMsg((q.list().get(0)).toString());
			}else{
				sm.setOperFlag(false);
				sm.setResMsg("主人真是个持家的好总管，在此日期内无消费记录！鼓掌......");
			}
		}else{
			sm.setOperFlag(false);
			sm.setResMsg("系统异常,请联系管理员！");
		}
		return sm;
	}
	/**
	 * 分页数据列表(历史数据查询)
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGridHistory(MmsXfRecord xfRecord, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		StringBuffer hql = new StringBuffer();
		hql.append(" from MmsXfRecord where delFlag='1' ");
		String sql=searchCondition(xfRecord,hql);
		dataGrid = xfRecordDao
				.findDataPage(dataGrid, pageSize, sql);
		@SuppressWarnings("unchecked")
		List<MmsXfRecord> xfRecords=dataGrid.getRows();
		//在此将类别id转换为类别名称
		if(xfRecords.size()>0 && xfRecords != null){
			List<MmsXfType> xfTypes=getAllType();
			for(int i=0;i<xfRecords.size();i++){
				for(int j=0;j<xfTypes.size();j++){
					if((xfRecords.get(i).getXfType()).equals((xfTypes.get(j).getTypeId()))){
						xfRecords.get(i).setXfType(xfTypes.get(j).getTypeName());
					}
				}
			}
		}
		return dataGrid;
	}
}
