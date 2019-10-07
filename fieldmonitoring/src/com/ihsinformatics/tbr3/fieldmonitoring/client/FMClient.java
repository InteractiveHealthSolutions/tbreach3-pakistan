/**
 * Provides various Client-side methods used in the Application
 */

package com.ihsinformatics.tbr3.fieldmonitoring.client;

import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public final class FMClient
{
	/**
	 * Creates a 'long' code for a given string using some mathematics
	 * 
	 * @param string
	 * @return
	 */
	public static long getSimpleCode (String string)
	{
		long code = 1;
		for (int i = 0; i < string.length (); i++)
			code *= string.charAt (i);
		return code;
	}

	/**
	 * Verifies whether client has entered a valid pass code (required for some
	 * sensitive operations)
	 * 
	 * @return
	 */
	public static boolean verifyClientPasscode (String passcode)
	{
		try
		{
			String storedPasscode = Cookies.getCookie ("Pass");
			long passedCode = getSimpleCode (passcode.substring (0, 3));
			long existing = Long.parseLong (storedPasscode);
			return (passedCode == existing);
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			return false;
		}
	}

	/**
	 * Get usually desired value from a widget
	 * 1. Text fields return their respective text
	 * 2. List boxes return selected value
	 * 
	 * @param widget
	 * @return
	 */
	public static String get (Widget widget)
	{
		try
		{
			if (widget instanceof TextBoxBase)
				return ((TextBoxBase) widget).getText ();
			if (widget instanceof ListBox)
				return ((ListBox) widget).getValue (((ListBox) widget).getSelectedIndex ());
			if (widget instanceof ValueBoxBase<?>)
				return ((ValueBoxBase<?>) widget).getText ();
		}
		catch (Exception e)
		{
		}
		return "";
	}

	/**
	 * Get usually desired value from a widget
	 * 1. Text fields return their respective text
	 * 2. List boxes return selected value
	 * 
	 * @param listBox
	 * @return
	 */
	public static String getKey (ListBox listBox)
	{
		try
		{
			return FM.getDefinitionKey (listBox.getName (), get (listBox));
		}
		catch (Exception e)
		{
			return "";
		}
	}

	/**
	 * Get index of a given value from a widget (probably ListBox)
	 * 
	 * @param widget
	 * 
	 * @param value
	 * @return
	 */
	public static int getIndex (Widget widget, String value)
	{
		if (widget instanceof ListBox)
		{
			ListBox listBox = (ListBox) widget;
			for (int i = 0; i < listBox.getItemCount (); i++)
				if (listBox.getValue (i).equalsIgnoreCase (value))
					return i;
		}
		return -1;
	}

	/**
	 * Fill a widget with values from definition based on its "name" property.
	 * This method also sets current index of a widget to default (if exists in defaults)
	 * 
	 * @param widget
	 * @return
	 */
	public static Widget fillList (Widget widget)
	{
		if (widget instanceof ListBox)
		{
			ListBox listBox = (ListBox) widget;
			String name = listBox.getName ();
			listBox.clear ();
			String[] values = FM.getDefinitionValues (name);
			for (String s : values)
				listBox.addItem (s, FM.getDefinitionKey (name, s));
			String defaultValue = FM.getDefinitionValue (name, FM.getDefaultValue (name));
			listBox.setSelectedIndex (FMClient.getIndex (listBox, defaultValue));
			return listBox;
		}
		return widget;
	}
}
