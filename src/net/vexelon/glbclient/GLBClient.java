package net.vexelon.glbclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import net.vexelon.glbclient.exceptions.GLBHttpException;
import net.vexelon.glbclient.exceptions.GLBInvalidCredentialsException;

import org.apache.http.client.ClientProtocolException;

public interface GLBClient {

	public void login()
		throws Exception;
	
	public void logout()
		throws IOException, ClientProtocolException, GLBHttpException;
	
	public String getCurrentBalance() 
		throws UnsupportedEncodingException, IOException, ClientProtocolException, GLBHttpException;
	
	public String getAvailableMinutes()
		throws UnsupportedEncodingException, IOException, ClientProtocolException, GLBHttpException;
	
	public String getAvailableInternetBandwidth()
		throws UnsupportedEncodingException, IOException, ClientProtocolException, GLBHttpException;
	
	public String getCreditLimit()
		throws UnsupportedEncodingException, IOException, ClientProtocolException, GLBHttpException;
	
	public void close();
}
