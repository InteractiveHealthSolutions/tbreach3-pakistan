package org.irdresearch.tbreach.mobileevent;

import java.nio.charset.Charset;
import java.util.Calendar;

import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;

public class addDataTester {

	public static void main(String[] args)
	{
		TarseelServices sc = TarseelContext.getServices();
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, -1);
		c.add(Calendar.MINUTE, 1);

		for (int i = 0; i < 900; i++)
		{
			//System.out.println(i%20);
			//if(i%30==0)
				//c.add(Calendar.MINUTE, 1);
		//o.setSystemProcessingEndDate(systemProcessingEndDate);
		//o.setSystemProcessingStartDate(systemProcessingStartDate);
		String text = new String((i+"huuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuugeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee").getBytes(), Charset.forName("UTF-8"));
		System.out.println(text);
		String cellNum = "03333647535";
		if(i%10 == 0)
			cellNum = "03343872951";
		if(i%5 == 0)
			cellNum = "03158225726";
		
		sc.getSmsService().createNewOutboundSms(cellNum, text, c.getTime(), Priority.HIGH, 24, PeriodType.HOUR, 2, null);
		sc.commitTransaction();

		}
		sc.closeSession();
		
		

	}
}
