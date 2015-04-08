package org.zv.fintrack.ejb.bean;

import java.util.LinkedList;
import java.util.List;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

import org.apache.commons.logging.Log;
import org.zv.fintrack.ejb.api.IncomeDao;
import org.zv.fintrack.pd.Income;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.util.LoggerFactory;

/**
 * DAO for Income entity.
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("unchecked")
@Stateless
@DeclareRoles({"viewer", "reporter"})
public class IncomeDaoBean implements IncomeDao {

	/**
	 * Class logger.
	 */
	private Log log = LoggerFactory.make();
	
    /**
     * Handle to the entity manager (injected).
     */
	@PersistenceContext(name="fintrack")
    private EntityManager em;

	/**
	 * Retrieve all items.
	 * @return a list.
	 */
	@RolesAllowed("viewer")
//	@PermitAll
	public List<Income> getAll(int maxResults) {
		log.debug("IncomeDaoBean.getAll(" + maxResults + ")");
		Query q = em.createQuery("SELECT i FROM Income AS i ORDER BY i.createDate DESC");
		if (maxResults > 0) {
			q.setMaxResults(maxResults);
		}
		return q.getResultList();
	}

	/**
	 * Retrieve items by selected criteria.
	 * @return a list.
	 */
	@RolesAllowed("viewer")
//	@PermitAll
	public List<Income> getPlain(Date dateFrom, Date dateTo, String userId) {
		log.debug("IncomeDaoBean.getPlain(" + dateFrom + ", " + dateTo + ")");
		String query = "SELECT i FROM Income AS i WHERE i.createDate BETWEEN :dateFrom AND :dateTo ";
		if (userId != null && userId.length() > 0) {
			query += " AND i.userId = '" + userId + "'";
		}
		Query q = em.createQuery(query + " ORDER BY i.createDate DESC");
		q.setParameter("dateFrom", dateFrom);
		q.setParameter("dateTo", dateTo);
		return q.getResultList();
	}
	
	/**
	 * Retrieve summary records, filtered by selected criteria.
	 * @return a list.
	 */
	@RolesAllowed("viewer")
//	@PermitAll
	public List<SummaryBean> getSummarySimple(Date dateFrom, Date dateTo) {
		log.debug("IncomeDaoBean.getSummarySimple(" + dateFrom + ", " + dateTo + ")");
		/* this query fails on aggregate constructor if no result is selected
		Query q = em.createQuery(
				"SELECT new org.zv.fintrack.pd.bean.SummaryBean(COUNT(i.incomeId), SUM(i.amount)) " + 
				" FROM Income AS i WHERE i.createDate BETWEEN :dateFrom AND :dateTo");
		q.setParameter("dateFrom", dateFrom);
		q.setParameter("dateTo", dateTo);
		return q.getResultList();
		*/
		Query q = em.createNativeQuery(
				"SELECT COUNT(i.income_id), SUM(i.amount) " + 
				"  FROM Incomes AS i " +
				" WHERE i.create_date BETWEEN ?1 AND ?2 ");
		q.setParameter(1, dateFrom);
		q.setParameter(2, dateTo);
		
		List<SummaryBean> list = new LinkedList<SummaryBean>();
		List<Object[]> rows = q.getResultList();
		for (Object[] row : rows) {
			SummaryBean bean = new SummaryBean(null, row[0], row[1]); 
			list.add(bean);
		}
		return list;
	}
	
	/**
	 * Retrieve summary records, grouped by month + category, filtered by selected criteria.
	 * @return a list.
	 */
	@RolesAllowed("viewer")
//	@PermitAll
	public List<SummaryBean> getSummaryByMonth(Date dateFrom, Date dateTo) {
		log.debug("IncomeDaoBean.getSummaryByMonth(" + dateFrom + ", " + dateTo + ")");
		Query q = em.createNativeQuery(
				"SELECT SUBSTRING(i.create_date::varchar, 1, 7), COUNT(i.income_id), SUM(i.amount) " + 
				"  FROM Incomes AS i " +
				" WHERE i.create_date BETWEEN ?1 AND ?2 " + 
				" GROUP BY SUBSTRING(i.create_date::varchar, 1, 7) " +
				" ORDER BY SUBSTRING(i.create_date::varchar, 1, 7) ");
		q.setParameter(1, dateFrom);
		q.setParameter(2, dateTo);
		
		List<SummaryBean> list = new LinkedList<SummaryBean>();
		List<Object[]> rows = q.getResultList();
		for (Object[] row : rows) {
			list.add(new SummaryBean(row[0], row[1], row[2]));
		}
		return list;
	}

	/**
	 * Calculate totals from supplied list.
	 * @return a bean.
	 */
	@PermitAll
	public SummaryBean getTotal(List<SummaryBean> incomes) {
		double totalAmount = 0.0;
		long totalCount = 0;
		for (SummaryBean bean : incomes) {
			totalAmount += bean.getAmount();
			totalCount += bean.getCount();
		}
		return new SummaryBean(null, totalCount, totalAmount);
	}
	
	/**
	 * Retrieve by id.
	 * @param id category id.
	 * @return requested entity.
	 */
	@RolesAllowed("viewer")
	public Income getById(Integer id) {
		return em.find(Income.class, id);
	}

	/**
	 * Persist entity to database.
	 * @param income
	 */
	@RolesAllowed("reporter")
	public Income save(Income income) {
		Income merged_income = em.merge(income);
		em.flush();
		return merged_income;
	}

	/**
	 * Remove entity from database.
	 * @param income id
	 */
	@RolesAllowed("reporter")
	public void delete(Integer id) {
		Income income = em.find(Income.class, id);
		em.remove(income);
		em.flush();
	}
}
