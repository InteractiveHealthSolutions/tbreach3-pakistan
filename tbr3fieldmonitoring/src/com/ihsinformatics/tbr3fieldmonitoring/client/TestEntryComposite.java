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

import org.hsqldb.Server;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import com.google.gwt.user.client.ui.RadioButton;
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

/**
 * @author Tahira
 * 
 */
public class TestEntryComposite extends Composite implements IForm, ClickHandler, ValueChangeHandler<Boolean>
{
	private static final ServerServiceAsync service = GWT.create(ServerService.class);
	private static final String formName = "TEST_ENTRY";
	private static boolean valid;
	
	private FlexTable mainFlexTable = new FlexTable();
	private FlexTable userProfileFlexTable = new FlexTable();
	private FlexTable headerFlexTable = new FlexTable();
	private FlexTable dailyVisitFlexTable = new FlexTable();
	private FlexTable patientResultsFlexTable = new FlexTable();
	
	
	///testing Test Type : Spirometry and Diabetes Toggle
	private FlowPanel testTypeResultFlowPanel = new FlowPanel();
	private Label patientResultLabel = new Label("Patient Result/Score");
	private TextBox patientResultTextBox = new TextBox();
	
	private Label spirometryResultLabel = new Label("Spirometry Results");
	private Label actualResultLabel = new Label("Actual Result");
	private Label predictedResultLabel = new Label("Predicted Result");
	private Label percentageLabel = new Label("%");
	
	private Label fvcLabel = new Label("FVC");
	private TextBox fvcActualResultTextBox = new TextBox();
	private TextBox fvcPredictedResultTextBox = new TextBox();
	private TextBox fvcPercentageResultTextBox = new TextBox();
	
	private Label fev1Label = new Label("FEV1");
	private TextBox fev1ActualResultTextBox = new TextBox();
	private TextBox fev1PredictedResultTextBox = new TextBox();
	private TextBox fev1PercentageResultTextBox = new TextBox();
	
	private Label ratioLabel = new Label("FVC/FEV1 Ratio");
	private TextBox ratioActualResultTextBox = new TextBox();
	private TextBox ratioPredictedResultTextBox = new TextBox();
	private TextBox ratioPercentageResultTextBox = new TextBox();
	
	private Label pefLabel = new Label("PEF Result");
	private TextBox pefActualResultTextBox = new TextBox();
	private TextBox pefPredictedResultTextBox = new TextBox();
	private TextBox pefPercentageResultTextBox = new TextBox();
	
    // till here	

	private FlowPanel userNamePanel = new FlowPanel();

	private Label loginAsLabel = new Label("Login As:");
	private Label usernameLabel = new Label("user");
	private Label leftBraceLabel = new Label("(");
	private Label rightBraceLabel = new Label(")");
	private Hyperlink logoutHyperlink = new Hyperlink("Logout,", "");
	private Hyperlink mainMenuHyperlink = new Hyperlink("Back to Menu", "");

	private Label formHeadingLabel = new Label("DIABETES/SPIROMETRY ENTRY FORM");

	private Label formDateLabel = new Label("Form Date   ");
	private DateBox formDateBox = new DateBox();

	private Label locationIdLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();

	private TextBox locationNameTextBox = new TextBox();
	private Label locationNameLabel = new Label("Location Name   ");

	private Label patientAgeLabel = new Label("Patient Age");
	private TextBox patientAgeTextBox = new TextBox();

	private Label patientGenderLabel = new Label("Patient Gender");
	private FlowPanel patientGenderFlowPanel = new FlowPanel();
	private RadioButton genderMaleRadioButton = new RadioButton(
			"genderRadioGroup", "Male");
	private RadioButton genderFemaleRadioButton = new RadioButton(
			"genderRadioGroup", "Female");

	private Label testTypeLabel = new Label("Test Type");
	private FlowPanel testTypeFlowPanel = new FlowPanel();
	private RadioButton testDiabetesRadioButton = new RadioButton(
			"testTypeRadioGroup", "Diabetes");
	private RadioButton testSpirometryRadioButton = new RadioButton(
			"testTypeRadioGroup", "Spirometry");
	
	private Anchor validateLocationIdAnchor = new Anchor("Validate Location ID", false);

	private Button submitButton = new Button("Submit");

	MainMenuComposite mainMenu;

	public TestEntryComposite()
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
		dailyVisitFlexTable.setWidget(1, 0, formDateLabel);
		formDateLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(1, 1, formDateBox);
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		dailyVisitFlexTable.setWidget(2, 0, locationIdLabel);
		locationIdLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(2, 1, locationIdIntegerBox);
		locationIdIntegerBox.addStyleName("textbox");
		
		dailyVisitFlexTable.setWidget(3, 1, validateLocationIdAnchor);
		validateLocationIdAnchor.addStyleName("hyperlink");

		dailyVisitFlexTable.setWidget(4, 0, locationNameLabel);
		locationNameLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(4, 1, locationNameTextBox);
		locationNameTextBox.addStyleName("textbox");

		dailyVisitFlexTable.setWidget(5, 0, patientAgeLabel);
		patientAgeLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(5, 1, patientAgeTextBox);
		patientAgeTextBox.addStyleName("textbox");

		dailyVisitFlexTable.setWidget(6, 0, patientGenderLabel);
		patientGenderLabel.addStyleName("text");

		patientGenderFlowPanel.add(genderMaleRadioButton);
		patientGenderFlowPanel.add(genderFemaleRadioButton);

		dailyVisitFlexTable.setWidget(6, 1, patientGenderFlowPanel);

		dailyVisitFlexTable.setWidget(7, 0, testTypeLabel);
		testTypeLabel.addStyleName("text");

		testTypeFlowPanel.add(testDiabetesRadioButton);
		testTypeFlowPanel.add(testSpirometryRadioButton);

		dailyVisitFlexTable.setWidget(7, 1, testTypeFlowPanel);
		
		//testing Patient Test Result Flow Panel
//		testTypeResultFlowPanel.add(patientResultLabel);
//		testTypeResultFlowPanel.add(patientResultTextBox);
//		patientResultTextBox.addStyleName(".table-textbox");
//		testTypeResultFlowPanel.add(fvcActualResultTextBox);
//		fvcActualResultTextBox.addStyleName(".table-textbox");
//		testTypeResultFlowPanel.add(TextBox);
//		TextBox.addStyleName(".table-textbox");
//		patientResultLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
//		patientResultTextBox.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
//		fvcActualResultTextBox.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
//		TextBox.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
//		
		//testTypeResultFlowPanel.addStyleName("flowPanel");
		
		patientResultsFlexTable.setWidget(1, 0, spirometryResultLabel);
		spirometryResultLabel.addStyleName("text-bold");
		patientResultsFlexTable.setWidget(1, 1, actualResultLabel);
		actualResultLabel.addStyleName("text");
		patientResultsFlexTable.setWidget(1, 2, predictedResultLabel);
		predictedResultLabel.addStyleName("text");
		patientResultsFlexTable.setWidget(1, 3, percentageLabel);
		percentageLabel.addStyleName("text");
		
		patientResultsFlexTable.setWidget(2, 0, fvcLabel);
		fvcLabel.addStyleName("text");
		patientResultsFlexTable.setWidget(2, 1, fvcActualResultTextBox);
		patientResultsFlexTable.setWidget(2, 2, fvcPredictedResultTextBox);
		patientResultsFlexTable.setWidget(2, 3, fvcPercentageResultTextBox);
		
		
		
		//patientResultsFlexTable.setWidget(0, 0, testTypeResultFlowPanel);
		//patientResultsFlexTable.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		//till here 
		

		dailyVisitFlexTable.setWidget(8, 1, submitButton);
		submitButton.setStyleName("submitButton");
		submitButton.setSize("169", "30");

		// loginFlexTable.setStyleName("flexTableCell");

		mainFlexTable.setWidget(0, 0, userProfileFlexTable);
		mainFlexTable.setWidget(1, 0, headerFlexTable);
		mainFlexTable.setWidget(2, 0, dailyVisitFlexTable);

		dailyVisitFlexTable.setSize("80%", "");
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(1, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(2, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(3, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(4, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(5, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(6, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(7, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(8, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(9, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(10, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(11, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(12, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(13, 1,
				HasHorizontalAlignment.ALIGN_LEFT);
		dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(14, 1,
				HasHorizontalAlignment.ALIGN_LEFT);

		mainFlexTable.setBorderWidth(1);

		usernameLabel.setText(TBR3.getCurrentUserName());
		
		submitButton.addClickHandler(this);
		validateLocationIdAnchor.addClickHandler(this);
		logoutHyperlink.addClickHandler(this);
		mainMenuHyperlink.addClickHandler(this);
		testDiabetesRadioButton.addValueChangeHandler(this);
		testSpirometryRadioButton.addValueChangeHandler(this);

		// mainMenuHyperlink.addClickHandler(this);
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
		 else if(sender == mainMenuHyperlink)
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
		 else if (sender == logoutHyperlink)
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
		TBR3Client.clearControls(dailyVisitFlexTable);
		IntegerBox[] integerBoxes = { locationIdIntegerBox };

		//ListBox[] listBoxes = { townListBox  };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("0");
//		for (int i = 0; i < listBoxes.length; i++)
//			listBoxes[i].setSelectedIndex(0);
		locationIdIntegerBox.setText("000000");
		formDateBox.getTextBox().setText("");
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3fieldmonitoring.client.IForm#validate()
	 */
	@Override
	public boolean validate()
	{
		valid = true;
		StringBuilder errorMessage = new StringBuilder();
//		if(TBR3Client.get(locationIdIntegerBox).equals(""))
//			errorMessage.append("Location ID: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
//		if(TBR3Client.get(locationNameTextBox).equals(""))
//			errorMessage.append("Location name: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
//		if(TBR3Client.get(visitDateBox).equals(""))
//			errorMessage.append("Visit Date: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
//		if (arrivalHourMinutePicker.getHour() == -1)
//			errorMessage.append("Arrival time: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if (departureHourMinutePicker.getHour() == -1)
//			errorMessage.append("Departure time: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if (!metGpYesRadioButton.isChecked() && !metGpNoRadioButton.isChecked())
//			errorMessage.append("Met GP: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if (!givenCouponsYesRadioButton.isChecked() && !givenCouponsNoRadioButton.isChecked())
//			errorMessage.append("Given Coupons/Rx Pads: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if (!marketingYesRadioButton.isChecked() && !marketingNoRadioButton.isChecked())
//			errorMessage.append("Marketing Activity: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if(TBR3Client.get(marketingDescriptionTextArea).equals(""))
//			errorMessage.append("Marketing Activity Description: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if(TBR3Client.get(marketingBudgetItemsTextBox).equals(""))
//			errorMessage.append("Marketing Budget Items: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if(TBR3Client.get(amountIntegerBox).equals(""))
//			errorMessage.append("Amount: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if(TBR3Client.get(countIntegerBox).equals(""))
//			errorMessage.append("Count: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		if(TBR3Client.get(commentsTextArea).equals(""))
//			errorMessage.append("Comments: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
//		
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
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "F_DATE"), DateTimeUtil.getFormattedDate(	enteredDate, "yyyy-MM-dd HH:mm:ss")));
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "LOCATION_ID"), TBR3Client.get(locationIdIntegerBox)));
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator,	formName, "LOCATION_NAME"), TBR3Client.get(locationNameTextBox)));
									
									// TODO : complete it
									
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
	
	@Override
	public void fillData()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setCurrent()
	{
		
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event)
	{
		Widget sender = (Widget) event.getSource();
		if(sender == testSpirometryRadioButton)
		{
			dailyVisitFlexTable.remove(submitButton);
			dailyVisitFlexTable.remove(patientResultLabel);
			dailyVisitFlexTable.remove(patientResultTextBox);
			patientResultsFlexTable.setWidget(3, 1, submitButton);
			patientResultsFlexTable.getCellFormatter().setHorizontalAlignment(3	, 1, HasHorizontalAlignment.ALIGN_RIGHT);
			mainFlexTable.setWidget(3, 0, patientResultsFlexTable);
//			dailyVisitFlexTable.setWidget(8, 1, patientResultsFlexTable);
			//dailyVisitFlexTable.setWidget(9, 1, submitButton);
		}
		else if(sender == testDiabetesRadioButton)
		{
			mainFlexTable.remove(patientResultsFlexTable);
			
			dailyVisitFlexTable.remove(submitButton);
			dailyVisitFlexTable.setWidget(8, 0, patientResultLabel);
			patientResultLabel.addStyleName("text");
			dailyVisitFlexTable.getCellFormatter().setHorizontalAlignment(8, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			dailyVisitFlexTable.setWidget(8, 1, patientResultTextBox);
			patientResultTextBox.addStyleName("textbox");
			dailyVisitFlexTable.setWidget(9, 1, submitButton);
		}
	}

}