package org.zv.fintrack.command;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zv.common.mvc.CommandSupport;
import org.zv.common.mvc.Resource;
import org.zv.fintrack.ejb.api.CategoryDao;
import org.zv.fintrack.ejb.api.ExpenseDao;
import org.zv.fintrack.pd.Category;
import org.zv.fintrack.pd.Expense;
import org.zv.fintrack.util.Messages;

/**
 * Handle list request.
 * 
 * @author Arvid Juskaitis
 */
public class ExpenseCommand extends CommandSupport {

	/**
	 * Selected expense for editing, deleting.
	 */
	private Expense expense = new Expense();

	/**
	 * Error message
	 */
	private String error;
	
	/**
	 * Message
	 */
	private String message;

	/**
	 * Selected income/expense record id for editing, deleting.
	 */
	private Integer preinitId;

	/**
	 * Session bean, injected by MVC.
	 */
	private CategoryDao categoryDao;
	
	/**
	 * Session bean, injected by MVC.
	 */
	private ExpenseDao expenseDao;
	
	@Resource(name = "ejb/CategoryDao")
	public void setCategoryDao(CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
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
		expense.setCreateDate(new Date());
		
		// Forward to the view
		return "expense";
	}

	/**
	 * Handle submit.
	 * 
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String save(HttpServletRequest request, HttpServletResponse response) {
		// do validation
		long days = Math.abs(new Date().getTime() - expense.getCreateDate().getTime())/1000/3660/24;
		if (days > 365) {
			error = Messages.getMessageResourceString("messages", Locale.US, "error.date.range", new Long[] { days });
			return "expense";
		}
		if (expense.getCategoryId() == null) {
			error = Messages.getMessageResourceString("messages", Locale.US, "error.no.category", null);
			return "expense";
		}
		if (expense.getAmount() == 0.0) {
			error = Messages.getMessageResourceString("messages", Locale.US, "error.amount.min", new Float[] { 0.0f });
			return "expense";
		}

		// persist/update entity
		if (preinitId == null) {
			expense.setUserId(request.getUserPrincipal().getName());
			expenseDao.save(expense);
			message = Messages.getMessageResourceString("messages", Locale.US, "status.added.expense", null);

			// Reset to default
			expense = new Expense();
			return execute(request, response);
		} else {
			expense.setExpenseId(preinitId);
			expense.setUserId(request.getUserPrincipal().getName());
			expenseDao.save(expense);
			message = Messages.getMessageResourceString("messages", Locale.US, "status.updated.expense", new Integer[] {preinitId});
		}
		
		return "expense";
	}

	/**
	 * Edit selected expense record.
	 * 
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) {
		if (preinitId == null) {
			throw new RuntimeException("item id is not set");
		}
    	expense = expenseDao.getById(preinitId);
		return "expense";
	}
	
	/**
	 * Prompt for deletion of expense record.
	 * 
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		if (preinitId == null) {
			throw new RuntimeException("item id is not set");
		}
    	expense = expenseDao.getById(preinitId);
		return "expense-delete";
	}

	/**
	 * Do actual deletion of expense record.
	 * 
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String doDelete(HttpServletRequest request, HttpServletResponse response) {
		if (preinitId == null) {
			throw new RuntimeException("item id is not set");
		}
    	expenseDao.delete(preinitId);
		expense = new Expense();
		message = Messages.getMessageResourceString("messages", Locale.US, "status.deleted.expense", new Integer[] {preinitId});
		return "expense-delete";
	}

	public List<Category> getCategories() {
		return categoryDao.getAll();
	}

	public Expense getExpense() {
		return expense;
	}

	public void setCreateDate(Date createDate) {
		expense.setCreateDate(createDate);
	}

	public void setCategoryId(String categoryId) {
		expense.setCategoryId(categoryId);
	}

	public void setAmount(float amount) {
		expense.setAmount(amount);
	}

	public void setDescr(String descr) {
		expense.setDescr(descr);
	}

	public String getError() {
		return error;
	}

	public String getMessage() {
		return message;
	}

	public Integer getPreinitId() {
		return preinitId;
	}

	public void setPreinitId(Integer preinitId) {
		this.preinitId = preinitId;
	}
}
