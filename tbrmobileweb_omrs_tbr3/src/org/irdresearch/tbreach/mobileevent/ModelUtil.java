package org.irdresearch.tbreach.mobileevent;

import org.irdresearch.tbreach.server.ServerServiceImpl;
import org.irdresearch.tbreach.shared.model.Encounter;
import org.irdresearch.tbreach.shared.model.EncounterResults;
import org.irdresearch.tbreach.shared.model.EncounterResultsId;
import org.irdresearch.tbreach.shared.model.Patient;


public class ModelUtil {
	
	public static EncounterResults createEncounterResult(Encounter e, String element, String value) {
		EncounterResultsId erId = new EncounterResultsId(e.getId().getEncounterId(), e.getId().getPid1(), e.getId().getPid2(), element);
		EncounterResults er =  new EncounterResults(erId, value);
		
		return er;
	}
	
	public static Boolean isValidGPID(String gpid) {
		Boolean result = false;
		
		ServerServiceImpl ssl = new ServerServiceImpl();
		try {
			result = ssl.exists("GP", "where GPID='" + gpid.toUpperCase() + "'");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return result;
	}

	public static Boolean isValidCHWID(String chwid) {
		Boolean result = false;
		
		ServerServiceImpl ssl = new ServerServiceImpl();
		try {
			result = ssl.exists("Worker", "where WorkerID='" + chwid.toUpperCase() + "'");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return result;
	}
	
	public static Boolean isPatientClosed(String pid) throws Exception {
		
		ServerServiceImpl ssl = new ServerServiceImpl();
		Boolean result = false;
		Patient p = null;
		
		
		p = ssl.findPatient(pid);
		
		
		return new Boolean(p.getPatientStatus().equals("CLOSED"));
	}
}
