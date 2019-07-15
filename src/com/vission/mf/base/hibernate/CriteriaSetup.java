package com.vission.mf.base.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
/**
* 功能/模块 ： hiberate查询自动生成引擎
*/
public class CriteriaSetup {
	public static final String LIKE_ALL = "like_all_";
	public static final String LIKE_START = "like_start_";
	public static final String LIKE_END = "like_end_";
	public static final String NOLIKE_END = "nolike_end_";
	public static final String NOLIKE_ALL = "no_like_all_";
	public static final String NOLIKE_START = "no_like_start_";
	public static final String LE = "le_";//小于等于
	public static final String GE = "ge_";//大于等于
	public static final String LT = "lt_";//小于
	public static final String GT = "gt_";//大于
	public static final String EQ = "eq_";//等于
	public static final String IN = "in_";
	
	public static final String ASC="asc";
	
	public static final String DESC="desc";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setup(Criteria criteria, Map filter) {
		if (filter != null && !filter.isEmpty()) {
			for (Entry<String, Object> e : (Set<Entry<String, Object>>) filter
					.entrySet()) {
				Object value = e.getValue();
				String key = e.getKey();
				setupColumn(key, value, criteria);
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static void setupColumn(String key, Object value, Criteria criteria) {
		// 如果字符串为空，同时非必须字段，直接返回
		if (isBlankObj(value)) {
			return;
		}

		if (key.startsWith(EQ)) {
			key = key.substring(EQ.length());
			criteria.add(Restrictions.eq(key, value));
		} else if (key.startsWith(LIKE_ALL)) {
			key = key.substring(LIKE_ALL.length());
			criteria.add(Restrictions.like(key, (String) value,
					MatchMode.ANYWHERE));

		} else if (key.startsWith(LE)) {
			key = key.substring(LE.length());
			criteria.add(Restrictions.le(key, value));
		} else if (key.startsWith(GE)) {
			key = key.substring(GE.length());
			criteria.add(Restrictions.ge(key, value));

		} else if (key.startsWith(LT)) {
			key = key.substring(LT.length());
			criteria.add(Restrictions.lt(key, value));
		} else if (key.startsWith(GT)) {
			key = key.substring(GT.length());
			criteria.add(Restrictions.gt(key, value));
		} else if (key.startsWith(LIKE_START)) {
			key = key.substring(LIKE_START.length());
			criteria.add(Restrictions
					.like(key, (String) value, MatchMode.START));
		} else if (key.startsWith(LIKE_END)) {
			key = key.substring(LIKE_END.length());
			criteria.add(Restrictions.like(key, (String) value, MatchMode.END));

		} else if (key.startsWith(NOLIKE_END)) {
			key = key.substring(NOLIKE_END.length());
			criteria.add(Restrictions.not(Restrictions.like(key,
					(String) value, MatchMode.END)));
		} else if (key.startsWith(NOLIKE_START)) {
			key = key.substring(NOLIKE_START.length());
			criteria.add(Restrictions.not(Restrictions.like(key,
					(String) value, MatchMode.START)));
		} else if (key.startsWith(NOLIKE_ALL)) {
			key = key.substring(NOLIKE_ALL.length());
			criteria.add(Restrictions.not(Restrictions.like(key,
					(String) value, MatchMode.ANYWHERE)));
		} else if (key.startsWith(IN)) {
			key = key.substring(IN.length());
			if(value instanceof String){
				criteria.add(Restrictions.in(key, value.toString().split(",")));
			}else if(value instanceof List){
				criteria.add(Restrictions.in(key, (List)value));
			}
		}
	}

	protected static boolean isBlankObj(Object o) {
		if (o == null) {
			return true;
//		}
//		if (o instanceof String) {
//			return StringUtils.isBlank(o.toString());
		} else {
			return false;
		}
	}

}