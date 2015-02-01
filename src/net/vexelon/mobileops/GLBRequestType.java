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
	LOGOUT("https://login.telenor.bg/logout", "service=http://www.telenor.bg"),
	/*
	 * Status checks
	 */
	PAGE_BILLCHECK("/mg/myglobul.bch", "action=billcheck"),
	GET_BALANCE("/billing-payment", ""),
	GET_CREDITLIMIT("/loadusagelines", ""),
	/*
	 * Invoice check
	 */
	PAGE_INVOICE("/einv", ""),
	PAGE_INVOICE_EXPORT("/einv-export", ""),
	GET_INVOICE("/st/EI2Export", "")
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
		}
		
		return null;
	}
}
