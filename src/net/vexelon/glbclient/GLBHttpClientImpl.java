package net.vexelon.glbclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import net.vexelon.glbclient.exceptions.GLBHttpException;
import net.vexelon.glbclient.exceptions.GLBSecureCodeRequiredException;
import net.vexelon.glbclient.exceptions.GLBInvalidCredentialsException;
import net.vexelon.myglob.Defs;
import net.vexelon.myglob.Utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Entity;

import android.util.Log;
import android.webkit.URLUtil;

public class GLBHttpClientImpl implements GLBClient {
	
	private final String HTTP_MYGLOBUL_SITE = "https://my.globul.bg";
	private final String HTTP_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; ro; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8";
	//private final String HTTP_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; ro; rv:2.1.0.8) Gecko/20090122 Firefox/3.5.2";
	private final int DEFAULT_BUFFER_SIZE = 1024;
	private final String RESPONSE_ENCODING = "Windows-1251";
	
	private String username;
	private String password;
	private DefaultHttpClient httpClient = null;
	private CookieStore httpCookieStore = null;
	
	public GLBHttpClientImpl(String username, String password) {
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
		qparams.add(new BasicNameValuePair("continuation", URLDecoder.decode("myglobul.portal%3Faction%3Duserhome%26pkey%3D0%26jkey%3D0", "UTF-8")));			
		qparams.add(new BasicNameValuePair("image.x", Integer.toString(Utils.getRandomInt(1024)) ));
		qparams.add(new BasicNameValuePair("image.y", Integer.toString(Utils.getRandomInt(768)) ));
		qparams.add(new BasicNameValuePair("password", password));
		//qparams.add(new BasicNameValuePair("refid", ""));
		qparams.add(new BasicNameValuePair("username", username));
		
		handleLogin(qparams);
	}
	
	public void logout() 
		throws IOException, ClientProtocolException, GLBHttpException {
		
		HttpGet httpGet = new HttpGet(HTTP_MYGLOBUL_SITE + GLBRequestType.LOGOUT.getPath() + "?" + GLBRequestType.LOGOUT.getParams());

		HttpResponse resp = httpClient.execute(httpGet);
		StatusLine status = resp.getStatusLine();
		
		if ( status.getStatusCode() != HttpStatus.SC_OK )
			throw new GLBHttpException(status.getReasonPhrase(), status.getStatusCode());
		
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
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.TRUE);
		params.setParameter(CoreProtocolPNames.USER_AGENT, HTTP_USER_AGENT);
		//params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, HTTP.UTF_8);
		httpClient = new DefaultHttpClient(params);
		
		httpCookieStore = new BasicCookieStore();
		httpClient.setCookieStore(httpCookieStore);
	}
	
	/**
	 * Login logic
	 * @param qparams
	 */
	private void handleLogin(List<NameValuePair> qparams) 
		throws Exception {
		
		HttpPost httpPost = createPostRequest(HTTP_MYGLOBUL_SITE + GLBRequestType.LOGIN.getPath(), qparams);
		
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
					//<img class="code" alt="Ако се затруднявате с разчитането на кода от картинката, моля кликнете върху нея за да я смените." src="/mg/my/GetImage?refid=b7b8fa558b461f1e2d400ae0f3348f2f">
					throw new GLBSecureCodeRequiredException("");
				}
				
				throw new GLBInvalidCredentialsException();
			}
		}
		else if ( status.getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY ) {
			
			resp.getEntity().consumeContent();
			
			//NOTE: Kind of a hack (sometimes we get 302 from the web serv), 
			//      May not work if Globul changes impl.
			throw new GLBHttpException(status.getReasonPhrase(), status.getStatusCode());
		}		
	}
	
	private HttpPost createPostRequest(String url, List<NameValuePair> qparams) 
		throws UnsupportedEncodingException {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(qparams, HTTP.UTF_8));
		return httpPost;
	}
	
	private String doPostRequest(GLBRequestType requestType) throws UnsupportedEncodingException,
			IOException, ClientProtocolException, GLBHttpException {

		HttpPost httpPost = createPostRequest(HTTP_MYGLOBUL_SITE + requestType.getPath(), requestType.getParamsAsList());
		httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
		httpPost.setHeader("X-Prototype-Version", "1.6.0.2");

		HttpResponse resp = httpClient.execute(httpPost);
		StatusLine status = resp.getStatusLine();

		if (status.getStatusCode() != HttpStatus.SC_OK)
			throw new GLBHttpException(status.getReasonPhrase(), status.getStatusCode());

		HttpEntity entity = resp.getEntity();
		ByteArrayOutputStream baos = new ByteArrayOutputStream(DEFAULT_BUFFER_SIZE);
		entity.writeTo(baos);
		baos.close();

		return new String(baos.toByteArray(), RESPONSE_ENCODING);
		//return baos.toString();
	}
	
}
