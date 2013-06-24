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

import java.util.List;
import java.util.Map;

/**
 * Client interface
 * 
 * @author p.petrov
 *
 */
public interface IClient {

	public void login()
		throws HttpClientException, InvalidCredentialsException, SecureCodeRequiredException;
	
	public void logout()
		throws HttpClientException;
	
	/**
	 * Get count of downloaded bytes up to this moment
	 * @return
	 */
	public long getDownloadedBytesCount();
	
	public String getCurrentBalance() 
		throws HttpClientException;
	
	public String getAvailableMinutes()
		throws HttpClientException;
	
	public String getAvailableInternetBandwidth()
		throws HttpClientException;
	
	public String getTravelAndSurfBandwidth() 
			throws HttpClientException;	
	
	public String getCreditLimit()
		throws HttpClientException;

	public String getAvailableMSPackage()
		throws HttpClientException;
	
	public List<Map<String, String>> getInvoiceInfo()
			throws HttpClientException;
	
	public void close();
}
