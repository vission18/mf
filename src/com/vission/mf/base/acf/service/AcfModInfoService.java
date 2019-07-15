package com.vission.mf.base.acf.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.chart.BeginRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.vission.mf.base.acf.bo.AcfColInfoBo;
import com.vission.mf.base.acf.dao.AcfColInfoDao;
import com.vission.mf.base.acf.dao.AcfModInfoDao;
import com.vission.mf.base.acf.po.AcfColInfo;
import com.vission.mf.base.acf.po.AcfModInfo;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.service.BaseService;
import com.vission.mf.base.util.ClassUtil;

/**
 * 功能模块：模块信息service类
 *
 */
@Service("acfModInfoService")
@Transactional
public class AcfModInfoService extends BaseService {

	@Autowired
	private AcfModInfoDao acfModInfoDao;

	@Autowired
	private AcfColInfoDao acfColInfoDao;
	
	@Autowired
	private AcfService acfService;
	
	
	
	/**
	 * 新增
	 * @param modInfo
	 * @throws ServiceException
	 */
	public void save(AcfModInfo modInfo) throws ServiceException{
		this.acfModInfoDao.onlySave(modInfo);
	}
	
	public DataGrid getColDataGridByModId(String modId, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		return acfColInfoDao.findDataGrid(dataGrid, pageSize, modId);
	}
	
	public AcfColInfo getColInfoByColName(String colName,String modId) throws ServiceException{
		return this.acfColInfoDao.getColInfoByColName(colName, modId);
	}
	
	public void saveColInfo(AcfColInfo colInfo) throws ServiceException{
		this.acfColInfoDao.save(colInfo);
	}
	/**
	 * 自动生成代码
	 * @param modInfo
	 * @throws ServiceException
	 */
	public void autoCreateCode(AcfModInfo modInfo) throws ServiceException{
		if(modInfo == null){
			return;
		}
		if(modInfo.getColList() ==null || "".equals(modInfo.getColList())){
			logger.info("模块"+modInfo.getModChaName()+"没有列！");
			return;
		}
		List<AcfColInfo> colInfoList=(List)JSONArray.parseArray(modInfo.getColList() ,
				AcfColInfo.class);
		List<AcfColInfoBo> colBoList = new ArrayList<AcfColInfoBo>();
		if(colInfoList != null && colInfoList.size()>0){
			for(AcfColInfo colInfo : colInfoList){
				AcfColInfoBo colBo = new AcfColInfoBo();
				//允许为空
				if("1".equals(colInfo.getIsNull())){
					colBo.setCheckIsNull(1);
				}else{
					colBo.setCheckIsNull(0);
				}
				//是否主键
				if("1".equals(colInfo.getIsPk())){
					colBo.setCheckIsPk(1);
				}else{
					colBo.setCheckIsPk(0);
				}
				if("CHAR".equals(colInfo.getColType())||"CLOB".equals(colInfo.getColType())||
						"DATE".equals(colInfo.getColType())||"VARCHAR".equals(colInfo.getColType())){
					colBo.setCloType("String");
				}else if("INTEGER".equals(colInfo.getColType())){
					colBo.setCloType("Int");
				}else{
					colBo.setCloType("String");
				}
				colBo.setColDesc(colInfo.getColDesc());
				colBo.setColChaName(colInfo.getColChaName());
				colBo.setColName(colInfo.getColEngName());
				colBo.setColNameToUpcase(colInfo.getColEngName().toUpperCase());
				//驼峰命名
				String tfcolName = ClassUtil.tfNames(colInfo.getColEngName());
				colBo.setP_colTfNames(tfcolName.substring(0, 1).toLowerCase()
						+ tfcolName.substring(1, tfcolName.length()));
				colBo.setColTfNames(tfcolName);
				colBoList.add(colBo);
			}
			this.acfService.createFileMain(colBoList, modInfo);
		}
	}
}