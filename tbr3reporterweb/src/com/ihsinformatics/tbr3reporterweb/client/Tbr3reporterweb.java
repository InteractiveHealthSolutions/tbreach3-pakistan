/**
 * Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 * Contributors: Owais Hussain
 */
package com.ihsinformatics.tbr3reporterweb.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ihsinformatics.tbr3reporterweb.shared.CustomMessage;
import com.ihsinformatics.tbr3reporterweb.shared.ErrorType;
import com.ihsinformatics.tbr3reporterweb.shared.InfoType;
import com.ihsinformatics.tbr3reporterweb.shared.Report;
import com.ihsinformatics.tbr3reporterweb.shared.TBR3;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tbr3reporterweb implements EntryPoint, ClickHandler,
		KeyDownHandler {
	private static ServerServiceAsync service = GWT.create(ServerService.class);

	private static LoadingWidget loading = new LoadingWidget();
	private static RootPanel rootPanel;

	static VerticalPanel verticalPanel = new VerticalPanel();
	private FlexTable headerFlexTable = new FlexTable();
	private FlexTable loginFlexTable = new FlexTable();

	private Label formHeadingLabel = new Label("USER AUTHENTICATION");
	private Label userNameLabel = new Label("User ID:");
	private Label passwordLabel = new Label("Password:");

	private TextBox userTextBox = new TextBox();
	private PasswordTextBox passwordTextBox = new PasswordTextBox();

	private Button loginButton = new Button("Login");
	private Button manageButton = new Button("Manage Reports");
	private Button deleteButton = new Button("Delete");

	private ListBox reportsList = new ListBox();

	private final ReportsComposite reportsComposite = new ReportsComposite();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		rootPanel = RootPanel.get();
		rootPanel.setStyleName("rootPanel");
		rootPanel.setSize("800px", "50%");
		// verticalPanel.addStyleName("verticalPanel");
		rootPanel.add(verticalPanel);
		headerFlexTable.setWidget(0, 1, formHeadingLabel);
		headerFlexTable.getRowFormatter().addStyleName(0, "Tbr3Header");
		headerFlexTable.setSize("100%", "");

		// loginFlexTable.setBorderWidth(2);
		loginFlexTable.setWidget(1, 0, userNameLabel);
		userNameLabel.addStyleName("text");

		loginFlexTable.setWidget(1, 1, userTextBox);
		userTextBox.setAlignment(TextAlignment.JUSTIFY);
		userTextBox.addStyleName("textbox");

		loginFlexTable.setWidget(2, 0, passwordLabel);
		passwordLabel.addStyleName("text");

		loginFlexTable.setWidget(2, 1, passwordTextBox);
		passwordTextBox.setWidth("200");
		passwordTextBox.addStyleName("textbox");
		// loginButton.setStyleName("button:active");
		loginButton.setStyleName("submitButton");

		loginFlexTable.setWidget(3, 1, loginButton);
		loginButton.setSize("169", "30");

		loginFlexTable.setStyleName("flexTableCell");

		verticalPanel.add(headerFlexTable);
		verticalPanel.setCellHorizontalAlignment(headerFlexTable,
				HasHorizontalAlignment.ALIGN_CENTER);

		verticalPanel.add(loginFlexTable);
		verticalPanel.setSize("800px", "");
		verticalPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setCellHorizontalAlignment(loginFlexTable,
				HasHorizontalAlignment.ALIGN_CENTER);
		loginFlexTable.setSize("100%", "");
		loginFlexTable.getCellFormatter().setHorizontalAlignment(1, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		loginFlexTable.getCellFormatter().setHorizontalAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		loginFlexTable.getCellFormatter().setHorizontalAlignment(0, 0,
				HasHorizontalAlignment.ALIGN_RIGHT);
		loginFlexTable.getCellFormatter().setVerticalAlignment(1, 1,
				HasVerticalAlignment.ALIGN_MIDDLE);
		loginFlexTable.getCellFormatter().setVerticalAlignment(0, 1,
				HasVerticalAlignment.ALIGN_MIDDLE);

		// verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setBorderWidth(1);
		loginButton.addClickHandler(this);
		manageButton.addClickHandler(this);
		passwordTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				boolean enterPressed = event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER;
				if (enterPressed) {
					loginButton.click();
				}
			}
		});
	}

	/**
	 * Display/Hide main panel and loading widget
	 * 
	 * @param status
	 */
	public void load(boolean status) {
		verticalPanel.setVisible(!status);
		if (status)
			loading.show();
		else
			loading.hide();
	}

	/**
	 * Set browser cookies
	 */
	public static void setCookies(String userName, String passCode,
			String password) {
		Cookies.removeCookie("UserName");
		Cookies.removeCookie("Pass");
		Cookies.removeCookie("LoginTime");
		Cookies.removeCookie("SessionLimit");

		TBR3.setCurrentUser(userName);
		TBR3.setPassCode(password);
		if (!userName.equals(""))
			Cookies.setCookie("UserName", TBR3.getCurrentUser());
		if (!password.equals(""))
			Cookies.setCookie("Password", TBR3.getPassCode());
		if (!passCode.equals("")) {
			Cookies.setCookie("Pass", TBR3.getPassCode());
			Cookies.setCookie("LoginTime", String.valueOf(new Date().getTime()));
			Cookies.setCookie("SessionLimit",
					String.valueOf(new Date().getTime() + TBR3.sessionLimit));
		}
	}

	private void doLogin() {
		// Check for empty fields
		if (TBR3ReporterClient.get(userTextBox).equals("")
				|| TBR3ReporterClient.get(passwordTextBox).equals("")) {
			Window.alert(CustomMessage
					.getErrorMessage(ErrorType.EMPTY_DATA_ERROR));
			return;
		}
		try {
			service.authenticate(TBR3ReporterClient.get(userTextBox),
					TBR3ReporterClient.get(passwordTextBox),
					new AsyncCallback<Boolean>() {
						@Override
						public void onSuccess(Boolean result) {
							if (result) {
								Window.alert(CustomMessage
										.getInfoMessage(InfoType.ACCESS_GRANTED));
								setCookies(
										TBR3ReporterClient.get(userTextBox),
										String.valueOf(TBR3ReporterClient
												.getSimpleCode(TBR3ReporterClient
														.get(passwordTextBox)
														.substring(0, 3))),
										TBR3ReporterClient.get(passwordTextBox));
								login();
							} else {
								Window.alert(CustomMessage
										.getErrorMessage(ErrorType.AUTHENTICATION_ERROR));
							}
						}

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handle User Login. If user is already logged in, main menu will display
	 * otherwise session renewal window will appear
	 */
	private void login() {
		String userName;
		String passCode;
		String password;
		String sessionLimit;
		try {
			// Try to get Cookies
			userName = Cookies.getCookie("UserName");
			passCode = Cookies.getCookie("Pass");
			password = Cookies.getCookie("Password");
			sessionLimit = Cookies.getCookie("SessionLimit");
			if (userName == null || passCode == null || sessionLimit == null)
				throw new Exception();
			userTextBox.setText(userName);

			// If session is expired then renew
			if (new Date().getTime() > Long.parseLong(sessionLimit))
				if (!renewSession())
					throw new Exception();
			setCookies(userName, passCode, password);
			service.setCurrentUser(userName, new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					verticalPanel.clear();
					verticalPanel.add(reportsComposite);
					verticalPanel.add(manageButton);
				}

				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}
			});
		} catch (Exception e) {
			loginFlexTable.setVisible(true);
		}
	}

	public void reportsView(boolean view) {
		manageButton.setVisible(!view);
		// rightFlexTable.setVisible(!reports);
		if (view) {
			verticalPanel.add(new FileUploader().getFileUploaderWidget());
			// Fetch list of reports from server
			service.getReportsList(new AsyncCallback<Report[]>() {
				@Override
				public void onSuccess(Report[] result) {
					for (Report report : result) {
						reportsList.addItem(report.getName());
					}
					verticalPanel.add(reportsList);
					verticalPanel.add(deleteButton);
					// TODO: Remove when delete function is complete
					reportsList.setVisible(false);
					deleteButton.setVisible(false);
					load(false);
				}

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("List of reports cannot be populated. Have you copied the reports to rpt directory?");
					load(false);
				}
			});
		}
	}

	public void deleteReport() {
		// TODO: To be completed
		load(false);
	}

	/**
	 * To renew browsing session
	 * 
	 * @return true if renew was successful
	 */
	public static boolean renewSession() {
		String passcode = Window
				.prompt(CustomMessage
						.getErrorMessage(ErrorType.SESSION_EXPIRED)
						+ "\n"
						+ "Please enter first 4 characters of your password to renew session.",
						"");
		if (TBR3ReporterClient.verifyClientPasscode(passcode)) {
			Window.alert(CustomMessage.getInfoMessage(InfoType.SESSION_RENEWED));
			return true;
		}
		Window.alert(CustomMessage
				.getErrorMessage(ErrorType.AUTHENTICATION_ERROR));
		return false;
	}

	/**
	 * Log out the application
	 */
	public static void logout() {
		try {
			flushAll();
			setCookies("", "", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Remove all widgets from application
	 */
	public static void flushAll() {
		rootPanel.clear();
		rootPanel
				.add(new HTML(
						"Application has been shut down. It is now safe to close the Browser window."));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();
		if (sender == loginButton) {
			doLogin();
		} else if (sender == manageButton) {
			reportsView(true);
		} else if (sender == deleteButton) {
			load(true);
			deleteReport();
		}
	}

	@Override
	public void onKeyDown(KeyDownEvent event) {
		Object source = event.getSource();
		if (source == passwordTextBox || source == userTextBox) {
			doLogin();
		}
	}
}
