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

import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.client.FMClient;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
import com.google.gwt.user.client.ui.Button;

/**
 * @author Tahira
 *
 */
public class FirstVisitComposite extends Composite implements IForm, ClickHandler, BlurHandler
{
	private static ServerServiceAsync	service					= GWT.create (ServerService.class);
	private static final String formName = "FIRST_VIS";
	private static LoadingWidget		loading					= new LoadingWidget ();
	
	final String creator = "none";
	private boolean valid;
	
	FlexTable flexTable = new FlexTable();
	FlexTable topFlexTable = new FlexTable();
	Label formTitleLabel = new Label(FM.getProjectTitle () + " First Visit");
	FlexTable firstVisitFlexTable = new FlexTable();
	FlexTable bottomFlexTable = new FlexTable();
	
	private final String[] hourStrings = { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23" };
	private final String[] minuteStrings = { "00", "05", "10", "15", "20",
			"25", "30", "35", "40", "45", "50", "55" };
	
	private Label locationIDLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();
	private Label locationIdHintLabel = new Label("(Format: 101999)");
	private Label locationNameLabel = new Label("Location Name");
	private TextBox locationNameTextBox = new TextBox();
	private Label locationTypeLabel = new Label("Location Type");
	private ListBox locationTypesListBox = new ListBox();
	private Label address1Label = new Label("Address 1");
	private TextArea address1TextArea = new TextArea();
	private Label address2Label = new Label("Address 2");
	private TextArea address2TextArea = new TextArea();
	private Label mobileLabel = new Label("Mobile Number");
	private TextBox mobileTextBox = new TextBox();
	private Label mobileNoHintLabel = new Label("(Format: 0333-1234567)");
	private Label townLabel = new Label("Town");
	private ListBox townListBox = new ListBox();

	private final Label formDateLabel = new Label("Form Date");
	private DateBox formDateBox = new DateBox();

	private Label capacityLabel = new Label("Capacity");
	private IntegerBox capacityIntegerBox = new IntegerBox();
	private Label capacityHintLabel = new Label("(Format: 29)");

	private Label specialityLabel = new Label("Speciality");
	private TextBox specialityTextBox = new TextBox();

	private Label startTimeLabel = new Label("Start Time");
	// start time
	private AbsolutePanel startTimePanel = new AbsolutePanel();
	private ListBox startHourListBox = new ListBox();
	private ListBox startMinuteListBox = new ListBox();

	private Label endTimeLabel = new Label("End Time");
	// end time
	private AbsolutePanel endTimePanel = new AbsolutePanel();
	private ListBox endHourListBox = new ListBox();
	private ListBox endMinuteListBox = new ListBox();
	private final AbsolutePanel buttonAbsolutePanel = new AbsolutePanel();
	private final Button submitButton = new Button("Submit");
	private final Button clearButton = new Button("Clear");

	
	public FirstVisitComposite() {

		initWidget(flexTable);
		flexTable.setSize("100%", "100%");
		
		flexTable.setWidget(0, 0, topFlexTable);
		topFlexTable.setSize("600", "39");
		
		formTitleLabel.setStyleName("title");
		topFlexTable.setWidget(0, 0, formTitleLabel);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		flexTable.setWidget(1, 0, firstVisitFlexTable);
		firstVisitFlexTable.setWidth("600");
		
		firstVisitFlexTable.setWidget(0, 0, formDateLabel);
		firstVisitFlexTable.setWidget(0, 1, formDateBox);
		formDateBox.setWidth("184px");
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));
		formDateBox.setValue(new Date());

		firstVisitFlexTable.setWidget(1, 0, locationIDLabel);
		locationIDLabel.setWidth("100px");

		firstVisitFlexTable.setWidget(1, 1, locationIdIntegerBox);
		locationIdIntegerBox.setWidth("180px");
		locationIdIntegerBox.setMaxLength(6);
		locationIdIntegerBox.setVisibleLength(6);

		firstVisitFlexTable.setWidget(1, 2, locationIdHintLabel);

		firstVisitFlexTable.setWidget(2, 0, locationNameLabel);

		firstVisitFlexTable.setWidget(2, 1, locationNameTextBox);
		locationNameTextBox.setWidth("180px");
		locationNameTextBox.setMaxLength(50);
		locationNameTextBox.setVisibleLength(50);

		firstVisitFlexTable.setWidget(3, 0, locationTypeLabel);

		firstVisitFlexTable.setWidget(3, 1, locationTypesListBox);
		locationTypesListBox.setWidth("180px");
		locationTypesListBox.setName("LOCATION_TYPE");
		firstVisitFlexTable.setWidget(4, 0, address1Label);

		firstVisitFlexTable.setWidget(4, 1, address1TextArea);
		address1TextArea.setWidth("180px");
		address1TextArea.getElement().setAttribute("maxlength", "255");

		firstVisitFlexTable.setWidget(5, 0, address2Label);
		firstVisitFlexTable.setWidget(5, 1, address2TextArea);
		address2TextArea.setWidth("180px");
		address2TextArea.getElement().setAttribute("maxlength", "255");

		firstVisitFlexTable.setWidget(6, 0, townLabel);
		firstVisitFlexTable.setWidget(6, 1, townListBox);
		townListBox.setWidth("180px");
		townListBox.setName("TOWN");
		firstVisitFlexTable.setWidget(7, 0, mobileLabel);
		mobileLabel.setWidth("136px");

		firstVisitFlexTable.setWidget(7, 1, mobileTextBox);
		mobileTextBox.setWidth("180px");
		mobileTextBox.setMaxLength(12);
		mobileTextBox.setVisibleLength(12);

		firstVisitFlexTable.setWidget(7, 2, mobileNoHintLabel);
		firstVisitFlexTable.setWidget(8, 0, capacityLabel);

		firstVisitFlexTable.setWidget(8, 1, capacityIntegerBox);
		capacityIntegerBox.setWidth("180px");
		capacityIntegerBox.setMaxLength(2);
		capacityIntegerBox.setVisibleLength(2);

		firstVisitFlexTable.setWidget(8, 2, capacityHintLabel);
		firstVisitFlexTable.setWidget(9, 0, specialityLabel);

		firstVisitFlexTable.setWidget(9, 1, specialityTextBox);
		specialityTextBox.setWidth("180px");
		specialityTextBox.setMaxLength(15);
		specialityTextBox.setVisibleLength(15);

		firstVisitFlexTable.setWidget(10, 0, startTimeLabel);
		startTimePanel.add(startHourListBox);
		startTimePanel.add(startMinuteListBox);

		startHourListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		startMinuteListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		firstVisitFlexTable.setWidget(10, 1, startTimePanel);

		// end time controls
		firstVisitFlexTable.setWidget(11, 0, endTimeLabel);

		endTimePanel.add(endHourListBox);
		endTimePanel.add(endMinuteListBox);

		endHourListBox.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		endMinuteListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		firstVisitFlexTable.setWidget(11, 1, endTimePanel);
		
		flexTable.setWidget(2, 0, bottomFlexTable);
		bottomFlexTable.setSize("600", "39");
		
		bottomFlexTable.setWidget(0, 0, buttonAbsolutePanel);
		buttonAbsolutePanel.setWidth("452px");
		submitButton.setText("Submit");
		submitButton.setSize("100", "25");
		
		buttonAbsolutePanel.add(submitButton);
		clearButton.setText("Clear");
		clearButton.setSize("100", "25");
		
		buttonAbsolutePanel.add(clearButton);
		bottomFlexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		populateTimeListbox(startHourListBox, hourStrings);
		populateTimeListbox(startMinuteListBox, minuteStrings);

		populateTimeListbox(endHourListBox, hourStrings);
		populateTimeListbox(endMinuteListBox, minuteStrings);
		
		locationIdIntegerBox.addBlurHandler(this);
		submitButton.addClickHandler(this);
		clearButton.addClickHandler(this);
		
		
		
		refresh(flexTable);
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
	
	/**
	 * Display/Hide main panel and loading widget
	 * 
	 * @param status
	 */
	public void load (boolean status)
	{
		flexTable.setVisible (!status);
		if (status)
			loading.show ();
		else
			loading.hide ();
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
		clearControls(firstVisitFlexTable);
		IntegerBox[] integerBoxes = { capacityIntegerBox };
		ListBox[] listBoxes = { townListBox, locationTypesListBox };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("0");
		for (int i = 0; i < listBoxes.length; i++)
			listBoxes[i].setSelectedIndex(0);

		locationIdIntegerBox.setText("000000");
		locationNameTextBox.setEnabled(false);
		formDateBox.setValue(new Date());
		address1TextArea.setText("");
		address2TextArea.setText("");
	}


	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#validate()
	 */
	@Override
	public boolean validate()
	{
		valid = true;

		StringBuilder errorMessage = new StringBuilder();

		if (FMClient.get(locationNameTextBox).equals(""))
			errorMessage.append("Location Name: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(locationIdIntegerBox).equals(""))
			errorMessage.append("Location ID: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(capacityIntegerBox).equals(""))
			errorMessage.append("Capacity: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(specialityTextBox).equals(""))
			errorMessage.append("Speciality: "
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
			clearUp();
		}
	}
	
	public static void populateTimeListbox(ListBox listBox, String[] values)
	{
		for (String item : values)
			listBox.addItem(item);
	}
}
