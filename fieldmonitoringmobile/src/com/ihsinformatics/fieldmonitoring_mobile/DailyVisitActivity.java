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
public class DailyVisitActivity extends AbstractFragmentActivity implements
		OnEditorActionListener {
	private static final String formName = "DAILY_VIS";

	MyTextView formDateTextView;
	MyButton formDateButton;
	MyTextView formDateViewOnly;

	MyTextView locationIdTextView;
	MyEditText locationId;

	MyTextView locationNameTextView;
	MyEditText locationName;

	MyTextView townTextView;
	MySpinner town;

	MyTextView visitDateTextView;
	MyButton visitDateButton;

	MyTextView arrivalTimeTextView;
	MyButton arrivalTimeButton;

	MyTextView departureTimeTextView;
	MyButton departureTimeButton;

	MyTextView metDrTextView;
	MyRadioGroup metDrRadioGroup;
	MyRadioButton metDrYes;
	MyRadioButton metDrNo;

	MyTextView givenCouponsTextView;
	MyRadioGroup givenCouponsRadioGroup;
	MyRadioButton givenCouponsYes;
	MyRadioButton givenCouponsNo;

	MyTextView marketingActivityTextView;
	MyRadioGroup marketingActivityRadioGroup;
	MyRadioButton marketingActivityYes;
	MyRadioButton marketingActivityNo;

	MyTextView marketingActivityDescriptionTextView;
	MyEditText marketingActivityDescription;

	MyTextView mrMarketingBudgetItemsTextView;
	MyEditText mrMarketingBudgetItems;

	MyTextView amountTextView;
	MyEditText amount;

	MyTextView institutionalMarketingItemsTextView;

	MyCheckBox posters;
	MyEditText postersAmount;

	MyCheckBox brochures;
	MyEditText brochuresAmount;

	MyCheckBox rxPads;
	MyEditText rxPadsAmount;

	MyCheckBox others;
	MyEditText othersAmount;

	MyTextView drPotentialTextView;
	MySpinner drPotential;

	MyTextView commentsTextView;
	MyEditText comments;

	/**
	 * Subclass representing Fragment for adult-screeening suspect form
	 * 
	 * @author owais.hussain@ihsinformatics.com
	 * 
	 */
	@SuppressLint("ValidFragment")
	class DailyVisitFragment extends Fragment {
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
	class DailyVisitFragmentPagerAdapter extends FragmentPagerAdapter {
		/** Constructor of the class */
		public DailyVisitFragmentPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		/** This method will be invoked when a page is requested to create */
		@Override
		public Fragment getItem(int arg0) {
			DailyVisitFragment fragment = new DailyVisitFragment();
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
		TAG = "DailyVisitActivity";
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
		DailyVisitFragmentPagerAdapter pagerAdapter = new DailyVisitFragmentPagerAdapter(
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

		visitDateTextView = new MyTextView(context, R.style.text,
				R.string.visit_date);
		visitDateButton = new MyButton(context, R.style.button,
				R.drawable.custom_button_beige, R.string.visit_date,
				R.string.visit_date);

		arrivalTimeTextView = new MyTextView(context, R.style.text,
				R.string.arrival_time);
		arrivalTimeButton = new MyButton(context, R.style.button,
				R.drawable.custom_button_beige, R.string.arrival_time,
				R.string.arrival_time);

		departureTimeTextView = new MyTextView(context, R.style.text,
				R.string.departure_time);
		departureTimeButton = new MyButton(context, R.style.button,
				R.drawable.custom_button_beige, R.string.departure_time,
				R.string.departure_time);

		metDrTextView = new MyTextView(context, R.style.text, R.string.met_dr);
		metDrYes = new MyRadioButton(context, R.string.yes, R.style.radio,
				R.string.yes);
		metDrNo = new MyRadioButton(context, R.string.no, R.style.radio,
				R.string.no);
		metDrRadioGroup = new MyRadioGroup(context, new MyRadioButton[] {
				metDrYes, metDrNo }, R.string.met_dr, R.style.radio,
				App.isLanguageRTL(), 0);

		givenCouponsTextView = new MyTextView(context, R.style.text,
				R.string.given_coupons);
		givenCouponsYes = new MyRadioButton(context, R.string.yes,
				R.style.radio, R.string.yes);
		givenCouponsNo = new MyRadioButton(context, R.string.no, R.style.radio,
				R.string.no);
		givenCouponsRadioGroup = new MyRadioGroup(context, new MyRadioButton[] {
				givenCouponsYes, givenCouponsNo }, R.string.given_coupons,
				R.style.radio, App.isLanguageRTL(), 0);

		marketingActivityTextView = new MyTextView(context, R.style.text,
				R.string.marketing_activity);
		marketingActivityYes = new MyRadioButton(context, R.string.yes,
				R.style.radio, R.string.yes);
		marketingActivityNo = new MyRadioButton(context, R.string.no,
				R.style.radio, R.string.no);
		marketingActivityRadioGroup = new MyRadioGroup(
				context,
				new MyRadioButton[] { marketingActivityYes, marketingActivityNo },
				R.string.marketing_activity, R.style.radio,
				App.isLanguageRTL(), 0);

		marketingActivityDescriptionTextView = new MyTextView(context,
				R.style.text, R.string.marketing_activity_description);
		marketingActivityDescription = new MyEditText(context,
				R.string.marketing_activity_description,
				R.string.marketing_activity_description_hint,
				InputType.TYPE_CLASS_TEXT, R.style.edit, 50, true);

		mrMarketingBudgetItemsTextView = new MyTextView(context, R.style.text,
				R.string.mr_marketing_budget_items);
		mrMarketingBudgetItems = new MyEditText(context,
				R.string.mr_marketing_budget_items,
				R.string.mr_marketing_budget_items_hint,
				InputType.TYPE_CLASS_TEXT, R.style.edit, 30, true);

		amountTextView = new MyTextView(context, R.style.text, R.string.amount);
		amount = new MyEditText(context, R.string.amount, R.string.amount_hint,
				InputType.TYPE_CLASS_NUMBER, R.style.edit, 4, false);

		institutionalMarketingItemsTextView = new MyTextView(context,
				R.style.text, R.string.institutional_marketing_items);

		posters = new MyCheckBox(context, R.string.posters, R.style.edit,
				R.string.posters, false);
		postersAmount = new MyEditText(context, R.string.posters,
				R.string.count_hint, InputType.TYPE_CLASS_NUMBER, R.style.edit,
				3, false);

		brochures = new MyCheckBox(context, R.string.brochures, R.style.edit,
				R.string.brochures, false);
		brochuresAmount = new MyEditText(context, R.string.brochures,
				R.string.count_hint, InputType.TYPE_CLASS_NUMBER, R.style.edit,
				3, false);

		rxPads = new MyCheckBox(context, R.string.rx_pads, R.style.edit,
				R.string.rx_pads, false);
		rxPadsAmount = new MyEditText(context, R.string.rx_pads,
				R.string.count_hint, InputType.TYPE_CLASS_NUMBER, R.style.edit,
				3, false);

		others = new MyCheckBox(context, R.string.others, R.style.edit,
				R.string.others, false);
		othersAmount = new MyEditText(context, R.string.others,
				R.string.count_hint, InputType.TYPE_CLASS_NUMBER, R.style.edit,
				3, false);

		drPotentialTextView = new MyTextView(context, R.style.text,
				R.string.dr_potential);
		drPotential = new MySpinner(context, getResources().getStringArray(
				R.array.dr_potentials), R.string.dr_potential,
				R.string.option_hint);

		commentsTextView = new MyTextView(context, R.style.text,
				R.string.comment);
		comments = new MyEditText(context, R.string.comment,
				R.string.comment_hint, InputType.TYPE_TEXT_FLAG_MULTI_LINE,
				R.style.edit, 100, true);
		comments.setMinLines(4);
		comments.setMaxHeight(8);

		View[][] viewGroups = {
				{ formDateTextView, /* formDateButton, */formDateViewOnly,
						locationIdTextView, locationId, locationNameTextView,
						locationName, townTextView, town, visitDateTextView,
						visitDateButton },
				{ arrivalTimeTextView, arrivalTimeButton,
						departureTimeTextView, departureTimeButton,
						metDrTextView, metDrRadioGroup, givenCouponsTextView,
						givenCouponsRadioGroup },
				{ marketingActivityTextView, marketingActivityRadioGroup,
						marketingActivityDescriptionTextView,
						marketingActivityDescription,
						mrMarketingBudgetItemsTextView, mrMarketingBudgetItems,
						amountTextView, amount },
				{ institutionalMarketingItemsTextView, posters, postersAmount,
						brochures, brochuresAmount, rxPads, rxPadsAmount,
						others, othersAmount },
				{ drPotentialTextView, drPotential, commentsTextView, comments } };

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
		visitDateButton.setOnClickListener(this);
		arrivalTimeButton.setOnClickListener(this);
		departureTimeButton.setOnClickListener(this);
		marketingActivityYes.setOnCheckedChangeListener(this);
		marketingActivityNo.setOnCheckedChangeListener(this);
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

		views = new View[] { locationId, locationName, town,
				marketingActivityDescription, mrMarketingBudgetItems, amount,
				posters, postersAmount, brochures, brochuresAmount, rxPads,
				rxPadsAmount, others, othersAmount, drPotential, comments };
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
		// String completeLocation = App.getLocation();
		// String locationInitialId = completeLocation.split("\\s+")[0];
		// locationId.setText(locationInitialId);
		// TODO: check this
		// locationId.setEnabled(false);
		// locationName.setText(App.getLocation());
		locationName.setEnabled(false);
		metDrYes.setChecked(true);
		givenCouponsYes.setChecked(true);
		marketingActivityYes.setChecked(true);
		formDateViewOnly.setTextColor(getResources().getColor(
				R.color.DeepSkyBlue));
		updateDisplay();
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
		if (view == visitDateButton) {
			showDialog(VISIT_DATE_DIALOG_ID);
		} else if (view == arrivalTimeButton) {
			showDialog(START_TIME_DIALOG_ID);
		} else if (view == departureTimeButton) {
			showDialog(END_TIME_DIALOG_ID);
		} else if (view == firstButton) {
			gotoFirstPage();
		} else if (view == lastButton) {
			gotoLastPage();
		} else if (view == clearButton) {
			initView(views);
		} else if (view == saveButton) {
			submit();
			// validate();
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
		if (button == posters) {
			boolean isPosters = posters.isChecked() ? true : false;
			postersAmount.setEnabled(isPosters);
		} else if (button == brochures) {
			boolean isBrochures = brochures.isChecked() ? true : false;
			brochuresAmount.setEnabled(isBrochures);
		} else if (button == rxPads) {
			boolean isRxPads = rxPads.isChecked() ? true : false;
			rxPadsAmount.setEnabled(isRxPads);
		} else if (button == others) {
			boolean isOthers = others.isChecked() ? true : false;
			othersAmount.setEnabled(isOthers);
		}

		if (state) {
			if (button == marketingActivityYes) {
				marketingActivityDescriptionTextView.setEnabled(state);
				marketingActivityDescription.setEnabled(state);
				mrMarketingBudgetItemsTextView.setEnabled(state);
				mrMarketingBudgetItems.setEnabled(state);
				amountTextView.setEnabled(state);
				amount.setEnabled(state);
				marketingActivityNo.setChecked(!state);

			} else if (button == marketingActivityNo) {
				marketingActivityDescriptionTextView.setEnabled(!state);
				marketingActivityDescription.setEnabled(!state);
				mrMarketingBudgetItemsTextView.setEnabled(!state);
				mrMarketingBudgetItems.setEnabled(!state);
				amountTextView.setEnabled(!state);
				amount.setEnabled(!state);
				marketingActivityYes.setChecked(!state);
			}
		}

		// else if(button == marketingActivityYes)
		// {
		// mrMarketingBudgetItemsTextView.setEnabled(true);
		// mrMarketingBudgetItems.setEnabled(true);
		// amountTextView.setEnabled(true);
		// amount.setEnabled(true);
		// marketingActivityNo.setChecked(false);
		// }
		// else if(button == marketingActivityNo)
		// {
		// mrMarketingBudgetItemsTextView.setEnabled(false);
		// mrMarketingBudgetItems.setEnabled(false);
		// amountTextView.setEnabled(false);
		// amount.setEnabled(false);
		// marketingActivityYes.setChecked(false);
		// }
	}

	@Override
	public boolean onLongClick(View arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateDisplay() {
		formDateButton.setText(DateFormat.format("dd-MMM-yyyy", formDate));
		visitDateButton.setText(DateFormat.format("dd-MMM-yyyy", visitDate));
		arrivalTimeButton.setText(DateFormat.format("hh:mm aa", startTime));
		departureTimeButton.setText(DateFormat.format("hh:mm aa", endTime));
		formDateViewOnly.setText(DateFormat.format("dd-MMM-yyyy", formDate));

		boolean isPosters = posters.isChecked() ? true : false;
		postersAmount.setEnabled(isPosters);

		boolean isBrochures = brochures.isChecked() ? true : false;
		brochuresAmount.setEnabled(isBrochures);

		boolean isRxPads = rxPads.isChecked() ? true : false;
		rxPadsAmount.setEnabled(isRxPads);

		boolean isOthers = others.isChecked() ? true : false;
		othersAmount.setEnabled(isOthers);

		boolean isMarketingActivity = marketingActivityYes.isChecked() ? true
				: false;
		mrMarketingBudgetItems.setEnabled(isMarketingActivity);
		amount.setEnabled(isMarketingActivity);

	}

	@Override
	public boolean validate() {
		boolean valid = true;
		StringBuffer message = new StringBuffer();
		// Validate mandatory controls
		View[] mandatory = { locationId, locationName, comments };
		for (View v : mandatory) {
			if (App.get(v).equals("")) {
				valid = false;
				message.append(v.getTag().toString() + ". ");
				((EditText) v).setHintTextColor(getResources().getColor(
						R.color.Red));
			}
		}
		String a = App.get(locationName);

		if (marketingActivityYes.isChecked()
				&& App.get(mrMarketingBudgetItems).equals("")) {
			valid = false;
			message.append(mrMarketingBudgetItems.getTag().toString() + ". ");
			mrMarketingBudgetItems.setHintTextColor(getResources().getColor(
					R.color.Red));
		}

		if (marketingActivityYes.isChecked() && App.get(amount).equals("")) {
			valid = false;
			message.append(amount.getTag().toString() + ". ");
			amount.setHintTextColor(getResources().getColor(R.color.Red));
		}

		if (marketingActivityYes.isChecked()
				&& App.get(marketingActivityDescription).equals("")) {
			valid = false;
			message.append(marketingActivityDescription.getTag().toString()
					+ ". ");
			marketingActivityDescription.setHintTextColor(getResources()
					.getColor(R.color.Red));
		}

		if (posters.isChecked() && App.get(postersAmount).equals("")) {
			valid = false;
			message.append(postersAmount.getTag().toString() + ". ");
			postersAmount
					.setHintTextColor(getResources().getColor(R.color.Red));
		}

		if (brochures.isChecked() && App.get(brochuresAmount).equals("")) {
			valid = false;
			message.append(brochuresAmount.getTag().toString() + ". ");
			brochuresAmount.setHintTextColor(getResources().getColor(
					R.color.Red));
		}

		if (rxPads.isChecked() && App.get(rxPadsAmount).equals("")) {
			valid = false;
			message.append(rxPadsAmount.getTag().toString() + ". ");
			rxPadsAmount.setHintTextColor(getResources().getColor(R.color.Red));
		}
		if (others.isChecked() && App.get(othersAmount).equals("")) {
			valid = false;
			message.append(othersAmount.getTag().toString() + ". ");
			othersAmount.setHintTextColor(getResources().getColor(R.color.Red));
		}

		if (!valid) {
			message.append(getResources().getString(R.string.empty_data) + "\n");
		}

		// Validate data
		if (valid) {
			if (marketingActivityYes.isChecked()
					&& !RegexUtil.isSentence(App
							.get(marketingActivityDescription))) {
				valid = false;
				message.append(marketingActivityDescription.getTag().toString()
						+ ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				marketingActivityDescription.setTextColor(getResources()
						.getColor(R.color.Red));
			}

			if (marketingActivityYes.isChecked()
					&& !RegexUtil.isWord(App.get(mrMarketingBudgetItems))) {
				valid = false;
				message.append(mrMarketingBudgetItems.getTag().toString()
						+ ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				mrMarketingBudgetItems.setTextColor(getResources().getColor(
						R.color.Red));
			}

			if (marketingActivityYes.isChecked()
					&& !RegexUtil.isAlphaNumeric(App.get(amount))) {
				valid = false;
				message.append(amount.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				amount.setTextColor(getResources().getColor(R.color.Red));
			}

			if (posters.isChecked()
					&& !RegexUtil.isNumeric(App.get(postersAmount), false)) {
				valid = false;
				message.append(postersAmount.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				postersAmount
						.setTextColor(getResources().getColor(R.color.Red));
			}
			if (brochures.isChecked()
					&& !RegexUtil.isNumeric(App.get(brochuresAmount), false)) {
				valid = false;
				message.append(brochuresAmount.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				brochuresAmount.setTextColor(getResources().getColor(
						R.color.Red));
			}
			if (rxPads.isChecked()
					&& !RegexUtil.isNumeric(App.get(rxPadsAmount), false)) {
				valid = false;
				message.append(rxPadsAmount.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				rxPadsAmount.setTextColor(getResources().getColor(R.color.Red));
			}
			if (others.isChecked()
					&& !RegexUtil.isNumeric(App.get(othersAmount), false)) {
				valid = false;
				message.append(othersAmount.getTag().toString() + ": "
						+ getResources().getString(R.string.invalid_data)
						+ "\n");
				othersAmount.setTextColor(getResources().getColor(R.color.Red));
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
			if (visitDate.getTime().after(Calendar.getInstance().getTime())) {
				valid = false;
				message.append(visitDateButton.getTag()
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
			observations.add(new String[] { "VISIT_DATE",
					App.getSqlDateTime(visitDate) });
			observations.add(new String[] { "ARRIVAL_TIME",
					App.getSqlDateTime(startTime).split("\\s+")[1] });
			observations.add(new String[] { "DEPARTURE_TIME",
					App.getSqlDateTime(endTime).split("\\s+")[1] });
			observations.add(new String[] { "MET_GP",
					metDrYes.isChecked() ? "Yes" : "No" });
			observations.add(new String[] { "GIVEN_COUPONS",
					givenCouponsYes.isChecked() ? "Yes" : "No" });
			observations.add(new String[] { "MARKETING_ACTIVITY",
					marketingActivityYes.isChecked() ? "Yes" : "No" });

			if (marketingActivityYes.isChecked()) {
				observations.add(new String[] {
						"MARKETING_ACTIVITY_DESCRIPTION",
						App.get(marketingActivityDescription) });
				observations.add(new String[] { "MARKETING_BUDGET_ITEMS",
						App.get(mrMarketingBudgetItems) });
				observations.add(new String[] { "AMOUNT", App.get(amount) });
			}

			if (posters.isChecked()) {
				observations.add(new String[] { "MARKETING_ITEMS_POSTERS",
						posters.isChecked() ? "Yes" : "No" });
				observations.add(new String[] { "COUNT_POSTERS",
						App.get(postersAmount) });
			}
			if (brochures.isChecked()) {
				observations.add(new String[] { "MARKETING_ITEMS_BROCHURES",
						brochures.isChecked() ? "Yes" : "No" });
				observations.add(new String[] { "COUNT_BROCHURES",
						App.get(brochuresAmount) });
			}
			if (rxPads.isChecked()) {
				observations.add(new String[] { "MARKETING_ITEMS_PADS",
						rxPads.isChecked() ? "Yes" : "No" });
				observations.add(new String[] { "COUNT_PADS",
						App.get(rxPadsAmount) });
			}
			if (others.isChecked()) {
				observations.add(new String[] { "MARKETING_ITEMS_OTHERS",
						others.isChecked() ? "Yes" : "No" });
				observations.add(new String[] { "COUNT_OTHERS",
						App.get(othersAmount) });
			}

			observations.add(new String[] { "GP_POTENTIAL",
					App.get(drPotential) });
			observations.add(new String[] { "COMMENTS", App.get(comments) });

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
					result = serverService.saveVisits(FormType.DAILY_VISIT,
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
						App.getAlertDialog(DailyVisitActivity.this,
								AlertType.INFO,
								getResources().getString(R.string.inserted))
								.show();
						initView(views);
					} else {
						App.getAlertDialog(DailyVisitActivity.this,
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
					DailyVisitActivity.this, AlertType.ERROR, getResources()
							.getString(R.string.data_connection_error));
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
								DailyVisitActivity.this,
								AlertType.ERROR,
								getResources().getString(
										R.string.item_not_found)).show();
					} else {
						String message = "Valid location";
						App.getAlertDialog(DailyVisitActivity.this,
								AlertType.INFO, message).show();
						if (location != null) {
							locationName.setText(location.getName()
									.split("\\+")[0]);

							// locationName.setText(location.getName());
							String town = location.getName().split("\\+")[1];
							if (!town.equals("none")) {
								ArrayAdapter townAdap = (ArrayAdapter) DailyVisitActivity.this.town
										.getAdapter(); // cast to an
														// ArrayAdapter
								int spinnerPosition = townAdap
										.getPosition(town);
								// set the default according to value
								DailyVisitActivity.this.town
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
