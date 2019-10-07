
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.AccessType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.InfoType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.UserRightsUtil;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.PersonRole;
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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class UsersComposite extends Composite implements IForm, ClickHandler, ChangeHandler
{
	private static ServerServiceAsync	service						= GWT.create (ServerService.class);
	private static LoadingWidget		loading						= new LoadingWidget ();
	private static final String			menuName					= "SETUP";

	private UserRightsUtil				rights						= new UserRightsUtil ();
	private boolean						valid;
	private User						currentUser;
	private String[]					currentRoles;

	private FlexTable					flexTable					= new FlexTable ();
	private FlexTable					leftFlexTable				= new FlexTable ();
	private FlexTable					rightFlexTable				= new FlexTable ();
	private FlexTable					topFlexTable				= new FlexTable ();
	private Grid						grid						= new Grid (1, 3);

	private ToggleButton				createButton				= new ToggleButton ("Create User");
	private Button						saveButton					= new Button ("Save");
	private Button						deleteButton				= new Button ("Delete");
	private Button						closeButton					= new Button ("Close");

	private Label						lblTbReachUsers				= new Label (FM.getProjectTitle () + " Users");
	private Label						lblUserName					= new Label ("User Name:");
	private Label						lblUserRoles				= new Label ("User Roles:");
	private Label						lblStatus					= new Label ("Status:");
	private Label						lblPassword					= new Label ("Password:");
	private Label						lblSecretQuestion			= new Label ("Secret Question:");
	private Label						lblSecretAnswer				= new Label ("Secret Answer:");

	private TextBox						userNameTextBox				= new TextBox ();

	private PasswordTextBox				passwordTextBox				= new PasswordTextBox ();
	private PasswordTextBox				secretAnswerPasswordTextBox	= new PasswordTextBox ();

	private ListBox						userRolesListBox			= new ListBox ();
	private ListBox						userStatusComboBox			= new ListBox ();
	private ListBox						secretQuestionComboBox		= new ListBox ();
	private ListBox						userRolesComboBox			= new ListBox ();
	private ListBox						usersListBox				= new ListBox ();

	@SuppressWarnings("deprecation")
	public UsersComposite ()
	{
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 1, topFlexTable);
		lblTbReachUsers.setStyleName ("title");
		topFlexTable.setWidget (0, 0, lblTbReachUsers);
		flexTable.setWidget (1, 0, leftFlexTable);
		leftFlexTable.setSize ("100%", "100%");
		userRolesComboBox.setTitle ("This list contains User Roles. Selecting anyone fills User names in the list box below.");
		userRolesComboBox.setName ("USER_ROLE");
		userRolesComboBox.setEnabled (false);
		leftFlexTable.setWidget (0, 0, userRolesComboBox);
		usersListBox.setTitle ("This list contains existing User Names assigned the Role selected. Clicking anyone fills the details in right panel.");
		usersListBox.addClickHandler (this);
		leftFlexTable.setWidget (1, 0, usersListBox);
		usersListBox.setVisibleItemCount (5);
		leftFlexTable.getCellFormatter ().setHorizontalAlignment (1, 0, HasHorizontalAlignment.ALIGN_LEFT);
		leftFlexTable.getCellFormatter ().setVerticalAlignment (1, 0, HasVerticalAlignment.ALIGN_TOP);
		leftFlexTable.getCellFormatter ().setVerticalAlignment (0, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.setWidget (1, 1, rightFlexTable);
		rightFlexTable.setSize ("100%", "100%");
		lblUserName.setWordWrap (false);
		rightFlexTable.setWidget (0, 0, lblUserName);
		userNameTextBox.setTitle ("User names should be unique.");
		rightFlexTable.setWidget (0, 1, userNameTextBox);
		lblUserRoles.setWordWrap (false);
		rightFlexTable.setWidget (1, 0, lblUserRoles);
		userRolesListBox.setTitle ("List of User Roles to be assigned to a User. Multiple values can also be selected.");
		userRolesListBox.setName ("USER_ROLE");
		userRolesListBox.setMultipleSelect (true);
		rightFlexTable.setWidget (1, 1, userRolesListBox);
		userRolesListBox.setWidth ("100%");
		userRolesListBox.setVisibleItemCount (5);
		lblStatus.setWordWrap (false);
		rightFlexTable.setWidget (2, 0, lblStatus);
		userStatusComboBox.setTitle ("User's status. If this is set to Suspended, User will not be able to log into Web/Mobile application.");
		userStatusComboBox.setName ("USER_STATUS");
		rightFlexTable.setWidget (2, 1, userStatusComboBox);
		lblPassword.setWordWrap (false);
		rightFlexTable.setWidget (3, 0, lblPassword);
		passwordTextBox.setTitle ("Password should be of at least 8 characters. The password is not stored as plain text but a code from which the password cannot be found out.");
		passwordTextBox.setMaxLength (20);
		rightFlexTable.setWidget (3, 1, passwordTextBox);
		lblSecretQuestion.setWordWrap (false);
		rightFlexTable.setWidget (4, 0, lblSecretQuestion);
		secretQuestionComboBox.addItem ("WHO IS YOUR FAVOURITE NATIONAL HERO?");
		secretQuestionComboBox.addItem ("WHAT PHONE MODEL ARE YOU PLANNING TO PURCHASE NEXT?");
		secretQuestionComboBox.addItem ("WHERE WAS YOUR MOTHER BORN?");
		secretQuestionComboBox.addItem ("WHEN DID YOU BUY YOUR FIRST CAR?");
		secretQuestionComboBox.addItem ("WHAT WAS YOUR CHILDHOOD NICKNAME?");
		secretQuestionComboBox.addItem ("WHAT IS YOUR FAVOURITE CARTOON CHARACTER?");
		rightFlexTable.setWidget (4, 1, secretQuestionComboBox);
		lblSecretAnswer.setWordWrap (false);
		rightFlexTable.setWidget (5, 0, lblSecretAnswer);
		secretAnswerPasswordTextBox.setTitle ("Answer to Secret question is used for password recovery on login screen. This cannot be changed.");
		secretAnswerPasswordTextBox.setMaxLength (20);
		rightFlexTable.setWidget (5, 1, secretAnswerPasswordTextBox);
		createButton.setEnabled (false);
		rightFlexTable.setWidget (6, 0, createButton);
		rightFlexTable.setWidget (6, 1, grid);
		grid.setSize ("100%", "100%");
		saveButton.setEnabled (false);
		grid.setWidget (0, 0, saveButton);
		deleteButton.setEnabled (false);
		grid.setWidget (0, 1, deleteButton);
		grid.setWidget (0, 2, closeButton);
		rightFlexTable.getCellFormatter ().setVerticalAlignment (1, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter ().setVerticalAlignment (1, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter ().setHorizontalAlignment (1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter ().setVerticalAlignment (1, 1, HasVerticalAlignment.ALIGN_TOP);

		createButton.addClickHandler (this);
		saveButton.addClickHandler (this);
		deleteButton.addClickHandler (this);
		closeButton.addClickHandler (this);
		userRolesComboBox.addChangeHandler (this);

		refresh (flexTable);
		setRights (menuName.toUpperCase ());
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

	public void clearUp ()
	{
		clearControls (rightFlexTable);
		usersListBox.clear ();
		userNameTextBox.setFocus (true);
	}

	public void setCurrent ()
	{
		currentUser.setCurrentStatus (FMClient.get (userStatusComboBox).charAt (0));
		currentUser.setSecretQuestion (FMClient.get (secretQuestionComboBox));
		// Set current roles selected
		ArrayList<String> roles = new ArrayList<String> ();
		for (int i = 0; i < userRolesComboBox.getItemCount (); i++)
			if (userRolesListBox.isItemSelected (i))
				roles.add (FM.getDefinitionKey (userRolesListBox.getName (), userRolesListBox.getItemText (i)));
		currentRoles = new String[roles.size ()];
		for (int i = 0; i < roles.size (); i++)
			currentRoles[i] = roles.get (i);
	}

	public void fillData ()
	{
		try
		{
			service.findUser (FMClient.get (usersListBox), new AsyncCallback<User> ()
			{
				public void onSuccess (User result)
				{
					currentUser = result;
					userNameTextBox.setValue (result.getUserName ());
					userStatusComboBox.setSelectedIndex (FMClient.getIndex (userStatusComboBox, String.valueOf (result.getCurrentStatus ())));
					passwordTextBox.setValue (currentUser.getPassword ());
					secretQuestionComboBox.setSelectedIndex (FMClient.getIndex (secretQuestionComboBox, result.getSecretQuestion ()));
					secretAnswerPasswordTextBox.setValue (currentUser.getSecretAnswer ());
					load (false);
					try
					{
						service.findPersonRoles (result.getPid (), new AsyncCallback<PersonRole[]> ()
						{
							@Override
							public void onSuccess (PersonRole[] result)
							{
								currentRoles = new String[result.length];
								userRolesListBox.clear ();
								for (String s : FM.getDefinitionValues ("USER_ROLE"))
									userRolesListBox.addItem (s);
								for (int i = 0; i < result.length; i++)
								{
									currentRoles[i] = result[i].getId ().getRole ();
									userRolesListBox.setItemSelected (FMClient.getIndex (userRolesListBox, FM.getDefinitionValue (userRolesListBox.getName (), currentRoles[i])), true);
								}
							}

							@Override
							public void onFailure (Throwable caught)
							{
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
	 * Validation Rules for Users form
	 * 
	 * 1. All fields are mandatory
	 * 
	 * 2. User name is unique and cannot contain spaces
	 * 
	 * 3. Password must be of at least 8 characters
	 */
	public boolean validate ()
	{
		final StringBuilder errorMessage = new StringBuilder ();
		valid = true;
		/* Validate mandatory fields */
		if (userNameTextBox.getText ().equals ("") || passwordTextBox.getText ().equals ("") || secretAnswerPasswordTextBox.getText ().equals (""))
		{
			errorMessage.append (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR) + "\n");
			valid = false;
		}
		/* Validate data-type rules */
		if (userNameTextBox.getText ().contains (" "))
		{
			errorMessage.append (CustomMessage.getErrorMessage (ErrorType.INVALID_DATA_ERROR) + "\n");
			valid = false;
		}
		/* Check if at least 1 role was selected */
		if (userRolesListBox.getSelectedIndex () == -1)
		{
			errorMessage.append ("No role was selected. You must assign at least one role to the user\n");
			valid = false;
		}
		/* Validate password */
		if (passwordTextBox.getText ().length () < 8)
		{
			errorMessage.append ("Password is too short. Please enter a strong password of at least 8 characters\n");
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
			currentUser = new User ();
			try
			{
				currentUser.setPid (FMClient.get (userNameTextBox).toUpperCase ());
				currentUser.setUserName (FMClient.get (userNameTextBox).toUpperCase ());
				currentUser.setLoggedIn (false);
				currentUser.setPassword (FMClient.get (passwordTextBox));
				currentUser.setSecretAnswer (FMClient.get (secretAnswerPasswordTextBox));
				setCurrent ();
				service.saveUser (currentUser, currentRoles, new AsyncCallback<Boolean> ()
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
	}

	public void updateData ()
	{
		if (validate ())
		{
			setCurrent ();
			try
			{
				service.updateUser (currentUser, currentRoles, new AsyncCallback<Boolean> ()
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
				Window.alert (CustomMessage.getErrorMessage (ErrorType.UPDATE_ERROR));
			}
		}
	}

	public void deleteData ()
	{
		if (validate ())
		{
			try
			{
				service.deleteUser (currentUser, new AsyncCallback<Boolean> ()
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
				Window.alert (CustomMessage.getErrorMessage (ErrorType.DELETE_ERROR));
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
								userRolesComboBox.setEnabled (rights.getAccess (AccessType.SELECT));
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
		if (sender == usersListBox)
		{
			fillData ();
		}
		else if (sender == createButton)
		{
			if (createButton.isDown ())
				clearUp ();
			userRolesComboBox.setEnabled (!createButton.isDown ());
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
		load (true);
		String selectedValue = FMClient.get (userRolesComboBox);
		try
		{
			service.getColumnData ("user", "user_name", "where pid in (select pid from person_role where role='" + selectedValue + "')", new AsyncCallback<String[]> ()
			{
				public void onSuccess (String[] result)
				{
					usersListBox.clear ();
					for (int i = 0; i < result.length; i++)
						usersListBox.insertItem (result[i], i);
					load (false);
				}

				public void onFailure (Throwable caught)
				{
					Window.alert (CustomMessage.getErrorMessage (ErrorType.ITEM_NOT_FOUND));
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
