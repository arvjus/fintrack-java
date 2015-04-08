package org.zv.fintrack.ejb;

import java.util.List;

import javax.naming.NamingException;

import org.apache.commons.logging.Log;
import org.zv.fintrack.pd.User;
import org.zv.fintrack.support.SetUpDataTestCaseSupport;
import org.zv.fintrack.util.LoggerFactory;
import org.zv.fintrack.ejb.api.UserDao;

public class TestUserDao extends SetUpDataTestCaseSupport {
	
	/**
	 * Class logger.
	 */
	private Log log = LoggerFactory.make();

	@Override
	protected String getDataName() {
		return "testUsers.xml";
	}

	@Override
	protected void tearDown() throws Exception {
		// we can skip cleanup operation
	}
	
	/**
	 * Test finding of users.
	 * 
	 * @throws Exception
	 */
	public void testFindUsers() throws Exception {
		log.info("findAll");
		List<User> users = getUserDao().findAll();
        assertEquals("invalid number of records", 2, users.size());

        log.info("find admin");
		User user = getUserDao().find("admin");
		assertNotNull("record was expected", user);
        assertEquals("wrong record", "admin123", user.getPasswd());

        log.info("find nonuser");
		user = getUserDao().find("nonuser");
		assertNull("record was not expected", user);
    }
	
	/**
	 * Test adding, removing of users.
	 * 
	 * @throws Exception
	 */
	public void testUpdateRemoveUsers() throws Exception {
		log.info("findAll");
		List<User> users = getUserDao().findAll();
        assertEquals("invalid number of records", 2, users.size());

        log.info("persist test");
        User user = new User();
        user.setUserId("test");
		getUserDao().persist(user);
		users = getUserDao().findAll();
        assertEquals("invalid number of records", 3, users.size());

        log.info("remove user");
		user = getUserDao().find("user");
		assertNotNull("record was expected", user);
		getUserDao().remove(user);
		users = getUserDao().findAll();
        assertEquals("invalid number of records", 2, users.size());
	}
	
	/**
	 * Get a new instance of UserDao 
	 * @throws NamingException
	 */
	private UserDao getUserDao() throws NamingException {
		return (UserDao)getContext().lookup("UserDaoBeanLocal");
	}
}
