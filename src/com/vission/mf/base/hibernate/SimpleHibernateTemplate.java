package com.vission.mf.base.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.vission.mf.base.model.bo.DataGrid;
import com.vission.mf.base.sellms.model.po.SmsCustInfo;
import com.vission.mf.base.util.BeanUtils;

/**
 * 功能/模块 ： * Hibernate的范型基类.
 * 
 * 可以在service类中直接创建使用 .也可以继承出DAO子类,在多个Service类中共享DAO操作.
 * 通过Hibernate的sessionFactory.getCurrentSession()获得session,直接使用Hibernate原生API.
 * 
 * @param <T>
 *            DAO操作的对象类型
 * @param <PK>
 *            主键类型
 */
@SuppressWarnings({ "unchecked", "serial" })
public class SimpleHibernateTemplate<T, PK extends Serializable> implements
		Serializable {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;

	public SimpleHibernateTemplate(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public String getEntityClassName() {
		return entityClass.getName();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void save(T entity) {
		Assert.notNull(entity);
		getSession().saveOrUpdate(entity);
		logger.info("save entity: {}", entity);
	}
	
	public void merge(T entity) {
		Assert.notNull(entity);
		getSession().merge(entity);
		logger.info("merge entity: {}", entity);
	}
	public void onlySave(T entity) {
		Assert.notNull(entity);
		getSession().save(entity);
		logger.info("onlySave entity: {}", entity);
	}

	public void onlyUpdate(T entity) {
		Assert.notNull(entity);
		getSession().update(entity);
		logger.info("onlyUpdate entity: {}", entity);
	}
	
	public void delete(T entity) {
		Assert.notNull(entity);
		getSession().delete(entity);
		logger.info("delete entity: {}", entity);
	}

	public void delete(PK id) {
		Assert.notNull(id);
		delete(get(id));
	}

	public List<T> findAll() {
		return findByCriteria();
	}

	public Page<T> findAll(Page<T> page) {
		return findByCriteria(page);
	}

	/**
	 * 按id获取对象.
	 */
	public T get(final PK id) {
		return (T) getSession().load(entityClass, id);
	}
	/**
	 * 按id获取对象立即加载，防止session失效.
	 */
	public T getOnce(final PK id) {
		return (T) getSession().get(entityClass, id);
	}

	/**
	 * 按HQL查询对象列表.
	 * 
	 * @param hql
	 *            hql语句
	 * @param values
	 *            数量可变的参数
	 */
	@SuppressWarnings("rawtypes")
	public List find(String hql, Object... values) {
		return createQuery(hql, values).list();
	}

	/**
	 * 按HQL分页查询. 暂不支持自动获取总结果数,需用户另行执行查询.
	 * 
	 * @param page
	 *            分页参数.包括pageSize 和firstResult.
	 * @param hql
	 *            hql语句.
	 * @param values
	 *            数量可变的参数.
	 * 
	 * @return 分页查询结果,附带结果列表及所有查询时的参数.
	 */
	public Page<T> find(Page<T> page, String hql, Object... values) {
		Assert.notNull(page);

		if (page.isAutoCount()) {
			logger.warn("HQL查询暂不支持自动获取总结果数,hql为{}", hql);
		}
		Query q = createQuery(hql, values);
		if (page.isFirstSetted()) {
			q.setFirstResult(page.getFirst());
		}
		if (page.isPageSizeSetted()) {
			q.setMaxResults(page.getPageSize());
		}
		page.setResult(q.list());
		return page;
	}

	/**
	 * 按HQL查询唯一对象.
	 */
	public Object findUnique(String hql, Object... values) {
		return createQuery(hql, values).uniqueResult();
	}

	/**
	 * 按HQL查询Intger类形结果.
	 */
	public Integer findInt(String hql, Object... values) {
		return (Integer) findUnique(hql, values);
	}

	/**
	 * 按HQL查询Long类型结果.
	 */
	public Long findLong(String hql, Object... values) {
		return (Long) findUnique(hql, values);
	}

	/**
	 * 按Criterion查询对象列表.
	 * 
	 * @param criterion
	 *            数量可变的Criterion.
	 */
	public List<T> findByCriteria(Criterion... criterion) {
		return createCriteria(criterion).list();
	}

	/**
	 * 按Criterion查询对象列表.
	 * 
	 * @param criterion
	 *            数量可变的Criterion.
	 */
	public List<T> findByCriteria(Criterion criterion, String orderName,
			boolean isAsc) {
		if (isAsc) {
			return createCriteria(criterion).addOrder(Order.asc(orderName))
					.list();
		} else {
			return createCriteria(criterion).addOrder(Order.desc(orderName))
					.list();
		}
	}

	/**
	 * 按Criterion分页查询.
	 * 
	 * @param page
	 *            分页参数.包括pageSize、firstResult、orderBy、asc、autoCount.
	 *            其中firstResult可直接指定,也可以指定pageNo. autoCount指定是否动态获取总结果数.
	 * 
	 * @param criterion
	 *            数量可变的Criterion.
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	@SuppressWarnings("rawtypes")
	public Page<T> findByCriteria(Page page, Criterion... criterion) {
		Assert.notNull(page);

		Criteria c = createCriteria(criterion);

		if (page.isAutoCount()) {
			page.setTotalCount(countQueryResult(c));
		}
		if (page.isFirstSetted()) {
			c.setFirstResult(page.getFirst());
		}
		if (page.isPageSizeSetted()) {
			c.setMaxResults(page.getPageSize());
		}

		if (page.isOrderBySetted()) {
			if (page.getOrder().endsWith(QueryParameter.ASC)) {
				c.addOrder(Order.asc(page.getOrderBy()));
			} else {
				c.addOrder(Order.desc(page.getOrderBy()));
			}
		}
		page.setResult(c.list());
		return page;
	}

	/**
	 * 按属性查找对象列表.
	 */
	public List<T> findByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		return createCriteria(Restrictions.eq(propertyName, value)).list();
	}

	/**
	 * 按属性查找唯一对象.
	 */
	public T findUniqueByProperty(String propertyName, Object value) {
		Assert.hasText(propertyName);
		return (T) createCriteria(Restrictions.eq(propertyName, value))
				.uniqueResult();
	}
	
	/**
	 * 按属性查找唯一对象.
	 */
	public T findUniqueByProperty(Criterion... criterions) {
	
		return (T) createCriteria( criterions)
				.uniqueResult();
	}

	/**
	 * 根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 */
	public Query createQuery(String queryString, Object... values) {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 */
	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}

	/**
	 * 判断对象的属性值在数据库内是否唯一.
	 * 
	 * 在修改对象的情景下,如果属性新修改的值(value)等于属性原值(orgValue)则不作比较.
	 * 传回orgValue的设计侧重于从页面上发出Ajax判断请求的场景. 否则需要SS2里那种以对象ID作为第3个参数的isUnique函数.
	 */
	public boolean isPropertyUnique(String propertyName, Object newValue,
			Object orgValue) {
		if (newValue == null || newValue.equals(orgValue))
			return true;

		Object object = findUniqueByProperty(propertyName, newValue);
		return (object == null);
	}

	/**
	 * 通过count查询获得本次查询所能获得的对象总数.
	 * 
	 * @return page对象中的totalCount属性将赋值.
	 */
	@SuppressWarnings("rawtypes")
	protected int countQueryResult(Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) BeanUtils.getFieldValue(impl, "orderEntries");
			BeanUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		int totalCount = ((Long) c.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		if (totalCount < 1) {
			totalCount = 0;
		}

		// 将之前的Projection和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}

		try {
			BeanUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}

	/*
	 * hql 只能以from开头,不能以select 开头 比如from user where XXX ORDER BY
	 */

	public DataGrid findDataPage(DataGrid dataGrid, int pageSize, String hql,
			Object... values) {
		Assert.notNull(dataGrid);
		Query q = createQuery(hql, values);

		q.setFirstResult(dataGrid.getStartRow());

		q.setMaxResults(pageSize);
		int tolal = findLong("select count(*) " + hql, values).intValue();

		dataGrid.setTotal(tolal);
		dataGrid.setRows(q.list());
		return dataGrid;
	}

	/**
	 * 按Criterion分页查询.
	 * 
	 * @param DataPage
	 * 
	 * 
	 * @param criterion
	 *            数量可变的Criterion.
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	public DataGrid findByCriteria(DataGrid dataGrid, int pageSize,
			String orderName, boolean isAsc, Criterion... criterion) {
		Assert.notNull(dataGrid);
		Criteria c = createCriteria(criterion);
		dataGrid.setTotal(countQueryResult(c));
		if (isAsc)
			c.addOrder(Order.asc(orderName));
		else
			c.addOrder(Order.desc(orderName));
		c.setFirstResult(dataGrid.getStartRow());
		c.setMaxResults(pageSize);
		dataGrid.setRows(c.list());
		return dataGrid;
	}

	/**
	 * 按Criterion分页查询.
	 * 
	 * @param DataPage
	 * @param pageSize
	 * @param filterMap
	 *            通过CriteriaSetup设置好的规则进行处理
	 * @param orderMap
	 * 
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid findByCriteria(DataGrid dataGrid, int pageSize, Map filterMap,
			Map orderMap) {
		Assert.notNull(dataGrid);//判断对象是否为空

		Criteria criteria = getSession().createCriteria(entityClass);

		if (!CollectionUtils.isEmpty(filterMap)) {
			CriteriaSetup.setup(criteria, filterMap);
		}
		int count = countQueryResult(criteria);
		dataGrid.setTotal(count);
		criteria.setFirstResult(dataGrid.getStartRow());
		criteria.setMaxResults(pageSize);
		if (count <= 0) {
			dataGrid.setRows(new ArrayList<T>());
		} else {
			if (!CollectionUtils.isEmpty(orderMap)) {
				sortCriteria(criteria, orderMap);
			}

			dataGrid.setRows(criteria.list());
		}

		return dataGrid;
	}

	/**
	 * 不分页得到所有数据
	 * @param dataGrid
	 * @param filterMap
	 * @param orderMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid findByCriteria(DataGrid dataGrid, Map filterMap,
			Map orderMap) {
		Assert.notNull(dataGrid);

		Criteria criteria = getSession().createCriteria(entityClass);

		if (!CollectionUtils.isEmpty(filterMap)) {
			CriteriaSetup.setup(criteria, filterMap);
		}
		int count = countQueryResult(criteria);
		dataGrid.setTotal(count);
		criteria.setFirstResult(dataGrid.getStartRow());
		if (count <= 0) {
			dataGrid.setRows(new ArrayList<T>());
		} else {
			if (!CollectionUtils.isEmpty(orderMap)) {
				sortCriteria(criteria, orderMap);
			}

			dataGrid.setRows(criteria.list());
		}

		return dataGrid;
	}
	
	@SuppressWarnings("rawtypes")
	protected void sortCriteria(Criteria criteria, Map sortMap) {
		for (Object o : sortMap.keySet()) {
			String fieldName = o.toString();
			String orderType = sortMap.get(fieldName).toString();
			if (CriteriaSetup.ASC.equalsIgnoreCase(orderType)) {
				criteria.addOrder(Order.asc(fieldName));
			} else {
				criteria.addOrder(Order.desc(fieldName));
			}
		}
	}


}