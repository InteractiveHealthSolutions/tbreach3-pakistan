
package com.ihsinformatics.tbr3.fieldmonitoring.client;

import java.util.Date;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.CustomMessage;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.ErrorType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.InfoType;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.FM;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Defaults;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Definition;
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author owais.hussain@irdresearch.org
 */
public class FieldMonitoring implements EntryPoint, ClickHandler
{
	private static ServerServiceAsync	service					= GWT.create (ServerService.class);
	private static LoadingWidget		loading					= new LoadingWidget ();
	private final MainMenuComposite		mainMenu				= new MainMenuComposite ();

	static RootPanel					rootPanel;
	private VerticalPanel				verticalPanel			= new VerticalPanel ();
	private FlexTable					loginFlexTable			= new FlexTable ();
	private FlexTable					recoveryFlexTable		= new FlexTable ();
	private FlexTable					newPasswordFlexTable	= new FlexTable ();

	private Label						label					= new Label ("Login Name");
	private Label						label_1					= new Label ("Password");
	private Label						lblLoginAs				= new Label ("Login As");
	private Label						label_2					= new Label ("Secret Question");
	private Label						label_4					= new Label ("Answer");
	private Label						label_5					= new Label ("New Password");
	private Label						label_6					= new Label ("Confirm Password");
	private Label						secretQuestionLabel		= new Label ("Secret Question");

	private TextBox						loginNameTextBox		= new TextBox ();
	private TextBox						answerTextBox			= new TextBox ();

	private ListBox						rolesComboBox			= new ListBox ();

	private PasswordTextBox				passwordTextBox			= new PasswordTextBox ();
	private PasswordTextBox				newPasswordTextBox		= new PasswordTextBox ();
	private PasswordTextBox				confirmPasswordTextBox	= new PasswordTextBox ();

	private Button						loginButton				= new Button ("Login");
	private Button						recoveryButton			= new Button ("Recover");
	private Button						setPasswordButton		= new Button ("Set Password");
	private Button						forgotPasswordButton	= new Button ("Forgot Password");

	// private Label textToServerLabel = new Label();

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad ()
	{
		Window.setTitle (FM.getProjectTitle ());
		rootPanel = RootPanel.get ();
		rootPanel.setSize ("100%", "100%");
		createFlexTables ();
		createEventHandlers ();
		recoveryFlexTable.setVisible (false);
		newPasswordFlexTable.setVisible (false);
		loginNameTextBox.setFocus (false);
		loginFlexTable.getCellFormatter ().setHorizontalAlignment (2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		loginFlexTable.getCellFormatter ().setHorizontalAlignment (2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		load (true);
//		try
//		{
//			service.getSchema (new AsyncCallback<String[][]> ()
//			{
//				public void onSuccess (String[][] result)
//				{
//					FM.fillSchema (result);
					// Fill definition lists
					try
					{
						service.getDefaults (new AsyncCallback<Defaults[]> ()
						{
							public void onSuccess (Defaults[] result)
							{
								FM.fillDefaults (result);
								// Load default values
								try
								{
									service.getDefinitions (new AsyncCallback<Definition[]> ()
									{
										public void onSuccess (Definition[] result)
										{
											FM.fillDefinitions (result);
											rolesComboBox = (ListBox) FMClient.fillList (rolesComboBox);
											loginNameTextBox.setFocus (true);
											//login ();
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
//				}
//
//				@Override
//				public void onFailure (Throwable caught)
//				{
//					caught.printStackTrace ();
//					load (false);
//				}
//			});
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace ();
//		}
	}

	/**
	 * Create event handlers
	 */
	private void createEventHandlers ()
	{
		Window.addWindowClosingHandler (new ClosingHandler ()
		{
			public void onWindowClosing (ClosingEvent event)
			{
				event.setMessage (CustomMessage.getInfoMessage (InfoType.CONFIRM_CLOSE));
			}
		});

		loginButton.addClickHandler (this);
		forgotPasswordButton.addClickHandler (this);
		recoveryButton.addClickHandler (this);
		setPasswordButton.addClickHandler (this);
	}

	/**
	 * Create tables and add controls
	 */
	private void createFlexTables ()
	{
		loginNameTextBox.setMaxLength (50);
		passwordTextBox.setMaxLength (50);
		forgotPasswordButton.setStyleName ("a:visited");
		rootPanel.add (verticalPanel);
		verticalPanel.setHorizontalAlignment (HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setSize ("100%", "100%");
		verticalPanel.add (loginFlexTable);
		verticalPanel.setCellHorizontalAlignment (loginFlexTable, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add (recoveryFlexTable);
		verticalPanel.setCellHorizontalAlignment (recoveryFlexTable, HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add (newPasswordFlexTable);
		verticalPanel.setCellHorizontalAlignment (newPasswordFlexTable, HasHorizontalAlignment.ALIGN_CENTER);
		loginFlexTable.setSize ("50%", "100%");
		loginFlexTable.setWidget (0, 0, label);
		loginFlexTable.setWidget (0, 1, loginNameTextBox);
		loginFlexTable.setWidget (1, 0, label_1);
		loginFlexTable.setWidget (1, 1, passwordTextBox);
		loginFlexTable.setWidget (2, 0, lblLoginAs);
		rolesComboBox.setName ("USER_ROLE");
		loginFlexTable.setWidget (2, 1, rolesComboBox);
		loginFlexTable.setWidget (3, 0, loginButton);
		loginFlexTable.setWidget (3, 1, forgotPasswordButton);
		loginFlexTable.getCellFormatter ().setHorizontalAlignment (3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		loginFlexTable.getCellFormatter ().setHorizontalAlignment (1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		loginFlexTable.getCellFormatter ().setHorizontalAlignment (0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		loginFlexTable.getCellFormatter ().setHorizontalAlignment (3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		loginFlexTable.getCellFormatter ().setHorizontalAlignment (1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		loginFlexTable.getCellFormatter ().setHorizontalAlignment (0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		recoveryFlexTable.setSize ("75%", "100%");
		recoveryFlexTable.setWidget (0, 0, label_2);
		recoveryFlexTable.setWidget (0, 1, secretQuestionLabel);
		recoveryFlexTable.setWidget (1, 0, label_4);
		recoveryFlexTable.setWidget (1, 1, answerTextBox);
		recoveryFlexTable.setWidget (2, 1, recoveryButton);
		recoveryFlexTable.getCellFormatter ().setHorizontalAlignment (2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		recoveryFlexTable.getCellFormatter ().setHorizontalAlignment (1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		recoveryFlexTable.getCellFormatter ().setHorizontalAlignment (0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		recoveryFlexTable.getCellFormatter ().setHorizontalAlignment (2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		recoveryFlexTable.getCellFormatter ().setHorizontalAlignment (1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		recoveryFlexTable.getCellFormatter ().setHorizontalAlignment (0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		newPasswordFlexTable.setSize ("75%", "100%");
		newPasswordFlexTable.setWidget (0, 0, label_5);
		newPasswordFlexTable.setWidget (0, 1, newPasswordTextBox);
		newPasswordFlexTable.setWidget (1, 0, label_6);
		newPasswordFlexTable.setWidget (1, 1, confirmPasswordTextBox);
		newPasswordFlexTable.setWidget (2, 1, setPasswordButton);
		newPasswordFlexTable.getCellFormatter ().setHorizontalAlignment (2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		newPasswordFlexTable.getCellFormatter ().setHorizontalAlignment (1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		newPasswordFlexTable.getCellFormatter ().setHorizontalAlignment (0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		newPasswordFlexTable.getCellFormatter ().setHorizontalAlignment (2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		newPasswordFlexTable.getCellFormatter ().setHorizontalAlignment (1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		newPasswordFlexTable.getCellFormatter ().setHorizontalAlignment (0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	/**
	 * Set visibility of Login, Password Recovery and New Password panels
	 */
	private void setVisibilty ()
	{
		if (loginFlexTable.isVisible ())
		{
			loginFlexTable.setVisible (false);
			recoveryFlexTable.setVisible (true);
			newPasswordFlexTable.setVisible (false);
		}
		else if (recoveryFlexTable.isVisible ())
		{
			loginFlexTable.setVisible (false);
			recoveryFlexTable.setVisible (false);
			newPasswordFlexTable.setVisible (true);
		}
		else if (newPasswordFlexTable.isVisible ())
		{
			loginFlexTable.setVisible (true);
			recoveryFlexTable.setVisible (false);
			newPasswordFlexTable.setVisible (false);
		}
	}

	/**
	 * Handle User Login. If user is already logged in, main menu will display
	 * otherwise session renewal window will appear
	 */
	private void login ()
	{
//		String userName;
//		String userId;
//		String role;
//		String passCode;
//		String sessionLimit;
//		try
//		{
//			// Try to get Cookies
//			userName = Cookies.getCookie ("UserName");
//			userId = Cookies.getCookie ("UserID");
//			passCode = Cookies.getCookie ("Pass");
//			role = Cookies.getCookie ("Role");
//			sessionLimit = Cookies.getCookie ("SessionLimit");
//			if (userName == null || role == null || passCode == null || sessionLimit == null)
//				throw new Exception ();
//			loginNameTextBox.setText (userName);
//
//			// If session is expired then renew
//			if (new Date ().getTime () > Long.parseLong (sessionLimit))
//				if (!renewSession ())
//					throw new Exception ();
//			setCookies (userName, userId, role, passCode);
//			service.setCurrentUser (userName, role, new AsyncCallback<Void>()
//			{
//				public void onSuccess (Void result)
//				{
					verticalPanel.clear ();
					verticalPanel.add (mainMenu);
//				}
//				
//				public void onFailure (Throwable caught)
//				{
//					caught.printStackTrace ();
//				}
//			});
//		}
//		catch (Exception e)
//		{
//			loginFlexTable.setVisible (true);
//		}
	}

	/**
	 * Remove all widgets from application
	 */
	public static void flushAll ()
	{
		rootPanel.clear ();
		rootPanel.add (new HTML ("Application has been shut down. It is now safe to close the Browser window."));
	}

	/**
	 * Display/Hide main panel and loading widget
	 * 
	 * @param status
	 */
	public void load (boolean status)
	{
		verticalPanel.setVisible (!status);
		if (status)
			loading.show ();
		else
			loading.hide ();
	}

	/**
	 * Log out the application
	 */
	public static void logout ()
	{
		try
		{
			flushAll ();
			String userName = FM.getCurrentUserName ();
			setCookies ("", "", "", "");
			service.recordLogout (userName, new AsyncCallback<Void> ()
			{
				public void onSuccess (Void result)
				{
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
	 * Set browser cookies
	 */
	public static void setCookies (String userName, String userId, String role, String passCode)
	{
		Cookies.removeCookie ("UserName");
		Cookies.removeCookie ("UserID");
		Cookies.removeCookie ("Role");
		Cookies.removeCookie ("Pass");
		Cookies.removeCookie ("LoginTime");
		Cookies.removeCookie ("SessionLimit");

		FM.setCurrentUserName (userName);
		FM.setCurrentRole (role);
		FM.setPassCode (passCode);
		if(!userName.equals (""))
			Cookies.setCookie ("UserName", FM.getCurrentUserName ());
		if(!role.equals (""))
			Cookies.setCookie ("Role", FM.getCurrentRole ());
		if(!passCode.equals (""))
		{
			Cookies.setCookie ("Pass", FM.getPassCode ());
			Cookies.setCookie ("LoginTime", String.valueOf (new Date ().getTime ()));
			Cookies.setCookie ("SessionLimit", String.valueOf (new Date ().getTime () + FM.getSessionLimit ()));
		}
	}

	/**
	 * To renew browsing session
	 * 
	 * @return true if renew was successful
	 */
	public static boolean renewSession ()
	{
		String passcode = Window.prompt (CustomMessage.getErrorMessage (ErrorType.SESSION_EXPIRED) + "\n" + "Please enter first 4 characters of your password to renew session.", "");
		if (FMClient.verifyClientPasscode (passcode))
		{
			Window.alert (CustomMessage.getInfoMessage (InfoType.SESSION_RENEWED));
			return true;
		}
		Window.alert (CustomMessage.getErrorMessage (ErrorType.AUTHENTICATION_ERROR));
		return false;
	}

	public void onClick (ClickEvent event)
	{
		Widget sender = (Widget) event.getSource ();
		final String userName = FMClient.get (loginNameTextBox).toUpperCase ();
		final String role = FMClient.get (rolesComboBox);
		load (true);
		if (sender == loginButton)
		{
//			if (userName.equals ("") || FMClient.get (passwordTextBox).equals ("") || role.equals (""))
//			{
//				Window.alert (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR));
//				return;
//			}
//			try
//			{
//				service.authenticate (userName, FMClient.get (passwordTextBox), role, new AsyncCallback<User> ()
//				{
//					public void onSuccess (User result)
//					{
//						if (result == null)
//						{
//							Window.alert (CustomMessage.getErrorMessage (ErrorType.AUTHENTICATION_ERROR));
//						}
//						else
//						{
//							if (result.getCurrentStatus () == 'D')
//								Window.alert (CustomMessage.getErrorMessage (ErrorType.USER_ROLE_UNDEFINED));
//							else
//							{
//								Window.alert (CustomMessage.getInfoMessage (InfoType.ACCESS_GRANTED));
//								setCookies (result.getUserName (), result.getPid (), role, String.valueOf (FMClient.getSimpleCode (FMClient.get (passwordTextBox).substring (0, 3))));
								login ();
//							}
//						}
						load (false);
//					}
//
//					public void onFailure (Throwable caught)
//					{
//						Window.alert (CustomMessage.getErrorMessage (ErrorType.UNKNOWN_ERROR));
//						load (false);
//					}
//				});
//			}
//			catch (Exception e)
//			{
//				e.printStackTrace ();
//			}
		}
		else if (sender == forgotPasswordButton)
		{
//			if (userName.equals (""))
//				Window.alert (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR));
//			else
//			{
//				try
//				{
//					service.authenticateUser (userName, new AsyncCallback<Boolean> ()
//					{
//						public void onSuccess (Boolean result)
//						{
//							if (result)
//							{
//								FM.setCurrentUserName (userName);
//								// Set Secret question
//								try
//								{
//									service.getSecretQuestion (userName, new AsyncCallback<String> ()
//									{
//										public void onSuccess (String result)
//										{
//											secretQuestionLabel.setText (result);
//										}
//
//										public void onFailure (Throwable caught)
//										{
//											// Not implemented
//										}
//									});
//								}
//								catch (Exception e)
//								{
//									e.printStackTrace ();
//								}
//								setVisibilty ();
//							}
//							load (false);
//						}
//
//						public void onFailure (Throwable caught)
//						{
//							Window.alert (CustomMessage.getErrorMessage (ErrorType.USER_NOT_FOUND));
//							load (false);
//						}
//					});
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace ();
//				}
//			}
		}
		else if (sender == recoveryButton)
		{
//			recoveryButton.setEnabled (false);
//			if (answerTextBox.getText ().equalsIgnoreCase (""))
//				Window.alert (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR));
//			else
//			{
//				try
//				{
//					service.verifySecretAnswer (userName, answerTextBox.getText (), new AsyncCallback<Boolean> ()
//					{
//						public void onSuccess (Boolean result)
//						{
//							if (result)
//								setVisibilty ();
//							load (false);
//						}
//
//						public void onFailure (Throwable caught)
//						{
//							Window.alert (CustomMessage.getErrorMessage (ErrorType.DATA_MISMATCH_ERROR) + "\n" + "Please enter valid Secret answer.");
//							answerTextBox.setText ("");
//							load (false);
//						}
//					});
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace ();
//				}
//			}
		}
		else if (sender == setPasswordButton)
		{
//			if (newPasswordTextBox.getText ().equalsIgnoreCase ("") || confirmPasswordTextBox.getText ().equalsIgnoreCase (""))
//				Window.alert (CustomMessage.getErrorMessage (ErrorType.EMPTY_DATA_ERROR));
//			else if (!newPasswordTextBox.getText ().equals (confirmPasswordTextBox.getText ()))
//				Window.alert (CustomMessage.getErrorMessage (ErrorType.DATA_MISMATCH_ERROR));
//			else
//			{
//				try
//				{
//					service.updatePassword (userName, newPasswordTextBox.getText (), new AsyncCallback<Boolean> ()
//					{
//						public void onSuccess (Boolean result)
//						{
//							if (result)
//							{
//								Window.alert (CustomMessage.getInfoMessage (InfoType.UPDATED));
//								setVisibilty ();
//								load (false);
//							}
//						}
//
//						public void onFailure (Throwable caught)
//						{
//							Window.alert (CustomMessage.getErrorMessage (ErrorType.UPDATE_ERROR));
//							load (false);
//						}
//					});
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace ();
//				}
//			}
		}
	}
}