/*
Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. */
package com.ihsinformatics.tbreach3.tbr3datawarehouse;

import java.sql.Connection;
import java.util.Date;
import java.util.logging.Logger;

import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.DateTimeUtil;

/**
 * Data warehousing process for TBR3 Sehatmand Zindagi
 * 
 * @author owais.hussain@ihsinformatics.com
 */
public final class DataWarehouseMain {

	private static final Logger log = Logger.getLogger("DataWarehouse");
	public static final String directoryPath = "c:\\Users\\Owais\\git\\tbreach3-pakistan\\tbr3datawarehouse\\DataDump";
	public static final String filePath = directoryPath + new Date().getTime()
			+ ".sql";
	public static Connection conn;

	/**
	 * Main executable. Arguments need to be provided as: -R to hard reset
	 * warehouse (Extract > Load > Transform > Dimensional modeling > Nightly
	 * process) -l to extract/load data from various sources (stage1) -t to
	 * transform schema from (stage2) -d to create dimension tables (data
	 * warehouse) -f to create fact tables -u to update data warehouse (nightly
	 * run)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0 || args[0] == null) {
			System.out
					.println("Arguments are invalid. Arguments must be provided as:\n"
							+ "-R to hard reset warehouse (Extract/Load > Transform > Dimensional modeling > Fact tables)\n"
							+ "-l to extract/load data from various sources (stage1)\n"
							+ "-t to transform schema from (stage2)\n"
							+ "-d to create dimension tables (data warehouse)\n"
							+ "-f to create fact tables\n"
							+ "-u to update data warehouse (nightly run)\n");
			return;
		}
		DataWarehouseMain dw = new DataWarehouseMain();
		if (dw.hasSwitch(args, "R")) {
			dw.resetDataWarehouse();
			return;
		}
		if (dw.hasSwitch(args, "l")) {
			dw.extractLoad();
		}
		if (dw.hasSwitch(args, "t")) {
			dw.transform();
		}
		if (dw.hasSwitch(args, "d")) {
			dw.createDimensions();
		}
		if (dw.hasSwitch(args, "f")) {
			dw.createFacts();
		}
		if (dw.hasSwitch(args, "u")) {
			dw.updateWarehosue();
		}
	}

	public boolean hasSwitch(String[] args, String switchChar) {
		for (String s : args) {
			if (s.equals("-" + switchChar)) {
				return true;
			}
		}
		return false;
	}

	public void resetDataWarehouse() {
		log.info("Starting DW hard reset");
		extractLoad();
		transform();
		createDimensions();
		createFacts();
		updateWarehosue();
		log.info("Finished DW hard reset");
	}

	public void extractLoad() {
		log.info("Starting ETL");
		// Create OpenMRS DB clone into DW
		// Create Field Monitoring DB clone into DW
		log.info("Finished ETL");
	}

	public void transform() {
		log.info("Starting data transformation");
		log.info("Finished data transformation");
	}

	public void createDimensions() {
		log.info("Starting dimension modeling");
		log.info("Finished dimension modeling");
	}

	public void createFacts() {
		log.info("Starting fact tables");
		log.info("Finished fact tables");
	}

	public void updateWarehosue() {
		log.info("Starting DW update");
		log.info("Finished DW update");
	}
}
