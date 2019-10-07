/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.

Contributors: Tahira Niazi */
package com.ihsinformatics.fieldmonitoring_mobile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ihsinformatics.fieldmonitoring_mobile.custom.MyButton;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyCheckBox;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyEditText;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyRadioButton;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyRadioGroup;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MySpinner;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyTextView;
import com.ihsinformatics.fieldmonitoring_mobile.model.OpenMrsObject;
import com.ihsinformatics.fieldmonitoring_mobile.shared.AlertType;
import com.ihsinformatics.fieldmonitoring_mobile.shared.FormType;
import com.ihsinformatics.fieldmonitoring_mobile.util.RegexUtil;

/**
 * @author tahira.niazi@ihsinformatics.com
 */
public class DiabetesSpirometryActivity extends AbstractFragmentActivity
		implements OnEditorActionListener {
	private static final String formName = "TEST_ENTRY";

	MyTextView formDateTextView;
	MyButton formDateButton;
	MyTextView formDateViewOnly;

	MyTextView locationIdTextView;
	MyEditText locationId;

	MyTextView locationNameTextView;
	MyEditText locationName;

	MyTextView testTypeTextView;
	MyCheckBox diabetesCheckBox;
	MyCheckBox spirometryCheckBox;

	MyTextView ageTextView;
	MyEditText age;

	MyTextView genderTextView;
	MyRadioGroup gender;
	MyRadioButton male;
	MyRadioButton female;

	MyTextView patientResultTextView;
	MyEditText patientResult;

	MyTextView fvcResultTextView;
	MyEditText fvcResult;

	MyTextView fvcPredictedTextView;
	MyEditText fvcPredicted;

	MyTextView fvcPercentageTextView;
	MyTextView fvcPercentage;

	MyTextView fevResultTextView;
	MyEditText fevResult;

	MyTextView fevPredictedTextView;
	MyEditText fevPredicted;

	MyTextView fevPercentageTextView;
	MyTextView fevPercentage;

	MyTextView fevDiagnosisTextView;
	MyTextView fevDiagnosis;

	MyTextView fvcFevRatioTextView;
	MyEditText fvcFevRatio;

	MyTextView fvcFevRatioPredictedTextView;
	MyEditText fvcFevRatioPredicted;

	MyTextView fvcFevPercentageTextView;
	MyTextView fvcFevPercentage;

	MyTextView fvcDiagnosisTextView;
	MyTextView fvcDiagnosis;

	MyTextView pefResultTextView;
	MyEditText pefResult;

	MyTextView pefPredictedTextView;
	MyEditText pefPredicted;

	MyTextView pefPercentageTextView;
	MyTextView pefPercentage;

	/**
	 * Subclass representing Fragment for adult-screeening suspect form
	 * 
	 * @author owais.hussain@ihsinformatics.com
	 * 
	 */
	@SuppressLint("ValidFragment")
	class DiabetesSpirometryFragment extends Fragment {
		int currentPage;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle data = getArguments();
			currentPage = data.getInt("current_page", 0);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Return a layout of views from pre-filled ArrayList of groups
			if (currentPage != 0 && groups.size() != 0)
				return groups.get(currentPage - 1);
			else
				return null;
		}
	}

	/**
	 * Subclass for Pager Adapter. Uses AdultScreeningSuspect subclass as items
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class DiabetesSpirometryFragmentPagerAdapter extends FragmentPagerAdapter {
		/** Constructor of the class */
		public DiabetesSpirometryFragmentPagerAdapter(
				FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		/** This method will be invoked when a page is requested to create */
		@Override
		public Fragment getItem(int arg0) {
			DiabetesSpirometryFragment fragment = new DiabetesSpirometryFragment();
			Bundle data = new Bundle();
			data.putInt("current_page", arg0 + 1);
			fragment.setArguments(data);
			return fragment;
		}

		/** Returns the number of pages */
		@Override
		public int getCount() {
			return PAGE_COUNT;
		}
	}

	@Override
	public void createViews(Context context) {
		TAG = "DiabetesSpirometryEntryActivity";
		PAGE_COUNT = 5;
		pager = (ViewPager) findViewById(R.template_id.pager);
		navigationSeekbar.setMax(PAGE_COUNT - 1);
		navigatorLayout = (LinearLayout) findViewById(R.template_id.navigatorLayout);
		// If the form consists only of single page, then hide the
		// navigatorLayout
		if (PAGE_COUNT < 2) {
			navigatorLayout.setVisibility(View.GONE);
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		DiabetesSpirometryFragmentPagerAdapter pagerAdapter = new DiabetesSpirometryFragmentPagerAdapter(
				fragmentManager);
		pager.setAdapter(pagerAdapter);
		pager.setOffscreenPageLimit(PAGE_COUNT);
		// Create views for pages
		formDateTextView = new MyTextView(context, R.style.text,
				R.string.form_date);
		formDateButton = new MyButton(context, R.style.button,
				R.drawable.custom_button_beige, R.string.form_date,
				R.string.form_date);

		formDateViewOnly = new MyTextView(context, R.style.text,
				R.string.form_date);

		locationIdTextView = new MyTextView(context, R.style.text,
				R.string.location_id);
		locationId = new MyEditText(context, R.string.location_id,
				R.string.location_id_hint, InputType.TYPE_CLASS_NUMBER,
				R.style.edit, 6, false);

		locationNameTextView = new MyTextView(context, R.style.text,
				R.string.location_name);
		locationName = new MyEditText(context, R.string.location_name,
				R.string.empty_string, InputType.TYPE_CLASS_TEXT, R.style.edit,
				50, false);

		testTypeTextView = new MyTextView(context, R.style.text,
				R.string.test_type);
		diabetesCheckBox = new MyCheckBox(context, R.string.diabetes,
				R.style.edit, R.string.diabetes, false);
		spirometryCheckBox = new MyCheckBox(context, R.string.spirometry,
				R.style.edit, R.string.spirometry, false);
		// testType = new MySpinner(context,
		// getResources().getStringArray(R.array.test_types),
		// R.string.test_type, R.string.option_hint);

		ageTextView = new MyTextView(context, R.style.text, R.string.age);
		age = new MyEditText(context, R.string.age, R.string.age_hint,
				InputType.TYPE_CLASS_NUMBER, R.style.edit, 3, false);

		genderTextView = new MyTextView(context, R.style.text, R.string.gender);
		male = new MyRadioButton(context, R.string.male, R.style.radio,
				R.string.male);
		female = new MyRadioButton(context, R.string.female, R.style.radio,
				R.string.female);
		gender = new MyRadioGroup(context,
				new MyRadioButton[] { male, female }, R.string.gender,
				R.style.radio, App.isLanguageRTL(), 1);

		patientResultTextView = new MyTextView(context, R.style.text,
				R.string.patient_result);
		patientResult = new MyEditText(context, R.string.patient_result,
				R.string.patient_result_hint, InputType.TYPE_CLASS_TEXT,
				R.style.edit, 10, false);

		fvcResultTextView = new MyTextView(context, R.style.text,
				R.string.fvc_result);
		fvcResult = new MyEditText(context, R.string.fvc_result,
				R.string.result_format_hint, InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL, R.style.edit, 5,
				false);

		fvcPredictedTextView = new MyTextView(context, R.style.text,
				R.string.fvc_predicted);
		fvcPredicted = new MyEditText(context, R.string.fvc_predicted,
				R.string.result_format_hint, InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL, R.style.edit, 5,
				false);

		fvcPercentageTextView = new MyTextView(context, R.style.text,
				R.string.fvc_percentage);
		fvcPercentage = new MyTextView(context, R.style.text,
				R.string.percentage_default_value);

		fvcDiagnosisTextView = new MyTextView(context, R.style.text,
				R.string.fvc_diagnosis);
		fvcDiagnosis = new MyTextView(context, R.style.text,
				R.string.fvc_diagnosis_value);

		fevResultTextView = new MyTextView(context, R.style.text,
				R.string.fev_result);
		fevResult = new MyEditText(context, R.string.fev_result,
				R.string.result_format_hint, InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL, R.style.edit, 5,
				false);

		fevPredictedTextView = new MyTextView(context, R.style.text,
				R.string.fev_predicted);
		fevPredicted = new MyEditText(context, R.string.fev_predicted,
				R.string.result_format_hint, InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL, R.style.edit, 5,
				false);

		fevPercentageTextView = new MyTextView(context, R.style.text,
				R.string.fev_percentage);
		fevPercentage = new MyTextView(context, R.style.text,
				R.string.percentage_default_value);

		fevDiagnosisTextView = new MyTextView(context, R.style.text,
				R.string.fev_diagnosis);
		fevDiagnosis = new MyTextView(context, R.style.text,
				R.string.fev_diagnosis_value);

		fvcFevRatioTextView = new MyTextView(context, R.style.text,
				R.string.fvcfev_ratio);
		fvcFevRatio = new MyEditText(context, R.string.fvcfev_ratio,
				R.string.result_format_hint, InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL, R.style.edit, 5,
				false);

		fvcFevRatioPredictedTextView = new MyTextView(context, R.style.text,
				R.string.fvcfev_ratio_predicted);
		fvcFevRatioPredicted = new MyEditText(context,
				R.string.fvcfev_ratio_predicted, R.string.result_format_hint,
				InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL, R.style.edit, 5,
				false);

		fvcFevPercentageTextView = new MyTextView(context, R.style.text,
				R.string.fvcfev_percentage);
		fvcFevPercentage = new MyTextView(context, R.style.text,
				R.string.percentage_default_value);

		pefResultTextView = new MyTextView(context, R.style.text,
				R.string.pef_result);
		pefResult = new MyEditText(context, R.string.pef_result,
				R.string.result_format_hint, InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL, R.style.text, 5,
				false);

		pefPredictedTextView = new MyTextView(context, R.style.text,
				R.string.pef_predicted);
		pefPredicted = new MyEditText(context, R.string.pef_predicted,
				R.string.result_format_hint, InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL, R.style.edit, 5,
				false);

		pefPercentageTextView = new MyTextView(context, R.style.text,
				R.string.pef_percentage);
		pefPercentage = new MyTextView(context, R.style.text,
				R.string.percentage_default_value);

		View[][] viewGroups = {
				{ formDateTextView, formDateViewOnly, /* formDateButton, */
				locationIdTextView, locationId, locationNameTextView,
						locationName, ageTextView, age, genderTextView, gender },
				{ testTypeTextView, diabetesCheckBox, spirometryCheckBox,
						patientResultTextView, patientResult,
						fvcResultTextView, fvcResult, fvcPredictedTextView,
						fvcPredicted, fvcPercentageTextView, fvcPercentage,
						fvcDiagnosisTextView, fvcDiagnosis },
				{ fevResultTextView, fevResult, fevPredictedTextView,
						fevPredicted, fevPercentageTextView, fevPercentage,
						fevDiagnosisTextView, fevDiagnosis },
				{ fvcFevRatioTextView, fvcFevRatio,
						fvcFevRatioPredictedTextView, fvcFevRatioPredicted,
						fvcFevPercentageTextView, fvcFevPercentage },
				{ pefResultTextView, pefResult, pefPredictedTextView,
						pefPredicted, pefPercentageTextView, pefPercentage } };

		// Create layouts and store in ArrayList
		groups = new ArrayList<ViewGroup>();
		for (int i = 0; i < PAGE_COUNT; i++) {
			LinearLayout layout = new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			for (int j = 0; j < viewGroups[i].length; j++) {
				layout.addView(viewGroups[i][j]);
			}
			ScrollView scrollView = new ScrollView(context);
			scrollView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			scrollView.addView(layout);
			groups.add(scrollView);
		}

		// Set event listeners
		firstButton.setOnClickListener(this);
		navigationSeekbar.setOnSeekBarChangeListener(this);
		formDateButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);

		fvcResult.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fvcPercentage.setText("0.00");
				fvcDiagnosis.setText("none");
				if (fvcResult.length() == 5) {
					validateValue(fvcResult); // do nothing just validate regex
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		fvcPredicted.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fvcPercentage.setText("0.00");
				fvcDiagnosis.setText("none");
				if (fvcPredicted.length() == 5) {
					if (validateValue(fvcPredicted)) {
						BigDecimal percentageResult = calculatePercentage(
								App.get(fvcResult), App.get(fvcPredicted));
						fvcPercentage.setText(percentageResult.toString());
						fvcDiagnosis.setText(getFvcDiagnosis(App
								.get(fvcPercentage)));
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable editText) {

			}
		});

		fevResult.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fevPercentage.setText("0.00");
				fevDiagnosis.setText("none");
				if (fevResult.length() == 5) {
					validateValue(fevResult);

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		fevPredicted.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fevPercentage.setText("0.00");
				fevDiagnosis.setText("none");
				if (fevPredicted.length() == 5) {
					if (validateValue(fevPredicted)) {
						BigDecimal percentageResult = calculatePercentage(
								App.get(fevResult), App.get(fevPredicted));
						fevPercentage.setText(percentageResult.toString());
						fevDiagnosis.setText(getFevDiagnosis(App
								.get(fevPercentage)));
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable editText) {

			}
		});

		fvcFevRatio.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fvcFevPercentage.setText("0.00");
				if (fvcFevRatio.length() == 5) {
					validateValue(fvcFevRatio);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		fvcFevRatioPredicted.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fvcFevPercentage.setText("0.00");
				if (fvcFevRatioPredicted.length() == 5) {
					if (validateValue(fvcFevRatioPredicted)) {
						BigDecimal percentageResult = calculatePercentage(
								App.get(fvcFevRatio),
								App.get(fvcFevRatioPredicted));
						fvcFevPercentage.setText(percentageResult.toString());
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable editText) {

			}
		});

		pefResult.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				pefPercentage.setText("0.00");
				if (pefResult.length() == 5) {
					validateValue(pefResult);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		pefPredicted.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				pefPercentage.setText("0.00");
				if (pefPredicted.length() == 5) {
					if (validateValue(pefPredicted)) {
						BigDecimal percentageResult = calculatePercentage(
								App.get(pefResult), App.get(pefPredicted));
						pefPercentage.setText(percentageResult.toString());
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable editText) {

			}
		});

		locationId.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				locationName.setText("");
				locationName.setHint("Location Name required");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 6) {
					validateLocation();
				}
			}
		});

		views = new View[] { locationId, locationName, diabetesCheckBox,
				spirometryCheckBox, age, patientResult, fvcResult,
				fvcPredicted, fevResult, fevPredicted, fvcFevRatio,
				fvcFevRatioPredicted, pefResult, pefPredicted };

		for (View v : views) {
			if (v instanceof Spinner) {
				((Spinner) v).setOnItemSelectedListener(this);
			} else if (v instanceof CheckBox) {
				((CheckBox) v).setOnCheckedChangeListener(this);
			}
		}
		pager.setOnPageChangeListener(this);

		// Detect RTL language
		if (App.isLanguageRTL()) {
			Collections.reverse(groups);
			for (ViewGroup g : groups) {
				LinearLayout linearLayout = (LinearLayout) g.getChildAt(0);
				linearLayout.setGravity(Gravity.RIGHT);
			}
			for (View v : views) {
				if (v instanceof EditText) {
					((EditText) v).setGravity(Gravity.RIGHT);
				}
			}
		}
	}

	@Override
	public void initView(View[] views) {
		super.initView(views);
		formDate = Calendar.getInstance();
		visitDate = Calendar.getInstance();
		startTime = Calendar.getInstance();
		endTime = Calendar.getInstance();
		male.setChecked(true);
		String completeLocation = App.getLocation();
		String locationInitialId = completeLocation.split("\\s+")[0];
		// locationId.setText(locationInitialId);
		// locationId.setEnabled(false);
		// locationName.setText(App.getLocation());
		locationName.setEnabled(false);
		diabetesCheckBox.setChecked(true);
		spirometryCheckBox.setChecked(true);

		fvcPercentage
				.setTextColor(getResources().getColor(R.color.DeepSkyBlue));
		fvcDiagnosis.setTextColor(getResources().getColor(R.color.DeepSkyBlue));
		fevPercentage
				.setTextColor(getResources().getColor(R.color.DeepSkyBlue));
		fevDiagnosis.setTextColor(getResources().getColor(R.color.DeepSkyBlue));
		fvcFevPercentage.setTextColor(getResources().getColor(
				R.color.DeepSkyBlue));
		pefPercentage
				.setTextColor(getResources().getColor(R.color.DeepSkyBlue));

		fevDiagnosis.setText(R.string.fev_diagnosis_value);
		fvcDiagnosis.setText(R.string.fvc_diagnosis_value);

		formDateViewOnly.setTextColor(getResources().getColor(
				R.color.DeepSkyBlue));

		updateDisplay();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View view) {
		if (view == firstButton) {
			gotoFirstPage();
		} else if (view == formDateButton) {
			showDialog(DATE_DIALOG_ID);
		} else if (view == firstButton) {
			gotoFirstPage();
		} else if (view == lastButton) {
			gotoLastPage();
		} else if (view == clearButton) {
			initView(views);
		} else if (view == saveButton) {
			submit();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		MySpinner spinner = (MySpinner) parent;
		boolean visible = spinner.getSelectedItemPosition() == 1;
		// if (parent == cough)
		// {
		// coughDurationTextView.setEnabled(visible);
		// coughDuration.setEnabled(visible);
		// coughProductiveTextView.setEnabled(visible);
		// coughProductive.setEnabled(visible);
		// }
		//
		// else if (parent == tbBefore)
		// {
		// tbMedicationTextView.setEnabled(visible);
		// tbMedication.setEnabled(visible);
		// }
		// else if (parent == screenedBefore)
		// {
		// if (screenedBefore.getSelectedItem().toString()
		// .equals(getResources().getString(R.string.yes)))
		// {
		// String message =
		// "The client is screened already. The form is being closed.";
		// AlertDialog dialog;
		// AlertDialog.Builder builder = new Builder(this);
		// builder.setMessage(message);
		// builder.setIcon(R.drawable.info);
		// dialog = builder.create();
		// dialog.setTitle("Information");
		// OnClickListener listener = new OnClickListener()
		// {
		// @Override
		// public void onClick(DialogInterface dialog, int which)
		// {
		// dialog.dismiss();
		// finish();
		// Intent mainMenuIntent = new Intent(
		// DailyVisit.this,
		// MainMenuActivity.class);
		// startActivity(mainMenuIntent);
		// }
		// };
		// dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", listener);
		// dialog.show();
		// }
		// }
		updateDisplay();
	}

	@Override
	public void onCheckedChanged(CompoundButton button, boolean state) {
		if (button == diabetesCheckBox) {
			patientResultTextView.setEnabled(state);
			patientResult.setEnabled(state);

		}
		if (button == spirometryCheckBox) {
			fvcResultTextView.setEnabled(state);
			fvcResult.setEnabled(state);
			fvcPredictedTextView.setEnabled(state);
			fvcPredicted.setEnabled(state);
			fvcPercentageTextView.setEnabled(state);
			fvcPercentage.setEnabled(state);
			fvcDiagnosisTextView.setEnabled(state);
			fvcDiagnosis.setEnabled(state);

			fevResultTextView.setEnabled(state);
			fevResult.setEnabled(state);
			fevPredictedTextView.setEnabled(state);
			fevPredicted.setEnabled(state);
			fevPercentageTextView.setEnabled(state);
			fevPercentage.setEnabled(state);
			fevDiagnosisTextView.setEnabled(state);
			fevDiagnosis.setEnabled(state);

			fvcFevRatioTextView.setEnabled(state);
			fvcFevRatio.setEnabled(state);
			fvcFevRatioPredictedTextView.setEnabled(state);
			fvcFevRatioPredicted.setEnabled(state);
			fvcFevPercentageTextView.setEnabled(state);
			fvcFevPercentage.setEnabled(state);

			pefResultTextView.setEnabled(state);
			pefResult.setEnabled(state);
			pefPredictedTextView.setEnabled(state);
			pefPredicted.setEnabled(state);
			pefPercentageTextView.setEnabled(state);
			pefPercentage.setEnabled(state);

		}
	}

	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateDisplay() {
		formDateButton.setText(DateFormat.format("dd-MMM-yyyy", formDate));
		formDateViewOnly.setText(DateFormat.format("dd-MMM-yyyy", formDate));
	}

	@Override
	public boolean validate() {
		boolean valid = true;
		StringBuffer message = new StringBuffer();
		// Validate mandatory controls
		View[] mandatory = { locationId, locationName, age };
		for (View v : mandatory) {
			if (App.get(v).equals("")) {
				valid = false;
				message.append(v.getTag().toString() + ". ");
				((EditText) v).setHintTextColor(getResources().getColor(
						R.color.Red));
			}
		}

		if (diabetesCheckBox.isChecked() && App.get(patientResult).equals("")) {
			valid = false;
			message.append(patientResult.getTag().toString() + ".");
			patientResult
					.setHintTextColor(getResources().getColor(R.color.Red));
		}

		if (spirometryCheckBox.isChecked()) {
			if (App.get(fvcResult).equals("")) {
				valid = false;
				message.append(fvcResult.getTag().toString() + ".");
				fvcResult
						.setHintTextColor(getResources().getColor(R.color.Red));
			}
			if (App.get(fvcPredicted).equals("")) {
				valid = false;
				message.append(fvcPredicted.getTag().toString() + ".");
				fvcPredicted.setHintTextColor(getResources().getColor(
						R.color.Red));
			}
			if (App.get(fevResult).equals("")) {
				valid = false;
				message.append(fevResult.getTag().toString() + ".");
				fevResult
						.setHintTextColor(getResources().getColor(R.color.Red));
			}
			if (App.get(fevPredicted).equals("")) {
				valid = false;
				message.append(fevPredicted.getTag().toString() + ".");
				fevPredicted.setHintTextColor(getResources().getColor(
						R.color.Red));
			}
			if (App.get(fvcFevRatio).equals("")) {
				valid = false;
				message.append(fvcFevRatio.getTag().toString() + ".");
				fvcFevRatio.setHintTextColor(getResources().getColor(
						R.color.Red));
			}
			if (App.get(fvcFevRatioPredicted).equals("")) {
				valid = false;
				message.append(fvcFevRatioPredicted.getTag().toString() + ".");
				fvcFevRatioPredicted.setHintTextColor(getResources().getColor(
						R.color.Red));
			}
			if (App.get(pefResult).equals("")) {
				valid = false;
				message.append(pefResult.getTag().toString() + ".");
				pefResult
						.setHintTextColor(getResources().getColor(R.color.Red));
			}
			if (App.get(pefPredicted).equals("")) {
				valid = false;
				message.append(pefPredicted.getTag().toString() + ".");
				pefPredicted.setHintTextColor(getResources().getColor(
						R.color.Red));
			}
		}

		if (!valid) {
			message.append(getResources().getString(R.string.empty_data) + "\n");
		}

		// Validate data
		if (valid) {
		}

		// Validate ranges
		try {
			// Form date range
			if (formDate.getTime().after(Calendar.getInstance().getTime())) {
				valid = false;
				message.append(formDateButton.getTag()
						+ ": "
						+ getResources().getString(
								R.string.invalid_date_or_time) + "\n");
			}
			// Age range
			if (!App.get(age).equals("")) {
				int a = Integer.parseInt(App.get(age));
				if (a < 0 || a > 99) {
					valid = false;
					age.setTextColor(getResources().getColor(R.color.Red));
					message.append(age.getTag().toString() + ": "
							+ getResources().getString(R.string.out_of_range)
							+ "\n");
				}
			}
		} catch (NumberFormatException e) {
		}

		if (valid) {
			if (!diabetesCheckBox.isChecked()
					&& !spirometryCheckBox.isChecked()) {
				valid = false;
				message.append(testTypeTextView.getText() + ": "
						+ "Please select atleast one type.");
			}
		}

		if (!valid) {
			App.getAlertDialog(this, AlertType.ERROR, message.toString())
					.show();
		}

		return valid;
	}

	@Override
	public boolean submit() {
		if (validate()) {
			final ContentValues values = new ContentValues();
			values.put("formDate", App.getSqlDate(formDate));
			values.put("location", App.getLocation());
			values.put("locationId", App.getLocation().split("\\s+")[0]);
			values.put("formName", formName);
			// String locationId = App.getLocation().split("\\s+")[0];

			// values.put ("dob", App.getSqlDate (dob));
			final ArrayList<String[]> observations = new ArrayList<String[]>();

			observations.add(new String[] { "F_DATE",
					App.getSqlDateTime(formDate) });
			observations
					.add(new String[] { "LOCATION_ID", App.get(locationId) });
			observations.add(new String[] { "LOCATION_NAME",
					App.get(locationName) });
			observations.add(new String[] { "PATIENT_AGE", App.get(age) });
			observations.add(new String[] { "PATIENT_GENDER",
					male.isChecked() ? "M" : "F" });

			if (diabetesCheckBox.isChecked()) {
				observations.add(new String[] { "TEST_DIABETES", "Yes" });
				observations.add(new String[] { "PATIENT_RESULT",
						App.get(patientResult) });
			}
			if (spirometryCheckBox.isChecked()) {
				observations.add(new String[] { "TEST_SPIROMETRY", "Yes" });

				observations.add(new String[] { "FVC_RESULT",
						App.get(fvcResult) });
				observations.add(new String[] { "FVC_PREDICTED",
						App.get(fvcPredicted) });
				observations.add(new String[] { "FVC_PERCENTAGE",
						App.get(fvcPercentage) });
				observations.add(new String[] { "FVC_DIAGNOSIS",
						App.get(fvcDiagnosis) });

				observations.add(new String[] { "FEV1_RESULT",
						App.get(fevResult) });
				observations.add(new String[] { "FEV1_PREDICTED",
						App.get(fevPredicted) });
				observations.add(new String[] { "FEV1_PERCENTAGE",
						App.get(fevPercentage) });
				observations.add(new String[] { "FEV1_DIAGNOSIS",
						App.get(fevDiagnosis) });

				observations.add(new String[] { "FVC/FEV1_RATIO",
						App.get(fvcFevRatio) });
				observations.add(new String[] { "FVC/FEV1_RATIO_PREDICTED",
						App.get(fvcFevRatioPredicted) });
				observations.add(new String[] { "FVC/FEV1_PERCENTAGE",
						App.get(fvcFevPercentage) });

				observations.add(new String[] { "PEF_RESULT",
						App.get(pefResult) });
				observations.add(new String[] { "PEF_PREDICTED",
						App.get(pefPredicted) });
				observations.add(new String[] { "PEF_PERCENTAGE",
						App.get(pefPercentage) });
			}

			AsyncTask<String, String, String> updateTask = new AsyncTask<String, String, String>() {
				@Override
				protected String doInBackground(String... params) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							loading.setIndeterminate(true);
							loading.setCancelable(false);
							loading.setMessage(getResources().getString(
									R.string.loading_message));
							loading.show();
						}
					});

					String result = "";
					result = serverService.saveVisits(FormType.TEST_ENTRY,
							values, observations.toArray(new String[][] {}));
					return result;
				}

				@Override
				protected void onProgressUpdate(String... values) {
				}

				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					loading.dismiss();
					if (result.equals("SUCCESS")) {
						App.getAlertDialog(DiabetesSpirometryActivity.this,
								AlertType.INFO,
								getResources().getString(R.string.inserted))
								.show();
						initView(views);
					} else {
						App.getAlertDialog(DiabetesSpirometryActivity.this,
								AlertType.ERROR, result).show();
						initView(views);
					}
				}
			};
			updateTask.execute("");
		}
		return true;
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data)
	// {
	// super.onActivityResult(requestCode, resultCode, data);
	// // Retrieve barcode scan results
	// if (requestCode == Barcode.BARCODE_RESULT)
	// {
	// if (resultCode == RESULT_OK)
	// {
	// String str = data.getStringExtra(Barcode.SCAN_RESULT);
	// // Check for valid Id
	// if (RegexUtil.isValidId(str)
	// && !RegexUtil.isNumeric(str, false))
	// {
	// patientId.setText(str);
	// }
	// else
	// {
	// App.getAlertDialog(
	// this,
	// AlertType.ERROR,
	// patientId.getTag().toString()
	// + ": "
	// + getResources().getString(
	// R.string.invalid_data)).show();
	// }
	// }
	// else if (resultCode == RESULT_CANCELED)
	// {
	// // Handle cancel
	// App.getAlertDialog(this, AlertType.ERROR,
	// getResources().getString(R.string.operation_cancelled))
	// .show();
	// }
	// // Set the locale again, since the Barcode app restores system's
	// // locale because of orientation
	// Locale.setDefault(App.getCurrentLocale());
	// Configuration config = new Configuration();
	// config.locale = App.getCurrentLocale();
	// getApplicationContext().getResources().updateConfiguration(config,
	// null);
	// }
	// };

	@Override
	public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	public BigDecimal calculatePercentage(String actualResult,
			String predictedResult) {
		float result = (Float.parseFloat(actualResult) / Float
				.parseFloat(predictedResult)) * 100;
		return new BigDecimal(String.valueOf(result)).setScale(1,
				BigDecimal.ROUND_FLOOR);
	}

	public boolean validateValue(EditText editText1) {
		boolean valid = true;
		StringBuffer message = new StringBuffer();
		if (!App.get(editText1).equals("")
				&& !RegexUtil.isSpirometryResultPattern(App.get(editText1))) {
			valid = false;
			message.append(editText1.getTag().toString() + ": "
					+ getResources().getString(R.string.invalid_data) + "\n");
			editText1.setTextColor(getResources().getColor(R.color.Red));
		}

		if (!valid) {
			App.getAlertDialog(this, AlertType.ERROR, message.toString())
					.show();
			editText1.requestFocus();
		}
		return valid;
	}

	public String getFvcDiagnosis(String fvcPercentage) {
		String fvcDiagnosis = "none";

		if (Integer.parseInt(fvcPercentage.split("\\.")[0]) > 70) {
			fvcDiagnosis = "Normal";
		} else if (Integer.parseInt(fvcPercentage.split("\\.")[0]) >= 60
				&& Integer.parseInt(fvcPercentage.split("\\.")[0]) < 70) {
			fvcDiagnosis = "Mild Restriction";
		} else if (Integer.parseInt(fvcPercentage.split("\\.")[0]) >= 50
				&& Integer.parseInt(fvcPercentage.split("\\.")[0]) < 60) {
			fvcDiagnosis = "Moderate Restriction";
		} else if (Integer.parseInt(fvcPercentage.split("\\.")[0]) >= 34
				&& Integer.parseInt(fvcPercentage.split("\\.")[0]) < 50) {
			fvcDiagnosis = "Severe Restriction";
		}
		return fvcDiagnosis;
	}

	public String getFevDiagnosis(String fevPercentage) {
		String fevDiagnosis = "none";

		if (Integer.parseInt(fevPercentage.split("\\.")[0]) > 80) {
			fevDiagnosis = "Intermittent and Mild Persistent";
		} else if (Integer.parseInt(fevPercentage.split("\\.")[0]) >= 60
				&& Integer.parseInt(fevPercentage.split("\\.")[0]) <= 80) {
			fevDiagnosis = "Moderate Persistent";
		} else if (Integer.parseInt(fevPercentage.split("\\.")[0]) < 60) {
			fevDiagnosis = "Severe Persistent";
		}
		return fevDiagnosis;
	}

	public void validateLocation() {

		if (locationId.getText().toString().equals("")
				|| App.get(locationId).length() != 6) {
			StringBuffer message = new StringBuffer();
			message.append("Enter valid Location ID");
			locationId.setHintTextColor(getResources().getColor(R.color.Red));
			App.getAlertDialog(this, AlertType.ERROR, message.toString())
					.show();
		} else if (!App.isOfflineMode()
				&& !serverService.checkInternetConnection()) {
			AlertDialog alertDialog = App.getAlertDialog(
					DiabetesSpirometryActivity.this, AlertType.ERROR,
					getResources().getString(R.string.data_connection_error));
			alertDialog
					.setTitle(getResources().getString(R.string.error_title));
			alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
					new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// finish ();
							return;
						}
					});
			alertDialog.show();
		} else {
			AsyncTask<String, String, String> updateTask = new AsyncTask<String, String, String>() {
				OpenMrsObject location = null;

				@Override
				protected String doInBackground(String... params) {
					try {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								loading.setIndeterminate(true);
								loading.setCancelable(false);
								loading.show();
							}
						});
						publishProgress("Searching...");
						location = serverService.getLocation(App
								.get(locationId));
						if (location == null)
							return "FAIL";
					} catch (Exception e) {
						Log.e(TAG, e.getMessage());
						// App.getAlertDialog (DailyVisitActivity.this,
						// AlertType.ERROR, getResources ().getString
						// (R.string.item_not_found)).show ();
					}
					return "SUCCESS";
				}

				@Override
				protected void onProgressUpdate(String... values) {
					loading.setMessage(values[0]);
				};

				@Override
				protected void onPostExecute(String result) {
					super.onPostExecute(result);
					// perform all UI changes here
					loading.dismiss();
					locationName.setText("");
					if (!result.equals("SUCCESS")) {
						App.getAlertDialog(
								DiabetesSpirometryActivity.this,
								AlertType.ERROR,
								getResources().getString(
										R.string.item_not_found)).show();
					} else {
						String message = "Valid location";
						App.getAlertDialog(DiabetesSpirometryActivity.this,
								AlertType.INFO, message).show();
						if (location != null) {
							locationName.setText(location.getName());
						}
					}
					// loading.dismiss ();
				}
			};
			updateTask.execute("");
		}
	}
}
