/**
 * Provides Authentication funcionality
 */

package com.ihsinformatics.tbr3.fieldmonitoring.server;

import com.ihsinformatics.tbr3.fieldmonitoring.server.util.HibernateUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.server.util.MDHashUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.User;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.UserRights;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class UserAuthentication
{
	public UserAuthentication ()
	{
		// Not implemented
	}

	/**
	 * Check if the user of the given name exists in the database
	 * 
	 * @param userName
	 * @return
	 */
	public static boolean userExsists (String userName)
	{
		try
		{
			boolean exists = HibernateUtil.util.count ("select count(*) from User where user_name = '" + userName.toUpperCase () + "'") > 0;
			return exists;
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}
		return false;
	}

	/**
	 * Checks if the user exists with given password in the database
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public static boolean validatePassword (String userName, String password)
	{
		try
		{
			String user = HibernateUtil.util.selectObject ("select password from user where user_name = '" + userName.toUpperCase () + "' and current_status='A'").toString ();
			if (MDHashUtil.match (password, user))
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}
		return false;
	}

	/**
	 * Confirm the secret answer for given user in the database
	 * 
	 * @param userName
	 * @param secretAnswer
	 * @return
	 */
	public static boolean validateSecretAnswer (String userName, String secretAnswer)
	{
		try
		{
			User user = (User) HibernateUtil.util.findObject ("from User where userName = '" + userName.toUpperCase () + "'");
			String ansString = user.getSecretAnswer ().toString ();
			if (MDHashUtil.match (secretAnswer, ansString))
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}
		return false;
	}

	/**
	 * Get access rights on a table for given user role
	 * 
	 * @param userRole
	 * @param tableName
	 * @return
	 */
	public static UserRights getRights (String userRole, String tableName)
	{
		return (UserRights) HibernateUtil.util.findObject ("from UserRights where userRole='" + userRole + "' and MenuName='" + tableName + "'");
	}
}
