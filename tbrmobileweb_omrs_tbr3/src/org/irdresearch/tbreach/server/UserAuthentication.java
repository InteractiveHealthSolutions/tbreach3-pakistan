/**
 * Provides Authentication funcionality
 */

package org.irdresearch.tbreach.server;

import org.irdresearch.tbreach.shared.model.Users;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class UserAuthentication
{
	public UserAuthentication()
	{
		// Not implemented
	}

	public static boolean userExsists(String userName)
	{
		try
		{
			return HibernateUtil.util.count("select count(*) from Users where UserName = '" + userName.toUpperCase() + "'") > 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean validatePassword(String userName, String password)
	{
		try
		{
			// Users user = (Users) HibernateUtil.util.findObject("from Users where UserName = '" + userName.toUpperCase() + "'");
			// if(MDHashUtil.match(password, user.getPassword()))
			String user = HibernateUtil.util.selectObject("select Password from Users where UserName = '" + userName.toUpperCase() + "' and Status='ACTIVE'").toString();
			if (MDHashUtil.match(password, user))
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean validateSecretAnswer(String userName, String secretAnswer)
	{
		try
		{
			Users user = (Users) HibernateUtil.util.findObject("from Users where UserName = '" + userName.toUpperCase() + "'");
			if (MDHashUtil.match(secretAnswer, user.getSecretAnswer()))
				return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
