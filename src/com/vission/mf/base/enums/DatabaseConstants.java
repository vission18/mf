package com.vission.mf.base.enums;

public class DatabaseConstants {
	public static int DB_TYPE_DB2 = 1;//db2
	public static int DB_TYPE_ORACLE = 2;//oracle
	public static int DB_TYPE_MYSQL = 3;//mysql
	public static int DB_TYPE_SQLSERVER = 4;//sql server
	public static int DB_TYPE_OTHER = 0;//other
	public static int DB_TYPE_INFORMIX = 5;//INFORMIX
	public static int DB_TYPE_SYBASE = 6;//SYBASE
	
	//表模式
	public final static String TABLE_SCHEM = "TABLE_SCHEM";
	//表类别
	public final static String TABLE_CAT = "TABLE_CAT";
	
	//表相关
	public final static String TABLE_NAME = "TABLE_NAME";//表名称
	public final static String TABLE_TYPE = "TABLE_TYPE";//类型
	public final static String TABLE_TYPE_TABLE = "TABLE";
	public final static String TABLE_TYPE_VIEW = "VIEW";
	public final static String REMARKS = "REMARKS";//描述表、列的注释
	public final static String TABLESPACE_NAME = "TABLESPACE_NAME";//表空间
	public final static String SCOPE_CATLOG = "SCOPE_CATLOG";//表的类别，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
	public final static String SCOPE_SCHEMA = "SCOPE_SCHEMA";//表的模式，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
	public final static String SCOPE_TABLE = "SCOPE_TABLE";//表名称，它是引用属性的作用域（如果 DATA_TYPE 不是 REF，则为 null）
	public final static String SOURCE_DATA_TYPE = "SOURCE_DATA_TYPE";//不同类型或用户生成 Ref 类型、来自 java.sql.Types 的 SQL 类型的源类型（如果 DATA_TYPE 不是 DISTINCT 或用户生成的 REF，则为 null）
	public final static String IS_AUTOINCREMENT = "IS_AUTOINCREMENT";//指示此列是否自动新增

	//列相关
	public final static String COLUMN_NAME = "COLUMN_NAME";//列名称
	public final static String IS_NULLABLE = "IS_NULLABLE";//ISO 规则用于确定列是否包括 null
	public final static String IS_NULLABLE_YES = "YES";
	public final static String IS_NULLABLE_NO = "NO";
	public final static String COLUMN_DEF = "COLUMN_DEF";//该列的默认值，当值在单引号内时应被解释为一个字符串
	public final static String COLUMN_SIZE = "COLUMN_SIZE";//列的大小
	public final static String DECIMAL_DIGITS = "DECIMAL_DIGITS";//小数部分的位数。对于 DECIMAL_DIGITS 不适用的数据类型,则返回 Null
	public final static String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH";//对于 char 类型，该长度是列中的最大字节数
	public final static String ORDINAL_POSITION = "ORDINAL_POSITION";//表中的列的索引（从 1 开始）
	
	//主键
	public final static String PK_NAME = "PK_NAME";
	
	//索引
	public final static String UNIQUERULE = "UNIQUERULE";
	public final static String INDEX_QUALIFIER = "INDEX_QUALIFIER";
	public final static String INDEX_NAME = "INDEX_NAME";
	public final static String TYPE = "TYPE";
	public final static String ASC_OR_DESC = "ASC_OR_DESC";
	public final static String CARDINALITY = "CARDINALITY";
	public final static String PAGES = "PAGES";
	public final static String FILTER_CONDITION = "FILTER_CONDITION";
	
	//外键
	public final static String PKTABLE_CAT = "PKTABLE_CAT";
	public final static String PKTABLE_SCHEM = "PKTABLE_SCHEM";
	public final static String PKTABLE_NAME = "PKTABLE_NAME";
	public final static String PKCOLUMN_NAME = "PKCOLUMN_NAME";
	public final static String FKTABLE_CAT = "FKTABLE_CAT";
	public final static String FKTABLE_SCHEM = "FKTABLE_SCHEM";
	public final static String FKTABLE_NAME = "FKTABLE_NAME";
	public final static String FKCOLUMN_NAME = "FKCOLUMN_NAME";
	public final static String KEY_SEQ = "KEY_SEQ";
	public final static String UPDATE_RULE = "UPDATE_RULE";
	public final static String DELETE_RULE = "DELETE_RULE";
	public final static String FK_NAME = "FK_NAME";
	public final static String DEFERRABILITY = "DEFERRABILITY";
	
	//其他
	public final static String TYPE_NAME = "TYPE_NAME";//数据源依赖的类型名称,对于 UDT,该类型名称是完全限定的
	public final static String BUFFER_LENGTH = "BUFFER_LENGTH";//未被使用
	public final static String LOCAL_TYPE_NAME = "LOCAL_TYPE_NAME";
	public final static String LITERAL_PREFIX = "LITERAL_PREFIX";
	public final static String LITERAL_SUFFIX = "LITERAL_SUFFIX";
	public final static String CREATE_PARAMS = "CREATE_PARAMS";
	public final static String DATA_TYPE = "DATA_TYPE";//来自 java.sql.Types 的 SQL 类型
	public final static String PRECISION = "PRECISION";
	public final static String NULLABLE = "NULLABLE";//是否允许使用 NULL
	public final static String SEARCHABLE = "SEARCHABLE";
	public final static String MINIMUM_SCALE = "MINIMUM_SCALE";
	public final static String MAXIMUM_SCALE = "MAXIMUM_SCALE";
	public final static String SQL_DATA_TYPE = "SQL_DATA_TYPE";//未使用
	public final static String SQL_DATETIME_SUB = "SQL_DATETIME_SUB";//未使用
	public final static String NUM_PREC_RADIX = "NUM_PREC_RADIX";//基数（通常为 10 或 2）
	public final static String UNSIGNED_ATTRIBUTE = "UNSIGNED_ATTRIBUTE";
	public final static String FIXED_PREC_SCALE = "FIXED_PREC_SCALE";
	public final static String AUTO_INCREMENT = "AUTO_INCREMENT";
	public final static String CASE_SENSITIVE = "CASE_SENSITIVE";

}
