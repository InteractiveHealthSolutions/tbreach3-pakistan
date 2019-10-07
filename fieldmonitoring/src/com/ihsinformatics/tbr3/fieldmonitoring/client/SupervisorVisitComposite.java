/**
 * Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 * Contributors: Tahira Niazi
*/
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.Date;
import java.util.Iterator;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.client.FirstVisitComposite;
import com.ihsinformatics.tbr3.fieldmonitoring.client.FMClient;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;

/**
 * @author Tahira
 *
 */
public class SupervisorVisitComposite extends Composite implements IForm, ClickHandler, BlurHandler
{
	private static final String formName = "SUPER_VIS";
	private boolean valid;

	FlexTable flexTable = new FlexTable();
	FlexTable topFlexTable = new FlexTable();
	FlexTable supervisorVisitFlexTable = new FlexTable();
	FlexTable bottomFlexTable = new FlexTable();
	AbsolutePanel absolutePanel = new AbsolutePanel();

	private final String[] hourStrings = { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23" };
	private final String[] minuteStrings = { "00", "05", "10", "15", "20",
			"25", "30", "35", "40", "45", "50", "55" };
	
	Label formTitleLabel = new Label(FM.getProjectTitle () + " Supervisor Visit");

	private Label formDateLabel = new Label("Form Date   ");
	private DateBox formDateBox = new DateBox();

	private Label locationIdLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();

	private TextBox locationNameTextBox = new TextBox();
	private Label locationNameLabel = new Label("Location Name   ");

	private Label visitDateLabel = new Label("Visit Date");
	private DateBox visitDateBox = new DateBox();

	private Label visitTimeLabel = new Label("Visit Time");

	// visit time controls
	private AbsolutePanel visitTimePanel = new AbsolutePanel();
	private ListBox visitHourListBox = new ListBox();
	private ListBox visitMinuteListBox = new ListBox();

	private Label townLabel = new Label("Town");
	private ListBox townListBox = new ListBox();

	private Label gpPotentialLabel = new Label("GP Potential");
	private ListBox gpPotentialListBox = new ListBox();

	private Label mrLastVisitLabel = new Label("MR Last Visit");
	private DateBox mrLastVisitDateBox = new DateBox();

	private Label relationshipLabel = new Label("Relationship of MR/GP");
	private TextArea relationshipTextArea = new TextArea();

	private Label locationIdHintLabel = new Label("(Format: 101999)");

	Button submitButton = new Button("Submit");
	Button clearButton = new Button("Clear");
	
	public SupervisorVisitComposite() {
		
		initWidget(flexTable);
		flexTable.setSize("80%", "100%");
		
		flexTable.setWidget(0, 0, topFlexTable);
		topFlexTable.setWidth("650");
		formTitleLabel.setStyleName("title");
		
		topFlexTable.setWidget(0, 0, formTitleLabel);
		topFlexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		flexTable.setWidget(1, 0, supervisorVisitFlexTable);
		supervisorVisitFlexTable.setWidth("650");
		
		supervisorVisitFlexTable.setWidget(0, 0, formDateLabel);
		supervisorVisitFlexTable.setWidget(0, 1, formDateBox);
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		supervisorVisitFlexTable.setWidget(1, 0, locationIdLabel);
		supervisorVisitFlexTable.setWidget(1, 1, locationIdIntegerBox);
		locationIdIntegerBox.setWidth("100px");
		locationIdIntegerBox.setMaxLength(6);
		locationIdIntegerBox.setVisibleLength(6);
		supervisorVisitFlexTable.setWidget(1, 2, locationIdHintLabel);
		locationIdHintLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.setWidget(2, 0, locationNameLabel);
		supervisorVisitFlexTable.setWidget(2, 1, locationNameTextBox);
		locationNameTextBox.setEnabled(false);
		supervisorVisitFlexTable.setWidget(3, 0, townLabel);
		townListBox.setName("TOWN");
		supervisorVisitFlexTable.setWidget(3, 1, townListBox);
		supervisorVisitFlexTable.setWidget(4, 0, visitDateLabel);
		supervisorVisitFlexTable.setWidget(4, 1, visitDateBox);
		visitDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));
		supervisorVisitFlexTable.setWidget(5, 0, visitTimeLabel);

		visitTimePanel.add(visitHourListBox);
		visitTimePanel.add(visitMinuteListBox);

		visitHourListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		visitMinuteListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		supervisorVisitFlexTable.setWidget(5, 1, visitTimePanel);
		supervisorVisitFlexTable.setWidget(6, 0, gpPotentialLabel);
		supervisorVisitFlexTable.setWidget(6, 1, gpPotentialListBox);
		gpPotentialListBox.setName("GP_POTENTIAL_RATING");

		supervisorVisitFlexTable.setWidget(7, 0, mrLastVisitLabel);
		supervisorVisitFlexTable.setWidget(7, 1, mrLastVisitDateBox);
		mrLastVisitDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));
		supervisorVisitFlexTable.setWidget(8, 0, relationshipLabel);
		relationshipLabel.setWidth("165px");
		supervisorVisitFlexTable.setWidget(8, 1, relationshipTextArea);

		flexTable.setWidget(2, 0, bottomFlexTable);
		bottomFlexTable.setWidth("650");
		
		bottomFlexTable.setWidget(0, 0, absolutePanel);
		absolutePanel.setWidth("550px");
		
		absolutePanel.add(submitButton);
		submitButton.setSize("100", "25");
		
		absolutePanel.add(clearButton);
		clearButton.setSize("100", "25");
		bottomFlexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		FirstVisitComposite.populateTimeListbox(visitHourListBox, hourStrings);
		FirstVisitComposite.populateTimeListbox(visitMinuteListBox, minuteStrings);
		
		submitButton.addClickHandler(this);
		clearButton.addClickHandler(this);
		locationIdIntegerBox.addBlurHandler(this);
		
		refresh(flexTable);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		clearUp();
	}

	public void refresh (Widget widget)
	{
		if (widget instanceof FlexTable)
		{
			Iterator<Widget> iter = ((FlexTable) widget).iterator ();
			while (iter.hasNext ())
				refresh (iter.next ());
		}
		else if (widget instanceof Panel)
		{
			Iterator<Widget> iter = ((Panel) widget).iterator ();
			while (iter.hasNext ())
				refresh (iter.next ());
		}
		else if (widget instanceof TextBox)
		{
			TextBox text = (TextBox) widget;
			String name = text.getName ();
			if (!name.equals (""))
			{
				String[] parts = name.split (";");
				if (parts.length == 2)
					text.setMaxLength (FM.getMaxLength (parts[0], parts[1]));
			}
		}
		else if (widget instanceof ListBox)
		{
			if (!((ListBox) widget).getName ().equals (""))
				widget = FMClient.fillList (widget);
		}
	}
	
	public void clearControls (Widget w)
	{
		if (w instanceof Panel)
		{
			Iterator<Widget> iter = ((Panel) w).iterator ();
			while (iter.hasNext ())
				clearControls (iter.next ());
		}
		else if (w instanceof TextBoxBase)
		{
			((TextBoxBase) w).setText ("");
		}
		else if (w instanceof RichTextArea)
		{
			((RichTextArea) w).setText ("");
		}
		else if (w instanceof ListBox)
		{
			((ListBox) w).setSelectedIndex (0);
		}
		else if (w instanceof DatePicker)
		{
			((DatePicker) w).setValue (new Date ());
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.dom.client.BlurHandler#onBlur(com.google.gwt.event.dom.client.BlurEvent)
	 */
	@Override
	public void onBlur(BlurEvent event)
	{
		Widget sender = (Widget) event.getSource();
		if (sender == locationIdIntegerBox)
		{
			Window.alert("Location ID - Blur Event called");
		}
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#clearUp()
	 */
	@Override
	public void clearUp()
	{
		clearControls(supervisorVisitFlexTable);
		IntegerBox[] integerBoxes = { locationIdIntegerBox };

		ListBox[] listBoxes = { townListBox, gpPotentialListBox };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("000000");
		for (int i = 0; i < listBoxes.length; i++)
			listBoxes[i].setSelectedIndex(0);
		formDateBox.setValue(new Date());
		visitDateBox.setValue(new Date());
		mrLastVisitDateBox.setValue(new Date());
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#validate()
	 */
	@Override
	public boolean validate()
	{
		valid = true;

		StringBuilder errorMessage = new StringBuilder();

		if (FMClient.get(locationIdIntegerBox).equals(""))
			errorMessage.append("Location ID: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		// if (FMClient.get(locationNameTextBox).equals(""))
		// errorMessage.append("Location Name: "
		// + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
		// + "\n");
		if (FMClient.get(visitDateBox).equals(""))
			errorMessage.append("Visit Date: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(mrLastVisitDateBox).equals(""))
			errorMessage.append("MR Last Visit Date: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(relationshipTextArea).equals(""))
			errorMessage.append("Relationship of MR/GP: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");

		valid = errorMessage.length() == 0;
		if (!valid)
		{
			Window.alert(errorMessage.toString());
		}
		return valid;
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#saveData()
	 */
	@Override
	public void saveData()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#updateData()
	 */
	@Override
	public void updateData()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#deleteData()
	 */
	@Override
	public void deleteData()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#fillData()
	 */
	@Override
	public void fillData()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#setCurrent()
	 */
	@Override
	public void setCurrent()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#setRights(java.lang.String)
	 */
	@Override
	public void setRights(String menuName)
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#onClick(com.google.gwt.event.dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event)
	{
		Widget sender = (Widget) event.getSource();
		if (sender == submitButton)
		{
			Window.alert("Submit button - Click Event called");
		}
		else if (sender == clearButton)
		{
			Window.alert("Clear button - Click Event called");
		}
		
	}

}
