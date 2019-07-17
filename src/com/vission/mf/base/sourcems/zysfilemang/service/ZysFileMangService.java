package com.vission.mf.base.sourcems.zysfilemang.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.enums.BaseConstants;
import com.vission.mf.base.enums.db.SYS_USER_INFO;
import com.vission.mf.base.exception.ServiceException;
import com.vission.mf.base.hibernate.CriteriaSetup;
import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.model.bo.SessionInfo;
import com.vission.mf.base.model.bo.Tree;
import com.vission.mf.base.model.po.SysUserInfo;
import com.vission.mf.base.service.BaseService;
import com.vission.mf.base.sourcems.zysfilemang.dao.ZysFileMangDao;
import com.vission.mf.base.sourcems.zysfilemang.po.ZysFileMang;
import com.vission.mf.base.util.ClassUtil;
import com.vission.mf.base.util.DateUtil;

/**
 * 作者：acf 描述：ZysFileMangService 业务逻辑处理 日期：2019-7-4 15:07:15 类型：SERVICE文件
 */
@Service("zysFileManService")
@Transactional
public class ZysFileMangService extends BaseService {

	@Autowired
	private ZysFileMangDao zysFileMangDao;

	/**
	 * 分页数据列表
	 */
	@Transactional(readOnly = true)
	public DataGrid dataGrid( HttpSession session,ZysFileMang po, int pageNo, int pageSize) {
		DataGrid dataGrid = new DataGrid();
		dataGrid.setStartRow((pageNo - 1) * pageSize);
		
		//文件创建者权限过滤，admin和超级访问者不需要过滤
		SessionInfo sessionInfo = (SessionInfo) session
				.getAttribute(BaseConstants.USER_SESSION_KEY);
		SysUserInfo userInfo = sessionInfo.getUser();
		//访问者用户取父级ID
		String userId = SYS_USER_INFO.USER_TYPE_VIEW.equals(userInfo.getUserType())?userInfo.getParentUserId():userInfo.getUserId();
		po.setCreateUser(userId);
		
		//读取配置文件
		ResourceBundle rb = null;
		// 读取acf_config.properties配置文件
		rb = ResourceBundle.getBundle("acf_config");
		
		if (rb != null) {
			String userIds = ClassUtil.chcString(rb.getString("FILE_USER_ID_NOCHECK"));
			if(!"".equals(userIds) && userIds.contains(po.getCreateUser())){
				po.setCreateUser(null);
			}
		}
		return zysFileMangDao.findDataGrid(dataGrid, pageSize, po);
	}

	/**
	 * 新增查询条件
	 */
	private void setupFilterMap(ZysFileMang po, Map<String, Object> filterMap) {

		if (po.getPkId() != null && !po.getPkId().trim().equals("")
				&& !po.getPkId().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "pkId", po.getPkId());
		}

		if (po.getFileType() != null && !po.getFileType().trim().equals("")
				&& !po.getFileType().trim().equals("null")) {
			filterMap
					.put(CriteriaSetup.LIKE_ALL + "fileType", po.getFileType());
		}

		if (po.getFileNm() != null && !po.getFileNm().trim().equals("")
				&& !po.getFileNm().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "fileNm", po.getFileNm());
		}

		if (po.getFilePath() != null && !po.getFilePath().trim().equals("")
				&& !po.getFilePath().trim().equals("null")) {
			filterMap
					.put(CriteriaSetup.LIKE_ALL + "filePath", po.getFilePath());
		}

		if (po.getFileRemark() != null && !po.getFileRemark().trim().equals("")
				&& !po.getFileRemark().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "fileRemark",
					po.getFileRemark());
		}

		if (po.getCompPwd() != null && !po.getCompPwd().trim().equals("")
				&& !po.getCompPwd().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "compPwd", po.getCompPwd());
		}

		if (po.getCostPrice() != null && !po.getCostPrice().trim().equals("")
				&& !po.getCostPrice().trim().equals("null")) {
			filterMap.put(CriteriaSetup.LIKE_ALL + "costPrice",
					po.getCostPrice());
		}

		if (po.getNowPrice() != null && !po.getNowPrice().trim().equals("")
				&& !po.getNowPrice().trim().equals("null")) {
			filterMap
					.put(CriteriaSetup.LIKE_ALL + "nowPrice", po.getNowPrice());
		}
		
		if (po.getCreateUser() != null && !po.getCreateUser().trim().equals("")
				&& !po.getCreateUser().trim().equals("null")) {
			filterMap
					.put(CriteriaSetup.EQ + "createUser", po.getCreateUser());
		}
	}

	@Transactional(readOnly = true)
	public ZysFileMang getZysFileMangById(String pkId) {
		return zysFileMangDao.getFileInfoByPkId(pkId);
	}

	/**
	 * 删除
	 */
	public void deleteZysFileMangById(String pkId) throws ServiceException {
		this.zysFileMangDao.delete(this.getZysFileMangById(pkId));
	}

	/**
	 * 保存
	 */
	public void saveZysFileMang(ZysFileMang po) throws ServiceException {
		this.zysFileMangDao.merge(po);
	}

	/**
	 * 查询参数
	 * 
	 * @param codeType
	 * @param isQueryParent
	 * @return
	 * @throws ServiceException
	 */
	public List<Tree> queryCodeTableByCodeType(String codeType,
			boolean isQueryParent) throws ServiceException {
		List<Tree> codelist = new ArrayList<Tree>();
		if (codeType == null || "".equals(codeType)) {
			logger.info("查询编码类型为空！");
			return codelist;
		}
		codelist = this.zysFileMangDao.queryCodeTableByCodeType(codeType,
				isQueryParent);
		return codelist;
	}

	/**
	 * 新增记录初始化
	 * 
	 * @param filePo
	 * @throws ServiceException
	 */
	public void initFilePara(ZysFileMang filePo) throws ServiceException {
		//File file = new File(filePo.getFilePath());
		filePo.setCreateTime(DateUtil.getCurrentSystemTime());
		//filePo.setDownloadCount((int) (195+(Math.random())*126));
		filePo.setDownloadCount(0);
	/*	filePo.setFileNm(file.getName());
		filePo.setFileSize(file.length());*/
		//初始年月
		filePo.setCountOndate(DateUtil.getYearMonthStr());
	}
	
	/**
	 * 保存日志
	 * @param OperType
	 * @param operContent
	 * @throws ServiceException
	 */
	public void saveOperLog(String OperType,String operContent)throws ServiceException{
		this.saveOperLogInfo(OperType,operContent);
	}
}
