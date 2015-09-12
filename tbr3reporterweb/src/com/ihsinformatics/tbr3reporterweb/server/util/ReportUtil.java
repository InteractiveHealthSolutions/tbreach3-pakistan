/**
 * Implements reporting features. 
 */
package com.ihsinformatics.tbr3reporterweb.server.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import com.ihsinformatics.tbr3reporterweb.shared.Parameter;

/**
 * @author owais.hussain@ihsinformatics.com
 * 
 */
public class ReportUtil {
	private static String resourcesPath;
	private static String reportsPath;
	public static final char separatorChar = File.separatorChar;
	public static final String[] allowedExtensions = {"jrxml", "doc", "docx",
			"xls", "xlsx", "rar", "zip"};

	/**
	 * 
	 */
	public ReportUtil(String resourcesPath) {
		if (!resourcesPath.endsWith(String.valueOf(separatorChar))) {
			resourcesPath += separatorChar;
		}
		ReportUtil.resourcesPath = resourcesPath;
		ReportUtil.reportsPath = resourcesPath + "rpt" + separatorChar;
	}

	@SuppressWarnings("deprecation")
	public String generateCSVfromQuery(String query, char separator) {
		try {
			Connection con = HibernateUtil.util.getSession().connection();
			con.setCatalog("xpertsms");
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery(query);
			ArrayList<String> list = new ArrayList<String>();
			int range = result.getMetaData().getColumnCount();
			String record = "";
			for (int i = 0; i < range; i++)
				record += result.getMetaData().getColumnName(i + 1) + separator;
			list.add(record.substring(0, record.length() - 1));
			while (result.next()) {
				record = "";
				for (int i = 0; i < range; i++)
					record += result.getString(i + 1) + separator;
				if (record.length() > 0)
					list.add(record.substring(0, record.length() - 1));
			}
			String dest = resourcesPath + String.valueOf(new Date().getTime())
					+ ".csv";
			StringBuilder text = new StringBuilder();
			for (int i = 0; i < list.size(); i++)
				text.append(list.get(i) + "\r\n");
			// Delete file if existing
			try {
				File file = new File(dest);
				file.delete();
				Writer output = null;
				output = new BufferedWriter(new FileWriter(file));
				output.write(text.toString());
				output.flush();
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return dest.substring(dest.lastIndexOf(File.separatorChar) + 1);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings({"deprecation"})
	public String generateReportFromQuery(String fileName, String query,
			Parameter[] params, boolean export) {
		try {
			Connection con = HibernateUtil.util.getSession().connection();
			con.setCatalog("xpertsms");
			Statement statement = con.createStatement();
			ResultSet result = statement.executeQuery(query);
			JRResultSetDataSource resultSource = new JRResultSetDataSource(
					result);
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < params.length; i++) {
				/**
				 * Cast the parameter into appropriate type
				 */
				map.put(params[i].getName(), toObject(params[i]));
			}
			JasperReport jasperReport = JasperCompileManager
					.compileReport(reportsPath + fileName
							+ (fileName.endsWith(".jrxml") ? "" : ".jrxml"));
			JasperPrint print = JasperFillManager.fillReport(jasperReport, map,
					resultSource);
			String dest = resourcesPath + String.valueOf(new Date().getTime())
					+ (export == true ? ".csv" : ".pdf");
			// Delete file if existing
			try {
				File file = new File(dest);
				file.delete();
			} catch (Exception e) {
				// Not implemented
			}
			JRAbstractExporter exporter;
			if (export)
				exporter = new JRCsvExporter();
			else
				exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(
					dest));
			exporter.exportReport();
			System.out.println(dest.substring(dest
					.lastIndexOf(File.separatorChar) + 1));
			return dest.substring(dest.lastIndexOf(File.separatorChar) + 1);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	@SuppressWarnings("deprecation")
	public String generateReport(String fileName, Parameter[] params,
			boolean export) {
		try {
			Connection con = HibernateUtil.util.getSession().connection();
			con.setCatalog("xpertsms");
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < params.length; i++) {
				/**
				 * Cast the parameter into appropriate type
				 */
				map.put(params[i].getName(), toObject(params[i]));
			}
			JasperReport jasperReport = JasperCompileManager
					.compileReport(reportsPath + fileName
							+ (fileName.endsWith(".jrxml") ? "" : ".jrxml"));
			JasperPrint print = JasperFillManager.fillReport(jasperReport, map,
					con);
			String dest = resourcesPath + String.valueOf(new Date().getTime())
					+ (export == true ? ".csv" : ".pdf");
			// Delete file if existing
			try {
				File file = new File(dest);
				file.delete();
			} catch (Exception e) {
				// Not implemented
			}
			JRAbstractExporter exporter;
			if (export)
				exporter = new JRCsvExporter();
			else
				exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(
					dest));
			exporter.exportReport();
			System.out.println(dest.substring(dest
					.lastIndexOf(File.separatorChar) + 1));
			return dest.substring(dest.lastIndexOf(File.separatorChar) + 1);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Get list of Reports in reports directory with last modified date
	 * 
	 * @return String[][]
	 */
	public String[][] getReportList() {
		String[][] reports;
		File dir = new File(reportsPath);
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				for (String s : allowedExtensions)
					if (name.endsWith(s))
						return true;
				return false;
			}
		};
		File[] files = dir.listFiles(filter);
		reports = new String[files.length][4];
		for (int i = 0; i < files.length; i++) {
			reports[i][0] = String.valueOf(i);
			reports[i][1] = files[i].getName();

			Date date = new Date(files[i].lastModified());
			SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			reports[i][2] = format.format(date);
			try {
				String query = "";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return reports;
	}

	public Object toObject(Parameter param) {
		try {
			String value = param.getValue();
			switch (param.getType()) {
				case BOOLEAN :
					return Boolean.parseBoolean(value);
				case BYTE :
					return Byte.parseByte(value);
				case CHAR :
					return value.charAt(0);
				case DATE :
					return new Date(Long.parseLong(value));
				case DOUBLE :
					return Double.parseDouble(value);
				case FLOAT :
					return Float.parseFloat(value);
				case INT :
					return Integer.parseInt(value);
				case LONG :
					return Long.parseLong(value);
				case SHORT :
					return Short.parseShort(value);
				case STRING :
					return value;
				default :
					return null;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}
}
