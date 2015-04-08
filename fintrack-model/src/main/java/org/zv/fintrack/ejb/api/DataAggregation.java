package org.zv.fintrack.ejb.api;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;

import org.zv.fintrack.pd.bean.SummaryBean;

@Local
public interface DataAggregation {

	/**
	 * Calculate totals from supplied list.
	 * @return a bean.
	 */
	SummaryBean total(List<SummaryBean> beans);

	/**
	 * Join two set results by adding missing gaps.
	 * Result is presented as list of maps with month, income, expense values. List is ordered by month field.
	 * @return a list of maps.
	 */
	List<Map<String, Object>> joinSummary(List<SummaryBean> incomes, List<SummaryBean> expenses);
}