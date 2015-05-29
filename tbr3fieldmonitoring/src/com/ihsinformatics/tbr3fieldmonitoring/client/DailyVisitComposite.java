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

import java.awt.Choice;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.velocity.runtime.directive.Break;

import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.web.bindery.requestfactory.vm.impl.Deobfuscator;
import com.ihsinformatics.tbr3fieldmonitoring.server.ServerServiceImpl;
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
public class DailyVisitComposite extends Composite implements IForm, ClickHandler, ValueChangeHandler<Boolean>
{
	private static Logger logger = Logger.getLogger(DailyVisitComposite.class.getName());
	
	private static ServerServiceAsync service = GWT.create(ServerService.class);
	private static final String formName = "DAILY_VIS";

	private List<String[]> mainItemsCount = new ArrayList<String[]>();
	
	private List<String> itemsList = new ArrayList<String>(); 
	
	private String[] singleItemWithCount;
	
	private static boolean valid;
	
	private FlexTable mainFlexTable = new FlexTable();
	private FlexTable userProfileFlexTable = new FlexTable();
	private FlexTable headerFlexTable = new FlexTable();
	private FlexTable dailyVisitFlexTable = new FlexTable();

	private FlowPanel userNamePanel = new FlowPanel();

	private Label loginAsLabel = new Label("Login As:");
	private Label usernameLabel = new Label("user");
	private Label leftBraceLabel = new Label("(");
	private Label rightBraceLabel = new Label(")");
	private Hyperlink logoutHyperlink = new Hyperlink("Logout,", "");
	private Hyperlink mainMenuHyperlink = new Hyperlink("Back to Menu", "");

	private Label formHeadingLabel = new Label("DAILY VISIT FORM");

	private Label formDateLabel = new Label("Form Date   ");
	private DateBox formDateBox = new DateBox();

	private Label locationIDLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();

	private TextBox locationNameTextBox = new TextBox();
	private Label locationNameLabel = new Label("Location Name   ");

	private Label townLabel = new Label("Town");
	private ListBox townListBox = new ListBox();

	private Label visitDateLabel = new Label("Visit Date");
	private DateBox visitDateBox = new DateBox();
	
	private Label arrivalTimeLabel = new Label("Arrival Time");
	private HourMinutePicker arrivalHourMinutePicker = new HourMinutePicker(
			PickerFormat._12_HOUR);

	private Label departureTimeLabel = new Label("Departure Time");
	private HourMinutePicker departureHourMinutePicker = new HourMinutePicker(
			PickerFormat._12_HOUR);

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

	private Label marketingActivityLabel = new Label("Marketing Activity");
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

	private Label institutionalMarketingItemsLabel = new Label(
			"Institutional Marketing Items");
	private ListBox institutionalMarketingItemsListBox = new ListBox();

	private Label countLabel = new Label("Count");
	private IntegerBox countIntegerBox = new IntegerBox();

	private Button addMoreButton = new Button("(Add Item...)");

	private Label gpPotentialLabel = new Label("GP Potential");
	private ListBox gpPotentialListBox = new ListBox();

	private Label commentsLabel = new Label("Comments");
	private TextArea commentsTextArea = new TextArea();
	
	private Anchor validateLocationIdAnchor = new Anchor(
			"Validate Location ID", false);

	private Button submitButton = new Button("Submit");

	MainMenuComposite mainMenu;

	public DailyVisitComposite()
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

		dailyVisitFlexTable.setWidget(2, 0, locationIDLabel);
		locationIDLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(2, 1, locationIdIntegerBox);
		locationIdIntegerBox.setStyleName("textbox");

		dailyVisitFlexTable.setWidget(3, 1, validateLocationIdAnchor);
		validateLocationIdAnchor.addStyleName("hyperlink");
		
		dailyVisitFlexTable.setWidget(4, 0, locationNameLabel);
		locationNameLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(4, 1, locationNameTextBox);
		locationNameTextBox.addStyleName("textbox");

		dailyVisitFlexTable.setWidget(5, 0, townLabel);
		townLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(5, 1, townListBox);
		townListBox.setName("TOWN");

		dailyVisitFlexTable.setWidget(6, 0, visitDateLabel);
		visitDateLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(6, 1, visitDateBox);
		visitDateBox.addStyleName("textbox");
		visitDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));

		dailyVisitFlexTable.setWidget(7, 0, arrivalTimeLabel);
		arrivalTimeLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(7, 1, arrivalHourMinutePicker);

		dailyVisitFlexTable.setWidget(8, 0, departureTimeLabel);
		departureTimeLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(8, 1, departureHourMinutePicker);

		dailyVisitFlexTable.setWidget(9, 0, metGpLabel);
		metGpLabel.addStyleName("text");

		metGpFlowPanel.add(metGpYesRadioButton);
		metGpFlowPanel.add(metGpNoRadioButton);

		dailyVisitFlexTable.setWidget(9, 1, metGpFlowPanel);

		dailyVisitFlexTable.setWidget(10, 0, givenCouponsLabel);
		givenCouponsLabel.addStyleName("text");

		givenCouponsFlowPanel.add(givenCouponsYesRadioButton);
		givenCouponsFlowPanel.add(givenCouponsNoRadioButton);

		dailyVisitFlexTable.setWidget(10, 1, givenCouponsFlowPanel);

		dailyVisitFlexTable.setWidget(11, 0, marketingActivityLabel);
		marketingActivityLabel.addStyleName("text");

		marketingActivityFlowPanel.add(marketingYesRadioButton);
		marketingActivityFlowPanel.add(marketingNoRadioButton);

		dailyVisitFlexTable.setWidget(11, 1, marketingActivityFlowPanel);

		dailyVisitFlexTable.setWidget(12, 0, marketingDescriptionLabel);
		marketingDescriptionLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(12, 1, marketingDescriptionTextArea);
		marketingDescriptionTextArea.addStyleName("textbox");

		dailyVisitFlexTable.setWidget(13, 0, marketingBudgetItemsLabel);
		marketingBudgetItemsLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(13, 1, marketingBudgetItemsTextBox);
		marketingBudgetItemsTextBox.addStyleName("textbox");

		dailyVisitFlexTable.setWidget(14, 0, amountLabel);
		amountLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(14, 1, amountIntegerBox);
		amountIntegerBox.addStyleName("textbox");
		amountIntegerBox.setMaxLength(8);
		amountIntegerBox.setText("0");

		dailyVisitFlexTable.setWidget(15, 0, institutionalMarketingItemsLabel);
		institutionalMarketingItemsLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(15, 1, institutionalMarketingItemsListBox);
		institutionalMarketingItemsListBox.setName("MARKETING_ITEMS");

		dailyVisitFlexTable.setWidget(16, 0, countLabel);
		countLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(16, 1, countIntegerBox);
		countIntegerBox.addStyleName("textbox");

		dailyVisitFlexTable.setWidget(16, 2, addMoreButton);

		dailyVisitFlexTable.setWidget(17, 0, gpPotentialLabel);
		gpPotentialLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(17, 1, gpPotentialListBox);
		gpPotentialListBox.setName("GP_POTENTIAL_RATING");

		dailyVisitFlexTable.setWidget(18, 0, commentsLabel);
		commentsLabel.addStyleName("text");

		dailyVisitFlexTable.setWidget(18, 1, commentsTextArea);
		commentsTextArea.addStyleName("textbox");

		dailyVisitFlexTable.setWidget(19, 1, submitButton);
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
		
		validateLocationIdAnchor.addClickHandler(this);
		addMoreButton.addClickHandler(this);
		submitButton.addClickHandler(this);
		mainMenuHyperlink.addClickHandler(this);
		logoutHyperlink.addClickHandler(this);
		marketingYesRadioButton.addValueChangeHandler(this);
		marketingNoRadioButton.addValueChangeHandler(this);
		
		
		TBR3Client.refresh(dailyVisitFlexTable);

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
			logger.log(Level.INFO, "go to main menu");
			mainMenu = new MainMenuComposite();
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(mainMenu);
			
		}
		else if(sender == validateLocationIdAnchor)
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
		else if(sender == addMoreButton)
		{
			boolean ifExists = false;
			
			if(TBR3Client.get(countIntegerBox).equals(""))
			{
				Window.alert("Count must not be empty.");
			}
			else if(mainItemsCount.size() !=0)
			{
				for(String[] arr : mainItemsCount)
				{
					if(arr[0].equals(TBR3Client.get(institutionalMarketingItemsListBox)))
					{
						Window.alert("Please select a different institutional marketing item.");
						itemsList.clear();
						ifExists = true;
						return;
					}
				}
				if(!ifExists)
				{
					itemsList.clear();
					itemsList.add(TBR3Client.get(institutionalMarketingItemsListBox));
					itemsList.add(TBR3Client.get(countIntegerBox));
					singleItemWithCount = itemsList.toArray(new String[itemsList.size()]);
					mainItemsCount.add(singleItemWithCount);
					Window.alert("Item inserted succesfully.");
				}
			}
			else
			{
				itemsList.clear();
				itemsList.add(TBR3Client.get(institutionalMarketingItemsListBox));
				itemsList.add(TBR3Client.get(countIntegerBox));
				singleItemWithCount = itemsList.toArray(new String[itemsList.size()]);
				mainItemsCount.add(singleItemWithCount);
				Window.alert("Item inserted succesfully.");
			}
			
			for(String[] arr : mainItemsCount)
			{
				System.out.println("Item is " + arr[0] + " and it's count is " + arr[1]);
			}
				
		}
		else if (sender == logoutHyperlink)
		{
			Tbr3fieldmonitoring.logout();
		}
		
	}

	@Override
	public void clearUp()
	{
		TBR3Client.clearControls(dailyVisitFlexTable);
		IntegerBox[] integerBoxes = { locationIdIntegerBox, amountIntegerBox, countIntegerBox};

		ListBox[] listBoxes = { townListBox, gpPotentialListBox };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("0");
		for (int i = 0; i < listBoxes.length; i++)
			listBoxes[i].setSelectedIndex(0);
		locationIdIntegerBox.setText("000000");
		formDateBox.getTextBox().setText("");
		visitDateBox.getTextBox().setText("");
		arrivalHourMinutePicker.clear();
		departureHourMinutePicker.clear();
		metGpYesRadioButton.setChecked(true);
		givenCouponsYesRadioButton.setChecked(true);
		marketingYesRadioButton.setChecked(true);
	}

	@Override
	public boolean validate()
	{
		valid = true;
		StringBuilder errorMessage = new StringBuilder();
		if(TBR3Client.get(locationIdIntegerBox).equals(""))
			errorMessage.append("Location ID: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
		if(TBR3Client.get(locationNameTextBox).equals(""))
			errorMessage.append("Location name: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
		if(TBR3Client.get(visitDateBox).equals(""))
			errorMessage.append("Visit Date: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
		if (arrivalHourMinutePicker.getHour() == -1)
			errorMessage.append("Arrival time: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if (departureHourMinutePicker.getHour() == -1)
			errorMessage.append("Departure time: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if (!metGpYesRadioButton.isChecked() && !metGpNoRadioButton.isChecked())
			errorMessage.append("Met GP: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if (!givenCouponsYesRadioButton.isChecked() && !givenCouponsNoRadioButton.isChecked())
			errorMessage.append("Given Coupons/Rx Pads: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if (!marketingYesRadioButton.isChecked() && !marketingNoRadioButton.isChecked())
			errorMessage.append("Marketing Activity: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if(TBR3Client.get(marketingDescriptionTextArea).equals(""))
			errorMessage.append("Marketing Activity Description: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		
		if(!marketingNoRadioButton.isChecked())
		{
			if(TBR3Client.get(marketingBudgetItemsTextBox).equals(""))
				errorMessage.append("Marketing Budget Items: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
			if(TBR3Client.get(amountIntegerBox).equals(""))
				errorMessage.append("Amount: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		}
		
		if(TBR3Client.get(countIntegerBox).equals(""))
			errorMessage.append("Count: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		if(TBR3Client.get(commentsTextArea).equals(""))
			errorMessage.append("Comments: " + CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR) + "\n");
		
		valid = errorMessage.length() == 0;
		if (!valid)
		{
			Window.alert(errorMessage.toString());
		}
		return valid;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveData()
	{
		if(validate())
		{
			final Date enteredDate = new Date();
			final int eID = 0;
			final String creator = TBR3.getCurrentUserName();
			service.getUserRoles(TBR3.getCurrentUserName(), TBR3.getPassword(), new AsyncCallback<String[]>()
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
							EncounterId encounterId = new EncounterId(eID, creator, creator, formName);
							
							Encounter encounter = new Encounter(encounterId, TBR3Client.get(locationIdIntegerBox));
							encounter.setLocationId(TBR3Client.get(locationIdIntegerBox));
							encounter.setDateEntered(enteredDate);
							encounter.setDateStart(new Date());
							encounter.setDateEnd(new Date());
							ArrayList<EncounterResults> encounterResults = new ArrayList<EncounterResults>();
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "F_DATE"), DateTimeUtil.getFormattedDate(enteredDate, "yyyy-MM-dd HH:mm:ss")));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "LOCATION_ID"), TBR3Client.get(locationIdIntegerBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator,	formName, "LOCATION_NAME"),	TBR3Client.get(locationNameTextBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "TOWN"), TBR3Client.get(townListBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "VISIT_DATE"), TBR3Client.get(visitDateBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "ARRIVAL_TIME"), FirstVisitComposite.getTimeString(arrivalHourMinutePicker.getMinute(), arrivalHourMinutePicker.getHour())));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "DEPARTURE_TIME"), FirstVisitComposite.getTimeString(departureHourMinutePicker.getMinute(), departureHourMinutePicker.getHour())));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MET_GP"), metGpYesRadioButton.isChecked() ? "Yes" : "No"));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "GIVEN_COUPONS"), givenCouponsYesRadioButton.isChecked() ? "Yes" : "No"));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ACTIVITY"), marketingYesRadioButton.isChecked() ? "Yes" : "No"));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ACTIVITY_DESCRIPTION"), TBR3Client.get(marketingDescriptionTextArea)));
							
							if(marketingYesRadioButton.isChecked())
							{
								encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_BUDGET_ITEMS"), TBR3Client.get(marketingBudgetItemsTextBox)));
								encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "AMOUNT"), TBR3Client.get(amountIntegerBox)));
							}
							
							if(mainItemsCount.size() == 0)
							{
								if(TBR3Client.get(institutionalMarketingItemsListBox).equals("POSTERS"))
								{
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ITEMS_POSTERS"), "POSTERS"));
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COUNT_POSTERS"), TBR3Client.get(countIntegerBox)));
								}
								else if(TBR3Client.get(institutionalMarketingItemsListBox).equals("BROCHURES"))
								{
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ITEMS_BROCHURES"), "BROCHURES"));
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COUNT_BROCHURES"), TBR3Client.get(countIntegerBox)));
								}
								else if(TBR3Client.get(institutionalMarketingItemsListBox).equals("RX PADS"))
								{
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ITEMS_PADS"), "RX PADS"));
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COUNT_PADS"), TBR3Client.get(countIntegerBox)));
								}
								else if(TBR3Client.get(institutionalMarketingItemsListBox).equals("OTHERS"))
								{
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ITEMS_OTHERS"), "OTHERS"));
									encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COUNT_OTHERS"), TBR3Client.get(countIntegerBox)));
								}
							}
							else
							{
								for(String[] arr : mainItemsCount)
								{
									if(arr[0].equals("POSTERS"))
									{
										encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ITEMS_POSTERS"), arr[0]));
										encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COUNT_POSTERS"), arr[1]));
									}
									else if(arr[0].equals("BROCHURES"))
									{
										encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ITEMS_BROCHURES"), arr[0]));
										encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COUNT_BROCHURES"), arr[1]));
									}
									else if(arr[0].equals("RX PADS"))
									{
										encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ITEMS_PADS"), arr[0]));
										encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COUNT_PADS"), arr[1]));
									}
									else if(arr[0].equals("OTHERS"))
									{
										encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "MARKETING_ITEMS_OTHERS"), arr[0]));
										encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COUNT_OTHERS"), arr[1]));
									}
								}
							}
							
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "GP_POTENTIAL"), TBR3Client.get(gpPotentialListBox)));
							encounterResults.add(new EncounterResults(new EncounterResultsId(eID, creator, creator, formName, "COMMENTS"), TBR3Client.get(commentsTextArea)));
							
							try
							{
								service.saveFormData(encounter,encounterResults.toArray(new EncounterResults[] {}),	new AsyncCallback<String>()
										{

											@Override
											public void onFailure(Throwable caught)
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

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event)
	{
		Widget sender = (Widget) event.getSource();
		if(sender == marketingYesRadioButton)
		{
			boolean marketing = marketingYesRadioButton.getValue();
			marketingBudgetItemsTextBox.setEnabled(marketing);
			amountIntegerBox.setEnabled(marketing);
		}
		else if(sender == marketingNoRadioButton)
		{
			boolean marketing = marketingYesRadioButton.getValue();
			marketingBudgetItemsTextBox.setEnabled(marketing);
			amountIntegerBox.setEnabled(marketing);
		}
		
	}

}
