
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.Date;
import java.util.Iterator;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.AccessType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.DataType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.Parameter;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.UserRightsUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class Report_LogComposite extends Composite implements IReport, ClickHandler, ChangeHandler
{
	private static ServerServiceAsync	service			= GWT.create (ServerService.class);
	private static LoadingWidget		loading			= new LoadingWidget ();
	private static final String			menuName		= "DATALOG";
	private UserRightsUtil				rights			= new UserRightsUtil ();
	private boolean						valid;

	private FlexTable					flexTable		= new FlexTable ();
	private FlexTable					topFlexTable	= new FlexTable ();
	private FlexTable					leftFlexTable	= new FlexTable ();
	private FlexTable					rightFlexTable	= new FlexTable ();
	private Grid						grid			= new Grid (1, 3);

	private Button						viewButton		= new Button ("Save");
	private Button						closeButton		= new Button ("Close");
	private Button						exportButton	= new Button ("Export");
	
	private Label						lblTbReachLog	= new Label (FM.getProjectTitle () + " Log");
	private Label						lblUserId		= new Label ("User ID:");

	private ListBox						logTypeListBox	= new ListBox ();
	private ListBox						userIdComboBox	= new ListBox ();
	
	public Report_LogComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 1, topFlexTable);
		lblTbReachLog.setStyleName ("title");
		topFlexTable.setWidget (0, 0, lblTbReachLog);
		flexTable.setWidget (1, 0, leftFlexTable);
		logTypeListBox.setEnabled (false);
		logTypeListBox.addItem ("LOGIN");
		logTypeListBox.addItem ("INSERT");
		logTypeListBox.addItem ("UPDATE");
		logTypeListBox.addItem ("DELETE");
		leftFlexTable.setWidget (0, 0, logTypeListBox);
		logTypeListBox.setVisibleItemCount (5);
		flexTable.setWidget (1, 1, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		rightFlexTable.setWidget (0, 0, lblUserId);
		rightFlexTable.setWidget (0, 1, userIdComboBox);
		rightFlexTable.setWidget (1, 1, grid);
		grid.setSize ("100%", "100%");
		viewButton.setEnabled (false);
		viewButton.setText ("View");
		grid.setWidget (0, 0, viewButton);
		exportButton.setEnabled (false);
		grid.setWidget (0, 1, exportButton);
		grid.setWidget (0, 2, closeButton);
		flexTable.getRowFormatter ().setVerticalAlign (1, HasVerticalAlignment.ALIGN_TOP);

		viewButton.addClickHandler (this);
		exportButton.addClickHandler (this);
		closeButton.addClickHandler (this);

		try
		{
			load (true);
			service.getColumnData ("Users", "UserName", "", new AsyncCallback<String[]> ()
			{
				public void onSuccess (String[] result)
				{
					userIdComboBox.clear ();
					for (int i = 0; i < result.length; i++)
						userIdComboBox.insertItem (result[i], i);
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

		setRights (menuName);
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

	public void clearUp ()
	{
		clearControls (flexTable);
	}

	public boolean validate ()
	{
		final StringBuilder errorMessage = new StringBuilder ();
		valid = true;
		/* Validate mandatory fields */
		if (true)
		{
			errorMessage.append (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR) + "\n");
			valid = false;
		}
		/* Validate data-type rules */
		if (true)
		{
			errorMessage.append (CustomMessage.getErrorMessage (ErrorType.INVALID_DATA_ERROR) + "\n");
			valid = false;
		}
		if (!valid)
			Window.alert (errorMessage.toString ());
		return valid;
	}

	public void viewData (boolean export)
	{
		String reportName = "";
		Parameter[] params = {new Parameter ("UserName", FM.getCurrentUserName (), DataType.STRING),
				new Parameter ("UserID", FMClient.get (userIdComboBox), DataType.STRING),};
		switch (logTypeListBox.getSelectedIndex ())
		{
			// User Login
			case 0 :
				reportName = "UserLoginReport.jrxml";
				break;
			// Insert
			case 1 :
				reportName = "InsertLogReport.jrxml";
				break;
			// Update
			case 2 :
				reportName = "UpdateLogReport.jrxml";
				break;
			// Delete
			case 3 :
				reportName = "DeleteLogReport.jrxml";
				break;
		}
		try
		{
//			service.generateReport (reportName, params, export, new AsyncCallback<String> ()
//			{
//
//				public void onSuccess (String result)
//				{
//					Window.open (result, "_blank", "");
//					load (false);
//				}
//
//				public void onFailure (Throwable caught)
//				{
//					load (false);
//				}
//			});
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			load (false);
		}
	}

	public void setRights (String menuName)
	{
		try
		{
			load (true);
			service.getUserRgihts (FM.getCurrentUserName (), FM.getCurrentRole (), menuName,
					new AsyncCallback<Boolean[]> ()
					{

						public void onSuccess (Boolean[] result)
						{
							final Boolean[] userRights = result;
							if (!FM.getCurrentRole ().equals ("GUEST"))
							{
								rights.setRoleRights (FM.getCurrentRole (), userRights);
								logTypeListBox.setEnabled (rights.getAccess (AccessType.SELECT));
								exportButton.setEnabled (rights.getAccess (AccessType.PRINT));
								viewButton.setEnabled (rights.getAccess (AccessType.PRINT));
							}
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

	public void onClick (ClickEvent event)
	{
		Widget sender = (Widget) event.getSource ();
		load (true);
		if (sender == viewButton)
		{
			viewData (false);
		}
		else if (sender == exportButton)
		{
			viewData (true);
		}
		else if (sender == closeButton)
		{
			MainMenuComposite.clear ();
		}
	}

	public void onChange (ChangeEvent event)
	{
		// Not implemented
	}
}
