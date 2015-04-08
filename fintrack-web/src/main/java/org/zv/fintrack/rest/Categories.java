package org.zv.fintrack.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.zv.fintrack.ejb.api.CategoryDao;
import org.zv.fintrack.pd.Category;
import org.zv.fintrack.util.JndiUtil;

@Path("/categories")
public class Categories {
	@GET
	@Produces({"application/xml", "application/json"})
    public List<Category> getCategories() {
		CategoryDao categoryDao = (CategoryDao)JndiUtil.lookup("ejb/CategoryDao");
		return categoryDao.getAll();
    }
}
