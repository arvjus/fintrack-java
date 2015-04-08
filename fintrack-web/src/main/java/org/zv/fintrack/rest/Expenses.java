package org.zv.fintrack.rest;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;

import org.zv.fintrack.pd.Expense;
import org.zv.fintrack.pd.Category;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.ejb.api.ExpenseDao;
import org.zv.fintrack.ejb.api.CategoryDao;
import org.zv.fintrack.util.JndiUtil;

@Path("/expenses")
public class Expenses {
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@POST
	@Path("/new")
	@Produces({"application/xml", "application/json"})
    public Expense addNew(@FormParam("createDate") String createDate, @FormParam("userId") String userId, @FormParam("categoryId") String categoryId, @FormParam("amount") Float amount, @FormParam("descr") String descr) throws ParseException {
		ExpenseDao expenseDao = (ExpenseDao)JndiUtil.lookup("ejb/ExpenseDao");
		Expense expense = new Expense();
		expense.setCreateDate(simpleDateFormat.parse(createDate));
		expense.setUserId(userId);
		expense.setCategoryId(categoryId);
		expense.setAmount(amount);
		expense.setDescr(descr);
		expense = expenseDao.save(expense);
		CategoryDao categoryDao = (CategoryDao)JndiUtil.lookup("ejb/CategoryDao");
		Category category = categoryDao.getById(categoryId);
		expense.setCategory(category);
		return expense;
    }
	
	@GET
	@Path("/latest-{max}")
	@Produces({"application/xml", "application/json"})
    public List<Expense> getLatestExpenses(@PathParam("max") int max) {
		ExpenseDao expenseDao = (ExpenseDao)JndiUtil.lookup("ejb/ExpenseDao");
		return expenseDao.getAll(max);
    }
	
	@POST
	@Path("/list")
	@Produces({"application/xml", "application/json"})
    public List<Expense> getExpenses(@FormParam("dateFrom") String dateFrom, @FormParam("dateTo") String dateTo, @FormParam("userId") String userId, @FormParam("categoryIds") List<String> categoryIds) throws ParseException {
		ExpenseDao expenseDao = (ExpenseDao)JndiUtil.lookup("ejb/ExpenseDao");
		return expenseDao.getPlain(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo), userId, categoryIds.toArray());
    }

	@POST
	@Path("/summary/bycategory")
	@Produces({"application/xml", "application/json"})
    public List<SummaryBean> getSummaryByCategory(@FormParam("dateFrom") String dateFrom, @FormParam("dateTo") String dateTo, @FormParam("categoryIds") List<String> categoryIds) throws ParseException {
		ExpenseDao expenseDao = (ExpenseDao)JndiUtil.lookup("ejb/ExpenseDao");
		return expenseDao.getSummaryByCategory(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo), categoryIds.toArray());
    }
	
	@POST
	@Path("/summary/bymonth")
	@Produces({"application/xml", "application/json"})
    public List<SummaryBean> getSummaryByMonth(@FormParam("dateFrom") String dateFrom, @FormParam("dateTo") String dateTo, @FormParam("categoryIds") List<String> categoryIds) throws ParseException {
		ExpenseDao expenseDao = (ExpenseDao)JndiUtil.lookup("ejb/ExpenseDao");
		return expenseDao.getSummaryByMonth(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo), categoryIds.toArray());
    }
}
