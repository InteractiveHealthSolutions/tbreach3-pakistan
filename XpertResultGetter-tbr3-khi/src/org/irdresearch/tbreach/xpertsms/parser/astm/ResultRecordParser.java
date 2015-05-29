package org.irdresearch.tbreach.xpertsms.parser.astm;

import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMMessageConstants;
import org.irdresearch.tbreach.xpertsms.model.astm.XpertASTMResultUploadMessage;
import org.irdresearch.tbreach.xpertsms.net.exception.astm.InvalidASTMMessageFormatException;
import org.irdresearch.tbreach.xpertsms.ui.ControlPanel;

public class ResultRecordParser extends BaseParser {
	
	private boolean isMTBResult;
	private boolean isRifResult;
	
	public ResultRecordParser(XpertASTMResultUploadMessage record, String messageString) {
		super(record,messageString);
		isMTBResult = false;
		isRifResult = false;
	}
	
	public void parse() throws InvalidASTMMessageFormatException {
		
		
		boolean isProbe = false;
		
		String[] fields = messageString.split("\\" + record.getFieldDelimiter());
		
		if(fields.length > ASTMMessageConstants.MAX_NUM_RESULT_FIELDS || fields.length < ASTMMessageConstants.MIN_NUM_RESULT_FIELDS) {
			throw new InvalidASTMMessageFormatException("R001 - Result record must have not more than " + ASTMMessageConstants.MAX_NUM_RESULT_FIELDS + " fields");
		}
	
		//at this point we are only dealing with the main results and not the probes
		if(fields.length == ASTMMessageConstants.NUM_PROBE_RESULT_FIELDS) {
			
			isProbe = true;
		}
		
		
		if (!isProbe) {
			setUniversalTestId(fields[2]);

			// In case of QC
			if (!isMTBResult() && !isRifResult())
				return;

			setResultValue(fields[3]);
			setResultStatus(fields[8]);

			if (fields[10].length() != 0)
				setOperatorId(fields[10]);

			if (fields[11].length() != 0)
				setTestStartDate(fields[11]);

			if (fields[12].length() != 0)
				setTestEndDate(fields[12]);

			if (fields[13].length() != 0)
				setInstrumentData(fields[13]);
		}
		
		else if (ControlPanel.props.getProperty("exportprobes").equalsIgnoreCase(ASTMMessageConstants.TRUE)){
			//deal with probe

			setProbeResult(fields[2],fields[3]);
			
		}
	}
	
	private void setUniversalTestId(String field) throws InvalidASTMMessageFormatException {
		String universalTestId[] = field.split("\\" + record.getComponentDelimiter());
		
	    if(universalTestId.length < 7) {
	    	throw new InvalidASTMMessageFormatException("R002 - Universal Test Id must contain at least 7 fields");
	    }
	    
	    
	    //if the result isn't MTB or Rif, return
	    if(universalTestId[3].equals(ControlPanel.props.getProperty("mtbcode"))) {
	    	isMTBResult = true;
	    	isRifResult = false;
	    }
	    
	    else if(universalTestId[3].equals(ControlPanel.props.getProperty("rifcode"))) {
	    	isMTBResult = false;
	    	isRifResult = true;
	    }
	    
	    else
	    	return;
	    
	    if(universalTestId[1].length() > ASTMMessageConstants.MAX_UNIVERSAL_TEST_ID_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("R003 - Universal Test Id must contain at most " + ASTMMessageConstants.MAX_UNIVERSAL_TEST_ID_LENGTH + " characters");
	    }
	    
	    if(record.getUniversalTestId()==null)
	    	record.setUniversalTestId(universalTestId[1]);
	    
	    if(universalTestId[3].length() > ASTMMessageConstants.MAX_SYSTEM_DEFINED_TEST_ID_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("R004 - System defined test Id must contain at most " + ASTMMessageConstants.MAX_SYSTEM_DEFINED_TEST_ID_LENGTH + " characters");
	    }
	    
	    
	    
	    if(universalTestId[4].length() > ASTMMessageConstants.MAX_SYSTEM_DEFINED_TEST_NAME_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("R005 - System defined test name must contain at most " + ASTMMessageConstants.MAX_SYSTEM_DEFINED_TEST_NAME_LENGTH + " characters");
	    }
	    
	    record.setSystemDefinedTestName(universalTestId[4]);
	    
	    if(universalTestId[5].length() > ASTMMessageConstants.MAX_SYSTEM_DEFINED_TEST_VERSION_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("R006 - System defined test version must contain at most " + ASTMMessageConstants.MAX_SYSTEM_DEFINED_TEST_VERSION_LENGTH + " characters");
	    }
	    
	    record.setSystemDefinedTestVersion(universalTestId[5]);
	    
	    //don't need to get into analyte name because we have the code
	    /*if(universalTestId[6].length() > ASTMMessageConstants.MAX_ANALYTE_NAME_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("R007 - System defined test name must contain at most " + ASTMMessageConstants.MAX_ANALYTE_NAME_LENGTH + " characters");
	    }*/
	    
	}
	
	
	private void setResultValue(String field) throws InvalidASTMMessageFormatException {
		
		String result[] = field.split("\\" + record.getComponentDelimiter());
		
		if(result.length==0)
			return;
		
	    if(result.length >  2) {
	    	throw new InvalidASTMMessageFormatException("R007 - Result Measurement Value must contain at most 2 fields");
	    }
	    
	    if(result[0].length() > ASTMMessageConstants.MAX_QUAL_VALUE_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("R008 - Test result name must contain at most " + ASTMMessageConstants.MAX_QUAL_VALUE_LENGTH + " characters");
	    }
	    
	    if(isMTBResult()) {
	    	record.setMtbResult(result[0]);
	    }
	    
	    else if(isRifResult()) {
	    	record.setRifResult(result[0]);
	    }
	    
	    
	}

	private void setResultStatus(String field) throws InvalidASTMMessageFormatException {
		String status[] = field.split("\\" + record.getRepeatDelimiter());
		char stat;
		for(int i=0;i<status.length;i++) {
			if(status[i].length() > ASTMMessageConstants.MAX_RESULT_STATUS_LENGTH) {
				throw new InvalidASTMMessageFormatException("R009 - Each component of Result Status must contain at most " + ASTMMessageConstants.MAX_RESULT_STATUS_LENGTH + " characters");
			}
			
			stat = status[i].charAt(0);
			
			switch (stat) {
				case ASTMMessageConstants.FINAL_RESULT:
					record.setFinal(true);
					break;
				
				case ASTMMessageConstants.PENDING_RESULT:
					record.setPending(true);
					break;
					
				case ASTMMessageConstants.ERROR_RESULT:
					record.setError(true);
					break;
					
				case ASTMMessageConstants.CORRECTED_RESULT:
					record.setCorrection(true);
					break;
			};
			
			
		}
	}
	
	private void setOperatorId(String field) throws InvalidASTMMessageFormatException {
		 if(field.length() > ASTMMessageConstants.MAX_OPERATOR_ID_LENGTH) {
		    	throw new InvalidASTMMessageFormatException("R010 - Operator ID must be at most " + ASTMMessageConstants.MAX_OPERATOR_ID_LENGTH + " characters");
		    }
		 
		 record.setOperatorId(field);
	}
	
	private void setTestStartDate(String field) throws InvalidASTMMessageFormatException {
		if(field.length() > ASTMMessageConstants.MAX_DATE_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("R011 - Start date must be at most " + ASTMMessageConstants.MAX_DATE_LENGTH + " characters");
	    }
	 
		record.setTestStartDate(field);
		
	}
	
	private void setTestEndDate(String field) throws InvalidASTMMessageFormatException {
		if(field.length() > ASTMMessageConstants.MAX_DATE_LENGTH) {
	    	throw new InvalidASTMMessageFormatException("R012 - End date must be at most " + ASTMMessageConstants.MAX_DATE_LENGTH + " characters");
	    }
	 
		record.setTestEndDate(field);
	}
	
	private void setInstrumentData(String field) throws InvalidASTMMessageFormatException {
		String instData[] = field.split("\\" + record.getComponentDelimiter());
		
		if(instData.length > 0) {
			record.setPcId(instData[0]);
		}
		
		if(instData.length > 1) {
			record.setInstrumentSerial(instData[1]);
		}
		
		if(instData.length > 2) {
			record.setModuleId(instData[2]);
		}
		
		if(instData.length > 3) {
			record.setCartridgeId(instData[3]);
		}
		
		if(instData.length > 4) {
			record.setReagentLotId(instData[4]);
		}
		
		if(instData.length > 5) {
			record.setExpDate(instData[5]);
		}
	}
	
	public void setProbeResult(String type,String result) throws InvalidASTMMessageFormatException {
		String[] typeList = type.split("\\" + record.getComponentDelimiter());
		String[] resultList = result.split("\\" + record.getComponentDelimiter());
		
		/*R|2|^G4v5^^TBPos^^^Probe D^|NO RESULT^|||
		R|3|^G4v5^^TBPos^^^Probe D^Ct|^0|||
		R|4|^G4v5^^TBPos^^^Probe D^EndPt|^0|||*/
		
		String probeName = typeList[6];
		
		boolean isMainProbeName = false;
		if(typeList.length==7) {
			isMainProbeName = true;
		}
		
		if(probeName.equals(ASTMMessageConstants.SPC)) {
			if(isMainProbeName) {
				if(record.getProbeResultSPC()==null) {
					record.setProbeResultSPC(resultList[0]);
				}
				
				return;
			}
			
			else {
				if(typeList[7].equals(ASTMMessageConstants.CT) && record.getProbeCtSPC()==null) {
					record.setProbeCtSPC(resultList[1]);
				}
				
				else if(typeList[7].equals(ASTMMessageConstants.ENDPT) && record.getProbeEndptSPC()==null) {
					record.setProbeEndptSPC(resultList[1]);
				}
				
				return;
			}
		}
		
		else if(probeName.equalsIgnoreCase(ASTMMessageConstants.PROBE_A)) {
			if(isMainProbeName) {
				if(record.getProbeResultA()==null) {
					record.setProbeResultA(resultList[0]);
				}
				
				return;
			}
			
			else {
				if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.CT) && record.getProbeCtA()==null) {
					record.setProbeCtA(resultList[1]);
				}
				
				else if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.ENDPT) && record.getProbeEndptA()==null) {
					record.setProbeEndptA(resultList[1]);
				}
				
				return;
			}
		}
		
		else if(probeName.equalsIgnoreCase(ASTMMessageConstants.PROBE_B)) {
			if(isMainProbeName) {
				if(record.getProbeResultB()==null) {
					record.setProbeResultB(resultList[0]);
				}
				
				return;
			}
			
			else {
				if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.CT) && record.getProbeCtB()==null) {
					record.setProbeCtB(resultList[1]);
				}
				
				else if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.ENDPT) && record.getProbeEndptB()==null) {
					record.setProbeEndptB(resultList[1]);
				}
				
				return;
			}
		}
		
		else if(probeName.equalsIgnoreCase(ASTMMessageConstants.PROBE_C)) {
			if(isMainProbeName) {
				if(record.getProbeResultC()==null) {
					record.setProbeResultC(resultList[0]);
				}
				
				return;
			}
			
			else {
				if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.CT) && record.getProbeCtC()==null) {
					record.setProbeCtC(resultList[1]);
				}
				
				else if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.ENDPT) && record.getProbeEndptC()==null) {
					record.setProbeEndptC(resultList[1]);
				}
				
				return;
			}
		}
		
		else if(probeName.equalsIgnoreCase(ASTMMessageConstants.PROBE_D)) {
			if(isMainProbeName) {
				if(record.getProbeResultD()==null) {
					record.setProbeResultD(resultList[0]);
				}
				
				return;
			}
			
			else {
				if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.CT) && record.getProbeCtD()==null) {
					record.setProbeCtD(resultList[1]);
				}
				
				else if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.ENDPT) && record.getProbeEndptD()==null) {
					record.setProbeEndptD(resultList[1]);
				}
				
				return;
			}
		}
		
		else if(probeName.equalsIgnoreCase(ASTMMessageConstants.PROBE_E)) {
			if(isMainProbeName) {
				if(record.getProbeResultE()==null) {
					record.setProbeResultE(resultList[0]);
				}
				
				return;
			}
			
			else {
				if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.CT) && record.getProbeCtE()==null) {
					record.setProbeCtE(resultList[1]);
				}
				
				else if(typeList[7].equalsIgnoreCase(ASTMMessageConstants.ENDPT) && record.getProbeEndptE()==null) {
					record.setProbeEndptE(resultList[1]);
				}
				
				return;
			}
		}
	}
	
	
	

	/**
	 * @return the isMTBResult
	 */
	public boolean isMTBResult() {
		return isMTBResult;
	}

	/**
	 * @param isMTBResult the isMTBResult to set
	 */
	public void setMTBResult(boolean isMTBResult) {
		this.isMTBResult = isMTBResult;
	}

	/**
	 * @return the isRifResult
	 */
	public boolean isRifResult() {
		return isRifResult;
	}

	/**
	 * @param isRifResult the isRifResult to set
	 */
	public void setRifResult(boolean isRifResult) {
		this.isRifResult = isRifResult;
	}
	
	
}
