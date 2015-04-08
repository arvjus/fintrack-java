package org.zv.fintrack.ejb.bean;

import java.util.LinkedList;
import java.util.List;
import java.util.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;

import org.apache.commons.logging.Log;
import org.zv.fintrack.ejb.api.ExpenseDao;
import org.zv.fintrack.pd.Expense;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.util.LoggerFactory;

/**
 * DAO for Expense entity.
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("unchecked")
@Stateless
@DeclareRoles({"viewer", "reporter"})
public class ExpenseDaoBean implements ExpenseDao {

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
    public List<Expense> getAll(int maxResults) {
		log.debug("ExpenseDaoBean.getAll(" + maxResults + ")");
		Query q = em.createQuery("SELECT e FROM Expense AS e ORDER BY e.createDate DESC");
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
	public List<Expense> getPlain(Date dateFrom, Date dateTo, String userId, Object [] categoryIds) {
		log.debug("ExpenseDaoBean.getPlain(" + dateFrom + ", " + dateTo + ", " + categoryIds + ")");
		String query = "SELECT e FROM Expense AS e WHERE e.createDate BETWEEN :dateFrom AND :dateTo ";
		if (userId != null && userId.length() > 0) {
			query += " AND e.userId = '" + userId + "'";
		}
		if (categoryIds != null && categoryIds.length > 0) {
			String categories = "";
			for (Object categoryId : categoryIds) {
				if (categories.length() > 0) {
					categories += ",";
				}
				categories += "'" + categoryId + "'";
			}
			query += " AND e.categoryId IN(" + categories + ")";
		}
		Query q = em.createQuery(query + " ORDER BY e.createDate DESC");
		q.setParameter("dateFrom", dateFrom);
		q.setParameter("dateTo", dateTo);
		return q.getResultList();
	}

	/**
	 * Retrieve summary records, grouped by category, filtered by selected criteria.
	 * @return a list.
	 */
	@RolesAllowed("viewer")
//	@PermitAll
	public List<SummaryBean> getSummaryByCategory(Date dateFrom, Date dateTo, Object [] categoryIds) {
		log.debug("ExpenseDaoBean.getSummaryByCategory(" + dateFrom + ", " + dateTo + ", " + categoryIds + ")");
		String query =  "SELECT new org.zv.fintrack.pd.bean.SummaryBean(c.name, COUNT(e.expenseId), SUM(e.amount))" + 
						"  FROM Expense AS e INNER JOIN e.category AS c" + 
						" WHERE e.createDate BETWEEN :dateFrom AND :dateTo";
		if (categoryIds != null && categoryIds.length > 0) {
			String categories = "";
			for (Object categoryId : categoryIds) {
				if (categories.length() > 0) {
					categories += ",";
				}
				categories += "'" + categoryId + "'";
			}
			query += " AND e.categoryId IN(" + categories + ")";
		}
		Query q = em.createQuery(query + " GROUP BY c.name ORDER BY c.name");
		q.setParameter("dateFrom", dateFrom);
		q.setParameter("dateTo", dateTo);
		
		List<SummaryBean> list = new LinkedList<SummaryBean>();
		list.addAll(q.getResultList());
		return list;
	}

	/**
	 * Retrieve summary records, grouped by month, filtered by selected criteria.
	 * @return a list.
	 */
	@RolesAllowed("viewer")
//	@PermitAll
	public List<SummaryBean> getSummaryByMonth(Date dateFrom, Date dateTo, Object [] categoryIds) {
		log.debug("ExpenseDaoBean.getSummaryByMonth(" + dateFrom + ", " + dateTo + ", " + categoryIds + ")");
		String query =  "SELECT SUBSTRING(e.create_date::varchar, 1, 7), COUNT(e.expense_id), SUM(e.amount) " + 
						"  FROM Expenses AS e " +
						" WHERE e.create_date BETWEEN ?1 AND ?2 ";
		if (categoryIds != null && categoryIds.length > 0) {
			String categories = "";
			for (Object categoryId : categoryIds) {
				if (categories.length() > 0) {
					categories += ",";
				}
				categories += "'" + categoryId + "'";
			}
			query += " AND e.category_id IN(" + categories + ")";
		}
		query += "GROUP BY SUBSTRING(e.create_date::varchar, 1, 7) ";
		query += "ORDER BY SUBSTRING(e.create_date::varchar, 1, 7) ";
		Query q = em.createNativeQuery(query);
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
	 * Retrieve by id.
	 * @param id category id.
	 * @return requested entity.
	 */
	@RolesAllowed("viewer")
	public Expense getById(Integer id) {
		return em.find(Expense.class, id);
	}

	/**
	 * Persist entity to database.
	 * @param expense
	 */
	@RolesAllowed("reporter")
	public Expense save(Expense expense) {
		Expense merged_expense = em.merge(expense);
		em.flush();
		return merged_expense;
	}

	/**
	 * Remove entity from database.
	 * @param expense
	 */
	@RolesAllowed("reporter")
	public void delete(Integer id) {
		Expense expense = em.find(Expense.class, id);
		em.remove(expense);
		em.flush();
	}
}
