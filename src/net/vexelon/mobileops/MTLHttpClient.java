package net.vexelon.mobileops;

import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

public class MTLHttpClient implements Client {
	
	private final String HTTP_MYMTEL_SITE = "https://www.mtel.bg/my";
	private final String HTTP_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; ro; rv:1.9.2.8) Gecko/20100722 Firefox/3.6.8";
	private final int DEFAULT_BUFFER_SIZE = 1024;
	private final String RESPONSE_ENCODING = "Windows-1251";
	
	private String username;
	private String password;
	private DefaultHttpClient httpClient = null;
	private CookieStore httpCookieStore = null;

	public MTLHttpClient(String username, String password) {
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
	public String getAvailableInternetBandwidth() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAvailableMSPackage() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAvailableMinutes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCreditLimit() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCurrentBalance() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void login() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout() throws Exception {
		// TODO Auto-generated method stub

	}

}
