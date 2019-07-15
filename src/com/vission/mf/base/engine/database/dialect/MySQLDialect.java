package com.vission.mf.base.engine.database.dialect;

import java.util.List;

import com.vission.mf.base.engine.database.bo.Column;
import com.vission.mf.base.engine.database.bo.Forkey;
import com.vission.mf.base.engine.database.bo.Index;
import com.vission.mf.base.engine.database.bo.Table;
import com.vission.mf.base.enums.DatabaseConstants;
import com.vission.mf.base.exception.ServiceException;

@SuppressWarnings("unused")
public class MySQLDialect extends Dialect {
	@Override
	public String getTableIndexSQL() {
		StringBuffer sb = new StringBuffer();
		sb.append("select a.INDNAME ").append(DatabaseConstants.INDEX_NAME);
		sb.append(",a.UNIQUERULE ").append(DatabaseConstants.UNIQUERULE);
		sb.append(",b.COLNAME ").append(DatabaseConstants.COLUMN_NAME);
		sb.append(",b.COLSEQ ").append(DatabaseConstants.ORDINAL_POSITION);
		sb.append(" from SYSCAT.INDEXES a, SYSCAT.INDEXCOLUSE b ");
		sb.append("where a.INDNAME=b.INDNAME and a.INDSCHEMA=b.INDSCHEMA and a.UNIQUERULE<>'P' and a.TABSCHEMA=? and a.TABNAME=? order by a.INDNAME,b.COLSEQ");
		return sb.toString();
	}
	
	@Override
	public String getCreateTableSQL(Table table, List<Column> columns) {
		StringBuffer tableSql = new StringBuffer();
		StringBuffer primaryKeySql = new StringBuffer("PRIMARY KEY (");
		String sqlStart = "CREATE TABLE ";
		if (columns != null && columns.size()>0) {
			tableSql.append(sqlStart).append(table.getTableEname()).append("( \r\n");
			for (Column column : columns) {
				tableSql.append("	").append(column.getColumnEname()).append("     ");
				if (!"DATE".equals(column.getColumnType()) && !"INTEGER".equals(column.getColumnType())) {
					tableSql.append(column.getColumnType());
					tableSql.append("(").append(column.getColumnLen()).append(") ");
				} else {
					tableSql.append(column.getColumnType());
				}
				if (!column.isNullFlag()) {
					tableSql.append("NOT NULL");
				}
				tableSql.append(",");
				tableSql.append("\r\n");
				
				if (column.isKeyFlag()) {
					primaryKeySql.append(column.getColumnEname()).append(",");
				}
			}
		}
		if(primaryKeySql.length()>sqlStart.length()){
			primaryKeySql.deleteCharAt(primaryKeySql.length()-1);
			primaryKeySql.append(")\r\n");
			tableSql.append("	").append(primaryKeySql);
		}else{
			tableSql.deleteCharAt(tableSql.length()-1);
		}
		
		tableSql.append(");");
		tableSql.append("\r\n");
		return tableSql.toString();
	}
	
	@Override
	public String getCreateIndexSQL(Table table, List<Index> indexs) throws ServiceException {
		StringBuffer indexSql = new StringBuffer();
		if (indexs != null && indexs.size()>0) {
			for (Index idnex : indexs) {
				if (idnex.isUniqueFlag()) {
					indexSql.append("CREATE UNIQUE INDEX ");
				} else {
					indexSql.append("CREATE INDEX ");
				}
				indexSql.append(idnex.getIndexName());
				indexSql.append(" ON ").append(table.getTableEname());
				indexSql.append("(");
				String[] columns = idnex.getIndexColumns().split(",");
				for (int i = 0; i <= columns.length - 1; i++) {
					indexSql.append(columns[i]);
					if (i < columns.length - 1) {
						indexSql.append(",");
					}
				}
				indexSql.append(");\r\n");
			}
		}
		return indexSql.toString();
	}
	
	@Override
	public String getCreateForkeySQL(Table table, List<Forkey> forkeys) throws ServiceException {
		StringBuffer constraintSql = new StringBuffer();
		if (forkeys != null && forkeys.size()>0) {
			constraintSql.append("ALTER TABLE ").append(table.getTableEname()).append("\r\n");
			for (Forkey forkey : forkeys) {
				constraintSql.append("	").append("ADD CONSTRAINT ").append(forkey.getForkeyName());
				String[] forKeyRef = forkey.getFkColumns().split(":");
				constraintSql.append(" FOREIGN KEY (").append(forKeyRef[0]).append(") ");
				constraintSql.append("REFERENCES ").append(forkey.getFkTableName()).append("(");
				constraintSql.append(forKeyRef[1]).append(")");
				constraintSql.append("\r\n");
			}
			constraintSql.append(";");
		}
		return constraintSql.toString();
	}
	
	@Override
	public String getTableSpaceSQL() throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append("select tbspace ").append(DatabaseConstants.TABLESPACE_NAME);
		sb.append(" from syscat.tables where tabschema=? and tabname=?");
		return sb.toString();
	}
	
	@Override
	public boolean isUniqueIndex(String str) throws ServiceException {
		return str.equals("U");
	}
	
	@Override
	public String getTopOneSql(String sql) throws ServiceException {
		if(sql.toLowerCase().indexOf("fetch first")>0){
			return sql;
		}else{
			return sql + " fetch first 1 rows only";
		}
	}
	
	@Override
	public String getPageSql(String sql, int pageSize, int startRow) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append(sql).append(" limit  ").append(startRow).append(",").append(pageSize);
		return sb.toString();
	}
}
