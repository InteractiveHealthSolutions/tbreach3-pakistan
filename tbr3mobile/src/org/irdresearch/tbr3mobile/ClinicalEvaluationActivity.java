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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class ClinicalEvaluationActivity extends AbstractFragmentActivity
{
	static final int	GET_PATIENT_ID	= 1;
	// Views displayed in pages, sorted w.r.t. appearance on pager
	MyTextView			formDateTextView;
	MyButton			formDateButton;

	MyTextView			patientIdTextView;
	MyEditText			patientId;
	MyButton			scanBarcode;

	MyCheckBox			tb;
	MyCheckBox			diabetes;

	MyTextView			tbHistoryTextView;
	MyTextView			coughTextView;
	MySpinner			cough;
	MyTextView			haemoptysisTextView;
	MySpinner			haemoptysis;
	MyTextView			feverTextView;
	MySpinner			fever;
	MyTextView			nightSweatsTextView;
	MySpinner			nightSweats;
	MyTextView			fatigueTextView;
	MySpinner			fatigue;
	MyTextView			breathingShortnessTextView;
	MySpinner			breathingShortness;
	MyTextView			appetiteLossTextView;
	MySpinner			appetiteLoss;

	MyTextView			diabetesHistoryTextView;
	MyTextView			thirstOrUrinationTextView;
	MySpinner			thirstOrUrination;
	MyTextView			diabetesFatigueTextView;
	MySpinner			diabetesFatigue;
	MyTextView			numbnessTextView;
	MySpinner			numbness;
	MyTextView			blurryVisionTextView;
	MySpinner			blurryVision;
	MyTextView			delayedHealingTextView;
	MySpinner			delayedHealing;

	MyTextView			diabetesExamTextView;
	MyTextView			refusedExaminationTextView;
	MySpinner			refusedExamination;
	MyTextView			leftFootTextView;
	MySpinner			leftFoot;
	MyTextView			rightFootTextView;
	MySpinner			rightFoot;
	MyTextView			leftAnkleTextView;
	MySpinner			leftAnkle;
	MyTextView			rightAnkleTextView;
	MySpinner			rightAnkle;
	MyTextView			leftFootPalpableTextView;
	MySpinner			leftFootPalpable;
	MyTextView			rightFootPalpableTextView;
	MySpinner			rightFootPalpable;
	MyTextView			monofilamentTextView;
	MySpinner			monofilament;
	MyTextView			glucoseTextView;
	MySpinner			glucose;

	MyTextView			commentsTextView;
	MyEditText			comments;

	/**
	 * Subclass representing Fragment for clinical evaluation form
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class ClinicalEvaluationFragment extends Fragment
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
	 * Subclass for Pager Adapter. Uses ClinicalEvaluationFragment subclass as
	 * items
	 * 
	 * @author owais.hussain@irdresearch.org
	 * 
	 */
	class ClinicalEvaluationFragmentPagerAdapter extends FragmentPagerAdapter
	{
		/** Constructor of the class */
		public ClinicalEvaluationFragmentPagerAdapter (FragmentManager fragmentManager)
		{
			super (fragmentManager);
		}

		/** This method will be invoked when a page is requested to create */
		@Override
		public Fragment getItem (int arg0)
		{
			ClinicalEvaluationFragment fragment = new ClinicalEvaluationFragment ();
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
		TAG = "ClinicalEvaluationActivity";
		PAGE_COUNT = 6;
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
		ClinicalEvaluationFragmentPagerAdapter pagerAdapter = new ClinicalEvaluationFragmentPagerAdapter (fragmentManager);
		pager.setAdapter (pagerAdapter);
		pager.setOffscreenPageLimit (PAGE_COUNT);
		// Create views for pages
		formDateTextView = new MyTextView (context, R.style.text, R.string.form_date);
		formDateButton = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.form_date, R.string.form_date);

		patientIdTextView = new MyTextView (context, R.style.text, R.string.patient_id);
		patientId = new MyEditText (context, R.string.patient_id, R.string.patient_id_hint, InputType.TYPE_CLASS_TEXT, R.style.edit, RegexUtil.idLength, false);
		patientId.setCompoundDrawables (getResources ().getDrawable (R.drawable.barcode), null, null, null);
		scanBarcode = new MyButton (context, R.style.button, R.drawable.custom_button_beige, R.string.scan_barcode, R.string.scan_barcode);

		tb = new MyCheckBox (context, R.string.tb, R.style.edit, R.string.tb, true);
		diabetes = new MyCheckBox (context, R.string.diabetes, R.style.edit, R.string.diabetes, true);

		tbHistoryTextView = new MyTextView (context, R.style.text, R.string.tb_history);
		coughTextView = new MyTextView (context, R.style.text, R.string.cough);
		cough = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.cough, R.string.option_hint);
		haemoptysisTextView = new MyTextView (context, R.style.text, R.string.haemoptysis);
		haemoptysis = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.haemoptysis, R.string.option_hint);
		feverTextView = new MyTextView (context, R.style.text, R.string.fever);
		fever = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.fever, R.string.option_hint);
		nightSweatsTextView = new MyTextView (context, R.style.text, R.string.night_sweats);
		nightSweats = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.night_sweats, R.string.option_hint);
		fatigueTextView = new MyTextView (context, R.style.text, R.string.fatigue);
		fatigue = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.fatigue, R.string.option_hint);
		breathingShortnessTextView = new MyTextView (context, R.style.text, R.string.breathing_shortness2);
		breathingShortness = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.breathing_shortness2, R.string.option_hint);
		appetiteLossTextView = new MyTextView (context, R.style.text, R.string.appetite_loss);
		appetiteLoss = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.appetite_loss, R.string.option_hint);

		diabetesHistoryTextView = new MyTextView (context, R.style.text, R.string.diabetes_history);
		thirstOrUrinationTextView = new MyTextView (context, R.style.text, R.string.thirst_or_urination);
		thirstOrUrination = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.thirst_or_urination, R.string.option_hint);
		diabetesFatigueTextView = new MyTextView (context, R.style.text, R.string.diabetes_fatigue);
		diabetesFatigue = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.diabetes_fatigue, R.string.option_hint);
		numbnessTextView = new MyTextView (context, R.style.text, R.string.numbness);
		numbness = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.numbness, R.string.option_hint);
		blurryVisionTextView = new MyTextView (context, R.style.text, R.string.blurry_vision);
		blurryVision = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.blurry_vision, R.string.option_hint);
		delayedHealingTextView = new MyTextView (context, R.style.text, R.string.delayed_healing);
		delayedHealing = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.delayed_healing, R.string.option_hint);

		diabetesExamTextView = new MyTextView (context, R.style.text, R.string.diabetes_exam);
		refusedExaminationTextView = new MyTextView (context, R.style.text, R.string.refused_examination);
		refusedExamination = new MySpinner (context, getResources ().getStringArray (R.array.all_options), R.string.refused_examination, R.string.option_hint);
		leftFootTextView = new MyTextView (context, R.style.text, R.string.left_foot);
		leftFoot = new MySpinner (context, getResources ().getStringArray (R.array.foot_exam_options), R.string.left_foot, R.string.option_hint);
		rightFootTextView = new MyTextView (context, R.style.text, R.string.right_foot);
		rightFoot = new MySpinner (context, getResources ().getStringArray (R.array.foot_exam_options), R.string.right_foot, R.string.option_hint);
		leftAnkleTextView = new MyTextView (context, R.style.text, R.string.left_ankle);
		leftAnkle = new MySpinner (context, getResources ().getStringArray (R.array.palpable_options), R.string.left_ankle, R.string.option_hint);
		rightAnkleTextView = new MyTextView (context, R.style.text, R.string.right_ankle);
		rightAnkle = new MySpinner (context, getResources ().getStringArray (R.array.palpable_options), R.string.right_ankle, R.string.option_hint);
		leftFootPalpableTextView = new MyTextView (context, R.style.text, R.string.left_foot_palpable);
		leftFootPalpable = new MySpinner (context, getResources ().getStringArray (R.array.palpable_options), R.string.left_foot_palpable, R.string.option_hint);
		rightFootPalpableTextView = new MyTextView (context, R.style.text, R.string.right_foot_palpable);
		rightFootPalpable = new MySpinner (context, getResources ().getStringArray (R.array.palpable_options), R.string.right_foot_palpable, R.string.option_hint);
		monofilamentTextView = new MyTextView (context, R.style.text, R.string.monofilament);
		monofilament = new MySpinner (context, getResources ().getStringArray (R.array.palpable_options), R.string.monofilament, R.string.option_hint);
		glucoseTextView = new MyTextView (context, R.style.text, R.string.glucose_monitoring);
		glucose = new MySpinner (context, getResources ().getStringArray (R.array.glucose_options), R.string.glucose_monitoring, R.string.option_hint);

		commentsTextView = new MyTextView (context, R.style.text, R.string.comments);
		comments = new MyEditText (context, R.string.comments, R.string.feedback_hint, InputType.TYPE_CLASS_TEXT, R.style.edit, 200, true);

		View[][] viewGroups = {
				{formDateTextView, formDateButton, patientIdTextView, patientId, scanBarcode},
				{tb, diabetes},
				{tbHistoryTextView, coughTextView, cough, haemoptysisTextView, haemoptysis, feverTextView, fever, nightSweatsTextView, nightSweats, fatigueTextView, fatigue,
						breathingShortnessTextView, breathingShortness, appetiteLossTextView, appetiteLoss},
				{diabetesHistoryTextView, thirstOrUrinationTextView, thirstOrUrination, diabetesFatigueTextView, diabetesFatigue, numbnessTextView, numbness, blurryVisionTextView, blurryVision,
						delayedHealingTextView, delayedHealing},
				{diabetesExamTextView, refusedExaminationTextView, refusedExamination, leftFootTextView, leftFoot, rightFootTextView, rightFoot, leftAnkleTextView, leftAnkle, rightAnkleTextView,
						rightAnkle, leftFootPalpableTextView, leftFootPalpable, rightFootPalpableTextView, rightFootPalpable, monofilamentTextView, monofilament, glucoseTextView, glucose},
				{commentsTextView, comments}};
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
		views = new View[] {patientId, tb, diabetes, cough, refusedExamination};
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
		View[] mandatory = {patientId};
		for (View v : mandatory)
		{
			if (App.get (v).equals (""))
			{
				valid = false;
				message.append (v.getTag () + ". ");
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
				message.append (patientId.getTag () + ": " + getResources ().getString (R.string.invalid_data) + "\n");
				patientId.setTextColor (getResources ().getColor (R.color.Red));
			}
		}
		// Validate range
		if (formDate.getTime ().after (new Date ()))
		{
			valid = false;
			message.append (formDateButton.getTag () + ": " + getResources ().getString (R.string.invalid_date_or_time) + "\n");
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
			final ArrayList<String[]> observations = new ArrayList<String[]> ();
			values.put ("formDate", App.getSqlDate (formDate));
			values.put ("location", App.getLocation ());
			values.put ("patientId", App.get (patientId));
			if (tb.isChecked ())
			{
				observations.add (new String[] {"Cough", App.get (cough)});
				if (cough.getSelectedItemPosition () == 1)
				{
					observations.add (new String[] {"Haemoptysis", App.get (haemoptysis)});
				}
				observations.add (new String[] {"Fever", App.get (fever)});
				observations.add (new String[] {"Night Sweats", App.get (nightSweats)});
				observations.add (new String[] {"Fatigue", App.get (fatigue)});
				observations.add (new String[] {"Breathing Shortness", App.get (breathingShortness)});
				observations.add (new String[] {"Appetite Loss", App.get (appetiteLoss)});
			}
			if (diabetes.isChecked ())
			{
				observations.add (new String[] {"Fatigue", App.get (diabetesFatigue)});
				observations.add (new String[] {"Numbness", App.get (numbness)});
				observations.add (new String[] {"Vision Difficulty", App.get (blurryVision)});
				observations.add (new String[] {"Delayed Healing", App.get (delayedHealing)});
				observations.add (new String[] {"Refused Clinical Exam", App.get (refusedExamination)});
				if (refusedExamination.getSelectedItemPosition () == 0)
				{
					observations.add (new String[] {"Left Foot Exam", App.get (leftFoot)});
					observations.add (new String[] {"Right Foot Exam", App.get (rightFoot)});
					observations.add (new String[] {"Left Posterior Tibial", App.get (leftAnkle)});
					observations.add (new String[] {"Right Posterior Tibial", App.get (rightAnkle)});
					observations.add (new String[] {"Left Dorsalis Pedis", App.get (leftFoot)});
					observations.add (new String[] {"Right Dorsalis Pedis", App.get (rightFoot)});
					observations.add (new String[] {"Monofilament Test", App.get (monofilament)});
					observations.add (new String[] {"Glucose Monitoring", App.get (glucose)});
				}
			}
			observations.add (new String[] {"Remarks", App.get (comments)});
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
					String result = serverService.saveClinicalEvaluation (FormType.CLINICAL_EVALUATION, values, observations.toArray (new String[][] {}));
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
						App.getAlertDialog (ClinicalEvaluationActivity.this, AlertType.INFO, getResources ().getString (R.string.inserted)).show ();
						initView (views);
					}
					else
					{
						App.getAlertDialog (ClinicalEvaluationActivity.this, AlertType.ERROR, result).show ();
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
		if (button == tb)
		{
			coughTextView.setEnabled (state);
			cough.setEnabled (state);
			if (state && cough.getSelectedItemPosition () == 1)
			{
				haemoptysisTextView.setEnabled (state);
				haemoptysis.setEnabled (state);
			}
			feverTextView.setEnabled (state);
			fever.setEnabled (state);
			nightSweatsTextView.setEnabled (state);
			nightSweats.setEnabled (state);
			fatigueTextView.setEnabled (state);
			fatigue.setEnabled (state);
			breathingShortnessTextView.setEnabled (state);
			breathingShortness.setEnabled (state);
			appetiteLossTextView.setEnabled (state);
			appetiteLoss.setEnabled (state);
		}
		else if (button == diabetes)
		{
			thirstOrUrinationTextView.setEnabled (state);
			thirstOrUrination.setEnabled (state);
			diabetesFatigueTextView.setEnabled (state);
			diabetesFatigue.setEnabled (state);
			numbnessTextView.setEnabled (state);
			numbness.setEnabled (state);
			blurryVisionTextView.setEnabled (state);
			blurryVision.setEnabled (state);
			delayedHealingTextView.setEnabled (state);
			delayedHealing.setEnabled (state);
			diabetesExamTextView.setEnabled (state);
			refusedExaminationTextView.setEnabled (state);
			refusedExamination.setEnabled (state);
			if (state && refusedExamination.getSelectedItemPosition () == 0)
			{
				leftFootTextView.setEnabled (state);
				leftFoot.setEnabled (state);
				rightFootTextView.setEnabled (state);
				rightFoot.setEnabled (state);
				leftAnkleTextView.setEnabled (state);
				leftAnkle.setEnabled (state);
				rightAnkleTextView.setEnabled (state);
				rightAnkle.setEnabled (state);
				leftFootPalpableTextView.setEnabled (state);
				leftFootPalpable.setEnabled (state);
				rightFootPalpableTextView.setEnabled (state);
				rightFootPalpable.setEnabled (state);
				monofilamentTextView.setEnabled (state);
				monofilament.setEnabled (state);
				glucoseTextView.setEnabled (state);
				glucose.setEnabled (state);
			}
		}
	}

	@Override
	public void onItemSelected (AdapterView<?> parent, View view, int position, long id)
	{
		MySpinner spinner = (MySpinner) parent;
		boolean enable = spinner.getSelectedItemPosition () == 1;
		if (parent == cough)
		{
			haemoptysisTextView.setEnabled (enable);
			haemoptysis.setEnabled (enable);
		}
		if (parent == refusedExamination)
		{
			enable = spinner.getSelectedItemPosition () == 0;
			leftFootTextView.setEnabled (enable);
			leftFoot.setEnabled (enable);
			rightFootTextView.setEnabled (enable);
			rightFoot.setEnabled (enable);
			leftAnkleTextView.setEnabled (enable);
			leftAnkle.setEnabled (enable);
			rightAnkleTextView.setEnabled (enable);
			rightAnkle.setEnabled (enable);
			leftFootPalpableTextView.setEnabled (enable);
			leftFootPalpable.setEnabled (enable);
			rightFootPalpableTextView.setEnabled (enable);
			rightFootPalpable.setEnabled (enable);
			monofilamentTextView.setEnabled (enable);
			monofilament.setEnabled (enable);
			glucoseTextView.setEnabled (enable);
			glucose.setEnabled (enable);
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
