/**
 * Barcode Scanning Activity. This will require Barcode scanner to be installed in the device
 */

package org.irdresearch.tbr3mobile;

/**
 * @author owais.hussain@irdresearch.org
 * 
 */
public class Barcode
{
	static final int	BARCODE_RESULT		= 0;
	static final String	BARCODE_INTENT		= "com.google.zxing.client.android.SCAN";
	static final String	SCAN_MODE			= "SCAN_MODE";
	static final String	QR_MODE				= "QR_MODE";
	static final String	SCAN_RESULT			= "SCAN_RESULT";
	static final String	SCAN_RESULT_FORMAT	= "SCAN_RESULT_FORMAT";
}
