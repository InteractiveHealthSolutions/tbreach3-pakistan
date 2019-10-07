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
package com.ihsinformatics.fieldmonitoring_mobile;

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

import com.ihsinformatics.fieldmonitoring_mobile.custom.MyButton;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyEditText;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MySpinner;
import com.ihsinformatics.fieldmonitoring_mobile.custom.MyTextView;
import com.ihsinformatics.fieldmonitoring_mobile.model.OpenMrsObject;
import com.ihsinformatics.fieldmonitoring_mobile.shared.AlertType;
import com.ihsinformatics.fieldmonitoring_mobile.shared.FormType;
import com.ihsinformatics.fieldmonitoring_mobile.util.DatabaseUtil;
import com.ihsinformatics.fieldmonitoring_mobile.util.RegexUtil;

/**
 * @author tahira.niazi@ihsinformatics.com
 */
public class FirstVisit extends AbstractFragmentActivity {

	private static final String formName = "FIRST_VIS";

	private static DatabaseUtil dbUtil;

	MyTextView formDateTextView;
	MyButton formDateButton;
	MyTextView formDateViewOnly;

	MyTextView locationIdTextView;
	MyEditText locationId;

	MyTextView locationNameTextView;
	MyEditText locationName;

	MyTextView townTextView;
	MySpinner town;

	MyTextView capacityTextView;
	MyEditText capacity;

	MyTextView specialityTextView;
	MyEditText speciality;

	MyTextView startTimeTextView;
	MyButton startTimeButton;

	MyTextView endTimeTextView;
	MyButton endTimeButton;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ihsinformatics.fieldmonitoring_mobile.AbstractFragmentActivity#
	 * createViews(android.content.Context)
	 */
	@Override
	public void createViews(Context context) {
		dbUtil = new DatabaseUtil(context);
		TAG = "FirstVisitActivity";
		PAGE_COUNT = 2;
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

		townTextView = new MyTextView(context, R.style.text,
				R.string.field_monitoring_town);
		town = new MySpinner(context, getResources().getStringArray(
				R.array.field_monitoring_towns),
				R.string.field_monitoring_town, R.string.option_hint);

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
						locationName, townTextView, town, },
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

		views = new View[] { locationId, locationName, town, capacity,
				speciality };
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
		startTime = Calendar.getInstance();
		endTime = Calendar.getInstance();
		// visitDate = Calendar.getInstance();
		// startTime = Calendar.getInstance();
		// endTime = Calendar.getInstance();
		// String completeLocation = App.getLocation();
		// String locationInitialId = completeLocation.split("\\s+")[0];
		// locationId.setText(locationInitialId);
		// TODO: check this
		// locationId.setEnabled(false);
		// locationName.setText(App.getLocation());
		locationName.setEnabled(false);
		// metDrYes.setChecked(true);
		// givenCouponsYes.setChecked(true);
		// marketingActivityYes.setChecked(true);
		formDateViewOnly.setTextColor(getResources().getColor(
				R.color.DeepSkyBlue));
		updateDisplay();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged
	 * (android.widget.CompoundButton, boolean)
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnLongClickListener#onLongClick(android.view.View)
	 */
	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ihsinformatics.fieldmonitoring_mobile.AbstractFragmentActivity#
	 * updateDisplay()
	 */
	@Override
	public void updateDisplay() {
		formDateButton.setText(DateFormat.format("dd-MMM-yyyy", formDate));
		formDateViewOnly.setText(DateFormat.format("dd-MMM-yyyy", formDate));

		startTimeButton.setText(DateFormat.format("hh:mm aa", startTime));
		endTimeButton.setText(DateFormat.format("hh:mm aa", endTime));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ihsinformatics.fieldmonitoring_mobile.AbstractFragmentActivity#validate
	 * ()
	 */
	@Override
	public boolean validate() {
		boolean valid = true;
		StringBuffer message = new StringBuffer();
		// Validate mandatory controls
		View[] mandatory = { locationId, locationName, capacity, speciality };
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ihsinformatics.fieldmonitoring_mobile.AbstractFragmentActivity#submit
	 * ()
	 */
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

			// values.put ("dob", App.getSqlDate (dob));
			final ArrayList<String[]> observations = new ArrayList<String[]>();

			observations.add(new String[] { "F_DATE",
					App.getSqlDateTime(formDate) });
			observations
					.add(new String[] { "LOCATION_ID", App.get(locationId) });
			observations.add(new String[] { "LOCATION_NAME",
					App.get(locationName) });
			observations.add(new String[] { "TOWN", App.get(town) });
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
						App.getAlertDialog(FirstVisit.this, AlertType.INFO,
								getResources().getString(R.string.inserted))
								.show();
						initView(views);
					} else {
						App.getAlertDialog(FirstVisit.this, AlertType.ERROR,
								result).show();
						initView(views);
					}
				}
			};
			updateTask.execute("");
		}
		return true;
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
			AlertDialog alertDialog = App.getAlertDialog(FirstVisit.this,
					AlertType.ERROR,
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
								FirstVisit.this,
								AlertType.ERROR,
								getResources().getString(
										R.string.item_not_found)).show();
					} else {
						String message = "Valid location";
						App.getAlertDialog(FirstVisit.this, AlertType.INFO,
								message).show();
						if (location != null) {
							locationName.setText(location.getName()
									.split("\\+")[0]);

							// locationName.setText(location.getName());
							String town = location.getName().split("\\+")[1];
							if (!town.equals("none")) {
								ArrayAdapter townAdap = (ArrayAdapter) FirstVisit.this.town
										.getAdapter(); // cast to an
														// ArrayAdapter
								int spinnerPosition = townAdap
										.getPosition(town);
								// set the default according to value
								FirstVisit.this.town
										.setSelection(spinnerPosition);
							}

						}
					}
					// loading.dismiss ();
				}
			};
			updateTask.execute("");
		}
	}
}
