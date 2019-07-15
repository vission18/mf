package com.vission.mf.base.engine.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.RowSet;
import javax.sql.rowset.FilteredRowSet;
import javax.sql.rowset.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.rowset.FilteredRowSetImpl;

/**
 * 结果集过滤类
 */
public class ResultSetFilter {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private FilteredRowSet frs;
	/**
	 * @param rs --过滤的结果集
	 * @param args --过滤条件 columnName,value
	 * @throws SQLException 
	 */
	public ResultSetFilter(ResultSet rs) throws SQLException{
		frs = new FilteredRowSetImpl();
		frs.populate(rs);
	}
	
	/**
	 * 过滤数据
	 * @param dataList --过滤后获取的列数据 列名list
	 * @param filterArgs --过滤条件
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> filterData(List<String> dataColumnList, RangeEntery[] filterArgs) throws SQLException{
		frs.beforeFirst();//重新筛选前必须调用这个方法
		frs.setFilter(new Range(filterArgs));
		List<Map<String, Object>> dataMapList = new ArrayList<Map<String, Object>>(); 
		while (frs.next()) {
			Map<String, Object> dataMap = new HashMap<String, Object>(dataColumnList.size());
			for(String data : dataColumnList){
				dataMap.put(data, frs.getObject(data));
			}
			dataMapList.add(dataMap);
		}
		return dataMapList;
	}
	
	public void closeFilter() throws SQLException{
		frs.close();
	}
	
	public static void main(String[] args) throws Throwable {
		Class.forName("com.ibm.db2.jcc.DB2Driver");
		Connection connect = DriverManager.getConnection("jdbc:db2://localhost:50000/imsdb", "db2admin", "db2admin");
		PreparedStatement ps = connect.prepareStatement("SELECT * FROM A2_ACCT_TABLE");
		ResultSet rs = ps.executeQuery();

		ResultSetFilter rsf = new ResultSetFilter(rs);
		List<String> dataList = new ArrayList<String>();
		dataList.add("AMT1");
		dataList.add("AMT2");
		RangeEntery[] re = new RangeEntery[2];
		re[0] = new RangeEntery("cust_type", "001");
		re[1] = new RangeEntery("curr_code", "CNY");
		List<Map<String,Object>> dataMap = rsf.filterData(dataList, re);
		for(Map<String,Object> data : dataMap){
			for(String key : data.keySet()){
				System.out.print(key+"="+data.get(key)+"      ");
			}
			System.out.println();
		}

		rsf.closeFilter();
		rs.close();
		ps.close();
		connect.close();

	}
}

class Range implements Predicate {
	private RangeEntery[] args;
	private Map<String, RangeEntery> columnNames;

	public Range(RangeEntery... args) {
		if (args == null || args.length == 0) {
			return;
		}

		columnNames = new HashMap<String, RangeEntery>(args.length);
		for (RangeEntery re : args) {
			if (re != null) {
				columnNames.put(re.columnName, re);
			}
		}

		if (columnNames.size() == 0) {
			columnNames = null;
		} else {
			this.args = columnNames.values().toArray(
					new RangeEntery[columnNames.size()]);
		}
	}

	//过滤结果集--主要的数据过滤方法
	public boolean evaluate(RowSet rs) {
		if (args == null) {
			return true;
		}

		try {
			boolean[] arrFlag = new boolean[columnNames.size()];//存储所有过滤条件的结果
			int i = 0;
			for (RangeEntery re : args) {
				arrFlag[i] = re.filter(rs.getObject(re.columnName));
				i++;
			}
			for(i=0; i< arrFlag.length; i++){
				if(!arrFlag[i]){//有一个条件不满足时返回false (此处实现了 and 操作, 如需 or 操作则需要另外实现)
					return false;
				}
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	//根据列名过滤
	public boolean evaluate(Object value, String columnName)
			throws SQLException {
		return (columnNames == null || !columnNames.containsKey(columnName)) ? true
				: columnNames.get(columnName).filter(value);
	}

	//根据列索引过滤,暂未实现
	public boolean evaluate(Object value, int column) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
}
