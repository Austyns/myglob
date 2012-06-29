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
package net.vexelon.mobileops;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import net.vexelon.mobileops.exceptions.HttpClientException;
import net.vexelon.mobileops.exceptions.InvalidCredentialsException;
import net.vexelon.mobileops.exceptions.SecureCodeRequiredException;
import net.vexelon.myglob.configuration.Defs;
import net.vexelon.myglob.utils.TrustAllSocketFactory;
import net.vexelon.myglob.utils.UserAgentHelper;
import net.vexelon.myglob.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

public class GLBHttpClient implements Client {
	
	private final String HTTP_MYGLOBUL_SITE = "https://my.globul.bg";
	
	private final int DEFAULT_BUFFER_SIZE = 1024;
	private final String RESPONSE_ENCODING = "Windows-1251";
	private final int MAX_REQUEST_RETRIES = 2;
	
	private String username;
	private String password;
	private DefaultHttpClient httpClient = null;
	private CookieStore httpCookieStore = null;
	
	public GLBHttpClient(String username, String password) {
		this.username = username;
		this.password = password;
		
		initHttpClient();
	}
	
	public void close() {
		if ( httpClient != null )
			httpClient.getConnectionManager().shutdown();
	}

	/**
	 * Perform login into the web system using the specified user and password
	 */
	public void login() 
		throws Exception {
		
		List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair("action", "loginexec"));
		qparams.add(new BasicNameValuePair("continuation", 
				URLDecoder.decode("myglobul.portal%3Faction%3Duserhome%26pkey%3D0%26jkey%3D0", 
						"UTF-8")));			
		qparams.add(new BasicNameValuePair("image.x", Integer.toString(Utils.getRandomInt(1024)) ));
		qparams.add(new BasicNameValuePair("image.y", Integer.toString(Utils.getRandomInt(768)) ));
		qparams.add(new BasicNameValuePair("password", password));
		//qparams.add(new BasicNameValuePair("refid", ""));
		qparams.add(new BasicNameValuePair("username", username));
		
		handleLogin(qparams);
	}
	
	public void logout() 
		throws IOException, ClientProtocolException, HttpClientException {
		
		StringBuilder fullUrl = new StringBuilder(100);
		fullUrl.append(HTTP_MYGLOBUL_SITE).append(GLBRequestType.LOGOUT.getPath()).append("?")
			.append(GLBRequestType.LOGOUT.getParams());
		
		HttpGet httpGet = new HttpGet(fullUrl.toString());
		HttpResponse resp = httpClient.execute(httpGet);
		StatusLine status = resp.getStatusLine();
		
		if ( status.getStatusCode() != HttpStatus.SC_OK )
			throw new HttpClientException(status.getReasonPhrase(), status.getStatusCode());
		
		resp.getEntity().consumeContent();		
	}
	
	public String getCurrentBalance() 
		throws Exception {
		
		return doPostRequest(GLBRequestType.GET_BALANCE);
	}
	
	public String getAvailableMinutes()
		throws Exception {
		
		return doPostRequest(GLBRequestType.GET_MINUTES);
	}
	
	public String getAvailableInternetBandwidth()
		throws Exception {
		
		return doPostRequest(GLBRequestType.GET_BANDWIDTH);	
	}
	
	public String getCreditLimit()
		throws Exception {
		
		return doPostRequest(GLBRequestType.GET_CREDITLIMIT);		
	}
	
	public String getAvailableMSPackage()
		throws Exception {
		
		return doPostRequest(GLBRequestType.GET_MSPACKAGE);
	}
	
	// ---
	
	/**
	 * Initialize Http Client
	 */
	private void initHttpClient() {
		
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		params.setParameter(CoreProtocolPNames.USER_AGENT, UserAgentHelper.getRandomUserAgent());
		//params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);

		// Bugfix #1: The target server failed to respond
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);
		
		DefaultHttpClient client = new DefaultHttpClient(params);
		httpCookieStore = new BasicCookieStore();
		client.setCookieStore(httpCookieStore);

		// Bugfix #1: Adding retry handler to repeat failed requests
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
			
			@Override
			public boolean retryRequest(IOException exception, int executionCount,
					HttpContext context) {
				
				if (executionCount >= MAX_REQUEST_RETRIES) {
					return false;
				}
				
				if (exception instanceof NoHttpResponseException || exception instanceof ClientProtocolException) {
					return true;
				}
				
				return false;
			}
		};
		client.setHttpRequestRetryHandler(retryHandler);
		
		// SSL
		HostnameVerifier verifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		try {
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", new PlainSocketFactory(), 80));
			registry.register(new Scheme("https", new TrustAllSocketFactory(), 443));
	
			SingleClientConnManager connMgr = new SingleClientConnManager(
					client.getParams(), registry);
			
			httpClient = new DefaultHttpClient(connMgr, client.getParams());
		}
		catch (InvalidAlgorithmParameterException e) {
			Log.e(Defs.LOG_TAG, "", e);
			
			// init without connection manager
			httpClient = new DefaultHttpClient(client.getParams());
		}
		
		HttpsURLConnection.setDefaultHostnameVerifier(verifier);

	}
	
	/**
	 * Login logic
	 * @param qparams
	 */
	private void handleLogin(List<NameValuePair> qparams) 
		throws Exception {
		
		StringBuilder fullUrl = new StringBuilder(100);
		fullUrl.append(HTTP_MYGLOBUL_SITE).append(GLBRequestType.LOGIN.getPath());
		
		HttpPost httpPost = createPostRequest(fullUrl.toString(), qparams);
		
//		rametersapplication/x-www-form-urlencoded
//		action	
//		continuation	myglobul.portal%3Faction%3Duserhome%26pkey%3D0%26jkey%3D0
//		image.x	0
//		image.y	0
//		imglogin	JFNH
//		password	mypassword
//		refid		0a7b8fa558b46142e2d400ae0f2548f2f
//		username	0899123456		

		HttpResponse resp = httpClient.execute(httpPost);
		StatusLine status = resp.getStatusLine();
		
		if ( status.getStatusCode() == HttpStatus.SC_OK ) {
			
			// retrieve contents of the reply
			HttpEntity entity = resp.getEntity();
			ByteArrayOutputStream baos = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
			entity.writeTo(baos);
			baos.close();
			
			String content = baos.toString();
			
			// if the username is not present in the content, then we're obviously not logged in
			if ( content.indexOf("action=pdetails") == -1 || content.indexOf("action=chpass") == -1 ) {
				//check if secure code image is sent
				if ( content.indexOf("/mg/my/GetImage?refid=") != -1 ) {
					//TODO: retrieve image url
					//<img class="code" alt="Ако се затруднявате с разчитането на кода от картинката, моля кликнете 
					//върху нея за да я смените." src="/mg/my/GetImage?refid=b7b8fa558b461f1e2d400ae0f3348f2f">
					throw new SecureCodeRequiredException("");
				}
				
				throw new InvalidCredentialsException();
			}
		}
		else if ( status.getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY ) {
			
			resp.getEntity().consumeContent();
			
			//NOTE: Kind of a hack (sometimes we get 302 from the web serv), 
			//      May not work if Globul changes impl.
			throw new HttpClientException(status.getReasonPhrase(), status.getStatusCode());
		}		
	}
	
	private HttpPost createPostRequest(String url, List<NameValuePair> qparams) 
		throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(qparams, HTTP.UTF_8));
		return httpPost;
	}
	
	private String doPostRequest(GLBRequestType requestType) throws UnsupportedEncodingException,
			IOException, ClientProtocolException, HttpClientException {

		HttpPost httpPost = createPostRequest(HTTP_MYGLOBUL_SITE + requestType.getPath(), 
				requestType.getParamsAsList());
		httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		httpPost.setHeader("X-Prototype-Version", "1.6.0.2");

		HttpResponse resp = httpClient.execute(httpPost);
		StatusLine status = resp.getStatusLine();

		if (status.getStatusCode() != HttpStatus.SC_OK)
			throw new HttpClientException(status.getReasonPhrase(), status.getStatusCode());

		HttpEntity entity = resp.getEntity();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
		entity.writeTo(baos);
		baos.close();

		return new String(baos.toByteArray(), RESPONSE_ENCODING);
		//return baos.toString();
	}
	
}
