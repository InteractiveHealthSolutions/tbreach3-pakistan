package org.irdresearch.tbreach.xpertsms.ui;
import javax.swing.*;

import org.irdresearch.tbreach.xpertsms.net.ResultServer;

import java.awt.*;
import java.awt.event.ActionEvent;

public class XpertSMSMain
{
 
  private static ControlPanel settingsPanel;
  //private static JTextPane monitorPanel;
  
  
  
  public XpertSMSMain()
  {
    
  }
  
  private static void createAndShowGUI() {
	  JFrame mainFrame = new JFrame("XpertSMS");
	  
	  //Show splash screen
	  final SplashScreen splash = SplashScreen.getSplashScreen();
      if (splash == null) {
          System.out.println("SplashScreen.getSplashScreen() returned null");
          return;
      }
      Graphics2D g = splash.createGraphics();
      if (g == null) {
          System.out.println("g is null");
          return;
      }
      for(int i=0; i<100; i++) {
          renderSplashFrame(g, i);
          splash.update();
          try {
              Thread.sleep(90);
          }
          catch(InterruptedException e) {
          }
      }
      splash.close();
      
      /////////////////////////////
      
      //set Frame size and location
      Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
      mainFrame.setLocation( d.width / 2 - 400, d.height / 2 - 300 );
      mainFrame.setSize(800, 600);
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      //set frame's layout
      Container mainPane = mainFrame.getContentPane();
      GridBagLayout gridbag = new GridBagLayout();
      GridBagConstraints c = new GridBagConstraints();
     
      mainPane.setLayout(gridbag);
      
      
     //add button panel
/*      c.gridwidth = GridBagConstraints.REMAINDER; //last
      c.anchor = GridBagConstraints.PAGE_START;
      c.weightx = 0.5;
      c.weighty = 0.5;
      c.gridx = 0;
      c.gridy = 0;
     // c.fill = GridBagConstraints.BOTH;
      oldControlPanel = new OldControlPanel();
     /// mainPane.add(oldControlPanel, c);
*/      ////////////////////////////////////////////////////
      
      //add settings panel
      //monitorPanel = new JTextPane();
      c.fill = GridBagConstraints.BOTH;
      c.weightx = 1;
      c.weighty = 1;
      
      c.gridx = 1;
      c.gridy = 1;
      settingsPanel = new ControlPanel();
      mainPane.add(settingsPanel, c);
      
      
      //add text panel
      
      /*monitorPanel.setEditable(false);
      monitorPanel.setPreferredSize(new Dimension(300,300));
      c.gridx = 1;
      c.gridy = 0;
      //c.fill = GridBagConstraints.BOTH;
      mainFrame.add(new JScrollPane( monitorPanel ), c);*/
     
      //monitorFrame.setResizable(true);
      //monitorFrame.pack();
      //monitorFrame.setVisible(true);
      mainFrame.pack();
      mainFrame.setResizable(false);
      mainFrame.setVisible(true);
  }
  
  static void renderSplashFrame(Graphics2D g, int frame) {
      final String[] comps = {"1","2","3"};
      g.setComposite(AlphaComposite.Clear);
      g.fillRect(120,140,200,40);
      g.setPaintMode();
      g.setColor(Color.BLACK);
      g.drawString("Loading "+comps[(frame/5)%3]+"...", 120, 150);
  }


/**
 * @return the settingsPanel
 */
public ControlPanel getSettingsPanel() {
	return settingsPanel;
}





public static void main( String[] args )
  {
	  SwingUtilities.invokeLater(new Runnable() {
          public void run() {
               //Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
          }
      });
	  
  }
}