/**
 * Activity to view, submit and delete saved forms
 */

package org.irdresearch.tbr3mobile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import org.irdresearch.tbr3mobile.custom.MyCheckBox;
import org.irdresearch.tbr3mobile.shared.AlertType;
import org.irdresearch.tbr3mobile.util.DateTimeUtil;
import org.irdresearch.tbr3mobile.util.JsonUtil;
import org.irdresearch.tbr3mobile.util.ServerService;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.ScrollView;
import android.widget.Toast;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class SavedFormsActivity extends Activity implements OnCheckedChangeListener, OnClickListener
{
	public static final String			TAG				= "ReportsActivity";
	public static final String			SEARCH_RESULT	= "SEARCH_RESULT";
	private static final AtomicInteger	counter			= new AtomicInteger ();
	public static ServerService			serverService;
	public static ProgressDialog		loading;

	Button								showSavedFormsButton;
	Button								submitFormsButton;
	Button								discardFormsButton;
	ScrollView							searchResultsScrollView;
	ListView							savedFormsListView;
	RadioGroup							formsRadioGroup;
	Animation							alphaAnimation;

	protected void onCreate (Bundle savedInstanceState)
	{
		setTheme (App.getTheme ());
		setContentView (R.layout.saved_forms);
		super.onCreate (savedInstanceState);
		serverService = new ServerService (this);
		loading = new ProgressDialog (this);
		showSavedFormsButton = (Button) findViewById (R.saved_forms_id.showSavedFormsButton);
		submitFormsButton = (Button) findViewById (R.saved_forms_id.submitSavedFormsButton);
		discardFormsButton = (Button) findViewById (R.saved_forms_id.discardFormsButton);
		searchResultsScrollView = (ScrollView) findViewById (R.saved_forms_id.resultsScrollView);
		savedFormsListView = (ListView) findViewById (R.saved_forms_id.savedFormsListView);
		formsRadioGroup = (RadioGroup) findViewById (R.saved_forms_id.formsRadioGroup);
		alphaAnimation = AnimationUtils.loadAnimation (this, R.anim.alpha_animation);

		showSavedFormsButton.setOnClickListener (this);
		submitFormsButton.setOnClickListener (this);
		discardFormsButton.setOnClickListener (this);
		discardFormsButton.setEnabled (false);
	}

	@Override
	public void onBackPressed ()
	{
		finish ();
		Intent loginIntent = new Intent (getApplicationContext (), MainMenuActivity.class);
		startActivity (loginIntent);
	}

	@Override
	public void onCheckedChanged (RadioGroup radioGroup, int checkedId)
	{
		// Not implemented
	}

	@Override
	public void onClick (View view)
	{
		if (view == showSavedFormsButton)
		{
			formsRadioGroup.removeAllViews ();
			String[][] forms = serverService.getSavedForms (App.getUsername ());
			for (String[] form : forms)
			{
				Date date = new Date (Long.parseLong (form[0]));
				String formName = form[1];
				MyCheckBox checkBox = new MyCheckBox (this);
				checkBox.setTag (form[0]);
				checkBox.setText (DateTimeUtil.getSQLDate (date) + " " + formName);
				checkBox.setId (counter.getAndIncrement ());
				checkBox.setTextAppearance (this, R.style.text);
				checkBox.setChecked (true);
				formsRadioGroup.addView (checkBox);
			}
		}
		else if (view == submitFormsButton)
		{
			AsyncTask<String, Integer, String[]> submitTask = new AsyncTask<String, Integer, String[]> ()
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
					ArrayList<String> responses = new ArrayList<String> ();
					for (int i = 0; i < formsRadioGroup.getChildCount (); i++)
					{
						MyCheckBox checkBox = (MyCheckBox) formsRadioGroup.getChildAt (i);
						if (!checkBox.isChecked ())
						{
							continue;
						}
						String[] form = serverService.getSavedForm (App.getUsername (), checkBox.getTag ().toString ());
						try
						{
							Long timestamp = Long.parseLong (form[0]);
							String formName = form[1];
							String json = form[2];
							JSONObject jsonObj = new JSONObject (json);
							String response = serverService.post ("?content=" + JsonUtil.getEncodedJson (jsonObj));
							JSONObject jsonResponse = JsonUtil.getJSONObject (response);
							if (jsonResponse.has ("result"))
							{
								String result = jsonResponse.getString ("result");
								if (result.equalsIgnoreCase ("SUCCESS"))
								{
									Date date = new Date (timestamp);
									responses.add (formName + " (" + DateTimeUtil.getSQLDate (date) + "): " + result);
									serverService.deleteSavedForm (timestamp);
								}
								else
								{
									responses.add (timestamp.toString () + " (" + formName + "). " + response);
								}
							}
							else
							{
								responses.add (timestamp.toString () + " (" + formName + "). " + response);
							}
						}
						catch (Exception e)
						{
							Log.e (TAG, e.getMessage ());
						}
					}
					return responses.toArray (new String[] {});
				}

				@Override
				protected void onPostExecute (String[] result)
				{
					super.onPostExecute (result);
					loading.dismiss ();
					StringBuilder response = new StringBuilder ();
					for (String s : result)
					{
						response.append (s + "\n");
					}
					discardFormsButton.setEnabled (true);
					App.getAlertDialog (SavedFormsActivity.this, AlertType.INFO, response.toString ()).show ();
					formsRadioGroup.removeAllViews ();					
				}
			};
			submitTask.execute ("");
		}
		else if (view == discardFormsButton)
		{
			final String[][] forms = serverService.getSavedForms (App.getUsername ());
			if (forms.length == 0)
			{
				App.getAlertDialog (this, AlertType.INFO, getResources ().getString (R.string.item_not_found)).show ();
			}
			else
			{
				AlertDialog confirmDialog = App.getAlertDialog (SavedFormsActivity.this, AlertType.QUESTION, getResources ().getString (R.string.delete_forms));
				confirmDialog.setButton (AlertDialog.BUTTON_POSITIVE, getResources ().getString (R.string.yes), new DialogInterface.OnClickListener ()
				{
					@Override
					public void onClick (DialogInterface dialog, int which)
					{
						StringBuilder formsData = new StringBuilder ();
						for (int i = 0; i < formsRadioGroup.getChildCount (); i++)
						{
							MyCheckBox checkBox = (MyCheckBox) formsRadioGroup.getChildAt (i);
							if (!checkBox.isChecked ())
							{
								continue;
							}
							Long formTimestamp = Long.parseLong (checkBox.getTag ().toString ());
							String[] form = serverService.getSavedForm (App.getUsername (), formTimestamp.toString ());
							String formStr = Arrays.toString (form);
							//formStr = formStr.replace ("\"", "").replace ("\\", "").replace ("value:", "").replace ("concept:", "");
							formsData.append (formStr);
							formsData.append ("\n\n\n");
							serverService.deleteSavedForm (formTimestamp);
						}
						String[] emailAddreses = {App.getSupportEmail ()};
						Intent emailIntent = new Intent (Intent.ACTION_SEND);
						emailIntent.putExtra (Intent.EXTRA_EMAIL, emailAddreses);
						StringBuilder subject = new StringBuilder ();
						subject.append (getResources ().getString (R.string.app_name));
						subject.append (" : ");
						subject.append (App.getUsername ());
						emailIntent.putExtra (Intent.EXTRA_SUBJECT, subject.toString ());
						emailIntent.setType ("plain/text");
						emailIntent.putExtra (Intent.EXTRA_TEXT, formsData.toString ());
						for (String[] form : forms)
						{
							Long timestamp = Long.parseLong (form[0]);
							serverService.deleteSavedForm (timestamp);
						}
						Toast.makeText (SavedFormsActivity.this, getResources ().getString (R.string.deleted), App.getDelay ()).show ();
						startActivity (emailIntent);
						formsRadioGroup.removeAllViews ();
					}
				});
				confirmDialog.show ();
			}
		}
	}
}
