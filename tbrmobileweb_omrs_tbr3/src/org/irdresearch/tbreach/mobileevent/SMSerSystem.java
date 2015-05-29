package org.irdresearch.tbreach.mobileevent;

import java.util.Timer;

public class SMSerSystem 
{
	private static SMSerSystem _instance;

	private SMSerSystem(){
		
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	public static void instantiateSmserSystem() {
		if(_instance == null)
		{
			_instance = new SMSerSystem();
			
			Timer responseReaderTimer = new Timer();
			responseReaderTimer.schedule(new ResponseReaderJob(), 1000*60*5, 1000*60*5);
		}
		else
		{
			throw new InstantiationError("An instance of SmserSystem already exists");
		}
	}
	
	public static SMSerSystem getInstance() {
		if(_instance == null){
			throw new InstantiationError("SmserSystem not instantiated");
		}
		return _instance;
	}
}
