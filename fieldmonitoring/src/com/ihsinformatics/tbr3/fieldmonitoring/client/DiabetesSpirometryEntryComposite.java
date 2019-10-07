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

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.client.FMClient;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

/**
 * @author Tahira
 *
 */
public class DiabetesSpirometryEntryComposite extends Composite implements IForm, ClickHandler, ValueChangeHandler<Boolean>, BlurHandler
{
	private static final String formName = "TEST_ENTRY";
	private static boolean valid;
	
	private final FlexTable flexTable = new FlexTable();
	private final FlexTable topFlexTable = new FlexTable();
	private final Label formTitleLabel = new Label(FM.getProjectTitle () + " Diabetes/Spirometry Entry");
	private final FlexTable testEntryFlexTable = new FlexTable();
	private FlexTable patientResultsFlexTable = new FlexTable();
	private AbsolutePanel spiroResultAbsolutePanel = new AbsolutePanel();
	private final FlexTable bottomFlexTable = new FlexTable();

	private Label formDateLabel = new Label("Form Date   ");
	private DateBox formDateBox = new DateBox();

	private Label locationIdLabel = new Label("Location ID");
	private IntegerBox locationIdIntegerBox = new IntegerBox();
	private Label locationIdHintLabel = new Label("(Format: 101999)");

	private TextBox locationNameTextBox = new TextBox();
	private Label locationNameLabel = new Label("Location Name   ");

	private Label patientAgeLabel = new Label("Patient Age");
	private IntegerBox patientAgeIntegerBox = new IntegerBox();

	private Label patientGenderLabel = new Label("Patient Gender");
	private FlowPanel patientGenderFlowPanel = new FlowPanel();
	private RadioButton genderMaleRadioButton = new RadioButton(
			"genderRadioGroup", "Male");
	private RadioButton genderFemaleRadioButton = new RadioButton(
			"genderRadioGroup", "Female");

	private Label testTypeLabel = new Label("Test Type");
	private FlowPanel testTypeFlowPanel = new FlowPanel();

	private CheckBox testDiabetesCheckBox = new CheckBox("Diabetes");
	private CheckBox testSpirometryCheckBox = new CheckBox("Spirometry");
	
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

	private Label diagnosisLabel = new Label("Diagnosis: ");
	private Label diagnosisResultLabel = new Label("");

	private Label ratioLabel = new Label("FVC/FEV1 Ratio");
	private TextBox ratioActualResultTextBox = new TextBox();
	private TextBox ratioPredictedResultTextBox = new TextBox();
	private TextBox ratioPercentageResultTextBox = new TextBox();

	private Label pefLabel = new Label("PEF Result");
	private TextBox pefActualResultTextBox = new TextBox();
	private TextBox pefPredictedResultTextBox = new TextBox();
	private TextBox pefPercentageResultTextBox = new TextBox();

	
	private final AbsolutePanel buttonAbsolutePanel = new AbsolutePanel();
	private final Button submitButton = new Button("Submit");
	private final Button clearButton = new Button("Clear");

	public DiabetesSpirometryEntryComposite() {
		
		initWidget(flexTable);
		flexTable.setSize("80%", "100%");
		
		flexTable.setWidget(0, 0, topFlexTable);
		topFlexTable.setWidth("650");
		formTitleLabel.setStyleName("title");
		
		topFlexTable.setWidget(0, 0, formTitleLabel);
		flexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		flexTable.setWidget(1, 0, testEntryFlexTable);
		testEntryFlexTable.setWidth("650");
		
		testEntryFlexTable.setWidget(0, 0, formDateLabel);
		testEntryFlexTable.setWidget(0, 1, formDateBox);
		formDateBox.setFormat(new DefaultFormat(DateTimeFormat
				.getFormat("yyyy-MM-dd")));
		testEntryFlexTable.setWidget(1, 0, locationIdLabel);
		testEntryFlexTable.setWidget(1, 1, locationIdIntegerBox);
		testEntryFlexTable.setWidget(1, 2, locationIdHintLabel);
		locationIdHintLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		testEntryFlexTable.setWidget(2, 0, locationNameLabel);
		testEntryFlexTable.setWidget(2, 1, locationNameTextBox);
		testEntryFlexTable.setWidget(3, 0, patientAgeLabel);
		testEntryFlexTable.setWidget(3, 1, patientAgeIntegerBox);
		testEntryFlexTable.setWidget(4, 0, patientGenderLabel);
		patientGenderLabel.setWidth("151px");

		patientGenderFlowPanel.add(genderMaleRadioButton);
		patientGenderFlowPanel.add(genderFemaleRadioButton);

		testEntryFlexTable.setWidget(4, 1, patientGenderFlowPanel);
		testEntryFlexTable.setWidget(5, 0, testTypeLabel);
		testTypeFlowPanel.add(testDiabetesCheckBox);
		testTypeFlowPanel.add(testSpirometryCheckBox);

		testEntryFlexTable.setWidget(5, 1, testTypeFlowPanel);
		
		patientResultsFlexTable.setWidget(1, 0, spirometryResultLabel);
		patientResultsFlexTable.setWidget(1, 1, actualResultLabel);
		patientResultsFlexTable.setWidget(1, 2, predictedResultLabel);
		predictedResultLabel.setWordWrap(true);
		patientResultsFlexTable.setWidget(1, 3, percentageLabel);

		patientResultsFlexTable.setWidget(2, 0, fvcLabel);
		fvcActualResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(2, 1, fvcActualResultTextBox);
		fvcActualResultTextBox.setWidth("100px");
		fvcPredictedResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(2, 2, fvcPredictedResultTextBox);
		fvcPredictedResultTextBox.setWidth("100px");
		fvcPercentageResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(2, 3, fvcPercentageResultTextBox);
		fvcPercentageResultTextBox.setWidth("100px");
		fvcPercentageResultTextBox.setEnabled(false);

		patientResultsFlexTable.setWidget(3, 0, fev1Label);
		fev1ActualResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(3, 1, fev1ActualResultTextBox);
		fev1ActualResultTextBox.setWidth("100px");
		fev1PredictedResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(3, 2, fev1PredictedResultTextBox);
		fev1PredictedResultTextBox.setWidth("100px");
		fev1PercentageResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(3, 3, fev1PercentageResultTextBox);
		fev1PercentageResultTextBox.setWidth("100px");
		fev1PercentageResultTextBox.setEnabled(false);

		patientResultsFlexTable.setWidget(4, 2, diagnosisLabel);
		patientResultsFlexTable.setWidget(4, 3, diagnosisResultLabel);
		diagnosisResultLabel.setWordWrap(true);

		patientResultsFlexTable.setWidget(5, 0, ratioLabel);
		ratioLabel.setWidth("120px");
		ratioActualResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(5, 1, ratioActualResultTextBox);
		ratioActualResultTextBox.setWidth("100px");
		ratioPredictedResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(5, 2, ratioPredictedResultTextBox);
		ratioPredictedResultTextBox.setWidth("100px");
		ratioPercentageResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(5, 3, ratioPercentageResultTextBox);
		ratioPercentageResultTextBox.setWidth("100px");
		ratioPercentageResultTextBox.setEnabled(false);

		patientResultsFlexTable.setWidget(6, 0, pefLabel);
		pefLabel.setWidth("90px");
		pefActualResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(6, 1, pefActualResultTextBox);
		pefActualResultTextBox.setWidth("100px");
		pefPredictedResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(6, 2, pefPredictedResultTextBox);
		pefPredictedResultTextBox.setWidth("100px");
		pefPercentageResultTextBox.setAlignment(TextAlignment.LEFT);
		patientResultsFlexTable.setWidget(6, 3, pefPercentageResultTextBox);
		pefPercentageResultTextBox.setWidth("100px");
		pefPercentageResultTextBox.setEnabled(false);

		patientResultsFlexTable.getColumnFormatter().setWidth(1, "80px");
		patientResultsFlexTable.getColumnFormatter().setWidth(2, "80px");
		patientResultsFlexTable.getColumnFormatter().setWidth(3, "80px");

		spiroResultAbsolutePanel.setWidth("100%");
		patientResultsFlexTable.setSize("80%", "");
		
//		flexTable.setWidget(2, 0, patientResultsFlexTable);
		patientResultsFlexTable.getCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		patientResultsFlexTable.getCellFormatter().setHorizontalAlignment(1, 2, HasHorizontalAlignment.ALIGN_CENTER);
		patientResultsFlexTable.getCellFormatter().setHorizontalAlignment(1, 3, HasHorizontalAlignment.ALIGN_CENTER);
		
		flexTable.setWidget(3, 0, bottomFlexTable);
		bottomFlexTable.setWidth("650");
		
		bottomFlexTable.setWidget(0, 0, buttonAbsolutePanel);
		buttonAbsolutePanel.setSize("550px", "30px");
		submitButton.setText("Submit");
		
		buttonAbsolutePanel.add(submitButton);
		submitButton.setSize("100", "25");
		clearButton.setText("Clear");
		
		buttonAbsolutePanel.add(clearButton);
		clearButton.setSize("100", "25");
		bottomFlexTable.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		locationIdIntegerBox.addBlurHandler(this);
		testDiabetesCheckBox.addValueChangeHandler(this);
		testSpirometryCheckBox.addValueChangeHandler(this);
		submitButton.addClickHandler(this);
		clearButton.addClickHandler(this);
		
		TextBox[] spiroResultTextBoxes = { fvcActualResultTextBox,
				fvcPredictedResultTextBox, fev1ActualResultTextBox,
				fev1PredictedResultTextBox, ratioActualResultTextBox,
				ratioPredictedResultTextBox, pefActualResultTextBox,
				pefPredictedResultTextBox };

		for (TextBox textBox : spiroResultTextBoxes)
		{
			textBox.addBlurHandler(this);
		}
		clearUp();
		refresh(testEntryFlexTable);
		
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
			Window.alert("Location ID - Blur event called");
		}
		else if (sender == fvcActualResultTextBox)
		{
			fvcPredictedResultTextBox.setFocus(true);
		}
		else if (sender == fvcPredictedResultTextBox)
		{
			if (!FMClient.get(fvcActualResultTextBox).equals("")
					&& !FMClient.get(fvcPredictedResultTextBox).equals(""))
			{
				BigDecimal percentageResult = calcualtePercentage(
						FMClient.get(fvcActualResultTextBox),
						FMClient.get(fvcPredictedResultTextBox));
				fvcPercentageResultTextBox.setText(percentageResult.toString());
				fev1ActualResultTextBox.setFocus(true);
			}
			else
				Window.alert("FVC Actual and Predicted results are required.");

		}
		else if (sender == fev1ActualResultTextBox)
		{
			fev1PredictedResultTextBox.setFocus(true);
		}
		else if (sender == fev1PredictedResultTextBox)
		{
			if (!FMClient.get(fev1ActualResultTextBox).equals("")
					&& !FMClient.get(fev1PredictedResultTextBox).equals(""))
			{
				BigDecimal percentageResult = calcualtePercentage(
						FMClient.get(fev1ActualResultTextBox),
						FMClient.get(fev1PredictedResultTextBox));
				fev1PercentageResultTextBox
						.setText(percentageResult.toString());
				if (Integer.parseInt(FMClient
						.get(fev1PercentageResultTextBox).split("\\.")[0]) > 80)
				{
					diagnosisResultLabel
							.setText("Intermittent and Mild Persistent");
				}
				else if (Integer.parseInt(FMClient.get(
						fev1PercentageResultTextBox).split("\\.")[0]) > 60
						&& Integer.parseInt(FMClient.get(
								fev1PercentageResultTextBox).split("\\.")[0]) < 80)
				{
					diagnosisResultLabel.setText("Moderate Persistent");
				}
				else if (Integer.parseInt(FMClient.get(
						fev1PercentageResultTextBox).split("\\.")[0]) < 60)
				{
					diagnosisResultLabel.setText("Severe Persistent");
				}

				ratioActualResultTextBox.setFocus(true);
			}
			else
				Window.alert("FEV1 Actual and Predicted results are required.");
		}
		else if (sender == ratioActualResultTextBox)
		{
			ratioPredictedResultTextBox.setFocus(true);
		}
		else if (sender == ratioPredictedResultTextBox)
		{
			if (!FMClient.get(ratioActualResultTextBox).equals("")
					&& !FMClient.get(ratioPredictedResultTextBox).equals(""))
			{
				BigDecimal percentageResult = calcualtePercentage(
						FMClient.get(ratioActualResultTextBox),
						FMClient.get(ratioPredictedResultTextBox));
				ratioPercentageResultTextBox.setText(percentageResult
						.toString());
				pefActualResultTextBox.setFocus(true);
			}
			else
			{
				Window.alert("FVC/FEV1 Ratio Actual and Predicted results are required.");
			}
		}
		else if (sender == pefActualResultTextBox)
		{
			pefPredictedResultTextBox.setFocus(true);
		}
		else if (sender == pefPredictedResultTextBox)
		{
			if (!FMClient.get(pefActualResultTextBox).equals("")
					&& !FMClient.get(pefPredictedResultTextBox).equals(""))
			{
				BigDecimal percentageResult = calcualtePercentage(
						FMClient.get(pefActualResultTextBox),
						FMClient.get(pefPredictedResultTextBox));
				pefPercentageResultTextBox.setText(percentageResult.toString());
			}
			else
				Window.alert("PEF Actual and Predicted results are required.");
		}

	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.ValueChangeHandler#onValueChange(com.google.gwt.event.logical.shared.ValueChangeEvent)
	 */
	@Override
	public void onValueChange(ValueChangeEvent<Boolean> event)
	{
		Widget sender = (Widget) event.getSource();
		if (sender instanceof CheckBox)
		{
			CheckBox checkBox = (CheckBox) sender;
			boolean choice = checkBox.getValue();
			if (sender == testDiabetesCheckBox)
			{
				// check this
				if (choice)
				{
					testEntryFlexTable.setWidget(6, 0, patientResultLabel);
//					testEntryFlexTable.getCellFormatter().setHorizontalAlignment(8, 0, HasHorizontalAlignment.ALIGN_RIGHT);
					testEntryFlexTable.setWidget(6, 1, patientResultTextBox);
				}
				else
				{
					testEntryFlexTable.remove(patientResultLabel);
					testEntryFlexTable.remove(patientResultTextBox);
				}
			}
			else if (sender == testSpirometryCheckBox)
			{
				if (choice)
				{
					flexTable.setWidget(2, 0, spiroResultAbsolutePanel);
					spiroResultAbsolutePanel.add(patientResultsFlexTable);
					flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
				}
				else
				{
					flexTable.remove(spiroResultAbsolutePanel);
					spiroResultAbsolutePanel.remove(patientResultsFlexTable);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.ihsinformatics.tbr3.fieldmonitoring.client.IForm#clearUp()
	 */
	@Override
	public void clearUp()
	{
		clearControls(testEntryFlexTable);
		clearControls(patientResultsFlexTable);
		IntegerBox[] integerBoxes = { locationIdIntegerBox,
				patientAgeIntegerBox };

		// ListBox[] listBoxes = { townListBox };
		for (int i = 0; i < integerBoxes.length; i++)
			integerBoxes[i].setText("0");
		// for (int i = 0; i < listBoxes.length; i++)
		// listBoxes[i].setSelectedIndex(0);
		formDateBox.setValue(new Date());
		locationIdIntegerBox.setText("000000");
		TextBox[] spiroResultTextBoxes = { fvcActualResultTextBox,
				fvcPredictedResultTextBox, fev1ActualResultTextBox,
				fev1PredictedResultTextBox, ratioActualResultTextBox,
				ratioPredictedResultTextBox, pefActualResultTextBox,
				pefPredictedResultTextBox };
		for (TextBox textBox : spiroResultTextBoxes)
		{
			textBox.setText("0.00");
		}
		TextBox[] spiroPercentageTextBoxes = { fvcPercentageResultTextBox,
				fev1PercentageResultTextBox, ratioPercentageResultTextBox,
				pefPercentageResultTextBox };
		for (TextBox textBox : spiroPercentageTextBoxes)
		{
			textBox.setText("0.0");
		}
		locationNameTextBox.setEnabled(false);
		
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
		if (FMClient.get(patientAgeIntegerBox).equals(""))
			errorMessage.append("Patient age: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");
		if (!genderMaleRadioButton.isChecked()
				&& !genderFemaleRadioButton.isChecked())
			errorMessage.append("Patient gender: "
					+ CustomMessage.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
					+ "\n");

		if (testDiabetesCheckBox.isChecked())
		{
			if (FMClient.get(patientResultTextBox).equals(""))
				errorMessage.append("Patient Result/Score: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");
		}
		else if (testSpirometryCheckBox.isChecked())
		{
			if (FMClient.get(fvcActualResultTextBox).equals(""))
				errorMessage.append("FVC Actual Result: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");
			if (FMClient.get(fvcPredictedResultTextBox).equals(""))
				errorMessage.append("FVC Predicted Result: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");

			if (FMClient.get(fev1ActualResultTextBox).equals(""))
				errorMessage.append("FEV1 Actual Result: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");
			if (FMClient.get(fev1PredictedResultTextBox).equals(""))
				errorMessage.append("FEV1 Predicted Result: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");

			if (FMClient.get(ratioActualResultTextBox).equals(""))
				errorMessage.append("FVC/FEV1 Ratio Actual Result: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");
			if (FMClient.get(ratioPredictedResultTextBox).equals(""))
				errorMessage.append("FVC/FEV1 Ratio Predicted Result: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");

			if (FMClient.get(pefActualResultTextBox).equals(""))
				errorMessage.append("PEF Actual Result: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");
			if (FMClient.get(pefPredictedResultTextBox).equals(""))
				errorMessage.append("PEF Predicted Result: "
						+ CustomMessage
								.getErrorMessage(ErrorType.EMPTY_DATA_ERROR)
						+ "\n");
		}
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
	
	public BigDecimal calcualtePercentage(String actualResult, String predictedResult)
	{
		float result = (Float.parseFloat(actualResult) / Float
				.parseFloat(predictedResult)) * 100;
		return new BigDecimal(String.valueOf(result)).setScale(1,
				BigDecimal.ROUND_FLOOR);
	}

}
