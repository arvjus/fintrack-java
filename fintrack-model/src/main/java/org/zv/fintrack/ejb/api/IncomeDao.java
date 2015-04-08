package org.zv.fintrack.ejb.api;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;

import org.zv.fintrack.pd.Income;
import org.zv.fintrack.pd.bean.SummaryBean;

@Local
public interface IncomeDao {

	/**
	 * Retrieve all items.
	 * @return a list.
	 */
	List<Income> getAll(int maxResults);

	/**
	 * Retrieve items by selected criteria.
	 * @return a list.
	 */
	List<Income> getPlain(Date dateFrom, Date dateTo, String userId);

	/**
	 * Retrieve summary records, filtered by selected criteria.
	 * @return a list.
	 */
	List<SummaryBean> getSummarySimple(Date dateFrom, Date dateTo);

	/**
	 * Retrieve summary records, grouped by month + category, filtered by selected criteria.
	 * @return a list.
	 */
	List<SummaryBean> getSummaryByMonth(Date dateFrom, Date dateTo);

	/**
	 * Retrieve by id.
	 * @param id category id.
	 * @return requested entity.
	 */
	Income getById(Integer id);

	/**
	 * Persist entity to database.
	 * @param income
	 */
	Income save(Income income);

	/**
	 * Remove entity from database.
	 * @param income id
	 */
	void delete(Integer id);
}