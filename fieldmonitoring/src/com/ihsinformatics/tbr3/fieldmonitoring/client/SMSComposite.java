
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.Date;
import java.util.Iterator;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.AccessType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.InfoType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.UserRightsUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Sms;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;

public class SMSComposite extends Composite implements IForm, ClickHandler, ChangeHandler
{
	private static ServerServiceAsync	service				= GWT.create (ServerService.class);
	private static LoadingWidget		loading				= new LoadingWidget ();
	private static final String			menuName			= "SMS";
	private static final String			tableName			= "Sms";

	private UserRightsUtil				rights				= new UserRightsUtil ();
	private boolean						valid;

	private FlexTable					flexTable			= new FlexTable ();
	private FlexTable					topFlexTable		= new FlexTable ();
	private FlexTable					rightFlexTable		= new FlexTable ();
	private Grid						grid				= new Grid (1, 2);
	private VerticalPanel				groupsVerticalPanel	= new VerticalPanel ();

	private Button						saveButton			= new Button ("Save");
	private Button						closeButton			= new Button ("Close");

	private Label						lblTbReachSms		= new Label (FM.getProjectTitle () + " SMS Utility");
	private Label						lblMessageToSend	= new Label ("Message to send:");
	private Label						lblDatetime			= new Label ("Date/Time:");
	private Label						lblGroups			= new Label ("Groups:");

	private TextArea					messageTextBox		= new TextArea ();

	private CheckBox					chwCheckBox			= new CheckBox ("CHW's");
	private CheckBox					gpCheckBox			= new CheckBox ("GPs");
	private CheckBox					supervisorCheckBox	= new CheckBox ("Supervisors");
	private CheckBox					patientCheckBox		= new CheckBox ("Patients");
	private CheckBox					monitorCheckBox		= new CheckBox ("Monitors");

	private DateBox						smsDateDateBox		= new DateBox ();

	public SMSComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 0, topFlexTable);
		lblTbReachSms.setStyleName ("title");
		topFlexTable.setWidget (0, 0, lblTbReachSms);
		flexTable.setWidget (1, 0, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		rightFlexTable.setWidget (0, 0, lblMessageToSend);
		messageTextBox.setCharacterWidth (30);
		messageTextBox.setVisibleLines (5);
		rightFlexTable.setWidget (0, 1, messageTextBox);
		rightFlexTable.setWidget (1, 0, lblDatetime);
		smsDateDateBox.setFormat (new DefaultFormat (DateTimeFormat.getFormat ("yyyy-MM-dd HH:mm")));
		rightFlexTable.setWidget (1, 1, smsDateDateBox);
		rightFlexTable.setWidget (2, 0, lblGroups);
		rightFlexTable.setWidget (2, 1, groupsVerticalPanel);
		chwCheckBox.setHTML ("CHWs");
		groupsVerticalPanel.add (chwCheckBox);
		groupsVerticalPanel.add (gpCheckBox);
		groupsVerticalPanel.add (monitorCheckBox);
		groupsVerticalPanel.add (patientCheckBox);
		groupsVerticalPanel.add (supervisorCheckBox);
		rightFlexTable.setWidget (3, 1, grid);
		grid.setSize ("100%", "100%");
		saveButton.setEnabled (false);
		grid.setWidget (0, 0, saveButton);
		grid.setWidget (0, 1, closeButton);
		rightFlexTable.getCellFormatter ().setVerticalAlignment (0, 0, HasVerticalAlignment.ALIGN_TOP);
		rightFlexTable.getCellFormatter ().setVerticalAlignment (2, 0, HasVerticalAlignment.ALIGN_TOP);
		rightFlexTable.getCellFormatter ().setVerticalAlignment (1, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getRowFormatter ().setVerticalAlign (1, HasVerticalAlignment.ALIGN_TOP);

		saveButton.addClickHandler (this);
		closeButton.addClickHandler (this);

		refreshList ();
		setRights (menuName);
	}

	public void refreshList ()
	{
		// Not implemented
	}

	/**
	 * Display/Hide main panel and loading widget
	 * 
	 * @param status
	 */
	public void load (boolean status)
	{
		flexTable.setVisible (!status);
		if (status)
			loading.show ();
		else
			loading.hide ();
	}

	public void clearControls (Widget w)
	{
		if (w instanceof Panel)
		{
			Iterator<Widget> iter = ((Panel) w).iterator ();
			while (iter.hasNext ())
				clearControls (iter.next ());
		}
		else if (w instanceof TextBoxBase)
		{
			((TextBoxBase) w).setText ("");
		}
		else if (w instanceof RichTextArea)
		{
			((RichTextArea) w).setText ("");
		}
		else if (w instanceof ListBox)
		{
			((ListBox) w).setSelectedIndex (0);
		}
		else if (w instanceof DatePicker)
		{
			((DatePicker) w).setValue (new Date ());
		}
	}
	
	public void setCurrent()
	{
		// Not implemented
	}

	public void fillData ()
	{
		try
		{
			service.getColumnData (tableName, "", "", new AsyncCallback<String[]> ()
			{

				public void onSuccess (String[] result)
				{
					load (false);
				}

				public void onFailure (Throwable caught)
				{
					load (false);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			load (false);
		}
	}

	public void clearUp ()
	{
		clearControls (flexTable);
	}

	public boolean validate ()
	{
		final StringBuilder errorMessage = new StringBuilder ();
		valid = true;
		/* Validate whether at least one group is selected */
		boolean check = false;
		for (int i = 0; i < groupsVerticalPanel.getWidgetCount (); i++)
		{
			if (groupsVerticalPanel.getWidget (i) instanceof CheckBox)
			{
				CheckBox chk = (CheckBox) groupsVerticalPanel.getWidget (i);
				if (chk.getValue ())
				{
					check = true;
					break;
				}
			}
		}
		if (!check)
		{
			errorMessage.append ("You did not select any group. Please check at least one group for sending SMS.");
			valid = false;
		}
		/* Validate mandatory fields */
		if (messageTextBox.getText ().length () <= 0)
		{
			errorMessage.append (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR) + "\n");
			valid = false;
		}
		if (!valid)
		{
			Window.alert (errorMessage.toString ());
			load (false);
		}
		return valid;
	}

	public void saveData ()
	{
		if (validate ())
		{
			try
			{
				String filter = "where ifnull(Mobile, '') <> '' and (";
				load (true);
				if (chwCheckBox.getValue ())
					filter += "PID in (select WorkerID from Worker) or ";
				if (gpCheckBox.getValue ())
					filter += "PID in (select GPID from GP) and ";
				if (monitorCheckBox.getValue ())
					filter += "PID in (select MonitorID from Monitor) or ";
				if (supervisorCheckBox.getValue ())
					filter += "PID in (select SupervisorID from Supervisor) or ";
				if (patientCheckBox.getValue ())
					filter += "PID in (select PatientID from Patient where DiseaseConfirmed = 1) or ";
				filter += "1=0)";
				service.getColumnData ("Contact", "Mobile", filter, new AsyncCallback<String[]> ()
				{

					public void onSuccess (String[] result)
					{
						Sms[] messages = new Sms[result.length];
						for (int i = 0; i < result.length; i++)
						{
							messages[i] = new Sms (result[i], smsDateDateBox.getValue ());
							messages[i].setMessageText (FMClient.get (messageTextBox));
							messages[i].setStatus ("PENDING");
						}
						try
						{
							service.sendGenericSMSAlert (messages, new AsyncCallback<Void> ()
							{

								public void onSuccess (Void result)
								{
									Window.alert (CustomMessage.getInfoMessage (InfoType.INSERTED)
											+ "\nMessages will be delivered within 1 hour from Scheduled time.");
									load (false);
								}

								public void onFailure (Throwable caught)
								{
									load (false);
								}
							});
						}
						catch (Exception e)
						{
							e.printStackTrace ();
						}
					}

					public void onFailure (Throwable caught)
					{
						load (false);
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace ();
				load (false);
			}
		}
	}

	public void updateData ()
	{
		// Not implemented
	}

	public void deleteData ()
	{
		// Not implemented
	}

	public void setRights (String menuName)
	{
		try
		{
			load (true);
			service.getUserRgihts (FM.getCurrentUserName (), FM.getCurrentRole (), menuName, new AsyncCallback<Boolean[]> ()
			{

				public void onSuccess (Boolean[] result)
				{
					final Boolean[] userRights = result;
					try
					{
						service.findUser (FM.getCurrentUserName (), new AsyncCallback<User> ()
						{

							public void onSuccess (User result)
							{
								rights.setRoleRights (FM.getCurrentRole (), userRights);
								saveButton.setEnabled (rights.getAccess (AccessType.UPDATE));
								load (false);
							}

							public void onFailure (Throwable caught)
							{
								load (false);
							}
						});
					}
					catch (Exception e)
					{
						e.printStackTrace ();
						load (false);
					}
				}

				public void onFailure (Throwable caught)
				{
					load (false);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			load (false);
		}
	}

	public void onClick (ClickEvent event)
	{
		Widget sender = (Widget) event.getSource ();
		load (true);
		if (sender == saveButton)
		{
			saveData ();
		}
	}

	public void onChange (ChangeEvent event)
	{
		// Not implemented
	}
}
