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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
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
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

/**
 * @author Tahira
 *
 */
public class DailyVisitComposite extends Composite implements IForm, BlurHandler, ClickHandler, ValueChangeHandler<Boolean>
{
	private static final String formName = "DAILY_VIS";
	private static boolean valid;
	
	FlexTable flexTable = new FlexTable();
	FlexTable topFlexTable = new FlexTable();
	FlexTable dailyVisitFlexTable = new FlexTable();
	FlexTable bottomFlexTable = new FlexTable();
	
	private final String[] hourStrings = { "00", "01", "02", "03", "04", "05",
			"06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
			"17", "18", "19", "20", "21", "22", "23" };
	private final String[] minuteStrings = { "00", "05", "10", "15", "20",
			"25", "30", "35", "40", "45", "50", "55" };
	
	Label formTitleLabel = new Label(FM.getProjectTitle () + " Daily Visit");
	
	private Label formDateLabel = new Label("Form Date");
	private DateBox formDateBox = new DateBox();

	private Label locationIdLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();

	private TextBox locationNameTextBox = new TextBox();
	private Label locationNameLabel = new Label("Location Name");

	private Label townLabel = new Label("Town");
	private ListBox townListBox = new ListBox();

	private Label visitDateLabel = new Label("Visit Date");
	private DateBox visitDateBox = new DateBox();

	private Label arrivalTimeLabel = new Label("Arrival Time");
	private AbsolutePanel arrivalTimePanel = new AbsolutePanel();
	private ListBox arrivalHourListBox = new ListBox();
	private ListBox arrivalMinuteListBox = new ListBox();

	private Label departureTimeLabel = new Label("Departure Time");
	private AbsolutePanel departureTimePanel = new AbsolutePanel();
	private ListBox departureHourListBox = new ListBox();
	private ListBox departureMinuteListBox = new ListBox();

	private Label metGpLabel = new Label("Met GP");
	private FlowPanel metGpFlowPanel = new FlowPanel();
	private RadioButton metGpYesRadioButton = new RadioButton(
			"metGpRadioGroup", "Yes");
	private RadioButton metGpNoRadioButton = new RadioButton("metGpRadioGroup",
			"No");

	private Label givenCouponsLabel = new Label("Given Coupons/Rx Pads");
	private FlowPanel givenCouponsFlowPanel = new FlowPanel();
	private RadioButton givenCouponsYesRadioButton = new RadioButton(
			"givenCouponsRadioGroup", "Yes");
	private RadioButton givenCouponsNoRadioButton = new RadioButton(
			"givenCouponsRadioGroup", "No");
	
	private final Label marketingActivityLabel = new Label("Marketing Activity");
	private FlowPanel marketingActivityFlowPanel = new FlowPanel();
	private RadioButton marketingYesRadioButton = new RadioButton(
			"marketingRadioGroup", "Yes");
	private RadioButton marketingNoRadioButton = new RadioButton(
			"marketingRadioGroup", "No");

	private Label marketingDescriptionLabel = new Label(
			"Marketing Activity Description");
	private TextArea marketingDescriptionTextArea = new TextArea();

	private Label marketingBudgetItemsLabel = new Label(
			"MR Marketing Budget Items");
	private TextBox marketingBudgetItemsTextBox = new TextBox();

	private Label amountLabel = new Label("Amount");
	private IntegerBox amountIntegerBox = new IntegerBox(); // takes only
															// integer

	private CheckBox postersCheckBox = new CheckBox("Posters");
	private IntegerBox postersIntegerBox = new IntegerBox();

	private CheckBox brochuresCheckBox = new CheckBox("Brochures");
	private IntegerBox brochuresIntegerBox = new IntegerBox();

	private CheckBox rxPadsCheckBox = new CheckBox("Rx Pads");
	private IntegerBox rxPadsIntegerBox = new IntegerBox();

	private CheckBox othersCheckBox = new CheckBox("Others");
	private IntegerBox othersIntegerBox = new IntegerBox();

	private Label institutionalMarketingItemsLabel = new Label(
			"Institutional Marketing Items");

	private Button addMoreButton = new Button("(Add Item...)");

	private Label gpPotentialLabel = new Label("GP Potential");
	private ListBox gpPotentialListBox = new ListBox();

	private Label commentsLabel = new Label("Comments");
	private TextArea commentsTextArea = new TextArea();

	private Label locationIdHintLabel = new Label("(Format: 101999)");

	private final AbsolutePanel buttonAbsolutePanel = new AbsolutePanel();
	private final Button submitButton = new Button("Submit");
	private final Button clearButton = new Button("Clear");
	
	public DailyVisitComposite() {
		
		initWidget(flexTable);
		flexTable.setSize("550px", "30px");
		
		flexTable.setWidget(0, 0, topFlexTable);
		topFlexTable.setWidth("650");
		formTitleLabel.setStyleName("title");
		
		topFlexTable.setWidget(0, 0, formTitleLabel);
		
		flexTable.setWidget(1, 0, dailyVisitFlexTable);
		dailyVisitFlexTable.setWidth("650");

		dailyVisitFlexTable.setWidget(0, 0, formDateLabel);
		dailyVisitFlexTable.setWidget(0, 1, formDateBox);
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		dailyVisitFlexTable.setWidget(1, 0, locationIdLabel);
		dailyVisitFlexTable.setWidget(1, 1, locationIdIntegerBox);

		dailyVisitFlexTable.setWidget(1, 2, locationIdHintLabel);
		locationIdHintLabel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		dailyVisitFlexTable.setWidget(2, 0, locationNameLabel);

		dailyVisitFlexTable.setWidget(2, 1, locationNameTextBox);
		locationNameTextBox.setEnabled(false);

		dailyVisitFlexTable.setWidget(3, 0, townLabel);
		dailyVisitFlexTable.setWidget(3, 1, townListBox);
		townListBox.setWidth("180px");
		townListBox.setName("TOWN");
		dailyVisitFlexTable.setWidget(4, 0, visitDateLabel);
		dailyVisitFlexTable.setWidget(4, 1, visitDateBox);
		visitDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		dailyVisitFlexTable.setWidget(5, 0, arrivalTimeLabel);
		arrivalTimePanel.add(arrivalHourListBox);
		arrivalTimePanel.add(arrivalMinuteListBox);
		arrivalHourListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		arrivalMinuteListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		dailyVisitFlexTable.setWidget(5, 1, arrivalTimePanel);
		dailyVisitFlexTable.setWidget(6, 0, departureTimeLabel);

		departureTimePanel.add(departureHourListBox);
		departureTimePanel.add(departureMinuteListBox);
		departureHourListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		departureMinuteListBox.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		dailyVisitFlexTable.setWidget(6, 1, departureTimePanel);
		dailyVisitFlexTable.setWidget(7, 0, metGpLabel);
		metGpFlowPanel.add(metGpYesRadioButton);
		metGpFlowPanel.add(metGpNoRadioButton);
		dailyVisitFlexTable.setWidget(7, 1, metGpFlowPanel);
		dailyVisitFlexTable.setWidget(8, 0, givenCouponsLabel);

		givenCouponsFlowPanel.add(givenCouponsYesRadioButton);
		givenCouponsFlowPanel.add(givenCouponsNoRadioButton);
		dailyVisitFlexTable.setWidget(8, 1, givenCouponsFlowPanel);

		dailyVisitFlexTable.setWidget(9, 0, marketingActivityLabel);
		marketingActivityFlowPanel.add(marketingYesRadioButton);
		marketingActivityFlowPanel.add(marketingNoRadioButton);
		dailyVisitFlexTable.setWidget(9, 1, marketingActivityFlowPanel);
		dailyVisitFlexTable.setWidget(10, 0, marketingDescriptionLabel);
		marketingDescriptionLabel.setWidth("232px");
		dailyVisitFlexTable.setWidget(10, 1, marketingDescriptionTextArea);
		dailyVisitFlexTable.setWidget(11, 0, marketingBudgetItemsLabel);
		dailyVisitFlexTable.setWidget(11, 1, marketingBudgetItemsTextBox);
		dailyVisitFlexTable.setWidget(12, 0, amountLabel);
		dailyVisitFlexTable.setWidget(12, 1, amountIntegerBox);
		amountIntegerBox.setMaxLength(8);
		amountIntegerBox.setText("0");

		dailyVisitFlexTable.setWidget(13, 0, institutionalMarketingItemsLabel);
		dailyVisitFlexTable.setWidget(14, 0, postersCheckBox);
		dailyVisitFlexTable.setWidget(14, 1, postersIntegerBox);
		postersIntegerBox.setEnabled(false);

		dailyVisitFlexTable.setWidget(15, 0, brochuresCheckBox);
		dailyVisitFlexTable.setWidget(15, 1, brochuresIntegerBox);
		brochuresIntegerBox.setEnabled(false);

		dailyVisitFlexTable.setWidget(16, 0, rxPadsCheckBox);
		dailyVisitFlexTable.setWidget(16, 1, rxPadsIntegerBox);
		rxPadsIntegerBox.setEnabled(false);

		dailyVisitFlexTable.setWidget(17, 0, othersCheckBox);
		dailyVisitFlexTable.setWidget(17, 1, othersIntegerBox);
		othersIntegerBox.setEnabled(false);


		dailyVisitFlexTable.setWidget(18, 0, gpPotentialLabel);
		dailyVisitFlexTable.setWidget(18, 1, gpPotentialListBox);
		gpPotentialListBox.setWidth("180px");
		gpPotentialListBox.setName("GP_POTENTIAL_RATING");
		dailyVisitFlexTable.setWidget(19, 0, commentsLabel);
		dailyVisitFlexTable.setWidget(19, 1, commentsTextArea);

		flexTable.setWidget(2, 0, bottomFlexTable);
		bottomFlexTable.setWidth("650");
		
		bottomFlexTable.setWidget(0, 0, buttonAbsolutePanel);
		buttonAbsolutePanel.setWidth("550px");
		
		buttonAbsolutePanel.add(submitButton);
		submitButton.setSize("100", "25");
		
		buttonAbsolutePanel.add(clearButton);
		clearButton.setSize("100", "25");
		bottomFlexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		FirstVisitComposite.populateTimeListbox(arrivalHourListBox, hourStrings);
		FirstVisitComposite.populateTimeListbox(arrivalMinuteListBox, minuteStrings);

		FirstVisitComposite.populateTimeListbox(departureHourListBox, hourStrings);
		FirstVisitComposite.populateTimeListbox(departureMinuteListBox, minuteStrings);
		
		locationIdIntegerBox.addBlurHandler(this);
		submitButton.addClickHandler(this);
		clearButton.addClickHandler(this);
		
		marketingYesRadioButton.addValueChangeHandler(this);
		marketingNoRadioButton.addValueChangeHandler(this);

		CheckBox[] checkBoxes = { postersCheckBox, brochuresCheckBox,
				rxPadsCheckBox, othersCheckBox };
		for (CheckBox checkBox : checkBoxes)
			checkBox.addValueChangeHandler(this);
		
		refresh (flexTable);
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
		clearControls(dailyVisitFlexTable);
		IntegerBox[] integerBoxes = { locationIdIntegerBox, amountIntegerBox,
				postersIntegerBox, brochuresIntegerBox, rxPadsIntegerBox,
				othersIntegerBox };

		ListBox[] listBoxes = { townListBox, gpPotentialListBox };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("0");
		for (int i = 0; i < listBoxes.length; i++)
			listBoxes[i].setSelectedIndex(0);
		locationIdIntegerBox.setText("000000");
		formDateBox.setValue(new Date());
		visitDateBox.setValue(new Date());
		metGpYesRadioButton.setChecked(true);
		givenCouponsYesRadioButton.setChecked(true);
		marketingYesRadioButton.setChecked(true);

		CheckBox[] checkBoxes = { postersCheckBox, brochuresCheckBox,
				rxPadsCheckBox, othersCheckBox };
		for (CheckBox checkBox : checkBoxes)
		{
			checkBox.setChecked(false);
		}
		postersIntegerBox.setEnabled(false);
		brochuresIntegerBox.setEnabled(false);
		rxPadsIntegerBox.setEnabled(false);
		othersIntegerBox.setEnabled(false);
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
		if (FMClient.get(visitDateBox).equals(""))
			errorMessage.append("Visit Date: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (!metGpYesRadioButton.isChecked() && !metGpNoRadioButton.isChecked())
			errorMessage.append("Met GP: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (!givenCouponsYesRadioButton.isChecked()
				&& !givenCouponsNoRadioButton.isChecked())
			errorMessage.append("Given Coupons/Rx Pads: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (!marketingYesRadioButton.isChecked()
				&& !marketingNoRadioButton.isChecked())
			errorMessage.append("Marketing Activity: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (FMClient.get(marketingDescriptionTextArea).equals(""))
			errorMessage.append("Marketing Activity Description: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");

		if (!marketingNoRadioButton.isChecked())
		{
			if (FMClient.get(marketingBudgetItemsTextBox).equals(""))
				errorMessage.append("Marketing Budget Items: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");
			if (FMClient.get(amountIntegerBox).equals(""))
				errorMessage.append("Amount: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");
		}

		if (FMClient.get(commentsTextArea).equals(""))
			errorMessage.append("Comments: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (postersCheckBox.isChecked()
				&& FMClient.get(postersIntegerBox).equals(""))
			errorMessage.append("Posters Count: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (brochuresCheckBox.isChecked()
				&& FMClient.get(brochuresIntegerBox).equals(""))
			errorMessage.append("Brochures Count: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (rxPadsCheckBox.isChecked()
				&& FMClient.get(rxPadsIntegerBox).equals(""))
			errorMessage.append("Rx Pads Count: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (othersCheckBox.isChecked()
				&& FMClient.get(othersIntegerBox).equals(""))
			errorMessage.append("Other Institutional Items Count: "
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

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event)
	{
		Widget sender = (Widget) event.getSource();
		if (sender == marketingYesRadioButton)
		{
			boolean marketing = marketingYesRadioButton.getValue();
			marketingBudgetItemsTextBox.setEnabled(marketing);
			amountIntegerBox.setEnabled(marketing);
		}
		else if (sender == marketingNoRadioButton)
		{
			boolean marketing = marketingYesRadioButton.getValue();
			marketingBudgetItemsTextBox.setEnabled(marketing);
			amountIntegerBox.setEnabled(marketing);
		}
		if (sender instanceof CheckBox)
		{
			CheckBox checkBox = (CheckBox) sender;
			boolean choice = checkBox.getValue();
			if (sender == postersCheckBox)
				postersIntegerBox.setEnabled(choice);
			else if (sender == brochuresCheckBox)
				brochuresIntegerBox.setEnabled(choice);
			else if (sender == rxPadsCheckBox)
				rxPadsIntegerBox.setEnabled(choice);
			else if (sender == othersCheckBox)
				othersIntegerBox.setEnabled(choice);
		}
	}
	

}
