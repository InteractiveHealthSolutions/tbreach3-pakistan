package org.irdresearch.tbreach.xpertsms.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMNetworkConstants;

public class TestClient {
	
	public static void main (String[] args) {
		Socket socket = null;
		
		try {
			socket = new Socket("localhost",12221);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("connected to localhost:12222");
		PrintWriter out = null;
		try {
			out = new PrintWriter(socket.getOutputStream(),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("established output stream");
		
		out.println("H|@^\\|URM-Gq3kPdSA-01||CEPHEID^GeneXpert^2.1|||||LIS-1||P|1394-97|20100311153337");
		out.println("P|1|||");
		out.println("O|1|100303FIIFV2001HM+E4||^^^HemosIL|R|20100303114352|||||||||ORH||||||||||F");
		out.println("R|1|^HemosIL^^F2^Xpert HemosIL FII & FV^1^FII^|FII HOMOZYGOUS^|||||F||samir alsanady|20100303114352|20100303121429|Sheth-Opt745^704185^602023^26874780^02001^20110912");
		out.println("R|2|^HemosIL^^F2^^^FII 20210G^|NEG^|||");
		out.println("R|3|^HemosIL^^F2^^^FII 20210G^Ct|^0|||");
		out.println("R|4|^HemosIL^^F2^^^FII 20210G^EndPt|^10.0|||");
		out.println("R|5|^HemosIL^^F2^^^FII 20210A^|POS^|||");
		out.println("R|6|^HemosIL^^F2^^^FII 20210A^Ct|^24.5|||");
		out.println("R|7|^HemosIL^^F2^^^FII 20210A^EndPt|^455.0|||");
		out.println("R|8|^HemosIL^^F5^Xpert HemosIL FII & FV^1^FV^|FV HOMOZYGOUS^|||||F||samir alsanady|20100303114352|20100303121429|Sheth-Opt745^704185^602023^26874780^02001^20110912");
		out.println("R|9|^HemosIL^^F5^^^FV 1691G^|NEG^|||");
		out.println("R|10|^HemosIL^^F5^^^FV 1691G^Ct|^0|||");
		out.println("R|11|^HemosIL^^F5^^^FV 1691G^EndPt|^0.0|||");
		out.println("R|12|^HemosIL^^F5^^^FV 1691A^|POS^|||");
		out.println("R|13|^HemosIL^^F5^^^FV 1691A^Ct|^25.5|||");
		out.println("R|14|^HemosIL^^F5^^^FV 1691A^EndPt|^281.0|||");
		out.println("L|1|N");
		System.out.println("Data written");
		try {
			//socket.setSoLinger(true, 60);
			out.close();
			socket.close();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
