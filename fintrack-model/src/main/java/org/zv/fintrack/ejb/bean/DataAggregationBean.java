package org.zv.fintrack.ejb.bean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.Stateless;
import javax.annotation.security.PermitAll;

import org.apache.commons.logging.Log;
import org.zv.fintrack.ejb.api.DataAggregation;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.util.LoggerFactory;
import org.zv.fintrack.util.FintrackUtils;

/**
 * DAO for Income entity.
 * 
 * @author arvid.juskaitis
 */
@Stateless
@PermitAll
public class DataAggregationBean implements DataAggregation {

	/**
	 * Class logger.
	 */
	private Log log = LoggerFactory.make();
	
	/**
	 * Calculate totals from supplied list.
	 * @return a bean.
	 */
	public SummaryBean total(List<SummaryBean> beans) {
		double totalAmount = 0.0;
		long totalCount = 0;
		for (SummaryBean bean : beans) {
			totalAmount += bean.getAmount();
			totalCount += bean.getCount();
		}
		return new SummaryBean(null, totalCount, new BigDecimal(totalAmount).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
	}

	/**
	 * Join two set results by adding missing gaps.
	 * Result is presented as list of maps with month, income, expense values. List is ordered by month field.
	 * @return a list of maps.
	 */
	public List<Map<String, Object>> joinSummary(List<SummaryBean> incomes, List<SummaryBean> expenses) {
		// collect all groups, beans
		Set<String> groups = new TreeSet<String>();
		Map<String, Object> beans = new HashMap<String, Object>(); 
		for (SummaryBean income : incomes) {
			groups.add(income.getGroup());
			beans.put("i" + income.getGroup(), income);
		}
		for (SummaryBean expense : expenses) {
			groups.add(expense.getGroup());
			beans.put("e" + expense.getGroup(), expense);
		}

		// put beans into maps, list
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String group : groups) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yyyymm", group);
			map.put("month", FintrackUtils.yyyymm2month(group));
			if (beans.containsKey("i"+group)) {
				map.put("income", beans.get("i"+group));
			} else {
				map.put("income", new SummaryBean(group, 0, 0));
			}
			if (beans.containsKey("e"+group)) {
				map.put("expense", beans.get("e"+group));
			} else {
				map.put("expense", new SummaryBean(group, 0, 0));
			}
			list.add(map);
		}
		
		// sort
		Collections.sort(list, new Comparator<Map<String, Object>>(){
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return o1.get("yyyymm").toString().compareTo(o2.get("yyyymm").toString());
			}
		});
		return list;
	}
}
