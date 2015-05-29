package org.irdresearch.tbreach.xpertsms.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMNetworkConstants;
import org.irdresearch.tbreach.xpertsms.constants.global.FileConstants;
import org.irdresearch.tbreach.xpertsms.model.XpertResultUploadMessage;
import org.irdresearch.tbreach.xpertsms.ui.ControlPanel;

public class ResultThread extends Thread {
	
	private Socket socket = null;
	int threadCount;
	//private XpertASTMResultUploadMessage xpertMessage;
	private PrintWriter pw;
	private int frameCounter;
	private ResultServer server;
	
	public ResultThread(Socket socket, int threadCount, ResultServer server) {
		super("resultthread");
		this.threadCount = threadCount;
		this.socket = socket;
		pw = null;
		frameCounter = 0;
		this.server = server;
		// = new XpertASTMResultUploadMessage();
		//messages = new ArrayList<String>();
	}
	
	public void run() {
	
		
		
		try {
			pw = new PrintWriter(new File(FileConstants.XPERT_SMS_DIR + System.getProperty("file.separator") + "log_t" + threadCount + ".txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		println(pw,"Establishing XPertSMS connection",true);
		
		try {
			
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			println(pw,"Established XpertSMS connection",true);
			
			boolean etxRecvd = false;
			byte b;
			byte[] textByte = new byte[1];
			String text = "";
			String message = "";
			//TODO CHECK THIS
			println(pw,"reading",true);
			b = dis.readByte();
			
			System.out.print(b);
			
						
			//Establishment phase ends
			
			//Contention phase won't happen because receiver never sends ENQ
			
			//Messages
			
			while(!server.getStopped() && (b = dis.readByte())!=ASTMNetworkConstants.NULL) {
				
				if (b==ASTMNetworkConstants.ENQ) {
					//println(pw,"ENQ received");
					dos.write(ASTMNetworkConstants.ACK);
					//println(pw,"ACK sent");
				}

				
				else if(b==ASTMNetworkConstants.STX) {
					//println(pw,"STX received");
					 //start picking up text until ETB or ETX at which point break
					dis.readByte();
				   frameCounterPlusPlus();
				   
				   while(!server.getStopped()) {
					   textByte[0] = dis.readByte();
					   //System.out.println("received text " + textByte[0]);
					   if(textByte[0]==ASTMNetworkConstants.ETB || textByte[0]==ASTMNetworkConstants.ETX){
						   b = textByte[0];
						   break;
					   }
					   
					   text += new String(textByte);
				   }
				  	
				}
				
				if(b==ASTMNetworkConstants.ETB) {
					//expect next frame
					//println(pw,"ETB received");
				}
				
				else if(b==ASTMNetworkConstants.ETX) {
					//println(pw,"ETX received");
					etxRecvd = true;
					//last frame
				}
				
				else if(b==ASTMNetworkConstants.CR) {
					//println(pw,"CR received");
					//expect LF
					
					if((b=dis.readByte())==ASTMNetworkConstants.LF){//if LF end, else keep going
						//println(pw,"LF Received");
						
						message += text;
						text = "";
						 
						
						 if(etxRecvd) {
							 //dos.write(NetworkConstants.EOT);
							 
							 etxRecvd = false;
							 //process();
						 }
						 
							 dos.write(ASTMNetworkConstants.ACK);
					}
				}
				
				else if (b==ASTMNetworkConstants.EOT){
					//break;
					//println(pw,"EOT Received.");
					
					//
					//println(pw,"message\n" + message);
					
					 server.putMessage(message);
					
					 println(pw, "Result Received",true);
					 message = "";
					
				}
				
				else {
					System.out.println("Other byte: " + b);
					
				}

			
			}
			if(server.getStopped()) {
				println(pw, "Stop Message Received!",true);
			}
			
			if(socket!=null)
				socket.close();
			
			message = null;
			println(pw,"Clean up completed",true);
			pw.flush();
			pw.close();
			
			}
		
			catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	}

	public void frameCounterPlusPlus() {
		frameCounter = frameCounter + 1;
		if(frameCounter == 8)
			frameCounter = 0;
	}
	
	public void print(PrintWriter pw, String text, boolean toGUI) {
		
		pw.print(server.getLogEntryDateString(new Date()) + ": " + text);
		pw.flush();
		if(toGUI)
			server.updateTextPane(server.getLogEntryDateString(new Date()) + ": " + text);
	}
	
	public void println(PrintWriter pw, String text, boolean toGUI) {
		
		print(pw,text + "\n",toGUI);
	}
	
	
	
	
}

