package org.irdresearch.tbreach.xpertsms.net;

//import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
//import javax.swing.text.SimpleAttributeSet;
//import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

//import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMNetworkConstants;
import org.irdresearch.tbreach.xpertsms.constants.global.FileConstants;
import org.irdresearch.tbreach.xpertsms.model.XpertResultUploadMessage;
import org.irdresearch.tbreach.xpertsms.model.astm.XpertASTMResultUploadMessage;
import org.irdresearch.tbreach.xpertsms.parser.astm.ASTMProcessorThread;
import org.irdresearch.tbreach.xpertsms.net.HttpSender;
import org.irdresearch.tbreach.xpertsms.ui.ControlPanel;

public class ResultServer extends Thread {
	//create socket and listen for results from Xpert
	//when result arrives,
	//pass socket to newly spawned thread
		
	//go back to listening
	//private static Vector<String> messages;
	private ArrayBlockingQueue <String> messages;
	private ArrayBlockingQueue <XpertResultUploadMessage> outgoingMessages;
	private int threadCount;
	private int status;
	private String errorCode;
	private JTextPane monitorPane;
	private Boolean stopped;
	private ServerSocket socket;
	private PrintWriter csvWriter;
	
	private SimpleDateFormat logEntryFormatter = null;
	private SimpleDateFormat fileNameFormatter = null;;
	
	
	public ResultServer(JTextPane monitorPane) {
	
		messages = new ArrayBlockingQueue<String>(15);
		outgoingMessages = new ArrayBlockingQueue<XpertResultUploadMessage>(15);
		threadCount = 0;
		this.monitorPane = monitorPane;
		stopped = false;
		
		logEntryFormatter = new SimpleDateFormat(FileConstants.FILE_ENTRY_DATE_FORMAT);
		fileNameFormatter =  new SimpleDateFormat(FileConstants.FILE_NAME_DATE_FORMAT);
		
		File csv = new File(FileConstants.XPERT_SMS_DIR + System.getProperty("file.separator") + fileNameFormatter.format(new Date())+ "_xpertdump.csv");
		try {
			csvWriter = new PrintWriter(csv);
			String header="\"messageId\",\"systemId\",\"systemName\",\"softwareVersion\",\"receiverId\",\"processingId\",\"versionNumber\",\"messageDateTime\",\"instrumentSpecimenId\",\"universalTestId\",\"priority\",\"orderDateTime\",\"actionCode\",\"";
			header+="specimenType\",\"reportType\",\"systemDefinedTestName\",\"systemDefinedTestVersion\",\"resultStatus\",\"operatorId\",\"testStartDate\",\"testEndDate\",\"pcId\",\"instrumentSerial\",\"moduleId\",\"cartridgeId\",\"reagentLotId\",\"expDate\",\"";
			header+="isFinal\",\"isPending\",\"isError\",\"isCorrection\",\"errorCode\",\"patientId\",\"sampleId\",\"mtbResult\",\"rifResult\",\"probeResultA\",\"probeResultB\",\"probeResultC\",\"probeResultD\",\"";
			header+="probeResultE\",\"probeResultSPC\",\"probeCtA\",\"probeCtB\",\"probeCtC\",\"probeCtD\",\"probeCtE\",\"probeCtSPC\",\"probeEndptA\",\"probeEndptB\",\"probeEndptC\",\"probeEndptD\",\"probeEndptE\",\"probeEndptSPC\"";
			
			writeToCSV(header);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void run () {
		
		startServer();
		
		if(status==-1) {
			JOptionPane.showMessageDialog(null,errorCode,"Error occurred",JOptionPane.ERROR_MESSAGE);
		}
		
		else if(status==0) {
			JOptionPane.showMessageDialog(null,MessageCodes.SERVER_STOPPED,"Notification!",JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void startServer()  {
		socket = null;
		boolean propsLoaded = false;
		try {
			propsLoaded = loadProperties();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			errorCode = MessageCodes.ERROR_LOADING_PROPERTIES;
			status = -1;
		}
		
		if(!propsLoaded) {
			errorCode = MessageCodes.PROPERTIES_NOT_SET;
			status = -1;
		}
		
		//stopped = true;
		
	 	ASTMProcessorThread apt =  new ASTMProcessorThread(this);	
		apt.start();
		
		HttpSender hs = new HttpSender(this);
		hs.start();
		
		
		int port = Integer.parseInt(ControlPanel.props.getProperty("localport"));
		try {
			socket = new ServerSocket(port);
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("could not listen on port " + port);
			errorCode = MessageCodes.PORT_INACCESSIBLE;
			status = -1;
		}
		System.out.println("listening on port " + port);
		
		// TEST HTTP
		XpertASTMResultUploadMessage xpertMess = new XpertASTMResultUploadMessage();
		xpertMess.setCartridgeId("test");
		xpertMess.setInstrumentSerial("34323fffd");
		xpertMess.setMessageId("ef3s4333aab4");
		xpertMess.setModuleId("334fefac34223");
		xpertMess.setMtbResult("MTB DETECTED VERY LOW");
		xpertMess.setExpDate("20140401");
		xpertMess.setPcId("ALITEST");
		xpertMess.setProbeCtA("0.0");
		xpertMess.setProbeCtB("1.0");
		xpertMess.setProbeCtC("2.0");
		xpertMess.setProbeCtD("3.0");
		xpertMess.setProbeCtE("4.0");
		xpertMess.setProbeCtSPC("5.0");
		xpertMess.setProbeEndptA("10.0");
		xpertMess.setProbeEndptB("11.0");
		xpertMess.setProbeEndptC("12.0");
		xpertMess.setProbeEndptD("13.0");
		xpertMess.setProbeEndptE("14.0");
		xpertMess.setProbeEndptSPC("15.0");
		xpertMess.setProbeResultA("POS");
		xpertMess.setProbeResultB("NEG");
		xpertMess.setProbeResultC("N/A");
		xpertMess.setProbeResultD("POS");
		xpertMess.setProbeResultE("NEG");
		xpertMess.setProbeResultSPC("POS");
		xpertMess.setReagentLotId("1223239994");
		xpertMess.setRifResult("DETECTED");
		xpertMess.setSampleId("1410000016-6");//real
		
		xpertMess.setTestEndDate("20131125");
		xpertMess.setOperatorId("OWAIS");
		//xpertMess.setPatientId("111202180001-8");//harry potter
		xpertMess.setPatientId("1310000200-8");//fake patient
		
		xpertMess.setSystemId("CHS");
		putOutGoingMessage(xpertMess);
		
		//
		/*XpertASTMResultUploadMessage xpertMess2 = new XpertASTMResultUploadMessage();
		xpertMess2.setCartridgeId("test2");
		xpertMess2.setInstrumentSerial("34323fffd2");
		xpertMess2.setMessageId("ef3s4333aab4");
		xpertMess2.setModuleId("334fefac342232");
		xpertMess2.setMtbResult("MTB NOT DETECTED");
		xpertMess2.setExpDate("20140401");
		xpertMess2.setPcId("ALITEST");
		xpertMess2.setProbeCtA("0.0");
		xpertMess2.setProbeCtB("1.0");
		xpertMess2.setProbeCtC("2.0");
		xpertMess2.setProbeCtD("3.0");
		xpertMess2.setProbeCtE("4.0");
		xpertMess2.setProbeCtSPC("5.0");
		xpertMess2.setProbeEndptA("10.0");
		xpertMess2.setProbeEndptB("11.0");
		xpertMess2.setProbeEndptC("12.0");
		xpertMess2.setProbeEndptD("13.0");
		xpertMess2.setProbeEndptE("14.0");
		xpertMess2.setProbeEndptSPC("15.0");
		xpertMess2.setProbeResultA("POS");
		xpertMess2.setProbeResultB("NEG");
		xpertMess2.setProbeResultC("N/A");
		xpertMess2.setProbeResultD("POS");
		xpertMess2.setProbeResultE("NEG");
		xpertMess2.setProbeResultSPC("POS");
		xpertMess2.setReagentLotId("12232399942");
		//xpertMess2.setRifResult("DETECTED");
		xpertMess2.setSampleId("1410000015-8");//real
		
		xpertMess2.setTestEndDate("20131123");
		xpertMess2.setOperatorId("Jahangir");
		//xpertMess2.setPatientId("111202180001-8");//harry potter
		xpertMess2.setPatientId("1310000200-8");//fake patient
		
		xpertMess2.setSystemId("CHS");
		putOutGoingMessage(xpertMess2);*/
		
		//
		/*XpertASTMResultUploadMessage xpertMess3 = new XpertASTMResultUploadMessage();
		xpertMess3.setCartridgeId("test3");
		xpertMess3.setInstrumentSerial("34323fffd3");
		xpertMess3.setMessageId("ef3s4333aab43");
		xpertMess3.setModuleId("334fefac342233");
		xpertMess3.setMtbResult("MTB DETECTED HIGH");
		xpertMess3.setExpDate("20140401");
		xpertMess3.setPcId("ALITEST");
		xpertMess3.setProbeCtA("0.0");
		xpertMess3.setProbeCtB("1.0");
		xpertMess3.setProbeCtC("2.0");
		xpertMess3.setProbeCtD("3.0");
		xpertMess3.setProbeCtE("4.0");
		xpertMess3.setProbeCtSPC("5.0");
		xpertMess3.setProbeEndptA("10.0");
		xpertMess3.setProbeEndptB("11.0");
		xpertMess3.setProbeEndptC("12.0");
		xpertMess3.setProbeEndptD("13.0");
		xpertMess3.setProbeEndptE("14.0");
		xpertMess3.setProbeEndptSPC("15.0");
		xpertMess3.setProbeResultA("POS");
		xpertMess3.setProbeResultB("NEG");
		xpertMess3.setProbeResultC("N/A");
		xpertMess3.setProbeResultD("POS");
		xpertMess3.setProbeResultE("NEG");
		xpertMess3.setProbeResultSPC("POS");
		xpertMess3.setReagentLotId("12232399943");
		xpertMess3.setRifResult("NOT DETECTED");
		xpertMess3.setSampleId("1410000016-6");//real
		
		xpertMess3.setTestEndDate("20131123");
		xpertMess3.setOperatorId("Jahangir");
		//xpertMess3.setPatientId("111202180001-8");//harry potter
		xpertMess3.setPatientId("1310000200-8");//fake patient
		
		xpertMess3.setSystemId("CHS");
		putOutGoingMessage(xpertMess3);*/
		//writeToCSV(xpertMess.toCSV());*/
		
		/*String message1 = "H|@^\\|URM-tTjX+pTA-01||GeneXpert PC^GeneXpert^4.3|||||IRD_XPERT|| P|1394-97|20121020053034\n"
+ "P|1|||1621005001\nO|1|051012274||^^^G4v5|R|20121006153947|||||||||ORH||||||||||F\nR|1|^G4v5^^TBPos^Xpert MTB-RIF Assay G4^5^MTB^|MTB DETECTED VERY LOW^|||||F||Karachi X-ray|20121006153947|20121006172116|Cepheid3WDBYQ1^707851^615337^101256275^04405^20130106\n"
+ "R|2|^G4v5^^TBPos^^^Probe D^|POS^|||\nR|3|^G4v5^^TBPos^^^Probe D^Ct|^32.4|||\nR|4|^G4v5^^TBPos^^^Probe D^EndPt|^102.0|||\nR|5|^G4v5^^TBPos^^^Probe C^|POS^|||\nR|6|^G4v5^^TBPos^^^Probe C^Ct|^31.2|||\n"
+ "R|7|^G4v5^^TBPos^^^Probe C^EndPt|^130.0|||\nR|8|^G4v5^^TBPos^^^Probe E^|NEG^|||\nR|9|^G4v5^^TBPos^^^Probe E^Ct|^0|||\nR|10|^G4v5^^TBPos^^^Probe E^EndPt|^4.0|||\nR|11|^G4v5^^TBPos^^^Probe B^|POS^|||\nR|12|^G4v5^^TBPos^^^Probe B^Ct|^31.5|||\nR|13|^G4v5^^TBPos^^^Probe B^EndPt|^88.0|||\n"
+ "R|14|^G4v5^^TBPos^^^Probe A^|POS^|||\nR|15|^G4v5^^TBPos^^^Probe A^Ct|^30.7|||\nR|16|^G4v5^^TBPos^^^Probe A^EndPt|^92.0|||\nR|17|^G4v5^^TBPos^^^SPC^|NA^|||\nR|18|^G4v5^^TBPos^^^SPC^Ct|^28.1|||\nR|19|^G4v5^^TBPos^^^SPC^EndPt|^302.0|||\nR|20|^G4v5^^Rif^Xpert MTB-RIF Assay G4^5^Rif Resistance^|Rif Resistance DETECTED^|||||F||Karachi X-ray|20121006153947|20121006172116|Cepheid3WDBYQ1^707851^615337^101256275^04405^20130106"
+ "R|21|^G4v5^^Rif^^^Probe D^|POS^|||\nR|22|^G4v5^^Rif^^^Probe D^Ct|^32.4|||\nR|23|^G4v5^^Rif^^^Probe D^EndPt|^102.0|||\nR|24|^G4v5^^Rif^^^Probe C^|POS^|||\nR|25|^G4v5^^Rif^^^Probe C^Ct|^31.2|||\nR|26|^G4v5^^Rif^^^Probe C^EndPt|^130.0|||\nR|27|^G4v5^^Rif^^^Probe E^|NEG^|||\nR|28|^G4v5^^Rif^^^Probe E^Ct|^0|||\nR|29|^G4v5^^Rif^^^Probe E^EndPt|^4.0|||\n"
+ "R|30|^G4v5^^Rif^^^Probe B^|POS^|||\nR|31|^G4v5^^Rif^^^Probe B^Ct|^31.5|||\nR|32|^G4v5^^Rif^^^Probe B^EndPt|^88.0|||\nR|33|^G4v5^^Rif^^^Probe A^|POS^|||\nR|34|^G4v5^^Rif^^^Probe A^Ct|^30.7|||\nR|35|^G4v5^^Rif^^^Probe A^EndPt|^92.0|||\nR|36|^G4v5^^Rif^^^SPC^|NA^|||\nR|37|^G4v5^^Rif^^^SPC^Ct|^28.1|||\nR|38|^G4v5^^Rif^^^SPC^EndPt|^302.0|||\nR|39|^G4v5^^QC^Xpert MTB-RIF Assay G4^5^QC Check^|^|||||F||Karachi X-ray|20121006153947|20121006172116|Cepheid3WDBYQ1^707851^615337^101256275^04405^20130106\nR|40|^G4v5^^QC^^^QC-1^|NEG^|||\n"
+ "R|41|^G4v5^^QC^^^QC-1^Ct|^0|||\nR|42|^G4v5^^QC^^^QC-1^EndPt|^0|||\nR|43|^G4v5^^QC^^^QC-2^|NEG^|||\nR|44|^G4v5^^QC^^^QC-2^Ct|^0|||\nR|45|^G4v5^^QC^^^QC-2^EndPt|^0|||\nL|1|N";

		putMessage(message1);
		
		String message2 = "H|@^\\|URM-b+UY+pTA-02||GeneXpert PC^GeneXpert^4.3|||||IRD_XPERT||P|1394-97|20121020053357\n"
 + "P|1|||7761\nO|1|814913||^^^G4v5|R|20120913003937|||||||||ORH||||||||||F\nR|1|^G4v5^^TBPos^Xpert MTB-RIF Assay G4^5^MTB^|MTB DETECTED MEDIUM^|||||F||Sunil Asif|20120913003937|20120913022015|Cepheid3WDBYQ1^706593^611954^101259317^04405^20130106\nR|2|^G4v5^^TBPos^^^Probe D^|POS^|||\n"
 + "R|3|^G4v5^^TBPos^^^Probe D^Ct|^18.9|||\nR|4|^G4v5^^TBPos^^^Probe D^EndPt|^242.0|||\nR|5|^G4v5^^TBPos^^^Probe C^|POS^|||\nR|6|^G4v5^^TBPos^^^Probe C^Ct|^18.2|||\nR|7|^G4v5^^TBPos^^^Probe C^EndPt|^250.0|||\nR|8|^G4v5^^TBPos^^^Probe E^|NEG^|||\nR|9|^G4v5^^TBPos^^^Probe E^Ct|^0|||\nR|10|^G4v5^^TBPos^^^Probe E^EndPt|^-3.0|||\n"
 + "R|11|^G4v5^^TBPos^^^Probe B^|POS^|||\nR|12|^G4v5^^TBPos^^^Probe B^Ct|^19.2|||\nR|13|^G4v5^^TBPos^^^Probe B^EndPt|^133.0|||\nR|14|^G4v5^^TBPos^^^Probe A^|POS^|||\nR|15|^G4v5^^TBPos^^^Probe A^Ct|^17.5|||\nR|16|^G4v5^^TBPos^^^Probe A^EndPt|^156.0|||\nR|17|^G4v5^^TBPos^^^SPC^|NA^|||\nR|18|^G4v5^^TBPos^^^SPC^Ct|^27.4|||\n"
 + "R|19|^G4v5^^TBPos^^^SPC^EndPt|^333.0|||\nR|20|^G4v5^^Rif^Xpert MTB-RIF Assay G4^5^Rif Resistance^|Rif Resistance DETECTED^|||||F||Sunil Asif|20120913003937|20120913022015|Cepheid3WDBYQ1^706593^611954^101259317^04405^20130106\nR|21|^G4v5^^Rif^^^Probe D^|POS^|||\nR|22|^G4v5^^Rif^^^Probe D^Ct|^18.9|||\nR|23|^G4v5^^Rif^^^Probe D^EndPt|^242.0|||\nR|24|^G4v5^^Rif^^^Probe C^|POS^|||\n"
 + "R|25|^G4v5^^Rif^^^Probe C^Ct|^18.2|||\nR|26|^G4v5^^Rif^^^Probe C^EndPt|^250.0|||\nR|27|^G4v5^^Rif^^^Probe E^|NEG^|||\nR|28|^G4v5^^Rif^^^Probe E^Ct|^0|||\nR|29|^G4v5^^Rif^^^Probe E^EndPt|^-3.0|||\nR|30|^G4v5^^Rif^^^Probe B^|POS^|||\nR|31|^G4v5^^Rif^^^Probe B^Ct|^19.2|||\nR|32|^G4v5^^Rif^^^Probe B^EndPt|^133.0|||\nR|33|^G4v5^^Rif^^^Probe A^|POS^|||\nR|34|^G4v5^^Rif^^^Probe A^Ct|^17.5|||\n"
 + "R|35|^G4v5^^Rif^^^Probe A^EndPt|^156.0|||\nR|36|^G4v5^^Rif^^^SPC^|NA^|||\nR|37|^G4v5^^Rif^^^SPC^Ct|^27.4|||\nR|38|^G4v5^^Rif^^^SPC^EndPt|^333.0|||\nR|39|^G4v5^^QC^Xpert MTB-RIF Assay G4^5^QC Check^|^|||||F||Sunil Asif|20120913003937|20120913022015|Cepheid3WDBYQ1^706593^611954^101259317^04405^20130106\nR|40|^G4v5^^QC^^^QC-1^|NEG^|||\nR|41|^G4v5^^QC^^^QC-1^Ct|^0|||\nR|42|^G4v5^^QC^^^QC-1^EndPt|^0|||\n"
 + "R|43|^G4v5^^QC^^^QC-2^|NEG^|||\nR|44|^G4v5^^QC^^^QC-2^Ct|^0|||\nR|45|^G4v5^^QC^^^QC-2^EndPt|^0|||\nL|1|N";
		
		putMessage(message2);*/
		
		//TEST COMMENTED OUT
		try {
			while (!stopped) {
				
					threadCount++;
					updateTextPane(getLogEntryDateString(new Date()) + ": Waiting for GeneXpert\n");
					new ResultThread(socket.accept(), threadCount, this).start();
					updateTextPane(getLogEntryDateString(new Date()) + "Connected to GeneXpert\n");
			}
			
			if(stopped) {
				updateTextPane(getLogEntryDateString(new Date()) + "Stop message received\n");
			}
			if(!socket.isClosed())
				socket.close();
			status = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			if(stopped) {
				if(!socket.isClosed()) {
					try {
						socket.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					status =  0;
				}
			}
			else {
				status = -1;
				errorCode = MessageCodes.GENERAL_ERROR;
				e.printStackTrace();
			}
		}
	}
	
	public boolean loadProperties() throws FileNotFoundException,IOException {
		
		ControlPanel.props.load(new FileInputStream(FileConstants.FILE_PATH));
		
		String mtbCode = ControlPanel.props.getProperty("mtbcode");
    	String rifCode = ControlPanel.props.getProperty("rifcode");
    	String qcCode = ControlPanel.props.getProperty("qccode");
    	String serverURL = ControlPanel.props.getProperty("serverurl");
    	String serverPort = ControlPanel.props.getProperty("serverport");
    	String localPort = ControlPanel.props.getProperty("localport");
    	String exportProbes = ControlPanel.props.getProperty("exportprobes");
    	
    	if(mtbCode==null || mtbCode.length()==0 || rifCode==null || rifCode.length()==0 || qcCode==null || qcCode.length()==0 || localPort==null || localPort.length()==0 || exportProbes==null || exportProbes.length()==0)
    	{
    		return false;
    	}

		return true;
	}
	
	public ArrayBlockingQueue<String> getMessages() {
		return messages;
	}
	
	public void putMessage(String s) {
		try {
			messages.put(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getHead() {
		String s = null;
		try {
			s=  messages.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			s= null;
		}
		
		return s;
	}
	
	public void removeMessage(int index) {
		messages.remove(index);
	}
	
	/*public void addMessageAtIndex(String message, int index) {
		messages.add(index, message);
	}*/
	
	public int getMessageListSize() {
		return messages.size();
	}
	
	

	public ArrayBlockingQueue<XpertResultUploadMessage> getOutgoingMessages() {
		return outgoingMessages;
	}
	
	
	public int getOutgoingMessageListSize() {
		return outgoingMessages.size();
	}
	
	public void putOutGoingMessage(XpertResultUploadMessage message) {
		try {
			outgoingMessages.put(message);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public XpertResultUploadMessage getOutgoingMessagesHead() {
		XpertResultUploadMessage message = null;
		try {
			message = outgoingMessages.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			message = null;
		}
		
		return message;
	}
	

	/**
	 * @return the errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the monitorPane
	 */
	public JTextPane getMonitorPane() {
		return monitorPane;
	}

	/**
	 * @param monitorPane the monitorPane to set
	 */
	public void setMonitorPane(JTextPane monitorPane) {
		this.monitorPane = monitorPane;
	}
	
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the stopped
	 */
	public Boolean getStopped() {
		return stopped;
	}

	/**
	 * @param stopped the stopped to set
	 */
	public void setStopped(Boolean stopped) {
		this.stopped = stopped;
		System.out.println("Server " + stopped.toString());
		
	}

	/**
	 * @return the socket
	 */
	public ServerSocket getSocket() {
		return socket;
	}

	/**
	 * @param socket the socket to set
	 */
	public void setSocket(ServerSocket socket) {
		this.socket = socket;
	}

	public synchronized void updateTextPane(final String text) {
		
        SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
            	
            	
                StyledDocument doc = monitorPane.getStyledDocument();
                try {
                    doc.insertString(doc.getLength(), text, null);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
                monitorPane.setCaretPosition(doc.getLength() - 1);
            }
        });
    }
	
	public synchronized void writeToCSV(String text) {
		System.out.println(text);
		System.out.println("printing....");
		csvWriter.println(text);
		csvWriter.flush();
		System.out.println("printed");
	}
	
	public String getLogEntryDateString(Date date) {
		return logEntryFormatter.format(date);
	}
	
	public String getFileNameDateString(Date date) {
		return fileNameFormatter.format(date);
	}
}
