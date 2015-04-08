package org.zv.fintrack.command;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zv.common.mvc.CommandSupport;
import org.zv.common.mvc.Resource;
import org.zv.fintrack.ejb.api.IncomeDao;
import org.zv.fintrack.pd.Income;
import org.zv.fintrack.util.Messages;

/**
 * Handle list request.
 * 
 * @author Arvid Juskaitis
 */
public class IncomeCommand extends CommandSupport {

	/**
	 * Selected income for editing, deleting.
	 */
	private Income income = new Income();

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
	private IncomeDao incomeDao;
	
	@Resource(name = "ejb/IncomeDao")
	public void setIncomeDao(IncomeDao incomeDao) {
		this.incomeDao = incomeDao;
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
		income.setCreateDate(new Date());
		
		// Forward to the view
		return "income";
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
		long days = Math.abs(new Date().getTime() - income.getCreateDate().getTime())/1000/3660/24;
		if (days > 365) {
			error = Messages.getMessageResourceString("messages", Locale.US, "error.date.range", new Long[] { days });
			return "income";
		}
		if (income.getAmount() == 0.0) {
			error = Messages.getMessageResourceString("messages", Locale.US, "error.amount.min", new Float[] { 0.0f });
			return "income";
		}
		
		// persist/update entity
		if (preinitId == null) {
			income.setUserId(request.getUserPrincipal().getName());
			incomeDao.save(income);
			message = Messages.getMessageResourceString("messages", Locale.US, "status.added.income", null);

			// Reset to default
			income = new Income();
			return execute(request, response);
		} else {
			income.setIncomeId(preinitId);
			income.setUserId(request.getUserPrincipal().getName());
			incomeDao.save(income);
			message = Messages.getMessageResourceString("messages", Locale.US, "status.updated.income", new Integer[] {preinitId});
		}
		
		return "income";
	}

	/**
	 * Edit selected income record.
	 * 
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) {
		if (preinitId == null) {
			throw new RuntimeException("item id is not set");
		}
		income = incomeDao.getById(preinitId);
		return "income";
	}
	
	/**
	 * Prompt for deletion of selected income record.
	 * 
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		if (preinitId == null) {
			throw new RuntimeException("item id is not set");
		}
		income = incomeDao.getById(preinitId);
		return "income-delete";
	}
	
	/**
	 * Do actual deletion of income record.
	 * 
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String doDelete(HttpServletRequest request, HttpServletResponse response) {
		if (preinitId == null) {
			throw new RuntimeException("item id is not set");
		}
		incomeDao.delete(preinitId);
		income = new Income();
		message = Messages.getMessageResourceString("messages", Locale.US, "status.deleted.income", new Integer[] {preinitId});
		return "income-delete";
	}
	
	public Income getIncome() {
		return income;
	}

	public void setCreateDate(Date createDate) {
		income.setCreateDate(createDate);
	}

	public void setAmount(float amount) {
		income.setAmount(amount);
	}

	public void setDescr(String descr) {
		income.setDescr(descr);
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
