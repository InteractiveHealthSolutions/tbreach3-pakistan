/**
 * Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 * Contributors: Tahira Niazi
 */
package com.ihsinformatics.tbr3fieldmonitoring.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Tahira
 * 
 */
public class MainMenuComposite extends Composite implements ClickHandler
{
	private static Logger logger = Logger.getLogger(MainMenuComposite.class.getName());

	private FlexTable mainFlexTable = new FlexTable();
	private FlexTable userProfileFlexTable = new FlexTable();
	private FlexTable headerFlexTable = new FlexTable();
	private FlexTable menuFlexTable = new FlexTable();

	private FlowPanel userNamePanel = new FlowPanel();

	Image bulletImage1 = new Image("arrowbullet.jpg");
	Image bulletImage2 = new Image("arrowbullet.jpg");
	Image bulletImage3 = new Image("arrowbullet.jpg");
	Image bulletImage4 = new Image("arrowbullet.jpg");
	Image bulletImage5 = new Image("arrowbullet.jpg");

	private Label loginAsLabel = new Label("Login As:");
	private Label usernameLabel = new Label("user");
	private Label leftBraceLabel = new Label("(");
	private Label rightBraceLabel = new Label(")");
	private Hyperlink logoutHyperlink = new Hyperlink("Logout,", "");
	private Hyperlink mainMenuHyperlink = new Hyperlink("Back to Menu", "");
	private Label formHeadingLabel = new Label("MAIN MENU");
	private Anchor firstFieldVisitAnchor = new Anchor("First Field Visit Form",
			false);
	private Anchor supervisorVisitAnchor = new Anchor("Supervisor Visit Form",
			false);
	private Anchor dailyVisitAnchor = new Anchor("Daily Visit Form", false);
	private Anchor campInformationAnchor = new Anchor("Camp Information Form",
			false);
	private Anchor testFormAnchor = new Anchor("Diabetes/Spirometry Form",
			false);

	@SuppressWarnings("deprecation")
	public MainMenuComposite()
	{
		super();
		initWidget(mainFlexTable);
		// mainFlexTable.setStyleName("verticalPanel");
		mainFlexTable.setSize("100%", "100%");

		userNamePanel.add(loginAsLabel);
		userNamePanel.add(usernameLabel);
		userNamePanel.add(leftBraceLabel);
		userNamePanel.add(logoutHyperlink);
		userNamePanel.add(mainMenuHyperlink);

		logoutHyperlink.addStyleName("hyperlink");
		mainMenuHyperlink.addStyleName("hyperlink");

		userNamePanel.add(rightBraceLabel);
		loginAsLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		usernameLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		logoutHyperlink.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		mainMenuHyperlink.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);
		leftBraceLabel.getElement().getStyle().setDisplay(Display.INLINE_BLOCK);
		rightBraceLabel.getElement().getStyle()
				.setDisplay(Display.INLINE_BLOCK);

		userNamePanel.addStyleName("flowPanel");

		userProfileFlexTable.setWidget(0, 0, userNamePanel);
		userProfileFlexTable.getCellFormatter().setHorizontalAlignment(0, 2,
				HasHorizontalAlignment.ALIGN_RIGHT);

		headerFlexTable.setWidget(0, 1, formHeadingLabel);
		headerFlexTable.getRowFormatter().addStyleName(0, "Tbr3Header");
		headerFlexTable.setSize("100%", "");

		// adding widgets to menuFlexTable
		menuFlexTable.setWidget(1, 0, bulletImage1);
		menuFlexTable.setWidget(1, 1, firstFieldVisitAnchor);
		firstFieldVisitAnchor.addStyleName("hyperlink");
		menuFlexTable.setWidget(2, 0, bulletImage2);
		menuFlexTable.setWidget(2, 1, supervisorVisitAnchor);
		supervisorVisitAnchor.addStyleName("hyperlink");
		menuFlexTable.setWidget(3, 0, bulletImage3);
		menuFlexTable.setWidget(3, 1, dailyVisitAnchor);
		dailyVisitAnchor.addStyleName("hyperlink");
		menuFlexTable.setWidget(4, 0, bulletImage4);
		menuFlexTable.setWidget(4, 1, campInformationAnchor);
		campInformationAnchor.addStyleName("hyperlink");
		menuFlexTable.setWidget(5, 0, bulletImage5);
		menuFlexTable.setWidget(5, 1, testFormAnchor);
		testFormAnchor.addStyleName("hyperlink");

		mainFlexTable.setWidget(0, 0, userProfileFlexTable);
		mainFlexTable.setWidget(1, 0, headerFlexTable);
		mainFlexTable.setWidget(2, 0, menuFlexTable);

		menuFlexTable.setSize("50%", "");

		// styling of all widgets in the menuFlexTable

		// mainFlexTable.getCellFormatter().setHorizontalAlignment(2, 0,
		// HasHorizontalAlignment.ALIGN_CENTER);
		mainFlexTable.setBorderWidth(1);
		mainFlexTable.getCellFormatter().setHorizontalAlignment(2, 0,
				HasHorizontalAlignment.ALIGN_CENTER);

		usernameLabel.setText(Cookies.getCookie("UserName"));

		firstFieldVisitAnchor.addClickHandler(this);
		supervisorVisitAnchor.addClickHandler(this);
		dailyVisitAnchor.addClickHandler(this);
		campInformationAnchor.addClickHandler(this);
		testFormAnchor.addClickHandler(this);

		logoutHyperlink.addClickHandler(this);
		mainMenuHyperlink.addClickHandler(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.dom.client.ClickHandler#onClick(com.google.gwt.event
	 * .dom.client.ClickEvent)
	 */
	@Override
	public void onClick(ClickEvent event)
	{
		logger.log(Level.INFO, "Click event fired by " + event.getSource());
		Widget sender = (Widget) event.getSource();
		if (sender == firstFieldVisitAnchor)
		{
			final FirstVisitComposite firstVisitComposite = new FirstVisitComposite();
			Cookies.setCookie("CurrentMenu", "First Visit");
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(firstVisitComposite);
		}
		else if (sender == supervisorVisitAnchor)
		{
			final SupervisorVisitComposite supervisorVisitComposite = new SupervisorVisitComposite();
			Cookies.setCookie("CurrentMenu", "Supervisor Visit");
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(supervisorVisitComposite);
		}
		else if (sender == dailyVisitAnchor)
		{
			final DailyVisitComposite dailyVisitComposite = new DailyVisitComposite();
			Cookies.setCookie("CurrentMenu", "Daily Visit");
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(dailyVisitComposite);
		}
		else if (sender == campInformationAnchor)
		{
			final CampInformationComposite campInformationComposite = new CampInformationComposite();
			Cookies.setCookie("CurrentMenu", "Camp Information");
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(campInformationComposite);
		}
		else if (sender == testFormAnchor)
		{
			final TestEntryComposite testEntryComposite = new TestEntryComposite();
			Cookies.setCookie("CurrentMenu", "Test Entry");
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(testEntryComposite);
		}
		else if (sender == mainMenuHyperlink)
		{
			final MainMenuComposite mainMenuComposite = new MainMenuComposite();
			Cookies.setCookie("CurrentMenu", "Main Menu");
			Tbr3fieldmonitoring.verticalPanel.clear();
			Tbr3fieldmonitoring.verticalPanel.add(mainMenuComposite);
		}
		else if (sender == logoutHyperlink)
		{
			Tbr3fieldmonitoring.logout();
		}
	}

	public static void clear()
	{
		Cookies.setCookie("CurrentMenu", "");
	}
}
