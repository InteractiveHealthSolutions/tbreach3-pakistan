package org.irdresearch.tbreach.mobileevent;

import org.irdresearch.tbreach.server.ServerServiceImpl;

public class ChartWalker {

	private String id;
	private boolean isSputumPositive;
	private boolean isDoubleSputumNegative;
	private boolean isTripleNotProduced;
	private ServerServiceImpl ssl;

	public ChartWalker(String id) {
		setId(id);
		isSputumPositive = false;
		isDoubleSputumNegative = false;
		isTripleNotProduced = false;
		ssl = new ServerServiceImpl();
	}
	
	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}
	
	private boolean isSputumPositive() throws Exception {
		boolean result = false;
		
		long count = ssl.count("SputumResults", " where PatientID='" + id + "' AND Month = 0 AND (SmearResult IS NOT NULL AND SmearResult NOT LIKE '%NEGATIVE%')");
		
		if(count>=1)
			result = true;
		else
			result = false;
		
		return result;
	}
	
	private boolean isDoubleNegativeSputum() throws Exception {
		boolean result = false;
		
		long count = ssl.count("SputumResults", " where PatientID='" + id + "' AND Month = 0 AND (SmearResult IS NOT NULL AND SmearResult LIKE '%NEGATIVE%')");
		
		if(count>=2)
			result = true;
		else
			result = false;
		
		return result;
	}
	
	private boolean isTripleNotProduced() throws Exception {
		boolean result = false;
		
		long count = 0;
		
		count = ssl.count("SputumResults", " where PatientID='" + id + "' AND Month=0 AND (Remarks LIKE '%VERIFIED%' OR Remarks IS NULL)");
		
		if(count!=0) {
			return false;
		}
		
		long rejectCount = 0;
		long noProduceCount = 0;
		
		rejectCount = ssl.count("SputumResults", " where PatientID='" + id  + "' AND Month=0 AND Remarks LIKE '%REJECTED%' AND Remarks IS NOT NULL");
		noProduceCount = ssl.count("EncounterResults", " where PatientID='" + id + "' AND Element='SPUTUM_COLLECTED' AND Value='NO, UNABLE TO GENERATE'");
		
		if(rejectCount + noProduceCount >= 3) {
			return true;
		}
		
		return false;
	}
	
	private boolean isTBDrugHistory() throws Exception {
		boolean result = false;
		
		String[] er = null;
		
		er = ssl.getColumnData("EncounterResults", "Value", " where PID1='"+ id + "' AND Element='PAST_TB_DRUG_HISTORY'");
		
		String tbhist = er[0];
		
		if(tbhist.equals("YES"))
			result = true;
		else
			result = false;
		
		return result;
	}
	
	private String getGenXpertResult() throws Exception {
		String result = null;
		
		String[] gxpResults=null;
		
		gxpResults = ssl.getColumnData("GeneXpertResults", "IsPositive"," where PatientID='" + id + "' AND DateTested IS NOT NULL ORDER BY DateTested DESC");
		
		if(gxpResults.length==0) {
			System.out.println("null");
			return null;
		}
		
		else if(gxpResults[0].equals("true")) {
			
			System.out.println("+ve ------>" + gxpResults[0]);
			return "POSITIVE";
		}
		else if(gxpResults[0].equals("false")){
			System.out.println("-ve ------>" + gxpResults[0]);
			return "NEGATIVE";
		}
		System.out.println("default ------>" + gxpResults[0]);
		return result;
	}
	
	private String getXrayResult() throws Exception {
		String result = null;
		
		String[] xRayResult = null;
		
		
		xRayResult = ssl.getColumnData("XRayResults", "XRayResults", " where PatientId='"+ id + "' AND DateReported IS NOT NULL ORDER BY DateReported ASC");
		
		if(xRayResult.length==0) {
			return "XRay Result Pending";
		}
		
		else if (xRayResult[0].contains("SUGGESTIVE") || xRayResult[0].contains("SUSPICIOUS")) {
			return "SUGGESTIVE";
		}
		
		else 
			return "NOT SUGGESTIVE";
		
		//return result;
	}
	
	private String getClinicalDiagnosisResult() throws Exception {
		String result = null;
		
		String[] cdfResult = null;
		long count = 0;
		
		//xRayResult = ssl.getColumnData("Encounter", "XRayResults", " where PatientId='"+ id + "' AND DateReported IS NOT NULL ORDER BY DateReported ASC");
		count = ssl.count("Encounter",  " where EncounterType='CDF' AND PID1='" + id + "'");
		
		if(count==0) {
			result =  "Refer to Indus for Clinical Diagnosis";
		}
		
		cdfResult = ssl.getColumnData("EncounterResults", "Value", " where PID1='"+ id + "' AND Element='DIAGNOSIS'");	
		
		if(cdfResult.length==0) {
			result = "Refer to Indus for Clinical Diagnosis";
		}
		
		else {
			result = cdfResult[0];
		}
		
		return result;
	}
	
	private boolean isBaselineDone() throws Exception {
		return ssl.exists("Encounter", " where PID1='" + id + "' AND EncounterType='BASELINE'");
	}
	
	public String walk() throws Exception {
		String result = "";
		
		isSputumPositive = isSputumPositive();
		
		if(!isSputumPositive)
			isDoubleSputumNegative = isDoubleNegativeSputum();
		
		if(!isDoubleSputumNegative)
			isTripleNotProduced = isTripleNotProduced();
		
		if(isSputumPositive) {
			System.out.println("1");
			if(isBaselineDone()) {
				return "Smear +ve TB - Continue DOT";
			}
			
			else {
				return "Smear +ve TB - Fill Baseline Treatment form";
			}
		}
		
		else if(isDoubleSputumNegative) {
			System.out.println("2");
			String gxpResult = getGenXpertResult();
			boolean tbHist = isTBDrugHistory();
			
			if(tbHist) {
				System.out.println("2a");
				if(gxpResult==null) {
					System.out.println("2b");
					return "Smear -ve - GenXpert Pending";
				}
				
				else if(gxpResult.equals("POSITIVE")) {
					System.out.println("2c");
					if(isBaselineDone()) {
						return "GenXpert +ve - Continue DOT";
					}
					
					else {
						return "GenXpert +ve - Fill Baseline Treatment form";
					}
				}
				
				else {
					System.out.println("2d");
					return "GenXpert -ve - " + getClinicalDiagnosisResult();
				}
			}
			
			else { // no history
				String xRayResult = getXrayResult();
				
				if(xRayResult.equals("SUGGESTIVE")) {
					if(gxpResult==null) {
						return "Smear -ve - GenXpert Pending";
					}
					
					else if(gxpResult.equals("POSITIVE")) {
						if(isBaselineDone()) {
							return "GenXpert +ve - Continue DOT";
						}
						
						else {
							return "GenXpert +ve - Fill Baseline Treatment form";
						}
					}
					
					else {
						return "GenXpert -ve - " + getClinicalDiagnosisResult();
					}
				}
				
				else if(xRayResult.equals("NOT SUGGESTIVE")) {
					return "TB Not Detected";
				}
				
				else
					return "Smear -ve  - " + xRayResult;
				
				
			}
		}
		
		else if (isTripleNotProduced) {
			System.out.println("3");
			boolean tbHist  = isTBDrugHistory();
			
			if(tbHist) {
				return "Sputum not produced - " + getClinicalDiagnosisResult();
			}
			
			else {
				String xRayResult = getXrayResult();
				
				if(xRayResult.equals("SUGGESTIVE")) {
					return "Sputum not produced - " + getClinicalDiagnosisResult();
				}
				
				else if(xRayResult.equals("NOT SUGGESTIVE")) {
					return "TB Not Detected";
				}
				
				else
					return "Sputum not produced - " + xRayResult;
			}
			
		}
		
		
		else {
			System.out.println("4");
			return "Sputum Results pending"; 
		}
		
	
	}
	
	public String childWalk() {
		
		//if no paed conf do paed conf
		//if no paed clinical diag do diag
		//if ppd not done... do PPD
		//if ppd done, get result
		//ppdResult > 10
			//A
			//if not CXR, do CXR
			//if not AFB Smear, do AFB Smear
			//if not PPA
		//ppdResult < 10
			//if TB strongly suspected, go to A
			//else antibiotic and followup
		return "";
	}
	
}
