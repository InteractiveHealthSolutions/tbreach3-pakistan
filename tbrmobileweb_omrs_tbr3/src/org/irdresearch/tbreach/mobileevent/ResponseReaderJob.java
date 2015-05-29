package org.irdresearch.tbreach.mobileevent;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimerTask;

import org.hibernate.FetchMode;
/*import org.ird.immunizationreminder.context.Context;
import org.ird.immunizationreminder.context.ServiceContext;
import org.ird.immunizationreminder.datamodel.entities.Child;
import org.ird.immunizationreminder.datamodel.entities.Child.STATUS;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms;
import org.ird.immunizationreminder.datamodel.entities.ReminderSms.REMINDER_STATUS;
import org.ird.immunizationreminder.datamodel.entities.Response;
import org.ird.immunizationreminder.datamodel.entities.Response.RESPONSE_TYPE;
import org.ird.immunizationreminder.datamodel.entities.Vaccination;
import org.ird.immunizationreminder.datamodel.entities.Vaccination.VACCINATION_STATUS;
import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
import org.ird.immunizationreminder.web.utils.IMRGlobals;*/
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.InboundMessage;
import org.irdresearch.smstarseel.data.InboundMessage.InboundStatus;
import org.irdresearch.tbreach.server.ServerServiceImpl;
import org.irdresearch.tbreach.shared.model.GeneXpertResults;



public class ResponseReaderJob extends TimerTask{

	private static int MAX_WITHOUT_PROBES = 17;
	private static int MAX_WITH_PROBES = MAX_WITHOUT_PROBES + 18;
	
	/*private static int MAX_CELL_NUMBER_MATCH_LENGTH = Integer.parseInt(
			Context.getIRSetting("cellnumber.number-length-without-zero","10")
			);*/
	
	@Override
	public void run() 
	{
		TarseelServices tsc = TarseelContext.getServices();

		try{
			List<InboundMessage> list = tsc.getSmsService().findInbound(null, null, InboundStatus.UNREAD, null, null, null, tsc.getDeviceService().findProjectById(1).getProjectId(), false);
			
			System.out.println("Running Job: ResponseReaderJob "+new Date() + ". Fetched "+list.size()+" UNREAD sms");

			for (InboundMessage ib : list) {
				//ServiceContext sc = Context.getServices();
	
				try{
					String sender = ib.getOriginator();
	
					if(sender.length() !=0){// MAX_CELL_NUMBER_MATCH_LENGTH){
						sender = sender.substring(sender.length());// - MAX_CELL_NUMBER_MATCH_LENGTH);
					}
					
					String text = ib.getText();
					if(text==null || text.length()==0)
						continue;
				
					parseText(text);
						
						
					

					tsc.getSmsService().markInboundAsRead(ib.getReferenceNumber());
				}
				catch (Exception e) {
					e.printStackTrace();
					
				}
				
			}
			
			//without if it would throw exception transaction not successfully started
			if(list.size() > 0) tsc.commitTransaction();
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = "Exception running ResponseReaderJob "+e.getMessage();
			
			
		}
		finally{
			tsc.closeSession();
		}
	}
	
	
	public void parseText(String text) {
		boolean update = false;
		String[] fields = text.split("\\^");
		String sampleId = null;
		String mtb = null;
		String rif = null;
		String systemId = null;
		String rFinal = null;
		String rPending = null;
		String rError = null;
		String rCorrected = null;
		String resultDate = null;
		String errorCode = null;
		
		String operatorId = null;
		String pcId = null;
		String instrumentSerial = null;
		String moduleId = null;
		String cartridgeId = null;
		String reagentLotId = null;
		System.out.println("------>" + operatorId);
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
		String patientId = null;
		String expDate = null;
		
		int probeIndex = 0;
		
		patientId = fields[0];
		sampleId = fields[1];
		mtb = fields[2];
		rif = fields[3];
		systemId = fields[4];
		pcId = fields[5];
		operatorId = fields[6];
		instrumentSerial = fields[7];
		moduleId = fields[8];
		cartridgeId = fields[9];
		reagentLotId = fields[10];
		resultDate = fields[11];
		rFinal = fields[12];
		rPending = fields[13];
		rError = fields[14];
		if(rError!=null&&rError.equals("yes")) {
			errorCode = fields[15];
			rCorrected = fields[16];
			if(fields.length > 17)
				probeIndex = 17;
		}
		else {
			rCorrected = fields[15];
			if(fields.length > 16)
				probeIndex = 16;
		}
		
		if(probeIndex != 0) {
			probeResultA = fields[probeIndex];
			probeResultB = fields[probeIndex + 1];
			probeResultC = fields[probeIndex + 2];
			probeResultD = fields[probeIndex + 3];
			probeResultE = fields[probeIndex + 4];
			probeResultSPC = fields[probeIndex + 5];
			
			probeCtA = fields[probeIndex + 6];
			probeCtB = fields[probeIndex + 7];
			probeCtC = fields[probeIndex + 8];
			probeCtD = fields[probeIndex + 9];
			probeCtE = fields[probeIndex + 10];
			probeCtSPC = fields[probeIndex + 11];
			
			probeEndptA = fields[probeIndex + 12];
			probeEndptB = fields[probeIndex + 13];
			probeEndptC = fields[probeIndex + 14];
			probeEndptD = fields[probeIndex + 15];
			probeEndptE = fields[probeIndex + 16];
			probeEndptSPC = fields[probeIndex + 17];
			
			
		}

		/*
		 public String toSMS() {
		
			String smsText = "";
			smsText += spaceIfNull(sampleId) + "^" + spaceIfNull(mtbResult) + "^" + spaceIfNull(rifResult);
			
			smsText += "^" + spaceIfNull(pcId) + "^" + spaceIfNull(operatorId)+ "^" + spaceIfNull(instrumentSerial)+ "^" + spaceIfNull(moduleId) + "^" + spaceIfNull(cartridgeId) +  "^" + spaceIfNull(reagentLotId)+ "^" + spaceIfNull(testEndDate);
			
			if(isFinal()!=null && isFinal())
				smsText += "^" + "yes";
			else
				smsText += "^" + "no";
			if(isPending()!=null && isPending())
				smsText += "^" + "yes";
			else
				smsText += "^" + "no";
			
			if(isError()!=null && isError()) {
				smsText += "^" + "yes";
				smsText += "^" + errorCode;
			}
			else
				smsText += "^" + "no";
			
			if(isCorrection()!=null && isCorrection())
				smsText += "^" + "yes";
			else
				smsText += "^" + "no";
			
			
			if(ControlPanel.props.getProperty("exportprobes").equalsIgnoreCase(ASTMMessageConstants.TRUE)) {
				smsText += "^" + spaceIfNull(probeResultA) +
				"^" + spaceIfNull(probeResultB) + "^" + spaceIfNull(probeResultC) + "^" + spaceIfNull(probeResultD) + "^" + spaceIfNull(probeResultE) +
				"^" + spaceIfNull(probeResultSPC) + "^" + spaceIfNull(probeCtA) + "^" + spaceIfNull(probeCtB) + "^" + spaceIfNull(probeCtC) +
				"^" + spaceIfNull(probeCtD) + "^" + spaceIfNull(probeCtE) + "^" + spaceIfNull(probeCtSPC) + "^" + spaceIfNull(probeEndptA) + 
				"^" + spaceIfNull(probeEndptB) + "^" + spaceIfNull(probeEndptB) + "^" + spaceIfNull(probeEndptC) + "^" + spaceIfNull(probeEndptD) +
				"^" + spaceIfNull(probeEndptE) + "^" + spaceIfNull(probeEndptSPC);
			}
			
			return smsText;
		
		 */
		Date resultDateObj = null;
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
				cal.set(Calendar.HOUR, Integer.parseInt(hour));
			else
				cal.set(Calendar.HOUR,0);
			if(minute!=null)
				cal.set(Calendar.MINUTE, Integer.parseInt(minute));
			else
				cal.set(Calendar.MINUTE,0);
			if(second!=null)
				cal.set(Calendar.SECOND, Integer.parseInt(second));
			cal.set(Calendar.SECOND,0);
			
			cal.set(Calendar.MILLISECOND,0);
			
			resultDateObj = cal.getTime();
			System.out.println("TIME" + resultDateObj.getTime());
			System.out.println(cal.get(Calendar.HOUR));
			System.out.println(cal.get(Calendar.MINUTE));
			System.out.println(cal.get(Calendar.SECOND));
			System.out.println(cal.get(Calendar.MILLISECOND));
			
			
			
			
			
		}
		
		//if(rPending!=null) {
			
			ServerServiceImpl ssl = new ServerServiceImpl();
			GeneXpertResults[] gxp = null;
			GeneXpertResults gxpNew = null;
//			GeneXpertResultsAuto gxp = null;
			try {
				gxp = ssl.findGeneXpertResults(sampleId,patientId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			EventHandler eh = new EventHandler();
			if(gxp==null || gxp.length==0) {
				//System.out.println("NOT FOUND");
				GeneXpertResults gxpU = eh.createGeneXpertResults(patientId,sampleId, mtb, rif, resultDate,instrumentSerial,moduleId,cartridgeId,reagentLotId,expDate, operatorId,pcId,probeResultA,probeResultB,probeResultC,probeResultD,probeResultE,probeResultSPC,probeCtA,probeCtB,probeCtC,probeCtD,probeCtE,probeCtSPC,probeEndptA,probeEndptB,probeEndptC,probeEndptD,probeEndptE,probeEndptSPC,errorCode,systemId);
				try {
					//ssl.updateGeneXpertResultsAuto(gxp, gxp.getIsPositive(),operatorId,pcId,instrumentSerial,moduleId,cartridgeId,reagentLotId);
					ssl.saveGeneXpertResults(gxpU);//, gxp.getIsPositive(),operatorId,pcId,instrumentSerial,moduleId,cartridgeId,reagentLotId);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
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
					}
					
					else {
						gxpNew.setGeneXpertResult(mtb);
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
			gxpNew.setCartridgeId(cartridgeId);
			gxpNew.setLaboratoryId(systemId);
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
	
			
		
		
	}
	
	public static void main (String args[]) {
		ResponseReaderJob rrj = new ResponseReaderJob();
		rrj.parseText("1223232^123456^ERROR^ERROR^ALITEST^Ding dong^34323fffd^erereffsddrere^test^PPPODLSS^20130101^no^no^no^no^POS^NEG^N/A^POS^NEG^POS^0.0^1.0^2.0^3.0^4.0^5.0^10.0^11.0^12.0^13.0^14.0^16.0");
	}
}
