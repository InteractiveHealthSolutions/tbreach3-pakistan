package org.irdresearch.tbreach.xpertsms.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.PlainDocument;

import org.irdresearch.tbreach.xpertsms.constants.astm.ASTMMessageConstants;
import org.irdresearch.tbreach.xpertsms.constants.global.FileConstants;
import org.irdresearch.tbreach.xpertsms.net.ResultServer;


public class ControlPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 7965523667046214649L;
	private static final Color bgColour = new Color(255,255,255);// lightBlue(187,193,253)
	private static final Color borderColour = new Color(128,128,255); //darkBlue(128,128,255)
	
    protected static final String mtbCodeString = "MTB code";
    protected static final String rifCodeString = "RIF code";
    protected static final String qcCodeString = "QC code";
    protected static final String exportProbeString = "Export Probe Data";
    protected static final String serverURLString = "Server URL";
    protected static final String serverPortString = "Server Port";
    protected static final String webappString = "Destination page";
    protected static final String localPortString = "Local port";
    protected static final String extUsernameString = "Server Username";
    protected static final String extPasswordString = "Server Password";
    protected static final String sendMethodString = "Send Method";
    protected static final String serverSMSNumberString = "Server SMS Number";
    protected static final String localDatabasePortString = "Database port";
    protected static final String localDatabaseNameString = "Database name";
    protected static final String localDatabaseUserString = "Database User";
    protected static final String localDatabasePasswordString = "Database Password";
    protected static final String startString = "Start";
	protected static final String stopString = "Stop";
    protected static final String settingsString = "ControlPanel";

    protected static final String saveString = "Save";
    protected JLabel actionLabel;
    protected JTextField mtbCodeField;
    protected JTextField rifCodeField;
    protected JTextField qcCodeField;
    protected JTextField serverURLField;
    protected JTextField serverPortField;
    protected JTextField webappStringField;
    protected JTextField localPortField;
    protected JTextField extUsernameField;
    protected JPasswordField extPasswordField;
    protected JTextField serverSMSNumberField;
    protected JCheckBox exportProbes;
    protected JComboBox sendMethodBox;
    protected JTextField localDatabasePortField;
    protected JTextField localDatabaseNameField;
    protected JTextField localDatabaseUserField;
    protected JPasswordField localDatabasePasswordField;
    
    protected JButton saveButton;
    protected JButton startButton;
    protected JButton stopButton;
    protected JButton settingsButton;
    
    public static Properties props;
    
    private ResultServer server;
    private JTextPane monitorPanel;
    
    
    public ControlPanel() {
    	
    	props = new Properties();
    	
    	if(new File(FileConstants.FILE_PATH).exists()) {
    		try {
    			props.load(new FileInputStream(FileConstants.FILE_PATH));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
        setLayout(new BorderLayout());
        
        //Create a regular text field.
        mtbCodeField = new JTextField(15);
        mtbCodeField.setActionCommand(mtbCodeString);
        mtbCodeField.setText(props.getProperty("mtbcode"));
        mtbCodeField.addActionListener(this);
      
        rifCodeField = new JTextField(16);
        rifCodeField.setActionCommand(rifCodeString);
        rifCodeField.setText(props.getProperty("rifcode"));
        rifCodeField.addActionListener(this);
        
        qcCodeField = new  JTextField(16);
        qcCodeField.setActionCommand(qcCodeString);
        qcCodeField.setText(props.getProperty("qccode"));
        qcCodeField.addActionListener(this);
        
        serverURLField = new JTextField(15);
        serverURLField.setActionCommand(serverURLString);
        serverURLField.setText(props.getProperty("serverurl"));
        serverURLField.addActionListener(this);
        
        serverPortField = new JTextField(15);
        serverPortField.setActionCommand(serverPortString);
        serverPortField.setText(props.getProperty("serverport"));
        serverPortField.addActionListener(this);
        
        webappStringField = new JTextField(15);
        webappStringField.setActionCommand(webappString);
        webappStringField.setText(props.getProperty("webappstring"));
        webappStringField.addActionListener(this);
        
        localPortField = new JTextField(15);
        localPortField.setActionCommand(localPortString);
        localPortField.setText(props.getProperty("localport"));
        localPortField.addActionListener(this);
        
        extUsernameField = new JTextField(15);
        extUsernameField.setActionCommand(extUsernameString);
        extUsernameField.setText(props.getProperty("serveruser"));
        extUsernameField.addActionListener(this);
        
        extPasswordField = new JPasswordField(15);
        extPasswordField.setActionCommand(extPasswordString);
        extPasswordField.setText(props.getProperty("serverpass"));
        extPasswordField.addActionListener(this);
        
        serverSMSNumberField = new JTextField(15);
        serverSMSNumberField.setActionCommand(serverSMSNumberString);
        serverSMSNumberField.setText(props.getProperty("serversmsnumber"));
        serverSMSNumberField.addActionListener(this);

        localDatabasePortField = new JTextField(15);
        localDatabasePortField.setActionCommand(localDatabasePortString);
        localDatabasePortField.setText(props.getProperty("localdbport"));
        localDatabasePortField.addActionListener(this);
      
        localDatabaseNameField = new JTextField(15);
        localDatabaseNameField.setActionCommand(localDatabaseNameString);
        localDatabaseNameField.setText(props.getProperty("localdbname"));
        localDatabaseNameField.addActionListener(this);
        
        localDatabaseUserField = new JTextField(15);
        localDatabaseUserField.setActionCommand(localDatabaseUserString);
        localDatabaseUserField.setText(props.getProperty("localdbuser"));
        localDatabaseUserField.addActionListener(this);
        
        localDatabasePasswordField = new JPasswordField(15);
        localDatabasePasswordField.setActionCommand(localDatabasePasswordString);
        localDatabasePasswordField.setText(props.getProperty("localdbpass"));
        localDatabasePasswordField.addActionListener(this);
        
        
        
        String[] methods = { "https", "http", "sms","csvdump" };
        sendMethodBox  = new JComboBox(methods);
        sendMethodBox.addActionListener(this);
        
        if(props.getProperty("sendmethod")!=null && props.getProperty("sendmethod").equalsIgnoreCase("csvdump"))
        	sendMethodBox.setSelectedIndex(3);
        
        else if(props.getProperty("sendmethod")!=null && props.getProperty("sendmethod").equalsIgnoreCase("sms"))
        	sendMethodBox.setSelectedIndex(2);
        
        else if(props.getProperty("sendmethod")!=null && props.getProperty("sendmethod").equalsIgnoreCase("http"))
        	sendMethodBox.setSelectedIndex(1);
        
        else
        	sendMethodBox.setSelectedIndex(0);
        
  
        exportProbes = new JCheckBox(exportProbeString);
        exportProbes.setMnemonic(KeyEvent.VK_C);
        if(props.getProperty("exportprobes")!=null && props.getProperty("exportprobes").equalsIgnoreCase(ASTMMessageConstants.TRUE))
        	exportProbes.setSelected(true);
        
        //Create some labels for the fields.
        JLabel mtbCodeFieldLabel = new JLabel(mtbCodeString + ": ");
        mtbCodeFieldLabel.setLabelFor(mtbCodeField);
        JLabel rifCodeFieldLabel = new JLabel(rifCodeString + ": ");
        rifCodeFieldLabel.setLabelFor(rifCodeField);
        JLabel qcCodeFieldLabel = new JLabel(qcCodeString + ": ");
        qcCodeFieldLabel.setLabelFor(qcCodeField);
        JLabel exportProbeDataFieldLabel = new JLabel(exportProbeString + ": ");
        exportProbeDataFieldLabel.setLabelFor(exportProbes);
        JLabel serverURLFieldLabel = new JLabel(serverURLString + ": ");
        serverURLFieldLabel.setLabelFor(serverURLField);
        JLabel serverPortFieldLabel = new JLabel(serverPortString + ": ");
        serverPortFieldLabel.setLabelFor(serverPortField);
        JLabel webappStringFieldLabel = new JLabel(webappString + ": ");
        serverPortFieldLabel.setLabelFor(webappStringField);
        
        JLabel localPortFieldLabel = new JLabel(localPortString + ": ");
        localPortFieldLabel.setLabelFor(localPortField);
        
        JLabel extUsernameFieldLabel = new JLabel(extUsernameString + ": ");
        extUsernameFieldLabel.setLabelFor(extUsernameField);
        JLabel extPasswordFieldLabel = new JLabel(extPasswordString + ": ");
        extPasswordFieldLabel.setLabelFor(extPasswordField);
        
        JLabel serverSMSNumberFieldLabel = new JLabel(serverSMSNumberString + ": ");
        serverSMSNumberFieldLabel.setLabelFor(serverSMSNumberField);
        
        JLabel localDatabasePortFieldLabel = new JLabel(localDatabasePortString + ": ");
        localDatabasePortFieldLabel.setLabelFor(localDatabasePortField);
        JLabel localDatabaseNameFieldLabel = new JLabel(localDatabaseNameString + ": ");
        localDatabaseNameFieldLabel.setLabelFor(localDatabaseNameField);
        JLabel localDatabaseUserFieldLabel = new JLabel(localDatabaseUserString + ": ");
        localDatabaseUserFieldLabel.setLabelFor(localDatabaseUserField);
        JLabel localDatabasePasswordFieldLabel = new JLabel(localDatabasePasswordString + ": ");
        localDatabasePasswordFieldLabel.setLabelFor(localDatabasePasswordField);
        
        //Create a label to put messages during an action event.
        actionLabel = new JLabel("");
        actionLabel.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

        //Lay out the text controls and the labels.
        JPanel textControlsPane = new JPanel();
        textControlsPane.setBackground(bgColour);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);

        JLabel[] labels = {mtbCodeFieldLabel, rifCodeFieldLabel, qcCodeFieldLabel,localPortFieldLabel};
        JTextField[] textFields = {mtbCodeField, rifCodeField,qcCodeField,localPortField};
        addLabelTextRows(labels, textFields, gridbag, textControlsPane);
        
        c.anchor = GridBagConstraints.EAST;
        
        JLabel sendMethodLabel = new JLabel(sendMethodString + ": ");
        c.gridwidth = GridBagConstraints.RELATIVE;     //end row
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0; 
        textControlsPane.add(sendMethodLabel,c);
        
        sendMethodBox.setCursor(Cursor.getDefaultCursor());
        sendMethodBox.setBackground(bgColour);
        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        textControlsPane.add(sendMethodBox,c);
        
        //serverURLField,serverPortField,
       
        c.anchor = GridBagConstraints.EAST;
       
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(serverURLFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(serverURLField, c);
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(serverPortFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(serverPortField, c);
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(webappStringFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(webappStringField, c);
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(extUsernameFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(extUsernameField, c);
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(extPasswordFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(extPasswordField, c);
        
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(serverSMSNumberFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(serverSMSNumberField, c);
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(localDatabasePortFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(localDatabasePortField, c);
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(localDatabaseNameFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(localDatabaseNameField, c);
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(localDatabaseUserFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(localDatabaseUserField, c);
        
        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
        c.fill = GridBagConstraints.NONE;      //reset to default
        c.weightx = 0.0;                       //reset to default
        textControlsPane.add(localDatabasePasswordFieldLabel, c);

        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        textControlsPane.add(localDatabasePasswordField, c);
        
        exportProbes.setCursor(Cursor.getDefaultCursor());
        exportProbes.setBackground(bgColour);
        //exportProbes.setMargin(new Insets(0,0,0,0));
        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
        c.fill = GridBagConstraints.HORIZONTAL;
        textControlsPane.add(exportProbes,c);
        
        saveButton = new JButton();
        
        saveButton.setText(saveString);
        saveButton.setCursor(Cursor.getDefaultCursor());
        saveButton.setMargin(new Insets(0,0,0,0));
        saveButton.setActionCommand(saveString);
        saveButton.addActionListener(this);
        saveButton.setEnabled(true);
        textControlsPane.add(saveButton, c);
        
        startButton = new JButton();
        
        startButton.setText(startString);
        startButton.setCursor(Cursor.getDefaultCursor());
        startButton.setMargin(new Insets(0,0,0,0));
        startButton.setActionCommand(startString);
        startButton.addActionListener(this);
        textControlsPane.add(startButton, c);
       
        stopButton = new JButton();
        
        stopButton.setText(stopString);
        stopButton.setCursor(Cursor.getDefaultCursor());
        stopButton.setMargin(new Insets(0,0,0,0));
        stopButton.setActionCommand(stopString);
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);
        textControlsPane.add(stopButton, c);
        
        c.gridwidth = GridBagConstraints.REMAINDER; //last
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        textControlsPane.add(actionLabel, c);
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColour),"TB REACH XpertSMS"),
                                BorderFactory.createEmptyBorder(5,5,5,5)));


       
        JPanel leftPane = new JPanel(new BorderLayout());
        leftPane.add(textControlsPane, 
                     BorderLayout.PAGE_START);
     
        add(leftPane, BorderLayout.LINE_START);
        //add(rightPane, BorderLayout.LINE_END);
        leftPane.setBackground(bgColour);
        monitorPanel = new JTextPane();
        monitorPanel.setEditable(false);
        monitorPanel.setPreferredSize(new Dimension(300,300));
        c.gridx = 1;
        c.gridy = 0;
        //c.fill = GridBagConstraints.BOTH;
        add(new JScrollPane( monitorPanel ), BorderLayout.LINE_END);
    }

    private void addLabelTextRows(JLabel[] labels,
                                  JTextField[] textFields,
                                  GridBagLayout gridbag,
                                  Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            c.weighty = 1.0;
            container.add(textFields[i], c);
        }
    }

    ////////////////////////ACTION PERFORMED/////////////////////////////////
    
    public void actionPerformed(ActionEvent e) {
        //String prefix = "You typed \"";
        if (saveString.equals(e.getActionCommand())) {
            String mtbCode = mtbCodeField.getText();
            String rifCode = rifCodeField.getText();
            String qcCode = qcCodeField.getText();
            String serverURL = serverURLField.getText();
            String serverPort = serverPortField.getText();
            String webappString = webappStringField.getText();
            String localPort = localPortField.getText();
            String serverUser = extUsernameField.getText();
            String serverPassword = extPasswordField.getText();
            boolean probeExport = exportProbes.isSelected();
            String sendMethod = (String)sendMethodBox.getSelectedItem();
            String serverSMSNumber = serverSMSNumberField.getText();
            String localDBPort = localDatabasePortField.getText();
            String localDBName = localDatabaseNameField.getText();
            String localDBUser = localDatabaseUserField.getText();
            String localDBPassword = localDatabasePasswordField.getText();
            
            if(validateData(mtbCode,rifCode,qcCode,serverURL,serverPort,webappString,localPort,serverSMSNumber,sendMethod, localDBPort, localDBName, localDBUser, localDBPassword))
            {
            
            	props.setProperty("mtbcode", mtbCode);
            	props.setProperty("rifcode", rifCode);
            	props.setProperty("qccode", qcCode);
            	props.setProperty("serverurl", serverURL);
            	props.setProperty("serverport", serverPort);
            	props.setProperty("webappstring", webappString);
            	props.setProperty("localport", localPort);
            	props.setProperty("serveruser", serverUser);
            	props.setProperty("serverpass", serverPassword);
            	props.setProperty("sendmethod", sendMethod);
            	props.setProperty("localdbport", localDBPort );
            	props.setProperty("localdbname", localDBName );
            	props.setProperty("localdbuser", localDBUser );
            	props.setProperty("localdbpass", localDBPassword );
            	if(probeExport)
            		props.setProperty("exportprobes", "true");
            	else
            		props.setProperty("exportprobes", "false");
            	props.setProperty("serversmsnumber", serverSMSNumber);
            	try {
            		
            		if(!(new File(FileConstants.XPERT_SMS_DIR).exists())) {
            			boolean checkDir =  new File(FileConstants.XPERT_SMS_DIR).mkdir();
            			
            			if(!checkDir) {
            				JOptionPane.showMessageDialog(null,"Could not create properties file. Please check the permissions of your home holder!","Error occurred!",JOptionPane.ERROR_MESSAGE);
            				startButton.setEnabled(false);
            				return;
            			}
            		}
            		
            		props.store(new FileOutputStream(FileConstants.FILE_PATH),null);
            		props.load(new FileInputStream(FileConstants.FILE_PATH));
            		JOptionPane.showMessageDialog(null,"Settings updated!","Success!",JOptionPane.INFORMATION_MESSAGE);
            		startButton.setEnabled(true);
    			} catch (FileNotFoundException fnfe) {
    				// TODO Auto-generated catch block
    				fnfe.printStackTrace();
    				JOptionPane.showMessageDialog(null,"Could not create properties file. Please check the permissions of your home holder!","Error occurred!",JOptionPane.ERROR_MESSAGE);
    				startButton.setEnabled(false);
    			} catch (IOException ioe) {
    				// TODO Auto-generated catch block
    				ioe.printStackTrace();
    				JOptionPane.showMessageDialog(null,"Could not create properties file!","Error occurred!",JOptionPane.ERROR_MESSAGE);
    				startButton.setEnabled(false);
    			}
            }  
        }
        
       else if(startString.equals(e.getActionCommand())) {
    	if(!(new File(FileConstants.FILE_PATH).exists())) {
       		JOptionPane.showMessageDialog(null,"Please configure and save settings before starting!","Error occurred",JOptionPane.ERROR_MESSAGE);
       	}
    	
    	else {
    		stopButton.setEnabled(true);
    		startButton.setEnabled(false);
    		saveButton.setEnabled(false);
    		
    		//disable settings fields
    		mtbCodeField.setEnabled(false);
            rifCodeField.setEnabled(false);
            qcCodeField.setEnabled(false);
            serverURLField.setEnabled(false);
            serverPortField.setEnabled(false);
            localPortField.setEnabled(false);
            extUsernameField.setEnabled(false);
            extPasswordField.setEnabled(false);
            webappStringField.setEnabled(false);
            exportProbes.setEnabled(false);
            sendMethodBox.setEnabled(false);
            localDatabasePortField.setEnabled(false);
            localDatabaseNameField.setEnabled(false);
            localDatabaseUserField.setEnabled(false);
            localDatabasePasswordField.setEnabled(false);
    		startServer();
    	}
       }
       
       else if(stopString.equals(e.getActionCommand())) {
       	stopServer();
       	stopButton.setEnabled(false);
       	startButton.setEnabled(true);
       	saveButton.setEnabled(true);
       	
       	//enable settings fields
		mtbCodeField.setEnabled(true);
        rifCodeField.setEnabled(true);
        qcCodeField.setEnabled(true);
        serverURLField.setEnabled(true);
        serverPortField.setEnabled(true);
        localPortField.setEnabled(true);
        extUsernameField.setEnabled(true);
        extPasswordField.setEnabled(true);
        webappStringField.setEnabled(true);
        exportProbes.setEnabled(true);
        sendMethodBox.setEnabled(true);
        localDatabasePortField.setEnabled(true);
        localDatabaseNameField.setEnabled(true);
        localDatabaseUserField.setEnabled(true);
        localDatabasePasswordField.setEnabled(true);
       }
    }
    
    private boolean validateData(String mtbCode, String rifCode, String qcCode, String serverURL, String serverPort,String webappString,String localPort,String smsNumber,String sendMethod, String localDBPort,String localDBName, String localDBUser,String localDBPassword) {
    	String errorText = "";
    	
    	if(mtbCode==null || mtbCode.length()==0) {
    		errorText += "MTB Code is a mandatory field\n";
    	}
    	if(rifCode==null || rifCode.length()==0) {
    		errorText += "RIF Code is a mandatory field\n";
    	}
    	if(qcCode==null || qcCode.length()==0) {
    		errorText += "QC Code is a mandatory field\n";
    	}
    	
    	int locPort = 0;
    	try {
			 locPort = Integer.parseInt(localPort);
		} catch (NumberFormatException e) {
			errorText += "Local Port must be a number\n";
		}
    	
    	if(locPort <=0 || locPort > 65535) {
    		errorText += "Local Port must be a number between 1 and 65535\n";
    	}
    
    	if(sendMethod.equals("http")) {
 
    		if(serverURL==null || serverURL.length()==0) {
    			errorText += "Server URL is a mandatory field for sending over http\n";
    		}	
    		if(serverPort==null || serverPort.length()==0) {
    			errorText += "Server Port is a mandatory field for sending over http\n";
    		}
    		
    		int port = 0;
        	
        	try {
    			port = Integer.parseInt(serverPort);
    		} catch (NumberFormatException e) {
    			errorText += "Server Port must be a number\n";
    		}
    		
    		if(port <=0 || port > 65535) {
        		errorText += "Server Port must be a number between 1 and 65535\n";
        	}
    		
    		if(webappString==null || webappString.length()==0) {
    			errorText += "Destination page is a mandatory field for sending over http\n";
    		}
    		
    	}
    	
    	if(sendMethod.equals("sms")) {
    		 
    		if(smsNumber==null || smsNumber.length()==0) {
    			errorText += "Server SMS Number is a mandatory field for sending over sms\n";
    			
    		}	
    		
    		if(localDBPort==null || localDBPort.length()==0) {
    			errorText += "Local DB Port is a mandatory field for sending over sms\n";
    			
    		}	
    		
    		if(localDBName==null || localDBName.length()==0) {
    			errorText += "Local DB Name is a mandatory field for sending over sms\n";
    			
    		}	
    		
    		if(localDBUser==null || localDBUser.length()==0) {
    			errorText += "Local DB User is a mandatory field for sending over sms\n";
    			
    		}	
    		
    		if(localDBPassword==null || localDBPassword.length()==0) {
    			errorText += "Local DB Password is a mandatory field for sending over sms\n";
    			
    		}	
    		
    	}
    	
    	if(errorText.length()>0) {
    		JOptionPane.showMessageDialog(null,errorText,"Error occurred",JOptionPane.ERROR_MESSAGE);
    		return false;
    	}
    	
    	return true;
    }
    
    
    ////////////////////SERVER CONTROL/////////////////////
    
    public void startServer() {

    	server = new ResultServer(monitorPanel);
    
    	
    	server.start();
    	
    	
    }

    private void stopServer() {
    	server.setStopped(true);
    	try {
			server.getSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		server.getMessages().clear();
		server = null;
    }

    
   
}
