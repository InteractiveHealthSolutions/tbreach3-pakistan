package com.ihsinformatics.tbreach3.tbr3datawarehouse;

import java.io.File;
import java.util.logging.Logger;

import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.CommandType;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.DatabaseUtil;
import com.ihsinformatics.tbreach3.tbr3datawarehouse.util.FileUtil;

/**
 * ILMS database processing class for TBR3 warehouse
 * 
 * @author daniyal.idrees@ihsinformatics.com
 *
 */

public class IlmsProcessor extends AbstractProcessor{
	
	private static final Logger log = Logger.getLogger(Class.class.getName());
	private static final String schemaName = "ilms";
	private String scriptFilePath;
	private DatabaseUtil dwDb;
	private DatabaseUtil ilmsDb;

	String[] sourceTables = {}; //tables to be added 
	
	/**
	 * Constructor to initialize the object
	 * 
	 * @param scriptFilePath
	 *            that contains SQL statements to execute for Field Monitoring
	 *            DB
	 * @param ilmsDb
	 *            connection to Field Monitoring DB
	 * @param dwDb
	 *            connection to Data warehouse
	 */
	public IlmsProcessor(String scriptFilePath, DatabaseUtil ilmsDb,
			DatabaseUtil dwDb) {
		this.scriptFilePath = scriptFilePath;
		this.ilmsDb = ilmsDb;
		this.dwDb = dwDb;
		Object obj = ilmsDb.runCommand(CommandType.SELECT,
				"select count(*) from user");
		if (obj == null)
			log.warning("Unable to connect with ILMS!");
		else
			log.info("ILMS connection OK");
	}
	
	
	// Create OpenMRS DB schema into DW
		public boolean createSchema(boolean fromScratch) {
			FileUtil fileUtil = new FileUtil();
			String[] queries = fileUtil.getLines(scriptFilePath);
			// Recreate tables
			for (String query : queries) {
				if (query.toUpperCase().startsWith("DROP")) {
					dwDb.runCommand(CommandType.DROP, query);
				} else {
					dwDb.runCommand(CommandType.CREATE, query);
				}
			}
			return true;
		}
		
		/**
		 * Extracts data from OpenMRS connection and stores as CSV files
		 * 
		 * @param dataPath
		 *            where CSV files will be stored
		 * @return
		 */
		public boolean extract(String dataPath) {
			log.info("Importing data from source into raw files");
			// Fetch file from source and generate CSVs
			for (String table : sourceTables) {
				String fileName = dataPath.replace("\\", "\\\\") + schemaName + "_"
						+ table + ".csv";
				File file = new File(fileName);
				if (file.exists()) {
					file.delete();
				}
				String query = "SELECT * FROM "
						+ table
						+ " INTO OUTFILE '"
						+ fileName
						+ "' FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n'";
				Object obj = ilmsDb.runCommand(CommandType.EXECUTE, query);
				if (obj == null) {
					log.warning("No data was exported to CSV for table: " + table);
				}
			}
			return true;
		}
		
		/**
		 * Loads table data from CSV files into Data warehouse
		 * 
		 * @param dataPath
		 *            where CSV files will be loaded from
		 * @return
		 */
		public boolean load(String dataPath) {
			boolean noImport = true;
			log.info("Importing data from raw files into data warehouse");
			for (String table : sourceTables) {
				String filePath = dataPath.replace("\\", "\\\\") + schemaName + "_"
						+ table + ".csv";
				File file = new File(filePath);
				if (!file.exists()) {
					log.warning("No CSV file exists for table " + table);
					continue;
				}
				String query = "LOAD DATA INFILE '"
						+ filePath
						+ "' INTO TABLE "
						+ "om_"
						+ table
						+ " FIELDS TERMINATED BY ',' ENCLOSED BY '\"' LINES TERMINATED BY '\n'";
				Object obj = dwDb.runCommand(CommandType.EXECUTE, query);
				if (obj == null) {
					log.warning("No data was from CSV for table: " + table);
				} else {
					noImport = false;
				}
				// Try to delete the CSV
				try {
					file.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return !noImport;
		}
		
		/**
		 * Denormalize and standardize tables according to the warehouse
		 */
		boolean transform() {
			
			//TODO
			
			return false;
		}
		
		
}
