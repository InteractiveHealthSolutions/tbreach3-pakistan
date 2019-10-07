/**
 * Interface implemented by all Report Forms 
 */

package com.ihsinformatics.tbr3.fieldmonitoring.client;

import com.google.gwt.event.dom.client.ClickEvent;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public interface IReport
{
	void clearUp ();

	boolean validate ();

	void viewData (boolean export);

	void setRights (String menuName);

	void onClick (ClickEvent event);
}
