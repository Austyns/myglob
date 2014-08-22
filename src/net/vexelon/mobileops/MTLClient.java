/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2010 Petar Petrov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.vexelon.mobileops;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

public class MTLClient implements IClient {
	
	private final String HTTP_MYMTEL_SITE = "https://www.mtel.bg/my";
	private final String HTTP_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; ro; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8";
	private final int DEFAULT_BUFFER_SIZE = 1024;
	private final String RESPONSE_ENCODING = "Windows-1251";
	
	private String username;
	private String password;
	private DefaultHttpClient httpClient = null;
	private CookieStore httpCookieStore = null;

	public MTLClient(String username, String password) {
		this.username = username;
		this.password = password;
		
		initHttpClient();
	}
	
	/**
	 * Initialize Http Client
	 */
	private void initHttpClient() {
		
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.TRUE);
		params.setParameter(CoreProtocolPNames.USER_AGENT, HTTP_USER_AGENT);
		//params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
		httpClient = new DefaultHttpClient(params);
		
		httpCookieStore = new BasicCookieStore();
		httpClient.setCookieStore(httpCookieStore);
	}	

	@Override
	public void close() {
		if ( httpClient != null )
			httpClient.getConnectionManager().shutdown();
	}
	
	@Override
	public long getDownloadedBytesCount() {
		// TODO Auto-generated method stub
		return 0;
	}	

	@Override
	public String getAvailableInternetBandwidth() throws HttpClientException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getTravelAndSurfBandwidth() throws HttpClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAvailableMSPackage() throws HttpClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAvailableMinutes() throws HttpClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCreditLimit() throws HttpClientException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentBalance() throws HttpClientException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public byte[] getInvoiceData()
			throws HttpClientException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public long getInvoiceDateTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void login() throws HttpClientException, InvalidCredentialsException, SecureCodeRequiredException {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() throws HttpClientException {
		// TODO Auto-generated method stub

	}

}
