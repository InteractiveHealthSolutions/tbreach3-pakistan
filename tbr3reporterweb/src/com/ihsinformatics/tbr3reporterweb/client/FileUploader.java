/* Copyright(C) 2016 Interactive Health Solutions, Pvt. Ltd.

This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html

Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 */

package com.ihsinformatics.tbr3reporterweb.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class FileUploader extends Composite {
	private static LoadingWidget loading = new LoadingWidget();

	private FormPanel form = new FormPanel();
	private FileUpload fileUpload = new FileUpload();
	private CheckBox updateCheckBox = new CheckBox("Update if report exists.");

	public FileUploader() {
	}

	/**
	 * Display/Hide main panel and loading widget
	 * 
	 * @param status
	 */
	public void load(boolean status) {
		form.setVisible(!status);
		if (status)
			loading.show();
		else
			loading.hide();
	}

	public Widget getFileUploaderWidget() {
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setAction(GWT.getModuleBaseURL() + "fileupload");
		VerticalPanel holder = new VerticalPanel();
		fileUpload.setName("upload");
		fileUpload.setTitle("Select iReports file...");
		holder.add(fileUpload);
		holder.add(new Button("Submit", new ClickHandler() {
			public void onClick(ClickEvent event) {
				GWT.log("You selected: " + fileUpload.getFilename(), null);
				load(true);
				form.submit();
			}
		}));
		holder.add(updateCheckBox);
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			public void onSubmit(SubmitEvent event) {
				if (!"".equalsIgnoreCase(fileUpload.getFilename())) {
					GWT.log("Upload file...", null);
				} else {
					event.cancel();
					load(false);
				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				form.reset();
				Window.alert("Report has been successfully uploaded.");
				load(false);
			}
		});
		form.add(holder);
		return form;
	}
}