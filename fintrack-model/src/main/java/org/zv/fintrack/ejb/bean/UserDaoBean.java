package org.zv.fintrack.ejb.bean;

import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.DenyAll;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.zv.fintrack.ejb.api.UserDao;
import org.zv.fintrack.pd.User;
import org.zv.fintrack.pd.Role;
import org.zv.fintrack.pd.UserRole;

/**
 * DAO for Income entity.
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("unchecked")
@Stateless
@DeclareRoles("admin")
public class UserDaoBean implements UserDao {

    /**
     * Handle to the entity manager (injected).
     */
	@PersistenceContext(name="fintrack")
    private EntityManager em;

	/**
	 * Retrieve all items.
	 * @return a list.
	 */
    @RolesAllowed("admin")
	public List<User> findAll() {
		return em.createQuery("SELECT u FROM User AS u ORDER BY u.userId ASC").getResultList();
	}

    @RolesAllowed("admin")
	public User find(String userId) {
		List<User> users = em.createQuery("SELECT u FROM User AS u WHERE u.userId = :userId")
        	.setParameter("userId", userId)
        	.getResultList();
		if (users.size() == 0) {
			return null;
		}
		return users.get(0);
	}

    @RolesAllowed("admin")
	public void persist(User user) {
		em.merge(user);
	}

    @RolesAllowed("admin")
	public void remove(User user) {
    	User mergeduser = em.merge(user);
    	em.remove(mergeduser);
	}

    @DenyAll
	public void grantRoles(User user, List<Role> roles) {
        em.createQuery("DELETE FROM UserRole WHERE user = :user")
          .setParameter("user", user)
          .executeUpdate();
        
        for (Role role : roles) {
            UserRole ur = new UserRole();
            ur.setUser(user);
            ur.setRole(role);
            em.persist(ur);
        }
	}
}
