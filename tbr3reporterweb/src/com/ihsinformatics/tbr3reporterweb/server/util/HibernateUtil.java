/**
 * Implements hibernate features. This class must be initialized before performing any hibernate operation 
 */

package com.ihsinformatics.tbr3reporterweb.server.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.ihsinformatics.tbr3reporterweb.server.LogType;
import com.ihsinformatics.tbr3reporterweb.shared.TBR3;

/**
 * @author owais.hussain@ihsinformatics.com
 * 
 */
public class HibernateUtil implements Serializable {
	private static final long serialVersionUID = -2333198879612643152L;
	public static HibernateUtil util = new HibernateUtil();
	private static SessionFactory sessionFactory;
	private Class<?>[] classes;

	/**
	 * Default Constructor to initialize Session
	 */
	public HibernateUtil() {
		/* Use when trying Annotation */
		// sessionFactory = getInitializedConfiguration().buildSessionFactory();
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
			classes = getClasses(TBR3.packageName + ".shared.model");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a list of classes in the Hibernate model
	 * 
	 * @return
	 */
	public Class<?>[] getModelClasses() {
		return classes;
	}

	/**
	 * Create a query object from query string
	 * 
	 * @param queryString
	 * @return
	 */
	public Query create(String query) {
		return getSession().createQuery(query);
	}

	/**
	 * Get record count for given query
	 * 
	 * @return
	 */
	public long count(String query) {
		return count(create(query));
	}

	/**
	 * Get record count for given query
	 * 
	 * @return
	 */
	public long count(Query query) {
		return Long.parseLong(query.uniqueResult().toString());
	}

	/**
	 * Overloaded method for findObject(String)
	 * 
	 * @param query
	 * @return
	 */
	public Object findObject(String query) {
		Object[] objects = findObjects(query);
		if (objects == null)
			return null;
		if (objects.length == 0)
			return null;
		return objects[0];
	}

	/**
	 * Get a list of objects from given query.
	 * 
	 * @param query
	 * @return
	 */
	public Object[] findObjects(String query) {
		Session session = getSession();
		try {
			Query q = session.createQuery(query);
			List<?> list = q.list();
			return list.toArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	/**
	 * Get a list of objects from given SQL query
	 * 
	 * @param SQLQuery
	 * @return
	 */
	public Object selectObject(String SqlQuery) {
		return selectObjects(SqlQuery)[0];
	}

	/**
	 * Get a list of objects from given SQL query
	 * 
	 * @param SQLQuery
	 * @return
	 */
	public Object[] selectObjects(String SqlQuery) {
		Session session = getSession();
		try {
			SQLQuery q = session.createSQLQuery(SqlQuery);
			List<?> list = q.list();
			return list.toArray();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	/**
	 * Get a list of objects from given SQL query
	 * 
	 * @param SQLQuery
	 * @return
	 */
	public Object[][] selectData(String SqlQuery) {
		Session session = getSession();
		try {
			SQLQuery q = session.createSQLQuery(SqlQuery);
			List<?> list = q.list();
			Object[][] tableData = new Object[list.size()][];
			int cnt = 0;
			for (ListIterator<?> iter = list.listIterator(); iter.hasNext();) {
				Object[] array = (Object[]) iter.next();
				tableData[cnt++] = array;
			}
			return tableData;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			session.close();
		}
	}

	/**
	 * Update an object present in the database
	 * 
	 * @param obj
	 * @return
	 */
	public boolean update(Object obj) {
		Session session = getSession();
		try {
			session.update(obj);
			session.flush();
			session.close();
			recordLog(LogType.UPDATE, obj);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}

	/**
	 * Saves an object into database
	 * 
	 * @param obj
	 * @return
	 */
	public boolean bulkSave(Object[] objects) {
		Session session = getSession();
		try {
			for (Object o : objects)
				session.save(o);
			session.flush();
			session.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}

	public boolean save(Object obj) {
		Session session = getSession();
		try {
			session.save(obj);
			session.flush();
			session.close();
			recordLog(LogType.INSERT, obj);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}

	/**
	 * Deletes an Object from database
	 * 
	 * @param obj
	 * @return
	 */
	public boolean delete(Object obj) {
		Session session = getSession();
		try {
			session.delete(obj);
			session.flush();
			session.close();
			recordLog(LogType.DELETE, obj);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}

	/**
	 * Records an object into one of the Log tables
	 * 
	 * @param logType
	 *            determine type of log (Delete Log, Change Log, etc.)
	 * @param obj
	 *            actual object to be logged
	 */
	public void recordLog(LogType logType, Object obj) {
		// Not implemented
	}

	/**
	 * Executes a DML command
	 * 
	 * @param command
	 *            SQL statement as command
	 * @return number of records affected
	 * @throws Exception 
	 */
	public int runCommand(String command) throws Exception {
		Session session = getSession();
		try {
			Transaction transaction = session.beginTransaction();
			SQLQuery q = session.createSQLQuery(command);
			int results = q.executeUpdate();
			transaction.commit();
			return results;
		} catch (HibernateException e) {
			throw new Exception(e.getMessage());
		} finally {
			session.close();
		}
	}

	/**
	 * Executes a Stored Procedure
	 * 
	 * @param procedure
	 *            SQL statement as procedure
	 * @return number of records affected
	 */
	@SuppressWarnings("deprecation")
	public boolean runProcedure(String procedure) {
		Session session = getSession();
		try {
			session.beginTransaction();
			CallableStatement callableStatement = session.connection()
					.prepareCall(procedure);
			System.out.println("Executing stored procedure: " + procedure);
			callableStatement.execute();
			session.getTransaction().commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			session.close();
		}
	}

	/**
	 * @return the session
	 */
	public Session getSession() {
		return sessionFactory.openSession();
	}

	/**
	 * Returns the respective Database class to which an object belongs
	 * 
	 * @param obj
	 *            object to be identified
	 * @return Class object of class 'Class'
	 */
	public Class<?> identifyClass(Object obj) {
		return obj.getClass();
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and sub packages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class<?>[] getClasses(String packageName)
			throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and sub
	 * directories.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class<?>> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return classes;
	}

}
