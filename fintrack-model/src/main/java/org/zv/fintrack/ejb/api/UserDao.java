package org.zv.fintrack.ejb.api;

import java.util.List;

import javax.ejb.Local;

import org.zv.fintrack.pd.User;
import org.zv.fintrack.pd.Role;

@Local
public interface UserDao {
	List<User> findAll();
	User find(String userId);
	void persist(User user);
	void remove(User user);
	void grantRoles(User user, List<Role> roles);
}
