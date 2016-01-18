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

import java.util.ArrayList;
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
import com.ihsinformatics.tbr3reporterweb.shared.Report;
import com.ihsinformatics.tbr3reporterweb.shared.TBR3;

/**
 * @author owais.hussain@ihsinformatics.com
 */
public class ReportsComposite extends Composite implements IReport,
		ClickHandler, ChangeHandler, ValueChangeHandler<Boolean> {
	private static ServerServiceAsync service = GWT.create(ServerService.class);
	private static LoadingWidget loading = new LoadingWidget();
	private static final String menuName = "DATALOG";
	private static Report[] reports;
	private String locationId = "";
	private String userId = "";

	private FlexTable flexTable = new FlexTable();
	private FlexTable topFlexTable = new FlexTable();
	private FlexTable rightFlexTable = new FlexTable();
	private FlexTable filterFlexTable = new FlexTable();
	private Grid grid = new Grid(1, 4);

	private Button viewButton = new Button("Save");
	private Button exportButton = new Button("Export");

	private Label lblSelectCategory = new Label("Select Category:");
	private Label lblCaution = new Label(
			"Some reports may take up to 5 minutes to generate. Please wait until report download window appears.");
	private Label lblReportsTitle = new Label("Sehatmand Zindagi Reports v1.0");
	private Label lblSelectReport = new Label("Select Report:");
	private Label lblFilter = new Label("Filter (Check all that apply):");

	private TextBox locationIdTextBox = new TextBox();
	private TextBox userIdTextBox = new TextBox();

	private ListBox categoryComboBox = new ListBox();
	private ListBox reportsListComboBox = new ListBox();
	private ListBox locationFilterTypeComboBox = new ListBox();
	private ListBox userFilterTypeComboBox = new ListBox();
	private ListBox sortTypeComboBox = new ListBox();

	private DateBox fromDateBox = new DateBox();
	private DateBox toDateBox = new DateBox();
	private DateBox fromTimeDateBox = new DateBox();
	private DateBox toTimeDateBox = new DateBox();

	private CheckBox dateRangeFilterCheckBox = new CheckBox("Date Range:");
	private CheckBox timeRangeFilterCheckBox = new CheckBox("Time Range:");
	private CheckBox locationIdCheckBox = new CheckBox("Location ID:");
	private CheckBox userIdCheckBox = new CheckBox("User ID:");
	private CheckBox sortCheckBox = new CheckBox("Sort By:");

	@SuppressWarnings("deprecation")
	public ReportsComposite() {
		initWidget(flexTable);
		flexTable.setSize("100%", "100%");
		flexTable.setWidget(0, 0, topFlexTable);
		lblReportsTitle.setStyleName("title");
		topFlexTable.setWidget(0, 0, lblReportsTitle);
		topFlexTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_CENTER);
		topFlexTable.getRowFormatter().setVerticalAlign(0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		topFlexTable.getCellFormatter().setVerticalAlignment(0, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.setWidget(1, 0, rightFlexTable);
		rightFlexTable.setSize("100%", "100%");
		rightFlexTable.setWidget(0, 0, lblSelectCategory);
		categoryComboBox.addItem("-- Select Category --");
		categoryComboBox.addItem("Reports");
		categoryComboBox.addItem("Data Dumps");
		rightFlexTable.setWidget(0, 1, categoryComboBox);
		categoryComboBox.setWidth("100%");
		rightFlexTable.setWidget(1, 0, lblSelectReport);
		rightFlexTable.setWidget(1, 1, reportsListComboBox);
		reportsListComboBox.setWidth("100%");
		reportsListComboBox.addChangeHandler(this);
		lblCaution.setStyleName("gwt-MenuItem-selected");
		rightFlexTable.setWidget(2, 1, lblCaution);
		lblCaution.setWidth("100%");
		rightFlexTable.setWidget(3, 0, lblFilter);
		rightFlexTable.setWidget(3, 1, filterFlexTable);
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
		filterFlexTable.setWidget(2, 0, locationIdCheckBox);
		locationFilterTypeComboBox.setEnabled(false);
		filterFlexTable.setWidget(2, 1, locationFilterTypeComboBox);
		locationFilterTypeComboBox.setWidth("100%");
		locationIdTextBox.setVisibleLength(20);
		locationIdTextBox.setMaxLength(20);
		locationIdTextBox.setEnabled(false);
		filterFlexTable.setWidget(2, 2, locationIdTextBox);
		locationIdTextBox.setWidth("100%");
		filterFlexTable.setWidget(3, 0, userIdCheckBox);
		userFilterTypeComboBox.setEnabled(false);
		filterFlexTable.setWidget(3, 1, userFilterTypeComboBox);
		userFilterTypeComboBox.setWidth("100%");
		userIdTextBox.setEnabled(false);
		userIdTextBox.setVisibleLength(20);
		userIdTextBox.setMaxLength(20);
		filterFlexTable.setWidget(3, 2, userIdTextBox);
		userIdTextBox.setWidth("100%");
		rightFlexTable.setWidget(5, 1, grid);
		grid.setSize("100%", "100%");
		viewButton.setEnabled(false);
		viewButton.setText("View");
		grid.setWidget(0, 0, viewButton);
		grid.setWidget(0, 1, exportButton);
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
		locationIdCheckBox.addValueChangeHandler(this);
		userIdCheckBox.addValueChangeHandler(this);
		sortCheckBox.addValueChangeHandler(this);
		categoryComboBox.addChangeHandler(this);
		viewButton.addClickHandler(this);
		exportButton.addClickHandler(this);

		refreshList();
		setRights(menuName);
	}

	private void refreshList() {
		String[] filterOptions = { "LOOKS LIKE" };
		for (String s : filterOptions) {
			locationFilterTypeComboBox.addItem(s);
			userFilterTypeComboBox.addItem(s);
		}
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
	 * @param locationColumnName
	 * @param dateColumnName
	 * @param userColumnName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private String getFilter(String locationColumnName, String dateColumnName,
			String userColumnName) {
		StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
		if (locationIdCheckBox.getValue() && locationColumnName != null) {
			where.append(" AND " + locationId + " LIKE '"
					+ TBR3ReporterClient.get(locationIdTextBox) + "%'");
		}
		if (userIdCheckBox.getValue() && userColumnName != null) {
			where.append(" AND " + userId + " LIKE '"
					+ TBR3ReporterClient.get(userIdTextBox) + "%'");
		}
		if (dateRangeFilterCheckBox.getValue() && !dateColumnName.equals("")
				&& dateColumnName != null) {
			Date fromDate = fromDateBox.getValue();
			Date toDate = toDateBox.getValue();
			String from = (fromDate.getYear() + 1900) + "-"
					+ (fromDate.getMonth() + 1) + "-" + (fromDate.getDate());
			String to = (toDate.getYear() + 1900) + "-"
					+ (toDate.getMonth() + 1) + "-" + (toDate.getDate());
			where.append(" AND " + dateColumnName + " BETWEEN '" + from
					+ "' AND '" + to + "'");
		}
		return where.toString();
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
		String reportSelected = TBR3ReporterClient.get(reportsListComboBox);
		String query = "";
		if (TBR3ReporterClient.get(categoryComboBox).equals("Reports")) {
			// Fetch query and parameter names from Report object
		} else if (TBR3ReporterClient.get(categoryComboBox)
				.equals("Data Dumps")) {
			if (reportSelected.equals("Patient Data")) {
				query = "select * from sz_dw.dim_patient"
						+ getFilter(null, "date_created", null);
			} else if (reportSelected.equals("Locations Data")) {
				query = "select * from sz_dw.dim_location"
						+ getFilter(null, "date_created", null);
			} else if (reportSelected.equals("Forms Data")) {
				query = "select * from sz_dw.dim_encounter"
						+ getFilter(null, "date_created", null);
			} else if (reportSelected.equals("Users Data")) {
				query = "select surrogate_id, system_id, user_id, username, person_id, identifier, creator, date_created, changed_by, date_changed, retired, retire_reason, uuid from sz_dw.dim_user"
						+ getFilter(null, "date_created", null);
			} else if (reportSelected.equals("Field Monitoring Camps")) {
				query = "select * from sz_dw.fm_enc_camp_info"
						+ getFilter("location_name", "date_entered", "pid1");
			} else if (reportSelected.equals("Field Monitoring Daily Visits")) {
				query = "select * from sz_dw.fm_enc_daily_vis"
						+ getFilter("location_name", "date_entered", "pid1");
			} else if (reportSelected.equals("Field Monitoring First Visits")) {
				query = "select * from sz_dw.fm_enc_first_vis"
						+ getFilter("location_name", "date_entered", "pid1");
			} else if (reportSelected
					.equals("Field Monitoring Supervisor Visits")) {
				query = "select * from sz_dw.fm_enc_super_vis"
						+ getFilter("location_name", "date_entered", "pid1");
			} else if (reportSelected.equals("OpenMRS Blood Sugar Order")) {
				query = "select * from om_enc_blood_sugar_order"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Blood Sugar Results")) {
				query = "select * from om_enc_blood_sugar_results"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Clinical Evaluation")) {
				query = "select * from om_enc_clinical_evaluation"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Client Information")) {
				query = "select * from om_enc_customer_information"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS CXR Order")) {
				query = "select * from om_enc_cxr_order"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS CXR Results")) {
				query = "select * from om_enc_cxr_result"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS CXR Test")) {
				query = "select * from om_enc_cxr_test"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected
					.equals("OpenMRS Diabetes First Encounter")) {
				query = "select * from om_enc_diabetes_first_encounter"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Diabetes Follow-up")) {
				query = "select * from om_enc_diabetes_follow_up"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Drug Dispersal")) {
				query = "select * from om_enc_drug_dispersal"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS GXP Order")) {
				query = "select * from om_enc_gxp_order"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS GXP Results")) {
				query = "select * from om_enc_gxp_result"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS GXP Test")) {
				query = "select * from om_enc_gxp_test"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS HbA1c Order")) {
				query = "select * from om_enc_hba1c_order"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Mental Health Screening")) {
				query = "select * from om_enc_mental_health_screening"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Non-Pulmonary Suspect")) {
				query = "select * from om_enc_non_pulmonary_suspect"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Paediatric Screening")) {
				query = "select * from om_enc_paediatric_screening"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Patient GPS")) {
				query = "select * from om_enc_patient_gps"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Screening")) {
				query = "select * from om_enc_screening"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Side Effects")) {
				query = "select * from om_enc_side_effects"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Smear Microscopy Order")) {
				query = "select * from om_enc_smear_order"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected
					.equals("OpenMRS Smear Microscopy Results")) {
				query = "select * from om_enc_smear_result"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Spirometry Order")) {
				query = "select * from om_enc_spirometry_order"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Spirometry Results")) {
				query = "select * from om_enc_spirometry_result"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected
					.equals("OpenMRS Sputum Instructions Video")) {
				query = "select * from om_enc_sputum_instructions"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS TB First Encounter")) {
				query = "select * from om_enc_tb_first_encounter"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS TB Follow-up")) {
				query = "select * from om_enc_tb_follow_up"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Test Indication")) {
				query = "select * from om_enc_test_indication"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Treatment Initiation")) {
				query = "select * from om_enc_treatment_initiation"
						+ getFilter("location_name", "date_entered", null);
			} else if (reportSelected.equals("OpenMRS Vitals")) {
				query = "select * from om_enc_vital"
						+ getFilter("location_name", "date_entered", null);
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
					Parameter[] params = getParameters();
					service.generateReport(reportSelected, params, export,
							new AsyncCallback<String>() {
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

	/**
	 * Convert the form filters into respective parameters
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Parameter[] getParameters() {
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("UserName", TBR3.getCurrentUser(),
				DataType.STRING));
		if (userIdCheckBox.getValue()) {
			params.add(new Parameter("UserID", TBR3ReporterClient
					.get(userIdTextBox) + "%", DataType.STRING));
		} else {
			params.add(new Parameter("UserID", "%", DataType.STRING));
		}
		if (locationIdCheckBox.getValue()) {
			params.add(new Parameter("LocationID", TBR3ReporterClient
					.get(locationIdTextBox) + "%", DataType.STRING));
		} else {
			params.add(new Parameter("LocationID", "%", DataType.STRING));
		}
		if (dateRangeFilterCheckBox.getValue()
				&& !TBR3ReporterClient.get(fromDateBox).equals("")) {
			Date startDate = fromDateBox.getValue();
			Date endDate = toDateBox.getValue();
			// Add time part, if available
			if (timeRangeFilterCheckBox.getValue()) {
				startDate.setHours(fromTimeDateBox.getValue().getHours());
				startDate.setMinutes(fromTimeDateBox.getValue().getMinutes());
				endDate.setHours(toTimeDateBox.getValue().getHours());
				endDate.setMinutes(toTimeDateBox.getValue().getMinutes());
			}
			params.add(new Parameter("DateFrom", String.valueOf(startDate
					.getTime()), DataType.DATE));
			params.add(new Parameter("DateTo",
					String.valueOf(endDate.getTime()), DataType.DATE));
		} else {
			params.add(new Parameter("DateFrom", String.valueOf(1),
					DataType.DATE));
			params.add(new Parameter("DateTo", String.valueOf(new Date()
					.getTime()), DataType.DATE));
		}
		return params.toArray(new Parameter[] {});
	}

	public void setRights(String menuName) {
		// Not implemented
	}

	public void deleteSelectedReport() {
		// TODO: To be implemented
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		load(true);
		if (sender == viewButton) {
			viewData(false);
		} else if (sender == exportButton) {
			viewData(true);
		}
	}

	@Override
	public void onChange(ChangeEvent event) {
		Widget sender = (Widget) event.getSource();
		// Fill report names
		if (sender == categoryComboBox) {
			String text = TBR3ReporterClient.get(sender);
			reportsListComboBox.clear();
			if (text.equals("Reports")) {
				// Fetch list of reports from server
				service.getReportsList(new AsyncCallback<Report[]>() {

					@Override
					public void onSuccess(Report[] result) {
						reports = result;
						for (Report report : reports) {
							reportsListComboBox.addItem(report.getName());
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("List of reports cannot be populated. Have you copied the reports to rpt directory?");
					}
				});

			} else if (text.equals("Data Dumps")) {
				reportsListComboBox.addItem("Patient Data");
				reportsListComboBox.addItem("Locations Data");
				reportsListComboBox.addItem("Forms Data");
				reportsListComboBox.addItem("Users Data");
				reportsListComboBox.addItem("Field Monitoring Camps");
				reportsListComboBox.addItem("Field Monitoring Daily Visits");
				reportsListComboBox.addItem("Field Monitoring First Visits");
				reportsListComboBox
						.addItem("Field Monitoring Supervisor Visits");
				reportsListComboBox.addItem("OpenMRS Blood Sugar Order");
				reportsListComboBox.addItem("OpenMRS Blood Sugar Results");
				reportsListComboBox.addItem("OpenMRS Clinical Evaluation");
				reportsListComboBox.addItem("OpenMRS Client Information");
				reportsListComboBox.addItem("OpenMRS CXR Order");
				reportsListComboBox.addItem("OpenMRS CXR Results");
				reportsListComboBox.addItem("OpenMRS CXR Test");
				reportsListComboBox.addItem("OpenMRS Diabetes First Encounter");
				reportsListComboBox.addItem("OpenMRS Diabetes Follow-up");
				reportsListComboBox.addItem("OpenMRS Drug Dispersal");
				reportsListComboBox.addItem("OpenMRS GXP Order");
				reportsListComboBox.addItem("OpenMRS GXP Results");
				reportsListComboBox.addItem("OpenMRS GXP Test");
				reportsListComboBox.addItem("OpenMRS HbA1c Order");
				reportsListComboBox.addItem("OpenMRS Mental Health Screening");
				reportsListComboBox.addItem("OpenMRS Non-Pulmonary Suspect");
				reportsListComboBox.addItem("OpenMRS Paediatric Screening");
				reportsListComboBox.addItem("OpenMRS Patient GPS");
				reportsListComboBox.addItem("OpenMRS Screening");
				reportsListComboBox.addItem("OpenMRS Side Effects");
				reportsListComboBox.addItem("OpenMRS Smear Microscopy Order");
				reportsListComboBox.addItem("OpenMRS Smear Microscopy Results");
				reportsListComboBox.addItem("OpenMRS Spirometry Order");
				reportsListComboBox.addItem("OpenMRS Spirometry Results");
				reportsListComboBox
						.addItem("OpenMRS Sputum Instructions Video");
				reportsListComboBox.addItem("OpenMRS TB First Encounter");
				reportsListComboBox.addItem("OpenMRS TB Follow-up");
				reportsListComboBox.addItem("OpenMRS Test Indication");
				reportsListComboBox.addItem("OpenMRS Treatment Initiation");
				reportsListComboBox.addItem("OpenMRS Vitals");

			}
			// Disable view on data dumps
			viewButton.setEnabled(!TBR3ReporterClient.get(categoryComboBox)
					.equals("Data Dumps"));
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public void onValueChange(ValueChangeEvent<Boolean> event) {
		Widget sender = (Widget) event.getSource();
		if (sender == dateRangeFilterCheckBox) {
			fromDateBox.setEnabled(dateRangeFilterCheckBox.getValue());
			Date from = new Date(110, 0, 1);
			fromDateBox.setValue(from);
			toDateBox.setEnabled(dateRangeFilterCheckBox.getValue());
			Date to = new Date();
			to.setHours(0);
			to.setMinutes(0);
			to.setSeconds(0);
			toDateBox.setValue(new Date());
		} else if (sender == timeRangeFilterCheckBox) {
			fromTimeDateBox.setEnabled(timeRangeFilterCheckBox.getValue());
			toTimeDateBox.setEnabled(timeRangeFilterCheckBox.getValue());
		} else if (sender == locationIdCheckBox) {
			locationFilterTypeComboBox
					.setEnabled(locationIdCheckBox.getValue());
			locationIdTextBox.setEnabled(locationIdCheckBox.getValue());
		} else if (sender == userIdCheckBox) {
			userFilterTypeComboBox.setEnabled(userIdCheckBox.getValue());
			userIdTextBox.setEnabled(userIdCheckBox.getValue());
		} else if (sender == sortCheckBox) {
			sortTypeComboBox.setEnabled(sortCheckBox.getValue());
		}
	}
}
