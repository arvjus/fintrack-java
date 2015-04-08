package org.zv.fintrack.command;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zv.common.mvc.CommandSupport;
import org.zv.common.mvc.Resource;
import org.zv.fintrack.ejb.api.CategoryDao;
import org.zv.fintrack.ejb.api.IncomeDao;
import org.zv.fintrack.ejb.api.ExpenseDao;
import org.zv.fintrack.ejb.api.DataAggregation;
import org.zv.fintrack.pd.Category;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.util.FintrackUtils;

/**
 * Handle summary request.
 * 
 * @author Arvid Juskaitis
 */
public class SummaryCommand extends CommandSupport {

	/**
	 * Form element - date range selection.
	 */
	private Date dateFrom;
	
	/**
	 * Form element - date range selection.
	 */
	private Date dateTo;

	/**
	 * Keep state of income selection.
	 */
	private Boolean incomeSelected = false;

	/**
	 * Keep state of category selections (for setter).
	 */
	private String [] categoryIds; 
	
	/**
	 * Groupped by selection.
	 */
	private String grouppedBy; 
	
	/**
	 * Keep state of include selection (for setter).
	 */
	private Boolean plotChart; 
	
	/**
	 * Request result - incomes.
	 */
	private List<SummaryBean> incomes = new ArrayList<SummaryBean>();

	/**
	 * Request result - incomes.
	 */
	private SummaryBean incomesTotal = new SummaryBean(null, 0, 0);

	/**
	 * Request result - expenses.
	 */
	private List<SummaryBean> expenses = new ArrayList<SummaryBean>();

	/**
	 * Request result - expenses.
	 */
	private SummaryBean expensesTotal = new SummaryBean(null, 0, 0);

	/**
	 * Joined summary aggregate.
	 */
	private List<Map<String, Object>> summary;
	
	/**
	 * Session bean, injected by MVC.
	 */
	private CategoryDao categoryDao;
	
	/**
	 * Session bean, injected by MVC.
	 */
	private IncomeDao incomeDao;
	
	/**
	 * Session bean, injected by MVC.
	 */
	private ExpenseDao expenseDao;

	/**
	 * Session bean, injected by MVC.
	 */
	private DataAggregation dataAggregation;
	
	
	@Resource(name = "ejb/CategoryDao")
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	@Resource(name = "ejb/IncomeDao")
	public void setIncomeDao(IncomeDao incomeDao) {
		this.incomeDao = incomeDao;
	}

	@Resource(name = "ejb/ExpenseDao")
	public void setExpenseDao(ExpenseDao expenseDao) {
		this.expenseDao = expenseDao;
	}
	
	@Resource(name = "ejb/DataAggregation")
	public void setDataAggregation(DataAggregation dataAggregation) {
		this.dataAggregation = dataAggregation;
	}
	
	/**
	 * Retrieve data according grouppedBy variable.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (grouppedBy == null) {
			// Set initial values
			Calendar botm = Calendar.getInstance();
			botm.setTime(new Date());
			botm.set(Calendar.DAY_OF_MONTH, 1);
			dateFrom =  botm.getTime();
			dateTo = new Date();
			plotChart = true;
			grouppedBy = "categories";
		} else if (grouppedBy.equals("categories")) {
			if (incomeSelected || categoryIds == null || categoryIds.length == 0) {
				incomes = incomeDao.getSummarySimple(dateFrom, dateTo);
				incomesTotal = dataAggregation.total(incomes);
			}

			if (categoryIds != null || !incomeSelected) {
		    	expenses = expenseDao.getSummaryByCategory(dateFrom, dateTo, categoryIds);
		    	expensesTotal = dataAggregation.total(expenses);
			}
		} else if (grouppedBy.equals("months")) {
			if (incomeSelected || categoryIds == null || categoryIds.length == 0) {
				incomes = incomeDao.getSummaryByMonth(dateFrom, dateTo);
				incomesTotal = dataAggregation.total(incomes);
			}

			if (categoryIds != null || !incomeSelected) {
				expenses = expenseDao.getSummaryByMonth(dateFrom, dateTo, categoryIds);
		    	expensesTotal = dataAggregation.total(expenses);
			}

			summary = dataAggregation.joinSummary(incomes, expenses);
		}  
		
		// Forward to the view
		return "summary";
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Boolean getIncomeSelected() {
		return incomeSelected;
	}

	public void setIncomeSelected(Boolean incomeSelected) {
		this.incomeSelected = incomeSelected;
	}

	public Map<String, Boolean> getCategoryIds() {
		return (Map<String, Boolean>) FintrackUtils.arrayToMap(categoryIds, Boolean.TRUE);
	}

	public void setCategoryIds(String [] categoryIds) {
		this.categoryIds = categoryIds;
	}

	public String getGrouppedBy() {
		return grouppedBy;
	}

	public void setGrouppedBy(String grouppedBy) {
		this.grouppedBy = grouppedBy;
	}

	public Boolean getPlotChart() {
		return plotChart;
	}

	public void setPlotChart(Boolean plotChart) {
		this.plotChart = plotChart;
	}

	public List<Category> getCategories() {
		return categoryDao.getAll();
	}

	public List<SummaryBean> getIncomes() {
		return incomes;
	}

	public SummaryBean getIncomesTotal() {
		return incomesTotal;
	}

	public List<SummaryBean> getExpenses() {
		return expenses;
	}

	public SummaryBean getExpensesTotal() {
		return expensesTotal;
	}

	public List<Map<String, Object>> getSummary() {
		return summary;
	}
}
