/**
 * Loading Widget for GWT 
 */

package com.ihsinformatics.tbr3.fieldmonitoring.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * @author branflake2267
 * 
 */
public class LoadingWidget extends Composite
{
	private HorizontalPanel	pWidget	= new HorizontalPanel ();
	private HTML			html	= new HTML ();

	/**
	 * Initialize loading widget
	 */
	public LoadingWidget ()
	{
		Image image = new Image ("images/loading.gif");
		pWidget.add (new HTML ("&nbsp;"));
		pWidget.add (image);
		pWidget.add (new HTML ("&nbsp;"));
		pWidget.add (html);
		initWidget (pWidget);
		pWidget.setStyleName ("loadingImage");
		hide ();
	}

	/**
	 * Hide loading widget
	 */
	public void hide ()
	{
		pWidget.setVisible (false);
		Window.setStatus ("Done");
	}

	/**
	 * Show loading widget
	 */
	public void show ()
	{
		pWidget.setVisible (true);
		Window.setStatus ("Loading...");
	}

	/**
	 * Show loading widget with text
	 * 
	 * @param s
	 */
	public void show (String s)
	{
		show ();
		setHTML (s);
	}

	/**
	 * Set text for loading
	 * 
	 * @param s
	 */
	public void setHTML (String s)
	{
		html.setVisible (true);
		html.setHTML (s);
	}

	/**
	 * Hide loading widget timed
	 */
	public void hideTimed ()
	{
		pWidget.setVisible (true);
		Timer t = new Timer ()
		{
			public void run ()
			{
				pWidget.setVisible (false);
			}
		};
		t.schedule (3000);
	}
}