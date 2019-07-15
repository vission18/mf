package com.vission.mf.base.mms;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.enums.db.SYS_OPERLOG_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.po.MmsXfType;
import com.vission.mf.base.model.po.ShowOprMessage;
import com.vission.mf.base.service.BaseService;

/**
 * 
 * 功能/模块 ：消费类别业务处理层 使用Spring的@Service/@Autowired 指定IOC设置.
 */
@Service("xfTypeService")
@Transactional
public class XfTypeService extends BaseService {

	@Autowired
	private XfTypeDao xfTypeDao;

	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid(MmsXfType xfType, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		Map<String, Object> filterMap = new HashMap<String, Object>();
		setupFilterMap(xfType, filterMap); // 将查询条件对象拆分成 将对象型查询条件拆分成集合型查询条件
		Map<String, String> orderMap = new HashMap<String, String>(1);
		orderMap.put("typeName", CriteriaSetup.ASC);
		return xfTypeDao.findByCriteria(dataGrid, pageSize, filterMap,
				orderMap);// 空对象 页尺寸 map类型的查询条件 查询条件
	}

/**
	 * 删除
	 */
	public void delete(MmsXfType xfType) throws ServiceException {
		xfTypeDao.delete(xfType);
		this.saveOperLogInfo(SYS_OPERLOG_INFO.DELETE_SYS_USER,
				xfType.getTypeName());
	}

	/**
	 * 保存
	 */
	public ShowOprMessage save(MmsXfType xfType) throws ServiceException {
		ShowOprMessage oprMsg=new ShowOprMessage();
		try {
			if (xfType.getTypeId() == null || "".equals(xfType.getTypeId())) {
				if (this.isReXfType(xfType)) {
					oprMsg.setOperFlag(false);
					oprMsg.setResMsg("主人，该类别已存在哦！");
					return oprMsg;
				}
				xfTypeDao.onlySave(xfType);
			}else{
				xfTypeDao.onlyUpdate(xfType);
			}
		} catch (Exception e) {
			oprMsg.setOperFlag(false);
			oprMsg.setResMsg("出错啦，主人，赶快去找李大叔吧^_^");
			return oprMsg;
		}
		oprMsg.setOperFlag(true);
		oprMsg.setResMsg("恭喜主人，类别保存成功！");
		return oprMsg;
	}

	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(MmsXfType xfType, Map<String, Object> filterMap) {
		if (xfType.getTypeName() != null
				&& !xfType.getTypeName().trim().equals("")
				&& !xfType.getTypeName().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "typeName",
					xfType.getTypeName());
		}
	}

	/**
	 * 根据id查找类别
	 */
	@Transactional(readOnly = true)
	public MmsXfType getXfTypeById(String xfTypeId) {
		return xfTypeDao.get(xfTypeId);
	}


	/**
	 * 批量导入类别
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public List<MmsXfType> saveMoreXfType(InputStream in)
			throws ServiceException {
		List<MmsXfType> list0 = new ArrayList<MmsXfType>();//存在重复类别
		List<MmsXfType> list1 = new ArrayList<MmsXfType>();//存储Excel中类别
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(in);
			in.close();
			HSSFSheet sheet = workbook.getSheetAt(0);
			int numberOfRow = sheet.getLastRowNum();
		//	HashSet<String> branchSet = getBranchSet();//获得所有机构编号
			for (int i = 1; i <= numberOfRow; i++) {
				HSSFRow row = sheet.getRow(i);
				MmsXfType xfType = new MmsXfType();
				if (row.getCell(0) == null
						|| "".equals(row.getCell(0).getStringCellValue().trim())) {
					throw new ServiceException("第" + row.getRowNum() + "行：类别名称不能为空，请检查！");
				}
				xfType.setTypeName(row.getCell(0).getStringCellValue());
				xfType.setRemark(row.getCell(1).getStringCellValue());
				xfType.setTypeStatus(true);
				list1.add(xfType);
			}
		}catch (Exception e) {
			if("Cannot get a text value from a numeric cell".equals(e.getMessage())){
				throw new ServiceException("[请将所有单元格设置成文本格式]");
			}else{
				throw new ServiceException(e.getMessage());
			}
		}
		list0 = uniqXfTypeList(list1)[0];
		if (list0.size() <= 0) {
			xfTypeDao.saveMoreXfType(uniqXfTypeList(list1)[1]);
		}
		
		return list0;
	}

	/**
	 * 对excel表中的用户数据进行排重 排重条件为LoginName,UserName
	 */
	@SuppressWarnings("rawtypes")
	public List[] uniqXfTypeList(List<MmsXfType> list) throws ServiceException {
		List[] lists = new List[2];
		Map<String, MmsXfType> map1 = new HashMap<String, MmsXfType>();
		Map<String, MmsXfType> map2 = new HashMap<String, MmsXfType>();
		List<MmsXfType> repeatList = new ArrayList<MmsXfType>();//重复用户的集合
		List<MmsXfType> result = new ArrayList<MmsXfType>();//排重后结果
		for (int i = 0, j = list.size(); i < j; i++) {
			if (map1.containsKey(list.get(i).getTypeName())) {
				repeatList.add(list.get(i));// 将重复的数据放入list中，备用！
			} else {
				map1.put(list.get(i).getTypeName(), list.get(i));
			}
		}
		for (String key : map1.keySet()) {
			if(!isReXfType(map1.get(key))){
				result.add(map1.get(key));
			}else{
				repeatList.add(map1.get(key));// 将重复的数据放入list中
			}
			
/*			if (map2.containsKey(map1.get(key).getTypeName())) {
				repeatList.add(map1.get(key));// 将重复的数据放入list中，备用！
			} else {
				if (!isReXfType(map1.get(key))) {
					map2.put(map1.get(key).getTypeName(), map1.get(key));
				} else {
					repeatList.add(map1.get(key));// 将重复的数据放入list中，备用
				}
			}*/
		}
/*		for (String key : map2.keySet()) {
			result.add(map2.get(key));
		}*/
		lists[0] = repeatList;
		lists[1] = result;
		return lists;
	}

	/**
	 * 辅助方法，判断要插入的user的LoginName，UserName是否与数据中的数据重复
	 */
	public boolean isReXfType(MmsXfType xfType) {
		boolean r = false;
		List<MmsXfType> list = xfTypeDao.findAll();
		for (int i = 0, j = list.size(); i < j; i++) {
			if (xfType.getTypeName().equals(list.get(i).getTypeName())) {
				r = true;
				break;
			}
		}
		return r;
	}
	
	/**
	 * 根据用户登录名查询用户模糊查询 add by syh
	 */
	public List<MmsXfType> getAll() {
		return xfTypeDao.findAll();
	}
}
