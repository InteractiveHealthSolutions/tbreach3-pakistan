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
package com.ihsinformatics.tbr3fieldmonitoring.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.ihsinformatics.tbr3fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3fieldmonitoring.shared.DateTimeUtil;
import com.ihsinformatics.tbr3fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3fieldmonitoring.shared.InfoType;
import com.ihsinformatics.tbr3fieldmonitoring.shared.TBR3;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Encounter;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterId;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterResults;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.EncounterResultsId;
import com.ihsinformatics.tbr3fieldmonitoring.shared.model.Location;
import com.summatech.gwt.client.HourMinutePicker;
import com.summatech.gwt.client.HourMinutePicker.PickerFormat;

/**
 * @author Tahira
 * 
 */
public class FirstVisitComposite extends Composite implements IForm,
		ClickHandler
{
	private static Logger logger = Logger.getLogger(FirstVisitComposite.class
			.getName());
	private static ServerServiceAsync service = GWT.create(ServerService.class);

	private static final String formName = "FIRST_VIS";
	final String creator = "none";

	private boolean valid;

	private FlexTable mainFlexTable = new FlexTable();
	private FlexTable userProfileFlexTable = new FlexTable();
	private FlexTable headerFlexTable = new FlexTable();
	private FlexTable firstVisitFlexTable = new FlexTable();

	private FlowPanel userNamePanel = new FlowPanel();

	private Label loginAsLabel = new Label("Login As:");
	private Label usernameLabel = new Label("user");
	private Label leftBraceLabel = new Label("(");
	private Label rightBraceLabel = new Label(")");
	private Hyperlink logoutHyperlink = new Hyperlink("Logout,", "");
	private Hyperlink mainMenuHyperlink = new Hyperlink("Back to Menu", "");
	private Label formHeadingLabel = new Label("FIELD VISIT FORM");
	private Label formDateLabel = new Label("Form Date   ");
	private Label locationIDLabel = new Label("Location ID");
	private Label locationNameLabel = new Label("Location Name   ");
	private Label locationTypeLabel = new Label("Location Type   ");
	private Label address1Label = new Label("Address 1");
	private Label address2Label = new Label("Address 2");
	private Label mobileLabel = new Label("Mobile Number");
	private Label townLabel = new Label("Town");

	private DateBox formDateBox = new DateBox();
	private IntegerBox locationIDIntegerBox = new IntegerBox();
	private TextBox locationNameTextBox = new TextBox();
	private TextArea address1TextArea = new TextArea();
	private TextArea address2TextArea = new TextArea();

	private TextBox mobileTextBox = new TextBox();

	private ListBox locationTypesListBox = new ListBox();
	private ListBox townListBox = new ListBox();

	private Label capacityLabel = new Label("Capacity");

	private IntegerBox capacityIntegerBox = new IntegerBox();

	private Label specialityLabel = new Label("Speciality");
	private TextBox specialityTextBox = new TextBox();

	private Label startTimeLabel = new Label("Start Time");
	HourMinutePicker startHourMinutePicker = new HourMinutePicker(
			PickerFormat._12_HOUR);

	private Label endTimeLabel = new Label("End Time");
	private HourMinutePicker endHourMinutePicker = new HourMinutePicker(
			PickerFormat._12_HOUR);

	private Button submitButton = new Button("Submit");

	private final Label mobileNoHintLabel = new Label("(Format : 0333-1234567)");

	MainMenuComposite mainMenu;

	@SuppressWarnings("deprecation")
	public FirstVisitComposite()
	{
		initWidget(mainFlexTable);
		logger.log(Level.INFO, "Composite initiated.");

		// mainFlexTable.setStyleName("verticalPanel");
		mainFlexTable.setSize("100%", "100%");

		userNamePanel.add(loginAsLabel);
		userNamePanel.add(usernameLabel);
		userNamePanel.add(leftBraceLabel);
		userNamePanel.add(logoutHyperlink);
		userNamePanel.add(mainMenuHyperlink);

		logoutHyperlink.addStyleName("hyperlink");
		mainMenuHyperlink.addStyleName("hyperlink");

		userNamePanel.add(rightBraceLabel);
		loginAsLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		usernameLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		logoutHyperlink.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		mainMenuHyperlink.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		leftBraceLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		rightBraceLabel.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		userNamePanel.addStyleName("flowPanel");

		userProfileFlexTable.setWidget(0, 0, userNamePanel);
		userProfileFlexTable.getCellFormatter().setHorizontalAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_RIGHT);

		headerFlexTable.setWidget(0, 1, formHeadingLabel);
		headerFlexTable.getRowFormatter().addStyleName(0, "Tbr3Header");
		headerFlexTable.setSize("100%", "");

		// loginFlexTable.setBorderWidth(2);
		firstVisitFlexTable.setWidget(1, 0, formDateLabel);
		formDateLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(1, 1, formDateBox);
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		firstVisitFlexTable.setWidget(2, 0, locationIDLabel);
		locationIDLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(2, 1, locationIDIntegerBox);
		locationIDIntegerBox.setMaxLength(6);
		locationIDIntegerBox.setVisibleLength(6);
		locationIDIntegerBox.setWidth("200");
		locationIDIntegerBox.addStyleName("textbox");

		firstVisitFlexTable.setWidget(3, 0, locationNameLabel);
		locationNameLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(3, 1, locationNameTextBox);
		locationNameTextBox.setMaxLength(50);
		locationNameTextBox.setVisibleLength(50);
		locationNameTextBox.addStyleName("textbox");

		firstVisitFlexTable.setWidget(4, 0, locationTypeLabel);
		locationTypeLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(4, 1, locationTypesListBox);
		locationTypesListBox.setName("LOCATION_TYPE");
		// add style for list

		firstVisitFlexTable.setWidget(5, 0, address1Label);
		address1Label.addStyleName("text");

		firstVisitFlexTable.setWidget(5, 1, address1TextArea);
		// address1TextBox.setWidth("200%");
		address1TextArea.addStyleName("textbox");
		address1TextArea.getElement().setAttribute("maxlength", "255");

		firstVisitFlexTable.setWidget(6, 0, address2Label);
		address2Label.addStyleName("text");

		firstVisitFlexTable.setWidget(6, 1, address2TextArea);
		// address2TextBox.setWidth("200%");
		address2TextArea.addStyleName("textbox");
		address2TextArea.getElement().setAttribute("maxlength", "255");

		firstVisitFlexTable.setWidget(7, 0, townLabel);
		townLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(7, 1, townListBox);
		townListBox.setName("TOWN");

		firstVisitFlexTable.setWidget(8, 0, mobileLabel);
		mobileLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(8, 1, mobileTextBox);
		mobileTextBox.addStyleName("textbox");
		mobileTextBox.setMaxLength(12);
		mobileTextBox.setVisibleLength(12);

		firstVisitFlexTable.setWidget(8, 2, mobileNoHintLabel);
		mobileNoHintLabel.addStyleName("hintLabel");

		firstVisitFlexTable.setWidget(9, 0, capacityLabel);
		capacityLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(9, 1, capacityIntegerBox);
		capacityIntegerBox.addStyleName("textbox");
		capacityIntegerBox.setMaxLength(2);
		capacityIntegerBox.setVisibleLength(2);

		firstVisitFlexTable.setWidget(10, 0, specialityLabel);
		specialityLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(10, 1, specialityTextBox);
		specialityTextBox.addStyleName("textbox");
		specialityTextBox.setMaxLength(15);
		specialityTextBox.setVisibleLength(15);

		firstVisitFlexTable.setWidget(11, 0, startTimeLabel);
		startTimeLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(11, 1, startHourMinutePicker);

		firstVisitFlexTable.setWidget(12, 0, endTimeLabel);
		endTimeLabel.addStyleName("text");

		firstVisitFlexTable.setWidget(12, 1, endHourMinutePicker);

		firstVisitFlexTable.setWidget(13, 1, submitButton);
		submitButton.setStyleName("submitButton");
		submitButton.setSize("169", "30");

		// loginFlexTable.setStyleName("flexTableCell");

		mainFlexTable.setWidget(0, 0, userProfileFlexTable);
		mainFlexTable.setWidget(1, 0, headerFlexTable);
		mainFlexTable.setWidget(2, 0, firstVisitFlexTable);

		firstVisitFlexTable.setSize("100%", "");
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(2, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(3, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(4, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(5, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(6, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(7, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(8, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(9, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(10, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(11, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(12, 1,
				HasHorizontalAlignment.ALIGN_LEFT);

		mainFlexTable.setBorderWidth(1);

		usernameLabel.setText(TBR3.getCurrentUserName());

		submitButton.addClickHandler(this);
		logoutHyperlink.addClickHandler(this);
		mainMenuHyperlink.addClickHandler(this);

		TBR3Client.refresh(firstVisitFlexTable);
		logger.log(Level.INFO, "Components reset/refilled.");
		firstVisitFlexTable.getCellFormatter().setHorizontalAlignment(8, 2,
				HasHorizontalAlignment.ALIGN_LEFT);
		// mainFlexTable.getCellFormatter().setHorizontalAlignment(0, 0,
		// HasHorizontalAlignment.ALIGN_CENTER);
		// mainFlexTable.getCellFormatter().setHorizontalAlignment(1, 0,
		// HasHorizontalAlignment.ALIGN_RIGHT);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event)
	{
		Widget sender = (Widget) event.getSource();
		if (sender == submitButton)
		{
			try
			{
				saveData();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else if (sender == mainMenuHyperlink)
		{
			mainMenu = new MainMenuComposite();
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(mainMenu);
		}
		else if (sender == logoutHyperlink)
		{
			Tbr3fieldmonitoring.logout();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#clearUp()
	 */
	@Override
	public void clearUp()
	{
		TBR3Client.clearControls(firstVisitFlexTable);
		IntegerBox[] integerBoxes = { capacityIntegerBox };
		ListBox[] listBoxes = { townListBox, locationTypesListBox };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("0");
		for (int i = 0; i < listBoxes.length; i++)
			listBoxes[i].setSelectedIndex(0);

		locationIDIntegerBox.setText("000000");
		formDateBox.getTextBox().setText("");
		address1TextArea.setText("");
		address2TextArea.setText("");
		startHourMinutePicker.clear();
		endHourMinutePicker.clear();

	}

	@Override
	public boolean validate()
	{
		valid = true;

		StringBuilder errorMessage = new StringBuilder();

		if (TBR3Client.get(locationNameTextBox).equals(""))
			errorMessage.append("Location Name: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (TBR3Client.get(locationIDIntegerBox).equals(""))
			errorMessage.append("Location ID: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (TBR3Client.get(capacityIntegerBox).equals(""))
			errorMessage.append("Capacity: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (TBR3Client.get(specialityTextBox).equals(""))
			errorMessage.append("Speciality: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (startHourMinutePicker.getHour() == -1)
			errorMessage.append("Start time: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");

		valid = errorMessage.length() == 0;
		if (!valid)
		{
			Window.alert(errorMessage.toString());
		}
		return valid;

	}

	public void saveData()
	{
		if (validate())
		{
			final ArrayList<String> locationAttributeValues = new ArrayList<String>();

			final Date enteredDate = new Date();
			final int eID = 0;
			final String creator = TBR3.getCurrentUserName(); // pid1
			final String address = TBR3Client.get(address1TextArea) + " "
					+ TBR3Client.get(address2TextArea);
			service.getUserRoles(TBR3.getCurrentUserName(), TBR3.getPassword(),
					new AsyncCallback<String[]>()
					{

						@Override
						public void onSuccess(String[] result)
						{
							boolean hasPrivilege = java.util.Arrays.asList(
									result).contains("Health Worker")
									| java.util.Arrays.asList(result).contains(
											"Program Admin")
									| java.util.Arrays.asList(result).contains(
											"Reporting")
									| java.util.Arrays.asList(result).contains(
											"Supervisor")
									| java.util.Arrays.asList(result).contains(
											"System Developer")
									| java.util.Arrays.asList(result).contains(
											"System Implementer");
							if (result == null)
								Window.alert("You don't have privileges to add encounters.");
							else
							{
								if (hasPrivilege)
								{
									EncounterId encounterId = new EncounterId(
											eID, creator, creator, formName);
									Encounter encounter = new Encounter(
											encounterId, TBR3Client
													.get(locationIDIntegerBox));
									encounter.setLocationId(TBR3Client
											.get(locationIDIntegerBox));
									encounter.setDateEntered(enteredDate);
									encounter.setDateStart(new Date());
									encounter.setDateEnd(new Date());
									ArrayList<EncounterResults> encounterResults = new ArrayList<EncounterResults>();
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"F_DATE"),
											DateTimeUtil.getFormattedDate(
													enteredDate,
													"yyyy-MM-dd HH:mm:ss")));
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"LOCATION_ID"), TBR3Client
													.get(locationIDIntegerBox)));
									encounterResults
											.add(new EncounterResults(
													new EncounterResultsId(eID,
															creator, creator,
															formName,
															"LOCATION_NAME"),
													TBR3Client
															.get(locationNameTextBox)));
									encounterResults
											.add(new EncounterResults(
													new EncounterResultsId(eID,
															creator, creator,
															formName,
															"LOCATION_TYPE"),
													TBR3Client
															.get(locationTypesListBox)));
									if (!address.equals(""))
										encounterResults
												.add(new EncounterResults(
														new EncounterResultsId(
																eID, creator,
																creator,
																formName,
																"ADDRESS"),
														address));
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"TOWN"), TBR3Client
													.get(townListBox)));
									if (!TBR3Client.get(mobileTextBox).equals(
											""))
										encounterResults
												.add(new EncounterResults(
														new EncounterResultsId(
																eID, creator,
																creator,
																formName,
																"MOBILE_NO"),
														TBR3Client
																.get(mobileTextBox)));
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"CAPACITY"), TBR3Client
													.get(capacityIntegerBox)));
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"SPECIALITY"), TBR3Client
													.get(specialityTextBox)));
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"START_TIME"),
											getTimeString(startHourMinutePicker
													.getMinute(),
													startHourMinutePicker
															.getHour())));
									if (!String.valueOf(
											endHourMinutePicker.getHour())
											.equals("-1"))
										encounterResults
												.add(new EncounterResults(
														new EncounterResultsId(
																eID, creator,
																creator,
																formName,
																"END_TIME"),
														getTimeString(
																endHourMinutePicker
																		.getMinute(),
																endHourMinutePicker
																		.getHour())));

									final Location location = new Location();
									location.setLocationName(TBR3Client
											.get(locationNameTextBox));
									location.setAddress1(address);

									locationAttributeValues.add(TBR3Client
											.get(locationIDIntegerBox));
									locationAttributeValues.add(TBR3Client.get(
											mobileTextBox).equals("") ? " "
											: TBR3Client.get(mobileTextBox));
									locationAttributeValues.add(TBR3Client
											.get(locationTypesListBox));
									locationAttributeValues.add(TBR3Client
											.get(townListBox));
									locationAttributeValues.add(TBR3Client
											.get(capacityIntegerBox));
									locationAttributeValues.add(TBR3Client
											.get(specialityTextBox));
									locationAttributeValues.add(String.valueOf(
											startHourMinutePicker.getHour())
											.equals("-1") ? "00:00:00"
											: getTimeString(
													startHourMinutePicker
															.getMinute(),
													startHourMinutePicker
															.getHour()));
									locationAttributeValues.add(String.valueOf(
											endHourMinutePicker.getHour())
											.equals("-1") ? "00:00:00"
											: getTimeString(endHourMinutePicker
													.getMinute(),
													endHourMinutePicker
															.getHour()));

									try
									{
										service.saveFormData(
												encounter,
												encounterResults
														.toArray(new EncounterResults[] {}),
												new AsyncCallback<String>()
												{

													@Override
													public void onFailure(
															Throwable caught)
													{
														caught.printStackTrace();
													}

													@Override
													public void onSuccess(
															String result)
													{
														if (result
																.equals("SUCCESS"))
														{
															service.saveLocation(
																	location,
																	TBR3.getCurrentUserName(),
																	TBR3.getPassword(),
																	locationAttributeValues
																			.toArray(new String[] {}),
																	new AsyncCallback<Boolean>()
																	{

																		@Override
																		public void onFailure(
																				Throwable caught)
																		{
																			caught.printStackTrace();

																		}

																		@Override
																		public void onSuccess(
																				Boolean result)
																		{
																			if (result == true)
																			{
																				Window.alert(CustomMessage
																						.getInfoMessage(InfoType.INSERTED));
																				clearUp();
																			}
																			else
																			{
																				Window.alert("Encounter saved successfully. Error saving location in OpenMRS.");
																			}
																		}
																	});

														}
														else
														{
															Window.alert(CustomMessage
																	.getErrorMessage(ErrorType.INSERT_ERROR)
																	+ "\nDetails: "
																	+ result);
														}
													}
												});
									}
									catch (Exception e)
									{
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								else
								{
									Window.alert("You don't have enough privileges to add encounters.");
								}

							}
						}

						@Override
						public void onFailure(Throwable caught)
						{
							// TODO Auto-generated method stub

						}
					});
		}
	}

	@Override
	public void fillData()
	{

	}

	@Override
	public void setCurrent()
	{

	}

	public static String getTimeString(int minutes, int hour)
	{
		String timeMinutes = String.valueOf(minutes);
		String timeHour = String.valueOf(hour);

		StringBuilder timeHoursStringBuilder = new StringBuilder();
		StringBuilder completeEndTimeStringBuilder = new StringBuilder();

		if (timeHour.length() == 1)
		{
			timeHoursStringBuilder.append("0").append(timeHour);
			timeHour = timeHoursStringBuilder.toString();
		}

		if (timeMinutes.length() == 1)
		{
			timeMinutes = "00";
		}

		completeEndTimeStringBuilder.append(timeHour).append(":" + timeMinutes)
				.append(":00");
		return completeEndTimeStringBuilder.toString();
	}

}
