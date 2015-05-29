package org.irdresearch.tbreach.mobileevent;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class StarterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		
		
		
		
//		//setup email server
//		try {
//			System.out.println(">>>>INSTANTIATING EMAIL ENGINE......");
//			
//			EmailEngine.instantiateEmailEngine(prop);
//			
//			System.out.println("......EMAIL ENGINE STARTED SUCCESSFULLY......".toLowerCase());
//			LoggerUtil.logIt("......EMAIL ENGINE STARTED SUCCESSFULLY......");
//		} 
//		catch (EmailException e) {
//			throw new ServletException("Loading EmailEngine threw exception",e);
//		}
//		
		System.out.println(">>>>INSTANTIATING AUTOMATED SERVICES......");
		SMSerSystem.instantiateSmserSystem();
	}
}
