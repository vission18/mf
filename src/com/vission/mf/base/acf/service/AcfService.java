package com.vission.mf.base.acf.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vission.mf.base.acf.bo.AcfColInfoBo;
import com.vission.mf.base.acf.bo.AcfTempFileNameBo;
import com.vission.mf.base.acf.po.AcfModInfo;
import com.vission.mf.base.service.BaseService;
import com.vission.mf.base.util.ClassUtil;
import com.vission.mf.base.util.DateUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * 功能/模块 ：自动生成框架业务处理层
 */
@Service("acfService")
@Transactional
public class AcfService extends BaseService {

	/**
	 * 生成文件入口
	 * @param colsList
	 * @param modInfo
	 */
	public void createFileMain(List<AcfColInfoBo> colsList,AcfModInfo modInfo) {
		if(modInfo == null || colsList == null){
			return;
		}
		Map paraMap = new HashMap();

		ResourceBundle rb = null;
		// 读取acf_config.properties配置文件
		rb = ResourceBundle.getBundle("acf_config");
		if (null == rb) {
			logger.info("acf_config.properties文件不存在，请检查文件路径！");
			return;
		}
		String temp_path = ClassUtil.chcString(rb.getString("TEMPFILE_PATH"));
		File tempPath = new File(temp_path);
		if (!tempPath.exists()) {
			logger.info("模板文件目录不存在：" + temp_path);
			return;
		}
		//获取包路径前缀
		String packageUrlIns = modInfo.getPackageUrlIns();
		if("".equals(packageUrlIns)){
			packageUrlIns = ClassUtil.chcString(rb.getString("DEFAULT_PACKAGE_URL"));
		}
		if(!packageUrlIns.trim().endsWith(".")){
			packageUrlIns = packageUrlIns+".";
		}
		paraMap.put("TEMP_PATH", tempPath);
		paraMap.put("BASE_PATH", ClassUtil.chcString(rb.getString("BASE_PATH")));
		paraMap.put("EXPLAIN", modInfo.getModChaName());
		paraMap.put("PACKAGE_URL_INS", packageUrlIns);
		paraMap.put("PACKAGE_BASE_URL", ClassUtil.chcString(rb.getString("PACKAGE_BASE_URL")));
		paraMap.put("MOD_NAME", ClassUtil.chcString(modInfo.getModChaName()));
		paraMap.put("JSP_BASE_PATH", ClassUtil.chcString(rb.getString("JSP_BASE_PATH")));
		paraMap.put("JS_BASE_PATH", ClassUtil.chcString(rb.getString("JS_BASE_PATH")));
		paraMap.put("IN_JS_PATH", ClassUtil.chcString(rb.getString("IN_JS_PATH")));
		paraMap.put("SQL_PATH", ClassUtil.chcString(rb.getString("SQL_PATH")));

		AcfTempFileNameBo atfnBo = new AcfTempFileNameBo();
		atfnBo.init(modInfo.getModEngName());
		// atfnBo.setMoudExplainStr("test模板");
		atfnBo.setAcnList(colsList);
		// 调用自动创建dbJava文件入口
		 this.autoCreateDbFile(paraMap,atfnBo);

		// 调用自动创建poJava文件入口
		 this.autoCreatePoFile(paraMap,atfnBo);

		// 调用自动创建DaoJava文件入口
		 this.autoCreateDaoFile(paraMap,atfnBo);

		// 调用自动创建ServiceJava文件入口
		 this.autoCreateServiceFile(paraMap,atfnBo);

		// 调用自动创建ControllerJava文件入口
		this.autoCreateControllerFile(paraMap, atfnBo);
		
		// 调用自动创建JSP文件入口
		this.autoCreateJspFile(paraMap, atfnBo);
		
		// 调用自动创建JS文件入口
		this.autoCreateJsFile(paraMap, atfnBo);
		

		// 调用自动创建SQL文件入口
		this.autoCreateSqlFile(paraMap, atfnBo);
	}

	/**
	 * 自动生成db文件
	 * 
	 * @param paraMap
	 */
	public void autoCreateDbFile(Map paraMap, AcfTempFileNameBo atfnBo) {
		String create_path = ClassUtil.chcString(paraMap.get("BASE_PATH"));

		// 生成文件名
		String createFilePath = create_path + "\\"
				+ ClassUtil.chcString(atfnBo.getMoudLowerCaseName()) + "\\db";
		// 检查生成文件目录是否存在，不存在则创建
		File checkDirFile = new File(createFilePath);
		if (!checkDirFile.exists()) {
			checkDirFile.mkdirs();
		}
		createFilePath = createFilePath + "\\"
				+ ClassUtil.chcString(atfnBo.getMoudDbName()) + ".java";
		logger.info("准备生成db文件：" + createFilePath);
		try {
			// step3 创建数据模型
			Map<String, Object> dataMap = new HashMap<String, Object>();

			// 包路径(小写)
			dataMap.put("packgeUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS"))+atfnBo.getMoudLowerCaseName() + ".db");
			// 生成日期
			dataMap.put("cdate", DateUtil.getSystemTime());
			// 包名基础路径
			dataMap.put("packgeBaseUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_BASE_URL")));

			dataMap.put("atfnBo", atfnBo);
			this.createFile("dbTemplate.ftl", createFilePath, dataMap, paraMap);
		} catch (Exception e) {
			logger.error("db文件创建失败：" + e.getMessage());
		}
	}

	/**
	 * 自动生成po文件
	 * 
	 * @param paraMap
	 */
	public void autoCreatePoFile(Map paraMap, AcfTempFileNameBo atfnBo) {
		String create_path = ClassUtil.chcString(paraMap.get("BASE_PATH"));

		// 生成文件名
		String createFilePath = create_path + "\\"
				+ atfnBo.getMoudLowerCaseName() + "\\po";
		// 检查生成文件目录是否存在，不存在则创建
		File checkDirFile = new File(createFilePath);
		if (!checkDirFile.exists()) {
			checkDirFile.mkdirs();
		}
		createFilePath = createFilePath + "\\" + atfnBo.getMoudPoName()
				+ ".java";
		logger.info("准备生成po文件：" + createFilePath);
		try {
			// step3 创建数据模型
			Map<String, Object> dataMap = new HashMap<String, Object>();

			// 包路径(小写)
			dataMap.put("packgeUrl", ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS"))+atfnBo.getMoudLowerCaseName() + ".po");
			// 生成日期
			dataMap.put("cdate", DateUtil.getSystemTime());
			// 包名基础路径
			dataMap.put("packgeBaseUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_BASE_URL")));
			//包名前缀路径
			dataMap.put("packgeInsUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS")));
			dataMap.put("atfnBo", atfnBo);
			this.createFile("poTemplate.ftl", createFilePath, dataMap, paraMap);
		} catch (Exception e) {
			logger.error("po文件创建失败：" + e.getMessage());
		}
	}

	/**
	 * 自动生成dao文件
	 * 
	 * @param paraMap
	 */
	public void autoCreateDaoFile(Map paraMap, AcfTempFileNameBo atfnBo) {
		String create_path = ClassUtil.chcString(paraMap.get("BASE_PATH"));

		// 生成文件名
		String createFilePath = create_path + "\\"
				+ atfnBo.getMoudLowerCaseName() + "\\dao";
		// 检查生成文件目录是否存在，不存在则创建
		File checkDirFile = new File(createFilePath);
		if (!checkDirFile.exists()) {
			checkDirFile.mkdirs();
		}
		createFilePath = createFilePath + "\\" + atfnBo.getMoudDaoName()
				+ ".java";
		logger.info("准备生成dao文件：" + createFilePath);
		try {
			// step3 创建数据模型
			Map<String, Object> dataMap = new HashMap<String, Object>();

			// 包路径(小写)
			dataMap.put("packgeUrl", ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS"))+atfnBo.getMoudLowerCaseName() + ".dao");
			// 生成日期
			dataMap.put("cdate", DateUtil.getSystemTime());
			dataMap.put("atfnBo", atfnBo);
			// 包名基础路径
			dataMap.put("packgeBaseUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_BASE_URL")));
			//包名前缀路径
			dataMap.put("packgeInsUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS")));
			this.createFile("daoTemplate.ftl", createFilePath, dataMap, paraMap);
		} catch (Exception e) {
			logger.error("dao文件创建失败：" + e.getMessage());
		}
	}

	/**
	 * 自动生成service文件
	 * 
	 * @param paraMap
	 */
	public void autoCreateServiceFile(Map paraMap, AcfTempFileNameBo atfnBo) {
		String create_path = ClassUtil.chcString(paraMap.get("BASE_PATH"));

		// 生成文件名
		String createFilePath = create_path + "\\"
				+ atfnBo.getMoudLowerCaseName() + "\\service";
		// 检查生成文件目录是否存在，不存在则创建
		File checkDirFile = new File(createFilePath);
		if (!checkDirFile.exists()) {
			checkDirFile.mkdirs();
		}
		createFilePath = createFilePath + "\\" + atfnBo.getMoudServiceName()
				+ ".java";
		logger.info("准备生成service文件：" + createFilePath);
		try {
			// step3 创建数据模型
			Map<String, Object> dataMap = new HashMap<String, Object>();

			// 包路径(小写)
			dataMap.put("packgeUrl", ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS"))+atfnBo.getMoudLowerCaseName() + ".service");
			// 生成日期
			dataMap.put("cdate", DateUtil.getSystemTime());
			dataMap.put("atfnBo", atfnBo);
			// 包名基础路径
			dataMap.put("packgeBaseUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_BASE_URL")));
			//包名前缀路径
			dataMap.put("packgeInsUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS")));
			this.createFile("serviceTemplate.ftl", createFilePath, dataMap,
					paraMap);
		} catch (Exception e) {
			logger.error("service文件创建失败：" + e.getMessage());
		}
	}

	/**
	 * 自动生成controller文件
	 * 
	 * @param paraMap
	 */
	public void autoCreateControllerFile(Map paraMap, AcfTempFileNameBo atfnBo) {
		String create_path = ClassUtil.chcString(paraMap.get("BASE_PATH"));

		// 生成文件名
		String createFilePath = create_path + "\\"
				+ atfnBo.getMoudLowerCaseName() + "\\controller";
		// 检查生成文件目录是否存在，不存在则创建
		File checkDirFile = new File(createFilePath);
		if (!checkDirFile.exists()) {
			checkDirFile.mkdirs();
		}
		createFilePath = createFilePath + "\\" + atfnBo.getMoudControllerName()
				+ ".java";
		logger.info("准备生成controller文件：" + createFilePath);
		try {
			// step3 创建数据模型
			Map<String, Object> dataMap = new HashMap<String, Object>();

			// 包路径(小写)
			dataMap.put("packgeUrl", ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS"))+atfnBo.getMoudLowerCaseName()
					+ ".controller");
			// 生成日期
			dataMap.put("cdate", DateUtil.getSystemTime());
			dataMap.put("atfnBo", atfnBo);
			// 包名基础路径
			dataMap.put("packgeBaseUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_BASE_URL")));
			//包名前缀路径
			dataMap.put("packgeInsUrl",
					ClassUtil.chcString(paraMap.get("PACKAGE_URL_INS")));
			dataMap.put("inJsPath",
					ClassUtil.chcString(paraMap.get("IN_JS_PATH")));
			this.createFile("controllerTemplate.ftl", createFilePath, dataMap,
					paraMap);
		} catch (Exception e) {
			logger.error("controller文件创建失败：" + e.getMessage());
		}
	}
	
	/**
	 * 自动生成JSP文件
	 * 
	 * @param paraMap
	 */
	public void autoCreateJspFile(Map paraMap, AcfTempFileNameBo atfnBo) {
		String create_path = ClassUtil.chcString(paraMap.get("JSP_BASE_PATH"));

		// 生成文件名
		String createFilePath = create_path + "\\"
				+ atfnBo.getMoudLowerCaseName();
		// 检查生成文件目录是否存在，不存在则创建
		File checkDirFile = new File(createFilePath);
		if (!checkDirFile.exists()) {
			checkDirFile.mkdirs();
		}
		createFilePath = createFilePath + "\\" + atfnBo.getMoudJspName();
		logger.info("准备生成JSP文件：" + createFilePath);
		try {
			// step3 创建数据模型
			Map<String, Object> dataMap = new HashMap<String, Object>();

			dataMap.put("atfnBo", atfnBo);
			// 包名基础路径
			dataMap.put("modName",
					ClassUtil.chcString(paraMap.get("MOD_NAME")));
			//包名前缀路径
			dataMap.put("baseUrl","${pageContext.request.contextPath}");
			dataMap.put("buttonsData","${accessButtons.data}");
			dataMap.put("accessButtons","${accessButtons.type}");
			dataMap.put("inJsPath",
					ClassUtil.chcString(paraMap.get("IN_JS_PATH")));
			
			this.createFile("jspTemplate.ftl", createFilePath, dataMap,
					paraMap);
		} catch (Exception e) {
			logger.error("jsp文件创建失败：" + e.getMessage());
		}
	}
	
	
	/**
	 * 自动生成JS文件
	 * 
	 * @param paraMap
	 */
	public void autoCreateJsFile(Map paraMap, AcfTempFileNameBo atfnBo) {
		String create_path = ClassUtil.chcString(paraMap.get("JS_BASE_PATH"));

		// 生成文件名
		String createFilePath = create_path + "\\"
				+ atfnBo.getMoudLowerCaseName();
		// 检查生成文件目录是否存在，不存在则创建
		File checkDirFile = new File(createFilePath);
		if (!checkDirFile.exists()) {
			checkDirFile.mkdirs();
		}
		createFilePath = createFilePath + "\\" + atfnBo.getMoudJsName();
		logger.info("准备生成JS文件：" + createFilePath);
		try {
			// step3 创建数据模型
			Map<String, Object> dataMap = new HashMap<String, Object>();

			dataMap.put("atfnBo", atfnBo);
			// 包名基础路径
			dataMap.put("modName",
					ClassUtil.chcString(paraMap.get("MOD_NAME")));
			//包名前缀路径
			dataMap.put("baseUrl","${pageContext.request.contextPath}");
			dataMap.put("buttonsData","${accessButtons.data}");
			dataMap.put("inJsPath",
					ClassUtil.chcString(paraMap.get("IN_JS_PATH")));
			
			String width="100";
			List<AcfColInfoBo> acnList = atfnBo.getAcnList();
			if(acnList != null && acnList.size()>0){
				width = (1024/(acnList.size()-1))+"";
			}
			dataMap.put("colWidth",width);
			
			
			this.createFile("jsTemplate.ftl", createFilePath, dataMap,
					paraMap);
		} catch (Exception e) {
			logger.error("js文件创建失败：" + e.getMessage());
		}
	}
	
	/**
	 * 自动生成JS文件
	 * 
	 * @param paraMap
	 */
	public void autoCreateSqlFile(Map paraMap, AcfTempFileNameBo atfnBo) {
		String create_path = ClassUtil.chcString(paraMap.get("SQL_PATH"));

		// 生成文件名
		String createFilePath = create_path + "\\"
				+ atfnBo.getMoudLowerCaseName();
		// 检查生成文件目录是否存在，不存在则创建
		File checkDirFile = new File(createFilePath);
		if (!checkDirFile.exists()) {
			checkDirFile.mkdirs();
		}
		createFilePath = createFilePath + "\\" + atfnBo.getMoudPoName()+".sql";
		logger.info("准备生成SQL文件：" + createFilePath);
		try {
			// step3 创建数据模型
			Map<String, Object> dataMap = new HashMap<String, Object>();

			dataMap.put("atfnBo", atfnBo);
			dataMap.put("modName", ClassUtil.chcString(paraMap.get("MOD_NAME")));
			
			this.createFile("sqlTemplate.ftl", createFilePath, dataMap,
					paraMap);
		} catch (Exception e) {
			logger.error("sql文件创建失败：" + e.getMessage());
		}
	}
	

	/**
	 * 生成模板文件入口
	 * 
	 * @param templateName
	 * @param createFilePath
	 * @param dataMap
	 */
	public void createFile(String templateName, String createFilePath,
			Map dataMap, Map paraMap) {
		Writer out = null;
		try {
			// 创建freeMarker配置实例
			Configuration configuration = new Configuration();
			// 获取模版路径
			configuration.setDirectoryForTemplateLoading(new File(ClassUtil
					.chcString(paraMap.get("TEMP_PATH"))));
			// 加载模版文件（表中的字段）
			Template template = configuration.getTemplate(templateName);
			if (null == template) {
				logger.info("模板文件不存在:" + templateName);
				return;
			}
			File createFile = new File(createFilePath);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(createFile)));
			template.process(dataMap, out);
			logger.info("文件生成成功：" + createFilePath);
		} catch (Exception e) {
			logger.error("文件生成失败：" + e.getMessage());
		} finally {
			try {
				if (null != out) {
					out.flush();
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage());
			}
		}
	}

}
