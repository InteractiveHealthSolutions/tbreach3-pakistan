/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.

Contributors: Owais Hussain */

package com.ihsinformatics.fieldmonitoring_mobile.custom;

import android.content.Context;
import android.widget.CheckBox;

import com.ihsinformatics.fieldmonitoring_mobile.R;

/**
 * Custom CheckBox view for ease
 * 
 * @author owais.hussain@irdresearch.org
 * 
 */
public class MyCheckBox extends CheckBox {
	public MyCheckBox(Context context) {
		super(context);
	}

	/**
	 * 
	 * @param context
	 * @param style
	 *            Style Id from resources. Pass -1 to keep default style
	 * @param text
	 *            Text Id from resources. Pass -1 if not to be set
	 * @param checked
	 *            Initial checked state
	 */
	public MyCheckBox(Context context, int tag, int style, int text,
			boolean checked) {
		super(context);
		if (tag != -1) {
			setTag(getResources().getString(tag));
		}
		if (style != -1) {
			setTextAppearance(context, style);
		}
		if (text != -1) {
			setText(text);
		}
		setChecked(checked);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			setTextColor(getResources().getColor(R.color.DeepSkyBlue));
		} else {
			setTextColor(getResources().getColor(R.color.DarkGray));
		}
	}

}
