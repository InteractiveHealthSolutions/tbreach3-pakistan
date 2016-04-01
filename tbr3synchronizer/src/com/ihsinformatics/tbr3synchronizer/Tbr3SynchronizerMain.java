/**
 * Copyright(C) 2016 Interactive Health Solutions, Pvt. Ltd.
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors. 
 */

package com.ihsinformatics.tbr3synchronizer;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ihsinformatics.tbr3synchronizer.util.Synchronize;

/**
 * 
 * Main class for TBR3 synchronization
 * 
 * @author Hera Rafique
 */

public class Tbr3SynchronizerMain {

	public static void main(String[] args) {
		Synchronize synchronize = new Synchronize();
		if (!synchronize.insertDataIntoTempTable()) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.format(new Date()) + " Error occured");
			return;
		}
		if (!synchronize.synchronize()) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.format(new Date()) + " Error occured");
			return;
		}
		if (!synchronize.dropTempTables()) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
					.format(new Date()) + " Error occured");
			return;
		}
		System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
				.format(new Date()) + " Synchronization process completed");

	}
}
