/**
 * This class contains constants and project-specific methods which will be used throughout the System
 */

package org.irdresearch.tbreach.shared;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public final class TBR
{
	private static final String resourcesPath 		= "/var/lib/tomcat6/webapps/tbreach/";
	//private static final String	resourcesPath	= "C:\\workspace\\TBR\\trunk\\TBReach\\war\\";
	private static String		currentUser			= "";
	private static String		passCode			= "";
	public static final String	hashingAlgorithm	= "SHA";
	public static final String	packageName			= "org.irdresearch.tbreach";
	public static final String	projectTitle		= "TB REACH";
	public static final char	separatorChar		= ',';
	public static final int		sessionLimit		= 15 * 60 * 1000;
	public static String[][]	lists;

	public static void fillLists (String[][] lists)
	{
		TBR.lists = lists;
	}

	/**
	 * Concatenate an Array of Strings into single String
	 * 
	 * @param array
	 * @return string
	 */
	public static String concatenateArray (String[] array)
	{
		StringBuilder concatenated = new StringBuilder ();
		for (String s : array)
		{
			concatenated.append (s);
			concatenated.append (TBR.separatorChar);
		}
		concatenated.deleteCharAt (concatenated.length () - 1); // Remove additional separator
		return concatenated.toString ();
	}

	/**
	 * Get a list of values which will be constant throughout the application
	 * 
	 * @param listType
	 * @return array
	 */
	public static String[] getList (ListType listType)
	{
		return lists[listType.ordinal ()];
	}

	/**
	 * Get secret question
	 * 
	 * @return array
	 */
	public static String[] getSecretQuestions ()
	{
		String[] questions = {"WHO IS YOUR FAVOURITE NATIONAL HERO?",
				"WHAT PHONE MODEL ARE YOU PLANNING TO PURCHASE NEXT?", "WHERE WAS YOUR MOTHER BORN?",
				"WHEN DID YOU BUY YOUR FIRST CAR?", "WHAT WAS YOUR CHILDHOOD NICKNAME?",
				"WHAT IS YOUR FAVOURITE CARTOON CHARACTER?"};
		return questions;
	}

	/**
	 * Get current User Name (saved in cookies on client-side)
	 * 
	 * @return currentUser
	 */
	public static String getCurrentUser ()
	{
		return currentUser;
	}

	/**
	 * Set current user
	 * 
	 * @param currentUser
	 */
	public static void setCurrentUser (String currentUser)
	{
		TBR.currentUser = currentUser.toUpperCase ();
	}

	/**
	 * Get pass code (first 4 characters of User's password)
	 * 
	 * @return passCode
	 */
	public static String getPassCode ()
	{
		return passCode;
	}

	/**
	 * Set pass code for current user
	 * 
	 * @param passCode
	 */
	public static void setPassCode (String passCode)
	{
		TBR.passCode = passCode;
	}

	/**
	 * @return the reportPath
	 */
	public static String getReportPath ()
	{
		return getResourcesPath () + "rpt/";
	}

	/**
	 * @return the staticFilePath
	 */
	public static String getStaticFilePath ()
	{
		return getResourcesPath () + "StaticData.xml";
	}

	/**
	 * @return the resourcesPath
	 */
	public static String getResourcesPath ()
	{
		return resourcesPath;
	}
}
