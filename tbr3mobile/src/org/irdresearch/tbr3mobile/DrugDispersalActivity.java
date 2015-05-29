/**
 * 
 */

package org.irdresearch.tbr3mobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import org.irdresearch.tbr3mobile.custom.MyButton;
import org.irdresearch.tbr3mobile.custom.MyCheckBox;
import org.irdresearch.tbr3mobile.custom.MyEditText;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class DrugDispersalActivity extends AbstractFragmentActivity
{
	static final int	GET_PATIENT_ID	= 1;
	// Views displayed in pages, sorted w.r.t. appearance on pager
	MyTextView			formDateTextView;
	MyButton			formDateButton;

	MyTextView			patientIdTextView;
	MyEditText			patientId;
	MyButton			scanBarcode;

	MyTextView			deliveryLocationTextView;
	MySpinner			deliveryLocation;

	MyTextView			collectorTextView;
	MySpinner			collector;
	MyTextView			otherCollectorTextView;
	MyEditText			otherCollector;

	MyTextView			drugsForDaysTextView;
	MyEditText			drugsForDays;
	MyTextView			drugsMissedTextView;
	MyCheckBox			drugsMissed;
	MyTextView			drugsMissedForDaysTextView;
	MyEditText			drugsMissedForDays;

	MyTextView			nextDateTextView;
	DatePicker			nextDatePicker;
	Calendar			nextDate;

	/**
	 * Subclass representing Fragment for drug dispersal form
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class DrugDispersalFragment extends Fragment
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
			return null;
		}
	}

	/**
	 * Subclass for Pager Adapter. Uses DrugDispersalFragment subclass as items
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class DrugDispersalFragmentPagerAdapter extends FragmentPagerAdapter
	{
		/** Constructor of the class */
		public DrugDispersalFragmentPagerAdapter (FragmentManager fragmentManager)
		{
			super (fragmentManager);
		}

		/** This method will be invoked when a page is requested to create */
		@Override
		public Fragment getItem (int arg0)
		{
			DrugDispersalFragment fragment = new DrugDispersalFragment ();
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
		TAG = "DrugDispersalActivity";
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
		DrugDispersalFragmentPagerAdapter pagerAdapter = new DrugDispersalFragmentPagerAdapter (fragmentManager);
		pager.setAdapter (pagerAdapter);
		pager.setOffscreenPageLimit (PAGE_COUNT);
		// Create views for pages
		formDateTextView = new MyTextView (context, R.style.text, R.string.form_date);
		formDateButton = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.form_date, R.string.form_date);

		patientIdTextView = new MyTextView (context, R.style.text, R.string.patient_id);
		patientId = new MyEditText (context, R.string.patient_id, R.string.patient_id_hint, InputType.TYPE_CLASS_TEXT, R.style.edit, RegexUtil.idLength, false);
		patientId.setCompoundDrawables (getResources ().getDrawable (R.drawable.barcode), null, null, null);
		scanBarcode = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.scan_barcode, R.string.scan_barcode);

		deliveryLocationTextView = new MyTextView (context, R.style.text, R.string.delivery_location);
		deliveryLocation = new MySpinner (context, getResources ().getStringArray (R.array.delivery_locations), R.string.delivery_location, R.string.option_hint);

		collectorTextView = new MyTextView (context, R.style.text, R.string.collector);
		collector = new MySpinner (context, getResources ().getStringArray (R.array.collectors), R.string.collector, R.string.option_hint);
		otherCollectorTextView = new MyTextView (context, R.style.text, R.string.other_collector);
		otherCollector = new MyEditText (context, R.string.other_collector, R.string.other_collector_hint, InputType.TYPE_CLASS_TEXT, R.style.edit, RegexUtil.idLength, false);

		drugsForDaysTextView = new MyTextView (context, R.style.text, R.string.drug_for_days);
		drugsForDays = new MyEditText (context, R.string.drug_for_days, R.string.drug_for_days_hint, InputType.TYPE_CLASS_NUMBER, R.style.edit, 2, false);
		drugsMissed = new MyCheckBox (context, R.string.missed_medication, R.style.edit, R.string.missed_medication, false);
		drugsMissedForDaysTextView = new MyTextView (context, R.style.text, R.string.days_missed_medication);
		drugsMissedForDays = new MyEditText (context, R.string.days_missed_medication, R.string.days_missed_medication_hint, InputType.TYPE_CLASS_NUMBER, R.style.edit, 2, false);

		nextDateTextView = new MyTextView (context, R.style.text, R.string.next_dispersal_date);
		nextDatePicker = new DatePicker (context);
		nextDatePicker.setTag (getResources ().getString (R.string.next_dispersal_date));
		ArrayList<View> touchables = nextDatePicker.getTouchables ();
		for (int i = 0; i < touchables.size (); i++)
		{
			if (i == 2 || i == 5 || i == 8)
				touchables.get (i).setBackgroundResource (R.drawable.numberpicker_down_normal);
			else if (i == 0 || i == 3 || i == 6)
				touchables.get (i).setBackgroundResource (R.drawable.numberpicker_up_normal);
			else
				touchables.get (i).setBackgroundResource (R.drawable.custom_button_beige);
		}
		nextDate = Calendar.getInstance ();

		View[][] viewGroups = { {formDateTextView, formDateButton, patientIdTextView, patientId, scanBarcode},
				{deliveryLocationTextView, deliveryLocation, collectorTextView, collector, otherCollectorTextView, otherCollector},
				{drugsForDaysTextView, drugsForDays, drugsMissed, drugsMissedForDaysTextView, drugsMissedForDays}, {nextDateTextView, nextDatePicker}};
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
		views = new View[] {patientId, deliveryLocation, collector, drugsMissed};
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
		patientId.setOnLongClickListener (this);
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
		updateDisplay ();
		nextDate.setTime (new Date ());
		nextDate.add (Calendar.MONTH, 1);
		nextDatePicker.updateDate (nextDate.get (Calendar.YEAR), nextDate.get (Calendar.MONTH), nextDate.get (Calendar.DAY_OF_MONTH));
		otherCollectorTextView.setEnabled (false);
		otherCollector.setEnabled (false);
		drugsMissedForDaysTextView.setEnabled (false);
		drugsMissedForDays.setEnabled (false);
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
		View[] mandatory = {patientId, drugsForDays};
		for (View v : mandatory)
		{
			if (App.get (v).equals (""))
			{
				valid = false;
				message.append (v.getTag () + ". ");
				((EditText) v).setHintTextColor (getResources ().getColor (R.color.Red));
			}
		}
		// Explicitly check for Other collector and Drugs missed
		if (otherCollector.isEnabled () && App.get (otherCollector).equals (""))
		{
			valid = false;
			message.append (otherCollector.getTag () + ". ");
			((EditText) otherCollector).setHintTextColor (getResources ().getColor (R.color.Red));
		}
		if (drugsMissed.isChecked () && App.get (drugsMissedForDays).equals (""))
		{
			valid = false;
			message.append (drugsMissedForDays.getTag () + ". ");
			((EditText) drugsMissedForDays).setHintTextColor (getResources ().getColor (R.color.Red));
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
				message.append (patientId.getTag () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
				patientId.setTextColor (getResources ().getColor (R.color.Red));
			}
		}
		if (!RegexUtil.isNumeric (App.get (drugsForDays), false))
		{
			valid = false;
			message.append (drugsForDays.getTag ().toString () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
			drugsForDays.setTextColor (getResources ().getColor (R.color.Red));
		}
		if (!RegexUtil.isNumeric (App.get (drugsMissedForDays), false))
		{
			valid = false;
			message.append (drugsMissedForDays.getTag ().toString () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
			drugsMissedForDays.setTextColor (getResources ().getColor (R.color.Red));
		}
		// Validate range
		if (formDate.getTime ().after (new Date ()))
		{
			valid = false;
			message.append (formDateButton.getTag () + ": " + getResources ().getString (R.string.invalid_date_or_time) + "\n");
		}
		if (!App.get (drugsForDays).equals (""))
		{
			int days = Integer.parseInt (App.get (drugsForDays));
			if (days < 0 || days > 100)
			{
				valid = false;
				message.append (drugsForDays.getTag ().toString () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
			}
		}
		if (!App.get (drugsMissedForDays).equals (""))
		{
			int days = Integer.parseInt (App.get (drugsMissedForDays));
			if (days < 0 || days > 100)
			{
				valid = false;
				message.append (drugsMissedForDays.getTag ().toString () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
			}
		}
		if (nextDate.getTime ().getTime () < (formDate.getTime ().getTime ()))
		{
			valid = false;
			message.append (nextDatePicker.getTag () + ": " + getResources ().getString (R.string.invalid_date_or_time) + "\n");
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
			observations.add (new String[] {"Drug Dispersal Site", App.get (deliveryLocation)});
			observations.add (new String[] {"Drug Collector", App.get (collector)});
			if (otherCollector.isEnabled ())
			{
				observations.add (new String[] {"Other Drug Collector", App.get (otherCollector)});
			}
			observations.add (new String[] {"Drug Days", App.get (drugsForDays)});
			observations.add (new String[] {"Medication Missed", drugsMissed.isChecked () ? "Yes" : "No"});
			if (drugsMissed.isChecked ())
			{
				observations.add (new String[] {"Days Medication Missed", App.get (drugsMissedForDays)});
			}
			observations.add (new String[] {"Next Drug Dispersal", App.getSqlDate (nextDate)});
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
					String result = serverService.saveDrugDispersal (FormType.DRUG_DISPERSAL, values, observations.toArray (new String[][] {}));
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
						App.getAlertDialog (DrugDispersalActivity.this, AlertType.INFO, getResources ().getString (R.string.inserted)).show ();
						initView (views);
					}
					else
					{
						App.getAlertDialog (DrugDispersalActivity.this, AlertType.ERROR, result).show ();
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
		// Retrieve barcode scan results or Search for ID
		if (requestCode == Barcode.BARCODE_RESULT || requestCode == GET_PATIENT_ID)
		{
			if (resultCode == RESULT_OK)
			{
				String str = "";
				if (requestCode == Barcode.BARCODE_RESULT)
					str = data.getStringExtra (Barcode.SCAN_RESULT);
				else if (requestCode == GET_PATIENT_ID)
					str = data.getStringExtra (PatientSearchActivity.SEARCH_RESULT);
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
	}

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
		if (button == drugsMissed)
		{
			if (button.isChecked ())
			{
				drugsMissedForDaysTextView.setEnabled (true);
				drugsMissedForDays.setEnabled (true);
			}
			else
			{
				drugsMissedForDaysTextView.setEnabled (false);
				drugsMissedForDays.setEnabled (false);
			}
		}
	}

	@Override
	public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
	{
		MySpinner spinner = (MySpinner) parent;
		if (parent == collector)
		{
			boolean enable = spinner.getSelectedItemPosition () == 4;
			otherCollectorTextView.setEnabled (enable);
			otherCollector.setEnabled (enable);
		}
		updateDisplay ();
	}

	@Override
	public boolean onLongClick (View view)
	{
		// Not implemented
		return false;
	}
}
