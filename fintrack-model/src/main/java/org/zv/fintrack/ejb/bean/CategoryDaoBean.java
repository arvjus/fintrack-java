package org.zv.fintrack.ejb.bean;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.zv.fintrack.pd.Category;
import org.zv.fintrack.ejb.api.CategoryDao;

/**
 * DAO for Category entity.
 * 
 * @author arvid.juskaitis
 */
@Stateless
public class CategoryDaoBean implements CategoryDao {
	
	@PersistenceContext(name="fintrack")
	private EntityManager em;
	
	@PermitAll
	public List<Category> getAll() {
		return em.createQuery("SELECT c FROM Category AS c ORDER BY c.orderPos").getResultList();
	}

	@PermitAll
	public Category getById(String id) {
		return em.find(Category.class, id);
	}
}
