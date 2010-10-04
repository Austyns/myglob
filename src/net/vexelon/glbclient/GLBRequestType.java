package net.vexelon.glbclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public enum GLBRequestType {
	
	LOGIN("/mg/myglobul.portal", ""),
	GET_BALANCE("/mg/myglobul.bch", "action=billcheckperform"),
	GET_MINUTES("/mg/myglobul.bch", "action=bundlecheckperform"),
	GET_BANDWIDTH("/mg/myglobul.bch", "action=gprscheckperform"),
	GET_CREDITLIMIT("/mg/myglobul.bch", "action=creditlimitcheckperform"),
	LOGOUT("/mg/myglobul.portal", "action=logout");

	private final String path;
	private final String params;
	
	GLBRequestType(String path, String params) {
		this.path = path;
		this.params = params;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getParams() {
		return this.params;
	}
	
	public List<NameValuePair> getParamsAsList() {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		String[] pairs = params.split("&");
		for (String pair : pairs) {
			String[] keys = params.split("=");
			
			list.add( new BasicNameValuePair(keys[0], keys[1] != null ? keys[1] : "")  );
		}
		
		return list;
	}

}
