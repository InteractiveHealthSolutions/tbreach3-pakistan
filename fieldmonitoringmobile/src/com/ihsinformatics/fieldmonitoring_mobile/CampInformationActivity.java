/* Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.

Contributors: Tahira Niazi */
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
import com.ihsinformatics.fieldmonitoring_mobile.model.OpenMrsObject;
import com.ihsinformatics.fieldmonitoring_mobile.shared.AlertType;
import com.ihsinformatics.fieldmonitoring_mobile.shared.FormType;
import com.ihsinformatics.fieldmonitoring_mobile.util.RegexUtil;

/**
 * @author tahira.niazi@ihsinformatics.com
 */
public class CampInformationActivity extends AbstractFragmentActivity implements
		OnEditorActionListener {
	private static final String formName = "CAMP_INFO";

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

	MyTextView staffMemberNamesTextView;
	MyEditText staffMemberNames;

	MyTextView startTimeTextView;
	MyButton startTimeButton;

	MyTextView endTimeTextView;
	MyButton endTimeButton;

	MyTextView perCampExpenseForStaffTextView;
	MyEditText perCampExpenseForStaff;

	MyTextView perCampExpenseForOthersTextView;
	MyEditText perCampExpenseForOthers;

	MyTextView numberXrayRecommendedTextView;
	MyEditText numberXrayRecommended;

	/**
	 * Subclass representing Fragment for adult-screeening suspect form
	 * 
	 * @author owais.hussain@ihsinformatics.com
	 * 
	 */
	@SuppressLint("ValidFragment")
	class CampInformationFragment extends Fragment {
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
	class CampInformationFragmentPagerAdapter extends FragmentPagerAdapter {
		/** Constructor of the class */
		public CampInformationFragmentPagerAdapter(
				FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		/** This method will be invoked when a page is requested to create */
		@Override
		public Fragment getItem(int arg0) {
			CampInformationFragment fragment = new CampInformationFragment();
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
		TAG = "CampInformationActivity";
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
		CampInformationFragmentPagerAdapter pagerAdapter = new CampInformationFragmentPagerAdapter(
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

		townTextView = new MyTextView(context, R.style.text,
				R.string.field_monitoring_town);
		town = new MySpinner(context, getResources().getStringArray(
				R.array.field_monitoring_towns),
				R.string.field_monitoring_town, R.string.option_hint);

		capacityTextView = new MyTextView(context, R.style.text,
				R.string.location_capacity);
		capacity = new MyEditText(context, R.string.capacity,
				R.string.capacity_hint, InputType.TYPE_CLASS_NUMBER,
				R.style.edit, 4, false);

		staffMemberNamesTextView = new MyTextView(context, R.style.text,
				R.string.staff_member_names);
		staffMemberNames = new MyEditText(context, R.string.staff_member_names,
				R.string.staff_member_names_hint, InputType.TYPE_CLASS_TEXT,
				R.style.edit, 50, true);

		startTimeTextView = new MyTextView(context, R.style.text,
				R.string.camp_start_time);
		startTimeButton = new MyButton(context, R.style.button,
				R.drawable.custom_button_beige, R.string.camp_start_time,
				R.string.camp_start_time);

		endTimeTextView = new MyTextView(context, R.style.text,
				R.string.camp_end_time);
		endTimeButton = new MyButton(context, R.style.button,
				R.drawable.custom_button_beige, R.string.camp_end_time,
				R.string.camp_end_time);

		perCampExpenseForStaffTextView = new MyTextView(context, R.style.text,
				R.string.per_camp_expense_staff);
		perCampExpenseForStaff = new MyEditText(context,
				R.string.per_camp_expense_staff,
				R.string.per_camp_expense_staff_hint,
				InputType.TYPE_CLASS_NUMBER, R.style.edit, 6, false);

		perCampExpenseForOthersTextView = new MyTextView(context, R.style.text,
				R.string.per_camp_expense_others);
		perCampExpenseForOthers = new MyEditText(context,
				R.string.per_camp_expense_others,
				R.string.per_camp_expense_others_hint,
				InputType.TYPE_CLASS_NUMBER, R.style.edit, 6, false);

		numberXrayRecommendedTextView = new MyTextView(context, R.style.text,
				R.string.number_xray_recommended);
		numberXrayRecommended = new MyEditText(context,
				R.string.number_xray_recommended,
				R.string.number_xray_recommended_hint,
				InputType.TYPE_CLASS_NUMBER, R.style.edit, 4, false);

		View[][] viewGroups = {
				{ formDateTextView, formDateViewOnly, /* formDateButton, */
				locationIdTextView, locationId, locationNameTextView,
						locationName, townTextView, town },
				{ capacityTextView, capacity, staffMemberNamesTextView,
						staffMemberNames, startTimeTextView, startTimeButton,
						endTimeTextView, endTimeButton },
				{ perCampExpenseForStaffTextView, perCampExpenseForStaff,
						perCampExpenseForOthersTextView,
						perCampExpenseForOthers, numberXrayRecommendedTextView,
						numberXrayRecommended } };

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
		startTimeButton.setOnClickListener(this);
		endTimeButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
		saveButton.setOnClickListener(this);
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
				staffMemberNames, perCampExpenseForStaff,
				perCampExpenseForOthers, numberXrayRecommended };

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
		// String completeLocation = App.getLocation();
		// String locationInitialId = completeLocation.split("\\s+")[0];
		// locationId.setText(locationInitialId);
		// locationId.setEnabled(false);
		// locationName.setText(App.getLocation());
		locationName.setEnabled(false);
		formDateViewOnly.setTextColor(getResources().getColor(
				R.color.DeepSkyBlue));
		updateDisplay();
	}

	@SuppressWarnings("deprecation")
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
	}

	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateDisplay() {
		formDateButton.setText(DateFormat.format("dd-MMM-yyyy", formDate));
		startTimeButton.setText(DateFormat.format("hh:mm aa", startTime));
		endTimeButton.setText(DateFormat.format("hh:mm aa", endTime));
		formDateViewOnly.setText(DateFormat.format("dd-MMM-yyyy", formDate));
	}

	@Override
	public boolean validate() {
		boolean valid = true;
		StringBuffer message = new StringBuffer();
		// Validate mandatory controls
		View[] mandatory = { locationId, locationName, capacity,
				staffMemberNames, perCampExpenseForStaff,
				perCampExpenseForOthers, numberXrayRecommended };
		for (View v : mandatory) {
			if (App.get(v).equals("")) {
				valid = false;
				message.append(v.getTag().toString() + ". ");
				((EditText) v).setHintTextColor(getResources().getColor(
						R.color.Red));
			}
		}

		if (!valid) {
			message.append(getResources().getString(R.string.empty_data) + "\n");
		}

		// Validate data
		if (valid) {
			if (!RegexUtil.isLocationId(App.get(locationId))) {
				valid = false;
				message.append(locationId.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				locationId.setTextColor(getResources().getColor(R.color.Red));
			}

			if (!RegexUtil.isNumeric(App.get(capacity), false)) {
				valid = false;
				message.append(capacity.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				capacity.setTextColor(getResources().getColor(R.color.Red));
			}

			if (App.get(staffMemberNames).length() < 3) {
				valid = false;
				message.append(staffMemberNames.getTag().toString()
						+ ": Min 3 characters should be entered." + "\n");
				staffMemberNames.setTextColor(getResources().getColor(
						R.color.Red));
			} else {
				if (!RegexUtil.isWord(App.get(staffMemberNames))) {
					valid = false;
					message.append(staffMemberNames.getTag().toString() + ": "
							+ getResources().getString(R.string.invalid_data)
							+ "\n");
					staffMemberNames.setTextColor(getResources().getColor(
							R.color.Red));
				}
			}

			if (!RegexUtil.isNumeric(App.get(perCampExpenseForStaff), false)) {
				valid = false;
				message.append(perCampExpenseForStaff.getTag().toString()
						+ ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				perCampExpenseForStaff.setTextColor(getResources().getColor(
						R.color.Red));
			}
			if (!RegexUtil.isNumeric(App.get(perCampExpenseForOthers), false)) {
				valid = false;
				message.append(perCampExpenseForOthers.getTag().toString()
						+ ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				perCampExpenseForOthers.setTextColor(getResources().getColor(
						R.color.Red));
			}
			if (!RegexUtil.isNumeric(App.get(numberXrayRecommended), false)) {
				valid = false;
				message.append(numberXrayRecommended.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				numberXrayRecommended.setTextColor(getResources().getColor(
						R.color.Red));
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
			observations.add(new String[] { "TOWN", App.get(town) });
			observations.add(new String[] { "LOCATION_CAPACITY",
					App.get(capacity) });
			observations.add(new String[] { "STAFF_MEMBER_NAMES",
					App.get(staffMemberNames) });
			observations.add(new String[] { "CAMP_START_TIME",
					App.getSqlDateTime(startTime).split("\\s+")[1] });
			observations.add(new String[] { "CAMP_END_TIME",
					App.getSqlDateTime(endTime).split("\\s+")[1] });
			observations.add(new String[] { "CAMP_EXPENSE_STAFF",
					App.get(perCampExpenseForStaff) });
			observations.add(new String[] { "CAMP_EXPENSE_OTHERS",
					App.get(perCampExpenseForOthers) });
			observations.add(new String[] { "XRAY_TOTAL_NO",
					App.get(numberXrayRecommended) });

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
					result = serverService.saveVisits(FormType.CAMP_INFO,
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
						App.getAlertDialog(CampInformationActivity.this,
								AlertType.INFO,
								getResources().getString(R.string.inserted))
								.show();
						initView(views);
					} else {
						App.getAlertDialog(CampInformationActivity.this,
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
					CampInformationActivity.this, AlertType.ERROR,
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
								CampInformationActivity.this,
								AlertType.ERROR,
								getResources().getString(
										R.string.item_not_found)).show();
					} else {
						String message = "Valid location";
						App.getAlertDialog(CampInformationActivity.this,
								AlertType.INFO, message).show();
						if (location != null) {
							locationName.setText(location.getName()
									.split("\\+")[0]);

							// locationName.setText(location.getName());
							String town = location.getName().split("\\+")[1];
							if (!town.equals("none")) {
								ArrayAdapter townAdap = (ArrayAdapter) CampInformationActivity.this.town
										.getAdapter(); // cast to an
														// ArrayAdapter
								int spinnerPosition = townAdap
										.getPosition(town);
								// set the default according to value
								CampInformationActivity.this.town
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
