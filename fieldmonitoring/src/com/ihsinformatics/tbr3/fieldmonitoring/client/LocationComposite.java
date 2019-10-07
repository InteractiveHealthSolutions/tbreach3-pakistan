
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.Date;
import java.util.Iterator;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.AccessType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.InfoType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.UserRightsUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Location;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.User;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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

public class LocationComposite extends Composite implements IForm, ClickHandler, ChangeHandler
{
	private static ServerServiceAsync	service					= GWT.create (ServerService.class);
	private static LoadingWidget		loading					= new LoadingWidget ();
	private static final String			menuName				= "SETUP";

	private UserRightsUtil				rights					= new UserRightsUtil ();
	private boolean						valid;
	private Location					currentLocation;

	private FlexTable					flexTable				= new FlexTable ();
	private FlexTable					topFlexTable			= new FlexTable ();
	private FlexTable					leftFlexTable			= new FlexTable ();
	private FlexTable					rightFlexTable			= new FlexTable ();
	private Grid						grid					= new Grid (1, 3);

	private ToggleButton				createButton			= new ToggleButton ("Create");
	private Button						saveButton				= new Button ("Save");
	private Button						deleteButton			= new Button ("Delete");
	private Button						closeButton				= new Button ("Close");

	private Label						label					= new Label (FM.getProjectTitle () + " Locations");
	private Label						lblLocationId			= new Label ("Location ID:");
	private Label						lblLocationName			= new Label ("Location Name:");
	private Label						lblLocationType			= new Label ("Location Type:");
	private Label						lblAddress				= new Label ("Address 1:");
	private Label						lblAddress_1			= new Label ("Address 2:");
	private Label						lblCity					= new Label ("City:");
	private Label						lblStateprovince		= new Label ("State/Province:");
	private Label						lblCountry				= new Label ("Country:");
	private Label						lblRegion				= new Label ("Region:");
	private Label						lblPhone				= new Label ("Phone:");
	private Label						lblMobile				= new Label ("Mobile:");
	private Label						lblFax					= new Label ("Fax:");
	private Label						lblEmailAddress			= new Label ("Email Address:");

	private TextBox						locationIdTextBox		= new TextBox ();
	private TextBox						locationNameTextBox		= new TextBox ();
	private TextBox						address1TextBox			= new TextBox ();
	private TextBox						address2TextBox			= new TextBox ();
	private TextBox						cityTextBox				= new TextBox ();
	private TextBox						stateTextBox			= new TextBox ();
	private TextBox						phoneTextBox			= new TextBox ();
	private TextBox						mobileTextBox			= new TextBox ();
	private TextBox						faxTextBox				= new TextBox ();
	private TextBox						emailTextBox			= new TextBox ();

	private ListBox						locationNamesListBox	= new ListBox ();
	private ListBox						locationTypesComboBox	= new ListBox ();
	private ListBox						locationTypeComboBox	= new ListBox ();
	private ListBox						countryComboBox			= new ListBox ();
	private ListBox						regionComboBox			= new ListBox ();

	public LocationComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 1, topFlexTable);
		label.setWordWrap (false);
		label.setStyleName ("title");
		topFlexTable.setWidget (0, 0, label);
		topFlexTable.getCellFormatter ().setHorizontalAlignment (0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.setWidget (1, 0, leftFlexTable);
		locationTypesComboBox.setName ("LOCATION_TYPE");
		locationTypesComboBox.setTitle ("This box contains Location Types from Definition. Selecting anyone fills the list box below.");
		leftFlexTable.setWidget (0, 0, locationTypesComboBox);
		locationNamesListBox.setTitle ("This list box contains Location names of selected type. Clicking anyone fills details in right panel.");
		leftFlexTable.setWidget (1, 0, locationNamesListBox);
		locationNamesListBox.setVisibleItemCount (10);
		flexTable.setWidget (1, 1, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		rightFlexTable.setWidget (0, 0, lblLocationId);
		locationIdTextBox.setVisibleLength (5);
		locationIdTextBox.setEnabled (false);
		locationIdTextBox.setName ("location;location_name");
		rightFlexTable.setWidget (0, 1, locationIdTextBox);
		rightFlexTable.setWidget (1, 0, lblLocationName);
		locationNameTextBox.setVisibleLength (35);
		locationNameTextBox.setName ("location;location_name");
		rightFlexTable.setWidget (1, 1, locationNameTextBox);
		rightFlexTable.setWidget (2, 0, lblLocationType);
		locationTypeComboBox.setName ("LOCATION_TYPE");
		rightFlexTable.setWidget (2, 1, locationTypeComboBox);
		rightFlexTable.setWidget (3, 0, lblAddress);
		address1TextBox.setVisibleLength (35);
		address1TextBox.setName ("location;address1");
		rightFlexTable.setWidget (3, 1, address1TextBox);
		rightFlexTable.setWidget (4, 0, lblAddress_1);
		address2TextBox.setVisibleLength (35);
		address2TextBox.setName ("location;address2");
		rightFlexTable.setWidget (4, 1, address2TextBox);
		rightFlexTable.setWidget (5, 0, lblCity);
		cityTextBox.setName ("location;city");
		rightFlexTable.setWidget (5, 1, cityTextBox);
		rightFlexTable.setWidget (6, 0, lblStateprovince);
		stateTextBox.setName ("location;state");
		rightFlexTable.setWidget (6, 1, stateTextBox);
		rightFlexTable.setWidget (7, 0, lblCountry);
		countryComboBox.setName ("COUNTRY");
		rightFlexTable.setWidget (7, 1, countryComboBox);
		rightFlexTable.setWidget (8, 0, lblRegion);
		regionComboBox.setName ("REGION");
		rightFlexTable.setWidget (8, 1, regionComboBox);
		rightFlexTable.setWidget (9, 0, lblPhone);
		phoneTextBox.setName ("location;phone");
		rightFlexTable.setWidget (9, 1, phoneTextBox);
		rightFlexTable.setWidget (10, 0, lblMobile);
		mobileTextBox.setName ("location;mobile");
		rightFlexTable.setWidget (10, 1, mobileTextBox);
		rightFlexTable.setWidget (11, 0, lblFax);
		faxTextBox.setName ("location;fax");
		rightFlexTable.setWidget (11, 1, faxTextBox);
		rightFlexTable.setWidget (12, 0, lblEmailAddress);
		emailTextBox.setVisibleLength (35);
		emailTextBox.setName ("location;email");
		rightFlexTable.setWidget (12, 1, emailTextBox);
		createButton.setEnabled (false);
		rightFlexTable.setWidget (13, 0, createButton);
		rightFlexTable.setWidget (13, 1, grid);
		grid.setSize ("100%", "100%");
		saveButton.setEnabled (false);
		grid.setWidget (0, 0, saveButton);
		deleteButton.setEnabled (false);
		grid.setWidget (0, 1, deleteButton);
		grid.setWidget (0, 2, closeButton);
		flexTable.getRowFormatter ().setVerticalAlign (1, HasVerticalAlignment.ALIGN_TOP);

		locationTypesComboBox.addChangeHandler (this);
		locationNamesListBox.addChangeHandler (this);
		createButton.addClickHandler (this);
		saveButton.addClickHandler (this);
		deleteButton.addClickHandler (this);
		closeButton.addClickHandler (this);

		refresh (flexTable);
		setRights (menuName);
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
		currentLocation.setAddress1 (FMClient.get (address1TextBox).toUpperCase ());
		currentLocation.setAddress2 (FMClient.get (address2TextBox).toUpperCase ());
		currentLocation.setAddress3 ("");
		currentLocation.setAddress4 ("");
		currentLocation.setCity (FMClient.get (cityTextBox).toUpperCase ());
		currentLocation.setState (FMClient.get (stateTextBox).toUpperCase ());
		currentLocation.setCountry (FMClient.getKey (countryComboBox));
		currentLocation.setRegion (FMClient.getKey (regionComboBox));
		currentLocation.setPhone (FMClient.get (phoneTextBox));
		currentLocation.setMobile (FMClient.get (mobileTextBox));
		currentLocation.setFax (FMClient.get (faxTextBox));
		currentLocation.setEmail (FMClient.get (emailTextBox).toUpperCase ());
	}

	public void fillData ()
	{
		try
		{
			service.findLocation (FMClient.get (locationNamesListBox), new AsyncCallback<Location> ()
			{
				@Override
				public void onSuccess (Location result)
				{
					if (result == null)
						Window.alert (CustomMessage.getErrorMessage (ErrorType.ITEM_NOT_FOUND));
					else
					{
						currentLocation = result;
						locationIdTextBox.setValue (currentLocation.getLocationId ());
						locationNameTextBox.setValue (currentLocation.getLocationName ());
						address1TextBox.setValue (currentLocation.getAddress1 ());
						address2TextBox.setValue (currentLocation.getAddress2 ());
						cityTextBox.setValue (currentLocation.getCity ());
						stateTextBox.setValue (currentLocation.getState ());
						countryComboBox.setSelectedIndex (FMClient.getIndex (countryComboBox, currentLocation.getCountry ()));
						String region = FM.getDefinitionValue (regionComboBox.getName (), currentLocation.getRegion ());
						regionComboBox.setSelectedIndex (FMClient.getIndex (regionComboBox, region));
						phoneTextBox.setValue (currentLocation.getPhone ());
						mobileTextBox.setValue (currentLocation.getMobile ());
						faxTextBox.setValue (currentLocation.getFax ());
						emailTextBox.setValue (currentLocation.getEmail ());
					}
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

	public void clearUp ()
	{
		clearControls (flexTable);
	}

	public boolean validate ()
	{
		final StringBuilder errorMessage = new StringBuilder ();
		valid = true;
		/* Validate mandatory fields */
		if (FMClient.get (locationIdTextBox).equals ("") || FMClient.get (locationNameTextBox).equals ("") || FMClient.get (locationTypeComboBox).equals (""))
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
				currentLocation = new Location (FMClient.get (locationIdTextBox).toUpperCase (), FMClient.get (locationNameTextBox).toUpperCase (), FMClient.getKey (locationTypeComboBox));
				setCurrent ();
				service.saveLocation (currentLocation, new AsyncCallback<Boolean> ()
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
				load (false);
			}
		}
	}

	public void updateData ()
	{
		if (validate ())
		{
			try
			{
				setCurrent ();
				service.updateLocation (currentLocation, new AsyncCallback<Boolean> ()
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
				load (false);
			}
		}
	}

	public void deleteData ()
	{
		if (validate ())
		{
			try
			{
				service.deleteLocation (currentLocation, new AsyncCallback<Boolean> ()
				{
					public void onSuccess (Boolean result)
					{
						if (result)
						{
							Window.alert (CustomMessage.getInfoMessage (InfoType.DELETED));
							clearUp ();
							locationNamesListBox.removeItem (FMClient.getIndex (locationNamesListBox, currentLocation.getLocationId ()));
						}
						else
							Window.alert (CustomMessage.getErrorMessage (ErrorType.DELETE_ERROR));
						load (false);
					}

					public void onFailure (Throwable caught)
					{
						Window.alert (CustomMessage.getErrorMessage (ErrorType.DELETE_ERROR));
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
								locationTypeComboBox.setEnabled (rights.getAccess (AccessType.SELECT));
								locationNamesListBox.setEnabled (rights.getAccess (AccessType.SELECT));
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
		if (sender == createButton)
		{
			if (createButton.isDown ())
				clearUp ();
			locationIdTextBox.setEnabled (createButton.isDown ());
			locationTypesComboBox.setEnabled (!createButton.isDown ());
			locationNamesListBox.setEnabled (!createButton.isDown ());
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
			MainMenuComposite.clear ();
		}
	}

	public void onChange (ChangeEvent event)
	{
		Widget sender = (Widget) event.getSource ();
		load (true);
		if (sender == locationTypesComboBox)
		{
			try
			{
				String locationType = FMClient.getKey (locationTypesComboBox);
				service.findLocationsByType (locationType, new AsyncCallback<Location[]>()
				{
					public void onSuccess (Location[] result)
					{
						locationNamesListBox.clear ();
						for (Location s : result)
							locationNamesListBox.addItem (s.getLocationName (), s.getLocationId ());
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
		else if (sender == locationNamesListBox)
		{
			fillData ();
		}
	}
}
