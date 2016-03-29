/**
 * Copyright(C) 2016 Interactive Health Solutions, Pvt. Ltd.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. 
 * 
 */

package com.ihsinformatics.tbr3synchronizer.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * queries for selecting data created or changed 'n' days ago
 * 
 * @author Hera Rafique
 *
 */

public class QueryStringLocal {

	private Date startDate;
	private Date endDate;

	private static final long MILLIS_PER_DAY = 86400000;
	// TODO "yyyy-MM-dd"
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd hh:mm:ss");

	QueryStringLocal() {
		setStartDate(new Date());
		setEndDate(new Date(startDate.getTime() - 0 * MILLIS_PER_DAY));
	}

	public String getDataCreatedChangedNdaysAgo(String table, int days) {

		setEndDate(new Date(startDate.getTime() - days * MILLIS_PER_DAY));

		String query = "select * from " + table + " where (date_created <= '"
				+ simpleDateFormat.format(startDate)
				+ "' and date_created >= '" + simpleDateFormat.format(endDate)
				+ "' )" + " or ( date_changed <= '"
				+ simpleDateFormat.format(startDate)
				+ "' and date_changed >= '" + simpleDateFormat.format(endDate)
				+ "' )";

		return query;
	}

	public String getDataCreatedNdaysAgo(String table, int days) {

		setEndDate(new Date(startDate.getTime() - days * MILLIS_PER_DAY));

		String query = "select * from " + table + " where (date_created <= '"
				+ simpleDateFormat.format(startDate)
				+ "' and date_created >= '" + simpleDateFormat.format(endDate)
				+ "' )";

		return query;
	}

	public String getDataChangedNdaysAgo(String table, int days) {

		setEndDate(new Date(startDate.getTime() - days * MILLIS_PER_DAY));

		String query = "select * from " + table + " where ( date_changed <= '"
				+ simpleDateFormat.format(startDate)
				+ "' and date_changed >= '" + simpleDateFormat.format(endDate)
				+ "' )";

		return query;
	}

	public String getData(String table) {

		String query = "select * from " + table;

		return query;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date start_date) {
		this.startDate = start_date;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date end_date) {
		this.endDate = end_date;
	}

}
