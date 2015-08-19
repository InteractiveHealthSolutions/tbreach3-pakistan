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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
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
import com.ihsinformatics.tbr3fieldmonitoring.shared.RegexUtil;
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
public class CampInformationComposite extends Composite implements IForm, ClickHandler
{
	private static final ServerServiceAsync service = GWT.create(ServerService.class);
	private static final String formName = "CAMP_INFO";
	private static boolean valid;
	
	private FlexTable mainFlexTable = new FlexTable();
	private FlexTable userProfileFlexTable = new FlexTable();
	private FlexTable headerFlexTable = new FlexTable();
	private FlexTable campInfoFlexTable = new FlexTable();

	private FlowPanel userNamePanel = new FlowPanel();

	private Label loginAsLabel = new Label("Login As:");
	private Label usernameLabel = new Label("user");
	private Label leftBraceLabel = new Label("(");
	private Label rightBraceLabel = new Label(")");
	private Hyperlink logoutHyperlink = new Hyperlink("Logout,", "");
	private Hyperlink mainMenuHyperlink = new Hyperlink("Back to Menu", "");

	private Label formHeadingLabel = new Label("CAMP INFORMATION FORM");

	private Label formDateLabel = new Label("Form Date   ");
	private DateBox formDateBox = new DateBox();

	private Label locationIdLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();

	private TextBox locationNameTextBox = new TextBox();
	private Label locationNameLabel = new Label("Location Name   ");

	private Label townLabel = new Label("Town");
	private ListBox townListBox = new ListBox();

	private Label capacityLabel = new Label("Camp Capacity");
	private IntegerBox capacityIntegerBox = new IntegerBox();

	private Label staffMemberNamesLabel = new Label("Staff Members' Name");
	private TextArea staffMembeNamesTextArea = new TextArea();

	private Label campStartTimeLabel = new Label("Camp Start Time");
	HourMinutePicker campStartHourMinutePicker = new HourMinutePicker(
			PickerFormat._12_HOUR);

	private Label campEndTimeLabel = new Label("Camp End Time");
	private HourMinutePicker campEndHourMinutePicker = new HourMinutePicker(
			PickerFormat._12_HOUR);

	private Label campExpenseStaffLabel = new Label(
			"Per Camp Expense for Staff");
	private IntegerBox campExpenseStaffIntegerBox = new IntegerBox();

	private Label campExpenseOthersLabel = new Label(
			"Per Camp Expense for Others");
	private IntegerBox campExpenseOthersIntegerBox = new IntegerBox();

	private Label xrayRecommendedLabel = new Label("No. of Xray Recommended");
	private IntegerBox xrayRecommendedIntegerBox = new IntegerBox();
	
	private Anchor validateLocationIdAnchor = new Anchor("Validate Location ID", false);

	private Button submitButton = new Button("Submit");

	MainMenuComposite mainMenu;

	@SuppressWarnings("deprecation")
	public CampInformationComposite()
	{
		initWidget(mainFlexTable);
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
		campInfoFlexTable.setWidget(1, 0, formDateLabel);
		formDateLabel.addStyleName("text");

		campInfoFlexTable.setWidget(1, 1, formDateBox);
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		campInfoFlexTable.setWidget(2, 0, locationIdLabel);
		locationIdLabel.addStyleName("text");

		campInfoFlexTable.setWidget(2, 1, locationIdIntegerBox);
		locationIdIntegerBox.setStyleName("textbox");
		
		campInfoFlexTable.setWidget(3, 0, locationNameLabel);
		locationNameLabel.addStyleName("text");
		
		campInfoFlexTable.setWidget(3, 1, locationNameTextBox);
		locationNameTextBox.addStyleName("textbox");
		
		campInfoFlexTable.setWidget(4, 1, validateLocationIdAnchor);
		validateLocationIdAnchor.addStyleName("hyperlink");

		campInfoFlexTable.setWidget(5, 0, townLabel);
		townLabel.addStyleName("text");

		campInfoFlexTable.setWidget(5, 1, townListBox);
		townListBox.setName("TOWN");

		campInfoFlexTable.setWidget(6, 0, capacityLabel);
		capacityLabel.addStyleName("text");

		campInfoFlexTable.setWidget(6, 1, capacityIntegerBox);
		capacityIntegerBox.addStyleName("textbox");

		campInfoFlexTable.setWidget(7, 0, staffMemberNamesLabel);
		staffMemberNamesLabel.addStyleName("text");

		campInfoFlexTable.setWidget(7, 1, staffMembeNamesTextArea);
		staffMembeNamesTextArea.addStyleName("textbox");

		campInfoFlexTable.setWidget(8, 0, campStartTimeLabel);
		campStartTimeLabel.addStyleName("text");

		campInfoFlexTable.setWidget(8, 1, campStartHourMinutePicker);
		campStartHourMinutePicker.setWidth("170px");

		campInfoFlexTable.setWidget(9, 0, campEndTimeLabel);
		campEndTimeLabel.addStyleName("text");

		campInfoFlexTable.setWidget(9, 1, campEndHourMinutePicker);

		campInfoFlexTable.setWidget(10, 0, campExpenseStaffLabel);
		campExpenseStaffLabel.addStyleName("text");

		campInfoFlexTable.setWidget(10, 1, campExpenseStaffIntegerBox);
		campExpenseStaffIntegerBox.addStyleName("textbox");

		campInfoFlexTable.setWidget(11, 0, campExpenseOthersLabel);
		campExpenseOthersLabel.addStyleName("text");

		campInfoFlexTable.setWidget(11, 1, campExpenseOthersIntegerBox);
		campExpenseOthersIntegerBox.addStyleName("textbox");

		campInfoFlexTable.setWidget(12, 0, xrayRecommendedLabel);
		xrayRecommendedLabel.addStyleName("text");

		campInfoFlexTable.setWidget(12, 1, xrayRecommendedIntegerBox);
		xrayRecommendedIntegerBox.addStyleName("textbox");

		campInfoFlexTable.setWidget(13, 1, submitButton);
		submitButton.setStyleName("submitButton");
		submitButton.setSize("169", "30");

		mainFlexTable.setWidget(0, 0, userProfileFlexTable);
		mainFlexTable.setWidget(1, 0, headerFlexTable);
		mainFlexTable.setWidget(2, 0, campInfoFlexTable);
		
		campInfoFlexTable.setSize("80%", "");
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(2, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(3, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(4, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(5, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(6, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(7, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(8, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(9, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(10, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(11, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(12, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(13, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		campInfoFlexTable.getCellFormatter().setHorizontalAlignment(14, 1,
				HasHorizontalAlignment.ALIGN_LEFT);

		mainFlexTable.setBorderWidth(1);
		
		usernameLabel.setText(TBR3.getCurrentUserName());
		
		submitButton.addClickHandler(this);
		validateLocationIdAnchor.addClickHandler(this);
		mainMenuHyperlink.addClickHandler(this);
		logoutHyperlink.addClickHandler(this);
		
		TBR3Client.refresh(campInfoFlexTable);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (sender == mainMenuHyperlink)
		{
			mainMenu = new MainMenuComposite();
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(mainMenu);
		}
		else if (sender == validateLocationIdAnchor)
		{
			locationNameTextBox.setText("");
			if (RegexUtil.isLocationID(TBR3Client.get(locationIdIntegerBox)))
			{
				service.getLocation(TBR3.getCurrentUserName(),
						TBR3.getPassword(),
						TBR3Client.get(locationIdIntegerBox),
						new AsyncCallback<String>()
						{
							@Override
							public void onSuccess(String result)
							{
								if (!result.contains("FAILED"))
								{
									locationNameTextBox.setText("");
									locationNameTextBox.setText(result);
									Window.alert("Loacation found: " + result);
								}
								else
								{
									Window.alert("Location "
											+ TBR3Client
													.get(locationIdIntegerBox)
											+ ":"
											+ CustomMessage
													.getErrorMessage(ErrorType.ITEM_NOT_FOUND));
								}
							}

							@Override
							public void onFailure(Throwable caught)
							{
								// TODO Auto-generated method stub

							}
						});
			}
			else
			{
				Window.alert("Enter 6-digit Location ID.");
			}
		}
		else if(sender == logoutHyperlink)
		{
			Tbr3fieldmonitoring.logout();
		}
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#clearUp()
	 */
	@Override
	public void clearUp()
	{
		TBR3Client.clearControls(campInfoFlexTable);
		IntegerBox[] integerBoxes = { locationIdIntegerBox, capacityIntegerBox,  campExpenseStaffIntegerBox, campExpenseOthersIntegerBox, xrayRecommendedIntegerBox};

		ListBox[] listBoxes = { townListBox  };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("0");
		for (int i = 0; i < listBoxes.length; i++)
			listBoxes[i].setSelectedIndex(0);
		locationIdIntegerBox.setText("000000");
		formDateBox.getTextBox().setText("");
		campStartHourMinutePicker.clear();
		campEndHourMinutePicker.clear();
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#validate()
	 */
	@Override
	public boolean validate()
	{
		valid = true;
		StringBuilder errorMessage = new StringBuilder();
		if(TBR3Client.get(locationIdIntegerBox).equals(""))
			errorMessage.append("Location ID: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
		if(TBR3Client.get(locationNameTextBox).equals(""))
			errorMessage.append("Location name: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
		if(TBR3Client.get(capacityIntegerBox).equals(""))
			errorMessage.append("Capacity: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
		if(TBR3Client.get(staffMembeNamesTextArea).equals(""))
			errorMessage.append("Staff Members' Names: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
		if (campStartHourMinutePicker.getHour() == -1)
			errorMessage.append("Camp Start Time: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if (campEndHourMinutePicker.getHour() == -1)
			errorMessage.append("Camp End Time: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if (TBR3Client.get(campExpenseStaffIntegerBox).equals(""))
			errorMessage.append("Per Camp Expense for Staff: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if (TBR3Client.get(campExpenseOthersIntegerBox).equals(""))
			errorMessage.append("Per Camp Expense for Others: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if (TBR3Client.get(xrayRecommendedIntegerBox).equals(""))
			errorMessage.append("No. of Xray Recommended: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");

		valid = errorMessage.length() == 0;
		if (!valid)
		{
			Window.alert(errorMessage.toString());
		}
		return valid;
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#saveData()
	 */
	@Override
	public void saveData()
	{
		if(validate())
		{
			final Date enteredDate = new Date();
			final int eID = 0;
			final String creator = TBR3.getCurrentUserName(); // pid1

			service.getUserRoles(TBR3.getCurrentUserName(), TBR3.getPassword(),
			new AsyncCallback<String[]>()
			{
				@Override
				public void onFailure(Throwable caught)
				{
					// TODO Auto-generated method stub

				}

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
											.get(locationIdIntegerBox));
							encounter.setLocationId(TBR3Client
									.get(locationIdIntegerBox));
							encounter.setDateEntered(enteredDate);
							encounter.setDateStart(new Date());
							encounter.setDateEnd(new Date());
							ArrayList<EncounterResults> encounterResults = new ArrayList<EncounterResults>();
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "F_DATE"), DateTimeUtil.getFormattedDate(enteredDate, "yyyy-MM-dd HH:mm:ss")));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "LOCATION_ID"), TBR3Client.get(locationIdIntegerBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator,	formName, "LOCATION_NAME"), TBR3Client.get(locationNameTextBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID,creator, creator, formName,"TOWN"), TBR3Client.get(townListBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID,creator, creator, formName,"LOCATION_CAPACITY"), TBR3Client.get(capacityIntegerBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID,creator, creator, formName,"STAFF_MEMBER_NAMES"), TBR3Client.get(staffMembeNamesTextArea)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID,creator, creator, formName,"CAMP_START_TIME"), FirstVisitComposite.getTimeString(campStartHourMinutePicker.getMinute(), campStartHourMinutePicker.getHour())));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID,creator, creator, formName,"CAMP_END_TIME"), FirstVisitComposite.getTimeString(campEndHourMinutePicker.getMinute(), campEndHourMinutePicker.getHour())));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID,creator, creator, formName,"CAMP_EXPENSE_STAFF"), TBR3Client.get(campExpenseStaffIntegerBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID,creator, creator, formName,"CAMP_EXPENSE_OTHERS"), TBR3Client.get(campExpenseOthersIntegerBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID,creator, creator, formName,"XRAY_TOTAL_NO"), TBR3Client.get(xrayRecommendedIntegerBox)));
							
							
							// TODO : complete it
							
							final Location location = new Location();
							location.setLocationName(TBR3Client
									.get(locationNameTextBox));

							try
							{
								service.saveFormData(encounter,	encounterResults.toArray(new EncounterResults[] {}), new AsyncCallback<String>()
								{

									@Override
									public void onFailure(
											Throwable caught)
									{
										caught.printStackTrace();
									}

									@Override
									public void onSuccess(String result)
									{
										if (result.equals("SUCCESS"))
										{

											Window.alert(CustomMessage.getInfoMessage(InfoType.INSERTED));
											clearUp();
										}
										else
										{
											Window.alert(CustomMessage.getErrorMessage(ErrorType.INSERT_ERROR) + "\nDetails: " + result);
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

					}
				}
			});
		}
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#fillData()
	 */
	@Override
	public void fillData()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#setCurrent()
	 */
	@Override
	public void setCurrent()
	{
		// TODO Auto-generated method stub
		
	}

}
