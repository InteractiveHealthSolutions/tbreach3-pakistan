package org.irdresearch.tbreach.mobileevent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import org.irdresearch.tbreach.server.ServerServiceImpl;
import org.irdresearch.tbreach.shared.model.*;

import org.openmrs.EncounterType;

import org.openmrs.Encounter;
import org.openmrs.EncounterRole;
import org.openmrs.Obs;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Provider;
import org.openmrs.User;

import org.openmrs.api.*;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.ModuleMustStartException;
import org.openmrs.util.DatabaseUpdateException;
import org.openmrs.util.InputRequiredException;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.Concept;
//import org.irdresearch.tbreach.server.XmlUtil;

public class EventHandler {

	private HttpServletRequest request;
	private ServerServiceImpl ssl;

	
	String patientId = null;
	String sampleId = null;
	String mtb = null;
	String mtbBurden = null;
	
	String rif = null;

	
	String rFinal = null;
	String rPending = null;
	String rError =null;
	String rCorrected =null;
	String resultDate =null;
	String errorCode = null;
	
	String operatorId =null;//=" + operatorId;// e.g Karachi Xray
	String pcId =null;
	String instrumentSerial =null;
	String moduleId = null;
	String cartridgeId = null;
	String reagentLotId = null;
	String expDate = null;
	String systemId = null;
	String receiverId = null;
	
	String probeResultA = null;
	String probeResultB = null;
	String probeResultC = null;
	String probeResultD = null;
	String probeResultE = null;
	String probeResultSPC = null;
	
	String probeCtA = null;
	String probeCtB = null;
	String probeCtC = null;
	String probeCtD = null;
	String probeCtE = null;
	String probeCtSPC = null;
	
	String probeEndptA = null;
	String probeEndptB = null;
	String probeEndptC = null;
	String probeEndptD = null;
	String probeEndptE = null;
	String probeEndptSPC = null;
	
	 Date resultDateObj = null;
	 Date expDateObj = null;
	 
	 String username = null;
	 String password = null;
	

	public EventHandler() {
		ssl = new ServerServiceImpl();
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public String handleEvent() {

		String xmlResponse = null;

		String reqType = request.getParameter("type");
		System.out.println("----->" + reqType);
		
		
		
		if(reqType==null) {
			//return XmlUtil.createErrorXml("Bad Request");
			return XmlUtil.getXMLMessage("Bad Request", XmlStrings.EMR, "Bad Request");
		}
		
		else if(reqType.equals(RequestType.REMOTE_ASTM_RESULT)) {
			
			username = request.getParameter("username");
			password = request.getParameter("password");
			patientId = request.getParameter("pid");
			sampleId = request.getParameter("sampleid");
			mtb = request.getParameter("mtb");
			
			rif = request.getParameter("rif");
			if(rif!=null && rif.equalsIgnoreCase("null"))
				rif = null;
			
			 rFinal = request.getParameter("final");
			 rPending = request.getParameter("pending");
			 rError = request.getParameter("error");
			 rCorrected = request.getParameter("correction");
			 resultDate = request.getParameter("enddate");
			 errorCode = request.getParameter("errorcode");
			
			 operatorId = request.getParameter("operatorid");//=" + operatorId;// e.g Karachi Xray
			 pcId = request.getParameter("pcid");
			 instrumentSerial = request.getParameter("instserial");
			
			 moduleId = request.getParameter("moduleid");
			 cartridgeId = request.getParameter("cartrigeid");
			 reagentLotId = request.getParameter("reagentlotid");
			 systemId = request.getParameter("systemid");
			 expDate = request.getParameter("expdate");
			System.out.println("------>" + operatorId);
			System.out.println("------>" + systemId);
			 probeResultA = request.getParameter("probea");
			 probeResultB = request.getParameter("probeb");
			 probeResultC = request.getParameter("probec");
			 probeResultD = request.getParameter("probed");
			 probeResultE = request.getParameter("probee");
			 probeResultSPC = request.getParameter("probespc");
			
			 probeCtA = request.getParameter("probeact");
			 probeCtB = request.getParameter("probebct");
			 probeCtC = request.getParameter("probecct");
			 probeCtD = request.getParameter("probedct");
			 probeCtE = request.getParameter("probeect");
			 probeCtSPC = request.getParameter("probespcct");
			
			 probeEndptA = request.getParameter("probeaendpt");
			 probeEndptB = request.getParameter("probebendpt");
			 probeEndptC = request.getParameter("probecendpt");
			 probeEndptD = request.getParameter("probedendpt");
			 probeEndptE = request.getParameter("probeeendpt");
			 probeEndptSPC = request.getParameter("probespcendpt");
			 
			
				if(resultDate!=null) {
					System.out.println("handling time " + resultDate);
					String year = resultDate.substring(0,4);
					String month = resultDate.substring(4,6);
					String date = resultDate.substring(6,8);
					String hour = null;
					String minute = null;
					String second = null;
				
					if(resultDate.length()==14) {
						hour = resultDate.substring(8,10);
						minute = resultDate.substring(10,12);
						second = resultDate.substring(12,14);
						System.out.println("res before: " + hour + "," + minute + "," + second);
						
					}
					
					GregorianCalendar cal = new GregorianCalendar();
					System.out.println("res base:" + cal.getTime());
					cal.set(Calendar.YEAR, Integer.parseInt(year));
					cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
					cal.set(Calendar.DATE, Integer.parseInt(date));
					if(hour!=null)
						cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
					else
						cal.set(Calendar.HOUR_OF_DAY,0);
					if(minute!=null)
						cal.set(Calendar.MINUTE, Integer.parseInt(minute));
					else
						cal.set(Calendar.MINUTE,0);
					if(second!=null)
						cal.set(Calendar.SECOND, Integer.parseInt(second));
					cal.set(Calendar.SECOND,0);
					
					cal.set(Calendar.MILLISECOND,0);
					
					resultDateObj = cal.getTime();
				}
				
				if(expDate!=null) {
					System.out.println("Exp: " + expDate);
					String year = expDate.substring(0,4);
					String month = expDate.substring(4,6);
					String date = expDate.substring(6,8);
					String hour = null;
					String minute = null;
					String second = null;
				
					if(expDate.length()==14) {
						hour = expDate.substring(8,10);
						minute = expDate.substring(10,12);
						second = expDate.substring(12,14);
						System.out.println("EXP before:" + hour + "," + minute + "," + second);
						
					}
					
					GregorianCalendar cal = new GregorianCalendar();
					System.out.println("exp base: " + cal.getTime());
					cal.set(Calendar.YEAR, Integer.parseInt(year));
					cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
					cal.set(Calendar.DATE, Integer.parseInt(date));
					if(hour!=null)
						cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
					else
						cal.set(Calendar.HOUR_OF_DAY,0);
					if(minute!=null)
						cal.set(Calendar.MINUTE, Integer.parseInt(minute));
					else
						cal.set(Calendar.MINUTE,0);
					if(second!=null)
						cal.set(Calendar.SECOND, Integer.parseInt(second));
					cal.set(Calendar.SECOND,0);
					
					cal.set(Calendar.MILLISECOND,0);
					
					expDateObj = cal.getTime();
				}
			
			//return doRemoteASTMResult();
			String masterDumpResult = doRemoteASTMResult();
			System.out.println("ASTMResult: "+ masterDumpResult);
			String emrResult = astmResultTriage();
			System.out.println("EMRResult: " + emrResult);
			
			//return masterDumpResult + omrsResult;
			String sendBack = XmlUtil.getXMLMessage(masterDumpResult, "OpenMRS", emrResult);
			System.out.println("---->" + sendBack);
			return sendBack;
		}
		
		else {

			//return XmlUtil.createErrorXml("Invalid request type");
			return XmlUtil.getXMLMessage("Invalid Request Type", XmlStrings.EMR,"Invalid Request Type");
		}
	}
	
	private String astmResultTriage() {
		String xml = null;
		String message = "";
		System.out.println(request.toString());  
		patientId = request.getParameter("pid");
		sampleId = request.getParameter("sampleid");
		cartridgeId = request.getParameter("cartrigeid");
		org.openmrs.Patient omrsPatient = null;
		
		boolean update = false;
		boolean insert = false;
		
		
		//if there is no patient id or sample id, send to error database
		if(patientId==null)
			return "No patient ID specified";
		
		if(sampleId==null)
			return "No sample ID specified";
		
		File propsFile = new File("C:\\Application Data\\OpenMRS", "openmrs-runtime.properties");
		
	//	File propsFile = new File(OpenmrsUtil.getApplicationDataDirectory(), "openmrs-runtime.properties");
		
		//File propsFile = new File("/var/lib/tomcat6/.xpertsms", "openmrs-runtime.properties");
		System.out.println("props found");
		  Properties props = new Properties();
		  OpenmrsUtil.loadProperties(props, propsFile);
		 
		  try {
			 
			  
			  
			 // if(!Context.isSessionOpen()) {
				  System.out.println("Starting context with " + (String)props.get("connection.url"));
			  
				  Context.startup((String)props.get("connection.url"), (String)props.get("connection.username"),(String)props.get("connection.password"), props);
			 
		} catch (ModuleMustStartException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "Module failure when starting OpenMRS";
		} catch (DatabaseUpdateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "Database error when starting OpenMRS";
		} catch (InputRequiredException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "General failure when starting OpenMRS";
		}
		catch(Exception e1) {
			e1.printStackTrace();
			return "Failure when starting OpenMRS";
		}
		  
		System.out.println("Context started for sampleID - " + sampleId);
		  
		  List<org.openmrs.Patient> p = null;
		  PatientIdentifierType patientIdentifierType = null;
		 
			List <PatientIdentifierType>typeList = null;
		 try {
			  
			
			
			 	if(!Context.isSessionOpen()) {
			 		System.out.println("opening session");
			 		Context.openSession();
			 	}
			 	System.out.println("auth");
		    	Context.authenticate(username, password);
		    System.out.println("auth done");
		   
			//check that patient exists. If not send to error database
			typeList = Context.getPatientService().getAllPatientIdentifierTypes();
			for(int i=0;i<typeList.size();i++) {
				if(typeList.get(i).getName().equals("OpenMRS Identification Number")) {
					patientIdentifierType = typeList.get(i);
					break;
				}
			}
			
			if(patientIdentifierType==null) {
				return "Patient ID type not found in OpenMRS";
			}
		
			    p = Context.getPatientService().getPatients(null,patientId,typeList,false);
		
		
		
			    if(p==null || p.size()==0) {
			    	return "Patient not found in OpenMRS. Please ensure patient exists and try again";
			    }
		
			    else {
			    	omrsPatient = p.get(0);
			    }
		
		
		  Concept sampleIdConcept = null;
		
			    
		///check the sample Id
			    sampleIdConcept = Context.getConceptService().getConceptByName("GXP Barcode"); //TODO - get the right concept name here
		
		
		List<Obs> sampleIdList = null;
		Encounter xpertOrderEnc = null;
		EncounterType xpertOrderEncType = null;
		Obs sampleIdObs = null;
		Encounter xpertResultEnc = null;
		
			    
			   sampleIdList =  Context.getObsService().getObservationsByPersonAndConcept(Context.getPersonService().getPerson(omrsPatient.getPersonId()), sampleIdConcept);
			 
			   
			 //if there is no sample id match for this patient, send to error database
			   if(sampleIdList == null) {
					return "Could not match sample ID with patient ID in OpenMRS";
			   }
				
				//if sample id exists, check the encounter type
				if(sampleIdList.size()==1) {
					System.out.println("1");
					sampleIdObs = sampleIdList.get(0);
					xpertOrderEnc = sampleIdObs.getEncounter();
					xpertOrderEncType = xpertOrderEnc.getEncounterType();
					if(!xpertOrderEncType.getName().equals("GXP Order") || !sampleIdObs.getValueText().equals(sampleId)) { // TODO - check that this name matches the server
						return "GXP Order for this sample and patient not found";
					}
					
					else {
						insert = true;
					}
				}
				
				else if(sampleIdList.size() > 1) {
					System.out.println(">1 ---> " + sampleIdList.size());
					Encounter tempEnc = null;
					for(int i=0; i< sampleIdList.size(); i++) {
						tempEnc = sampleIdList.get(i).getEncounter();
						System.out.println(i + tempEnc.getEncounterType().getName()+"---"+sampleIdList.get(i).getValueText());
						
						if(tempEnc.getEncounterType().getName().equals("GXP Order") && sampleIdList.get(i).getValueText().equals(sampleId)) {
							if(xpertOrderEnc==null) {
								System.out.println("FOUND");
								xpertOrderEnc = tempEnc;
								insert = true;
							}
							
							else {
								System.out.println("duplicate orders found... quitting");
								return "Duplicate orders found for this sample.";
							}
						}
						
					}
					
					
				}
				
				if(xpertOrderEnc==null) {
					System.out.println("no order found... quitting");
					return "Could not find GXP order for this sample. Please make sure you have filled the GXP Order form";
				}
				
			
				
				else if(sampleIdList.size() > 1) {
					System.out.println("hunt for existing result for this sample");
					Encounter tempEnc = null;
					Set<Obs> obsSet = null;
					for(int i=0; i< sampleIdList.size(); i++) {
						tempEnc = sampleIdList.get(i).getEncounter();
						System.out.println(tempEnc.getEncounterType().getName().toString());
						if(tempEnc.getEncounterType().getName().equals("GXP Result") && sampleIdList.get(i).getValueText().equals(sampleId)) { // TODO - check that this name matches the server
							System.out.println("sample id match");
							if(xpertResultEnc==null) {
								System.out.println("checking for cartridge id match");
								xpertResultEnc = tempEnc;
								obsSet = xpertResultEnc.getObs();
								for(Obs o : obsSet) {
									
									if(o.getConcept().getName().toString().equals("GXP Cartridge ID") && o.getValueText().equals(cartridgeId)) {
										//xpertResultEnc = tempEnc;
										insert = false;
										update = true;
										System.out.println("Existing result found");
										break;
										
									}
								}
							}
						}	
					}
			}
		
/////////////////////////////////////////////////////////////		
		
		
		if(!insert && !update) {
			System.out.println("INSERT + UPDDATE -> DATA IS MESSED UP!!!");
			return "Data error!"; 
		}
		
		//add a new encounter since we have an order already and no match
		if(insert ) {
			System.out.println("Inserting");
			xpertResultEnc = new Encounter();
			xpertResultEnc.setPatient(omrsPatient);
			//xpertResultEnc.setEncounterType(encounterType)
			xpertResultEnc.setEncounterType(Context.getEncounterService().getEncounterType("GXP RESULT"));
			
			EncounterRole labTechRole = null;
			
			
			List<EncounterRole> allRoles = null;
			
			
			allRoles = Context.getEncounterService().getAllEncounterRoles(false);
				
		
			for(EncounterRole er: allRoles) {
				if(er.getName().equals("Lab Technician")) {
					labTechRole = er;
					break;
				}
			}
			
			if(labTechRole==null) {
				System.out.println("NO ROLE!!!");
				return "No lab technician role found in OpenMRS. Contact IHS support!";
			}
			
			Provider prov  =  Context.getProviderService().getProviderByIdentifier(operatorId);
			
			if(prov==null) {
				System.out.println("Provider not found!!");
				return "Invalid Provider name. Check that your GeneXpert Dx username matches that of a Provider in OpenMRS";
			}
			
			xpertResultEnc.setProvider(labTechRole,prov); 			
			xpertResultEnc.setEncounterDatetime(resultDateObj);
			
			org.openmrs.Location loc = Context.getLocationService().getLocation(systemId);
		
			if(loc==null) {
				System.out.println("Location not found!!");
				return "Invalid location name! Check your System ID field in GeneXpert Dx and make sure it matches a Location in OpenMRS";
			}
			
			xpertResultEnc.setLocation(loc);
			/////
			if(mtb != null) {
				System.out.println("----MTB----" + mtb);
				int index = mtb.indexOf("MTB DETECTED");
			// mtbBurden = null;
				if(index!=-1) {
					mtbBurden = mtb.substring(index+"MTB DETECTED".length()+1);
				
					/*gxpNew.setGeneXpertResult("MTB DETECTED");
					gxpNew.setIsPositive(new Boolean(true));
					gxpNew.setMtbBurden(mtbBurden);
					gxpNew.setErrorCode(0);*/
					mtb = "MTB DETECTED";
				}
			
				
		}
			
		String rifResult = null;
		if(rif != null) {
			int index = rif.indexOf("NOT DETECTED");
			
			if(index!=-1) {
				rifResult = "NOT DETECTED";
			}
			
			else if(rif.indexOf("DETECTED")!=-1){
				rifResult = "DETECTED";
			}
			
			else {
				rifResult = rif.toUpperCase();
			}
			
			
		}
		
		sampleIdObs = new Obs();
		
		sampleIdObs.setConcept(sampleIdConcept);
		sampleIdObs.setValueText(sampleId);
		xpertResultEnc.addObs(sampleIdObs);
		
		Obs mtbObs = new Obs();
		System.out.println("----MTB----**" + mtb);
		//mtbObs.setConcept(Context.getConceptService().getConceptByName("GXP Result"));
		mtbObs.setConcept(getConceptBySpecifiedName("GXP Result"));
		System.out.println(mtbObs.getConcept().getName());
		System.out.println(mtbObs.getConcept().getNames().size());
		
		mtbObs.setValueCoded(Context.getConceptService().getConceptByName(mtb));
		xpertResultEnc.addObs(mtbObs);
		
		if(mtbBurden!=null) {
		Obs mtbBurdenObs = new Obs();
		
		mtbBurdenObs.setConcept(Context.getConceptService().getConceptByName("MTB Burden"));
		mtbBurdenObs.setValueCoded(Context.getConceptService().getConceptByName(mtbBurden));
		xpertResultEnc.addObs(mtbBurdenObs);
		}
		
		if(rif!=null) {
			Obs rifObs = new Obs();
			
			rifObs.setConcept(getConceptBySpecifiedName("RIF Result"));
			rifObs.setValueCoded(Context.getConceptService().getConceptByName(rifResult));
			xpertResultEnc.addObs(rifObs);
			}
			/////
			
		
			if(errorCode!=null) {
				Obs errorObs = new Obs();
				
				errorObs.setConcept(Context.getConceptService().getConcept("Error Code"));
				errorObs.setValueText(errorCode);
				xpertResultEnc.addObs(errorObs);
				
				
			}
			
			
		
			Obs instrumentSerialObs = new Obs();
			instrumentSerialObs.setConcept(Context.getConceptService().getConcept("Instrument Serial No"));
			instrumentSerialObs.setValueText(instrumentSerial);
			xpertResultEnc.addObs(instrumentSerialObs);
			
			Obs moduleIdObs = new Obs();
			moduleIdObs.setConcept(Context.getConceptService().getConcept("GXP Module ID"));
			moduleIdObs.setValueText(moduleId);
			xpertResultEnc.addObs(moduleIdObs);
			
			Obs cartridgeIdObs = new Obs();
			cartridgeIdObs.setConcept(Context.getConceptService().getConcept("GXP Cartridge ID"));
			cartridgeIdObs.setValueText(cartridgeId);
			xpertResultEnc.addObs(cartridgeIdObs);
			
			Obs reagentLotIdObs = new Obs();
			reagentLotIdObs.setConcept(Context.getConceptService().getConcept("Reagent Lot No"));
			reagentLotIdObs.setValueText(reagentLotId);
			xpertResultEnc.addObs(reagentLotIdObs);
			
			Obs expDateObs = new Obs();
			expDateObs.setConcept(Context.getConceptService().getConcept("Reagent Expiry"));
			expDateObs.setValueDate(expDateObj);
			xpertResultEnc.addObs(expDateObs);
			
			if(probeResultA!=null) {
				Obs probeResultAObs = new Obs();
				probeResultAObs.setConcept(Context.getConceptService().getConcept("Probe Result A"));
				probeResultAObs.setValueCoded(Context.getConceptService().getConcept(probeResultA));
				xpertResultEnc.addObs(probeResultAObs);
			}
			
			if(probeResultB!=null) {
				Obs probeResultBObs = new Obs();
				probeResultBObs.setConcept(Context.getConceptService().getConcept("Probe Result B"));
				probeResultBObs.setValueCoded(Context.getConceptService().getConcept(probeResultB));
				xpertResultEnc.addObs(probeResultBObs);
			}
			
			if(probeResultC!=null) {
				Obs probeResultCObs = new Obs();
				probeResultCObs.setConcept(Context.getConceptService().getConcept("Probe Result C"));
				probeResultCObs.setValueCoded(Context.getConceptService().getConcept(probeResultC));
				xpertResultEnc.addObs(probeResultCObs);
			}
			
			if(probeResultD!=null) {
				Obs probeResultDObs = new Obs();
				probeResultDObs.setConcept(Context.getConceptService().getConcept("Probe Result D"));
				probeResultDObs.setValueCoded(Context.getConceptService().getConcept(probeResultD));
				xpertResultEnc.addObs(probeResultDObs);
			}
			
			if(probeResultE!=null) {
				Obs probeResultEObs = new Obs();
				probeResultEObs.setConcept(Context.getConceptService().getConcept("Probe Result E"));
				probeResultEObs.setValueCoded(Context.getConceptService().getConcept(probeResultE));
				xpertResultEnc.addObs(probeResultEObs);
			}
			
			if(probeResultSPC!=null) {
				Obs probeResultSPCObs = new Obs();
				probeResultSPCObs.setConcept(Context.getConceptService().getConcept("Probe Result SPC"));
				probeResultSPCObs.setValueCoded(Context.getConceptService().getConcept(probeResultSPC));
				xpertResultEnc.addObs(probeResultSPCObs);
			}
			
			if(probeCtA!=null) {
				Obs probeCtAObs= new Obs();
				probeCtAObs.setConcept(Context.getConceptService().getConcept("Probe CT A"));
				probeCtAObs.setValueNumeric(Double.parseDouble(probeCtA));
				xpertResultEnc.addObs(probeCtAObs);
			}
			
			if(probeCtB!=null) {
				Obs probeCtBObs= new Obs();
				probeCtBObs.setConcept(Context.getConceptService().getConcept("Probe CT B"));
				probeCtBObs.setValueNumeric(Double.parseDouble(probeCtB));
				xpertResultEnc.addObs(probeCtBObs);
			}
			
			if(probeCtC!=null) {
				Obs probeCtCObs= new Obs();
				probeCtCObs.setConcept(Context.getConceptService().getConcept("Probe CT C"));
				probeCtCObs.setValueNumeric(Double.parseDouble(probeCtC));
				xpertResultEnc.addObs(probeCtCObs);
			}
			
			if(probeCtD!=null) {
				Obs probeCtDObs= new Obs();
				probeCtDObs.setConcept(Context.getConceptService().getConcept("Probe CT D"));
				probeCtDObs.setValueNumeric(Double.parseDouble(probeCtD));
				xpertResultEnc.addObs(probeCtDObs);
			}
			
			if(probeCtE!=null) {
				Obs probeCtEObs= new Obs();
				probeCtEObs.setConcept(Context.getConceptService().getConcept("Probe CT E"));
				probeCtEObs.setValueNumeric(Double.parseDouble(probeCtE));
				xpertResultEnc.addObs(probeCtEObs);
			}
			
			if(probeCtSPC!=null) {
				Obs probeCtSPCObs= new Obs();
				probeCtSPCObs.setConcept(Context.getConceptService().getConcept("Probe CT SPC"));
				probeCtSPCObs.setValueNumeric(Double.parseDouble(probeCtSPC));
				xpertResultEnc.addObs(probeCtSPCObs);
			}
			
			if(probeEndptA!=null) {
				Obs probeEndptAObs= new Obs();
				probeEndptAObs.setConcept(Context.getConceptService().getConcept("Probe End Point A"));
				probeEndptAObs.setValueNumeric(Double.parseDouble(probeEndptA));
				xpertResultEnc.addObs(probeEndptAObs);
			}
			
			if(probeEndptB!=null) {
				Obs probeEndptBObs= new Obs();
				probeEndptBObs.setConcept(Context.getConceptService().getConcept("Probe End Point B"));
				probeEndptBObs.setValueNumeric(Double.parseDouble(probeEndptB));
				xpertResultEnc.addObs(probeEndptBObs);
			}
			
			if(probeEndptC!=null) {
				Obs probeEndptCObs= new Obs();
				probeEndptCObs.setConcept(Context.getConceptService().getConcept("Probe End Point C"));
				probeEndptCObs.setValueNumeric(Double.parseDouble(probeEndptC));
				xpertResultEnc.addObs(probeEndptCObs);
			}
			
			if(probeEndptD!=null) {
				Obs probeEndptDObs= new Obs();
				probeEndptDObs.setConcept(Context.getConceptService().getConcept("Probe End Point D"));
				probeEndptDObs.setValueNumeric(Double.parseDouble(probeEndptD));
				xpertResultEnc.addObs(probeEndptDObs);
			}
			
			if(probeEndptE!=null) {
				Obs probeEndptEObs= new Obs();
				probeEndptEObs.setConcept(Context.getConceptService().getConcept("Probe End Point E"));
				probeEndptEObs.setValueNumeric(Double.parseDouble(probeEndptE));
				xpertResultEnc.addObs(probeEndptEObs);
			}
			
			if(probeEndptSPC!=null) {
				Obs probeEndptSPCObs= new Obs();
				probeEndptSPCObs.setConcept(Context.getConceptService().getConcept("Probe End Point SPC"));
				probeEndptSPCObs.setValueNumeric(Double.parseDouble(probeEndptSPC));
				xpertResultEnc.addObs(probeEndptSPCObs);
			}
			Set<Obs> setObs = xpertResultEnc.getAllObs();
			for(Obs obs : setObs) {
				if(obs.getConcept()==null)
					System.out.println(obs.getValueAsString(Locale.UK));
			}
			
			Context.getEncounterService().saveEncounter(xpertResultEnc);
	
			//xml = XmlUtil.createSuccessXml();
			return "Success";
	
		}
		//TODO check if this needs to be updated
		else if(update) {
			return "Duplicate cartridge ID: " +  cartridgeId + ". To edit, please edit the encounter via OpenMRS.";
			//return "Result for patient: " + patientId + ", sample: " + sampleId +  ", cartridge: " + cartridgeId + " already exists. To edit, please edit the encounter via OpenMRS.";
		}
		
		else {
			return "General failure submitting to OpenMRS";
		}
		 }
		 
		 catch(ContextAuthenticationException cae) {
			 
			 cae.printStackTrace();
			// return "Result for patient: " + patientId + ", sample: " + sampleId +  ", cartridge: " + cartridgeId + " could not be submitted. Authentication Failure!";
			 return "Error: Authentication Failure!";
		 }
		 
		 catch(Exception e) {
			
			 e.printStackTrace();
			 return "Error: General Failure!";
		 }
		
		 finally {
			 System.out.println("closing session for sampleid "  + sampleId);
			 Context.closeSession();
			 Context.shutdown();
			 System.out.println("closed session for sampleid "  + sampleId);
		 }
		
		
	}

		
	private String doRemoteASTMResult() {
		String xml = null;
		boolean insert = false;
		boolean update = false;
		
		GeneXpertResults test = null;
		
		try {
			test = ssl.findGeneXpertResultsByCartridgeID(cartridgeId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			//return "Error checking test data for sample " + sampleId + " for patient " + patientId + ". Error: " + e1.getMessage();
		}
		
		if(test!=null) {
			//return "Result for sample " + sampleId + " for patient " + patientId + " could not be stored. Error: Duplicate cartridge ID " + cartridgeId;
			return "Error: Duplicate cartridge ID: " + cartridgeId;
		}
		
		

		if(resultDate!=null) {
			System.out.println("handling time");
			String year = resultDate.substring(0,4);
			String month = resultDate.substring(4,6);
			String date = resultDate.substring(6,8);
			String hour = null;
			String minute = null;
			String second = null;
		
			if(resultDate.length()==14) {
				hour = resultDate.substring(8,10);
				minute = resultDate.substring(10,12);
				second = resultDate.substring(12,14);
				
			}
			
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(Calendar.YEAR, Integer.parseInt(year));
			cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
			cal.set(Calendar.DATE, Integer.parseInt(date));
			if(hour!=null)
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
			else
				cal.set(Calendar.HOUR_OF_DAY,0);
			if(minute!=null)
				cal.set(Calendar.MINUTE, Integer.parseInt(minute));
			else
				cal.set(Calendar.MINUTE,0);
			if(second!=null)
				cal.set(Calendar.SECOND, Integer.parseInt(second));
			cal.set(Calendar.SECOND,0);
			
			cal.set(Calendar.MILLISECOND,0);
			
			resultDateObj = cal.getTime();
			
			
			
			
		}
		
		//if(rPending!=null) {
			
			ssl = new ServerServiceImpl();
			GeneXpertResults[] gxp = null;
			GeneXpertResults gxpNew = null;
//			GeneXpertResultsAuto gxp = null;
			try {
				gxp = ssl.findGeneXpertResults(sampleId,patientId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(gxp==null || gxp.length==0) {
				//System.out.println("NOT FOUND");
				
				GeneXpertResults gxpU = createGeneXpertResults(patientId,sampleId, mtb, rif, resultDate,instrumentSerial,moduleId,cartridgeId,reagentLotId,expDate, operatorId,pcId,probeResultA,probeResultB,probeResultC,probeResultD,probeResultE,probeResultSPC,probeCtA,probeCtB,probeCtC,probeCtD,probeCtE,probeCtSPC,probeEndptA,probeEndptB,probeEndptC,probeEndptD,probeEndptE,probeEndptSPC,errorCode,systemId);
				try {
					//ssl.updateGeneXpertResultsAuto(gxp, gxp.getIsPositive(),operatorId,pcId,instrumentSerial,moduleId,cartridgeId,reagentLotId);
					ssl.saveGeneXpertResults(gxpU);//, gxp.getIsPositive(),operatorId,pcId,instrumentSerial,moduleId,cartridgeId,reagentLotId);
					return "Success";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "Could not save data for Sample ID " + gxpU.getSputumTestId();
				}
				
			}
			
			else {
				
				for(int i=0;i<gxp.length;i++) {
					if(gxp[i].getDateTested()!=null) {
					System.out.println("STORED TIME:" + gxp[i].getDateTested().getTime());
						if(resultDateObj.getTime()== gxp[i].getDateTested().getTime()){
								gxpNew = gxp[i];
								update = true;
								//System.out.println("date match");
								break;
						}
						
					}
				}
		
				if(!update) {
					for(int i=0;i<gxp.length;i++) {
						if(gxp[i].getGeneXpertResult()==null){//F form filled
							update = true;
							gxpNew = gxp[i];
							System.out.println("ID match null result");
							break;
						
						}
					}
				}
				
			}
			

			//set mtb
			if(update==true) {
				if(mtb != null) {
					//System.out.println("----MTB----" + mtb);
					int index = mtb.indexOf("MTB DETECTED");
					String mtbBurden = null;
					if(index!=-1) {
						mtbBurden = mtb.substring(index+"MTB DETECTED".length()+1);
					
						gxpNew.setGeneXpertResult("MTB DETECTED");
						gxpNew.setIsPositive(new Boolean(true));
						gxpNew.setMtbBurden(mtbBurden);
						gxpNew.setErrorCode(0);
					}
				
					else {
						index = mtb.indexOf("MTB NOT DETECTED");
						//System.out.println("mtb :" + index + " " + mtb);
						if(index!=-1) {
							gxpNew.setGeneXpertResult("MTB NOT DETECTED");
							gxpNew.setIsPositive(new Boolean(false));
							mtbBurden = null;
					}
					
					else {
						gxpNew.setGeneXpertResult(mtb);
						mtbBurden = null;
					}
				}
			}
			
			if(rif != null) {
				int index = rif.indexOf("NOT DETECTED");
				String rifResult = null;
				if(index!=-1) {
					rifResult = "NOT DETECTED";
				}
				
				else if(rif.indexOf("DETECTED")!=-1){
					rifResult = "DETECTED";
				}
				
				else {
					rifResult = rif.toUpperCase();
				}
				
				gxpNew.setDrugResistance(rifResult);
			}
			
			
			gxpNew.setDateTested(resultDateObj);
			gxpNew.setInstrumentSerial(instrumentSerial);
			gxpNew.setModuleId(moduleId);
			gxpNew.setReagentLotId(reagentLotId);
			gxpNew.setExpDate(expDate);
			gxpNew.setCartridgeId(cartridgeId);
			gxpNew.setPcId(pcId);
			gxpNew.setOperatorId(operatorId);
			if(errorCode!=null)
				gxpNew.setErrorCode(Integer.parseInt(errorCode));
			//Probes
			gxpNew.setProbeResultA(probeResultA);
			gxpNew.setProbeResultB(probeResultB);
			gxpNew.setProbeResultC(probeResultC);
			gxpNew.setProbeResultD(probeResultD);
			gxpNew.setProbeResultE(probeResultE);
			gxpNew.setProbeResultSPC(probeResultSPC);
			
			if(probeCtA!=null)
				gxpNew.setProbeCtA(Double.parseDouble(probeCtA));
			if(probeCtB!=null)
				gxpNew.setProbeCtB(Double.parseDouble(probeCtB));
			if(probeCtC!=null)
				gxpNew.setProbeCtC(Double.parseDouble(probeCtC));
			if(probeCtD!=null)
				gxpNew.setProbeCtD(Double.parseDouble(probeCtD));
			if(probeCtE!=null)
				gxpNew.setProbeCtE(Double.parseDouble(probeCtE));
			if(probeCtSPC!=null)
				gxpNew.setProbeCtSPC(Double.parseDouble(probeCtSPC));
			
			if(probeEndptA!=null)
				gxpNew.setProbeEndptA(Double.parseDouble(probeEndptA));
			if(probeEndptB!=null)
				gxpNew.setProbeEndptB(Double.parseDouble(probeEndptB));
			if(probeEndptC!=null)
				gxpNew.setProbeEndptC(Double.parseDouble(probeEndptC));
			if(probeEndptD!=null)
				gxpNew.setProbeEndptD(Double.parseDouble(probeEndptD));
			if(probeEndptE!=null)
				gxpNew.setProbeEndptE(Double.parseDouble(probeEndptE));
			if(probeEndptSPC!=null)
				gxpNew.setProbeEndptSPC(Double.parseDouble(probeEndptSPC));
			
			 
			try {
				//ssl.updateGeneXpertResultsAuto(gxp, gxp.getIsPositive(),operatorId,pcId,instrumentSerial,moduleId,cartridgeId,reagentLotId);
				ssl.updateGeneXpertResults(gxpNew, true);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		return "Success";
	}
	
	
	
	public GeneXpertResults createGeneXpertResults(String patientId, String SampleID, String mtb, String rif, String resultDate,String instrumentSerial,String moduleId,String cartridgeId,String reagentLotId,String expDate,String operatorId,String pcId,String probeResultA,String probeResultB,String probeResultC,String probeResultD,String probeResultE,String probeResultSPC,String probeCtA,String probeCtB,String probeCtC,String probeCtD,String probeCtE,String probeCtSPC,String probeEndptA,String probeEndptB,String probeEndptC,String probeEndptD,String probeEndptE,String probeEndptSPC,String errorCode, String systemId) {
		GeneXpertResults gxp = new GeneXpertResults();
		gxp.setSputumTestId(SampleID);
		gxp.setPatientId(patientId);
		gxp.setLaboratoryId(systemId);
		
		
		if(rif!=null && rif.equalsIgnoreCase("null"))
			rif = null;
		
		if(mtb != null) {
			//System.out.println("----MTB----" + mtb);
			int index = mtb.indexOf("MTB DETECTED");
			String mtbBurden = null;
			if(index!=-1) {
				mtbBurden = mtb.substring(index+"MTB DETECTED".length()+1);
				
				gxp.setGeneXpertResult("MTB DETECTED");
				gxp.setIsPositive(new Boolean(true));
				gxp.setMtbBurden(mtbBurden);
				gxp.setErrorCode(0);
			}
			
			else {
				index = mtb.indexOf("MTB NOT DETECTED");
				//System.out.println("mtb :" + index + " " + mtb);
				if(index!=-1) {
					gxp.setGeneXpertResult("MTB NOT DETECTED");
					gxp.setIsPositive(new Boolean(false));
					mtbBurden = null;
				}
				
				else {
					gxp.setGeneXpertResult(mtb);
					mtbBurden = null;
				}
			}
		}
		
		if(rif != null) {
			int index = rif.indexOf("NOT DETECTED");
			String rifResult = null;
			if(index!=-1) {
				rifResult = "NOT DETECTED";
			}
			
			else if(rif.indexOf("DETECTED")!=-1){
				rifResult = "DETECTED";
			}
			
			else {
				rifResult = rif.toUpperCase();
			}
			
			gxp.setDrugResistance(rifResult);
		}
		
		if(resultDate!=null) {
			String year = resultDate.substring(0,4);
			String month = resultDate.substring(4,6);
			String date = resultDate.substring(6,8);
			String hour = null;
			String minute = null;
			String second = null;
		
			if(resultDate.length()==14) {
				hour = resultDate.substring(8,10);
				minute = resultDate.substring(10,12);
				second = resultDate.substring(12,14);
				
			}
			
			GregorianCalendar cal = new GregorianCalendar();
			cal.set(Calendar.YEAR, Integer.parseInt(year));
			cal.set(Calendar.MONTH, Integer.parseInt(month)-1);
			cal.set(Calendar.DATE, Integer.parseInt(date));
			if(hour!=null)
				cal.set(Calendar.HOUR, Integer.parseInt(hour));
			else
				cal.set(Calendar.HOUR,0);
			if(minute!=null)
				cal.set(Calendar.MINUTE, Integer.parseInt(minute));
			cal.set(Calendar.MINUTE,0);
			if(second!=null)
				cal.set(Calendar.SECOND, Integer.parseInt(second));
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);
			gxp.setDateTested(cal.getTime());
			
		}
		
		gxp.setInstrumentSerial(instrumentSerial);
		gxp.setModuleId(moduleId);
		gxp.setReagentLotId(reagentLotId);
		gxp.setExpDate(expDate);
		gxp.setCartridgeId(cartridgeId);
		gxp.setPcId(pcId);
		gxp.setOperatorId(operatorId);
		if(errorCode!=null)
			gxp.setErrorCode(Integer.parseInt(errorCode));
		
		
		//Probes
		gxp.setProbeResultA(probeResultA);
		gxp.setProbeResultB(probeResultB);
		gxp.setProbeResultC(probeResultC);
		gxp.setProbeResultD(probeResultD);
		gxp.setProbeResultE(probeResultE);
		gxp.setProbeResultSPC(probeResultSPC);
		
		if(probeCtA!=null)
			gxp.setProbeCtA(Double.parseDouble(probeCtA));
		if(probeCtB!=null)
			gxp.setProbeCtB(Double.parseDouble(probeCtB));
		if(probeCtC!=null)
			gxp.setProbeCtC(Double.parseDouble(probeCtC));
		if(probeCtD!=null)
			gxp.setProbeCtD(Double.parseDouble(probeCtD));
		if(probeCtE!=null)
			gxp.setProbeCtE(Double.parseDouble(probeCtE));
		if(probeCtSPC!=null)
			gxp.setProbeCtSPC(Double.parseDouble(probeCtSPC));
		
		if(probeEndptA!=null)
			gxp.setProbeEndptA(Double.parseDouble(probeEndptA));
		if(probeEndptB!=null)
			gxp.setProbeEndptB(Double.parseDouble(probeEndptB));
		if(probeEndptC!=null)
			gxp.setProbeEndptC(Double.parseDouble(probeEndptC));
		if(probeEndptD!=null)
			gxp.setProbeEndptD(Double.parseDouble(probeEndptD));
		if(probeEndptE!=null)
			gxp.setProbeEndptE(Double.parseDouble(probeEndptE));
		if(probeEndptSPC!=null)
			gxp.setProbeEndptSPC(Double.parseDouble(probeEndptSPC));
		
		return gxp;
	}
	
	private Concept getConceptBySpecifiedName(String name) {
		Concept c = null;
		
		List<Concept> cList = Context.getConceptService().getConceptsByName(name);
		
		for(Concept tc : cList) {
			if(tc.getName().toString().equalsIgnoreCase(name)) {
				c = tc;
				break;
			}
		}
		
		return c;
		
	}
	
}
