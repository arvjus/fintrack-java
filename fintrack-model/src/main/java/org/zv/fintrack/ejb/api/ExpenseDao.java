package org.zv.fintrack.ejb.api;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import org.zv.fintrack.pd.Expense;
import org.zv.fintrack.pd.bean.SummaryBean;

@Local
public interface ExpenseDao {

	/**
	 * Retrieve all items.
	 * @return a list.
	 */
	List<Expense> getAll(int maxResults);

	/**
	 * Retrieve items by selected criteria.
	 * @return a list.
	 */
	List<Expense> getPlain(Date dateFrom, Date dateTo, String userId, Object[] categoryIds);

	/**
	 * Retrieve summary records, grouped by category, filtered by selected criteria.
	 * @return a list.
	 */
	List<SummaryBean> getSummaryByCategory(Date dateFrom, Date dateTo, Object[] categoryIds);

	/**
	 * Retrieve summary records, grouped by month, filtered by selected criteria.
	 * @return a list.
	 */
	List<SummaryBean> getSummaryByMonth(Date dateFrom, Date dateTo, Object[] categoryIds);

	/**
	 * Retrieve by id.
	 * @param id category id.
	 * @return requested entity.
	 */
	Expense getById(Integer id);

	/**
	 * Persist entity to database.
	 * @param expense
	 */
	Expense save(Expense expense);

	/**
	 * Remove entity from database.
	 * @param expense id
	 */
	void delete(Integer id);
}