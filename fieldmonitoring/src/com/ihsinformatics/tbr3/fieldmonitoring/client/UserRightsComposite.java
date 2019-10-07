
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
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.UserRights;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.UserRightsId;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class UserRightsComposite extends Composite implements IForm, ClickHandler, ChangeHandler
{
	private static ServerServiceAsync	service			= GWT.create (ServerService.class);
	private static LoadingWidget		loading			= new LoadingWidget ();
	private static final String			menuName		= "SETUP";
	private static final String			tableName		= "user_rights";

	private UserRightsUtil				rights			= new UserRightsUtil ();
	private UserRights					current;

	private FlexTable					flexTable		= new FlexTable ();
	private FlexTable					topFlexTable	= new FlexTable ();
	private FlexTable					leftFlexTable	= new FlexTable ();
	private FlexTable					rightFlexTable	= new FlexTable ();
	private FlexTable					accessFlexTable	= new FlexTable ();
	private Grid						grid			= new Grid (1, 3);

	private ToggleButton				createButton	= new ToggleButton ("Create");
	private Button						saveButton		= new Button ("Save");
	private Button						deleteButton	= new Button ("Delete");
	private Button						closeButton		= new Button ("Close");

	private Label						lblTbReachUser	= new Label (FM.getProjectTitle () + " User Rights");
	private Label						lblDataItem		= new Label ("Data Item:");
	private Label						lblAccess		= new Label ("Access:");

	private ListBox						menuComboBox	= new ListBox ();
	private ListBox						rolesListBox	= new ListBox ();

	private CheckBox					searchCheckBox	= new CheckBox ("Search");
	private CheckBox					insertCheckBox	= new CheckBox ("Insert");
	private CheckBox					updateCheckBox	= new CheckBox ("Update");
	private CheckBox					deleteCheckBox	= new CheckBox ("Delete");
	private CheckBox					printCheckBox	= new CheckBox ("Print");

	public UserRightsComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 1, topFlexTable);
		lblTbReachUser.setWordWrap (false);
		lblTbReachUser.setStyleName ("title");
		topFlexTable.setWidget (0, 0, lblTbReachUser);
		flexTable.setWidget (1, 0, leftFlexTable);
		rolesListBox.setName ("USER_ROLE");
		rolesListBox.setEnabled (false);
		rolesListBox.clear ();
		leftFlexTable.setWidget (0, 0, rolesListBox);
		rolesListBox.setVisibleItemCount (5);
		flexTable.setWidget (1, 1, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		lblDataItem.setWordWrap (false);
		rightFlexTable.setWidget (0, 0, lblDataItem);
		menuComboBox.setName ("MENU_NAME");
		rightFlexTable.setWidget (0, 1, menuComboBox);
		menuComboBox.setWidth ("");
		lblAccess.setWordWrap (false);
		rightFlexTable.setWidget (1, 0, lblAccess);
		rightFlexTable.setWidget (1, 1, accessFlexTable);
		accessFlexTable.setWidth ("100%");
		accessFlexTable.setWidget (0, 0, insertCheckBox);
		accessFlexTable.setWidget (0, 1, searchCheckBox);
		accessFlexTable.setWidget (1, 0, updateCheckBox);
		accessFlexTable.setWidget (1, 1, printCheckBox);
		accessFlexTable.setWidget (2, 0, deleteCheckBox);
		createButton.setEnabled (false);
		createButton.setVisible (false);
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
		menuComboBox.addChangeHandler (this);

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
		current.setSearchAccess (searchCheckBox.getValue ());
		current.setInsertAccess (insertCheckBox.getValue ());
		current.setUpdateAccess (updateCheckBox.getValue ());
		current.setDeleteAccess (deleteCheckBox.getValue ());
		current.setPrintAccess (printCheckBox.getValue ());
	}

	public void clearUp ()
	{
		clearControls (leftFlexTable);
	}

	public boolean validate ()
	{
		return true;
	}

	public void saveData ()
	{
		if (validate ())
		{
			try
			{
				// Not implemented
			}
			catch (Exception e)
			{
				e.printStackTrace ();
			}
		}
	}

	public void updateData ()
	{
		if (validate ())
		{
			setCurrent ();
			try
			{
				service.updateUserRights (current, new AsyncCallback<Boolean> ()
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
				// Not implemented
			}
			catch (Exception e)
			{
				e.printStackTrace ();
			}
		}
	}

	public void fillData ()
	{
		try
		{
			service.getRowRecord (tableName, new String[] {"search_access", "insert_access", "update_access", "delete_access", "print_access"}, "user_role='" + FMClient.get (rolesListBox)
					+ "' and menu_name='" + FMClient.get (menuComboBox) + "'", new AsyncCallback<String[]> ()
			{
				public void onSuccess (String[] result)
				{
					Boolean[] rights = new Boolean[result.length];
					for (int i = 0; i < result.length; i++)
						rights[i] = Boolean.parseBoolean (result[i]);
					current = new UserRights (new UserRightsId (FMClient.get (rolesListBox), FMClient.get (menuComboBox)), rights[0], rights[1], rights[2], rights[3], rights[4]);
					searchCheckBox.setValue (current.isSearchAccess ());
					insertCheckBox.setValue (current.isInsertAccess ());
					updateCheckBox.setValue (current.isUpdateAccess ());
					deleteCheckBox.setValue (current.isDeleteAccess ());
					printCheckBox.setValue (current.isPrintAccess ());
					load (false);
				}

				public void onFailure (Throwable caught)
				{
					// If User Rights are not available, try to Insert
					try
					{
						UserRightsId userRightsId = new UserRightsId (FMClient.get (rolesListBox), FMClient.get (menuComboBox));
						UserRights userRights = new UserRights (userRightsId, false, false, false, false, false);
						service.saveUserRights (userRights, new AsyncCallback<Boolean> ()
						{
							public void onSuccess (Boolean result)
							{
								// Inserted
								fillData ();
							}

							public void onFailure (Throwable caught)
							{
								Window.alert (CustomMessage.getErrorMessage (ErrorType.DATA_ACCESS_ERROR));
							}
						});
					}
					catch (Exception e)
					{
						e.printStackTrace ();
					}
					load (false);
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace ();
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
								rolesListBox.setEnabled (rights.getAccess (AccessType.SELECT));
								createButton.setEnabled (false);
								saveButton.setEnabled (rights.getAccess (AccessType.UPDATE));
								deleteButton.setEnabled (false);
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
			rolesListBox.setEnabled (!createButton.isDown ());
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
		if (sender == menuComboBox)
		{
			fillData ();
		}
	}
}
