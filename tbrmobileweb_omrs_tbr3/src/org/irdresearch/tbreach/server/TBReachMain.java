package org.irdresearch.tbreach.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.irdresearch.tbreach.shared.model.*;

public class TBReachMain
{
	public static void main(String[] args)
	{
		try
		{
			/*
			Object[][] data = HibernateUtil.util.selectData ("select PatientID, SputumTestID, date(DateTested), Month from tbreach_rpt.tmp_SputumResults where SputumTestID not in (select SAMPLE_BARCODE from Enc_SPUTUM_COL) order by PatientID ");
			int encounterId = 1;
			for (int i = 0; i < data.length; i++)
			{
				String pid1 = data[i][0].toString();
				String pid2 = "L-DUMMY-99";
				String date = (data[i][2] == null ? "" : data[i][2].toString());
				if (i > 0)
				{
					if (data[i][0].toString().equals(data[i - 1][0].toString()))
						encounterId++;
					else
						encounterId = 1;
				}
				// Object[] objs = HibernateUtil.util.selectObjects ("select EncounterID from Encounter where PID1 = '" + pid1 + "' and PID2 = '" + pid2 + "'");
				System.out.println ("insert into Encounter (EncounterID, PID1, PID2, EncounterType, LocationID, DateEncounterStart, DateEncounterEnd, Details, DateEncounterEntered) " +
												"values ('" + encounterId + "', '" + pid1 + "', '" + pid2 + "', 'SPUTUM_COL', 'L-INDUS', '" + date + "', '" + date + "', '', '" + date + "');");
				System.out.println ("insert into EncounterResults (EncounterID, PID1, PID2, Element, Value) " +
						"values ('" + encounterId + "', '" + pid1 + "', '" + pid2 + "', 'ENTERED_DATE', date('" + date + "'));");
				System.out.println ("insert into EncounterResults (EncounterID, PID1, PID2, Element, Value) " +
						"values ('" + encounterId + "', '" + pid1 + "', '" + pid2 + "', 'COLLECTION_MONTH', '" + data[i][3] + "');");
				System.out.println ("insert into EncounterResults (EncounterID, PID1, PID2, Element, Value) " +
						"values ('" + encounterId + "', '" + pid1 + "', '" + pid2 + "', 'PATIENT_STATUS', 'VERIFIED');");
				System.out.println ("insert into EncounterResults (EncounterID, PID1, PID2, Element, Value) " +
						"values ('" + encounterId + "', '" + pid1 + "', '" + pid2 + "', 'SPUTUM_COLLECTED', 'YES');");
				System.out.println ("insert into EncounterResults (EncounterID, PID1, PID2, Element, Value) " +
						"values ('" + encounterId + "', '" + pid1 + "', '" + pid2 + "', 'SAMPLE_BARCODE', '" + data[i][1] + "');");
				System.out.println ("insert into EncounterResults (EncounterID, PID1, PID2, Element, Value) " +
						"values ('" + encounterId + "', '" + pid1 + "', '" + pid2 + "', 'SUSPECT_SAMPLE', '-9');");
			}
			System.out.print ("Done!");
			*/

			// Object[][] data =
			// HibernateUtil.util.selectData("select EncounterID, PID1, PID2, EncounterType, DateEncounterEntered, Month from imp_EncounterResults order by PID1, PID2");
			// String pid1 = data[0][1].toString();
			// String pid2 = data[0][2].toString();
			// int counter = 1;
			// for (int i = 0; i < data.length; i++)
			// {
			// if (!(data[i][1].equals(pid1) && data[i][2].equals(pid2)))
			// counter = 1;
			// int max = Integer.parseInt(data[i][0].toString());
			// System.out.println("insert into imp_Encounter (EncounterID, PID1, PID2, EncounterType, DateEncounterEntered, MONTH) values (" + (max +
			// counter)
			// + ", '" + data[i][1] + "', '" + data[i][2] + "', '" + data[i][3] + "', '" + data[i][4] + "', '" + data[i][5]
			// +"');");
			// counter++;
			// pid1 = data[i][1].toString();
			// pid2 = data[i][2].toString();
			// }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void recoverData()
	{
		/* THIS PIECE OF CODE IS THE RESULT OF A BLUNDER THAT I MADE ON 16-Sep-2011 */
		String filePath = "D:\\Pick n Drop\\Forms-2011-16-09-1025\\";
		String[] files = {
		/*
		 * "BASELINE.csv", "CDF.csv", "CT_SUSPECT.csv", "DFR.csv", "DRUG_ADM.csv", "END_FOL.csv", "FOLLOW_UP.csv", "P_INFO.csv", "PAED_CONF.csv",
		 * "PAED_DIAG.csv", "SPUTUM_COL.csv", "SUSPECT_ID.csv", "TB_HISTORY.csv"
		 */
		};

		String[] header = { "EncounterID", "PID1", "PID2", "DateEncounterStart", "DateEncounterEnd", "DateEncounterEntered" };
		for (String s : files)
		{
			try
			{
				File file = new File(filePath + s);
				Scanner sc = new Scanner(file);
				String firstLine = sc.nextLine();
				header = firstLine.split(",");
				while (sc.hasNextLine())
				{
					String line = sc.nextLine();
					String append = "000";
					Date limit = new Date(2011 + 1900, 8, 11, 23, 59, 59);
					line = line.replace("\"", "");
					String[] parts = line.split(",");
					int id = Integer.parseInt(parts[findIndex(header, "EncounterID")]);
					String pid1 = parts[findIndex(header, "PID1")];
					if (!pid1.contains("-"))
						pid1 = append.substring(0, 11 - pid1.length()) + pid1;
					String pid2 = parts[findIndex(header, "PID2")];
					String encounterType = file.getName().substring(0, file.getName().indexOf('.'));
					String locationId = "";
					Date dateEncounterStart = null, dateEncounterEnd = null, dateEncounterEntered = null;
					String details = "";
					try
					{
						dateEncounterStart = parseDate(parts[findIndex(header, "DateEncounterStart")]);
					}
					catch (Exception e1)
					{
						System.out.println("Bad date");
					}
					try
					{
						dateEncounterEnd = parseDate(parts[findIndex(header, "DateEncounterEnd")]);
					}
					catch (Exception e1)
					{
						System.out.println("Bad date");
					}
					try
					{
						dateEncounterEntered = parseDate(parts[findIndex(header, "DateEncounterEntered")]);
					}
					catch (Exception e1)
					{
						System.out.println("Bad date");
					}

					EncounterId encounterId = new EncounterId(id, pid1, pid2);
					Encounter encounter = new Encounter(encounterId, encounterType, locationId, dateEncounterStart, dateEncounterEnd,
							dateEncounterEntered, details);

					/* IF DATE OF ENCOUNTER EXCEEDS THE LIMIT, THEN SKIP THE RECORD */
					if (encounter.getDateEncounterEntered() != null)
						if (encounter.getDateEncounterEntered().after(limit))
						{
							System.out.println("Skipping: " + encounter);
							continue;
						}

					// Encounter results
					ArrayList<EncounterResults> results = new ArrayList<EncounterResults>();
					for (int i = 8; i < parts.length; i++)
					{
						EncounterResultsId erId = new EncounterResultsId(encounter.getId().getEncounterId(), encounter.getId().getPid1(), encounter
								.getId().getPid2(), header[i]);
						EncounterResults r = new EncounterResults(erId, parts[i]);
						results.add(r);
					}

					try
					{
						ServerServiceImpl impl = new ServerServiceImpl();
						if (!impl.exists("Encounter", "EncounterId='" + encounter.getId().getEncounterId() + "' and PID1='"
								+ encounter.getId().getPid1() + "' and PID2='" + encounter.getId().getPid2() + "'"))
						{
							HibernateUtil.util.save(encounter);
							HibernateUtil.util.bulkSave(results.toArray());
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				System.out.println(s + " Done! Wait, I'm panting...");
				Thread.sleep(10000);
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static Date parseDate(String str)
	{
		try
		{
			String[] parts = str.split(" ");
			String[] dateParts = parts[0].split("/");
			int date, month, year, hour = 0, min = 0;
			date = Integer.parseInt(dateParts[0]);
			month = Integer.parseInt(dateParts[1]);
			year = Integer.parseInt(dateParts[2]);

			try
			{
				String[] timeParts = parts[1].split(":");
				hour = Integer.parseInt(timeParts[0]);
				min = Integer.parseInt(timeParts[1]);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			Date dt = new Date(year - 1900, month - 1, date, hour, min, 0);
			return dt;
		}
		catch (Exception e)
		{
			return null;
		}
	}

	public static int findIndex(String[] array, String str)
	{
		for (int i = 0; i < array.length; i++)
			if (array[i].equalsIgnoreCase(str))
				return i;
		return -1;
	}
}
