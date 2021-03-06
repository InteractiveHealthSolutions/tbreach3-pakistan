/**
 * Custom EditText view for ease
 */

package org.irdresearch.tbr3mobile.custom;

import android.content.Context;
import android.text.InputFilter;
import android.widget.EditText;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class MyEditText extends EditText
{
	public MyEditText (Context context)
	{
		super (context);
	}

	/**
	 * 
	 * @param context
	 * @param tag
	 *            Text Id from resources. Pass -1 if no tag is to be set
	 * @param hint
	 *            Text Id from resources. Pass -1 if no hint is to be set
	 * @param inputType
	 *            InputType enum value
	 * @param style
	 *            Style Id from resources. Pass -1 to keep default style
	 * @param maxLength
	 *            Limit of number of maximum characters. Pass 0 for unlimited
	 */
	public MyEditText (Context context, int tag, int hint, int inputType, int style, int maxLength, boolean multiline)
	{
		super (context);
		setInputType (inputType);
		if (style != -1)
		{
			setTextAppearance (context, style);
		}
		if (tag != -1)
		{
			setTag (getResources ().getString (tag));
		}
		if (hint != -1)
		{
			setHint (hint);
		}
		if (maxLength > 0)
		{
			setFilters (new InputFilter[] {new InputFilter.LengthFilter (maxLength)});
		}
		if (!multiline)
		{
			setMaxLines (1);
		}
	}
}
