package com.vission.mf.base.dao;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Service;

import com.vission.mf.base.enums.db.SYS_ANNO_ACCE_INFO;
import com.vission.mf.base.hibernate.SimpleHibernateTemplate;
import com.vission.mf.base.model.po.SysAnnoAcceInfo;

/**
 * 功能/模块 ：公告附件管理
 * 
 */
@SuppressWarnings("serial")
@Service("sysAnnoAcceInfoDao")
public class SysAnnoAcceInfoDao extends
		SimpleHibernateTemplate<SysAnnoAcceInfo, String> {

	//临时存入的acce的id
	private static final String LIN_SHI_ID = "LIN_SHI_ID";
	
	@Autowired
	private JdbcTemplate jdbcTemplate = null;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

	
	public SysAnnoAcceInfoDao() {
		super(SysAnnoAcceInfo.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SysAnnoAcceInfo> getFileNamesByAnnoId(String annoId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ").append(SYS_ANNO_ACCE_INFO.FILE_NAME);
		sb.append(" from ").append(SYS_ANNO_ACCE_INFO.TABLE_NAME);
		sb.append(" where ").append(SYS_ANNO_ACCE_INFO.ANNO_ID);
		sb.append(" =:annoId");

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(
				"annoId", annoId);
		logger.info("JDBC:" + sb.toString());

		List<SysAnnoAcceInfo> list=  namedParameterJdbcTemplate.query(sb
				.toString(), mapSqlParameterSource, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				SysAnnoAcceInfo sysAnnoAcceInfo = new SysAnnoAcceInfo();
				sysAnnoAcceInfo.setAnnoId(null);
				sysAnnoAcceInfo.setFileName(rs
						.getString(SYS_ANNO_ACCE_INFO.FILE_NAME));
				sysAnnoAcceInfo.setFileContent(null);
				return sysAnnoAcceInfo;
			}
		});
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public SysAnnoAcceInfo getByAnnoIdAndFileName(String annoId, String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select *  from ").append(SYS_ANNO_ACCE_INFO.TABLE_NAME);
		sb.append(" where ").append(SYS_ANNO_ACCE_INFO.ANNO_ID);
		sb.append(" =:annoId");
		sb.append(" and ").append(SYS_ANNO_ACCE_INFO.FILE_NAME);
		sb.append(" =:fileName");
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource(
				"annoId", annoId);
		mapSqlParameterSource.addValue("fileName", fileName);
		logger.info("JDBC:" + sb.toString());

		List<SysAnnoAcceInfo> list=  namedParameterJdbcTemplate.query(sb
				.toString(), mapSqlParameterSource, new RowMapper() {
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				SysAnnoAcceInfo sysAnnoAcceInfo = new SysAnnoAcceInfo();
				sysAnnoAcceInfo.setAnnoId(rs
						.getString(SYS_ANNO_ACCE_INFO.ANNO_ID));
				sysAnnoAcceInfo.setFileName(rs
						.getString(SYS_ANNO_ACCE_INFO.FILE_NAME));

				sysAnnoAcceInfo.setFileContent(blobToBytes(rs
						.getBlob(SYS_ANNO_ACCE_INFO.FILE_CONTENT)));
				return sysAnnoAcceInfo;
			}
		});
		if(list==null||list.size()==0){
			return new SysAnnoAcceInfo();
		}else{
			return (SysAnnoAcceInfo)list.get(0);
		}
	}
	
	public int deleteByAnnoId(String annoId) {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from ").append(SYS_ANNO_ACCE_INFO.TABLE_NAME);
		sb.append(" where ").append(SYS_ANNO_ACCE_INFO.ANNO_ID);
		sb.append(" =:annoId");
		Map<String, String> map = new HashMap<String, String>();
		map.put("annoId", annoId);
		logger.info("JDBC:" + sb.toString());
		return namedParameterJdbcTemplate.update(sb.toString(), map);

	}
	
	public int deleteByAnnoIdAndFilename(String annoId,String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append("delete from ").append(SYS_ANNO_ACCE_INFO.TABLE_NAME);
		sb.append(" where ").append(SYS_ANNO_ACCE_INFO.ANNO_ID);
		sb.append(" =:annoId");
		sb.append(" and ").append(SYS_ANNO_ACCE_INFO.FILE_NAME);
		sb.append(" =:fileName");
		Map<String, String> map = new HashMap<String, String>();
		map.put("annoId", annoId);
		map.put("fileName", fileName);
		logger.info("JDBC:" + sb.toString());
		return namedParameterJdbcTemplate.update(sb.toString(), map);
	}

	private byte[] blobToBytes(Blob blob) {
		BufferedInputStream is = null;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			byte[] bytes = new byte[(int) blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;
			while (offset < len
					&& (read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
			return bytes;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				is.close();
				is = null;
			} catch (IOException e) {
				return null;
			}
		}
	}
	
	public void saveWithOutId(final SysAnnoAcceInfo sysAnnoAcceInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ").append(SYS_ANNO_ACCE_INFO.TABLE_NAME);
		sb.append("(").append(SYS_ANNO_ACCE_INFO.ANNO_ID);
		sb.append(",").append(SYS_ANNO_ACCE_INFO.FILE_NAME);
		sb.append(",").append(SYS_ANNO_ACCE_INFO.FILE_CONTENT);
		sb.append(") values (?,?,?)");
		logger.info("JDBC:" + sb.toString());

		jdbcTemplate.execute(sb.toString(),
				new AbstractLobCreatingPreparedStatementCallback(
						new DefaultLobHandler()) {
					protected void setValues(PreparedStatement ps,
							LobCreator lobCreator) throws SQLException {
						ps.setString(1, LIN_SHI_ID);
						ps.setString(2, sysAnnoAcceInfo.getFileName());
						lobCreator.setBlobAsBytes(ps, 3, sysAnnoAcceInfo.getFileContent());

					}
				});
	}

	/**
	 * 更新acce
	 */
	public void updatelinshiid(final String annoId) {
		StringBuffer sb = new StringBuffer();
		sb.append("update ").append(SYS_ANNO_ACCE_INFO.TABLE_NAME);
		sb.append(" set ").append(SYS_ANNO_ACCE_INFO.ANNO_ID);
		sb.append(" =:annoId ").append(" where ").append(SYS_ANNO_ACCE_INFO.ANNO_ID);
		sb.append(" =:linshiid");
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("annoId", annoId);
		map.put("linshiid", LIN_SHI_ID);
		namedParameterJdbcTemplate.update(sb.toString(), map);
	}

}