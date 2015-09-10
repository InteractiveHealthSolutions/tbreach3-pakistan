/**
 * Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 * Contributors: Owais Hussain
 */
package com.ihsinformatics.tbr3reporterweb.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.ihsinformatics.tbr3reporterweb.shared.DataType;
import com.ihsinformatics.tbr3reporterweb.shared.Parameter;
import com.ihsinformatics.tbr3reporterweb.shared.TBR3;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class ReportsComposite extends Composite
		implements
			IReport,
			ClickHandler,
			ChangeHandler,
			ValueChangeHandler<Boolean> {
	private static ServerServiceAsync service = GWT.create(ServerService.class);
	private static LoadingWidget loading = new LoadingWidget();
	private static final String menuName = "DATALOG";
	private String filter = "";
	private String startDate = "";
	private String endDate = "";
	private String laboratoryId = "";
	private String operatorId = "";

	private FlexTable flexTable = new FlexTable();
	private FlexTable topFlexTable = new FlexTable();
	private FlexTable rightFlexTable = new FlexTable();
	private FlexTable filterFlexTable = new FlexTable();
	private Grid grid = new Grid(1, 4);

	private Button viewButton = new Button("Save");
	private Button closeButton = new Button("Close");
	private Button exportButton = new Button("Export");

	private Label lblSelectCategory = new Label("Select Category:");
	private Label lblSnapshot = new Label("Snapshot:");
	private Label snapshotLabel = new Label();
	private Label lblCaution = new Label(
			"Some reports may take 5 to 10 minutes to generate. Please wait until report download window appears.");
	private Label lblXpertSmsLog = new Label("XpertSMS Reports");
	private Label lblSelectReport = new Label("Select Report:");
	private Label lblFilter = new Label("Filter (Check all that apply):");

	private TextBox laboratoryIdTextBox = new TextBox();
	private TextBox operatorIdTextBox = new TextBox();

	private ListBox categoryComboBox = new ListBox();
	private ListBox reportsListComboBox = new ListBox();
	private ListBox laboratoryFilterTypeComboBox = new ListBox();
	private ListBox operatorFilterTypeComboBox = new ListBox();
	private ListBox sortTypeComboBox = new ListBox();

	private DateBox fromDateBox = new DateBox();
	private DateBox toDateBox = new DateBox();
	private DateBox fromTimeDateBox = new DateBox();
	private DateBox toTimeDateBox = new DateBox();

	private CheckBox dateRangeFilterCheckBox = new CheckBox("Date Range:");
	private CheckBox timeRangeFilterCheckBox = new CheckBox("Time Range:");
	private CheckBox laboratoryIdCheckBox = new CheckBox("Laboratory ID:");
	private CheckBox operatorIdCheckBox = new CheckBox("Operator ID:");
	private CheckBox sortCheckBox = new CheckBox("Sort By:");

	@SuppressWarnings("deprecation")
	public ReportsComposite() {
		initWidget(flexTable);
		flexTable.setSize("100%", "100%");
		flexTable.setWidget(0, 0, topFlexTable);
		lblXpertSmsLog.setStyleName("title");
		topFlexTable.setWidget(0, 0, lblXpertSmsLog);
		topFlexTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		topFlexTable.getRowFormatter().setVerticalAlign(0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		topFlexTable.getCellFormatter().setVerticalAlignment(0, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.setWidget(1, 0, rightFlexTable);
		rightFlexTable.setSize("100%", "100%");
		rightFlexTable.setWidget(0, 0, lblSnapshot);
		rightFlexTable.setWidget(0, 1, snapshotLabel);
		rightFlexTable.setWidget(1, 0, lblSelectCategory);
		categoryComboBox.addItem("-- Select Category --");
		categoryComboBox.addItem("GeneXpert Reports");
		categoryComboBox.addItem("Data Dumps");
		rightFlexTable.setWidget(1, 1, categoryComboBox);
		categoryComboBox.setWidth("100%");
		rightFlexTable.setWidget(2, 0, lblSelectReport);
		rightFlexTable.setWidget(2, 1, reportsListComboBox);
		reportsListComboBox.setWidth("100%");
		reportsListComboBox.addChangeHandler(this);
		lblCaution.setStyleName("gwt-MenuItem-selected");
		rightFlexTable.setWidget(3, 1, lblCaution);
		lblCaution.setWidth("100%");
		rightFlexTable.setWidget(4, 0, lblFilter);
		rightFlexTable.setWidget(4, 1, filterFlexTable);
		filterFlexTable.setWidth("100%");
		filterFlexTable.setWidget(0, 0, dateRangeFilterCheckBox);
		dateRangeFilterCheckBox.setVisible(true);
		timeRangeFilterCheckBox.setVisible(true);
		dateRangeFilterCheckBox.setWidth("100%");
		fromDateBox.setEnabled(false);
		filterFlexTable.setWidget(0, 1, fromDateBox);
		fromDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("dd-MM-yyyy")));
		fromDateBox.setWidth("100%");
		toDateBox.setEnabled(false);
		filterFlexTable.setWidget(0, 2, toDateBox);
		toDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("dd-MM-yyyy")));
		toDateBox.setWidth("100%");
		filterFlexTable.getCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		filterFlexTable.getCellFormatter().setHorizontalAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_CENTER);
		filterFlexTable.setWidget(1, 0, timeRangeFilterCheckBox);
		fromTimeDateBox.setEnabled(false);
		fromTimeDateBox.setValue(new Date(1297969200000L));
		fromTimeDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getShortTimeFormat()));
		filterFlexTable.setWidget(1, 1, fromTimeDateBox);
		fromTimeDateBox.setWidth("50%");
		toTimeDateBox.setEnabled(false);
		toTimeDateBox.setValue(new Date(1298012400000L));
		toTimeDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getShortTimeFormat()));
		filterFlexTable.setWidget(1, 2, toTimeDateBox);
		toTimeDateBox.setWidth("50%");
		filterFlexTable.setWidget(2, 0, laboratoryIdCheckBox);
		laboratoryFilterTypeComboBox.setEnabled(false);
		filterFlexTable.setWidget(2, 1, laboratoryFilterTypeComboBox);
		laboratoryFilterTypeComboBox.setWidth("100%");
		laboratoryIdTextBox.setVisibleLength(20);
		laboratoryIdTextBox.setMaxLength(20);
		laboratoryIdTextBox.setEnabled(false);
		filterFlexTable.setWidget(2, 2, laboratoryIdTextBox);
		laboratoryIdTextBox.setWidth("100%");
		filterFlexTable.setWidget(3, 0, operatorIdCheckBox);
		operatorFilterTypeComboBox.setEnabled(false);
		filterFlexTable.setWidget(3, 1, operatorFilterTypeComboBox);
		operatorFilterTypeComboBox.setWidth("100%");
		operatorIdTextBox.setEnabled(false);
		operatorIdTextBox.setVisibleLength(20);
		operatorIdTextBox.setMaxLength(20);
		filterFlexTable.setWidget(3, 2, operatorIdTextBox);
		operatorIdTextBox.setWidth("100%");
		filterFlexTable.setWidget(4, 0, sortCheckBox);
		sortTypeComboBox.setEnabled(false);
		filterFlexTable.setWidget(4, 1, sortTypeComboBox);
		sortTypeComboBox.setWidth("100%");
		rightFlexTable.setWidget(5, 1, grid);
		grid.setSize("100%", "100%");
		viewButton.setEnabled(false);
		viewButton.setText("View");
		grid.setWidget(0, 0, viewButton);
		exportButton.setEnabled(false);
		grid.setWidget(0, 1, exportButton);
		grid.setWidget(0, 3, closeButton);
		rightFlexTable.getCellFormatter().setHorizontalAlignment(4, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getRowFormatter().setVerticalAlign(1,
				HasVerticalAlignment.ALIGN_TOP);
		rightFlexTable.getRowFormatter().setVerticalAlign(4,
				HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

		dateRangeFilterCheckBox.addValueChangeHandler(this);
		timeRangeFilterCheckBox.addValueChangeHandler(this);
		laboratoryIdCheckBox.addValueChangeHandler(this);
		operatorIdCheckBox.addValueChangeHandler(this);
		sortCheckBox.addValueChangeHandler(this);
		categoryComboBox.addChangeHandler(this);
		viewButton.addClickHandler(this);
		exportButton.addClickHandler(this);
		closeButton.addClickHandler(this);

		refreshList();
		setRights(menuName);
	}

	private void refreshList() {
		String[] filterOptions = {"IS EXACTLY", "STARTS WITH", "ENDS ON",
				"LOOKS LIKE"};
		for (String s : filterOptions) {
			laboratoryFilterTypeComboBox.addItem(s);
			operatorFilterTypeComboBox.addItem(s);
		}
		sortTypeComboBox.addItem("PatientID");
		sortTypeComboBox.addItem("SampleID");
		sortTypeComboBox.addItem("LaboratoryID");
		sortTypeComboBox.addItem("DateTested");
	}

	/**
	 * Display/Hide main panel and loading widget
	 * 
	 * @param status
	 */
	public void load(boolean status) {
		flexTable.setVisible(!status);
		if (status)
			loading.show();
		else
			loading.hide();
	}

	/**
	 * Creates appropriate filter for given column names
	 * 
	 * @param laboratoryColumnName
	 * @param dateColumnName
	 * @param gpColumnName
	 * @param monitorColumnName
	 * @param chwColumnName
	 */
	@SuppressWarnings("deprecation")
	private String filterData(String laboratoryColumnName,
			String dateColumnName, String operatorColumnName) {
		filter = "";
		startDate = "";
		endDate = "";
		laboratoryId = "";
		operatorId = "";

		if (dateRangeFilterCheckBox.getValue()) {
			Date start = new Date(fromDateBox.getValue().getTime());
			Date end = new Date(toDateBox.getValue().getTime());
			StringBuilder startString = new StringBuilder();
			StringBuilder endString = new StringBuilder();
			if (timeRangeFilterCheckBox.getValue()) {
				start.setHours(fromTimeDateBox.getValue().getHours());
				start.setMinutes(fromTimeDateBox.getValue().getMinutes());
				end.setHours(toTimeDateBox.getValue().getHours());
				end.setMinutes(toTimeDateBox.getValue().getMinutes());
			}
			startString.append((start.getYear() + 1900) + "-"
					+ (start.getMonth() + 1) + "-" + start.getDate() + " "
					+ start.getHours() + ":" + start.getMinutes() + ":00");
			endString.append((end.getYear() + 1900) + "-"
					+ (end.getMonth() + 1) + "-" + end.getDate() + " "
					+ end.getHours() + ":" + end.getMinutes() + ":00");
			startDate = startString.toString();
			endDate = endString.toString();
		}
		if (laboratoryIdCheckBox.getValue()) {
			switch (laboratoryFilterTypeComboBox.getSelectedIndex()) {
				case 0 :
					laboratoryId = " = '"
							+ TBR3ReporterClient.get(laboratoryIdTextBox) + "'";
					break;
				case 1 :
					laboratoryId = " LIKE '"
							+ TBR3ReporterClient.get(laboratoryIdTextBox)
							+ "%'";
					break;
				case 2 :
					laboratoryId = " LIKE '%"
							+ TBR3ReporterClient.get(laboratoryIdTextBox) + "'";
					break;
				case 3 :
					laboratoryId = " LIKE '%"
							+ TBR3ReporterClient.get(laboratoryIdTextBox)
							+ "%'";
					break;
			}
		}
		if (operatorIdCheckBox.getValue()) {
			switch (operatorFilterTypeComboBox.getSelectedIndex()) {
				case 0 :
					operatorId = " = '"
							+ TBR3ReporterClient.get(operatorIdTextBox) + "'";
					break;
				case 1 :
					operatorId = " LIKE '"
							+ TBR3ReporterClient.get(operatorIdTextBox) + "%'";
					break;
				case 2 :
					operatorId = " LIKE '%"
							+ TBR3ReporterClient.get(operatorIdTextBox) + "'";
					break;
				case 3 :
					operatorId = " LIKE '%"
							+ TBR3ReporterClient.get(operatorIdTextBox) + "%'";
					break;
			}
		}
		if (dateRangeFilterCheckBox.getValue() && !dateColumnName.equals(""))
			filter += " AND " + dateColumnName + " BETWEEN '" + startDate
					+ "' AND '" + endDate + "'";
		if (laboratoryIdCheckBox.getValue() && !laboratoryColumnName.equals(""))
			filter += " AND " + laboratoryColumnName + laboratoryId;
		if (operatorIdCheckBox.getValue() && !operatorColumnName.equals(""))
			filter += " AND " + operatorColumnName + operatorId;
		return filter;
	}

	public String sortData() {
		if (!sortCheckBox.getValue()) {
			return "";
		}
		String columnName = TBR3ReporterClient.get(sortTypeComboBox);
		return " ORDER BY " + columnName;
	}

	@Override
	public void clearUp() {
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public void viewData(final boolean export) {
		String reportSelected = TBR3ReporterClient.get(reportsListComboBox)
				.replace(" ", "");
		String query = "";
		// Case Detection Reports
		if (TBR3ReporterClient.get(categoryComboBox)
				.equals("GeneXpert Reports")) {
			if (reportSelected.equals("GeneXpertAllTestReport")) {
				query = "SELECT G.TestID, G.PatientID, G.SputumTestID AS SampleID, G.LaboratoryID, G.OperatorID, G.DateTested, G.DrugResistance, G.GeneXpertResult, G.MTBBurden, G.ErrorCode, G.InstrumentID, G.ModuleID, G.ReagentLotID, G.PcID, G.CartridgeID, G.CartridgeExpiryDate, G.Remarks, G.ProbeResultA, G.ProbeResultB, G.ProbeResultC, G.ProbeResultD, G.ProbeResultE, G.ProbeResultSPC, G.ProbeCtA, G.ProbeCtB, G.ProbeCtC, G.ProbeCtD, G.ProbeCtE, G.ProbeCtSPC, G.ProbeEndptA, G.ProbeEndptB, G.ProbeEndptC, G.ProbeEndptD, G.ProbeEndptE, G.ProbeEndptSPC FROM GeneXpertResults AS G WHERE 1 = 1 "
						+ filterData("G.LaboratoryID", "G.DateTested",
								"G.OperatorID") + sortData();
			} else if (reportSelected.equals("GeneXpertErrorReport")) {
				query = "SELECT G.TestID, G.PatientID, G.SputumTestID AS SampleID, G.LaboratoryID, G.DateTested, G.ErrorCode, G.Remarks, G.InstrumentID, G.ModuleID, G.CartridgeID, G.ReagentLotID  FROM GeneXpertResults G WHERE IFNULL(G.ErrorCode, 0) <> 0 "
						+ filterData("G.LaboratoryID", "G.DateTested",
								"G.OperatorID") + sortData();
			} else if (reportSelected.equals("GeneXpertFailedTestReport")) {
				query = "SELECT G.TestID, G.PatientID, G.SputumTestID AS SampleID, G.LaboratoryID, G.DateTested, G.OperatorID, G.GeneXpertResult, G.DrugResistance, G.ErrorCode, G.InstrumentID, G.ModuleID, G.CartridgeID, G.ReagentLotID FROM GeneXpertResults AS G WHERE GeneXpertResult in ('ERROR', 'INVALID', 'NO RESULT') "
						+ filterData("G.LaboratoryID", "G.DateTested",
								"G.OperatorID") + sortData();
			} else if (reportSelected.equals("GeneXpertPositiveReport")) {
				query = "SELECT G.TestID, G.PatientID, G.SputumTestID AS SampleID, G.LaboratoryID, G.MTBBurden, G.DrugResistance, G.DateTested, Remarks FROM GeneXpertResults G WHERE G.GeneXpertResult = 'MTB DETECTED' "
						+ filterData("G.LaboratoryID", "G.DateTested",
								"G.OperatorID") + sortData();
			} else if (reportSelected.equals("GeneXpertProbeReport")) {
				query = "SELECT G.TestID, G.PatientID, G.SputumTestID AS SampleID, G.LaboratoryID AS LabID, G.DateTested, G.GeneXpertResult AS Result, G.MTBBurden, G.DrugResistance AS Resistance, G.ProbeResultA AS A, G.ProbeCtA AS CtA, G.ProbeEndptA AS EndPtA, G.ProbeResultB AS B, G.ProbeCtB AS CtB, G.ProbeEndptB AS EndPtB, G.ProbeResultC AS C, G.ProbeCtC AS CtC, G.ProbeEndptC AS EndPtC, G.ProbeResultD AS D, G.ProbeCtD AS CtD, G.ProbeEndptD AS EndPtD, G.ProbeResultE AS E, G.ProbeCtE AS CtE, G.ProbeEndptE AS EndPtE, G.ProbeResultSPC AS SPC, G.ProbeCtSPC AS CtSPC, G.ProbeEndptSPC AS EndPtSPC FROM GeneXpertResults G WHERE 1 = 1 "
						+ filterData("G.LaboratoryID", "G.DateTested",
								"G.OperatorID") + sortData();
			} else if (reportSelected.equals("GeneXpertRifResistantReport")) {
				query = "SELECT G.TestID, G.PatientID, G.SputumTestID AS SampleID, G.LaboratoryID, G.MTBBurden, G.DateTested  FROM GeneXpertResults G WHERE DrugResistance='DETECTED' "
						+ filterData("G.LaboratoryID", "G.DateTested",
								"G.OperatorID") + sortData();
			} else {
				query = "";
			}
		} else if (TBR3ReporterClient.get(categoryComboBox)
				.equals("Data Dumps")) {
			if (reportSelected.equals("GeneXpertResultsDump")) {
				query = "SELECT TestID, PatientID, SputumTestID, LaboratoryID, DateSubmitted, DateTested, GeneXpertResult, IsPositive, MTBBurden, DrugResistance, ErrorCode, Remarks, PcID, InstrumentID, ModuleID, CartridgeID, ReagentLotID, OperatorID, ProbeResultA, ProbeResultB, ProbeResultC, ProbeResultD, ProbeResultE, ProbeResultSPC, ProbeCtA, ProbeCtB, ProbeCtC, ProbeCtD, ProbeCtE, ProbeCtSPC, ProbeEndptA, ProbeEndptB, ProbeEndptC, ProbeEndptD, ProbeEndptE, ProbeEndptSPC FROM GeneXpertResults ORDER BY TestID";
			} else {
				query = "";
			}
		}
		load(true);
		if (query.equals("")) {
			try {
				Window.alert("Report is either out of format or does not match the schema. Please report to the developers.");
			} catch (Exception e) {
				load(false);
				e.printStackTrace();
			}
		} else {
			if (TBR3ReporterClient.get(categoryComboBox).equals("Data Dumps")) {
				try {
					service.generateCSVfromQuery(query,
							new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {
									Window.open(result, "_blank", "");
									load(false);
								}

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
									load(false);
								}
							});
				} catch (Exception e) {
					load(false);
					e.printStackTrace();
				}
			} else {
				try {
					Parameter[] params = new Parameter[]{new Parameter(
							"UserName", TBR3.getCurrentUser(), DataType.STRING)};
					service.generateReportFromQuery(reportSelected, query,
							params, export, new AsyncCallback<String>() {
								@Override
								public void onSuccess(String result) {

									String url = Window.Location.getHref()
											+ "/" + result;
									Window.open(url, "_blank", "");
									load(false);
								}

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
									load(false);
								}
							});
				} catch (Exception e) {
					load(false);
					e.printStackTrace();
				}
			}
		}
	}

	public void setRights(String menuName) {
		// Not implemented
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		load(true);
		if (sender == viewButton) {
			viewData(false);
		} else if (sender == exportButton) {
			viewData(true);
		} else if (sender == closeButton) {
			// MainMenuComposite.clear();
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		Widget sender = (Widget) event.getSource();
		// Fill report names
		if (sender == categoryComboBox) {
			String text = TBR3ReporterClient.get(sender);
			reportsListComboBox.clear();
			if (text.equals("GeneXpert Reports")) {
				reportsListComboBox.addItem("GeneXpert All Test Report");
				reportsListComboBox.addItem("GeneXpert Error Report");
				reportsListComboBox.addItem("GeneXpert Failed Test Report");
				reportsListComboBox.addItem("GeneXpert Positive Report");
				reportsListComboBox.addItem("GeneXpert Probe Report");
				reportsListComboBox.addItem("GeneXpert Rif Resistant Report");
			} else if (text.equals("Data Dumps")) {
				reportsListComboBox.addItem("GeneXpert Results Dump");
			}
			// Disable view on data dumps
			viewButton.setEnabled(!TBR3ReporterClient.get(categoryComboBox)
					.equals("Data Dumps"));
		}
	}

	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		Widget sender = (Widget) event.getSource();
		if (sender == dateRangeFilterCheckBox) {
			fromDateBox.setEnabled(dateRangeFilterCheckBox.getValue());
			toDateBox.setEnabled(dateRangeFilterCheckBox.getValue());
		} else if (sender == timeRangeFilterCheckBox) {
			fromTimeDateBox.setEnabled(timeRangeFilterCheckBox.getValue());
			toTimeDateBox.setEnabled(timeRangeFilterCheckBox.getValue());
		} else if (sender == laboratoryIdCheckBox) {
			laboratoryFilterTypeComboBox.setEnabled(laboratoryIdCheckBox
					.getValue());
			laboratoryIdTextBox.setEnabled(laboratoryIdCheckBox.getValue());
		} else if (sender == operatorIdCheckBox) {
			operatorFilterTypeComboBox
					.setEnabled(operatorIdCheckBox.getValue());
			operatorIdTextBox.setEnabled(operatorIdCheckBox.getValue());
		} else if (sender == sortCheckBox) {
			sortTypeComboBox.setEnabled(sortCheckBox.getValue());
		}
	}
}
