
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.Date;
import java.util.Iterator;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.AccessType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.InfoType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.UserRightsUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.User;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.UserMapping;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.UserMappingId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
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
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DatePicker;

public class UserMappingComposite extends Composite implements IForm, ClickHandler, ChangeHandler, ValueChangeHandler<String>
{
	private static ServerServiceAsync	service					= GWT.create (ServerService.class);
	private static LoadingWidget		loading					= new LoadingWidget ();
	private static final String			menuName				= "SETUP";

	private UserRightsUtil				rights					= new UserRightsUtil ();
	private boolean						valid;
	private UserMapping					currentMapping;

	private FlexTable					flexTable				= new FlexTable ();
	private FlexTable					topFlexTable			= new FlexTable ();
	private FlexTable					leftFlexTable			= new FlexTable ();
	private FlexTable					rightFlexTable			= new FlexTable ();
	private Grid						grid					= new Grid (1, 3);
	private Grid						weekdaysGrid			= new Grid (4, 2);
	private HorizontalPanel				timingHorizontalPanel	= new HorizontalPanel ();

	private ToggleButton				createButton			= new ToggleButton ("Create");
	private ToggleButton				toggleAllButton			= new ToggleButton ("Toggle All");
	private Button						saveButton				= new Button ("Save");
	private Button						deleteButton			= new Button ("Delete");
	private Button						closeButton				= new Button ("Close");

	private Label						label					= new Label (FM.getProjectTitle () + " User Mappings");
	private Label						lblUserName				= new Label ("User Name:");
	private Label						lblFacilityName			= new Label ("Facility Name:");
	private Label						lblCurentStatus			= new Label ("Curent Status:");
	private Label						lblWeekdays				= new Label ("Weekdays:");
	private Label						lblTiming				= new Label ("Timing:");
	private Label						lblTo					= new Label ("to");

	private TextBox						userNameTextBox			= new TextBox ();

	private DateBox						fromTimeBox				= new DateBox ();
	private DateBox						toTimeBox				= new DateBox ();

	private ListBox						facilitiesComboBox		= new ListBox ();
	private ListBox						usersListBox			= new ListBox ();
	private ListBox						facilityComboBox		= new ListBox ();
	private ListBox						currentStatusComboBox	= new ListBox ();

	private CheckBox					mondayCheckBox			= new CheckBox ("MONDAY");
	private CheckBox					tuesdayCheckBox			= new CheckBox ("TUESDAY");
	private CheckBox					wednesdayCheckBox		= new CheckBox ("WEDNESDAY");
	private CheckBox					thursdayCheckBox		= new CheckBox ("THURSDAY");
	private CheckBox					fridayCheckBox			= new CheckBox ("FRIDAY");
	private CheckBox					saturdayCheckBox		= new CheckBox ("SATURDAY");
	private CheckBox					sundayCheckBox			= new CheckBox ("SUNDAY");

	@SuppressWarnings("deprecation")
	public UserMappingComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 1, topFlexTable);
		label.setWordWrap (false);
		label.setStyleName ("title");
		topFlexTable.setWidget (0, 0, label);
		topFlexTable.getCellFormatter ().setHorizontalAlignment (0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		flexTable.setWidget (1, 0, leftFlexTable);
		leftFlexTable.setWidget (0, 0, facilitiesComboBox);
		usersListBox.setEnabled (false);
		leftFlexTable.setWidget (1, 0, usersListBox);
		usersListBox.setVisibleItemCount (10);
		flexTable.setWidget (1, 1, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		rightFlexTable.setWidget (0, 0, lblUserName);
		userNameTextBox.setName ("user;user_name");
		rightFlexTable.setWidget (0, 1, userNameTextBox);
		rightFlexTable.setWidget (1, 0, lblFacilityName);
		rightFlexTable.setWidget (1, 1, facilityComboBox);
		rightFlexTable.setWidget (2, 0, lblCurentStatus);
		currentStatusComboBox.setName ("USER_STATUS");
		rightFlexTable.setWidget (2, 1, currentStatusComboBox);
		rightFlexTable.setWidget (3, 0, lblWeekdays);
		rightFlexTable.setWidget (3, 1, weekdaysGrid);
		weekdaysGrid.setWidth ("100%");
		weekdaysGrid.setWidget (0, 0, mondayCheckBox);
		weekdaysGrid.setWidget (0, 1, fridayCheckBox);
		weekdaysGrid.setWidget (1, 0, tuesdayCheckBox);
		weekdaysGrid.setWidget (1, 1, saturdayCheckBox);
		weekdaysGrid.setWidget (2, 0, wednesdayCheckBox);
		weekdaysGrid.setWidget (2, 1, sundayCheckBox);
		weekdaysGrid.setWidget (3, 0, thursdayCheckBox);
		toggleAllButton.addClickHandler (new ClickHandler ()
		{
			public void onClick (ClickEvent event)
			{
				boolean state = toggleAllButton.isDown ();
				mondayCheckBox.setValue (state);
				tuesdayCheckBox.setValue (state);
				wednesdayCheckBox.setValue (state);
				thursdayCheckBox.setValue (state);
				fridayCheckBox.setValue (state);
				saturdayCheckBox.setValue (state);
				sundayCheckBox.setValue (state);
			}
		});
		weekdaysGrid.setWidget (3, 1, toggleAllButton);
		weekdaysGrid.getCellFormatter ().setHorizontalAlignment (3, 1, HasHorizontalAlignment.ALIGN_CENTER);
		rightFlexTable.setWidget (4, 0, lblTiming);
		rightFlexTable.setWidget (4, 1, timingHorizontalPanel);
		fromTimeBox.setFormat (new DefaultFormat (DateTimeFormat.getMediumTimeFormat ()));
		timingHorizontalPanel.add (fromTimeBox);
		fromTimeBox.setWidth ("50px");
		timingHorizontalPanel.add (lblTo);
		timingHorizontalPanel.setCellVerticalAlignment (lblTo, HasVerticalAlignment.ALIGN_MIDDLE);
		toTimeBox.setFormat (new DefaultFormat (DateTimeFormat.getMediumTimeFormat ()));
		timingHorizontalPanel.add (toTimeBox);
		toTimeBox.setWidth ("50px");
		createButton.setEnabled (false);
		rightFlexTable.setWidget (5, 0, createButton);
		rightFlexTable.setWidget (5, 1, grid);
		grid.setSize ("100%", "100%");
		saveButton.setEnabled (false);
		grid.setWidget (0, 0, saveButton);
		deleteButton.setEnabled (false);
		grid.setWidget (0, 1, deleteButton);
		grid.setWidget (0, 2, closeButton);
		rightFlexTable.getCellFormatter ().setVerticalAlignment (3, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getRowFormatter ().setVerticalAlign (1, HasVerticalAlignment.ALIGN_TOP);

		createButton.addClickHandler (this);
		saveButton.addClickHandler (this);
		deleteButton.addClickHandler (this);
		closeButton.addClickHandler (this);
		usersListBox.addChangeHandler (this);
		userNameTextBox.addValueChangeHandler (this);
		facilitiesComboBox.addChangeHandler (this);

		refresh (flexTable);
		setRights (menuName);
		load (true);
		try
		{
			load (true);
			service.getTableData ("select location_name, location_id from location where location_type='FACILITY'", new AsyncCallback<String[][]> ()
			{
				public void onSuccess (String[][] result)
				{
					facilityComboBox.clear ();
					facilitiesComboBox.clear ();
					for (int i = 0; i < result.length; i++)
					{
						facilityComboBox.addItem (result[i][0], result[i][1]);
						facilitiesComboBox.addItem (result[i][0], result[i][1]);
					}
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
		String status = FMClient.get (currentStatusComboBox);
		currentMapping.setCurrentStatus (status);
		currentMapping.setTimeFrom (fromTimeBox.getValue ());
		currentMapping.setTimeTo (toTimeBox.getValue ());
		char mon, tue, wed, thu, fri, sat, sun;
		mon = mondayCheckBox.getValue () ? '1' : '0';
		tue = tuesdayCheckBox.getValue () ? '1' : '0';
		wed = wednesdayCheckBox.getValue () ? '1' : '0';
		thu = thursdayCheckBox.getValue () ? '1' : '0';
		fri = fridayCheckBox.getValue () ? '1' : '0';
		sat = saturdayCheckBox.getValue () ? '1' : '0';
		sun = sundayCheckBox.getValue () ? '1' : '0';
		StringBuilder weekdays = new StringBuilder ();
		weekdays.append (mon);
		weekdays.append (tue);
		weekdays.append (wed);
		weekdays.append (thu);
		weekdays.append (fri);
		weekdays.append (sat);
		weekdays.append (sun);
		currentMapping.setWeekDays (weekdays.toString ());
	}

	public void fillData ()
	{
		try
		{
			UserMappingId userMappingId = new UserMappingId (FMClient.get (usersListBox), FMClient.get (facilitiesComboBox));
			service.findUserMapping (userMappingId, new AsyncCallback<UserMapping> ()
			{
				public void onSuccess (UserMapping result)
				{
					currentMapping = result;
					String userName = usersListBox.getItemText (FMClient.getIndex (usersListBox, FMClient.get (usersListBox)));
					userNameTextBox.setText (userName);
					String locationId = String.valueOf (currentMapping.getId ().getLocationId ());
					facilityComboBox.setSelectedIndex (FMClient.getIndex (facilityComboBox, locationId));
					currentStatusComboBox.setSelectedIndex (FMClient.getIndex (currentStatusComboBox, currentMapping.getCurrentStatus ()));
					String weekdays = currentMapping.getWeekDays ();
					if (weekdays != null && weekdays.length () == 7)
					{
						boolean mon = weekdays.charAt (0) == '1';
						boolean tue = weekdays.charAt (1) == '1';
						boolean wed = weekdays.charAt (2) == '1';
						boolean thu = weekdays.charAt (3) == '1';
						boolean fri = weekdays.charAt (4) == '1';
						boolean sat = weekdays.charAt (5) == '1';
						boolean sun = weekdays.charAt (6) == '1';
						mondayCheckBox.setValue (mon);
						tuesdayCheckBox.setValue (tue);
						wednesdayCheckBox.setValue (wed);
						thursdayCheckBox.setValue (thu);
						fridayCheckBox.setValue (fri);
						saturdayCheckBox.setValue (sat);
						sundayCheckBox.setValue (sun);
					}
					fromTimeBox.getTextBox ().setValue (String.valueOf (currentMapping.getTimeFrom ()));
					toTimeBox.getTextBox ().setValue (String.valueOf (currentMapping.getTimeTo ()));
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
		/* Validate mandatory fields */
		if (FMClient.get (facilityComboBox).equals (""))
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
				UserMappingId mappingId = new UserMappingId (FMClient.get (userNameTextBox).toUpperCase (), FMClient.get (facilityComboBox));
				currentMapping = new UserMapping (mappingId);
				setCurrent ();
				service.saveUserMapping (currentMapping, new AsyncCallback<Boolean> ()
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
				service.updateUserMapping (currentMapping, new AsyncCallback<Boolean> ()
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
				service.deleteUserMapping (currentMapping, new AsyncCallback<Boolean> ()
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
								usersListBox.setEnabled (rights.getAccess (AccessType.SELECT));
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
			usersListBox.setEnabled (!createButton.isDown ());
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
		if (sender == facilitiesComboBox)
		{
			try
			{
				service.getTableData ("select u.user_name, m.user_id from user_mapping as m inner join user as u on m.user_id = u.pid where location_id='" + FMClient.get (facilitiesComboBox) + "'",
						new AsyncCallback<String[][]> ()
						{
							public void onSuccess (String[][] result)
							{
								usersListBox.clear ();
								for (int i = 0; i < result.length; i++)
									usersListBox.addItem (result[i][0], result[i][1]);
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
		if (sender == usersListBox)
		{
			fillData ();
		}
	}

	public void onValueChange (ValueChangeEvent<String> event)
	{
		Widget sender = (Widget) event.getSource ();
		if (sender == userNameTextBox)
		{
			try
			{
				if (FMClient.get (userNameTextBox).equals (""))
					return;
				service.findUser (FMClient.get (userNameTextBox), new AsyncCallback<User> ()
				{
					public void onSuccess (User result)
					{
						if (result == null)
						{
							Window.alert (CustomMessage.getErrorMessage (ErrorType.USER_NOT_FOUND));
							userNameTextBox.setFocus (true);
						}
					}

					public void onFailure (Throwable caught)
					{
					}
				});
			}
			catch (Exception e)
			{
				e.printStackTrace ();
			}
		}
	}
}
