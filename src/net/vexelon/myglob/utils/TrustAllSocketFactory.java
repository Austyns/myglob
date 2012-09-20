/*
 * The MIT License
 * 
 * Copyright (c) 2010 Petar Petrov
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.vexelon.myglob.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * <p>
 * Use this socket factory to blindly verify server certificates.
 * <p> 
 * Also includes reverse DNS fix for Android < 4.0
 * Source: http://code.google.com/p/android/issues/detail?id=13117#c14
 * 
 * @author petarov
 *
 */
public final class TrustAllSocketFactory implements LayeredSocketFactory {
	
//	private static TrustAllSocketFactory INSTANCE = new TrustAllSocketFactory();
	private SSLContext sslContext = null;
	private SSLSocketFactory socketFactory = null;
	
//	public TrustAllSocketFactory getSocketFactory() {
//		return INSTANCE;
//	}
	
	public TrustAllSocketFactory() throws InvalidAlgorithmParameterException {
		super();
		
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[] {};
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {
					
				}
			}
		};
		
		SecureRandom secureRND = new SecureRandom();
		
		try {
			sslContext = SSLContext.getInstance(org.apache.http.conn.ssl.SSLSocketFactory.TLS);
			sslContext.init(null, trustAllCerts, secureRND);
		} catch (NoSuchAlgorithmException e) {
			throw new InvalidAlgorithmParameterException("Failed to initlize TLS context!", e);
		} catch (KeyManagementException e) {
			throw new InvalidAlgorithmParameterException("Failed to init SSL context!", e);
		}
		
		socketFactory = sslContext.getSocketFactory();
	}
	
	/**
	 * Prevents the reverse DNS lookup from being made. Fixed in Android 4.0, but still needs
	 * workaround for earlier versions. 
	 * Source: http://code.google.com/p/android/issues/detail?id=13117#c14
	 * @param socket
	 * @param host
	 */
    private void injectHostname(Socket socket, String host) {
        try {
            Field field = InetAddress.class.getDeclaredField("hostName");
            field.setAccessible(true);
            field.set(socket.getInetAddress(), host);
        } catch (Exception ignored) {
        	// uhm
        }
    }	

	@Override
	public Socket connectSocket(Socket sock, String host, int port,
			InetAddress localAddress, int localPort, HttpParams params)
			throws IOException, UnknownHostException, ConnectTimeoutException {
		
		if (host == null) {
			throw new IllegalArgumentException("Target host may not be null.");
		}
		if (params == null) {
			throw new IllegalArgumentException("Parameters may not be null.");
		}

		SSLSocket sslSocket = (SSLSocket) ((sock != null) ? sock : createSocket());

		if ((localAddress != null) || (localPort > 0)) {

			// we need to bind explicitly
			if (localPort < 0) {
				localPort = 0; // indicates "any"
			}

			InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
			sslSocket.bind(isa);
		}

		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);

		InetSocketAddress remoteAddress;
		remoteAddress = new InetSocketAddress(host, port);

		sslSocket.connect(remoteAddress, connTimeout);
		sslSocket.setSoTimeout(soTimeout);

		return sslSocket;
	}

	@Override
	public Socket createSocket() throws IOException {
		return (SSLSocket)socketFactory.createSocket();
	}

	@Override
	public boolean isSecure(Socket sock) throws IllegalArgumentException {
		return true;
	}

	@Override
	public Socket createSocket(Socket socket, String host, int port,
			boolean autoClose) throws IOException, UnknownHostException {
		injectHostname(socket, host); // reverse DNS fix
		SSLSocket sslSocket = (SSLSocket) socketFactory.createSocket(socket, host, port, autoClose);
		return sslSocket;
	}
	
}
