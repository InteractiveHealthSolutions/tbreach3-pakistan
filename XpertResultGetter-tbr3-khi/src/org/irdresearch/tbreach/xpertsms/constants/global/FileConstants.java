package org.irdresearch.tbreach.xpertsms.constants.global;
import java.text.SimpleDateFormat;

public class FileConstants {
	public static String XPERT_SMS_DIR = System.getProperty("user.home") + System.getProperty("file.separator") + "xpertsms_data";
	public static String FILE_PATH = XPERT_SMS_DIR + System.getProperty("file.separator") + "xpertsms.properties";
	public static String FILE_ENTRY_DATE_FORMAT = "yyyy-MM-dd_hh.mm.ss";
	public static String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";
}
