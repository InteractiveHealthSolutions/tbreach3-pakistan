/**
 * Interface implemented by all Forms 
 */

package com.ihsinformatics.tbr3.fieldmonitoring.client;

import com.google.gwt.event.dom.client.ClickEvent;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public interface IForm
{
	void clearUp ();

	boolean validate ();

	void saveData ();

	void updateData ();

	void deleteData ();

	void fillData ();

	void setCurrent();

	void setRights (String menuName);
	
	void onClick (ClickEvent event);
}
