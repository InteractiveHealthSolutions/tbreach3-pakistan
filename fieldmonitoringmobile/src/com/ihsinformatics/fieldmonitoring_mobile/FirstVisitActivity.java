/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.

Contributors: Tahira Niazi */
package com.ihsinformatics.fieldmonitoring_mobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.ihsinformatics.fieldmonitoring_mobile.custom.MyButton;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyEditText;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MySpinner;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyTextView;
import com.ihsinformatics.fieldmonitoring_mobile.shared.AlertType;
import com.ihsinformatics.fieldmonitoring_mobile.shared.FormType;
import com.ihsinformatics.fieldmonitoring_mobile.shared.Metadata;
import com.ihsinformatics.fieldmonitoring_mobile.util.DatabaseUtil;
import com.ihsinformatics.fieldmonitoring_mobile.util.RegexUtil;

/**
 * @author tahira.niazi@ihsinformatics.com
 */
public class FirstVisitActivity extends AbstractFragmentActivity implements
		OnEditorActionListener {
	private static final String formName = "FIRST_VIS";

	private static DatabaseUtil dbUtil;

	MyTextView formDateTextView;
	MyButton formDateButton;
	MyTextView formDateViewOnly;

	MyTextView locationIdTextView;
	MyEditText locationId;

	MyTextView locationNameTextView;
	MyEditText locationName;

	MyTextView locationTypeTextView;
	MySpinner locationType;

	MyTextView address1TextView;
	MyEditText address1;

	MyTextView address2TextView;
	MyEditText address2;

	MyTextView townTextView;
	MySpinner town;

	MyTextView mobileNumberTextView;
	MyEditText mobileNumber;

	MyTextView capacityTextView;
	MyEditText capacity;

	MyTextView specialityTextView;
	MyEditText speciality;

	MyTextView startTimeTextView;
	MyButton startTimeButton;

	MyTextView endTimeTextView;
	MyButton endTimeButton;

	/**
	 * Subclass representing Fragment for first visit form
	 * 
	 * @author owais.hussain@ihsinformatics.com
	 * 
	 */
	@SuppressLint("ValidFragment")
	class FirstVisitFragment extends Fragment {
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
	class FirstVisitFragmentPagerAdapter extends FragmentPagerAdapter {
		/** Constructor of the class */
		public FirstVisitFragmentPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		/** This method will be invoked when a page is requested to create */
		@Override
		public Fragment getItem(int arg0) {
			FirstVisitFragment fragment = new FirstVisitFragment();
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
		dbUtil = new DatabaseUtil(context);
		TAG = "FirstVisitActivity";
		PAGE_COUNT = 3;
		pager = (ViewPager) findViewById(R.template_id.pager);
		navigationSeekbar.setMax(PAGE_COUNT - 1);
		navigatorLayout = (LinearLayout) findViewById(R.template_id.navigatorLayout);
		// If the form consists only of single page, then hide the
		// navigatorLayout
		if (PAGE_COUNT < 2) {
			navigatorLayout.setVisibility(View.GONE);
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FirstVisitFragmentPagerAdapter pagerAdapter = new FirstVisitFragmentPagerAdapter(
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
				R.string.location_name_hint, InputType.TYPE_CLASS_TEXT,
				R.style.edit, 50, false);

		locationTypeTextView = new MyTextView(context, R.style.text,
				R.string.location_type);
		locationType = new MySpinner(context, getResources().getStringArray(
				R.array.location_types), R.string.location_type,
				R.string.option_hint);

		address1TextView = new MyTextView(context, R.style.text,
				R.string.address1);
		address1 = new MyEditText(context, R.string.address1,
				R.string.address1_hint, InputType.TYPE_TEXT_FLAG_MULTI_LINE,
				R.style.edit, 150, true);
		address1.setMinLines(4);
		address1.setMaxHeight(8);

		address2TextView = new MyTextView(context, R.style.text,
				R.string.address2);
		address2 = new MyEditText(context, R.string.address2,
				R.string.address1_hint, InputType.TYPE_TEXT_FLAG_MULTI_LINE,
				R.style.edit, 150, true);
		address2.setMinLines(4);
		address2.setMaxHeight(8);

		townTextView = new MyTextView(context, R.style.text,
				R.string.field_monitoring_town);
		town = new MySpinner(context, getResources().getStringArray(
				R.array.field_monitoring_towns),
				R.string.field_monitoring_town, R.string.option_hint);

		mobileNumberTextView = new MyTextView(context, R.style.text,
				R.string.mobile_number);
		mobileNumber = new MyEditText(context, R.string.mobile_number,
				R.string.mobile_number_hint, InputType.TYPE_CLASS_TEXT,
				R.style.edit, 12, false);

		capacityTextView = new MyTextView(context, R.style.text,
				R.string.capacity);
		capacity = new MyEditText(context, R.string.capacity,
				R.string.capacity_hint, InputType.TYPE_CLASS_NUMBER,
				R.style.edit, 3, false);

		specialityTextView = new MyTextView(context, R.style.text,
				R.string.speciality);
		speciality = new MyEditText(context, R.string.speciality,
				R.string.speciality_hint, InputType.TYPE_CLASS_TEXT,
				R.style.edit, 20, false);

		startTimeTextView = new MyTextView(context, R.style.text,
				R.string.start_time);
		startTimeButton = new MyButton(context, R.style.button,
				R.drawable.custom_button_beige, R.string.start_time,
				R.string.start_time);

		endTimeTextView = new MyTextView(context, R.style.text,
				R.string.end_time);
		endTimeButton = new MyButton(context, R.style.button,
				R.drawable.custom_button_beige, R.string.end_time,
				R.string.end_time);

		View[][] viewGroups = {
				{ formDateTextView, formDateViewOnly, /* formDateButton, */
				locationIdTextView, locationId, locationNameTextView,
						locationName, locationTypeTextView, locationType },
				{ address1TextView, address1, address2TextView, address2,
						townTextView, town, mobileNumberTextView, mobileNumber },
				{ capacityTextView, capacity, specialityTextView, speciality,
						startTimeTextView, startTimeButton, endTimeTextView,
						endTimeButton } };

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
		formDateButton.setOnClickListener(this);
		startTimeButton.setOnClickListener(this);
		endTimeButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
		firstButton.setOnClickListener(this);
		navigationSeekbar.setOnSeekBarChangeListener(this);

		views = new View[] { locationId, locationName, locationType, address1,
				address2, town, mobileNumber, capacity, speciality };
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
		formDateViewOnly.setTextColor(getResources().getColor(
				R.color.DeepSkyBlue));

		SimpleDateFormat sdfFormDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		// SimpleDateFormat sdfVisitTime = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		super.initView(views);
		// formDate = Calendar.getInstance();
		// startTime = Calendar.getInstance();
		// endTime = Calendar.getInstance();
		updateDisplay();

		String receivedFormDate = "";
		String locationId = "";
		String addressLine1 = "";
		String addressLine2 = "";
		String locationName = "";
		String locationType = "";
		String town = "";
		String mobileNumber = "";
		String capacity = "";
		String speciality = "";
		String receivedStartTime = "";
		String receivedEndTime = "";
		String receivedStartDatetime = "";
		String receivedEndDatetime = "";

		Intent intent = getIntent();

		if (intent.getExtras() != null) {
			receivedFormDate = intent.getStringExtra("formDate");

			locationId = intent.getStringExtra("locationId");
			locationName = intent.getStringExtra("locationName")
					.split("\\s", 2)[1];
			locationType = intent.getStringExtra("locationType");
			addressLine1 = intent.getStringExtra("address1");
			addressLine2 = intent.getStringExtra("address2");
			town = intent.getStringExtra("town");
			mobileNumber = intent.getStringExtra("mobileNumber");
			capacity = intent.getStringExtra("capacity");
			speciality = intent.getStringExtra("speciality");
			receivedStartTime = intent.getStringExtra("startTime");
			receivedEndTime = intent.getStringExtra("endTime");
			receivedStartDatetime = receivedFormDate.split("\\s+")[0] + " "
					+ receivedStartTime;
			receivedEndDatetime = receivedFormDate.split("\\s+")[0] + " "
					+ receivedEndTime;

			try {
				Date intentFormDate = sdfFormDate.parse(receivedFormDate);
				formDate.setTime(intentFormDate);
				formDateButton.setText(DateFormat.format("dd-MMM-yyyy",
						formDate));
				formDateViewOnly.setText(DateFormat.format("dd-MMM-yyyy",
						formDate));

				Date intentStartTime = sdfFormDate.parse(receivedStartDatetime);
				startTime.setTime(intentStartTime);
				startTimeButton.setText(DateFormat
						.format("hh:mm aa", startTime));

				Date intentEndTime = sdfFormDate.parse(receivedEndDatetime);
				endTime.setTime(intentEndTime);
				endTimeButton.setText(DateFormat.format("hh:mm aa", endTime));

				this.locationId.setText(locationId);
				this.locationName.setText(locationName);
				// set location type
				ArrayAdapter locationTypeAdapter = (ArrayAdapter) this.locationType
						.getAdapter(); // cast to an ArrayAdapter
				int locTypepinnerPosition = locationTypeAdapter
						.getPosition(locationType);
				// set the default according to value
				this.locationType.setSelection(locTypepinnerPosition);

				this.address1.setText(addressLine1);
				this.address2.setText(addressLine2);
				// set town
				ArrayAdapter townAdapter = (ArrayAdapter) this.town
						.getAdapter(); // cast to an ArrayAdapter
				int townSpinnerPosition = townAdapter.getPosition(town);
				// set the default according to value
				this.town.setSelection(townSpinnerPosition);
				this.mobileNumber.setText((mobileNumber.equals("")) ? ""
						: mobileNumber);

				this.capacity.setText(capacity);
				this.speciality.setText(speciality);

				// getIntent().removeExtra("locationId");
				// getIntent().removeExtra("locationName");
				// getIntent().removeExtra("locationType");
				// getIntent().removeExtra("address1");
				// getIntent().removeExtra("address2");
				// getIntent().removeExtra("town");
				// getIntent().removeExtra("mobileNumber");
				// getIntent().removeExtra("capacity");
				// getIntent().removeExtra("speciality");
				// getIntent().removeExtra("startTime");
				// getIntent().removeExtra("endTime");

				Bundle nullExtra = null;
				getIntent().replaceExtras(nullExtra);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (view == firstButton) {
			gotoFirstPage();
		}
		// else if (view == formDateButton)
		// {
		// showDialog(DATE_DIALOG_ID);
		// }
		else if (view == startTimeButton) {
			showDialog(START_TIME_DIALOG_ID);
		} else if (view == endTimeButton) {
			showDialog(END_TIME_DIALOG_ID);
		} else if (view == firstButton) {
			gotoFirstPage();
		} else if (view == lastButton) {
			gotoLastPage();
		} else if (view == clearButton) {
			initView(views);
		} else if (view == saveButton) {
			submit();
		}
		// else if(view == testButton)
		// {
		// String time = App.getSqlDate(formDate);
		// formDateTextView.setText(time);
		// visitDateTextView.setText(App.getSqlDate(visitDate));
		// startTimeTextView.setText(App.getSqlDateTime(startTime));
		// endTimeTextView.setText(App.getSqlDateTime(endTime));
		// }
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
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

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
		// Calendar now = Calendar.getInstance();
		// int currentYear = now.get(Calendar.YEAR);
		// int currentMonth = now.get(Calendar.MONTH);
		// int currentDay = now.get(Calendar.DAY_OF_MONTH);

		startTimeButton.setText(DateFormat.format("hh:mm aa", startTime));
		endTimeButton.setText(DateFormat.format("hh:mm aa", endTime));
	}

	@Override
	public boolean validate() {

		boolean valid = true;
		StringBuffer message = new StringBuffer();
		// Validate mandatory controls
		View[] mandatory = { locationId, locationName, capacity, speciality,
				address1, mobileNumber };
		for (View v : mandatory) {
			if (App.get(v).equals("")) {
				valid = false;
				message.append(v.getTag().toString() + ". ");
				((EditText) v).setHintTextColor(getResources().getColor(
						R.color.Red));
			}
		}

		// if(marketingActivityYes.isChecked() &&
		// App.get(mrMarketingBudgetItems).equals(""))
		// {
		// valid = false;
		// message.append(mrMarketingBudgetItems.getTag().toString() + ". ");
		// mrMarketingBudgetItems.setHintTextColor(getResources().getColor(R.color.Red));
		// }

		if (!valid) {
			message.append(getResources().getString(R.string.empty_data) + "\n");
		}

		// validate ranges and regex

		// Validate data
		if (valid) {
			if (!RegexUtil.isSentence(App.get(locationName))) {
				valid = false;
				message.append(locationName.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				locationName.setTextColor(getResources().getColor(R.color.Red));
			}

			if (!App.get(mobileNumber).equals("")
					&& !RegexUtil.isContactNumber(App.get(mobileNumber))) {
				valid = false;
				message.append(mobileNumber.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				mobileNumber.setTextColor(getResources().getColor(R.color.Red));
			}
			if (!RegexUtil.isNumeric(App.get(capacity), false)) {
				valid = false;
				message.append(capacity.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				capacity.setTextColor(getResources().getColor(R.color.Red));
			}
			if (!RegexUtil.isWord(App.get(speciality))) {
				valid = false;
				message.append(speciality.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				speciality.setTextColor(getResources().getColor(R.color.Red));
			}
			if (!RegexUtil.isLocationId(App.get(locationId))) {
				valid = false;
				message.append(locationId.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				locationId.setTextColor(getResources().getColor(R.color.Red));
			}
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
		} catch (NumberFormatException e) {
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
			values.put("locationId", App.get(locationId));
			final String completeLocationName = App.get(locationId) + " "
					+ App.get(locationName);
			values.put("formName", formName);
			String completeAddress = null;
			String addr1 = App.get(address1).equals("") ? " " : App
					.get(address1);
			String addr2 = App.get(address2).equals("") ? " " : App
					.get(address2);
			completeAddress = addr1 + " \n" + addr2;
			// completeAddress.append(App.get(address1));
			// completeAddress.append(System.getProperty("line.separator"));
			// completeAddress.append(App.get(address2));

			// values.put ("dob", App.getSqlDate (dob));
			final ArrayList<String[]> observations = new ArrayList<String[]>();

			observations.add(new String[] { "F_DATE",
					App.getSqlDateTime(formDate) });
			observations
					.add(new String[] { "LOCATION_ID", App.get(locationId) });
			observations.add(new String[] { "LOCATION_NAME",
					completeLocationName });
			observations.add(new String[] { "LOCATION_TYPE",
					App.get(locationType) });
			observations.add(new String[] { "ADDRESS",
					completeAddress.toString() });
			// check if address 1 and address2 are not empty
			observations.add(new String[] { "TOWN", App.get(town) });
			String mobile = App.get(mobileNumber);
			observations.add(new String[] {
					"MOBILE_NO",
					App.get(mobileNumber).equals("") ? "" : App
							.get(mobileNumber) });
			observations.add(new String[] { "CAPACITY", App.get(capacity) });
			observations
					.add(new String[] { "SPECIALITY", App.get(speciality) });
			observations.add(new String[] { "START_TIME",
					App.getSqlDateTime(startTime).split("\\s+")[1] });
			observations.add(new String[] { "END_TIME",
					App.getSqlDateTime(endTime).split("\\s+")[1] });

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
					result = serverService.saveVisits(FormType.FIRST_VISIT,
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
						ContentValues values = new ContentValues();
						// String locname = locationObj.getString("name");
						// If location is found, then save it into local DB
						values.put("id", App.get(locationId));
						values.put("type", Metadata.LOCATION);
						values.put("name", completeLocationName);
						dbUtil.insert(Metadata.METADATA_TABLE, values);

						App.getAlertDialog(FirstVisitActivity.this,
								AlertType.INFO,
								getResources().getString(R.string.inserted))
								.show();
						initView(views);
					} else if (result.contains("DUPLICATE")) {
						App.getAlertDialog(FirstVisitActivity.this,
								AlertType.ERROR, result).show();
						locationId.setTextColor(getResources().getColor(
								R.color.Red));
					} else {
						App.getAlertDialog(FirstVisitActivity.this,
								AlertType.ERROR, result).show();
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

}
