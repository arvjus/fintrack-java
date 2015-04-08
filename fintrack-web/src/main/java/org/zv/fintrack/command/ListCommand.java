package org.zv.fintrack.command;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.zv.common.mvc.CommandSupport;
import org.zv.common.mvc.Resource;
import org.zv.fintrack.ejb.api.CategoryDao;
import org.zv.fintrack.ejb.api.IncomeDao;
import org.zv.fintrack.ejb.api.ExpenseDao;
import org.zv.fintrack.pd.Category;
import org.zv.fintrack.pd.Expense;
import org.zv.fintrack.pd.Income;
import org.zv.fintrack.util.FintrackUtils;

/**
 * Handle list request.
 * 
 * @author Arvid Juskaitis
 */
public class ListCommand extends CommandSupport {

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
	 * Form element - record owner (optional).
	 */
	private String userId;

	/**
	 * Request result - incomes.
	 */
	private List<Income> incomes = new ArrayList<Income>();

	/**
	 * Request result - expenses.
	 */
	private List<Expense> expenses = new ArrayList<Expense>();

	/**
	 * Selected income for editing, deleting.
	 */
	private Income income;

	/**
	 * Selected expense for editing, deleting.
	 */
	private Expense expense;

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
	
	/**
	 * Put initial values for html elements and return without populating lists.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// Set default values
		Calendar botm = Calendar.getInstance();
		botm.setTime(new Date());
		botm.set(Calendar.DAY_OF_MONTH, 1);
		dateFrom =  botm.getTime();
		dateTo = new Date();
		
		// Forward to the view
		return "list";
	}

	/**
	 * List selected records.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String refresh(HttpServletRequest request, HttpServletResponse response) {
		if (incomeSelected || categoryIds == null || categoryIds.length == 0) {
			incomes = incomeDao.getPlain(dateFrom, dateTo, userId);
		}

		if (categoryIds != null || !incomeSelected) {
	    	expenses = expenseDao.getPlain(dateFrom, dateTo, userId, categoryIds);
		}
	
		// save search parameters
		HttpSession session = request.getSession(); 
		session.setAttribute("list.dateFrom", dateFrom);
		session.setAttribute("list.dateTo", dateTo);
		session.setAttribute("list.incomeSelected", incomeSelected);
		session.setAttribute("list.categoryIds", categoryIds);
		session.setAttribute("list.userId", userId);

		// Forward to the view
		return "list";
	}

	/**
	 * Restore search parameter and list selected records.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String back(HttpServletRequest request, HttpServletResponse response) {
		// restore search parameters
		dateFrom = (Date)request.getSession().getAttribute("list.dateFrom");
		dateTo = (Date)request.getSession().getAttribute("list.dateTo");
		incomeSelected = (Boolean)request.getSession().getAttribute("list.incomeSelected");
		categoryIds = (String[])request.getSession().getAttribute("list.categoryIds");
		userId = (String)request.getSession().getAttribute("list.userId");

		// Continue listing
		return refresh(request, response);
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public List<Category> getCategories() {
		return categoryDao.getAll();
	}

	public List<Income> getIncomes() {
		return incomes;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public Income getIncome() {
		return income;
	}

	public void setIncome(Income income) {
		this.income = income;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}
}
