/*
 * The MIT License
 *
 * Copyright (c) 2012 Petar Petrov
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
package net.vexelon.myglob.actions;

import java.util.Date;
import java.util.List;

public class ActionResult {
	
	private String stringResult;
//	private int intResult;
	private long downloadedBytesCount;
	private Date checkedOn;
	private List<?> listResult; 

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

}
