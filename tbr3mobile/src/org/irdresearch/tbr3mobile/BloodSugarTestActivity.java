/**
 * Blood Sugar Test Order Activity
 */

package org.irdresearch.tbr3mobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import org.irdresearch.tbr3mobile.custom.MyButton;
import org.irdresearch.tbr3mobile.custom.MyEditText;
import org.irdresearch.tbr3mobile.custom.MyRadioButton;
import org.irdresearch.tbr3mobile.custom.MyRadioGroup;
import org.irdresearch.tbr3mobile.custom.MySpinner;
import org.irdresearch.tbr3mobile.custom.MyTextView;
import org.irdresearch.tbr3mobile.shared.AlertType;
import org.irdresearch.tbr3mobile.shared.FormType;
import org.irdresearch.tbr3mobile.util.RegexUtil;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class BloodSugarTestActivity extends AbstractFragmentActivity implements OnEditorActionListener
{
	// Views displayed in pages, sorted w.r.t. appearance on pager
	MyTextView		formDateTextView;
	MyButton		formDateButton;

	MyTextView		patientIdTextView;
	MyEditText		patientId;
	MyButton		scanBarcode;

	MyTextView		takingMedicationTextView;
	MySpinner		takingMedication;
	MyTextView		diabetesMedicineTextView;
	MyEditText		diabetesMedicine;
	MyTextView		familyDiabetesTextView;
	MySpinner		familyDiabetes;

	MyTextView		physicalActivityTextView;
	MySpinner		physicalActivity;
	MyTextView		vegetableUsageTextView;
	MySpinner		vegetableUsage;
	MyTextView		lastMealTextView;
	MySpinner		lastMeal;

	MyTextView		paidForTestTextView;
	MyRadioGroup	paidForTest;
	MyRadioButton	yes;
	MyRadioButton	no;
	MyTextView		labTestCodeTextView;
	MyEditText		labTestCode;

	/**
	 * Subclass representing Fragment for blood sugar test form
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class BloodSugarTestFragment extends Fragment
	{
		int	currentPage;

		@Override
		public void onCreate (Bundle savedInstanceState)
		{
			super.onCreate (savedInstanceState);
			Bundle data = getArguments ();
			currentPage = data.getInt ("current_page", 0);
		}

		@Override
		public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			// Return a layout of views from pre-filled ArrayList of groups
			if (currentPage != 0 && groups.size () != 0)
				return groups.get (currentPage - 1);
			else
				return null;
		}
	}

	/**
	 * Subclass for Pager Adapter. Uses BloodSugarTest subclass as items
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class BloodSugarTestFragmentPagerAdapter extends FragmentPagerAdapter
	{
		/** Constructor of the class */
		public BloodSugarTestFragmentPagerAdapter (FragmentManager fragmentManager)
		{
			super (fragmentManager);
		}

		/** This method will be invoked when a page is requested to create */
		@Override
		public Fragment getItem (int arg0)
		{
			BloodSugarTestFragment fragment = new BloodSugarTestFragment ();
			Bundle data = new Bundle ();
			data.putInt ("current_page", arg0 + 1);
			fragment.setArguments (data);
			return fragment;
		}

		/** Returns the number of pages */
		@Override
		public int getCount ()
		{
			return PAGE_COUNT;
		}
	}

	@Override
	public void createViews (final Context context)
	{
		TAG = "BloodSugarTestActivity";
		PAGE_COUNT = 4;
		pager = (ViewPager) findViewById (R.template_id.pager);
		navigationSeekbar.setMax (PAGE_COUNT - 1);
		navigatorLayout = (LinearLayout) findViewById (R.template_id.navigatorLayout);
		// If the form consists only of single page, then hide the
		// navigatorLayout
		if (PAGE_COUNT < 2)
		{
			navigatorLayout.setVisibility (View.GONE);
		}
		FragmentManager fragmentManager = getSupportFragmentManager ();
		BloodSugarTestFragmentPagerAdapter pagerAdapter = new BloodSugarTestFragmentPagerAdapter (fragmentManager);
		pager.setAdapter (pagerAdapter);
		pager.setOffscreenPageLimit (PAGE_COUNT);
		// Create views for pages
		formDateTextView = new MyTextView (context, R.style.text, R.string.form_date);
		formDateButton = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.form_date, R.string.form_date);
		patientIdTextView = new MyTextView (context, R.style.text, R.string.patient_id);
		patientId = new MyEditText (context, R.string.patient_id, R.string.patient_id_hint, InputType.TYPE_CLASS_TEXT, R.style.edit, RegexUtil.idLength, false);
		scanBarcode = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.scan_barcode, R.string.scan_barcode);

		takingMedicationTextView = new MyTextView (context, R.style.text, R.string.taking_medication);
		takingMedication = new MySpinner (context, getResources ().getStringArray (R.array.options), R.string.taking_medication, R.string.option_hint);
		diabetesMedicineTextView = new MyTextView (context, R.style.text, R.string.diabetes_medicine);
		diabetesMedicine = new MyEditText (context, R.string.diabetes_medicine, R.string.diabetes_medicine_hint, InputType.TYPE_TEXT_VARIATION_PERSON_NAME, R.style.edit, 50, true);
		familyDiabetesTextView = new MyTextView (context, R.style.text, R.string.family_diabetes);
		familyDiabetes = new MySpinner (context, getResources ().getStringArray (R.array.options), R.string.family_diabetes, R.string.option_hint);

		physicalActivityTextView = new MyTextView (context, R.style.text, R.string.physical_activity);
		physicalActivity = new MySpinner (context, getResources ().getStringArray (R.array.options), R.string.physical_activity, R.string.option_hint);
		vegetableUsageTextView = new MyTextView (context, R.style.text, R.string.vegetable_usage);
		vegetableUsage = new MySpinner (context, getResources ().getStringArray (R.array.daily_frequency), R.string.vegetable_usage, R.string.option_hint);
		lastMealTextView = new MyTextView (context, R.style.text, R.string.last_meal);
		lastMeal = new MySpinner (context, getResources ().getStringArray (R.array.last_meal_duration), R.string.last_meal, R.string.option_hint);

		paidForTestTextView = new MyTextView (context, R.style.text, R.string.paid_for_test);
		yes = new MyRadioButton (context, R.string.yes, R.style.edit, R.string.yes);
		yes.setChecked (true);
		no = new MyRadioButton (context, R.string.no, R.style.edit, R.string.no);
		paidForTest = new MyRadioGroup (context, new MyRadioButton[] {yes, no}, R.string.paid_for_test, R.style.edit, App.isLanguageRTL ());
		labTestCodeTextView = new MyTextView (context, R.style.text, R.string.lab_test_code);
		labTestCode = new MyEditText (context, R.string.lab_test_code, R.string.lab_test_code_hint, InputType.TYPE_CLASS_TEXT, R.style.edit, 12, false);

		View[][] viewGroups = { {formDateTextView, formDateButton, patientIdTextView, patientId, scanBarcode},
				{takingMedicationTextView, takingMedication, diabetesMedicineTextView, diabetesMedicine, familyDiabetesTextView, familyDiabetes},
				{physicalActivityTextView, physicalActivity, vegetableUsageTextView, vegetableUsage, lastMealTextView, lastMeal}, {paidForTestTextView, paidForTest, labTestCodeTextView, labTestCode}};
		// Create layouts and store in ArrayList
		groups = new ArrayList<ViewGroup> ();
		for (int i = 0; i < PAGE_COUNT; i++)
		{
			LinearLayout layout = new LinearLayout (context);
			layout.setOrientation (LinearLayout.VERTICAL);
			for (int j = 0; j < viewGroups[i].length; j++)
			{
				layout.addView (viewGroups[i][j]);
			}
			ScrollView scrollView = new ScrollView (context);
			scrollView.setLayoutParams (new LayoutParams (LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			scrollView.addView (layout);
			groups.add (scrollView);
		}
		// Set event listeners
		formDateButton.setOnClickListener (this);
		firstButton.setOnClickListener (this);
		lastButton.setOnClickListener (this);
		clearButton.setOnClickListener (this);
		saveButton.setOnClickListener (this);
		scanBarcode.setOnClickListener (this);
		navigationSeekbar.setOnSeekBarChangeListener (this);
		views = new View[] {patientId, takingMedication, diabetesMedicine, familyDiabetes, physicalActivity, vegetableUsage, lastMeal, paidForTest, labTestCode};
		for (View v : views)
		{
			if (v instanceof Spinner)
			{
				((Spinner) v).setOnItemSelectedListener (this);
			}
			else if (v instanceof CheckBox)
			{
				((CheckBox) v).setOnCheckedChangeListener (this);
			}
		}
		pager.setOnPageChangeListener (this);
		// Detect RTL language
		if (App.isLanguageRTL ())
		{
			Collections.reverse (groups);
			for (ViewGroup g : groups)
			{
				LinearLayout linearLayout = (LinearLayout) g.getChildAt (0);
				linearLayout.setGravity (Gravity.RIGHT);
			}
			for (View v : views)
			{
				if (v instanceof EditText)
				{
					((EditText) v).setGravity (Gravity.RIGHT);
				}
			}
		}
	}

	@Override
	public void initView (View[] views)
	{
		super.initView (views);
		formDate = Calendar.getInstance ();
		diabetesMedicine.setEnabled (false);
		updateDisplay ();
	}

	@Override
	public void updateDisplay ()
	{
		formDateButton.setText (DateFormat.format ("dd-MMM-yyyy", formDate));
	}

	@Override
	public boolean validate ()
	{
		boolean valid = true;
		StringBuffer message = new StringBuffer ();
		// Validate mandatory controls
		View[] mandatory = {patientId, labTestCode};
		for (View v : mandatory)
		{
			if (App.get (v).equals (""))
			{
				valid = false;
				message.append (v.getTag ().toString () + ". ");
				((EditText) v).setHintTextColor (getResources ().getColor (R.color.Red));
			}
		}
		if (App.get (takingMedication).equals ("Yes") && App.get (diabetesMedicine).equals (""))
		{
			valid = false;
			message.append (takingMedication.getTag ().toString () + ". ");
		}
		if (!valid)
		{
			message.append (getResources ().getString (R.string.empty_data) + "\n");
		}
		// Validate data
		if (valid)
		{
			if (!RegexUtil.isValidId (App.get (patientId)))
			{
				valid = false;
				message.append (patientId.getTag ().toString () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
				patientId.setTextColor (getResources ().getColor (R.color.Red));
			}
			if (!RegexUtil.isValidCheckDigit (App.get (labTestCode)))
			{
				valid = false;
				message.append (labTestCode.getTag ().toString () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
				labTestCode.setTextColor (getResources ().getColor (R.color.Red));
			}
		}
		// Validate ranges
		try
		{
			// Form date range
			if (formDate.getTime ().after (Calendar.getInstance ().getTime ()))
			{
				valid = false;
				message.append (formDateButton.getTag () + ": " + getResources ().getString (R.string.invalid_date_or_time) + "\n");
			}
		}
		catch (NumberFormatException e)
		{
		}
		if (!valid)
		{
			App.getAlertDialog (this, AlertType.ERROR, message.toString ()).show ();
		}
		return valid;
	}

	public boolean submit ()
	{
		if (validate ())
		{
			final ContentValues values = new ContentValues ();
			values.put ("formDate", App.getSqlDate (formDate));
			values.put ("location", App.getLocation ());
			values.put ("patientId", App.get (patientId));
			final ArrayList<String[]> observations = new ArrayList<String[]> ();
			observations.add (new String[] {"Sugar Medication", App.get (takingMedication)});
			if (diabetesMedicine.isEnabled ())
			{
				observations.add (new String[] {"Sugar Medicine", App.get (diabetesMedicine)});
			}
			observations.add (new String[] {"Family Diabetes", App.get (familyDiabetes)});
			observations.add (new String[] {"30-Minute Physical Activity", App.get (physicalActivity)});
			observations.add (new String[] {"Vegetable Use", App.get (vegetableUsage)});
			observations.add (new String[] {"Last Food", App.get (lastMeal)});
			observations.add (new String[] {"Sugar Test Payment", (yes.isChecked () ? "Yes" : "No")});
			observations.add (new String[] {"Blood Sugar Test Barcode", App.get (labTestCode)});
			AsyncTask<String, String, String> updateTask = new AsyncTask<String, String, String> ()
			{
				@Override
				protected String doInBackground (String... params)
				{
					runOnUiThread (new Runnable ()
					{
						@Override
						public void run ()
						{
							loading.setIndeterminate (true);
							loading.setCancelable (false);
							loading.setMessage (getResources ().getString (R.string.loading_message));
							loading.show ();
						}
					});
					String result = serverService.saveBloodSugarTest (FormType.BLOOD_SUGAR_TEST, values, observations.toArray (new String[][] {}));
					return result;
				}

				@Override
				protected void onProgressUpdate (String... values)
				{
				};

				@Override
				protected void onPostExecute (String result)
				{
					super.onPostExecute (result);
					loading.dismiss ();
					if (result.equals ("SUCCESS"))
					{
						App.getAlertDialog (BloodSugarTestActivity.this, AlertType.INFO, getResources ().getString (R.string.inserted)).show ();
						initView (views);
					}
					else
					{
						App.getAlertDialog (BloodSugarTestActivity.this, AlertType.ERROR, result).show ();
					}
				}
			};
			updateTask.execute ("");
		}
		return true;
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult (requestCode, resultCode, data);
		// Retrieve barcode scan results
		if (requestCode == Barcode.BARCODE_RESULT)
		{
			if (resultCode == RESULT_OK)
			{
				String str = data.getStringExtra (Barcode.SCAN_RESULT);
				// Check for valid Id
				if (RegexUtil.isValidId (str) && !RegexUtil.isNumeric (str, false))
				{
					patientId.setText (str);
				}
				else
				{
					App.getAlertDialog (this, AlertType.ERROR, patientId.getTag ().toString () + ": " + getResources ().getString (R.string.invalid_data)).show ();
				}
			}
			else if (resultCode == RESULT_CANCELED)
			{
				// Handle cancel
				App.getAlertDialog (this, AlertType.ERROR, getResources ().getString (R.string.operation_cancelled)).show ();
			}
			// Set the locale again, since the Barcode app restores system's
			// locale because of orientation
			Locale.setDefault (App.getCurrentLocale ());
			Configuration config = new Configuration ();
			config.locale = App.getCurrentLocale ();
			getApplicationContext ().getResources ().updateConfiguration (config, null);
		}
	};

	@Override
	public void onClick (View view)
	{
		if (view == formDateButton)
		{
			showDialog (DATE_DIALOG_ID);
		}
		else if (view == firstButton)
		{
			gotoFirstPage ();
		}
		else if (view == lastButton)
		{
			gotoLastPage ();
		}
		else if (view == clearButton)
		{
			initView (views);
		}
		else if (view == saveButton)
		{
			submit ();
		}
		else if (view == scanBarcode)
		{
			Intent intent = new Intent (Barcode.BARCODE_INTENT);
			intent.putExtra (Barcode.SCAN_MODE, Barcode.QR_MODE);
			startActivityForResult (intent, Barcode.BARCODE_RESULT);
		}
	}

	@Override
	public void onCheckedChanged (CompoundButton button, boolean state)
	{
		// Not implemented
	}

	@Override
	public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
	{
		MySpinner spinner = (MySpinner) parent;
		boolean visible = spinner.getSelectedItemPosition () == 1;
		if (parent == takingMedication)
		{
			diabetesMedicineTextView.setEnabled (visible);
			diabetesMedicine.setEnabled (visible);
		}
		updateDisplay ();
	}

	@Override
	public boolean onLongClick (View v)
	{
		return false;
	}

	@Override
	public boolean onEditorAction (TextView v, int actionId, KeyEvent event)
	{
		return true;
	}
}
