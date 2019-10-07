/**
 * This form is used to maintain terms and definitions used within the project
 */

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
import com.ihsinformatics.tbr3.fieldmonitoring.shared.model.Dictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class DictionaryComposite extends Composite implements IForm, ClickHandler, ChangeHandler, KeyPressHandler
{
	private static ServerServiceAsync	service						= GWT.create (ServerService.class);
	private static LoadingWidget		loading						= new LoadingWidget ();
	private static final String			menuName					= "HELP";
	private static final String			tableName					= "dictionary";

	private UserRightsUtil				rights						= new UserRightsUtil ();
	private boolean						valid;
	private Dictionary					current;
	private ArrayList<String>			terms						= new ArrayList<String> ();

	private HorizontalPanel				editDeleteHorizontalPanel	= new HorizontalPanel ();
	private FlexTable					flexTable					= new FlexTable ();
	private FlexTable					topFlexTable				= new FlexTable ();
	private FlexTable					editFlexTable				= new FlexTable ();
	private FlexTable					searchFlexTable				= new FlexTable ();
	private FlexTable					detailsflexTable			= new FlexTable ();

	private ToggleButton				addButton					= new ToggleButton ("AddTerm");
	private Button						deleteButton				= new Button ("Delete");
	private Button						editButton					= new Button ("Add/Edit");
	private Button						backToSearchButton			= new Button ("Back to Search");
	private Button						saveButton					= new Button ("Save");
	private Button						closeButton					= new Button ("Close");
	private Button						exportAllDefinitionsButton	= new Button ("Export All Definitions");

	private Label						lblTbReachProject			= new Label (FM.getProjectTitle () + " Project Dictionary");
	private Label						lblTypeSearch				= new Label ("Type to Search:");
	private Label						lblHistory					= new Label ("History:");
	private Label						lblTerm						= new Label ("Term:");
	private Label						lblDefinition				= new Label ("Definition:");

	private TextArea					definitionTextArea			= new TextArea ();
	private TextArea					historyTextArea				= new TextArea ();
	private TextArea					newDefinitionTextArea		= new TextArea ();

	private TextBox						newTermTextBox				= new TextBox ();
	private TextBox						searchTermTextBox			= new TextBox ();

	private ListBox						termsListBox				= new ListBox ();

	public DictionaryComposite ()
	{
		termsListBox.setVisibleItemCount (20);
		initWidget (flexTable);
		flexTable.setSize ("80%", "100%");
		flexTable.setWidget (0, 0, topFlexTable);
		topFlexTable.setSize ("100%", "100%");
		lblTbReachProject.setWordWrap(false);
		lblTbReachProject.setStyleName ("title");
		topFlexTable.setWidget (0, 0, lblTbReachProject);
		flexTable.getCellFormatter ().setHeight (1, 0, "");
		flexTable.getCellFormatter ().setWidth (1, 0, "");
		flexTable.setWidget (1, 0, editFlexTable);
		editFlexTable.setSize ("100%", "100%");
		lblTerm.setWordWrap(false);
		editFlexTable.setWidget (0, 0, lblTerm);
		editFlexTable.setWidget (0, 1, newTermTextBox);
		newTermTextBox.setWidth ("300px");
		lblDefinition.setWordWrap(false);
		editFlexTable.setWidget (1, 0, lblDefinition);
		editFlexTable.setWidget (1, 1, newDefinitionTextArea);
		newDefinitionTextArea.setWidth ("400px");
		newDefinitionTextArea.setVisibleLines (5);
		editFlexTable.setWidget (2, 0, addButton);
		saveButton.setEnabled (false);
		editFlexTable.setWidget (2, 1, saveButton);
		editFlexTable.setWidget (3, 1, backToSearchButton);
		editFlexTable.getCellFormatter ().setHorizontalAlignment (2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		editFlexTable.getCellFormatter ().setHorizontalAlignment (3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.setWidget (2, 0, searchFlexTable);
		searchFlexTable.setSize ("100%", "100%");
		lblTypeSearch.setWordWrap(false);
		searchFlexTable.setWidget (0, 0, lblTypeSearch);
		searchFlexTable.setWidget (0, 1, searchTermTextBox);
		searchTermTextBox.setWidth ("200px");
		searchFlexTable.setWidget (1, 0, termsListBox);
		termsListBox.setWidth ("250px");
		searchFlexTable.setWidget (1, 1, detailsflexTable);
		detailsflexTable.setSize ("100%", "100%");
		definitionTextArea.setVisibleLines (8);
		definitionTextArea.setReadOnly (true);
		detailsflexTable.setWidget (0, 0, definitionTextArea);
		definitionTextArea.setSize ("400px", "");
		lblHistory.setWordWrap(false);
		detailsflexTable.setWidget (1, 0, lblHistory);
		historyTextArea.setVisibleLines (12);
		detailsflexTable.setWidget (2, 0, historyTextArea);
		historyTextArea.setSize ("400px", "");
		searchFlexTable.getCellFormatter ().setHorizontalAlignment (1, 0, HasHorizontalAlignment.ALIGN_LEFT);
		searchFlexTable.getCellFormatter ().setVerticalAlignment (1, 1, HasVerticalAlignment.ALIGN_TOP);
		searchFlexTable.getCellFormatter ().setVerticalAlignment (1, 0, HasVerticalAlignment.ALIGN_TOP);
		searchFlexTable.setWidget (2, 0, exportAllDefinitionsButton);
		exportAllDefinitionsButton.setWidth ("100%");
		searchFlexTable.setWidget (2, 1, editDeleteHorizontalPanel);
		editDeleteHorizontalPanel.setWidth ("100%");
		editDeleteHorizontalPanel.add (editButton);
		editButton.setText ("Add/Edit");
		editButton.setWidth ("100%");
		editDeleteHorizontalPanel.add (deleteButton);
		deleteButton.setWidth ("100%");
		editDeleteHorizontalPanel.add (closeButton);
		closeButton.setWidth ("100%");
		flexTable.getRowFormatter ().setVerticalAlign (2, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter ().setVerticalAlignment (1, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter ().setHorizontalAlignment (1, 0, HasHorizontalAlignment.ALIGN_LEFT);

		searchTermTextBox.addKeyPressHandler (this);
		termsListBox.addChangeHandler (this);
		addButton.addClickHandler (this);
		saveButton.addClickHandler (this);
		backToSearchButton.addClickHandler (this);
		closeButton.addClickHandler (this);
		deleteButton.addClickHandler (this);
		editButton.addClickHandler (this);
		exportAllDefinitionsButton.addClickHandler (this);

		editFlexTable.setVisible (false);
		refreshList ();
		setRights (menuName);
	}

	public void refreshList ()
	{
		try
		{
			load (true);
			service.getColumnData (tableName, "term", "", new AsyncCallback<String[]> ()
			{

				public void onSuccess (String[] result)
				{
					for (String s : result)
						terms.add (s);
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
		current.setPreviousDefinition (current.getDefinition ());
		current.setDefinition (FMClient.get (definitionTextArea));
		current.setLastRevised (new Date ());
		current.setRevisedBy (FM.getCurrentUserName ());
	}

	public void fillList ()
	{
		termsListBox.clear ();
		String prefix = FMClient.get (searchTermTextBox).toUpperCase ();
		for (String s : terms)
			if (s.toUpperCase ().contains (prefix))
				termsListBox.addItem (s);
	}

	public void exportList ()
	{
		try
		{
//			service.generateCSVfromQuery (FM.getDatabaseName (),
//					"select term_id, term, definition, (select ifnull(user_name, '') from user where pid = defined_by) as defined_by, entered_on from dictionary",
//					new AsyncCallback<String> ()
//					{
//						public void onSuccess (String result)
//						{
//							Window.open (result, "_blank", "");
//							load (false);
//						}
//
//						public void onFailure (Throwable caught)
//						{
//							caught.printStackTrace ();
//							load (false);
//						}
//					});
		}
		catch (Exception e)
		{
			e.printStackTrace ();
			load (false);
		}
	}

	public void fillData ()
	{
		try
		{
			service.findDictionary (FMClient.get (termsListBox), new AsyncCallback<Dictionary> ()
			{

				public void onSuccess (Dictionary result)
				{
					current = result;
					definitionTextArea.setText (current.getDefinition ());
					StringBuilder history = new StringBuilder ();
					history.append ("Original Definition: " + current.getOriginalDefinition ());
					history.append ("\r\n");
					history.append ("(By " + current.getDefinedBy () + ". " + current.getEnteredOn ().toString () + ")");
					history.append ("\r\n");
					if (current.getPreviousDefinition () != null)
					{
						history.append ("Previous Definition: " + current.getPreviousDefinition ());
						history.append ("\r\n");
						history.append ("(By " + current.getRevisedBy () + ". Revised On "
								+ current.getLastRevised ().toString () + ")");
					}
					historyTextArea.setText (history.toString ());
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
		/* Validate uniqueness */
		String term = FMClient.get (newTermTextBox);
		for (String s : terms)
			if (s.equals (term.toUpperCase ()))
			{
				errorMessage.append (CustomMessage.getErrorMessage (ErrorType.DUPLICATION_ERROR));
				valid = false;
			}
		/* Validate mandatory fields */
		if (newDefinitionTextArea.getText ().length () == 0 || FMClient.get (newTermTextBox).length () == 0)
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
				current = new Dictionary (FMClient.get (newTermTextBox).toUpperCase (), FM.getCurrentUserName (), new Date ());
				current.setDefinition (FMClient.get (newDefinitionTextArea));
				current.setOriginalDefinition (FMClient.get (newDefinitionTextArea));
				service.saveDictionary (current, new AsyncCallback<Boolean> ()
				{

					public void onSuccess (Boolean result)
					{
						if (result)
						{
							// Add term to existing terms
							terms.add (current.getTerm ());
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
		try
		{
			if (current.getTerm ().equalsIgnoreCase (FMClient.get (newTermTextBox)))
				setCurrent ();
			else
			{
				Window.alert ("Please select a definition from Search form first to edit.");
			}
			service.updateDictionary (current, new AsyncCallback<Boolean> ()
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

	public void deleteData ()
	{
		if (validate ())
		{
			try
			{
				service.deleteDictionary (current, new AsyncCallback<Boolean> ()
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
							termsListBox.setEnabled (rights.getAccess (AccessType.SELECT));
							addButton.setEnabled (rights.getAccess (AccessType.INSERT));
							editButton.setEnabled (rights.getAccess (AccessType.UPDATE));
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
		if (sender == addButton)
		{
			if (addButton.isDown ())
				clearUp ();
		}
		else if (sender == editButton)
		{
			searchFlexTable.setVisible (false);
			editFlexTable.setVisible (true);
			if (!addButton.isDown ())
			{
				newTermTextBox.setText (FMClient.get (termsListBox));
				newDefinitionTextArea.setText (definitionTextArea.getText ());
			}
			newTermTextBox.setReadOnly (!addButton.isDown ());
		}
		else if (sender == saveButton)
		{
			load (true);
			if (addButton.isDown ())
				saveData ();
			else
				updateData ();
		}
		else if (sender == backToSearchButton)
		{
			editFlexTable.setVisible (false);
			searchFlexTable.setVisible (true);
		}
		else if (sender == exportAllDefinitionsButton)
		{
			exportList ();
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
		if (sender == termsListBox)
		{
			fillData ();
		}
	}

	public void onKeyPress (KeyPressEvent event)
	{
		if (event.isMetaKeyDown ())
			return;
		Widget sender = (Widget) event.getSource ();
		load (true);
		if (sender == searchTermTextBox)
		{
			if (FMClient.get (searchTermTextBox).length () > 1)
			{
				fillList ();
			}
			load (false);
		}
	}
}
