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

import org.zv.fintrack.pd.Income;
import org.zv.fintrack.pd.bean.SummaryBean;
import org.zv.fintrack.ejb.api.IncomeDao;
import org.zv.fintrack.util.JndiUtil;

@Path("/incomes")
public class Incomes {
	private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@POST
	@Path("/new")
	@Produces({"application/xml", "application/json"})
    public Income addNew(@FormParam("createDate") String createDate, @FormParam("userId") String userId, @FormParam("amount") Float amount, @FormParam("descr") String descr) throws ParseException {
		IncomeDao incomeDao = (IncomeDao)JndiUtil.lookup("ejb/IncomeDao");
		Income income = new Income();
		income.setCreateDate(simpleDateFormat.parse(createDate));
		income.setUserId(userId);
		income.setAmount(amount);
		income.setDescr(descr);
		income = incomeDao.save(income);
		return income;
    }
	
	@GET
	@Path("/latest-{max}")
	@Produces({"application/xml", "application/json"})
    public List<Income> getLatest(@PathParam("max") int max) {
		IncomeDao incomeDao = (IncomeDao)JndiUtil.lookup("ejb/IncomeDao");
		return incomeDao.getAll(max);
    }
	
	@POST
	@Path("/list")
	@Produces({"application/xml", "application/json"})
    public List<Income> getList(@FormParam("dateFrom") String dateFrom, @FormParam("dateTo") String dateTo, @FormParam("userId") String userId) throws ParseException {
		IncomeDao incomeDao = (IncomeDao)JndiUtil.lookup("ejb/IncomeDao");
		return incomeDao.getPlain(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo), userId);
    }
	
	@POST
	@Path("/summary/simple")
	@Produces({"application/xml", "application/json"})
    public List<SummaryBean> getSummarySimple(@FormParam("dateFrom") String dateFrom, @FormParam("dateTo") String dateTo) throws ParseException {
		IncomeDao incomeDao = (IncomeDao)JndiUtil.lookup("ejb/IncomeDao");
		return incomeDao.getSummarySimple(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo));
    }
	
	@POST
	@Path("/summary/bymonth")
	@Produces({"application/xml", "application/json"})
    public List<SummaryBean> getSummaryMonth(@FormParam("dateFrom") String dateFrom, @FormParam("dateTo") String dateTo) throws ParseException {
		IncomeDao incomeDao = (IncomeDao)JndiUtil.lookup("ejb/IncomeDao");
		return incomeDao.getSummaryByMonth(simpleDateFormat.parse(dateFrom), simpleDateFormat.parse(dateTo));
    }
}
