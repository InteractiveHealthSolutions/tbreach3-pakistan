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
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.client.FirstVisitComposite;
import com.ihsinformatics.tbr3.fieldmonitoring.client.FMClient;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

/**
 * @author Tahira
 *
 */
public class CampInformationComposite extends Composite implements IForm, ClickHandler, BlurHandler
{
	private static final String formName = "CAMP_INFO";
	private static boolean valid;
	
	FlexTable flexTable = new FlexTable();
	FlexTable topFlexTable = new FlexTable();
	FlexTable campInfoFlexTable = new FlexTable();
	FlexTable bottomFlexTable = new FlexTable();
	AbsolutePanel buttonAbsolutePanel = new AbsolutePanel();

	private final String[] hourStrings = { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23" };
	private final String[] minuteStrings = { "00", "05", "10", "15", "20",
			"25", "30", "35", "40", "45", "50", "55" };
	
	Label formTitleLabel = new Label(FM.getProjectTitle () + " Camp Information");
	
	private final Label formDateLabel = new Label("Form Date");
	private DateBox formDateBox = new DateBox();

	private Label locationIdLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();

	private TextBox locationNameTextBox = new TextBox();
	private Label locationNameLabel = new Label("Location Name");

	private Label townLabel = new Label("Town");
	private ListBox townListBox = new ListBox();

	private Label capacityLabel = new Label("Camp Capacity");
	private IntegerBox capacityIntegerBox = new IntegerBox();

	private Label staffMemberNamesLabel = new Label("Staff Members' Name");
	private TextArea staffMembeNamesTextArea = new TextArea();

	private Label campStartTimeLabel = new Label("Camp Start Time");
	private AbsolutePanel startTimePanel = new AbsolutePanel();
	private ListBox startHourListBox = new ListBox();
	private ListBox startMinuteListBox = new ListBox();

	private Label campEndTimeLabel = new Label("Camp End Time");
	private AbsolutePanel endTimePanel = new AbsolutePanel();
	private ListBox endHourListBox = new ListBox();
	private ListBox endMinuteListBox = new ListBox();

	private Label campExpenseStaffLabel = new Label(
			"Per Camp Expense for Staff");
	private IntegerBox campExpenseStaffIntegerBox = new IntegerBox();

	private Label campExpenseOthersLabel = new Label(
			"Per Camp Expense for Others");
	private IntegerBox campExpenseOthersIntegerBox = new IntegerBox();

	private Label xrayRecommendedLabel = new Label("No. of Xray Recommended");
	private IntegerBox xrayRecommendedIntegerBox = new IntegerBox();
	private Label locationIdHintLabel = new Label("(Format: 101999)");

	Button submitButton = new Button("Submit");
	Button clearButton = new Button("Clear");
	
	public CampInformationComposite() {
		
		
		initWidget(flexTable);
		flexTable.setSize("100%", "100%");
		
		flexTable.setWidget(0, 0, topFlexTable);
		topFlexTable.setWidth("650");
		formTitleLabel.setStyleName("title");
		
		topFlexTable.setWidget(0, 0, formTitleLabel);
		
		flexTable.setWidget(1, 0, campInfoFlexTable);
		campInfoFlexTable.setWidth("650");
		
		campInfoFlexTable.setWidget(0, 0, formDateLabel);
		campInfoFlexTable.setWidget(0, 1, formDateBox);
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		campInfoFlexTable.setWidget(1, 0, locationIdLabel);
		locationIdIntegerBox.setAlignment(TextAlignment.LEFT);

		campInfoFlexTable.setWidget(1, 1, locationIdIntegerBox);
		locationIdIntegerBox.setWidth("100px");
		locationIdIntegerBox.setMaxLength(6);
		locationIdIntegerBox.setVisibleLength(6);

		campInfoFlexTable.setWidget(1, 2, locationIdHintLabel);
		locationIdHintLabel.setWidth("122px");
		locationIdHintLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.setWidget(2, 0, locationNameLabel);
		campInfoFlexTable.setWidget(2, 1, locationNameTextBox);
		campInfoFlexTable.setWidget(3, 0, townLabel);
		campInfoFlexTable.setWidget(3, 1, townListBox);
		townListBox.setWidth("180px");
		townListBox.setName("TOWN");
		campInfoFlexTable.setWidget(4, 0, capacityLabel);
		campInfoFlexTable.setWidget(4, 1, capacityIntegerBox);
		campInfoFlexTable.setWidget(5, 0, staffMemberNamesLabel);
		campInfoFlexTable.setWidget(5, 1, staffMembeNamesTextArea);
		staffMembeNamesTextArea.setWidth("151");
		campInfoFlexTable.setWidget(6, 0, campStartTimeLabel);

		startTimePanel.add(startHourListBox);
		startTimePanel.add(startMinuteListBox);
		startHourListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		startMinuteListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		campInfoFlexTable.setWidget(6, 1, startTimePanel);
		campInfoFlexTable.setWidget(7, 0, campEndTimeLabel);

		endTimePanel.add(endHourListBox);
		endTimePanel.add(endMinuteListBox);

		endHourListBox.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		endMinuteListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		campInfoFlexTable.setWidget(7, 1, endTimePanel);
		campInfoFlexTable.setWidget(8, 0, campExpenseStaffLabel);
		campExpenseStaffLabel.setWidth("234px");
		campInfoFlexTable.setWidget(8, 1, campExpenseStaffIntegerBox);
		campInfoFlexTable.setWidget(9, 0, campExpenseOthersLabel);
		campExpenseOthersLabel.setWidth("219px");
		campInfoFlexTable.setWidget(9, 1, campExpenseOthersIntegerBox);
		campInfoFlexTable.setWidget(10, 0, xrayRecommendedLabel);
		campInfoFlexTable.setWidget(10, 1, xrayRecommendedIntegerBox);

		
		
		flexTable.setWidget(2, 0, bottomFlexTable);
		bottomFlexTable.setSize("650", "39");
		
		bottomFlexTable.setWidget(0, 0, buttonAbsolutePanel);
		buttonAbsolutePanel.setSize("550px", "30px");
		
		submitButton.setText("Submit");
		buttonAbsolutePanel.add(submitButton);
		submitButton.setSize("100", "25");
		
		clearButton.setText("Clear");
		buttonAbsolutePanel.add(clearButton);
		clearButton.setSize("100", "25");
		bottomFlexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		FirstVisitComposite.populateTimeListbox(startHourListBox, hourStrings);
		FirstVisitComposite.populateTimeListbox(startMinuteListBox,
				minuteStrings);
		FirstVisitComposite.populateTimeListbox(endHourListBox, hourStrings);
		FirstVisitComposite
				.populateTimeListbox(endMinuteListBox, minuteStrings);
		
		locationIdIntegerBox.addBlurHandler(this);
		submitButton.addClickHandler(this);
		clearButton.addClickHandler(this);
		
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
		clearControls(campInfoFlexTable);
		IntegerBox[] integerBoxes = { locationIdIntegerBox, capacityIntegerBox,
				campExpenseStaffIntegerBox, campExpenseOthersIntegerBox,
				xrayRecommendedIntegerBox };

		ListBox[] listBoxes = { townListBox };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("0");
		for (int i = 0; i < listBoxes.length; i++)
			listBoxes[i].setSelectedIndex(0);
		locationIdIntegerBox.setText("000000");
		locationNameTextBox.setEnabled(false);
		formDateBox.setValue(new Date());
		
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
		if (FMClient.get(locationNameTextBox).equals(""))
			errorMessage.append("Location name: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(capacityIntegerBox).equals(""))
			errorMessage.append("Capacity: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(staffMembeNamesTextArea).equals(""))
			errorMessage.append("Staff Members' Names: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(campExpenseStaffIntegerBox).equals(""))
			errorMessage.append("Per Camp Expense for Staff: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(campExpenseOthersIntegerBox).equals(""))
			errorMessage.append("Per Camp Expense for Others: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(xrayRecommendedIntegerBox).equals(""))
			errorMessage.append("No. of Xray Recommended: "
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
