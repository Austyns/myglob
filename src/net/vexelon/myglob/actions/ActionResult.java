/*
 * MyGlob Android Application
 * 
 * Copyright (C) 2012 Petar Petrov
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
package net.vexelon.myglob.actions;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class ActionResult {
	
	private String stringResult;
//	private int intResult;
	private long downloadedBytesCount;
	private Date checkedOn;
	private List<?> listResult;
	private JSONObject json;

	public String getString() {
		return stringResult;
	}
	
//	public int getInt() {
//		return intResult;
//	}	

	public void setResult(String stringResult) {
		this.stringResult = stringResult;
	}
	
	public long getBytesCount() {
		return downloadedBytesCount;
	}

	public void setBytesCount(long bytesFetched) {
		this.downloadedBytesCount = bytesFetched;
	}

	public Date getCheckedOn() {
		return checkedOn;
	}

	public void setCheckedOn(Date checkedOn) {
		this.checkedOn = checkedOn;
	}	
	
//	public void setResult(int intResult) {
//		this.intResult = intResult;
//	}	
	
	public List<?> getListResult() {
		return listResult;
	}

	public void setResult(List<?> listResult) {
		this.listResult = listResult;
	}
	
	public void setResult(JSONObject json) {
		this.json = json;
	}
	
	public JSONObject getJsonResult() {
		return json;
	}
	
}
