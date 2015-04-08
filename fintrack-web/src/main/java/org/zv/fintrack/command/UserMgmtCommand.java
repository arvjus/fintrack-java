package org.zv.fintrack.command;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.zv.common.mvc.CommandSupport;
import org.zv.common.mvc.Resource;
import org.zv.fintrack.pd.User;
import org.zv.fintrack.pd.Role;
import org.zv.fintrack.util.FintrackUtils;
import org.zv.fintrack.ejb.api.UserDao;

/**
 * Handle logout request and redirect to user management page.
 * 
 * @author Arvid Juskaitis
 */
@SuppressWarnings("serial")
public class UserMgmtCommand extends CommandSupport {

	/**
	 * Session bean, injected by MVC.
	 */
	private UserDao userDao;
	
	@Resource(name = "ejb/UserDao")
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * All available roles.
	 */
	static private List<Role> roles = new LinkedList<Role>();
	{
		roles.clear();
		roles.add(Role.viewer);
		roles.add(Role.reporter);
		roles.add(Role.admin);
	}
	
	/**
	 * Keep state of role selections (for setter).
	 */
	private String [] selectedRoles;
	
	/**
	 * Keep state of category selections (for getter).
	 */
	private Map<String, Boolean> selectedRoleMap; 
	
	/**
	 * Selected user id for editing, deleting.
	 */
	private String preinitId;

	/**
	 * Selected user for editing, deleting.
	 */
	private User user;


	public List<Role> getRoles() {
		return roles;
	}
	
	public void setSelectedRoles(String[] selectedRoles) {
		this.selectedRoles = selectedRoles;
	}

	public Map<String, Boolean> getSelectedRoleMap() {
		return selectedRoleMap;
	}
	
	public void setPreinitId(String preinitId) {
		this.preinitId = preinitId;
	}

	public String getPreinitId() {
		return preinitId;
	}

	public User getUser() {
		return user;
	}


	/**
	 * List recently added records.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		if (preinitId != null) {
			Object obj = userDao.find(preinitId);
			System.out.println("object: " + obj + ", class: " + obj.getClass().getName());
			//selectedRoles = new String [user.getUserRoles().size()];
		}
		selectedRoleMap = (Map<String, Boolean>) FintrackUtils.arrayToMap(selectedRoles, Boolean.TRUE);
		
		// Retrieve data and put it to request context
		request.setAttribute("users", userDao.findAll());
	
		// Forward to the view
		return "user-mgmt";
	}
	
	/**
	 * Create a new user.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String create(HttpServletRequest request, HttpServletResponse response) {
		// Retrieve data and put it to request context
		request.setAttribute("users", userDao.findAll());
	
		// Forward to the view
		return "user-mgmt";
	}

	/**
	 * Update selected user.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String update(HttpServletRequest request, HttpServletResponse response) {
		// Retrieve data and put it to request context
		request.setAttribute("users", userDao.findAll());
	
		// Forward to the view
		return "user-mgmt";
	}
	/**
	 * Delete selected user.
	 *  
	 * @param request
	 * @param response
	 * @return view to forward
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		// Retrieve data and put it to request context
		request.setAttribute("users", userDao.findAll());
	
		// Forward to the view
		return "user-mgmt";
	}
}
