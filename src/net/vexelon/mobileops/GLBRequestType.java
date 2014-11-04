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

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public enum GLBRequestType {
	LOGIN("https://login.telenor.bg/login", "asid=s01&service=https%3A%2F%2Fmy.telenor.bg%2Flogin"),
	LOGIN_ACTION("https://login.telenor.bg/login", ""),
	LOGOUT("/mg/myglobul.portal", "action=logoff"),
	/*
	 * Status checks
	 */
	PAGE_BILLCHECK("/mg/myglobul.bch", "action=billcheck"),
	GET_BALANCE("/loadusagelines", ""),
//	GET_BALANCE("/mg/myglobul.bch", "action=billcheckperform"),
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
