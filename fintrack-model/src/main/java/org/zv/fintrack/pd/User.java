package org.zv.fintrack.pd;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

/**
 * User entity.
 * 
 * @author arvid.juskaitis
 */
@SuppressWarnings("serial")
@Entity
@Table(schema = "public", name = "users")
public class User implements Serializable {

	@Id
	@Column(name = "user_id")
	private String	userId;

	private String	passwd;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<UserRole> userRoles;

	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}	
	
	public List<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (userRoles != null) {
			for (UserRole ur : userRoles) {
				if (sb.length() > 0) {
					sb.append(", ");
				}
				sb.append(ur.getRole());
			}			
		}
		return userId + ": " + sb.toString();
	}
}
