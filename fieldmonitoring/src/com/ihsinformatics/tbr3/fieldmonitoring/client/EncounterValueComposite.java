
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
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterElementId;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterValue;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.EncounterValueId;
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

public class EncounterValueComposite extends Composite implements IForm, ClickHandler, ChangeHandler
{
	private static ServerServiceAsync	service						= GWT.create (ServerService.class);
	private static LoadingWidget		loading						= new LoadingWidget ();
	private static final String			menuName					= "ENCOUNTER";
	private static final String			tableName					= "encounter_value";

	private UserRightsUtil				rights						= new UserRightsUtil ();
	private boolean						valid;
	private EncounterType				currentType;
	private EncounterElement			currentElement;
	private EncounterValue				currentValue;

	private FlexTable					flexTable					= new FlexTable ();
	private FlexTable					topFlexTable				= new FlexTable ();
	private FlexTable					leftFlexTable				= new FlexTable ();
	private FlexTable					rightFlexTable				= new FlexTable ();
	private Grid						grid						= new Grid (1, 3);

	private ToggleButton				createButton				= new ToggleButton ("Create");
	private Button						saveButton					= new Button ("Save");
	private Button						deleteButton				= new Button ("Delete");
	private Button						closeButton					= new Button ("Close");

	private Label						lblTbControlEncounter		= new Label ("TB CONTROL Encounter Values");
	private Label						lblType						= new Label ("Type:");
	private Label						lblElement					= new Label ("Element:");
	private Label						lblValues					= new Label ("Values:");
	private Label						lblElementValue				= new Label ("Element Value:");
	private Label						lblConsequence				= new Label ("Consequence:");

	private TextBox						elementValueTextBox			= new TextBox ();
	private TextBox						consequenceTextBox			= new TextBox ();

	private ListBox						encounterElementComboBox	= new ListBox ();
	private ListBox						encounterTypeComboBox		= new ListBox ();
	private ListBox						elementValueListBox			= new ListBox ();

	public EncounterValueComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 1, topFlexTable);
		lblTbControlEncounter.setWordWrap(false);
		lblTbControlEncounter.setStyleName ("title");
		topFlexTable.setWidget (0, 0, lblTbControlEncounter);
		flexTable.setWidget (1, 0, leftFlexTable);
		lblType.setWordWrap(false);
		leftFlexTable.setWidget (0, 0, lblType);
		encounterTypeComboBox.setTitle("List of Encounter Types. Selecting anyone fills its Elements in the Elements Combo Box.");
		leftFlexTable.setWidget (0, 1, encounterTypeComboBox);
		lblElement.setWordWrap(false);
		leftFlexTable.setWidget (1, 0, lblElement);
		encounterElementComboBox.setTitle("List of Encounter Elements for seleted Type. Selecting anyone fills existing values in Values List Box.");
		leftFlexTable.setWidget (1, 1, encounterElementComboBox);
		lblValues.setWordWrap(false);
		leftFlexTable.setWidget (2, 0, lblValues);
		elementValueListBox.setTitle("This list contains existing Values defined for seleted Type and Element exclusively. Selecting anyone fills the details in right panel.");
		elementValueListBox.setVisibleItemCount (5);
		leftFlexTable.setWidget (2, 1, elementValueListBox);
		leftFlexTable.getCellFormatter ().setVerticalAlignment (2, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.setWidget (1, 1, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		lblElementValue.setWordWrap(false);
		rightFlexTable.setWidget (0, 0, lblElementValue);
		elementValueTextBox.setTitle("Element Values are defined exclusively. Undefined values are assumed to be open-text.");
		elementValueTextBox.setVisibleLength (10);
		elementValueTextBox.setEnabled (false);
		elementValueTextBox.setName ("encounter_value;value");
		rightFlexTable.setWidget (0, 1, elementValueTextBox);
		lblConsequence.setWordWrap(false);
		rightFlexTable.setWidget (1, 0, lblConsequence);
		consequenceTextBox.setTitle("Consequence is the effect of a specific value selected. [This is not yet functional]");
		consequenceTextBox.setName ("encounter_value;consequence_procedure");
		rightFlexTable.setWidget (1, 1, consequenceTextBox);
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
		encounterTypeComboBox.addChangeHandler (this);
		encounterElementComboBox.addChangeHandler (this);
		elementValueListBox.addChangeHandler (this);

		refresh (flexTable);
		try
		{
			service.getColumnData ("encounter_type", "encounter_type", "", new AsyncCallback<String[]> ()
			{
				public void onSuccess (String[] result)
				{
					encounterTypeComboBox.clear ();
					encounterTypeComboBox.addItem ("");
					for (String s : result)
						encounterTypeComboBox.addItem (s);
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
			e.printStackTrace();
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
		EncounterValueId valueId = new EncounterValueId ();
		valueId.setEncounterType (FMClient.get (encounterTypeComboBox));
		valueId.setElement (FMClient.get (encounterElementComboBox));
		valueId.setValue (FMClient.get (elementValueTextBox).toUpperCase ());
		currentValue.setId (valueId);
		currentValue.setConsequenceProcedure (FMClient.get (consequenceTextBox).toUpperCase ());
	}

	public void fillData ()
	{
		try
		{
			service.findEncounterValue (currentType.getEncounterType (), FMClient.get (encounterElementComboBox),
					FMClient.get (elementValueListBox), new AsyncCallback<EncounterValue> ()
					{
						public void onSuccess (EncounterValue result)
						{
							currentValue = result;
							elementValueTextBox.setValue (result.getId ().getValue ());
							consequenceTextBox.setValue (result.getConsequenceProcedure ());
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
			e.printStackTrace();
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
		if (currentType == null || currentElement == null)
		{
			errorMessage.append (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR)
					+ ". Please make sure you have selected Encounter Type and Element from Combo boxes." + "\n");
			valid = false;
		}
		/* Validate mandatory fields */
		if (FMClient.get (elementValueTextBox).equals (""))
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
			EncounterElementId encounterElementId = new EncounterElementId (FMClient.get (encounterTypeComboBox),
					FMClient.get (elementValueTextBox).toUpperCase ());
			currentElement = new EncounterElement (encounterElementId);
			currentValue = new EncounterValue ();
			setCurrent ();
			try
			{
				service.saveEncounterValue (currentValue, new AsyncCallback<Boolean> ()
				{
					public void onSuccess (Boolean result)
					{
						if (result == null)
							Window.alert (CustomMessage.getErrorMessage (ErrorType.DUPLICATION_ERROR));
						else if (result)
						{
							Window.alert (CustomMessage.getInfoMessage (InfoType.INSERTED));
							elementValueListBox.addItem (currentElement.getId ().getElement ());
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
				e.printStackTrace();
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
				service.updateEncounterValue (currentValue, new AsyncCallback<Boolean> ()
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
				e.printStackTrace();
			}
		}
	}

	public void deleteData () 
	{
		if (validate ())
		{
			try
			{
				service.deleteEncounterValue (currentValue, new AsyncCallback<Boolean> ()
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
				e.printStackTrace();
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
							encounterElementComboBox.setEnabled (rights.getAccess (AccessType.SELECT));
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
			e.printStackTrace();
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
			encounterElementComboBox.setEnabled (!createButton.isDown ());
			elementValueTextBox.setEnabled (createButton.isDown ());
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
							service.getColumnData ("encounter_element", "element",
									"encounter_type='" + currentType.getEncounterType () + "'", new AsyncCallback<String[]> ()
									{
										public void onSuccess (String[] result)
										{
											encounterElementComboBox.clear ();
											for (String s : result)
												encounterElementComboBox.addItem (s);
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
							e.printStackTrace();
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
				e.printStackTrace();
			}
		}
		else if (sender == encounterElementComboBox)
		{
			try
			{
				service.findEncounterElement (currentType.getEncounterType (), FMClient.get (encounterElementComboBox),
						new AsyncCallback<EncounterElement> ()
						{
							public void onSuccess (EncounterElement result)
							{
								currentElement = result;
								try
								{
									service.getColumnData (tableName, "value", "where encounter_type='"
											+ result.getId ().getEncounterType () + "' and element='"
											+ result.getId ().getElement () + "'", new AsyncCallback<String[]> ()
									{
										public void onSuccess (String[] result)
										{
											elementValueListBox.clear ();
											for (String s : result)
												elementValueListBox.addItem (s);
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
									e.printStackTrace();
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
				e.printStackTrace();
			}
		}
		else if (sender == elementValueListBox)
		{
			fillData ();
		}
	}
}
