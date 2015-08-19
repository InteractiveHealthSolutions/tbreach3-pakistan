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
public class SupervisorVisitComposite extends Composite implements IForm,
		ClickHandler
{

	private static ServerServiceAsync service = GWT.create(ServerService.class);
	private static final String formName = "SUPER_VIS";

	private boolean valid;

	private FlexTable mainFlexTable = new FlexTable();
	private FlexTable userProfileFlexTable = new FlexTable();
	private FlexTable headerFlexTable = new FlexTable();
	private FlexTable supervisorVisitFlexTable = new FlexTable();

	private FlowPanel userNamePanel = new FlowPanel();

	private Label loginAsLabel = new Label("Login As:");
	private Label usernameLabel = new Label("user");
	private Label leftBraceLabel = new Label("(");
	private Label rightBraceLabel = new Label(")");
	private Hyperlink logoutHyperlink = new Hyperlink("Logout,", "");
	private Hyperlink mainMenuHyperlink = new Hyperlink("Back to Menu", "");

	private Label formHeadingLabel = new Label("SUPERVISOR VISIT FORM");

	private Label formDateLabel = new Label("Form Date   ");
	private DateBox formDateBox = new DateBox();

	private Label locationIdLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();

	private TextBox locationNameTextBox = new TextBox();
	private Label locationNameLabel = new Label("Location Name   ");

	private Label visitDateLabel = new Label("Visit Date");
	private DateBox visitDateBox = new DateBox();

	private Label visitTimeLabel = new Label("Visit Time");
	private HourMinutePicker visitHourMinutePicker = new HourMinutePicker(
			PickerFormat._12_HOUR);

	private Label townLabel = new Label("Town");
	private ListBox townListBox = new ListBox();

	private Label gpPotentialLabel = new Label("GP Potential");
	private ListBox gpPotentialListBox = new ListBox();

	private Label mrLastVisitLabel = new Label("MR Last Visit");
	private DateBox mrLastVisitDateBox = new DateBox();

	private Label relationshipLabel = new Label("Relationship of MR/GP");
	private TextArea relationshipTextArea = new TextArea();

	private Anchor validateLocationIdAnchor = new Anchor(
			"Validate Location ID", false);

	private Button submitButton = new Button("Submit");

	MainMenuComposite mainMenu;

	@SuppressWarnings("deprecation")
	public SupervisorVisitComposite()
	{
		initWidget(mainFlexTable);

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
		supervisorVisitFlexTable.setWidget(1, 0, formDateLabel);
		formDateLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(1, 1, formDateBox);
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		supervisorVisitFlexTable.setWidget(2, 0, locationIdLabel);
		locationIdLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(2, 1, locationIdIntegerBox);
		locationIdIntegerBox.setMaxLength(6);
		locationIdIntegerBox.setVisibleLength(6);
		locationIdIntegerBox.addStyleName("textbox");

		supervisorVisitFlexTable.setWidget(3, 1, validateLocationIdAnchor);
		validateLocationIdAnchor.addStyleName("hyperlink");

		supervisorVisitFlexTable.setWidget(4, 0, locationNameLabel);
		locationNameLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(4, 1, locationNameTextBox);
		locationNameTextBox.addStyleName("textbox");

		supervisorVisitFlexTable.setWidget(5, 0, townLabel);
		townListBox.setName("TOWN");
		townLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(5, 1, townListBox);

		supervisorVisitFlexTable.setWidget(6, 0, visitDateLabel);
		visitDateLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(6, 1, visitDateBox);
		visitDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));
		visitDateBox.addStyleName("textbox");

		supervisorVisitFlexTable.setWidget(7, 0, visitTimeLabel);
		visitTimeLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(7, 1, visitHourMinutePicker);

		supervisorVisitFlexTable.setWidget(8, 0, gpPotentialLabel);
		gpPotentialLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(8, 1, gpPotentialListBox);
		gpPotentialListBox.setName("GP_POTENTIAL_RATING");

		supervisorVisitFlexTable.setWidget(9, 0, mrLastVisitLabel);
		mrLastVisitLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(9, 1, mrLastVisitDateBox);
		mrLastVisitDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));
		mrLastVisitDateBox.addStyleName("textbox");

		supervisorVisitFlexTable.setWidget(10, 0, relationshipLabel);
		relationshipLabel.addStyleName("text");

		supervisorVisitFlexTable.setWidget(10, 1, relationshipTextArea);
		relationshipTextArea.addStyleName("textbox");

		supervisorVisitFlexTable.setWidget(11, 1, submitButton);
		submitButton.setStyleName("submitButton");
		submitButton.setSize("169", "30");

		// loginFlexTable.setStyleName("flexTableCell");

		mainFlexTable.setWidget(0, 0, userProfileFlexTable);
		mainFlexTable.setWidget(1, 0, headerFlexTable);
		mainFlexTable.setWidget(2, 0, supervisorVisitFlexTable);

		supervisorVisitFlexTable.setSize("100%", "");
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(1,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(2,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(3,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(4,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(5,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(6,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(7,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(8,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(9,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(10,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(11,
				1, HasHorizontalAlignment.ALIGN_LEFT);
		supervisorVisitFlexTable.getCellFormatter().setHorizontalAlignment(12,
				1, HasHorizontalAlignment.ALIGN_LEFT);

		mainFlexTable.setBorderWidth(1);

		usernameLabel.setText(TBR3.getCurrentUserName());

		logoutHyperlink.addClickHandler(this);
		mainMenuHyperlink.addClickHandler(this);
		validateLocationIdAnchor.addClickHandler(this);
		submitButton.addClickHandler(this);

		TBR3Client.refresh(supervisorVisitFlexTable);

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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#clearUp()
	 */
	@Override
	public void clearUp()
	{
		TBR3Client.clearControls(supervisorVisitFlexTable);
		IntegerBox[] integerBoxes = { locationIdIntegerBox };

		ListBox[] listBoxes = { townListBox, gpPotentialListBox };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("000000");
		for (int i = 0; i < listBoxes.length; i++)
			listBoxes[i].setSelectedIndex(0);
		formDateBox.getTextBox().setText("");
		visitDateBox.getTextBox().setText("");
		mrLastVisitDateBox.getTextBox().setText("");
		visitHourMinutePicker.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#validate()
	 */
	@Override
	public boolean validate()
	{
		valid = true;

		StringBuilder errorMessage = new StringBuilder();

		if (TBR3Client.get(locationIdIntegerBox).equals(""))
			errorMessage.append("Location ID: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (TBR3Client.get(locationNameTextBox).equals(""))
			errorMessage.append("Location Name: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (TBR3Client.get(visitDateBox).equals(""))
			errorMessage.append("Visit Date: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (visitHourMinutePicker.getHour() == -1)
			errorMessage.append("Visit time: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (TBR3Client.get(mrLastVisitDateBox).equals(""))
			errorMessage.append("MR Last Visit Date: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (TBR3Client.get(relationshipTextArea).equals(""))
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#saveData()
	 */
	@Override
	public void saveData()
	{
		if (validate())
		{
			final Date enteredDate = new Date();
			final int eID = 0;
			final String creator = TBR3.getCurrentUserName(); // pid1

			final String visitDate = TBR3Client.get(visitDateBox).split(
					"\\s+")[0]
					+ " "
					+ FirstVisitComposite.getTimeString(
							visitHourMinutePicker.getMinute(),
							visitHourMinutePicker.getHour());

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
													.get(locationIdIntegerBox)));
									encounterResults
											.add(new EncounterResults(
													new EncounterResultsId(eID,
															creator, creator,
															formName,
															"LOCATION_NAME"),
													TBR3Client
															.get(locationNameTextBox)));
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"TOWN"), TBR3Client
													.get(townListBox)));
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"VISIT_DATE"),
											visitDate));
									encounterResults.add(new EncounterResults(
											new EncounterResultsId(eID,
													creator, creator, formName,
													"GP_POTENTIAL"), TBR3Client
													.get(gpPotentialListBox)));
									encounterResults
											.add(new EncounterResults(
													new EncounterResultsId(eID,
															creator, creator,
															formName,
															"MR_LAST_VISIT_DATE"),
													TBR3Client
															.get(mrLastVisitDateBox)));
									encounterResults
											.add(new EncounterResults(
													new EncounterResultsId(eID,
															creator, creator,
															formName,
															"RELATIONSHIP_MR_GP"),
													TBR3Client
															.get(relationshipTextArea)));

									final Location location = new Location();
									location.setLocationName(TBR3Client
											.get(locationNameTextBox));

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

															Window.alert(CustomMessage
																	.getInfoMessage(InfoType.INSERTED));
															clearUp();
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

							}

						}
					});

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#fillData()
	 */
	@Override
	public void fillData()
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#setCurrent()
	 */
	@Override
	public void setCurrent()
	{
		// TODO Auto-generated method stub

	}
}
