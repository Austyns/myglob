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
		throws Exception;
	
	public String getAvailableMinutes()
		throws Exception;
	
	public String getAvailableInternetBandwidth()
		throws Exception;
	
	public String getCreditLimit()
		throws Exception;

	public String getAvailableMSPackage()
		throws Exception;
	
	public void close();
}
