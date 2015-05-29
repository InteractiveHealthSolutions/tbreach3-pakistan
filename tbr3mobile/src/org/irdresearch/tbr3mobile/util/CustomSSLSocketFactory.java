/**
 * Copyright(C) 2015 Interactive Health Solutions, Pvt. Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License (GPLv3), or any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Interactive Health Solutions, info@ihsinformatics.com
 * You can also access the license on the internet at the address: http://www.gnu.org/licenses/gpl-3.0.html
 * Interactive Health Solutions, hereby disclaims all copyright interest in this program written by the contributors.
 * Contributors: Owais
 */

package org.irdresearch.tbr3mobile.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author owais.hussain@irdresearch.org
 *
 */
public class CustomSSLSocketFactory extends SSLSocketFactory
{

	private final SSLSocketFactory	mDelegate;

	/**
	 * Creates an instance of the {@code CompatSSLSocketFactory} which uses the
	 * provided factory to obtain {@code SSLSocket} instances and then
	 * reconfigures them for compatibility.
	 */
	public CustomSSLSocketFactory (SSLSocketFactory delegate)
	{
		mDelegate = delegate;
	}

	/** List of cipher suites needed for compatibility with the server. */
	private static final String[]	COMPAT_CIPHER_SUITES	= {"SSL_RSA_WITH_RC4_128_MD5",};

	@Override
	public String[] getDefaultCipherSuites ()
	{
		String[] original = mDelegate.getDefaultCipherSuites ();

		// Add any supported missing cipher suites from COMPAT_CIPHER_SUITES to
		// the
		// end of the list.
		List<String> result = new ArrayList<String> (Arrays.asList (original));
		Set<String> supported = new HashSet<String> (Arrays.asList (getSupportedCipherSuites ()));
		for (String cipherSuite : COMPAT_CIPHER_SUITES)
		{
			if ((!result.contains (cipherSuite)) && (supported.contains (cipherSuite)))
			{
				result.add (cipherSuite);
			}
		}
		if (result.size () == original.length)
		{
			// No changes to the default list
			return original;
		}

		return result.toArray (new String[result.size ()]);
	}

	protected void configureSocket (SSLSocket socket)
	{
		// Uncomment the lines below to modify the list of protocols enabled for
		// this socket.
		// This example enables TLSv1 only.
		// socket.setEnabledProtocols(new String[] {"TLSv1"});

		socket.setEnabledCipherSuites (getDefaultCipherSuites ());
	}

	@Override
	public SSLSocket createSocket (Socket socket, String host, int port, boolean autoClose) throws IOException
	{
		SSLSocket sslSocket = (SSLSocket) mDelegate.createSocket (socket, host, port, autoClose);
		configureSocket (sslSocket);
		return sslSocket;
	}

	@Override
	public SSLSocket createSocket (String host, int port) throws IOException, UnknownHostException
	{
		SSLSocket sslSocket = (SSLSocket) mDelegate.createSocket (host, port);
		configureSocket (sslSocket);
		return sslSocket;
	}

	@Override
	public SSLSocket createSocket (InetAddress host, int port) throws IOException
	{
		SSLSocket sslSocket = (SSLSocket) mDelegate.createSocket (host, port);
		configureSocket (sslSocket);
		return sslSocket;
	}

	@Override
	public SSLSocket createSocket (String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException
	{
		SSLSocket sslSocket = (SSLSocket) mDelegate.createSocket (host, port, localHost, localPort);
		configureSocket (sslSocket);
		return sslSocket;
	}

	@Override
	public SSLSocket createSocket (InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException
	{
		SSLSocket sslSocket = (SSLSocket) mDelegate.createSocket (address, port, localAddress, localPort);
		configureSocket (sslSocket);
		return sslSocket;
	}

	@Override
	public String[] getSupportedCipherSuites ()
	{
		return mDelegate.getSupportedCipherSuites ();
	}
}
