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

import java.util.Map;

public interface IClient {

	void login()
		throws HttpClientException, InvalidCredentialsException, SecureCodeRequiredException;
	
	void logout()
		throws HttpClientException;
	
	/**
	 * Get count of downloaded bytes up to this moment
	 * @return
	 */
	long getDownloadedBytesCount();
	
	String getCurrentBalance() 
		throws HttpClientException;
	
	byte[] getInvoiceData()
			throws HttpClientException, InvoiceException;
	
	long getInvoiceDateTime();
	
	Map<String, String> getInvoiceSummary()
			throws HttpClientException, InvoiceException;	
	
	void close();
}
