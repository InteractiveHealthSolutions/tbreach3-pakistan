/* Copyright(C) 2014 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
*/
package com.ihsinformatics.tbr3.treatmentregister;

/**
 * Constants for file interactions
 * @author ali.habib@irdresearch.org
 */
public class FileConstants {
	
	public static String TBR3_DIR = System.getProperty("user.home") + System.getProperty("file.separator") + "xpertsms";
	
	public static String FILE_PATH = TBR3_DIR + System.getProperty("file.separator") + "tbr3treatment.properties";
	
	public static String FILE_ENTRY_DATE_FORMAT = "yyyy-MM-dd_hh.mm.ss";
	
	public static String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";
}
