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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public enum GLBRequestType {
	LOGIN("https://login.globul.bg/login", "service=https%3A%2F%2Fmy.globul.bg%2Fmg%2Fmyglobul.portal%3Faction%3Duserhome%26pkey%3D0%26jkey%3D0"),
	LOGIN_ACTION("https://login.globul.bg/login", ""),
	LOGOUT("/mg/myglobul.portal", "action=logoff"),
	/*
	 * Status checks
	 */
	PAGE_BILLCHECK("/mg/myglobul.bch", "action=billcheck"),
	GET_BALANCE("/mg/myglobul.bch", "action=billcheckperform"),
	GET_MINUTES("/mg/myglobul.bch", "action=bundlecheckperform"),
	GET_BANDWIDTH("/mg/myglobul.bch", "action=gprscheckperform"),
	GET_TRAVELNSURF("/mg/myglobul.bch", "action=drpCheckPerform"),
	GET_CREDITLIMIT("/mg/myglobul.bch", "action=creditlimitcheckperform"),
	GET_MSPACKAGE("/mg/myglobul.bch", "action=smsmmsCheckPerform"),
	/*
	 * Invoice check
	 */
	PAGE_INVOICE("/mg/myglobul.e2i", "action=invoice&pkey=217&jkey=219&brand=&model=0"),
	PAGE_INVOICE_EXPORT("/mg/myglobul.e2i", "action=export&pkey=217&jkey=225&brand=&model=0"),
	GET_INVOICE("mg/ei2/EI2Export", "")
	;
	
	private final String path;
	private final String params;
	private List<NameValuePair> list ;
	
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
		if (list == null) {
			// split
			list = new ArrayList<NameValuePair>();
			String[] pairs = this.params.split("&");
			for (String pair : pairs) {
				String[] keys = pair.split("=");
				list.add(new BasicNameValuePair(keys[0], keys[1] != null ? keys[1] : ""));
			}			
		}
		
		return new ArrayList<NameValuePair>(list);
	}

	/**
	 * Return request type from action keyword
	 * @param action
	 * @return <code>GLBRequestType</code> or <code>null</code>
	 */
	public static GLBRequestType getFromAction(String action) {
		String full = "action=" + action;
		if (GET_BALANCE.params.equals(full)) {
			return GET_BALANCE;
		} else if (GET_MINUTES.params.equals(full)) {
			return GET_MINUTES;
		} else if (GET_BANDWIDTH.params.equals(full)) {
			return GET_BANDWIDTH;
		} else if (GET_CREDITLIMIT.params.equals(full)) {
			return GET_CREDITLIMIT;
		} else if (GET_MSPACKAGE.params.equals(full)) {
			return GET_MSPACKAGE;
		} else if (GET_TRAVELNSURF.params.equals(full)) {
			return GET_TRAVELNSURF;
		}
		
		return null;
	}
}
