
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.Date;
import java.util.Iterator;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.AccessType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.InfoType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.UserRightsUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterElement;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterPrerequisite;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterPrerequisiteId;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class EncounterPrerequisiteComposite extends Composite implements IForm, ClickHandler, ChangeHandler
{
	private static ServerServiceAsync	service										= GWT.create (ServerService.class);
	private static LoadingWidget		loading										= new LoadingWidget ();
	private static final String			menuName									= "ENCOUNTER";
	private static final String			tableName									= "encounter_prerequisite";

	private UserRightsUtil				rights										= new UserRightsUtil ();
	private boolean						valid;
	private EncounterType				currentType;
	private EncounterPrerequisite		currentPrerequisite;

	private FlexTable					flexTable									= new FlexTable ();
	private FlexTable					topFlexTable								= new FlexTable ();
	private FlexTable					leftFlexTable								= new FlexTable ();
	private FlexTable					rightFlexTable								= new FlexTable ();

	private HorizontalPanel				prerequisiteEncounterTypeHorizontalPanel	= new HorizontalPanel ();
	private HorizontalPanel				conditionElementHorizontalPanel				= new HorizontalPanel ();

	private Grid						grid										= new Grid (1, 3);

	private ToggleButton				createButton								= new ToggleButton ("Create");
	private Button						saveButton									= new Button ("Save");
	private Button						deleteButton								= new Button ("Delete");
	private Button						closeButton									= new Button ("Close");

	private Label						lblTbControlEncounter						= new Label (
																							"TB CONTROL Encounter Prerequisites");
	private Label						lblType										= new Label ("Type:");
	private Label						lblPrereqNo									= new Label ("Pre-req. No:");
	private Label						lblPrerequisiteEncounterType				= new Label (
																							"Pre-requisite Type:");
	private Label						prerequisiteEncounterNameLabel				= new Label ("");
	private Label						lblConditionElement							= new Label ("Condition Element:");
	private Label						conditionElementLabel						= new Label ("");
	private Label						lblPossibleValueRegex						= new Label (
																							"Possible Value Regex:");

	private TextBox						possibleValueRegexTextBox					= new TextBox ();

	private ListBox						encounterTypeComboBox						= new ListBox ();
	private ListBox						prerequisiteNoListBox						= new ListBox ();
	private ListBox						prerequisiteEncounterComboBox				= new ListBox ();
	private ListBox						conditionElementComboBox					= new ListBox ();

	public EncounterPrerequisiteComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 1, topFlexTable);
		topFlexTable.setSize("100%", "100%");
		lblTbControlEncounter.setStyleName ("title");
		topFlexTable.setWidget (0, 0, lblTbControlEncounter);
		flexTable.setWidget (1, 0, leftFlexTable);
		leftFlexTable.setSize("100%", "100%");
		lblType.setWordWrap(false);
		leftFlexTable.setWidget (0, 0, lblType);
		encounterTypeComboBox.setName ("encounter_type;encounter_type");
		encounterTypeComboBox
				.setTitle ("List of Encounter Types. Selecting anyone fills its Pre-requisite numbers in the Combo Box below.");
		leftFlexTable.setWidget (0, 1, encounterTypeComboBox);
		lblPrereqNo.setWordWrap(false);
		leftFlexTable.setWidget (1, 0, lblPrereqNo);
		lblPrereqNo.setWidth ("100%");
		prerequisiteNoListBox
				.setTitle ("This list contains number of Pre-requisites defined for seleted Type. Selecting a number fills the details in right panel.");
		prerequisiteNoListBox.setVisibleItemCount (5);
		leftFlexTable.setWidget (1, 1, prerequisiteNoListBox);
		leftFlexTable.getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.setWidget (1, 1, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		lblPrerequisiteEncounterType.setWordWrap(false);
		rightFlexTable.setWidget (0, 0, lblPrerequisiteEncounterType);
		lblPrerequisiteEncounterType.setWidth ("100%");
		rightFlexTable.setWidget (0, 1, prerequisiteEncounterTypeHorizontalPanel);
		prerequisiteEncounterTypeHorizontalPanel.add (prerequisiteEncounterComboBox);
		prerequisiteEncounterComboBox.setName ("encounter_type;encounter_type");
		prerequisiteEncounterNameLabel.setWordWrap(false);
		prerequisiteEncounterTypeHorizontalPanel.add (prerequisiteEncounterNameLabel);
		prerequisiteEncounterComboBox.addChangeHandler (this);
		lblConditionElement.setWordWrap(false);
		rightFlexTable.setWidget (1, 0, lblConditionElement);
		lblConditionElement.setWidth ("100%");
		rightFlexTable.setWidget (1, 1, conditionElementHorizontalPanel);
		conditionElementHorizontalPanel.add (conditionElementComboBox);
		conditionElementLabel.setWordWrap(false);
		conditionElementHorizontalPanel.add (conditionElementLabel);
		lblPossibleValueRegex.setWordWrap(false);
		rightFlexTable.setWidget (2, 0, lblPossibleValueRegex);
		lblPossibleValueRegex.setWidth ("100%");
		possibleValueRegexTextBox.setName ("encounter_prerequisite;possible_value_regex");
		rightFlexTable.setWidget (2, 1, possibleValueRegexTextBox);
		createButton.setEnabled (false);
		rightFlexTable.setWidget (3, 0, createButton);
		rightFlexTable.setWidget (3, 1, grid);
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
		encounterTypeComboBox.addChangeHandler (this);
		conditionElementComboBox.addChangeHandler (this);
		prerequisiteNoListBox.addChangeHandler (this);

		refresh (flexTable);
		try
		{
			service.getColumnData ("encounter_type", "encounter_type", "", new AsyncCallback<String[]> ()
			{
				public void onSuccess (String[] result)
				{
					encounterTypeComboBox.clear ();
					prerequisiteEncounterComboBox.clear ();
					prerequisiteEncounterComboBox.addItem ("");
					encounterTypeComboBox.addItem ("");
					for (String s : result)
					{
						encounterTypeComboBox.addItem (s);
						prerequisiteEncounterComboBox.addItem (s);
					}
					setRights (menuName);
				}

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
		currentPrerequisite.setPrerequisiteEncounter (FMClient.get (prerequisiteEncounterComboBox));
		currentPrerequisite.setConditionElement (FMClient.get (conditionElementComboBox));
		currentPrerequisite.setPossibleValueRegex (FMClient.get (possibleValueRegexTextBox));
	}

	public void fillData ()
	{
		try
		{
			int no = Integer.parseInt (FMClient.get (prerequisiteNoListBox));
			EncounterPrerequisiteId encounterPrerequisiteId = new EncounterPrerequisiteId (
					currentType.getEncounterType (), no);
			service.findEncounterPrerequisite (encounterPrerequisiteId, new AsyncCallback<EncounterPrerequisite> ()
			{
				public void onSuccess (EncounterPrerequisite result)
				{
					currentPrerequisite = result;
					prerequisiteEncounterComboBox.setSelectedIndex (FMClient.getIndex (prerequisiteEncounterComboBox,
							currentPrerequisite.getPrerequisiteEncounter ()));
					if (conditionElementComboBox.getItemCount () == 0)
						conditionElementComboBox.addItem (currentPrerequisite.getConditionElement ());
					else
						conditionElementComboBox.setSelectedIndex (FMClient.getIndex (conditionElementComboBox,
								currentPrerequisite.getConditionElement ()));
					possibleValueRegexTextBox.setValue (currentPrerequisite.getPossibleValueRegex ());
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
			load (false);
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
		/* Validate selection in controls */
		if (currentType == null)
		{
			errorMessage.append (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR)
					+ ". Please make sure you have selected Encounter Type from Combo box (on left panel)." + "\n");
			valid = false;
		}
		/* Validate mandatory fields */
		if (FMClient.get (prerequisiteEncounterComboBox).equals ("")
				|| FMClient.get (conditionElementComboBox).equals (""))
		{
			errorMessage
					.append (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR)
							+ ". Please make sure you have selected Pre-requisite Encounter Type and Element from Combo boxes (on right panel)."
							+ "\n");
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
			EncounterPrerequisiteId prereqId = new EncounterPrerequisiteId (currentType.getEncounterType (), 0);
			currentPrerequisite = new EncounterPrerequisite (prereqId, FMClient.get (prerequisiteEncounterComboBox));
			setCurrent ();
			try
			{
				service.saveEncounterPrerequisite (currentPrerequisite, new AsyncCallback<Boolean> ()
				{
					public void onSuccess (Boolean result)
					{
						if (result == null)
							Window.alert (CustomMessage.getErrorMessage (ErrorType.DUPLICATION_ERROR));
						else if (result)
						{
							Window.alert (CustomMessage.getInfoMessage (InfoType.INSERTED));
							clearUp ();
						}
						else
							Window.alert (CustomMessage.getErrorMessage (ErrorType.INSERT_ERROR));
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

	public void updateData ()
	{
		if (validate ())
		{
			setCurrent ();
			try
			{
				service.updateEncounterPrerequisite (currentPrerequisite, new AsyncCallback<Boolean> ()
				{
					public void onSuccess (Boolean result)
					{
						if (result == null)
							Window.alert (CustomMessage.getErrorMessage (ErrorType.DUPLICATION_ERROR));
						else if (result)
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
				service.deleteEncounterPrerequisite (currentPrerequisite, new AsyncCallback<Boolean> ()
				{
					public void onSuccess (Boolean result)
					{
						if (result == null)
							Window.alert (CustomMessage.getErrorMessage (ErrorType.DELETE_ERROR));
						else if (result)
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
							encounterTypeComboBox.setEnabled (rights.getAccess (AccessType.SELECT));
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
			encounterTypeComboBox.setEnabled (!createButton.isDown ());
			prerequisiteNoListBox.setEnabled (!createButton.isDown ());
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
		if (sender == encounterTypeComboBox)
		{
			try
			{
				service.findEncounterType (FMClient.get (encounterTypeComboBox), new AsyncCallback<EncounterType> ()
				{
					public void onSuccess (EncounterType result)
					{
						currentType = result;
						try
						{
							service.getColumnData (tableName, "prerequisite_no",
									"encounter_type='" + currentType.getEncounterType () + "'",
									new AsyncCallback<String[]> ()
									{
										public void onSuccess (String[] result)
										{
											prerequisiteNoListBox.clear ();
											for (String s : result)
												prerequisiteNoListBox.addItem (s);
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
		else if (sender == prerequisiteNoListBox)
		{
			fillData ();
		}
		else if (sender == prerequisiteEncounterComboBox)
		{
			try
			{
				prerequisiteEncounterNameLabel.setText ("");
				service.findEncounterType (FMClient.get (prerequisiteEncounterComboBox),
						new AsyncCallback<EncounterType> ()
						{
							public void onSuccess (EncounterType result)
							{
								prerequisiteEncounterNameLabel.setText (result.getDescription ());
							}

							@Override
							public void onFailure (Throwable caught)
							{
								caught.printStackTrace ();
							}
						});
				service.getColumnData ("encounter_element", "element",
						"encounter_type='" + FMClient.get (prerequisiteEncounterComboBox) + "'",
						new AsyncCallback<String[]> ()
						{
							public void onSuccess (String[] result)
							{
								conditionElementComboBox.clear ();
								conditionElementComboBox.addItem ("");
								for (String s : result)
									conditionElementComboBox.addItem (s);
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
				load (false);
			}
		}
		else if (sender == conditionElementComboBox)
		{
			try
			{
				conditionElementLabel.setText ("");
				service.findEncounterElement (FMClient.get (prerequisiteEncounterComboBox),
						FMClient.get (conditionElementComboBox), new AsyncCallback<EncounterElement> ()
						{
							public void onSuccess (EncounterElement result)
							{
								conditionElementLabel.setText (result.getDescription ());
								load (false);
							}

							@Override
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
				load (false);
			}
		}
	}
}
