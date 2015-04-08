package org.zv.fintrack.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zv.common.mvc.CommandSupport;
import org.zv.common.mvc.Resource;
import org.zv.fintrack.ejb.api.IncomeDao;
import org.zv.fintrack.ejb.api.ExpenseDao;

/**
 * Handle logout request and redirect to home page.
 * 
 * @author Arvid Juskaitis
 */
public class HomeCommand extends CommandSupport {

	/**
	 * Session bean, injected by MVC.
	 */
	private IncomeDao incomeDao;
	
	/**
	 * Session bean, injected by MVC.
	 */
	private ExpenseDao expenseDao;

	@Resource(name = "ejb/IncomeDao")
	public void setIncomeDao(IncomeDao incomeDao) {
		this.incomeDao = incomeDao;
	}

	@Resource(name = "ejb/ExpenseDao")
	public void setExpenseDao(ExpenseDao expenseDao) {
		this.expenseDao = expenseDao;
	}
	
	/**
	 * List recently added records.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 * @throws NamingException 
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		// Retrieve data and put it to request context
		request.setAttribute("recentIncomes", incomeDao.getAll(8));
		request.setAttribute("recentExpenses", expenseDao.getAll(15));
	
		// Forward to the view
		return "home";
	}
}
