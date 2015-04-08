package org.zv.fintrack.ejb.api;

import java.util.List;
import javax.ejb.Local;

import org.zv.fintrack.pd.Category;

/**
 * DAO for Category entity.
 * 
 * @author arvid.juskaitis
 */
@Local
public interface CategoryDao {

	/**
	 * Retrieve all items.
	 * @return a list.
	 */
	List<Category> getAll();

	/**
	 * Get category by id.
	 *  
	 * @param id
	 * @return
	 */
	Category getById(String id);
}
