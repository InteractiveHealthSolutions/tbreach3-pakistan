/**
 * Blood Sugar Test Results Activity
 */

package org.irdresearch.tbr3mobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.irdresearch.tbr3mobile.custom.MyButton;
import org.irdresearch.tbr3mobile.custom.MyEditText;
import org.irdresearch.tbr3mobile.custom.MyRadioButton;
import org.irdresearch.tbr3mobile.custom.MyRadioGroup;
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
import android.view.View.OnClickListener;
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
public class BloodSugarResultsActivity extends AbstractFragmentActivity implements OnEditorActionListener
{
	private static final AtomicInteger	counter	= new AtomicInteger ();

	// Views displayed in pages, sorted w.r.t. appearance on pager
	MyTextView							formDateTextView;
	MyButton							formDateButton;

	MyTextView							patientIdTextView;
	MyEditText							patientId;
	MyButton							scanBarcode;

	MyTextView							sugarTestResultTextView;
	MyEditText							sugarTestResult;
	MyButton							labTestCodeButton;
	MyRadioGroup						labTestCodes;

	/**
	 * Subclass representing Fragment for blood sugar results form
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class BloodSugarResultsFragment extends Fragment
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
	 * Subclass for Pager Adapter. Uses BloodSugarResults subclass as items
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class BloodSugarResultsFragmentPagerAdapter extends FragmentPagerAdapter
	{
		/** Constructor of the class */
		public BloodSugarResultsFragmentPagerAdapter (FragmentManager fragmentManager)
		{
			super (fragmentManager);
		}

		/** This method will be invoked when a page is requested to create */
		@Override
		public Fragment getItem (int arg0)
		{
			BloodSugarResultsFragment fragment = new BloodSugarResultsFragment ();
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
		TAG = "BloodSugarResultsActivity";
		PAGE_COUNT = 1;
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
		BloodSugarResultsFragmentPagerAdapter pagerAdapter = new BloodSugarResultsFragmentPagerAdapter (fragmentManager);
		pager.setAdapter (pagerAdapter);
		pager.setOffscreenPageLimit (PAGE_COUNT);
		// Create views for pages
		formDateTextView = new MyTextView (context, R.style.text, R.string.form_date);
		formDateButton = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.form_date, R.string.form_date);
		patientIdTextView = new MyTextView (context, R.style.text, R.string.patient_id);
		patientId = new MyEditText (context, R.string.patient_id, R.string.patient_id_hint, InputType.TYPE_CLASS_TEXT, R.style.edit, RegexUtil.idLength, false);
		scanBarcode = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.scan_barcode, R.string.scan_barcode);
		labTestCodeButton = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.lab_test_code, R.string.lab_test_code);
		labTestCodes = new MyRadioGroup (context, new MyRadioButton[] {}, R.string.lab_test_code, R.style.edit, App.isLanguageRTL ());
		sugarTestResultTextView = new MyTextView (context, R.style.text, R.string.diabetes_result);
		sugarTestResult = new MyEditText (context, R.string.diabetes_result, R.string.empty_string, InputType.TYPE_CLASS_NUMBER, R.style.edit, 3, false);
		View[][] viewGroups = {{formDateTextView, formDateButton, patientIdTextView, patientId, scanBarcode, labTestCodeButton, labTestCodes, sugarTestResultTextView, sugarTestResult}};
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
		labTestCodeButton.setOnClickListener (this);
		firstButton.setOnClickListener (this);
		lastButton.setOnClickListener (this);
		clearButton.setOnClickListener (this);
		saveButton.setOnClickListener (this);
		scanBarcode.setOnClickListener (this);
		navigationSeekbar.setOnSeekBarChangeListener (this);
		views = new View[] {patientId, sugarTestResult, labTestCodeButton, labTestCodes};
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
		labTestCodes.removeAllViews ();
		sugarTestResult.setEnabled (false);
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
		View[] mandatory = {patientId, sugarTestResult};
		for (View v : mandatory)
		{
			if (App.get (v).equals (""))
			{
				valid = false;
				message.append (v.getTag ().toString () + ". ");
				((EditText) v).setHintTextColor (getResources ().getColor (R.color.Red));
			}
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
			if (!RegexUtil.isNumeric (App.get (sugarTestResult), false))
			{
				valid = false;
				message.append (sugarTestResult.getTag ().toString () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
				sugarTestResult.setTextColor (getResources ().getColor (R.color.Red));
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
			// Check if there's exactly 1 item in test codes
			if (labTestCodes.getChildCount () != 1)
			{
				valid = false;
				message.append (labTestCodes.getTag () + ": " + getResources ().getString (R.string.empty_selection) + "\n");
			}
			if (valid)
			{
				Integer bloodSugar = Integer.parseInt (App.get (sugarTestResult));
				if (bloodSugar < 0 || bloodSugar > 500)
				{
					valid = false;
					message.append (sugarTestResult.getTag () + ": " + getResources ().getString (R.string.out_of_range) + "\n");
				}
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
			observations.add (new String[] {"Sugar Test Result", App.get (sugarTestResult)});
			String labCode = ((TextView) labTestCodes.getChildAt (0)).getText ().toString ();
			observations.add (new String[] {"Blood Sugar Test Barcode", labCode});
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
					String result = serverService.saveBloodSugarResults (FormType.BLOOD_SUGAR_RESULTS, values, observations.toArray (new String[][] {}));
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
						App.getAlertDialog (BloodSugarResultsActivity.this, AlertType.INFO, getResources ().getString (R.string.inserted)).show ();
						initView (views);
					}
					else
					{
						App.getAlertDialog (BloodSugarResultsActivity.this, AlertType.ERROR, result).show ();
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
		// Fetch existing Test order codes from Server
		else if (view == labTestCodeButton)
		{
			final String patId = App.get (patientId);
			if (!patId.equals (""))
			{
				AsyncTask<String, String, String[]> getTask = new AsyncTask<String, String, String[]> ()
				{
					@Override
					protected String[] doInBackground (String... params)
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
						String[] patientObs = serverService.getPatientObs (patId, "Blood Sugar Test Barcode");
						return patientObs;
					}

					@Override
					protected void onProgressUpdate (String... values)
					{
					};

					@Override
					protected void onPostExecute (String[] result)
					{
						super.onPostExecute (result);
						loading.dismiss ();
						if (result != null)
						{
							labTestCodes.removeAllViews ();
							for (String s : result)
							{
								MyTextView textView = new MyTextView (BloodSugarResultsActivity.this, R.style.text, R.string.empty_string);
								textView.setId (counter.getAndIncrement ());
								textView.setText (s);
								textView.setTag (s);
								textView.setOnClickListener (new OnClickListener ()
								{
									@Override
									public void onClick (View view)
									{
										TextView tv = (TextView) view;
										labTestCodes.removeAllViews ();
										labTestCodes.addView (tv);
									}
								});
								labTestCodes.addView (textView);
							}
							sugarTestResult.setEnabled (true);
						}
						else
						{
							App.getAlertDialog (BloodSugarResultsActivity.this, AlertType.ERROR,
									getResources ().getString (R.string.lab_test_code) + ": " + getResources ().getString (R.string.item_not_found)).show ();
						}
					}
				};
				getTask.execute ("");
			}
			else
			{
				App.getAlertDialog (this, AlertType.ERROR, getResources ().getString (R.string.lab_test_code) + ": " + getResources ().getString (R.string.empty_data)).show ();
			}
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
		// Not implemented
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
