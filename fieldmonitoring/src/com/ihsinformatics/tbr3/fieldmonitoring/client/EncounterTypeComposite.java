
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.Date;
import java.util.Iterator;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.AccessType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.InfoType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.UserRightsUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterType;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class EncounterTypeComposite extends Composite implements IForm, ClickHandler, ChangeHandler
{
	private static ServerServiceAsync	service					= GWT.create (ServerService.class);
	private static LoadingWidget		loading					= new LoadingWidget ();
	private static final String			menuName				= "ENCOUNTER";
	private static final String			tableName				= "encounter_type";

	private UserRightsUtil				rights					= new UserRightsUtil ();
	private boolean						valid;
	private EncounterType				current;

	private FlexTable					flexTable				= new FlexTable ();
	private FlexTable					topFlexTable			= new FlexTable ();
	private FlexTable					leftFlexTable			= new FlexTable ();
	private FlexTable					rightFlexTable			= new FlexTable ();
	private Grid						grid					= new Grid (1, 3);

	private ToggleButton				createButton			= new ToggleButton ("Create");
	private Button						saveButton				= new Button ("Save");
	private Button						deleteButton			= new Button ("Delete");
	private Button						closeButton				= new Button ("Close");

	private Label						lblType					= new Label ("Type:");
	private Label						label					= new Label (FM.getProjectTitle ()
																		+ " Encounter Types");
	private Label						lblKey					= new Label ("Encounter Type:");
	private Label						lblValue				= new Label ("Description:");

	private TextBox						encounterTypeTextBox	= new TextBox ();
	private TextBox						descriptionTextBox		= new TextBox ();

	private ListBox						encounterTypeListBox	= new ListBox ();

	public EncounterTypeComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 1, topFlexTable);
		label.setWordWrap (false);
		label.setStyleName ("title");
		topFlexTable.setWidget (0, 0, label);
		flexTable.setWidget (1, 0, leftFlexTable);
		lblType.setWordWrap (false);

		leftFlexTable.setWidget (0, 0, lblType);
		encounterTypeListBox.setEnabled (false);
		leftFlexTable.setWidget (1, 0, encounterTypeListBox);
		encounterTypeListBox.setVisibleItemCount (5);
		encounterTypeListBox
				.setTitle ("This list contains existing Encounter Types. Selecting anyone fills the details in right panel.");
		flexTable.setWidget (1, 1, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		lblKey.setWordWrap (false);
		rightFlexTable.setWidget (0, 0, lblKey);
		encounterTypeTextBox.setVisibleLength (10);
		encounterTypeTextBox.setEnabled (false);
		encounterTypeTextBox.setName ("encounter_type;encounter_type");
		encounterTypeTextBox.setTitle ("Here goes the Form name, used in the program.");
		rightFlexTable.setWidget (0, 1, encounterTypeTextBox);
		lblValue.setWordWrap (false);
		rightFlexTable.setWidget (1, 0, lblValue);
		descriptionTextBox.setName ("encounter_type;description");
		rightFlexTable.setWidget (1, 1, descriptionTextBox);
		createButton.setEnabled (false);
		rightFlexTable.setWidget (2, 0, createButton);
		rightFlexTable.setWidget (2, 1, grid);
		grid.setSize ("100%", "100%");
		saveButton.setEnabled (false);
		grid.setWidget (0, 0, saveButton);
		deleteButton.setEnabled (false);
		grid.setWidget (0, 1, deleteButton);
		grid.setWidget (0, 2, closeButton);
		flexTable.getRowFormatter ().setVerticalAlign (1, HasVerticalAlignment.ALIGN_TOP);

		createButton.addClickHandler (this);
		saveButton.addClickHandler (this);
		deleteButton.addClickHandler (this);
		closeButton.addClickHandler (this);
		encounterTypeListBox.addChangeHandler (this);

		refresh (flexTable);
		try
		{
			service.getColumnData ("encounter_type", "encounter_type", "", new AsyncCallback<String[]> ()
			{
				@Override
				public void onSuccess (String[] result)
				{
					encounterTypeListBox.clear ();
					for (String s : result)
						encounterTypeListBox.addItem (s);
					setRights (menuName);
				}

				@Override
				public void onFailure (Throwable caught)
				{
					caught.printStackTrace ();
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace ();
		}
	}

	/**
	 * This method refreshes data inside a widget recursively. If the widget is
	 * a List box and the "name" property of the list box is set, then the
	 * method searches the value in "name" (e.g. MARITAL_STATUS) in definitions
	 * and loads into the list. Otherwise if the "name" property is not set, the
	 * list box is left untouched. If the widget is text-type, and the "name"
	 * property is set, then the method sets max length to the allowed length in
	 * table meta data. (The format of name property has to be in the format:
	 * table_name;column_name)
	 * 
	 * @param widget
	 */
	public void refresh (Widget widget)
	{
		if (widget instanceof FlexTable)
		{
			Iterator<Widget> iter = ((FlexTable) widget).iterator ();
			while (iter.hasNext ())
				refresh (iter.next ());
		}
		else if (widget instanceof Panel)
		{
			Iterator<Widget> iter = ((Panel) widget).iterator ();
			while (iter.hasNext ())
				refresh (iter.next ());
		}
		else if (widget instanceof TextBox)
		{
			TextBox text = (TextBox) widget;
			String name = text.getName ();
			if (!name.equals (""))
			{
				String[] parts = name.split (";");
				if (parts.length == 2)
					text.setMaxLength (FM.getMaxLength (parts[0], parts[1]));
			}
		}
		else if (widget instanceof ListBox)
		{
			if (!((ListBox) widget).getName ().equals (""))
				widget = FMClient.fillList (widget);
		}
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

	public void setCurrent ()
	{
		current.setDescription (FMClient.get (descriptionTextBox).toUpperCase ());
	}

	public void fillData ()
	{
		try
		{
			service.findEncounterType (FMClient.get (encounterTypeListBox), new AsyncCallback<EncounterType> ()
			{
				public void onSuccess (EncounterType result)
				{
					current = result;
					encounterTypeTextBox.setValue (result.getEncounterType ());
					descriptionTextBox.setValue (result.getDescription ());
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

	public void clearUp ()
	{
		clearControls (rightFlexTable);
	}

	public boolean validate ()
	{
		final StringBuilder errorMessage = new StringBuilder ();
		valid = true;
		/* Validate mandatory fields */
		if (FMClient.get (encounterTypeTextBox).equals ("") || FMClient.get (descriptionTextBox).equals (""))
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
			/* Validate uniqueness */
			try
			{
				service.exists (tableName, "encounter_type='" + FMClient.get (encounterTypeListBox) + "'",
						new AsyncCallback<Boolean> ()
						{
							public void onSuccess (Boolean result)
							{
								if (!result)
								{
									current = new EncounterType (FMClient.get (encounterTypeTextBox).toUpperCase ());
									setCurrent ();
									try
									{
										service.saveEncounterType (current, new AsyncCallback<Boolean> ()
										{
											public void onSuccess (Boolean result)
											{
												if (result)
												{
													Window.alert (CustomMessage.getInfoMessage (InfoType.INSERTED));
													clearUp ();
												}
												else
													Window.alert (CustomMessage
															.getErrorMessage (ErrorType.INSERT_ERROR));
												load (false);
											}

											public void onFailure (Throwable caught)
											{
												Window.alert (CustomMessage.getErrorMessage (ErrorType.INSERT_ERROR));
												load (false);
											}
										});
									}
									catch (Exception e)
									{
										e.printStackTrace ();
									}
								}
								else
									Window.alert (CustomMessage.getErrorMessage (ErrorType.DUPLICATION_ERROR));
								load (false);
							}

							public void onFailure (Throwable caught)
							{
								Window.alert (CustomMessage.getErrorMessage (ErrorType.INSERT_ERROR));
								load (false);
							}
						});
			}
			catch (Exception e)
			{
				e.printStackTrace ();
			}
			load (false);
		}
	}

	public void updateData ()
	{
		if (validate ())
		{
			setCurrent ();
			try
			{
				service.updateEncounterType (current, new AsyncCallback<Boolean> ()
				{
					public void onSuccess (Boolean result)
					{
						if (result)
							Window.alert (CustomMessage.getInfoMessage (InfoType.UPDATED));
						else
							Window.alert (CustomMessage.getErrorMessage (ErrorType.UPDATE_ERROR));
						load (false);
					}

					public void onFailure (Throwable caught)
					{
						Window.alert (CustomMessage.getErrorMessage (ErrorType.UPDATE_ERROR));
						load (false);
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace ();
			}
		}
	}

	public void deleteData ()
	{
		if (validate ())
		{
			try
			{
				service.deleteEncounterType (current, new AsyncCallback<Boolean> ()
				{
					public void onSuccess (Boolean result)
					{
						if (result)
						{
							Window.alert (CustomMessage.getInfoMessage (InfoType.DELETED));
							clearUp ();
						}
						else
							Window.alert (CustomMessage.getErrorMessage (ErrorType.DELETE_ERROR));
						load (false);
					}

					public void onFailure (Throwable caught)
					{
						caught.printStackTrace ();
						load (false);
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace ();
			}
		}
	}

	public void setRights (String menuName)
	{
		load (true);
		try
		{
			service.getUserRgihts (FM.getCurrentUserName (), FM.getCurrentRole (), menuName,
					new AsyncCallback<Boolean[]> ()
					{
						public void onSuccess (Boolean[] result)
						{
							final Boolean[] userRights = result;
							rights.setRoleRights (FM.getCurrentRole (), userRights);
							encounterTypeListBox.setEnabled (rights.getAccess (AccessType.SELECT));
							createButton.setEnabled (rights.getAccess (AccessType.INSERT));
							saveButton.setEnabled (rights.getAccess (AccessType.UPDATE));
							deleteButton.setEnabled (rights.getAccess (AccessType.DELETE));
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
		if (sender == createButton)
		{
			if (createButton.isDown ())
				clearUp ();
			encounterTypeListBox.setEnabled (!createButton.isDown ());
			encounterTypeTextBox.setEnabled (createButton.isDown ());
			load (false);
		}
		else if (sender == saveButton)
		{
			if (createButton.isDown ())
				saveData ();
			else
				updateData ();
		}
		else if (sender == deleteButton)
		{
			deleteData ();
		}
		else if (sender == closeButton)
		{
			if (menuName.equals ("DEFINITION"))
				Window.alert ("Please refresh your browser if you have made any changes to this form.");
			MainMenuComposite.clear ();
		}
	}

	public void onChange (ChangeEvent event)
	{
		Widget sender = (Widget) event.getSource ();
		load (true);
		if (sender == encounterTypeListBox)
		{
			fillData ();
		}
	}
}
